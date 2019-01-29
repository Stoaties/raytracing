/**
 * 
 */
package sim.physics;

/**
 * La classe <b>SNotVisibleLightException</b> repr�sente une exception lanc�e lorsqu'une lumi�re dans le domaine du visible
 * doit �tre interpr�t�e et qu'elle n'est pas situ�e dans l'intervalle appropri�e �tant de <b>380 nm</b> � <b>780 nm</b>. 
 * 
 * @author Simon V�zina
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
