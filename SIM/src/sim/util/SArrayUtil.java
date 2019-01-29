package sim.util;

import java.util.Arrays;

import sim.exception.SRuntimeException;
import sim.math.SAbsoluteDoubleComparator;
import sim.math.SMath;

/**
 * La classe <b>SArrayutil</b> permet d'effectuer des opérations sur des tableaux (Array) de type primitif.
 * 
 * @author Simon Vézina
 * @since 2017-12-15
 * @version 2017-12-15
 */
public final class SArrayUtil {

  /**
   * ...
   * 
   * @param array
   * @return
   */
  public static double strategicArraySum(double[] array)
  {
    // STRATÉGIE DE SOMMATION : Trier en valeur absolue les nombres et en faire l'addition.
    
    // Faire une copie des données dans un tableau de type objet Double.
    Double[] tab = new Double[array.length];
    
    for(int i = 0; i < array.length; i++)
        tab[i] = new Double(array[i]);
    
    // Faire le trie des données avec le comparateur en valeur absolue.
    Arrays.sort(tab, new SAbsoluteDoubleComparator());
    
    // Faire la somme des termes du tableau dans l'ordre stratégique qui devrait minimiser l'imprécision.
    double value = 0.0;
    
    for(Double d : tab)
      value += d.doubleValue();
    
    return value;
    
    
    // STRATÉGIE DE SOMMATION : Trier les + et -, faire l'addition séparée et additionner les deux après.
    
    /*
    
    // Méthode #1 : Additionner les termes petits ensemble
    double[] tab_positive = new double[array.length];
    double[] tab_negative = new double[array.length];
    
    int index_positive = 0;
    int index_negative = 0;
    
    // Trier les valeurs positives et négatives
    for(int i = 0; i < array.length; i++)
    {
      if(array[i] < 0)
      {
        tab_negative[index_negative] = array[i];
        index_negative++;
      }
      else
      {
        tab_positive[index_positive] = array[i];
        index_positive++;
      }
    }
    
    // Ordonner les valeus en ordre croisssant
    Arrays.sort(tab_negative);    // tous les négatifs gros sont au début du tableau
    Arrays.sort(tab_positive);    // tous les zéros non affecté sont au début du tableau
    
    // Faire la somme des nombres négatifs
    double negative_sum = 0.0;
    
    for(int i = index_negative-1; i >= 0; i--)
      negative_sum += tab_negative[i];
      
    // Faire la somme des nombre positifs
    double positive_sum = 0.0;
    
    for(int i = tab_positive.length - index_positive; i < tab_positive.length; i++)
      positive_sum += tab_positive[i];
    
    // Retourner la somme des positifs avec les négatifs
    return positive_sum + negative_sum;
    
    */
    
  }
  
  /**
   * Méthode pour obtenir la <b>plus grande valeur</b> d'un tableau.
   * 
   * @param tab Le tableau.
   * @return La plus grande valeur du tableau.
   */
  public static int findMax(int[] tab)
  {
    int max = tab[0]; 
    
    for(int v : tab)
      if(v > max)
        max = v;
    
    return max;
  }
  
  /**
   * Méthode pour obtenir la <b>plus grande valeur</b> d'un tableau.
   * 
   * @param tab Le tableau.
   * @return La plus grande valeur du tableau.
   */
  public static double findMax(double[] tab)
  {
    double max = tab[0]; 
    
    for(double v : tab)
      if(v > max)
        max = v;
    
    return max;
  }
  
  /**
   * Méthode pour obtenir la <b>plus petite valeur</b> d'un tableau.
   * 
   * @param tab Le tableau.
   * @return La plus petite valeur du tableau.
   */
  public static int findMin(int[] tab)
  {
    int min = tab[0]; 
    
    for(int v : tab)
      if(v < min)
        min = v;
    
    return min;
  }
  
  /**
   * Méthode pour obtenir la <b>plus petite valeur</b> d'un tableau.
   * 
   * @param tab Le tableau.
   * @return La plus petite valeur du tableau.
   */
  public static double findMin(double[] tab)
  {
    double min = tab[0]; 
    
    for(double v : tab)
      if(v < min)
        min = v;
    
    return min;
  }
  
  /**
   * Méthode pour convertir un tableau d'entier vers un tableau de nombre réel entre une valeur minimale et maximale.
   * La correspondance entre les valeurs converties sera linéaire.
   * 
   * @param data Les entiers à convertir.
   * @param min La borne minimale.
   * @param max La borne maximale.
   * @return Le tableau converti.
   * @throws SRuntimeException Si les bornes sont mal définies.
   */
  public static double[] mappingIntToDouble(int[] data, double min, double max) throws SRuntimeException
  {
    if(min > max)
      throw new SRuntimeException("Erreur SMath 003 : La borne minimale " + min + " et la borne maximale " + max + " sont mal définies.");
    
    int min_value = SMath.findMin(data);
    int max_value = SMath.findMax(data);
    
    double[] result = new double[data.length];
    
    // Itérer sur l'ensemble des éléments du tableau.
    for(int i = 0; i < result.length; i++)
    {
      double t = SMath.reverseLinearInterpolation((double)data[i], (double)min_value, (double)max_value);
      result[i] = SMath.linearInterpolation(min, max, t);
    }
    
    return result;
  }
  
  /**
   * Méthode pour convertir un tableau de nombre réel vers un tableau d'entier entre une valeur minimale et maximale.
   * La correspondance entre les valeurs converties sera linéaire.
   * 
   * @param data Les données à convertir.
   * @param min La borne minimale.
   * @param max La borne maximale.
   * @return Le tableau converti.
   * @throws SRuntimeException Si les bornes sont mal définies.
   */
  public static int[] mappingDoubleToInt(double[] data, int min, int max)
  {
    if(min > max)
      throw new SRuntimeException("Erreur SMath 004 : La borne minimale " + min + " et la borne maximale " + max + " sont mal définies.");
    
    double min_value = SMath.findMin(data);
    double max_value = SMath.findMax(data);
    
    int[] result = new int[data.length];
    
    // Itérer sur l'ensemble des éléments du tableau.
    for(int i = 0; i < result.length; i++)
    {
      double t = SMath.reverseLinearInterpolation(data[i], min_value, max_value);
      result[i] = (int)SMath.linearInterpolation(min, max, t);
    }
    
    return result;
  }
  
  /**
   * <p>
   * Méthode permettant de générer un tableau contenant les éléments identique de deux tableaux de valeur <ul>préalablement trié</ul>.
   * </p>
   * 
   * <p>
   * <b>REMARQUE</b> : Le fonctionnement de cette méthode ne sera pas valide si les deux tableaux passés en paramètre ne sont pas péalablement trié.
   * </p>
   * Référence : https://stackoverflow.com/questions/32676381/find-intersection-of-two-arrays
   * 
   * @param tab1 Le 1ier tableau à comparer.
   * @param tab2 Le 2ième tableau à comparer.
   * @return Un tableau comprenant les éléments identiques de deux tableaux (l'intersection des deux tableaux).
   */
  public static double[] intersectionSortedArray(double[] tab1, double[] tab2)
  {
    return intersectionSortedArray(tab1, tab2, SMath.EPSILON);
  }
  
  /**
   * <p>
   * Méthode permettant de générer un tableau contenant les éléments identique de deux tableaux de valeur <ul>préalablement trié</ul>.
   * </p>
   * 
   * <p>
   * <b>REMARQUE</b> : Le fonctionnement de cette méthode ne sera pas valide si les deux tableaux passés en paramètre ne sont pas péalablement trié.
   * </p>
   * Référence : https://stackoverflow.com/questions/32676381/find-intersection-of-two-arrays
   * 
   * @param tab1 Le 1ier tableau à comparer.
   * @param tab2 Le 2ième tableau à comparer.
   * @param epsilon La précision de la comparaison.
   * @return Un tableau comprenant les éléments identiques de deux tableaux (l'intersection des deux tableaux).
   */
  public static double[] intersectionSortedArray(double[] tab1, double[] tab2, double epsilon)
  {
    double intersection[] = new double[Math.min(tab1.length, tab2.length)];
    int count = 0;

    int i = 0; int j = 0;
    while (i < tab1.length && j < tab2.length)
    {
      // Vérifier s'il y a égalité.
      if(SMath.nearlyEquals(tab1[i], tab2[j], epsilon)) 
      {
        intersection[count] = tab1[i];
        count++;
        i++;
        j++;
      }
      else
        // Avancer dans la recherche des éléments.
        if (tab1[i] < tab2[j]) 
          i++;                    // avancer dans le tableau 1
        else
          j++;                    // avancer dans le tableau 2
    }

    // Construire un tableau avec l'espace mémoire minimum.
    double[] result = new double[count];
    for(int k = 0; k < result.length; k++)
      result[k] = intersection[k];
    
    return result;
  }
  
  /**
   * ...
   */
  public static double[] resizeArray(double[] array, int new_size)
  {
      // Remplir un nouveau tableau ayant seulement la taille demandé.
      // Tous les éléments du tableau précédent 
      double[] result = new double[new_size];
      
      for(int i = 0; i < new_size; i++)
        result[i] = array[i];
     
      return result;
  }

}//fin de la classe SArrayUtil
