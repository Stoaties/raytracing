package sim.math;

/**
 * La classe d'exception <b>SNoCoplanarException</b> repr�sente une exception qui est lanc�e lorsqu'une m�thode n�cessitant l'usage d'�l�ment coplanaire n'est pas satisfaite.
 * 
 * @author Simon V�zina
 * @since 2016-12-19
 * @version 2017-11-03
 */
public class SNoCoplanarException extends RuntimeException {

  /**
   * La variable <b>serialVersionUID<b> correspond � un code d'identification de l'exception.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message Le message de l'erreur.
   */
  public SNoCoplanarException(String message)
  {
    super(message);
  }
  
}//fin de la classe SNoCoplanarException
