/**
 * 
 */
package sim.graphics.light;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.geometry.SGeometricUtil;
import sim.math.SVector3d;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SStringUtil;

/**
 * La classe <b>SEllipticalApertureLight</b> repr�sente une source de lumi�re � ouverture elliptique (ou circulaire) pouvant r�aliser de l'interf�rence.
 * 
 * @author Simon V�zina
 * @since 2016-03-03
 * @version 2016-03-08
 */
public class SEllipticalApertureLight extends SAbstractPlanarApertureLight {

//----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'une ouverture planaire de forme elliptique par d�faut.
   */
  public SEllipticalApertureLight()
  {
    this(DEFAULT_WAVE_LENGTH, DEFAULT_POSITION, DEFAULT_AMPLIFICATION, DEFAULT_CONSTANT_ATTENUATOR, DEFAULT_LINEAR_ATTENUATOR, DEFAULT_QUADRATIC_ATTENUATOR, DEFAULT_PERIOD_ITERATION, DEFAULT_NB_OSCILLATOR, DEFAULT_FRONT, DEFAULT_UP, DEFAULT_HEIGHT, DEFAULT_WIDTH);
  }

  /**
   * Constructeur d'une source de lumi�re � ouverture planaire de forme elliptique.
   * 
   * @param wave_length La longueur d'onde de la source de lumi�re (en nm).
   * @param position La position de la source.
   * @param amp Le facteur d'amplification de la source de lumi�re.
   * @param cst_att La constante d'att�nuation � taux constant.
   * @param lin_att La constante d'att�nuation � taux lin�aire.
   * @param quad_att La constante d'att�nuation � taux quadratique.
   * @param period_iteration Le nombre d'it�rations r�alis�es dans les calculs effectu�s sur un cycle de p�riode d'oscillation des oscillateurs.
   * @param nb_oscillator Le nombre d'oscillateur dans l'axe de la plus petite dimension (entre <i>width</i> et <i>height</i>).
   * @param front L'orientation perpendiculaire au plan de l'ouverture planaire.
   * @param up L'orientation du haut de l'ouverture planaire.
   * @param height La hauteur de l'ouverture planaire (dans l'orientation de <i>up</i>).
   * @param width La largeur de l'ouverture planaire.
   * @throws SConstructorException Si une erreur est survenue � la construction.
   */
  public SEllipticalApertureLight(double wave_length, SVector3d position, 
                                   double amp, double cst_att, double lin_att, double quad_att, 
                                   int period_iteration, int nb_oscillator, SVector3d front, SVector3d up, 
                                   double height, double width) throws SConstructorException
  {
    super(wave_length, position, amp, cst_att, lin_att, quad_att, period_iteration, nb_oscillator, front, up, height, width);
  
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SEllipticalApertureLight 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }

  /**
   * Constructeur d'une source de lumi�re � ouverture elliptique pouvant r�aliser de l'interf�rence
   * � partir d'information lue dans un fichier de format txt.
   * 
   * @param sbr Le BufferedReader cherchant l'information dans le fichier txt.
   * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
   * @throws SException Si des facteurs d'att�nuation sont initialis�s avec des valeurs erron�es.
   * @throws SConstructorException Si une erreur est survenue � la construction. 
   * @see SBufferedReader
   */
  public SEllipticalApertureLight(SBufferedReader sbr) throws IOException, SConstructorException
  {
    this();   
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SEllipticalApertureLight 002 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
  }
  
  //------------
  // M�THODES //
  //------------
  
  @Override
  public int getCodeName()
  {
    return ELLIPTICAL_APERTURE_LIGHT_CODE;
  }

  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_RECTANGULAR_APERTURE_LIGHT);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //�crire les propri�t�s de la classe SEllipticalApertureLight et ses param�tres h�rit�s
    writeSEllipticalApertureLightParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }

  /**
   * M�thode pour �crire les param�tres associ�s � la classe SEllipticalApertureLight et ses param�tres h�rit�s.
   * 
   * @param bw Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException Si une erreur I/O s'est produite.
   * @see IOException
   */
  protected void writeSEllipticalApertureLightParameter(BufferedWriter bw)throws IOException
  {
    // �crire les param�tres h�rit�s de la classe
    writeSAbstractPlanarApertureLightParameter(bw);
  }
  
  @Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_ELLIPTICAL_APERTURE_LIGHT;
  }

  @Override
  protected void readingInitialization() throws SInitializationException
  {
    super.readingInitialization();
    
    initialize();
  }
  
  /**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
   
  }

  @Override
  protected boolean isInsideAperture(double x, double y)
  {
    if(SGeometricUtil.isOnEllipsePerimeter(width/2.0, height/2.0, width, height, x, y) < 1)
      return true;
    else
      return false;
  }

}//fin de la classe SEllipticalApertureLight
