/**
 * 
 */
package sim.math;

/**
 * La classe d'exception <b>SInfinityOfSolutionsException</b> sera lancé lorsqu'un problème mathématique 
 * admet un nombre infini de solution.
 * 
 * @author Simon Vézina
 * @since 2017-12-03
 * @version 2017-12-03
 */
public class SInfinityOfSolutionsException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -7172741241556104803L;

  /**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message Le message de l'erreur.
   */
  public SInfinityOfSolutionsException(String message)
  {
    super(message);
  }

  /**
   * Constructeur de l'exception avec message d'erreur et cause de l'exception.
   * 
   * @param message Le message de l'erreur.
   * @param cause La cause de l'erreur.
   */
  public SInfinityOfSolutionsException(String message, Throwable cause)
  {
    super(message, cause);
  }

}//fin de la classe SInfinityOfSolutionsException
