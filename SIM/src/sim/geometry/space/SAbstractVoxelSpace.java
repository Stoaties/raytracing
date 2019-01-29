/**
 * 
 */
package sim.geometry.space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import sim.geometry.SGeometry;
import sim.geometry.SRay;
import sim.math.SVector3d;

/**
 * La classe abstraite <b>SAbstractVoxelSpace</b> repr�sente un espace de g�om�trie dont le partitionnement
 * de l'espace s'effectue par voxel. Cette classe contient des m�thodes utilitaires pour analyser cet espace.
 * 
 * @author Simon V�zina
 * @since 2015-12-24
 * @version 2015-12-29
 */
public abstract class SAbstractVoxelSpace extends SAbstractGeometrySpace {

  //------------
  // VARIABLE //
  //------------
  
  /**
   * La variable 'linear_list' correspond � une liste de primitive ne fait pas partie de la carte des voxels puisque ce sont des primitives <b>sans bo�te englobante</b>.
   * L'algorithme d'intersection testera alors toutes les primitives de cette liste avec chaque rayon. 
   */
  protected List<SGeometry> linear_list;  
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un espace de voxel abstrait par d�faut. 
   */
  public SAbstractVoxelSpace()
  {
    super();
    
    linear_list = new ArrayList<SGeometry>();
  }

  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir l'intersection la plus pr�s entre un rayon et des g�om�tries situ�es dans une carte de voxels. 
   * 
   * @param ray - Le rayon � intersecter avec les g�om�tries.
   * @param t_max - Le temps maximal.
   * @param map - La carte des voxels.
   * @param builder - Le constructeur de voxel (pour v�rifier si l'intersection est � l'int�rieur du voxel).
   * @param voxel - Le voxel de la carte o� sont r�alis�s les tests d'intersection.
   * @return Le rayon avec les caract�ristiques de l'intersection (s'il y en a eu une) de temps minimum.
   */
  protected SRay nearestIntersectionInVoxelMap(SRay ray, double t_max, Map<SVoxel,List<SGeometry>> map, SVoxelBuilder builder, SVoxel voxel)
  {
    if(map != null)         // Si la carte n'est pas null
      if(!map.isEmpty())    // Si la carte n'est pas vide
      {
        // Obtenir la liste des g�om�tries en r�f�rence dans le voxel
        List<SGeometry> list = map.get(voxel);
        
        if(list != null)          // Si le voxel est dans la carte
          if(!list.isEmpty())     // Si la liste de g�om�trie associ�e � ce voxel n'est pas vide
          {
            // R�aliser les intersections avec la liste disponible dans le voxel.
            // Cette liste sera tri�e en ordre croissant de temps (le plus petit temps en premier dans la liste)
            List<SRay> list_intersection = intersections(list, ray, t_max);   
                
            // Regarder la liste des intersections et prendre l'intersection au temps le plus petit,
            // mais qui se retrouve dans le voxel courant (sinon, il est rejet�).
            // Comparer celles trouv�es avec le rayon d�j� intersect� pour garder celle de moindre temps
            if(!list_intersection.isEmpty())
              for(SRay r : list_intersection)
                if(builder.buildVoxel(r.getIntersectionPosition()).equals(voxel))   
                  return r;
          }
      }
    
    // Il n'y a pas d'intersection ad�quate qui a �t� r�alis�, on retourne un rayon sans intersection
    return ray;
  }
    
  /**
   * M�thode pour obtenir la liste des intersections transparente en ordre d�croissant dont la plus �loign� (premi�re de la liste) sera une g�om�trie opaque s'il y a eu intersection de cette nature.
   * 
   * @param ray - Le rayon � intersecter.
   * @param t_max - Le temps maximal pouvant �tre parcouru par le rayon.
   * @param map - La carte des voxels.
   * @param builder - Le constructeur de voxel (pour v�rifier si l'intersection est � l'int�rieur du voxel).
   * @param voxel - Le voxel de la carte o� sont r�alis�s les tests d'intersection.
   * @return La liste des intersections transparente en odre d�croissant dont le premier �l�ment sera une g�om�trie opaque s'il y a eu intersection de cette nature.
   */
  protected List<SRay> nearestOpaqueIntersectionInVoxelMap(SRay ray, double t_max, Map<SVoxel,List<SGeometry>> map, SVoxelBuilder builder, SVoxel voxel)
  {
    if(map != null)
      if(!map.isEmpty())
      {
        // Obtenir la liste des g�om�tries en r�f�rence dans le voxel
        List<SGeometry> list = map.get(voxel);
        
        if(list != null)          
          if(!list.isEmpty())     
            return nearestOpaqueIntersectionInVoxel(list, ray, t_max, builder, voxel);  // Obtenir la liste de l'intersection opaque la plus pr�s
      }

    // La carte n'�tant vide, on retourne une liste vide
    return new ArrayList<SRay>();    
  }
  
  /**
   * <p>
   * M�thode qui �value l'intersection la plus pr�s entre un rayon et les diff�rentes g�om�tries <b>opaque</b> de l'espace ne d�passant pas un certain temps maximal.
   * Une liste des g�om�tries transparentes sera ordonn�e en <b>ordre inverse d'apparition</b> (ordre d�croissant du temps).
   * La <b>liste d�butera par une g�om�trie opaque</b> s'il y a eu une intersection avec une g�om�trie opaque.
   * S'il n'y a pas eu d'intersection avec une g�om�trie opaque, elle contiendra uniquement des g�om�tries transparentes.
   * </p>
   * 
   * <p>
   * En comparaison avec sa version pr�c�dente, cette m�thode n'acceptera uniquement les intersections situ�es � l'int�rieur du voxel associ� � la liste des g�om�tries.
   * </p>
   *
   * @param list - La liste des g�om�tries.
   * @param ray - Le rayon � intersecter avec les g�om�tries de l'espace.
   * @param t_max - Le temps maximal.
   * @param builder - Le constructeur de voxel (pour v�rifier si l'intersection est � l'int�rieur du voxel).
   * @param voxel - Le voxel de la carte o� sont r�alis�s les tests d'intersection.
   * @return Une liste des g�om�tries transparentes intersect�es en <b>ordre d�croissant</b> d�butant par une g�om�trie opaque s'il y a eu lieu. La liste sera <b>vide</b> s'il n'y a <b>aucune intersection</b> sur le temps maximal.
   */
  private List<SRay> nearestOpaqueIntersectionInVoxel(List<SGeometry> list, SRay ray, final double t_max, SVoxelBuilder builder, SVoxel voxel)
  {
    // Obtenir la liste compl�te des intersections tri�es en ordre croissant
    List<SRay> list_intersection = intersections(list, ray, t_max);
    
    // Iterer sur l'ensemble des intersections et conserver celle 
    // o� il y a des g�om�tries transparentes et une seule opaque s'il y en a une
    List<SRay> return_list = new ArrayList<SRay>();
    
    for(SRay r : list_intersection)
    {
      // Ajouter et traiter l'intersection uniquement si elle se retrouve dans le voxel
      if(builder.buildVoxel(r.getIntersectionPosition()).equals(voxel))
      {
        return_list.add(r);   // ajouter cette intersection
        
        // Si la g�om�trie intersect� n'est pas transparente,
        // ce sera la derni�re intersection ajout�e � la liste
        // car elle sera la seule intersection opaque de la liste.
        if(!r.getGeometry().isTransparent())
        {
          Collections.reverse(return_list);     // Inverser l'ordre en ordre d�croissant
          return return_list;                   // retourner une liste avec g�om�trie opaque
        }
      }
    }
    
    Collections.reverse(return_list);           // Inverser l'ordre en ordre d�croissant
    return return_list;                         // retourner une liste sans g�om�trie opaque
  }
  
  /**
   * M�thode qui �value l'ensemble des g�om�tries d'une carte de voxel o� une vecteur v se retrouve � l'int�rieur.
   * 
   * @param map - La carte des voxels.
   * @param builder - Le constructeur de voxel attitr� � la carte de voxel.
   * @param v - La position du vecteur.
   * @return La liste des g�om�tries o� le vecteur v se retrouve � l'int�rieur.
   */
  protected List<SGeometry> listInsideGeometryInMap(Map<SVoxel,List<SGeometry>> map, SVoxelBuilder builder, SVector3d v)
  {
    // Obtenir la liste des g�om�tries dans la cartes des voxel 
    // et obtenir les g�om�tries o� le vecteur v s'y retrouve
    if(map != null)        
      if(!map.isEmpty())    
        return listInsideGeometry(map.get(builder.buildVoxel(v)), v);
        
    // La carte �tant null ou vide, nous retournons une liste vide.
    return new ArrayList<SGeometry>();
  }
  
  /**
   * M�thode pour ajouter des g�om�tries � la carte de voxel avec les voxels o� la g�om�trie est situ�e.
   * 
   * @param map - La carte des voxels contenant une liste de g�om�trie.
   * @param extremum_voxel - Le voxel extremum associ� � la carte de voxels.
   * @param geometry - La g�om�trie � ajouter.
   * @param list - La liste des voxels o� la g�om�trie est situ�e.
   */
  protected SVoxel addGeometryToMap(Map<SVoxel, List<SGeometry>> map, SVoxel extremum_voxel, SGeometry geometry, List<SVoxel> list)
  {
    // Iterer sur la liste des voxels
    for(SVoxel v : list)
    {  
      // Si le voxel est d�j� dans la carte : ajouter la g�om�trie � la liste existante de g�om�trie de ce voxel
      if(map.containsKey(v))            
        map.get(v).add(geometry);
      else                                    
      {
        // Si le voxel n'est pas dans la carte : cr�er une liste avec la g�om�trie dedans. 
        // Ajouter le voxel comme cl� de recherche dans la carte.
        // Il faudra ajuster le voxel d'extr�me �galement.
        List<SGeometry> l = new ArrayList<SGeometry>();
        
        // Ajouter la g�om�trie � la liste
        l.add(geometry);                                       
        
        // Ajouter le voxel � la carte
        map.put(v, l);                                           
        
        // Mise � jour du voxel d'extremum (si n�cessaire)
        extremum_voxel = updateExtremumVoxel(extremum_voxel, v);   
      }
    }
    
    return extremum_voxel;
  }
  
  /**
   * M�thode pour faire la mise � jour du voxel d�finissant les extermums en valeur absolue d'une carte de voxels.
   * 
   * @param previous_extremum - Le voxel �tant celui d�finissant l'ancien extremum.
   * @param new_voxel - Le nouveau voxel pouvant red�finir le voxel extremum.
   * @return Un nouveau voxel d�finissant un nouvel extremum ou celui pass� en param�tre s'il demeure extremum.
   */
  protected SVoxel updateExtremumVoxel(SVoxel previous_extremum, SVoxel new_voxel)
  {
    // Voxel de retour �tant l'ancien par d�faut (s'il n'est pas chang�)
    SVoxel return_voxel = previous_extremum;
    
    //Mise � jour si n�cessaire de la coordonn�e extermum en x
    if(Math.abs(new_voxel.getX()) > return_voxel.getX())
      return_voxel = new SVoxel(Math.abs(new_voxel.getX()), return_voxel.getY(), return_voxel.getZ());
    
    //Mise � jour si n�cessaire de la coordonn�e extermum en y
    if(Math.abs(new_voxel.getY()) > return_voxel.getY())
      return_voxel = new SVoxel(return_voxel.getX(), Math.abs(new_voxel.getY()), return_voxel.getZ());
    
    //Mise � jour si n�cessaire de la coordonn�e extermum en z
    if(Math.abs(new_voxel.getZ()) > return_voxel.getZ())
      return_voxel = new SVoxel(return_voxel.getX(), return_voxel.getY(), Math.abs(new_voxel.getZ()));
    
    return return_voxel;
  }
  
  /**
   * M�thode pour obtenir le nombre de g�om�tries en r�f�rence dans les voxels.
   * 
   * @param map - La carte des voxels.
   * @return Le nombre de primitives dans les voxels de la carte.
   */
  protected int evaluateNbGeometryReference(Map<SVoxel, List<SGeometry>> map)
  {
    int count = 0;
    
    // Iterer sur l'ensemble des valeurs de la carte
    for(List<SGeometry> l : map.values())
      count += l.size();
    
    return count;
  }
  
  /**
   * M�thode pour obtenir le nombre de g�om�tries en r�f�rence en moyenne par voxel.
   * 
   * @param map - La carte des voxels.
   * @return Le nombre moyen de primitives par voxel.
   */
  protected double evaluateNbGeometryReferencePerVoxel(Map<SVoxel, List<SGeometry>> map)
  {
    // Valeur statistique �tant un calcul de moyenne
    return (double) evaluateNbGeometryReference(map) / (double) map.keySet().size();
  }
  
}//fin de la classe SAbstractVoxelSpace
