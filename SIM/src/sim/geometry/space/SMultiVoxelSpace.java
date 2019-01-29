/**
 * 
 */
package sim.geometry.space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import sim.exception.SRuntimeException;
import sim.geometry.SGeometry;
import sim.geometry.SRay;
import sim.math.SVector3d;
import sim.util.SLog;

/**
 * La classe <b>SMultiVoxelSpace</b> représente un espace de géométrie partitionné en plusieurs niveaux de résolution de voxel.
 * Une géométrie sera ainsi introduite dans un seul espace de géométrie en fonction de sa taille afin de regrouper les géométries
 * de taille semblable.
 * 
 * @author Simon Vézina
 * @since 2015-11-25
 * @version 2016-02-14
 */
public class SMultiVoxelSpace extends SAbstractVoxelSpace {

  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>entry_list</b> représente la liste des espaces de voxel sur lesquel il y aura itération pour effectuer les tests d'intersections.
   */
  private final List<SVoxelSpaceEntry> entry_list;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un espace de géométrie à taille de voxel multiple par défaut. 
   */
  public SMultiVoxelSpace()
  {
    super();
    
    entry_list = new ArrayList<SVoxelSpaceEntry>();
        
    space_initialized = false;
  }

  @Override
  public SRay nearestIntersection(SRay ray, double t_max) throws SRuntimeException
  {
    // Vérifier si le rayon a déjà intersecté une géométrie
    if(ray.asIntersected())
      throw new SRuntimeException("Erreur SMultiVoxelSpace 001 : Le rayon en paramètre a déjà intersecté une géométrie.");
    
    // Vérifier la valeur de t_max est adéquate
    if(t_max < 0.0)
      throw new SRuntimeException("Erreur SMultiVoxelSpace 002 : Le temps maximale ne peut pas être négative.");
   
    // Vérifier si l'espace a été initialisé
    if(!space_initialized)
      throw new SRuntimeException("Erreur SMultiVoxelSpace 003 : L'espace de géométries de voxel n'a pas été initialisé.");
    
    //Résultat de l'intersection avec la carte de voxel. Sera égale à "ray" s'il y en a pas eu.
    SRay intersection_in_voxel = nearestIntersectionInVoxelMapList(ray, t_max);
    
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
   * Méthode pour obtenir l'intersection la plus près entre un rayon et des géométries situées dans la liste des cartes de voxel.
   * 
   * @param ray - Le rayon à intersecter avec les géométries.
   * @param t_max - Le temps maximal.
   * @return Le rayon avec les caractéristiques de l'intersection (s'il y en a eu une).
   */
  private SRay nearestIntersectionInVoxelMapList(SRay ray, double t_max)
  {
    //Réaliser des calculs d'intersection seulement si la liste des cartes n'est pas vide
    if(entry_list.isEmpty())
      return ray;

    // Définir le rayon ayant réalisé l'intersection de moindre temps
    SRay minimum_ray = ray;
    
    // Construire la queue de priorité
    PriorityQueue<SVoxelSpacePriorityEntry> priority_queue = buildPriorityQueue(ray, t_max);
           
    // Itérer tant qu'il y a des FTVA à parcourir dans la queue de priorité
    while(!priority_queue.isEmpty())
    {
      // Obtenir le FTVA en priorité : Celui dont le temps d'entré dans son prochain voxel est le plus petit
      SVoxelSpacePriorityEntry entry = priority_queue.poll();
      
      SFastTraversalVoxelAlgorithm FTVA = entry.getFastTraversalVoxelAlgorithm();
      
      // Condition pour poursuivre l'itération :
      //----------------------------------------
      // - Le FTVA possède un prochain voxel.
      // - S'il y a eu une intersection, le prochain voxel possède un temps d'entré inférieur au temps de l'intersection déjà trouvée.
      // - Le prochain voxel du FTVA n'a pas rencontré d'intersection.
      
      if(FTVA.asNextVoxel())
        if(FTVA.nextMinTime() < minimum_ray.getT())
        {
          // Prochain voxel à tester par le FTVA
          SVoxel voxel = FTVA.nextVoxel();
          
          // Effectuer le test de l'intersection 
          SRay new_intersection = nearestIntersectionInVoxelMap(ray, t_max, entry.getData().getVoxelMap(), entry.getData().getVoxelBuilder(), voxel);
          
          // Remettre le FTVA dans la queue de priorité s'il n'y a pas eu
          // de nouvelle intersection de trouvée.
          if(!new_intersection.asIntersected())
            priority_queue.add(entry);            // *** CONDITION POUR POURSUIVRE L'ITÉRATION DU FTVA ***
          else
            // Mise à jour de l'intersection si le temps est inférieur 
            if(new_intersection.getT() < minimum_ray.getT())
              minimum_ray = new_intersection;
        }
    }//fin while
    
    return minimum_ray;
  }
 
  @Override
  public List<SRay> nearestOpaqueIntersection(SRay ray, double t_max) throws SRuntimeException
  {
    // Vérifier si le rayon a déjà intersecté une géométrie
    if(ray.asIntersected())
      throw new SRuntimeException("Erreur SMultiVoxelSpace 004 : Le rayon en paramètre a déjà intersecté une géométrie.");
    
    // Vérifier la valeur de t_max est adéquate
    if(t_max < 0.0)
      throw new SRuntimeException("Erreur SMultiVoxelSpace 005 : Le temps maximale ne peut pas être négative.");
   
    // Vérifier si l'espace a été initialisé
    if(!space_initialized)
      throw new SRuntimeException("Erreur SMultiVoxelSpace 006 : L'espace de géométries de voxel n'a pas été initialisé.");
    
    // Obtenir la liste de l'intersection la plus près à partir de la liste linéaire des géométries
    List<SRay> list = nearestOpaqueIntersection(linear_list, ray, t_max);
            
    // Retourner la fusion de la liste linéraire avec la liste obtenue par la liste des cartes de voxels.
    return mergeNearestOpaqueIntersection(list, nearestOpaqueIntersectionInVoxelMapList(ray, t_max));
  }

  /**
   * Méthode pour obtenir l'intersection opaque la plus près à partir de la liste des cartes de voxels.
   * 
   * @param ray - Le rayon à intersecter avec les géométries.
   * @param t_max - Le temps maximal.
   * @return La liste contenant l'intersection opaque la plus près.
   */
  private List<SRay> nearestOpaqueIntersectionInVoxelMapList(SRay ray, double t_max) 
  {
    // La liste à retourner
    List<SRay> return_list = new ArrayList<SRay>();
        
    //Réaliser des calculs d'intersection seulement si la liste des cartes n'est pas vide
    if(entry_list.isEmpty())
      return return_list;
    
    // Définir un rayon réalisant l'intersection opaque de moindre temps
    SRay minimum_opaque_ray = ray;
      
    // Construire la queue de priorité
    PriorityQueue<SVoxelSpacePriorityEntry> priority_queue = buildPriorityQueue(ray, t_max);
        
    // Itérer sur les FTVA tant qu'il y en a de disponible dans la queue
    while(!priority_queue.isEmpty())
    {
      // Obtenir le FTVA en priorité : Celui dont le temps d'entré dans son prochain voxel est le plus petit
      SVoxelSpacePriorityEntry entry = priority_queue.poll();
        
      SFastTraversalVoxelAlgorithm FTVA = entry.getFastTraversalVoxelAlgorithm();
        
      // Condition pour poursuivre l'itération :
      //----------------------------------------
      // - Le FTVA possède un prochain voxel.
      // - S'il y a eu une intersection opaque, le prochain voxel possède un temps d'entré inférieur au temps de l'intersection opaque déjà trouvée.
      // - Le prochain voxel du FTVA n'a pas rencontré d'intersection opaque.
        
      if(FTVA.asNextVoxel())
        if(FTVA.nextMinTime() < minimum_opaque_ray.getT())
        {
          // Nous devons tester le prochain voxel du ce FTVA
          SVoxel voxel = FTVA.nextVoxel();
            
          List<SRay> list = nearestOpaqueIntersectionInVoxelMap(ray, t_max, entry.getData().getVoxelMap(), entry.getData().getVoxelBuilder(), voxel); 
            
          if(list.isEmpty())
            priority_queue.add(entry);      // *** CONDITION POUR POURSUIVRE L'ITÉRATION DU FTVA ***
          else
          {
            // Ajouter la nouvelle liste de l'intersection opaque à la liste cumulative
            return_list = mergeNearestOpaqueIntersection(return_list, list);
              
            // Mettre à jour l'intersection opaque de moindre temps si le temps est inférieur
            if(list.get(0).getGeometry().isTransparent())
              priority_queue.add(entry);    //  *** CONDITION POUR POURSUIVRE L'ITÉRATION DU FTVA ***
            else
              if(list.get(0).getT() < minimum_opaque_ray.getT())
                minimum_opaque_ray = list.get(0);
          }
        }
    }//fin while
           
    return return_list;
  }
  
  @Override
  public List<SGeometry> listInsideGeometry(SVector3d v)
  {
    // Obtenir la liste des géométries où le vecteur v s'y retrouve dans la liste linéaire
    List<SGeometry> return_list = listInsideGeometry(linear_list, v);
    
    // Iterer sur l'ensemble des cartes de voxel et y ajouter les géométries où le vecteur v s'y retrouve
    for(SVoxelSpaceEntry e : entry_list)
      return_list.addAll(listInsideGeometryInMap(e.getVoxelMap(), e.getVoxelBuilder(), v));
    
    return return_list;
  }

  @Override
  public void initialize()
  {
    SLog.logWriteLine("Message SMultiVoxelSpace : Construction de l'espace des géométries avec voxel multiple.");
        
    // Séparateur de la collection de géométrie
    //SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_BOX_AND_NO_BOX);
    //SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_HALF_AND_HALF);
    //SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_AT_AVERAGE_SIZE, 2);
    //SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_AT_AVERAGE_SIZE, 3);
    //SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_AT_AVERAGE_SIZE, 4);
    SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_AT_HALF_SIGMA, 3);
        
    // Obtenir la liste des boîtes englobantes en liste séparée
    List<List<SBoundingBox>> split_list = splitter.getBoundingBoxSplitList();
    
    // Obtenir la liste des géométries sans boîte englobante et l'affecter à la liste linéaire
    linear_list = splitter.getNoBoxList();
       
    // Compter les cartes de voxel NON VIDE
    int list_count = 0;  
    
    // Remplir les cartes de voxel pour chaque liste de géométries triées
    for(List<SBoundingBox> l : split_list)
    {
      // S'assurer que la liste n'est pas vide, sinon il n'y a pas de carte de voxel à construire
      if(!l.isEmpty())
      {
        // Faire l'évaluation de la dimension des voxels et construire le générateur de voxel
        SVoxelDimensionEvaluator evaluator = new SVoxelDimensionEvaluator(l, SVoxelDimensionEvaluator.MID_AVERAGE_LENGHT_ALGORITHM);
        
        // Générateur de voxel pour la carte de voxel en construction
        SVoxelBuilder voxel_builder = new SVoxelBuilder(evaluator.getDimension()); 
                
        // Construire la carte des voxels qui vont accueillir ces géométries
        Map<SVoxel, List<SGeometry>> voxel_map = new HashMap<SVoxel, List<SGeometry>>();
               
        // Construire le voxel d'extrême à la nouvelle carte et le mettre à jour à chaque insertion de géométrie
        SVoxel extremum_voxel = new SVoxel(0, 0, 0);
                
        // Iterer sur l'ensemble boîtes englobante.
        // Intégrer leur voxels attitrés avec leur géométrie à la carte des voxels.
        // Mettre à jour le voxel d'extrême
        for(SBoundingBox box : l)
          extremum_voxel = addGeometryToMap(voxel_map, extremum_voxel, box.getGeometry(), voxel_builder.buildVoxel(box));     
        
        // Construire la cellule a carte de voxel et l'ajouter à la liste
        entry_list.add(new SVoxelSpaceEntry(voxel_map, voxel_builder, extremum_voxel));
        
        // Messages multiples à afficher
        SLog.logWriteLine("Message SMultiVoxelSpace : Construction de l'espace des voxels #" + (list_count+1) +".");
        SLog.logWriteLine("Message SMultiVoxelSpace : Nombre de géométries dans la carte de voxels : " + l.size() + " géométries.");
        SLog.logWriteLine("Message SMultiVoxelSpace : Taille des voxels : " + evaluator.getDimension() + " unités.");  
        SLog.logWriteLine("Message SMultiVoxelSpace : Nombre de référence à des géométries : " + evaluateNbGeometryReference(voxel_map) + " références.");
        SLog.logWriteLine("Message SMultiVoxelSpace : Nombre moyen de référence à des géométries par voxel : " + evaluateNbGeometryReferencePerVoxel(voxel_map) + " références/voxel.");
        
        SLog.logWriteLine();
        
        list_count++;
      }//fin if     
    }//fin for
     
    // Remarque : le comptage s'effectuant à la fin de la boucle for, le nombre est présentement égal à list_count (et non list_count+1)
    SLog.logWriteLine("Message SMultiVoxelSpace : Fin de la construction des " + (list_count) + " espaces multiples de voxels.");
    SLog.logWriteLine();
    
    space_initialized = true;
  }
  
  /**
   * Méthode pour construire la queue de priorité permettant d'itérer judicieusement sur l'ensemble des cartes de voxels.
   * 
   * @param ray - Le rayon à intersection avec les géométries des voxels.
   * @param t_max - Le temps maximal que le rayon peut voyager des les voxels.
   * @return La queue de priorité des cartes de voxels.
   */
  private PriorityQueue<SVoxelSpacePriorityEntry> buildPriorityQueue(SRay ray, double t_max)
  {
    // La queue de priorité
    PriorityQueue<SVoxelSpacePriorityEntry> priority_queue = new PriorityQueue<SVoxelSpacePriorityEntry>();
          
    // Remplir la queue de priorité
    for(SVoxelSpaceEntry e : entry_list)
      priority_queue.add( new SVoxelSpacePriorityEntry(e, new SFastTraversalVoxelAlgorithm(ray, t_max, e.getVoxelBuilder().getDimension() ,e.getAbsoluteExtremumVoxel())));
     
    return priority_queue;
  }
  
}//fin SMultiVoxelSpace
