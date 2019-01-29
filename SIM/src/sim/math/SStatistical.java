/**
 * 
 */
package sim.math;

/**
 * L'interface <b>SStatistical</b> repr�sente un objet pouvant �tre analys� dans un calcul statistique.
 * 
 * @author Simon V�zina
 * @since 2016-02-03
 * @version 2016-02-03
 */
public interface SStatistical {

  /**
   * M�thode pour obtenir une valeur de type <b>double</b> de l'objet pouvant �tre analys�e dans un calcul de statistique.
   * 
   * @return La valeur � analyser statistiquement.
   */
  public double getStatisticalValue();
  
}//fin de l'interface SStatistical
