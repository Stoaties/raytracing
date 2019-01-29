/**
 * 
 */
package sim.physics;

import sim.exception.SConstructorException;
import sim.math.SVector3d;

/**
 * La classe <b>SOsillator</b> repr�sente un oscillateur harmonique simple. 
 * 
 * @author Simon V�zina
 * @since 2016-02-14
 * @version 2016-02-23
 */
public class SOscillator {
  
  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>DEFAULT_AMPLITUDE</b> correspond � l'amplitude de l'oscillateur par d�faut.
   */
  private static final double DEFAULT_AMPLITUDE = 1.0;
  
  /**
   * La constante <b>DEFAULT_PHI</b> correspond � la constante de phase par d�faut.
   */
  private static final double DEFAULT_PHI = 0.0;
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>position</b> correspond � la position de l'oscillateur
   */
  private final SVector3d position;

  /**
   * La variable <b>frequency</b> correspond � la fr�quence de l'oscillateur. 
   */
  private final double frequency;
  
  /**
   * La variable <b>A</b> correspond � l'amplitude de l'oscillateur.
   */
  private final double A;
  
  /**
   * La variable <b>phi</b> correspond � la constante de phase des ondes g�n�r�es par l'oscillateur.
   */
  private final double phi;
  
  //-------------
  // Pr�calcul //
  //-------------
  
  /**
   * La variable <b>omega</b> correspond � la fr�quence angulaire des ondes g�n�r�es par l'oscillateur.
   */
  private final double omega;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un oscillateur harmonique simple d'amplitude et de phase par d�faut. 
   * 
   * @param position La position de l'oscillateur.
   * @param frequency La fr�quence de l'oscillateur.
   */
  public SOscillator(SVector3d position, double frequency)
  {
    this(position, frequency, DEFAULT_AMPLITUDE, DEFAULT_PHI);
  }
  
  /**
   * Constructeur d'un oscillateur harmonique simple. 
   * 
   * @param position La position de l'oscillateur.
   * @param frequency La fr�quence de l'oscillateur.
   * @param amplitude L'amplitude de l'oscillateur.
   * @param phase La phase initiale de l'oscillateur.
   * @throws SConstructorException Si la fr�quence des oscillations est n�gatives.
   */
  public SOscillator(SVector3d position, double frequency, double amplitude, double phase) throws SConstructorException
  {
    // V�rification de la longueur d'onde de l'oscillateur
    if(frequency < 0)
      throw new SConstructorException("Erreur SOscillator 001 : La fr�quence '" + frequency + "' ne peut �tre n�gative.");
    
    this.position = position;
    
    this.frequency = frequency;
    
    A = amplitude;
    
    phi = SWaveOptics.phaseBetweenZeroAnd2Pi(phase);
    
    // �valuer la fr�quence angulaire
    omega = 2*Math.PI*frequency;
  }

  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir l'amplitude de l'oscillateur harmonique simple.
   * 
   * @return L'amplitude de l'oscillateur.
   */
  public double getAmplitude()
  {
    return A;
  }
  
  /**
   * M�thode pour obtenir la position de l'oscillateur.
   * 
   * @return La position de l'oscillateur.
   */
  public SVector3d getPosition()
  {
    return position;
  }
  
  /**
   * M�thode pour obtenir la fr�quence des oscillations de l'oscillateur.
   * 
   * @return La fr�quence de l'oscillateur.
   */
  public double getFrequency()
  {
    return frequency;
  }
  
  /**
   * M�thode pour obtenir la p�riodes des oscillations de l'oscillateur.
   * 
   * @return La p�riode (en seconde).
   */
  public double getPeriod()
  {
    return 1.0 / frequency;
  }
   
  /**
   * M�thode pour obtenir la valeur de l'onde � l'endroit o� le point P a �t� d�sign� (par la m�thode <i>setEvaluatePosition(SVector3d p)</i>).
   * 
   * @param t Le temps.
   * @return La valeur de l'oscillateur au temps t.
   */
  public double getValue(double t)
  {
    // Pour des raisons num�riques, je vais �valuer omega*t et le "recadrer" entre 0 et 2Pi
    return A*Math.sin( SWaveOptics.phaseBetweenZeroAnd2Pi(omega*t) + phi);
  }
  
  /**
   * M�thode pour obtenir la phase initiale (constante de phase) de l'oscillateur. 
   * Ceci correspond au param�tre <b>phi</b> de l'oscillateur dans l'expression
   * <ul>x = A sin (omega*t + phi)</ul>
   * 
   * @return La constante de phase de l'oscillateur entre z�ro et 2*Pi.
   */
  public double getInitialPhase()
  {
    return phi;
  }
  
  /**
   * M�thode pour obtenir la phase temporelle de l'oscillateur.
   * Ceci correspond au param�tre <b>omega*t</b> de l'oscillateur dans l'expression
   * <ul>x = A sin (omega*t + phi)</ul>
   * 
   * @param t Le temps de l'oscillation de l'oscillateur.
   * @return La phase temporelle de l'oscillateur entre z�ro et 2*Pi.
   */
  public double getTimePhase(double t)
  {
    return SWaveOptics.phaseBetweenZeroAnd2Pi(omega*t);
  }
  
}//fin de la classe SOscillator
