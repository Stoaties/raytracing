/**
 * 
 */
package sim.geometry.space;

import java.util.ArrayList;
import java.util.List;

import sim.exception.SConstructorException;
import sim.geometry.SGeometry;
import sim.math.SStatistical;
import sim.math.SVector3d;

/**
 * <p>
 * La classe <b>SBoundingBox</b> représente une boîte englobant une géométrie aligné sur les axes x,y et z.
 * Cette boîte contient une référence à une géométrie à l'intérieur de celle-ci.
 * </p>
 * 
 * <p>
 * Puisque la classe <b>SBoundingBox</b> implémente la classe <b>Comparable</b>, elle peut être
 * triée en fonction de sa taille maximale déterminée par la méthode getMaxLenght().
 * </p>
 * 
 * <p>
 * Puisque la classe <b>SBoundingBox</b> implémente la classe <b>SStatistical</b>, elle peut être
 * analysée statistiquement en fonction de sa taille maximale déterminée par la méthode getMaxLenght().
 * </p>
 * 
 * @author Simon Vézina
 * @since 2015-08-04
 * @version 2016-02-07
 */
public class SBoundingBox implements Comparable<SBoundingBox>, SStatistical {

  /**
   * La variable <b>min_point</b> représente la coordonnée (x,y,z) <b>minimale</b> de la boîte.
   * En comparaison avec un carré à deux dimensions, cela représenterait le coin inférieur gauche.
   */
  private final SVector3d min_point;    
  
  /**
   * La variable <b>max_point</b> représente la coordonnée (x,y,z) <b>maximale</b> de la boîte.
   * En comparaison avec un carré à deux dimensions, cela représenterait le coin supérieur droit.
   */
  private final SVector3d max_point;    //la coordonnée (x,y,z) maximale de la boîte
  
  /**
   * La variable <b>geometry</b> correspond à la géométrie à l'intérieur de la boîte englobante.
   */
  private final SGeometry geometry;     
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'une boîte englobante à partir de deux points extrémums.
   * 
   * @param geometry - La géométrie à l'intérieur de la boîte.
   * @param min_point - Le point minimal de la boîte.
   * @param max_point - Le point maximal de la boîte.
   * @throws SConstructorException Si min_point n'est pas totalement inférieur au max_point.
   */
  public SBoundingBox(SGeometry geometry, SVector3d min_point, SVector3d max_point) throws SConstructorException
  {
    if(min_point.getX() > max_point.getX() || min_point.getY() > max_point.getY() || min_point.getZ() > max_point.getZ())
      throw new SConstructorException("Erreur SBoundingBox 001 : Le point min " + min_point.toString() + " n'est pas inférieur au point max " + max_point.toString() + ".");
    
    this.geometry = geometry;
    this.min_point = min_point;
    this.max_point = max_point;
  }

  /**
   * Constructeur d'une boîte englobant à partir de plusieurs point pouvant définir les extrémités de la boîte englobante.
   * 
   * @param geometry La géométrie à l'intérieur de la boîte. 
   * @param point_list La liste des points.
   * @throws SConstructorException Si la liste contient moins de deux points.
   */
  public SBoundingBox(SGeometry geometry, List<SVector3d> point_list) throws SConstructorException
  {
    if(point_list.size() < 2)
      throw new SConstructorException("Erreur SBoundingBox 002 : La list contient " + point_list.size() + " points ce qui est inférieur à 2.");
    
    this.geometry = geometry;
    
    min_point = SVector3d.findMinValue(point_list);
    max_point = SVector3d.findMaxValue(point_list);
  }
  
  //------------
  // MÉTHODES //
  //------------
  
  /**
   * Méthode pour obtenir la géométrie à l'intérieur de la boîte englobante.
   * 
   * @return La géométrie à l'intérieur de la boîte englobante.
   */
  public SGeometry getGeometry()
  {
    return geometry;
  }
  
  /**
   * Méthode pour obtenir la coordonnée minimale de la boîte englobante.
   * 
   * @return Le point minimal de la boîte englobante.
   */
  public SVector3d getMinPoint()
  {
    return min_point;
  }
  
  /**
   * Méthode pour obtenir la coordonnée maximale de la boîte englobante.
   * 
   * @return Le point maximal de la boîte englobante.
   */
  public SVector3d getMaxPoint()
  {
    return max_point;
  }
  
  /**
   * Méthode pour obtenir la liste des huit points représentant la boîte englobante.
   * 
   * @return La liste des points. 
   */
  public List<SVector3d> getListPoint()
  {
    List<SVector3d> list = new ArrayList<SVector3d>();
    
    // Ajouter les points dans le plan min_z
    list.add(min_point);
    list.add(new SVector3d(max_point.getX(), min_point.getY(), min_point.getZ()));
    list.add(new SVector3d(max_point.getX(), max_point.getY(), min_point.getZ()));
    list.add(new SVector3d(min_point.getX(), max_point.getY(), min_point.getZ()));
    
    // Ajouter les points dans le plan max_z
    list.add(new SVector3d(min_point.getX(), min_point.getY(), max_point.getZ()));
    list.add(new SVector3d(max_point.getX(), min_point.getY(), max_point.getZ()));
    list.add(max_point);
    list.add(new SVector3d(min_point.getX(), max_point.getY(), max_point.getZ()));
    
    return list;
  }
  
  /**
   * Méthode pour obtenir le volume de la boîte englobante.
   * Puisque la boîte est alignée sur l'axe x,y et z, l'équation du volume V correspond à
   * <ul> V = dx * dy * dz</ul>
   * <p>
   * Il y a cependant une mise en garde avec cette méthode. Si une géométrie est planaire (comme un triangle) et qu'elle
   * est située dans l'un des 6 plans des axes x,y et z, cette méthode retournera un volume très près de <b>zéro</b>.
   * </p>
   * 
   * @return Le volume de la boîte englobante.
   */
  public double getVolume()
  {
    return (max_point.getX() - min_point.getX()) * (max_point.getY() - min_point.getY()) * (max_point.getZ() - min_point.getZ());
  }
  
  /**
   * Méthode pour obtenir la longueur moyenne de la boîte englobante.
   * Puisque la boîte est alignée sur l'axe x,y et z, l'équation de la longueur moyenne correspond à
   * <ul> L_moy = ( dx + dy + dz ) / 3</ul>
   * 
   * @return La longueur moyenne de la boîte.
   */
  public double getAverageLenght()
  {
    return ( (max_point.getX() - min_point.getX()) + (max_point.getY() - min_point.getY()) + (max_point.getZ() - min_point.getZ()) ) / 3;
  }
  
  /**
   * Méthode pour obtenir la dimension la plus grande de la boîte englobante.
   * Puisuqe la boîte est alignée sur l'axe x, y et z, l'équation de la dimension la plus grande correspond à
   * <ul> L_max = max { dx, dy, dz }</ul>
   * 
   * @return La dimension la plus grande de la boîte.
   */
  public double getMaxLenght()
  {
    double dx = max_point.getX() - min_point.getX();
    double dy = max_point.getY() - min_point.getY();
    double dz = max_point.getZ() - min_point.getZ();
    
    double max = dx;
    
    if(max < dy)
      max = dy;
    
    if(max < dz)
      max = dz;
    
    return max;
  }

  @Override
  public int compareTo(SBoundingBox box)
  {
    return Double.compare(this.getMaxLenght(), box.getMaxLenght());
  }

  @Override
  public double getStatisticalValue()
  {
    return getMaxLenght();
  }
  
}//fin de la classe SBoundingBox
