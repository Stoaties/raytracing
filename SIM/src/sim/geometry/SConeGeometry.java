/**
 * 
 */
package sim.geometry;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.exception.SNoImplementationException;
import sim.graphics.SPrimitive;
import sim.math.SVector3d;
import sim.math.SVectorUV;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SStringUtil;

/**
 * La classe </b>SConeGeometry</b> représentant la géométrie d'un cône. 
 * La base du cône est située en son point P1 et la pointe du cône est située en son point P2. 
 * 
 * @author Simon Vézina
 * @since 2015-08-11
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public class SConeGeometry extends STubeGeometry {

  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>disk</b> correspond au disque situé à la base du cône.
   */
  private SDiskGeometry disk; 
  
  //---------------------------
  // Paramètres de précalcul //
  //---------------------------
  
  /**
   * La variable <b>H</b> correspond à la hauteur du cône. 
   */
  private double H;           
 
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * Construction d'un cône par défaut. 
   */
  public SConeGeometry()
  {
    this(DEFAULT_P1, DEFAULT_P2, DEFAULT_R);
  }

  /**
   * Constructeur d'un cône.
   * 
   * @param P1 - Le point de la base du cône. 
   * @param P2 - Le point su sommet du cône.
   * @param R - Le rayon de la base du cône.
   */
  public SConeGeometry(SVector3d P1, SVector3d P2, double R)
  {
    this(P1, P2, R, null);
  }

  /**
   * Constructeur d'un cône avec une primitive comme parent.
   * 
   * @param P1 - Le point de la base du cône. 
   * @param P2 - Le point su sommet du cône.
   * @param R - Le rayon de la base du cône.
   * @param parent - La primitive en parent.
   * @throws SConstructorException S'il y a eu une erreur lors de la construction de la géométrie.
   */
  public SConeGeometry(SVector3d P1, SVector3d P2, double R, SPrimitive parent)throws SConstructorException
  {
    super(P1, P2, R, parent);
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SConeGeometry 001 : Une erreur lors de l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());
    }
  }
  
  /**
   * Constructeur du cône à partir d'information lue dans un fichier de format txt.
   * Puisqu'une géométrie est construite à l'intérieure d'une primitive, une référence à celle-ci doit être intégrée au constructeur pour y a voir accès.
   *
   * @param sbr - Le BufferedReader cherchant l'information dans le fichier txt.
   * @param parent - La primitive qui fait la construction de cette géométrie (qui est le parent).
   * @throws IOException Si une erreur de de type I/O est lancée.
   * @throws SConstructorException S'il y a eu une erreur lors de la construction de la géométrie.
   * @see SBufferedReader
   * @see SPrimitive
   */
  public SConeGeometry(SBufferedReader sbr, SPrimitive parent)throws IOException, SConstructorException
  {
    super(sbr, parent);
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SConeGeometry 002 : Une erreur lors de l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());
    }
  }

  //------------
  // MÉTHODES //
  //------------
  
  @Override
  public int getCodeName()
  {
    return SAbstractGeometry.CONE_CODE;
  }

  @Override
  public boolean isClosedGeometry()
  {
    return true;
  }

  @Override
  public SRay intersection(SRay ray) throws SAlreadyIntersectedRayException
  {
    return ray;
  }
    
  /**
   * Méthode permettant d'évaluer l'intersection entre un rayon et la partie cônique du cône.
   * 
   * @param ray - Le rayon à intersecter.
   * @return Le rayon avec les caractéristiques de l'intersection entre avec la partie cônique du cône.
   */
  private SRay conicIntersection(SRay ray)
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
  @Override
  public boolean isInside(SVector3d v)
  {
    return SGeometricUtil.isOnConeSurface(P1, S12, R, H, v) < 0;      
  }
  
  @Override
  protected SVector3d evaluateIntersectionNormal(SRay ray, double intersection_t)
  {
    return SGeometricUtil.outsideTwoInfinitesConesNormal(P1, S12, R, H, ray.getPosition(intersection_t));
  }

  /**
   * Méthode pour faire l'initialisation de l'objet après sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    H = P2.substract(P1).modulus();                               // hauteur du cône
    
    // Définir le disque avec une normale à la surface extérieur au cône.
    disk = new SDiskGeometry(P1, S12.multiply(-1.0), R, null);    
  }

  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_CONE);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //Écrire les propriétés de la classe SCercleGeometry et ses paramètres hérités
    writeSTubeGeometryParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }

 
  @Override
  protected SVectorUV evaluateIntersectionUV(SRay ray, double intersection_t)
  {
    throw new SNoImplementationException("Erreur SConeGeometry 004 : Cette méthode n'a pas encore été implémentée.");
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
    return SKeyWordDecoder.KW_CONE;
  }
  
}//fin de la classe SConeGeometry
