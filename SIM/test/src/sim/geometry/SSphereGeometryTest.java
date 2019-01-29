/**
 * 
 */
package sim.geometry;

import org.junit.Assert;
import org.junit.Test;



import sim.math.SVector3d;
import sim.math.SVectorUV;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SSphereGeometry</b>.
 * 
 * @author Simon Vézina
 * @since 2015-10-19
 * @version 2017-08-17
 */
public class SSphereGeometryTest {

  /**
   * Test de l'intersection entre un rayon à l'origine et une sphère à l'origine.
   */
  @Test
  public void intersectionTest1()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 0.0), new SVector3d(1.0, 0.0, 0.0), 1.0);
      
    SSphereGeometry geometry = new SSphereGeometry(new SVector3d(0.0, 0.0, 0.0), 1.0);
      
    SRay calculated_solution = geometry.intersection(ray);
    
    // Première validation : Le rayon doit avoir réalisé une intersection.
    Assert.assertTrue(calculated_solution.asIntersected());
    
    // Construction de la solution attendue (dépendra si le rayon supporte ou non les coordonnées de texture.
    SRay expected_solution;
    SVector3d expected_normal = new SVector3d(1.0, 0.0, 0.0);
    
    if(!calculated_solution.asUV())
      expected_solution = ray.intersection(geometry, expected_normal, 1.0);
    else
      expected_solution = ray.intersection(geometry, expected_normal, new SVectorUV(0.5, 0.5), 1.0);
      
    // Deuxième validation : Est-ce que le rayon intersecté est dans le bon état.
    Assert.assertEquals(expected_solution, calculated_solution);
  }

  /**
   * Test de l'intersection <u>sans succès</u> entre un rayon et une sphère.
   */
  @Test
  public void intersectionTest2()
  {
    SRay ray = new SRay(new SVector3d(2.0, 3.0, 4.0), new SVector3d(-1.0, -7.0, 1.0), 1.0);
      
    SSphereGeometry geometry = new SSphereGeometry(new SVector3d(7.0, 2.0, 3.0), 3.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    SRay expected_solution = ray; // sans intersection
      
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>avec 2 succès positifs</u> entre un rayon et une sphère dans un cas quelconque.
   */
  @Test
  public void intersectionTest3()
  {
    SRay ray = new SRay(new SVector3d(-2.0, 3.0, 7.0), new SVector3d(-2.0, 4.0, 3.0), 1.0);
      
    SSphereGeometry geometry = new SSphereGeometry(new SVector3d(-5.0, 8.0, 12.0), 3.0);
    
    SRay calculated_solution = geometry.intersection(ray);
      
    // Première validation : Le rayon doit avoir réalisé une intersection.
    Assert.assertTrue(calculated_solution.asIntersected());
    
    // Construction de la solution attendue (dépendra si le rayon supporte ou non les coordonnées de texture.
    SRay expected_solution;
    SVector3d expected_normal = new SVector3d(0.406866302380934740, -0.48039927142853617, -0.7769661202380688);
    
    if(!calculated_solution.asUV())
      expected_solution = ray.intersection(geometry, expected_normal, 0.8897005464285977);
    else
      expected_solution = ray.intersection(geometry, expected_normal, new SVectorUV(0.3267757370472301, 0.6595082350297791), 0.8897005464285977);
      
    // Deuxième validation : Est-ce que le rayon intersecté est dans le bon état.
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>avec 1 succès négatif et 1 succès positif </u> entre un rayon et une sphère dans un cas quelconque.
   */
  @Test
  public void intersectionTest4()
  {
    SRay ray = new SRay(new SVector3d(-2.0, 3.0, 7.0), new SVector3d(-2.0, 4.0, 3.0), 1.0);
      
    SSphereGeometry geometry = new SSphereGeometry(new SVector3d(2.0, 8.0, 12.0), 9.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    // Première validation : Le rayon doit avoir réalisé une intersection.
    Assert.assertTrue(calculated_solution.asIntersected());
    
    // Construction de la solution attendue (dépendra si le rayon supporte ou non les coordonnées de texture.
    SRay expected_solution; 
    SVector3d expected_normal = new SVector3d(-0.9127773503330572, 0.3811102562216699, 0.1469438032773635);
    
    if(!calculated_solution.asUV())
      expected_solution = ray.intersection(geometry, expected_normal, 2.107498076498757);
    else
      expected_solution = ray.intersection(geometry, expected_normal, new SVectorUV(0.9745963417181047, 0.37554182528176416), 2.107498076498757);
    
    // Deuxième validation : Est-ce que le rayon intersecté est dans le bon état.
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>avec 2 succès négatif</u> entre un rayon et une sphère dans un cas quelconque.
   */
  @Test
  public void intersectionTest5()
  {
    SRay ray = new SRay(new SVector3d(1.0, 1.0, 1.0), new SVector3d(1.0, 2.0, 3.0), 1.0);
      
    SSphereGeometry geometry = new SSphereGeometry(new SVector3d(-1.0, -1.0, -1.0), 3.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    SRay expected_solution = ray; // sans intersection
      
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>avec 1 succès</u> entre un rayon et une sphère tel que le rayon <u>par de la surface de la sphère vers l'intérieur de la sphère</u>.
   */
  @Test
  public void intersectionTest6()
  {
    SVector3d center = new SVector3d(2.0, 3.0, 4.0);
    double R = 3.0;
      
    SRay ray = new SRay(center.add( new SVector3d(3.2, 6.7, 2.3).normalize().multiply(R)), new SVector3d(-1.0, -2.0, -3.0), 1.0);
      
    SSphereGeometry geometry = new SSphereGeometry(center, R);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    // Première validation : Le rayon doit avoir réalisé une intersection.
    Assert.assertTrue(calculated_solution.asIntersected());
    
    // Construction de la solution attendue (dépendra si le rayon supporte ou non les coordonnées de texture.
    SRay expected_solution; 
    SVector3d expected_normal = new SVector3d(-0.020216421541674617, -0.0018378565037885746, -0.999793938060999);
    
    if(!calculated_solution.asUV())
      expected_solution = ray.intersection(geometry, expected_normal, 1.2956888351709637);
    else
      expected_solution = ray.intersection(geometry, expected_normal, new SVectorUV(0.246782231934278, 0.5005850082238754), 1.2956888351709637);
      
    // Deuxième validation : Est-ce que le rayon intersecté est dans le bon état.
    Assert.assertEquals(expected_solution, calculated_solution);
  }
    
}//fin de la classe SSphereGeometryTest
