/**
 * 
 */
package sim.geometry;

import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.math.SMath;
import sim.math.SVector3d;

/**
 * La classe <b>SGeometricIntersection</b> représente une classe utilitaire
 * permettant d'évaluer des intersections entre un rayon et différentes
 * géométries.
 * <p>
 * Le rayon étant paramétrisé dans le temps, la solution au intersection est le
 * temps requis afin qu'un rayon intersection la géométrie. Un ensemble solution
 * vide signifie qu'aucune intersection n'a été réalisé entre le rayon et la
 * géométrie et un ensemble solution multiple signifie que le rayon peut
 * intersecter la géométrie à plusieurs endroits (dont plusieurs temps).
 * </p>
 * 
 * @author Simon Vézina
 * @since 2015-11-08
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public class SGeometricIntersection {

	/**
	 * Méthode permettant d'évaluer l'intersection entre un rayon et un plan. Un
	 * rayon peut réaliser jusqu'à <b>une intersection</b> avec un plan.
	 * 
	 * @param ray     - Le rayon à intersecter avec le plan.
	 * @param r_plane - La position de référence du plan.
	 * @param n_plane - La normale à la surface du plan.
	 * @return L'ensemble solution des temps pour réaliser l'intersection entre le
	 *         rayon et le plan <u>dans un tableau trié</u>. L'ensemble solution
	 *         contient 0 ou 1 élément.
	 */
	public static double[] planeIntersection(SRay ray, SVector3d r_plane, SVector3d n_plane) {
		double A = n_plane.dot(ray.getDirection());
		double B = n_plane.dot(ray.getOrigin().substract(r_plane));
		
		return SMath.linearRealRoot(A, B);
		
		//throw new SNoImplementationException("Erreur SGeometricIntersection : La méthode n'a pas été implémentée.");
	}

	/**
	 * Méthode permettant d'évaluer l'intersection entre un rayon et une sphère. Un
	 * rayon peut réaliser jusqu'à <b>deux intersections</b> avec une sphère.
	 * 
	 * @param ray      - Le rayon à intersecter avec la sphère.
	 * @param r_sphere - La position de la sphère.
	 * @param R        - Le rayon de la sphère (doit être positif).
	 * @return L'ensemble solution des temps pour réaliser l'intersection entre le
	 *         rayon et la sphère <u>dans un tableau trié</u>. L'ensemble solution
	 *         contient 0, 1 ou 2 éléments.
	 */
	public static double[] sphereIntersection(SRay ray, SVector3d r_sphere, double R) {
	
		double A = ray.getDirection().dot(ray.getDirection());
		double B = ray.getOrigin().substract(r_sphere).dot(ray.getDirection()) * 2 ;
		double C = ray.getOrigin().substract(r_sphere).dot(ray.getOrigin().substract(r_sphere)) - (R*R);
		
		return SMath.quadricRealRoot(A, B, C);
		
		//throw new SNoImplementationException("Erreur SGeometricIntersection : La méthode n'a pas été implémentée.");
	}

	/**
	 * Méthode permettant d'évaluer l'intersection entre un rayon et un tube infini.
	 * Un rayon peut réaliser jusqu'à <b>deux intersections</b> avec le tube infini.
	 * 
	 * @param ray    - Le rayon à intersecter avec le tube infini.
	 * @param r_tube - Une position sur l'axe central du tube infini.
	 * @param axis   - L'axe du tube (doit être normalisé).
	 * @param R      - Le rayon du tube (doit être positif).
	 * @return L'ensemble solution des temps pour réaliser l'intersection entre le
	 *         rayon et le tube infini <u>dans un tableau trié</u>. L'ensemble
	 *         solution contient 0, 1 ou 2 éléments.
	 */
	public static double[] infiniteTubeIntersection(SRay ray, SVector3d r_tube, SVector3d axis, double R) {
		throw new SNoImplementationException("Erreur SGeometricIntersection : La méthode n'a pas été implémentée.");
	}

	/**
	 * Méthode permettant d'évaluer l'intersection entre un rayon et deux cônes
	 * infinis relié par leur pointe. Un rayon peut réaliser jusqu'à <b>deux
	 * intersections</b> avec les deux cônes infinis.
	 * 
	 * @param ray    - Le rayon à intersecter avec les deux cônes.
	 * @param r_cone - Une position sur l'axe central des deux cônes où le rayon
	 *               <i>R</i> a été défini.
	 * @param axis   - L'axe des deux cônes dans la direction localisant la pointe
	 *               du cône à partir de la position <i>r_cone</i> (doit être
	 *               normalisé).
	 * @param R      - Le rayon du cône à la position <i>r_cone</i> (doit être
	 *               positif).
	 * @param H      - La hauteur du cône étant définie comme la distance entre la
	 *               position <i>r_cone</i> et la pointe des cônes (doit être
	 *               positif).
	 * @return L'ensemble solution des temps pour réaliser l'intersectin entre le
	 *         rayon et les deux cônes infinis <u>dans un tableau trié</u>.
	 *         L'ensemble solution contient 0, 1 ou 2 éléments.
	 */
	public static double[] infiniteTwoConeIntersection(SRay ray, SVector3d r_cone, SVector3d axis, double R, double H) {
		throw new SNoImplementationException("Erreur SGeometricIntersection : La méthode n'a pas été implémentée.");
	}

	/**
	 * Méthode permettant d'évaluer l'intersection entre un rayon et un tore
	 * (beigne). Un rayon peut réaliser jusqu'à <b>quatre intersections</b> avec un
	 * tore.
	 * 
	 * @param ray     Le rayon à intersection avec le tore.
	 * @param r_torus La position centrale du tore.
	 * @param n_torus La normale au plan du tore.
	 * @param R       Le rayon de révolution du cylindre formant le tore (doit être
	 *                positif).
	 * @param r       Le rayon de la partie cylindrique du tore (doit être positif)
	 * @return L'ensemble solution des temps pour réaliser l'intersection entre le
	 *         rayon et le tore <u>dans un tableau trié</u>. L'ensemble solution
	 *         contient 0, 1, 2, 3 ou 4 éléments.
	 * @throws SRuntimeException Si R ou r est négatif.
	 */
	public static double[] torusIntersection(SRay ray, SVector3d r_torus, SVector3d n_torus, double R, double r)
			throws SRuntimeException {
		if (R < 0)
			throw new SRuntimeException(
					"Erreur SGeometricIntersection 001 : Le rayon du tore R = " + R + " est négatif.");

		if (r < 0)
			throw new SRuntimeException(
					"Erreur SGeometricIntersection 002 : Le rayon du cylindre du tore r = " + r + " est négatif.");

		throw new SNoImplementationException("Erreur SGeometricIntersection : La méthode n'a pas été implémentée.");
	}

}// fin de la classe SGeometricIntersection
