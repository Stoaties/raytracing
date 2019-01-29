/**
 * 
 */
package sim.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sim.readwrite.SKeyWordDecoder;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SKeyWordDecoder</b>.
 * 
 * @author Simon Vézina
 * @since 2016-02-24
 * @version 2016-04-03 
 */
public class SKeyWordDecoderTest {

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

  @Test
  public void test_nbKeyWordKey1()
  {
    Assert.assertEquals(SKeyWordDecoder.getKeyWord().length, SKeyWordDecoder.getNbKeyWord());
  }

  
  
  // À FAIRE ...
  
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    test1();
  }

  private static void test1()
  {
    /*
    buildDataBase();
    
    //iterating over keys only
    for(String key_word : kw_map.keySet())
    {    
      System.out.print("Key = " + key_word);
      System.out.println("\t\t\t Value = " + kw_map.get(key_word));
    }
    */
  }
  
}
