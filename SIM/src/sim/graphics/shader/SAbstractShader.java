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
 * La classe abstraite <b>SAbstractShader</b> représente un <i>shader</i> avec plusieurs fonctionnalités de base.
 * 
 * @author Simon Vézina
 * @since 2015-02-01
 * @version 2017-08-22
 */
public abstract class SAbstractShader implements SShader {

  //--------------
  // CONSTANTES //
  //--------------
  
	//type de mode de réflexion spéculaire disponible
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
	 * La constance <b>MAX_REFLEXION_CODE</b> correspond au nombre de codes différents maximals reconnus par le shader étant égal à {@value}.
	 */
	final private static int MAX_REFLEXION_CODE = 7;
	
	//-------------
	// VARIABLES //
	//-------------
	
	/**
	 * La variable <b>geometry_space</b> représente l'espace des géométries.
	 */
	final protected SGeometrySpace geometry_space;	   
	
	/**
	 * La variable <b>t_max</b> représente le temps maximal qu'un rayon peut se déplacer selon ce shader.
	 */
	final protected double t_max;                      
	
	/**
	 * La variable <b>light_list</b> représente une liste des sources de lumière qui seront utilisées dans le calcul de l'illumination.
	 */
	final protected List<SLight> light_list;				   
	
	/**
	 * La variable <b>reflexion_algo</b> représente le code correspond au type d'algorithme utilisé pour évaluer l'illumination.
	 */
	final protected int reflexion_algo;       
	
	
	private static int MULTIPLE_INSIDE_GEOMETRY_ERROR = 0;      //code d'erreur lorsqu'il y a plusieurs géométries imbriquées ensemble
	
	//-----------------
	// CONSTRUCTEURS //
	//-----------------
	
	/**
	 * Constructeur d'une shader abstrait avec le modèle de réflexion de <b><i>Blinn</i></b>.
	 * 
	 * @param geometry_space - L'espace des géométries.
	 * @param t_max - Temps de déplacement maximal d'un rayon.
	 * @param light_list - La liste des sources de lumières.
	 */
	public SAbstractShader(SGeometrySpace geometry_space, double t_max, List<SLight> light_list) 
	{
		this(geometry_space, t_max, light_list, BLINN_REFLEXION);
	}

	/**
	 * Constructeur d'une shader abstrait.
	 * 
	 * @param geometry_space - L'espace des géométries.
	 * @param t_max - Temps de déplacement maximal d'un rayon.
	 * @param light_list - La liste des sources de lumières.
	 * @param reflexion_algo - Le type d'algorithme pour réaliser le calcul de l'illumination.
	 * @throws SConstructorException Si la valeur de t_max est inférieur à une valeur de seuil.
	 * @throws SConstructorException Si le type d'algorithme de réflexion spéculaire n'est pas reconnu.
	 * @see NO_SPECULAR_REFLEXION
	 * @see PHONG_SPECULAR_REFLEXION
	 * @see BLINN_SPECULAR_REFLEXION
	 */
	public SAbstractShader(SGeometrySpace geometry_space, double t_max, List<SLight> light_list, int reflexion_algo) throws SConstructorException 
	{
		if(t_max < SRay.getEpsilon())
		  throw new SConstructorException("Erreur SAbstractShader 001 : Le temps maximal '" + t_max + "' doit être supérieur à '" + SRay.getEpsilon() +"'.");
		
		if(reflexion_algo < 0 || reflexion_algo > MAX_REFLEXION_CODE)
		  throw new SConstructorException("Erreur SAbstractShader 002 : Le code de l'algorithme de réflexion '" + reflexion_algo + "' n'est par reconnu. Il ne peut pas dépasser " + MAX_REFLEXION_CODE + ".");
		
		this.geometry_space = geometry_space;
		this.t_max = t_max;
		this.light_list = light_list;
		this.reflexion_algo = reflexion_algo;
	}
	
	/**
	 * Méthode qui fait l'analyse du type de source de lumière et redirige vers l'appel de la 
	 * méthode appropriée pour faire le calcul de shading particulier de chaque source de lumière.
	 * @param ray - Le rayon en intersection.
	 * @param light - La source de lumière.
	 * @param material - Le matériel qui interagit avec la source de lumière.
	 * @return la couleur résultat du calcul du shading.
	 * @throws SNoImplementationException Si le type de source n'a pas été implémenté dans le calcul de shading. 
	 */
	protected SColor shadeWithLight(SRay ray, SLight light, SMaterial material) throws SNoImplementationException
	{
		// Évaluer l'illumination du rayon en fonction du type de source en jeu
	  switch(light.getCodeName())
		{
		  case SAbstractLight.AMBIENT_LIGHT_CODE :  return shadeWithAmbientLight(ray, (SAmbientLight)light, material);
		  
		  case SAbstractLight.DIRECTIONAL_LIGHT_CODE : return shadeWithDirectionalLight(ray, (SDirectionalLight)light, material);
		  
		  case SAbstractLight.POINT_LIGHT_CODE : return shadeWithPointLight(ray, (SPointLight)light, material);
		  
		  case  SAbstractLight.LINEAR_APERTURE_LIGHT_CODE : 
		  case  SAbstractLight.RECTANGULAR_APERTURE_LIGHT_CODE :  
		  case  SAbstractLight.ELLIPTICAL_APERTURE_LIGHT_CODE :   
		  case  SAbstractLight.APERTURE_MASK_LIGHT_CODE :         return shadeWithInterferenceLight(ray, (SInterferenceLight)light, material);
		  
		  default : throw new SNoImplementationException("Erreur SAbstractShader 003 : La source de lumière est de type indéterminé.");
		}
	}
	
	/**
	 * Méthode qui effectue le calcul de shading pour une source de lumière ambiante.
	 * @param ray - Le rayon en intersection.
	 * @param light - La source de lumière ambiante.
	 * @param material - Le matériel qui interagit avec la source de lumière.
	 * @return la couleur résultant d'un shading avec une source de lumière ambiante.
	 */
	protected SColor shadeWithAmbientLight(SRay ray, SAmbientLight light, SMaterial material)
	{
		if(ray.asUV())
		  return shadeWithAmbientReflexion(light.getColor(), material.ambientColor(ray.getUV()));   //avec coordonnée uv
		else
		  return shadeWithAmbientReflexion(light.getColor(), material.ambientColor());              //sans coordonnée uv
	}
	
	/**
	 * Méthode qui effectue le calcul de shading pour une source de lumière directionnelle.
	 * @param ray - Le rayon en intersection.
	 * @param light - La source de lumière directionnelle.
	 * @param material - Le matériel qui interagit avec la source de lumière.
	 * @return la couleur résultant d'un shading avec une source de lumière directionnelle.
	 */
	protected SColor shadeWithDirectionalLight(SRay ray, SDirectionalLight light, SMaterial material)
	{
		// Évaluer le rayon d'ombrage (shadow ray)
	  SShadowRay shadow_ray = new SShadowRay(ray, light, geometry_space);
		
		// Si la surface intersectée est dans l'ombrage, il n'y aura pas de contribution de cette source de lumière
		if(shadow_ray.isInShadow())
			return SIllumination.NO_ILLUMINATION;
		
		// Obtenir la couleur associée à un éclairage avec orientation
		return shadeWithOrientedLight(shadow_ray.filtredLight(), ray, material, light.getOrientation());
	}
	
	/**
	 * Méthode qui effectue le calcul de shading pour une source de lumière ponctuelle.
	 * @param ray - Le rayon en intersection.
	 * @param light - La source de lumière ponctuelle.
	 * @param material - Le matériel qui interagit avec la source de lumière.
	 * @return la couleur résultant d'un shading avec une source de lumière ponctuelle.
	 */
	protected SColor shadeWithPointLight(SRay ray, SPointLight light, SMaterial material)
	{
	  // Évaluer le rayon d'ombrage (shadow ray)
	  SShadowRay shadow_ray = new SShadowRay(ray, light, geometry_space);
		
	  // Si la surface intersectée est dans l'ombrage, il n'y aura pas de contribution de cette source de lumière
		if(shadow_ray.isInShadow())
			return SIllumination.NO_ILLUMINATION;
		
		// Évaluer l'orientation de la source de lumière
		SVector3d light_orientation = light.getOrientation(ray.getIntersectionPosition());
			
		// Obtenir la couleur associée à un éclairage avec orientation
		SColor color = shadeWithOrientedLight(shadow_ray.filtredLight(), ray, material, light_orientation);
			
		//Ajouter le facteur d'attenuation et d'amplification à la source de lumière retournée
		double factor = light.amplification() * light.attenuation(ray.getIntersectionPosition());
		
		// Couleur avec amplification et atténuation
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
    // Ajouter la luminosité selon le type de rayon (avec ou sans coordonnée UV)
    if(ray.asUV())
    {
      // Contribution de la réflexion diffuse
      SColor color = shadeWithDiffuseReflexion(filtred_light_color, material.diffuseColor(ray.getUV()), ray.getShadingNormal(), light_orientation);
      
      // Constribution de la réflexion spéculaire
      color = color.add(shadeWithSpecularReflexion(filtred_light_color, material.specularColor(ray.getUV()), ray.getShadingNormal(), ray.getDirection(), light_orientation, material.getShininess()));
    
      return color;
    }
    else
    {
      // Contribution de la réflexion diffuse
      SColor color = shadeWithDiffuseReflexion(filtred_light_color, material.diffuseColor(), ray.getShadingNormal(), light_orientation);
      
      // Constribution de la réflexion spéculaire
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
    
    //Si la surface intersectée est dans l'ombrage.
    if(shadow_ray.isInShadow())
      return SIllumination.NO_ILLUMINATION;
    
    // Obtenir l'intensité relative de l'interférence.
    double intensity = light.getRelativeIntensity(ray.getIntersectionPosition());
    
    // Ajouter le facteur d'attenuation et d'amplification à la source de lumière retournée
    // à la valeur de l'intensité évaluée après calcul d'interférence.
    double factor = light.amplification() * light.attenuation(ray.getIntersectionPosition()) * intensity;
   
    // Retourner la couleur adéquatement pondéré
    return light.getColor().multiply(factor);
	}
	
  @Override
  public double evaluateRefractiveIndex(SVector3d position)
  {
    //Pour déterminer l'indice de réfraction d'un milieu ambiant, 
    //il faut vérifier dans quelle géométrie le rayon est situé.
    //- Si le rayon est situé à la frontière d'une géométrie (sur la surface), il sera considéré comme à l'extérieur et l'indice n = 1.0 sera affecté.
    //- Si le rayon est situé dans une géométrie, il faudra obtenir son indice de réfraction s'il possède une primitive comme parent, sinon n = 1.0 sera affecté.
    //- Si le rayon est situé dans plusieurs géométrie, NOUS AVONS PRÉSENTEMENT UN PROBLÈME !!! qui devra être résolu dans le futur.
    
    List<SGeometry> list_inside = geometry_space.listInsideGeometry(position);
    
    // Cas #1 : La position est à l'intérieur d'aucune géométrie
    if(list_inside.isEmpty())
      return 1.0;               
    
    // Cas #2 : La position est uniquement situé dans une géométrie
    if(list_inside.size() == 1)
      return list_inside.get(0).getPrimitiveParent().getMaterial().refractiveIndex();
    
    // Cas #3 : Le cas sans solution idéale, car il y a des géométries imbriquées sans clarification de la situation
    
    //-----------------------------------------
    //C'est ici que nous avons un problème!!!
    //-----------------------------------------
    MULTIPLE_INSIDE_GEOMETRY_ERROR++;
        
    //Pour le moment, nous allons faire la moyenne de tout les indices de réfraction pour définir l'indice du milieu
    double total_refractive_index = 0.0;
                
    for(SGeometry g : list_inside)
      total_refractive_index += g.getPrimitiveParent().getMaterial().refractiveIndex();   //pour faire la moyenne des indices de réfraction
         
    //Message de la situation de plusieurs géométrie transparente imbriquée
    if(MULTIPLE_INSIDE_GEOMETRY_ERROR == 1)
      SLog.logWriteLine(SStringUtil.END_LINE_CARACTER + "Message SAbstractShader : Il y a une situation où " + list_inside.size() + " géométries sont imbriquées et l'identification de l'indice de réfraction sera une moyenne des indices des matériaux.");
                  
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
      
      default : throw new SRuntimeException("Erreur SAbstractShader 004 : Le code " + reflexion_algo + " n'est pas reconnu dans cette méthode.");
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
      
      default : throw new SRuntimeException("Erreur SAbstractShader 005 : Le code " + reflexion_algo + " n'est pas reconnu dans cette méthode.");
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
      
      default : throw new SRuntimeException("Erreur SAbstractShader 006 : Le code " + reflexion_algo + " n'est pas reconnu dans cette méthode.");
    }
  }
    
}//fin classe SAbstractShader
