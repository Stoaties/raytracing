/**
 * 
 */
package sim.math;

/**
 * L'interface <b>SStatistical</b> représente un objet pouvant être analysé dans un calcul statistique.
 * 
 * @author Simon Vézina
 * @since 2016-02-03
 * @version 2016-02-03
 */
public interface SStatistical {

  /**
   * Méthode pour obtenir une valeur de type <b>double</b> de l'objet pouvant être analysée dans un calcul de statistique.
   * 
   * @return La valeur à analyser statistiquement.
   */
  public double getStatisticalValue();
  
}//fin de l'interface SStatistical
