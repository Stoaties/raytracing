/**
 * 
 */
package sim.geometry.space;

import java.util.ArrayList;
import java.util.List;

import sim.exception.SNoImplementationException;
import sim.geometry.SAbstractGeometry;
import sim.geometry.SCubeGeometry;
import sim.geometry.SDiskGeometry;
import sim.geometry.SGeometry;
import sim.geometry.SSphereGeometry;
import sim.geometry.STorusGeometry;
import sim.geometry.STransformableGeometry;
import sim.geometry.STriangleGeometry;
import sim.geometry.STubeGeometry;
import sim.math.SAffineTransformation;
import sim.math.SMath;
import sim.math.SMatrix4x4;
import sim.math.SVector3d;

/**
 * La classe <b>SBoundingBoxBuilder</b> repr�sente un constructeur de bo�te englobante autour de diff�rentes g�om�tries.
 * 
 * <p>
 * Les g�om�tries pouvant �tre englob�es par une bo�tes sont les suivantes :
 * <ul>- STriangleGeometry</ul>
 * <ul>- SBtriangleGeometry</ul>
 * <ul>- SSphereGeometry</ul>
 * <ul>- STubeGeometry</ul>
 * <ul>- SCylinderGeometry</ul>
 * <ul>- SConeGeometry</ul>
 * <ul>- SCubeGeometry</ul>
 * <ul>- SDiskGeometry</ul>
 * <p>
 * 
 * @author Simon V�zina
 * @since 2015-08-04
 * @since 2017-12-20 (version labo � Le ray tracer v2.1)
 */
public class SBoundingBoxBuilder {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>POSITIVE_MULTIPLICATOR</b> correspond � une constante faiblement sup�rieure � 1.0. 
   */
  private static final double POSITIVE_MULTIPLICATOR = 1.0 + 10.0*SMath.EPSILON;
  
  /**
   * La constante <b>POSITIVE_MULTIPLICATOR</b> correspond � une constante faiblement inf�rieure � 1.0. 
   */
  private static final double NEGATIVE_MULTIPLICATOR = 1.0 - 10.0*SMath.EPSILON;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un fabriquant de bo�te englobante par d�faut. 
   */
  public SBoundingBoxBuilder()
  {
    
  }

  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir une bo�te englobante pour une g�om�trie. 
   * Si la g�om�trie n'est pas admissible � recevoir une bo�te englobante, la r�f�rence <b>null</b> sera retourn�e.
   * 
   * @param geometry La g�om�trie � �tre embo�t�e.
   * @return La bo�te englobante autour de la g�om�trie ou <b>null</b> si aucune bo�te n'a �t� construite.
   */
  public SBoundingBox buildBoundingBox(SGeometry geometry)
  {
    switch(geometry.getCodeName())
    {
      case SAbstractGeometry.TRIANGLE_CODE : 
      case SAbstractGeometry.BTRIANGLE_CODE :     return buildBoundingBoxForSTriangleGeometry((STriangleGeometry)geometry);
      
      case SAbstractGeometry.TRANSFORMABLE_CODE : return buildBoundingBoxForSTransformableGeometry((STransformableGeometry)geometry);
          
      case SAbstractGeometry.SPHERE_CODE :        return buildBoundingBoxForSSphereGeometry((SSphereGeometry)geometry);
      
      case SAbstractGeometry.TUBE_CODE : 
      case SAbstractGeometry.CYLINDER_CODE :  
      case SAbstractGeometry.CONE_CODE :          return buildBoundingBoxForSTubeGeometry((STubeGeometry)geometry);
      
      case SAbstractGeometry.CUBE_CODE :          return buildBoundingBoxForSCubeGeometry((SCubeGeometry)geometry);
      
      case SAbstractGeometry.DISK_CODE :          return buildBoundingBoxForSDiskGeometry((SDiskGeometry)geometry);
      
      case SAbstractGeometry.TORUS_CODE :         return buildBoundingBoxForSTorusGeometry((STorusGeometry)geometry);
      
      default : return null;
    }
  }
  
  /**
   * M�thode pour obtenir une bo�te englobante autour de la g�om�trie du STriangleGeometry.
   * 
   * @param triangle La g�om�trie du triangle.
   * @return La bo�te englobante autour du triangle.
   */
  private SBoundingBox buildBoundingBoxForSTriangleGeometry(STriangleGeometry triangle)
  {
    SVector3d[] tab = { triangle.getP0(), triangle.getP1(), triangle.getP2() };
    
    SVector3d min = SVector3d.findMinValue(tab);
    SVector3d max = SVector3d.findMaxValue(tab);
    
    //Si le triangle est parall�le � l'un des 6 plans de la bo�te, la bo�te sera mal d�finie.
    //Modifions par un "delta" nos coordonn�es pour �viter cette situation  
    return new SBoundingBox(triangle, buildMinValue(min), buildMaxValue(max));
  }
   
  /**
   * M�thode pour obtenir une bo�te englobante autour de la g�om�trie du STransformableGeometry.
   * 
   * @param geometry La g�om�trie transformable.
   * @return La bo�te englobante autour de la g�om�trie transformable. Elle sera <b>null</b> si la g�om�trie ne peut pas avoir de bo�te englobante.
   */
  private SBoundingBox buildBoundingBoxForSTransformableGeometry(STransformableGeometry geometry)
  {
    // G�n�rer l'ensemble des huit points d�signant la bo�te englobante
    List<SVector3d> list = generateBoundingBoxListPoint(geometry);
    
    // Si la g�om�trie n'est pas support�e, il faut retourner une bo�te 'null'.
    if(list.isEmpty())
      return null;
    
    return new SBoundingBox(geometry, list);
  }
  
  /**
   * M�thode pour g�n�rer une liste de huit points d�signant la bo�te englobante.
   * Ces points seront transform�s selon les matrices de transformation de la g�om�trie transformable 
   * afin de s'assurer de la bonne dimension de la bo�te englobante apr�s transformation.
   * 
   * @param geometry La g�om�trie transformable.
   * @return Une liste de point correspondant � la bo�te englobante de la g�om�trie transformable.
   */
  private List<SVector3d> generateBoundingBoxListPoint(STransformableGeometry geometry)
  {
    // La liste des points avant la transformation
    List<SVector3d> list;
    
    // La g�om�trie interne � la g�om�trie transformable
    SGeometry inside_geometry = geometry.getGeometry();
    
    // V�rifier s'il y a une g�om�trie interne � la g�om�trie transformable
    if(inside_geometry == null)
      return new ArrayList<SVector3d>();
    
    // Obtenir la liste des huit points de la bo�te englobante
    // Il faudra g�n�rer de fa�on r�cursive pour le cas d'une g�om�trie transformable dans une g�om�trie transformable.
    // Il faudra g�n�rer la bo�te englobante d'une g�om�trie quelconque et obenir ses huit points sinon. 
    if(inside_geometry.getCodeName() == SAbstractGeometry.TRANSFORMABLE_CODE)
      list = generateBoundingBoxListPoint((STransformableGeometry)inside_geometry);
    else
    {
      SBoundingBox box = buildBoundingBox(inside_geometry);
      
      // V�rifier si la g�om�trie peut avoir une bo�te englobante, donc bo�te non null
      if(box == null)
        return new ArrayList<SVector3d>();
      
      list = box.getListPoint();
    }
    
    // Transformer la liste des points d�finissant la bo�te englobante.
    return transformBoundingBoxVertex(geometry, list);
  }
  
  /**
   * M�thode pour transformer la liste des huits points d�finissant la bo�te englobante autour d'une g�om�trie transformable
   * afin de les apporter dans l'espace monde de la g�om�trie.
   * 
   * @param geometry La g�om�trie transformable.
   * @param vertex_list La liste des huits sommets de la bo�te enblobante.
   * @return La liste des huits sommet de la bo�te englobante dans l'espace monde de la g�om�trie.
   */
  private List<SVector3d> transformBoundingBoxVertex(STransformableGeometry geometry, List<SVector3d> vertex_list)
  {
    throw new SNoImplementationException("C'est m�thode n'a pas �t� impl�ment�e.");
  }
    
  /**
   * M�thode pour obtenir une bo�te englobante autour de la g�om�trie du SSphereGeometry.
   * 
   * @param sphere La sph�re.
   * @return La bo�te englobante de la sph�re.
   */
  private SBoundingBox buildBoundingBoxForSSphereGeometry(SSphereGeometry sphere)
  {
    SVector3d position = sphere.getPosition();
    double positive_R = sphere.getRay();
    double negative_R = -1*positive_R;
    
    SVector3d min = position.add( new SVector3d(negative_R, negative_R, negative_R));
    SVector3d max = position.add( new SVector3d(positive_R, positive_R, positive_R));
    
    return new SBoundingBox(sphere, buildMinValue(min), buildMaxValue(max));
  }
  
  /**
   * M�thode pour obtenir une bo�te englobante autour de la g�om�trie du SDiskGeometry.
   * 
   * @param disk Le disque.
   * @return La bo�te englobante du disque.
   */
  private SBoundingBox buildBoundingBoxForSDiskGeometry(SDiskGeometry disk)
  {
    // L'impl�mentation sera identique � cette de la sph�re, 
    // car un disque est enti�rement localis� � l'int�rieur d'une sph�re peut importe l'orientation de sa normale.
    // Ce choix n'est cependant pas optimal !!!
    // Exploiter l'orientation de la normale � la surface pour limiter la taille de la bo�te serait une bonne id�e.
    
    SVector3d position = disk.getPosition();
    double positive_R = disk.getRay();
    double negative_R = -1*positive_R;
    
    SVector3d min = position.add( new SVector3d(negative_R, negative_R, negative_R));
    SVector3d max = position.add( new SVector3d(positive_R, positive_R, positive_R));
    
    return new SBoundingBox(disk, buildMinValue(min), buildMaxValue(max));
  }
  
  /**
   * M�thode pour obtenir une bo�te englobante autour de la g�om�trie du STubeGeometry.
   * 
   * @param tube Le tube.
   * @return La bo�te englobante du tube.
   */
  private SBoundingBox buildBoundingBoxForSTubeGeometry(STubeGeometry tube)
  {
    SVector3d[] tab = { tube.getP1(), tube.getP2() };
    
    SVector3d min = SVector3d.findMinValue(tab);
    SVector3d max = SVector3d.findMaxValue(tab);
    
    double positive_R = tube.getRay();
    double negative_R = -1*positive_R;
    
    min = min.add( new SVector3d(negative_R, negative_R, negative_R));
    max = max.add( new SVector3d(positive_R, positive_R, positive_R));
    
    return new SBoundingBox(tube, buildMinValue(min), buildMaxValue(max));
  }
  
  /**
   * M�thode pour obtenir une bo�te englobante autour de la g�om�trie du STubeGeometry.
   * 
   * @param tube Le tube.
   * @return La bo�te englobante du tube.
   */
  private SBoundingBox buildBoundingBoxForSCubeGeometry(SCubeGeometry cube)
  {
    SVector3d position = cube.getPosition();
    
    double positive_R = cube.getSize() / 2.0;
    double negative_R = -1*positive_R;
    
    SVector3d min = position.add( new SVector3d(negative_R, negative_R, negative_R));
    SVector3d max = position.add( new SVector3d(positive_R, positive_R, positive_R));
    
    return new SBoundingBox(cube, buildMinValue(min), buildMaxValue(max));
  }
  
  /**
   * M�thode pour obtenir une bo�te englobante autour de la g�om�trie du STorusGeometry.
   * 
   * @param torus Le tore.
   * @return La bo�te englobante du tore.
   */
  private SBoundingBox buildBoundingBoxForSTorusGeometry(STorusGeometry torus)
  {
    SVector3d position = torus.getPosition();
        
    double positive_R = torus.getMajorRadius() + 2.0*torus.getMinorRadius(); // on va pas prendre de chance ...
    double negative_R = -1*positive_R;
    
    SVector3d min = position.add( new SVector3d(negative_R, negative_R, negative_R));
    SVector3d max = position.add( new SVector3d(positive_R, positive_R, positive_R));
    
    return new SBoundingBox(torus, buildMinValue(min), buildMaxValue(max));
  }
  
  /**
   * M�thode pour g�n�rer un vecteur minimal avec un l�ger d�placement le rendant encore <b>plus petit</b>.
   * 
   * @param min Le vecteur minimal de r�f�rence.
   * @return Le vecteur minimal l�g�rement plus petit.
   */
  private SVector3d buildMinValue(SVector3d min)
  {
    double x,y,z;
    
    if(min.getX() < 0)
      x = min.getX() * POSITIVE_MULTIPLICATOR;
    else
      x = min.getX() * NEGATIVE_MULTIPLICATOR;
    
    if(min.getY() < 0)
      y = min.getY() * POSITIVE_MULTIPLICATOR;
    else
      y = min.getY() * NEGATIVE_MULTIPLICATOR;
    
    if(min.getZ() < 0)
      z = min.getZ() * POSITIVE_MULTIPLICATOR;
    else
      z = min.getZ() * NEGATIVE_MULTIPLICATOR;
    
    return new SVector3d(x, y, z);
  }
  
  /**
   * M�thode pour g�n�rer un vecteur maximal avec un l�ger d�placement le rendant encore <b>plus grand</b>.
   * 
   * @param min Le vecteur maximal de r�f�rence.
   * @return Le vecteur minimal l�g�rement plus petit.
   */
  private SVector3d buildMaxValue(SVector3d max)
  {
    double x,y,z;
    
    if(max.getX() > 0)
      x = max.getX() * POSITIVE_MULTIPLICATOR;
    else
      x = max.getX() * NEGATIVE_MULTIPLICATOR;
    
    if(max.getY() > 0)
      y = max.getY() * POSITIVE_MULTIPLICATOR;
    else
      y = max.getY() * NEGATIVE_MULTIPLICATOR;
    
    if(max.getZ() > 0)
      z = max.getZ() * POSITIVE_MULTIPLICATOR;
    else
      z = max.getZ() * NEGATIVE_MULTIPLICATOR;
    
    return new SVector3d(x, y, z);
  }
  
}//fin de la classe SBoundingBoxBuilder
