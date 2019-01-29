/**
 * 
 */
package sim.graphics.light;

import sim.graphics.SColor;
import sim.readwrite.SWriteable;


/**
 * L'interface <b>SLight</b> représentant une source de lumière.
 * 
 * @author Simon Vézina
 * @since 2015-01-09
 * @version 2016-02-17
 */
public interface SLight extends SWriteable {

  /**
   * Méthode pour obtenir le numéro correspondant au nom de la source de lumière.
   * 
   * @return Le numéro correspondant au nom de la géométrie.
   */
  public int getCodeName();
  
  /**
	 * Méthode pour obtenir la couleur de la source de lumière.
	 * 
	 * @return La couleur de la source de lumière.
	 */
	public SColor getColor();
		
}//fin interface SLight
