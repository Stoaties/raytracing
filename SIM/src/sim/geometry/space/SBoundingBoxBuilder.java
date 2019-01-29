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
 * La classe <b>SBoundingBoxBuilder</b> représente un constructeur de boîte englobante autour de différentes géométries.
 * 
 * <p>
 * Les géométries pouvant être englobées par une boîtes sont les suivantes :
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
 * @author Simon Vézina
 * @since 2015-08-04
 * @since 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public class SBoundingBoxBuilder {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>POSITIVE_MULTIPLICATOR</b> correspond à une constante faiblement supérieure à 1.0. 
   */
  private static final double POSITIVE_MULTIPLICATOR = 1.0 + 10.0*SMath.EPSILON;
  
  /**
   * La constante <b>POSITIVE_MULTIPLICATOR</b> correspond à une constante faiblement inférieure à 1.0. 
   */
  private static final double NEGATIVE_MULTIPLICATOR = 1.0 - 10.0*SMath.EPSILON;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un fabriquant de boîte englobante par défaut. 
   */
  public SBoundingBoxBuilder()
  {
    
  }

  //------------
  // MÉTHODES //
  //------------
  
  /**
   * Méthode pour obtenir une boîte englobante pour une géométrie. 
   * Si la géométrie n'est pas admissible à recevoir une boîte englobante, la référence <b>null</b> sera retournée.
   * 
   * @param geometry La géométrie à être emboîtée.
   * @return La boîte englobante autour de la géométrie ou <b>null</b> si aucune boîte n'a été construite.
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
   * Méthode pour obtenir une boîte englobante autour de la géométrie du STriangleGeometry.
   * 
   * @param triangle La géométrie du triangle.
   * @return La boîte englobante autour du triangle.
   */
  private SBoundingBox buildBoundingBoxForSTriangleGeometry(STriangleGeometry triangle)
  {
    SVector3d[] tab = { triangle.getP0(), triangle.getP1(), triangle.getP2() };
    
    SVector3d min = SVector3d.findMinValue(tab);
    SVector3d max = SVector3d.findMaxValue(tab);
    
    //Si le triangle est parallèle à l'un des 6 plans de la boîte, la boîte sera mal définie.
    //Modifions par un "delta" nos coordonnées pour éviter cette situation  
    return new SBoundingBox(triangle, buildMinValue(min), buildMaxValue(max));
  }
   
  /**
   * Méthode pour obtenir une boîte englobante autour de la géométrie du STransformableGeometry.
   * 
   * @param geometry La géométrie transformable.
   * @return La boîte englobante autour de la géométrie transformable. Elle sera <b>null</b> si la géométrie ne peut pas avoir de boîte englobante.
   */
  private SBoundingBox buildBoundingBoxForSTransformableGeometry(STransformableGeometry geometry)
  {
    // Générer l'ensemble des huit points désignant la boîte englobante
    List<SVector3d> list = generateBoundingBoxListPoint(geometry);
    
    // Si la géométrie n'est pas supportée, il faut retourner une boîte 'null'.
    if(list.isEmpty())
      return null;
    
    return new SBoundingBox(geometry, list);
  }
  
  /**
   * Méthode pour générer une liste de huit points désignant la boîte englobante.
   * Ces points seront transformés selon les matrices de transformation de la géométrie transformable 
   * afin de s'assurer de la bonne dimension de la boîte englobante après transformation.
   * 
   * @param geometry La géométrie transformable.
   * @return Une liste de point correspondant à la boîte englobante de la géométrie transformable.
   */
  private List<SVector3d> generateBoundingBoxListPoint(STransformableGeometry geometry)
  {
    // La liste des points avant la transformation
    List<SVector3d> list;
    
    // La géométrie interne à la géométrie transformable
    SGeometry inside_geometry = geometry.getGeometry();
    
    // Vérifier s'il y a une géométrie interne à la géométrie transformable
    if(inside_geometry == null)
      return new ArrayList<SVector3d>();
    
    // Obtenir la liste des huit points de la boîte englobante
    // Il faudra générer de façon récursive pour le cas d'une géométrie transformable dans une géométrie transformable.
    // Il faudra générer la boîte englobante d'une géométrie quelconque et obenir ses huit points sinon. 
    if(inside_geometry.getCodeName() == SAbstractGeometry.TRANSFORMABLE_CODE)
      list = generateBoundingBoxListPoint((STransformableGeometry)inside_geometry);
    else
    {
      SBoundingBox box = buildBoundingBox(inside_geometry);
      
      // Vérifier si la géométrie peut avoir une boîte englobante, donc boîte non null
      if(box == null)
        return new ArrayList<SVector3d>();
      
      list = box.getListPoint();
    }
    
    // Transformer la liste des points définissant la boîte englobante.
    return transformBoundingBoxVertex(geometry, list);
  }
  
  /**
   * Méthode pour transformer la liste des huits points définissant la boîte englobante autour d'une géométrie transformable
   * afin de les apporter dans l'espace monde de la géométrie.
   * 
   * @param geometry La géométrie transformable.
   * @param vertex_list La liste des huits sommets de la boîte enblobante.
   * @return La liste des huits sommet de la boîte englobante dans l'espace monde de la géométrie.
   */
  private List<SVector3d> transformBoundingBoxVertex(STransformableGeometry geometry, List<SVector3d> vertex_list)
  {
    throw new SNoImplementationException("C'est méthode n'a pas été implémentée.");
  }
    
  /**
   * Méthode pour obtenir une boîte englobante autour de la géométrie du SSphereGeometry.
   * 
   * @param sphere La sphère.
   * @return La boîte englobante de la sphère.
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
   * Méthode pour obtenir une boîte englobante autour de la géométrie du SDiskGeometry.
   * 
   * @param disk Le disque.
   * @return La boîte englobante du disque.
   */
  private SBoundingBox buildBoundingBoxForSDiskGeometry(SDiskGeometry disk)
  {
    // L'implémentation sera identique à cette de la sphère, 
    // car un disque est entièrement localisé à l'intérieur d'une sphère peut importe l'orientation de sa normale.
    // Ce choix n'est cependant pas optimal !!!
    // Exploiter l'orientation de la normale à la surface pour limiter la taille de la boîte serait une bonne idée.
    
    SVector3d position = disk.getPosition();
    double positive_R = disk.getRay();
    double negative_R = -1*positive_R;
    
    SVector3d min = position.add( new SVector3d(negative_R, negative_R, negative_R));
    SVector3d max = position.add( new SVector3d(positive_R, positive_R, positive_R));
    
    return new SBoundingBox(disk, buildMinValue(min), buildMaxValue(max));
  }
  
  /**
   * Méthode pour obtenir une boîte englobante autour de la géométrie du STubeGeometry.
   * 
   * @param tube Le tube.
   * @return La boîte englobante du tube.
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
   * Méthode pour obtenir une boîte englobante autour de la géométrie du STubeGeometry.
   * 
   * @param tube Le tube.
   * @return La boîte englobante du tube.
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
   * Méthode pour obtenir une boîte englobante autour de la géométrie du STorusGeometry.
   * 
   * @param torus Le tore.
   * @return La boîte englobante du tore.
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
   * Méthode pour générer un vecteur minimal avec un léger déplacement le rendant encore <b>plus petit</b>.
   * 
   * @param min Le vecteur minimal de référence.
   * @return Le vecteur minimal légèrement plus petit.
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
   * Méthode pour générer un vecteur maximal avec un léger déplacement le rendant encore <b>plus grand</b>.
   * 
   * @param min Le vecteur maximal de référence.
   * @return Le vecteur minimal légèrement plus petit.
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
