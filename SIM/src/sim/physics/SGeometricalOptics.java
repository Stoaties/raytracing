/**
 * 
 */
package sim.physics;

import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.math.SVector3d;

/**
 * La classe <b>SGeometricalOptics</b> représente une classe utilisaire pouvante effectuer des calculs en lien avec la <b>l'optique géométrique</b>.
 * 
 * @author Simon Vézina
 * @since 2015-01-16
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public class SGeometricalOptics {

	/**
	 * Méthode qui évalue la <b>réflexion</b> d'un rayon <b><i>v</i></b> sur une normale à la surface <b><i>N</i></b>.
	 * La solution à la réflexion est un rayon réfléchi <b><i>R</i></b>. Ce calcul respecte la <u>loi de la réflexion</u>.
	 * 
	 * @param v - Le rayon <b><i>v</i></b> incident.  
	 * @param N - La normale à la surface <b><i>N</i></b>.
	 * @return Le rayon réfléchi <b><i>R</i></b>.
	 */
	public static SVector3d reflexion(SVector3d v, SVector3d N)
	{
		throw new SNoImplementationException("Cette méthode doit être implémentée dans le cadre d'un laboratoire.");
	}
	
	/**
	 * Méthode qui évalue la <b>réfraction</b> d'un rayon <b><i>v</i></b> par rapport à une normale à la surface <b><i>N</i></b>.
	 * La solution à la réfraction est un rayon transmis <b><i>T</i></b>. Ce calcul respecte la <u>loi de la réfraction</u>.
	 * 
	 * @param v - Le rayon <b><i>v</i></b> incident.  
	 * @param N - La normale à la surface <b><i>N</i></b>.
	 * @param n1 - L'indice de réfraction du milieu incident.
	 * @param n2 - L'indice de réfraction du milieu réfracté.
	 * @return Le rayon transmis <b><i>T</i></b>.
	 * @throws SRuntimeException S'il y a réflexion totale interne ce qui interdit la transmission d'un rayon selon la loi de la réfraction.
	 */
	public static SVector3d refraction(SVector3d v, SVector3d N, double n1, double n2)throws SRuntimeException
	{
	  throw new SNoImplementationException("Cette méthode doit être implémentée dans le cadre d'un laboratoire.");
	}
	
	/**
	 * <p>
	 * Méthode qui détermine s'il y aura réflexion totale interne. 
	 * </p>
	 * <p>
	 * Une réflexion totale interne est <u>uniquement possible</u> si n1 > n2. 
	 * </p>
	 * 
	 * @param v - Le rayon <b><i>v</i></b> incident.  
	 * @param N - La normale à la surface <b><i>N</i></b>.
	 * @param n1 - L'indice de réfraction du milieu incident.
	 * @param n2 - L'indice de réfraction du milieu réfracté.
	 * @return <b>true</b> s'il y a réflexion totale interne et <b>false</b> sinon.
	 */
	public static boolean isTotalInternalReflection(SVector3d v, SVector3d N, double n1, double n2)
	{
	  throw new SNoImplementationException("Cette méthode doit être implémentée dans le cadre d'un laboratoire.");
	}

}//fin classe SGeometricalOptics
