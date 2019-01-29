/**
 * 
 */
package sim.graphics;

import java.util.ArrayList;
import java.util.List;

import sim.exception.SConstructorException;
import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.graphics.shader.SShader;
import sim.math.SVectorPixel;
import sim.util.SLog;
import sim.util.SStringUtil;

/**
 * La classe <b>SMultiCastRaytracer</b> correspond � un <i>ray tracer</i> pouvant lancer plusieurs rayons par pixel afin
 * d'y �valuer une couleur. Le <i>ray tracer</i> support �galement le calcul en parall�le (<i>multithread</i>).
 * 
 * @author Simon V�zina
 * @since 2015-12-06
 * @version 2015-12-20
 */
public class SMultiCastRaytracer extends SAbstractRaytracer {

  /**
   * La variable <b>task</b> repr�sente le nombre de t�ches effectu�es en parall�le.
   */
  private final int task;  
  
  /**
   * La variable <b>sampling</b> repr�ente le nombre de rayon lanc� par pixel lors du calcul de la couleur.
   */
  private final int sampling;                       
  
  /**
   * Constructeur d'un raytracer � lanc� multiple de rayon par pixel.
   * 
   * @param view_frustum - La pyramide de vue.
   * @param shader - Le calculateur d'illumination.
   * @param viewport - L'�cran de vue.
   * @param sampling - Le nombre de rayon lanc� par pixel.
   * @param task - Le nombre de t�ches r�alis�es en parall�le.
   */
  public SMultiCastRaytracer(SViewFrustum view_frustum, SShader shader, SViewport viewport, int sampling, int task)
  {
    super(view_frustum, shader, viewport);
    
    //V�rification que le sampling soit sup�rieur � 0
    if(sampling < 1)
      throw new SConstructorException("Erreur SMultiCastRaytracer 001 : Le nombre de rayon par pixel '" + sampling + "' n'est pas sup�rieur � 0.");
    
    //V�rification que le sampling soit sup�rieur � 0
    if(task < 1)
      throw new SConstructorException("Erreur SMultiCastRaytracer 002 : Le nombre de rayon par pixel '" + task + "' n'est pas sup�rieur � 0.");
      
    this.sampling = sampling;
    this.task = task;
    
    //Message de mauvaise fonctionnalit� si la coordon�e interne d'un pixel n'est pas al�atoire lors de super sampling
    if(sampling > 1 && view_frustum.getPixelInternalCoordinate() != SViewFrustum.RANDOM_PIXEL)
      SLog.logWriteLine("Message SMultiCastRaytracer : Puisque le sampling est � '" + sampling + "' et que la coordonn�e interne d'un pixel est '" + SViewFrustum.PIXEL_COORDINATE[view_frustum.getPixelInternalCoordinate()] + "'(code = " + view_frustum.getPixelInternalCoordinate() + ") et non pas '" + SViewFrustum.PIXEL_COORDINATE[SViewFrustum.RANDOM_PIXEL] + "'(code = " + SViewFrustum.RANDOM_PIXEL + "), l'antialiasing ne peut pas �tre applicable.");
  }

  @Override
  public void raytrace(int nb_pixel) throws SRuntimeException
  {
    // Nombre de pixel par t�che
    int pixel_per_task = nb_pixel / task;
    
    // Faire la liste des raytracer (runnable) et la remplir
    List<SRaytracerRunnable> raytracer_list = new ArrayList<SRaytracerRunnable>();
    
    // Le nombre de pixel restant � �tre affect� aux diff�rents thread de l'ex�cution
    int pixel_left_to_calculate = nb_pixel;
    
    for(int i = 0; i < task; i++)
      if(i == task -1 )
      {
        raytracer_list.add(new SRaytracerRunnable(view_frustum, shader, viewport, sampling, pixel_left_to_calculate));  // les restants
        pixel_left_to_calculate = 0;
      }
      else
      {
        raytracer_list.add(new SRaytracerRunnable(view_frustum, shader, viewport, sampling, pixel_per_task));  
        pixel_left_to_calculate = pixel_left_to_calculate - pixel_per_task;
      }
    
    // Faire la liste des threads et la remplir
    List<Thread> thread_list = new ArrayList<Thread>();
    
    for(int i = 0; i < task; i++)
      thread_list.add(new Thread(raytracer_list.get(i)));
    
    // Partir l'ensemble des threads
    for(Thread t : thread_list)
      t.start();
    
    // Faire l'attente de la fin de toutes les threads de la liste
    for(Thread t : thread_list)
      try{
        t.join();
      }catch(InterruptedException e){
        throw new SRuntimeException("Erreur SMultiCastRaytracer 003 : Un thread fut interropue spontan�ment." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
      }
    
    // Transf�rer les couleurs calcul�es par les raytracer dans le viewport
    for(SRaytracerRunnable r : raytracer_list)
      r.setColorViewport();
    
  }

  @Override
  protected SColor evaluatePixelColor(SVectorPixel pixel)
  {
    throw new SNoImplementationException("Erreur SMultiCastRaytracer 004 : La m�thode n'a pas �t� impl�ment�e.");
  }

}//fin de la classe SMultiCastRaytracer
