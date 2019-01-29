/**
 * 
 */
package sim.geometry;

import org.junit.Assert;
import org.junit.Test;

import sim.math.SVector3d;
import sim.math.SVectorUV;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SBTriangleGeometry</b>.
 * 
 * @author Simon Vezina
 * @since 2016-05-13
 * @version 2018-03-08
 */
public class SBTriangleGeometryTest {

  /**
   * Test de l'intersection <u>avec succès</u> entre un rayon aligné selon l'axe z et un triangle dans le plan xy.
   * Dans ce test, la normale à la surface n'est pas interpolée et il n'y a pas de coordonnée uv.
   */
  @Test
  public void intersectionTest1()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 1.0), new SVector3d(0.0, 0.0, -1.0), 1.0);
    
    SVector3d P0 = new SVector3d(1.0, 1.0, 0.0);
    SVector3d P1 = new SVector3d(1.0, -1.0, 0.0);
    SVector3d P2 = new SVector3d(-1.0, 0.0, 0.0);
    
    SVector3d N0 = new SVector3d(0.0, 0.0, 1.0);
    SVector3d N1 = new SVector3d(0.0, 0.0, 1.0);
    SVector3d N2 = new SVector3d(0.0, 0.0, 1.0);
    
    SBTriangleGeometry geometry = new SBTriangleGeometry(P0, P1, P2, N0, N1, N2);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    SRay expected_solution = ray.intersection(geometry, new SVector3d(0.0, 0.0, 1.0), new SVectorUV(0.0, 0.0), 1.0);
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }

  /**
   * Test de l'intersection <u>avec succès</u> entre un rayon aligné selon l'axe z et un triangle dans le plan xy.
   * Dans ce test, il y aura interpolation de la normale et il n'y a pas de coordonnée uv.
   */
  @Test
  public void intersectionTest2()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 1.0), new SVector3d(0.0, 0.0, -1.0), 1.0);
    
    SVector3d P0 = new SVector3d(1.0, 1.0, 0.0);
    SVector3d P1 = new SVector3d(1.0, -1.0, 0.0);
    SVector3d P2 = new SVector3d(-1.0, 0.0, 0.0);
    
    SVector3d N0 = new SVector3d(0.0, 0.0, 1.0).normalize();
    SVector3d N1 = new SVector3d(1.0, 1.0, 1.0).normalize();
    SVector3d N2 = new SVector3d(0.0, 1.0, 1.0).normalize();
    
    SVectorUV default_interpolated_uv = new SVectorUV(0.0, 0.0);
    
    SBTriangleGeometry geometry = new SBTriangleGeometry(P0, P1, P2, N0, N1, N2);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    SVector3d interpolated_normal = new SVector3d(0.14433756729740646, 0.4978909578906803, 0.7478909578906803);
    
    SRay expected_solution = ray.intersection(geometry, interpolated_normal, default_interpolated_uv, 1.0);
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>avec succès</u> entre un rayon aligné selon l'axe z et un triangle dans le plan xy.
   * Dans ce test, il y aura interpolation de la normale et de la coordonnée de texture uv.
   */
  @Test
  public void intersectionTest3()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 1.0), new SVector3d(0.0, 0.0, -1.0), 1.0);
    
    SVector3d P0 = new SVector3d(1.0, 1.0, 0.0);
    SVector3d P1 = new SVector3d(1.0, -1.0, 0.0);
    SVector3d P2 = new SVector3d(-1.0, 0.0, 0.0);
    
    SVector3d N0 = new SVector3d(0.0, 0.0, 1.0).normalize();
    SVector3d N1 = new SVector3d(1.0, 1.0, 1.0).normalize();
    SVector3d N2 = new SVector3d(0.0, 1.0, 1.0).normalize();
    
    SVectorUV UV0 = new SVectorUV(0.0, 0.0);
    SVectorUV UV1 = new SVectorUV(1.0, 1.0);
    SVectorUV UV2 = new SVectorUV(0.0, 1.0);
    
    SBTriangleGeometry geometry = new SBTriangleGeometry(P0, P1, P2, N0, N1, N2, UV0, UV1, UV2);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    SVector3d interpolated_normal = new SVector3d(0.14433756729740646, 0.4978909578906803, 0.7478909578906803);
    SVectorUV interpolated_uv = new SVectorUV(0.25, 0.75);
    
    SRay expected_solution = ray.intersection(geometry, interpolated_normal, interpolated_uv, 1.0);
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>sans succès</u> entre un rayon et un triangle dans le plan xy.
   */
  @Test
  public void intersectionTest4()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 1.0), new SVector3d(-1.0, -1.0, -1.0), 1.0);
    
    SVector3d P0 = new SVector3d(1.0, 1.0, 0.0);
    SVector3d P1 = new SVector3d(1.0, -1.0, 0.0);
    SVector3d P2 = new SVector3d(-1.0, 0.0, 0.0);
    
    SVector3d N0 = new SVector3d(0.0, 0.0, 1.0);
    SVector3d N1 = new SVector3d(0.0, 0.0, 1.0);
    SVector3d N2 = new SVector3d(0.0, 0.0, 1.0);
    
    SBTriangleGeometry geometry = new SBTriangleGeometry(P0, P1, P2, N0, N1, N2);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    SRay expected_solution = ray; // sans intersection
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }

  /**
   * Test de l'intersection <u>avec succès</u> entre un rayon et un triangle quelconque.
   * Dans ce test, on n'effectue pas d'interpolation sur la normale et les coordonnées UV.
   */
  @Test
  public void intersectionTest5()
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
    
    SBTriangleGeometry geometry = new SBTriangleGeometry(P0, P1, P2);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    // Ces valeurs de normale et UV ne sont pas le fruit d'une interpolation, mais les valeurs par défaut attribuable au triangle dans le test.
    SVector3d interpolated_normal = new SVector3d(-0.45106718384945754, -0.8775030829421608, -0.16287030140919792);
    SVectorUV interpolated_uv = new SVectorUV(0.0, 0.0);
    
    SRay expected_solution = ray.intersection(geometry, interpolated_normal, interpolated_uv, t);
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>avec succès</u> entre un rayon et un triangle quelconque.
   * Dans ce test, on vérifie que la normale et les coordonnées UV sont bien interpolées.
   */
  @Test
  public void intersectionTest6()
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
  	
  	SVector3d N0 = new SVector3d(0.0, 0.0, 1.0).normalize();
    SVector3d N1 = new SVector3d(1.0, 1.0, 1.0).normalize();
    SVector3d N2 = new SVector3d(0.0, 1.0, 1.0).normalize();
    
    SVectorUV UV0 = new SVectorUV(0.0, 0.0);
    SVectorUV UV1 = new SVectorUV(1.0, 1.0);
    SVectorUV UV2 = new SVectorUV(0.0, 1.0);
	
    SRay ray = new SRay(r_int.add(ray_direction.multiply(-t)), ray_direction, 1.0);
    
    SBTriangleGeometry geometry = new SBTriangleGeometry(P0, P1, P2, N0, N1, N2, UV0, UV1, UV2);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    // Valeur de la normale et UV après interpolation.
    SVector3d interpolated_normal = new SVector3d(0.10894657270257797, 0.3725490237982545, 0.8110578631525966);
    SVectorUV interpolated_uv = new SVectorUV(0.18870099923136155, 0.5614911606456578);
    
    SRay expected_solution = ray.intersection(geometry, interpolated_normal, interpolated_uv, t);
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
}//fin de la classe SBTriangleGeometryTest
