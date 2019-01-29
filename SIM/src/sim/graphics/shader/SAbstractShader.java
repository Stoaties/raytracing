/**
 * 
 */
package sim.graphics.shader;

import java.util.List;

import sim.exception.SConstructorException;
import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.geometry.SGeometry;
import sim.geometry.SRay;
import sim.geometry.space.SGeometrySpace;
import sim.graphics.SColor;
import sim.graphics.light.SAbstractLight;
import sim.graphics.light.SAmbientLight;
import sim.graphics.light.SDirectionalLight;
import sim.graphics.light.SInterferenceLight;
import sim.graphics.light.SLight;
import sim.graphics.light.SPointLight;
import sim.graphics.light.SShadowRay;
import sim.graphics.material.SMaterial;
import sim.math.SVector3d;
import sim.util.SLog;
import sim.util.SStringUtil;

/**
 * La classe abstraite <b>SAbstractShader</b> repr�sente un <i>shader</i> avec plusieurs fonctionnalit�s de base.
 * 
 * @author Simon V�zina
 * @since 2015-02-01
 * @version 2017-08-22
 */
public abstract class SAbstractShader implements SShader {

  //--------------
  // CONSTANTES //
  //--------------
  
	//type de mode de r�flexion sp�culaire disponible
  final public static String[] REFLEXION_ALGORITHM = { "no_light", "ambient", "diffuse", "phong_specular", "blinn_specular", "no_specular", "phong", "blinn" };
  final public static int NO_LIGHT = 0;
  final public static int AMBIENT = 1;
  final public static int DIFFUSE = 2;
  final public static int PHONG_SPECULAR = 3;
  final public static int BLINN_SPECULAR = 4;
  final public static int NO_SPECULAR_REFLEXION = 5;
	final public static int PHONG_REFLEXION = 6;
	final public static int BLINN_REFLEXION = 7;
	
	/**
	 * La constance <b>MAX_REFLEXION_CODE</b> correspond au nombre de codes diff�rents maximals reconnus par le shader �tant �gal � {@value}.
	 */
	final private static int MAX_REFLEXION_CODE = 7;
	
	//-------------
	// VARIABLES //
	//-------------
	
	/**
	 * La variable <b>geometry_space</b> repr�sente l'espace des g�om�tries.
	 */
	final protected SGeometrySpace geometry_space;	   
	
	/**
	 * La variable <b>t_max</b> repr�sente le temps maximal qu'un rayon peut se d�placer selon ce shader.
	 */
	final protected double t_max;                      
	
	/**
	 * La variable <b>light_list</b> repr�sente une liste des sources de lumi�re qui seront utilis�es dans le calcul de l'illumination.
	 */
	final protected List<SLight> light_list;				   
	
	/**
	 * La variable <b>reflexion_algo</b> repr�sente le code correspond au type d'algorithme utilis� pour �valuer l'illumination.
	 */
	final protected int reflexion_algo;       
	
	
	private static int MULTIPLE_INSIDE_GEOMETRY_ERROR = 0;      //code d'erreur lorsqu'il y a plusieurs g�om�tries imbriqu�es ensemble
	
	//-----------------
	// CONSTRUCTEURS //
	//-----------------
	
	/**
	 * Constructeur d'une shader abstrait avec le mod�le de r�flexion de <b><i>Blinn</i></b>.
	 * 
	 * @param geometry_space - L'espace des g�om�tries.
	 * @param t_max - Temps de d�placement maximal d'un rayon.
	 * @param light_list - La liste des sources de lumi�res.
	 */
	public SAbstractShader(SGeometrySpace geometry_space, double t_max, List<SLight> light_list) 
	{
		this(geometry_space, t_max, light_list, BLINN_REFLEXION);
	}

	/**
	 * Constructeur d'une shader abstrait.
	 * 
	 * @param geometry_space - L'espace des g�om�tries.
	 * @param t_max - Temps de d�placement maximal d'un rayon.
	 * @param light_list - La liste des sources de lumi�res.
	 * @param reflexion_algo - Le type d'algorithme pour r�aliser le calcul de l'illumination.
	 * @throws SConstructorException Si la valeur de t_max est inf�rieur � une valeur de seuil.
	 * @throws SConstructorException Si le type d'algorithme de r�flexion sp�culaire n'est pas reconnu.
	 * @see NO_SPECULAR_REFLEXION
	 * @see PHONG_SPECULAR_REFLEXION
	 * @see BLINN_SPECULAR_REFLEXION
	 */
	public SAbstractShader(SGeometrySpace geometry_space, double t_max, List<SLight> light_list, int reflexion_algo) throws SConstructorException 
	{
		if(t_max < SRay.getEpsilon())
		  throw new SConstructorException("Erreur SAbstractShader 001 : Le temps maximal '" + t_max + "' doit �tre sup�rieur � '" + SRay.getEpsilon() +"'.");
		
		if(reflexion_algo < 0 || reflexion_algo > MAX_REFLEXION_CODE)
		  throw new SConstructorException("Erreur SAbstractShader 002 : Le code de l'algorithme de r�flexion '" + reflexion_algo + "' n'est par reconnu. Il ne peut pas d�passer " + MAX_REFLEXION_CODE + ".");
		
		this.geometry_space = geometry_space;
		this.t_max = t_max;
		this.light_list = light_list;
		this.reflexion_algo = reflexion_algo;
	}
	
	/**
	 * M�thode qui fait l'analyse du type de source de lumi�re et redirige vers l'appel de la 
	 * m�thode appropri�e pour faire le calcul de shading particulier de chaque source de lumi�re.
	 * @param ray - Le rayon en intersection.
	 * @param light - La source de lumi�re.
	 * @param material - Le mat�riel qui interagit avec la source de lumi�re.
	 * @return la couleur r�sultat du calcul du shading.
	 * @throws SNoImplementationException Si le type de source n'a pas �t� impl�ment� dans le calcul de shading. 
	 */
	protected SColor shadeWithLight(SRay ray, SLight light, SMaterial material) throws SNoImplementationException
	{
		// �valuer l'illumination du rayon en fonction du type de source en jeu
	  switch(light.getCodeName())
		{
		  case SAbstractLight.AMBIENT_LIGHT_CODE :  return shadeWithAmbientLight(ray, (SAmbientLight)light, material);
		  
		  case SAbstractLight.DIRECTIONAL_LIGHT_CODE : return shadeWithDirectionalLight(ray, (SDirectionalLight)light, material);
		  
		  case SAbstractLight.POINT_LIGHT_CODE : return shadeWithPointLight(ray, (SPointLight)light, material);
		  
		  case  SAbstractLight.LINEAR_APERTURE_LIGHT_CODE : 
		  case  SAbstractLight.RECTANGULAR_APERTURE_LIGHT_CODE :  
		  case  SAbstractLight.ELLIPTICAL_APERTURE_LIGHT_CODE :   
		  case  SAbstractLight.APERTURE_MASK_LIGHT_CODE :         return shadeWithInterferenceLight(ray, (SInterferenceLight)light, material);
		  
		  default : throw new SNoImplementationException("Erreur SAbstractShader 003 : La source de lumi�re est de type ind�termin�.");
		}
	}
	
	/**
	 * M�thode qui effectue le calcul de shading pour une source de lumi�re ambiante.
	 * @param ray - Le rayon en intersection.
	 * @param light - La source de lumi�re ambiante.
	 * @param material - Le mat�riel qui interagit avec la source de lumi�re.
	 * @return la couleur r�sultant d'un shading avec une source de lumi�re ambiante.
	 */
	protected SColor shadeWithAmbientLight(SRay ray, SAmbientLight light, SMaterial material)
	{
		if(ray.asUV())
		  return shadeWithAmbientReflexion(light.getColor(), material.ambientColor(ray.getUV()));   //avec coordonn�e uv
		else
		  return shadeWithAmbientReflexion(light.getColor(), material.ambientColor());              //sans coordonn�e uv
	}
	
	/**
	 * M�thode qui effectue le calcul de shading pour une source de lumi�re directionnelle.
	 * @param ray - Le rayon en intersection.
	 * @param light - La source de lumi�re directionnelle.
	 * @param material - Le mat�riel qui interagit avec la source de lumi�re.
	 * @return la couleur r�sultant d'un shading avec une source de lumi�re directionnelle.
	 */
	protected SColor shadeWithDirectionalLight(SRay ray, SDirectionalLight light, SMaterial material)
	{
		// �valuer le rayon d'ombrage (shadow ray)
	  SShadowRay shadow_ray = new SShadowRay(ray, light, geometry_space);
		
		// Si la surface intersect�e est dans l'ombrage, il n'y aura pas de contribution de cette source de lumi�re
		if(shadow_ray.isInShadow())
			return SIllumination.NO_ILLUMINATION;
		
		// Obtenir la couleur associ�e � un �clairage avec orientation
		return shadeWithOrientedLight(shadow_ray.filtredLight(), ray, material, light.getOrientation());
	}
	
	/**
	 * M�thode qui effectue le calcul de shading pour une source de lumi�re ponctuelle.
	 * @param ray - Le rayon en intersection.
	 * @param light - La source de lumi�re ponctuelle.
	 * @param material - Le mat�riel qui interagit avec la source de lumi�re.
	 * @return la couleur r�sultant d'un shading avec une source de lumi�re ponctuelle.
	 */
	protected SColor shadeWithPointLight(SRay ray, SPointLight light, SMaterial material)
	{
	  // �valuer le rayon d'ombrage (shadow ray)
	  SShadowRay shadow_ray = new SShadowRay(ray, light, geometry_space);
		
	  // Si la surface intersect�e est dans l'ombrage, il n'y aura pas de contribution de cette source de lumi�re
		if(shadow_ray.isInShadow())
			return SIllumination.NO_ILLUMINATION;
		
		// �valuer l'orientation de la source de lumi�re
		SVector3d light_orientation = light.getOrientation(ray.getIntersectionPosition());
			
		// Obtenir la couleur associ�e � un �clairage avec orientation
		SColor color = shadeWithOrientedLight(shadow_ray.filtredLight(), ray, material, light_orientation);
			
		//Ajouter le facteur d'attenuation et d'amplification � la source de lumi�re retourn�e
		double factor = light.amplification() * light.attenuation(ray.getIntersectionPosition());
		
		// Couleur avec amplification et att�nuation
		return color.multiply(factor);
	}
	
	/**
	 * ...
	 * 
	 * @param filtred_light_color
	 * @param ray
	 * @param material
	 * @param light_orientation
	 * @return
	 */
	private SColor shadeWithOrientedLight(SColor filtred_light_color, SRay ray, SMaterial material, SVector3d light_orientation)
  {
    // Ajouter la luminosit� selon le type de rayon (avec ou sans coordonn�e UV)
    if(ray.asUV())
    {
      // Contribution de la r�flexion diffuse
      SColor color = shadeWithDiffuseReflexion(filtred_light_color, material.diffuseColor(ray.getUV()), ray.getShadingNormal(), light_orientation);
      
      // Constribution de la r�flexion sp�culaire
      color = color.add(shadeWithSpecularReflexion(filtred_light_color, material.specularColor(ray.getUV()), ray.getShadingNormal(), ray.getDirection(), light_orientation, material.getShininess()));
    
      return color;
    }
    else
    {
      // Contribution de la r�flexion diffuse
      SColor color = shadeWithDiffuseReflexion(filtred_light_color, material.diffuseColor(), ray.getShadingNormal(), light_orientation);
      
      // Constribution de la r�flexion sp�culaire
      color = color.add(shadeWithSpecularReflexion(filtred_light_color, material.specularColor(), ray.getShadingNormal(), ray.getDirection(), light_orientation, material.getShininess()));
    
      return color;
    }
  }
	
	/**
	 * ...
	 * 
	 * @param ray
	 * @param light
	 * @param material
	 * @return
	 */
	protected SColor shadeWithInterferenceLight(SRay ray, SInterferenceLight light, SMaterial material)
	{
	  // Calcul du rayon d'ombre.
	  SShadowRay shadow_ray = new SShadowRay(ray, light, geometry_space);
    
    //Si la surface intersect�e est dans l'ombrage.
    if(shadow_ray.isInShadow())
      return SIllumination.NO_ILLUMINATION;
    
    // Obtenir l'intensit� relative de l'interf�rence.
    double intensity = light.getRelativeIntensity(ray.getIntersectionPosition());
    
    // Ajouter le facteur d'attenuation et d'amplification � la source de lumi�re retourn�e
    // � la valeur de l'intensit� �valu�e apr�s calcul d'interf�rence.
    double factor = light.amplification() * light.attenuation(ray.getIntersectionPosition()) * intensity;
   
    // Retourner la couleur ad�quatement pond�r�
    return light.getColor().multiply(factor);
	}
	
  @Override
  public double evaluateRefractiveIndex(SVector3d position)
  {
    //Pour d�terminer l'indice de r�fraction d'un milieu ambiant, 
    //il faut v�rifier dans quelle g�om�trie le rayon est situ�.
    //- Si le rayon est situ� � la fronti�re d'une g�om�trie (sur la surface), il sera consid�r� comme � l'ext�rieur et l'indice n = 1.0 sera affect�.
    //- Si le rayon est situ� dans une g�om�trie, il faudra obtenir son indice de r�fraction s'il poss�de une primitive comme parent, sinon n = 1.0 sera affect�.
    //- Si le rayon est situ� dans plusieurs g�om�trie, NOUS AVONS PR�SENTEMENT UN PROBL�ME !!! qui devra �tre r�solu dans le futur.
    
    List<SGeometry> list_inside = geometry_space.listInsideGeometry(position);
    
    // Cas #1 : La position est � l'int�rieur d'aucune g�om�trie
    if(list_inside.isEmpty())
      return 1.0;               
    
    // Cas #2 : La position est uniquement situ� dans une g�om�trie
    if(list_inside.size() == 1)
      return list_inside.get(0).getPrimitiveParent().getMaterial().refractiveIndex();
    
    // Cas #3 : Le cas sans solution id�ale, car il y a des g�om�tries imbriqu�es sans clarification de la situation
    
    //-----------------------------------------
    //C'est ici que nous avons un probl�me!!!
    //-----------------------------------------
    MULTIPLE_INSIDE_GEOMETRY_ERROR++;
        
    //Pour le moment, nous allons faire la moyenne de tout les indices de r�fraction pour d�finir l'indice du milieu
    double total_refractive_index = 0.0;
                
    for(SGeometry g : list_inside)
      total_refractive_index += g.getPrimitiveParent().getMaterial().refractiveIndex();   //pour faire la moyenne des indices de r�fraction
         
    //Message de la situation de plusieurs g�om�trie transparente imbriqu�e
    if(MULTIPLE_INSIDE_GEOMETRY_ERROR == 1)
      SLog.logWriteLine(SStringUtil.END_LINE_CARACTER + "Message SAbstractShader : Il y a une situation o� " + list_inside.size() + " g�om�tries sont imbriqu�es et l'identification de l'indice de r�fraction sera une moyenne des indices des mat�riaux.");
                  
    return total_refractive_index / list_inside.size();    
  }
  
  /**
   * ...
   * 
   * @param ambient_light_color
   * @param material_color
   * @return
   * @throws SRuntimeException
   */
  private SColor shadeWithAmbientReflexion(SColor ambient_light_color, SColor material_color) throws SRuntimeException
  {
    switch(reflexion_algo)
    {
      case NO_LIGHT : 
      case DIFFUSE :
      case PHONG_SPECULAR : 
      case BLINN_SPECULAR :   return SIllumination.NO_ILLUMINATION;
      
      case AMBIENT :
      case NO_SPECULAR_REFLEXION :
      case PHONG_REFLEXION :
      case BLINN_REFLEXION :  return SIllumination.ambientReflexion(ambient_light_color, material_color);
      
      default : throw new SRuntimeException("Erreur SAbstractShader 004 : Le code " + reflexion_algo + " n'est pas reconnu dans cette m�thode.");
    }
  }
  
 /**
  * ...
  * 
  * @param diffuse_light_color
  * @param material_color
  * @param normal
  * @param light_orientation
  * @return
  * @throws SRuntimeException
  */
  private SColor shadeWithDiffuseReflexion(SColor diffuse_light_color, SColor material_color, SVector3d normal, SVector3d light_orientation) throws SRuntimeException
  {
    switch(reflexion_algo)
    {
      case NO_LIGHT : 
      case AMBIENT :
      case PHONG_SPECULAR : 
      case BLINN_SPECULAR :   return SIllumination.NO_ILLUMINATION;
      
      case DIFFUSE :
      case NO_SPECULAR_REFLEXION :
      case PHONG_REFLEXION :
      case BLINN_REFLEXION :  return SIllumination.lambertianReflexion(diffuse_light_color, material_color, normal, light_orientation);
      
      default : throw new SRuntimeException("Erreur SAbstractShader 005 : Le code " + reflexion_algo + " n'est pas reconnu dans cette m�thode.");
    }
  }
  
 /**
  * ...
  * 
  * @param specular_light_color
  * @param material_color
  * @param normal
  * @param ray_orientation
  * @param light_orientation
  * @param shininess
  * @return
  * @throws SRuntimeException
  */
  private SColor shadeWithSpecularReflexion(SColor specular_light_color, SColor material_color, SVector3d normal, SVector3d ray_orientation, SVector3d light_orientation, double shininess) throws SRuntimeException
  {
    switch(reflexion_algo)
    {
      case NO_LIGHT : 
      case AMBIENT :
      case DIFFUSE :          
      case NO_SPECULAR_REFLEXION :  return SIllumination.NO_ILLUMINATION;
      
      
      case PHONG_SPECULAR : 
      case PHONG_REFLEXION :        return SIllumination.phongSpecularReflexion(specular_light_color, material_color, normal, ray_orientation, light_orientation, shininess);
        
      case BLINN_SPECULAR :
      case BLINN_REFLEXION :        return SIllumination.blinnSpecularReflexion(specular_light_color, material_color, normal, ray_orientation, light_orientation, shininess);
      
      default : throw new SRuntimeException("Erreur SAbstractShader 006 : Le code " + reflexion_algo + " n'est pas reconnu dans cette m�thode.");
    }
  }
    
}//fin classe SAbstractShader
