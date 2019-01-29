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
 * La classe <b>SMultiVoxelSpace</b> repr�sente un espace de g�om�trie partitionn� en plusieurs niveaux de r�solution de voxel.
 * Une g�om�trie sera ainsi introduite dans un seul espace de g�om�trie en fonction de sa taille afin de regrouper les g�om�tries
 * de taille semblable.
 * 
 * @author Simon V�zina
 * @since 2015-11-25
 * @version 2016-02-14
 */
public class SMultiVoxelSpace extends SAbstractVoxelSpace {

  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>entry_list</b> repr�sente la liste des espaces de voxel sur lesquel il y aura it�ration pour effectuer les tests d'intersections.
   */
  private final List<SVoxelSpaceEntry> entry_list;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un espace de g�om�trie � taille de voxel multiple par d�faut. 
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
    // V�rifier si le rayon a d�j� intersect� une g�om�trie
    if(ray.asIntersected())
      throw new SRuntimeException("Erreur SMultiVoxelSpace 001 : Le rayon en param�tre a d�j� intersect� une g�om�trie.");
    
    // V�rifier la valeur de t_max est ad�quate
    if(t_max < 0.0)
      throw new SRuntimeException("Erreur SMultiVoxelSpace 002 : Le temps maximale ne peut pas �tre n�gative.");
   
    // V�rifier si l'espace a �t� initialis�
    if(!space_initialized)
      throw new SRuntimeException("Erreur SMultiVoxelSpace 003 : L'espace de g�om�tries de voxel n'a pas �t� initialis�.");
    
    //R�sultat de l'intersection avec la carte de voxel. Sera �gale � "ray" s'il y en a pas eu.
    SRay intersection_in_voxel = nearestIntersectionInVoxelMapList(ray, t_max);
    
    //R�sultat de l'intersection avec les g�om�tries hors voxel
    List<SRay> list_intersection_not_in_voxel = intersections(linear_list, ray, t_max);
    
    // Ajouter l'intersection de la carte de voxel � la liste lin�aire.
    // Cette liste sera ainsi pas vide.
    list_intersection_not_in_voxel.add(intersection_in_voxel);
    
    // Trier la liste pour le dernier �l�ment ajout�.
    Collections.sort(list_intersection_not_in_voxel); 
    
    // Retourner le permier �l�ment de la liste (sera sans intersection s'il n'y en a pas eu).
    return list_intersection_not_in_voxel.get(0);
  }

  /**
   * M�thode pour obtenir l'intersection la plus pr�s entre un rayon et des g�om�tries situ�es dans la liste des cartes de voxel.
   * 
   * @param ray - Le rayon � intersecter avec les g�om�tries.
   * @param t_max - Le temps maximal.
   * @return Le rayon avec les caract�ristiques de l'intersection (s'il y en a eu une).
   */
  private SRay nearestIntersectionInVoxelMapList(SRay ray, double t_max)
  {
    //R�aliser des calculs d'intersection seulement si la liste des cartes n'est pas vide
    if(entry_list.isEmpty())
      return ray;

    // D�finir le rayon ayant r�alis� l'intersection de moindre temps
    SRay minimum_ray = ray;
    
    // Construire la queue de priorit�
    PriorityQueue<SVoxelSpacePriorityEntry> priority_queue = buildPriorityQueue(ray, t_max);
           
    // It�rer tant qu'il y a des FTVA � parcourir dans la queue de priorit�
    while(!priority_queue.isEmpty())
    {
      // Obtenir le FTVA en priorit� : Celui dont le temps d'entr� dans son prochain voxel est le plus petit
      SVoxelSpacePriorityEntry entry = priority_queue.poll();
      
      SFastTraversalVoxelAlgorithm FTVA = entry.getFastTraversalVoxelAlgorithm();
      
      // Condition pour poursuivre l'it�ration :
      //----------------------------------------
      // - Le FTVA poss�de un prochain voxel.
      // - S'il y a eu une intersection, le prochain voxel poss�de un temps d'entr� inf�rieur au temps de l'intersection d�j� trouv�e.
      // - Le prochain voxel du FTVA n'a pas rencontr� d'intersection.
      
      if(FTVA.asNextVoxel())
        if(FTVA.nextMinTime() < minimum_ray.getT())
        {
          // Prochain voxel � tester par le FTVA
          SVoxel voxel = FTVA.nextVoxel();
          
          // Effectuer le test de l'intersection 
          SRay new_intersection = nearestIntersectionInVoxelMap(ray, t_max, entry.getData().getVoxelMap(), entry.getData().getVoxelBuilder(), voxel);
          
          // Remettre le FTVA dans la queue de priorit� s'il n'y a pas eu
          // de nouvelle intersection de trouv�e.
          if(!new_intersection.asIntersected())
            priority_queue.add(entry);            // *** CONDITION POUR POURSUIVRE L'IT�RATION DU FTVA ***
          else
            // Mise � jour de l'intersection si le temps est inf�rieur 
            if(new_intersection.getT() < minimum_ray.getT())
              minimum_ray = new_intersection;
        }
    }//fin while
    
    return minimum_ray;
  }
 
  @Override
  public List<SRay> nearestOpaqueIntersection(SRay ray, double t_max) throws SRuntimeException
  {
    // V�rifier si le rayon a d�j� intersect� une g�om�trie
    if(ray.asIntersected())
      throw new SRuntimeException("Erreur SMultiVoxelSpace 004 : Le rayon en param�tre a d�j� intersect� une g�om�trie.");
    
    // V�rifier la valeur de t_max est ad�quate
    if(t_max < 0.0)
      throw new SRuntimeException("Erreur SMultiVoxelSpace 005 : Le temps maximale ne peut pas �tre n�gative.");
   
    // V�rifier si l'espace a �t� initialis�
    if(!space_initialized)
      throw new SRuntimeException("Erreur SMultiVoxelSpace 006 : L'espace de g�om�tries de voxel n'a pas �t� initialis�.");
    
    // Obtenir la liste de l'intersection la plus pr�s � partir de la liste lin�aire des g�om�tries
    List<SRay> list = nearestOpaqueIntersection(linear_list, ray, t_max);
            
    // Retourner la fusion de la liste lin�raire avec la liste obtenue par la liste des cartes de voxels.
    return mergeNearestOpaqueIntersection(list, nearestOpaqueIntersectionInVoxelMapList(ray, t_max));
  }

  /**
   * M�thode pour obtenir l'intersection opaque la plus pr�s � partir de la liste des cartes de voxels.
   * 
   * @param ray - Le rayon � intersecter avec les g�om�tries.
   * @param t_max - Le temps maximal.
   * @return La liste contenant l'intersection opaque la plus pr�s.
   */
  private List<SRay> nearestOpaqueIntersectionInVoxelMapList(SRay ray, double t_max) 
  {
    // La liste � retourner
    List<SRay> return_list = new ArrayList<SRay>();
        
    //R�aliser des calculs d'intersection seulement si la liste des cartes n'est pas vide
    if(entry_list.isEmpty())
      return return_list;
    
    // D�finir un rayon r�alisant l'intersection opaque de moindre temps
    SRay minimum_opaque_ray = ray;
      
    // Construire la queue de priorit�
    PriorityQueue<SVoxelSpacePriorityEntry> priority_queue = buildPriorityQueue(ray, t_max);
        
    // It�rer sur les FTVA tant qu'il y en a de disponible dans la queue
    while(!priority_queue.isEmpty())
    {
      // Obtenir le FTVA en priorit� : Celui dont le temps d'entr� dans son prochain voxel est le plus petit
      SVoxelSpacePriorityEntry entry = priority_queue.poll();
        
      SFastTraversalVoxelAlgorithm FTVA = entry.getFastTraversalVoxelAlgorithm();
        
      // Condition pour poursuivre l'it�ration :
      //----------------------------------------
      // - Le FTVA poss�de un prochain voxel.
      // - S'il y a eu une intersection opaque, le prochain voxel poss�de un temps d'entr� inf�rieur au temps de l'intersection opaque d�j� trouv�e.
      // - Le prochain voxel du FTVA n'a pas rencontr� d'intersection opaque.
        
      if(FTVA.asNextVoxel())
        if(FTVA.nextMinTime() < minimum_opaque_ray.getT())
        {
          // Nous devons tester le prochain voxel du ce FTVA
          SVoxel voxel = FTVA.nextVoxel();
            
          List<SRay> list = nearestOpaqueIntersectionInVoxelMap(ray, t_max, entry.getData().getVoxelMap(), entry.getData().getVoxelBuilder(), voxel); 
            
          if(list.isEmpty())
            priority_queue.add(entry);      // *** CONDITION POUR POURSUIVRE L'IT�RATION DU FTVA ***
          else
          {
            // Ajouter la nouvelle liste de l'intersection opaque � la liste cumulative
            return_list = mergeNearestOpaqueIntersection(return_list, list);
              
            // Mettre � jour l'intersection opaque de moindre temps si le temps est inf�rieur
            if(list.get(0).getGeometry().isTransparent())
              priority_queue.add(entry);    //  *** CONDITION POUR POURSUIVRE L'IT�RATION DU FTVA ***
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
    // Obtenir la liste des g�om�tries o� le vecteur v s'y retrouve dans la liste lin�aire
    List<SGeometry> return_list = listInsideGeometry(linear_list, v);
    
    // Iterer sur l'ensemble des cartes de voxel et y ajouter les g�om�tries o� le vecteur v s'y retrouve
    for(SVoxelSpaceEntry e : entry_list)
      return_list.addAll(listInsideGeometryInMap(e.getVoxelMap(), e.getVoxelBuilder(), v));
    
    return return_list;
  }

  @Override
  public void initialize()
  {
    SLog.logWriteLine("Message SMultiVoxelSpace : Construction de l'espace des g�om�tries avec voxel multiple.");
        
    // S�parateur de la collection de g�om�trie
    //SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_BOX_AND_NO_BOX);
    //SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_HALF_AND_HALF);
    //SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_AT_AVERAGE_SIZE, 2);
    //SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_AT_AVERAGE_SIZE, 3);
    //SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_AT_AVERAGE_SIZE, 4);
    SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_AT_HALF_SIGMA, 3);
        
    // Obtenir la liste des bo�tes englobantes en liste s�par�e
    List<List<SBoundingBox>> split_list = splitter.getBoundingBoxSplitList();
    
    // Obtenir la liste des g�om�tries sans bo�te englobante et l'affecter � la liste lin�aire
    linear_list = splitter.getNoBoxList();
       
    // Compter les cartes de voxel NON VIDE
    int list_count = 0;  
    
    // Remplir les cartes de voxel pour chaque liste de g�om�tries tri�es
    for(List<SBoundingBox> l : split_list)
    {
      // S'assurer que la liste n'est pas vide, sinon il n'y a pas de carte de voxel � construire
      if(!l.isEmpty())
      {
        // Faire l'�valuation de la dimension des voxels et construire le g�n�rateur de voxel
        SVoxelDimensionEvaluator evaluator = new SVoxelDimensionEvaluator(l, SVoxelDimensionEvaluator.MID_AVERAGE_LENGHT_ALGORITHM);
        
        // G�n�rateur de voxel pour la carte de voxel en construction
        SVoxelBuilder voxel_builder = new SVoxelBuilder(evaluator.getDimension()); 
                
        // Construire la carte des voxels qui vont accueillir ces g�om�tries
        Map<SVoxel, List<SGeometry>> voxel_map = new HashMap<SVoxel, List<SGeometry>>();
               
        // Construire le voxel d'extr�me � la nouvelle carte et le mettre � jour � chaque insertion de g�om�trie
        SVoxel extremum_voxel = new SVoxel(0, 0, 0);
                
        // Iterer sur l'ensemble bo�tes englobante.
        // Int�grer leur voxels attitr�s avec leur g�om�trie � la carte des voxels.
        // Mettre � jour le voxel d'extr�me
        for(SBoundingBox box : l)
          extremum_voxel = addGeometryToMap(voxel_map, extremum_voxel, box.getGeometry(), voxel_builder.buildVoxel(box));     
        
        // Construire la cellule a carte de voxel et l'ajouter � la liste
        entry_list.add(new SVoxelSpaceEntry(voxel_map, voxel_builder, extremum_voxel));
        
        // Messages multiples � afficher
        SLog.logWriteLine("Message SMultiVoxelSpace : Construction de l'espace des voxels #" + (list_count+1) +".");
        SLog.logWriteLine("Message SMultiVoxelSpace : Nombre de g�om�tries dans la carte de voxels : " + l.size() + " g�om�tries.");
        SLog.logWriteLine("Message SMultiVoxelSpace : Taille des voxels : " + evaluator.getDimension() + " unit�s.");  
        SLog.logWriteLine("Message SMultiVoxelSpace : Nombre de r�f�rence � des g�om�tries : " + evaluateNbGeometryReference(voxel_map) + " r�f�rences.");
        SLog.logWriteLine("Message SMultiVoxelSpace : Nombre moyen de r�f�rence � des g�om�tries par voxel : " + evaluateNbGeometryReferencePerVoxel(voxel_map) + " r�f�rences/voxel.");
        
        SLog.logWriteLine();
        
        list_count++;
      }//fin if     
    }//fin for
     
    // Remarque : le comptage s'effectuant � la fin de la boucle for, le nombre est pr�sentement �gal � list_count (et non list_count+1)
    SLog.logWriteLine("Message SMultiVoxelSpace : Fin de la construction des " + (list_count) + " espaces multiples de voxels.");
    SLog.logWriteLine();
    
    space_initialized = true;
  }
  
  /**
   * M�thode pour construire la queue de priorit� permettant d'it�rer judicieusement sur l'ensemble des cartes de voxels.
   * 
   * @param ray - Le rayon � intersection avec les g�om�tries des voxels.
   * @param t_max - Le temps maximal que le rayon peut voyager des les voxels.
   * @return La queue de priorit� des cartes de voxels.
   */
  private PriorityQueue<SVoxelSpacePriorityEntry> buildPriorityQueue(SRay ray, double t_max)
  {
    // La queue de priorit�
    PriorityQueue<SVoxelSpacePriorityEntry> priority_queue = new PriorityQueue<SVoxelSpacePriorityEntry>();
          
    // Remplir la queue de priorit�
    for(SVoxelSpaceEntry e : entry_list)
      priority_queue.add( new SVoxelSpacePriorityEntry(e, new SFastTraversalVoxelAlgorithm(ray, t_max, e.getVoxelBuilder().getDimension() ,e.getAbsoluteExtremumVoxel())));
     
    return priority_queue;
  }
  
}//fin SMultiVoxelSpace
