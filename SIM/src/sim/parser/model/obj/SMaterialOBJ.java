/**
 * 
 */
package sim.parser.model.obj;

/**
 * Classe qui repr�sente la description d'un mat�riel pour un objet 3d de format OBJ (Wavefront).
 * @author Simon V�zina
 * @since 2015-03-28
 * @version 2015-11-05
 */
public class SMaterialOBJ {

  //Param�tres par d�faut accessibles publiquement
  public static final String DEFAULT_NAME = "default";
  public static final SVertex DEFAULT_AMBIENT_COEFFICIENT = new SVertex(0.2f, 0.2f, 0.2f);  
  public static final SVertex DEFAULT_DIFFUSE_COEFFICIENT = new SVertex(0.3f, 0.7f, 0.3f);
  public static final SVertex DEFAULT_SPECULAR_COEFFICIENT = new SVertex(0.2f, 0.2f, 0.2f);
  public static final float DEFAULT_SHININESS = 5.0f;
  
  private final String material_name; //nom du mat�riel
  
  private final SVertex Ka; //coefficient de r�flexion ambiante
  private final SVertex Kd; //coefficient de r�flexion diffuse
  private final SVertex Ks; //coefficient de r�flexion sp�culaire
  private final float Ns;   //constante de brillance (shininess)
  
  private final String texture_Ka;  //nom de la texture de couleur ambiante
  private final String texture_Kd;  //nom de la texture de couleur diffuse
  private final String texture_Ks;  //nom de la texture de couleur sp�culaire
  
  /**
   *  Constructeur d'un mat�riel par d�faut pour objet 3d de format OBJ.
   */
  public SMaterialOBJ()
  {
    this(DEFAULT_NAME, DEFAULT_AMBIENT_COEFFICIENT, DEFAULT_DIFFUSE_COEFFICIENT, DEFAULT_SPECULAR_COEFFICIENT, DEFAULT_SHININESS);
  }
  
  /**
   * Constructeur d'un mat�riel pour objet 3d de format OBJ.
   * 
   * @param material_name - Le nom du mat�riel.
   * @param Ka - Le coefficient de r�flexion ambiante pour les composantes rgb.
   * @param Kd - Le coefficient de r�flexion diffuse pour les composantes rgb.
   * @param Ks - Le coefficient de r�flexion sp�culaire pour les composantes rgb.
   * @param Ns - La constance de brillance (shininess).
   */
  public SMaterialOBJ(String material_name, SVertex Ka, SVertex Kd, SVertex Ks, float Ns)
  {
    this(material_name, Ka, Kd, Ks, Ns, null, null, null);
  }
  
  /**
   * Constructeur d'un mat�riel pour objet 3d de format OBJ avec texture.
   * 
   * @param material_name - Le nom du mat�riel.
   * @param Ka - Le coefficient de r�flexion ambiante pour les composantes rgb.
   * @param Kd - Le coefficient de r�flexion diffuse pour les composantes rgb.
   * @param Ks - Le coefficient de r�flexion sp�culaire pour les composantes rgb.
   * @param Ns - La constance de brillance (shininess).
   * @param texture_Ka - Le nom du fichier comprenant l'information de la texture pour la r�flexion ambiante.
   * @param texture_Kd - Le nom du fichier comprenant l'information de la texture pour la r�flexion diffuse.
   * @param texture_Ks - Le nom du fichier comprenant l'information de la texture pour la r�flexion sp�culaire.
   */
  public SMaterialOBJ(String material_name, SVertex Ka, SVertex Kd, SVertex Ks, float Ns, String texture_Ka, String texture_Kd, String texture_Ks)
  {
    this.material_name = material_name;
    
    this.Ka = Ka;
    this.Kd = Kd;
    this.Ks = Ks;
    this.Ns = Ns;
    
    this.texture_Ka = texture_Ka;
    this.texture_Kd = texture_Kd;
    this.texture_Ks = texture_Ks;
  }
  
  /**
   * M�thode pour obtenir le nom du mat�riel. Le nom par d�faut sera "default" si le mat�riel
   * a �t� construit sans nom particulier.
   * @return Le nom du mat�riel.
   */
  public String getMaterialName()
  {
    return material_name;
  }
  
  /**
   * M�thode pour obtenir les coefficients de r�flexion ambiante du mat�riel.
   * @return Le vertex des coefficients de r�flexion ambiante.
   */
  public SVertex getKa()
  {
    return Ka;
  }
  
  /**
   * M�thode pour obtenir les coefficients de r�flexion diffuse du mat�riel.
   * @return Le vertex des coefficients de r�flexion diffuse.
   */
  public SVertex getKd()
  {
    return Kd;
  }
  
  /**
   * M�thode pour obtenir les coefficients de r�flexion sp�culaire du mat�riel.
   * @return Le vertex des coefficients de r�flexion sp�culaire.
   */
  public SVertex getKs()
  {
    return Ks;
  }
  
  /**
   * M�thode pour obtenir le coefficient de brillance (shininess) du mat�riel.
   * @return Le coefficient de brillance (shininess).
   */
  public float getNs()
  {
    return Ns;
  }
  
  /**
   * M�thode pour obtenir le nom du fichier de la texture pour la r�flexion ambiante.
   * @return Le nom du fichier comprenant l'information de la texture pour la r�flexion ambiante. 
   * Le nom du fichier sera <b>null</b> s'il n'y a pas de texture affect�e.
   */
  public String getTextureKaFileName()
  {
    return texture_Ka;
  }
  
  /**
   * M�thode pour obtenir le nom du fichier de la texture pour la r�flexion diffuse.
   * @return Le nom du fichier comprenant l'information de la texture pour la r�flexion diffuse.
   * Le nom du fichier sera <b>null</b> s'il n'y a pas de texture affect�e.
   */
  public String getTextureKdFileName()
  {
    return texture_Kd;
  }
  
  /**
   * M�thode pour obtenir le nom du fichier de la texture pour la r�flexion sp�culaire.
   * @return Le nom du fichier comprenant l'information de la texture pour la r�flexion sp�culaire.
   * Le nom du fichier sera <b>null</b> s'il n'y a pas de texture affect�e.
   */
  public String getTextureKsFileName()
  {
    return texture_Ks;
  }
  
  /**
   * M�thode pour d�terminer si le mat�riel contient une r�f�rence � une texture. 
   * @return <b>true</b> si la mat�riel contient une texture et <b>false</b> sinon.
   */
  public boolean asTexture()
  {
    if(texture_Ka != null)
      return true;
    
    if(texture_Kd != null)
      return true;
    
    if(texture_Ks != null)
      return true;
    
    return false;
  }
  
}//fin SMaterialOBJ
