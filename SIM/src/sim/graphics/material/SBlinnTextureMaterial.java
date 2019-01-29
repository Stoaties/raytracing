/**
 * 
 */
package sim.graphics.material;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.graphics.SColor;
import sim.graphics.STexture;
import sim.graphics.STextureReader;
import sim.math.SVectorUV;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * <p>La classe <b>STextureMaterial</b> repr�sente un <b>mat�riel de Blinn</b> dont les coefficients de r�lfexion ambiant, diffus et sp�culaire
 * peuvent �tre d�termin� par la pr�sence d'une <b>texture de couleur</b>. Une texture distincte peut �tre utilis�e pour les trois types de r�flexion.</p>
 * <p>Lorsqu'une texture est utilis�e, le coefficient de r�flexion sera multipli�e par une couleur de base blanche (1,1,1) ce qui correspond
 * � utiliser la couleur de la texture comme �tant la couleur de la r�flexion du mat�riel (pas usage de la couleur de base du mat�riel).</p>
 * 
 * @see SBlinnMaterial
 * @author Simon V�zina
 * @since 2015-10-19
 * @version 2015-12-08
 */
public class SBlinnTextureMaterial extends SBlinnMaterial implements STextureMaterial {
  
  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { 
    SKeyWordDecoder.KW_TEXTURE_KA, SKeyWordDecoder.KW_TEXTURE_KD, SKeyWordDecoder.KW_TEXTURE_KS
  };
  
  /**
   * La variable 'ambiente_texture' correspond � la texture de r�flexion ambiante du mat�riel.
   */
  private STexture ambiente_texture;    
  
  /**
   * La variable 'diffuse_texture' correspond � la texture de r�flexion diffuse du mat�riel.
   */
  private STexture diffuse_texture;    
  
  /**
   * La variable 'specular_texture' correspond � la texture de r�flexion speculaire du mat�riel.
   */
  private STexture specular_texture;    
    
  /**
   * La variable <b>is_uv_format_selected</b> permet de d�finir si le code d'interpr�tation de la coordonn�e uv de la texture a �t� d�fini.
   */
  private boolean is_uv_format_selected;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * Construction d'un mat�riel avec une couleur par d�faut. 
   */
  public SBlinnTextureMaterial()
  {
    this(SBlinnMaterial.DEFAULT_COLOR);
  }

  /**
   * Constructeur d'un mat�riel avec une couleur.
   * 
   * @param color - La couleur du mat�riel.
   * @throws SConstructorException Si une erreur est survenue lors de la construction.
   */
  public SBlinnTextureMaterial(SColor color) throws SConstructorException
  {
    super(color);
    
    ambiente_texture = null;
    diffuse_texture = null;
    specular_texture = null;
    
    is_uv_format_selected = false;
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SBlinnTextureMaterial 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }

  /**
   * Constructeur d'un mat�riel � partir d'information lue dans un fichier de format txt.
   * 
   * @param br - Le BufferedReader cherchant l'information dans le fichier txt.
   * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
   * @throws SConstructorException Si une erreur est survenue lors de la construction.
   * @see SBufferedReader
   */
  public SBlinnTextureMaterial(SBufferedReader br) throws IOException, SConstructorException
  {
    this();   
        
    try{
      read(br);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SBlinnTextureMaterial 002 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
  }
  
  @Override
  public SColor ambientColor(SVectorUV uv)
  {
    if(ambiente_texture == null)
      return ambientColor();
    else
      return SBlinnMaterial.DEFAULT_COLOR.multiply(ambiente_texture.getSColor(uv));
  }
  
  @Override
  public SColor diffuseColor(SVectorUV uv)
  {
    if(diffuse_texture == null)
      return diffuseColor();
    else
      return SBlinnMaterial.DEFAULT_COLOR.multiply(diffuse_texture.getSColor(uv));
  }
  
  @Override
  public SColor specularColor(SVectorUV uv)
  {
    if(specular_texture == null)
      return specularColor();
    else
      return SBlinnMaterial.DEFAULT_COLOR.multiply(specular_texture.getSColor(uv));
  }
  
  @Override
  public boolean asTexture()
  {
    return true;
  }
  
  @Override
  public void setUVFormat(int uv_format) throws SRuntimeException
  {
    if(is_uv_format_selected)
      throw new SRuntimeException("Erreur STextureOpenGLMaterial 002 : Le format d'interpr�tation de coordonn�e uv a d�j� �t� d�fini pour cette texture.");
    
    if(ambiente_texture != null)
      ambiente_texture.setUVFormat(uv_format);
    
    if(diffuse_texture != null)
      diffuse_texture.setUVFormat(uv_format);
    
    if(specular_texture != null)
      specular_texture.setUVFormat(uv_format);
    
    is_uv_format_selected = true;
  }
  
  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line)throws SReadingException
  {
    //Faire la v�rification du code pour la classe parent
    if(super.read(sbr, code, remaining_line))
      return true;
    else
      switch(code)
      {
        case SKeyWordDecoder.CODE_TEXTURE_KA :  String ambiente_texture_file_name = readStringNotEmpty(remaining_line, SKeyWordDecoder.KW_TEXTURE_KA);
                                                
                                                // Lecture de la texture seulement si le nom du fichier n'est pas 'STexture.DEFAULT_FILE_NAME == none'
                                                if(!ambiente_texture_file_name.equals(STexture.DEFAULT_FILE_NAME))
                                                {
                                                  STextureReader texture_reader_KA = new STextureReader(ambiente_texture_file_name);
                                                  
                                                  if(texture_reader_KA.asRead())
                                                    ambiente_texture = texture_reader_KA.getValue();
                                                  else
                                                    throw new SReadingException("Erreur SBlinnTextureMaterial 003 - La texture de r�flexion ambiante '" + ambiente_texture_file_name + "' n'a pas �t� charg�e.");
                                                }   
                                                return true;
                                                
        case SKeyWordDecoder.CODE_TEXTURE_KD :  String diffuse_texture_file_name = readStringNotEmpty(remaining_line, SKeyWordDecoder.KW_TEXTURE_KD);
                                                
                                                // Lecture de la texture seulement si le nom du fichier n'est pas 'STexture.DEFAULT_FILE_NAME == none'
                                                if(!diffuse_texture_file_name.equals(STexture.DEFAULT_FILE_NAME))
                                                {
                                                  STextureReader texture_reader_KD = new STextureReader(diffuse_texture_file_name);
                                                
                                                  if(texture_reader_KD.asRead())
                                                    diffuse_texture = texture_reader_KD.getValue();
                                                  else
                                                    throw new SReadingException("Erreur SBlinnTextureMaterial 004 - La texture de r�flexion diffuse '" + diffuse_texture_file_name + "' n'a pas �t� charg�e.");
                                                }
                                                
                                                return true;
                                                
         case SKeyWordDecoder.CODE_TEXTURE_KS : String specular_texture_file_name = readStringNotEmpty(remaining_line, SKeyWordDecoder.KW_TEXTURE_KS);
                                                
                                                // Lecture de la texture seulement si le nom du fichier n'est pas 'STexture.DEFAULT_FILE_NAME == none'
                                                if(!specular_texture_file_name.equals(STexture.DEFAULT_FILE_NAME))
                                                {
                                                  STextureReader texture_reader_KS = new STextureReader(specular_texture_file_name);
                                                
                                                  if(texture_reader_KS.asRead())
                                                    specular_texture = texture_reader_KS.getValue();
                                                  else
                                                    throw new SReadingException("Erreur SBlinnTextureMaterial 005 - La texture de r�flexion sp�culaire '" + specular_texture_file_name + "' n'a pas �t� charg�e.");
                                                }
                                                
                                                return true;
                                                
        default : return false;
      }
    
  }
  
  @Override
  public void write(BufferedWriter bw) throws IOException 
  {
    bw.write(SKeyWordDecoder.KW_TEXTURE_MATERIAL);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //�crire les propri�t�s de la classe SColorMaterial et ses param�tres h�rit�s
    writeTextureMaterialParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write("\t#end material" + SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);  
  }
  
  /**
   * M�thode pour �crire les param�tres associ�s � la classe STextureMaterial et ses param�tres h�rit�s.
   * 
   * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException Si une erreur I/O s'est produite.
   * @see IOException
   */
  protected void writeTextureMaterialParameter(BufferedWriter bw)throws IOException 
  {
    //�crire les propri�t�s de la classe SBlinnMaterial h�rit�es
    writeBlinnMaterialParameter(bw);
    
    //Texture ambiante
    bw.write(SKeyWordDecoder.KW_TEXTURE_KA);
    bw.write("\t");
    
    if(ambiente_texture != null)
      bw.write(ambiente_texture.getFileName());
    else
      bw.write(STexture.DEFAULT_FILE_NAME);
    
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //Texture diffuse
    bw.write(SKeyWordDecoder.KW_TEXTURE_KD);
    bw.write("\t");
    
    if(diffuse_texture != null)
      bw.write(diffuse_texture.getFileName());
    else
      bw.write(STexture.DEFAULT_FILE_NAME);
    
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //Texture sp�culaire
    bw.write(SKeyWordDecoder.KW_TEXTURE_KS);
    bw.write("\t");
    
    if(specular_texture != null)
      bw.write(specular_texture.getFileName());
    else
      bw.write(STexture.DEFAULT_FILE_NAME);
    
    bw.write(SStringUtil.END_LINE_CARACTER);
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
  protected void readingInitialization() throws SInitializationException
  {
    super.readingInitialization();
    
    initialize();
  }
  
  @Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_TEXTURE_MATERIAL;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
  @Override
  public boolean isUVFormatSelected()
  {
    return is_uv_format_selected;
  }
  
}//fin de la classe STextureMaterial
