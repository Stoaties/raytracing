/**
 * 
 */
package sim.geometry.space;

import java.util.ArrayList;
import java.util.List;

import sim.math.SVector3d;

/**
 * <p>Classe repr�sentant un constructeur de voxel. L'objectif sera de faire correspondre une coordonn�e espace
 * monde � une coordonn�e espace voxel. Pour ce faire, il faudra sp�cifier <b>1 unit� voxel = x unit� monde</b>.</p> 
 * <p>Prenant en argument un vecteur ou une bo�te englobante,
 * le constructeur de voxel va convertir en espace voxel les coordonn�e (x,y,z) des arguments. Dans le cas d'une bo�te,
 * il est possible que la bo�te n�cessite plusieurs voxels en raison de la grande taille de la bo�te
 * ou de la petite taille espace du voxel.</p>
 * 
 * @author Simon V�zina
 * @since 2015-08-04
 * @version 2015-08-04
 */
public class SVoxelBuilder {

  private final double dimension;   //la dimension espace d'un unit� voxel ("1" unit� voxel = "dimension" unit� monde)
 
  /**
   * Constructeur d'un constructeur de voxel.
   * @param dimension - La dimension d'un voxel en unit� monde.
   */
  public SVoxelBuilder(double dimension)
  {
    this.dimension = dimension;
  }

  /**
   * M�thode pour obtenir la dimension espace d'un voxel ("1" unit� voxel = "dimension" unit� monde).
   * @return La dimension d'un voxel en espace monde.
   */
  public double getDimension()
  {
    return dimension;
  }
  
  /**
   * M�thode pour construire un voxel dans lequel un vecteur 3d est situ� � l'int�rieur.
   * @param v - Le vecteur.
   * @return Le voxel o� est situ� le vecteur 3d.
   */
  public SVoxel buildVoxel(SVector3d v)
  {
    int x_voxel = (int)Math.floor(v.getX()/dimension);
    int y_voxel = (int)Math.floor(v.getY()/dimension);
    int z_voxel = (int)Math.floor(v.getZ()/dimension);
    
    return new SVoxel(x_voxel, y_voxel, z_voxel);
  }
  
  /**
   * M�thode pour construire une liste de voxel dans lequel une bo�te englobant est situ�e � l'int�rieur.
   * Si la dimension du voxel est grande et que la bo�te est petite, la liste peut contenir qu'un seul voxel.
   * @param box - La bo�te.
   * @return La liste de voxels o� est situ� la bo�te.
   */
  public List<SVoxel> buildVoxel(SBoundingBox box)
  {
    List<SVoxel> list = new ArrayList<SVoxel>();
    
    SVoxel min = buildVoxel(box.getMinPoint());
    SVoxel max = buildVoxel(box.getMaxPoint());
    
    if(min.equals(max))   //la bo�te se retrouve dans un seul voxel
      list.add(min);
    else                  //la bo�te se retouve dans plusieurs voxels
      for(int i = min.getX(); i <= max.getX(); i++)
        for(int j = min.getY(); j <= max.getY(); j++)
          for(int k = min.getZ(); k <= max.getZ(); k++)
            list.add(new SVoxel(i, j, k));
    
    return list;
  }
  
}//fin de la classe SVoxelBuilder
