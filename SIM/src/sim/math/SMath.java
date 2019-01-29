/**
 * 
 */
package sim.math;

import java.util.Arrays;

import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;

/**
 * La classe <b>SMath</b> contient des méthodes de calcul qui sont
 * complémentaire à la classe java.lang.Math. Elle perment entre autre d'évaluer
 * les racines réelles d'un polynôme de degré 1, 2, 3 et 4.
 * 
 * @author Simon Vézina
 * @since 2015-02-19
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public final class SMath {

	/**
	 * La constante <b>NO_ROOT_SOLUTION</b> représente le tableau des solutions au
	 * racine d'un polynôme où il n'y a <u>pas de solution réelle</u>. Le tableau
	 * est donc vide et de taille égale à zéro.
	 */
	public static double[] NO_ROOT_SOLUTION = new double[0];

	/**
	 * <p>
	 * La constante <b>EPSILON</b> représentante un nombre très petit, mais non nul.
	 * Ce chiffre peut être utilisé pour comparer une valeur de type double avec le
	 * chiffre zéro. Puisqu'un double égale à zéro est difficile à obtenir
	 * numériquement après un calcul (sauf si l'on multiplie par zéro), il est
	 * préférable de comparer un "pseudo zéro" avec cette constante.
	 * </p>
	 * <p>
	 * Avec une valeur de EPSILON = 1e-10, cette valeur permet de comparer
	 * adéquatement des nombres autour de '1' avec suffisamment de chiffres
	 * significatifs.
	 * </p>
	 */
	public static double EPSILON = 1e-10;

	/**
	 * La constante <b>EPSILON_RELAXED</b> représente un nombre très petit, mais
	 * mille fois plus grand que EPSILON (1000*EPSILON). Pour des raisons
	 * numériques, le seuil EPSILON est trop petit et ce nouveau seuil peut être
	 * adéquat.
	 */
	public static double EPSILON_RELAXED = EPSILON * 1000.0;

	/**
	 * La constante <b>NEGATIVE_EPSILON</b> représentante un nombre très petit, mais
	 * non nul qui est <b>negatif</b>. Ce chiffre peut être utilisé pour comparer
	 * une valeur arbiraire de type double avec le chiffre zéro, mais qui sera
	 * négatif. Puisqu'un double égale à zéro est difficile à obtenir numériquement
	 * après un calcul (sauf si l'on multiplie par zéro), il est préférable de
	 * comparer un "pseudo zéro" avec cette constante.
	 */
	public static double NEGATIVE_EPSILON = -1.0 * EPSILON;

	/**
	 * La constante <b>ONE_PLUS_EPSILON</b> représente une constante égale à <b>1 +
	 * EPSILON</b> ce qui correspond à un nombre légèrement supérieur à 1.
	 */
	public static double ONE_PLUS_EPSILON = 1 + EPSILON;

	/**
	 * La constante <b>ONE_MINUS_EPSILON</b> représente une constant égale à <b>1 -
	 * EPSILON</b> ce qui correspond à un nombre légèrement inférieur à 1.
	 */
	public static double ONE_MINUS_EPSILON = 1 - EPSILON;

	/**
	 * La constante <b>ONE_PLUS_1000EPSILON</b> représente une constante égale à
	 * <b>1 + 1000*EPSILON</b> ce qui correspond à un nombre légèrement supérieur à
	 * 1.
	 */
	public static double ONE_PLUS_1000EPSILON = 1 + 1000 * EPSILON;

	/**
	 * La constante <b>ONE_MINUS_EPSILON</b> représente une constant égale à <b>1 -
	 * 1000*EPSILON</b> ce qui correspond à un nombre légèrement inférieur à 1.
	 */
	public static double ONE_MINUS_1000EPSILON = 1 - 1000 * EPSILON;

	/**
	 * La constante <b>INFINITY</b> représente un nombre positif égale à l'infini.
	 * Cette valeur est obtenue à partir de la classe java.lang.Double.
	 * 
	 * @see java.lang.Double
	 */
	public static double INFINITY = Double.POSITIVE_INFINITY;

	/**
	 * Méthode pour déterminer si deux nombres de type double sont <b>relativement
	 * égaux</b>. En utilisant une approche de calcul de différence, on vérifie si
	 * <ul>
	 * a - b < EPSILON*ref
	 * </ul>
	 * afin de <b>validé l'égalité</b> entre a et b (a == b). EPSILON est un seuil
	 * de précision et ref est une base de référence (la valeur absolue la plus
	 * élevée parmis a et b).
	 * <p>
	 * Cependant, si les deux chiffres sont inférieurs à EPSILON, ils seront
	 * considérés comme égaux.
	 * </p>
	 * 
	 * @param a Le 1ier nombre à comparer.
	 * @param b Le 2ième nombre à comparer.
	 * @return <b>true</b> si les deux nombres sont <b>relativement égaux</b> et
	 *         <b>false</b> sinon.
	 */
	public static boolean nearlyEquals(double a, double b) {
		return nearlyEquals(a, b, EPSILON);
	}

	/**
	 * Méthode pour déterminer si deux nombres de type double sont <b>relativement
	 * égaux</b>. En utilisant une approche de calcul de différence, on vérifie si
	 * <ul>
	 * a - b < EPSILON*ref
	 * </ul>
	 * afin de <b>validé l'égalité</b> entre a et b (a == b). EPSILON est un seuil
	 * de précision et ref est une base de référence (la valeur absolue la plus
	 * élevée parmis a et b).
	 * <p>
	 * Cenpendant, si les deux chiffres sont inférieurs à EPSILON, ils seront
	 * considérés comme égaux.
	 * </p>
	 * 
	 * @param a       Le 1ier nombre à comparer.
	 * @param b       Le 2ième nombre à comparer.
	 * @param epsilon - La précision acceptable.
	 * @return <b>true</b> si les deux nombres sont <b>relativement égaux</b> et
	 *         <b>false</b> sinon.
	 */
	public static boolean nearlyEquals(double a, double b, double epsilon) {
		double absA = Math.abs(a);
		double absB = Math.abs(b);
		double diff = Math.abs(a - b);

		// Vérification du cas particulier : 0 = 0 et infiny = infiny (mais pas certain
		// ...)
		if (a == b)
			return true;

		// Cas des petites chiffres : Vérifier si les deux chiffres sont très près l'un
		// de l'autre
		if (diff < epsilon)
			return true;

		// Cas général
		double positive_max;

		if (absA > absB)
			positive_max = absA;
		else
			positive_max = absB;

		if (diff < positive_max * epsilon)
			return true;
		else
			return false;
	}

	/**
	 * Méthpde pour déterminer si une valeur est relativement près de zéro. Cette
	 * méthode est nécessaire, car une opération mathématique menant au chiffre 0
	 * peut être 0.0 et -0.0 ce qui n'est pas égal selon JAVA.
	 * 
	 * @param value La valeur à comparer avec 0.
	 * @return <b>true</b> si la valeur est <b>relativement égal</b> à zéro et
	 *         <b>false</b> sinon.
	 */
	public static boolean nearlyZero(double value) {
		return nearlyEquals(value, 0.0);
	}

	/**
	 * Méthode pour déterminer si deux tableau de nombres de type double sont
	 * <b>relativement égaux</b>. En utilisant une approche de calcul de différence,
	 * on vérifie si pour chaque élément du tableau
	 * <ul>
	 * a - b < EPSILON*ref
	 * </ul>
	 * afin de <b>validé l'égalité</b> entre a et b (a == b). EPSILON est un seuil
	 * de précision et ref est une base de référence (la valeur absolue la plus
	 * élevée parmis a et b).
	 * <p>
	 * Cenpendant, si les deux chiffres sont inférieurs à EPSILON, ils seront
	 * considérés comme égaux.
	 * </p>
	 * 
	 * @param tab1    Le premier tableau à comparer.
	 * @param tab2    Le deuxième tableau à comparer.
	 * @param epsilon La précision acceptable.
	 * @return <b>true</b> si les deux tableaux sont <b>relativement égaux</b> et
	 *         <b>false</b> sinon.
	 */
	public static boolean nearlyEquals(double[] tab1, double[] tab2, double epsilon) {
		// Vérifier que les deux tableaux ont la même taille.
		if (tab1.length != tab2.length)
			return false;

		// Vérifier que l'ensemble des donnée indexe par indexe sont "nearlyEquals".
		for (int i = 0; i < tab1.length; i++)
			if (!nearlyEquals(tab1[i], tab2[i], epsilon))
				return false;

		// Puisque l'ensemble des valeurs indexe par indexe sont "nearlyEquals", le
		// tableau doit l'être.
		return true;
	}

	/**
	 * Méthode permettant d'évaluer la racine réelle d'un polynôme de degré '1' de
	 * la forme
	 * <ul>
	 * Ax + B = 0.
	 * </ul>
	 * <p>
	 * Un polynôme de degré '1' possède au maximum <b>une</b> racine réelle.
	 * </p>
	 * 
	 * @param A Le coefficient devant le terme de puissance '1' (x).
	 * @param B Le coefficient devant le terme de puissance '0' (1).
	 * @return La racine réelle d'un polynôme de degré '1' (s'il y en a).
	 * @throws SInfinityOfSolutionsException Si le polynôme contient une infinité de
	 *                                       solution (0x + 0 = 0).
	 */
	public static double[] linearRealRoot(double A, double B) throws SInfinityOfSolutionsException {
		// Vérifier si le polynôme n'est pas d'un degré inférieur.
		if (nearlyZero(A)) {
			// Vérifier si l'équation n'est pas 0*t + B = 0.
			// Si B == 0, l'équation admettra une infinité de solutions.
			// Autrement, l'équation n'admettra aucune solution.
			if (nearlyZero(B))
				throw new SInfinityOfSolutionsException(
						"L'équation linéaire 0x + 0 = 0 admet une infinité de solutions.");
			else
				return NO_ROOT_SOLUTION;
		}
		double[] res = {-B/A};
		return (res);
		
		//throw new SNoImplementationException("Erreur SMath : C'est méthode n'a pas été implémentée.");

	}

	/**
	 * Méthode permettant d'évaluer les racines réelles d'un polynôme de degré '2'
	 * de la forme
	 * <ul>
	 * Ax^2 + Bx + C = 0.
	 * </ul>
	 * <p>
	 * Un polynôme de degré '2' possède au maximum <b>deux</b> racines réelles.
	 * </p>
	 * 
	 * @param A Le coefficient devant le terme de puissance '2' (x^2).
	 * @param B Le coefficient devant le terme de puissance '1' (x).
	 * @param C Le coefficient devant le terme de puissance '0' (1).
	 * @return Les racines réelles de d'un polynôme de degré '2' (s'il y en a). Les
	 *         solutions retournées dans un tableau sont <b>triées en ordre
	 *         croissant</b>.
	 * @throws SInfinityOfSolutionsException Si le polynôme contient une infinité de
	 *                                       solution (0x^2 + 0x + 0 = 0).
	 */
	public static double[] quadricRealRoot(double A, double B, double C) throws SInfinityOfSolutionsException {
		// Vérifier si le polynôme n'est pas d'un degré inférieur
		double[] solution = new double[2];
		if (nearlyZero(A))
			return linearRealRoot(B, C);

		double discriminant = B*B - 4*A*C;
		if(discriminant<0) {
			return new double[0];
		} else {
			
			double solution1;
			double solution2;
			solution1 = (-B+Math.sqrt(discriminant))/(2*A);
			solution2 = (-B-Math.sqrt(discriminant))/(2*A);
			if(solution1 > solution2) {
				solution[0] = solution2;
				solution[1] = solution1;
			} else {
				solution[0] = solution1;
				solution[1] = solution2;
			}
		}
		return solution;
		//throw new SNoImplementationException("Erreur SMath : C'est méthode n'a pas été implémentée.");

	}

	/**
	 * Méthode permettant d'évaluer les racines réelles d'un polynôme de degré '3'
	 * de la forme
	 * <ul>
	 * Ax^3 + Bx^2 + Cx + D = 0.
	 * </ul>
	 * <p>
	 * Un polynôme de degré '3' possède au maximum <b>trois</b> racines réelles.
	 * </p>
	 * 
	 * @param A Le coefficient devant le terme de puissance '3' (x^3).
	 * @param B Le coefficient devant le terme de puissance '2' (x^2).
	 * @param C Le coefficient devant le terme de puissance '1' (x).
	 * @param D Le coefficient devant le terme de puissance '0' (1).
	 * @return Les racines réelles de d'un polynôme de degré '3' (s'il y en a). Les
	 *         solutions retournées dans un tableau sont <b>triées en ordre
	 *         croissant</b>.
	 * @throws SInfinityOfSolutionsException Si le polynôme contient une infinité de
	 *                                       solution (0x^3 + 0x^2 + 0x + 0 = 0).
	 */
	public static double[] cubicRealRoot(double A, double B, double C, double D) throws SInfinityOfSolutionsException {
		// Vérifier si le polynôme n'est pas d'un degré inférieur
		if (nearlyZero(A))
			return quadricRealRoot(B, C, D);

		throw new SNoImplementationException("Erreur SMath : C'est méthode n'a pas été implémentée.");
	}

	/**
	 * Méthode permettant d'évaluer les racines réelles d'un polynôme de degré '4'
	 * de la forme
	 * <ul>
	 * Ax^4 + Bx^3 + Cx^2 + Dx + E = 0.
	 * </ul>
	 * <p>
	 * Un polynôme de degré '4' possède au maximum <b>quatre</b> racines réelles.
	 * </p>
	 * 
	 * @param A Le coefficient devant le terme de puissance '4' (x^4).
	 * @param B Le coefficient devant le terme de puissance '3' (x^3).
	 * @param C Le coefficient devant le terme de puissance '2' (x^2).
	 * @param D Le coefficient devant le terme de puissance '1' (x).
	 * @param E Le coefficient devant le terme de puissance '0' (1).
	 * @return Les racines réelles de d'un polynôme de degré '4' (s'il y en a). Les
	 *         solutions retournées dans un tableau sont <b>triées en ordre
	 *         croissant</b>.
	 * @throws SInfinityOfSolutionsException Si le polynôme contient une infinité de
	 *                                       solution (0x^4 + 0x^3 + 0x^2 + 0x + 0 =
	 *                                       0).
	 */
	public static double[] quarticRealRoot(double A, double B, double C, double D, double E)
			throws SInfinityOfSolutionsException {
		// Vérifier si le polynôme n'est pas d'un degré inférieur.
		if (nearlyZero(A))
			return cubicRealRoot(B, C, D, E);

		throw new SNoImplementationException("Erreur SMath : C'est méthode n'a pas été implémentée.");
	}

	/**
	 * Méthode qui effectue le calcul inverse de l'interpolation linéaire d'une
	 * valeur numérique. Cela sigifie que l'on cherche la valeur du paramètre
	 * d'interpolation t à partir d'une valeur interpolée ainsi que des deux valeurs
	 * extrêmes.
	 * 
	 * @param v  La valeur interpolée dont la valeur de t doit être calculée.
	 * @param v0 La valeur de référence pondérée par 1 - t.
	 * @param v1 La valeur de référence pondérée par le facteur t.
	 * @return La facteur t d'interpolation linéaire.
	 */
	public static double reverseLinearInterpolation(double v, double v0, double v1) {
		// À partir de la relation v = (1-t)*v0 + t*v1 , on doit isoler t.
		return (v - v0) / (v1 - v0);
	}

	/**
	 * Méthode qui effectue le calcul de l'interpolation linéaire d'une valeur
	 * numérique.
	 * 
	 * @param v0 La valeur de référence pondérée par 1 - t.
	 * @param v1 La valeur de référence pondérée par le facteur t.
	 * @param t  Le paramètre de pondération.
	 * @return La valeur interpolée.
	 * @throws SRuntimeException Si la contrainte sur t n'est pas respectée (0 <= t
	 *                           <= 1).
	 */
	public static double linearInterpolation(double v0, double v1, double t) throws SRuntimeException {
		// Doit satisfaire la contrainte sur t
		if (t < 0.0 || t > 1.0)
			throw new SRuntimeException(
					"Erreur SMath 002 : Le paramètre d'interpolation t = " + t + " n'est pas compris entre 0 et 1.");

		// Calcul de l'interpolation : v = v0*(1 - t) + v1*t
		return v0 * (1.0 - t) + (v1 * t);
	}

	/**
	 * Méthode qui effectue le calcul de l'interpolation quadratique d'une valeur
	 * numérique.
	 * 
	 * @param v0 La valeur de référence pondérée par 1 - t*t.
	 * @param v1 La valeur de référence pondérée par le facteur t*t.
	 * @param t  Le paramètre de pondération.
	 * @return La valeur interpolée.
	 * @throws SRuntimeException Si la contrainte sur t n'est pas respectée (0 <= t
	 *                           <= 1).
	 */
	public static double quadricInterpolation(double v0, double v1, double t) throws SRuntimeException {
		// Doit satisfaire la contrainte sur t
		if (t < 0.0 || t > 1.0)
			throw new SRuntimeException(
					"Erreur SMath 003 : Le paramètre d'interpolation t = " + t + " n'est pas compris entre 0 et 1.");

		// Calcul de l'interpolation : v = v0*(1 - t^2) + v1*t^2
		double t2 = t * t;

		return v0 * (1.0 - t2) + v1 * t2;
	}

	/**
	 * ...
	 * 
	 * @param array
	 * @return
	 */
	public static double strategicArraySum(double[] array) {
		// STRATÉGIE DE SOMMATION : Trier en valeur absolue les nombres et en faire
		// l'addition.

		// Faire une copie des données dans un tableau de type objet Double.
		Double[] tab = new Double[array.length];

		for (int i = 0; i < array.length; i++)
			tab[i] = new Double(array[i]);

		// Faire le trie des données avec le comparateur en valeur absolue.
		Arrays.sort(tab, new SAbsoluteDoubleComparator());

		// Faire la somme des termes du tableau dans l'ordre stratégique qui devrait
		// minimiser l'imprécision.
		double value = 0.0;

		for (Double d : tab)
			value += d.doubleValue();

		return value;

	}

	/**
	 * Méthode déterminant le signe d'un nombre. Les résultats sont
	 * <ul>
	 * -1 si a < 0
	 * </ul>
	 * <ul>
	 * 0 si a = 0
	 * </ul>
	 * <ul>
	 * 1 si a > 0
	 * </ul>
	 * 
	 * @param a Le nombre.
	 * @return Le signe du nombre.
	 */
	public static double sgn(double a) {
		if (a > 0)
			return 1.0;
		else if (a < 0)
			return -1.0;
		else
			return 0.0;
	}

	/**
	 * Méthode pour obtenir la <b>plus grande valeur</b> d'un tableau.
	 * 
	 * @param tab Le tableau.
	 * @return La plus grande valeur du tableau.
	 */
	public static int findMax(int[] tab) {
		int max = tab[0];

		for (int v : tab)
			if (v > max)
				max = v;

		return max;
	}

	/**
	 * Méthode pour obtenir la <b>plus grande valeur</b> d'un tableau.
	 * 
	 * @param tab Le tableau.
	 * @return La plus grande valeur du tableau.
	 */
	public static double findMax(double[] tab) {
		double max = tab[0];

		for (double v : tab)
			if (v > max)
				max = v;

		return max;
	}

	/**
	 * Méthode pour obtenir la <b>plus petite valeur</b> d'un tableau.
	 * 
	 * @param tab Le tableau.
	 * @return La plus petite valeur du tableau.
	 */
	public static int findMin(int[] tab) {
		int min = tab[0];

		for (int v : tab)
			if (v < min)
				min = v;

		return min;
	}

	/**
	 * Méthode pour obtenir la <b>plus petite valeur</b> d'un tableau.
	 * 
	 * @param tab Le tableau.
	 * @return La plus petite valeur du tableau.
	 */
	public static double findMin(double[] tab) {
		double min = tab[0];

		for (double v : tab)
			if (v < min)
				min = v;

		return min;
	}

	/**
	 * Méthode pour convertir un tableau d'entier vers un tableau de nombre réel
	 * entre une valeur minimale et maximale. La correspondance entre les valeurs
	 * converties sera linéaire.
	 * 
	 * @param data Les entiers à convertir.
	 * @param min  La borne minimale.
	 * @param max  La borne maximale.
	 * @return Le tableau converti.
	 * @throws SRuntimeException Si les bornes sont mal définies.
	 */
	public static double[] mappingIntToDouble(int[] data, double min, double max) throws SRuntimeException {
		if (min > max)
			throw new SRuntimeException("Erreur SMath 003 : La borne minimale " + min + " et la borne maximale " + max
					+ " sont mal définies.");

		int min_value = SMath.findMin(data);
		int max_value = SMath.findMax(data);

		double[] result = new double[data.length];

		// Itérer sur l'ensemble des éléments du tableau.
		for (int i = 0; i < result.length; i++) {
			double t = SMath.reverseLinearInterpolation((double) data[i], (double) min_value, (double) max_value);
			result[i] = SMath.linearInterpolation(min, max, t);
		}

		return result;
	}

	/**
	 * Méthode pour convertir un tableau de nombre réel vers un tableau d'entier
	 * entre une valeur minimale et maximale. La correspondance entre les valeurs
	 * converties sera linéaire.
	 * 
	 * @param data Les données à convertir.
	 * @param min  La borne minimale.
	 * @param max  La borne maximale.
	 * @return Le tableau converti.
	 * @throws SRuntimeException Si les bornes sont mal définies.
	 */
	public static int[] mappingDoubleToInt(double[] data, int min, int max) {
		if (min > max)
			throw new SRuntimeException("Erreur SMath 004 : La borne minimale " + min + " et la borne maximale " + max
					+ " sont mal définies.");

		double min_value = SMath.findMin(data);
		double max_value = SMath.findMax(data);

		int[] result = new int[data.length];

		// Itérer sur l'ensemble des éléments du tableau.
		for (int i = 0; i < result.length; i++) {
			double t = SMath.reverseLinearInterpolation(data[i], min_value, max_value);
			result[i] = (int) SMath.linearInterpolation(min, max, t);
		}

		return result;
	}

	/**
	 * <p>
	 * Méthode permettant de générer un tableau contenant les éléments identique de
	 * deux tableaux de valeur
	 * <ul>
	 * préalablement trié
	 * </ul>
	 * .
	 * </p>
	 * 
	 * <p>
	 * <b>REMARQUE</b> : Le fonctionnement de cette méthode ne sera pas valide si
	 * les deux tableaux passés en paramètre ne sont pas péalablement trié.
	 * </p>
	 * Référence :
	 * https://stackoverflow.com/questions/32676381/find-intersection-of-two-arrays
	 * 
	 * @param tab1 Le 1ier tableau à comparer.
	 * @param tab2 Le 2ième tableau à comparer.
	 * @return Un tableau comprenant les éléments identiques de deux tableaux
	 *         (l'intersection des deux tableaux).
	 */
	public static double[] intersectionSortedArray(double[] tab1, double[] tab2) {
		return intersectionSortedArray(tab1, tab2, EPSILON);
	}

	/**
	 * <p>
	 * Méthode permettant de générer un tableau contenant les éléments identique de
	 * deux tableaux de valeur
	 * <ul>
	 * préalablement trié
	 * </ul>
	 * .
	 * </p>
	 * 
	 * <p>
	 * <b>REMARQUE</b> : Le fonctionnement de cette méthode ne sera pas valide si
	 * les deux tableaux passés en paramètre ne sont pas péalablement trié.
	 * </p>
	 * Référence :
	 * https://stackoverflow.com/questions/32676381/find-intersection-of-two-arrays
	 * 
	 * @param tab1    Le 1ier tableau à comparer.
	 * @param tab2    Le 2ième tableau à comparer.
	 * @param epsilon La précision de la comparaison.
	 * @return Un tableau comprenant les éléments identiques de deux tableaux
	 *         (l'intersection des deux tableaux).
	 */
	public static double[] intersectionSortedArray(double[] tab1, double[] tab2, double epsilon) {
		double intersection[] = new double[Math.min(tab1.length, tab2.length)];
		int count = 0;

		int i = 0;
		int j = 0;
		while (i < tab1.length && j < tab2.length) {
			// Vérifier s'il y a égalité.
			if (SMath.nearlyEquals(tab1[i], tab2[j], epsilon)) {
				intersection[count] = tab1[i];
				count++;
				i++;
				j++;
			} else
			// Avancer dans la recherche des éléments.
			if (tab1[i] < tab2[j])
				i++; // avancer dans le tableau 1
			else
				j++; // avancer dans le tableau 2
		}

		// Construire un tableau avec l'espace mémoire minimum.
		double[] result = new double[count];
		for (int k = 0; k < result.length; k++)
			result[k] = intersection[k];

		return result;
	}

}// fin de la classe SMath
