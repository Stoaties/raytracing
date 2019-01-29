/**
 * 
 */
package sim.graphics.material;

import sim.exception.SConstructorException;
import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.graphics.SColor;
import sim.graphics.STexture;
import sim.graphics.STextureReader;
import sim.math.SVectorUV;
import sim.util.SInitializationException;
import sim.util.SLog;
import sim.util.SStringUtil;

/**
 * La classe <b>STextureOpenGLMaterial</b> repr�sente un mat�riel d�finit � l'aide de textures.
 * 
 * @author Simon V�zina
 * @since 2015-09-14
 * @version 2015-12-08
 */
public class STextureOpenGLMaterial extends SOpenGLMaterial implements STextureMaterial {

  /**
   * La variable 'ambiente_texture' correspond � la texture de r�flexion ambiante du mat�riel.
   */
  private final STexture ambiente_texture;    
  
  /**
   * La variable 'diffuse_texture' correspond � la texture de r�flexion diffuse du mat�riel.
   */
  private final STexture diffuse_texture;    
  
  /**
   * La variable 'specular_texture' correspond � la texture de r�flexion speculaire du mat�riel.
   */
  private final STexture specular_texture; 
  
  /**
   * La variable <b>is_uv_format_selected</b> permet de d�finir si le code d'interpr�tation de la coordonn�e uv de la texture a �t� d�fini.
   */
  private boolean is_uv_format_selected;
  
  /**
   * Constructeur d'un mat�riel respectant les standards de la librairie OpenGL incluant des textures de couleur.
   * 
   * @param name - Le nom du mat�riel.
   * @param ka - Le vecteur des coefficients de r�flexion ambiant.
   * @param kd - Le vecteur des coefficients de r�flexion diffuse.
   * @param ks - Le vecteur des coefficients de r�flexion sp�culaire.
   * @param shininess - La brillance du mat�riel.
   * @param texture_ka_file_name - Le nom de la texture des coefficients de r�flexion ambiante.
   * @param texture_kd_file_name - Le nom de la texture des coefficients de r�flexion diffuse.
   * @param texture_ks_file_name - Le nom de la texture des coefficients de r�flexion sp�culaire.
   * @throws SConstrutorException Si la brillance (shininess) est n�gative.
   * @throws SConstructorException Si une erreur est survenue lors de la construction.
   */
  public STextureOpenGLMaterial(String name, SColor ka, SColor kd, SColor ks, double shininess, String texture_ka_file_name, String texture_kd_file_name, String texture_ks_file_name) throws SConstructorException
  {
    super(name, ka, kd, ks, shininess);
    
    is_uv_format_selected = false;
    
    //Chargement de la texture de r�flexion ambiante
    if(texture_ka_file_name != null)
    {
      STextureReader texture_reader = new STextureReader(texture_ka_file_name);
      
      if(texture_reader.asRead())
        ambiente_texture = texture_reader.getValue();
      else
      {
        SLog.logWriteLine("Message STextureOpenGLMaterial - La texture de r�flexion ambiante '" + texture_ka_file_name + "' n'a pas �t� charg�e.");  
        ambiente_texture = null;
      }
    }
    else
      ambiente_texture = null;
        
    //Chargement de la texture de r�flexion diffuse
    if(texture_kd_file_name != null)
    {
      STextureReader texture_reader = new STextureReader(texture_kd_file_name);
      
      if(texture_reader.asRead())
        diffuse_texture = texture_reader.getValue();
      else
      {
        SLog.logWriteLine("Message STextureOpenGLMaterial - La texture de r�flexion diffuse '" + texture_kd_file_name + "' n'a pas �t� charg�e.");
        diffuse_texture = null;
      }
    }
    else
      diffuse_texture = null;
    
    //Chargement de la texture de r�flexion sp�culaire
    if(texture_ks_file_name != null)
    {
      STextureReader texture_reader = new STextureReader(texture_ks_file_name);
      
      if(texture_reader.asRead())
        specular_texture = texture_reader.getValue();
      else
      {
        SLog.logWriteLine("Message STextureOpenGLMaterial - La texture de r�flexion sp�culaire '" + texture_ks_file_name + "' n'a pas �t� charg�e.");
        specular_texture = null;
      }
    }
    else
      specular_texture = null;
  
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur STextureOpenGLMaterial 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  } 
  
  @Override
  public SColor ambientColor(SVectorUV uv)
  {
    if(ambiente_texture == null)
      return ambientColor();
    else
      return COLOR_BASE.multiply(ambiente_texture.getSColor(uv));
  }
  
  @Override
  public SColor diffuseColor(SVectorUV uv)
  {
    if(diffuse_texture == null)
      return diffuseColor();
    else
      return COLOR_BASE.multiply(diffuse_texture.getSColor(uv));
  }
  
  @Override
  public SColor specularColor(SVectorUV uv)
  {
    if(specular_texture == null)
      return specularColor();
    else
      return COLOR_BASE.multiply(specular_texture.getSColor(uv));
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
    throw new SNoImplementationException("Erreur STextureOpenGLMaterial 002 : La m�thode n'a pas �t� impl�ment�e.");
  }

  @Override
  public boolean isUVFormatSelected()
  {
    return is_uv_format_selected;
  }
  
}//fin de la classe STextureOpenGLMaterial
