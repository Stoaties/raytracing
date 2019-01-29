/**
 * 
 */
package sim.geometry;

import org.junit.Assert;
import org.junit.Test;
import sim.math.SVector3d;


/**
 * JUnit test permettant de valider les fonctionnalit�s de la classe <b>STubeGeometry</b>.
 * 
 * @author Simon V�zina
 * @since 2016-04-18
 * @version 2018-02-11
 */
public class STubeGeometryTest {

  /**
   * Test de l'intersection entre un rayon � l'origine et le tube align� selon l'axe z. 
   * Le rayon voyage selon l'axe x. L'intersection sera compos� d'un temps n�gatif (� rejeter) et un temps positif. 
   */
  @Test
  public void intersectionTest1()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 0.0), new SVector3d(1.0, 0.0, 0.0), 1.0);
      
    STubeGeometry geometry = new STubeGeometry(new SVector3d(0.0, 0.0, -1.0), new SVector3d(0.0, 0.0, 1.0), 1.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    // Orientation de la normale � la surface � l'ext�rieur du tube.
    SVector3d expected_normal = new SVector3d(1.0, 0.0, 0.0);
    
    SRay expected_solution = ray.intersection(geometry, expected_normal, 1.0);
      
    Assert.assertEquals(expected_solution, calculated_solution);
  }

  /**
   * Test de l'intersection <u>avec 2 succ�s</u> entre un rayon et le tube dans un configuration quelconque. Le test est r�alis� avec un tube est assez long.
   */
  @Test
  public void intersectionTest2a()
  {
    SRay ray = new SRay(new SVector3d(1.0, 2.0, 3.0), new SVector3d(2.0, 2.0, 1.0), 1.0);
      
    STubeGeometry geometry = new STubeGeometry(new SVector3d(5.0, 5.0, -2.0), new SVector3d(6.0, 7.0, 10.0), 3.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    SRay expected_solution = ray.intersection(geometry, new SVector3d(-0.7809976398562594, -0.6022192201512141, 0.16545300667989066), 1.0603359106580434);
      
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>sans succ�s</u> entre un rayon et le tube dans un configuration quelconque. Le test est r�alis� avec un tube qui n'est pas assez long.
   */
  @Test
  public void intersectionTest2b()
  {
    SRay ray = new SRay(new SVector3d(1.0, 2.0, 3.0), new SVector3d(2.0, 2.0, 1.0), 1.0);
      
    STubeGeometry geometry = new STubeGeometry(new SVector3d(5.0, 5.0, -2.0), new SVector3d(6.0, 7.0, 2.0), 3.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    SRay expected_solution = ray;
      
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>sans succ�s</u> entre un rayon et le tube dans un configuration quelconque. Le test est r�alis� avec un rayon qui voyage dans la mauvaise direction.
   */
  @Test
  public void intersectionTest2c()
  {
    SRay ray = new SRay(new SVector3d(1.0, 2.0, 3.0), new SVector3d(-2.0, -2.0, -1.0), 1.0);
      
    STubeGeometry geometry = new STubeGeometry(new SVector3d(5.0, 5.0, -2.0), new SVector3d(6.0, 7.0, 10.0), 3.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    SRay expected_solution = ray;
          
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>avec 2 succ�s</u> entre un rayon et le tube. Le 1ier temps est n�gatif. Le 2i�me temps est positif � l'int�rieur des extr�mit�s.
   */
  @Test
  public void intersectionTest3()
  {
    SRay ray = new SRay(new SVector3d(-2.0, 0.0, -2.0), new SVector3d(1.0, 0.0, 2.0), 1.0);
    
    STubeGeometry geometry = new STubeGeometry(new SVector3d(0.0, 0.0, 0.0), new SVector3d(5.0, 0.0, 0.0), 5.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    // Orientation de la normale � la surface � l'ext�rieur du tube.
    SVector3d expected_normal = new SVector3d(0.0, 0.0, 1.0);
    
    SRay expected_solution = ray.intersection(geometry, expected_normal, 3.5);
          
    Assert.assertEquals(expected_solution, calculated_solution);

  }
  
  /**
   * Test de l'intersection <u>avec 2 succ�s</u> entre un rayon et le tube. Le 1ier temps est positif, mais � l'ext�rieur des extr�mit�s. Le 2i�me temps est positif et � l'int�rieur des extr�mit�s.
   */
  @Test
  public void intersectionTest4()
  {
    SRay ray = new SRay(new SVector3d(-3.0, 0.0, -1.0), new SVector3d(2.0, 0.0, 1.0), 1.0);
      
    STubeGeometry geometry = new STubeGeometry(new SVector3d(0.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 5.0), 2.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    // Orientation de la normale � la surface � l'ext�rieur du tube.
    SVector3d expected_normal = new SVector3d(1.0, 0.0, 0.0);
    
    SRay expected_solution = ray.intersection(geometry, expected_normal, 2.5);
          
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection <u>avec 1 succ�s</u> entre un rayon et le tube o� le rayon par initialement sur la surface du tube.
   */
  @Test
  public void intersectionTest5()
  {
    SRay ray = new SRay(new SVector3d(5.0, 2.0, 0.0), new SVector3d(0.0, 2.0, 0.0), 1.0);
      
    STubeGeometry geometry = new STubeGeometry(new SVector3d(5.0, 5.0, -2.0), new SVector3d(5.0, 5.0, 2.0), 3.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    // Orientation de la normale � la surface � l'ext�rieur du tube.
    SVector3d expected_normal = new SVector3d(0.0, 1.0, 0.0);
    
    SRay expected_solution = ray.intersection(geometry, expected_normal, 3.0);
      
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
}//fin de la classe STubeGeometryTest
