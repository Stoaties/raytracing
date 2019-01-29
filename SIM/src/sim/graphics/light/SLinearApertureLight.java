/**
 * 
 */
package sim.graphics.light;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.math.SVector;
import sim.math.SVector3d;
import sim.physics.SOscillator;
import sim.physics.SPhysics;
import sim.physics.SWave;
import sim.physics.SWaveOptics;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * <p>
 * La classe <b>SLinearApertureLight</b> représente une source de lumière composé de plusieurs sources ponctuelles
 * alignée sur une ligne. Dans la littérature, ce type de source cause de l'interférence par fente simple portant
 * le nom de <i>single slit</i> en anglais.
 * </p>
 * 
 * <p>Dans cette implémentation, le nombre d'oscillateur à l'intérieur de la fente peut être déterminé ce qui
 * permet de simuler 
 * <ul> de <b>l'interférence de Young</b> pour deux oscillateurs,</ul>
 * <ul> de <b>l'interférence par réseau</b> s'il y a plusieurs oscillateurs séparés deux à deux par une distance de l'ordre de grandeur de la longueur d'onde de la source,</ul>
 * <ul> et de la <b>diffraction</b> s'il y a plusieurs oscillateurs sur une longueur de fente de l'ordre de grandeur de la longeur d'onde de la source.</ul>
 * </p>
 *
 * @author Simon Vézina
 * @since 2016-02-16
 * @version 2016-04-01
 */
public class SLinearApertureLight extends SAbstractInterferenceLight {

//--------------
  // CONSTANTES //
  //--------------

  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond à un tableau contenant l'ensemble des mots clés 
   * à utiliser reconnus lors de la définition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_POSITION };
  
  /**
   * La constante <b>DEFAULT_P1</b> correspond à la position de la 1ière extrémité de la fente par défaut.
   */
  protected static final SVector3d DEFAULT_P1 = new SVector3d(0.0, 0.0, 0.0);   
  
  /**
   * La constante <b>DEFAULT_P1</b> correspond à la position de la 2ième extrémité de la fente par défaut.
   */
  protected static final SVector3d DEFAULT_P2 = new SVector3d(0.0, 0.0001, 0.0);   
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>P1</b> correspond à la 1ière extrémité de la fente rectiligne.
   */
  protected SVector3d P1;
  
  /**
   * La variable <b>P2</b> correspond à la 2ième extrémité de la fente rectiligne.
   */
  protected SVector3d P2;
  
  /**
   * La variable <b>reading_point</b> correspond au numéro du point qui sera en lecture.
   */
  protected int reading_point;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'une ouverture linéaire par défaut.
   */
  public SLinearApertureLight()
  {
    this(DEFAULT_WAVE_LENGTH, DEFAULT_P1, DEFAULT_P2, DEFAULT_AMPLIFICATION, DEFAULT_CONSTANT_ATTENUATOR, DEFAULT_LINEAR_ATTENUATOR, DEFAULT_QUADRATIC_ATTENUATOR, DEFAULT_PERIOD_ITERATION, DEFAULT_NB_OSCILLATOR);
  }

  /**
   * Constructeur d'une source de lumière à ouverture linéaire pouvant réaliser de l'interférence.
   * 
   * @param wave_length La longueur d'onde de la source de lumière (en nm).
   * @param P1 Le premier point définissant la ligne d'oscillateurs de la source de lumière.
   * @param P2 Le deuxième point définissant la ligne d'oscillateurs de la source de lumière.
   * @param position La position de la source.
   * @param amp Le facteur d'amplification de la source de lumière.
   * @param cst_att La constante d'atténuation à taux constant.
   * @param lin_att La constante d'atténuation à taux linéaire.
   * @param quad_att La constante d'atténuation à taux quadratique.
   * @param period_iteration Le nombre d'itérations réalisées dans les calculs effectués sur un cycle de période d'oscillation des oscillateurs.
   * @param nb_oscillator Le nombre d'oscillateur dans l'axe de la plus petite dimension (entre <i>width</i> et <i>height</i>).
   * @throws SConstructorException Si une erreur est survenue à la construction.
   */
  public SLinearApertureLight(double wave_length, SVector3d P1, SVector3d P2, double amp, double cst_att, double lin_att, double quad_att, int period_iteration, int nb_oscillator) throws SConstructorException
  {
    // La position de l'ouverture sera au centre de P1 et P2
    super(wave_length, (SVector3d) SVector.linearInterpolation(P1, P2, 0.5), amp, cst_att, lin_att, quad_att, period_iteration, nb_oscillator);
    
    // Vérifier que les points P1 et P2 ne sont pas identique
    if(P1.equals(P2))
      throw new SConstructorException("Erreur SLinearApertureLight 001 : Le point P1 = " + P1 + " et le point P2 = " + P2 + " sont égaux.");
    
    this.P1 = P1;
    this.P2 = P2;
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SLinearApertureLight 002 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }

  /**
   * Constructeur Constructeur d'une source de lumière à ouverture linéaire pouvant réaliser de l'interférence
   * à partir d'information lue dans un fichier de format txt.
   * 
   * @param sbr Le BufferedReader cherchant l'information dans le fichier txt.
   * @throws IOException Si une erreur de l'objet SBufferedWriter est lancée.
   * @throws SException Si des facteurs d'atténuation sont initialisés avec des valeurs erronées.
   * @throws SConstructorException Si une erreur est survenue à la construction. 
   * @see SBufferedReader
   */
  public SLinearApertureLight(SBufferedReader sbr) throws IOException, SConstructorException
  {
    this();   
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SLinearApertureLight 003 : Une erreur à l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
  }
  
  
  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_LINEAR_APERTURE_LIGHT);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //Écrire les propriétés de la classe SCercleGeometry et ses paramètres hérités
    writeSLinearApertureLightParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
  /**
   * Méthode pour écrire les paramètres associés à la classe SLinearApertureLight et ses paramètres hérités.
   * 
   * @param bw Le BufferedWriter écrivant l'information dans un fichier txt.
   * @throws IOException Si une erreur I/O s'est produite.
   * @see IOException
   */
  protected void writeSLinearApertureLightParameter(BufferedWriter bw) throws IOException
  {
    // Écrire les paramètres hérités de la classe
    writeSAbstractInterferenceLightParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_POSITION);
    bw.write("\t\t");
    P1.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_POSITION);
    bw.write("\t\t");
    P2.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }

  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException
  {
    switch(code)
    {
      // On accepte ici deux type de mots clé pour la lecture du début et la fin du cylindre
      case SKeyWordDecoder.CODE_POINT :
      case SKeyWordDecoder.CODE_POSITION :  readPosition(remaining_line); return true;
      
      default : return super.read(sbr, code, remaining_line);
    }
  }
  
  /**
   * Méthode pour faire la lecture d'un point et l'affectation dépendra du numéro du point en lecture déterminé par la variable <i>reading_point</i>.
   * 
   * @param remaining_line - L'expression en string du vecteur positionnant le point du triangle.
   * @throws SReadingException - S'il y a une erreur de lecture.
   */
  private void readPosition(String remaining_line)throws SReadingException
  {
    switch(reading_point)
    {
      case 0 :  P1 = new SVector3d(remaining_line); break;
      
      case 1 :  SVector3d v = new SVector3d(remaining_line);
      
                if(v.equals(P1))
                  throw new SReadingException("Erreur SLinearApertureLight 004 : Le point P2 = " + v + " est identique au point P1 ce qui n'est pas acceptable.");
                
                P2 = v; 
                break;
      
      default : throw new SReadingException("Erreur SLinearApertureLight 005 : Il y a déjà 2 points de défini.");
    }
    
    reading_point++;      
  }
  
  @Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_LINEAR_APERTURE_LIGHT;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
  @Override
  protected void readingInitialization() throws SInitializationException
  {
    super.readingInitialization();
    
    initialize();
  }
  
  /**
   * Méthode pour faire l'initialisation de l'objet après sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    // Évaluer la position de la fente comme étant au centre des point P1 et P2
    position = (SVector3d) SVector.linearInterpolation(P1, P2, 0.5);
    
    // Cas particulier à 1 oscillateur
    if(nb_oscillator == 1)
    {
      SOscillator o = new SOscillator( position, SWaveOptics.waveLenghtToFrequency(wave_length*1e-9, SPhysics.c));
      wave_list.add( new SWave(o, SPhysics.c));
    }
    else
    {
      // Évaluer le vecteur définissant l'axe de la fente rectiligne
      SVector3d P1_to_P2 = P2.substract(P1);
      
      // Évaluer la distance entre deux oscillateurs consécutif.
      // Pour deux oscillateurs, la distance est le module.
      // Pour trois oscillateurs, la distance est le module divisé par 2.
      double distance = P1_to_P2.modulus() / (nb_oscillator-1);
      
      P1_to_P2 = P1_to_P2.normalize();
      
      // Remplir la liste des ondes.
      // Il faudra convertir la longueur d'onde de nm en m puis en fréquence.
      for(int i = 0; i < nb_oscillator; i++)
      {
        SOscillator o = new SOscillator( P1.add(P1_to_P2.multiply(i*distance)), SWaveOptics.waveLenghtToFrequency(wave_length*1e-9, SPhysics.c));
        wave_list.add( new SWave(o, SPhysics.c));
      }
    }
     
  }

  @Override
  public int getCodeName()
  {
    return LINEAR_APERTURE_LIGHT_CODE;
  }
  
}//fin de la classe SLinearApertureLight
