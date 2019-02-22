/**
 * 
 */
package sim.math;

import java.util.List;

import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.readwrite.SWriteable;

/**
 * <p>
 * L'interface <b>SVector</b> représente un vecteur mathématique où des
 * opérations mathématiques de base peuvent être effectuées.
 * </p>
 * 
 * <p>
 * Les opérations mathématiques supportées sont les suivantes :
 * <ul>
 * - l'addition de vecteurs.
 * </ul>
 * <ul>
 * - la multiplication par un scalaire d'un vecteur.
 * </ul>
 * </p>
 * 
 * @author Simon Vézina
 * @since 2015-09-22
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public interface SVector extends SWriteable {

	// ---------------------------
	// Méthodes de l'interface //
	// ---------------------------

	/**
	 * Méthode permettant d'effectuer l'addition mathématique entre deux vecteurs.
	 * 
	 * @param v Le vecteur à additionner
	 * @return Le vecteur résultat de l'addition des deux vecteurs.
	 */
	public SVector add(SVector v);

	/**
	 * Méthode permettant d'effectuer la multiplication par un scalaire d'un vecteur
	 * avec un scalaire.
	 * 
	 * @param cst La constante scalaire à multiplier avec le vecteur.
	 * @return Le vecteur résultant de la multiplication par un scalaire du vecteur.
	 */
	public SVector multiply(double cst);

	/**
	 * Méthode permettant d'effectuer le <b>produit scalaire</b> entre deux
	 * vecteurs.
	 * 
	 * @param v Le vecteur à mettre en produit scalaire avec le vecteur courant.
	 * @return Le résultat du produit scalaire.
	 */
	public double dot(SVector v);

	// ------------
	// MÉTHODES //
	// ------------

	/**
	 * <p>
	 * Méthode effectuant le calcul de l'interpolation linéaire entre deux vecteurs
	 * v0 et v1 par le facteur t. L'équation mathématique correspondant à
	 * l'interpolation est
	 * <ul>
	 * v = v0*(1 - t) + v1*t
	 * </ul>
	 * où v0 est le vecteur de référence et v1 est le vecteur pondéré par le facteur
	 * t.
	 * </p>
	 * 
	 * @param v0 Le 1ier vecteur de référence pondéré par 1 - t.
	 * @param v1 Le 2ième vecteur pondéré par le facteur t.
	 * @param t  Le paramètre de pondération.
	 * @return Le vecteur interpolé.
	 */
	public static SVector linearInterpolation(SVector v0, SVector v1, double t) {
		throw new SNoImplementationException("La méthode n'a pas été implémentée.");
	}

	/**
	 * <p>
	 * Méthode effectant le calcul de l'interpolation linéaire en coordonnée
	 * barycentrique entre trois vecteurs v0, v1 et v2 par le facteur t1 et t2.
	 * L'équation mathématique correspondant à l'interpolation est
	 * <ul>
	 * v = v0*(1 - t1 - t2) + v1*t1 + v2*t2
	 * </ul>
	 * où v0 est le vecteur de référence et v1 est le vecteur pondéré par le facteur
	 * t1 et v2 est le vecteur pondéré par le facteur t2.
	 * </p>
	 * 
	 * @param v0 Le 1ier vecteur de référence pondéré par 1 - t1 - t2.
	 * @param v1 Le 2ième vecteur pondéré par t1.
	 * @param v2 Le 3ième vecteur pondéré par t2.
	 * @param t1 Le 1ier paramètre de pondération.
	 * @param t2 Le 2ième paramètre de pondération.
	 * @return Le vecteur interpolé.
	 */
	public static SVector linearBarycentricInterpolation(SVector v0, SVector v1, SVector v2, double t1, double t2) {
		
		SVector v = v0.multiply(1 - t1 - t2).add(v1.multiply(t1).add(v2.multiply(t2)));
		return v;
		//throw new SNoImplementationException("La méthode n'a pas été implémentée.");
	}

	/**
	 * <p>
	 * Méthode effectant le calcul de l'interpolation linéaire en coordonnée
	 * barycentrique entre plusieurs vecteurs v0, v1, ... et v_n par les facteur t1,
	 * t2, ..., t_n. L'équation mathématique correspondant à l'interpolation est
	 * <ul>
	 * v = v0*(1 - t1 - t2 - ... - t_n) + v1*t1 + v2*t2 + ... + v_n*t_n
	 * </ul>
	 * où v0 est le vecteur de référence et les vecteur v_i sont les vecteurs
	 * pondérés par les facteurs t_i.
	 * </p>
	 * 
	 * @param vector_list La liste des vecteurs dans l'interpolation. Cette liste
	 *                    contient n+1 vecteur (v0 et les v_i étant de nombre n).
	 * @param t_list      La liste des facteurs de pondérations des vecteurs. Cette
	 *                    liste contient n facteurs (les n facteurs des n vecteur
	 *                    v_i).
	 * @return Le vecteur interpolé.
	 * @throws SRuntimeException Si le nombre d'élément des listes n'est pas adéquat
	 *                           (vector_list.size() != t_list.size()+1).
	 */
	public static SVector linearBarycentricInterpolation(List<SVector> vector_list, List<Double> t_list)
			throws SRuntimeException {
		// Vérifier que les deux listes ont la même taille
		if (vector_list.size() != t_list.size() + 1)
			throw new SRuntimeException(
					"Erreur SVectorUtil 007 : L'interpolation est impossible puisque les deux listes passées en paramètre n'ont les bonnes tailles.");

		// Vérification que la liste des vecteurs n'est pas vide
		if (vector_list.isEmpty())
			throw new SRuntimeException(
					"Erreur SVectorUtil 008 : L'interpolation est impossible puisque la liste des vecteurs est vide.");

		throw new SNoImplementationException("La méthode n'a pas été implémentée.");
	}

}// fin de l'interface SVector
