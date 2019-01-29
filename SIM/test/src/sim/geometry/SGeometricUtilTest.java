/**
 * 
 */
package sim.geometry;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import sim.math.SVector3d;

/**
 * JUnit test de la classe <b>SGeometricUtil</b>.
 * 
 * @author Simon Vézina
 * @since 2015-11-24
 * @version 2017-05-19
 */
public class SGeometricUtilTest {

  @Test
  public void isOneRectanglePerimeterTest() 
  {
    Assert.assertEquals(1, SGeometricUtil.isOnRectanglePerimeter(0, 0, 2.0, 2.0, 1.1, 0.9));
    Assert.assertEquals(1, SGeometricUtil.isOnRectanglePerimeter(0, 0, 2.0, 2.0, 0.9, 1.1));
    Assert.assertEquals(-1, SGeometricUtil.isOnRectanglePerimeter(0, 0, 2.0, 2.0, 0.9, 0.9));
    Assert.assertEquals(-1, SGeometricUtil.isOnRectanglePerimeter(0, 0, 5.0, 4.0, 2.0, 1.5));
    Assert.assertEquals(0, SGeometricUtil.isOnRectanglePerimeter(0, 0, 2.0, 3.0, 1.0, 0.5));
    Assert.assertEquals(0, SGeometricUtil.isOnRectanglePerimeter(0, 0, 2.0, 3.0, 0.5, 1.5));
  }
  
  @Test
  public void isOnCerclePerimeterTest()
  {
    fail("Test non implémenté!");
  }
  
  @Test
  public void isOnEllipsePerimeterTest()
  {
    fail("Test non implémenté!");
  }
  
  @Test
  public void isOnTwoParallelsPlanesSurfaceTest()
  {
    fail("Test non implémenté!");
  }
  
  @Test
  public void isOnSphereSurfaceTest() 
  {
    SVector3d r_s = new SVector3d(0.0, 0.0, 0.0);
    double R = 1.0;
    
    SVector3d outside = new SVector3d(2.0, 2.0, 2.0);
    SVector3d on = new SVector3d(3.4, 5.58, -4.932).normalize();
    SVector3d inside = new SVector3d(-32.5, -4.78, 36.2).normalize().multiply(0.7);
    
    Assert.assertEquals(1, SGeometricUtil.isOnSphereSurface(r_s, R, outside));
    Assert.assertEquals(0, SGeometricUtil.isOnSphereSurface(r_s, R, on));
    Assert.assertEquals(-1, SGeometricUtil.isOnSphereSurface(r_s, R, inside));
  }

  @Test
  public void outsideSphereNormalTest()
  {
    fail("Test non implémenté!");
  }
  
  @Test
  public void isOnInfiniteTubeSurfaceTest()
  {
    fail("Test non implémenté!");
  }
  
  @Test
  public void outsideInfiniteTubeNormalTest()
  {
    fail("Test non implémenté!");
  }
  
  @Test
  public void inOnTwoInfinitesConesSurfaceTest()
  {
    fail("Test non implémenté!");
  }
  
  @Test
  public void isOnCylinderSurfaceTest()
  {
    fail("Test non implémenté!");
  }
  
  @Test
  public void isOnConeSurfaceTest()
  {
    fail("Test non implémenté!");
  }
  
  @Test
  public void isOnTorusSurfaceTest() 
  {
    fail("Test non implémenté.");
  }
  
  @Test
  public void outsideTorusNormalTest() 
  {
    fail("Test non implémenté.");
  }
  
  @Test
  public void isOnAlignedCubeSurfaceTest()
  {
    Assert.assertEquals(-1, SGeometricUtil.isOnAlignedCubeSurface(new SVector3d(3.0, 0.0, 0.0), 2.0, new SVector3d(3.0,0.0,0.0)));
    Assert.assertEquals(-1, SGeometricUtil.isOnAlignedCubeSurface(new SVector3d(3.0, 0.0, 0.0), 2.0, new SVector3d(2.1,0.0,0.0)));
    Assert.assertEquals(0, SGeometricUtil.isOnAlignedCubeSurface(new SVector3d(3.0, 0.0, 0.0), 2.0, new SVector3d(2.0,0.0,0.0)));
    Assert.assertEquals(1, SGeometricUtil.isOnAlignedCubeSurface(new SVector3d(3.0, 0.0, 0.0), 2.0, new SVector3d(0.0,0.0,0.0)));
    Assert.assertEquals(1, SGeometricUtil.isOnAlignedCubeSurface(new SVector3d(3.0, 0.0, 0.0), 2.0, new SVector3d(1.0,0.0,0.0)));
    
    Assert.assertEquals(-1, SGeometricUtil.isOnAlignedCubeSurface(new SVector3d(3.0, 3.0, 3.0), 2.0, new SVector3d(3.9,2.9,3.9)));
    Assert.assertEquals(0, SGeometricUtil.isOnAlignedCubeSurface(new SVector3d(3.0, 3.0, 3.0), 2.0, new SVector3d(4.0,3.5,3.5)));
    Assert.assertEquals(1, SGeometricUtil.isOnAlignedCubeSurface(new SVector3d(3.0, 3.0, 3.0), 2.0, new SVector3d(4.0,4.0,5.0)));
  }
  
}//fin de la classe SGeometricUtil
