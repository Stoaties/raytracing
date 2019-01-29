/**
 * 
 */
package sim.geometry.space;

import sim.exception.SRuntimeException;

/**
 * La classe <b>SVoxelSpacePriorityEntry</b> représente une cellule de données pour un <b>SVoxelSpace</b> en comprenant un
 * itérateur FTVA (Fast Traversal Voxel Algorithm) sur les voxels particulier pour le test de l'intersection d'un rayon préalablement 
 * déterminé avec les géométries de l'espace des voxels. C'est l'itérateur FTVA qui sera utilisé dans l'attribution de la priorité à
 * l'aide de la méthode <i>public double nextMinTime()<i>.
 * 
 * @author Simon Vézina
 * @since 2016-02-04
 * @version 2016-02-04
 */
public class SVoxelSpacePriorityEntry implements Comparable<SVoxelSpacePriorityEntry>{

  /**
   * La variable <b>data</b> représente une cellule donnant accès aux données de l'espace des voxels (map, constructeur de voxel et le voxel d'extrême).
   */
  private final SVoxelSpaceEntry data;
  
  /**
   * La variable <b>FTVA</b> correspond à l'algorithme de parcourt de la carte de voxel (FastTraversalVoxelAlgorithm).
   * Cette variable doit être redéfinie à chaque fois qu'un nouveau rayon désire intersecter des géométries de la carte des voxels.
   */
  private final SFastTraversalVoxelAlgorithm FTVA;
  
  public SVoxelSpacePriorityEntry(SVoxelSpaceEntry data, SFastTraversalVoxelAlgorithm FTVA)
  {
    this.data = data;
    this.FTVA = FTVA;
  }

  //------------
  // MÉTHODES //
  //------------
  
  /**
   * Méthode pour obtenir les données décrivant l'espace de voxels de la cellule.
   * 
   * @return Les données de l'espace de voxels
   */
  public SVoxelSpaceEntry getData()
  {
    return data;
  }
  
  /**
   * Méthode pour obtenir le FTVA (Fast Traversal Voxel Algorithm).
   * 
   * @return Le FTVA.
   */
  public SFastTraversalVoxelAlgorithm getFastTraversalVoxelAlgorithm() throws SRuntimeException
  {
    return FTVA;
  }
  
  @Override
  public int compareTo(SVoxelSpacePriorityEntry entry)
  {
    // La comparaison de cette classe se fera par le temps où un FTVA est rendu à itérer.
    // Cette information sera obtenue par la méthode nextMinTime() d'un objet FTVA étant le temps pour entrer dans le prochain voxel courant.
    // Ceci permettra d'ordonner en ordre de priorité les voxels des FTVA à visiter en premier étant de plus petit temps.
       
    return Double.compare(this.FTVA.nextMinTime(), entry.FTVA.nextMinTime());
  }
  
}//fin de la classe SVoxelSpacePriorityEntry
