/**
 * 
 */
package sim.math;

import java.util.Comparator;

/**
 * ...
 * 
 * @author Simon Vézina
 * @since 2017-12-08
 * @version 2017-12-08
 */
public class SAbsoluteDoubleComparator implements Comparator<Double> {

  @Override
  public int compare(Double a, Double b)
  {
    return Double.compare(Math.abs(a.doubleValue()), Math.abs(b.doubleValue()));
  }

}// fin de la classe SAbsoluteDoubleComparator
