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
 * La classe <b>SVoxelSpace</b> repr�sentant un espace de g�om�tries distribu�es dans un espace de voxels (en trois dimensions).
 * Cette organisation permettra d'effecter l'intersection d'un rayon avec un nombre plus limit�s de g�om�tries ce qui va acc�l�rer les calculs.
 * </p> 
 * 
 * <p>
 * Distribu�e dans une grille r�guli�re de voxel, chaque g�om�trie sera localis�e dans un ou plusieurs voxels 
 * et le lancer d'un rayon parcourera un nombre limit� de voxels ce qui ainsi limitera le nombre de tests d'intersection.
 * </p>
 * 
 * @author Simon V�zina
 * @since 2015-08-04
 * @version 2016-04-04
 */
public class SVoxelSpace extends SAbstractVoxelSpace {

  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>voxel_map</b> correspond � la carte des voxels o� sont situ�es des g�om�tries admettant une bo�te englobante.
   */
  private final Map<SVoxel, List<SGeometry>> voxel_map;  
  
  /**
   * La variable <b>voxel_builder</b> correspond au constructeur de voxel. 
   * La taille des voxels sera d�termin�e par un objet de type SVoxelDimensionEvaluator. 
   */
  private SVoxelBuilder voxel_builder;                   
  
  /**
   * La variable <b>absolute_extremum_voxel</b> correspond � un voxel de coordonn�e extremums en valeur absolue � celle contenue dans la carte de voxel.
   * Ceci permet de conna�tre la taille maximal de la carte des voxels.
   */
  private SVoxel absolute_extremum_voxel;               
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un espace de voxel par d�faut. 
   */
  public SVoxelSpace()
  {
    super();
    
    voxel_map = new HashMap<SVoxel, List<SGeometry>>();
    voxel_builder = null;
    absolute_extremum_voxel = null;
  }

  //------------
  // M�THODES //
  //------------
  
  @Override
  public SRay nearestIntersection(SRay ray, double t_max) throws SRuntimeException
  {
    // V�rifier si le rayon a d�j� intersect� une g�om�trie
    if(ray.asIntersected())
      throw new SRuntimeException("Erreur SVoxelSpace 003 : Le rayon en param�tre a d�j� intersect� une g�om�trie.");
    
    // V�rifier la valeur de t_max est ad�quate
    if(t_max < 0.0)
      throw new SRuntimeException("Erreur SVoxelSpace 004 : Le temps maximale ne peut pas �tre n�gative.");
   
    // V�rifier si l'espace a �t� initialis�
    if(!space_initialized)
      throw new SRuntimeException("Erreur SVoxelSpace 005 : L'espace de g�om�tries de voxel n'a pas �t� initialis�.");
    
    //R�sultat de l'intersection avec la carte de voxel. Sera �gale � "ray" s'il y en a pas eu.
    SRay intersection_in_voxel = nearestIntersectionInVoxelMap(ray, t_max);
    
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
   * M�thode pour obtenir l'intersection la plus pr�s entre un rayon et des g�om�tries situ�es dans la carte de voxel.
   * @param ray - Le rayon � intersecter avec les g�om�tries.
   * @param t_max - Le temps maximal.
   * @return Le rayon avec les caract�ristiques de l'intersection (s'il y en a eu une).
   */
  private SRay nearestIntersectionInVoxelMap(SRay ray, double t_max)
  {
    //R�aliser des calculs d'intersection avec les g�om�tries de la carte uniquement si elle n'est pas vide
    if(!voxel_map.isEmpty())
    {
      //Cr�er la ligne de voxel � parcourir un � un
      SFastTraversalVoxelAlgorithm line_of_voxel = new SFastTraversalVoxelAlgorithm(ray, t_max, voxel_builder.getDimension(), absolute_extremum_voxel);
      
      //Faire l'it�ration sur la ligne de voxel depuis l'origine du rayon
      while(line_of_voxel.asNextVoxel())
      {
        SVoxel voxel = line_of_voxel.nextVoxel();
        
        // Faire le test de l'intersection des g�om�tries situ�es dans le voxel en cours
        SRay ray_intersection = nearestIntersectionInVoxelMap(ray, t_max, voxel_map, voxel_builder, voxel);
        
        // Retourner le r�sultat de l'intersection s'il y en a une, car elle sera n�cessairement de plus court temps
        if(ray_intersection.asIntersected())
          return ray_intersection;
      }
    }
    
    // Aucune intersection valide n'a �t� trouv�e.
    return ray;
  }
  
  @Override
  public List<SRay> nearestOpaqueIntersection(SRay ray, double t_max) throws SRuntimeException
  {
    // V�rifier si le rayon a d�j� intersect� une g�om�trie
    if(ray.asIntersected())
      throw new SRuntimeException("Erreur SVoxelSpace 006 : Le rayon en param�tre a d�j� intersect� une g�om�trie.");
    
    // V�rifier la valeur de t_max est ad�quate
    if(t_max < 0.0)
      throw new SRuntimeException("Erreur SVoxelSpace 007 : Le temps maximale ne peut pas �tre n�gative.");
   
    // V�rifier si l'espace a �t� initialis�
    if(!space_initialized)
      throw new SRuntimeException("Erreur SVoxelSpace 008 : L'espace de g�om�tries de voxel n'a pas �t� initialis�.");
   
    // La liste d�termin�e dans la carte des g�om�tries
    List<SRay> list_in_voxel = nearestOpaqueIntersectionInVoxelMap(ray, t_max);
    
    // La liste d�termin�e dans la liste lin�aire des g�om�tries
    List<SRay> list_not_in_voxel = nearestOpaqueIntersection(linear_list, ray, t_max);
    
    // La liste fusionn�e ad�quatement
    return mergeNearestOpaqueIntersection(list_in_voxel, list_not_in_voxel);    
  }

  /**
   * M�thode pour obtenir la liste des intersections transparente en ordre d�croissant dont la plus �loign� (premi�re de la liste) sera une g�om�trie opaque s'il y a eu intersection de cette nature.
   * 
   * @param ray - Le rayon � intersecter.
   * @param t_max - Le temps maximal pouvant �tre parcouru par le rayon.
   * @return La liste des intersections transparente en odre d�croissant dont le premier �l�ment sera une g�om�trie opaque s'il y a eu intersection de cette nature.
   */
  private List<SRay> nearestOpaqueIntersectionInVoxelMap(SRay ray, double t_max)
  {
    List<SRay> return_list = new ArrayList<SRay>();
    
    if(!voxel_map.isEmpty())
    {
      //Cr�er la ligne de voxel � parcourir un � un
      SFastTraversalVoxelAlgorithm line_of_voxel = new SFastTraversalVoxelAlgorithm(ray, t_max, voxel_builder.getDimension(), absolute_extremum_voxel);
      
      while(line_of_voxel.asNextVoxel())
      {
        SVoxel voxel = line_of_voxel.nextVoxel();
        
        // Obtenir la liste de l'intersection opaque associ� au voxel courant
        List<SRay> list = nearestOpaqueIntersectionInVoxelMap(ray, t_max, voxel_map, voxel_builder, voxel);
        
        // Ajouter cette liste � la liste � retourner
        return_list = mergeNearestOpaqueIntersection(return_list, list);
        
        // Retourner cette liste si l'intersection opaque a d�j� �t� trouv�e.
        if(!return_list.isEmpty())
          if(!return_list.get(0).getGeometry().isTransparent())
            return return_list;
      }
    }
    
    // La liste est vide ou elle contient uniquement des g�om�tries transparentes
    return return_list;
  }
  
  @Override
  public List<SGeometry> listInsideGeometry(SVector3d v)
  {
    // V�rifier que l'initialisation a �t� compl�t�e
    if(!space_initialized)
      throw new SRuntimeException("Erreur SVoxelSpace 009 : L'espace de voxel n'a pas �t� initialis�.");
    
    // Liste des g�om�tries o� le vecteur v sera situ� � l'int�rieur.
    // D�butons avec la liste disponible � partir des informations de la carte des voxels.
    List<SGeometry> inside_list = listInsideGeometryInMap(voxel_map, voxel_builder, v);
    
    // Ajouter les g�om�tries sans bo�te o� le vecteur v s'y retrouve.
    inside_list.addAll(listInsideGeometry(linear_list, v));
        
    return inside_list;
  }

  @Override
  public void initialize()
  {
    SLog.logWriteLine("Message SVoxelSpace : Construction de l'espace des g�om�tries avec voxel.");
    
    // S�parateur de la collection de g�om�trie
    SGeometryCollectionSplitter splitter = new SGeometryCollectionSplitter(geometry_list, SGeometryCollectionSplitter.SPLIT_BOX_AND_NO_BOX);
    
    // Obtenir la liste des g�om�tries sans bo�te englobante et l'affecter � la liste lin�aire
    linear_list = splitter.getNoBoxList();
    
    // Liste des bo�tes englobantes autour des g�om�tries de la liste.
    // Nous commencons pas une liste vide afin qu'elle soit remplac�e par celle donn�e par le "splitter".
    List<SBoundingBox> bounding_box_list = new ArrayList<SBoundingBox>();   
      
    // V�rifier qu'il y a au moins une liste de liste de bo�tes englobantes dans le "splitter".
    // Puisque le type de s�paration g�n�re uniquement une liste, alors nous pourrons l'utiliser et elle ne sera pas vide.
    if(!splitter.getBoundingBoxSplitList().isEmpty())
      bounding_box_list = splitter.getBoundingBoxSplitList().get(0);
        
    // S'assurer que la liste des bo�tes n'est pas vide, sinon il n'y a pas de carte de voxel � construire
    if(!bounding_box_list.isEmpty())
    {
      // Faire l'�valuation de la dimension des voxels et construire le g�n�rateur de voxel
      //-------------------------------------------------------------------------------------------------------------------------------------------------
      //SVoxelDimensionEvaluator evaluator = new SVoxelDimensionEvaluator(bounding_box_list, SVoxelDimensionEvaluator.ONE_FOR_ONE_ALGORITHM);
      //SVoxelDimensionEvaluator evaluator = new SVoxelDimensionEvaluator(bounding_box_list, SVoxelDimensionEvaluator.BIGGEST_AVERAGE_LENGHT_ALGORITHM);
      SVoxelDimensionEvaluator evaluator = new SVoxelDimensionEvaluator(bounding_box_list, SVoxelDimensionEvaluator.MID_AVERAGE_LENGHT_ALGORITHM);
      //SVoxelDimensionEvaluator evaluator = new SVoxelDimensionEvaluator(bounding_box_list, SVoxelDimensionEvaluator.SMALLEST_AVERAGE_LENGHT_ALGORITHM); 
      
      // G�n�rateur de voxel pour la carte de voxel en construction
      voxel_builder = new SVoxelBuilder(evaluator.getDimension()); 
            
      // Construire le voxel d'extr�me � la nouvelle carte et le mettre � jour � chaque insertion de g�om�trie
      absolute_extremum_voxel = new SVoxel(0, 0, 0);
              
      // Iterer sur l'ensemble bo�tes englobante.
      // Int�grer leur voxels attitr�s avec leur g�om�trie � la carte des voxels.
      // Mettre � jour le voxel d'extr�me
      for(SBoundingBox box : bounding_box_list)
        absolute_extremum_voxel = addGeometryToMap(voxel_map, absolute_extremum_voxel, box.getGeometry(), voxel_builder.buildVoxel(box));     
      
      // Messages multiples � afficher
      SLog.logWriteLine("Message SVoxelSpace : Nombre de g�om�tries dans la carte de voxels : " + bounding_box_list.size() + " g�om�tries.");
      SLog.logWriteLine("Message SVoxelSpace : Taille des voxels : " + evaluator.getDimension() + " unit�s.");  
      SLog.logWriteLine("Message SVoxelSpace : Nombre de r�f�rence � des g�om�tries : " + evaluateNbGeometryReference(voxel_map) + " r�f�rences.");
      SLog.logWriteLine("Message SVoxelSpace : Nombre moyen de r�f�rence � des g�om�tries par voxel : " + evaluateNbGeometryReferencePerVoxel(voxel_map) + " r�f�rences/voxel.");
      
      SLog.logWriteLine();
    }//fin if
    else
    {
      // Il n'y a pas de bo�te englobante de disponible pour l'espace avec voxel
      SLog.logWriteLine("Message SVoxelSpace : Aucune g�om�trie ne poss�de de bo�te englobante! Le choix d'un espace de g�om�tries en voxel devient in�fficace.");
     
      voxel_builder = null;   // Il n'y a pas de constructeur de voxel disponible
    }
    
    SLog.logWriteLine("Message SVoxelSpace : Fin de la construction de l'espace des g�om�tries avec voxel.");
    SLog.logWriteLine();
    
    // Initialisation de l'espace des voxels compl�t�e
    space_initialized = true;
  }
  
}//fin de la classe SVoxelSpace
