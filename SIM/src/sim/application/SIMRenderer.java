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
 * L'application <b>SIMRenderer</b> permet de représenter une scène en 3d sur une projection d'un écran 2d.
 * 
 * @author Simon Vézina
 * @since 2014-12-28
 * @version 2016-02-29
 */
public class SIMRenderer {

  /**
   * La constante <b>DEFAULT_CONFIG_FILE_NAME</b> correspond au nom du fichier de configuration utilisé par défaut.
   */
  public static final String DEFAULT_CONFIG_FILE_NAME = "configuration.cfg";
  
  //----------------
  // MÉTHODE MAIN //
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
      // Lancer l'application appropriée
      switch(config.getApplicationType())
      {
        // Lancer aucune application
        case 0 :  SLog.logWriteLine("Message SIMRenderer : Aucune application n'est lancée.");
                  break;
                  
        // Lancer la version "info" de l'application
        case 1 :  SLog.logWriteLine("Message SIMRenderer : La version INFO de l'application SIMRenderer est lancée.");
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
               
      //Écriture du fichier de configuration
      config.writeFile();    
     
    }catch(FileNotFoundException e){       
      SLog.logWriteLine("Message SIMRenderer : Un fichier n'a pas été trouvé." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());  
      SLog.logWriteLine("Message SIMRenderer : Un fichier n'a pas été trouvé.");
    }catch(SConstructorException e){
      SLog.logWriteLine("Message SIMRenderer : Une erreur lors de la construction d'un objet est survenue."  + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());
      e.printStackTrace();
    }catch(SNoImplementationException e){  
      // Exception lancée s'il y a une méthode non implémentée (possible lors d'un labotatoire !!!)
      SLog.logWriteLine("Message SIMRenderer : Une méthode n'a pas été implémentée."  + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage()); 
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
  // MÉTHODES //
  //------------
  
  /**
   * Méthode pour faire le chargement de la configuration.
   * Si le fichier est trouvé, la configuration sera déterminée par le contenu du fichier.
   * Si le fichier n'est pas trouvé, une configuration par défaut sera déterminée.
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
    
    // Le fichier de configuration n'a pas été trouvée.
    // Création du fichier de configuration par défaut. 
    return new SConfiguration(); 
  }
  
}//fin classe SIMRenderer



