/**
 * 
 */
package sim.geometry;

import org.junit.Assert;
import org.junit.Test;

import sim.exception.SNoImplementationException;
import sim.math.SMatrix4x4;
import sim.math.SVector3d;
import sim.util.SLog;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SRay</b>.
 * 
 * @author Simon Vézina
 * @since 2015-08-18
 * @version 2017-08-22
 */
public class SRayTest {

  /**
   * Test de la méthode getPosition avec un temps t = 0.
   */
  @Test
  public void getPositionTest1()
  {
    try{
      
      SVector3d r0 = new SVector3d(7.8, 3.2, -7.4);
      SVector3d v = new SVector3d(-45.3, 3.2, -98.5);
      double t = 0.0;
      
      SRay ray = new SRay(r0, v, 1.0);
      
      Assert.assertEquals(r0, ray.getPosition(t));
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SRayTest ---> Test non effectué : public void getPositionTest1()");
    }
    
  }
  
  /**
   * Test de la méthode getPosition avec un temps t > 0.
   */
  @Test
  public void getPositionTest2()
  {
    try{
      
        SVector3d r0 = new SVector3d(1.0, 2.0, 3.0);
      SVector3d v = new SVector3d(4.0, 5.0, 6.0);
      double t = 3.0;
      
      SRay ray = new SRay(r0, v, 1.0);
      
      SVector3d expected_solution = new SVector3d(13.0, 17.0, 21.0);
      
      Assert.assertEquals(expected_solution, ray.getPosition(t));
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SRayTest ---> Test non effectué : public void getPositionTest2()");
    }
  }
  
  /**
   * Test de la méthode getPosition avec une inversion du rayon.
   */
  @Test
  public void getPositionTest3()
  {
    try{
    
      SVector3d r0i = new SVector3d(45.7, 23.0, -57.0);
      SVector3d vi = new SVector3d(-41.9, 65.9, -38.5);
      double t = 78.4;
      
      SRay ray_i = new SRay(r0i, vi, 1.0);
      
      SRay ray_f = new SRay(ray_i.getPosition(t), vi.multiply(-1.0), 1.0);
      
      Assert.assertEquals(r0i, ray_f.getPosition(t));
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SRayTest ---> Test non effectué : public void getPositionTest3()");
    }
  }
  
  /**
   * Test de la méthode transformNotIntersectedRay avec une transformation de translation.
   */
  @Test
  public void transformNotIntersectedRayTest1()
  {
    try{
    
      SVector3d r0 = new SVector3d(5.0, 5.0, 5.0);
      SVector3d v = new SVector3d(3.0, 4.0, 5.0);
      SMatrix4x4 m = SMatrix4x4.translation(new SVector3d(-3.0, 2.0, 1.0));
      
      SRay ray = new SRay(r0, v, 1.0);
          
      SRay calculated_solution = ray.transformNotIntersectedRay(m);
          
      SRay expected_solution = new SRay(new SVector3d(2.0, 7.0, 6.0), v, 1.0);
      
      Assert.assertEquals(expected_solution, calculated_solution);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SRayTest ---> Test non effectué : public void transformNotIntersectedRayTest1()");
    }
  }
  
  /**
   * Test de la méthode transformNotIntersectedRay avec une transformation de rotation autour de l'axe z.
   */
  @Test
  public void transformNotIntersectedRayTest2()
  {
    try{
    
      SVector3d r0 = new SVector3d(0.0, 5.0, 5.0);
      SVector3d v = new SVector3d(3.0, 5.0, 5.0);
      SMatrix4x4 m = SMatrix4x4.rotationZ(90.0);
      
      SRay ray = new SRay(r0, v, 1.0);
          
      SRay calculated_solution = ray.transformNotIntersectedRay(m);
          
      SRay expected_solution = new SRay(new SVector3d(-5.0, 0.0, 5.0), new SVector3d(-5.0, 3.0, 5.0), 1.0);
      
      Assert.assertEquals(expected_solution, calculated_solution);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SRayTest ---> Test non effectué : public void transformNotIntersectedRayTest2()");
    }
  }
    
  /**
   * Test de la méthode compareTo.
   */
  @Test
  public void compareToTest()
  {
    java.util.List<SRay> list = new java.util.LinkedList<SRay>();
    java.util.List<SRay> sort_list = new java.util.LinkedList<SRay>();
    
    SVector3d origin = new SVector3d(1.0, 0.0, 0.0);
    SVector3d direction = new SVector3d(1.0, 0.0, 0.0);
    
    SRay initial_ray = new SRay(origin, direction, SRay.DEFAULT_REFRACTIVE_INDEX);
    
    // Faire une géométrie par défaut.
    SGeometry geo = new SCubeGeometry();
    
    // Faire une normale par défaut.
    SVector3d nor = new SVector3d(0.0, 0.0, 1.0);
    
    SRay ray1 = initial_ray.intersection(geo, nor, 1.0);
    SRay ray2 = initial_ray.intersection(geo, nor, 2.0);
    SRay ray3 = initial_ray.intersection(geo, nor, 3.0);
    SRay ray4 = initial_ray.intersection(geo, nor, 4.0);
    SRay ray5 = initial_ray.intersection(geo, nor, 5.0);
        
    //Dans l'ordre
    sort_list.add(ray1);
    sort_list.add(ray2);
    sort_list.add(ray3);
    sort_list.add(ray4);
    sort_list.add(ray5);
    sort_list.add(initial_ray);
    
    //Dans le désordre (avant le tirage par compareTo)
    list.add(ray2);
    list.add(ray4);
    list.add(ray5);
    list.add(initial_ray);
    list.add(ray1);
    list.add(ray3);
    
    //Faire le triage
    java.util.Collections.sort(list);
    
    //Comparer les listes
    Assert.assertEquals(sort_list, list);
  }
  
}//fin de la classe SRayTest
