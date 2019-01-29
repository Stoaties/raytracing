/**
 * 
 */
package sim.geometry;

import java.lang.Comparable;

import sim.exception.SConstructorException;
import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.math.SAffineTransformation;
import sim.math.SMath;
import sim.math.SMatrix4x4;
import sim.math.SVector3d;
import sim.math.SVectorUV;

/**
 * <p>
 * La classe <b>SRay</b> représente un rayon pouvant réaliser une intersection avec une géométrie.
 * Un rayon est un objet <u>immuable</u> qui peut être dans un étant <b>intersecté</b> ou <b>non intersecté</b>.
 * </p>
 * 
 * <p>
 * Lorsqu'un rayon est dans un étant <b>intersecté</b>, le rayon donne accès à la géométrie intersectée,
 * qui elle donne accès à une primitive qui donne finalement accès au matériel appliqué sur la géométrie.
 * C'est le paramètre <i>t</i> représentant le temps pour réaliser l'intersection qui permet d'ordonner la visibilité des géométries de la scène.
 * </p>
 * 
 * <p>
 * Cette classe implémente l'interface <b>Comparable</b> (permettant les usages des méthodes de triage des librairies de base de java). 
 * La comparaison sera effectuée sur la <b>valeur du champ local t</b> qui représente le <b>temps afin d'intersecter une géométrie</b>. 
 * S'il n'y a <b>pas d'intersection</b>, la valeur de t sera égale à <b>l'infini</b>.
 * </p> 
 *
 * @author Simon Vézina
 * @since 2014-12-30
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public class SRay implements Comparable<SRay> {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>MINIMUM_EPSILON</b> correspond à <b>la plus petite valeur du temps mininal de parcours</b> qu'un rayon doit effectuer avant de pouvoir intersecter une géométrie. 
   * Cette contrainte est nécessaire pour une raison de stabilité numérique. Elle est présentement définie à {@value} elle et doit <b>toujours être supérieure à SMath.EPSILON</b>.
   */
  public static final double MINIMUM_EPSILON = 1e-8;	
	
  /**
   * La constante <b>MAXIMAM_T</b> correspond à un temps signifiant que le rayon n'a pas réussi à intersecter de géométrie. 
   * Cette valeur correspond à un <b>temps infini</b>.
   */
	private static final double MAXIMUM_T = SMath.INFINITY;	
	
	/**
	 * La constante <b>DEFAULT_REFRACTIV_INDEX</b> correspond à la valeur de l'indice de réfraction par défaut où un rayon voyage.
	 * Cet indice de réfraction est égal à celui du vide (n = {@value}).
	 */
	public static final double DEFAULT_REFRACTIVE_INDEX = 1.0;			  
	
  //-----------------------
  // VARIABLES STATIQUES //
  //-----------------------
	
	/**
   * La variable <b>epsilon</b> correspond au temps mininal de parcours qu'un rayon doit effectuer avant de pouvoir intersecter une géométrie.
   */
  private static double epsilon = MINIMUM_EPSILON;    
  
  //-------------
  // VARIABLES //
  //-------------
  
	/**
	 * La variable <b>origin</b> correspond à l'origine du rayon (d'où est lancé le rayon).
	 */
	private final SVector3d origin;		  
	
	/**
	 * La variable <b>direction</b> correspond à la direction du rayon (dans quelle direction le rayon voyagera).
	 * On peut également comparer cette variable à la vitesse du rayon.
	 */
	private final SVector3d direction;	
	
	/**
	 * La variable <b>as_intersected</b> détermine si le rayon a déjà réalisé une intersection.
	 */
	private final boolean as_intersected;	         
	
	/**
	 * La varibale <b>geometry</b> correspond à la géométrie intersecté par le rayon.
	 */
	private final SGeometry geometry;
	
	/**
	 * La variable <b>normal</b> correspond à la normale à la surface sur la géométrie intersectée à l'endroit où l'intersection a été réalisée.
	 * Cette normale correspond à la <u>normale extérieure</u> à la géométrie. 
	 */
	private final SVector3d normal;			     
	
	/**
	 * La variable <b>uv</b> correspond à la coordonnée <i>uv</i> de texture associée à la géométrie intersectée par le rayon.
	 */
	private final SVectorUV uv;          
	
	/**
	 * La variable <b>int_t</b> correspond au temps requis au rayon pour intersecter la géometrie.
	 */
	private final double int_t;					         
	
	/**
	 * La variable <b>refractive_index</b> correspond à l'indice de réfraction du milieu où voyage le rayon.
	 */
	private final double refractive_index;
	
	/**
	 * La variable <b>previous_ray</b> correspond au rayon précédent au rayon présent dans la <i>hiérarchie récursive</i> du processus de lancé de rayon.
	 */
	private final SRay previous_ray;
	
	//-----------------------
	// CONSTRUCTEUR PUBLIC //
	//-----------------------
	
	/**
	 * Constructeur d'un rayon dont l'objectif sera de tenter d'intersecter une géométrie.
	 * 
	 * @param origin L'origine du rayon.
	 * @param direction La direction du rayon.
	 * @param refractive_index L'indice de réfraction du milieu où voyage le rayon.
	 * @throws SConstructorException Si la direction du rayon correspond à un vecteur nul (pas d'orientation).
	 */
	public SRay(SVector3d origin, SVector3d direction, double refractive_index) throws SConstructorException
	{
	  this(origin, direction, refractive_index, null);
	}
	
	//----------------------
	// CONSTRUCTEUR PRIVÉ //
	//----------------------
	
	/**
	 * Constructeur d'un rayon dont l'objectif sera de tenter d'intersection une géométrie.
	 * Ce rayon possède cependant un rayon parent précédent définissant l'ordre récursif du lancé des rayons.
	 * 
	 * @param origin L'origine du rayon.
   * @param direction La direction du rayon.
   * @param refractive_index L'indice de réfraction du milieu où voyage le rayon.
   * @param previous_ray Le rayon précédent dans la hiérarchie récursive du lancé des rayons.
   * @throws SConstructorException Si un paramètre est invalide lors de la construction du rayon.
	 */
	private SRay(SVector3d origin, SVector3d direction, double refractive_index, SRay previous_ray) throws SConstructorException
	{
	  // Vérifier que nous n'avons pas de valeur nulle pour l'origine et l'orientation.
	  if(origin == null || direction == null)
	    throw new SConstructorException("Erreur SRay 001 : Le rayon est mal défini, car origin = " + origin + " et direction = " + direction + " .");
    
	  // Vérification de l'orientation du rayon.
    if(direction.modulus() < SMath.EPSILON)
      throw new SConstructorException("Erreur SRay 001 : La direction du rayon égale à " + direction + " possède un module inférieur à " + SMath.EPSILON + " ce qui ne peut pas lui octroyer d'orientation.");
    
    this.origin = origin;
    this.direction = direction;
    
    this.as_intersected = false;
    
    this.geometry = null;
    this.normal = null;
    this.uv = null;
    this.int_t = MAXIMUM_T;   
    
    this.refractive_index = refractive_index;
    
    this.previous_ray = previous_ray;
	}
	
	/**
	 * Constructeur d'un rayon ayant réalisé une intersection avec une géométrie (sans coordonnée de texture uv).
	 * 
	 * @param origin L'origine du rayon.
	 * @param direction La direction du rayon.
	 * @param geometry La géométrie intersectée.
	 * @param normal L'orientation de la normale à la surface.
	 * @param t Le temps pour le rayon afin d'atteindre la géométrie.
	 * @param refractive_index L'indice de réfraction du milieu où voyage le rayon.
	 * @param previous_ray Le rayon précédent dans la hiérarchie récursive du lancé des rayons.
	 * @throws SConstructorException Si le temps du rayon est inférieur à <i>epsilon</i>.
	 * @throws SConstructorException Si un paramètre est invalide lors de la construction du rayon.
	 */
	private SRay(SVector3d origin, SVector3d direction, SGeometry geometry, SVector3d normal, double t, double refractive_index, SRay previous_ray) throws SConstructorException
	{
	  this(origin, direction, geometry, normal, null, t, refractive_index, previous_ray);
	}
	
	/**
   * Constructeur d'un rayon ayant réalisé une intersection avec une géométrie avec coordonnée de testure uv.
   * 
   * @param origin L'origine du rayon.
   * @param direction La direction du rayon.
   * @param geometry La géométrie intersectée.
   * @param normal L'orientation de la normale à la surface.
   * @param uv La coordonnée de texture. Une valeur <b>null</b> signifie qu'il n'y aura pas de coordonnée d'attribuée.
   * @param t Le temps pour le rayon afin d'atteindre la géométrie.
   * @param refractive_index L'indice de réfraction du milieu où voyage le rayon.
   * @param previous_ray Le rayon précédent dans la hiérarchie récursive du lancé des rayons.
   * @throws SConstructorException Si le temps du rayon est inférieur à <i>epsilon</i>.
   * @throws SConstructorException Si un paramètre est invalide lors de la construction du rayon.
   */
  private SRay(SVector3d origin, SVector3d direction, SGeometry geometry, SVector3d normal, SVectorUV uv, double t, double refractive_index, SRay previous_ray) throws SConstructorException
  {
    // Vérifier que nous n'avons pas de valeur nulle pour l'origine et l'orientation.
    if(origin == null || direction == null || normal == null)
      throw new SConstructorException("Erreur SRay 001 : Le rayon est mal défini, car origin = " + origin + ", direction = " + direction + " et normal = " + normal + " .");
  
    // Vérification que la géométrie n'est pas NULL.
    if(geometry == null)
      throw new SConstructorException("Erreur SRay 003 : La géométrie intersectée ne peut pas être 'null'.");
  
    // Vérification pour teste numérique
    if(t < epsilon)
      throw new SConstructorException("Erreur SRay 002 : Pour des raisons numériques, le temps de l'intersection t = " + t + " ne peut pas être inférieur à epsilon = " + epsilon + " ou négatif.");
    
    // Vérification de l'orientation du rayon
    if(direction.modulus() < SMath.EPSILON)
      throw new SConstructorException("Erreur SRay 003 : La direction du rayon égale à " + direction + " possède un module inférieur à " + SMath.EPSILON + " ce qui ne peut pas lui octroyer d'orientation.");
    
    this.origin = origin;
    this.direction = direction;
    this.geometry = geometry;
    this.normal = normal;
    this.int_t = t;
    this.as_intersected = true;
    this.refractive_index = refractive_index;
    this.previous_ray = previous_ray;
    
    // Le paramètre uv peut être 'null'. Sous cette condition, il n'y aura pas de coordonnée uv d'affectable à l'intersection.
    this.uv = uv;    
  }
  
  //------------
  // MÉTHODES //
  //------------
  
	/**
	 * Méthode pour définir le temps/distance minimal que doit effectuer un rayon afin de valider une intersection.
	 * Cette valeur est nécessaire pour des raisons numériques. Elle doit être positive et supérieure à un valeur minimal de 1e-6. 
	 * Il peut être affecté en fonction de la position du front clipping plane d'une pyramide de vue (view Frustum).
	 * @param e - La valeur d'epsilon.
	 * @throws SRuntimeException Si la nouvelle valeur d'epsilon est inférieure à la valeur minimale acceptée par la classe.
	 * @see MINIMUM_EPSILON
	 */
	public static void setEpsilon(double e)throws SRuntimeException
	{ 	
		//Mettre la valeur d'epsilon positive
		if(e < MINIMUM_EPSILON)
		  throw new SRuntimeException("Erreur SRay 004 : Pour des raisons numériques, la nouvelle valeur d'epsilon '" + e + "' doit être supérieure à " + MINIMUM_EPSILON + ".");
			
		epsilon = e;
	}
	
	/**
	 * Méthode pour obtenir la valeur d'epsilon présentement en vigueur pour les calculs d'intersection entre les rayons et les géométries.
	 * @return La valeur d'epsilon.
	 */
	public static double getEpsilon()
	{ 
	  return epsilon;
	}
	
	/**
	 * Méthode pour obtenir l'origine du rayon.
	 * @return L'origine du rayon.
	 */
	public SVector3d getOrigin()
	{ 
	  return origin;
	}
	
	/**
	 * Méthode pour obtenir la direction du rayon.
	 * @return La direction du rayon.
	 */
	public SVector3d getDirection()
	{ 
	  return direction; 
	}
		
	/**
	 * Méthode pour obtenir la position d'un rayon après un déplacement de celui-ci d'un temps <i>t</i>.
	 * @param t Le temps à écouler dans le déplacement du rayon.
	 * @return Le vecteur position du rayon après déplacement.
	 */
	public SVector3d getPosition(double t)
	{
		return origin.add(direction.multiply(t));
		//throw new SNoImplementationException("La méthode n'a pas été implémentée");
	}
	
	/**
	 * Méthode pour déterminer si le rayon a effectué une intersection avec une géométrie.
	 * @return <b>true</b> s'il y a eu une intersection et <b>false</b> sinon. 
	 */
	public boolean asIntersected()
	{ 
	  return as_intersected;
	}
	
	/**
	 * Méthode pour déterminer si le rayon courant possède hiérarchiquement un rayon avant lui.
	 * 
	 * @return <b>true</b> si un rayon a été lancé hiérarchiement avant celui-ci et <b>false</b> sinon.
	 */
	public boolean asPreviousRay()
	{
	  return previous_ray != null;
	}
	
	/**
	 * Méthode pour déterminer si le rayon a effectué une intersection de l'intérieur de la géométrie.
	 * <p> 
	 * Cette méthode ne fait que comparer la direction du rayon avec la normale à la surface extérieur de la géométrie.
	 * Même si la géométrie n'est pas fermée (donc pas d'intérieur), cette méthode peut quand même retourner <b>true</b>. 
	 * @return <b>true</b> s'il y a eu une intersection à l'intérieur de la géométrie et <b>false</b> sinon. 
	 * @throws SNotIntersectedRayException Si le rayon n'a pas effectué d'intersection.
	 */
	public boolean isInsideIntersection() throws SNotIntersectedRayException
	{
	  if(!as_intersected)
	    throw new SNotIntersectedRayException("Erreur SRay 006 : Le rayon n'a pas effectué d'intersection avec une géométrie.");
	  
	  // Afin de déterminer si une intersection est réalisée par l'intérieur de la géométrie,
	  // nous comparons l'orientation du rayon avec la normal (dite extérieure).
	  // Si les orientations sont identique (produit scalaire positif),
	  // alors l'intersection est intérieur.
	  return direction.dot(normal) > 0;
	}
		
	/**
	 * Méthode pour obtenir le temps (ou la distance puisque la direction est unitaire) afin de réaliser 
	 * une intersection entre le rayon et une géométrie. Si le rayon n'a pas été intersecté, la valeur sera <b> l'infini </b>.
	 * @return Le temps pour effectuer l'intersection (s'il y a intersection) et <b>l'infini</b> s'il n'y a <b>pas eu d'intersection</b>.
	 */
	public double getT()
	{ 
	  return int_t;
	}
		
	/**
	 * Méthode pour obtenir l'indice de réfraction du milieu dans lequel le rayon voyage.
	 * @return l'indice de réfraction du milieu où voyage le rayon.
	 */
	public double getRefractiveIndex()
	{ 
	  return refractive_index;
	}
	
	/**
	 * Méthode pour obtenir la position de l'intersection entre le rayon et la géométrie.
	 * @return La position de l'intersection
	 * @throws SNotIntersectedRayException S'il n'y a pas eu d'intersection avec ce rayon, le point d'intersection est indéterminé.
	 */
	public SVector3d getIntersectionPosition() throws SNotIntersectedRayException
	{
		if(!as_intersected)
		  throw new SNotIntersectedRayException("Erreur SRay 006 : Le rayon n'a pas effectué d'intersection avec une géométrie.");

	  // S'il y a intersection, la valeur de "t" correspond au temps de l'intersection 
	  return getPosition(int_t);
	}
	
	/**
	 * Méthode pour obtenir la géométrie qui est en intersection avec le rayon.
	 * @return La géométrie en intersection.
	 * @throws SNotIntersectedRayException S'il n'y a pas eu d'intersection avec ce rayon, la géométrie est indéterminée.
	 */
	public SGeometry getGeometry() throws SNotIntersectedRayException
	{
		if(!as_intersected)
		  throw new SNotIntersectedRayException("Erreur SRay 006 : Le rayon n'a pas effectué d'intersection avec une géométrie.");
	  
		return geometry;
	}
	
	/**
	 * Méthode pour obtenir la normale à la surface de la géométrie en intersection avec le rayon.
	 * Cette normale sera orientée dans le <u>sens opposé à la direction du rayon</u>.
	 * <p>
	 * Cette définition de la normale permet d'évaluer le bon côté de la surface de la géométrie lorsqu'il est temps d'évaluer l'illumination (shading).
	 * </p>
	 * @return La normale à la surface à l'endroit de l'intersection.
	 * @throws SNotIntersectedRayException S'il n'y a pas eu d'intersection avec ce rayon, la normale est indéterminée.
	 */
	public SVector3d getShadingNormal() throws SNotIntersectedRayException
	{
		if(!as_intersected)
		  throw new SNotIntersectedRayException("Erreur SRay 007 : Le rayon n'a pas effectué d'intersection avec une géométrie.");
	  		
		// Comparer l'orientation de la normale à la surface avec l'orientation du rayon.
		// Retourner l'orientation tel que la normale est dans le sens opposé à la direction du rayon.
		if(direction.dot(normal) < 0.0)
      return normal;                            // orientation opposée, donc intersection venant de l'extérieur
    else
      return normal.multiply(-1.0);             // orientation dans le même sens, donc intersection venant de l'intérieur
	}

	/**
	 * Méthode pour obtenir la normale à la surface d'une géométrie en intersection avec le rayon.
	 * Cette normale sera orientéw dans le <u>sens extérieur de la surface de la géométrie</u>. 
	 * <p>
	 * Cette définition est exactement celle fixée lors de l'intersection du rayon avec la géométrie.
	 * </p>
	 * @return La normale à la surface extérieur de la géométrie intersectée par le rayon. 
	 * @throws SNotIntersectedRayException S'il n'y a pas eu d'intersection avec ce rayon, la normale est indéterminée.
	 */
	public SVector3d getOutsideNormal() throws SNotIntersectedRayException
	{
	  if(!as_intersected)
      throw new SNotIntersectedRayException("Erreur SRay 008 : Le rayon n'a pas effectué d'intersection avec une géométrie.");
    
	  return normal;
	}
	
	/**
	 * Méthode pour obtenir la coordonnée <i>uv</i> associée à l'intersection sur la géométrie. 
	 * 
	 * @return La coordonnée uv associé à l'intersection sur la géométrie. 
	 * S'il n'y a pas de coordonnée uv associée à l'intersection, la valeur <b>null</b> sera retournée.
	 * @throws SNotIntersectedRayException S'il n'y a pas eu d'intersection avec ce rayon, la coordonnée <i>uv</i> ne peut pas avoir été déterminée.
	 * @throws SRuntimeException Le rayon n'a pas de coordonnée de texture d'attribué.
	 */
	public SVectorUV getUV() throws SNotIntersectedRayException, SRuntimeException
	{
	  // Vérifier que le rayon possède une coordonnée UV.
	  if(!asUV())
	    throw new SRuntimeException("Erreur SRay 010 : Il n'y a pas de coordonnée de texture associée à ce rayon.");
	  
	  return uv;
	}
	
	/**
	 * Méthode pour déterminer si le rayon intersecté possède une coordonnée de <i>uv</i> de texture.
	 * 
	 * @return <b>true</b> s'il y a une coordonnée <i>uv</i> et <b>false</b> sinon.
	 * @throws SNotIntersectedRayException Si le rayon n'a pas été intersecté, il ne peut pas y avoir de coordonnée <i>uv</i> d'attribuée au rayon.
	 */
	public boolean asUV() throws SNotIntersectedRayException
	{
	  // Vérifier que le rayon a été intersecté.
	  if(!as_intersected)
	    throw new SRuntimeException("Erreur SRay 010 : Le rayon n'a pas effectué d'intersection avec une géométrie.");
  
	  // Retourner "vrai" si uv n'est pas "null".
	  return uv != null;
	}
	
	/**
	 * Méthode pour générer un rayon intersecté à partir d'un rayon lancée et de ses caractéristiques définissant l'intersection.
	 * 
	 * @param geometry La géométrie qui est en intersection avec le rayon.
	 * @param normal La normale à la surface de la géométrie intersectée.
	 * @param t Le temps requis pour se rendre au lieu de l'intersection sur la géométrie.
	 * @return Le rayon avec les caractéristiques de l'intersection.
	 * @throws SAlreadyIntersectedRayException S'il y a déjà eu une intersection avec ce rayon.
	 */
	public SRay intersection(SGeometry geometry, SVector3d normal, double t) throws SAlreadyIntersectedRayException
	{
	  if(as_intersected)
	    throw new SAlreadyIntersectedRayException("Erreur SRay 011 : Ce rayon ne peut pas se faire intersecter, car il est présentement déjà intersecté.");
	  	  
	  // Construire un nouveau rayon intersecté avec les mêmes caractéristique que le rayon courant (sauf pour l'état d'intersection).
	  return new SRay(this.origin, this.direction, geometry, normal, t, this.refractive_index, this.previous_ray);
	}
	
	/**
   * Méthode pour générer un rayon intersecté à partir d'un rayon lancée et de ses caractéristiques définissant l'intersection.
   * 
   * @param geometry La géométrie qui est en intersection avec le rayon.
   * @param normal La normale à la surface de la géométrie intersectée.
   * @param uv La coordonnée uv associée à l'intersection.
   * @param t Le temps requis pour se rendre au lieu de l'intersection sur la géométrie.
   * @return Le rayon avec les caractéristiques de l'intersection.
   * @throws SAlreadyIntersectedRayException S'il y a déjà eu une intersection avec ce rayon.
   */
  public SRay intersection(SGeometry geometry, SVector3d normal, SVectorUV uv, double t) throws SAlreadyIntersectedRayException
  {
    if(as_intersected)
      throw new SAlreadyIntersectedRayException("Erreur SRay 012 : Ce rayon ne peut pas se faire intersecter, car il est présentement déjà intersecté.");
    
    // Construire un nouveau rayon intersecté avec les mêmes caractéristique que le rayon courant (sauf pour l'état d'intersection avec coordonnée de texture UV).
    return new SRay(this.origin, this.direction, geometry, normal, uv, t, this.refractive_index, this.previous_ray);
  }
  
  /**
   * Méthode pour construire un rayon récursif au rayon courant. 
   * L'origine du lancé du nouveau rayon correspondra à la position de l'intersection du rayon courant.
   * 
   * @param direction La nouvelle direction du rayon récursif.
   * @param refractive_index L'indice de réfraction où voyagera le rayon récursif.
   * @return Un rayon récursif au rayon courant.
   * @throws SNotIntersectedRayException Si le rayon n'a pas déjà réalisé une intersection.
   */
  public SRay castRecursiveRay(SVector3d direction, double refractive_index) throws SNotIntersectedRayException
  {
    // Vérifier que le rayon courant a déjà réalisé une intersection.
    if(!as_intersected)
      throw new SNotIntersectedRayException("Erreur SRay 013 : Ce rayon n'a pas été intersecté, il ne peut pas y avoir de lancé récursif de ce rayon");
    
    // Construire le nouveau rayon récursif avec le rayon courant comme étant le parent du rayon relancé.
    return new SRay(getIntersectionPosition(), direction, refractive_index, this);
  }
  
  /**
   * Méhode qui effectue la transformation d'un rayon <b>non intersecté</b> à partir d'une matrice de transformation linéaire de format 4x4.
   * 
   * @param transformation La matrice de transformation.
   * @return Le rayon transformé par la matrice de transformation.
   * @throws SAlreadyIntersectedRayException Si le rayon a été intersecté, il ne peut pas être transformé.
   */
  public SRay transformNotIntersectedRay(SMatrix4x4 transformation) throws SAlreadyIntersectedRayException
  {
    if(as_intersected)
      throw new SAlreadyIntersectedRayException("Erreur SRay 014 : Puisque ce rayon a été intersecté, il ne peut pas être transformé.");
    
    throw new SNoImplementationException("Erreur SRay : La méthode n'est pas implémentée.");
   
  }
  
  /**
   * Méhode qui effectue la transformation d'un rayon <b>non intersecté</b> à partir d'une matrice de transformation linéaire de format 4x4.
   * Cette version utilile le paramètre <i>transformed_axis_origin</i> qui représente l'origine du système d'axe
   * transformé par la matrice de transformation. Cette version permet la transformation du rayon avec 
   * moins d'allocation de mémoire. Cette version est pratique si la transformation de l'origine a été
   * préalablement calculé.
   * 
   * @param transformation La matrice de transformation.
   * @param transformed_origin L'origine du système d'axe transformé par la matrice de transformation.
   * @return Le rayon transformé par la matrice de transformation.
   * @throws SAlreadyIntersectedRayException Si le rayon a été intersecté, il ne peut pas être transformé.
   */
  public SRay transformNotIntersectedRay(SMatrix4x4 transformation, SVector3d transformed_axis_origin) throws SAlreadyIntersectedRayException
  {
    if(as_intersected)
      throw new SAlreadyIntersectedRayException("Erreur SRay 014 : Puisque ce rayon a été intersecté, il ne peut pas être transformé.");
        
    throw new SNoImplementationException("Erreur SRay : La méthode n'est pas implémentée.");
  }
      
  @Override
	public int compareTo(SRay ray) 
	{
		return Double.compare(this.int_t, ray.int_t);
	}
	
	@Override
  public String toString()
  {
    return  "SRay [origin=" + origin + ", direction=" + direction + 
        ", as_intersected=" + as_intersected + ", geometry=" + geometry +
        ", int_t=" + int_t + ", normal=" + normal + ", uv=" + uv + ", refractive_index=" + refractive_index + "]";

  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    
    if (obj == null) {
      return false;
    }
    
    if (!(obj instanceof SRay)) {
      return false;
    }
    
    SRay other = (SRay) obj;
    if (as_intersected != other.as_intersected) {
      return false;
    }
    
    if (direction == null) {
      if (other.direction != null) {
        return false;
      }
    } else if (!direction.equals(other.direction)) {
      return false;
    }
    
    if (geometry == null) {
      if (other.geometry != null) {
        return false;
      }
    } else if (!geometry.equals(other.geometry)) {
      return false;
    }
    
    if (normal == null) {
      if (other.normal != null) {
        return false;
      }
    } else if (!normal.equals(other.normal)) {
      return false;
    }
    
    if (uv == null) {
        if (other.uv != null) {
          return false;
        }
      } else if (!uv.equals(other.uv)) {
        return false;
      }
    
    if (origin == null) {
      if (other.origin != null) {
        return false;
      }
    } else if (!origin.equals(other.origin)) {
      return false;
    }
    
    if (!SMath.nearlyEquals(refractive_index, other.refractive_index)) {
      return false;
    }
    
    if (!SMath.nearlyEquals(int_t, other.int_t)){ 
      return false;
    }
    
    return true;
  }
	
}//fin classe SRay
