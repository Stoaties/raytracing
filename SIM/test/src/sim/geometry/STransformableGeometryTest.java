/**
 * 
 */
package sim.geometry;

import org.junit.Assert;
import org.junit.Test;

import sim.math.SVector3d;


/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>STransformableGeometry</b>.
 * 
 * @author Simon Vézina
 * @since 2016-04-19
 * @version 2018-03-08
 */
public class STransformableGeometryTest {

  /**
   * Test de la méthode isInside(SVector3d v) avec l'usage de la géométrie <b>SCubeGeometry</b> dans le cas où il n'y a pas de transformation.
   * Plusieurs tests seront réalisés où le point testé sera à l'intérieur et à l'extérieur de la géométrie transformée.
   */
  @Test
  public void isInsideTest1()
  {
    SCubeGeometry geometry = new SCubeGeometry(new SVector3d(0.0, 0.0, 0.0), 4.0);
    
  	SVector3d scale = new SVector3d(1.0, 1.0, 1.0);
  	SVector3d rotation = new SVector3d(0.0, 0.0, 0.0);
  	SVector3d translation = new SVector3d(0.0, 0.0, 0.0);
	  
    // Avec la transformation, la diagonale du cube dans le plan xy sera égal à (2^2 + 2^2)^1/2 = 2.828427...
    STransformableGeometry transformable_geometry = new STransformableGeometry(geometry, scale, rotation, translation);
    
    // Test #1 : Le centre du cube translaté
    Assert.assertEquals(true, transformable_geometry.isInside(new SVector3d(1.0, 0.0, 0.0)));
    
    // Test #2 : Une extréminté du cube sous une rotation (à l'intérieur)
    Assert.assertEquals(true, transformable_geometry.isInside(new SVector3d(-1.0, -1.0, -1.0)));
    
    // Test #3 : Une extréminté du cube sous une rotation (à l'extérieur)
    Assert.assertEquals(false, transformable_geometry.isInside(new SVector3d(3.0, 0.0, 0.0)));
    
    // Test #4 : Un vecteur non transformé
    Assert.assertEquals(false, transformable_geometry.isInside(new SVector3d(3.0, 3.0, 3.0)));	
  }
  
  /**
   * Test de la méthode isInside(SVector3d v) avec l'usage de la géométrie <b>SCubeGeometry</b> dans le cas où il y a plusieurs transformations.
   * Plusieurs tests seront réalisés où le point testé sera à l'intérieur et à l'extérieur de la géométrie transformée.
   */
  @Test
  public void isInsideTest2()
  {
    SCubeGeometry geometry = new SCubeGeometry(new SVector3d(0.0, 0.0, 0.0), 2.0);
    
  	SVector3d scale = new SVector3d(2.0, 2.0, 2.0);
  	SVector3d rotation = new SVector3d(0.0, 0.0, 45.0);
  	SVector3d translation = new SVector3d(10.0, 10.0, 10.0);
	  
    // Avec la transformation, la diagonale du cube dans le plan xy sera égal à (2^2 + 2^2)^1/2 = 2.828427...
    STransformableGeometry transformable_geometry = new STransformableGeometry(geometry, scale, rotation, translation);
    
    // Test #1 : Le centre du cube translaté
    Assert.assertEquals(true, transformable_geometry.isInside(new SVector3d(10.0, 10.0, 10.0)));
    
    // Test #2 : Une extréminté du cube sous une rotation (à l'intérieur)
    Assert.assertEquals(true, transformable_geometry.isInside(new SVector3d(10.0, 10.0 + 2.82842, 10.0)));
    
    // Test #3 : Une extréminté du cube sous une rotation (à l'extérieur)
    Assert.assertEquals(false, transformable_geometry.isInside(new SVector3d(10.0, 10.0 + 2.82843, 10.0)));
    
    // Test #4 : Un vecteur non transformé
    Assert.assertEquals(false, transformable_geometry.isInside(new SVector3d(0.0, 0.0, 0.0)));	
  }

  /**
   * Test de la méthode intersection avec l'usage de la géométrie <b>SCubeGeometry</b> dans le cas où aucune transformation n'est requise. 
   * Dans ce test, il y aura intersection.
   */
  @Test
  public void intersectionTest1()
  {
  	SCubeGeometry geometry = new SCubeGeometry(new SVector3d(0.0, 0.0, 0.0), 4.0);
  	  
  	SVector3d scale = new SVector3d(1.0, 1.0, 1.0);
  	SVector3d rotation = SVector3d.ZERO;
  	SVector3d translation = SVector3d.ZERO;
  	  
  	STransformableGeometry transformable_geometry = new STransformableGeometry(geometry, scale, rotation, translation);
  	  
  	SVector3d ray_position = new SVector3d(-5.0, 1.0, 1.0);
  	SVector3d ray_direction = new SVector3d(1.0, 0.0, 0.0);
  	  
  	SRay ray = new SRay(ray_position, ray_direction, 1.0);
  	
  	SRay calculated = transformable_geometry.intersection(ray);
  	
  	SRay expected = ray.intersection(transformable_geometry, new SVector3d(-1.0, 0.0, 0.0), 3.0);
  	
  	Assert.assertEquals(expected, calculated);
  }
  
  /**
   * Test de la méthode intersection avec l'usage de la géométrie <b>SCubeGeometry</b> dans le cas où aucune transformation n'est requise. 
   * Dans ce test, il n'y aura pas d'intersection.
   */
  @Test
  public void intersectionTest2()
  {
  	SCubeGeometry geometry = new SCubeGeometry(new SVector3d(0.0, 0.0, 0.0), 3.0);
  	  
  	SVector3d scale = new SVector3d(1.0, 1.0, 1.0);
  	SVector3d rotation = SVector3d.ZERO;
  	SVector3d translation = SVector3d.ZERO;
  	  
  	STransformableGeometry transformable_geometry = new STransformableGeometry(geometry, scale, rotation, translation);
  	  
  	SVector3d ray_position = new SVector3d(-5.0, 1.0, 1.0);
  	SVector3d ray_direction = new SVector3d(1.0, -3.0, 0.0);
  	  
  	SRay ray = new SRay(ray_position, ray_direction, 1.0);
  	
  	SRay calculated = transformable_geometry.intersection(ray);
  	
  	// Il n'y aura pas d'intersection.
  	SRay expected = ray;
  	
  	Assert.assertEquals(expected, calculated);
  }
  
  /**
   * Test de la méthode intersection avec l'usage de la géométrie <b>SCubeGeometry</b> dans le cas où il y aura uniquement une transformation de translation. 
   * Dans ce test, il y aura intersection.
   */
  @Test
  public void intersectionTest3()
  {
  	SCubeGeometry geometry = new SCubeGeometry(new SVector3d(0.0, 0.0, 0.0), 4.0);
  	  
  	SVector3d scale = new SVector3d(1.0, 1.0, 1.0);
  	SVector3d rotation = SVector3d.ZERO;
  	SVector3d translation = new SVector3d(0.0, 0.0, -5.0);
  	  
  	STransformableGeometry transformable_geometry = new STransformableGeometry(geometry, scale, rotation, translation);
  	  
  	SVector3d ray_position = new SVector3d(-5.0, 1.0, 1.0);
  	SVector3d ray_direction = new SVector3d(1.0, 0.0, -2.0);
  	  
  	SRay ray = new SRay(ray_position, ray_direction, 1.0);
  	
  	SRay calculated = transformable_geometry.intersection(ray);
  	
  	SRay expected = ray.intersection(transformable_geometry, new SVector3d(-1.0, 0.0, 0.0), 3.0);
  	
  	Assert.assertEquals(expected, calculated);
  }
  
  /**
   * Test de la méthode intersection avec l'usage de la géométrie <b>SCubeGeometry</b> dans un cas général. 
   * Dans ce test, il n'y aura pas d'intersection.
   */
  @Test
  public void intersectionTest4()
  {
  	SCubeGeometry geometry = new SCubeGeometry(new SVector3d(0.0, 0.0, 0.0), 3.0);
  	  
  	SVector3d scale = new SVector3d(5.0, 5.0, 5.0);
  	SVector3d rotation = new SVector3d(0.0, 0.0, 45.0);
  	SVector3d translation = new SVector3d(0.0, 0.0, -10.0);
  	  
  	STransformableGeometry transformable_geometry = new STransformableGeometry(geometry, scale, rotation, translation);
  	  
  	SVector3d ray_position = new SVector3d(5.0, 5.0, 9.0);
  	SVector3d ray_direction = new SVector3d(5.0, 0.0, -1.0);
  	  
  	SRay ray = new SRay(ray_position, ray_direction, 1.0);
  	
  	SRay calculated = transformable_geometry.intersection(ray);
  	
  	// Il n'y a pas d'intersection.
  	SRay expected = ray;
  	
  	Assert.assertEquals(expected, calculated);
  }
  
  /**
   * Test de la méthode intersection avec l'usage de la géométrie <b>SCubeGeometry</b> dans un cas général. 
   * Dans ce test, il y aura intersection car la rotation du cube selon l'axe z est faible. 
   * La normale "normalisée" sera la même dans l'espace monde et objet. Ce test ne vérifie pas la transformation de la normale.
   * 
   */
  @Test
  public void intersectionTest5a()
  {
  	SCubeGeometry geometry = new SCubeGeometry(new SVector3d(0.0, 0.0, 0.0), 4.0);
  	  
  	SVector3d scale = new SVector3d(5.0, 5.0, 5.0);
  	SVector3d rotation = new SVector3d(0.0, 0.0, 5.0);
  	SVector3d translation = new SVector3d(0.0, 0.0, -5.0);
  	  
  	STransformableGeometry transformable_geometry = new STransformableGeometry(geometry, scale, rotation, translation);
  	  
  	SVector3d ray_position = new SVector3d(7.0, 7.0, 7.0);
  	SVector3d ray_direction = new SVector3d(1.0, 0.0, -1.0);
  	  
  	SRay ray = new SRay(ray_position, ray_direction, 1.0);
  	
  	SRay calculated = transformable_geometry.intersection(ray);
  	
  	SRay expected = ray.intersection(transformable_geometry, new SVector3d(0.0, 0.0, 1.0), 2.0);
  	
  	Assert.assertEquals(expected, calculated);
  }
  
  /**
   * Test de la méthode intersection avec l'usage de la géométrie <b>SCubeGeometry</b> dans un cas général. 
   * Dans ce test, il n'y aura intersection car la rotation du cube selon l'axe z est prononcée.
   */
  @Test
  public void intersectionTest5b()
  {
    SCubeGeometry geometry = new SCubeGeometry(new SVector3d(0.0, 0.0, 0.0), 4.0);
      
    SVector3d scale = new SVector3d(5.0, 5.0, 5.0);
    SVector3d rotation = new SVector3d(0.0, 0.0, 65.0);
    SVector3d translation = new SVector3d(0.0, 0.0, -5.0);
      
    STransformableGeometry transformable_geometry = new STransformableGeometry(geometry, scale, rotation, translation);
      
    SVector3d ray_position = new SVector3d(7.0, 7.0, 7.0);
    SVector3d ray_direction = new SVector3d(1.0, 0.0, -1.0);
      
    SRay ray = new SRay(ray_position, ray_direction, 1.0);
    
    SRay calculated = transformable_geometry.intersection(ray);
    
    // Il n'y a pas d'intersection.
    SRay expected = ray;
    
    Assert.assertEquals(expected, calculated);
  }
  
  /**
   * Test de la méthode intersection avec l'usage de la géométrie <b>SCubeGeometry</b> dans un cas général. 
   * Dans ce test, il y aura intersection et la normale de l'espace objet devra être transformée vers l'espace monde et normalisée.
   */
  @Test
  public void intersectionTest6()
  {
  	SCubeGeometry geometry = new SCubeGeometry(new SVector3d(0.0, 0.0, 0.0), 4.0);
  	  
  	SVector3d scale = new SVector3d(5.0, 5.0, 5.0);
  	SVector3d rotation = new SVector3d(0.0, 45.0, 45.0);
  	SVector3d translation = new SVector3d(0.0, 0.0, -10.0);
  	  
  	STransformableGeometry transformable_geometry = new STransformableGeometry(geometry, scale, rotation, translation);
  	  
  	SVector3d ray_position = new SVector3d(5.0, 5.0, 2.0);
  	SVector3d ray_direction = new SVector3d(1.0, 0.0, -3.0);
  	  
  	SRay ray = new SRay(ray_position, ray_direction, 1.0);
  	
  	SRay calculated = transformable_geometry.intersection(ray);
  	
  	SRay expected = ray.intersection(transformable_geometry, new SVector3d(0.5, 0.5, 0.7071067811865476), 2.149656228075546);
  	
  	Assert.assertEquals(expected, calculated);
  }
    
}//fin de la classe STransformableGeometry
