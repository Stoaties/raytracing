/**
 * 
 */
package sim.geometry;

import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.math.SMath;
import sim.math.SVector3d;
import sim.math.SVectorUV;

/**
 * La classe <b>SGeometricUVMapping</b> est une classe utilitaire permettant de faire la correspondance
 * entre des coordonnées sur une géométrie et une coordonnée de texture uv.
 * 
 * @author Simon Vézina
 * @since 2015-11-10
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public class SGeometricUVMapping {

  /**
   * Méthode permettant d'obtenir une coordonnée uv sur une sphère.
   * 
   * @param r_sphere La position de la sphère.
   * @param R Rayon de la sphère (doit être positif).
   * @param P Le point P sur la sphère (doit être sur la sphère).
   * @return La coordonnée de texture uv.
   * @throws SRuntimeException Si le point P n'est pas situé sur la sphère.
   */
  public static SVectorUV sphereUVMapping(SVector3d r_sphere, double R, SVector3d P) throws SRuntimeException
  {
    throw new SNoImplementationException("La méthode doit être implémentée dans le cadre d'un laboratoire.");
  }
  
}//fin de la classe SGeometricUVMapping
