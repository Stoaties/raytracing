/**
 * 
 */
package sim.loader.model;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.geometry.SConeGeometry;
import sim.geometry.SCylinderGeometry;
import sim.geometry.SGeometry;
import sim.geometry.STubeGeometry;
import sim.graphics.SColor;
import sim.graphics.SModel;
import sim.graphics.SPrimitive;
import sim.graphics.material.SBlinnMaterial;
import sim.graphics.material.SMaterial;
import sim.loader.SLoaderException;
import sim.loader.SStringLoader;
import sim.math.SVector3d;
import sim.parser.model.agp.SModelAGPParser;
import sim.parser.model.agp.SModelAGPParserException;
import sim.parser.model.agp.SPoint;
import sim.parser.model.agp.SPointSequence;
import sim.util.SLog;
import sim.util.SStringUtil;

/**
 * La classe <b>SModelAGPLoader</b> permet de charge un mod�le de format .agp �tant le format de mod�le du <i>projet Anaglyphe</i> r�alis� par Anik Souli�re.
 * 
 * @author Simon V�zina
 * @since 2015-08-01
 * @version 2015-05-12
 */
public class SModelAGPLoader implements SStringLoader {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>FILE_EXTENSION</b> correspond au nom de l'extension du fichier de lecture de ce type de mod�le 3d.
   */
  public static final String FILE_EXTENSION = "agp";  
  
  /**
   * La constante <b>DEFAULT_COLOR</b> correspond � la couleur par d�faut du mod�le 3d.
   */
  private static final SColor DEFAULT_COLOR = new SColor(0.8, 0.8, 0.2);
  
  /**
   * La constante <b>DEFAULT_RAY</b> correspond au rayon par d�faut des segment utilis� pour repr�senter le mod�le 3d.
   */
  private static final double DEFAULT_RAY = 0.1;
  
  /**
   * La constante <b>TUBE_SEGMENT_CODE</b> correspond au code de r�f�rence pour construire des segments du mod�le 3d � l'aide de tube.
   */
  public static final int TUBE_SEGMENT_CODE = 0;
  
  /**
   * La constante <b>CYLINDER_SEGMENT_CODE</b> correspond au code de r�f�rence pour construire des segments du mod�le 3d � l'aide de cylindre.
   */
  public static final int CYLINDER_SEGMENT_CODE = 1;
 
  /**
   * La constante <b>CONE_SEGMENT_CODE</b> correspond au code de r�f�rence pour construire des segments du mod�le 3d � l'aide de cylindre.
   */
  public static final int CONE_SEGMENT_CODE = 3;
  
  /**
   * La constante <b>DEFAULT_SEGMENT_CODE</b> correspond au code de r�f�rence pour constuire des segments par d�faut �tat �gal � {@value}.
   */
  public static final int DEFAULT_SEGMENT_CODE = TUBE_SEGMENT_CODE;
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>segment_code</b> correspond au code de r�f�rence utilis� pour choisir quel type 
   * de segement sera construit lors de la construction du mod�le 3d.
   */
  private final int segment_code;
  
  /**
   * La variable <b>error</b> correspond au nombre d'erreur lors de lecture du mod�le 3d.
   */
  private int error;
  
  //-----------------
  // CONSTRUCTUEUR //
  //-----------------
  
  /**
   *  Constructeur d'un chargeur de mod�le 3d de format agp avec segment de forme par d�faut. 
   */
  public SModelAGPLoader()
  {
    this(DEFAULT_SEGMENT_CODE);
  }

  /**
   * Constructeur d'un chargeur de mod�le 3d de format agp. 
   * 
   * @param segment_code Le code de r�f�rence pour d�terminer le type de segment qui sera construit pour r�aliser le mod�le 3d.
   * @throws SConstructorException Si le code de r�f�rence n'est pas reconnu.
   */
  public SModelAGPLoader(int segment_code) throws SConstructorException
  {
    // V�rification du code de segment et affectation si valide
    switch(segment_code)
    {
      case TUBE_SEGMENT_CODE :
      case CYLINDER_SEGMENT_CODE : 
      case CONE_SEGMENT_CODE :      this.segment_code = segment_code;  break;
      
      default : throw new SConstructorException("Erreur SModelAGPLoader 001 : Le code de segment '" + segment_code + "' n'est pas reconnu.");
    }    
    
    error = 0;
  }
  
  //------------
  // M�THODES //
  //------------
  
  @Override
  public Object load(String string) throws SLoaderException
  {
    try{
      
      SModelAGPParser parser = new SModelAGPParser(string);
      
      SModel model = new SModel(string);
      SMaterial material = new SBlinnMaterial(DEFAULT_COLOR);
      
      // Construire des cylindres � partir de la liste de point des s�quences.
      // Puisque les points sont dans l'ordre, il y aura une chaine de cylindre.
      for(SPointSequence s : parser.getListSequence())
      {
        SPoint p1 = s.getPoint(0);  //le 1ier point de la s�quence
        SPoint p2;
        
        // Faire un segment avec comme point p2 le suivant, puis p1 devient p2 pour le prochain � faire 
        for(int i = 1; i < s.size(); i++)
        {
          p2 = s.getPoint(i);
             
          try{
           
            // Ajouter la primitive au mod�le
            model.addPrimitive(new SPrimitive(buildSegment(p1, p2, DEFAULT_RAY), material));
          
          }catch(SConstructorException e){
            // Compter le nombre d'erreur dans le mod�le (des points qui se sont r�p�t�s dans la s�quence) 
            error++;
          }
               
          p1 = p2;
        }
      }
      
      // Afficher un message d�nombrant le nombre d'erreurs
      if(error > 0)
        SLog.logWriteLine("Message SModelAGPLoader : Le mod�le " + string + " contient " + error + " erreurs dans la d�finition du mod�le."); 
      
      return model;
      
    }catch(SModelAGPParserException e){
      throw new SLoaderException("Erreur SModelAGPLoader 002 : Une erreur lors de la lecture est survenue ce qui emp�che le chargement du mod�le 3d. " + SStringUtil.END_LINE_CARACTER + e.getMessage());
    }
  }
  
  /**
   * M�thode pour contruire le type de segment ad�quat pour repr�sente le mod�le 3d.
   * 
   * @param p1 Le 1ier point du segment.
   * @param p2 Le 2i�me point du segment.
   * @param ray Le rayon du segment.
   * @return La g�om�trie correspondant au segment construit.
   * @throws SRuntimeException Si le code de r�f�rence du segment n'est pas trait�.
   */
  private SGeometry buildSegment(SPoint p1, SPoint p2, double ray) throws SRuntimeException
  {
    switch(segment_code)
    {
      case TUBE_SEGMENT_CODE :      return new STubeGeometry(pointToVector(p1), pointToVector(p2), ray); 
        
      case CYLINDER_SEGMENT_CODE :  return new SCylinderGeometry(pointToVector(p1), pointToVector(p2), ray);
      
      case CONE_SEGMENT_CODE :      return new SConeGeometry(pointToVector(p1), pointToVector(p2), ray);
      
      default : throw new SRuntimeException("Erreur SModelAGPLoader 003 : Le code de segment '" + segment_code + "' n'est pas reconnu.");
    }  
  }
  
  /**
   * M�thode pour convertir un SPoint en SVector3d.
   * 
   * @param p Le point � convertir.
   * @return Le vecteur associ� au point.
   */
  private SVector3d pointToVector(SPoint p)
  {
    return new SVector3d(p.getX(), p.getY(), p.getZ());
  }
  
}//fin de la classe SModelAGPLoader
