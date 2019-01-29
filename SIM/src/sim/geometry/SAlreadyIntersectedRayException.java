/**
 * 
 */
package sim.geometry;

/**
 * La classe d'exception <b>SAlreadyIntersectedRayException</b> repr�sente une exception lanc�e, car un rayon est d�j� dans un �tat intersect�.
 * 
 * @author Simon V�zina
 * @since 2017-06-02
 * @version 2017-06-02
 */
public class SAlreadyIntersectedRayException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -4266186178583035867L;

  /**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message - Le message de l'erreur.
   */
  public SAlreadyIntersectedRayException(String message)
  {
    super(message);
  }

  /**
   * Constructeur de l'exception avec message d'erreur et cause de l'exception.
   * 
   * @param message - Le message de l'erreur.
   * @param cause - La cause de l'erreur.
   */
  public SAlreadyIntersectedRayException(String message, Throwable cause)
  {
    super(message, cause);
  }

}// fin de la classe SAlreadyIntersectedRayException
