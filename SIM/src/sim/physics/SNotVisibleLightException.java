/**
 * 
 */
package sim.physics;

/**
 * La classe <b>SNotVisibleLightException</b> représente une exception lancée lorsqu'une lumière dans le domaine du visible
 * doit être interprétée et qu'elle n'est pas située dans l'intervalle appropriée étant de <b>380 nm</b> à <b>780 nm</b>. 
 * 
 * @author Simon Vézina
 * @since 2016-02-11
 * @version 2016-02-11
 */
public class SNotVisibleLightException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -851196823247010665L;

  /**
   * ...
   * 
   * @param arg0
   */
  public SNotVisibleLightException(String arg0) {
    super(arg0);
    
  }

  /**
   * ...
   * 
   * @param arg0
   * @param arg1
   */
  public SNotVisibleLightException(String arg0, Throwable arg1) {
    super(arg0, arg1);
    
  }

}//fin de la classe SNotVisibleLightException
