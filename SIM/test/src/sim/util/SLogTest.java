/**
 * 
 */
package sim.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SLog</b>.
 * 
 * @author Simon Vézina
 * @since 2016-01-14
 * @version 2016-04-20
 */
public class SLogTest {

  /**
   * Test #1 permettant de valider les fonctionnalités des méthodes suivante :
   * <ul>setLogFileName(String file_name)</ul>
   * <ul>logWriteLine(String str)</ul>
   * <ul>closeLogFile()</ul>
   */
  @Test
  public void test1_setLogFileName_logWriteLine_closeLogFile()
  {
    try{
      SLog.setLogFileName("logTest.txt");
    }catch(Exception e){
      fail("FAIL - " + e.getMessage());
    }
    
    SLog.setConsoleLog(false);
    
    SLog.logWriteLine("Ceci est un test!");
    SLog.logWriteLine("Ceci est encore un test!");
      
    try{
      SLog.closeLogFile();
    }catch(IOException e){
      fail("FAIL - " + e.getMessage());
    }
    
  }

  // À FAIRE ...
  
  /**
   * Méthode teste pour les fonctionnalités de la classe SLog.
   * @param args
   */
  public static void main(String[] args)
  {
    
    Test2();
    Test3();
  }
   
  private static void Test2()
  {
    try{
      SLog.setLogFileName("logTest.abc");  
    }catch(Exception e){System.out.println(e);}
    
    try{
      SLog.setLogFileName("logTest.ABC");  
      }catch(Exception e){System.out.println(e);}
  }
  
  private static void Test3()
  {
    try{
      SLog.setLogFileName("log_fichier1.txt");
    }catch(Exception e){System.out.println(e);}
    
    SLog.logWriteLine("Fichier 1 : Ceci est un test!");
    SLog.logWriteLine("Fichier 1 : Ceci est encore un test!");
    
    try{
      SLog.setLogFileName("log_fichier2.txt");
    }catch(Exception e){System.out.println(e);}
      
    SLog.logWriteLine("Fichier 2 : Ceci est un test!");
    SLog.logWriteLine("Fichier 2 : Ceci est encore un test!");
      
    try{
      SLog.closeLogFile();
    }catch(IOException ioe){}
  }
  
}
