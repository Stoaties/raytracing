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
 * La classe <b>SGeometricUtil</b> repr�sente une classe utilitaire permettant d'analyser des g�om�tries.
 * </p>
 * 
 * <p>
 * On y retrouve des m�thodes comme :
 * <ul>- �valuation si un vecteur <i>v</i> est � l'int�rieur, sur ou � l'ext�rieur d'un <u>p�rim�tre</u>.</ul>
 * <ul>- �valuation si un vecteur <i>v</i> est � l'int�rieur, sur ou � l'ext�rieur d'une <u>surface</u>.</ul>
 * </p>
 * 
 * @author Simon V�zina
 * @since 2015-11-23
 * @version 2017-08-09
 */
public class SGeometricUtil {

  /**
   * <p>
   * M�thode pour d�terminer si une coordonn�e (x,y) se retrouve sur le p�rim�tre d'un rectangle
   * centr� � la coordonn�e (x_R, y_R) avec des dimensions w par h.
   * </p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si (x,y) est � l'int�rieur du p�rim�tre, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si (x,y) est sur le p�rim�tre, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si (x,y) est � l'ext�rieur du p�rim�tre, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param x_R La position centrale du rectangle selon l'axe x (largeur).
   * @param y_R La position centrale du rectangle selon l'axe y (hauteur).
   * @param w La largeur du rectangle. Cette valeur doit �tre <b>positive</b>.
   * @param h La hauteur du rectangle. Cette valeur doit �tre <b>positive</b>.
   * @param x La coordonn�e x � analyser.
   * @param y La coordonn�e y � analyser.
   * @return <b>-1</b> si (x,y) est � l'int�rieur, <b>0</b> si (x,y) est sur le p�rim�tre et <b>1</b> si (x,y) est � l'ext�rieur du p�rim�tre.
   * @throws SRuntimeException Si la largeur w ou la hauteur h est n�gative.
   */
  public static int isOnRectanglePerimeter(double x_R, double y_R, double w, double h, double x, double y) throws SRuntimeException
  {
    if(w < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 001 : La valeur de w = " + w + " est n�gative.");
    
    if(h < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 002 : La valeur de h = " + h + " est n�gative.");
      
    // �valuer les distances maximales du rectangle � partir de son centre
    double half_w_plus = SMath.ONE_PLUS_EPSILON*w/2;
    double half_w_minus = SMath.ONE_MINUS_EPSILON*w/2;
    
    double half_h_plus = SMath.ONE_PLUS_EPSILON*h/2;
    double half_h_minus = SMath.ONE_MINUS_EPSILON*h/2;
    
    // Distance selon l'axe x entre le point et le centre du rectangle
    double dx = Math.abs(x - x_R);
    
    // V�rifier si la distance est � l'ext�rieur selon l'axe x
    if(dx > half_w_plus)
      return 1;
    
    // Distance selon l'axe y entre le point et le centre du rectangle
    double dy = Math.abs(y - y_R);
    
    // V�rifier si la distance est � l'ext�rieur selon l'axe y
    if(dy > half_h_plus)
      return 1;
    
    // V�rifier si la distance est � l'int�rieur selon x et y
    if(dx < half_w_minus && dy < half_h_minus)
      return -1;
    
    // Le dernier cas reste d'�tre sur le p�rim�tre
    return 0;
  }
  
  /**
   * <p>
   * M�thode pour d�terminer si une coordonn�e (x,y) se retrouve sur le p�rim�tre d'un cercle
   * centr� � la coordonn�e (x_C, y_C) avec un rayon R.
   * </p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si (x,y) est � l'int�rieur du p�rim�tre, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si (x,y) est sur le p�rim�tre, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si (x,y) est � l'ext�rieur du p�rim�tre, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param x_C La position centrale du cercle selon l'axe x (largeur).
   * @param y_C La position centrale du cercle selon l'axe y (hauteur).
   * @param R Le rayon du cercle. Cette valeur doit �tre <b>positive</b>.
   * @param x La coordonn�e x � analyser.
   * @param y La coordonn�e y � analyser.
   * @return <b>-1</b> si (x,y) est � l'int�rieur, <b>0</b> si (x,y) est sur le p�rim�tre et <b>1</b> si (x,y) est � l'ext�rieur du p�rim�tre.
   * @throws SRuntimeException Si le rayon R est n�gatif.
   */
  public static int isOnCerclePerimeter(double x_C, double y_C, double R, double x, double y) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 003 : La valeur de R = " + R + " est n�gative.");
    
    return isOnEllipsePerimeter(x_C, y_C, R, R, x, y);
  }
  
  /**
   * <p>
   * M�thode pour d�terminer si une coordonn�e (x,y) se retrouve � l'int�rieur d'une ellipse
   * centr� � la coordonn�e (x_E, y_E) avec des dimensions w par h.
   * </p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si (x,y) est � l'int�rieur du p�rim�tre, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si (x,y) est sur le p�rim�tre, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si (x,y) est � l'ext�rieur du p�rim�tre, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param x_E La position centrale de l'ellispse selon l'axe x (largeur).
   * @param y_E La position centrale de l'ellispse selon l'axe y (hauteur).
   * @param w Longueur du grand diam�tre (le double du demi-grand axe) selon l'axe x. Cette valeur doit �tre <b>positive</b>.
   * @param h Longueur du petit diam�tre (le double du demi-petit axe) selon l'axe y. Cette valeur doit �tre <b>positive</b>.
   * @param x La coordonn�e x � analyser.
   * @param y La coordonn�e y � analyser.
   * @return <b>-1</b> si (x,y) est � l'int�rieur, <b>0</b> si (x,y) est sur le p�rim�tre et <b>1</b> si (x,y) est � l'ext�rieur du p�rim�tre.
   * @throws SRuntimeException Si la longeur du grand diam�tre w ou la longueur du petit diam�tre h est n�gative.
   */
  public static int isOnEllipsePerimeter(double x_E, double y_E, double w, double h, double x, double y) throws SRuntimeException
  {
    if(w < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 004 : La valeur de w = " + w + " est n�gative.");
    
    if(h < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 005 : La valeur de h = " + h + " est n�gative.");
     
    // �valuer l'ellipse :
    // Si �gale � 1     => sur l'ellipse
    // Si inf�rieur � 1 => � l'int�rieur de l'ellipse
    // Si sup�rieur � 1 => � l'ext�rieur de l'ellipse
    double value = (x - x_E)*(x - x_E) / (w*w/4) + (y - y_E)*(y - y_E) / (h*h/4);
    
    // P.S. Dans la litt�rature, w et h correspond � la moitier des axes (au rayon). 
    //      C'est pourquoi il faut diviser par 4 dans la formule,
    //      car les param�tres de la m�thode correspond au diam�tre et non au rayon (r = D/2).
    return compareDistanceWithReference(value, 1.0);
  }
  
  /**
   * <p>M�thode permettant d'�valuer si un vecteur position v est situ� sur la surface de deux plans parall�les.</p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si v est � l'int�rieur des deux surfaces, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur l'une des deux surfaces, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si v est � l'ext�rieur des deux surfaces, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param r_plane1 La position du plan 1.
   * @param r_plane2 La position du plan 2.
   * @param axis_1to2 Un axe passant du plan 1 au plan 2 donnant ainsi l'alignement des plans (ceux-ci �tant perpendiculaire � cet axe). L'axe doit �tre <b>normalis�</b>.
   * @param v Le vecteur position � analyser.
   * @return <b>-1</b> si le vecteur est � l'int�rieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est � l'ext�rieur de la surface.
   */
  public static int isOnTwoParallelsPlanesSurface(SVector3d r_plane1, SVector3d r_plane2, SVector3d axis_1to2, SVector3d v)
  {
    // S'assurer de la normalisation de l'axe
    axis_1to2 = axis_1to2.normalize();
    
    // V�rifier si le vecteur v est � l'ext�rieur du plan 1
    SVector3d P1_to_v = v.substract(r_plane1);
    double distance1 = P1_to_v.dot(axis_1to2);
    
    //  � l'ext�rieur du plan 1
    if(distance1 < SMath.NEGATIVE_EPSILON)
      return 1;
    else
      // Trop pr�s du plan 1
      if(distance1 < SMath.EPSILON)
        return 0;
    
    //V�rifier si le vecteur v est � l'ext�rieur du plan 2
    SVector3d P2_to_v = v.substract(r_plane2);
    double distance2 = P2_to_v.dot(axis_1to2);
    
    // � l'ext�rieur du plan 2
    if(distance2 > SMath.EPSILON)
      return 1;
    else
      // Trop pr�s du plan 2
      if(distance2 > SMath.NEGATIVE_EPSILON)
        return 0;
    
    // Apr�s le test sur les deux plans, le vecteur v se retrouve entre les deux plans.
    return -1;
  }
  
  /**
   * <p>M�thode permettant d'�valuer si un vecteur position v est situ� sur la surface d'une sph�re.</p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si v est � l'int�rieur de la surface, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si v est � l'ext�rieur de la surface, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param r_s La position de la sph�re.
   * @param R Le rayon de la sph�re. Cette valuer doit �tre <b>positive</b>.
   * @param v Le vecteur position � analyser.
   * @return <b>-1</b> si le vecteur est � l'int�rieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est � l'ext�rieur de la surface.
   * @throws SRuntimeException Si le rayon R est n�gatif.
   */
  public static int isOnSphereSurface(SVector3d r_s, double R, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 006 : La valeur de R = " + R + " est n�gative.");
    
    // �valuer la distance entre le centre de la sph�re et le vecteur v
    double distance = v.substract(r_s).modulus();  
    
    return compareDistanceWithReference(distance, R);
  }
  
  /**
   * M�thode pour �valuer la normale � la <u>surface ext�rieure</u> d'une sph�re � partir d'un point <b>sur la sph�re</b>. 
   * La normale calcul�e sera normalis�e.
   * 
   * @param rS La position de la sph�re.
   * @param R Le rayon de la sph�re. Cette valuer doit �tre <b>positive</b>.
   * @param v Le vecteur position o� l'on veut �valuer la normale � la surface.
   * @return La normale � la surface ext�rieure d'une sph�re.
   * @throws SRuntimeException Si le rayon R est n�gatif.
   * @throws SRuntimeException Si le vecteur v n'est pas sur la surface.
   */
  public static SVector3d outsideSphereNormal(SVector3d rS, double R, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 006 : La valeur de R = " + R + " est n�gative.");
    
    // �valuer le vecteur distance entre le centre de la sph�re et le vecteur v.
    SVector3d rS_to_v = v.substract(rS);
    
    // V�rifier que le vecteur v est sur la surface.
    if(compareDistanceWithReference(rS_to_v.modulus(), R) != 0)
      throw new SRuntimeException("Erreur SGeometricUtil 007 : Le vecteur v = " + v + " n'est pas sur la sph�re (rS = " + rS + ", R = " + R + ").");
    
    return rS_to_v.normalize();
  }
   
  /**
   * <p>M�thode permettant d'�valuer si un vecteur position v est situ� sur la surface d'un tube infini.</p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si v est � l'int�rieur de la surface, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si v est � l'ext�rieur de la surface, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param rT Un point sur l'axe du tube infini.
   * @param axis L'orientation de l'axe du tube infini. L'axe doit �tre <b>normalis�</b>.
   * @param R Le rayon du tube infini. Cette valuer doit �tre <b>positive</b>.
   * @param v Le vecteur position � analyser.
   * @return <b>-1</b> si le vecteur est � l'int�rieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est � l'ext�rieur de la surface.
   * @throws SRuntimeException Si le rayon R est n�gatif.
   */
  public static int isOnInfiniteTubeSurface(SVector3d rT, SVector3d axis, double R, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 007 : La valeur de R = " + R + " est n�gative.");
    
    // S'assurer de la normalisation de l'axe
    axis = axis.normalize();
    
    // �valuons la distance entre le vecteur v et l'axe du tube
    double distance = axis.cross( v.substract(rT) ).cross(axis).modulus();
    
    return compareDistanceWithReference(distance, R, SMath.ONE_PLUS_1000EPSILON, SMath.ONE_MINUS_1000EPSILON);
  }
  
  /**
   * M�thode pour �valuer la normale � la <u>surface ext�rieure</u> d'un tube � partir d'un point <b>sur le tube</b>. 
   * La normale calcul�e sera normalis�e.
   *
   * @param rT Un point sur l'axe du tube infini.
   * @param axis L'orientation de l'axe du tube infini. L'axe doit �tre <b>normalis�</b>.
   * @param R Le rayon du tube infini. Cette valuer doit �tre <b>positive</b>.
   * @param v Le vecteur position � analyser.
   * @return La normale � la surface ext�rieure d'un tube.
   * @throws SRuntimeException Si le rayon R est n�gatif.
   * @throws SRuntimeException Si le vecteur v n'est pas sur la surface.
   */
  public static SVector3d outsideInfiniteTubeNormal(SVector3d rT, SVector3d axis, double R, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 007 : La valeur de R = " + R + " est n�gative.");
    
    // S'assurer de la normalisation de l'axe
    axis = axis.normalize();
    
    // �valuer le vecteur distance la plus courte entre l'axe du tube et le  vecteur v.
    SVector3d axis_to_v = axis.cross( v.substract(rT) ).cross(axis);
    
    // V�rifier que le vecteur v est sur la surface.
    if(compareDistanceWithReference(axis_to_v.modulus(), R, SMath.ONE_PLUS_1000EPSILON, SMath.ONE_MINUS_1000EPSILON) != 0)
      throw new SRuntimeException("Erreur SGeometricUtil 007 : Le vecteur v = " + v + " n'est pas sur le tube (rT = " + rT + ", axis = " + axis + ", R = " + R + ") et la distance v-axis est " + axis_to_v.modulus() + ".");
    
    // Retourner l'orientation ext�rieur de la normale du tube normalis�e.
    return axis_to_v.normalize();
  }
  
  /**
   * <p>M�thode permettant d'�valuer si un vecteur position v est situ� sur la surface de deux c�nes infinis (dont les pointes se touchent comme un sablier).</p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si v est � l'int�rieur de la surface, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si v est � l'ext�rieur de la surface, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param r_c Une position sur l'axe central des deux c�nes o� le rayon <i>R</i> a �t� d�fini.
   * @param axis L'axe des deux c�nes dans la direction localisant la pointe du c�ne � partir de la position <i>r_c</i>. L'axe doit �tre <b>normalis�</b>.
   * @param R Le rayon du c�ne � la position <i>r_c</i>. Cette valuer doit �tre <b>positive</b>.
   * @param H La hauteur du c�ne �tant d�finie comme la distance entre la position <i>r_c</i> et la pointe des c�nes. Cette valuer doit �tre <b>positif</b>.
   * @param v Le vecteur position � analyser.
   * @return <b>-1</b> si le vecteur est � l'int�rieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est � l'ext�rieur de la surface.
   * @throws SRuntimeException Si le rayon R ou la hauteur H du c�ne est n�gatif.
   */
  public static int inOnTwoInfinitesConesSurface(SVector3d r_c, SVector3d axis, double R, double H, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 008 : La valeur de R = " + R + " est n�gative.");
    
    if(H < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 009 : La valeur de H = " + H + " est n�gative.");
    
    // S'assurer de la normalisation de l'axe
    axis = axis.normalize();
    
    // �valuons la distance entre le vecteur v et l'axe du c�ne
    double distance = axis.cross( v.substract(r_c) ).cross(axis).modulus();
    
    // �valuons h �tant la distance entre le point de r�f�rence sur l'axe du c�ne et la projection de v sur l'axe du c�ne
    double h = v.substract(r_c).dot(axis);  // distance entre le point de 
    
    // �valuer le rayon du c�ne � l'endroit de la projection du vecteur v sur l'axe du c�ne
    // Cette valeur sera �gale � (1 - h/H)*R et devra �tre positif (si la projection h est plus grande que H)
    double R_var = Math.abs((1 - h/H)*R);
    
    //return compareDistanceWithReference(distance, R_var); // Ancien seuil pas assez relax�
    return compareDistanceWithReference(distance, R_var, SMath.ONE_PLUS_1000EPSILON, SMath.ONE_MINUS_1000EPSILON);
  }
  
  /**
   * 
   * @param rC Une position sur l'axe central des deux c�nes o� le rayon <i>R</i> a �t� d�fini.
   * @param axis L'axe des deux c�nes dans la direction localisant la pointe du c�ne � partir de la position <i>r_c</i>. L'axe doit �tre <b>normalis�</b>.
   * @param R Le rayon du c�ne � la position <i>rC</i>. Cette valuer doit �tre <b>positive</b>.
   * @param H La hauteur du c�ne �tant d�finie comme la distance entre la position <i>rC</i> et la pointe des c�nes. Cette valuer doit �tre <b>positif</b>.
   * @param v Le vecteur position � analyser.
   * @return
   * @throws SRuntimeException Si le rayon R ou la hauteur H du c�ne est n�gatif.
   * @throws SRuntimeException Si le vecteur v n'est pas sur la surface.
   */
  public static SVector3d outsideTwoInfinitesConesNormal(SVector3d rC, SVector3d axis, double R, double H, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 011 : La valeur de R = " + R + " est n�gative.");
    
    if(H < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 012 : La valeur de H = " + H + " est n�gative.");
   
    // S'assurer que le vecteur v est sur la surface.
    if(inOnTwoInfinitesConesSurface(rC, axis, R, H, v) != 0)
      throw new SRuntimeException("Erreur SGeometricUtil 013 : Le vecteur v = " + v + " n'est pas sur le double c�ne (rC = " + rC + ", axis = " + axis  + ", R = " + R + ", H = " + H + ").");
    
    try{
      
      // S'assurer de la normalisation de l'axe
      axis = axis.normalize();
      
      SVector3d top = rC.add(axis.multiply(H));
      
      SVector3d edge = top.substract(v).normalize();
      
      SVector3d ext_normal = edge.cross(axis).cross(edge);
      
      return ext_normal.normalize();
      
    }catch(SImpossibleNormalizationException e){
      //Si l'intersection est trop pr�s du point P2, alors un trouvera une r�ponse � �a !!!
      SLog.logWriteLine("Message SGeometricUtil : Normale �valu�e � la pointe du c�ne.");
      return axis;
    }
     
  }
  
  /**
   * <p>M�thode permettant d'�valuer si un vecteur position v est situ� sur la surface d'un cylindre.</p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si v est � l'int�rieur de la surface, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si v est � l'ext�rieur de la surface, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param r_1 Le 1ier point d�limitant l'axe du cylindre.
   * @param r_2 Le 2i�me point d�limitant l'axe du cylindre.
   * @param R Le rayon du cylindre. Cette valuer doit �tre <b>positive</b>.
   * @param v Le vecteur position � analyser.
   * @return <b>-1</b> si le vecteur est � l'int�rieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est � l'ext�rieur de la surface.
   * @throws SRuntimeException Si le rayon R est n�gatif.
   */
  public static int isOnCylinderSurface(SVector3d r_1, SVector3d r_2, double R, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 010 : La valeur de R = " + R + " est n�gative.");
    
    // D�terminer l'axe du cylindre
    SVector3d axis = r_2.substract(r_1).normalize();
        
    // D�terminer les codes des extr�mit� et du tube
    int code_extremity = isOnTwoParallelsPlanesSurface(r_1, r_2, axis, v);
    int code_tube = isOnInfiniteTubeSurface(r_1, axis, R, v);
    
    // Cas #1 : � l'ext�rieur des extr�mit�s du cylindre ou du tube
    if(code_extremity > 0 || code_tube > 0)
      return 1;
    
    // Case #2 : � l'int�rieur des extr�mit�s du cylindre et du tube
    if(code_extremity < 0 && code_tube < 0)
      return -1;
     
    // Case #3 : Sur la surface des disques du cylindre ou sur le tube
    return 0;
  }
  
  /**
   * <p>M�thode permettant d'�valuer si un vecteur position v est situ� sur la surface d'un c�ne.</p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si v est � l'int�rieur de la surface, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si v est � l'ext�rieur de la surface, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param r_c Une position sur l'axe central de la base du c�ne o� le rayon <i>R</i> a �t� d�fini.
   * @param axis L'axe du c�ne dans la direction localisant la pointe du c�ne � partir de la position <i>r_c</i>. L'axe doit �tre <b>normalis�</b>.
   * @param R Le rayon du c�ne � la position <i>r_c</i>. Cette valuer doit �tre <b>positive</b>.
   * @param H La hauteur du c�ne �tant d�finie comme la distance entre la position <i>r_c</i> et la pointe des c�nes. Cette valuer doit �tre <b>positif</b>.
   * @param v Le vecteur position � analyser.
   * @return <b>-1</b> si le vecteur est � l'int�rieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est � l'ext�rieur de la surface.
   * @throws SRuntimeException Si le rayon R ou la hauteur H du c�ne est n�gatif.
   */
  public static int isOnConeSurface(SVector3d r_c, SVector3d axis, double R, double H, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 011 : La valeur de R = " + R + " est n�gative.");
    
    if(H < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 012 : La valeur de H = " + H + " est n�gative.");
    
    // S'assurer de la normalisation de l'axe
    axis = axis.normalize();
    
    // D�terminer les codes des extr�mit� et du c�ne
    int code_extremity = isOnTwoParallelsPlanesSurface(r_c, r_c.add(axis.multiply(H)), axis, v);
    int code_cone = inOnTwoInfinitesConesSurface(r_c, axis, R, H, v);
    
    // Cas #1 : � l'ext�rieur des extr�mit�s du cone ou � l'ext�rieur de la forme c�nique
    if(code_extremity > 0 || code_cone > 0)
      return 1;
    
    // Case #2 : � l'int�rieur des extr�mit�s du cone et � l'int�rieur de la forme c�nique
    if(code_extremity < 0 && code_cone < 0)
      return -1;
     
    // Case #3 : Sur la surface de la base du c�ne ou sur la surface c�nique
    return 0;
  }
    
  /**
   * <p>M�thode permettant d'�valuer si un vecteur position v est situ� sur la surface d'un cube.</p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si v est � l'int�rieur de la surface, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si v est � l'ext�rieur de la surface, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param rC La position centrale du cube.
   * @param size La taille du cube.
   * @param v Le vecteur position � analyser.
   * @return <b>-1</b> si le vecteur est � l'int�rieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est � l'ext�rieur de la surface.
   * @throws SRuntimeException Si la taille est n�gatif.
   */
  public static int isOnAlignedCubeSurface(SVector3d rC, double size, SVector3d v)
  {
    if(size < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 013 : La taille du cube �gale � " + size + " est n�gative.");
    
    // �valuer la position du vecteur v par rapport au centre du cube.
    SVector3d p = v.substract(rC);
        
    // Modifier les conditions de validation afin qu'un point sur le cube ne soit pas � l'int�rieur
    double minus_positive_value = SMath.ONE_MINUS_EPSILON * size/2.0;
    double minus_negative_value = SMath.ONE_MINUS_EPSILON * -size/2.0;
     
    // Cas #1 : V�rifier si p est � l'int�rieur de toutes les conditions.
    if(   p.getX() < minus_positive_value && p.getX() > minus_negative_value 
       && p.getY() < minus_positive_value && p.getY() > minus_negative_value
       && p.getZ() < minus_positive_value && p.getZ() > minus_negative_value)
      return -1;
    
    // Modifier les conditions de validation afin qu'un point sur le cube ne soit pas � l'ext�rieur
    double plus_positive_value = SMath.ONE_PLUS_EPSILON * size/2.0;
    double plus_negative_value = SMath.ONE_PLUS_EPSILON * -size/2.0;
    
    // Cas #2 : V�rifier si p est � l'ext�rieur d'au moins une condition.
    if(   p.getX() > plus_positive_value || p.getX() < plus_negative_value 
       || p.getY() > plus_positive_value || p.getY() < plus_negative_value
       || p.getZ() > plus_positive_value || p.getZ() < plus_negative_value)
      return 1;
    
    // Cas #3 : On est absolument sur la surface.
    return 0;  
  }
   
  /**
   * <p>M�thode permettant d'�valuer si un vecteur position v est situ� sur la surface d'un tore.</p>
   * <p>
   * Le r�sultat de cette m�thode sera interpr�table de la fa�on suivante :
   * <ul>- Si v est � l'int�rieur de la surface, la m�thode retournera <b>-1</b>.</ul>
   * <ul>- Si v est sur la surface, la m�thode retournera <b>0</b>.</ul>
   * <ul>- Si v est � l'ext�rieur de la surface, la m�thode retournera <b>1</b>.</ul>
   * </p>
   * 
   * @param rT La position centrale du tore.
   * @param n La normale au plan du tore.
   * @param R Le rayon du p�rim�tre du tore ce qui repr�sente la hauteur du cylindre formant le tore.
   * @param r Le rayon de la partie cylindrique du tore.
   * @param v Le vecteur position � analyser.
   * @return <b>-1</b> si le vecteur est � l'int�rieur, <b>0</b> si le vecteur est sur la surface et <b>1</b> si le vecteur est � l'ext�rieur de la surface.
   * @throws SRuntimeException Si R ou r est n�gatif.
   */
  public static int isOnTorusSurface(SVector3d rT, SVector3d n, double R, double r, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 013 : La valeur de R = " + R + " est n�gative.");
    
    if(r < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 014 : La valeur de r = " + r + " est n�gative.");
    
    // Vecteur d�placement du centre du tore au point v
    SVector3d rT_to_v = v.substract(rT);
    
    // Axe le long du cylindre du tore par rapport au point d'intersection
    SVector3d cylinder_axis = rT_to_v.cross(n);
    
    // Axe radial partant du centre du tore vers le centre du cylindre du tore 
    SVector3d radius_axis = n.cross(cylinder_axis).normalize();
    
    // Position centrale du beigne par rapport au point d'intersection
    SVector3d rC = rT.add(radius_axis.multiply(R));
    
    // �valuer la distance entre v et l'axe du beigne
    double distance = v.substract(rC).modulus();
    
    // Pour des raisons num�riques, on doit utiliser un facteur multiplicatif plus grand.
    return compareDistanceWithReference(distance, r, SMath.ONE_PLUS_1000EPSILON, SMath.ONE_MINUS_1000EPSILON);
  }
  
  
  /**
   * M�thode pour �valuer la normale � la <u>surface ext�rieure</u> d'un tore � partir d'un point <b>sur le tore</b>. 
   * La normale calcul�e sera normalis�e.
   * 
   * @param rT La position centrale du tore.
   * @param n La normale au plan du tore.
   * @param R Le rayon du p�rim�tre du tore ce qui repr�sente la hauteur du cylindre formant le tore.
   * @param r Le rayon de la partie cylindrique du tore.
   * @param v Le vecteur position � analyser.
   * @return La normale � la surface du tore � l'endroit o� est situ� le vecteur v sur la surface du tore.
   * @throws SRuntimeException Si R ou r est n�gatif.
   * @throws SRuntimeException Si le vecteur v n'est pas sur la surface.
   */
  public static SVector3d outsideTorusNormal(SVector3d rT, SVector3d n, double R, double r, SVector3d v) throws SRuntimeException
  {
    if(R < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 013 : La valeur de R = " + R + " est n�gative.");
    
    if(r < 0)
      throw new SRuntimeException("Erreur SGeometricUtil 014 : La valeur de r = " + r + " est n�gative.");
    
    SVector3d rT_to_v = v.substract(rT);
    
    // Axe le long du cylindre du tore par rapport au point d'intersection
    SVector3d cylinder_axis = rT_to_v.cross(n);
    
    // Axe radial partant du centre du tore vers le centre du cylindre du tore 
    SVector3d radius_axis = n.cross(cylinder_axis).normalize();
    
    // Position centrale du beigne par rapport au point d'intersection
    SVector3d rC = rT.add(radius_axis.multiply(R));
    
    // Vecteur distance entre la position centrale du beigne et le vecteur v
    SVector3d rC_to_v = v.substract(rC);
    
    // V�rifier que le vecteur v est sur la surface.
    // Pour des raisons num�riques, on doit utiliser un facteur multiplicatif plus grand.
    if(compareDistanceWithReference(rC_to_v.modulus(), r, SMath.ONE_PLUS_1000EPSILON, SMath.ONE_MINUS_1000EPSILON) != 0)
      throw new SRuntimeException("Erreur SGeometricUtil 007 : Le vecteur v = " + v + " n'est pas sur le tore (rT = " + rT + ", n = " + n  + ", R = " + R + ", r = " + r + "). La distance entre v et l'axe rotatif du tore est d = " + rC_to_v.modulus() + ".");
    
    // Retourner la normale � la surface normalis�e.
    return rC_to_v.normalize();
  }
  
  //----------------------
  // M�THODE UTILITAIRE //
  //----------------------
  
  /**
   * <p>M�thode qui compare une distance avec une valeur de r�f�rence.</p>
   * <p> 
   * Voici les r�sultats admissibles de la comparaison:
   * <ul>- Si la distance est inf�rieure de la r�f�rence, la m�thode retournera -1 (int�rieur � la r�f�rence).</ul>
   * <ul>- Si la distance est �gale � la r�f�rence (� un EPSILON pr�s), la m�thode retournera 0 (sur la r�f�rence).</ul>
   * <ul>- Si la distance est sup�rieure � la r�f�rence, la m�thode retournera 1 (ext�rieur � la r�f�rence).</ul>
   * </p>
   * @param distance La distance � comparer.
   * @param reference La r�f�rence.
   * @return <b>-1</b> si la distance est inf�rieure, <b>0</b> si la distance est �gale et <b>1</b> si la distance est sup�rieure � la r�f�rence.
   */ 
  private static int compareDistanceWithReference(double distance, double reference)
  {
    return compareDistanceWithReference(distance, reference, SMath.ONE_PLUS_EPSILON, SMath.ONE_MINUS_EPSILON);
  }
  
  /**
   * <p>M�thode qui compare une distance avec une valeur de r�f�rence.</p>
   * <p> 
   * Voici les r�sultats admissibles de la comparaison:
   * <ul>- Si la distance est inf�rieure de la r�f�rence, la m�thode retournera -1 (int�rieur � la r�f�rence).</ul>
   * <ul>- Si la distance est �gale � la r�f�rence (� un EPSILON pr�s), la m�thode retournera 0 (sur la r�f�rence).</ul>
   * <ul>- Si la distance est sup�rieure � la r�f�rence, la m�thode retournera 1 (ext�rieur � la r�f�rence).</ul>
   * </p>
   * @param distance La distance � comparer.
   * @param reference La r�f�rence.
   * @param up_multiplicator Le multiplicateur sup�rieur � appliquer � la r�f�rence pour d�finir la borne sup�rieure de l'�galit�. Ce multiplicateur doit �tre pr�s de 1, mais l�g�rement sup�rieur (ex : 1.000001).
   * @param down_multiplicator Le multiplicateur inf�rieur � appliquer � la r�f�rence pour d�finir la borne inf�rieur de l'�galit�. Ce multiplicateur doit �tre pr�s de 1, mais l�g�rement inf�rieur (ex : 0.99999).
   * @return <b>-1</b> si la distance est inf�rieure, <b>0</b> si la distance est �gale et <b>1</b> si la distance est sup�rieure � la r�f�rence.
   */
  private static int compareDistanceWithReference(double distance, double reference, double up_multiplicator, double down_multiplicator)
  {
    // Cas #1 : Si la distance mentionne � l'int�rieure
    if(distance < down_multiplicator * reference)
      return -1;
    
    // Cas #2 : Si la distance mentionne � l'ext�rieur
    if(distance > up_multiplicator * reference)
      return 1;
     
    // Cas #3 : La distance est �gale � la r�f�rence
    return 0;
  }
  
}//fin de la classe SGeometricUtil
