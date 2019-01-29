/**
 * 
 */
package sim.graphics.light;

import sim.graphics.SColor;
import sim.readwrite.SWriteable;


/**
 * L'interface <b>SLight</b> repr�sentant une source de lumi�re.
 * 
 * @author Simon V�zina
 * @since 2015-01-09
 * @version 2016-02-17
 */
public interface SLight extends SWriteable {

  /**
   * M�thode pour obtenir le num�ro correspondant au nom de la source de lumi�re.
   * 
   * @return Le num�ro correspondant au nom de la g�om�trie.
   */
  public int getCodeName();
  
  /**
	 * M�thode pour obtenir la couleur de la source de lumi�re.
	 * 
	 * @return La couleur de la source de lumi�re.
	 */
	public SColor getColor();
		
}//fin interface SLight
