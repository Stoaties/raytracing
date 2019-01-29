/**
 * 
 */
package sim.parser;

/**
 * La classe <b>SParserException</b> représente une exception de type lecture de fichier (parsing).
 * 
 * @author Simon Vézina
 * @since 2017-05-26
 * @version 2017-05-26
 */
public class SParserException extends Exception 
{
  /**
   * 
   */
  private static final long serialVersionUID = -2774127680964476495L;

  /**
   * Constructeur de l'exception avec message d'erreur.
   * 
   * @param message - Le message de l'erreur.
   */
  public SParserException(String message)
  {
    super(message);
  }
  
  /**
   * Constructeur de l'exception avec message d'erreur et cause de l'exception.
   * 
   * @param message Le message de l'erreur.
   * @param cause La cause de l'erreur.
   */
  public SParserException(String message, Throwable cause)
  {
    super(message, cause);
  }
  
}// fin de la classe SParserException
