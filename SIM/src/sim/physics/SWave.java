/**
 * 
 */
package sim.physics;

import sim.exception.SConstructorException;
import sim.math.SVector3d;

/**
 * La classe <b>SWave</b> représente une onde physique généré par un oscillateur harmonique simple.
 * La forme de l'onde sera alors une onde sinusoïdale dont la fréquence et la phase initiale seront déterminées par son oscillateur.
 * La longueur d'onde l'onde sera déterminée par la vitesse de propagation du milieu et la fréquence de l'oscillateur. 
 * Dans le cas d'une onde électromagnétique voyageant dans le vide, la vitesse de propagation sera égale à <i>c</i>.
 * 
 * @author Simon Vézina
 * @since 2016-02-22
 * @version 2016-02-23
 */
public class SWave {

  /**
   * La variable <b>oscillator</b> correspond à l'oscillateur qui génère l'onde .
   */
  private final SOscillator oscillator;
  /**
   * La variable <b>wave_speed</b> correspond à la vitesse de propagation des ondes dans le milieu par défaut.
   */
  private final double wave_speed;
    
  /**
   * La variable <b>position</b> correspond à la position où l'onde doit être évaluée.
   */
  private SVector3d position;
  
  //--------------------------
  // Paramètre de précalcul //
  //--------------------------
  
  /**
   * La variable <b>k</b> correspond au nombre d'onde de l'onde.
   */
  private final double k;
  
  /**
   * La variable <b>kx</b> correspond à la phase spatiale de l'onde à l'endroit désigné par la variable <b>position</b>.
   */
  private double kx;
  
  /**
   * La variable <b>A</b> correspond à l'amplitude de l'onde à l'endroit désigné par la variable <b>position</b>.
   */
  private double A;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * Constructeur d'une onde électromagnétique progressive sinusoïdale voyageant dans le vitesse à la vitesse de la lumière.
   * 
   * @param oscillator L'oscillateur harmonique simple à l'origine de l'onde progressive.
   */
  public SWave(SOscillator oscillator)
  {
    this(oscillator, SPhysics.c);
  }
  
  /**
   * Constructeur d'une onde progressive sinusoïdale voyageant dans un milieu homogène à une vitesse constante.
   * 
   * @param oscillator L'oscillateur harmonique simple à l'origine de l'onde progressive.
   * @param wave_speed La vitesse de propagation de l'onde dans le milieu.
   * @throws SConstructorException Si la vitesse de l'onde est négative.
   */
  public SWave(SOscillator oscillator, double wave_speed) throws SConstructorException
  {
    if(wave_speed <= 0)
      throw new SConstructorException("Erreur SWave 001 : La vitesse de l'onde '" + wave_speed + "' doit être positive et non nulle.");
  
    this.oscillator = oscillator;
    
    this.wave_speed = wave_speed;
    
    k = evaluateWaveNumber(oscillator.getFrequency(), wave_speed);
    
    setPosition(oscillator.getPosition());
  }

  //------------
  // MÉTHODES //
  //------------
  
  /**
   * Méthode pour obtenir l'oscillateur qui génère l'onde progressive.
   * 
   * @return L'oscillateur.
   */
  public SOscillator getOscillator()
  {
    return oscillator;
  }
  
  /**
   * Méthode pour obtenir la vitesse de propagation de l'onde progressive.
   * 
   * @return La vitesse de l'onde.
   */
  public double getWaveSpeed()
  {
    return wave_speed;
  }
  
  /**
   * Méthode pour obtenir la phase spatiale de l'onde (paramètre k*x de l'onde progressive sinusoïdale)
   * à l'endroit déterminé par la méthode setPosition(). Si la précédente méthode n'a pas été appelée avant,
   * la position où sera évaluée la phase spatiale sera à l'endroit où l'oscillateur est situé ce qui engendra un phase nulle.
   *  
   * @return La phase spatiale à une endroit préalablement déterminé. 
   */
  public double getSpacePhase()
  {
    return kx;
  }
  
  /**
   * Méthode pour obtenir la phase spatiale de l'onde (paramètre k*x de l'onde progressive sinusoïdale)
   * à l'endroit désigné par le point p.
   * 
   * @param p Le point où sera évalué la phase spatiale de l'onde.
   * @return La phase spatiale de l'onde au point p.
   */
  public double getSpacePhase(SVector3d p)
  {
    return evaluateSpacePhase(p);
  }
  
  /**
   * <p>
   * Méthode pour définir la position où l'onde sera évaluée. Cette position porte également le nom de point P.
   * </p>
   * <p>
   * En fixant cette position, ceci permet de précalculer la phase spatiale de l'onde correspond au paramètre <b>k*x</b> de l'onde dans l'expression
   * <ul>x = A sin (k*x ± omega*t + phi)</ul>
   * </p>
   * 
   * @param p La position du point P.
   */
  public void setPosition(SVector3d p)
  {
    position = p;
    
    kx = evaluateSpacePhase(position);
    
    // POUR L'INSTANT ... IL N'Y A PAS DE PARAMÈTRE D'ATTÉNUATION CAUSÉE PAR LA PROPAGATION EN 3D DE L'ONDE !!!
    A = oscillator.getAmplitude();
  }
  
  /**
   * Méthode pour obtenir la valeur de l'onde progressive au temps t à partir de l'expression
   * <ul>y = A*sin(k*x - omega*t + phi)</ul>
   * 
   * @param t Le temps.
   * @return La valeur de l'onde progressive.
   */
  public double getValue(double t)
  {
    return A*Math.sin(kx - oscillator.getTimePhase(t) + oscillator.getInitialPhase());
  }
  
  /**
   * <p>
   * Méthode permettant d'évaluer le nombre d'onde de l'onde.
   * </p>
   * <p>
   * À partir de la définition k = 2*Pi/lamda et lamda = v/f, nous obtenons
   * <ul>k = 2*Pi*f/v</ul>
   * </p>
   * 
   * @param f La fréquence de l'oscillateur.
   * @param v La vitesse de propagation de l'onde.
   * @return Le nombre d'onde de l'onde.
   */
  private double evaluateWaveNumber(double f, double v)
  {
    return 2*Math.PI*f/v;
  }
  
  /**
   * Méthode permettant d'évaluer la phase spatiale à partir d'une position p où l'on désire l'évaluée.
   * Ceci correspond au paramètre <b>k*x</b> de l'onde dans l'expression
   * <ul>x = A sin (k*x ± omega*t + phi)</ul>
   *  
   * @param p Le point p.
   * @return La phase spatiale.
   */
  private double evaluateSpacePhase(SVector3d p)
  {
    // Définir la position x où l'on désire évaluer l'onde
    double x = p.substract(oscillator.getPosition()).modulus();
    
    // Définir le produit kx entre 0 et 2 Pi
    return SWaveOptics.phaseBetweenZeroAnd2Pi(k*x);
  }
  
}//fin de la classe SWave
