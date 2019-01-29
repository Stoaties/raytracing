/**
 * 
 */
package sim.readwrite.object;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.math.SImpossibleNormalizationException;
import sim.math.SVector3d;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SCameraRW</b> repr�sente une camera initialis�e lors d'une lecture en fichier et pouvant �tre �crite dans un fichier texte.
 * 
 * @author Simon V�zina
 * @since 2017-11-16
 * @version 2017-11-16
 */
public class SCameraRW extends SAbstractReadableWriteable {

//--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = {
    SKeyWordDecoder.KW_POSITION, SKeyWordDecoder.KW_UP, SKeyWordDecoder.KW_LOOK_AT,
    SKeyWordDecoder.KW_ANGLE, SKeyWordDecoder.KW_NEAR_CLIPPING_PLANE, SKeyWordDecoder.KW_FAR_CLIPPING_PLANE 
  };
  
  /**
   * La constante <b>DEFAULT_POSITION</b> correspond � la position de la cam�ra par d�faut.
   */
  private static final SVector3d DEFAULT_POSITION = new SVector3d(0.0, 0.0, 0.0);
  
  /**
   * La constante <b>DEFAULT_LOOK_AT</b> correspond � la direction vers o� la cam�ra regarde par d�faut.
   */
  private static final SVector3d DEFAULT_LOOK_AT = new SVector3d(0.0, 0.0, 1.0);
  
  /**
   * La constante <b>DEFAULT_UP</b> correspond � la direction verticale (le haut) de la cam�ra par d�faut.
   */
  private static final SVector3d DEFAULT_UP = new SVector3d(0.0, 1.0, 0.0);
  
  /**
   * La constante <b>DEFAULT_VIEW_ANGLE</b> correspond � l'angle d'ouverture vertical de la cam�ra par d�faut �tant �gal � {@value} degr�s.
   */
  private static final double DEFAULT_VIEW_ANGLE = 60.0;
  
  /**
   * La constante <b>DEFAULT_ZNEAR</b> correspond � distance � partir de laquelle la cam�ra <b>peut voir</b> par d�faut �tant �gale � {@value}.
   */
  private static final double DEFAULT_ZNEAR = 1.0;
  
  /**
   * La constante <b>DEFAULT_ZFAR</b> correspond � distance � partir de laquelle la cam�ra <b>ne peut plus voir</b> par d�faut �tant �gale � {@value}.
   */
  private static final double DEFAULT_ZFAR = 100.0;

  /**
   * La constante <b>MINIMUM_CAMERA_ANGLE</b> correspond � l'angle minimal d'ouverture d'une camera �tant �gal � {@value} degr�s.
   */
  private static final double MINIMUM_CAMERA_ANGLE = 10;
  
  /**
   * La constante <b>MAXIMUM_CAMERA_ANGLE</b> correspond � l'angle maximal d'ouverture d'une camera �tant �gal � {@value} degr�s.
   */
  private static final double MAXIMUM_CAMERA_ANGLE = 170;
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>position</b> repr�sente la position de la cam�ra.
   */
  private SVector3d position; 
  
  /**
   * La variable <b>look_at</b> repr�sente la position o� la cam�ra regarde.
   * Cette information permet de d�terminer dans quelle direction la cam�ra pointe.
   */
  private SVector3d look_at;  
    
  /**
   * La variable <b>up</b> correspond � la d�finition du haut pour la cam�ra.
   */
  private SVector3d up;   

  /**
   * La variable <b>view_angle</b> correspond � l'angle d'ouverture de la cam�ra dans la direction verticale (up).
   * Cet �l�ment se retrouve entre autre dans la fonction gluPerspective (param�tre <i>fovy</i>)de la librairie OpenGl pour faire la construction de la pyramide de vue.
   * Cette valeur doit �tre en <b>degr�</b>.
   */
  private double view_angle;  
  
  /**
   * La variable <b>z_near</b> correspond � la distance <b>la plus pr�s</b> pouvant �tre observ�e par la cam�ra.
   * Cette valeur doit toujours �tre <b>positive</b>. Elle correspond �galement la distance entre la cam�ra et le <i>near clipping plane</i> de la pyramide de vue.
   */
  private double z_near;      
  
  /**
   * Specifies the distance from the viewer to the far clipping plane (always positive). 
   */
  private double z_far;
    
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur vide de la cam�ra. Par d�faut, la camera sera situ�e � l'origine pointant selon l'axe x avec le haut orient� selon l'axe z.
   */
  public SCameraRW()
  {
    this.position = DEFAULT_POSITION;
    this.look_at = DEFAULT_LOOK_AT;
    this.up = DEFAULT_UP;
    this.view_angle = DEFAULT_VIEW_ANGLE;
    this.z_near = DEFAULT_ZNEAR;
    this.z_far = DEFAULT_ZFAR;
  }

  /**
   * Constructeur de la cam�ra � partir d'information lue dans un fichier de format .txt.
   * 
   * @param sbr Le BufferedReader cherchant l'information de le fichier .txt.
   * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
   * @throws SConstructorException Si un param�tre lors de la lecture rend l'objet invalide.
   * @see SBufferedReader
   */
  public SCameraRW(SBufferedReader sbr) throws IOException, SConstructorException
  {
    this();   //configuration de base s'il y a des param�tres non d�fini.
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      //Si le vecteur position et look_at sont incompatible ensemble
      throw new SConstructorException("Erreur SCameraRW 004 : La lecture rend l'objet dans un �tat invalide", e);
    }
  }
  
  public SVector3d getPosition()
  {
    return position;
  }
  
  
  
  
  @Override
  public void write(BufferedWriter bw)throws IOException
  {
    bw.write(SKeyWordDecoder.KW_CAMERA);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //�crire les param�tres de la classe SViewFrustum
    writeSCameraParameter(bw);
        
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
  /**
   * M�thode pour �crire les param�tres associ�s � la classe SViewFrustum.
   * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException S'il y a une erreur d'�criture.
   * @see IOException
   */
  protected void writeSCameraParameter(BufferedWriter bw)throws IOException
  {
    bw.write(SKeyWordDecoder.KW_POSITION);
    bw.write("\t");
    position.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_LOOK_AT);
    bw.write("\t\t");
    look_at.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_UP);
    bw.write("\t\t");
    up.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_ANGLE);
    bw.write("\t\t\t");
    bw.write(Double.toString(view_angle));
    bw.write(" degrees");
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_NEAR_CLIPPING_PLANE);
    bw.write("\t");
    bw.write(Double.toString(z_near));
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_FAR_CLIPPING_PLANE);
    bw.write("\t");
    bw.write(Double.toString(z_far));
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
  @Override
  protected boolean read(SBufferedReader br, int code, String remaining_line)throws SReadingException
  {
    switch(code)
    {
      case SKeyWordDecoder.CODE_POSITION : position = new SVector3d(remaining_line); return true; 
      
      case SKeyWordDecoder.CODE_UP :       up = new SVector3d(remaining_line); return true;
      
      case SKeyWordDecoder.CODE_LOOK_AT :  look_at = new SVector3d(remaining_line); return true;
      
      case SKeyWordDecoder.CODE_ANGLE : view_angle = this.readDoubleEqualOrGreaterThanValueAndEqualOrSmallerThanValue(remaining_line, MINIMUM_CAMERA_ANGLE, MAXIMUM_CAMERA_ANGLE, SKeyWordDecoder.KW_ANGLE); return true;
      
      case SKeyWordDecoder.CODE_NEAR_CLIPPING_PLANE : z_near = readDoubleGreaterThanZero(remaining_line, SKeyWordDecoder.KW_NEAR_CLIPPING_PLANE); return true;
      
      case SKeyWordDecoder.CODE_FAR_CLIPPING_PLANE : z_far = readDoubleGreaterThanZero(remaining_line, SKeyWordDecoder.KW_FAR_CLIPPING_PLANE); return true;
      
      default : return false;
    } 
  }
      
  @Override
  protected void readingInitialization() throws SInitializationException
  {
    initialize();
  }
   
  /**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    SVector3d front;
    
    try{
      
      // Construction du vecteur "front = devant" qui doivent �tre normalis�s
      front = (look_at.substract(position)).normalize();
    }catch(SImpossibleNormalizationException e){
      throw new SInitializationException("Erreur SCameraRW 005 : Le vecteur look_at = " + look_at + " est le vecteur position = " + position + " ne permet pas de construire un vecteur d�signant l'orientation avant de la camera normalis�.", e);
    }
    
    try {
      // Construction du vecteur "up = haut" � partir des informations fournies
      // afin de le rendre purement perpendiculaire au vecteur front (tout en �tant le le plan initial et gardant le m�me sens)
      up = front.cross(up).cross(front).normalize();
    }catch(SImpossibleNormalizationException e){
      throw new SInitializationException("Erreur SCameraRW 005 : Un vecteur up n'a pas pu �tre normalis�.", e);
    }
    
    //V�rifier que l'�cran de fond est plus loin que l'�cran de face
    if(z_far < z_near)
      throw new SInitializationException("Erreur SCameraRW 006 : La distance � l'�cran de fond '" + z_far + "' ne peut pas �tre plus pr�s que la distance � l'�cran de face '" + z_near + "'.");  
  }
  
  @Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_CAMERA;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
}// fin de la classe SCameraRW
