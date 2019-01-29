/**
 * 
 */
package sim.geometry;

import org.junit.Assert;
import org.junit.Test;
import sim.math.SVector3d;

/**
 * JUnit test permettant de valider les fonctionnalit�s de la classe <b>SDiskGeometry</b>.
 * 
 * @author Simon V�zina
 * @since 2017-01-23
 * @version 2018-01-30
 */
public class SDiskGeometryTest {

  /**
   * Test de l'intersection entre un rayon � voyageant parall�lement au disque en �tant situ� au-dessus. Il n'y a pas d'intersection.
   */
  @Test
  public void intersectionTest1a()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 1.0), new SVector3d(1.0, 0.0, 0.0), 1.0);
      
    SDiskGeometry geometry = new SDiskGeometry(new SVector3d(0.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0), 2.0);
      
    SRay calculated_solution = geometry.intersection(ray);
      
    SRay expected_solution = ray; // sans intersection
      
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection entre un rayon � voyageant parall�lement au disque en �tant situ� dans le plan du disque. Il y a une infinit� de solutions.
   */
  /*
  @Test
  public void intersectionTest1b()
  {
    SRay ray = new SRay(new SVector3d(0.0, 0.0, 0.0), new SVector3d(1.0, 0.0, 0.0), 1.0);
      
    SDiskGeometry geometry = new SDiskGeometry(new SVector3d(0.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0), 2.0);
   
    try{
   
      SRay calculated_solution = geometry.intersection(ray);
      fail("Ce test devrait donner une infinit� de solutions. Pr�sentement, votre solution de temps est t = " + calculated_solution.getT());
 
    }catch(SInfinityOfSolutionsException e){
      // c'est un succ�s
    }  
  }
  */
  
  /**
   * Test de l'intersection entre un rayon et un disque o� le rayon est initialement sur le disque. Il n'y a pas d'intersection.
   */
  @Test
  public void intersectionTest2()
  {
    // Un point du disque
    SVector3d r_p = new SVector3d(-1.0, 3.0, -7.0);
    double R = 3.0;
    
    // La base du plan
    SVector3d u1 = new SVector3d(7.0, 3.0, 2.0);
    SVector3d u2 = new SVector3d(-4.0, 2.0, -1.0);
    
    // La normale � la surface du plan
    SVector3d n_p = u1.cross(u2).normalize();
    
    // Origine du rayon (sur le plan)
    SVector3d r0 = r_p.add(u1.multiply(3.0)).add(u2.multiply(6.0));
    
    // Orientation du rayon (quelconque)
    SVector3d v = new SVector3d(8.0, -9.0, 2.0);
    
    // Le calcul � tester
    SRay ray = new SRay(r0, v, 1.0);
    
    SDiskGeometry geometry = new SDiskGeometry(r_p, n_p, R);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    SRay expected_solution = ray; // sans intersection
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }

  /**
   * Test de l'intersection entre un rayon et un disque o� le rayon o� le temps de l'intersection est positif. Le rayon est � <u>l'int�rieur du disque</u>.
   */
  @Test
  public void intersectionTest3()
  {
    // Un point du plan
    SVector3d r_p = new SVector3d(2.0, 2.0, 5.0);
    double R = 3.0;
    
    // La normale � la surface du plan
    SVector3d n_p = new SVector3d(1.0, 1.0, 1.0).normalize();
    
    // Origine du rayon 
    SVector3d r0 = new SVector3d(4.0, 4.0, 10.0);
    
    // Orientation du rayon 
    SVector3d v = new SVector3d(-1.0, 0.0, -2.0);
    
    // Le calcul � tester
    SRay ray = new SRay(r0, v, 1.0);
    
    SDiskGeometry geometry = new SDiskGeometry(r_p, n_p, R);
    
    SRay calculated_solution = geometry.intersection(ray);
    
    SRay expected_solution = ray.intersection(geometry, n_p, 3.0);   // intersection � l'int�rieur du disque
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection entre un rayon et un disque o� le rayon o� le temps de l'intersection est positif, mais le rayon est � <u>l'ext�rieur du disque</u>.
   */
  @Test
  public void intersectionTest4a()
  {
    // Un point du plan
    SVector3d r_p = new SVector3d(2.0, 2.0, 5.0);
    double R = 2.0;   // Remarque : 2.4490 et plus petit --> out  2.4499 et plus grand --> in
    
    // La normale � la surface du plan
    SVector3d n_p = new SVector3d(1.0, 1.0, 1.0).normalize();
    
    // Origine du rayon 
    SVector3d r0 = new SVector3d(4.0, 4.0, 10.0);
    
    // Orientation du rayon 
    SVector3d v = new SVector3d(-1.0, 0.0, -2.0);
    
    // Le calcul � tester
    SRay ray = new SRay(r0, v, 1.0);
    
    SDiskGeometry geometry = new SDiskGeometry(r_p, n_p, R);
    
    // R�aliser le test :
    SRay calculated_solution = geometry.intersection(ray);
    SRay expected_solution = ray; // intersection � l'ext�rieur du disque
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection entre un rayon et un disque o� le rayon o� le temps de l'intersection est positif, mais le rayon est � <u>l'int�rieur du disque</u>.
   */
  @Test
  public void intersectionTest4b()
  {
    // Un point du plan
    SVector3d r_p = new SVector3d(2.0, 2.0, 5.0);
    double R = 2.8;   // Remarque : 2.4490 et plus petit --> out  2.4499 et plus grand --> in
    
    // La normale � la surface du plan
    SVector3d n_p = new SVector3d(1.0, 1.0, 1.0).normalize();
    
    // Origine du rayon 
    SVector3d r0 = new SVector3d(4.0, 4.0, 10.0);
    
    // Orientation du rayon 
    SVector3d v = new SVector3d(-1.0, 0.0, -2.0);
    
    // Le calcul � tester
    SRay ray = new SRay(r0, v, 1.0);
    
    SDiskGeometry geometry = new SDiskGeometry(r_p, n_p, R);
    
    // R�aliser le test :
    SRay calculated_solution = geometry.intersection(ray);
    SRay expected_solution = ray.intersection(geometry, n_p, 3.0); 
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de l'intersection entre un rayon et un plan o� le rayon o� le temps de l'intersection est n�gatif. Il n'y a donc pas d'intersection.
   */
  @Test
  public void intersectionTest4c()
  {
    // Un point du plan
    SVector3d r_p = new SVector3d(2.0, 2.0, 5.0);
    double R = 2.0;
    
    // La normale � la surface du plan
    SVector3d n_p = new SVector3d(1.0, 1.0, 1.0).normalize();
    
    // Origine du rayon 
    SVector3d r0 = new SVector3d(0.0, 2.0, -15.0);
    
    // Orientation du rayon 
    SVector3d v = new SVector3d(-1.0, 0.0, -2.0);
    
    // Le calcul � tester
    SRay ray = new SRay(r0, v, 1.0);
    
    SDiskGeometry geometry = new SDiskGeometry(r_p, n_p, R);
    
    // R�aliser le test :
    SRay calculated_solution = geometry.intersection(ray);
    SRay expected_solution = ray;   // pas d'intersection
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de la m�thode <b>intersection</b> avec un rayon tr�s �loign� du plan menant � une intersection � un temps positif (o� t > R) � l'int�rieur du disque. Il y a donc intersection.
   */
  @Test
  public void intersectionTest5a()
  {
    // Le disque :
    SVector3d r_p = new SVector3d(2.0, 2.0, 2.0);
    SVector3d n_p = new SVector3d(0.0, 0.0, 1.0);
    double R = 5.0;
    SDiskGeometry geometry = new SDiskGeometry(r_p, n_p, R);
    
    // Le rayon :
    SVector3d r0 = new SVector3d(3.0, 2.0, 10.0);  
    SVector3d v = new SVector3d(0.0, 0.0, -1.0);
    SRay ray = new SRay(r0, v, 1.0);
    
    // R�aliser le test :
    SRay calculated_solution = geometry.intersection(ray);
    SRay expected_solution = ray.intersection(geometry, n_p, 8.0);
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
  /**
   * Test de la m�thode <b>intersection</b> avec un rayon tr�s �loign� du plan menant � une intersection � un temps positif (o� t > R) � l'ext�rieur du disque. Il n'y a donc pas d'intersection.
   */
  @Test
  public void intersectionTest5b()
  {
    // Le disque :
    SVector3d r_p = new SVector3d(2.0, 2.0, 2.0);
    SVector3d n_p = new SVector3d(0.0, 0.0, 1.0);
    double R = 5.0;
    SDiskGeometry geometry = new SDiskGeometry(r_p, n_p, R);
    
    // Le rayon :
    SVector3d r0 = new SVector3d(8.0, 2.0, 10.0);  
    SVector3d v = new SVector3d(0.0, 0.0, -1.0);
    SRay ray = new SRay(r0, v, 1.0);
    
    // R�aliser le test :
    SRay calculated_solution = geometry.intersection(ray);
    SRay expected_solution = ray;
    
    Assert.assertEquals(expected_solution, calculated_solution);
  }
  
}//fin de la classe SDiskGeometryTest
