/**
 * 
 */
package sim.graphics;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import sim.math.SVector3d;
import sim.math.SVectorPixel;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SViewFrustum</b>.
 * 
 * @author Simon Vézina
 * @since 2016-06-27
 * @version 2016-06-27
 */
public class SViewFrustumTest {

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

  @Test
  public void test()
  {
    // TODO
  }

  // À faire ...
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    
    test1();
    
  }
  
  /**
   * Test #1 : Test pour vérifier la fonctionnalité du calcul de la position d'un pixel dans l'espace du view frustum.
   */
  public static void test1()
  {
    SViewport viewport = new SViewport(5, 5);
    
    //Reproduction de l'exemple dans les notes de cours
    SCamera camera = new SCamera(new SVector3d(0.0, 0.0, 0.0),      //camera à l'origine
                             new SVector3d(1.0, 0.0, 0.0),      //regarde en x
                             new SVector3d(0.0, 0.0, 1.0));     //up en z
    
    SViewFrustum view_frustum = new SViewFrustum(camera, viewport);
    
    try{
      
      while(viewport.hasNextPixel())
      {
        SVectorPixel p = viewport.nextPixel();
        SVector3d position_pixel = view_frustum.viewportToViewFrustum(p);
        
        System.out.print("Pixel : " + p + "\t Vecteur : " + position_pixel);
        System.out.println();
      }     
    }catch(Exception e){ System.out.println(e.getMessage()); }
  }
  
}//fin de la classe SViewFrustum
