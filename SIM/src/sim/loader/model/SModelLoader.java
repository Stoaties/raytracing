/**
 * 
 */
package sim.loader.model;

import java.util.Hashtable;

import sim.graphics.SModel;
import sim.loader.SLoaderException;
import sim.loader.SStringLoader;
import sim.util.SStringUtil;

/**
 * Classe qui repr�sente un constructeur de mod�le 3d � partir du nom du fichier. Le loader aura
 * pour fonction de choisir le bon type de loader selon le format du fichier � lire.
 * @author Simon V�zina
 * @since 2015-03-16
 * @version 2015-07-31
 */
public class SModelLoader {

  private final Hashtable<String,SStringLoader> model_loader_chooser;
  
  /**
   *  Constructeur du loader de mod�le 3d. 
   */
  public SModelLoader()
  {
    model_loader_chooser = new Hashtable<String,SStringLoader>();
    
    //Insertion des loaders de mod�le 3d 
    model_loader_chooser.put(SModelOBJLoader.FILE_EXTENSION, new SModelOBJLoader());
    model_loader_chooser.put(SModelMSRLoader.FILE_EXTENSION, new SModelMSRLoader());
    model_loader_chooser.put(SModelAGPLoader.FILE_EXTENSION, new SModelAGPLoader());
  }

  /**
   * M�thode qui effectue la construction d'un SModel � partir du nom d'un fichier.
   * @param file_name - Le nom du fichier en lecture.
   * @return Le mod�le 3d construit.
   * @throws SLoaderException Si le fichier n'a pas pu �tre charg� correctement.
   */
  public SModel loadModel(String file_name)throws SLoaderException
  {
    String extension = SStringUtil.extensionFileLowerCase(file_name);  //obtenir l'extension du fichier
    
    //Construire le mod�le 3d
    if(!model_loader_chooser.containsKey(extension))
      throw new SLoaderException("Erreur SModelLoader 001 : Le fichier '" + file_name +"' de format '" + extension + "' n'est pas un format de mod�le pouvant �tre interpr�t� par ce programme.");
    else
    {
      SStringLoader loader = model_loader_chooser.get(extension);
      return (SModel)loader.load(file_name);
    }
  }
  
}//fin SModelLoader
