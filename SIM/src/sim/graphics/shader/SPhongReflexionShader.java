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
 * La classe <b>SPhongReflexionShader</b> repr�sente un shader selon le mod�le de r�flexion de phong. 
 * Ce shader fait l'analyse d'un rayon principal avec un mod�le de r�flexion ambiante, diffuse et sp�culaire.
 * 
 * @author Simon V�zina
 * @since 2015-02-03
 * @version 2016-05-06
 */
public class SPhongReflexionShader extends SAbstractShader {

	/**
	 * Constructeur d'un shader selon le mod�le de r�flexion de phong avec le mod�le de r�flexion sp�culaire de <b><i>Blinn</i></b>.
	 * @param geometry_space - L'espace des g�om�tries.
	 * @param t_max - Temps de d�placement maximal d'un rayon.
	 * @param light_list - La liste des sources de lumi�res.
	 */
	public SPhongReflexionShader(SGeometrySpace geometry_space, double t_max, List<SLight> light_list)
	{
		super(geometry_space, t_max, light_list);
	}

	/**
	 * Constructeur d'un shader selon le mod�le de r�flexion de phong.
	 * @param geometry_space - L'espace des g�om�tries.
	 * @param t_max - Temps de d�placement maximal d'un rayon.
	 * @param light_list - La liste des sources de lumi�res.
	 * @param specular_reflexion_algo - Le type d'algorithme pour r�aliser le calcul de la r�flexion sp�culaire.
	 */
	public SPhongReflexionShader(SGeometrySpace geometry_space, double t_max, List<SLight> light_list, int specular_reflexion_algo)
	{
		super(geometry_space, t_max, light_list, specular_reflexion_algo);
	}
	
	@Override
	public SColor shade(SRay ray) throws SRuntimeException
	{
		if(ray.asIntersected())
			throw new SRuntimeException("Erreur SPhongReflexionShader 001 : Le rayon a d�j� intersect� une g�om�trie pr�alablement.");
	
		// Effectuer l'intersection du rayon avec l'espace des g�om�tries
		ray = geometry_space.nearestIntersection(ray, t_max);
		
		// V�rification de l'intersection et retourner la couleur noire s'il n'y a pas d'intersection
		if(!ray.asIntersected())
			return SIllumination.NO_ILLUMINATION;
		
		 // Obtenir le mat�riel de la primitive
    SMaterial material = ray.getGeometry().getPrimitiveParent().getMaterial(); 
    
    // V�rifier s'il y a un algorithme d'illumination (pas en mode no_light)
    if(reflexion_algo == NO_LIGHT)
      if(ray.asUV())
        return material.diffuseColor(ray.getUV()); 
      else
        return material.diffuseColor();  
    
		// Retourner l'illumination directe
		return directIllumination(ray, material);
	}

	/**
	 * M�thode permettant d'�valuer la couleur attribu�e � un rayon par illumination directe.
	 * 
	 * @param ray Le rayon ayant r�alis� une intersection avec une g�om�trie de l'espace des g�om�tries.
	 * @param material Le mat�riel appliqu� sur la g�om�trie.
	 * @return La couleur de l'illumination directe.
	 */
	protected SColor directIllumination(SRay ray, SMaterial material)
	{
	  // Faire une somme de couleur et d�buter avec une couleur noire
    SColor color = SIllumination.NO_ILLUMINATION; 
                    
    // Iterer sur l'ensemble des lumi�res et ajouter leur contribution � la somme des couleurs.
    for(SLight light : light_list)
      color = color.add(shadeWithLight(ray, light, material));
      
    return color;
	}
	
}//fin classe SPhongReflexionShader
