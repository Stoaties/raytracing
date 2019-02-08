/**
 * 
 */
package sim.physics;

import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.math.SVector3d;

/**
 * La classe <b>SGeometricalOptics</b> repr�sente une classe utilisaire pouvante
 * effectuer des calculs en lien avec la <b>l'optique g�om�trique</b>.
 * 
 * @author Simon V�zina
 * @since 2015-01-16
 * @version 2017-12-20 (version labo � Le ray tracer v2.1)
 */
public class SGeometricalOptics {

	/**
	 * M�thode qui �value la <b>r�flexion</b> d'un rayon <b><i>v</i></b> sur une
	 * normale � la surface <b><i>N</i></b>. La solution � la r�flexion est un rayon
	 * r�fl�chi <b><i>R</i></b>. Ce calcul respecte la <u>loi de la r�flexion</u>.
	 * 
	 * @param v - Le rayon <b><i>v</i></b> incident.
	 * @param N - La normale � la surface <b><i>N</i></b>.
	 * @return Le rayon r�fl�chi <b><i>R</i></b>.
	 */
	public static SVector3d reflexion(SVector3d v, SVector3d N) {
		SVector3d res = v.add(N.multiply(v.multiply(-1).normalize().dot(N)).multiply(2));
		return res;
		// throw new SNoImplementationException("Cette m�thode doit �tre impl�ment�e
		// dans le cadre d'un laboratoire.");
	}

	/**
	 * M�thode qui �value la <b>r�fraction</b> d'un rayon <b><i>v</i></b> par
	 * rapport � une normale � la surface <b><i>N</i></b>. La solution � la
	 * r�fraction est un rayon transmis <b><i>T</i></b>. Ce calcul respecte la
	 * <u>loi de la r�fraction</u>.
	 * 
	 * @param v  - Le rayon <b><i>v</i></b> incident.
	 * @param N  - La normale � la surface <b><i>N</i></b>.
	 * @param n1 - L'indice de r�fraction du milieu incident.
	 * @param n2 - L'indice de r�fraction du milieu r�fract�.
	 * @return Le rayon transmis <b><i>T</i></b>.
	 * @throws SRuntimeException S'il y a r�flexion totale interne ce qui interdit
	 *                           la transmission d'un rayon selon la loi de la
	 *                           r�fraction.
	 */
	public static SVector3d refraction(SVector3d v, SVector3d N, double n1, double n2) throws SRuntimeException {
		if (!isTotalInternalReflection(v, N, n1, n2)) {
			double n = n1 / n2;
			double temp = Math.sqrt(1 - Math.pow(n, 2) * (1 - Math.pow(v.multiply(-1).dot(N.normalize()), 2)));
			SVector3d t = v.normalize().multiply(n)
					.add(N.multiply((v.multiply(-1).normalize().dot(N.normalize()) * n - temp)));
			return t;
		} else {
			return reflexion(v,N);
		}
		// throw new SNoImplementationException("Cette m�thode doit �tre impl�ment�e
		// dans le cadre d'un laboratoire.");
	}

	/**
	 * <p>
	 * M�thode qui d�termine s'il y aura r�flexion totale interne.
	 * </p>
	 * <p>
	 * Une r�flexion totale interne est <u>uniquement possible</u> si n1 > n2.
	 * </p>
	 * 
	 * @param v  - Le rayon <b><i>v</i></b> incident.
	 * @param N  - La normale � la surface <b><i>N</i></b>.
	 * @param n1 - L'indice de r�fraction du milieu incident.
	 * @param n2 - L'indice de r�fraction du milieu r�fract�.
	 * @return <b>true</b> s'il y a r�flexion totale interne et <b>false</b> sinon.
	 */
	public static boolean isTotalInternalReflection(SVector3d v, SVector3d N, double n1, double n2) {

		if (Math.pow(v.normalize().dot(N.normalize()), 2) + Math.pow(n2 / n1, 2) <= 1)
			return true;
		return false;

		// throw new SNoImplementationException("Cette m�thode doit �tre impl�ment�e
		// dans le cadre d'un laboratoire.");
	}

}// fin classe SGeometricalOptics
