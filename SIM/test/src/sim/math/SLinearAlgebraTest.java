/**
 * 
 */
package sim.math;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import sim.exception.SNoImplementationException;
import sim.util.SLog;

/**
 * JUnit test permettant de valider les fonctionnalit�s de la classe <b>SLinearAlgebra</b>.
 * 
 * @author Simon V�zina
 * @since 2016-12-15
 * @version 2017-12-18
 */
public class SLinearAlgebraTest {

  /**
   * JUnit Test de la m�thode planNormal dans le cas le plus simple o� le r�sultat est de module �gal � 1. Le test permet de v�rifier le respect de la r�gle de la main droite (l'ordre des points du triangle).
   */
  @Test
  public void planNormalTest1()
  {
    try{
      
      SVector3d r0 = new SVector3d(0.0, 0.0, 0.0);
      SVector3d r1 = new SVector3d(1.0, 0.0, 0.0); 
      SVector3d r2 = new SVector3d(0.0, 1.0, 0.0);
      
      // 1ier test : ordre r0-r1-r2
      SVector3d calculated = SLinearAlgebra.planNormal(r0, r1, r2);
      SVector3d expected = new SVector3d(0.0, 0.0, 1.0);
       
      Assert.assertEquals(expected, calculated);
   
      // 2i�me test : ordre r2-r1-r0
      calculated = SLinearAlgebra.planNormal(r2, r1, r0);
      expected = new SVector3d(0.0, 0.0, -1.0);
      
      Assert.assertEquals(expected, calculated);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void planNormalTest1()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode planNormal dans le cas o� le vecteur poss�de une longueur nulle.
   */
  @Test
  public void planNormalTest2()
  {
    try{
      
      SVector3d r0 = new SVector3d(0.0, 0.0, 0.0);
      SVector3d r1 = new SVector3d(4.0, 4.0, 0.0); 
      SVector3d r2 = new SVector3d(8.0, 8.0, 0.0);
      
      SVector3d calculated = SLinearAlgebra.planNormal(r0, r1, r2);
      SVector3d expected = new SVector3d(0.0, 0.0, 0.0);
      
      Assert.assertEquals(expected, calculated);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void planNormalTest2()"); 
    }
  }
  /**
   * JUnit Test de la m�thode planNormal dans le cas le plus simple o� le r�sultat n'est pas de module �gal � 1.
   */
  @Test
  public void planNormalTest3()
  {
    try{
      
      SVector3d r0 = new SVector3d(0.0, 0.0, 0.0);
      SVector3d r1 = new SVector3d(5.0, 0.0, 0.0); 
      SVector3d r2 = new SVector3d(0.0, 4.0, 0.0);
      
      // 1ier test : ordre r0-r1-r2
      SVector3d calculated = SLinearAlgebra.planNormal(r0, r1, r2);
      SVector3d expected = new SVector3d(0.0, 0.0, 20.0);
       
      Assert.assertEquals(expected, calculated);
   
      // 2i�me test : ordre r2-r1-r1
      calculated = SLinearAlgebra.planNormal(r2, r1, r0);
      expected = new SVector3d(0.0, 0.0, -20.0);
      
      Assert.assertEquals(expected, calculated);  
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void planNormalTest3()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode planNormal dans un cas quelconque. Le test v�rifie que l'ordre des points r0-r1-r2 donne le m�me r�sultat que l'ordre des points r1-r2-r0 et r2-r0-r1.
   */
  @Test
  public void planNormalTest4()
  {
    try{
      
    SVector3d r0 = new SVector3d(5.0, -7.0, 12.0);
    SVector3d r1 = new SVector3d(-4.0, 9.0, -11.0); 
    SVector3d r2 = new SVector3d(6.0, -5.0, 2.0);
    
    SVector3d expected = new SVector3d(-114.0, -113.0, -34.0);
    
    // V�rifier dans l'ordre ro-r1-r2     
    Assert.assertEquals(expected, SLinearAlgebra.planNormal(r0, r1, r2));
    
    // V�rifier dans l'ordre r1-r2-r0   
    Assert.assertEquals(expected, SLinearAlgebra.planNormal(r1, r2, r0));
    
    // V�rifier dans l'ordre r2-r0-r1   
    Assert.assertEquals(expected, SLinearAlgebra.planNormal(r2, r0, r1));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void planNormalTest4()"); 
    }
    
  }
  
 /**
   * JUnit Test de la m�thode normalizedPlanNormal dans le cas le plus simple. Le test permet de v�rifier le respect de la r�gle de la main droite (l'ordre des points du triangle) ainsi que le module du vecteur est unitaire.
   */
  @Test
  public void normalizedPlanNormalTest1()
  {
    try{
      
      SVector3d r0 = new SVector3d(0.0, 0.0, 0.0);
      SVector3d r1 = new SVector3d(5.0, 0.0, 0.0); 
      SVector3d r2 = new SVector3d(0.0, 4.0, 0.0);
      
      // 1ier test : ordre r0-r1-r2
      SVector3d calculated = SLinearAlgebra.normalizedPlanNormal(r0, r1, r2);
      SVector3d expected = new SVector3d(0.0, 0.0, 1.0);
       
      Assert.assertEquals(expected, calculated);
   
      // 2i�me test : ordre r2-r1-r1
      calculated = SLinearAlgebra.normalizedPlanNormal(r2, r1, r0);
      expected = new SVector3d(0.0, 0.0, -1.0);
      
      Assert.assertEquals(expected, calculated);  
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void normalizedPlanNormalTest1()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode normalizedPlanNormal dans un cas o� le vecteur est ind�termin�. Une exception doit �tre lanc�e.
   */ 
  @Test
  public void normalizedPlanNormalTest2()
  {
    try{
      
      SVector3d r0 = new SVector3d(0.0, 0.0, 0.0);
      SVector3d r1 = new SVector3d(4.0, 4.0, 0.0); 
      SVector3d r2 = new SVector3d(8.0, 8.0, 0.0);
      
      try{
        SVector3d expected = SLinearAlgebra.normalizedPlanNormal(r0, r1, r2);
        fail("Le test est en �chec, car les point r0 = " + r0 + ", r1 = " + r1 + " et r2 = " + r2 + " sont colin�aire et la normale � la surface n'est pas d�finie. Ainsi le r�sultat n = " + expected + " est mal calcul�.");
      }catch(SColinearException e){
        // L'exception doit �tre lanc�e. 
      }
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void normalizedPlanNormalTest2()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode normalizedPlanNormal dans un cas quelconque. Le test v�rifie que l'ordre des points r0-r1-r2 donne le m�me r�sultat que l'ordre des points r1-r2-r0 et r2-r0-r1.
   */
  @Test
  public void normalizedPlanNormalTest3()
  {
    try{
      
      SVector3d r0 = new SVector3d(5.0, -7.0, 12.0);
      SVector3d r1 = new SVector3d(-4.0, 9.0, -11.0); 
      SVector3d r2 = new SVector3d(6.0, -5.0, 2.0);
      
      SVector3d expected = new SVector3d(-0.6947991161621961, -0.68870438707354, -0.20722078903083044);
      
      // V�rifier dans l'ordre ro-r1-r2     
      Assert.assertEquals(expected, SLinearAlgebra.normalizedPlanNormal(r0, r1, r2));
      
      // V�rifier dans l'ordre r1-r2-r0   
      Assert.assertEquals(expected, SLinearAlgebra.normalizedPlanNormal(r1, r2, r0));
      
      // V�rifier dans l'ordre r2-r0-r1   
      Assert.assertEquals(expected, SLinearAlgebra.normalizedPlanNormal(r2, r0, r1));
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void normalizedPlanNormalTest3()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode triangleBarycentricCoordinates dans le cas d'un triangle (0,0,0) , (1,0,0) et (0,1,0). Les coordonn�es �valu�es sont au sommet du triangle.
   */
  @Test
  public void triangleBarycentricCoordinatesTest1()
  {
    try{
      
      SVector3d P0 = new SVector3d(0.0, 0.0, 0.0);
      SVector3d P1 = new SVector3d(1.0, 0.0, 0.0);
      SVector3d P2 = new SVector3d(0.0, 1.0, 0.0);
        
      double[] expected_1 = { 0.0, 0.0 };
      Assert.assertArrayEquals( expected_1, SLinearAlgebra.triangleBarycentricCoordinates(P0, P1, P2, P0), SMath.EPSILON);
      
      double[] expected_2 = { 1.0, 0.0 };
      Assert.assertArrayEquals( expected_2, SLinearAlgebra.triangleBarycentricCoordinates(P0, P1, P2, P1), SMath.EPSILON);
      
      double[] expected_3 = { 0.0, 1.0 };
      Assert.assertArrayEquals( expected_3, SLinearAlgebra.triangleBarycentricCoordinates(P0, P1, P2, P2), SMath.EPSILON);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void triangleBarycentricCoordinatesTest1()"); 
    } 
  }
  
  /**
   * JUnit Test de la m�thode triangleBarycentricCoordinates dans le cas d'un triangle (0,0,0) , (1,0,0) et (0,1,0). Les coordonn�es �valu�es sont au sommet du triangle, mais avec une coordonn�e en z (ils ne sont pas dans le plan du triangle).
   */
  @Test
  public void triangleBarycentricCoordinatesTest2()
  {
    try{
      
      SVector3d P0 = new SVector3d(0.0, 0.0, 0.0);
      SVector3d P1 = new SVector3d(1.0, 0.0, 0.0);
      SVector3d P2 = new SVector3d(0.0, 1.0, 0.0);
        
      double[] expected_1 = { 0.0, 0.0 };
      Assert.assertArrayEquals( expected_1, SLinearAlgebra.triangleBarycentricCoordinates(P0, P1, P2, P0.add(new SVector3d(0.0, 0.0, 2.0))), SMath.EPSILON);
      
      double[] expected_2 = { 1.0, 0.0 };
      Assert.assertArrayEquals( expected_2, SLinearAlgebra.triangleBarycentricCoordinates(P0, P1, P2, P1.add(new SVector3d(0.0, 0.0, 2.0))), SMath.EPSILON);
      
      double[] expected_3 = { 0.0, 1.0 };
      Assert.assertArrayEquals( expected_3, SLinearAlgebra.triangleBarycentricCoordinates(P0, P1, P2, P2.add(new SVector3d(0.0, 0.0, 2.0))), SMath.EPSILON);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void triangleBarycentricCoordinatesTest2()"); 
    } 
  }
  
  /**
   * JUnit Test de la m�thode triangleBarycentricCoordinates dans le cas d'un triangle quelconque dans le plan xy.
   */
  @Test
  public void triangleBarycentricCoordinatesTest3()
  {
    try{
      
      SVector3d P0 = new SVector3d(7.0, -4.0, 0.0);
      SVector3d P1 = new SVector3d(4.0, 3.0, 0.0);
      SVector3d P2 = new SVector3d(-2.0, -3.0, 0.0);
          
      // Test avec la position r1 (� l'int�rieur du triangle).
      SVector3d r1 = new SVector3d(4.0, -1.0, 0.0);
      double[] expected_1 = { 0.4, 0.2 };
      Assert.assertArrayEquals( expected_1, SLinearAlgebra.triangleBarycentricCoordinates(P0, P1, P2, r1), SMath.EPSILON);
      
      // Test avec la position r2 (� l'ext�rieur du triangle).
      SVector3d r2 = new SVector3d(5.0, 2.0, 0.0);
      double[] expected_2 = { 0.866666666666667, -0.066666666666667 };
      Assert.assertArrayEquals( expected_2, SLinearAlgebra.triangleBarycentricCoordinates(P0, P1, P2, r2), SMath.EPSILON);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void triangleBarycentricCoordinatesTest3()"); 
    } 
  }
  
  /**
   * JUnit Test de la m�thode triangleBarycentricCoordinates dans le cas d'un triangle quelconque.
   */
  @Test
  public void triangleBarycentricCoordinatesTest4()
  {
    try{
      
      SVector3d P0 = new SVector3d(7.0, -4.0, 12.0);
      SVector3d P1 = new SVector3d(-4.0, 3.0, 7.0);
      SVector3d P2 = new SVector3d(-2.0, -3.0, -8.0);
          
      // Test avec un point quelconque (qui sera � l'ext�rieur du triangle).
      SVector3d r1 = new SVector3d(12.0, 17.0, -30.0);
      double[] expected_1 = { -0.43705629049152345, 1.8797377507079955 };
      Assert.assertArrayEquals( expected_1, SLinearAlgebra.triangleBarycentricCoordinates(P0, P1, P2, r1), SMath.EPSILON);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void triangleBarycentricCoordinatesTest4()"); 
    } 
  }
  
  /**
   * JUnit Test de la m�thode isBarycentricCoordinatesInsideTriangle dans plusieurs cas vari�s.
   */
  @Test
  public void isBarycentricCoordinatesInsideTriangleTest1()
  {
    try{
      
      // Cas #1 : Coordonn�e sup�rieur � 1.
      double[] tab1 = { 2.3, 4.5 };
      Assert.assertFalse(SLinearAlgebra.isBarycentricCoordinatesInsideTriangle(tab1));
      
      // Cas #2 : Coordonn�e inf�rieur � 0.
      double[] tab2 = { 0.3, -1.5 };
      Assert.assertFalse(SLinearAlgebra.isBarycentricCoordinatesInsideTriangle(tab2));
      
      // Cas #3 : Coordonn�e � z�ro.
      double[] tab3 = { 0.0, 0.5 };
      Assert.assertTrue(SLinearAlgebra.isBarycentricCoordinatesInsideTriangle(tab3));
      
      // Cas #4 : Coordonn�e de somme �gale � 1.
      double[] tab4 = { 0.35, 0.65 };
      Assert.assertTrue(SLinearAlgebra.isBarycentricCoordinatesInsideTriangle(tab4));
      
      // Cas #5 : Coordonn�e de somme inf�rieur � 1
      double[] tab5 = { 0.25, 0.35 };
      Assert.assertTrue(SLinearAlgebra.isBarycentricCoordinatesInsideTriangle(tab5));
      
      // Cas #6 : Coordonn�e de somme inf�rieur � 1, mais avec valeur invalide.
      double[] tab6 = { -0.25, 0.45 };
      Assert.assertFalse(SLinearAlgebra.isBarycentricCoordinatesInsideTriangle(tab6));
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void isBarycentricCoordinatesInsideTriangleTest1()"); 
    } 
  }
  
  @Test
  public void isCoplanarTest1()
  {
    try{
      
      // Construction al�atoire des quatres points ce qui en r�sulte � ne pas �tre coplanaire
      SVector3d p0 = new SVector3d(12.0, -4.5, 23.3);
      SVector3d p1 = new SVector3d(22.0, -14.9, -13.3); 
      SVector3d p2 = new SVector3d(17.0, -24.3, 53.3);
      SVector3d p3 = new SVector3d(-45.0, -43.5, -20.2); 
      
      // Test des quatre points al�atoire qui seront pas coplanaires
      Assert.assertFalse(SLinearAlgebra.isCoplanar(p0, p1, p2, p3));
   
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void isCoplanarTest1()"); 
    } 
  }
  
  @Test
  public void isCoplanarTest2()
  {
    try{
    
      // Un vecteur de base
      SVector3d v1 = new SVector3d(2.1, 3.4, -2.3);
          
      // Construction de trois points p0, p1 et p2 sont colin�aires et l'autre point al�atoire 
      SVector3d p0 = v1;
      SVector3d p1 = v1.multiply(2.0);
      SVector3d p2 = v1.multiply(-7.2);
      
      SVector3d p3 = new SVector3d(91.3, 7.8, -34.7);
      
      // Test des quatre points dans la m�me base qui seront coplanaires
      Assert.assertTrue(SLinearAlgebra.isCoplanar(p0, p1, p2, p3));
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void isCoplanarTest2()"); 
    } 
  }
  
  @Test
  public void isCollinearTest1()
  {
    try{
    
      // Vecteurs de base
      SVector3d v1 = new SVector3d(2.1, 3.4, -2.3);
          
      // Construction de trois points p0, p1 et p2 sont colin�aires
      SVector3d p0 = v1;
      SVector3d p1 = v1.multiply(2.0);
      SVector3d p2 = v1.multiply(-7.2);
      
      // Test des trois points colin�aire
      Assert.assertTrue(SLinearAlgebra.isCollinear(p0, p1, p2));
     
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void isColinearTest1()"); 
    } 
  }
  
  @Test
  public void isCollinearTest2()
  {
    try{
    
      // Construction al�atoire des trois points ce qui en r�sulte � ne pas �tre colin�aire
      SVector3d p0 = new SVector3d(12.0, -4.5, 23.3);
      SVector3d p1 = new SVector3d(22.0, -14.9, -13.3); 
      SVector3d p2 = new SVector3d(17.0, -24.3, 53.3);
      
      // Test des trois points colin�aire
      Assert.assertFalse(SLinearAlgebra.isCollinear(p0, p1, p2));
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void isColinearTest2()"); 
    }   
  }
  
  @Test
  public void isCollinearTest3()
  {
    try{
      
      // Construction deux point identiques et un troisi�me point quelconque et ils seront colin�aire.
      SVector3d p0 = new SVector3d(45.7, 23.5, -7.9);
      SVector3d p1 = p0;
      
      SVector3d p2 = new SVector3d(23.5, -34.6, -23.1);
      
      // Test des trois points colin�aire
      Assert.assertTrue(SLinearAlgebra.isCollinear(p0, p1, p2));
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void isColinearTest3()"); 
    } 
  }
  
  @Test
  public void coplanarEdgeEdgeIntersectionTest1()
  {
    try{
      
      // Constructeur de deux droites tr�s simples.
      SVector3d p0 = new SVector3d(0.0, 0.0, 0.0);
      SVector3d p1 = new SVector3d(1.0, 0.0, 0.0);
      
      SVector3d p2 = new SVector3d(0.5, 1.0, 0.0);
      SVector3d p3 = new SVector3d(0.5, -1.0, 0.0);
      
      Assert.assertEquals(new SVector3d(0.5, 0.0, 0.0), SLinearAlgebra.coplanarEdgeEdgeIntersection(p0, p1, p2, p3));
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void coplanarEdgeEdgeIntersectionTest1()"); 
    } 
  }
  
  @Test
  public void coplanarEdgeEdgeIntersectionTest2()
  {
    try{
      
      SVector3d expected = new SVector3d(67.3, 23.4, -89.9);
      
      // Deux vecteurs repr�sentant l'axe des droites
      SVector3d axis1 = new SVector3d(2.1, 3.4, -2.3);
      SVector3d axis2 = new SVector3d(22.1, -83.4, -92.9);
      
      // Construction des quatres points al�atoirement avec les deux axes pour les deux droites.
      SVector3d p0 = expected.add(axis1.multiply(3.4));
      SVector3d p1 = expected.add(axis1.multiply(-17.4));
      
      SVector3d p2 = expected.add(axis2.multiply(7.8));
      SVector3d p3 = expected.add(axis2.multiply(-37.5));
      
      Assert.assertEquals(expected, SLinearAlgebra.coplanarEdgeEdgeIntersection(p0, p1, p2, p3)); 
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void coplanarEdgeEdgeIntersectionTest2()"); 
    } 
  }
  
  @Test
  public void coplanarEdgeEdgeIntersectionTest3()
  {
    try{
      
    // Construction al�atoire des quatre points ce qui en r�sulte � ne pas �tre colin�aire et donc pas d'intersection.
    SVector3d p0 = new SVector3d(12.0, -4.5, 23.3);
    SVector3d p1 = new SVector3d(22.0, -14.9, -13.3); 
    SVector3d p2 = new SVector3d(17.0, -24.3, 53.3); 
    SVector3d p3 = new SVector3d(-56.0, -14.3, 25.7);
    
    try{
      
      SVector3d error = SLinearAlgebra.coplanarEdgeEdgeIntersection(p0, p1, p2, p3);
      error.normalize();  // pour retirer le warning !
      
    }catch(SNoCoplanarException e){
      // C'est un succ�s !
    }
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void coplanarEdgeEdgeIntersectionTest3()"); 
    } 
  }
  
  /**
   * Test des points quelconques. Il n'y aura pas d'intersection des deux droites.
   */
  @Test
  public void isEdgeEdgeIntersectionTest1()
  {
    try{
      
      // Construction al�atoire des quatre points ce qui en r�sulte � ne pas �tre colin�aire et donc pas d'intersection.
      SVector3d p0 = new SVector3d(12.0, -4.5, 23.3);
      SVector3d p1 = new SVector3d(22.0, -14.9, -13.3); 
      SVector3d p2 = new SVector3d(17.0, -24.3, 53.3); 
      SVector3d p3 = new SVector3d(-56.0, -14.3, 25.7);
      
      Assert.assertFalse(SLinearAlgebra.isEdgeEdgeIntersection(p0, p1, p2, p3));
     
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void isEdgeEdgeIntersectionTest1()"); 
    }
  }
   
  /**
   * JUnit Test de la m�thode timeToCollinear dans le cas o� les points sont intialement colin�aire et ils sont immobiles.
   * La solution attendue sera une infinit� de temps. 
   */
  @Test
  public void timeToCollinearTest1()
  {
    try{
      
    // Consid�rons trois points colin�aire le long de l'axe y.
    SVector3d r0 = new SVector3d(4.5, 4.0, 3.2);
    SVector3d r1 = new SVector3d(4.5, 5.0, 3.2);
    SVector3d r2 = new SVector3d(4.5, 6.0, 3.2);
    
    SVector3d v = new SVector3d(0.0, 0.0, 0.0);
    
    // La solution attendue sera une infinit� de solutions.      
    try {
      double[] times = SLinearAlgebra.timeToCollinear(r0, v, r1, v, r2, v);
      fail("Ce test devrait avoir un nombre infini de solutions. Pr�sentement, la 1i�re solution invalide est " + times[0] + ".");
    }catch(SInfinityOfSolutionsException e) {
      // c'est un succ�s.
    }
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCollinearTest1()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode timeToCollinear dans le cas o� les points sont intialement colin�aire et qu'ils se d�placent avec la m�me vitesse.
   * La solution attendue sera une infinit� de temps. 
   */
  @Test
  public void timeToCollinearTest2()
  {
    try{
      
    // Consid�rons trois points colin�aire le long de l'axe y.
    SVector3d r0 = new SVector3d(4.5, 4.0, 3.2);
    SVector3d r1 = new SVector3d(4.5, 5.0, 3.2);
    SVector3d r2 = new SVector3d(4.5, 6.0, 3.2);
    
    SVector3d v = new SVector3d(2.0, 3.0, 4.0);
    
    // La solution attendue sera une infinit� de solutions.      
    try {
      double[] times = SLinearAlgebra.timeToCollinear(r0, v, r1, v, r2, v);
      fail("Ce test devrait avoir un nombre infini de solutions. Pr�sentement, la 1i�re solution invalide est " + times[0] + ".");
    }catch(SInfinityOfSolutionsException e) {
      // c'est un succ�s.
    }
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCollinearTest2()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode timeToCollinear dans le cas g�n�ral dans le plan xy.
   * Les trois points vont s'aligner sur la droite x = 3.
   */
  @Test
  public void timeToCollinearTest3()
  {
    try{
      
    // Consid�rons trois points qui vont devenir coplanaire gr�ce au mouvement de deux points.
    SVector3d r0 = new SVector3d(0.0, 0.0, 0.0);
    SVector3d r1 = new SVector3d(1.0, 2.0, 0.0);
    SVector3d r2 = new SVector3d(4.0, 4.0, 0.0);
    
    SVector3d v0 = new SVector3d(3.0, 0.0, 0.0);
    SVector3d v1 = new SVector3d(2.0, 0.0, 0.0);  
    SVector3d v2 = new SVector3d(-1.0, 0.0, 0.0);  
    
    double[] calculated_solution = SLinearAlgebra.timeToCollinear(r0, v0, r1, v1, r2, v2);
    double[] expected_solution = {1.0};
       
    Assert.assertArrayEquals(expected_solution, calculated_solution, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCollinearTest3()"); 
    }
  }
 
  /**
   * JUnit Test de la m�thode timeToCollinear dans le cas o� les trois points sont quelconques et se d�placent dans des directions quelconques.
   * Le r�sultat attendu sera qu'ils ne seront jamais colin�aire.
   */
  @Test
  public void timeToCollinearTest4()
  {
    try{
      
    // Consid�rons trois points qui vont devenir coplanaire gr�ce au mouvement du point au central.
    SVector3d r0 = new SVector3d(12.0, 45.0, 21.0);
    SVector3d r1 = new SVector3d(-13.0, -3.0, 12.0);
    SVector3d r2 = new SVector3d(25.0, -26.0, 14.0);
    
    SVector3d v0 = new SVector3d(56.0, 23.0, 12.0);
    SVector3d v1 = new SVector3d(-36.0, 33.0, 17.0);
    SVector3d v2 = new SVector3d(-16.0, -43.0, -21.0);
    
    double[] calculated_solution = SLinearAlgebra.timeToCollinear(r0, v0, r1, v1, r2, v2);
    double[] expected_solution = {};
    
    Assert.assertArrayEquals(expected_solution, calculated_solution, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCollinearTest4()"); 
    }
  }

  /**
   * JUnit Test de la m�thode timeToCollinear dans le cas G�N�RAL o� les trois points d�termin� sont colin�aire le long d'un axe quelconque � un instant t donn�e, 
   * mais que l'on va d�placer � un temps ult�rieur t = 0 afin de v�rifier si le test d�terminera t comme temps pour la colin�arit�.
   * Le r�sultat attendu sera le temps t choisi. 
   */
  @Test
  public void timeToCollinearTest5()
  {
    try{
    
    // Le point de r�f�rence et le d�placement formant l'axe de colin�arit�.
    SVector3d r_ini = new SVector3d(3.4, 8.7, 5.6);
    SVector3d axis = new SVector3d(2.3, 6.7, -4.5);
    
    // Consid�rons trois points colin�aire le long de l'axe initiale
    SVector3d r0 = r_ini;
    SVector3d r1 = r_ini.add(axis.multiply(4.5));
    SVector3d r2 = r_ini.add(axis.multiply(-7.5));
    
    // Voici les trois vitesses choisies al�atoirement.
    SVector3d v0 = new SVector3d(56.0, 23.0, 12.0);
    SVector3d v1 = new SVector3d(-36.0, 33.0, 17.0);
    SVector3d v2 = new SVector3d(-16.0, -43.0, -21.0);
    
    // Le temps al�atoire choisi
    double t = 4.2;
    
    // Le recule des positions des points au temps t = 0.
    r0 = r0.substract(v0.multiply(t));
    r1 = r1.substract(v1.multiply(t));
    r2 = r2.substract(v2.multiply(t));
  
    // Effectuer le calcul du temps de colin�arit�.
    double[] calculated_solution = SLinearAlgebra.timeToCollinear(r0, v0, r1, v1, r2, v2);
    double[] expected_solution = { t };
    
    Assert.assertArrayEquals(expected_solution, calculated_solution, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCollinearTest5()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode timeToCollinear dans le cas PARTICULIER o� les trois points d�termin� sont colin�aire le long d'un axe y � un instant t donn�e, 
   * mais que l'on va d�placer � un temps t = 0 afin de v�rifier si le test d�terminera t comme temps pour la colin�arit�.
   * Le r�sultat attendu sera le temps t choisi. 
   */
  @Test
  public void timeToCollinearTest6a()
  {
    try{
    
    // Consid�rons trois points colin�aire le long de l'axe y.
    SVector3d r0 = new SVector3d(4.5, 4.0, 3.2);
    SVector3d r1 = new SVector3d(4.5, 5.0, 3.2);
    SVector3d r2 = new SVector3d(4.5, 6.0, 3.2);
    
    // Voici les trois vitesses choisies al�atoirement.
    SVector3d v0 = new SVector3d(56.0, 23.0, 12.0);
    SVector3d v1 = new SVector3d(-36.0, 33.0, 17.0);
    SVector3d v2 = new SVector3d(-16.0, -43.0, -21.0);
    
    // Le temps al�atoire choisi
    double t = 8.4;
    
    // Le recule des positions des points au temps t = 0.
    r0 = r0.substract(v0.multiply(t));
    r1 = r1.substract(v1.multiply(t));
    r2 = r2.substract(v2.multiply(t));
  
    // Effectuer le calcul du temps de colin�arit�.
    double[] calculated_solution = SLinearAlgebra.timeToCollinear(r0, v0, r1, v1, r2, v2);
    double[] expected_solution = { t };
    
    Assert.assertArrayEquals(expected_solution, calculated_solution, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCollinearTest6a()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode timeToCollinear dans le cas PARTICULIER IDENTIQUE AU PR�C�DENT.
   * Ce test permet de v�rifier le <u>taux de tol�rence dans la comparaison des temps</u>.
   * 
   * Dans ce test, il faut accepter une diff�rence de 1e-5 sur les temps. 
   */
  @Test
  public void timeToCollinearTest6b()
  {
    try{
      
    // Consid�rons trois points colin�aire le long de l'axe y.
    SVector3d r0 = new SVector3d(4.5, 4.0, 3.2);
    SVector3d r1 = new SVector3d(4.5, 5.0, 3.2);
    SVector3d r2 = new SVector3d(4.5, 6.0, 3.2);
    
    // Voici les trois vitesses choisies al�atoirement.
    SVector3d v0 = new SVector3d(56.0, 23.0, 12.0);
    SVector3d v1 = new SVector3d(-36.0, 33.0, 17.0);
    SVector3d v2 = new SVector3d(-16.0, -43.0, -21.0);
    
    double t = 5.9;
    
    // Le recule des positions des points au temps t = 0.
    r0 = r0.substract(v0.multiply(t));
    r1 = r1.substract(v1.multiply(t));
    r2 = r2.substract(v2.multiply(t));
      
    // Effectuer le calcul du temps de colin�arit�.
    double[] calculated_solution = SLinearAlgebra.timeToCollinear(r0, v0, r1, v1, r2, v2);
    double[] expected_solution = { t };
    
    Assert.assertArrayEquals(expected_solution, calculated_solution, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCollinearTest6b()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode timeToCollinear dans le cas PARTICULIER IDENTIQUE AU PR�C�DENT.
   * Ce test permet de v�rifier le <u>taux de tol�rence au solution complexe</u>.
   * 
   * L'impl�mentation de cet algorithme n�cessite la r�solution d'un polyn�me du 2i�me degr�.
   * Pour des raisons num�rique, le discriminant devient tr�s l�g�rement n�gatif ce qui g�n�re une solution de temps complexe.
   * Puisque la partie imaginaire est extr�mement faible, nous pouvons toll�rer de retirer cette composante dans l'analyse des temps.
   */
  @Test
  public void timeToCollinearTest6c()
  {
    try{
      
    // Consid�rons trois points colin�aire le long de l'axe y.
    SVector3d r0 = new SVector3d(4.5, 4.0, 3.2);
    SVector3d r1 = new SVector3d(4.5, 5.0, 3.2);
    SVector3d r2 = new SVector3d(4.5, 6.0, 3.2);
    
    // Voici les trois vitesses choisies al�atoirement.
    SVector3d v0 = new SVector3d(56.0, 23.0, 12.0);
    SVector3d v1 = new SVector3d(-36.0, 33.0, 17.0);
    SVector3d v2 = new SVector3d(-16.0, -43.0, -21.0);
    
    double t = 2.1;
    
    // Le recule des positions des points au temps t = 0.
    r0 = r0.substract(v0.multiply(t));
    r1 = r1.substract(v1.multiply(t));
    r2 = r2.substract(v2.multiply(t));
      
    // Effectuer le calcul du temps de colin�arit�.
    double[] calculated_solution = SLinearAlgebra.timeToCollinear(r0, v0, r1, v1, r2, v2);
    double[] expected_solution = { t };
    
    Assert.assertArrayEquals(expected_solution, calculated_solution, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCollinearTest6c()"); 
    }
  }

  /**
   * JUnit Test de la m�thode timeToCollinear dans le cas PARTICULIER IDENTIQUE AU PR�C�DENT.
   * Dans ce test, on v�rifie si un alignement selon l'axe x sera �galement support�.
   */
  @Test
  public void timeToCollinearTest7d()
  {
    try{
      
    // Consid�rons trois points colin�aire le long de l'axe x.
    SVector3d r0 = new SVector3d(7.5, 4.0, 3.2);
    SVector3d r1 = new SVector3d(6.5, 4.0, 3.2);
    SVector3d r2 = new SVector3d(5.5, 4.0, 3.2);
    
    // Voici les trois vitesses choisies al�atoirement.
    SVector3d v0 = new SVector3d(56.0, 23.0, 12.0);
    SVector3d v1 = new SVector3d(-36.0, 33.0, 17.0);
    SVector3d v2 = new SVector3d(-16.0, -43.0, -21.0);
    
    // Le temps al�atoire choisi : 
    double t = 1.1;
    
    // Le recule des positions des points au temps t = 0.
    r0 = r0.substract(v0.multiply(t));
    r1 = r1.substract(v1.multiply(t));
    r2 = r2.substract(v2.multiply(t));
      
    // Effectuer le calcul du temps de colin�arit�.
    double[] calculated_solution = SLinearAlgebra.timeToCollinear(r0, v0, r1, v1, r2, v2);
    double[] expected_solution = { 1.0873380447585395 , t };    // Le 1ier temps n'�tait pas pr�visible. Il a �t� g�n�r� avec l'algorithme.
    
    Assert.assertArrayEquals(expected_solution, calculated_solution, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCollinearTest7d()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode timeToCoplanar dans le cas PARTICULIER o� tous les points sont coplanaires et immobiles.
   * La solution attentue sera une infinit� de solution (donc une exception).
   */
  @Test
  public void timeToCoplanarTest1()
  {
    try{
      
    // Consid�rons trois points colin�aire le long de l'axe xy � z = 5.0.
    SVector3d r0 = new SVector3d(4.5, 4.0, 5.0);
    SVector3d r1 = new SVector3d(2.8, 3.0, 5.0);
    SVector3d r2 = new SVector3d(-2.5, -3.0, 5.0);
    SVector3d r3 = new SVector3d(12.5, -13.0, 5.0);
    
    SVector3d v = SVector3d.ZERO;
    
    try{
      double[] solutions = SLinearAlgebra.timeToCoplanar(r0, v, r1, v, r2, v, r3, v);
      fail("Ce test est en �chec, car il devrait g�n�rer une infinit� de solutions. Pr�sentement, la 1i�re solution invalide est " + solutions[0] + ".");
    }catch(SInfinityOfSolutionsException e){
      // C'est un succ�s.
    }
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCoplanarTest1()"); 
    } 
  }
  
  /**
   * JUnit Test de la m�thode timeToCoplanar dans le cas PARTICULIER o� tous un point va se d�placer parall�lement � la surface form�e par les trois autres points.
   * La solution attentue sera aucun temps admissible.
   */
  @Test
  public void timeToCoplanarTest2()
  {
    try{
    
    // Consid�rons trois points colin�aire le long de l'axe xy � z = 5.0.
    SVector3d r0 = new SVector3d(4.5, 4.0, 5.0);
    SVector3d r1 = new SVector3d(2.8, 3.0, 5.0);
    SVector3d r2 = new SVector3d(-2.5, -3.0, 5.0);
    SVector3d r3 = new SVector3d(12.5, -13.0, 7.0);  
    
    SVector3d v = SVector3d.ZERO;
    SVector3d v3 = new SVector3d(4.5, -4.5, 0.0);
    
    double[] calculated_solution = SLinearAlgebra.timeToCoplanar(r0, v, r1, v, r2, v, r3, v3);
    double[] expected_solution = { };
    
    Assert.assertArrayEquals(expected_solution, calculated_solution, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCoplanarTest2()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode timeToCoplanar dans le cas PARTICULIER o� tous un point va se d�placer perpendiculairement � la surface form�e par les trois autres points.
   * La solution attentue sera une temps de d�placement pour que le 4i�me point puisse atteindre le plan des trois autres points.
   */
  @Test
  public void timeToCoplanarTest3()
  {
    try{
    
    // Consid�rons trois points colin�aire le long de l'axe xy � z = 5.0.
    SVector3d r0 = new SVector3d(4.5, 4.0, 5.0);
    SVector3d r1 = new SVector3d(2.8, 3.0, 5.0);
    SVector3d r2 = new SVector3d(-2.5, -3.0, 5.0);
    SVector3d r3 = new SVector3d(12.5, -13.0, 7.0);  
    
    SVector3d v = SVector3d.ZERO;
    SVector3d v3 = new SVector3d(2.4, -4.5, -0.5);    // � cette vitesse, puisqu'il y aura 2 unit�s � parcourir, le temps requis sera 4.
    
    double[] calculated_solution = SLinearAlgebra.timeToCoplanar(r0, v, r1, v, r2, v, r3, v3);
    double[] expected_solution = { 4.0 };
    
    Assert.assertArrayEquals(expected_solution, calculated_solution, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCoplanarTest3()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode timeToCoplanar dans un cas g�n�ral. Nous allons � partir d'un point de r�f�rence placer 4 points dans un m�me plan � un temps t = 6.9.
   * Nous allons reculer ces particules avec des vitesse quelconque � un temps t et v�rifier qu'ils seront coplanaire au temps t choisi.
   * Dans ce test, il y a trois temps admissible. Le temps t = 6.9 �tait pr�visible, mais pas les deux autres solutions.
   */
  @Test
  public void timeToCoplanarTest4()
  {
    try{
      
    // La position d'origine.
    SVector3d r_ini = new SVector3d(5.6, 4.3, 6.7);
    
    // Les vecteur de la base du plan.
    SVector3d dx = new SVector3d(3.2, -7.5, -9.2);
    SVector3d dy = new SVector3d(1.2, -3.8, -3.7);
    
    // Les 4 points au temps t dans un m�me plan.
    SVector3d r0 = r_ini.add(dx.multiply(4.8)).add(dy.multiply(3.8));
    SVector3d r1 = r_ini.add(dx.multiply(6.4)).add(dy.multiply(1.3));
    SVector3d r2 = r_ini.add(dx.multiply(2.5)).add(dy.multiply(3.9));
    SVector3d r3 = r_ini.add(dx.multiply(9.2)).add(dy.multiply(2.8));
    
    // V�rifions que les 4 points sont bel et bien coplanaire.
    Assert.assertTrue(SLinearAlgebra.isCoplanar(r0, r1, r2, r3));
    
    // Les vitesses des 4 points.
    SVector3d v0 = new SVector3d(3.5, 9.4, -2.1);
    SVector3d v1 = new SVector3d(-2.9, -2.9, -12.1);
    SVector3d v2 = new SVector3d(-7.2, -7.6, 7.1);
    SVector3d v3 = new SVector3d(5.7, 3.4, -3.5);
    
    // Le temps pour �tre coplanaire.
    double t = 6.9;
    
    // Remettre les points au temps t = 0;
    r0 = r0.substract(v0.multiply(t));
    r1 = r1.substract(v1.multiply(t));
    r2 = r2.substract(v2.multiply(t));
    r3 = r3.substract(v3.multiply(t));
    
    double[] calculated_solution = SLinearAlgebra.timeToCoplanar(r0, v0, r1, v1, r2, v2, r3, v3);
    double[] expected_solution = { 1.1109677011068304 , 6.8883489468382795 , t };   // les deux autres temps ont �t� obtenus avec l'algorithme.
    
    Assert.assertArrayEquals(expected_solution, calculated_solution, SMath.EPSILON);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearAlgebraTest ---> Test non effectu� : public void timeToCoplanarTest4()"); 
    }
  }
  
}//fin de la classe SLinearAlgebraTest
