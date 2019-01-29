/**
 * 
 */
package sim.graphics;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sim.exception.SConstructorException;
import sim.graphics.SColor;
import sim.graphics.SViewport;
import sim.math.SVectorPixel;

/**
 * JUnit test permettant de valider les fonctionnalit�s de la classe <b>SViewport</b>.
 * 
 * @author Simon V�zina
 * @since 2015-09-24
 * @version 2016-04-22
 */
public class SViewportTest {

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test v�rifiant si une mauvaise construction d'un SViewport lance une exception.
   */
  @Test
  public void test_constructor() 
  {
    try{
      
      SViewport viewport = new SViewport(-5, -5);
      
      fail("FAIL - La construction est impossible, car des dimensions du viewport sont n�gative.");
      
      // Pour �viter le warning.
      viewport.clear();
      
    }catch(SConstructorException e){
      // C'est le sc�nario d�sir�. 
    }
    
  }
  
  
  
  //A FAIRE !!!!!
  
  
  /**
   * @param args
   */
  public static void main(String[] args) 
  {
    //test1();
    test2();
  }

  /**
   * Test #1 : V�rifier l'�criture d'une image dans un fichier.
   */
  public static void test1()
  {
    sim.util.SChronometer chrono = new sim.util.SChronometer();
    chrono.start();
    System.out.println("D�but de la cr�ation de l'image.");
    
    SViewport viewport = new SViewport();
    
    float inc_x = 1.0f / (float)viewport.getWidth();
    float inc_y = 1.0f / (float)viewport.getHeight();
    
    float r = 0.0f;
    float g = 0.0f;
    float b = 0.0f;
    
    for(int j=0; j<viewport.getHeight(); j++)
    { r = 0.0f;
      for(int i=0; i<viewport.getWidth(); i++)
      {
        viewport.setColor(i, j, new SColor(r,g,b));
                
        r += inc_x;
      }
      b += inc_y;
    }
    
    try{
      viewport.writeImage();  
    }catch(IOException ioe){ ioe.printStackTrace(); }
    
    
    
    try{
      chrono.stop();
      System.out.println("Temps �coul� : " + chrono.getTime() + " s");
    }catch(Exception e){}
    
  }
  
  /**
   * Test #2 : Test pour v�rifier l'�criture de 3 images dans un fichier. 
   */
  public static void test2()
  {
    sim.util.SChronometer chrono = new sim.util.SChronometer();
    SViewport viewport = new SViewport();
    
    chrono.start();
    System.out.println("D�but de la cr�ation de l'image 001.");
    
    float r = 0.0f;
    float g = 0.0f;
    float b = 0.0f;
    
    while(viewport.hasNextPixel())
    {
      SVectorPixel p = viewport.nextPixel();
      
      viewport.setColor(p.getX(), p.getY(), new SColor(r,g,b));
            
      float inc = 1.0f / (float)(viewport.getWidth()*viewport.getHeight());
      r += inc;
      g += inc;
      b += inc;     
    }
    
    try{
      viewport.writeImage();  
    }catch(IOException ioe){ ioe.printStackTrace(); }
    
    try{
      chrono.stop();
      System.out.println("Temps �coul� : " + chrono.getTime() + " s");
    }catch(Exception e){}
    
    viewport.clear();
    
    
    chrono.start();
    System.out.println("D�but de la cr�ation de l'image 002.");
        
    while(viewport.hasNextPixel())
    {
      SVectorPixel p = viewport.nextPixel();
      
      viewport.setColor(p.getX(), p.getY(), new SColor(r,g,b));
            
      r = (float)Math.random();
      g = (float)Math.random();
      b = (float)Math.random();
      
    }
    
    try{
      viewport.writeImage();  
    }catch(IOException ioe){ ioe.printStackTrace(); }
    
    try{
      chrono.stop();
      System.out.println("Temps �coul� : " + chrono.getTime() + " s");
    }catch(Exception e){}
    
    viewport.clear();
    
    
    
    chrono.start();
    System.out.println("D�but de la cr�ation de l'image 003.");
    
    try{
      viewport.writeImage();  
    }catch(IOException ioe){ ioe.printStackTrace(); }
    
      
    try{
      chrono.stop();
      System.out.println("Temps �coul� : " + chrono.getTime() + " s");
    }catch(Exception e){}
    
  }
  
  
}//fin de la classe SViewportTest
