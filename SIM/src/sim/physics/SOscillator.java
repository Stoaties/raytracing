/**
 * 
 */
package sim.physics;

import sim.exception.SConstructorException;
import sim.math.SVector3d;

/**
 * La classe <b>SOsillator</b> représente un oscillateur harmonique simple. 
 * 
 * @author Simon Vézina
 * @since 2016-02-14
 * @version 2016-02-23
 */
public class SOscillator {
  
  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>DEFAULT_AMPLITUDE</b> correspond à l'amplitude de l'oscillateur par défaut.
   */
  private static final double DEFAULT_AMPLITUDE = 1.0;
  
  /**
   * La constante <b>DEFAULT_PHI</b> correspond à la constante de phase par défaut.
   */
  private static final double DEFAULT_PHI = 0.0;
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>position</b> correspond à la position de l'oscillateur
   */
  private final SVector3d position;

  /**
   * La variable <b>frequency</b> correspond à la fréquence de l'oscillateur. 
   */
  private final double frequency;
  
  /**
   * La variable <b>A</b> correspond à l'amplitude de l'oscillateur.
   */
  private final double A;
  
  /**
   * La variable <b>phi</b> correspond à la constante de phase des ondes générées par l'oscillateur.
   */
  private final double phi;
  
  //-------------
  // Précalcul //
  //-------------
  
  /**
   * La variable <b>omega</b> correspond à la fréquence angulaire des ondes générées par l'oscillateur.
   */
  private final double omega;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un oscillateur harmonique simple d'amplitude et de phase par défaut. 
   * 
   * @param position La position de l'oscillateur.
   * @param frequency La fréquence de l'oscillateur.
   */
  public SOscillator(SVector3d position, double frequency)
  {
    this(position, frequency, DEFAULT_AMPLITUDE, DEFAULT_PHI);
  }
  
  /**
   * Constructeur d'un oscillateur harmonique simple. 
   * 
   * @param position La position de l'oscillateur.
   * @param frequency La fréquence de l'oscillateur.
   * @param amplitude L'amplitude de l'oscillateur.
   * @param phase La phase initiale de l'oscillateur.
   * @throws SConstructorException Si la fréquence des oscillations est négatives.
   */
  public SOscillator(SVector3d position, double frequency, double amplitude, double phase) throws SConstructorException
  {
    // Vérification de la longueur d'onde de l'oscillateur
    if(frequency < 0)
      throw new SConstructorException("Erreur SOscillator 001 : La fréquence '" + frequency + "' ne peut être négative.");
    
    this.position = position;
    
    this.frequency = frequency;
    
    A = amplitude;
    
    phi = SWaveOptics.phaseBetweenZeroAnd2Pi(phase);
    
    // Évaluer la fréquence angulaire
    omega = 2*Math.PI*frequency;
  }

  //------------
  // MÉTHODES //
  //------------
  
  /**
   * Méthode pour obtenir l'amplitude de l'oscillateur harmonique simple.
   * 
   * @return L'amplitude de l'oscillateur.
   */
  public double getAmplitude()
  {
    return A;
  }
  
  /**
   * Méthode pour obtenir la position de l'oscillateur.
   * 
   * @return La position de l'oscillateur.
   */
  public SVector3d getPosition()
  {
    return position;
  }
  
  /**
   * Méthode pour obtenir la fréquence des oscillations de l'oscillateur.
   * 
   * @return La fréquence de l'oscillateur.
   */
  public double getFrequency()
  {
    return frequency;
  }
  
  /**
   * Méthode pour obtenir la périodes des oscillations de l'oscillateur.
   * 
   * @return La période (en seconde).
   */
  public double getPeriod()
  {
    return 1.0 / frequency;
  }
   
  /**
   * Méthode pour obtenir la valeur de l'onde à l'endroit où le point P a été désigné (par la méthode <i>setEvaluatePosition(SVector3d p)</i>).
   * 
   * @param t Le temps.
   * @return La valeur de l'oscillateur au temps t.
   */
  public double getValue(double t)
  {
    // Pour des raisons numériques, je vais évaluer omega*t et le "recadrer" entre 0 et 2Pi
    return A*Math.sin( SWaveOptics.phaseBetweenZeroAnd2Pi(omega*t) + phi);
  }
  
  /**
   * Méthode pour obtenir la phase initiale (constante de phase) de l'oscillateur. 
   * Ceci correspond au paramètre <b>phi</b> de l'oscillateur dans l'expression
   * <ul>x = A sin (omega*t + phi)</ul>
   * 
   * @return La constante de phase de l'oscillateur entre zéro et 2*Pi.
   */
  public double getInitialPhase()
  {
    return phi;
  }
  
  /**
   * Méthode pour obtenir la phase temporelle de l'oscillateur.
   * Ceci correspond au paramètre <b>omega*t</b> de l'oscillateur dans l'expression
   * <ul>x = A sin (omega*t + phi)</ul>
   * 
   * @param t Le temps de l'oscillation de l'oscillateur.
   * @return La phase temporelle de l'oscillateur entre zéro et 2*Pi.
   */
  public double getTimePhase(double t)
  {
    return SWaveOptics.phaseBetweenZeroAnd2Pi(omega*t);
  }
  
}//fin de la classe SOscillator
