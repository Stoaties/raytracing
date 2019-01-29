/**
 * 
 */
package sim.graphics.shader;

import java.util.List;

import sim.exception.SRuntimeException;
import sim.geometry.SRay;
import sim.geometry.space.SGeometrySpace;
import sim.graphics.SColor;
import sim.graphics.light.SLight;
import sim.graphics.material.SMaterial;

/**
 * La classe <b>SPhongReflexionShader</b> représente un shader selon le modèle de réflexion de phong. 
 * Ce shader fait l'analyse d'un rayon principal avec un modèle de réflexion ambiante, diffuse et spéculaire.
 * 
 * @author Simon Vézina
 * @since 2015-02-03
 * @version 2016-05-06
 */
public class SPhongReflexionShader extends SAbstractShader {

	/**
	 * Constructeur d'un shader selon le modèle de réflexion de phong avec le modèle de réflexion spéculaire de <b><i>Blinn</i></b>.
	 * @param geometry_space - L'espace des géométries.
	 * @param t_max - Temps de déplacement maximal d'un rayon.
	 * @param light_list - La liste des sources de lumières.
	 */
	public SPhongReflexionShader(SGeometrySpace geometry_space, double t_max, List<SLight> light_list)
	{
		super(geometry_space, t_max, light_list);
	}

	/**
	 * Constructeur d'un shader selon le modèle de réflexion de phong.
	 * @param geometry_space - L'espace des géométries.
	 * @param t_max - Temps de déplacement maximal d'un rayon.
	 * @param light_list - La liste des sources de lumières.
	 * @param specular_reflexion_algo - Le type d'algorithme pour réaliser le calcul de la réflexion spéculaire.
	 */
	public SPhongReflexionShader(SGeometrySpace geometry_space, double t_max, List<SLight> light_list, int specular_reflexion_algo)
	{
		super(geometry_space, t_max, light_list, specular_reflexion_algo);
	}
	
	@Override
	public SColor shade(SRay ray) throws SRuntimeException
	{
		if(ray.asIntersected())
			throw new SRuntimeException("Erreur SPhongReflexionShader 001 : Le rayon a déjà intersecté une géométrie préalablement.");
	
		// Effectuer l'intersection du rayon avec l'espace des géométries
		ray = geometry_space.nearestIntersection(ray, t_max);
		
		// Vérification de l'intersection et retourner la couleur noire s'il n'y a pas d'intersection
		if(!ray.asIntersected())
			return SIllumination.NO_ILLUMINATION;
		
		 // Obtenir le matériel de la primitive
    SMaterial material = ray.getGeometry().getPrimitiveParent().getMaterial(); 
    
    // Vérifier s'il y a un algorithme d'illumination (pas en mode no_light)
    if(reflexion_algo == NO_LIGHT)
      if(ray.asUV())
        return material.diffuseColor(ray.getUV()); 
      else
        return material.diffuseColor();  
    
		// Retourner l'illumination directe
		return directIllumination(ray, material);
	}

	/**
	 * Méthode permettant d'évaluer la couleur attribuée à un rayon par illumination directe.
	 * 
	 * @param ray Le rayon ayant réalisé une intersection avec une géométrie de l'espace des géométries.
	 * @param material Le matériel appliqué sur la géométrie.
	 * @return La couleur de l'illumination directe.
	 */
	protected SColor directIllumination(SRay ray, SMaterial material)
	{
	  // Faire une somme de couleur et débuter avec une couleur noire
    SColor color = SIllumination.NO_ILLUMINATION; 
                    
    // Iterer sur l'ensemble des lumières et ajouter leur contribution à la somme des couleurs.
    for(SLight light : light_list)
      color = color.add(shadeWithLight(ray, light, material));
      
    return color;
	}
	
}//fin classe SPhongReflexionShader
