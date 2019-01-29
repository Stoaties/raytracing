package sim.application.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * La classe <b>SJPanelRenderer</b> repr�sente le panel o� le viewport sera affich�.
 * 
 * @author Simon V�zina
 * @since 2015-08-22
 * @version 2017-12-08
 */
public class SJPanelRenderer extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = -8117857790526440072L;
  
  private BufferedImage image;
  
  /**
   * Create the panel.
   */
  public SJPanelRenderer()
  {
    
  }

  /**
   * M�thode pour d�finir l'image de type BufferedImage � assigner au panel.
   * @param image - L'image � dessiner.
   */
  public void setBufferedImage(BufferedImage image)
  {
    this.image = image;
  }
  
  @Override
  public void paintComponent(Graphics g) 
  { 
    //------------------------------------------------------------------
    //Proc�dure de redimensionnement en fonction de la taille du jpanel
    //------------------------------------------------------------------
    
    // Dimension du JPanel en taille de type --> jpanel.setPreferredSize(getMaximumSize());
    int width = this.getWidth();
    int height = this.getHeight();
    
    // Create new (blank) image of required (scaled) size
    BufferedImage scaledImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
    
    // Paint scaled version of image to new image
    Graphics2D graphics2D = scaledImage.createGraphics();
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2D.drawImage(image, 0, 0, width, height, null);
    
    // clean up
    graphics2D.dispose();
    
    //--------------------------------
    //Dessiner l'image redimensionn�e
    //--------------------------------
    
    g.drawImage(scaledImage, 0, 0, null); 
    repaint(); 
  } 

}//fin de la classe SJPanelRenderer
