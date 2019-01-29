/**
 * 
 */
package sim.math;

/**
 *  La classe <b>SSingularMatrixException</b> repr�sente une exception lanc�e lorsqu'une matrice est consid�r�e comme �tant singuli�re.
 * 
 * @author Simon V�zina
 * @since 2017-05-16
 * @version 2017-05-16
 */
public class SSingularMatrixException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message Le message de l'erreur.
   */
  public SSingularMatrixException(String message) 
  {
    super(message);
  }

}// fin de la classe SSingularMatrixException
