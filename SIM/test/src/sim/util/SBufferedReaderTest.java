/**
 * 
 */
package sim.util;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

/**
 * JUnit test permettant de valider les fonctionnalit�s de la classe <b>SBufferedReader</b>.
 * 
 * @author Simon V�zina
 * @since 2015-12-05
 * @version 2016-04-22
 */
public class SBufferedReaderTest {

  /**
   * Test permettant d'�valuer le comportement d'un SBufferedReader lors de l'ouverture d'un fichier de lecture impossible � trouver.
   */
  @Test
  public void test_constructor()
  {
    try{
      
      java.io.FileReader fr = new java.io.FileReader("Ce Fichier est inexistant.");
      
      fail("FAIL - Le fichier de lecture ne devrait pas �tre trouv�.");
      
      SBufferedReader sbr = new SBufferedReader(fr);
      
      try{
      sbr.close();
      }catch(IOException e){
        fail("FAIL - Ce sc�nario est encore moins possible � r�aliser.");
      }
      
    }catch(FileNotFoundException e){
      // C'est le sc�nario cibl�.
    }
    
  }

  // � faire ...
  
  /**
   * @param args
   */
  public static void main(String[] args) 
  {
    test1();
  }

  private static void test1()
  {
    try{
      
      java.io.FileReader fr = new java.io.FileReader("SBufferedReaderTest.txt");
      SBufferedReader sbr = new SBufferedReader(fr);
      
      String line = null;
      
      do
      {
        line = sbr.readLine();
        if(line != null)
          System.out.println((sbr.atLine()-1) + " : " + line);
          
      }while(line != null);
    
      sbr.close();
      fr.close();
    }catch(Exception e){ System.out.println(e.getMessage()); }
  }
  
}
