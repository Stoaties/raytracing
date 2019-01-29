/**
 * 
 */
package sim.graphics;

import sim.exception.SRuntimeException;
import sim.graphics.shader.SShader;
import sim.math.SVectorPixel;

/**
 * Classe repr�sentant un raytracer lan�ant uniquement <b>une rayon</b> pour chaque pixel du viewport.
 * 
 * @author Simon V�zina
 * @since 2015-04-07
 * @version 2015-07-17
 */
public class SSingleCastRaytracer extends SAbstractRaytracer {

  /**
   * Constructeur d'un raytracer.
   * 
   * @param view_frustum - La pyramide de vue.
   * @param shader - Le calculateur d'illumination (<i>shader</i>).
   * @param viewport - La fen�tre de rendu (<i>viewport</i>).
   */
  public SSingleCastRaytracer(SViewFrustum view_frustum, SShader shader, SViewport viewport)
  {
	  super(view_frustum, shader, viewport);
  }

  /*
   * (non-Javadoc)
   * 
   * @see sim.graphics.SRaytracer#raytrace(int)
   */
  @Override
  public void raytrace(int nb_pixel) throws SRuntimeException
  {
    // V�rification du nombre de pixels demand� en calcul
    if (nb_pixel < 0)
      throw new SRuntimeException("Erreur SSingleCastRaytracer 001 : Le nombre de pixels '" + nb_pixel + "' doit �tre sup�rieur � 0.");

    for(int nb = 0; nb < nb_pixel && viewport.hasNextPixel(); nb++)
    {
      final SVectorPixel pixel = viewport.nextPixel();	//le prochain pixel � calculer
      final SColor color = evaluatePixelColor(pixel);	  //d�terminer la couleur calcul� par le shader pour le pixel	
      viewport.setColor(pixel, color);	                //dessiner le pixel avec la couleur dans le bon pixel
    }
  }

  /**
   * M�thode pour �valuer la couleur � attribuer � un pixel.
   * 
   * @param pixel - La coordonn�e du pixel.
   * @return La couleur � attribuer au pixel.
   */
  protected SColor evaluatePixelColor(SVectorPixel pixel)
  {
    return raytracePixel(pixel);
  }

}// fin classe SSingleCastRaytracer
