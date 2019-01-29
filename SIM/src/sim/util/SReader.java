/**
 * 
 */
package sim.util;

import sim.exception.SRuntimeException;

/**
 * L'interface <b>SReader</b> repr�sente un objet pouvant effectuer la lecture des propri�t�s d'un objet construit 
 * � partir d'information contenue dans un fichier.
 * 
 * @author Simon V�zina
 * @since 2015-11-07
 * @version 2015-11-07
 */
public interface SReader {

  /**
   * M�thode permettant d'obtenir un objet du type sp�cifi� par la classe impl�mentant l'interface <b>SReader</b>
   * dont les propri�t�s ont �t� lues dans un fichier.
   * 
   * @return L'objet lu par le lecteur.
   * @throws SRuntimeException S'il n'y a pas eu d'objet lu par le lecteur.
   */
  public Object getValue() throws SRuntimeException;
  
  /**
   * M�thode qui d�termine si le lecteur a effectu� une lecture d'un objet.
   * 
   * @return <b>true</b> si le lecteur a effectu� une lecture d'un objet et <b>false</b> sinon.
   */
  public boolean asRead();
  
}//fin de l'interface SReader
