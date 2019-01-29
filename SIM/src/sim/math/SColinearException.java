/**
 * 
 */
package sim.math;

/**
 * La classe d'exception <b>SColinearrException<b> correspond � une exception lanc�e parce qu'un calcul fait intervenir un concept de colin�aire rendant un calcul impossible � r�aliser.
 * 
 * 
 * @author Simon V�zina
 * @since 2017-11-04
 * @version 2017-11-04
 */
public class SColinearException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -4545944308524116056L;

 
  /**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message Le message de l'erreur.
   */
  public SColinearException(String message)
  {
    super(message);
  }
  
  /**
   * Constructeur de l'exception avec message d'erreur et cause de l'exception.
   * 
   * @param message Le message de l'erreur.
   * @param cause La cause de l'erreur.
   */
  public SColinearException(String message, Throwable cause)
  {
    super(message, cause);
  }

}// fin de la classe SColinearException
