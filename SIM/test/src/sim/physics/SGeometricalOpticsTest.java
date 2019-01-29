/**
 * 
 */
package sim.physics;

import org.junit.Assert;
import org.junit.Test;

import sim.exception.SNoImplementationException;
import sim.math.SMath;
import sim.math.SVector3d;
import sim.util.SLog;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SGeometricalOptics</b>.
 * 
 * @author Simon Vézina
 * @since 2016-03-07
 * @version 2017-06-02
 */
public class SGeometricalOpticsTest {

  /**
   * JUnit Test de la méthode <b>reflexion</b> dans un scénario où le rayon est parallèle à la normale à la surface.
   */
  @Test
  public void reflexionTest1()
  {
    try{
      
    // Test #1 : Orientation orientation parallèle à la normale à la surface
    SVector3d v = (new SVector3d(4.0, -2.0, 3.0)).normalize();
    SVector3d N = v.multiply(-1);
    
    SVector3d expected_solution = v.multiply(-1);
    
    Assert.assertEquals(expected_solution, SGeometricalOptics.reflexion(v, N));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricalOpticsTest ---> Test non effectué : public void reflexionTest1()");
    }
  }
  
  /**
   * JUnit Test de la méthode reflexion dans un scénario où on vérifie que le vecteur réfléchi est obligatoirement dans le sens de la normale à la surface.
   */
  @Test
  public void reflexionTest2()
  {
    try{
      
    SVector3d v = new SVector3d(7.0, -9.0, -3.0);
    SVector3d N = new SVector3d(-4.0, 3.0, 2.0);
      
    Assert.assertTrue(SGeometricalOptics.reflexion(v, N).dot(N) >= 0);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricalOpticsTest ---> Test non effectué : public void reflexionTest2()");
    }
  }
  
  /**
   * JUnit Test de la méthode reflexion dans un scénario où on vérifie qu'il y a respect de la loi de la réflexion theta initiale égale theta finale.
   */
  @Test
  public void reflexionTest3()
  {
    try{
      
    SVector3d v = (new SVector3d(4.0, 5.0, -3.0)).normalize();
    SVector3d N = (new SVector3d(-1.0, 2.0, 5.0)).normalize();
      
    double theta_i = SVector3d.angleBetween(v.multiply(-1.0), N);                    // Il faut mettre le vecteur v dans le sens de N
    double theta_f = SVector3d.angleBetween(SGeometricalOptics.reflexion(v, N) ,N);
    
    Assert.assertEquals(theta_i, theta_f, SMath.EPSILON);
      
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricalOpticsTest ---> Test non effectué : public void reflexionTest3()");
    }
  }
  
  @Test
  public void isTotalInternalReflectionTest1()
  {
    try{
      
    // Test #1 : Orientation parallèle à la normale
    SVector3d v = (new SVector3d(2.3, -2.5, 7.9)).normalize();
    SVector3d N = v.multiply(-1);
    
    double n1 = 1.3;
    double n2 = 1.8;
    
    Assert.assertEquals(false, SGeometricalOptics.isTotalInternalReflection(v, N, n1, n2)); 
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricalOpticsTest ---> Test non effectué : public void isTotalInternalReflectionTest1()");
    }
  }

  @Test
  public void isTotalInternalReflectionTest2()
  {
    try{
      
    // Test #2 : Orientation quelconque de v et N
    SVector3d v = (new SVector3d(2.3, -2.5, 0.0)).normalize();
    SVector3d N = (new SVector3d(1.1, 2.9, 0.0)).normalize();
    
    double n1 = 1.3;
    double n2 = 1.8;
    
    // n1 < n2 : impossible 
    Assert.assertEquals(false, SGeometricalOptics.isTotalInternalReflection(v, N, n1, n2)); 
    
    // n1 > n2 : possible (dans ce cas ... il y a réflexion totale interne)
    Assert.assertEquals(true, SGeometricalOptics.isTotalInternalReflection(v, N, n2, n1));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricalOpticsTest ---> Test non effectué : public void isTotalInternalReflectionTest2()");
    }
  }
  
  @Test
  public void isTotalInternalReflectionTest3()
  {
    try{
      
    // Test #3 : Orientation quelconque de v et N
    SVector3d v = (new SVector3d(-1.8, -0.7, 0.0)).normalize();
    SVector3d N = (new SVector3d(2.8, 3.1, 0.0)).normalize();
    
    double n1 = 1.3;
    double n2 = 1.7;
    
    // n1 < n2 : impossible 
    Assert.assertEquals(false, SGeometricalOptics.isTotalInternalReflection(v, N, n1, n2)); 
    
    // n1 > n2 : possible (dans ce cas ... pas de réflexion totale interne)
    Assert.assertEquals(false, SGeometricalOptics.isTotalInternalReflection(v, N, n2, n1));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricalOpticsTest ---> Test non effectué : public void isTotalInternalReflectionTest3()");
    }
  }
  
  @Test
  public void refractionTest1()
  {
    try{
    
    // Test #1 : Orientation parallèle à la normale
    SVector3d v = (new SVector3d(2.3, -2.5, 7.9)).normalize();
    SVector3d N = v.multiply(-1);
    
    double n1 = 1.3;
    double n2 = 1.8;
    
    Assert.assertEquals(v, SGeometricalOptics.refraction(v, N, n1, n2));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricalOpticsTest ---> Test non effectué : public void refractionTest1()");
    }
  }
  
  @Test
  public void refractionTest2()
  {
    try{
      
    // Test #2 : Orientation quelconque de v et N
    SVector3d v = (new SVector3d(2.3, -2.5, 0.0)).normalize();
    SVector3d N = (new SVector3d(1.1, 2.9, 0.0)).normalize();
    
    double n1 = 1.3;
    double n2 = 1.8;
    
    Assert.assertEquals(new SVector3d(0.3329180135506553, -0.942955776403902, 0.0), SGeometricalOptics.refraction(v, N, n1, n2));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricalOpticsTest ---> Test non effectué : public void refractionTest2()");
    }
  }
  
  @Test
  public void refractionTest3()
  {
    try{
      
    // Test #3 : Orientation quelconque de v et N
    SVector3d v = (new SVector3d(-1.8, -0.7, 0.0)).normalize();
    SVector3d N = (new SVector3d(2.8, 3.1, 0.0)).normalize();
    
    double n1 = 1.3;
    double n2 = 1.7;
    
    // Tester les deux combinaisons de n1 et n2 donnant deux réfractions distinctes
    Assert.assertEquals(new SVector3d(-0.8842263948388124, -0.4670585430867921, 0.0), SGeometricalOptics.refraction(v, N, n1, n2));
    Assert.assertEquals(new SVector3d(-0.9782082673419917, -0.20762607183053589, 0.0), SGeometricalOptics.refraction(v, N, n2, n1));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SGeometricalOpticsTest ---> Test non effectué : public void refractionTest3()");
    }
  }
  
}//fin de la classe SGeometricalOptics
