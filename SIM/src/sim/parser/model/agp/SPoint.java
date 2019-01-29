/**
 * 
 */
package sim.parser.model.agp;

/**
 * Classe repr�sentant un point 3d d'un mod�le 3d de format "agp" (mod�le 3d du <i>projet Anaglyphe</i> r�alis� par Anik Souli�re).
 * 
 * @author Simon V�zina
 * @since 2015-08-01
 * @version 2015-08-01
 */
public class SPoint {

  //Les coordonn�es par d�faut
  private static final double DEFAULT_X = 0.0;
  private static final double DEFAULT_Y = 0.0;
  private static final double DEFAULT_Z = 0.0;
  
  //Les trois coordonn�es du point 3d
  private double x;
  private double y;
  private double z;
  
  /**
   *  Constructeur d'une point par d�faut. 
   */
  public SPoint()
  {
    x = DEFAULT_X;
    y = DEFAULT_Y;
    z = DEFAULT_Z;
  }

  /**
   * M�thode pour obtenir la coordonn�e <i>x</i> du point.
   * @return la coordonn�e <i>x</i>.
   */
  public double getX()
  {
    return x;
  }

  /**
   * M�thode pour obtenir la coordonn�e <i>y</i> du point.
   * @return la coordonn�e <i>y</i>.
   */
  public double getY()
  {
    return y;
  }

  /**
   * M�thode pour obtenir la coordonn�e <i>z</i> du point.
   * @return la coordonn�e <i>z</i>.
   */
  public double getZ()
  {
    return z;
  }

  /**
   * M�thode pour d�finir la coordonn�e <i>x</i> du point.
   * @param x - La nouvelle coordonn�e <i>x</i> du point.
   */
  public void setX(double x)
  {
    this.x = x;
  }

  /**
   * M�thode pour d�finir la coordonn�e <i>y</i> du point.
   * @param y - La nouvelle coordonn�e <i>y</i> du point.
   */
  public void setY(double y)
  {
    this.y = y;
  }

  /**
   * M�thode pour d�finir la coordonn�e <i>z</i> du point.
   * @param z - La nouvelle coordonn�e <i>z</i> du point.
   */
  public void setZ(double z)
  {
    this.z = z;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "SPoint [x=" + x + ", y=" + y + ", z=" + z + "]";
  }
  
}//fin de la classe SPoint
