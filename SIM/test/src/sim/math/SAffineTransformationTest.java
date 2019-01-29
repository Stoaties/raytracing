/**
 * 
 */
package sim.math;

import org.junit.Assert;
import org.junit.Test;

import sim.exception.SNoImplementationException;
import sim.util.SLog;

/**
 * JUnit test permettant de valider les fonctionnalit�s de la classe <b>SAffineTransformation</b>.
 * 
 * @author Simon V�zina
 * @since 2017-08-21
 * @version 2018-02-19
 */
public class SAffineTransformationTest {

  /**
   * JUnit Test de la m�thode <b>transformOrigin</b> dans le case d'une translation.
   */
  @Test
  public void transformOriginTest1() 
  {
    try{
      
    SMatrix4x4 m = SMatrix4x4.translation(5.0, 4.0, 3.0);
    
    SVector3d calculated_solution = SAffineTransformation.transformOrigin(m);
    
    SVector3d expected_solution = new SVector3d(5.0, 4.0, 3.0);
   
    Assert.assertEquals(expected_solution, calculated_solution);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearTransformation ---> Test non effectu� : public void transformOriginTest1()"); 
    }
  }

  /**
   * JUnit Test de la m�thode <b>transformOrigin</b> dans le case d'une rotation autour de l'axe x.
   */
  @Test
  public void transformOriginTest2() 
  {
    try{
    
      SMatrix4x4 m = SMatrix4x4.rotationX(30.0);
        
    SVector3d calculated_solution = SAffineTransformation.transformOrigin(m);
    
    SVector3d expected_solution = new SVector3d(0.0, 0.0, 0.0);
   
    Assert.assertEquals(expected_solution, calculated_solution);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearTransformation ---> Test non effectu� : public void transformOriginTest2()"); 
    }
  }
    
  /**
   * JUnit Test de la m�thode <b>transformPosition</b> d'une translation.
   */
  @Test
  public void transformPositionTest1() 
  {
    try{
      
    SMatrix4x4 m = SMatrix4x4.translation(5.0, 4.0, 3.0);
    SVector3d v = new SVector3d(2.0, -4.0, -8.0);
    
    SVector3d calculated_solution = SAffineTransformation.transformPosition(m, v);
    
    SVector3d expected_solution = new SVector3d(7.0, 0.0, -5.0);
   
    Assert.assertEquals(expected_solution, calculated_solution);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearTransformation ---> Test non effectu� : public void transformPositionTest1()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode <b>transformPosition</b> d'une rotation de 90 degr�s autour de l'axe z.
   */
  @Test
  public void transformPositionTest2() 
  {
    try{
    
    SMatrix4x4 m = SMatrix4x4.rotationZ(90.0);
    SVector3d v = new SVector3d(5.0, 0.0, 9.0);
    
    SVector3d calculated_solution = SAffineTransformation.transformPosition(m, v);
    
    SVector3d expected_solution = new SVector3d(0.0, 5.0, 9.0);
   
    Assert.assertEquals(expected_solution, calculated_solution);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearTransformation ---> Test non effectu� : public void transformPositionTest2()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode <b>transformOrientation</b> dans le cas d'une translation. L'orientation doit rest�e inchang�e.
   */
  @Test
  public void transformOrientationTest1() 
  {
    try{
    
    SMatrix4x4 m = SMatrix4x4.translation(5.0, 4.0, 3.0);
    SVector3d v = new SVector3d(2.0, -4.0, -8.0);
    
    SVector3d calculated_solution = SAffineTransformation.transformOrientation(m, v);
    
    SVector3d expected_solution = v;  // sans changement.
   
    Assert.assertEquals(expected_solution, calculated_solution);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearTransformation ---> Test non effectu� : public void transformOrientationTest1()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode <b>transformOrientation</b> d'une rotation de 90 degr�s autour de l'axe z.
   */
  @Test
  public void transformOrientationTest2() 
  {
    try{
    
    SMatrix4x4 m = SMatrix4x4.rotationZ(90.0);
    SVector3d v = new SVector3d(5.0, 0.0, 9.0);
    
    SVector3d calculated_solution = SAffineTransformation.transformOrientation(m, v);
    
    SVector3d expected_solution = new SVector3d(0.0, 5.0, 9.0);
   
    Assert.assertEquals(expected_solution, calculated_solution);
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SLinearTransformation ---> Test non effectu� : public void transformOrientationTest2()"); 
    }
  }
  
  /**
   * JUnit Test de la m�thode <b>transformNormal</b> dans le cas d'une translation. La normale doit rest�e inchang�e.
   */
  @Test
  public void transformNormalTest1() 
  {
    try{
      
      SMatrix4x4 m = SMatrix4x4.translation(5.0, 4.0, 3.0);
      SVector3d v = new SVector3d(0.0, 0.0, 1.0);
      
      SVector3d calculated_solution = SAffineTransformation.transformNormal(m, v);
      
      SVector3d expected_solution = v;  // sans changement
     
      Assert.assertEquals(expected_solution, calculated_solution);
      
      }catch(SNoImplementationException e){
        SLog.logWriteLine("SLinearTransformation ---> Test non effectu� : public void transformNormalTest1()"); 
      }
  }
  
  /**
   * JUnit Test de la m�thode <b>transformNormal</b> dans le cas d'une translation. La normale doit rest�e inchang�e, mais doit �tre normalis�e.
   */
  @Test
  public void transformNormalTest2() 
  {
    try{
      
      SMatrix4x4 m = SMatrix4x4.translation(5.0, 4.0, 3.0);
      SVector3d v = new SVector3d(5.0, -3.0, -8.0);
      
      SVector3d calculated_solution = SAffineTransformation.transformNormal(m, v);
      
      SVector3d expected_solution = v.normalize();  // sans changement, mais normalis�
     
      Assert.assertEquals(expected_solution, calculated_solution);
      
      }catch(SNoImplementationException e){
        SLog.logWriteLine("SLinearTransformation ---> Test non effectu� : public void transformNormalTest2()"); 
      }
  }
  
  /**
   * JUnit Test de la m�thode <b>transformNormal</b> dans le cas d'une rotation autour de l'axe z.
   */
  @Test
  public void transformNormalTest3() 
  {
    try{
      
      SMatrix4x4 m = SMatrix4x4.rotationZ(90.0);
      SVector3d v = new SVector3d(1.0, 0.0, 0.0);
      
      SVector3d calculated_solution = SAffineTransformation.transformNormal(m, v);
      
      SVector3d expected_solution = new SVector3d(0.0, 1.0, 0.0);
     
      Assert.assertEquals(expected_solution, calculated_solution);
      
      }catch(SNoImplementationException e){
        SLog.logWriteLine("SLinearTransformation ---> Test non effectu� : public void transformNormalTest3()"); 
      }
  }
  
  /**
   * JUnit Test de la m�thode <b>transformNormal</b> dans le cas d'une rotation autour de l'axe z. Le r�sultat doit �tre normalis�.
   */
  @Test
  public void transformNormalTest4() 
  {
    try{
      
      SMatrix4x4 m = SMatrix4x4.rotationZ(90.0);
      SVector3d v = new SVector3d(5.0, 0.0, 0.0);
      
      SVector3d calculated_solution = SAffineTransformation.transformNormal(m, v);
      
      SVector3d expected_solution = new SVector3d(0.0, 1.0, 0.0);
     
      Assert.assertEquals(expected_solution, calculated_solution);
      
      }catch(SNoImplementationException e){
        SLog.logWriteLine("SLinearTransformation ---> Test non effectu� : public void transformNormalTest4()"); 
      }
  }
  
}// fin de la classe SLinearTransformationTest
