/**
 * 
 */
package sim.geometry;

import sim.exception.SRuntimeException;
import sim.math.SImpossibleNormalizationException;
import sim.math.SMath;
import sim.math.SVector3d;
import sim.util.SLog;

/**
 * <p>
 * La classe <b>SGeometricUtil</b> représente une classe utilitaire permettant d'analyser des géométries.
 * </p>
 * 
 * <p>
 * On y retrouve des méthodes comme :
 * <ul>- Évaluation si un vecteur <i>v</i> est à l'intérieur, sur ou à l'extérieur d'un <u>périmètre</u>.</ul>
 * <ul>- Évaluation si un vecteur <i>v</i> est à l'intérieur, sur ou à l'extérieur d'une <u>surface</u>.</ul>
 * </p>
 * 
 * @author Simon Vézina
 * @since 2015-11-23
 * @version 2017-08-09
 */
public class SGeometricUtil {

  /**
   * <p>
   * Méthode pour déterminer si une coordonnée (x,y) se retrouve sur le périmètre d'un rectangle
   * centré à la coordonnée (x_R, y_R) avec des dimensions w par h.
   * </p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si (x,y) est à l'intérieur du périmètre, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si (x,y) est sur le périmètre, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si (x,y) est à l'extérieur du périmètre, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param x_R La position centrale du rectangle selon l'axe x (largeur).
   * @param y_R La position centrale du rectangle selon l'axe y (hauteur).
   * @param w La largeur du rectangle. Cette valeur doit être <b>positive</b>.
   * @param h La hauteur du rectangle. Cette valeur doit être <b>positive</b>.
   * @param x La coordonnée x à analyser.
   * @param y La coordonnée y à analyser.
   * @return <b>-1</b> si (x,y) est à l'intérieur, <b>0</b> si (x,y) est sur le périmètre et <b>1</b> si (x,y) est à l'extérieur du périmètre.
   * @throws SRuntimeException Si la largeur w ou la hauteur h est négative.
   */
  public static int isOnRectanglePerimeter(double x_R, double y_R, double w, double h, double x, double y) throws SRuntimeException
  {
    if(w < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 001 : La valeur de w = " + w + " est négative.");
    
    if(h < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 002 : La valeur de h = " + h + " est négative.");
      
    // Évaluer les distances maximales du rectangle à partir de son centre
    double half_w_plus = SMath.ONE_PLUS_EPSILON*w/2;
    double half_w_minus = SMath.ONE_MINUS_EPSILON*w/2;
    
    double half_h_plus = SMath.ONE_PLUS_EPSILON*h/2;
    double half_h_minus = SMath.ONE_MINUS_EPSILON*h/2;
    
    // Distance selon l'axe x entre le point et le centre du rectangle
    double dx = Math.abs(x - x_R);
    
    // Vérifier si la distance est à l'extérieur selon l'axe x
    if(dx > half_w_plus)
      return 1;
    
    // Distance selon l'axe y entre le point et le centre du rectangle
    double dy = Math.abs(y - y_R);
    
    // Vérifier si la distance est à l'extérieur selon l'axe y
    if(dy > half_h_plus)
      return 1;
    
    // Vérifier si la distance est à l'intérieur selon x et y
    if(dx < half_w_minus && dy < half_h_minus)
      return -1;
    
    // Le dernier cas reste d'être sur le périmètre
    return 0;
  }
  
  /**
   * <p>
   * Méthode pour déterminer si une coordonnée (x,y) se retrouve sur le périmètre d'un cercle
   * centré à la coordonnée (x_C, y_C) avec un rayon R.
   * </p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si (x,y) est à l'intérieur du périmètre, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si (x,y) est sur le périmètre, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si (x,y) est à l'extérieur du périmètre, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param x_C La position centrale du cercle selon l'axe x (largeur).
   * @param y_C La position centrale du cercle selon l'axe y (hauteur).
   * @param R Le rayon du cercle. Cette valeur doit être <b>positive</b>.
   * @param x La coordonnée x à analyser.
   * @param y La coordonnée y à analyser.
   * @return <b>-1</b> si (x,y) est à l'intérieur, <b>0</b> si (x,y) est sur le périmètre et <b>1</b> si (x,y) est à l'extérieur du périmètre.
   * @throws SRuntimeException Si le rayon R est négatif.
   */
  public static int isOnCerclePerimeter(double x_C, double y_C, double R, double x, double y) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 003 : La valeur de R = " + R + " est négative.");
    
    return isOnEllipsePerimeter(x_C, y_C, R, R, x, y);
  }
  
  /**
   * <p>
   * Méthode pour déterminer si une coordonnée (x,y) se retrouve à l'intérieur d'une ellipse
   * centré à la coordonnée (x_E, y_E) avec des dimensions w par h.
   * </p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si (x,y) est à l'intérieur du périmètre, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si (x,y) est sur le périmètre, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si (x,y) est à l'extérieur du périmètre, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param x_E La position centrale de l'ellispse selon l'axe x (largeur).
   * @param y_E La position centrale de l'ellispse selon l'axe y (hauteur).
   * @param w Longueur du grand diamètre (le double du demi-grand axe) selon l'axe x. Cette valeur doit être <b>positive</b>.
   * @param h Longueur du petit diamètre (le double du demi-petit axe) selon l'axe y. Cette valeur doit être <b>positive</b>.
   * @param x La coordonnée x à analyser.
   * @param y La coordonnée y à analyser.
   * @return <b>-1</b> si (x,y) est à l'intérieur, <b>0</b> si (x,y) est sur le périmètre et <b>1</b> si (x,y) est à l'extérieur du périmètre.
   * @throws SRuntimeException Si la longeur du grand diamètre w ou la longueur du petit diamètre h est négative.
   */
  public static int isOnEllipsePerimeter(double x_E, double y_E, double w, double h, double x, double y) throws SRuntimeException
  {
    if(w < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 004 : La valeur de w = " + w + " est négative.");
    
    if(h < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 005 : La valeur de h = " + h + " est négative.");
     
    // Évaluer l'ellipse :
    // Si égale à 1     => sur l'ellipse
    // Si inférieur à 1 => à l'intérieur de l'ellipse
    // Si supérieur à 1 => à l'extérieur de l'ellipse
    double value = (x - x_E)*(x - x_E) / (w*w/4) + (y - y_E)*(y - y_E) / (h*h/4);
    
    // P.S. Dans la littérature, w et h correspond à la moitier des axes (au rayon). 
    //      C'est pourquoi il faut diviser par 4 dans la formule,
    //      car les paramètres de la méthode correspond au diamètre et non au rayon (r = D/2).
    return compareDistanceWithReference(value, 1.0);
  }
  
  /**
   * <p>Méthode permettant d'évaluer si un vecteur position v est situé sur la surface de deux plans parallèles.</p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si v est à l'intérieur des deux surfaces, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur l'une des deux surfaces, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si v est à l'extérieur des deux surfaces, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param r_plane1 La position du plan 1.
   * @param r_plane2 La position du plan 2.
   * @param axis_1to2 Un axe passant du plan 1 au plan 2 donnant ainsi l'alignement des plans (ceux-ci étant perpendiculaire à cet axe). L'axe doit être <b>normalisé</b>.
   * @param v Le vecteur position à analyser.
   * @return <b>-1</b> si le vecteur est à l'intérieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est à l'extérieur de la surface.
   */
  public static int isOnTwoParallelsPlanesSurface(SVector3d r_plane1, SVector3d r_plane2, SVector3d axis_1to2, SVector3d v)
  {
    // S'assurer de la normalisation de l'axe
    axis_1to2 = axis_1to2.normalize();
    
    // Vérifier si le vecteur v est à l'extérieur du plan 1
    SVector3d P1_to_v = v.substract(r_plane1);
    double distance1 = P1_to_v.dot(axis_1to2);
    
    //  À l'extérieur du plan 1
    if(distance1 < SMath.NEGATIVE_EPSILON)
      return 1;
    else
      // Trop près du plan 1
      if(distance1 < SMath.EPSILON)
        return 0;
    
    //Vérifier si le vecteur v est à l'extérieur du plan 2
    SVector3d P2_to_v = v.substract(r_plane2);
    double distance2 = P2_to_v.dot(axis_1to2);
    
    // À l'extérieur du plan 2
    if(distance2 > SMath.EPSILON)
      return 1;
    else
      // Trop près du plan 2
      if(distance2 > SMath.NEGATIVE_EPSILON)
        return 0;
    
    // Après le test sur les deux plans, le vecteur v se retrouve entre les deux plans.
    return -1;
  }
  
  /**
   * <p>Méthode permettant d'évaluer si un vecteur position v est situé sur la surface d'une sphère.</p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si v est à l'intérieur de la surface, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si v est à l'extérieur de la surface, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param r_s La position de la sphère.
   * @param R Le rayon de la sphère. Cette valuer doit être <b>positive</b>.
   * @param v Le vecteur position à analyser.
   * @return <b>-1</b> si le vecteur est à l'intérieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est à l'extérieur de la surface.
   * @throws SRuntimeException Si le rayon R est négatif.
   */
  public static int isOnSphereSurface(SVector3d r_s, double R, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 006 : La valeur de R = " + R + " est négative.");
    
    // Évaluer la distance entre le centre de la sphère et le vecteur v
    double distance = v.substract(r_s).modulus();  
    
    return compareDistanceWithReference(distance, R);
  }
  
  /**
   * Méthode pour évaluer la normale à la <u>surface extérieure</u> d'une sphère à partir d'un point <b>sur la sphère</b>. 
   * La normale calculée sera normalisée.
   * 
   * @param rS La position de la sphère.
   * @param R Le rayon de la sphère. Cette valuer doit être <b>positive</b>.
   * @param v Le vecteur position où l'on veut évaluer la normale à la surface.
   * @return La normale à la surface extérieure d'une sphère.
   * @throws SRuntimeException Si le rayon R est négatif.
   * @throws SRuntimeException Si le vecteur v n'est pas sur la surface.
   */
  public static SVector3d outsideSphereNormal(SVector3d rS, double R, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 006 : La valeur de R = " + R + " est négative.");
    
    // Évaluer le vecteur distance entre le centre de la sphère et le vecteur v.
    SVector3d rS_to_v = v.substract(rS);
    
    // Vérifier que le vecteur v est sur la surface.
    if(compareDistanceWithReference(rS_to_v.modulus(), R) != 0)
      throw new SRuntimeException("Erreur SGeometricUtil 007 : Le vecteur v = " + v + " n'est pas sur la sphère (rS = " + rS + ", R = " + R + ").");
    
    return rS_to_v.normalize();
  }
   
  /**
   * <p>Méthode permettant d'évaluer si un vecteur position v est situé sur la surface d'un tube infini.</p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si v est à l'intérieur de la surface, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si v est à l'extérieur de la surface, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param rT Un point sur l'axe du tube infini.
   * @param axis L'orientation de l'axe du tube infini. L'axe doit être <b>normalisé</b>.
   * @param R Le rayon du tube infini. Cette valuer doit être <b>positive</b>.
   * @param v Le vecteur position à analyser.
   * @return <b>-1</b> si le vecteur est à l'intérieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est à l'extérieur de la surface.
   * @throws SRuntimeException Si le rayon R est négatif.
   */
  public static int isOnInfiniteTubeSurface(SVector3d rT, SVector3d axis, double R, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 007 : La valeur de R = " + R + " est négative.");
    
    // S'assurer de la normalisation de l'axe
    axis = axis.normalize();
    
    // Évaluons la distance entre le vecteur v et l'axe du tube
    double distance = axis.cross( v.substract(rT) ).cross(axis).modulus();
    
    return compareDistanceWithReference(distance, R, SMath.ONE_PLUS_1000EPSILON, SMath.ONE_MINUS_1000EPSILON);
  }
  
  /**
   * Méthode pour évaluer la normale à la <u>surface extérieure</u> d'un tube à partir d'un point <b>sur le tube</b>. 
   * La normale calculée sera normalisée.
   *
   * @param rT Un point sur l'axe du tube infini.
   * @param axis L'orientation de l'axe du tube infini. L'axe doit être <b>normalisé</b>.
   * @param R Le rayon du tube infini. Cette valuer doit être <b>positive</b>.
   * @param v Le vecteur position à analyser.
   * @return La normale à la surface extérieure d'un tube.
   * @throws SRuntimeException Si le rayon R est négatif.
   * @throws SRuntimeException Si le vecteur v n'est pas sur la surface.
   */
  public static SVector3d outsideInfiniteTubeNormal(SVector3d rT, SVector3d axis, double R, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 007 : La valeur de R = " + R + " est négative.");
    
    // S'assurer de la normalisation de l'axe
    axis = axis.normalize();
    
    // Évaluer le vecteur distance la plus courte entre l'axe du tube et le  vecteur v.
    SVector3d axis_to_v = axis.cross( v.substract(rT) ).cross(axis);
    
    // Vérifier que le vecteur v est sur la surface.
    if(compareDistanceWithReference(axis_to_v.modulus(), R, SMath.ONE_PLUS_1000EPSILON, SMath.ONE_MINUS_1000EPSILON) != 0)
      throw new SRuntimeException("Erreur SGeometricUtil 007 : Le vecteur v = " + v + " n'est pas sur le tube (rT = " + rT + ", axis = " + axis + ", R = " + R + ") et la distance v-axis est " + axis_to_v.modulus() + ".");
    
    // Retourner l'orientation extérieur de la normale du tube normalisée.
    return axis_to_v.normalize();
  }
  
  /**
   * <p>Méthode permettant d'évaluer si un vecteur position v est situé sur la surface de deux cônes infinis (dont les pointes se touchent comme un sablier).</p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si v est à l'intérieur de la surface, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si v est à l'extérieur de la surface, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param r_c Une position sur l'axe central des deux cônes où le rayon <i>R</i> a été défini.
   * @param axis L'axe des deux cônes dans la direction localisant la pointe du cône à partir de la position <i>r_c</i>. L'axe doit être <b>normalisé</b>.
   * @param R Le rayon du cône à la position <i>r_c</i>. Cette valuer doit être <b>positive</b>.
   * @param H La hauteur du cône étant définie comme la distance entre la position <i>r_c</i> et la pointe des cônes. Cette valuer doit être <b>positif</b>.
   * @param v Le vecteur position à analyser.
   * @return <b>-1</b> si le vecteur est à l'intérieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est à l'extérieur de la surface.
   * @throws SRuntimeException Si le rayon R ou la hauteur H du cône est négatif.
   */
  public static int inOnTwoInfinitesConesSurface(SVector3d r_c, SVector3d axis, double R, double H, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 008 : La valeur de R = " + R + " est négative.");
    
    if(H < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 009 : La valeur de H = " + H + " est négative.");
    
    // S'assurer de la normalisation de l'axe
    axis = axis.normalize();
    
    // Évaluons la distance entre le vecteur v et l'axe du cône
    double distance = axis.cross( v.substract(r_c) ).cross(axis).modulus();
    
    // Évaluons h étant la distance entre le point de référence sur l'axe du cône et la projection de v sur l'axe du cône
    double h = v.substract(r_c).dot(axis);  // distance entre le point de 
    
    // Évaluer le rayon du cône à l'endroit de la projection du vecteur v sur l'axe du cône
    // Cette valeur sera égale à (1 - h/H)*R et devra être positif (si la projection h est plus grande que H)
    double R_var = Math.abs((1 - h/H)*R);
    
    //return compareDistanceWithReference(distance, R_var); // Ancien seuil pas assez relaxé
    return compareDistanceWithReference(distance, R_var, SMath.ONE_PLUS_1000EPSILON, SMath.ONE_MINUS_1000EPSILON);
  }
  
  /**
   * 
   * @param rC Une position sur l'axe central des deux cônes où le rayon <i>R</i> a été défini.
   * @param axis L'axe des deux cônes dans la direction localisant la pointe du cône à partir de la position <i>r_c</i>. L'axe doit être <b>normalisé</b>.
   * @param R Le rayon du cône à la position <i>rC</i>. Cette valuer doit être <b>positive</b>.
   * @param H La hauteur du cône étant définie comme la distance entre la position <i>rC</i> et la pointe des cônes. Cette valuer doit être <b>positif</b>.
   * @param v Le vecteur position à analyser.
   * @return
   * @throws SRuntimeException Si le rayon R ou la hauteur H du cône est négatif.
   * @throws SRuntimeException Si le vecteur v n'est pas sur la surface.
   */
  public static SVector3d outsideTwoInfinitesConesNormal(SVector3d rC, SVector3d axis, double R, double H, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 011 : La valeur de R = " + R + " est négative.");
    
    if(H < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 012 : La valeur de H = " + H + " est négative.");
   
    // S'assurer que le vecteur v est sur la surface.
    if(inOnTwoInfinitesConesSurface(rC, axis, R, H, v) != 0)
      throw new SRuntimeException("Erreur SGeometricUtil 013 : Le vecteur v = " + v + " n'est pas sur le double cône (rC = " + rC + ", axis = " + axis  + ", R = " + R + ", H = " + H + ").");
    
    try{
      
      // S'assurer de la normalisation de l'axe
      axis = axis.normalize();
      
      SVector3d top = rC.add(axis.multiply(H));
      
      SVector3d edge = top.substract(v).normalize();
      
      SVector3d ext_normal = edge.cross(axis).cross(edge);
      
      return ext_normal.normalize();
      
    }catch(SImpossibleNormalizationException e){
      //Si l'intersection est trop près du point P2, alors un trouvera une réponse à ça !!!
      SLog.logWriteLine("Message SGeometricUtil : Normale évaluée à la pointe du cône.");
      return axis;
    }
     
  }
  
  /**
   * <p>Méthode permettant d'évaluer si un vecteur position v est situé sur la surface d'un cylindre.</p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si v est à l'intérieur de la surface, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si v est à l'extérieur de la surface, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param r_1 Le 1ier point délimitant l'axe du cylindre.
   * @param r_2 Le 2ième point délimitant l'axe du cylindre.
   * @param R Le rayon du cylindre. Cette valuer doit être <b>positive</b>.
   * @param v Le vecteur position à analyser.
   * @return <b>-1</b> si le vecteur est à l'intérieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est à l'extérieur de la surface.
   * @throws SRuntimeException Si le rayon R est négatif.
   */
  public static int isOnCylinderSurface(SVector3d r_1, SVector3d r_2, double R, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 010 : La valeur de R = " + R + " est négative.");
    
    // Déterminer l'axe du cylindre
    SVector3d axis = r_2.substract(r_1).normalize();
        
    // Déterminer les codes des extrémité et du tube
    int code_extremity = isOnTwoParallelsPlanesSurface(r_1, r_2, axis, v);
    int code_tube = isOnInfiniteTubeSurface(r_1, axis, R, v);
    
    // Cas #1 : À l'extérieur des extrémités du cylindre ou du tube
    if(code_extremity > 0 || code_tube > 0)
      return 1;
    
    // Case #2 : À l'intérieur des extrémités du cylindre et du tube
    if(code_extremity < 0 && code_tube < 0)
      return -1;
     
    // Case #3 : Sur la surface des disques du cylindre ou sur le tube
    return 0;
  }
  
  /**
   * <p>Méthode permettant d'évaluer si un vecteur position v est situé sur la surface d'un cône.</p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si v est à l'intérieur de la surface, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si v est à l'extérieur de la surface, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param r_c Une position sur l'axe central de la base du cône où le rayon <i>R</i> a été défini.
   * @param axis L'axe du cône dans la direction localisant la pointe du cône à partir de la position <i>r_c</i>. L'axe doit être <b>normalisé</b>.
   * @param R Le rayon du cône à la position <i>r_c</i>. Cette valuer doit être <b>positive</b>.
   * @param H La hauteur du cône étant définie comme la distance entre la position <i>r_c</i> et la pointe des cônes. Cette valuer doit être <b>positif</b>.
   * @param v Le vecteur position à analyser.
   * @return <b>-1</b> si le vecteur est à l'intérieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est à l'extérieur de la surface.
   * @throws SRuntimeException Si le rayon R ou la hauteur H du cône est négatif.
   */
  public static int isOnConeSurface(SVector3d r_c, SVector3d axis, double R, double H, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 011 : La valeur de R = " + R + " est négative.");
    
    if(H < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 012 : La valeur de H = " + H + " est négative.");
    
    // S'assurer de la normalisation de l'axe
    axis = axis.normalize();
    
    // Déterminer les codes des extrémité et du cône
    int code_extremity = isOnTwoParallelsPlanesSurface(r_c, r_c.add(axis.multiply(H)), axis, v);
    int code_cone = inOnTwoInfinitesConesSurface(r_c, axis, R, H, v);
    
    // Cas #1 : À l'extérieur des extrémités du cone ou à l'extérieur de la forme cônique
    if(code_extremity > 0 || code_cone > 0)
      return 1;
    
    // Case #2 : À l'intérieur des extrémités du cone et à l'intérieur de la forme cônique
    if(code_extremity < 0 && code_cone < 0)
      return -1;
     
    // Case #3 : Sur la surface de la base du cône ou sur la surface cônique
    return 0;
  }
    
  /**
   * <p>Méthode permettant d'évaluer si un vecteur position v est situé sur la surface d'un cube.</p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si v est à l'intérieur de la surface, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si v est à l'extérieur de la surface, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param rC La position centrale du cube.
   * @param size La taille du cube.
   * @param v Le vecteur position à analyser.
   * @return <b>-1</b> si le vecteur est à l'intérieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est à l'extérieur de la surface.
   * @throws SRuntimeException Si la taille est négatif.
   */
  public static int isOnAlignedCubeSurface(SVector3d rC, double size, SVector3d v)
  {
    if(size < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 013 : La taille du cube égale à " + size + " est négative.");
    
    // Évaluer la position du vecteur v par rapport au centre du cube.
    SVector3d p = v.substract(rC);
        
    // Modifier les conditions de validation afin qu'un point sur le cube ne soit pas à l'intérieur
    double minus_positive_value = SMath.ONE_MINUS_EPSILON * size/2.0;
    double minus_negative_value = SMath.ONE_MINUS_EPSILON * -size/2.0;
     
    // Cas #1 : Vérifier si p est à l'intérieur de toutes les conditions.
    if(   p.getX() < minus_positive_value && p.getX() > minus_negative_value 
       && p.getY() < minus_positive_value && p.getY() > minus_negative_value
       && p.getZ() < minus_positive_value && p.getZ() > minus_negative_value)
      return -1;
    
    // Modifier les conditions de validation afin qu'un point sur le cube ne soit pas à l'extérieur
    double plus_positive_value = SMath.ONE_PLUS_EPSILON * size/2.0;
    double plus_negative_value = SMath.ONE_PLUS_EPSILON * -size/2.0;
    
    // Cas #2 : Vérifier si p est à l'extérieur d'au moins une condition.
    if(   p.getX() > plus_positive_value || p.getX() < plus_negative_value 
       || p.getY() > plus_positive_value || p.getY() < plus_negative_value
       || p.getZ() > plus_positive_value || p.getZ() < plus_negative_value)
      return 1;
    
    // Cas #3 : On est absolument sur la surface.
    return 0;  
  }
   
  /**
   * <p>Méthode permettant d'évaluer si un vecteur position v est situé sur la surface d'un tore.</p>
   * <p>
   * Le résultat de cette méthode sera interprétable de la façon suivante :
   * <ul>- Si v est à l'intérieur de la surface, la méthode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la méthode retournera <b>0</b>.</ul>
   * <ul>- Si v est à l'extérieur de la surface, la méthode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param rT La position centrale du tore.
   * @param n La normale au plan du tore.
   * @param R Le rayon du périmètre du tore ce qui représente la hauteur du cylindre formant le tore.
   * @param r Le rayon de la partie cylindrique du tore.
   * @param v Le vecteur position à analyser.
   * @return <b>-1</b> si le vecteur est à l'intérieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est à l'extérieur de la surface.
   * @throws SRuntimeException Si R ou r est négatif.
   */
  public static int isOnTorusSurface(SVector3d rT, SVector3d n, double R, double r, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 013 : La valeur de R = " + R + " est négative.");
    
    if(r < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 014 : La valeur de r = " + r + " est négative.");
    
    // Vecteur déplacement du centre du tore au point v
    SVector3d rT_to_v = v.substract(rT);
    
    // Axe le long du cylindre du tore par rapport au point d'intersection
    SVector3d cylinder_axis = rT_to_v.cross(n);
    
    // Axe radial partant du centre du tore vers le centre du cylindre du tore 
    SVector3d radius_axis = n.cross(cylinder_axis).normalize();
    
    // Position centrale du beigne par rapport au point d'intersection
    SVector3d rC = rT.add(radius_axis.multiply(R));
    
    // Évaluer la distance entre v et l'axe du beigne
    double distance = v.substract(rC).modulus();
    
    // Pour des raisons numériques, on doit utiliser un facteur multiplicatif plus grand.
    return compareDistanceWithReference(distance, r, SMath.ONE_PLUS_1000EPSILON, SMath.ONE_MINUS_1000EPSILON);
  }
  
  
  /**
   * Méthode pour évaluer la normale à la <u>surface extérieure</u> d'un tore à partir d'un point <b>sur le tore</b>. 
   * La normale calculée sera normalisée.
   * 
   * @param rT La position centrale du tore.
   * @param n La normale au plan du tore.
   * @param R Le rayon du périmètre du tore ce qui représente la hauteur du cylindre formant le tore.
   * @param r Le rayon de la partie cylindrique du tore.
   * @param v Le vecteur position à analyser.
   * @return La normale à la surface du tore à l'endroit où est situé le vecteur v sur la surface du tore.
   * @throws SRuntimeException Si R ou r est négatif.
   * @throws SRuntimeException Si le vecteur v n'est pas sur la surface.
   */
  public static SVector3d outsideTorusNormal(SVector3d rT, SVector3d n, double R, double r, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 013 : La valeur de R = " + R + " est négative.");
    
    if(r < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 014 : La valeur de r = " + r + " est négative.");
    
    SVector3d rT_to_v = v.substract(rT);
    
    // Axe le long du cylindre du tore par rapport au point d'intersection
    SVector3d cylinder_axis = rT_to_v.cross(n);
    
    // Axe radial partant du centre du tore vers le centre du cylindre du tore 
    SVector3d radius_axis = n.cross(cylinder_axis).normalize();
    
    // Position centrale du beigne par rapport au point d'intersection
    SVector3d rC = rT.add(radius_axis.multiply(R));
    
    // Vecteur distance entre la position centrale du beigne et le vecteur v
    SVector3d rC_to_v = v.substract(rC);
    
    // Vérifier que le vecteur v est sur la surface.
    // Pour des raisons numériques, on doit utiliser un facteur multiplicatif plus grand.
    if(compareDistanceWithReference(rC_to_v.modulus(), r, SMath.ONE_PLUS_1000EPSILON, SMath.ONE_MINUS_1000EPSILON) != 0)
      throw new SRuntimeException("Erreur SGeometricUtil 007 : Le vecteur v = " + v + " n'est pas sur le tore (rT = " + rT + ", n = " + n  + ", R = " + R + ", r = " + r + "). La distance entre v et l'axe rotatif du tore est d = " + rC_to_v.modulus() + ".");
    
    // Retourner la normale à la surface normalisée.
    return rC_to_v.normalize();
  }
  
  //----------------------
  // MÉTHODE UTILITAIRE //
  //----------------------
  
  /**
   * <p>Méthode qui compare une distance avec une valeur de référence.</p>
   * <p> 
   * Voici les résultats admissibles de la comparaison:
   * <ul>- Si la distance est inférieure de la référence, la méthode retournera -1 (intérieur à la référence).</ul>
   * <ul>- Si la distance est égale à la référence (à un EPSILON près), la méthode retournera 0 (sur la référence).</ul>
   * <ul>- Si la distance est supérieure à la référence, la méthode retournera 1 (extérieur à la référence).</ul>
   * </p>
   * @param distance La distance à comparer.
   * @param reference La référence.
   * @return <b>-1</b> si la distance est inférieure, <b>0</b> si la distance est égale et <b>1</b> si la distance est supérieure à la référence.
   */ 
  private static int compareDistanceWithReference(double distance, double reference)
  {
    return compareDistanceWithReference(distance, reference, SMath.ONE_PLUS_EPSILON, SMath.ONE_MINUS_EPSILON);
  }
  
  /**
   * <p>Méthode qui compare une distance avec une valeur de référence.</p>
   * <p> 
   * Voici les résultats admissibles de la comparaison:
   * <ul>- Si la distance est inférieure de la référence, la méthode retournera -1 (intérieur à la référence).</ul>
   * <ul>- Si la distance est égale à la référence (à un EPSILON près), la méthode retournera 0 (sur la référence).</ul>
   * <ul>- Si la distance est supérieure à la référence, la méthode retournera 1 (extérieur à la référence).</ul>
   * </p>
   * @param distance La distance à comparer.
   * @param reference La référence.
   * @param up_multiplicator Le multiplicateur supérieur à appliquer à la référence pour définir la borne supérieure de l'égalité. Ce multiplicateur doit être près de 1, mais légèrement supérieur (ex : 1.000001).
   * @param down_multiplicator Le multiplicateur inférieur à appliquer à la référence pour définir la borne inférieur de l'égalité. Ce multiplicateur doit être près de 1, mais légèrement inférieur (ex : 0.99999).
   * @return <b>-1</b> si la distance est inférieure, <b>0</b> si la distance est égale et <b>1</b> si la distance est supérieure à la référence.
   */
  private static int compareDistanceWithReference(double distance, double reference, double up_multiplicator, double down_multiplicator)
  {
    // Cas #1 : Si la distance mentionne à l'intérieure
    if(distance < down_multiplicator * reference)
      return -1;
    
    // Cas #2 : Si la distance mentionne à l'extérieur
    if(distance > up_multiplicator * reference)
      return 1;
     
    // Cas #3 : La distance est égale à la référence
    return 0;
  }
  
}//fin de la classe SGeometricUtil
