/**
 * 
 */
package sim.exception;

/**
 * La classe <b>SConstructorException</b> repr�sente une exception lanc�e lors de la construction d'un objet
 * lorsqu'il y a un param�tre d�crivant l'objet le rendant invalide.
 * 
 * @author Simon V�zina
 * @since 2015-03-31
 * @version 2017-12-03
 */
public class SConstructorException extends RuntimeException {

  /**
   * La variable <b>serialVersionUID<b> correspond � un code d'identification de l'exception.
   */
  private static final long serialVersionUID = 1344374114943474870L;

  /**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message - Le message de l'erreur.
   */
  public SConstructorException(String message)
  {
    super(message);
  }

  /**
   * Constructeur de l'exception avec message d'erreur et cause de l'exception.
   * 
   * @param message - Le message de l'erreur.
   * @param cause - La cause de l'erreur.
   */
  public SConstructorException(String message, Throwable cause)
  {
    super(message, cause);
  }

}//fin de la classe SConstructorException
