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
 * La classe <b>SBoundingBox</b> repr�sente une bo�te englobant une g�om�trie align� sur les axes x,y et z.
 * Cette bo�te contient une r�f�rence � une g�om�trie � l'int�rieur de celle-ci.
 * </p>
 * 
 * <p>
 * Puisque la classe <b>SBoundingBox</b> impl�mente la classe <b>Comparable</b>, elle peut �tre
 * tri�e en fonction de sa taille maximale d�termin�e par la m�thode getMaxLenght().
 * </p>
 * 
 * <p>
 * Puisque la classe <b>SBoundingBox</b> impl�mente la classe <b>SStatistical</b>, elle peut �tre
 * analys�e statistiquement en fonction de sa taille maximale d�termin�e par la m�thode getMaxLenght().
 * </p>
 * 
 * @author Simon V�zina
 * @since 2015-08-04
 * @version 2016-02-07
 */
public class SBoundingBox implements Comparable<SBoundingBox>, SStatistical {

  /**
   * La variable <b>min_point</b> repr�sente la coordonn�e (x,y,z) <b>minimale</b> de la bo�te.
   * En comparaison avec un carr� � deux dimensions, cela repr�senterait le coin inf�rieur gauche.
   */
  private final SVector3d min_point;    
  
  /**
   * La variable <b>max_point</b> repr�sente la coordonn�e (x,y,z) <b>maximale</b> de la bo�te.
   * En comparaison avec un carr� � deux dimensions, cela repr�senterait le coin sup�rieur droit.
   */
  private final SVector3d max_point;    //la coordonn�e (x,y,z) maximale de la bo�te
  
  /**
   * La variable <b>geometry</b> correspond � la g�om�trie � l'int�rieur de la bo�te englobante.
   */
  private final SGeometry geometry;     
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'une bo�te englobante � partir de deux points extr�mums.
   * 
   * @param geometry - La g�om�trie � l'int�rieur de la bo�te.
   * @param min_point - Le point minimal de la bo�te.
   * @param max_point - Le point maximal de la bo�te.
   * @throws SConstructorException Si min_point n'est pas totalement inf�rieur au max_point.
   */
  public SBoundingBox(SGeometry geometry, SVector3d min_point, SVector3d max_point) throws SConstructorException
  {
    if(min_point.getX() > max_point.getX() || min_point.getY() > max_point.getY() || min_point.getZ() > max_point.getZ())
      throw new SConstructorException("Erreur SBoundingBox 001 : Le point min " + min_point.toString() + " n'est pas inf�rieur au point max " + max_point.toString() + ".");
    
    this.geometry = geometry;
    this.min_point = min_point;
    this.max_point = max_point;
  }

  /**
   * Constructeur d'une bo�te englobant � partir de plusieurs point pouvant d�finir les extr�mit�s de la bo�te englobante.
   * 
   * @param geometry La g�om�trie � l'int�rieur de la bo�te. 
   * @param point_list La liste des points.
   * @throws SConstructorException Si la liste contient moins de deux points.
   */
  public SBoundingBox(SGeometry geometry, List<SVector3d> point_list) throws SConstructorException
  {
    if(point_list.size() < 2)
      throw new SConstructorException("Erreur SBoundingBox 002 : La list contient " + point_list.size() + " points ce qui est inf�rieur � 2.");
    
    this.geometry = geometry;
    
    min_point = SVector3d.findMinValue(point_list);
    max_point = SVector3d.findMaxValue(point_list);
  }
  
  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir la g�om�trie � l'int�rieur de la bo�te englobante.
   * 
   * @return La g�om�trie � l'int�rieur de la bo�te englobante.
   */
  public SGeometry getGeometry()
  {
    return geometry;
  }
  
  /**
   * M�thode pour obtenir la coordonn�e minimale de la bo�te englobante.
   * 
   * @return Le point minimal de la bo�te englobante.
   */
  public SVector3d getMinPoint()
  {
    return min_point;
  }
  
  /**
   * M�thode pour obtenir la coordonn�e maximale de la bo�te englobante.
   * 
   * @return Le point maximal de la bo�te englobante.
   */
  public SVector3d getMaxPoint()
  {
    return max_point;
  }
  
  /**
   * M�thode pour obtenir la liste des huit points repr�sentant la bo�te englobante.
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
   * M�thode pour obtenir le volume de la bo�te englobante.
   * Puisque la bo�te est align�e sur l'axe x,y et z, l'�quation du volume V correspond �
   * <ul> V = dx * dy * dz</ul>
   * <p>
   * Il y a cependant une mise en garde avec cette m�thode. Si une g�om�trie est planaire (comme un triangle) et qu'elle
   * est situ�e dans l'un des 6 plans des axes x,y et z, cette m�thode retournera un volume tr�s pr�s de <b>z�ro</b>.
   * </p>
   * 
   * @return Le volume de la bo�te englobante.
   */
  public double getVolume()
  {
    return (max_point.getX() - min_point.getX()) * (max_point.getY() - min_point.getY()) * (max_point.getZ() - min_point.getZ());
  }
  
  /**
   * M�thode pour obtenir la longueur moyenne de la bo�te englobante.
   * Puisque la bo�te est align�e sur l'axe x,y et z, l'�quation de la longueur moyenne correspond �
   * <ul> L_moy = ( dx + dy + dz ) / 3</ul>
   * 
   * @return La longueur moyenne de la bo�te.
   */
  public double getAverageLenght()
  {
    return ( (max_point.getX() - min_point.getX()) + (max_point.getY() - min_point.getY()) + (max_point.getZ() - min_point.getZ()) ) / 3;
  }
  
  /**
   * M�thode pour obtenir la dimension la plus grande de la bo�te englobante.
   * Puisuqe la bo�te est align�e sur l'axe x, y et z, l'�quation de la dimension la plus grande correspond �
   * <ul> L_max = max { dx, dy, dz }</ul>
   * 
   * @return La dimension la plus grande de la bo�te.
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
