/**
 * 
 */
package sim.geometry.space;

import sim.exception.SRuntimeException;

/**
 * La classe <b>SVoxelSpacePriorityEntry</b> repr�sente une cellule de donn�es pour un <b>SVoxelSpace</b> en comprenant un
 * it�rateur FTVA (Fast Traversal Voxel Algorithm) sur les voxels particulier pour le test de l'intersection d'un rayon pr�alablement 
 * d�termin� avec les g�om�tries de l'espace des voxels. C'est l'it�rateur FTVA qui sera utilis� dans l'attribution de la priorit� �
 * l'aide de la m�thode <i>public double nextMinTime()<i>.
 * 
 * @author Simon V�zina
 * @since 2016-02-04
 * @version 2016-02-04
 */
public class SVoxelSpacePriorityEntry implements Comparable<SVoxelSpacePriorityEntry>{

  /**
   * La variable <b>data</b> repr�sente une cellule donnant acc�s aux donn�es de l'espace des voxels (map, constructeur de voxel et le voxel d'extr�me).
   */
  private final SVoxelSpaceEntry data;
  
  /**
   * La variable <b>FTVA</b> correspond � l'algorithme de parcourt de la carte de voxel (FastTraversalVoxelAlgorithm).
   * Cette variable doit �tre red�finie � chaque fois qu'un nouveau rayon d�sire intersecter des g�om�tries de la carte des voxels.
   */
  private final SFastTraversalVoxelAlgorithm FTVA;
  
  public SVoxelSpacePriorityEntry(SVoxelSpaceEntry data, SFastTraversalVoxelAlgorithm FTVA)
  {
    this.data = data;
    this.FTVA = FTVA;
  }

  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir les donn�es d�crivant l'espace de voxels de la cellule.
   * 
   * @return Les donn�es de l'espace de voxels
   */
  public SVoxelSpaceEntry getData()
  {
    return data;
  }
  
  /**
   * M�thode pour obtenir le FTVA (Fast Traversal Voxel Algorithm).
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
    // La comparaison de cette classe se fera par le temps o� un FTVA est rendu � it�rer.
    // Cette information sera obtenue par la m�thode nextMinTime() d'un objet FTVA �tant le temps pour entrer dans le prochain voxel courant.
    // Ceci permettra d'ordonner en ordre de priorit� les voxels des FTVA � visiter en premier �tant de plus petit temps.
       
    return Double.compare(this.FTVA.nextMinTime(), entry.FTVA.nextMinTime());
  }
  
}//fin de la classe SVoxelSpacePriorityEntry
