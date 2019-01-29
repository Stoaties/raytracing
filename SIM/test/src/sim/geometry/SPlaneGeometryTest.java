/**
 * 
 */
package sim.geometry;

import org.junit.Assert;
import org.junit.Test;
import sim.math.SVector3d;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SPlaneGeometryTest</b>.
 * 
 * @author Simon Vézina
 * @since 2016-04-19
 * @version 2018-01-30
 */
public class SPlaneGeometryTest {

  /**
   * Test de l'intersection entre un rayon à voyageant parallèlement au plan en étant au-dessus de celui-ci. Il n'y a pas d'intersection.
   */
  @Test
  public void intersectionTest1a()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 1.0), new SVector3d(1.0, 0.0, 0.0), 1.0);
      
    SPlaneGeometry geometry = new SPlaneGeometry(new SVector3d(0.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0));
      
    SRay calculated_solution = geometry.intersection(ray);
      
    SRay expected_solution = ray; // sans intersection
      
    Assert.assertEquals(expected_solution, calculated_solution);
  }
 
 /**
   * Test de l'intersection entre un rayon à voyageant parallèlement au plan et étant situé dans le plan. Il n'y a une infinité de solutions.
   */
  /*
  @Test
  public void intersectionTest1b()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 0.0), new SVector3d(1.0, 0.0, 0.0), 1.0);
      
    SPlaneGeometry geometry = new SPlaneGeometry(new SVector3d(0.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0));
      
	  try{
	    
    SRay calculated_solution = geometry.intersection(ray);
    fail("Ce test devrait donner une infinité de solutions. Présentement, votre solution de temps est t = " + calculated_solution.getT());
    
    }catch(SInfinityOfSolutionsException e){
		  // c'est un succès
	  }  
  }
  */
  
  /**
   * Test de l'intersection entre un rayon et un plan où le rayon est initialement sur le plan. Il n'y a pas d'intersection.
   */
  @Test
  public void intersectionTest2()
  {
    // Un point du plan
    SVector3d r_p = new SVector3d(-1.0, 3.0, -7.0);
    
    // La base du plan
    SVector3d u1 = new SVector3d(7.0, 3.0, 2.0);
    SVector3d u2 = new SVector3d(-4.0, 2.0, -1.0);
    
    // La normale à la surface du plan
    SVector3d n_p = u1.cross(u2).normalize();
    
    // Origine du rayon (sur le plan)
    SVector3d r0 = r_p.add(u1.multiply(3.0)).add(u2.multiply(6.0));
    
    // Orientation du rayon (quelconque)
    SVector3d v = new SVector3d(8.0, -9.0, 2.0);
    
    // Le calcul à tester
    SRay ray = new SRay(r0, v, 1.0);
    
    SPlaneGeometry geometry = new SPlaneGeometry(r_p, n_p);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    SRay expected_solution = ray; // sans intersection
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection entre un rayon et un plan où le rayon où le temps de l'intersection est positif
   */
  @Test
  public void intersectionTest3()
  {
    // Un point du plan
    SVector3d r_p = new SVector3d(2.0, 2.0, 5.0);
    
    // La normale à la surface du plan
    SVector3d n_p = new SVector3d(1.0, 1.0, 1.0).normalize();
    
    // Origine du rayon 
    SVector3d r0 = new SVector3d(4.0, 4.0, 10.0);
    
    // Orientation du rayon 
    SVector3d v = new SVector3d(-1.0, 0.0, -2.0);
    
    // Le calcul à tester
    SRay ray = new SRay(r0, v, 1.0);
    
    SPlaneGeometry geometry = new SPlaneGeometry(r_p, n_p);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    SRay expected_solution = ray.intersection(geometry, n_p, 3.0);
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection entre un rayon et un plan où le rayon où le temps de l'intersection est négatif. Il n'y a donc pas d'intersection.
   */
  @Test
  public void intersectionTest4()
  {
    // Un point du plan
    SVector3d r_p = new SVector3d(2.0, 2.0, 5.0);
    
    // La normale à la surface du plan
    SVector3d n_p = new SVector3d(1.0, 1.0, 1.0).normalize();
    
    // Origine du rayon 
    SVector3d r0 = new SVector3d(0.0, 2.0, -15.0);
    
    // Orientation du rayon 
    SVector3d v = new SVector3d(-1.0, 0.0, -2.0);
    
    // Le calcul à tester
    SRay ray = new SRay(r0, v, 1.0);
    
    SPlaneGeometry geometry = new SPlaneGeometry(r_p, n_p);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    SRay expected_solution = ray;   // pas d'intersection
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
}//fin de la classe SPlaneGeometryTest
