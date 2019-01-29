/**
 * 
 */
package sim.physics;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sim.math.SMath;
import sim.math.SVector3d;

/**
 * JUnit test permettant de valider les fonctionnalités de la classe SWaveOpticsTest.
 * 
 * @author Simon Vézina
 * @since 2016-02-14
 * @version 2016-03-27
 */
public class SWaveOpticsTest {

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public void test1_interferenceAverageWaveValue()
  {
    // Test du maximum en interférence sur l'axe qui passe par le centre des deux oscillateurs
    double wave_lenght = 555e-9;
    
    List<SOscillator> list = new ArrayList<SOscillator>();
    
    list.add( new SOscillator( new SVector3d(0.0, 0.001, 0.0),  SWaveOptics.waveLenghtToFrequency(wave_lenght, SPhysics.c)));
    list.add( new SOscillator( new SVector3d(0.0, -0.001, 0.0), SWaveOptics.waveLenghtToFrequency(wave_lenght, SPhysics.c)));
    
    SVector3d P = new SVector3d(5.0, 0.0, 0.0);
    
    List<SWave> wave_list = new ArrayList<SWave>();
    
    for(SOscillator o : list)
      wave_list.add(new SWave(o, SPhysics.c));
    
    double period = wave_list.get(0).getOscillator().getPeriod();
    
    double value = SWaveOptics.interferenceAverageWaveValue(wave_list, P, period, 1000);
    
    Assert.assertEquals(2.0, value, SMath.EPSILON);
  }

  @Test
  public void test2_interferenceAverageWaveValue()
  {
    // Test : Chapitre 3.2, Situation 1 (interférence constructive)
    double wave_lenght = 500e-9;
    
    List<SOscillator> list = new ArrayList<SOscillator>();
   
    list.add( new SOscillator( new SVector3d(0.0, 0.0005, 0.0),  SWaveOptics.waveLenghtToFrequency(wave_lenght, SPhysics.c)));
    list.add( new SOscillator(new SVector3d(0.0, -0.0005, 0.0), SWaveOptics.waveLenghtToFrequency(wave_lenght, SPhysics.c)));
    
    SVector3d P = new SVector3d(3.0, 0.003, 0.0);
    
    List<SWave> wave_list = new ArrayList<SWave>();
    
    for(SOscillator o : list)
      wave_list.add(new SWave(o, SPhysics.c));
    
    double period = wave_list.get(0).getOscillator().getPeriod();
    
    double value = SWaveOptics.interferenceAverageWaveValue(wave_list, P, period, 1000);
    
    Assert.assertEquals(2.0, value, SMath.EPSILON);
  }
  
  @Test
  public void test3_interferenceAverageWaveValue()
  {
    // Test : Chapitre 3.2, Situation 1 (interférence destructive)
    double wave_lenght = 500e-9;
    
    List<SOscillator> list = new ArrayList<SOscillator>();
    
    list.add( new SOscillator(new SVector3d(0.0, 0.0005, 0.0),  SWaveOptics.waveLenghtToFrequency(wave_lenght, SPhysics.c)));
    list.add( new SOscillator(new SVector3d(0.0, -0.0005, 0.0), SWaveOptics.waveLenghtToFrequency(wave_lenght, SPhysics.c)));
    
    SVector3d P = new SVector3d(3.0, 0.00225, 0.0);
    
    List<SWave> wave_list = new ArrayList<SWave>();
    
    for(SOscillator o : list)
      wave_list.add(new SWave(o, SPhysics.c));
    
    double period = wave_list.get(0).getOscillator().getPeriod();
    
    double value = SWaveOptics.interferenceAverageWaveValue(wave_list, P, period, 1000);
    
    Assert.assertEquals(0.0, value, SMath.EPSILON);
  }
  
  @Test
  public void test4_interferenceAverageWaveValue()
  {
    // Test : Test de la moyenne à un seul oscillateur
    double wave_lenght = 500e-9;
    
    List<SOscillator> list = new ArrayList<SOscillator>();
    
    list.add( new SOscillator(new SVector3d(0.0, 0.0, 0.0),  SWaveOptics.waveLenghtToFrequency(wave_lenght, SPhysics.c)));
    
    SVector3d P = new SVector3d(5.0, 6.0, 17.0);  // position quelconque
    
    List<SWave> wave_list = new ArrayList<SWave>();
    
    for(SOscillator o : list)
      wave_list.add(new SWave(o, SPhysics.c));
    
    double period = wave_list.get(0).getOscillator().getPeriod();
    
    double value = SWaveOptics.interferenceAverageWaveValue(wave_list, P, period, 1000);
    
    Assert.assertEquals(0.5, value, SMath.EPSILON);
  }
  
  @Test
  public void test1_phaseBetweenZeroAnd2Pi()
  {
    double phase = 547.976821645636346346346363;
    
    // Nous avons ici une bonne précision avec une petite phase
    Assert.assertEquals(Math.sin(phase), Math.sin(SWaveOptics.phaseBetweenZeroAnd2Pi(phase)), 1e-12);
  }
  
  @Test
  public void test2_phaseBetweenZeroAnd2Pi()
  {
    // Une phase calculé avec une longueur d'onde de 500 nm
    double phase = 2*Math.PI/500e-9*1.8764653223456345;
    
    // Nous avons ici une faible précision avec une grosse phase
    Assert.assertEquals(Math.sin(phase), Math.sin(SWaveOptics.phaseBetweenZeroAnd2Pi(phase)), 1e-8);
  }
  
}//fin de la classe SWaveOpticsTest
