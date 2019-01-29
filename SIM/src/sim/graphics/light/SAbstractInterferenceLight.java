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
 * La classe abstraite <b>SAbstractInterferenceLight</b> correspond � une source de lumi�re monochromatique pouvant r�aliser de l'interf�rence.
 * 
 * @author Simon V�zina
 * @since 2016-02-15
 * @version 2017-02-02
 */
public abstract class SAbstractInterferenceLight extends SAbstractAttenuatedLight implements SInterferenceLight {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = {
    SKeyWordDecoder.KW_WAVE_LENGTH, SKeyWordDecoder.KW_PERIOD_ITERATION, SKeyWordDecoder.KW_NB_OSCILLATOR
  };
  
  /**
   * La constante <b>DEFAULT_WAVE_LENGTH</b> correspond � la longueur d'onde (en nm) de la source de lumi�re monochromatique par d�faut.
   */
  protected static final double DEFAULT_WAVE_LENGTH = 550;
  
  /**
   * La constante <b>DEFAULT_PERIOD_ITERATION</b> correspond au nombre d'it�ration qui seront r�alis� lors d'un calcul effectu� sur une p�riode compl�te d'oscillation des oscillateurs.
   */
  protected static final int DEFAULT_PERIOD_ITERATION = 10;
  
  /**
   * La constante <b>DEFAULT_NB_OSCILLATOR</b> correspond au nombre d'oscillateurs d�finissant un axe de la fente par d�faut.
   */
  protected static final int DEFAULT_NB_OSCILLATOR = 2;
  
  //-------------
  // VARIABLES //
  //-------------
  /**
   * La variable <b>wave_length</b> correspond � la longueur d'onde de la source de lumi�re.
   */
  protected double wave_length;
  
  /**
   * La variable <b>period_iteration</b> correspond au nombre d'it�rations qui seront utilis�es pour �valuer une p�riode compl�te 
   * d'oscillation des oscillateurs composant la source de lumi�re. Ce param�tre d�termine la pr�cision lorsqu'il
   * y aura lors d'un calcul qui requi�re une moyenne sur cylce complet d'oscillation. 
   */
  protected int period_iteration;
  
  /**
   * La variable <b>nb_oscillator</b> correspond au nombre d'oscillateurs utilis�a pour d�finir l'axe vertical (le <i>up</i>) de l'ouverture.
   */
  protected int nb_oscillator;
  
  /**
   * La variable <b>wave_list</b> correspond � la liste ondes progressives g�n�r� par les multiples oscillateurs internes � la source de lumi�re.
   * Ce sont ces oscillateurs qui vont g�n�rer des ondes qui pourront interf�rer au site o� la lumi�re se dirigera.
   */
  protected final List<SWave> wave_list;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * Constructeur d'une source de lumi�re pouvant r�aliser de l'interf�rence par d�faut. 
   */
  public SAbstractInterferenceLight()
  {
    this(DEFAULT_WAVE_LENGTH, DEFAULT_POSITION, DEFAULT_AMPLIFICATION, DEFAULT_CONSTANT_ATTENUATOR, DEFAULT_LINEAR_ATTENUATOR, DEFAULT_QUADRATIC_ATTENUATOR, DEFAULT_PERIOD_ITERATION, DEFAULT_NB_OSCILLATOR);
  }

  /**
   * Constructeur d'une source de lumi�re pouvant r�aliser de l'interf�rence.
   * 
   * @param wave_length La longueur d'onde de la source de lumi�re (en nm).
   * @param position La position de la source.
   * @param amp Le facteur d'amplification de la source de lumi�re.
   * @param cst_att La constante d'att�nuation � taux constant.
   * @param lin_att La constante d'att�nuation � taux lin�aire.
   * @param quad_att La constante d'att�nuation � taux quadratique.
   * @param period_iteration Le nombre d'it�rations r�alis�es dans les calculs effectu�s sur un cycle de p�riode d'oscillation des oscillateurs.
   * @throws SConstructorException Si une erreur de construction est survenue.
   */
  public SAbstractInterferenceLight(double wave_length, SVector3d position, double amp, double cst_att, double lin_att, double quad_att, int period_iteration, int nb_oscillator) throws SConstructorException
  {
    super(DEFAULT_LIGHT_COLOR, position, amp, cst_att, lin_att, quad_att);
    
    if(wave_length < 0)
      throw new SConstructorException("Erreur SAbstractInterferenceLight 001 : La longueur d'onde de la source de lumi�re doit �tre positive.");
    
    if(period_iteration < 1)
      throw new SConstructorException("Erreur SAbstractInterferenceLight 002 : Le nombre d'it�ration sur une p�riode �tant de '" + period_iteration + "' doit �tre sup�rieur � 1.");
    
    if(nb_oscillator < 1)
      throw new SConstructorException("Erreur SAbstractInterferenceLight 003 : Le nombre d'oscillateur pour un axe de l'ouverture '" + nb_oscillator + "' doit �tre sup�rieur � 1.");
    
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
   * M�thode pour �crire les param�tres associ�s � la classe SAbstractInterferenceLight et ceux qu'il h�rite.
   * 
   * @param bw Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException S'il y a une erreur d'�criture.
   * @see IOException
   */
  protected void writeSAbstractInterferenceLightParameter(BufferedWriter bw)throws IOException
  {
    //�crire les param�tres h�rit�s de la classe SAbstractAttenuatedLight
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
    // V�rifier que la liste des ondes n'est pas vide
    if(wave_list.isEmpty())
      return 0.0;
    
    // �valuer la p�riode sur laquelle la moyenne sera effectu�e (le temps d'une oscillation compl�te)
    double period = wave_list.get(0).getOscillator().getPeriod();
    
    // �valuer l'intensit� moyenne sur un cycle complet des oscillateur
    double intensity = SWaveOptics.interferenceAverageWaveValue(wave_list, position_to_illuminate, period, period_iteration);
    
    if(intensity < 0)
      throw new SRuntimeException("SAbstractInterferenceLight --> intensity = " + intensity);
    
    // Nombre d'oscillateurs utilis�s dans le calcul
    //int nb = wave_list.size();                        // ce choix va cr�er par moment des valeurs n�gative, ce qui n'est pas math�matiquement possible.
    double nb = (double)wave_list.size();               // ce choix semble r�gler le probl�me !
    
    //--------------------------------
    // CODE DE TEST ---> � RETIRER !!!
    //--------------------------------
    if(intensity/(nb*nb) < 0)
      throw new SRuntimeException("SAbstractInterferenceLight::public double getRelativeIntensity(SVector3d position_to_illuminate) --> intensity/(nb*nb) = " + intensity/(nb*nb) + ", intensity = " + intensity + ", nb = " + nb);
    
    // On retourne l'intensit� relative au maximum de l'interf�rence constructive 
    // �tant �gale au nombre d'oscillateur au carr� 
    return intensity / (nb*nb);
  }

  /**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    // Vider la liste existante (pouvant �tre construite lors d'une constructure, mais qui devra �tre chang�e apr�s une lecture)
    wave_list.clear();
    
    // D�terminer la couleur associ�e � la longueur d'onde
    try{   
      color = SWaveOptics.wavelengthToSColor(wave_length);
    }catch(SNotVisibleLightException e){
      throw new SInitializationException("Erreur SAbstractInterferenceLight 005 : La source de lumi�re n'est pas dans le visible. On ne peut pas d�finir de couleur." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
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
      
      // Lecture des param�tres de la classe h�rit� SAbstractAttenuatedLight  
      default : return super.read(sbr, code, remaining_line);
    }
  }
    
}//fin de la classe SAbstractInterferenceLight
