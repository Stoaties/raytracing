/**
 * 
 */
package sim.geometry;

import org.junit.Assert;
import org.junit.Test;

import sim.exception.SNoImplementationException;
import sim.math.SVector3d;
import sim.util.SLog;

/**
 * JUnit test permettant de valider les fonctionnalit�s de la classe <b>SGeometricIntersection</b>.
 * 
 * @author Simon V�zina
 * @since 2016-03-09
 * @version 2017-03-02
 */
public class SGeometricIntersectionTest {

  /**
   * Test de l'intersection entre un rayon et un plan o� le rayon est initialement sur le plan. Le temps d'intersection est alors �gale � z�ro.
   */
  @Test
  public void test_planeIntersection_1()
  {
    try{
      
      // Un point du plan
      SVector3d r_p = new SVector3d(-1.0, 3.0, -7.0);
      
      // La base du plan
      SVector3d u1 = new SVector3d(7.0, 3.0, 2.0);
      SVector3d u2 = new SVector3d(-4.0, 2.0, -1.0);
      
      // La normale � la surface du plan
      SVector3d n_p = u1.cross(u2).normalize();
      
      // Origine du rayon (sur le plan)
      SVector3d r0 = r_p.add(u1.multiply(3.0)).add(u2.multiply(6.0));
      
      // Orientation du rayon (quelconque)
      SVector3d v = new SVector3d(8.0, -9.0, 2.0);
      
      // Le calcul � tester
      SRay ray = new SRay(r0, v, 1.0);
      
      double[] calculated_solution = SGeometricIntersection.planeIntersection(ray, r_p, n_p);
      
      double[] expected_solution = { 0.0 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_planeIntersection_1()");
    }
  }
  
  /**
   * Test de l'intersection entre un rayon et un plan o� le rayon o� le temps de l'intersection est positif
   */
  @Test
  public void test_planeIntersection_2()
  {
    try{
      
      // Un point du plan
      SVector3d r_p = new SVector3d(2.0, 2.0, 5.0);
      
      // La normale � la surface du plan
      SVector3d n_p = new SVector3d(1.0, 1.0, 1.0).normalize();
      
      // Origine du rayon 
      SVector3d r0 = new SVector3d(4.0, 4.0, 10.0);
      
      // Orientation du rayon 
      SVector3d v = new SVector3d(-1.0, 0.0, -2.0);
      
      // Le calcul � tester
      SRay ray = new SRay(r0, v, 1.0);
      
      double[] calculated_solution = SGeometricIntersection.planeIntersection(ray, r_p, n_p);
      
      double[] expected_solution = { 3.0 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_planeIntersection_2()");
    }
  }
  
  /**
   * Test de l'intersection entre un rayon et un plan o� le rayon o� le temps de l'intersection est n�gatif.
   */
  @Test
  public void test_planeIntersection_3()
  {
    try{
      
      // Un point du plan
      SVector3d r_p = new SVector3d(2.0, 2.0, 5.0);
      
      // La normale � la surface du plan
      SVector3d n_p = new SVector3d(1.0, 1.0, 1.0).normalize();
      
      // Origine du rayon 
      SVector3d r0 = new SVector3d(0.0, 2.0, -15.0);
      
      // Orientation du rayon 
      SVector3d v = new SVector3d(-1.0, 0.0, -2.0);
      
      // Le calcul � tester
      SRay ray = new SRay(r0, v, 1.0);
      
      double[] calculated_solution = SGeometricIntersection.planeIntersection(ray, r_p, n_p);
      
      double[] expected_solution = { -7.333333333333333 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_planeIntersection_3()");
    }
  }
  
  /**
   * Test de l'intersection entre un rayon � l'origine et une sph�re � l'origine.
   */
  @Test
  public void test_sphereIntersection_1()
  {
    try{
      
      SRay ray = new SRay(new SVector3d(0.0, 0.0, 0.0), new SVector3d(1.0, 0.0, 0.0), 1.0);
      
      double[] calculated_solution = SGeometricIntersection.sphereIntersection(ray, new SVector3d(0.0, 0.0, 0.0), 1.0);
      
      double[] expected_solution = { -1.0, 1.0 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
            
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_sphereIntersection_1()");
    }
    
  }

  /**
   * Test de l'intersection <u>sans succ�s</u> entre un rayon et une sph�re.
   */
  @Test
  public void test_sphereIntersection_2()
  {
    try{
      
      SRay ray = new SRay(new SVector3d(2.0, 3.0, 4.0), new SVector3d(-1.0, -7.0, 1.0), 1.0);
      
      double[] calculated_solution = SGeometricIntersection.sphereIntersection(ray, new SVector3d(7.0, 2.0, 3.0), 3.0);
      
      double[] expected_solution = { };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
            
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_sphereIntersection_2()");
    }
    
  }
  
  /**
   * Test de l'intersection <u>avec 2 succ�s positifs</u> entre un rayon et une sph�re dans un cas quelconque.
   */
  @Test
  public void test_sphereIntersection_3()
  {
    try{
      
      SRay ray = new SRay(new SVector3d(-2.0, 3.0, 7.0), new SVector3d(-2.0, 4.0, 3.0), 1.0);
      
      double[] calculated_solution = SGeometricIntersection.sphereIntersection(ray, new SVector3d(-5.0, 8.0, 12.0), 3.0);
      
      double[] expected_solution = { 0.8897005464285977, 1.937885660467954 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_sphereIntersection_3()");
    }
    
  }
  
  /**
   * Test de l'intersection <u>avec 1 succ�s n�gatif et 1 succ�s positif </u> entre un rayon et une sph�re dans un cas quelconque.
   */
  @Test
  public void test_sphereIntersection_4()
  {
    try{
      
      SRay ray = new SRay(new SVector3d(-2.0, 3.0, 7.0), new SVector3d(-2.0, 4.0, 3.0), 1.0);
      
      double[] calculated_solution = SGeometricIntersection.sphereIntersection(ray, new SVector3d(2.0, 8.0, 12.0), 9.0);
      
      double[] expected_solution = { -0.24542911098151582, 2.107498076498757 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_sphereIntersection_4()");
    }
    
  }
  
  /**
   * Test de l'intersection <u>avec 2 succ�s n�gatif</u> entre un rayon et une sph�re dans un cas quelconque.
   */
  @Test
  public void test_sphereIntersection_5()
  {
    try{
      
      SRay ray = new SRay(new SVector3d(1.0, 1.0, 1.0), new SVector3d(1.0, 2.0, 3.0), 1.0);
      
      double[] calculated_solution = SGeometricIntersection.sphereIntersection(ray, new SVector3d(-1.0, -1.0, -1.0), 3.0);
      
      double[] expected_solution = { -1.5785360670258626 , -0.1357496472598516 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_sphereIntersection_5()");
    }
    
  }
  
  /**
   * Test de l'intersection entre un rayon � l'origine se d�pla�ant en x et le tube align� selon l'axe z.
   */
  @Test
  public void test_infiniteTubeIntersection_1()
  {
    try{
      
      // Information sur le tube
      SVector3d r_tube = new SVector3d(0.0, 0.0, -1.0);
      SVector3d axis = new SVector3d(0.0, 0.0, 1.0);
      double R = 1.0;
      
      // Information sur le rayon
      SVector3d r0 = new SVector3d(0.0, 0.0, 0.0);
      SVector3d v = new SVector3d(1.0, 0.0, 0.0);
      SRay ray = new SRay(r0, v, 1.0);
      
      double[] calculated_solution = SGeometricIntersection.infiniteTubeIntersection(ray, r_tube, axis, R);
      
      double[] expected_solution = { -1.0, 1.0 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_infiniteTubeIntersection_1()");
    }
  }
  
  /**
   * Test de l'intersection <u>avec 2 succ�s positif</u> entre un rayon et le tube.
   */
  @Test
  public void test_infiniteTubeIntersection_2()
  {
    try{
      
      // Information sur le tube
      SVector3d r_tube1 = new SVector3d(5.0, 5.0, -2.0);
      SVector3d r_tube2 = new SVector3d(6.0, 7.0, 10.0);
      double R = 3.0;
      
      SVector3d axis = r_tube2.substract(r_tube1).normalize();
      
      // Information sur le rayon
      SRay ray = new SRay(new SVector3d(1.0, 2.0, 3.0), new SVector3d(2.0, 2.0, 1.0), 1.0);
      
      double[] calculated_solution = SGeometricIntersection.infiniteTubeIntersection(ray, r_tube1, axis, R);
      
      double[] expected_solution = { 1.0603359106580434, 3.346743735359655 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_infiniteTubeIntersection_2()");
    }
  }
  
  /**
   * Test de l'intersection <u>avec 2 succ�s n�gatif</u> entre un rayon et le tube.
   */
  @Test
  public void test_infiniteTubeIntersection_3()
  {
    try{
      
      // Information sur le tube
      SVector3d r_tube1 = new SVector3d(5.0, 5.0, -2.0);
      SVector3d r_tube2 = new SVector3d(6.0, 7.0, 10.0);
      double R = 3.0;
      
      SVector3d axis = r_tube2.substract(r_tube1).normalize();
      
      // Information sur le rayon
      SRay ray = new SRay(new SVector3d(1.0, 2.0, 3.0), new SVector3d(-2.0, -2.0, -1.0), 1.0);
      
      double[] calculated_solution = SGeometricIntersection.infiniteTubeIntersection(ray, r_tube1, axis, R);
      
      double[] expected_solution = { -3.346743735359655, -1.0603359106580434 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_infiniteTubeIntersection_3()");
    }
  }
  
  /**
   * Test de l'intersection <u>avec z�ro et 1 succ�s positif</u> entre un rayon et le tube. Le rayon part de la surface du tube. 
   */
  @Test
  public void test_infiniteTubeIntersection_4()
  {
    try{
      
      // Information sur le tube
      SVector3d r_tube1 = new SVector3d(5.0, 5.0, -2.0);
      SVector3d r_tube2 =  new SVector3d(5.0, 5.0, 2.0);
      double R = 3.0;
      
      SVector3d axis = r_tube2.substract(r_tube1).normalize();
      
      // Information sur le rayon
      SRay ray = new SRay(new SVector3d(5.0, 2.0, 0.0), new SVector3d(0.0, 2.0, 0.0), 1.0);
      
      double[] calculated_solution = SGeometricIntersection.infiniteTubeIntersection(ray, r_tube1, axis, R);
      
      double[] expected_solution = { 0.0, 3.0 };
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_infiniteTubeIntersection_4()");
    }
  }
  
  /**
   * Test de l'intersection entre un rayon pr�s de l'origine voyageant dans le plan xy et un c�ne parall�le � l'axe z dont la base est dans le plan xy et la pointe est � l'origine.
   * Les donn�es choisies donnent aucune intersection.
   */
  @Test
  public void test_infiniteTwoConeIntersection_1()
  {
    try{
      
      // Informatin sur le c�ne
      SVector3d r_cone1 = new SVector3d(0.0, 0.0, 2.0);
      SVector3d r_cone2 =  new SVector3d(0.0, 0.0, 0.0);
      double R = 3.0;
      
      SVector3d axis = r_cone2.substract(r_cone1);
      double H = axis.modulus();
      axis = axis.normalize();
      
      // Information sur le rayon
      SRay ray = new SRay(new SVector3d(1.0, 0.0, 0.0), new SVector3d(0.0, 1.0, 0.0), 1.0);
      
      double[] calculated_solution = SGeometricIntersection.infiniteTwoConeIntersection(ray, r_cone1, axis, R, H);
    		  
      double[] expected_solution = {} ;
      
      // R�aliser le test
      testingValuesSolutions(expected_solution, calculated_solution, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_infiniteTwoConeIntersection_1()");
    }
    
  }
  
  /**
   * Test de l'intersection entre un rayon voyageant selon l'axe z et un tore situ� � l'origine dans le plan xy.
   * Dans les multiples sc�narios test�s, il n'y aura pas d'intersection.
   * Ce test est effectu� avec un tore R = 1.0 et r = 0.2 .
   */
  @Test
  public void test_torusIntersection_1a()
  {
  	try{
        
  		// Informatin sur le tors
  		SVector3d r_torus = new SVector3d(0.0, 0.0, 0.0);
  		SVector3d n_torus = new SVector3d(0.0, 0.0, 1.0);
  		double R = 1.0;
  		double r = 0.2;
  		
  		// Solution attendue sans intersection
  	  double[] expected_solution = { } ;
  	  
  	  // Sc�nario #1
      SRay ray1 = new SRay(new SVector3d(0.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0), 1.0);
      double[] calculated_solution1 = SGeometricIntersection.torusIntersection(ray1, r_torus, n_torus, R, r);
  	  testingValuesSolutions(expected_solution, calculated_solution1, SRay.getEpsilon());
        
  	  // Sc�nario #2
      SRay ray2 = new SRay(new SVector3d(-1.4, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0), 1.0);
      double[] calculated_solution2 = SGeometricIntersection.torusIntersection(ray2, r_torus, n_torus, R, r);
      testingValuesSolutions(expected_solution, calculated_solution2, SRay.getEpsilon());
        
      // Sc�nario #3
      SRay ray3 = new SRay(new SVector3d(1.4, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0), 1.0);
      double[] calculated_solution3 = SGeometricIntersection.torusIntersection(ray3, r_torus, n_torus, R, r);
      testingValuesSolutions(expected_solution, calculated_solution3, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_torusIntersection_1a()");
    }
  }
  
  /**
   * Test de l'intersection entre un rayon voyageant selon l'axe z et un tore situ� � l'origine dans le plan xy.
   * Dans les multiples sc�narios test�s, il n'y aura pas d'intersection.
   */
  @Test
  public void test_torusIntersection_1b()
  {
    try{
        
      // Informatin sur le tors
      SVector3d r_torus = new SVector3d(0.0, 0.0, 0.0);   // c'est ce changement qui modifie le polyn�me du 4i�me degr� et qui me permet d'obtenir la bonne solution attendue.
      SVector3d n_torus = new SVector3d(0.0, 0.0, 1.0);
      double R = 3.0;
      double r = 0.2;
      
      // Solution attendue sans intersection
      double[] expected_solution = { } ;
      
      // Sc�nario #1
      SRay ray1 = new SRay(new SVector3d(0.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0), 1.0);
      double[] calculated_solution1 = SGeometricIntersection.torusIntersection(ray1, r_torus, n_torus, R, r);
      testingValuesSolutions(expected_solution, calculated_solution1, SRay.getEpsilon());
        
      // Sc�nario #2
      SRay ray2 = new SRay(new SVector3d(1.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0), 1.0);
      double[] calculated_solution2 = SGeometricIntersection.torusIntersection(ray2, r_torus, n_torus, R, r);
      testingValuesSolutions(expected_solution, calculated_solution2, SRay.getEpsilon());
        
      // Sc�nario #3
      SRay ray3 = new SRay(new SVector3d(5.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0), 1.0);
      double[] calculated_solution3 = SGeometricIntersection.torusIntersection(ray3, r_torus, n_torus, R, r);
      testingValuesSolutions(expected_solution, calculated_solution3, SRay.getEpsilon());
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_torusIntersection_1b()");
    }
  }
  
  /**
   * Test de l'intersection entre un rayon voyageant selon l'axe x et un tore situ� � l'origine dans le plan xy.
   * Dans les multiples sc�narios test�s, il y aura plusieurs.
   * Ce test est effectu� avec un tore R = 1.0 et r = 0.2 .
   */
  @Test
  public void test_torusIntersection_2a()
  {
    try{
        
      // Informatin sur le tore
      SVector3d r_torus = new SVector3d(0.0, 0.0, 0.0);
      SVector3d n_torus = new SVector3d(0.0, 0.0, 1.0);
      double R = 1.0;
      double r = 0.2;
      
      // Sc�nario #1
      double[] expected_solution1 = { 0.0, 0.4, 2.0, 2.4 } ;
      SRay ray1 = new SRay(new SVector3d(-1.2, 0.0, 0.0), new SVector3d(1.0, 0.0, 0.0), 1.0);
      double[] calculated_solution1 = SGeometricIntersection.torusIntersection(ray1, r_torus, n_torus, R, r);
      testingValuesSolutions(expected_solution1, calculated_solution1, SRay.getEpsilon());
             
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_torusIntersection_2a()");
    }
  }
  
  /**
   * Test de l'intersection entre un rayon voyageant selon l'axe x et un tore situ� � l'origine dans le plan xy.
   * Dans les multiples sc�narios test�s, il y aura plusieurs.
   * Ce test est effectu� avec un tore R = 3.0 et r = 1.0 .
   */
  @Test
  public void test_torusIntersection_2b()
  {
    try{
        
      // Informatin sur le tore
      SVector3d r_torus = new SVector3d(0.0, 0.0, 0.0);
      SVector3d n_torus = new SVector3d(0.0, 0.0, 1.0);
      double R = 3.0;
      double r = 1.0;
      
      // Sc�nario #1
      double[] expected_solution1 = { 1.0, 3.0, 7.0, 9.0 } ;
      SRay ray1 = new SRay(new SVector3d(-5.0, 0.0, 0.0), new SVector3d(1.0, 0.0, 0.0), 1.0);
      double[] calculated_solution1 = SGeometricIntersection.torusIntersection(ray1, r_torus, n_torus, R, r);
      testingValuesSolutions(expected_solution1, calculated_solution1, SRay.getEpsilon());
             
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_torusIntersection_2b()");
    }
  }
  
  /**
   * Test de l'intersection entre un rayon voyageant selon l'axe z et un tore situ� � l'origine dans le plan xy.
   * Dans les multiples sc�narios test�s, il y aura plusieurs.
   * Ce test est effectu� avec un tore R = 1.0 et r = 0.2 .
   */
  @Test
  public void test_torusIntersection_3a()
  {
    try{
        
      // Informatin sur le tore
      SVector3d r_torus = new SVector3d(0.0, 0.0, 0.0);
      SVector3d n_torus = new SVector3d(0.0, 0.0, 1.0);
      double R = 1.0;
      double r = 0.2;
      
      // Sc�nario #1
      double[] expected_solution1 = { -0.2, 0.2 } ;
      SRay ray1 = new SRay(new SVector3d(1.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0), 1.0);
      double[] calculated_solution1 = SGeometricIntersection.torusIntersection(ray1, r_torus, n_torus, R, r);
      testingValuesSolutions(expected_solution1, calculated_solution1, SRay.getEpsilon());
             
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_torusIntersection_3a()");
    }
  }
  
  /**
   * Test de l'intersection entre un rayon voyageant selon l'axe z et un tore situ� � l'origine dans le plan xy.
   * Dans les multiples sc�narios test�s, il y aura plusieurs.
   * Ce test est effectu� avec un tore R quelconque, mais r = 0.2 .
   */
  @Test
  public void test_torusIntersection_3b()
  {
    try{
        
      // Informatin sur le tore
      SVector3d r_torus = new SVector3d(0.0, 0.0, 0.0);
      SVector3d n_torus = new SVector3d(0.0, 0.0, 1.0);
      double R = 2.0;
      double r = 0.2;
      
      // Sc�nario #1
      double[] expected_solution1 = { -0.2, 0.2 } ;
      SRay ray1 = new SRay(new SVector3d(2.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0), 1.0);
      double[] calculated_solution1 = SGeometricIntersection.torusIntersection(ray1, r_torus, n_torus, R, r);
      testingValuesSolutions(expected_solution1, calculated_solution1, SRay.getEpsilon());
             
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricIntersectionTest ---> Test non effectu� : public void test_torusIntersection_3b()");
    }
  }
  
  //----------------------
  // M�THODES UTILITAIRES 
  //----------------------
  
  /**
   * M�thode pour faire la v�rification du nombre de solutions, de l'ordre des solutions et de leurs valeurs.
   * 
   * @param expected_solution Le tableau des solutions attendues.
   * @param calculated_solution Le tableau des solutions calcul�es.
   * @param epsilon Le niveau de pr�cision des solutions.
   */
  private void testingValuesSolutions(double[] expected_solution, double[] calculated_solution, double epsilon)
  {
    // V�rifier le nombre de solutions
    Assert.assertEquals(expected_solution.length, calculated_solution.length);  
    
    // V�rifier la valeur et l'ordre des solutions avec la taille du tableau attendue
    for(int i = 0; i < expected_solution.length; i++)
      Assert.assertEquals(expected_solution[i], calculated_solution[i], epsilon);
  }
  
}//fin de la classe SGeometricIntersectionTest
