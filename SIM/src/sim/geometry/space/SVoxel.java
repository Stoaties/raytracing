/**
 * 
 */
package sim.geometry.space;

/**
 * Classe qui repr�ente un voxel. Un voxel est un �l�ment d'une grille r�guli�re � 3 dimensions 
 * comparable � un pixel �tant � 2 dimensions.
 * 
 * @author Simon V�zina
 * @since 2015-08-04
 * @version 2015-08-04
 */
public class SVoxel {

  private final int x;    //coordonn�e x du voxel
  private final int y;    //coordonn�e x du voxel
  private final int z;    //coordonn�e x du voxel
  
  /**
   * Constructeur d'un voxel.
   * 
   * @param x - La coordonn�e x du voxel.
   * @param y - La coordonn�e y du voxel.
   * @param z - La coordonn�e z du voxel.
   */
  public SVoxel(int x, int y, int z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * M�thode pour obtenir la coordonn�e x du voxel.
   * @return La coordonn�e x du voxel.
   */
  public int getX()
  {
    return x;
  }
  
  /**
   * M�thode pour obtenir la coordonn�e y du voxel.
   * @return La coordonn�e y du voxel.
   */
  public int getY()
  {
    return y;
  }
  
  /**
   * M�thode pour obtenir la coordonn�e z du voxel.
   * @return La coordonn�e z du voxel.
   */
  public int getZ()
  {
    return z;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    result = prime * result + z;
    return result;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    
    if (obj == null) {
      return false;
    }
    
    if (!(obj instanceof SVoxel)) {
      return false;
    }
    
    SVoxel other = (SVoxel) obj;
    
    if (x != other.x) {
      return false;
    }
    
    if (y != other.y) {
      return false;
    }
    
    if (z != other.z) {
      return false;
    }
    
    return true;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "SVoxel [x=" + x + ", y=" + y + ", z=" + z + "]";
  }

  
}//fin de la classe SVoxel
