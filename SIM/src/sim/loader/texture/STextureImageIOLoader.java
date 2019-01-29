/**
 * 
 */
package sim.loader.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sim.graphics.STexture;
import sim.loader.SLoaderException;
import sim.loader.SStringLoader;
import sim.util.SFileSearch;
import sim.util.SStringUtil;

/**
 * La classe STextureImageIOLoader repr�sente un lecteur de texture de format 'gif', 'png' et 'jpeg' ('jpg') dont l'impl�mentation
 * provient de la classe javax.imageio.ImageIO.
 * 
 * @author Simon V�zina
 * @since 2015-09-27
 * @version 2015-10-28
 */
public class STextureImageIOLoader implements SStringLoader {

  /**
   * La constante 'FILE_EXTENSIONS' correspond aux diff�rents type de fichier image pouvant �tre interpr�t� par ce chargeur d'image.
   * La liste est la suivante : gif, png, jpg (jpeg)
   */
  public static final String[] FILE_EXTENSIONS = {"gif", "png", "jpeg", "jpg"};  //extensions des fichiers lues par ce loader
  
  /**
   * Constructeur d'un lecteur de texture selon l'impl�mentation de la classe javax.imageio.ImageIO supportant les formats
   * 'gif', 'png' et 'jpeg' ('jpg'). 
   */
  public STextureImageIOLoader()
  {
    
  }

  /* (non-Javadoc)
   * @see sim.loader.SStringLoader#load(java.lang.String)
   */
  @Override
  public Object load(String string) throws SLoaderException
  {
    //Trouver le fichier � partir du r�pertoire o� l'ex�cution de l'application est r�alis�e
    SFileSearch search = new SFileSearch("", string);
    
    if(!search.isFileFound())
      throw new SLoaderException("Erreur STextureImageIOLoader 001 : Le fichier '" + string + "' n'est pas trouv�.");
    
    if(search.isManyFileFound())
      throw new SLoaderException("Erreur STextureImageIOLoader 002 : Le fichier '" + string + "' a �t� trouv� plus d'une fois dans les diff�rents sous-r�pertoires. Veuillez en garder qu'une seule version.");
    
    File file = new File(search.getFileFoundList().get(0)); //prendre la 1ier trouv� dans la liste
    
    try{
      
      BufferedImage result = ImageIO.read(file);  //chargement de la texture en m�moire 
      
      //V�rification si la texture est bien charg�e
      if(result == null)
        throw new SLoaderException("Erreur STextureImageIOLoader 003 : Le fichier '" + string + "' de format '" + SStringUtil.extensionFileLowerCase(string) + "' n'a pas �t� ad�quatement interpr�t� par ce lecteur.");
      else
        return new STexture(search.getFileNameToSearch(), result, STexture.ORIGIN_UV_TOP_LEFT);
      
    }catch(IOException e){
      throw new SLoaderException("Erreur STextureImageIOLoader 004 : Le chargement de la texture '" + string + "' est impossible. " + SStringUtil.END_LINE_CARACTER + e.getMessage());
    }
  }

}//fin de la classe STextureImageIOLoader
