/**
 * 
 */
package sim.geometry;

/**
 * @author Simon Vézina
 * @since 2017-06-03
 * @version 2017-06-03
 */
public class SNotIntersectedRayException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -3272723702914312262L;

  /**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message - Le message de l'erreur.
   */
  public SNotIntersectedRayException(String message)
  {
    super(message);
    // TODO Auto-generated constructor stub
  }

  /**
   * Constructeur de l'exception avec message d'erreur et cause de l'exception.
   * 
   * @param message - Le message de l'erreur.
   * @param cause - La cause de l'erreur.
   */
  public SNotIntersectedRayException(String message, Throwable cause)
  {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

}// fin de la classe SNotIntersectedRayException
