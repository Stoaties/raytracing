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
 * entre des coordonn�es sur une g�om�trie et une coordonn�e de texture uv.
 * 
 * @author Simon V�zina
 * @since 2015-11-10
 * @version 2017-12-20 (version labo � Le ray tracer v2.1)
 */
public class SGeometricUVMapping {

  /**
   * M�thode permettant d'obtenir une coordonn�e uv sur une sph�re.
   * 
   * @param r_sphere La position de la sph�re.
   * @param R Rayon de la sph�re (doit �tre positif).
   * @param P Le point P sur la sph�re (doit �tre sur la sph�re).
   * @return La coordonn�e de texture uv.
   * @throws SRuntimeException Si le point P n'est pas situ� sur la sph�re.
   */
  public static SVectorUV sphereUVMapping(SVector3d r_sphere, double R, SVector3d P) throws SRuntimeException
  {
    throw new SNoImplementationException("La m�thode doit �tre impl�ment�e dans le cadre d'un laboratoire.");
  }
  
}//fin de la classe SGeometricUVMapping
