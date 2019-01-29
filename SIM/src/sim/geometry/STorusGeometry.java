/**
 * 
 */
package sim.geometry;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.exception.SNoImplementationException;
import sim.graphics.SPrimitive;
import sim.math.SImpossibleNormalizationException;
import sim.math.SVector3d;
import sim.math.SVectorUV;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>STorusGeometry</b> repr�sente la g�om�trie du tore (beigne).
 * 
 * @author Simon V�zina
 * @since 2017-02-09
 * @version 2017-12-20 (version labo � Le ray tracer v2.1)
 */
public class STorusGeometry extends SAbstractGeometry {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_POSITION, SKeyWordDecoder.KW_NORMAL,
    SKeyWordDecoder.KW_MAJOR_RADIUS, SKeyWordDecoder.KW_MINOR_RADIUS };
 
  /**
   * La constante <b>DEFAULT_POSITION<b> correspond � la position par d�faut d'un tore �tant � l'origine.
   */
  private static final SVector3d DEFAULT_POSITION = new SVector3d(0.0, 0.0, 0.0); 
   
  /**
   * La constante <b>DEFAULT_NORMAL</b> correspond � la normal au plan du tore par d�faut.
   */
  private static final SVector3d DEFAULT_NORMAL = new SVector3d(0.0, 0.0, 1.0);
  
  /**
   * La constante <b>DEFAULT_MAJOR_RADIUS<b> correspond au rayon du p�rim�tre par d�faut d'un tore �tant �gale � {@value}.
   */
  private static final double DEFAULT_MAJOR_RADIUS = 1.0;    // major radius (distance center tube and center torus)                                  
  
  /**
   * La constante <b>DEFAULT_MINOR_RADIUS<b> correspond au rayon du p�rim�tre par d�faut d'un tore �tant �gale � {@value}.
   */
  private static final double DEFAULT_MINOR_RADIUS = 0.2;    //  minor radius (radius of the tube)
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>position</b> repr�sente la position centrale du tore.
   */
  private SVector3d position;
  
  /**
   * La variable <b>normal</b> repr�sente la normale au plan du tore.
   */
  private SVector3d normal;
  
  /**
   * La variable <b>major_radius</b> repr�sente le rayon du p�rim�tre du tore (la circonf�rence du beigne).
   */
  private double major_radius;
  
  /**
   * La variable <b>minor_radius</b> repr�sente le rayon de la partie cylindrique du tore.
   */
  private double minor_radius;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * 
   */
  public STorusGeometry()
  {
    this(DEFAULT_POSITION, DEFAULT_NORMAL, DEFAULT_MAJOR_RADIUS, DEFAULT_MINOR_RADIUS);
  }

  /**
   * ...
   * 
   * @param position
   * @param normal
   * @param radius
   * @param r
   * @throws SConstructorException
   */
  public STorusGeometry(SVector3d position, SVector3d normal, double radius, double r) throws SConstructorException
  {
    this(position, normal, radius, r, null);
  }
  
  /**
   * ...
   * 
   * @param position
   * @param normal
   * @param major_radius
   * @param minor_radius
   * @param parent
   * @throws SConstructorException
   */
  public STorusGeometry(SVector3d position, SVector3d normal, double major_radius, double minor_radius, SPrimitive parent)  throws SConstructorException
  {
    super(parent);
    
    // V�rification des valeurs de radius et r
    if(major_radius < 0)
      throw new SConstructorException("Erreur STorusGeometry 001 : Le rayon du tore " + major_radius + " est n�gatif.");
    
    if(minor_radius < 0)
      throw new SConstructorException("Erreur STorusGeometry 002 : Le rayon de la partie cylindrique du tore �gal � " + minor_radius + " est n�gatif.");
    
    // Affectation des valeurs num�riques
    this.position = position;
    this.normal = normal;
    this.major_radius = major_radius;
    this.minor_radius = minor_radius;
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur STorusGeometry 003 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }
  
  /**
   * ...
   * 
   * @param sbr
   * @param parent
   * @throws IOException
   * @throws SConstructorException
   */
  public STorusGeometry(SBufferedReader sbr, SPrimitive parent)throws IOException, SConstructorException
  {
    this(DEFAULT_POSITION, DEFAULT_NORMAL, DEFAULT_MAJOR_RADIUS, DEFAULT_MINOR_RADIUS, parent);    
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SSphereGeometry 004 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }
   
  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir la position centrale du tore.
   * 
   * @return la position du centre du tore.
   */
  public SVector3d getPosition()
  {
    return position;
  }
  
  /**
   * M�thode pour obtenir le rayon du p�rim�tre du tore ce qui correspond � la hauteur du cylindre formant le tore.
   * 
   * @return le rayon du p�rim�tre du tore.
   */
  public double getMajorRadius()
  {
    return major_radius;
  }
  
  /**
   * M�thode pour obtenir le rayon de la partie cylindrique du tore.
   * 
   * @return la partie cylindrique du tore.
   */
  public double getMinorRadius()
  {
    return minor_radius;
  }
  
  @Override
  public int getCodeName()
  {
    return SAbstractGeometry.TORUS_CODE;
  }

  @Override
  public boolean isClosedGeometry()
  {
    return true;
  }

  @Override
  public boolean isInside(SVector3d v)
  {
    return SGeometricUtil.isOnTorusSurface(position, normal, major_radius, minor_radius, v) < 0;
  }

  @Override
  public SRay intersection(SRay ray) throws SAlreadyIntersectedRayException
  {
    return ray;
  }

  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_TORUS);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //�crire les propri�t�s de la classe STorusGeometry et ses param�tres h�rit�s
    writeSTorusGeometryParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }

  /**
   * M�thode pour �crire les param�tres associ�s � la classe STorusGeometry et ses param�tres h�rit�s.
   * @param bw Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException Si une erreur I/O s'est produite.
   * @see IOException
   */
  protected void writeSTorusGeometryParameter(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_POSITION);
    bw.write("\t");
    position.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_NORMAL);
    bw.write("\t");
    normal.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_MAJOR_RADIUS);
    bw.write("\t\t");
    bw.write(Double.toString(major_radius));
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_MINOR_RADIUS);
    bw.write("\t\t");
    bw.write(Double.toString(minor_radius));
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
  @Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_TORUS;
  }

  @Override
  protected SVector3d evaluateIntersectionNormal(SRay ray, double intersection_t)
  {
    return SGeometricUtil.outsideTorusNormal(position, normal, major_radius, minor_radius, ray.getPosition(intersection_t));
  }

  @Override
  protected SVectorUV evaluateIntersectionUV(SRay ray, double intersection_t)
  {
    throw new SNoImplementationException("Erreur STorusGeometry 006 : La m�thode n'a pas encore �t� impl�ment�e.");
  }

  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException, IOException
  {
    switch(code)
    {
      case SKeyWordDecoder.CODE_POSITION :      position = new SVector3d(remaining_line); return true;
      
      case SKeyWordDecoder.CODE_NORMAL :        normal = new SVector3d(remaining_line); return true;
                          
      case SKeyWordDecoder.CODE_MAJOR_RADIUS :  major_radius = readDoubleGreaterThanZero(remaining_line, SKeyWordDecoder.KW_MAJOR_RADIUS); return true;
         
      case SKeyWordDecoder.CODE_MINOR_RADIUS :  minor_radius = readDoubleGreaterThanZero(remaining_line, SKeyWordDecoder.KW_MINOR_RADIUS); return true;
      
      default : return false;
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
      normal = normal.normalize();
    }catch(SImpossibleNormalizationException e){
      throw new SInitializationException("Erreur STorusGeometry 007 : Une erreur est survenue lors de la normalisation de la normale � la surface du tore. Cette op�ration est impossible." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
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
  
}//fin de la classe STorusGeometry
