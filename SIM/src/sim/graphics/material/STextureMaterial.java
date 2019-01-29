/**
 * 
 */
package sim.graphics.material;

import sim.exception.SRuntimeException;

/**
 * L'interface STextureMaterial correspond � un mat�riel contenant des textures.
 * @author Simon V�zina
 * @since 2015-10-31
 * @version 2015-12-08
 */
public interface STextureMaterial extends SMaterial {

  /**
   * M�thode permettant de modifier le format d'interpr�tation des coordonn�es uv de texture.
   * @param uv_format - Le code d'interpr�tation des coordonn�e uv.
   * @throws SRuntimeException Si le code d'interpr�tation des coordonn�es uv n'est pas reconnu par le syst�me.
   * @throws SRuntimeException Si la d�finition a d�j� �t� r�alis� une fois. Une texture ne peut pas se faire modifier son code d'interpr�tation plus d'une fois.
   */
  public void setUVFormat(int uv_format) throws SRuntimeException;
  
  /**
   * M�thode d�terminant si le code d'interpr�tation des coordonn�es uv de texture a �t� d�fini pour cette texture.
   * @return <b>true</b> si le code a �t� d�fini et <b>false</b> sinon.
   */
  public boolean isUVFormatSelected();
  
}//fin de l'interface STextureMaterial
