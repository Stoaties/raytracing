/**
 * 
 */
package sim.graphics.shader;

import java.util.List;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.geometry.SRay;
import sim.geometry.space.SGeometrySpace;
import sim.graphics.SColor;
import sim.graphics.light.SLight;
import sim.graphics.material.SMaterial;
import sim.math.SVector3d;
import sim.physics.SGeometricalOptics;

/**
 * La classe <b>SRecursiveShader</b> représente un shader avec lancé de rayon récursif.
 * 
 * @author Simon Vézina
 * @since 2015-02-03
 * @version 2017-06-04
 */
public class SRecursiveShader extends SPhongReflexionShader {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>MINIMAL_MAX_DEPT</b> correspond au niveau de récursivité minimale étant égal à {@value}.
   */
	private static final int MINIMAL_MAX_DEPT = 1; 
	
	//-------------
	// VARIABLES //
	//-------------
	
	/**
	 * La variable <b>max_dept</b> correspond au niveau de récursivité qui sera utilisé lors des calculs d'illuminations.
	 */
  private final int max_dept;	
	
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
	/**
	 * Constructeur d'un shader avec lancé de rayon récursif.
	 * 
	 * @param geometry_space L'espace des géométries.
	 * @param t_max Le temps de déplacement maximal d'un rayon.
	 * @param light_list La liste des sources de lumières.
	 * @param reflexion_algo Le type d'algorithme pour réaliser le calcul de la réflexion.
	 * @param max_dept - Le niveau de rayon récursif.
	 * @throws SConstructorException Si le niveau de récursivité des rayons est inférieur au seuil minimal (habituellement 1).
   */
	public SRecursiveShader(SGeometrySpace geometry_space, double t_max, List<SLight> light_list, int reflexion_algo, int max_dept)throws SConstructorException 
	{
		super(geometry_space, t_max, light_list, reflexion_algo);
		
		if(max_dept < MINIMAL_MAX_DEPT)
      throw new SConstructorException("Erreur SRecursiveShader 002 : Le niveau '" + max_dept + "' de récursivité des rayons doit être supérieur à '" + MINIMAL_MAX_DEPT + "'.");
    
		this.max_dept = max_dept;
	}

	//------------
	// MÉTHODES //
	//------------
	
	@Override
	public SColor shade(SRay ray) throws SRuntimeException 
	{
		if(ray.asIntersected())
			throw new SRuntimeException("Erreur SRecursiveShader 003 : Le rayon a déjà intersecté une géométrie préalablement.");
		    
		//Lancer un rayon de niveau de récursivité 1
		return recursiveShade(ray, 1);
	}

	/**
	 * Méthode qui effectue l'illumination d'un rayon récursivement.
	 * 
	 * @param ray Le rayon à illuminer.
	 * @param depth Le niveau de récursivité du rayon.
	 * @return La couleur associée à l'illumination du rayon récursif.
	 */
	private SColor recursiveShade(SRay ray, int depth)
	{
		// Si le niveau de rayon récursif est trop profond, tout arrêter et retourner la couleur noire
		if(depth > max_dept)
			return SIllumination.NO_ILLUMINATION;
		
		// Effectuer l'intersection avec l'espace des géométries
		ray = geometry_space.nearestIntersection(ray, t_max);
			
		// S'il n'y a pas d'intersection, la couleur affectée sera noire
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
   
		// Constribution de l'illumination directe
		SColor color = directIllumination(ray, material);
			
		// Contribution de l'illumination indirecte (avec récursivité potentielle)
		color = color.add(indirectIllumination(ray, material, depth));
			
		return color;
	}
	
	/**
	 * Méthode permettant d'évaluer la couleur attribuée à un rayon par illumination indirecte.
	 * @param ray Le rayon ayant réalisé une intersection avec une géométrie de l'espace des géométries.
	 * @param material Le matériel appliqué sur la géométrie intersecté par le rayon.
	 * @param depth Le niveau de récursivité du rayon.
	 * @return La couleur de l'illumination indirecte.
	 */
	protected SColor indirectIllumination(SRay ray, SMaterial material, int depth)
	{
	  // La somme des couleurs par réflexion et réfraction
	  SColor color = SIllumination.NO_ILLUMINATION;
	  
	  //-----------------------------------------------
    //  Illumination indirecte : Loi de la réflexion  
    //-----------------------------------------------
    if(material.isReflective())
    {
      // Évaluer la nouvelle orientation du rayon selon la loi de la réflexion.
      SVector3d R = SGeometricalOptics.reflexion(ray.getDirection(), ray.getShadingNormal());
    
      // Rayon de réflexion, sans changement de milieu (indice de réfraction du rayon intersecté)
      SRay reflexion_ray = ray.castRecursiveRay(R, ray.getRefractiveIndex());
            
      // Ajouter la luminosité réflexive à la somme des couleurs en augmentant le niveau de 1
      color = color.add(recursiveShade(reflexion_ray, depth+1).multiply(material.reflectivity()));
    }
      
    //------------------------------------------------
    //  Illumination indirecte : Loi de la réfraction
    //------------------------------------------------
    if(material.isTransparent())
    {
      // Réfraction uniquement si la géométrie intersectée est une géométrie fermée (pas une surface)
      if(ray.getGeometry().isClosedGeometry())
      {
        // Indice de réfraction à déterminer (n1 = incident, n2 = réfracté)
        double n1 = ray.getRefractiveIndex();
          
        //Pour déterminer n2, il y a deux scénarios à considérer :
        //1) L'intersection vient de l'extérieur, donc n2 sera l'indice interne de la géométrie intersectée.
        //2) L'intersection vient de l'intérieur, donc on utilise le shader pour déterminer l'indice à l'extérieur de la géométrie intersectée de l'intérieur.
          double n2;
          
          boolean from_outside;
          
          if(!ray.isInsideIntersection())                                  //de l'extérieur
          {
            n2 = material.refractiveIndex();
            from_outside = true;
          }
          else                                                             //de l'intérieur
          {
            n2 = evaluateRefractiveIndex(ray.getIntersectionPosition());
            from_outside = false;
          }
          
          // Vérifier qu'il n'y a pas réflexion totale interne
          if(!SGeometricalOptics.isTotalInternalReflection(ray.getDirection(), ray.getShadingNormal(), n1, n2))
          {
            // Évaluer l'orientation du rayon selon la loi de la réfraction.
            SVector3d T = SGeometricalOptics.refraction(ray.getDirection(), ray.getShadingNormal(), n1, n2);
          
            // Rayon de réfraction avec changement de milieu (indice de réfraction n2)
            SRay refraction_ray = ray.castRecursiveRay(T, n2);
            
            // Ajouter la luminosité réfractive à la somme des couleurs en augmentant le niveau de 1
            // Venant de l'extérieur, nous allons appliquer un filtrage à la couleur
            if(from_outside)
              color = color.add(recursiveShade(refraction_ray, depth+1).multiply(material.transparencyColor()));  // couleur avec filtrage
            else  
              color = color.add(recursiveShade(refraction_ray, depth+1).multiply(material.transparency()));       // couleur déjà filtrée
          }
        }
      }
    
    return color;
	}
	
}//fin classe SRecursiveShader
