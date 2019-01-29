/**
 * 
 */
package sim.geometry.space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sim.exception.SRuntimeException;
import sim.geometry.SGeometry;
import sim.geometry.SRay;
import sim.math.SVector3d;
import sim.util.SLog;

/**
 * <p>
 * La classe <b>SVoxelSpace</b> représentant un espace de géométries distribuées dans un espace de voxels (en trois dimensions).
 * Cette organisation permettra d'effecter l'intersection d'un rayon avec un nombre plus limités de géométries ce qui va accélérer les calculs.
 * </p> 
 * 
 * <p>
 * Distribuée dans une grille régulière de voxel, chaque géométrie sera localisée dans un ou plusieurs voxels 
 * et le lancer d'un rayon parcourera un nombre limité de voxels ce qui ainsi limitera le nombre de tests d'intersection.
 * </p>
 * 
 * @author Simon Vézina
 * @since 2015-08-04
 * @version 2016-04-04
 */
public class SVoxelSpace extends SAbstractVoxelSpace {

  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>voxel_map</b> correspond à la carte des voxels où sont situées des géométries admettant une boîte englobante.
   */
  private final Map<SVoxel, List<SGeometry>> voxel_map;  
  
  /**
   * La variable <b>voxel_builder</b> correspond au constructeur de voxel. 
   * La taille des voxels sera déterminée par un objet de type SVoxelDimensionEvaluator. 
   */
  private SVoxelBuilder voxel_builder;                   
  
  /**
   * La variable <b>absolute_extremum_voxel</b> correspond à un voxel de coordonnée extremums en valeur absolue à celle contenue dans la carte de voxel.
   * Ceci permet de connaître la taille maximal de la carte des voxels.
   */
  private SVoxel absolute_extremum_voxel;               
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un espace de voxel par défaut. 
   */
  public SVoxelSpace()
  {
    super();
    
    voxel_map = new HashMap<SVoxel, List<SGeometry>>();
    voxel_builder = null;
    absolute_extremum_voxel = null;
  }

  //------------
  // MÉTHODES //
  //------------
  
  @Override
  public SRay nearestIntersection(SRay ray, double t_max) throws SRuntimeException
  {
    // Vérifier si le rayon a déjà intersecté une géométrie
    if(ray.asIntersected())
      throw new SRuntimeException("Erreur SVoxelSpace 003 : Le rayon en paramètre a déjà intersecté une géométrie.");
    
    // Vérifier la valeur de t_max est adéquate
    if(t_max < 0.0)
      throw new SRuntimeException("Erreur SVoxelSpace 004 : Le temps maximale ne peut pas être négative.");
   
    // Vérifier si l'espace a été initialisé
    if(!space_initialized)
      throw new SRuntimeException("Erreur SVoxelSpace 005 : L'espace de géométries de voxel n'a pas été initialisé.");
    
    //Résultat de l'intersection avec la carte de voxel. Sera égale à "ray" s'il y en a pas eu.
    SRay intersection_in_voxel = nearestIntersectionInVoxelMap(ray, t_max);
    
    //Résultat de l'intersection avec les géométries hors voxel
    List<SRay> list_intersection_not_in_voxel = intersections(linear_list, ray, t_max);
    
    // Ajouter l'intersection de la carte de voxel à la liste linéaire.
    // Cette liste sera ainsi pas vide.
    list_intersection_not_in_voxel.add(intersection_in_voxel);
    
    // Trier la liste pour le dernier élément ajouté.
    Collections.sort(list_intersection_not_in_voxel); 
    
    // Retourner le permier élément de la liste (sera sans intersection s'il n'y en a pas eu).
    return list_intersection_not_in_voxel.get(0);
  }

  /**
   * Méthode pour obtenir l'intersection la plus près entre un rayon et des géométries situées dans la carte de voxel.
   * @param ray - Le rayon à intersecter avec les géométries.
   * @param t_max - Le temps maximal.
   * @return Le rayon avec les caractéristiques de l'intersection (s'il y en a eu une).
   */
  private SRay nearestIntersectionInVoxelMap(SRay ray, double t_max)
  {
    //Réaliser des calculs d'intersection avec les géométries de la carte uniquement si elle n'est pas vide
    if(!voxel_map.isEmpty())
    {
      //Créer la ligne de voxel à parcourir un à un
      SFastTraversalVoxelAlgorithm line_of_voxel = new SFastTraversalVoxelAlgorithm(ray, t_max, voxel_builder.getDimension(), absolute_extremum_voxel);
      
      //Faire l'itération sur la ligne de voxel depuis l'origine du rayon
      while(line_of_voxel.asNextVoxel())
      {
        SVoxel voxel = line_of_voxel.nextVoxel();
        
        // Faire le test de l'intersection des géométries situées dans le voxel en cours
        SRay ray_intersection = nearestIntersectionInVoxelMap(ray, t_max, voxel_map, voxel_builder, voxel);
        
        // Retourner le résultat de l'intersection s'il y en a une, car elle sera nécessairement de plus court temps
        if(ray_intersection.asIntersected())
          return ray_intersection;
      }
    }
    
    // Aucune intersection valide n'a été trouvée.
    return ray;
  }
  
  @Override
  public List<SRay> nearestOpaqueIntersection(SRay ray, double t_max) throws SRuntimeException
  {
    // Vérifier si le rayon a déjà intersecté une géométrie
    if(ray.asIntersected())
      throw new SRuntimeException("Erreur SVoxelSpace 006 : Le rayon en paramètre a déjà intersecté une géométrie.");
    
    // Vérifier la valeur de t_max est adéquate
    if(t_max < 0.0)
      throw new SRuntimeException("Erreur SVoxelSpace 007 : Le temps maximale ne peut pas être négative.");
   
    // Vérifier si l'espace a été initialisé
    if(!space_initialized)
      throw new SRuntimeException("Erreur SVoxelSpace 008 : L'espace de géométries de voxel n'a pas été initialisé.");
   
    // La liste déterminée dans la carte des géométries
    List<SRay> list_in_voxel = nearestOpaqueIntersectionInVoxelMap(ray, t_max);
    
    // La liste déterminée dans la liste linéaire des géométries
    List<SRay> list_not_in_voxel = nearestOpaqueIntersection(linear_list, ray, t_max);
    
    // La liste fusionnée adéquatement
    return mergeNearestOpaqueIntersection(list_in_voxel, list_not_in_voxel);    
  }

  /**
   * Méthode pour obtenir la liste des intersections transparente en ordre décroissant dont la plus éloigné (première de la liste) sera une géométrie opaque s'il y a eu intersection de cette nature.
   * 
   * @param ray - Le rayon à intersecter.
   * @param t_max - Le temps maximal pouvant être parcouru par le rayon.
   * @return La liste des intersections transparente en odre décroissant dont le premier élément sera une géométrie opaque s'il y a eu intersection de cette nature.
   */
  private List<SRay> nearestOpaqueIntersectionInVoxelMap(SRay ray, double t_max)
  {
    List<SRay> return_list = new ArrayList<SRay>();
    
    if(!voxel_map.isEmpty())
    {
      //Créer la ligne de voxel à parcourir un à un
      SFastTraversalVoxelAlgorithm line_of_voxel = new SFastTraversalVoxelAlgorithm(ray, t_max, voxel_builder.getDimension(), absolute_extremum_voxel);
      
      while(line_of_voxel.asNextVoxel())
      {
        SVoxel voxel = line_of_voxel.nextVoxel();
        
        // Obtenir la liste de l'intersection opaque associé au voxel courant
        List<SRay> list = nearestOpaqueIntersectionInVoxelMap(ray, t_max, voxel_map, voxel_builder, voxel);
        
        // Ajouter cette liste à la liste à retourner
        return_list = mergeNearestOpaqueIntersection(return_list, list);
        
        // Retourner cette liste si l'intersection opaque a déjà été trouvée.
        if(!return_list.isEmpty())
          if(!return_list.get(0).getGeometry().isTransparent())
            return return_list;
      }
    }
    
    // La liste est vide ou elle contient uniquement des géométries transparentes
    return return_list;
  }
  
  @Override
  public List<SGeometry> listInsideGeometry(SVector3d v)
  {
    // Vérifier que l'initialisation a été complétée
    if(!space_initialized)
      throw new SRuntimeException("Erreur SVoxelSpace 009 : L'espace de voxel n'a pas été initialisé.");
    
    // Liste des géométries où le vecteur v sera situé à l'intérieur.
    // Débutons avec la liste disponible à partir des informations de la carte des voxels.
    List<SGeometry> inside_list = listInsideGeometryInMap(voxel_map, voxel_builder, v);
    
    // Ajouter les géométries sans boîte où le vecteur v s'y retrouve.
    inside_list.addAll(listInsideGeometry(linear_list, v));
        
    return inside_list;
  }

  @Override
  public void initialize()
  {
    SLog.logWriteLine("Message SVoxelSpace : Construction de l'espace des géométries avec voxel.");
    
    // Séparateur de la collection de géométrie
    SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_BOX_AND_NO_BOX);
    
    // Obtenir la liste des géométries sans boîte englobante et l'affecter à la liste linéaire
    linear_list = splitter.getNoBoxList();
    
    // Liste des boîtes englobantes autour des géométries de la liste.
    // Nous commencons pas une liste vide afin qu'elle soit remplacée par celle donnée par le "splitter".
    List<SBoundingBox> bounding_box_list = new ArrayList<SBoundingBox>();   
      
    // Vérifier qu'il y a au moins une liste de liste de boîtes englobantes dans le "splitter".
    // Puisque le type de séparation génère uniquement une liste, alors nous pourrons l'utiliser et elle ne sera pas vide.
    if(!splitter.getBoundingBoxSplitList().isEmpty())
      bounding_box_list = splitter.getBoundingBoxSplitList().get(0);
        
    // S'assurer que la liste des boîtes n'est pas vide, sinon il n'y a pas de carte de voxel à construire
    if(!bounding_box_list.isEmpty())
    {
      // Faire l'évaluation de la dimension des voxels et construire le générateur de voxel
      //-------------------------------------------------------------------------------------------------------------------------------------------------
      //SVoxelDimensionEvaluator evaluator = new SVoxelDimensionEvaluator(bounding_box_list, SVoxelDimensionEvaluator.ONE_FOR_ONE_ALGORITHM);
      //SVoxelDimensionEvaluator evaluator = new SVoxelDimensionEvaluator(bounding_box_list, SVoxelDimensionEvaluator.BIGGEST_AVERAGE_LENGHT_ALGORITHM);
      SVoxelDimensionEvaluator evaluator = new SVoxelDimensionEvaluator(bounding_box_list, SVoxelDimensionEvaluator.MID_AVERAGE_LENGHT_ALGORITHM);
      //SVoxelDimensionEvaluator evaluator = new SVoxelDimensionEvaluator(bounding_box_list, SVoxelDimensionEvaluator.SMALLEST_AVERAGE_LENGHT_ALGORITHM); 
      
      // Générateur de voxel pour la carte de voxel en construction
      voxel_builder = new SVoxelBuilder(evaluator.getDimension()); 
            
      // Construire le voxel d'extrême à la nouvelle carte et le mettre à jour à chaque insertion de géométrie
      absolute_extremum_voxel = new SVoxel(0, 0, 0);
              
      // Iterer sur l'ensemble boîtes englobante.
      // Intégrer leur voxels attitrés avec leur géométrie à la carte des voxels.
      // Mettre à jour le voxel d'extrême
      for(SBoundingBox box : bounding_box_list)
        absolute_extremum_voxel = addGeometryToMap(voxel_map, absolute_extremum_voxel, box.getGeometry(), voxel_builder.buildVoxel(box));     
      
      // Messages multiples à afficher
      SLog.logWriteLine("Message SVoxelSpace : Nombre de géométries dans la carte de voxels : " + bounding_box_list.size() + " géométries.");
      SLog.logWriteLine("Message SVoxelSpace : Taille des voxels : " + evaluator.getDimension() + " unités.");  
      SLog.logWriteLine("Message SVoxelSpace : Nombre de référence à des géométries : " + evaluateNbGeometryReference(voxel_map) + " références.");
      SLog.logWriteLine("Message SVoxelSpace : Nombre moyen de référence à des géométries par voxel : " + evaluateNbGeometryReferencePerVoxel(voxel_map) + " références/voxel.");
      
      SLog.logWriteLine();
    }//fin if
    else
    {
      // Il n'y a pas de boîte englobante de disponible pour l'espace avec voxel
      SLog.logWriteLine("Message SVoxelSpace : Aucune géométrie ne possède de boîte englobante! Le choix d'un espace de géométries en voxel devient inéfficace.");
     
      voxel_builder = null;   // Il n'y a pas de constructeur de voxel disponible
    }
    
    SLog.logWriteLine("Message SVoxelSpace : Fin de la construction de l'espace des géométries avec voxel.");
    SLog.logWriteLine();
    
    // Initialisation de l'espace des voxels complétée
    space_initialized = true;
  }
  
}//fin de la classe SVoxelSpace
