/**
 * 
 */
package sim.graphics.light;

import sim.math.SVector3d;

/**
 * L'interface <b>SInterferenceLight</b> représente une source de lumière dont l'intensité de la source
 * dépend de l'interférence interne de la source.
 *  
 * @author Simon Vézina
 * @since 2016-02-15
 * @version 2016-02-28
 */
public interface SInterferenceLight extends SAttenuatedLight {

  /**
   * <p>
   * Méthode permettant d'évaluer l'intensité relative d'une source de lumière monochromatique en incluant un facteur d'interférence
   * entre les différents oscillateurs composant la source de lumière. 
   * </p>
   * 
   * <p>
   * Les interprétations des intensités sont les suivantes :
   * <ul> I = 1     : Intensité avec <b>interférence constructive</b>.</ul>
   * <ul> I = 0.5   : Intensité <b>sans interférence</b> (intensité totale des oscillateurs sans interférence).</ul>
   * <ul> I = 0     : Intensité avec <b>interférence destructive</b>.</ul>
   * <ul> I = autre : Intensité avec <b>interférence partielle</b>.</ul>
   *  </p>
   *  
   * @param position_to_illuminate La position où l'intensité sera évaluée.
   * @return L'intensité relative de la source de lumière monochromatique.
   */
  public double getRelativeIntensity(SVector3d position_to_illuminate);
  
}//fin de l'interface SInterferenceLight
