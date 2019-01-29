package sim.math;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import sim.exception.SNoImplementationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * <p>
 * La classe <b>SVector3d</b> représente une vecteur <i>x</i>, <i>y</i> et <i>z</i> à trois dimensions. 
 * </p>
 * 
 * <p>
 * Les opérations mathématiques supportées sont les suivantes : 
 * <ul>- L'addition.</ul>
 * <ul>- La soustraction.</ul>
 * <ul>- La multiplication par un scalaire.</ul>
 * <ul>- Le produit scalaire (<i>dot product</i>).</ul>
 * <ul>- Le produit vectoriel (<i>cross product</i>).</ul>
 * <ul>- La normalisation (vecteur de module 1).</ul>
 * </p>
 * 
 * @author Simon Vezina
 * @since 2014-12-16
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public class SVector3d implements SVector, Comparable<SVector3d> {

  //---------------
  // CONSTANTES  //
  //---------------

  /**
   * La constante 'DEFAULT_COMPONENT' correspond à la composante par défaut des variables x,y et z du vecteur étant égale à {@value}.
   */
	private static final double  DEFAULT_COMPONENT = 0.0;

	/**
	 * La constant <b>ZERO</b> correspond au vecteur nul correspondant à la coordonnée (0.0, 0.0, 0.0).
	 */
	public static final SVector3d ZERO = new SVector3d(0.0, 0.0, 0.0);
	
	/**
	 * La constant <b>UNITARY_X</b> correspond au vecteur unitaire parallèle à l'axe x correspondant à (1.0, 0.0, 0.0).
	 * On utilise également la notation <b><i>i</i></b> pour le représenter.
	 */
	public static final SVector3d UNITARY_X = new SVector3d(1.0, 0.0, 0.0);
	
	/**
   * La constant <b>UNITARY_Y</b> correspond au vecteur unitaire parallèle à l'axe y correspondant à (0.0, 1.0, 0.0).
   * On utilise également la notation <b><i>j</i></b> pour le représenter.
   */
	public static final SVector3d UNITARY_Y = new SVector3d(0.0, 1.0, 0.0);
	
	/**
   * La constant <b>UNITARY_Z</b> correspond au vecteur unitaire parallèle à l'axe z correspondant à (0.0, 0.0, 1.0).
   * On utilise également la notation <b><i>k</i></b> pour le représenter.
   */
	public static final SVector3d UNITARY_Z = new SVector3d(0.0, 0.0, 1.0);
	
	/**
   * La constante <b>ORIGIN</b> représente un vecteur origine étant situé à la coordonnée (0.0, 0.0, 0.0).
   * Il est égal au vecteur <b>ZERO</b>.
   */
	public static final SVector3d ORIGIN = ZERO;
	
	//--------------
	// VARIABLES  //
	//--------------
	
	/**
	 * La variable <b>x</b> correspond à la composante x du vecteur 3d.
	 */
	private final double x;
	
	/**
   * La variable <b>y</b> correspond à la composante y du vecteur 3d.
   */
	private final double y;	
	
	/**
   * La variable <b>z</b> correspond à la composante z du vecteur 3d.
   */
	private final double z;	
	
	//-----------------
	// CONSTRUCTEURS //
	//-----------------
	/**
	 * Constructeur représentant un vecteur 3d à l'origine d'un système d'axe xyz.
	 */
  public SVector3d()
	{
		this(DEFAULT_COMPONENT, DEFAULT_COMPONENT, DEFAULT_COMPONENT);
	}
	
	/**
	 * Constructeur avec composante <i>x</i>,<i>y</i> et <i>z</i> du vecteur 3d.
	 * 
	 * @param x La composante <i>x</i> du vecteur.
	 * @param y La composante <i>y</i> du vecteur.
	 * @param z La composante <i>z</i> du vecteur.
	 */
	public SVector3d(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Constructeur d'un vecteur 3d en utilisant un string décrivant les paramètres x, y et z du vecteur. 
	 * Une lecture autorisée peut prendre la forme suivante : "double x" "double y" "double z" (incluant la notation avec , ( ) [ ] comme délimieur dans l'expression du string comme par exemple (2.3, 4.3, 5.7) ).
	 * 
	 * @param string - Le string de l'expression du vecteur en paramètres x, y, et z.
	 * @throws SReadingException Si le format de la lecture n'est pas adéquat.
	 */
	public SVector3d(String string)throws SReadingException
	{
		double[] tab = read(string);
		
		x = tab[0];
		y = tab[1];
		z = tab[2];
	}
	
	//------------
	// MÉTHODES //
	//------------
	
	/**
	 * Méthode qui donne accès à la coordonnée x du vecteur.
	 * 
	 * @return La coordonnée x.
	 */
	public double getX()
	{ 
	  return x;
	}
	
	/**
	 * Méthode qui donne accès à la coordonnée y du vecteur.
	 * 
	 * @return La coordonnée y.
	 */
	public double getY()
	{ 
	  return y;
	}
	
	/**
	 * Méthode qui donne accès à la coordonnée z du vecteur.
	 * 
	 * @return La coordonnée z.
	 */
	public double getZ()
	{ 
	  return z;
	}
	
	@Override
	public SVector3d add(SVector v)
	{
	  return add((SVector3d)v);
	}
	 
	/**
	 * Méthode qui calcule <b>l'addition</b> de deux vecteurs. 
	 * 
	 * @param v Le vecteur à ajouter.
	 * @return La somme des deux vecteurs.
	 */
	public SVector3d add(SVector3d v)
	{	
		//---------------------------------------------------------------------------
	  // VERSION LABORATOIRE :
	  // throw new SNoImplementationException("La méthode n'est pas implémentée.");
	  //---------------------------------------------------------------------------
	  
	  return new SVector3d(x + v.x, y + v.y, z + v.z);
	}
	
	/**
	 * Méthode qui calcul la <b>soustraction</b> de deux vecteurs. 
	 * 
	 * @param v - Le vecteur à soustraire au vecteur présent.
	 * @return La soustraction des deux vecteurs.
	 */
	public SVector3d substract(SVector3d v)
	{
	  //---------------------------------------------------------------------------
    // VERSION LABORATOIRE :
    // throw new SNoImplementationException("La méthode n'est pas implémentée.");
    //---------------------------------------------------------------------------
    
	  return new SVector3d(x - v.x, y - v.y, z - v.z);
	}
	
	/**
	 * Méthode qui effectue la <b>multiplication d'un vecteur par une scalaire</b>.
	 * 
	 * @param m Le scalaire.
	 * @return La multiplication d'un vecteur par un scalaire.
	 */
	public SVector3d multiply(double m)
	{
	  //---------------------------------------------------------------------------
    // VERSION LABORATOIRE :
    // throw new SNoImplementationException("La méthode n'est pas implémentée.");
    //---------------------------------------------------------------------------
    
	  return new SVector3d(m*x, m*y, m*z);
	}
	
	/**
	 * Méthode pour obtenir le <b>module</b> d'un vecteur.
	 * 
	 * @return Le module du vecteur.
	 */
	public double modulus()
	{
	  //---------------------------------------------------------------------------
    // VERSION LABORATOIRE :
    // throw new SNoImplementationException("La méthode n'est pas implémentée.");
    //---------------------------------------------------------------------------
    
	  return Math.sqrt((x*x) + (y*y) + (z*z));
	}
	
	/**
	 * Méthode pour <b>normaliser</b> un vecteur à trois dimensions. 
	 * Un vecteur normalisé possède la même orientation, mais possède une <b> longeur unitaire </b>.
	 * Si le <b>module du vecteur est nul</b>, le vecteur normalisé sera le <b> vecteur nul </b> (0.0, 0.0, 0.0).
	 * 
	 * @return Le vecteur normalisé.
	 * @throws SImpossibleNormalizationException Si le vecteur ne peut pas être normalisé étant trop petit (modulus() <  SMath.EPSILON) ou de longueur nulle.
	 */
	public SVector3d normalize() throws SImpossibleNormalizationException
	{
	  //---------------------------------------------------------------------------
    // VERSION LABORATOIRE :
    // throw new SNoImplementationException("La méthode n'est pas implémentée.");
    //---------------------------------------------------------------------------
   
	  // Obtenir le module du vecteur
	  double mod = modulus();			
		
		// Vérification du module. S'il est trop petit, nous ne pouvons pas numériquement normaliser ce vecteur
		if(mod < SMath.EPSILON)
		  throw new SImpossibleNormalizationException("Erreur SVector3d 002 : Le vecteur " + this.toString() + " étant nul ou prèsque nul ne peut pas être numériquement normalisé.");
		else
			return new SVector3d(x/mod, y/mod, z/mod);
	}
	
	@Override
	public double dot(SVector v)
	{
	  return dot((SVector3d)v);
	}
	
	/**
	 * Méthode pour effectuer le <b>produit scalaire</b> entre deux vecteurs.
	 * 
	 * @param v Le vecteur à mettre en produit scalaire.
	 * @return Le produit scalaire entre les deux vecteurs.
	 */
	public double dot(SVector3d v)
	{
	  //---------------------------------------------------------------------------
    // VERSION LABORATOIRE :
    // throw new SNoImplementationException("La méthode n'est pas implémentée.");
    //---------------------------------------------------------------------------
    
	  return (x*v.x + y*v.y + z*v.z);
	}
	
	/**
	 * Méthode pour effectuer le <b>produit vectoriel</b> avec un autre vecteur v.
	 * <p>
	 * Cette version du produit vectoriel est implémenté en respectant la <b> règle de la main droite </b>.
	 * Il est important de rappeler que le produit vectoriel est <b> anticommutatif </b> (AxB = -BxA) et que l'ordre des vecteurs doit être adéquat pour obtenir le résultat désiré.
	 * Si l'un des deux vecteurs est <b> nul </b> ou les deux vecteurs sont <b> parallèles </b>, le résultat sera un <b> vecteur nul </b>.
	 * </p>
	 * 
	 * @param v Le second vecteur dans le produit vectoriel.
	 * @return Le résultat du produit vectoriel.
	 */
	public SVector3d cross(SVector3d v)
	{
	  //---------------------------------------------------------------------------
    // VERSION LABORATOIRE :
    // throw new SNoImplementationException("La méthode n'est pas implémentée.");
    //---------------------------------------------------------------------------
    
	  return new SVector3d(	   y * v.z - z * v.y ,
				                 -1*(x * v.z - z * v.x),
					                   x * v.y - y * v.x );
	}

	
		
	/**
	 * Méthode pour obtenir un vecteur avec les coordonnée (x,y,z) les plus petites (en considérant le signe) parmi un ensemble de vecteurs. 
	 * 
	 * @param value_list La liste des vecteurs à analyser.
	 * @return Un vecteur ayant comme coordonnée les plus petites valeurs (x,y,z) trouvées.
	 */
	public static SVector3d findMinValue(List<SVector3d> value_list)
  {
	  return findMinValue(value_list.toArray(new SVector3d[value_list.size()]));
  }
	
	/**
	 * Méthode pour obtenir un vecteur avec les coordonnée (x,y,z) les plus petites (en considérant le signe) parmi un ensemble de vecteurs. 
	 * 
	 * @param tab - Le tableau des vecteurs à analyser.
	 * @return Un vecteur ayant comme coordonnée les plus petites valeurs (x,y,z) trouvées.
	 */
	public static SVector3d findMinValue(SVector3d[] tab)
	{
	  double x_min = tab[0].getX();
	  
	  for(int i = 1; i < tab.length; i++)
	    if(x_min > tab[i].getX())
	      x_min = tab[i].getX();
	  
	  double y_min = tab[0].getY();
    
    for(int i = 1; i < tab.length; i++)
      if(y_min > tab[i].getY())
        y_min = tab[i].getY();
    
    double z_min = tab[0].getZ();
    
    for(int i = 1; i < tab.length; i++)
      if(z_min > tab[i].getZ())
        z_min = tab[i].getZ();
    
    return new SVector3d(x_min, y_min, z_min);
	}
	
	/**
   * Méthode pour obtenir un vecteur avec les coordonnée (x,y,z) les plus grandes (en considérant le signe) parmi un ensemble de vecteurs. 
   * 
   * @param value_list La liste des vecteurs à analyser.
   * @return Un vecteur ayant comme coordonnée les plus grandes valeurs (x,y,z) trouvées.
   */
  public static SVector3d findMaxValue(List<SVector3d> value_list)
  {
    return findMaxValue(value_list.toArray(new SVector3d[value_list.size()]));
  }
  
	/**
   * Méthode pour obtenir un vecteur avec les coordonnée (x,y,z) les plus grandes (en considérant le signe) parmi un ensemble de vecteurs. 
   * 
   * @param tab - Le tableau des vecteurs à analyser.
   * @return Un vecteur ayant comme coordonnée les plus grandes valeurs (x,y,z) trouvées.
   */
  public static SVector3d findMaxValue(SVector3d[] tab)
  {
    double x_max = tab[0].getX();
    
    for(int i = 1; i < tab.length; i++)
      if(x_max < tab[i].getX())
        x_max = tab[i].getX();
    
    double y_max = tab[0].getY();
    
    for(int i = 1; i < tab.length; i++)
      if(y_max < tab[i].getY())
        y_max = tab[i].getY();
    
    double z_max = tab[0].getZ();
    
    for(int i = 1; i < tab.length; i++)
      if(z_max < tab[i].getZ())
        z_max = tab[i].getZ();
    
    return new SVector3d(x_max, y_max, z_max);
  }
  
	/**
	 * Méthode pour écrire les paramètres xyz du vecteur dans un fichier en format txt. Le format de l'écriture est "x"  "y"  "z" comme l'exemple suivant : 0.6  0.2  0.5
	 * 
	 * @param bw Le buffer d'écriture.
	 * @throws IOException S'il y a une erreur avec le buffer d'écriture.
	 * @see BufferedWriter
	 */
	public void write(BufferedWriter bw)throws IOException
	{
		bw.write(toString());
	}
	
	

	//----------------------------------------------------------------------------------------------
	//Méthodes pour calcul spécialisé (afin de réduire l'allocation de mémoire lors des calculs) 
	//----------------------------------------------------------------------------------------------
 
  /**
   * Méthode pour obtenir la distance entre deux points.
   * 
   * @param A Le premier point.
   * @param B Le deuxième point.
   * @return La distance entre le point A et B.
   */
  public static double distance(SVector3d A, SVector3d B)
  {
    throw new SNoImplementationException("La méthode n'est pas implémentée.");
  }
  
  /**
   * Méthode permettant d'obtenir l'angle entre deux vecteurs A et B. L'angle sera obtenu en radian.
   * 
   * @param A Le premier vecteur.
   * @param B Le second vecteur. 
   * @return L'angle entre les deux vecteurs.
   */
  public static double angleBetween(SVector3d A, SVector3d B)
  {
    throw new SNoImplementationException("La méthode n'est pas implémentée.");
  }
  
  /**
   * Méthode pour effectuer la <b>soustraction entre deux produits scalaires</b>. Cette opération vectorielle est équivalente à l'expression
   * <ul>(A - B) . C = A . C - B . C </ul>
   * @param A - Le vecteur A de l'expression.
   * @param B - Le vecteur B de l'expression.
   * @param C - Le vecteur C de l'expression.
   * @return Le scalaire représentant la solution de l'équation.
   */
  public static double AdotCsubstractBdotC(SVector3d A, SVector3d B, SVector3d C)
  {
    throw new SNoImplementationException("La méthode n'est pas implémentée.");
  }
 
 /**
  * Méthode qui effectue le <b>produit mixte</b> de trois vecteurs A, B et C. Cette opération vectorielle est équivalente à l'expression
  * <ul>(A x B) . C = (Ax*By*Cz + Bx*Cy*Az + Cx*Ay*Bz) - (Az*By*Cx + Bx*Cz*Ay + Cy*Ax*Bz)</ul>
  * Il est à remarquer que si deux des trois vecteurs sont égaux ou colinéaires, le produit mixte est nul.
  * @param A - Le 1ier vecteur dans le produit mixte.
  * @param B - Le 2ième vecteur dans le produit mixte.
  * @param C - Le 3ième vecteur dans le produit mixte.
  * @return Le scalaire résultant du produit mixte.
  */
  public static double AcrossBdotC(SVector3d A, SVector3d B, SVector3d C)
  {
    throw new SNoImplementationException("Méthode non implémentée.");
  }
 
 /**
  * Méthode qui effectue le <b>triple produit vectoriel</b> de trois vecteurs A, B et C. Cette opération vectorielle est équivalente à l'expression
  * <ul>(A x B) x C = (A . C) B - (B . C) A</ul>  
  * <p>Il est important de préciser que les deux expressions
  * <ul>(A x B) x C != A x (B x C)</ul>
  * ne sont pas égaux. L'ordre de priorité des opérations doit être bien défini.</p>
  *  
  * @param A - Le 1ier vecteur dans le double produit vectoriel.
  * @param B - Le 2ième vecteur dans le double produit vectoriel.
  * @param C - Le 3ième vecteur dans le double produit vectoriel.
  * @return Le vecteur résultant du produit mixte.
  */
 public static SVector3d AcrossBcrossC(SVector3d A, SVector3d B, SVector3d C)
 {
   throw new SNoImplementationException("La méthode n'est pas implémentée.");
 }
 
 /**
  * <p>Méthode qui effectue le <b>triple produit vectoriel</b> de trois vecteurs A, B et C avec l'ordre de priorité</p>
  * <ul>D = A x (B x C)</ul>
  * <p>où D est le résultat du triple produit vectoriel. Cette opération vectorielle est équivalente à l'expression</p>
  * <ul>A x (B x C) = (A . C) B - (A . B) C</ul>  
  * <p>et il est important de préciser que les deux expressions</p>
  * <ul>A x (B x C) != (A x B) x C</ul>
  * <p>ne sont pas égaux puisque le <b>produit vectoriel n'est pas commutatif</b>.</p>
  *  
  * @param A - Le 1ier vecteur dans le double produit vectoriel.
  * @param B - Le 2ième vecteur dans le double produit vectoriel.
  * @param C - Le 3ième vecteur dans le double produit vectoriel.
  * @return Le vecteur résultant du triple produit vectoriel.
  */
 public static SVector3d Across_BcrossC(SVector3d A, SVector3d B, SVector3d C)
 {
   throw new SNoImplementationException("La méthode n'est pas implémentée.");
 }  
 
 /**
  * <p>Méthode qui effectue le <b>triple produit vectoriel</b> de trois vecteurs A, B et C avec l'ordre de priorité</p>
  * <ul>D = (A x B) x C</ul>
  * <p>où D est le résultat du triple produit vectoriel. Cette opération vectorielle est équivalente à l'expression</p>
  * <ul>(A x B) x C = ?????</ul>  
  * <p>et il est important de préciser que les deux expressions</p>
  * <ul>(A x B) x C != A x (B x C)</ul>
  * <p>ne sont pas égaux puisque le <b>produit vectoriel n'est pas commutatif</b>.</p>
  *  
  * @param A - Le 1ier vecteur dans le double produit vectoriel.
  * @param B - Le 2ième vecteur dans le double produit vectoriel.
  * @param C - Le 3ième vecteur dans le double produit vectoriel.
  * @return Le vecteur résultant du triple produit vectoriel.
  */
 public static SVector3d AcrossB_crossC(SVector3d A, SVector3d B, SVector3d C)
 {
   throw new SNoImplementationException("Méthode non implémentée.");
 }
 
 /**
  * Méthode qui effectue une opération spécialisée de multiplication par un scalaire et d'addition vectorielle équivalente à
  * <ul>V = a*B + C.</ul>
  * @param a - Le scalaire à multiplier avec B.
  * @param B - Le 1ier vecteur dans l'expression vectorielle.
  * @param C - Le 2ième vecteur dans l'expression vectorielle à ajouter.
  * @return Le vecteur résultant de cette opération spécialisée.
  */
 public static SVector3d AmultiplyBaddC(double a, SVector3d B, SVector3d C)
 {
	 throw new SNoImplementationException("La méthode n'est pas implémentée.");
 }

 //------------------------
 // MÉTHODES UTILITAIRES //
 //------------------------
 /**
  * Méthode utilisant un string comme paramètre pour définir les composantes x, y et z du vecteur. 
  * Une lecture autorisée peut prendre la forme suivante : "double x" "double y" "double z" 
  * (en incluant la notation avec , ( ) [ ] < > comme délimieur dans l'expression du string comme par exemple (2.3, 4.3, 5.7) ).
  *  
  * @param string - Le string de l'expression du vecteur en paramètres x, y, et z.
  * @return un tableau de trois éléments tel que x = [0], y = [1] et z = [2]. 
  * @throws SReadingException Si le format de lecture n'est pas adéquat.
  */
 private double[] read(String string)throws SReadingException
 {
   StringTokenizer tokens = new StringTokenizer(string, SStringUtil.REMOVE_CARACTER_TOKENIZER);  
   
   if(tokens.countTokens() != 3)
     throw new SReadingException("Erreur SVector3d 003 : L'expression '" + string + "' ne contient pas exactement 3 paramètres pour les composantes xyz du vecteur 3d.");
   else
   {
     String s = "";          //String à convertir en double pour les composantes x, y et z.
     String comp = "";       //Nom de la composante en lecture utilisée pour l'envoie d'une Exception s'il y a lieu.
     
     try
     {
       double[] tab = new double[3]; //Tableau des 3 composantes
       
       comp = "x";
       s = tokens.nextToken();
       tab[0] = Double.valueOf(s);
       
       comp = "y";
       s = tokens.nextToken();
       tab[1] = Double.valueOf(s);
       
       comp = "z";
       s = tokens.nextToken();
       tab[2] = Double.valueOf(s);
       
       return tab;
       
     }catch(NumberFormatException e){ 
       throw new SReadingException("Erreur SVector3d 004 : L'expression '" + s +"' n'est pas valide pour définir la composante '" + comp + "' du vecteur en cours.");
     }
   } 
 }

 //----------------------
 // MÉTHODE OVERLOADED //
 //----------------------
 
 @Override
 public String toString()
 {
   return "[" + x + ", " + y + ", " + z + "]";   
 }

 @Override
 public int hashCode()
 {
   //hashcode autogénéré par Eclipse avec les paramètres x, y et z
   final int prime = 31;
   int result = 1;
   long temp;
   temp = Double.doubleToLongBits(x);
   result = prime * result + (int) (temp ^ (temp >>> 32));
   temp = Double.doubleToLongBits(y);
   result = prime * result + (int) (temp ^ (temp >>> 32));
   temp = Double.doubleToLongBits(z);
   result = prime * result + (int) (temp ^ (temp >>> 32));
   return result;
 }

 @Override
 public boolean equals(Object obj)
 {
   if(this == obj)
     return true;
   
   if(obj == null)
     return false;
   
   if(!(obj instanceof SVector3d))
     return false;
   
   SVector3d other = (SVector3d) obj;
   
   //Comparaison des valeurs x,y et z en utilisant la méthode de comparaison de SMath
   if(!SMath.nearlyEquals(x, other.x))
     return false;
   
   if(!SMath.nearlyEquals(y, other.y))
     return false;
   
   if(!SMath.nearlyEquals(z, other.z))
     return false;
   
   return true;
 }
 
 @Override
 public int compareTo(SVector3d o) 
 {
   // Variable déterminant la comparaison 
   // ( comp < 0 ==> plus petit )
   // ( comp > 0 ==> plus grand )
   // ( comp = 0 ==> égal )
   int comp;
   
   // Comparaison sur les x
   comp= Double.compare(this.x, o.x);
   
   if(comp != 0)
     return comp;
   
   // Comparaison sur les y
   comp= Double.compare(this.y, o.y);
   
   if(comp != 0)
     return comp;
   
   // Comparaison sur les z
   return Double.compare(this.z, o.z);  
 }
 
}//fin de la classe SVector3d
