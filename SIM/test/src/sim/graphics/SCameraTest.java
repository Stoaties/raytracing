/**
 * 
 */
package sim.graphics;

import java.io.BufferedWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sim.graphics.SCamera;
import sim.math.SVector3d;
import sim.util.SBufferedReader;
import sim.util.SLog;

/**
 * JUnit Test de la classe <b>SCamera</b>.
 * 
 * @author Simon Vézina
 * @since 2015-11-28
 * @version 2016-04-20
 */
public class SCameraTest {

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
  }

  /**
   * Test permettant de valider les fonctionnalités de la méthode getUp().
   */
  @Test
  public void test_getUp()
  {
    SCamera camera = new SCamera(new SVector3d(0.0, 0.0, 0.0), new SVector3d(1.0, 0.0, 0.0), new SVector3d(1.0, 0.0, 1.0));
    
    Assert.assertEquals(new SVector3d(0.0, 0.0, 1.0), camera.getUp());
  }

  //À faire ...
  
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    test1();
    
    try{
    SLog.closeLogFile();
    }catch(Exception e){}
    
  }

  private static void test1()
  {
    try
    {
      java.io.FileReader fr = new java.io.FileReader("lectureCamera.txt");
      SBufferedReader br = new SBufferedReader(fr);
      
      SCamera camera = new SCamera(br);
            
      java.io.FileWriter fw = new java.io.FileWriter("ecritureCamera.txt");
      BufferedWriter bw = new BufferedWriter(fw);
      
      camera.write(bw);
      
      bw.close(); //  fermer celui-ci en premier, sinon, ERROR !!!
      fw.close();
    }
    catch(java.io.FileNotFoundException e){ SLog.logWriteLine(e.getMessage()); }
    catch(Exception e){ SLog.logWriteLine(e.getMessage()); }    
  }
  
}//fin de la classe SCameraTest
