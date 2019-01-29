/**
 * 
 */
package sim.graphics.light;

import sim.math.SVector3d;

/**
 * L'interface <b>SInterferenceLight</b> repr�sente une source de lumi�re dont l'intensit� de la source
 * d�pend de l'interf�rence interne de la source.
 *  
 * @author Simon V�zina
 * @since 2016-02-15
 * @version 2016-02-28
 */
public interface SInterferenceLight extends SAttenuatedLight {

  /**
   * <p>
   * M�thode permettant d'�valuer l'intensit� relative d'une source de lumi�re monochromatique en incluant un facteur d'interf�rence
   * entre les diff�rents oscillateurs composant la source de lumi�re. 
   * </p>
   * 
   * <p>
   * Les interpr�tations des intensit�s sont les suivantes :
   * <ul> I = 1     : Intensit� avec <b>interf�rence constructive</b>.</ul>
   * <ul> I = 0.5   : Intensit� <b>sans interf�rence</b> (intensit� totale des oscillateurs sans interf�rence).</ul>
   * <ul> I = 0     : Intensit� avec <b>interf�rence destructive</b>.</ul>
   * <ul> I = autre : Intensit� avec <b>interf�rence partielle</b>.</ul>
   *  </p>
   *  
   * @param position_to_illuminate La position o� l'intensit� sera �valu�e.
   * @return L'intensit� relative de la source de lumi�re monochromatique.
   */
  public double getRelativeIntensity(SVector3d position_to_illuminate);
  
}//fin de l'interface SInterferenceLight
