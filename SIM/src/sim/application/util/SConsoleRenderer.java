/**
 * 
 */
package sim.application.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.geometry.space.SAbstractGeometrySpace;
import sim.graphics.SRaytracer;
import sim.graphics.SScene;
import sim.util.SChronometer;
import sim.util.SLog;
import sim.util.SStringUtil;

/**
 * La classe <b>SConsoleRenderer</b> repr�sente une console permettant de g�n�rer une image par 
 * calcul de <i>ray tracing</i> � partir d'un fichier de configuration. 
 * 
 * @author Simon V�zina
 * @since 2015-08-23
 * @version 2016-01-01
 */
public class SConsoleRenderer {

  /**
   * M�thode pour lancer l'application du ray tracer en format "console".
   * @param config - La configuration de l'application.
   */
  public static void raytrace(SConfiguration config)
  {
    try
    {
      SChronometer chrono = new SChronometer();
      
      //Lecture de la sc�ne
      chrono.start();
      SLog.logWriteLine("Message SConsoleRenderer : Lecture de la sc�ne '" + config.getReadDataFileName() + "'.");
      
      SScene scene = new SScene(config.getReadDataFileName());
        
      chrono.stop();
      SLog.logWriteLine("Message SConsoleRenderer : Fin de la lecture de la sc�ne.");
      SLog.logWriteLine("Message SConsoleRenderer : Dur�e : " + chrono.getTime() + " s.");
      SLog.logWriteLine("");
      
      // D�but du ray tracing
      SLog.logWriteLine("Message SConsoleRenderer : D�but du raytracing.");
      
      chrono.start();
      
      // Construction du raytracer
      SRaytracer raytracer = scene.buildRaytracer();
      
      // Afficher une ligne de "-" afin de mieux visualiser la progression du ray tracing
      for(int i=0; i<101; i++)
        SLog.logWrite("_");
      SLog.logWriteLine();
      
      // Effectuer le ray tracing avec affichage "." � chaque 1% compl�t�
      int pixels = raytracer.nbPixels();
      int pack = pixels / 100;
      
      for(int i=0; i<101; i++)
      {
        //Calcul du ray tracing
        raytracer.raytrace(pack);  //calcul pour une groupe de 1% des pixels
        
        SLog.logWrite(".");        //�criture du caract�re "." � chaque 1% de compl�t�
      }
      
      chrono.stop();
      
      SLog.logWriteLine();
      SLog.logWriteLine();
      SLog.logWriteLine("Message SConsoleRenderer : Fin du raytracing.");
      SLog.logWriteLine("Message SConsoleRenderer : Dur�e : " + chrono.getTime() + " s.");
      SLog.logWriteLine("Message SConsoleRenderer : Nombre de tests d'intersection r�alis�s : " + SAbstractGeometrySpace.getIntersectionTestCount() + " tests.");
      SLog.logWriteLine();
      
      //�criture de l'image
      chrono.start();
      SLog.logWriteLine("Message SConsoleRenderer : �criture de l'image.");
      
      scene.getViewport().writeImage();
      
      chrono.stop();
      SLog.logWriteLine("Message SConsoleRenderer : Fin de l'�criture de l'image.");
      SLog.logWriteLine("Message SConsoleRenderer : Dur�e : " + chrono.getTime() + " s.");
      SLog.logWriteLine("");
      
      //�criture de la sc�ne
      scene.write(config.getWriteDataFileName());
    
    }catch(FileNotFoundException e){       
      SLog.logWriteLine("Message SConsoleRenderer : Une erreur de type FileNotFoundException est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage()); 
    }catch(SConstructorException e){
      SLog.logWriteLine("Message SConsoleRenderer : Une erreur de type SConstructorException est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage()); 
      e.printStackTrace();
    }catch(IOException e){                 
      // Exception lanc�e s'il y a un probl�me en m�moire lors de la lecture/�criture d'un fichier             
      e.printStackTrace();                                               
    }
    
    SLog.logWriteLine("Message SConsoleRenderer : Fin de l'application."); 
  }

}//fin de la classe SConsoleRenderer
