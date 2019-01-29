/**
 * 
 */
package sim.graphics.light;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.math.SImpossibleNormalizationException;
import sim.math.SVector3d;
import sim.physics.SOscillator;
import sim.physics.SPhysics;
import sim.physics.SWave;
import sim.physics.SWaveOptics;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SLog;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe abstraite <b>SAbstractPlanarApertureLight</b> repr�sente une source de lumi�re planaire pouvant r�aliser de l'interf�rence.
 * 
 * @author Simon V�zina
 * @since 2016-02-24
 * @version 2016-03-08
 */
public abstract class SAbstractPlanarApertureLight extends SAbstractInterferenceLight {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = {
    SKeyWordDecoder.KW_WIDTH, SKeyWordDecoder.KW_HEIGHT, SKeyWordDecoder.KW_FRONT, SKeyWordDecoder.KW_UP
  };
  
  /**
   * La constante <b>DEFAULT_FRONT</b> correspond � l'orientation de la normale � la surface du plan form� par l'ouverture planaire.
   */
  protected static final SVector3d DEFAULT_FRONT = new SVector3d(0.0, 0.0, 1.0);
  
  /**
   * La constante <b>DEFAULT_UP</b> correspond � l'orientation du haut de la surface de l'ouverture planaire.
   */
  protected static final SVector3d DEFAULT_UP = new SVector3d(0.0, 1.0, 0.0);
  
  /**
   * La constante <b>DEFAULT_HEIGHT</b> correspond � la taille verticale de l'ouverture planaire. Cette dimension est dans l'axe de l'orientation <u>up</u>.
   * Cette valeur est �gale � {@value}.
   */
  protected static final double DEFAULT_HEIGHT = 0.001;
  
  /**
   * La constante <b>DEFAULT_HEIGHT</b> correspond � la taille horizontale de l'ouverture planaire. 
   * Cette dimension est dans l'axe perpentidulcaire � l'orientation <u>front</u> et <u>up</u> tel que
   * <ul>right = front x up.</ul>
   * Cette valeur est �gale � {@value}.
   */
  protected static final double DEFAULT_WIDTH = 0.001;
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>front</b> repr�sente l'axe perpendiculaire au plan de l'ouverture.
   */
  protected SVector3d front;
  
  /**
   * La variable <b>up</b> repr�sente l'axe parall�le au plan de l'ouverture dans la direction de la hauteur (<i>height</i>).
   */
  protected SVector3d up;
  
  /**
   * La variable <b>right</b> repr�sente l'axe parall�le au plan de l'ouverture dans la direction de la largeur (<i>width</i>).
   */
  protected SVector3d right;
  
  /**
   * La variable <b>height</b> repr�sente la taille de l'ouverture dans l'axe d�sign� par le haut (<i>up</i>) de l'ouveture.
   */
  protected double height;
  
  /**
   * La variable <b>width</b> repr�sente la taille de l'ouverture dans l'axe d�sign� par le c�t� (<i>front</i> x <i>up</i>) de l'ouveture.
   */
  protected double width;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'une source de lumi�re � ouverture planaire par d�faut. 
   */
  public SAbstractPlanarApertureLight()
  {
    this(DEFAULT_WAVE_LENGTH, DEFAULT_POSITION, DEFAULT_AMPLIFICATION, DEFAULT_CONSTANT_ATTENUATOR, DEFAULT_LINEAR_ATTENUATOR, DEFAULT_QUADRATIC_ATTENUATOR, DEFAULT_PERIOD_ITERATION, DEFAULT_NB_OSCILLATOR, DEFAULT_FRONT, DEFAULT_UP, DEFAULT_HEIGHT, DEFAULT_WIDTH);
  }

  /**
   * Constructeur d'une source de lumi�re � ouverture planaire.
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
  public SAbstractPlanarApertureLight(double wave_length, SVector3d position, double amp, double cst_att, double lin_att, double quad_att, int period_iteration, int nb_oscillator,
                                      SVector3d front, SVector3d up, double height, double width) throws SConstructorException
  {
    super(wave_length, position, amp, cst_att, lin_att, quad_att, period_iteration, nb_oscillator);
    
    if(front.equals(SVector3d.ORIGIN))
      throw new SConstructorException("Erreur SAbstractPlanarApertureLight 001 : Le vecteur 'front' = " + front + " ne peut pas �tre �gal � l'origine.");
    
    if(up.equals(SVector3d.ORIGIN))
      throw new SConstructorException("Erreur SAbstractPlanarApertureLight 002 : Le vecteur 'up' = " + up + " ne peut pas �tre �gal � l'origine.");
    
    if(height < 0)
      throw new SConstructorException("Erreur SAbstractPlanarApertureLight 003 : La hauteur de l'ouverture 'height' = " + height + " ne peut pas �tre n�gative.");
    
    if(width < 0)
      throw new SConstructorException("Erreur SAbstractPlanarApertureLight 004 : La largeur de l'ouverture 'width' = " + width + " ne peut pas �tre n�gative.");
  
    this.front = front;
    this.up = up;
    this.height = height;
    this.width = width;
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SAbstractPlanarApertureLight 005 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }

  
  /**
   * M�thode pour �crire les param�tres associ�s � la classe SAbstractPlanarApertureLight et ceux qu'il h�rite.
   * 
   * @param bw Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException S'il y a une erreur d'�criture.
   * @see IOException
   */
  protected void writeSAbstractPlanarApertureLightParameter(BufferedWriter bw)throws IOException
  {
    //�crire les param�tres h�rit�s de la classe SAbstractInterferenceLight
    writeSAbstractInterferenceLightParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_FRONT);
    bw.write("\t\t");
    front.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_UP);
    bw.write("\t\t");
    up.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_HEIGHT);
    bw.write("\t\t");
    bw.write(Double.toString(height));
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_WIDTH);
    bw.write("\t\t");
    bw.write(Double.toString(width));
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException 
  {
    switch(code)
    {
      case SKeyWordDecoder.CODE_WIDTH : width = this.readDoubleGreaterThanZero(remaining_line, SKeyWordDecoder.KW_WIDTH); return true;
      
      case SKeyWordDecoder.CODE_HEIGHT : height = this.readDoubleGreaterThanZero(remaining_line, SKeyWordDecoder.KW_HEIGHT); return true;
      
      case SKeyWordDecoder.CODE_UP : up = new SVector3d(remaining_line); return true;
      
      case SKeyWordDecoder.CODE_FRONT : front = new SVector3d(remaining_line); return true;
   
      // Lecture des param�tres de la classe h�rit� SAbstractAttenuatedLight  
      default : return super.read(sbr, code, remaining_line);
    }
  }
  
  /**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    try{
      
      // Normalisation de l'orientation du devant de l'ouverture
      front = front.normalize();
      
      // Reconstruction du vecteur up afin qui soit purement perpendiculaire au front
      // S'il est modifi�, il sera dans le m�me plan qu'� la construction et dans le m�me sens, mais strictement perpendiculaire � front
      up = front.cross(up).cross(front).normalize();
  
      right = front.cross(up).normalize();
      
    }catch(SImpossibleNormalizationException e){
      throw new SInitializationException("Erreur SAbstractPlanarApertureLight 005 : Un vecteur n'a pas pu �tre normalis�. La d�finition de front et up ne sont pas compatible." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
       
    // Initialisation particuli�re du cas � 1 oscillateur �tant au centre
    if(nb_oscillator == 1)
    {
      if(isInsideAperture(width/2.0, height/2.0))
      {
        SOscillator o = new SOscillator(position, SWaveOptics.waveLenghtToFrequency(wave_length*1e-9, SPhysics.c));
        wave_list.add( new SWave(o, SPhysics.c));
      }
    }
    else
    {
      // Parcourir la surface rectangulaire et cr�er un oscillateur aux endroits autoris�s par la fonction de filtrage (le masque)
      int nb_x;         // nombre d'oscillateurs selon l'axe x
      int nb_y;         // nombre d'oscillateurs selon l'axe y
      double size_x;    // distance horizontale entre deux oscillateurs cons�cutif
      double size_y;    // distance verticale entre deux oscillateurs cons�cutif
      
      // Identifier l'axe le plus court comme la r�f�rence � la variable nb_oscillator.
      // L'autre axe aura ainsi plus d'oscillateur avec un espacement l�g�rement plus petit 
      if(width < height)
      {
        nb_x = nb_oscillator;
        size_x = width / (nb_x-1);
        
        nb_y = (int) Math.ceil(height / size_x) + 1;
        size_y = height / (nb_y-1);
      }
      else
      {
        nb_y = nb_oscillator;
        size_y = height / (nb_y-1);
        
        nb_x = (int) Math.ceil(width / size_y) + 1;
        size_x = width / (nb_x-1);
      }
          
      // �valuer la position de l'origine du rectangle  comme �tant le coin inf�rieur gauche
      // � partir du centre du plan de l'ouverture
      SVector3d origin_xy = position.add(right.multiply(-1*width/2.0)).add(up.multiply(-1*height/2.0));
      
      for(int i = 0; i < nb_x; i++)
        for(int j = 0; j < nb_y; j++)
        {
          // V�rifier que la coordonn�e est situ�e dans la zone de l'ouverture
          if(isInsideAperture(i*size_x, j*size_y))
          {
            SVector3d p = origin_xy.add( right.multiply(i*size_x) ).add( up.multiply(j*size_y));
            
            SOscillator o = new SOscillator(p, SWaveOptics.waveLenghtToFrequency(wave_length*1e-9, SPhysics.c));
            wave_list.add( new SWave(o, SPhysics.c));
          }
        }
    } 
  }
  
  @Override
  protected void readingInitialization() throws SInitializationException
  {
    super.readingInitialization();
    
    initialize();
    
    SLog.logWriteLine("Message SAbstractPlanarApertureLight : Une source de lumi�re avec interf�rence (" + getReadableName() + ") a �t� construite avec " + wave_list.size() + " oscillateurs.");
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
  /**
   * <p>
   * M�thode permettant d'�valuer si une coordonn�e (x,y) du plan rectangulaire 
   * se retrouve dans la zone �tant d�finie comme �tant l'ouverture. 
   * </p>
   * <p>
   * L'orginie du plan se retouve dans le <u>coin inf�rieur gauche</u> du rectangle.
   * L'axe x est selon l'horizontale (<i>width</i>) et l'axe y est selon la verticale (<i>height</i>).
   * 
   * @param x La coordonn�e x.
   * @param y La coordonn�e y.
   * @return <b>true</b> si la coordonn�e (x,y) se retrouve dans la zone de l'ouverture et <b>false</b> sinon.
   */
  protected abstract boolean isInsideAperture(double x, double y);
    
}//fin de la classe SAbstractPlanarApertureLight
