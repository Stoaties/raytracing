/**
 * 
 */
package sim.graphics;

import java.util.ArrayList;
import java.util.List;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.graphics.shader.SShader;
import sim.math.SVectorPixel;

/**
 * La classe <b>SRaytracerRunnable</b> repr�sente une t�che d'un <i>ray tracer</i>. 
 * Cette t�che peut �tre effectu� en parall�le (<i>multithread</i>).
 * 
 * @author Simon V�zina
 * @since 2015-12-05
 * @version 2015-12-20
 */
public class SRaytracerRunnable extends SAbstractRaytracer implements Runnable {

  private final List<SVectorPixel> pixel_list;
  private final List<SColor> color_list;
  
  /**
   * La variable <b>sampling</b> correspond au nombre de rayons lanc�s dans chaque calcul de couleur d'un pixel.
   */
  private final int sampling;                       
  
  /**
   * Constructeur d'une t�che de <b>ray tracing</b>.
   * 
   * @param view_frustum - La pyramide de vue.
   * @param shader - Le calculateur d'illumination.
   * @param viewport - L'�cran de vue.
   * @param sampling - Le nombre de rayon par pixel dans le calcul de la couleur.
   * @param nb_pixel_to_calculate - Le nombre de pixels qui seront calcul�s lors de l'ex�cution de la t�che.
   * @throws SConstructorException - Si le nombre de rayons par pixel (<i>sampling</i>) est inf�rieur � 1.
   */
  public SRaytracerRunnable(SViewFrustum view_frustum, SShader shader, SViewport viewport, int sampling, int nb_pixel_to_calculate) throws SConstructorException
  {
    super(view_frustum, shader, viewport);
    
    if(sampling < 1)
      throw new SConstructorException("Erreur SRaytracerRunnable 001 : Le nombre de rayon par pixel '" + sampling + "' n'est pas sup�rieur � 0.");
    
    this.sampling = sampling;
    
    pixel_list = new ArrayList<SVectorPixel>();
    color_list = new ArrayList<SColor>();
    
    for(int i = 0; i < nb_pixel_to_calculate && viewport.hasNextPixel(); i++)
      pixel_list.add(viewport.nextPixel());
  }

  @Override
  public int nbPixels() 
  {
    return pixel_list.size();
  }
  
  /**
   * M�thode pour faire l'affectation des couleurs calcul�es dans le viewport.
   * 
   * @throws SRuntimeException S'il y avait des calculs � effecter et qu'ils n'ont pas �t� r�alis�s.
   */
  public void setColorViewport() throws SRuntimeException
  {
    // V�rifier s'il y a des pixels � calculer
    if(!pixel_list.isEmpty())
    {
      // S'assurer que le calcul des couleurs a �t� effectu�
      if(color_list.isEmpty())
        throw new SRuntimeException("Erreur SRaytracerRunnable 002 : Le raytracer n'a pas encore calcul� de couleur pouvant �tre affect�e au viewport.");
      
      // Faire l'affectation des couleurs dans le viewport en fonction des coordonn�es de pixel retenues dans le raytracer
      for(int i = 0; i < pixel_list.size(); i++)
        viewport.setColor(pixel_list.get(i), color_list.get(i));
    }
  }

  @Override
  public void raytrace(int nb_pixel) throws SRuntimeException
  {
    //V�rification du nombre de pixels demand� en calcul
    if(nb_pixel < 0)
      throw new SRuntimeException("Erreur SRaytracerRunnable 003 : Le nombre de pixels '" + nb_pixel + "' doit �tre sup�rieur � 0.");

    // It�ration sur l'ensemble des pixels demand� en calcul
    for(int i = 0; i < nb_pixel; i++)
    {
      int index_next_color = color_list.size();
      
      // V�rifier s'il reste des pixels � �valuer
      if(index_next_color < pixel_list.size())
        color_list.add(evaluatePixelColor(pixel_list.get(index_next_color)));
      else
        return;
    }
  }

  @Override
  public void run()
  {
    raytrace();
  }

  @Override
  protected SColor evaluatePixelColor(SVectorPixel pixel)
  {
    // Raytracting � un rayon par pixel  
    if(sampling == 1)                         
      return raytracePixel(pixel);
    // Raytracing � plusieurs rayons par pixel
    else                                      
    {
      SColor[] tab = new SColor[sampling];
      
      //Calcul la couleur pour chaque sampling
      for(int i=0; i<tab.length; i++)
        tab[i] = raytracePixel(pixel);
      
      //Faire la somme des couleurs
      SColor sum = new SColor(0.0, 0.0, 0.0);
      
      for(int i=0; i<tab.length; i++)
        sum = sum.add(tab[i]);
      
      //Retourner la couleur apr�s avoir divis� par le nombre de sampling
      return sum.multiply( 1.0 / (double)sampling);
    }
  }

}// fin de la classe SRaytracerRunnable
