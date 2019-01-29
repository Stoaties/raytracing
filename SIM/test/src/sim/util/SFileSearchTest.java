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

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SFileSearch</b>.
 * 
 * @author Simon Vézina
 * @since 2015-10-03
 * @version 2016-04-20
 */
public class SFileSearchTest {

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
   * Test permettant de vérifier la méthode isFileFound() dans un scénario où rien n'est trouvé.
   */
  @Test
  public void test_isFileFound()
  {
    SFileSearch search = new SFileSearch("Fichier qui n'est pas trouvable.");
    
    Assert.assertEquals(false, search.isFileFound());
  }

  /**
   * Test permettant de vérifier la méthode isManyFileFound() dans un scénario où rien n'est trouvé.
   */
  @Test
  public void test_isManyFileFound()
  {
    SFileSearch search = new SFileSearch("Fichier qui n'est pas trouvable.");
    
    Assert.assertEquals(false, search.isManyFileFound());
  }
  
  //À faire ...
  
  /**
   * Méthode pour tester les fonctionnalités locaux de la classe SFileSearch.
   * @param args
   */
  public static void main(String[] args)
  {
    test1();
    test2();
    test3();
  }
  
  /**
   * Test #1
   */
  public static void test1()
  {
    String file_name = "atenea.obj";
    
    SFileSearch search = new SFileSearch("", file_name);
    
    int count = search.getFileFoundList().size();
    
    if(count ==0)
    {
      System.out.println("Aucun fichier '" + file_name + "' a été trouvé.");
    }
    else
    {
      System.out.println("Le fichier '" + file_name + "' a été trouvé " + count + " fois.");
      
      for(String found : search.getFileFoundList())
      {
        System.out.println("Fichier trouvé : " + found);
      }
    }
  }

  /**
   * Test #2
   */
  public static void test2()
  {
    String file_name = "foot.obj";
    
    SFileSearch search = new SFileSearch("BIN", file_name);
    
    int count = search.getFileFoundList().size();
    
    if(count ==0)
    {
      System.out.println("Aucun fichier '" + file_name + "' a été trouvé.");
    }
    else
    {
      System.out.println("Le fichier '" + file_name + "' a été trouvé " + count + " fois.");
      
      for(String found : search.getFileFoundList())
      {
        System.out.println("Fichier trouvé : " + found);
      }
    }
  }
  
  /**
   * Test #3
   */
  public static void test3()
  {
    String file_name = "foot.obj";
    
    SFileSearch search = new SFileSearch("model", file_name);
    
    int count = search.getFileFoundList().size();
    
    if(count ==0)
    {
      System.out.println("Aucun fichier '" + file_name + "' a été trouvé.");
    }
    else
    {
      System.out.println("Le fichier '" + file_name + "' a été trouvé " + count + " fois.");
      
      for(String found : search.getFileFoundList())
      {
        System.out.println("Fichier trouvé : " + found);
      }
    }
  }
  
}
