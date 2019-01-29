/**
 * 
 */
package sim.application.util;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sim.util.SLog;

/**
 * JUnit Test de la classe SConfigurationTest.java
 * 
 * @author Simon V�zina
 * @since 2015-11-28
 * @version 2015-11-28
 */
public class SConfigurationTest {

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
   * Test #1 permettant d'�valuer l'�tat de la construction d'un SConfiguration lorsqu'un fichier de configuration n'est pas trouv�.
   */
  @Test
  public void test1_constructor()
  {
    try{
      
      SConfiguration config = new SConfiguration("Ce fichier n'existe pas.");
      fail("Fail - Le fichier de configuration n'est pas suppos� �tre trouv�.");
    
      // Op�ration inutile pour �viter un "warning"
      config.getClass();
      
    }catch(FileNotFoundException e){
      
      // Ce sc�nrio doit arriver ce qui signifie un succ�s.
      
    }catch(IOException e){
      fail("Fail - L'exception de type IOException n'est pas suppos� survenir." + "\t" + e.getMessage());
    }
  }

  // � FAIRE ...
  
  /**
   * @param args
   */
  public static void main(String[] args) 
  {
    test2();
    
    try{
      SLog.closeLogFile();
    }catch(IOException ioe){}
    
  }

  /**
   * Test #2 : V�rification du bon fonctionnement de la lecture du fichier de config.
   */
  public static void test2()
  {
    try{
      SConfiguration config = new SConfiguration();
      
      SLog.logWriteLine("Read file name : " + config.getReadDataFileName());
      SLog.logWriteLine("Write file name : " + config.getWriteDataFileName());
      
    }catch(Exception e){ e.printStackTrace(); }
    
    System.out.println("Fin du test #2");
  }
  
}
