/**
 * 
 */
package sim.graphics.light;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.graphics.SColor;
import sim.graphics.STexture;
import sim.graphics.STextureReader;
import sim.math.SVector3d;
import sim.math.SVectorUV;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SApertureMaskLight</b> repr�sente une ouverture rectangulaire masqu�e.
 * Le masque sera d�termin� par une texture. La couleur <u>noire</u> correspondra au masque
 * ce qui d�terminera o� <u>il n'y aura pas d'oscillateur</u> pour g�n�rer la source de lumi�re.
 * 
 * @author Simon V�zina
 * @since 2016-03-04
 * @version 2016-11-19
 */
public class SApertureMaskLight extends SAbstractPlanarApertureLight {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_MASK };

  /**
   * La constante <b>MASK_COLOR</b> correspond � la couleur du masque (zone o� les oscillateurs sont interdits).
   */
  //private static final SColor MASK_COLOR = SColor.BLACK;
  
  /**
   * La constante <b>MASK_COLOR</b> correspond � la couleur du masque (zone o� les oscillateurs sont autoris�s).
   */
  private static final SColor APERTURE_COLOR = new SColor(1.0, 1.0, 1.0, 1.0);
    
  /**
   * La constante <b>DEFAULT_MASK_TEXTURE</b> correspond au masque de l'ouverture rectangulaire par d�faut.
   * Elle repr�sente un masque sans obstruction (ouverture compl�tement).
   */
  private static final STexture DEFAULT_MASK_TEXTURE = buildDefaultMaskTexture();
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>mask_texture</b> correspond au masque que l'on doit appliquer sur l'ouverture rectangulaire
   * afin de limiter le nombre d'oscillateurs g�n�rant la lumi�re.
   */
  private STexture mask_texture;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * Constructeur d'une ouverture planaire avec masque par d�faut.
   */
  public SApertureMaskLight()
  {
    this(DEFAULT_WAVE_LENGTH, DEFAULT_POSITION, DEFAULT_AMPLIFICATION, DEFAULT_CONSTANT_ATTENUATOR, DEFAULT_LINEAR_ATTENUATOR, DEFAULT_QUADRATIC_ATTENUATOR, DEFAULT_PERIOD_ITERATION, DEFAULT_NB_OSCILLATOR, DEFAULT_FRONT, DEFAULT_UP, DEFAULT_HEIGHT, DEFAULT_WIDTH);
  }

  /**
   * Constructeur d'une source de lumi�re � ouverture planaire avec masque.
   * 
   * @param wave_length La longueur d'onde de la source de lumi�re (en nm).
   * @param position La position de la source.
   * @param amp Le facteur d'amplification de la source de lumi�re.
   * @param cst_att La constante d'att�nuation � taux constant.
   * @param lin_att La constante d'att�nuation � taux lin�aire.
   * @param quad_att La constante d'att�nuation � taux quadratique.
   * @param period_iteration Le nombre d'it�rations r�alis�es dans les calculs effectu�s sur un cycle de p�riode d'oscillation des oscillateurs.
   * @param nb_oscillator Le nombre d'oscillateurs dans l'axe de la plus petite dimension (entre <i>width</i> et <i>height</i>).
   * @param front L'orientation perpendiculaire au plan de l'ouverture planaire.
   * @param up L'orientation du haut de l'ouverture planaire.
   * @param height La hauteur de l'ouverture planaire (dans l'orientation de <i>up</i>).
   * @param width La largeur de l'ouverture planaire.
   * @throws SConstructorException Si une erreur est survenue � la construction.
   */
  public SApertureMaskLight(double wave_length, SVector3d position, 
                                   double amp, double cst_att, double lin_att, double quad_att, 
                                   int period_iteration, int nb_oscillator, SVector3d front, SVector3d up, 
                                   double height, double width) throws SConstructorException
  {
    super(wave_length, position, amp, cst_att, lin_att, quad_att, period_iteration, nb_oscillator, front, up, height, width);
  
    mask_texture = DEFAULT_MASK_TEXTURE;
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SApertureMaskLight 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }

  /**
   * Constructeur d'une source de lumi�re � ouverture rectangulaire masqu�e pouvant r�aliser de l'interf�rence
   * � partir d'information lue dans un fichier de format txt.
   * 
   * @param sbr Le BufferedReader cherchant l'information dans le fichier txt.
   * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
   * @throws SException Si des facteurs d'att�nuation sont initialis�s avec des valeurs erron�es.
   * @throws SConstructorException Si une erreur est survenue � la construction. 
   * @see SBufferedReader
   */
  public SApertureMaskLight(SBufferedReader sbr) throws IOException, SConstructorException
  {
    this();   
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SApertureMaskLight 002 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
  }
  
  //------------
  // M�THODES //
  //------------
  
  @Override
  public int getCodeName()
  {
    return APERTURE_MASK_LIGHT_CODE;
  }

  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException 
  {
    switch(code)
    {
      case SKeyWordDecoder.CODE_MASK : String mask_texture_file_name = readStringNotEmpty(remaining_line, SKeyWordDecoder.KW_MASK);
      
                                      // Lecture de la texture seulement si le nom du fichier n'est pas 'STexture.DEFAULT_FILE_NAME == none'
                                      if(!mask_texture_file_name.equals(STexture.DEFAULT_FILE_NAME))
                                      {
                                        STextureReader texture_reader = new STextureReader(mask_texture_file_name);
        
                                        if(texture_reader.asRead())
                                          mask_texture = texture_reader.getValue();
                                        else
                                          throw new SReadingException("Erreur SApertureMaskLight 003 - Le masque '" + mask_texture_file_name + "' n'a pas �t� charg�e.");
                                      }   
                                      else
                                        mask_texture = DEFAULT_MASK_TEXTURE;
                                                                            
                                      return true;
      
      // Lecture des param�tres de la classe h�rit� SAbstractPlanarApertureLight  
      default : return super.read(sbr, code, remaining_line);
    }
  }
  
  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_APERTURE_MASK_LIGHT);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //�crire les propri�t�s de la classe SCercleGeometry et ses param�tres h�rit�s
    writeSApertureMaskLightParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }

  /**
   * M�thode pour �crire les param�tres associ�s � la classe SApertureMaskLight et ses param�tres h�rit�s.
   * 
   * @param bw Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException Si une erreur I/O s'est produite.
   * @see IOException
   */
  protected void writeSApertureMaskLightParameter(BufferedWriter bw)throws IOException
  {
    // �crire les param�tres h�rit�s de la classe
    writeSAbstractPlanarApertureLightParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_MASK);
    bw.write("\t\t");
    bw.write(mask_texture.getFileName());
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
  @Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_APERTURE_MASK_LIGHT;
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
    // Puisque cette m�thode sera appel�e lors du constructeur de la classe SAbstractPlanarApertureLight
    // et que la texture n'a toujours pas �t� construite � ce moment-l�, je dois faire la gestion du 'null'.
    // Sous cette situation, aucun oscillateur sera constuit et la m�thode devra retourner false.
    if(mask_texture == null)
      return false;
    
    // Coordonn�e de texture associ� � la coordonn�e (x,y)
    SVectorUV uv = new SVectorUV(x/width, y/height);
    
    // Couleur obtenue dans la texture. Cependant, le param�tre alpha n'est peut-�tre pas bien d�fini.
    SColor color = mask_texture.getSColor(uv);
    
    // Modifier le param�tre alpha de la couleur de la texture pour s'assurer d'une bonne comparaison
    SColor color_mod = new SColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0);
    
    // Retourner vrai si la couleur est celle de l'ouverture
    return color_mod.equals(APERTURE_COLOR);
  }
  
  /**
   * M�thode pour construire un masque par d�faut. Celui-ci repr�sentera une ouverture compl�tement ouverte (sans obstruction).
   * 
   * @return Le masque par d�faut.
   */
  private static STexture buildDefaultMaskTexture()
  {
    // Construire une texture avec une ouverture compl�te.
    // Cela signifie que l'on va attribuer une valeur "blanche" � l'ensemble des texels de la texture
    // afin que le masque puis bloquer aucun oscillateur.
    // Soyons efficace et construisons une texture � 1 texel blanc.
    BufferedImage buffer = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
    buffer.setRGB(0, 0, APERTURE_COLOR.normalizeColor().getRGB());
    
    return new STexture(STexture.DEFAULT_FILE_NAME, buffer, STexture.ORIGIN_UV_BOTTOM_LEFT);
  }
  
}// fin de la classe SApertureMaskLight
