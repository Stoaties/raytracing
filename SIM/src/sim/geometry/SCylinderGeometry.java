/**
 * 
 */
package sim.geometry;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.graphics.SPrimitive;
import sim.math.SVector3d;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SStringUtil;

/**
 * La classe <b>SCylinderGeometry</b> représente la géométrie d'un cylindre. 
 * Héritant de la classe STubeGeometry, le cylindre possède deux disques fermant les deux extrémités du tube.
 * Le cylindre est une géométrie fermée.
 * 
 * @author Simon Vézina
 * @since 2015-07-07
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public class SCylinderGeometry extends STubeGeometry {

  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un cylindre par défaut. 
   */
  public SCylinderGeometry()
  {
    this(DEFAULT_P1, DEFAULT_P2, DEFAULT_R);
  }

  /**
   * Constructeur d'un cylindre.
   * 
   * @param P1 - Le 1ier point du cylindre. 
   * @param P2 - Le 2ième point du cylindre.
   * @param R - Le rayon du cylindre.
   */
  public SCylinderGeometry(SVector3d P1, SVector3d P2, double R)
  {
    this(P1, P2, R, null);
  }

  /**
   * Constructeur d'un tube avec une primitive comme parent.
   * 
   * @param P1 - Le 1ier point du cylindre. 
   * @param P2 - Le 2ième point du cylindre.
   * @param R - Le rayon du cylindre.
   * @param parent - La primitive en parent.
   * @throws SConstructorException Si une erreur lors de la construction de la géométrie est survenue.
   */
  public SCylinderGeometry(SVector3d P1, SVector3d P2, double R, SPrimitive parent) throws SConstructorException
  {
    super(P1, P2, R, parent);
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SCylinderGeometry 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
  }

  /**
   * Constructeur d'un cylindre à partir d'information lue dans un fichier de format txt.
   * Puisqu'une géométrie est construite à l'intérieure d'une primitive, une référence à celle-ci doit être intégrée au constructeur pour y a voir accès.
   * @param sbr - Le BufferedReader cherchant l'information dans le fichier txt.
   * @param parent - La primitive qui fait la construction de cette géométrie (qui est le parent).
   * @throws IOException Si une erreur de de type I/O est lancée.
   * @see SBufferedReader
   * @see SPrimitive
   */
  public SCylinderGeometry(SBufferedReader sbr, SPrimitive parent) throws IOException, SConstructorException
  {
    super(sbr, parent);
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SCylinderGeometry 002 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    } 
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
  public int getCodeName()
  {
    return SAbstractGeometry.CYLINDER_CODE;
  }
  
  @Override
  public SRay intersection(SRay ray) throws SAlreadyIntersectedRayException
  {
    return ray;
  }
  
  @Override
  public boolean isClosedGeometry()
  {
    return true;
  }
  
  @Override
  public boolean isInside(SVector3d v)
  {
    return SGeometricUtil.isOnCylinderSurface(P1, P2, R, v) < 0;
  }
    
  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_CYLINDER);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //Écrire les propriétés de la classe SCercleGeometry et ses paramètres hérités
    writeSTubeGeometryParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
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
    return SKeyWordDecoder.KW_CYLINDER;
  }
  
}//fin classe SCylinderGeometry
