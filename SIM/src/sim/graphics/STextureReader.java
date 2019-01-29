/**
 * 
 */
package sim.graphics;

import java.util.HashMap;
import java.util.Map;

import sim.exception.SRuntimeException;
import sim.loader.SLoaderException;
import sim.loader.texture.STextureLoader;
import sim.util.SLog;
import sim.util.SReader;
import sim.util.SStringUtil;

/**
 * La classe <b>STextureReader</b> repr�sente un lecteur de texture.
 * 
 * @author Simon V�zina
 * @since 2015-11-07
 * @version 2016-01-08
 */
public class STextureReader implements SReader {

  private static final STexture DEFAULT_TEXTURE = null;    
  
  //Carte des textures d�j� lues (pour ne pas refaire de chargement)
  //Cl� de recherche : Nom du fichier
  //Information : construction de la texture � partir du nom du fichier
  private static final Map<String, STexture> texture_map = new HashMap<String, STexture>();
  
  private String file_name;                 //nom du fichier du mod�le
  
  STexture texture;
  
  boolean is_read;
  
  /**
   * Constructeur d'un lecteur de texture.
   * 
   * @param file_name - Le nom du fichier de la texture.
   */
  public STextureReader(String file_name)
  {
    this.file_name = file_name;
    texture = DEFAULT_TEXTURE;
    is_read = false;
    
    initialize();
  }

  /**
   * M�thode pour obtenir le nom du fichier en lecture.
   * 
   * @return Le nom du fichier en lecture.
   */
  public String getFileName()
  {
    return file_name;
  }
  
  @Override
  public STexture getValue() throws SRuntimeException
  {
    if(is_read)
      return texture;
    else
      throw new SRuntimeException("Erreur STextureReader 001 : La texture '" + file_name + "' n'a pas �t� lu.");
  }
  
  @Override
  public boolean asRead()
  {
    return is_read;
  }
  
  /**
   * M�thode pour faire l'initialisation du lecteur de texture.
   */
  private void initialize()
  {
    try{
      
      //V�rifier si la texture a d�j� �t� lue
      if(texture_map.containsKey(file_name))
        texture = texture_map.get(file_name);
      else
      {
        STextureLoader texture_loader = new STextureLoader();
      
        SLog.logWriteLine("Message STextureReader : Lecture de la texture '" + file_name + "'.");
        
        texture = texture_loader.loadTexture(file_name);  //lecture de la texture (exception lanc�e s'il y a eu erreur)
        
        texture_map.put(file_name, texture);            //mettre le mod�le lu dans la carte des mod�les
      }
      
      //Construire une nouvelle texture � partir du m�me tableau des couleurs,
      //car il est possible que cette texture n�cessite une autre interpr�tations
      //des coordonn�es uv lors de son usage dans un mat�riel
      texture = new STexture(texture);
      
      is_read = true;
      
    }catch(SLoaderException e){
      SLog.logWriteLine("Erreur STextureReader 002 : Le chargement de la texture '" + file_name + "' est impossible. " + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());
    }
  }
  
}//fin de la classe STextureReader
