/**
 * 
 */
package sim.math;

/**
 * La classe <b>SInvalidFunctionException</b> repr�sente une exception lanc�e lorsqu'une fonction n'est pas valide.
 * 
 * @author Simon V�zina
 * @since 2017-05-27
 * @version 2017-05-27
 */
public class SInvalidFunctionException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -2260769199841923981L;

  /**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message - Le message de l'erreur.
   */
  public SInvalidFunctionException(String message)
  {
    super(message);
  }

  /**
   * Constructeur de l'exception avec message d'erreur et cause de l'exception.
   * 
   * @param message - Le message de l'erreur.
   * @param cause - La cause de l'erreur.
   */
  public SInvalidFunctionException(String message, Throwable cause)
  {
    super(message, cause);
  }
  
}// fin de la classe SInvalidFunctionException
