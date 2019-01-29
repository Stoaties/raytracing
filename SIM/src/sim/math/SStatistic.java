/**
 * 
 */
package sim.math;

import java.util.Collection;

import sim.exception.SRuntimeException;

/**
 * <p>
 * La classe <b>SStatistic</b> permet d'effectuer des calculs statistiques sur des objets impl�mentant l'interface <b>SStatistical</b>.
 * </p>
 * <p>
 * Les calculs statistiques support�s par cette classe sont les suivants :
 * <ul>- La moyenne.</ul>
 * <ul>- L'�cart type (sigma).</ul> 
 * </p>
 * 
 * @author Simon V�zina
 * @since 2016-02-03
 * @version 2016-03-22
 */
public class SStatistic {

  /**
   * M�thode pour effecter un calcul de la <b>moyenne</b> d'une collection de nombres.
   *  
   * @param collection - La collection de nombres.
   * @return La moyenne des nombres.
   * @throws SRuntimeException Si la collection de nombres est vide.
   */
  public static double average(Collection<? extends SStatistical> collection) throws SRuntimeException
  {
    /*
    // Version traditionnelle : 
    // V�rifier que la collection n'est pas vide
    if(collection.isEmpty())
      throw new SRuntimeException("Erreur SStatistic 001 : La collection utilis�e pour calculer une moyenne est vide.");
    
    double value = 0.0;
    
    // Effectuer la somme des valeurs
    for(SStatistical s : collection)
      value += s.getStatisticalValue();
    
    // Retourner la somme des valeurs divis� par le nombre de valeur somm�e
    return value / collection.size();
    */
    
    // Version Stream :
    return collection.stream() //
        .mapToDouble(SStatistical::getStatisticalValue) //
        .average() //
        .orElseThrow(() -> new SRuntimeException("Erreur SStatistic 001 : La collection utilis�e pour calculer une moyenne est vide."));
  }

  /**
   * M�thode pour effectuer un calcul <b>d'�cart-type</b> (<i>standard deviation</i>) d'une collection de nombres. 
   * Dans la litt�rature math�matique, ce calcul porte �galement le nom de <b>sigma</b>.
   *  
   * @param collection - La collection de nombre
   * @return L'�cart-type des nombres
   * @throws SRuntimeException Si la collection de nombres est inf�rieur � deux nombres.
   */
  public static double standardDeviation(Collection<? extends SStatistical> collection) throws SRuntimeException
  {
    // V�rifier que la collection n'est pas vide
    if(collection.size() < 2)
      throw new SRuntimeException("Erreur SStatistic 002 : La collection utilis�e pour calculer un �cart-type poss�de " + collection.size() + " nombre ce qui est inf�rieur � 2.");
    
    // Obtenir la moyenne de la collection
    double average_value = average(collection);
    
    double sum = 0.0;
    
    // Effectuer la somme au carr� des diff�rences entre la valeur et la moyenne 
    for(SStatistical s : collection)
      sum += Math.pow(s.getStatisticalValue() - average_value, 2);
    
    // Retourner la racine de la somme pr�c�dente divis� par le nombre de valeur moins 1
    return Math.sqrt(sum / (collection.size() - 1));
  }
  
}//fin de la classe SStatistic
