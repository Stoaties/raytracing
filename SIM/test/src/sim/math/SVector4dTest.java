/**
 * 
 */
package sim.math;

import static org.junit.Assert.*;

import java.io.BufferedWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sim.util.SReadingException;

/**
 * JUnit test permettant de valider les fonctionnalit�s de la classe SVector4d.
 *
 * @author Simon V�zina
 * @since 2015-10-24
 * @version 2016-03-16
 */
public class SVector4dTest {

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
  public void test1_constructor()
  {
    try{
      
      SVector4d calculated_solution = new SVector4d("[3.4  4.5  3.2]"); 
      calculated_solution.normalize();
      
      fail("Une exception de type SReadingException doit �tre lanc�e.");
      
    }catch(SReadingException e){
      
    }
  }

  @Test
  public void test2_constructor()
  {
    try{
      
      SVector4d calculated_solution = new SVector4d("[3.4  4.5  3.2  1.8]"); 
      SVector4d expected_solution = new SVector4d(3.4, 4.5, 3.2, 1.8);
      
      Assert.assertEquals(expected_solution, calculated_solution);
      
    }catch(SReadingException e){
      fail("Une exception de type SReadingException a �t� lanc�e, mais l'expression devrait �tre correcte.");
    }
  }
  
  //� FAIRE ...
  
  /**
   * @param args
   */
  public static void main(String[] args) 
  {
    test1();
    test2();
  }

  /**
   * Test #1 : Test pour v�rifier la fonctionnalit� de la lecture d'un string comme param�tre d'initialisation du vecteur.
   */
  private static void test1()
  {
    String s;
        
    s = "[3.4  4.5  3.2]";
    try{
    SVector4d v = new SVector4d(s);
    System.out.println(v);
    }catch(Exception e){ System.out.println(e); }
    
    s = "(3.7, 1.5, 5.2)";
    try{
    SVector4d v = new SVector4d(s);
    System.out.println(v);
    }catch(Exception e){ System.out.println(e); }
    
    s = "3.4  chien  3.8";
    try{
    SVector4d v = new SVector4d(s);
    System.out.println(v);
    }catch(Exception e){ System.out.println(e); }
    
    s = "(3.4 , 4.6 , 3.8 , 5.8)";
    try{
    SVector4d v = new SVector4d(s);
    System.out.println(v);
    }catch(Exception e){ System.out.println(e); }
    
    System.out.println();
  }
  
  /**
   * Test #2 : Test pour v�rifier la fonctionnalit� de l'�criture du vecteur dans un fichier txt
   */
  private static void test2()
  {
    try
    {
      String file_name = "vector4dTest.txt";
      SVector4d v = new SVector4d(0.2, 0.3, 0.5, 2.4);
      
      System.out.println("�criture dans le fichier : " + file_name);
      System.out.println("Vecteur � �crire : " + v);
      System.out.println();
      
      java.io.FileWriter fw = new java.io.FileWriter(file_name);
      BufferedWriter bw = new BufferedWriter(fw);
      
      v.write(bw);
      
      bw.close(); //  fermer celui-ci en premier, sinon, ERROR !!!
      fw.close();
    }catch(Exception e){ System.out.println(e); }
    
    System.out.println();
  }
  
}//fin de la classe SVector4dTest
