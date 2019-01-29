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
 * La classe <b>SApertureMaskLight</b> représente une ouverture rectangulaire masquée.
 * Le masque sera déterminé par une texture. La couleur <u>noire</u> correspondra au masque
 * ce qui déterminera où <u>il n'y aura pas d'oscillateur</u> pour générer la source de lumière.
 * 
 * @author Simon Vézina
 * @since 2016-03-04
 * @version 2016-11-19
 */
public class SApertureMaskLight extends SAbstractPlanarApertureLight {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond à un tableau contenant l'ensemble des mots clés 
   * à utiliser reconnus lors de la définition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_MASK };

  /**
   * La constante <b>MASK_COLOR</b> correspond à la couleur du masque (zone où les oscillateurs sont interdits).
   */
  //private static final SColor MASK_COLOR = SColor.BLACK;
  
  /**
   * La constante <b>MASK_COLOR</b> correspond à la couleur du masque (zone où les oscillateurs sont autorisés).
   */
  private static final SColor APERTURE_COLOR = new SColor(1.0, 1.0, 1.0, 1.0);
    
  /**
   * La constante <b>DEFAULT_MASK_TEXTURE</b> correspond au masque de l'ouverture rectangulaire par défaut.
   * Elle représente un masque sans obstruction (ouverture complètement).
   */
  private static final STexture DEFAULT_MASK_TEXTURE = buildDefaultMaskTexture();
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>mask_texture</b> correspond au masque que l'on doit appliquer sur l'ouverture rectangulaire
   * afin de limiter le nombre d'oscillateurs générant la lumière.
   */
  private STexture mask_texture;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * Constructeur d'une ouverture planaire avec masque par défaut.
   */
  public SApertureMaskLight()
  {
    this(DEFAULT_WAVE_LENGTH, DEFAULT_POSITION, DEFAULT_AMPLIFICATION, DEFAULT_CONSTANT_ATTENUATOR, DEFAULT_LINEAR_ATTENUATOR, DEFAULT_QUADRATIC_ATTENUATOR, DEFAULT_PERIOD_ITERATION, DEFAULT_NB_OSCILLATOR, DEFAULT_FRONT, DEFAULT_UP, DEFAULT_HEIGHT, DEFAULT_WIDTH);
  }

  /**
   * Constructeur d'une source de lumière à ouverture planaire avec masque.
   * 
   * @param wave_length La longueur d'onde de la source de lumière (en nm).
   * @param position La position de la source.
   * @param amp Le facteur d'amplification de la source de lumière.
   * @param cst_att La constante d'atténuation à taux constant.
   * @param lin_att La constante d'atténuation à taux linéaire.
   * @param quad_att La constante d'atténuation à taux quadratique.
   * @param period_iteration Le nombre d'itérations réalisées dans les calculs effectués sur un cycle de période d'oscillation des oscillateurs.
   * @param nb_oscillator Le nombre d'oscillateurs dans l'axe de la plus petite dimension (entre <i>width</i> et <i>height</i>).
   * @param front L'orientation perpendiculaire au plan de l'ouverture planaire.
   * @param up L'orientation du haut de l'ouverture planaire.
   * @param height La hauteur de l'ouverture planaire (dans l'orientation de <i>up</i>).
   * @param width La largeur de l'ouverture planaire.
   * @throws SConstructorException Si une erreur est survenue à la construction.
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
   * Constructeur d'une source de lumière à ouverture rectangulaire masquée pouvant réaliser de l'interférence
   * à partir d'information lue dans un fichier de format txt.
   * 
   * @param sbr Le BufferedReader cherchant l'information dans le fichier txt.
   * @throws IOException Si une erreur de l'objet SBufferedWriter est lancée.
   * @throws SException Si des facteurs d'atténuation sont initialisés avec des valeurs erronées.
   * @throws SConstructorException Si une erreur est survenue à la construction. 
   * @see SBufferedReader
   */
  public SApertureMaskLight(SBufferedReader sbr) throws IOException, SConstructorException
  {
    this();   
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SApertureMaskLight 002 : Une erreur à l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
  }
  
  //------------
  // MÉTHODES //
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
                                          throw new SReadingException("Erreur SApertureMaskLight 003 - Le masque '" + mask_texture_file_name + "' n'a pas été chargée.");
                                      }   
                                      else
                                        mask_texture = DEFAULT_MASK_TEXTURE;
                                                                            
                                      return true;
      
      // Lecture des paramètres de la classe hérité SAbstractPlanarApertureLight  
      default : return super.read(sbr, code, remaining_line);
    }
  }
  
  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_APERTURE_MASK_LIGHT);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //Écrire les propriétés de la classe SCercleGeometry et ses paramètres hérités
    writeSApertureMaskLightParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }

  /**
   * Méthode pour écrire les paramètres associés à la classe SApertureMaskLight et ses paramètres hérités.
   * 
   * @param bw Le BufferedWriter écrivant l'information dans un fichier txt.
   * @throws IOException Si une erreur I/O s'est produite.
   * @see IOException
   */
  protected void writeSApertureMaskLightParameter(BufferedWriter bw)throws IOException
  {
    // Écrire les paramètres hérités de la classe
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
   * Méthode pour faire l'initialisation de l'objet après sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    
  }

  @Override
  protected boolean isInsideAperture(double x, double y)
  {
    // Puisque cette méthode sera appelée lors du constructeur de la classe SAbstractPlanarApertureLight
    // et que la texture n'a toujours pas été construite à ce moment-là, je dois faire la gestion du 'null'.
    // Sous cette situation, aucun oscillateur sera constuit et la méthode devra retourner false.
    if(mask_texture == null)
      return false;
    
    // Coordonnée de texture associé à la coordonnée (x,y)
    SVectorUV uv = new SVectorUV(x/width, y/height);
    
    // Couleur obtenue dans la texture. Cependant, le paramètre alpha n'est peut-être pas bien défini.
    SColor color = mask_texture.getSColor(uv);
    
    // Modifier le paramètre alpha de la couleur de la texture pour s'assurer d'une bonne comparaison
    SColor color_mod = new SColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0);
    
    // Retourner vrai si la couleur est celle de l'ouverture
    return color_mod.equals(APERTURE_COLOR);
  }
  
  /**
   * Méthode pour construire un masque par défaut. Celui-ci représentera une ouverture complètement ouverte (sans obstruction).
   * 
   * @return Le masque par défaut.
   */
  private static STexture buildDefaultMaskTexture()
  {
    // Construire une texture avec une ouverture complète.
    // Cela signifie que l'on va attribuer une valeur "blanche" à l'ensemble des texels de la texture
    // afin que le masque puis bloquer aucun oscillateur.
    // Soyons efficace et construisons une texture à 1 texel blanc.
    BufferedImage buffer = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
    buffer.setRGB(0, 0, APERTURE_COLOR.normalizeColor().getRGB());
    
    return new STexture(STexture.DEFAULT_FILE_NAME, buffer, STexture.ORIGIN_UV_BOTTOM_LEFT);
  }
  
}// fin de la classe SApertureMaskLight
