/**
 * 
 */
package sim.exception;

/**
 * La classe d'exception <b>SCoordinateOutOfBoundException</b> correspond à une exception qui est lancée lorsqu'une coordonnée sélectionnée
 * pour effectuer un calcul ou pour avoir accès à une valeur dans une structure de donnée est à <b>l'extérieur des limites permises</b>.
 * 
 * @author Simon Vézina
 * @since 2016-02-12
 * @version 2016-02-12
 */
public class SCoordinateOutOfBoundException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 5958348783285534542L;

  /**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message - Le message de l'erreur.
   */
  public SCoordinateOutOfBoundException(String message)
  {
    super(message);
  }

  /**
   * Constructeur de l'exception avec message d'erreur et cause de l'exception.
   * 
   * @param message - Le message de l'erreur.
   * @param cause - La cause de l'erreur.
   */
  public SCoordinateOutOfBoundException(String message, Throwable cause)
  {
    super(message, cause); 
  }

}//fin de la classe SCoordinateOutOfBoundException
