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
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SCubeGeometry</b> repr�sente un cube align� sur les axes x,y et z qui est positionn� par rapport � son centre. 
 * 
 * @author Simon V�zina
 * @since 2015-10-19
 * @version 2017-08-22
 */
public class SCubeGeometry extends SAbstractGeometry {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_POSITION, SKeyWordDecoder.KW_SIZE };
  
  private static final SVector3d PLANE_XY_PLUS_NORMAL = new SVector3d(0.0, 0.0, 1.0);
  private static final SVector3d PLANE_XY_NEG_NORMAL = new SVector3d(0.0, 0.0, -1.0);
  
  private static final SVector3d PLANE_XZ_PLUS_NORMAL = new SVector3d(0.0, 1.0, 0.0);
  private static final SVector3d PLANE_XZ_NEG_NORMAL = new SVector3d(0.0, -1.0, 0.0);
  
  private static final SVector3d PLANE_YZ_PLUS_NORMAL = new SVector3d(1.0, 0.0, 0.0);
  private static final SVector3d PLANE_YZ_NEG_NORMAL = new SVector3d(-1.0, 0.0, 0.0);
   
  /**
   * La constante <b>DEFAULT_POSITION</b> correspond � la position par d�faut d'un cube �tant �gale � l'origine (0,0,0).
   */
  private static final SVector3d DEFAULT_POSITION = new SVector3d(0.0, 0.0, 0.0);
  
  /**
   * La consante <b>DEFAULT_SIZE</b> correspond � la taille par d�faut d'un cube �tant �gale � {@value}.
   */
  private static final double DEFAULT_SIZE = 1.0;
  
  /**
   * La variable <b>position</b> correspond � la position du cube (par rapport � son centre).
   */
  private SVector3d position;
  
  /**
   * La variable <b>size</b> correspond � la taille du cube.
   */
  private double size;
  
  //-----------------------
  //Param�tre utilitaire
  //-----------------------
  
  /**
   * La variable 'positive_half_size' correspond � la moiti� de la valeur <b>positive</b> de la taille (<i>size</i>) du cube . 
   * Cette variable est d�finie � l'initialisation et est utilis�e pour acc�l�rer certains calculs. 
   */
  private double positive_half_size;
  
  /**
   * La variable 'negative_half_size' correspond � la moiti� de la valeur <b>n�gative</b> de la taille (<i>size</i>) du cube . 
   * Cette variable est d�finie � l'initialisation et est utilis�e pour acc�l�rer certains calculs. 
   */
  private double negative_half_size;
  
  private SPlaneGeometry planeXYplus;
  private SPlaneGeometry planeXYneg;
  
  private SPlaneGeometry planeXZplus;
  private SPlaneGeometry planeXZneg;
  
  private SPlaneGeometry planeYZplus;
  private SPlaneGeometry planeYZneg;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   *  Constructeur d'un cube par d�faut. 
   */
  public SCubeGeometry()
  {
    this(DEFAULT_POSITION, DEFAULT_SIZE);
  }

  /**
   * Constructeur d'un cube sans parent primitive.
   * 
   * @param position - La position du cube (par rapport � son centre).
   * @param size - La dimension du cube.
   */
  public SCubeGeometry(SVector3d position, double size)
  {
    this(position, size, null);
  }
  
  /**
   * Constructeur d'un cube.
   * 
   * @param position - La position du cube (par rapport � son centre).
   * @param size - La dimension du cube.
   * @param parent - Le primitive parent au cube.
   * @throws SConstructorException Si la taille (<i>size</i>) du cube est n�gative.
   */
  public SCubeGeometry(SVector3d position, double size, SPrimitive parent)throws SConstructorException
  {
    super(parent);
    
    //V�rification que le rayon soit positif
    if(size < 0.0)
      throw new SConstructorException("Erreur SCubeGeometry 001 : Un cube de dimension size = " + size + " qui est n�gatif n'est pas une d�finition valide.");
    
    this.position = position;
    this.size = size;
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SCubeGeometry 002 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }
  
  /**
   * Constructeur d'une sph�re � partir d'information lue dans un fichier de format txt.
   * Puisqu'une g�om�trie est construite � l'int�rieure d'une primitive, une r�f�rence � celle-ci doit �tre int�gr�e au constructeur pour y a voir acc�s.
   * 
   * @param sbr - Le BufferedReader cherchant l'information dans le fichier txt.
   * @param parent - La primitive qui fait la construction de cette g�om�trie (qui est le parent).
   * @throws IOException Si une erreur de de type I/O est lanc�e.
   * @see SBufferedReader
   * @see SPrimitive
   */
  public SCubeGeometry(SBufferedReader sbr, SPrimitive parent)throws IOException, SConstructorException
  {
    this(DEFAULT_POSITION, DEFAULT_SIZE, parent);    
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SCubeGeometry 003 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }

  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir la position du cube. Cette position correspond au <b>centre</b> du cube.
   * 
   * @return la position du centre du cube.
   */
  public SVector3d getPosition()
  {
    return position;
  }
  
  /**
   * M�thode pour obtenir la taille du cube. Cette mesure correspond � n'importe quel segment du cube �tant tous �gaux. 
   * 
   * @return la taille du cube.
   */
  public double getSize()
  {
    return size;
  }
  
  @Override
  public int getCodeName()
  {
    return SAbstractGeometry.CUBE_CODE;
  }

  @Override
  public boolean isClosedGeometry()
  {
    return true;
  }

  @Override
  public boolean isInside(SVector3d v)
  {
    return SGeometricUtil.isOnAlignedCubeSurface(position, size, v) < 0;
  }

  @Override
  public SRay intersection(SRay ray) throws SAlreadyIntersectedRayException
  {
    //Consid�rons que l'intersection la plus pr�s est un rayon sans intersection
    SRay closer_intersection_ray = ray;
    
    //Effectuons la mise � jour du rayon d'intersection le plus pr�s en testant avec les 6 plans du cube.
    //Une affectation avec l'intersection le plus pr�s sera d�termin� dans l'appel des m�thodes qui suivent.
    closer_intersection_ray = intersectionPlaneXY(ray, closer_intersection_ray, planeXYplus, PLANE_XY_PLUS_NORMAL);
    closer_intersection_ray = intersectionPlaneXY(ray, closer_intersection_ray, planeXYneg, PLANE_XY_NEG_NORMAL);
    
    closer_intersection_ray = intersectionPlaneXZ(ray, closer_intersection_ray, planeXZplus, PLANE_XZ_PLUS_NORMAL);
    closer_intersection_ray = intersectionPlaneXZ(ray, closer_intersection_ray, planeXZneg, PLANE_XZ_NEG_NORMAL);
    
    closer_intersection_ray = intersectionPlaneYZ(ray, closer_intersection_ray, planeYZplus, PLANE_YZ_PLUS_NORMAL);
    closer_intersection_ray = intersectionPlaneYZ(ray, closer_intersection_ray, planeYZneg, PLANE_YZ_NEG_NORMAL);
    
    return closer_intersection_ray;
  }

  /**
   * M�thode qui effectue l'intersection d'un ray avec un plan xy. Si l'intersection est r�alis�e, on compare le temps de l'intersection
   * avec le temps d'intersection d'un autre rayon pour conna�tre la plus rapproch�e. Si la nouvelle intersection est plus pr�s,
   * on �value ses caract�ristiques et l'on retourne un nouveau rayon avec la nouvelle intersection, sinon on retourne l'ancien rayon. 
   *  
   * @param ray - Le rayon r�alisant l'intersection avec le plan xy.
   * @param closer_intersection_ray - Un rayon ayant pr�alablement d�j� r�alis� une intersection avec une autre face du cube.
   * @param plane - Le plan xy � intersect� dont la position z peut diff�rer d'un test � un autre.
   * @param outside_normal - La normale � la surface du plan xy pointant vers l'ext�rieur du cube.
   * @return Un nouveau rayon intersect� si la nouvelle intersection est plus pr�s que l'ancienne intersection, sinon on retourne le rayon <i>closer_intersection_ray</i>. 
   */
  private SRay intersectionPlaneXY(SRay ray, SRay closer_intersection_ray, SPlaneGeometry plane, SVector3d outside_normal)
  {
    // R�aliser le test de l'intersection sur le plan XY plus
    SRay intersection_ray = plane.intersection(ray);
    
    // S'il n'y a pas d'intersection avec le plan xy.
    if(!intersection_ray.asIntersected())
      return closer_intersection_ray;
    
    // �valuons les caract�ristiques de l'intersection uniquement si elle est plus pr�s que l'intersection pr�c�dente
    if(intersection_ray.getT() < closer_intersection_ray.getT())
    {
      // V�rifions si l'intersection est � l'int�rieur du carr� en utilisant une position d'intersection par rapport au centre du cube
      SVector3d p = intersection_ray.getIntersectionPosition().substract(position);
      
      if(    p.getX() < positive_half_size && p.getX() > negative_half_size
          && p.getY() < positive_half_size && p.getY() > negative_half_size)
      {
         return ray.intersection(this, intersection_ray.getOutsideNormal(), intersection_ray.getT());
      }
    }
    
    // Il n'y a pas d'intersection avec le plan xy dans le carr�.
    return closer_intersection_ray;
  }
  
  /**
    * M�thode qui effectue l'intersection d'un ray avec un plan xz. Si l'intersection est r�alis�e, on compare le temps de l'intersection
   * avec le temps d'intersection d'un autre rayon pour conna�tre la plus rapproch�e. Si la nouvelle intersection est plus pr�s,
   * on �value ses caract�ristiques et l'on retourne un nouveau rayon avec la nouvelle intersection, sinon on retourne l'ancien rayon. 
   *  
   * @param ray - Le rayon r�alisant l'intersection avec le plan xz.
   * @param closer_intersection_ray - Un rayon ayant pr�alablement d�j� r�alis� une intersection avec une autre face du cube.
   * @param plane - Le plan xz � intersect� dont la position y peut diff�rer d'un test � un autre.
   * @param outside_normal - La normale � la surface du plan xz pointant vers l'ext�rieur du cube.
   * @return Un nouveau rayon intersect� si la nouvelle intersection est plus pr�s que l'ancienne intersection, sinon on retourne le rayon <i>closer_intersection_ray</i>. 
   */
  private SRay intersectionPlaneXZ(SRay ray, SRay closer_intersection_ray, SPlaneGeometry plane, SVector3d outside_normal)
  {
    // R�aliser le test de l'intersection sur le plan XZ plus
    SRay intersection_ray = plane.intersection(ray);
    
    // S'il n'y a pas d'intersection avec le plan XZ.
    if(!intersection_ray.asIntersected())
      return closer_intersection_ray;
    
    // �valuons les caract�ristiques de l'intersection uniquement si elle est plus pr�s que l'intersection pr�c�dente
    if(intersection_ray.getT() < closer_intersection_ray.getT())
    {
      // V�rifions si l'intersection est � l'int�rieur du carr� en utilisant une position d'intersection par rapport au centre du cube
      SVector3d p = intersection_ray.getIntersectionPosition().substract(position);
      
      if(    p.getX() < positive_half_size && p.getX() > negative_half_size
          && p.getZ() < positive_half_size && p.getZ() > negative_half_size)
      {
        return ray.intersection(this, intersection_ray.getOutsideNormal(), intersection_ray.getT());
      }
    }
    
    // Il n'y a pas d'intersection avec le plan XZ dans le carr�.
    return closer_intersection_ray;
  }
  
  /**
   * M�thode qui effectue l'intersection d'un ray avec un plan yz. Si l'intersection est r�alis�e, on compare le temps de l'intersection
   * avec le temps d'intersection d'un autre rayon pour conna�tre la plus rapproch�e. Si la nouvelle intersection est plus pr�s,
   * on �value ses caract�ristiques et l'on retourne un nouveau rayon avec la nouvelle intersection, sinon on retourne l'ancien rayon. 
   *  
   * @param ray - Le rayon r�alisant l'intersection avec le plan yz.
   * @param closer_intersection_ray - Un rayon ayant pr�alablement d�j� r�alis� une intersection avec une autre face du cube.
   * @param plane - Le plan xz � intersect� dont la position x peut diff�rer d'un test � un autre.
   * @param outside_normal - La normale � la surface du plan yz pointant vers l'ext�rieur du cube.
   * @return Un nouveau rayon intersect� si la nouvelle intersection est plus pr�s que l'ancienne intersection, sinon on retourne le rayon <i>closer_intersection_ray</i>. 
   */
  private SRay intersectionPlaneYZ(SRay ray, SRay closer_intersection_ray, SPlaneGeometry plane, SVector3d outside_normal)
  {
    // R�aliser le test de l'intersection sur le plan YZ plus
    SRay intersection_ray = plane.intersection(ray);
    
    // S'il n'y a pas d'intersection avec le plan YZ.
    if(!intersection_ray.asIntersected())
      return closer_intersection_ray;
    
    // �valuons les caract�ristiques de l'intersection uniquement si elle est plus pr�s que l'intersection pr�c�dente
    if(intersection_ray.getT() < closer_intersection_ray.getT())
    {
      // V�rifions si l'intersection est � l'int�rieur du carr� en utilisant une position d'intersection par rapport au centre du cube
      SVector3d p = intersection_ray.getIntersectionPosition().substract(position);
      
      if(    p.getY() < positive_half_size && p.getY() > negative_half_size
          && p.getZ() < positive_half_size && p.getZ() > negative_half_size)
      {
        return ray.intersection(this, intersection_ray.getOutsideNormal(), intersection_ray.getT());
      }
    }
    
    // Il n'y a pas d'intersection avec le plan YZ dans le carr�.
    return closer_intersection_ray;
  }
  
  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_CUBE);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //�crire les propri�t�s de la classe SSphereGeometry et ses param�tres h�rit�s
    writeSCubeGeometryParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }

  /**
   * M�thode pour �crire les param�tres associ�s � la classe SCubeGeometry et ses param�tres h�rit�s.
   * 
   * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException Si une erreur I/O s'est produite.
   * @see IOException
   */
  protected void writeSCubeGeometryParameter(BufferedWriter bw)throws IOException
  {
    bw.write(SKeyWordDecoder.KW_POSITION);
    bw.write("\t");
    position.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_SIZE);
    bw.write("\t\t");
    bw.write(Double.toString(size));
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
  @Override
  protected SVector3d evaluateIntersectionNormal(SRay ray, double intersection_t)
  {
    throw new SNoImplementationException("Erreur SCubeGeometry 006 : La m�thode n'est pas d�finie.");
  }

  /**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    positive_half_size = size/2;
    negative_half_size = -1*size/2;
    
    // D�finition des plans avec des normales � la surface � l'ext�rieur du cube.
    planeXYplus = new SPlaneGeometry(position.add(new SVector3d(0.0, 0.0, positive_half_size)), PLANE_XY_PLUS_NORMAL);
    planeXYneg = new SPlaneGeometry(position.add(new SVector3d(0.0, 0.0, negative_half_size)), PLANE_XY_NEG_NORMAL);
    
    planeXZplus = new SPlaneGeometry(position.add(new SVector3d(0.0, positive_half_size, 0.0)), PLANE_XZ_PLUS_NORMAL);
    planeXZneg = new SPlaneGeometry(position.add(new SVector3d(0.0, negative_half_size, 0.0)), PLANE_XZ_NEG_NORMAL);
    
    planeYZplus = new SPlaneGeometry(position.add(new SVector3d(positive_half_size, 0.0, 0.0)), PLANE_YZ_PLUS_NORMAL);
    planeYZneg = new SPlaneGeometry(position.add(new SVector3d(negative_half_size, 0.0, 0.0)), PLANE_YZ_NEG_NORMAL); 
  }

  /* (non-Javadoc)
   * @see sim.util.SAbstractReadable#read(sim.util.SBufferedReader, int, java.lang.String)
   */
  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException, IOException
  {
    switch(code)
    {
      case SKeyWordDecoder.CODE_POSITION : position = new SVector3d(remaining_line); return true;
                          
      case SKeyWordDecoder.CODE_SIZE :     size = readDoubleGreaterThanZero(remaining_line, SKeyWordDecoder.KW_SIZE); return true;
          
      default : return false;
    }
  }

  @Override
  protected SVectorUV evaluateIntersectionUV(SRay ray, double intersection_t)
  {
    throw new SNoImplementationException("Erreur SCubeGeometry 007 : La m�thode n'a pas encore �t� impl�ment�e.");
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
    return SKeyWordDecoder.KW_CUBE;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
}//fin de la classe SCubeGeometry
