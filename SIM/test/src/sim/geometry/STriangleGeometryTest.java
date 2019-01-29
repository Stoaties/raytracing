/**
 * 
 */
package sim.geometry;

import org.junit.Assert;
import org.junit.Test;

import sim.math.SVector3d;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>STriangleGeometry</b>.
 * 
 * @author Simon Vezina
 * @since 2016-05-13
 * @version 2018-03-08
 */
public class STriangleGeometryTest {

  /**
   * Test de l'intersection <u>avec succès</u> entre un rayon aligné selon l'axe z et un triangle dans le plan xy.
   */
  @Test
  public void intersectionTest1()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 1.0), new SVector3d(0.0, 0.0, -1.0), 1.0);
    
    STriangleGeometry geometry = new STriangleGeometry(new SVector3d(1.0, 1.0, 0.0), new SVector3d(1.0, -1.0, 0.0), new SVector3d(-1.0, 0.0, 0.0));
    
    SRay calculated_solution = geometry.intersection(ray);
    
    // Orientation de la normale attendue en lien avec l'ordre des points du triangle.
    SVector3d expected_normal = new SVector3d(0.0, 0.0, -1.0);
    
    SRay expected_solution = ray.intersection(geometry, expected_normal, 1.0);
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }

  /**
   * Test de l'intersection <u>sans succès</u> entre un rayon et un triangle dans le plan xy.
   */
  @Test
  public void intersectionTest2()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 1.0), new SVector3d(-1.0, -1.0, -1.0), 1.0);
    
    STriangleGeometry geometry = new STriangleGeometry(new SVector3d(1.0, 1.0, 0.0), new SVector3d(1.0, -1.0, 0.0), new SVector3d(-1.0, 0.0, 0.0));
    
    SRay calculated_solution = geometry.intersection(ray);
    
    SRay expected_solution = ray;   // sans intersection
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>sans succès</u> entre un rayon et un triangle quelconque.
   */
  @Test
  public void intersectionTest3()
  {
    // Position de l'intersection.
    SVector3d r_int = new SVector3d(3.4, 6.7, 3.5);
    
    // Temps de l'intersection.
    double t = 5.9;
    
    // Orientation du rayon.
    SVector3d ray_direction = new SVector3d(-7.5, 3.8, -4.2);
      
    // Base du triangle.
    SVector3d b1 = new SVector3d(-7.8, 3.1, 4.9);
    SVector3d b2 = new SVector3d(1.7, -2.6, 9.3);
    
    // Points du triangle.
    SVector3d P0 = r_int.add(b1.multiply(2.5)).add(b2.multiply(1.8));
    SVector3d P1 = r_int.add(b1.multiply(-1.5)).add(b2.multiply(2.8));
    SVector3d P2 = r_int.add(b1.multiply(3.7)).add(b2.multiply(0.7));
    
    SRay ray = new SRay(r_int.add(ray_direction.multiply(-t)), ray_direction, 1.0);
    
    STriangleGeometry geometry = new STriangleGeometry(P0, P1, P2);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    // Il n'y a pas d'intersection.
    SRay expected_solution = ray;
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>avec succès</u> entre un rayon et un triangle quelconque.
   */
  @Test
  public void intersectionTest4()
  {
    // Position de l'intersection.
  	SVector3d r_int = new SVector3d(3.4, 6.7, 3.5);
  	
  	// Temps de l'intersection.
  	double t = 5.9;
  	
  	// Orientation du rayon.
  	SVector3d ray_direction = new SVector3d(-7.5, 3.8, -4.2);
  		
  	// Base du triangle.
  	SVector3d b1 = new SVector3d(-7.8, 3.1, 4.9);
  	SVector3d b2 = new SVector3d(1.7, -2.6, 9.3);
  	
  	// Points du triangle.
  	SVector3d P0 = r_int.add(b1.multiply(-2.5)).add(b2.multiply(-1.8));
  	SVector3d P1 = r_int.add(b1.multiply(-1.5)).add(b2.multiply(2.8));
  	SVector3d P2 = r_int.add(b1.multiply(3.7)).add(b2.multiply(0.7));
  	
  	SRay ray = new SRay(r_int.add(ray_direction.multiply(-t)), ray_direction, 1.0);
    
    STriangleGeometry geometry = new STriangleGeometry(P0, P1, P2);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    // JE SUIS À LA RECHERCHE DE LA NORMALE.
    SRay expected_solution = ray.intersection(geometry, new SVector3d(-0.45106718384945743, -0.8775030829421607, -0.1628703014091979), t);
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
}//fin de la classe STriangleGeometry
