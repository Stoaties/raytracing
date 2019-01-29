/**
 * 
 */
package sim.math;

/**
 *  La classe <b>SSingularMatrixException</b> représente une exception lancée lorsqu'une matrice est considérée comme étant singulière.
 * 
 * @author Simon Vézina
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
