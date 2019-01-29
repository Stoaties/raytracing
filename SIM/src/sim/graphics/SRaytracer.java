/**
 * 
 */
package sim.graphics;

import sim.exception.SRuntimeException;

/**
 * Interface repr�sentant un raytracer permettant de g�n�rer des images 2D � partir d'une description d'un environnement 3D.
 * @author Simon V�zina
 * @since 2015-04-07
 * @version 2015-04-07
 */
public interface SRaytracer {

  /**
   * M�thode pour effectuer le calcul du raytracing pour l'ensemble des pixels d'un viewport
   * � l'aide d'une description g�om�trique d'un environnement 3D.
   */
  public void raytrace();
  
  /**
   * M�thode pour effectuer le raytracing de la sc�ne sur un nombre limit� de pixels du viewport.
   * @param nb_pixel - Le nombre de pixels � calculer. Si le nombre est sup�rieur aux pixels disponibles, ils seronts tous calcul�s.
   * @throws SRuntimeException Si le nombre de pixels � calculer est n�gatif.
   */
  public void raytrace(int nb_pixel) throws SRuntimeException;
 
  /**
   * M�thode pour obtenir le nombre total de pixels � calculer.
   * @return Le nombre de pixels � calculer.
   */
  public int nbPixels();
  
}//fin interface SRaytracer
