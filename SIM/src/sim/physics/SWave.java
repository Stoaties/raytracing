/**
 * 
 */
package sim.physics;

import sim.exception.SConstructorException;
import sim.math.SVector3d;

/**
 * La classe <b>SWave</b> repr�sente une onde physique g�n�r� par un oscillateur harmonique simple.
 * La forme de l'onde sera alors une onde sinuso�dale dont la fr�quence et la phase initiale seront d�termin�es par son oscillateur.
 * La longueur d'onde l'onde sera d�termin�e par la vitesse de propagation du milieu et la fr�quence de l'oscillateur. 
 * Dans le cas d'une onde �lectromagn�tique voyageant dans le vide, la vitesse de propagation sera �gale � <i>c</i>.
 * 
 * @author Simon V�zina
 * @since 2016-02-22
 * @version 2016-02-23
 */
public class SWave {

  /**
   * La variable <b>oscillator</b> correspond � l'oscillateur qui g�n�re l'onde .
   */
  private final SOscillator oscillator;
  /**
   * La variable <b>wave_speed</b> correspond � la vitesse de propagation des ondes dans le milieu par d�faut.
   */
  private final double wave_speed;
    
  /**
   * La variable <b>position</b> correspond � la position o� l'onde doit �tre �valu�e.
   */
  private SVector3d position;
  
  //--------------------------
  // Param�tre de pr�calcul //
  //--------------------------
  
  /**
   * La variable <b>k</b> correspond au nombre d'onde de l'onde.
   */
  private final double k;
  
  /**
   * La variable <b>kx</b> correspond � la phase spatiale de l'onde � l'endroit d�sign� par la variable <b>position</b>.
   */
  private double kx;
  
  /**
   * La variable <b>A</b> correspond � l'amplitude de l'onde � l'endroit d�sign� par la variable <b>position</b>.
   */
  private double A;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * Constructeur d'une onde �lectromagn�tique progressive sinuso�dale voyageant dans le vitesse � la vitesse de la lumi�re.
   * 
   * @param oscillator L'oscillateur harmonique simple � l'origine de l'onde progressive.
   */
  public SWave(SOscillator oscillator)
  {
    this(oscillator, SPhysics.c);
  }
  
  /**
   * Constructeur d'une onde progressive sinuso�dale voyageant dans un milieu homog�ne � une vitesse constante.
   * 
   * @param oscillator L'oscillateur harmonique simple � l'origine de l'onde progressive.
   * @param wave_speed La vitesse de propagation de l'onde dans le milieu.
   * @throws SConstructorException Si la vitesse de l'onde est n�gative.
   */
  public SWave(SOscillator oscillator, double wave_speed) throws SConstructorException
  {
    if(wave_speed <= 0)
      throw new SConstructorException("Erreur SWave 001 : La vitesse de l'onde '" + wave_speed + "' doit �tre positive et non nulle.");
  
    this.oscillator = oscillator;
    
    this.wave_speed = wave_speed;
    
    k = evaluateWaveNumber(oscillator.getFrequency(), wave_speed);
    
    setPosition(oscillator.getPosition());
  }

  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir l'oscillateur qui g�n�re l'onde progressive.
   * 
   * @return L'oscillateur.
   */
  public SOscillator getOscillator()
  {
    return oscillator;
  }
  
  /**
   * M�thode pour obtenir la vitesse de propagation de l'onde progressive.
   * 
   * @return La vitesse de l'onde.
   */
  public double getWaveSpeed()
  {
    return wave_speed;
  }
  
  /**
   * M�thode pour obtenir la phase spatiale de l'onde (param�tre k*x de l'onde progressive sinuso�dale)
   * � l'endroit d�termin� par la m�thode setPosition(). Si la pr�c�dente m�thode n'a pas �t� appel�e avant,
   * la position o� sera �valu�e la phase spatiale sera � l'endroit o� l'oscillateur est situ� ce qui engendra un phase nulle.
   *  
   * @return La phase spatiale � une endroit pr�alablement d�termin�. 
   */
  public double getSpacePhase()
  {
    return kx;
  }
  
  /**
   * M�thode pour obtenir la phase spatiale de l'onde (param�tre k*x de l'onde progressive sinuso�dale)
   * � l'endroit d�sign� par le point p.
   * 
   * @param p Le point o� sera �valu� la phase spatiale de l'onde.
   * @return La phase spatiale de l'onde au point p.
   */
  public double getSpacePhase(SVector3d p)
  {
    return evaluateSpacePhase(p);
  }
  
  /**
   * <p>
   * M�thode pour d�finir la position o� l'onde sera �valu�e. Cette position porte �galement le nom de point P.
   * </p>
   * <p>
   * En fixant cette position, ceci permet de pr�calculer la phase spatiale de l'onde correspond au param�tre <b>k*x</b> de l'onde dans l'expression
   * <ul>x = A sin (k*x � omega*t + phi)</ul>
   * </p>
   * 
   * @param p La position du point P.
   */
  public void setPosition(SVector3d p)
  {
    position = p;
    
    kx = evaluateSpacePhase(position);
    
    // POUR L'INSTANT ... IL N'Y A PAS DE PARAM�TRE D'ATT�NUATION CAUS�E PAR LA PROPAGATION EN 3D DE L'ONDE !!!
    A = oscillator.getAmplitude();
  }
  
  /**
   * M�thode pour obtenir la valeur de l'onde progressive au temps t � partir de l'expression
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
   * M�thode permettant d'�valuer le nombre d'onde de l'onde.
   * </p>
   * <p>
   * � partir de la d�finition k = 2*Pi/lamda et lamda = v/f, nous obtenons
   * <ul>k = 2*Pi*f/v</ul>
   * </p>
   * 
   * @param f La fr�quence de l'oscillateur.
   * @param v La vitesse de propagation de l'onde.
   * @return Le nombre d'onde de l'onde.
   */
  private double evaluateWaveNumber(double f, double v)
  {
    return 2*Math.PI*f/v;
  }
  
  /**
   * M�thode permettant d'�valuer la phase spatiale � partir d'une position p o� l'on d�sire l'�valu�e.
   * Ceci correspond au param�tre <b>k*x</b> de l'onde dans l'expression
   * <ul>x = A sin (k*x � omega*t + phi)</ul>
   *  
   * @param p Le point p.
   * @return La phase spatiale.
   */
  private double evaluateSpacePhase(SVector3d p)
  {
    // D�finir la position x o� l'on d�sire �valuer l'onde
    double x = p.substract(oscillator.getPosition()).modulus();
    
    // D�finir le produit kx entre 0 et 2 Pi
    return SWaveOptics.phaseBetweenZeroAnd2Pi(k*x);
  }
  
}//fin de la classe SWave
