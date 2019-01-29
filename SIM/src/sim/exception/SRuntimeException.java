/**
 * 
 */
package sim.exception;

/**
 * La classe d'exception <b>SRuntimeException</b> correspond à une exception de type <tt>RuntimeException</tt> générée par les projects SIM.
 * 
 * @author Simon Vézina
 * @since 2015-01-25
 * @version 2017-11-03
 */
public class SRuntimeException extends RuntimeException {

  /**
   * La variable <b>serialVersionUID<b> correspond à un code d'identification de l'exception.
   */
	private static final long serialVersionUID = 436401966787368584L;

	/**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message Le message de l'erreur.
   */
	public SRuntimeException(String message)
	{
		super(message);
	}

	/**
   * Constructeur de l'exception avec message d'erreur et cause de l'exception.
   * 
   * @param message Le message de l'erreur.
   * @param cause La cause de l'erreur.
   */
	public SRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
	}

}//fin de la classe SRuntimeException
