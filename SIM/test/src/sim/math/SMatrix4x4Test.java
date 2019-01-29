package sim.math;


import org.junit.Assert;
import org.junit.Test;

import sim.exception.SNoImplementationException;
import sim.util.SLog;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe <b>SMatrix4x4</b>.
 * 
 * @author Simon Vézina
 * @since 2015-08-12
 * @version 2017-08-22
 */
public class SMatrix4x4Test {

  /**
   * JUnit test permettant de vérifier la précision numérique de la matrice de rotation
   * selon l'axe x.
   */
  @Test
  public void rotationXTest()
  {
    SVector3d v0 = new SVector3d(0.0, -2.6, -8.3);
    v0 = v0.normalize();
    
    double angle = 90;
    
    SVector3d v1 = SMatrix4x4.rotationX(angle).multiply(v0).getSVector3d();
    
    Assert.assertEquals(0.0, v0.dot(v1), SMath.EPSILON);
  }
  
  /**
   * JUnit test permettant de vérifier la précision numérique de la matrice de rotation
   * selon l'axe y.
   */
  @Test
  public void rotationYTest()
  {
    SVector3d v0 = new SVector3d(5.4, 0.0, -8.3);
    v0 = v0.normalize();
    
    double angle = 90;
    
    SVector3d v1 = SMatrix4x4.rotationY(angle).multiply(v0).getSVector3d();
    
    Assert.assertEquals(0.0, v0.dot(v1), SMath.EPSILON);
  }
  
  /**
   * JUnit test permettant de vérifier la précision numérique de la matrice de rotation
   * selon l'axe z.
   */
  @Test
  public void rotationZTest()
  {
    SVector3d v0 = new SVector3d(2.4, 5.6, 0.0);
    v0 = v0.normalize();
    
    double angle = 90;
    
    SVector3d v1 = SMatrix4x4.rotationZ(angle).multiply(v0).getSVector3d();
    
    Assert.assertEquals(0.0, v0.dot(v1), SMath.EPSILON);
  }
  
  @Test
  public void RzyxTest() 
  {
    try{
    
    SVector3d v = new SVector3d(30.0, 40.0, 50.0);
    
    SMatrix4x4 matrix_expected = new SMatrix4x4(0.49240387650610407, -0.456825992585671,   0.7408430568614907,  0.0,
                                                0.5868240888334652,   0.8028723374794714,  0.10504046113295196, 0.0,
                                                -0.6427876096865393,  0.38302222155948895, 0.6634139481689384,  0.0,
                                                0.0,                  0.0,                 0.0,                 1.0);
                                             
    Assert.assertEquals(matrix_expected, SMatrix4x4.Rzyx(v));                                          
       
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SMatrix4x4Test ---> Test non effectué : public void RzyxTest()");
    }
  }

  @Test
  public void RxyzTest() 
  {
    try{
   
    SVector3d v = new SVector3d(30.0, 40.0, 50.0);
    
    SMatrix4x4 matrix_expected = new SMatrix4x4(0.49240387650610407, -0.5868240888334652,   0.6427876096865393,  0.0,
                                                0.8700019037522058,   0.31046846097336755, -0.38302222155948895, 0.0,
                                                0.025201386257487246, 0.7478280708194912,   0.6634139481689384,  0.0,
                                                0.0,                  0.0,                  0.0,                 1.0);

    Assert.assertEquals(matrix_expected, SMatrix4x4.Rxyz(v));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SMatrix4x4Test ---> Test non effectué : public void RxyzTest()");
    }
  }
  
  /**
   * JUnit test évaluant la propriété de l'identité associée au produit des matrices Rxyz et Rzyx
   * et la propriété de la commutativité dans le résultat de ce produit.
   */
  @Test
  public void testIdentity_Rxyz_Rzyx() 
  {
    try{
      
    SVector3d v = new SVector3d(30.0, 40.0, 50.0);
    SVector3d inv_v = v.multiply(-1);
    
    Assert.assertEquals(SMatrix4x4.identity(), SMatrix4x4.Rxyz(v).multiply(SMatrix4x4.Rzyx(inv_v)));
    Assert.assertEquals(SMatrix4x4.identity(), SMatrix4x4.Rzyx(v).multiply(SMatrix4x4.Rxyz(inv_v)));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SMatrix4x4Test ---> Test non effectué : public void testIdentity_Rxyz_Rzyx()");
    }
  }
  
  @Test
  public void TrRzyxScTest()
  {
    try{
      
    SVector3d scale = new SVector3d(2.3, 4.5, 7.6);
    SVector3d rotation = new SVector3d(30.0, 60.0, -15.0);
    SVector3d translation = new SVector3d(-4.5, 3.2, -5.3);
    
    SMatrix4x4 matrix_expected = new SMatrix4x4(1.1108147002324287,  2.8908090895991276,  4.52226483845811,  -4.5,
                                                -0.2976419018678989, 3.2599996637256057, -5.145786696982827,  3.2,
                                                -1.9918584287042087, 1.125,               3.2908965343808676, -5.3,
                                                0.0,                 0.0,                 0.0,                 1.0);
    
    Assert.assertEquals(matrix_expected, SMatrix4x4.TrRzyxSc(translation, rotation, scale));
   
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SMatrix4x4Test ---> Test non effectué : public void TrRzyxScTest()");
    }
  }
  
  @Test
  public void ScRxyzTrTest()
  {
    try{
      
    SVector3d scale = new SVector3d(2.3, 4.5, 7.6);
    SVector3d rotation = new SVector3d(30.0, 60.0, -15.0);
    SVector3d translation = new SVector3d(-4.5, 3.2, -5.3);
     
    SMatrix4x4 matrix_expected = new SMatrix4x4(1.1108147002324287, 0.2976419018678989, 1.9918584287042087, -14.603061737200958,
                                                0.8735142772210072, 4.268647069914666,  -1.125,              15.691356376232397,
                                                -6.489289581237268, 2.1952495828140908, 3.290896534380868,   18.7848501483542,
                                                0.0,                0.0,                0.0,                 1.0);

    
    Assert.assertEquals(matrix_expected, SMatrix4x4.ScRxyzTr(scale, rotation, translation));
    
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SMatrix4x4Test ---> Test non effectué : public void ScRxyzTrTest()");
    }
  }
  
  /**
   * JUnit test évaluant la propriété de l'identité associée au produit des matrices TrRzyxSc et ScRxyzTr
   * et la propriété de la commutativité dans le résultat de ce produit.
   */
  @Test
  public void testIdentity_TrRzyxSc_ScRxyzTr() 
  {
    try{
      
    SVector3d scale = new SVector3d(2.3, 4.5, 7.6);
    SVector3d scale_inv = new SVector3d(1.0/scale.getX(), 1.0/scale.getY(), 1.0/scale.getZ());
    
    SVector3d translation = new SVector3d(-4.5, 3.2, -5.3);
    SVector3d translation_inv = translation.multiply(-1.0);
    
    SVector3d rotation = new SVector3d(30.0, 60.0, -15.0);
    SVector3d rotation_inv = rotation.multiply(-1.0);
      
    Assert.assertEquals(SMatrix4x4.identity(), SMatrix4x4.TrRzyxSc(translation, rotation, scale).multiply(SMatrix4x4.ScRxyzTr(scale_inv, rotation_inv, translation_inv)));
    Assert.assertEquals(SMatrix4x4.identity(), SMatrix4x4.ScRxyzTr(scale, rotation, translation).multiply(SMatrix4x4.TrRzyxSc(translation_inv, rotation_inv, scale_inv)));
  
    }catch(SNoImplementationException e){
      SLog.logWriteLine("SMatrix4x4Test ---> Test non effectué : public void testIdentity_TrRzyxSc_ScRxyzTr");
    }
  }
  
  /**
   * JUnit test permettant de vérifier si la matrice de rotation de type cosine directrice
   * reproduit les matrices de rotation autour des axes fixes x, y et z. 
   */
  @Test
  public void rotationUTest1() 
  {
    SVector3d i = new SVector3d(1.0, 0.0, 0.0);
    SVector3d j = new SVector3d(0.0, 1.0, 0.0);
    SVector3d k = new SVector3d(0.0, 0.0, 1.0);
    
    double angle = 30.0;
    
    Assert.assertEquals(SMatrix4x4.rotationX(angle), SMatrix4x4.rotationU(i, angle));
    Assert.assertEquals(SMatrix4x4.rotationY(angle), SMatrix4x4.rotationU(j, angle));
    Assert.assertEquals(SMatrix4x4.rotationZ(angle), SMatrix4x4.rotationU(k, angle));
  }
  
  /**
   * JUnit test permettant de vérifier si la rotation d'un vecteur quelconque sous une
   * matrice de rotation de type cosinue directrice d'un angle de 90o est adéquate
   * avec un test de produit scalaire avec deux vecteurs orthogonaux.
   */
  @Test
  public void rotationUTest2() 
  {
    // L'axe de rotation
    SVector3d u = new SVector3d(2.3, 5.7, -2.4);
    u = u.normalize();
    
    // L'angle de rotation de 90o
    double angle = 90;
    
    // Vecteur quelconque à tourner
    SVector3d v0 = new SVector3d(-3.4, 2.3, 8.5);
    v0 = v0.cross(u).normalize();
        
    SVector3d v1 = SMatrix4x4.rotationU(u, angle).multiply(v0).getSVector3d();
    
    Assert.assertEquals(0.0, v0.dot(v1), SMath.EPSILON);
  }
  
  /**
   * JUnit test permettant de vérifier si la rotation d'un vecteur quelconque sous une
   * matrice de rotation de type cosinue directrice d'un angle quelconque
   * permet de générer un vecteur avec un angle constant par rapport à l'axe de rotation.
   */
  @Test
  public void rotationUTest3() 
  {
    // L'axe de rotation
    SVector3d u = new SVector3d(8.9, -5.4, -6.8);
    u = u.normalize();
    
    // L'angle de rotation
    double angle = 73;
    
    // Vecteur quelconque à tourner
    SVector3d v0 = new SVector3d(-3.4, 8.2, 7.4);
           
    SVector3d v1 = SMatrix4x4.rotationU(u, angle).multiply(v0).getSVector3d();
    
    Assert.assertEquals(u.dot(v0), u.dot(v1), SMath.EPSILON);
  }
  
}//fin de la classe SMatrix4x4Test
