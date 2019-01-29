/**
 * 
 */
package sim.graphics;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.graphics.SCamera;
import sim.graphics.SViewport;
import sim.math.SVector3d;
import sim.math.SVectorPixel;

/**
 * Classe qui repr�sente la pyramide de vue associ� au rendu � g�n�rer. 
 * L'orientation de la pyramide de vue d�pend de la position et l'orientation d'une camera.
 * La g�om�trie de la pyramide vue est exprim� dans l'espace des primitives de la sc�ne.
 * @author Simon V�zina
 * @since 2014-12-28
 * @version 2015-07-16
 */
public class SViewFrustum {

  //Mode de s�lection de la coordonn�e interne d'un pixel
  public static final String[] PIXEL_COORDINATE = {"top_left", "top_right", "bottom_left", "bottom_right", "center", "random" };
  public static final int TOP_LEFT_PIXEL = 0;
  public static final int TOP_RIGHT_PIXEL = 1;
  public static final int BOTTOM_LEFT_PIXEL = 2;
  public static final int BOTTOM_RIGHT_PIXEL = 3;
  public static final int CENTER_PIXEL = 4;
  public static final int RANDOM_PIXEL = 5;
  
  private final SVector3d camera_position;         //la position de la camera
  private final int pixel_internal_coordinate;	   //code de coordonn�e interne du pixel
  
  //Param�tres en pr�calculs
	private final SVector3d centre_clipping_plane;   //Position du centre de l'�cran de face de la pyramide de vue
	private final SVector3d u1;                      //Vecteur pixel unitaire parall�le � la largeur
	private final SVector3d u2;                      //Vecteur pixel unitaire parall�le � la hauteur
	private final SVector3d r_ini;                   //Vecteur d�placement pour passer du centre � la coordonn�e du pixel (0,0)
	
	private final int x_max;                         //Index du pixel maximal en largeur (width)
	private final int y_max;                         //Index du pixel maximal en hauteur (height)
	
	/**
	 * Constructeur d'une pyramide de vue.
	 * @param camera - La camera de la sc�ne.
	 * @param viewport - Le viewport o� sera effectu� le rendu de la sc�ne.
	 * @throws SConstructorException S'il y a eu une erreur lors de la construction de la pyramide de vue.
	 */
	public SViewFrustum(SCamera camera, SViewport viewport)throws SConstructorException
	{
		this(camera, viewport, TOP_LEFT_PIXEL);
	}
	
	/**
	 * Constructeur d'une pyramide de vue.
	 * @param camera - La camera de la sc�ne.
	 * @param viewport - Le viewport o� sera effectu� le rendu de la sc�ne.
	 * @param pixel_coordonate_code - Le code identifiant la coordonn�e s�lectionn�e � l'int�rieur d'un pixel.
	 * @throws SConstructorException S'il y a eu une erreur lors de la construction de la pyramide de vue.
	 */
	public SViewFrustum(SCamera camera, SViewport viewport, int pixel_coordonate_code)throws SConstructorException
	{
		if(isPixelInternalCoordinateCodeValid(pixel_coordonate_code))
			pixel_internal_coordinate = pixel_coordonate_code;
		else
		  throw new SConstructorException("Ereur SViewFrustum 002 : Le code de coordonn�e interne du pixel '" + pixel_coordonate_code +"' n'est pas reconnu.");
	  
	  if(camera == null)
      throw new SConstructorException("Erreur SViewFrustum 003 : La cam�ra n'a pas �t� initialis�e (valeur null).");
    
    if(viewport == null)
      throw new SConstructorException("Erreur SViewFrustum 004 : Le viewport n'a pas �t� initialis� (valeur null).");
    
    //Position de la camera
    camera_position = camera.getPosition();
    
    //Relation math�matique pour �valuer H : H = 2n tan(theta/2)
    double H = 2*camera.getZNear()*Math.tan((camera.getViewAngle()/2)/360*(2*Math.PI));  //Hauteur du front clipping plane
    
    //Relation math�matique pour �valuer L : L = aH
    double L = viewport.normalAspect()*H;             //Largeur du front clipping plane
    
    //Position dans l'espace de la pyramide de vue du centre 
    centre_clipping_plane = camera.getPosition().add(camera.getFront().multiply(camera.getZNear()));
    
    //Vecteur en pixel unitaire parall�le � la largueur du front clipping plane 
    SVector3d u1_tmp = (camera.getFront().cross(camera.getUp())).normalize();
    u1 = u1_tmp.multiply(L/(double)viewport.getWidth());
    x_max = viewport.getWidth();
      
    //Vecteur en pixel unitaire parall�le � la largeur du front clipping plane 
    SVector3d u2_tmp = (camera.getFront().cross(u1)).normalize();
    u2 = u2_tmp.multiply(H/(double)viewport.getHeight());
    y_max = viewport.getHeight();
    
    //Vecteur permettant de localiser � partir du centre du front clipping plane le pixel de coordonn�e (0,0)
    SVector3d r_ini_1 = u1.multiply((double)(-1*viewport.getWidth()/2));
    SVector3d r_ini_2 = u2.multiply((double)(-1*viewport.getHeight()/2));
    r_ini = r_ini_1.add(r_ini_2);    
	}
	
	/**
   * M�thode pour obtenir la position de la camera associ�e � cette pyramide de vue (<i>view frustum</i>).
   * @return la pointe de la pyramide de vue correspondant � la position de la cam�ra.
   */
  public SVector3d getCameraPosition()
  {
    return camera_position;
  }
  
  /**
   * M�thode pour obtenir le code associ� � la coordonn�e interne du pixel qui sera calcul� par la pyramide de vue
   * � partir d'une coordonn�e pixel de l'�cran de vue.
   * @return le code de r�f�rence de la coordon�e interne d'un pixel.
   */
  public int getPixelInternalCoordinate()
  {
    return pixel_internal_coordinate; 
  }
  
	/**
   * M�thode pour retourner la position d'un pixel du Viewport dans le r�f�rentiel du ViewFrustum en coordonn�e xyz. 
   * Ce pixel sera situ� sur le front clipping plane.
   * @param p - La coordonn�e du pixel.
   * @return Le vecteur position du pixel sur le devant de la pyramide de vue.
   * @throws SRuntimeException Si la cam�ra n'a pas �t� initialis�e pour la pyramide de vue.
   * @throws SRuntimeException Si le viewport n'a pas �t� initialis� pour la pyramide de vue. 
   */
  public SVector3d viewportToViewFrustum(SVectorPixel p)throws SRuntimeException
  { 
    return viewportToViewFrustum(p.getX(), p.getY());
  } 
  
  /**
   * M�thode pour retourner la position d'un pixel de coordonn�e xy dans le Viewport dans le r�f�rentiel du ViewFrustum en coordonn�e xyz. 
   * Ce pixel sera situ� sur le front clipping plane � la coordonn�e d�termin� par le code choisi lors de la construction du SViewFrustum.
   * @param x - La coordonn�e x du pixel en largeur (width).
   * @param y - La coordonn�e y du pixel en hauteur (height).
   * @return Le vecteur position du pixel sur le devant de la pyramide de vue.
   */
  public SVector3d viewportToViewFrustum(int x, int y)
  {
    return viewportToViewFrustum(x, y, pixel_internal_coordinate);
  }
  
	/**
   * M�thode pour retourner la position d'un pixel de coordonn�e xy dans le Viewport dans le r�f�rentiel du ViewFrustum en coordonn�e xyz. 
   * Ce pixel sera situ� sur le front clipping plane � la coordonn�e d�termin� par le code choisi.
   * @param x - La coordonn�e x du pixel en largeur (width).
   * @param y - La coordonn�e y du pixel en hauteur (height).
   * @param pixel_code_coordinate - Le code de la coordonn�e interne du pixel qui sera calcul�.
   * @return Le vecteur position du pixel sur le devant de la pyramide de vue.
   * @throws SRunTimeException Si la coordonn�e (x,y) est � l'ext�rieur des limitives du viewport.
   * @throws SRunTimeException Si le code de coordonn�e interne du pixel est invalide.
   */
	public SVector3d viewportToViewFrustum(int x, int y, int pixel_code_coordinate)throws SRuntimeException
  {
	  if(x < 0 || x > x_max)
	    throw new SRuntimeException("Erreur SViewFrustum 005 : La coordonn�e x = " + x + "est � l'ext�rieur de l'interval acceptable [0," + x_max + "].");
  
	  if(y < 0 || y > y_max)
      throw new SRuntimeException("Erreur SViewFrustum 006 : La coordonn�e y = " + y + "est � l'ext�rieur de l'interval acceptable [0," + y_max + "].");
	  
	  //S�lection de la coordonn�e interne du pixel.
	  switch(pixel_code_coordinate)
	  {
	    case TOP_LEFT_PIXEL : return viewportToViewFrustumTopLeft(x,y);
	    case TOP_RIGHT_PIXEL : return viewportToViewFrustumTopRight(x,y);
	    case BOTTOM_LEFT_PIXEL : return viewportToViewFrustumBottomLeft(x,y);
	    case BOTTOM_RIGHT_PIXEL : return viewportToViewFrustumBottomRight(x,y);
	    case CENTER_PIXEL : return viewportToViewFrustumCenter(x,y);
	    case RANDOM_PIXEL : return viewportToViewFrustumRandom(x,y);
	    
	    default : throw new SRuntimeException("Erreur SViewFrustum 007 : Le code de s�lection interne au pixel '" + pixel_internal_coordinate + "' n'est pas bien d�fini."); 
	  }
  }
	
	/**
	 * <p>M�thode pour d�finir le m�canisme de localisation de la coordonn�e interne � un pixel. Les formats
	 * admissibles sont :</p>
	 * <p>TOP_LEFT_PIXEL = 1 : S�lection d'une coordonn�e dans le coin sup�rieur gauche du pixel.</p>
	 * <p> ... </p>
	 * <p>CENTER_PIXEL = 5 : S�lection d'une coordonn�e dans le centre du pixel.</p>
	 * <p>RANDOM_PIXEL = 6: S�lection d'une coordonn�e al�atoire � l'int�rieur du pixel.</p>
	 * @param code - Le code de localisation.
	 */
	private boolean isPixelInternalCoordinateCodeValid(int code)
	{
	  switch(code)
	  {
	    case TOP_LEFT_PIXEL :
	    case TOP_RIGHT_PIXEL :
	    case BOTTOM_LEFT_PIXEL :
	    case BOTTOM_RIGHT_PIXEL :
	    case CENTER_PIXEL : 
	    case RANDOM_PIXEL : return true;
	    
	    default : return false;
	  }
	}
	
  /**
   * M�thode pour retourner la position (x,y,z) d'un pixel de coodonn�e (x,y). La position du pixel correspondra � la <b>position sup�rieure gauche du pixel</b>.
   * @param x - La coordonn�e x du pixel en largeur (width).
   * @param y - La coordonn�e y du pixel en hauteur (height).
   * @return Le vecteur position du pixel sur le devant de la pyramide de vue situ� dans le coin sup�rieur gauche du pixel.
   */
  private SVector3d viewportToViewFrustumTopLeft(int x, int y)
  {
    //Vecteur permettant de localiser � partir du pixel (0,0) le pixel (x,y)
    SVector3d r = (u1.multiply((double)x)).add(u2.multiply((double)y));
    
    return ((centre_clipping_plane.add(r_ini)).add(r));
  }
  
	/**
   * M�thode pour retourner la position (x,y,z) d'un pixel de coodonn�e (x,y). La position du pixel correspondra � la <b>position sup�rieure droit du pixel</b>.
   * @param x - La coordonn�e x du pixel en largeur (width).
   * @param y - La coordonn�e y du pixel en hauteur (height).
   * @return Le vecteur position du pixel sur le devant de la pyramide de vue situ� dans le coin sup�rieur droit du pixel.
   */
  private SVector3d viewportToViewFrustumTopRight(int x, int y)
  {
    //Vecteur permettant de localiser � partir du pixel (0,0) le pixel (x,y)
    SVector3d r = (u1.multiply((double)x+1.0)).add(u2.multiply((double)y));
    
    return ((centre_clipping_plane.add(r_ini)).add(r));
  }
  
  /**
   * M�thode pour retourner la position (x,y,z) d'un pixel de coodonn�e (x,y). La position du pixel correspondra � la <b>position inf�rieur gauche du pixel</b>.
   * @param x - La coordonn�e x du pixel en largeur (width).
   * @param y - La coordonn�e y du pixel en hauteur (height).
   * @return Le vecteur position du pixel sur le devant de la pyramide de vue situ� dans le coin inf�rieur gauche du pixel.
   */
  private SVector3d viewportToViewFrustumBottomLeft(int x, int y)
  {
    //Vecteur permettant de localiser � partir du pixel (0,0) le pixel (x,y)
    SVector3d r = (u1.multiply((double)x)).add(u2.multiply((double)y+1.0));
    
    return ((centre_clipping_plane.add(r_ini)).add(r));
  }
  
  /**
   * M�thode pour retourner la position (x,y,z) d'un pixel de coodonn�e (x,y). La position du pixel correspondra � la <b>position inf�rieur droit du pixel</b>.
   * @param x - La coordonn�e x du pixel en largeur (width).
   * @param y - La coordonn�e y du pixel en hauteur (height).
   * @return Le vecteur position du pixel sur le devant de la pyramide de vue situ� dans le coin inf�rieur droit du pixel.
   */
  private SVector3d viewportToViewFrustumBottomRight(int x, int y)
  {
    //Vecteur permettant de localiser � partir du pixel (0,0) le pixel (x,y)
    SVector3d r = (u1.multiply((double)x+1.0)).add(u2.multiply((double)y+1.0));
    
    return ((centre_clipping_plane.add(r_ini)).add(r));
  }
  
  /**
   * M�thode pour retourner la position (x,y,z) d'un pixel de coodonn�e (x,y). La position du pixel correspondra � la <b>position centrale du pixel</b>.
   * @param x - La coordonn�e x du pixel en largeur (width).
   * @param y - La coordonn�e y du pixel en hauteur (height).
   * @return Le vecteur position du pixel sur le devant de la pyramide de vue situ� dans le coin sup�rieur gauche du pixel.
   */
  private SVector3d viewportToViewFrustumCenter(int x, int y)
  {
    //Vecteur permettant de localiser � partir du pixel (0,0) le pixel (x,y)
    SVector3d r = (u1.multiply((double)x+0.5)).add(u2.multiply((double)y+0.5));
    
    return ((centre_clipping_plane.add(r_ini)).add(r));
  }
			
  /**
   * M�thode pour retourner la position (x,y,z) d'un pixel de coodonn�e (x,y). La position du pixel correspondra � une <b>position al�atoire dans le pixel</b>.
   * @param x - La coordonn�e x du pixel en largeur (width).
   * @param y - La coordonn�e y du pixel en hauteur (height).
   * @return Le vecteur position du pixel sur le devant de la pyramide de vue situ� dans le coin sup�rieur gauche du pixel.
   */
  private SVector3d viewportToViewFrustumRandom(int x, int y)
  {
    //Vecteur permettant de localiser � partir du pixel (0,0) le pixel (x,y)
    SVector3d r = (u1.multiply((double)x+Math.random())).add(u2.multiply((double)y+Math.random()));
    
    return ((centre_clipping_plane.add(r_ini)).add(r));
  }
  
	

	
	
}//fin classe SViewFrustum
