/**
 * 
 */
package sim.math;

import org.junit.Assert;
import org.junit.Test;

import sim.exception.SNoImplementationException;
import sim.util.SLog;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SVector</b>.
 *  
 * @author Simon Vézina
 * @since 2016-05-13
 * @version 2017-02-25
 */
public class SVectorTest {

  /**
   * Test de la méthode linearBarycentricInterpolation pour le cas trivial où t = { 0, 1 }. 
   */
  @Test
  public void test_linearBarycentricInterpolation_1() 
  {
    try{
      
    SVector3d v0 = new SVector3d(4.5, 7.6, -3.4);
    SVector3d v1 = new SVector3d(2.3, -4.6, 2.9);
    SVector3d v2 = new SVector3d(-7.7, -12.4, 5.8);
    
    Assert.assertEquals(v0, (SVector3d)SVector.linearBarycentricInterpolation(v0, v1, v2, 0.0, 0.0));
    Assert.assertEquals(v1, (SVector3d)SVector.linearBarycentricInterpolation(v0, v1, v2, 1.0, 0.0));
    Assert.assertEquals(v2, (SVector3d)SVector.linearBarycentricInterpolation(v0, v1, v2, 0.0, 1.0));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVectorTest ---> Test non effectué : public void test_linearBarycentricInterpolation_1()");
    }
  }

  /**
   * Test de la méthode linearBarycentricInterpolation pour le cas quelconque. 
   */
  @Test
  public void test_linearBarycentricInterpolation_2() 
  {
    try{
      
    SVector3d v0 = new SVector3d(4.5, 7.6, -3.4);
    SVector3d v1 = new SVector3d(2.3, -4.6, 2.9);
    SVector3d v2 = new SVector3d(-7.7, -12.4, 5.8);
    
    Assert.assertEquals(new SVector3d(-0.82, -2.84, 1.54), SVector.linearBarycentricInterpolation(v0, v1, v2, 0.2, 0.4));
    Assert.assertEquals(new SVector3d(0.52, -4.94, 2.85), SVector.linearBarycentricInterpolation(v0, v1, v2, 0.7, 0.2));
        
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVectorTest ---> Test non effectué : public void test_linearBarycentricInterpolation_2()");
    }
  }
  
}//fin de la classe SVectorTest
