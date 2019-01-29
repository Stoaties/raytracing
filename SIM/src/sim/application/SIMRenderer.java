package sim.application;

import java.io.FileNotFoundException;
import java.io.IOException;

import sim.application.util.SConfiguration;
import sim.application.util.SConsoleComparator;
import sim.application.util.SConsoleRenderer;
import sim.application.util.SJFrameRenderer;
import sim.application.util.SRendererInfo;
import sim.exception.SConstructorException;
import sim.exception.SNoImplementationException;
import sim.util.SLog;
import sim.util.SStringUtil;

/**
 * L'application <b>SIMRenderer</b> permet de repr�senter une sc�ne en 3d sur une projection d'un �cran 2d.
 * 
 * @author Simon V�zina
 * @since 2014-12-28
 * @version 2016-02-29
 */
public class SIMRenderer {

  /**
   * La constante <b>DEFAULT_CONFIG_FILE_NAME</b> correspond au nom du fichier de configuration utilis� par d�faut.
   */
  public static final String DEFAULT_CONFIG_FILE_NAME = "configuration.cfg";
  
  //----------------
  // M�THODE MAIN //
  //----------------
  
	/**
	 * Lancement de l'application SIMRenderer.
	 * 
	 * @param args
	 */
  public static void main(String[] args) 
  {
    // Chargement de la configuration
    SConfiguration config = loadConfiguration(DEFAULT_CONFIG_FILE_NAME); 
    
    // Lancer l'application 
    try
    {
      // Lancer l'application appropri�e
      switch(config.getApplicationType())
      {
        // Lancer aucune application
        case 0 :  SLog.logWriteLine("Message SIMRenderer : Aucune application n'est lanc�e.");
                  break;
                  
        // Lancer la version "info" de l'application
        case 1 :  SLog.logWriteLine("Message SIMRenderer : La version INFO de l'application SIMRenderer est lanc�e.");
                  SLog.logWriteLine();
                  
                  SRendererInfo info = new SRendererInfo();
                  info.writeInfo();
                  info.writeDefaultScene();
                  
                  break;
        
        // Lancer la version "console" de l'application
        case 2 :  SConsoleRenderer.raytrace(config);
                  break;
        
        // Lancer la version "frame" de l'application         
        case 3 :  SJFrameRenderer frame = new SJFrameRenderer();
                  frame.setVisible(true);
                  frame.raytrace(config);
                  break;
        
        // Lancer l'application de comparaison d'image
        case 4 :  SConsoleComparator comparator = new SConsoleComparator(config.getReadDataFileName());
                  comparator.compareImage();
                  comparator.write(config.getWriteDataFileName());
                  break;
                   
        // L'application n'est pas reconnue
        default : SLog.logWriteLine("Message SIMRenderer : Le code de l'application '" + config.getApplicationType() + "' n'est pas reconnu."); 
      }
               
      //�criture du fichier de configuration
      config.writeFile();    
     
    }catch(FileNotFoundException e){       
      SLog.logWriteLine("Message SIMRenderer : Un fichier n'a pas �t� trouv�." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());  
      SLog.logWriteLine("Message SIMRenderer : Un fichier n'a pas �t� trouv�.");
    }catch(SConstructorException e){
      SLog.logWriteLine("Message SIMRenderer : Une erreur lors de la construction d'un objet est survenue."  + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());
      e.printStackTrace();
    }catch(SNoImplementationException e){  
      // Exception lanc�e s'il y a une m�thode non impl�ment�e (possible lors d'un labotatoire !!!)
      SLog.logWriteLine("Message SIMRenderer : Une m�thode n'a pas �t� impl�ment�e."  + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage()); 
      e.printStackTrace();                                          
    }catch(IOException e){                 
      e.printStackTrace(); 
    }
    
      
    //Fermeture du fichier log  
    try{
      SLog.closeLogFile();
    }catch(IOException e){
      e.printStackTrace();
    }
    
  }
  
  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour faire le chargement de la configuration.
   * Si le fichier est trouv�, la configuration sera d�termin�e par le contenu du fichier.
   * Si le fichier n'est pas trouv�, une configuration par d�faut sera d�termin�e.
   * 
   * @param file_name Le nom du fichier contenant la configuration.
   * @return La configuration de l'application.
   */
  private static SConfiguration loadConfiguration(String file_name)
  {
    try{
      
      // Construire la configuration par la lecture du fichier de configuration.
      return new SConfiguration(file_name);
    
    }catch(FileNotFoundException e){
    }catch(IOException e){
      e.printStackTrace();
    }
    
    // Le fichier de configuration n'a pas �t� trouv�e.
    // Cr�ation du fichier de configuration par d�faut. 
    return new SConfiguration(); 
  }
  
}//fin classe SIMRenderer



