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
 * La classe <b>SModelAGPLoader</b> permet de charge un modèle de format .agp étant le format de modèle du <i>projet Anaglyphe</i> réalisé par Anik Soulière.
 * 
 * @author Simon Vézina
 * @since 2015-08-01
 * @version 2015-05-12
 */
public class SModelAGPLoader implements SStringLoader {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>FILE_EXTENSION</b> correspond au nom de l'extension du fichier de lecture de ce type de modèle 3d.
   */
  public static final String FILE_EXTENSION = "agp";  
  
  /**
   * La constante <b>DEFAULT_COLOR</b> correspond à la couleur par défaut du modèle 3d.
   */
  private static final SColor DEFAULT_COLOR = new SColor(0.8, 0.8, 0.2);
  
  /**
   * La constante <b>DEFAULT_RAY</b> correspond au rayon par défaut des segment utilisé pour représenter le modèle 3d.
   */
  private static final double DEFAULT_RAY = 0.1;
  
  /**
   * La constante <b>TUBE_SEGMENT_CODE</b> correspond au code de référence pour construire des segments du modèle 3d à l'aide de tube.
   */
  public static final int TUBE_SEGMENT_CODE = 0;
  
  /**
   * La constante <b>CYLINDER_SEGMENT_CODE</b> correspond au code de référence pour construire des segments du modèle 3d à l'aide de cylindre.
   */
  public static final int CYLINDER_SEGMENT_CODE = 1;
 
  /**
   * La constante <b>CONE_SEGMENT_CODE</b> correspond au code de référence pour construire des segments du modèle 3d à l'aide de cylindre.
   */
  public static final int CONE_SEGMENT_CODE = 3;
  
  /**
   * La constante <b>DEFAULT_SEGMENT_CODE</b> correspond au code de référence pour constuire des segments par défaut état égal à {@value}.
   */
  public static final int DEFAULT_SEGMENT_CODE = TUBE_SEGMENT_CODE;
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>segment_code</b> correspond au code de référence utilisé pour choisir quel type 
   * de segement sera construit lors de la construction du modèle 3d.
   */
  private final int segment_code;
  
  /**
   * La variable <b>error</b> correspond au nombre d'erreur lors de lecture du modèle 3d.
   */
  private int error;
  
  //-----------------
  // CONSTRUCTUEUR //
  //-----------------
  
  /**
   *  Constructeur d'un chargeur de modèle 3d de format agp avec segment de forme par défaut. 
   */
  public SModelAGPLoader()
  {
    this(DEFAULT_SEGMENT_CODE);
  }

  /**
   * Constructeur d'un chargeur de modèle 3d de format agp. 
   * 
   * @param segment_code Le code de référence pour déterminer le type de segment qui sera construit pour réaliser le modèle 3d.
   * @throws SConstructorException Si le code de référence n'est pas reconnu.
   */
  public SModelAGPLoader(int segment_code) throws SConstructorException
  {
    // Vérification du code de segment et affectation si valide
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
  // MÉTHODES //
  //------------
  
  @Override
  public Object load(String string) throws SLoaderException
  {
    try{
      
      SModelAGPParser parser = new SModelAGPParser(string);
      
      SModel model = new SModel(string);
      SMaterial material = new SBlinnMaterial(DEFAULT_COLOR);
      
      // Construire des cylindres à partir de la liste de point des séquences.
      // Puisque les points sont dans l'ordre, il y aura une chaine de cylindre.
      for(SPointSequence s : parser.getListSequence())
      {
        SPoint p1 = s.getPoint(0);  //le 1ier point de la séquence
        SPoint p2;
        
        // Faire un segment avec comme point p2 le suivant, puis p1 devient p2 pour le prochain à faire 
        for(int i = 1; i < s.size(); i++)
        {
          p2 = s.getPoint(i);
             
          try{
           
            // Ajouter la primitive au modèle
            model.addPrimitive(new SPrimitive(buildSegment(p1, p2, DEFAULT_RAY), material));
          
          }catch(SConstructorException e){
            // Compter le nombre d'erreur dans le modèle (des points qui se sont répétés dans la séquence) 
            error++;
          }
               
          p1 = p2;
        }
      }
      
      // Afficher un message dénombrant le nombre d'erreurs
      if(error > 0)
        SLog.logWriteLine("Message SModelAGPLoader : Le modèle " + string + " contient " + error + " erreurs dans la définition du modèle."); 
      
      return model;
      
    }catch(SModelAGPParserException e){
      throw new SLoaderException("Erreur SModelAGPLoader 002 : Une erreur lors de la lecture est survenue ce qui empêche le chargement du modèle 3d. " + SStringUtil.END_LINE_CARACTER + e.getMessage());
    }
  }
  
  /**
   * Méthode pour contruire le type de segment adéquat pour représente le modèle 3d.
   * 
   * @param p1 Le 1ier point du segment.
   * @param p2 Le 2ième point du segment.
   * @param ray Le rayon du segment.
   * @return La géométrie correspondant au segment construit.
   * @throws SRuntimeException Si le code de référence du segment n'est pas traité.
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
   * Méthode pour convertir un SPoint en SVector3d.
   * 
   * @param p Le point à convertir.
   * @return Le vecteur associé au point.
   */
  private SVector3d pointToVector(SPoint p)
  {
    return new SVector3d(p.getX(), p.getY(), p.getZ());
  }
  
}//fin de la classe SModelAGPLoader
