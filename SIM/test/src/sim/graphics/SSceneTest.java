/**
 * 
 */
package sim.graphics;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sim.graphics.SScene;
import sim.util.SLog;

/**
 * JUnit test permettant de valider les fonctionnalit�s de la classe <b>SScene</b>.
 * 
 * @author Simon V�zina
 * @since 2015-10-07
 * @version 2016-04-20
 */
public class SSceneTest {

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
   * Test permettant de v�rifier la construction d'une sc�ne sans fichier de sc�ne valide.
   */
  @Test
  public void test_constructor()
  {
    try{
    
      SScene scene = new SScene("La sc�ne n'existe pas.");
    
      fail("FAIL - Le fichier de sc�ne n'est pas suppos� �tre trouv�.");
     
      // Afin de retirer le "warning".
      scene.buildRaytracer();
      
    }catch(FileNotFoundException e){
      
      // Ce sc�nario est suppos� ce produire.
    
    }catch(IOException e){
      fail("FAIL - Cette exception n'est pas supposer survenir." + "\t" + e.getMessage());
    }
    
  }

  //� FAIRE ...
  
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

  public static void test1()
  {
    /*
    try
    {
      java.io.FileReader fr = new java.io.FileReader("lectureScene.txt");
      SBufferedReader br = new SBufferedReader(fr);
      
      SScene scene = new SScene(br);
            
      java.io.FileWriter fw = new java.io.FileWriter("ecritureScene.txt");
      BufferedWriter bw = new BufferedWriter(fw);
      
      scene.write(bw);
      
      bw.close(); //  fermer celui-ci en premier, sinon, ERROR !!!
      fw.close();
    }
    catch(java.io.FileNotFoundException e){ SLog.logWriteLine(e.getMessage()); }
    catch(Exception e){ e.printStackTrace(); }  
    */
  }
  
}
