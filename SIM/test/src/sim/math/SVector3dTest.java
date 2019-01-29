package sim.math;

import org.junit.Assert;
import org.junit.Test;

import sim.exception.SNoImplementationException;
import sim.util.SLog;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SVector3d</b>.
 * 
 * @author Simon Vézina
 * @since 2015-08-13
 * @version 2017-08-17
 */
public class SVector3dTest {

  /**
   * Test de la méthode <b>add</b> dans des cas simples.
   */
  @Test
  public void addTest1()
  {
    try{
      
    SVector3d i = new SVector3d(1.0, 0.0, 0.0);
    SVector3d j = new SVector3d(0.0, 1.0, 0.0);
    SVector3d k = new SVector3d(0.0, 0.0, 1.0);
    
    Assert.assertEquals(SVector3d.ZERO, SVector3d.ZERO.add(SVector3d.ZERO));  // l'addition de deux nombres nuls.
    
    Assert.assertEquals(new SVector3d(1.0, 1.0, 0.0), i.add(j));
    Assert.assertEquals(new SVector3d(1.0, 1.0, 0.0), j.add(i));  // commutatif
    
    Assert.assertEquals(new SVector3d(1.0, 0.0, 1.0), i.add(k));
    Assert.assertEquals(new SVector3d(1.0, 0.0, 1.0), k.add(i));  // commutatif
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void addTest1()");
    }
  }
  
  /**
   * Test de la méthode <b>add</b> dans un cas complexe.
   */
  @Test
  public void addTest2()
  {
    try{
    
    SVector3d v1 = new SVector3d(5.0, 6.0, 7.0);
    SVector3d v2 = new SVector3d(-3.0, 2.0, -10.0);
    
    Assert.assertEquals(new SVector3d(2.0, 8.0, -3.0), v1.add(v2));
    Assert.assertEquals(new SVector3d(2.0, 8.0, -3.0), v2.add(v1)); // commutatif
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void addTest2()");
    }
  }
  
  /**
   * Test de la méthode <b>substract</b> dans des cas simples.
   */
  @Test
  public void substractTest1()
  {
    try{
      
    SVector3d i = new SVector3d(1.0, 0.0, 0.0);
    SVector3d j = new SVector3d(0.0, 1.0, 0.0);
    SVector3d k = new SVector3d(0.0, 0.0, 1.0);
    
    Assert.assertEquals(SVector3d.ZERO, SVector3d.ZERO.substract(SVector3d.ZERO));  // la soustraction de deux nombres nuls.
    
    Assert.assertEquals(new SVector3d(1.0, -1.0, 0.0), i.substract(j));
    Assert.assertEquals(new SVector3d(-1.0, 1.0, 0.0), j.substract(i));  // non commutatif
    
    Assert.assertEquals(new SVector3d(1.0, 0.0, -1.0), i.substract(k));
    Assert.assertEquals(new SVector3d(-1.0, 0.0, 1.0), k.substract(i));  // non commutatif
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void substractTest1()");
    }
  }
  
  /**
   * Test de la méthode <b>substract</b> dans un cas complexe.
   */
  @Test
  public void substractTest2()
  {
    try{
    
    SVector3d v1 = new SVector3d(5.0, 6.0, 7.0);
    SVector3d v2 = new SVector3d(-3.0, 2.0, -10.0);
    
    Assert.assertEquals(new SVector3d(8.0, 4.0, 17.0), v1.substract(v2));
    Assert.assertEquals(new SVector3d(-8.0, -4.0, -17.0), v2.substract(v1));  // non commutatif
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void substractTest2()");
    }
  }
  
  /**
   * Test de la méthode <b>multiply</b> dans un cas où il y a multiplication d'un vecteur unitaire avec un scalaire.
   */
  @Test
  public void multiplyTest1()
  {
    try{
    
      SVector3d i = new SVector3d(1.0, 0.0, 0.0);
      SVector3d j = new SVector3d(0.0, 1.0, 0.0);
      SVector3d k = new SVector3d(0.0, 0.0, 1.0);
      
      Assert.assertEquals(SVector3d.ZERO, i.multiply(0.0));   // multiplication par zéro
      Assert.assertEquals(SVector3d.ZERO, j.multiply(0.0));   // multiplication par zéro
      Assert.assertEquals(SVector3d.ZERO, k.multiply(0.0));   // multiplication par zéro
      
      Assert.assertEquals(new SVector3d(54.0, 0.0, 0.0), i.multiply(54.0));
      Assert.assertEquals(new SVector3d(0.0, -13.0, 0.0), j.multiply(-13.0));
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void multiplyTest1()");
    }
  }
  
  /**
   * Test de la méthode <b>multiply</b> dans un cas quelconque.
   */
  @Test
  public void multiplyTest2()
  {
    try{
      
      SVector3d v = new SVector3d(5.0, 10.0, -15.0);
      
      Assert.assertEquals(new SVector3d(15.0, 30.0, -45.0), v.multiply(3.0));       // scalaire positif
      Assert.assertEquals(new SVector3d(-15.0, -30.0, +45.0), v.multiply(-3.0));    // scalaire négatif
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void multiplyTest2()");
    }
  }
  
  /**
   * Test de la méthode <b>modulus</b> dans le cas d'un vecteur nul et unitaire.
   */
  @Test
  public void modulusTest1()
  {
    try{
      
      SVector3d i = new SVector3d(1.0, 0.0, 0.0);
      SVector3d j = new SVector3d(0.0, 1.0, 0.0);
      SVector3d k = new SVector3d(0.0, 0.0, 1.0);
      
      Assert.assertEquals(0.0, SVector3d.ZERO.modulus(), SMath.EPSILON);
      Assert.assertEquals(1.0, i.modulus(), SMath.EPSILON);
      Assert.assertEquals(1.0, j.modulus(), SMath.EPSILON);
      Assert.assertEquals(1.0, k.modulus(), SMath.EPSILON);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void modulusTest1()");
    }
  }
  
  /**
   * Test de la méthode <b>modulus</b> dans le cas plus général.
   */
  @Test
  public void modulusTest2()
  {
    try{
      
      SVector3d v1 = new SVector3d(2.0, 5.0, 10.0);
      SVector3d v2 = new SVector3d(-2.0, -5.0, -10.0);
      
      Assert.assertEquals(11.35781669, v1.modulus(), 1e-7);
      Assert.assertEquals(11.35781669, v2.modulus(), 1e-7);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void modulusTest2()");
    }
  }
  
  /**
   * Test de la méthode <b>dot</b> dans un cas simple.
   */
  @Test
  public void dotTest1()
  {
    try{
      
    SVector3d i = new SVector3d(1.0, 0.0, 0.0);
    SVector3d j = new SVector3d(0.0, 1.0, 0.0);
    SVector3d k = new SVector3d(0.0, 0.0, 1.0);
    
    Assert.assertEquals(i.dot(i), 1.0, SMath.EPSILON);
    Assert.assertEquals(j.dot(j), 1.0, SMath.EPSILON);
    Assert.assertEquals(k.dot(k), 1.0, SMath.EPSILON);
    
    Assert.assertEquals(i.dot(i.multiply(-1.0)), -1.0, SMath.EPSILON);
    
    Assert.assertEquals(i.dot(j), 0.0, SMath.EPSILON);
    Assert.assertEquals(i.dot(k), 0.0, SMath.EPSILON);
    Assert.assertEquals(j.dot(k), 0.0, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void dotTest1()");
    }
  }
  
  /**
   * Test de la méthode <b>cross</b> dans un cas simple.
   */
  @Test
  public void crossTest1()
  {
    try{
      
    SVector3d i = new SVector3d(1.0, 0.0, 0.0);
    SVector3d j = new SVector3d(0.0, 1.0, 0.0);
    SVector3d k = new SVector3d(0.0, 0.0, 1.0);
    
    Assert.assertEquals(i.cross(j), k);
    Assert.assertEquals(j.cross(i), k.multiply(-1.0));
    
    Assert.assertEquals(i.cross(i), SVector3d.ZERO);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void crossTest1()");
    }
  }
  
  /**
   * Test de la méthode <b>cross</b> dans le cas où a . ( a x b ) = 0 pour des vecteurs a et b quelconques.
   */
  @Test
  public void crossTest2()
  {
    try{
    
      SVector3d a = new SVector3d(1.0, 10.0, 30.0);
    SVector3d b = new SVector3d(4.0, -5.0, 6.0);
        
    Assert.assertEquals(a.dot(a.cross(b)), 0.0, SMath.EPSILON);
    Assert.assertEquals(b.dot(a.cross(b)), 0.0, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void crossTest2()");
    }
  }
  
  /**
   * Test de la méthode <b>distance</b> pour plusieurs cas quelconques.
   */
  @Test
  public void distanceTest1()
  {
    try{
      
    distanceV1andV2Test(new SVector3d(0.0, 4.0, 8.0), new SVector3d(-5.0, 3.0, -7.0));
    distanceV1andV2Test(new SVector3d(10.0, 0.0, 23.0), new SVector3d(-5.0, 23.0, -74.0));
    distanceV1andV2Test(new SVector3d(-320.0, 274.0, -17.0), new SVector3d(-15.0, 353.0, -7.0));
    distanceV1andV2Test(new SVector3d(14.0, -564.0, 16.0), new SVector3d(-15.0, 56.0, -17.0));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void distanceTest1()");
    }
  }
  
  /**
   * Méthode permettant de réaliser le test de la distance entre deux vecteurs.
   * 
   * @param v1 Le premier vecteur.
   * @param v2 Le deuxième vecteur.
   */
  private void distanceV1andV2Test(SVector3d v1, SVector3d v2)
  {
    // Test du calcul à réaliser de la distance : distance = | v2 - v1 | 
    Assert.assertEquals(v2.substract(v1).modulus(), SVector3d.distance(v1, v2), SMath.EPSILON);
    
    // Test de l'inversion : | v2 - v1 | = | v1 - v2 |
    Assert.assertEquals(SVector3d.distance(v1, v2), SVector3d.distance(v2, v1), SMath.EPSILON);
  }
  
  /**
   * Test de la méthode <b>AcrossBcrossC</b> pour un cas quelconque.
   */
  @Test
  public void AcrossBcrossCTest1() 
  {
    try{
      
    SVector3d a = new SVector3d(1.0, 10.0, 30.0);
    SVector3d b = new SVector3d(4.0, -5.0, 6.0);
    SVector3d c = new SVector3d(7.0, 8.0, -9.0);
    
    Assert.assertEquals(a.cross(b).cross(c), SVector3d.AcrossBcrossC(a, b, c));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void AcrossBcrossCTest1()");
    }
  }
 
  /**
   * Test de la méthode <b>Across_BcrossC</b> pour un cas quelconque.
   */
  @Test
  public void Across_BcrossCTest1() 
  {
    try{
    
    SVector3d a = new SVector3d(1.0, 10.0, 30.0);
    SVector3d b = new SVector3d(4.0, -5.0, 6.0);
    SVector3d c = new SVector3d(7.0, 8.0, -9.0);
    
    Assert.assertEquals(a.cross(b.cross(c)), SVector3d.Across_BcrossC(a, b, c));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void Across_BcrossCTest1()");
    }
  }
  
  /**
   * Test de la méthode <b>AdotCsubstractBdotC</b> pour un cas quelconque.
   */
  @Test
  public void AdotCsubstractBdotCTest1()
  {
    try{
    
    SVector3d a = new SVector3d(1.0, 10.0, 30.0);
    SVector3d b = new SVector3d(4.0, -5.0, 6.0);
    SVector3d c = new SVector3d(7.0, 8.0, -9.0);
    
    Assert.assertEquals(a.dot(c) - b.dot(c), SVector3d.AdotCsubstractBdotC(a, b, c), SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void AdotCsubstractBdotCTest1()");
    }
  }

  /**
   * Test de la méthode <b>AmultiplyBaddC</b> pour un cas quelconque.
   */
  @Test
  public void AmultiplyBaddCTest1()
  {
    try{
      
    double a = -10.0;
    SVector3d B = new SVector3d(4.0, -5.0, 6.0);
    SVector3d C = new SVector3d(7.0, 8.0, -9.0);
    
    Assert.assertEquals(B.multiply(a).add(C), SVector3d.AmultiplyBaddC(a,B,C));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void AmultiplyBaddCTest1()");
    }
  }
  
  /**
   * Test de la méthode <b>equals</b> pour un cas quelconque.
   */
  @Test
  public void equalsTest1() 
  {
    SVector3d v1 = new SVector3d(0.0, 1.0, 1.0);
    SVector3d v2 = new SVector3d(0.0, 1.0, 1.0);
    SVector3d v3 = new SVector3d(0.0, 1.0, 1.0000000000001);
    SVector3d v4 = new SVector3d(0.0, 1.0, 1.0000001);
    
    Assert.assertEquals(v1, v2);
    Assert.assertEquals(v1, v3);
    Assert.assertEquals(v2, v3);
    
    Assert.assertNotEquals(v1, v4);
    Assert.assertNotEquals(v2, v4);
    Assert.assertNotEquals(v3, v4);
  }

  /**
   * JUnit Test de la méthode <b>angleBetween</b> dans un scénario où les deux vecteurs sont identiques.
   */
  @Test
  public void angleBetweenTest1()
  {
    try{
    
    SVector3d v1 = new SVector3d(3.4, 5.6, 3.2);
    SVector3d v2 = v1;
    
    Assert.assertEquals(0.0, SVector3d.angleBetween(v1, v2), SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void angleBetweenTest1()");
    }
  }
  
  /**
   * JUnit Test de la méthode <b>angleBetween</b> dans un scénario où les deux vecteurs sont à 90 degré (Pi /2).
   */
  @Test
  public void angleBetweenTest2()
  {
    try{
    
    SVector3d v1 = new SVector3d(5.0, 0.0, 0.0);
    SVector3d v2 = new SVector3d(0.0, 4.0, 0.0);
    
    Assert.assertEquals(Math.PI/2, SVector3d.angleBetween(v1, v2), SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void angleBetweenTest2()");
    }
  }
  
  /**
   * JUnit Test de la méthode <b>angleBetween</b> dans un scénario tiré d'un exercice résolu (Mat - Chapitre 2.2).
   */
  @Test
  public void angleBetweenTest3()
  {
    try{
    
    SVector3d v1 = new SVector3d(3.0, 6.0, -2.0);
    SVector3d v2 = new SVector3d(-1.0, 2.0, 5.0);
    
    double expected_solution = Math.toRadians(91.49);
    
    Assert.assertEquals(expected_solution, SVector3d.angleBetween(v1, v2), 0.0001);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void angleBetweenTest3()");
    }
  }
  
  /**
   * JUnit Test de la méthode <b>normalize</b> dans un cas simple
   */
  @Test
  public void normalizeTest1()
  {
    try{
      
      SVector3d i = new SVector3d(1.0, 0.0, 0.0);
      
      SVector3d calculated_solution = i.normalize();
      
      Assert.assertEquals(i, calculated_solution);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SVector3dTest ---> Test non effectué : public void normalizeTest1()");
    }
  }
  
  //À FAIRE !!!
  
  /**
   * @param args
   */
  /*
  public static void main(String[] args) 
  {
    test1();
    test2();
  }
  */
  
  /**
   * Test #1 : Test pour vérifier la fonctionnalité de la lecture d'un string comme paramètre d'initialisation du vecteur.
   */
  /*
  private static void test1()
  {
    String s;
        
    s = "[3.4  4.5  3.2]";
    try{
    SVector3d v = new SVector3d(s);
    System.out.println(v);
    }catch(Exception e){ System.out.println(e); }
    
    s = "(3.7, 1.5, 5.2)";
    try{
    SVector3d v = new SVector3d(s);
    System.out.println(v);
    }catch(Exception e){ System.out.println(e); }
    
    s = "3.4  chien  3.8";
    try{
    SVector3d v = new SVector3d(s);
    System.out.println(v);
    }catch(Exception e){ System.out.println(e); }
    
    s = "(3.4 , 4.6 , 3.8 , 5.8)";
    try{
    SVector3d v = new SVector3d(s);
    System.out.println(v);
    }catch(Exception e){ System.out.println(e); }
    
    System.out.println();
  }
  */
  
  /**
   * Test #2 : Test pour vérifier la fonctionnalité de l'écriture du vecteur dans un fichier txt
   */
  /*
  private static void test2()
  {
    try
    {
      String file_name = "vector3dTest.txt";
      SVector3d v = new SVector3d(0.2, 0.3, 0.5);
      
      System.out.println("Écriture dans le fichier : " + file_name);
      System.out.println("Vecteur à écrire : " + v);
      System.out.println();
      
      java.io.FileWriter fw = new java.io.FileWriter(file_name);
      BufferedWriter bw = new BufferedWriter(fw);
      
      v.write(bw);
      
      bw.close(); //  fermer celui-ci en premier, sinon, ERROR !!!
      fw.close();
    }catch(Exception e){ System.out.println(e); }
    
    System.out.println();
  }
  */
  
}
