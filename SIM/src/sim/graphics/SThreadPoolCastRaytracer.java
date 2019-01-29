/**
 * 
 */
package sim.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.graphics.shader.SShader;
import sim.graphics.SAbstractRaytracer;
import sim.math.SVectorPixel;
import sim.util.SLog;

/**
 * La classe <b>SThreadPoolCastRaytracer</b> repr�sentant un raytracer lan�ant <b>plusieurs rayons</b> dans chaque pixel du viewport. 
 * Cette m�thode permet de r�duire l'aliassage dans l'image puisque qu'une moyenne de la couleur 
 * obtenue par chaque rayon lanc� dans chaque pixel sera utilis� pour d�finir la couleur de chaque pixel.
 * 
 * @author Simon V�zina et Robin V�zina
 * @since 2015-04-07
 * @version 2016-01-13
 */
public class SThreadPoolCastRaytracer extends SAbstractRaytracer {

  private static final int DEFAULT_TASK = 2;			  //nombre de t�ches par exc�cution par d�faut (typiquement le nombre de processeur que l'on veut exploiter)
  private static final int DEFAULT_SAMPLING = 1;		//nombre de rayon lanc� par pixel par d�faut
  
  private final int task;                           //nombre de t�ches par ex�cution (nombre de thread en ex�cution simultan�e)
  private final int sampling;	                      //nombre de rayon lanc� dans chaque pixel
  
  private final ThreadPoolExecutor executor;        //ex�cuteur des t�ches de calcul de la couleur de chaque pixel 
  
  /**
   * Sous-classe repr�sentant une t�che � r�aliser par un thread.
   * La t�che � r�aliser sera de calculer une SColor et prendra comme param�tre un SVectorPixel.
   * @author Simon V�zina et Robin V�zina
   * @since 2015-04-07
   * @version 2015-04-10
   */
  protected final class FutureSColor implements Callable<SColor>
  {
    private SVectorPixel pixel;	//le pixel dont la couleur doit �tre �valu�

    /**
     * Constructeur de la futur t�che � r�aliser.
     * @param pixel - La coordonn�e du pixel dont la couleur sera � �valuer.
     */
    public FutureSColor(SVectorPixel pixel) 
    {
      this.pixel = pixel;
    }

    @Override
    public SColor call() throws Exception
    {
      //La t�che a r�aliser sera d'�valuer la couleur du pixel (m�thode protected de la classe pr�sente).
      return evaluatePixelColor(pixel);
    }
  }

  
  /**
   * Constructeur d'un raytracer de type <i>multi cast</i> avec <b>un rayon</b> par d�faut. 
   * 
   * @param view_frustum - La pyramide de vue.
   * @param shader - Le calculateur d'illumination (<i>shader</i>).
   * @param viewport - La fen�tre de rendu (<i>viewport</i>).
   */
  public SThreadPoolCastRaytracer(SViewFrustum view_frustum, SShader shader, SViewport viewport)
  {
    this(view_frustum, shader, viewport, DEFAULT_SAMPLING, DEFAULT_TASK);
  }

  /**
   * Constructeur d'un raytracer de type <i>multi cast</i>.
   * 
   * @param view_frustum - La pyramide de vue.
   * @param shader - Le calculateur d'illumination (<i>shader</i>).
   * @param viewport - La fen�tre de rendu (<i>viewport</i>).
   * @param sampling - Le nombre de rayon lanc� par pixel afin de r�duire le cr�nelage (<i>anti aliasing>/i>). 
   * @param task - Le nombre de t�ches simultan�s en calcul (pour le <i>multithreading</i>).
   * @throws SConstructorException Si le nombre de rayons par pixel (<i>sampling</i>) n'est pas sup�rieur � 0.
   * @throws SConstructorException Si le nombre de t�ches (<i>task</i>) n'est pas sup�rieur � 0.
   */
  public SThreadPoolCastRaytracer(SViewFrustum view_frustum, SShader shader, SViewport viewport, int sampling, int task)throws SConstructorException
  {
    super(view_frustum, shader, viewport);
    
    //V�rification que le sampling soit sup�rieur � 0
    if(sampling > 0)
    	this.sampling = sampling;
    else
    	throw new SConstructorException("Erreur SMultiCastRaytracer 001 : Le nombre de rayon par pixel '" + sampling + "' n'est pas sup�rieur � 0.");
    
    //V�rification que le sampling soit sup�rieur � 0
    if(task > 0)
      this.task = task;
    else
      throw new SConstructorException("Erreur SMultiCastRaytracer 002 : Le nombre de rayon par pixel '" + task + "' n'est pas sup�rieur � 0.");
      
    //Message de mauvaise fonctionnalit� si la coordon�e interne d'un pixel n'est pas al�atoire lors de super sampling
    if(sampling > 1 && view_frustum.getPixelInternalCoordinate() != SViewFrustum.RANDOM_PIXEL)
      SLog.logWriteLine("Message SMultiCastRaytracer : Puisque le sampling est � '" + sampling + "' et que la coordonn�e interne d'un pixel est '" + SViewFrustum.PIXEL_COORDINATE[view_frustum.getPixelInternalCoordinate()] + "'(code = " + view_frustum.getPixelInternalCoordinate() + ") et non pas '" + SViewFrustum.PIXEL_COORDINATE[SViewFrustum.RANDOM_PIXEL] + "'(code = " + SViewFrustum.RANDOM_PIXEL + "), l'antialiasing ne peut pas �tre applicable.");
    
    //Construction de l'ex�cuteur du threadPool
    executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(task);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see sim.graphics.SRaytracer#raytrace(int)
   */
  @Override
  public void raytrace(int nb_pixel) throws SRuntimeException
  {
    //V�rification du nombre de pixels demand� en calcul
    if(nb_pixel < 0)
      throw new SRuntimeException("Erreur SMultiCastRaytracer 003 : Le nombre de pixels '" + nb_pixel + "' doit �tre sup�rieur � 0.");

    int count = 0; // compter le nombre de pixel calcul�

    //Ex�cuter des calculs tant que le nombre de pixel d�sir� n'est pas atteint
    while(count < nb_pixel)
    {
      // S'il y a encore des pixels disponibles � �tre dessin�
      if (viewport.hasNextPixel()) 
      {
        // le prochain pixel � calculer
        List<SVectorPixel> listSVectorPixel = new ArrayList<SVectorPixel>();
        for(int i = 0; i < task && viewport.hasNextPixel(); i++)
        {
          SVectorPixel nextPixel = viewport.nextPixel();
          listSVectorPixel.add(nextPixel);
        }

        
        List<FutureTask<SColor>> list = new ArrayList<FutureTask<SColor>>();
        for(SVectorPixel sVectorPixel : listSVectorPixel)
          list.add(new FutureTask<SColor>(new FutureSColor(sVectorPixel)));

        //Chargement des t�ches � faire dans l'ex�cuteur et lancement de la thread
        for (FutureTask<SColor> f : list)
          executor.execute(f);

        List<SColor> listSColor = new ArrayList<SColor>();

        // D�terminer la couleur calcul�e par le shader pour le pixel
        try{
          
          for(FutureTask<SColor> f : list)
            listSColor.add(f.get());
            
        } catch (InterruptedException e) {
          throw new SRuntimeException("Erreur SThreadPoolCastRaytracer 004 : Raytracer interropu.", e);
        } catch (ExecutionException e) {
          throw new SRuntimeException("Erreur SThreadPoolCastRaytracer 005 : Erreur d'ex�cution.", e);
        }

        // Dessiner les pixels calcul�s dans le viewport
        for(int i = 0; i < listSColor.size(); i++) 
          viewport.setColor(listSVectorPixel.get(i), listSColor.get(i));
        
        count += listSColor.size(); // augmenter le compteur
      } 
      else                         
        count = nb_pixel;           // nombre de pixel � calcul� atteint, donc on arr�te tout
    }
  }

  @Override
  protected SColor evaluatePixelColor(SVectorPixel pixel)
  {
    //--------------------------------------------------------------------
    //�valuer la couleur d'un pixel selon le nombre de sampling � �valuer
    //--------------------------------------------------------------------
    
    if(sampling == 1)                         //� un rayon par pixel
      return raytracePixel(pixel);
    else                                      //� plusieurs rayons par pixel
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
  
}// fin classe SSingleCastRaytracer
