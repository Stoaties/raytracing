/**
 * 
 */
package sim.graphics.light;


import java.util.List;
import java.util.LinkedList;

import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.geometry.SRay;
import sim.geometry.space.SGeometrySpace;
import sim.graphics.SColor;
import sim.graphics.material.SMaterial;
import sim.graphics.shader.SIllumination;
import sim.math.SVector3d;

/**
 * La classe <b>SShadewRay</b> repr�sente un rayon d'ombrage associ� � une source de lumi�re �clairant un point d'intersection.
 * 
 * @author Simon V�zina
 * @since 2015-01-21
 * @version 2017-08-22
 */
public class SShadowRay {

	final private boolean is_in_shadow;			//d�termine si la surface �clair�e est dans l'ombre de la source de lumi�re.
	final private SColor filtred_light_color;	//couleur de la source de lumi�re � l'endroit de l'intersection (ombrage, lumi�re filtr�e ou source de lumi�re intacte).
	
	/**
	 * Constructeur d'un rayon d'ombre associ� � une source de lumi�re.
	 * @param intersection_ray - Le rayon donnant acc�s � la surface intersect�e qui est �clair�e.
	 * @param light - La source de lumi�re �clairant la surface intersect�e.
	 * @param geometry_space - L'espace des g�om�tries de la sc�ne.
	 * @throws SRuntimeException - Si le rayon n'a pas intersect� de g�om�trie dans l'espace des g�om�tries.
	 */
	public SShadowRay(SRay intersection_ray, SLight light, SGeometrySpace geometry_space)throws SRuntimeException
	{
		if(!intersection_ray.asIntersected())
			throw new SRuntimeException("Erreur SShadowRay 001 : Le rayon n'a pas effectu� d'intersection avec une g�om�trie.");
		
		filtred_light_color = evaluateFilteredLightColor(intersection_ray, light, geometry_space);
		is_in_shadow = evaluateIsInShadow(filtred_light_color);
	}
	
	/**
	 * M�thode pour d�terminer si la surface d'intersection est situ�e dans l'ombre
	 * de la source de lumi�re associ�e � ce rayon d'ombre.
	 * @return <b>true</b> si la surface intersect�e est dans l'ombre de la source de lumi�re et <b>false</b> sinon.
	 */
	public boolean isInShadow()
	{ 
	  return is_in_shadow;
	}
	
	/**
	 * M�thode pour obtenir la couleur de la source de lumi�re apr�s filtrage caus� par son passage au travers des mat�riaux transparents. 
	 * @return la <b>couleur d'origine</b> de la lumi�re si aucune g�om�trie �tait entre la surface intersect�e et la source de lumi�re,
	 * la <b>couleur noire</b> s'il y a eu une g�om�trie opaque entre la surface intersect�e et la source de lumi�re (m�thode isInShadow() == true) et
	 * une <b>couleur filtr�e</b> s'il y a eu des g�om�tries transparentes entre la surface intersect�e et la source de lumi�re. 
	 */
	public SColor filtredLight()
	{ 
	  return filtred_light_color;
	}
	
	/**
	 * M�thode qui �value si la surface intersect�e est dans l'ombre de la source de lumi�re ou pas et d�termine
	 * la couleur filtr�e de la source de lumi�re si elle se rend � la surface intersect�e. 
	 * @param intersection_ray -  Le rayon donnant acc�s � la surface intersect�e � �clairer.
	 * @param light - La source de lumi�re �clairant la surface intersect�e.
	 * @param orientation_light - L'orientation de la source de lumi�re.
	 * @param distance - La distance entre la source de lumi�re et la surface intersect�e.
	 * @param geometry_space - L'espace des g�om�tries de la sc�ne.
	 */
	private SColor evaluateFilteredLightColor(SRay intersection_ray, SLight light, SGeometrySpace geometry_space)
	{
		//Cas de la source de lumi�re ambiante
		if(light instanceof SAmbientLight)
			return light.getColor();
		
		SVector3d d;		  //orientation de la source de source de lumi�re
		double distance;	//distance entre la source et la source de lumi�re
		
		//Cas de la source de lumi�re directionnelle
		if(light instanceof SDirectionalLight)
		{
			d = ((SDirectionalLight)light).getOrientation();
			distance = Double.POSITIVE_INFINITY;
		}
		else
			//Cas de la source de lumi�re ponctuelle
		  /*
		  // LIGNE CHANG�E POUR PERMETTRE AU SInterferenceLight d'�tre inclus ici !
		  if(light instanceof SPointLight)         
			{
				SPointLight point_light = (SPointLight)light;
				
				d = point_light.getOrientation(intersection_ray.getIntersectionPosition());
				distance = point_light.getPosition().substract(intersection_ray.getIntersectionPosition()).modulus();
			}
			*/
		  if(light instanceof SAttenuatedLight)         
      {
		    SAttenuatedLight point_light = (SAttenuatedLight)light;
        
        d = point_light.getOrientation(intersection_ray.getIntersectionPosition());
        distance = point_light.getPosition().substract(intersection_ray.getIntersectionPosition()).modulus();
      }
			else
				throw new SNoImplementationException("Erreur SShadowRay 002 : La source de lumi�re n'a pas �t� impl�ment�e.");
		  
		SVector3d L = d.multiply(-1.0);		//orientation inverse de la lumi�re
			
		// Tester si la normale est orient�e dans le m�me sens que la source de lumi�re 
		if(intersection_ray.getShadingNormal().dot(L) <= 0.0)
			return SIllumination.NO_ILLUMINATION;
		
		
			//Rayon de l'intersection � la source de lumi�re (avec indice de r�fraction de 1.0, car il n'y aura pas de calcul de r�fraction pour ce rayon)
			SRay intersection_to_light_ray = new SRay(intersection_ray.getIntersectionPosition(), L, SRay.DEFAULT_REFRACTIVE_INDEX);
			
			//Liste des g�om�tries rencontr�es avant d'atteindre la source de lumi�re 
			//en ordre inverse et d�butant par une g�om�trie opaque s'il y a eu intersection de ce type
			List<SRay> list_transparent = geometry_space.nearestOpaqueIntersection(intersection_to_light_ray, distance);
			
			//Test de la liste vide, il n'y a pas d'intersection, donc pas d'ombre ni lumi�re alt�r�e par des effets de transparence
			if(list_transparent.isEmpty())
				return light.getColor();
			else
			{
				//Si la liste poss�de une g�om�trie opaque (non transparente) au d�but, la lumiere sera bloqu�e
				if(!list_transparent.get(0).getGeometry().isTransparent())
					return SIllumination.NO_ILLUMINATION;
				else
				{
					List<SMaterial> material_list = new LinkedList<SMaterial>();
					
					for(SRay r : list_transparent)
						material_list.add(r.getGeometry().getPrimitiveParent().getMaterial());
						
					return SIllumination.filteredTransparencyLight(material_list, light.getColor());
				}
			}
			
	}
	
	/**
	 * M�thode qui d�termine si une couleur pass�e en param�tre est consid�r�e commme �tant dans l'ombre.
	 * @param c - La couleur � d�terminer si elle est dans l'ombrage.
	 * @return <b>true</b> si la couleur est 'noir' (0.0, 0.0, 0.0) et <b>false</b> sinon.
	 */
	private boolean evaluateIsInShadow(final SColor c)
	{
		if(c.equals(SIllumination.NO_ILLUMINATION))
			return true;
		else
			return false;
	}
	
}//fin classe SShadowRay
