/**
 * 
 */
package sim.graphics.light;

import java.io.IOException;

import sim.exception.SConstructorException;
import sim.graphics.SColor;
import sim.math.SVector3d;
import sim.util.SBufferedReader;

/**
 * La classe <b>SPointSpotLight</b> repr�sente une source de lumi�re ponctuelle ayant une orientation privil�gi�e. 
 * 
 * @author Simon V�zina
 * @since 2017-11-12
 * @version 2017-11-12
 */
public class SPointSpotLight extends SPointLight {

  /**
   * La variable <b>angular_projection</b> correspond � l'angle de projection de la lumi�re. La valeur maximale est de 180 degr�.
   */
  private double angular_projection;
  
  /**
   * La variable <b>orientation</b> correspond � l'orientation de la source de lumi�re.
   */
  private SVector3d orientation;
  
  /**
   * La variable <b>light_min_value</b> correspond � l'intensit� minimale de la source de lumi�re lorsque l'�clairage est �valuer � l'extr�mit� de son angle de projection.
   * Cette valeur est d�finie entre 0.0 (att�nuation compl�te � l'extr�mit�) et 1.0 (aucune att�nuation en fonction de l'angle).
   */
  private double light_min_value;
  
  /**
   * 
   */
  public SPointSpotLight()
  {
    // TODO Auto-generated constructor stub
  }

  /**
   * @param color
   * @param position
   */
  public SPointSpotLight(SColor color, SVector3d position)
  {
    super(color, position);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param color
   * @param position
   * @param amp
   * @param cst_att
   * @param lin_att
   * @param quad_att
   * @throws SConstructorException
   */
  public SPointSpotLight(SColor color, SVector3d position, double amp, double cst_att, double lin_att, double quad_att) throws SConstructorException
  {
    super(color, position, amp, cst_att, lin_att, quad_att);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param sbr
   * @throws IOException
   * @throws SConstructorException
   */
  public SPointSpotLight(SBufferedReader sbr) throws IOException, SConstructorException
  {
    super(sbr);
    // TODO Auto-generated constructor stub
  }

}// fin de la classe SPointSpotLight
