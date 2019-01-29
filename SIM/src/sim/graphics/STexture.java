/**
 * 
 */
package sim.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.math.SVectorUV;

/**
 * La classe <b>STexture</b> repr�sente une texture de couleur. Cette classe permet de mettre en m�moire une image et acc�der aux diff�rents pixels 
 * de couleur de l'image en coordonn�e texture (u,v) tel que u et v est compris entre 0.0 et 1.0.
 *
 * @author Simon V�zina
 * @since 2015-09-13
 * @version 2016-11-19
 */
public class STexture {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>UV_FORMAT</b> correspond � l'ensemble des formats d'interpr�tation des coordonn�es de texture.
   */
  final public static String[] UV_FORMAT = { "default", "origin_uv_top_left", "origin_uv_bottom_left" };
  
  /**
   * La constante <b>UV_DEFAULT</b> correspond au format d'interpr�tation de coordonn�e de texture par d�faut.
   */
  final public static int UV_DEFAULT = 0;
  
  /**
   * La constante <b>ORIGIN_UV_TOP_LEFT</b> correspond au format d'interpr�tation de coordonn�e de texture
   * positionnant l'orgine dans le coin sup�rieur gauche de la texture.
   */
  final public static int ORIGIN_UV_TOP_LEFT = 1;
  
  /**
   * La constante <b>ORIGIN_UV_BOTTOM_LEFT</b> correspond au format d'interpr�tation de coordonn�e de texture
   * positionnant l'orgine dans le coin inf�rieur gauche de la texture.
   */
  final public static int ORIGIN_UV_BOTTOM_LEFT = 2;
    
  /**
   * La constante 'DEFAULT_FILE_NAME' correspond � un nom de fichier pour une texture par d�faut ne pouvant �tre lu.
   * Une texture portant le nom de {@value} ne peut pas �tre charg�e en m�moire. 
   */
  public static final String DEFAULT_FILE_NAME = "none";  //nom du fichier par d�faut �tant invalide

  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>file_name</b> correspond au non du fichier repr�sentant la texture.
   */
  private final String file_name;                        
  
  /**
   * La variable <b>image</b> correspond � l'espace m�moire o� est enregistr� l'image.
   */
  private final BufferedImage image;                      
  
  /**
   * La variable <b>width</b> correspond � la largeur (x ou u) de la texture.
   */
  private final int width;                               
  
  /**
   * La variable <b>height</b> correspond � la hauteur (y ou v) de la texture.
   */
  private final int height;                               
  
  /**
   * La variable 'uv_coordinate_format' correspond a un code d�terminant le format des coordonn�e uv des textures. 
   * Le format pr�cise entre autre la localisation de l'origine dans une texture ainsi que le sens des axes uv. 
   */
  private int uv_coordinate_format;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'une texture de couleur en prenant le <b>coin sup�rieur gauche</b> d'une image comme origine de la coordonn�e uv de texture.
   * 
   * @param file_name Le nom du fichier.
   * @param image Le buffer comprenant l'information de l'image.
   * @throws SConstructorException S'il y a eu une erreur lors de la lecture emp�chant la construction de la texture.
   */
  public STexture(String file_name, BufferedImage image)throws SConstructorException
  {
    this(file_name, image, ORIGIN_UV_TOP_LEFT);
  }
  
  /**
   * Constructeur d'une texture de couleur.
   * 
   * @param file_name - Le nom du fichier.
   * @param uv_format - Le code de r�f�rence de l'interpr�tation des coordonn�e uv de texture.
   * @throws SConstructorException Si le code de r�f�rence de l'interpr�tation des coordonn�es uv de texture n'est pas reconnu par le syst�me.
   * @throws SConstructorException Si � la construction, on utilise le format 'UV_DEFAULT', car il faut obligatoirement sp�cifier un format.
   */
  public STexture(String file_name, BufferedImage image, int uv_format)throws SConstructorException
  {
    //Initialisation du format d'interpr�tation des coordonn�es de textures
    switch(uv_format)
    {
      case UV_DEFAULT : throw new SConstructorException("Erreur STexture 001 : Erreur de construction de la texture en raison d'un format par d�faut. � la construction, il faut absolument sp�cifier un format.");
      
      case ORIGIN_UV_TOP_LEFT :
      case ORIGIN_UV_BOTTOM_LEFT : uv_coordinate_format = uv_format; break;
      
      default : throw new SConstructorException("Erreur STexture 002 : Erreur de construction de la texture en raison du format d'interpr�tation '" + uv_format + "' qui n'est pas reconnu par le syst�me.");
    }
    
    this.file_name = file_name;
    this.image = image;
    width = image.getWidth();
    height = image.getHeight();
  }
  
  /**
   * Copie constructeur d'une texture. La copie de cette texture fera une copie de la r�f�rence au BufferedImage de la texture � copier. 
   * Ainsi, une modification au BufferedImage influencera �galement l'ensemble des BufferedImage des autres copies.
   * <p>L'int�r�t de cette copie permettra de modifier personnellement le param�tre <i>uv_coordinate_format</i> par la m�thode <i>setUVFormat(...)</i>
   * ce qui permettra d'interpr�ter individuellement les coordonn�es des textures pour chaque usage diff�rent d'une m�me texture.</p>
   * 
   * @param texture - La texture � copier.
   */
  public STexture(STexture texture)
  {
    file_name = texture.file_name;
    image = texture.image;
    width = texture.width;
    height = texture.height;  
    uv_coordinate_format = texture.uv_coordinate_format;
  }
  
  /**
   * M�thode pour obtenir le nom du fichier de la texture.
   * 
   * @return Le nom du fichier de la texture.
   */
  public String getFileName()
  {
    return file_name;
  }
  
  /**
   * M�thode pour obtenir la couleur d'une coordonn�e uv de la texture.
   * 
   * @param uv - La coordonn�e uv de la texture.
   * @return La couleur en coordonn�e uv de la texture.
   * @throws SRuntimeException Si le code d'interpr�tation de l'origine de la coordonn�e de texture uv est mal d�finie.
   */
  public SColor getSColor(SVectorUV uv) throws SRuntimeException
  {
    //S'assurer que la coordonn�e uv pourra acc�der une coordonn�e dans la texture
    SVectorUV crop_uv = uv.getInBound(SVectorUV.REPEAT);
    
    //Obtenir la couleur dans la texture � partir de la coordonn�e uv
    //Puisque l'origine (0,0) du BufferedImage est dans le coin sup�rieur gauche, nous allons utiliser cette convention pour aller cherche la bonne couleur. 
    //Cependant, il est possible de devoir interpr�ter l'origine diff�remment selon le choix pris lors de la cr�ation d'un mod�le 3d.
    switch(uv_coordinate_format)
    {
      case ORIGIN_UV_TOP_LEFT : return getSColorUVFormated(crop_uv);
      
      case ORIGIN_UV_BOTTOM_LEFT : return getSColorUVFormated(new SVectorUV(crop_uv.getU(), 1.0 - crop_uv.getV()));
      
      default : throw new SRuntimeException("Erreur STexture 002 : L'origine de la coordonn�e de texture de code '" + uv_coordinate_format + "' est mal d�finie.");
    }    
  }
  
  /**
   * M�thode pour modifier le format d'interpr�tation des coordonn�es uv de la texture.
   * Si le format utilis� est STexture.UV_DEFAULT, il n'y aura aucun changement, car le format par d�faut est celui d�sign� lors de la construction de la texture.
   * 
   * @param uv_format - Le code du format d'interpr�tation des coordonn�es uv de la texture.
   * @throws SRuntimeException Si le code du format d'interpr�tation n'est pas reconnu par le syst�me. 
   */
  public void setUVFormat(int uv_format) throws SRuntimeException
  {
    switch(uv_format)
    {
      case UV_DEFAULT :            break;   //rien changer, car le format par d�faut est celui lors de l'initialisation.
      case ORIGIN_UV_TOP_LEFT : 
      case ORIGIN_UV_BOTTOM_LEFT : uv_coordinate_format = uv_format; 
                                   break;
      
      default : throw new SRuntimeException("Erreur STexture 003 : Le code d'interpr�tation de l'origine de la coordonn�e de texture '" + uv_format +"' n'est pas reconnu par le syst�me.");
    }
    
  }
  
  /**
   * M�thode pour obtenir la couleur � partir d'une coordonn�e uv interpr�t� dans le format BufferedImage
   * positionnnant la coordonn�e de texel (0,0) dans le coin sup�rieur gauche de la texture.
   * 
   * @param uv - La coordonn�e uv du texel.
   * @return La couleur du texel.
   */
  private SColor getSColorUVFormated(SVectorUV uv)
  {
    return new SColor(new Color(image.getRGB( (int)(uv.getU()*(width-1)), (int)(uv.getV()*(height-1)) )));  
  }
  

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    
    if (obj == null)
      return false;
    
    if (getClass() != obj.getClass())
      return false;
    
    STexture other = (STexture) obj;
    
    // Comparer les dimensions des images
    if (height != other.height)
      return false;
    
    if (width != other.width)
      return false;
       
    // Comparer les images uniquement si elles ne sont pas null.
    // Deux images �gales � null seront consid�r�es �gales � ce point.
    if (image == null)
    {
      if (other.image != null)
        return false;
    }
    else
    {
      if(other.image == null)
        return false;
      else
      {
        // It�rer sur l'ensemble des pixels du BufferedImage
        for (int y = 0; y < height; y++)
          for (int x = 0; x < width; x++)
            // Puisque dans un buffered image, une couleur est convertie en code entier,
            // on peut utiliser l'op�rateur == et != pour comparer deux couleurs.
            if (image.getRGB(x, y) != other.image.getRGB(x, y))
              return false; 
      }
    }
          
    return true;
  }
  
}//fin de la classe STexture
