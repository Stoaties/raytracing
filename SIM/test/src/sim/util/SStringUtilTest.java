/**
 * 
 */
package sim.util;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit Test de la classe SStringUtil.
 * 
 * @author Simon Vézina
 * @since 2015-12-02
 * @version 2017-11-23
 */
public class SStringUtilTest {

  /**
   * JUnit Test de la méthode <b>merge</b>.
   */
  @Test
  public void mergeTest1()
  {
    String[] tab1 = { "ab", "ac", "ad" };
    
    String[] tab2 = { "bb", "bc" };
    
    String[] expected = { "ab", "ac", "ad", "bb", "bc" };
    
    Assert.assertEquals(Arrays.asList(expected), Arrays.asList(SStringUtil.merge(tab1, tab2)));
  }

  /**
   * JUnit Test de la méthode <b>exceptionMessage</b>.
   * 
   * Voici le message d'affichage attendu :
   * 
   * Erreur : Erreur 3
   *    Cause : Erreur 2
   *    Cause : Erreur 1
   */
  @Test
  public void exceptionMessageTest1()
  {
    Exception e1 = new Exception("Erreur 1");
    Exception e2 = new Exception("Erreur 2", e1);
    Exception e3 = new Exception("Erreur 3", e2);
    
    String expected = "Erreur : Erreur 3" + SStringUtil.END_LINE_CARACTER;
    expected = expected + SStringUtil.TAB_CARACTER + "Cause : Erreur 2" + SStringUtil.END_LINE_CARACTER;
    expected = expected + SStringUtil.TAB_CARACTER + "Cause : Erreur 1" + SStringUtil.END_LINE_CARACTER;
    
    Assert.assertEquals(expected, SStringUtil.exceptionMessage(e3));
  }
  
}// fin de la classe SStringUtilTest
