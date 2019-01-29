/**
 * 
 */
package sim.graphics.light;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.math.SVector3d;
import sim.physics.SNotVisibleLightException;
import sim.physics.SWave;
import sim.physics.SWaveOptics;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe abstraite <b>SAbstractInterferenceLight</b> correspond à une source de lumière monochromatique pouvant réaliser de l'interférence.
 * 
 * @author Simon Vézina
 * @since 2016-02-15
 * @version 2017-02-02
 */
public abstract class SAbstractInterferenceLight extends SAbstractAttenuatedLight implements SInterferenceLight {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond à un tableau contenant l'ensemble des mots clés 
   * à utiliser reconnus lors de la définition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = {
    SKeyWordDecoder.KW_WAVE_LENGTH, SKeyWordDecoder.KW_PERIOD_ITERATION, SKeyWordDecoder.KW_NB_OSCILLATOR
  };
  
  /**
   * La constante <b>DEFAULT_WAVE_LENGTH</b> correspond à la longueur d'onde (en nm) de la source de lumière monochromatique par défaut.
   */
  protected static final double DEFAULT_WAVE_LENGTH = 550;
  
  /**
   * La constante <b>DEFAULT_PERIOD_ITERATION</b> correspond au nombre d'itération qui seront réalisé lors d'un calcul effectué sur une période complète d'oscillation des oscillateurs.
   */
  protected static final int DEFAULT_PERIOD_ITERATION = 10;
  
  /**
   * La constante <b>DEFAULT_NB_OSCILLATOR</b> correspond au nombre d'oscillateurs définissant un axe de la fente par défaut.
   */
  protected static final int DEFAULT_NB_OSCILLATOR = 2;
  
  //-------------
  // VARIABLES //
  //-------------
  /**
   * La variable <b>wave_length</b> correspond à la longueur d'onde de la source de lumière.
   */
  protected double wave_length;
  
  /**
   * La variable <b>period_iteration</b> correspond au nombre d'itérations qui seront utilisées pour évaluer une période complète 
   * d'oscillation des oscillateurs composant la source de lumière. Ce paramètre détermine la précision lorsqu'il
   * y aura lors d'un calcul qui requière une moyenne sur cylce complet d'oscillation. 
   */
  protected int period_iteration;
  
  /**
   * La variable <b>nb_oscillator</b> correspond au nombre d'oscillateurs utiliséa pour définir l'axe vertical (le <i>up</i>) de l'ouverture.
   */
  protected int nb_oscillator;
  
  /**
   * La variable <b>wave_list</b> correspond à la liste ondes progressives généré par les multiples oscillateurs internes à la source de lumière.
   * Ce sont ces oscillateurs qui vont générer des ondes qui pourront interférer au site où la lumière se dirigera.
   */
  protected final List<SWave> wave_list;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * Constructeur d'une source de lumière pouvant réaliser de l'interférence par défaut. 
   */
  public SAbstractInterferenceLight()
  {
    this(DEFAULT_WAVE_LENGTH, DEFAULT_POSITION, DEFAULT_AMPLIFICATION, DEFAULT_CONSTANT_ATTENUATOR, DEFAULT_LINEAR_ATTENUATOR, DEFAULT_QUADRATIC_ATTENUATOR, DEFAULT_PERIOD_ITERATION, DEFAULT_NB_OSCILLATOR);
  }

  /**
   * Constructeur d'une source de lumière pouvant réaliser de l'interférence.
   * 
   * @param wave_length La longueur d'onde de la source de lumière (en nm).
   * @param position La position de la source.
   * @param amp Le facteur d'amplification de la source de lumière.
   * @param cst_att La constante d'atténuation à taux constant.
   * @param lin_att La constante d'atténuation à taux linéaire.
   * @param quad_att La constante d'atténuation à taux quadratique.
   * @param period_iteration Le nombre d'itérations réalisées dans les calculs effectués sur un cycle de période d'oscillation des oscillateurs.
   * @throws SConstructorException Si une erreur de construction est survenue.
   */
  public SAbstractInterferenceLight(double wave_length, SVector3d position, double amp, double cst_att, double lin_att, double quad_att, int period_iteration, int nb_oscillator) throws SConstructorException
  {
    super(DEFAULT_LIGHT_COLOR, position, amp, cst_att, lin_att, quad_att);
    
    if(wave_length < 0)
      throw new SConstructorException("Erreur SAbstractInterferenceLight 001 : La longueur d'onde de la source de lumière doit être positive.");
    
    if(period_iteration < 1)
      throw new SConstructorException("Erreur SAbstractInterferenceLight 002 : Le nombre d'itération sur une période étant de '" + period_iteration + "' doit être supérieur à 1.");
    
    if(nb_oscillator < 1)
      throw new SConstructorException("Erreur SAbstractInterferenceLight 003 : Le nombre d'oscillateur pour un axe de l'ouverture '" + nb_oscillator + "' doit être supérieur à 1.");
    
    this.wave_length = wave_length;
    
    this.period_iteration = period_iteration;
    
    this.nb_oscillator = nb_oscillator;
    
    wave_list = new ArrayList<SWave>();
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SAbstractInterferenceLight 004 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }
 
  
  
  /**
   * Méthode pour écrire les paramètres associés à la classe SAbstractInterferenceLight et ceux qu'il hérite.
   * 
   * @param bw Le BufferedWriter écrivant l'information dans un fichier txt.
   * @throws IOException S'il y a une erreur d'écriture.
   * @see IOException
   */
  protected void writeSAbstractInterferenceLightParameter(BufferedWriter bw)throws IOException
  {
    //Écrire les paramètres hérités de la classe SAbstractAttenuatedLight
    writeSAbstractAttenuatedLightParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_WAVE_LENGTH);
    bw.write("\t\t");
    bw.write(Double.toString(wave_length));
    bw.write(" nm");
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_PERIOD_ITERATION);
    bw.write("\t\t");
    bw.write(Integer.toString(period_iteration));
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_NB_OSCILLATOR);
    bw.write("\t\t");
    bw.write(Integer.toString(nb_oscillator));
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
  @Override
  public double getRelativeIntensity(SVector3d position_to_illuminate)
  {
    // Vérifier que la liste des ondes n'est pas vide
    if(wave_list.isEmpty())
      return 0.0;
    
    // Évaluer la période sur laquelle la moyenne sera effectuée (le temps d'une oscillation complète)
    double period = wave_list.get(0).getOscillator().getPeriod();
    
    // Évaluer l'intensité moyenne sur un cycle complet des oscillateur
    double intensity = SWaveOptics.interferenceAverageWaveValue(wave_list, position_to_illuminate, period, period_iteration);
    
    if(intensity < 0)
      throw new SRuntimeException("SAbstractInterferenceLight --> intensity = " + intensity);
    
    // Nombre d'oscillateurs utilisés dans le calcul
    //int nb = wave_list.size();                        // ce choix va créer par moment des valeurs négative, ce qui n'est pas mathématiquement possible.
    double nb = (double)wave_list.size();               // ce choix semble régler le problème !
    
    //--------------------------------
    // CODE DE TEST ---> À RETIRER !!!
    //--------------------------------
    if(intensity/(nb*nb) < 0)
      throw new SRuntimeException("SAbstractInterferenceLight::public double getRelativeIntensity(SVector3d position_to_illuminate) --> intensity/(nb*nb) = " + intensity/(nb*nb) + ", intensity = " + intensity + ", nb = " + nb);
    
    // On retourne l'intensité relative au maximum de l'interférence constructive 
    // étant égale au nombre d'oscillateur au carré 
    return intensity / (nb*nb);
  }

  /**
   * Méthode pour faire l'initialisation de l'objet après sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    // Vider la liste existante (pouvant être construite lors d'une constructure, mais qui devra être changée après une lecture)
    wave_list.clear();
    
    // Déterminer la couleur associée à la longueur d'onde
    try{   
      color = SWaveOptics.wavelengthToSColor(wave_length);
    }catch(SNotVisibleLightException e){
      throw new SInitializationException("Erreur SAbstractInterferenceLight 005 : La source de lumière n'est pas dans le visible. On ne peut pas définir de couleur." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }
  
  @Override
  protected void readingInitialization() throws SInitializationException
  {
    super.readingInitialization();
    
    initialize();
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException 
  {
    switch(code)
    {
      case SKeyWordDecoder.CODE_WAVE_LENGTH : wave_length = readDoubleGreaterThanZero(remaining_line, SKeyWordDecoder.KW_WAVE_LENGTH); return true;
     
      case SKeyWordDecoder.CODE_PERIOD_ITERATION : period_iteration = this.readIntGreaterThanZero(remaining_line, SKeyWordDecoder.KW_PERIOD_ITERATION); return true;
      
      case SKeyWordDecoder.CODE_NB_OSCILLATOR : nb_oscillator = readIntEqualOrGreaterThanZero(remaining_line, SKeyWordDecoder.KW_NB_OSCILLATOR); return true;
      
      // Lecture des paramètres de la classe hérité SAbstractAttenuatedLight  
      default : return super.read(sbr, code, remaining_line);
    }
  }
    
}//fin de la classe SAbstractInterferenceLight
