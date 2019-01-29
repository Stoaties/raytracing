package sim.application.util;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

import sim.exception.SConstructorException;
import sim.geometry.space.SAbstractGeometrySpace;
import sim.graphics.SRaytracer;
import sim.graphics.SScene;
import sim.util.SChronometer;
import sim.util.SLog;

/**
 * La classe <b>SJFrameRenderer</b> repr�sentant un JFrame permettant de lancer l'application du <i>ray tracer</i>.
 * 
 * @author Simon V�zina
 * @since 2015-08-22
 * @version 2017-12-08
 */
public class SJFrameRenderer extends JFrame {

  /**
   * La variable <b>NB_LINES_BEFORE_REPAINT</b> repr�sente le nombre de lignes calcul�es par le <i>ray tracer</i> avant d'effectuer un repaint du jpanel.
   */
  private final int NB_LINES_BEFORE_REPAINT = 1;   
  
  /**
   * 
   */
  private static final long serialVersionUID = 1663616456131809383L;
  
  private SJPanelRenderer contentPane;

  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur de l'application <i>ray tracer</i> dans un format "JFrame".
   */
  public SJFrameRenderer()
  {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    
    //Le panel du renderer
    contentPane = new SJPanelRenderer();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    contentPane.setPreferredSize(getMaximumSize());       //permettre au jpanel d'occuper le maximum d'espace dans le jframe
    setContentPane(contentPane);
  }

  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour lancer l'application du <i>ray tracer</i> en format "JFrame".
   * 
   * @param config - La configuration de l'application.
   */
  public void raytrace(SConfiguration config)
  {
    try
    {
      SChronometer chrono = new SChronometer();
      
      //Lecture de la sc�ne
      chrono.start();
      SLog.logWriteLine("Message SJFrameRenderer : Lecture de la sc�ne '" + config.getReadDataFileName() + "'.");
      
      SScene scene = new SScene(config.getReadDataFileName());
        
      chrono.stop();
      SLog.logWriteLine("Message SJFrameRenderer : Fin de la lecture de la sc�ne.");
      SLog.logWriteLine("Message SJFrameRenderer : Dur�e : " + chrono.getTime() + " s.");
      SLog.logWriteLine("");
      
      //Ray tracing
      chrono.start();
      SLog.logWriteLine("Message SJFrameRenderer : D�but du raytracing.");
      
      //Construction du raytracer
      SRaytracer raytracer = scene.buildRaytracer();
      
      //Mettre � jour la taille de la fen�tre
      setBounds(100, 100, scene.getViewport().getWidth(), scene.getViewport().getHeight());
      
      //Effectuer le ray tracing ligne par ligne
      int nb_pixels = scene.getViewport().getWidth()*NB_LINES_BEFORE_REPAINT;
      
      while(scene.getViewport().hasNextPixel())
      {
        raytracer.raytrace(nb_pixels);
        contentPane.setBufferedImage(scene.getViewport().getBufferedImage());
        repaint();
      }
                       
      chrono.stop();
      
      SLog.logWriteLine("Message SJFrameRenderer : Fin du raytracing.");
      SLog.logWriteLine("Message SJFrameRenderer : Dur�e : " + chrono.getTime() + " s.");
      SLog.logWriteLine("Message SJFrameRenderer : Nombre de tests d'intersection r�alis�s : " + SAbstractGeometrySpace.getIntersectionTestCount() + " tests.");
      
      SLog.logWriteLine("");
      
      //�criture de l'image
      chrono.start();
      SLog.logWriteLine("Message SJFrameRenderer : �criture de l'image.");
      
      //�criture du fichier png
      scene.getViewport().writeImage();
      
      chrono.stop();
      SLog.logWriteLine("Message SJFrameRenderer : Fin de l'�criture de l'image.");
      SLog.logWriteLine("Message SJFrameRenderer : Dur�e : " + chrono.getTime() + " s.");
      SLog.logWriteLine("");
      
      //�criture de la sc�ne
      scene.write(config.getWriteDataFileName());
 
    }catch(FileNotFoundException e){       
      SLog.logWriteLine("Message SJFrameRenderer : Une erreur de type FileNotFoundException est survenue.");
      SLog.logException(e);
    }catch(SConstructorException e){
      SLog.logWriteLine("Message SJFrameRenderer : Une erreur de type SConstructorException est survenue."); 
      SLog.logWriteLine(e.getMessage());
      e.printStackTrace();
    }catch(IOException e){                 
      SLog.logWriteLine("Message SJFrameRenderer : Une erreur de type I/O est survenue."); 
      SLog.logWriteLine(e.getMessage());
      e.printStackTrace();                                              
    }
      
    SLog.logWriteLine("Message SJFrameRenderer : Fin de l'application."); 
  }
  
}//fin de la classe SJFrameRenderer
