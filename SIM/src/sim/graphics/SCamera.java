/**
 * 
 */
package sim.graphics;

import java.io.BufferedWriter;
import java.io.IOException;



import sim.exception.SConstructorException;
import sim.math.SImpossibleNormalizationException;
import sim.math.SVector3d;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SCamera</b> repr�sente une cam�ra en 3d.
 * 
 * @author Simon V�zina
 * @since 2014-12-26
 * @version 2016-02-24
 */
public class SCamera extends SAbstractReadableWriteable {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = {
    SKeyWordDecoder.KW_POSITION, SKeyWordDecoder.KW_UP, SKeyWordDecoder.KW_LOOK_AT,
    SKeyWordDecoder.KW_ANGLE, SKeyWordDecoder.KW_NEAR_CLIPPING_PLANE, SKeyWordDecoder.KW_FAR_CLIPPING_PLANE 
  };
  
  /**
   * La constante <b>DEFAULT_POSITION</b> correspond � la position de la cam�ra par d�faut.
   */
	private static final SVector3d DEFAULT_POSITION = new SVector3d(0.0, 0.0, 0.0);
	
	/**
   * La constante <b>DEFAULT_LOOK_AT</b> correspond � la direction vers o� la cam�ra regarde par d�faut.
   */
	private static final SVector3d DEFAULT_LOOK_AT = new SVector3d(0.0, 0.0, 1.0);
	
	/**
   * La constante <b>DEFAULT_UP</b> correspond � la direction verticale (le haut) de la cam�ra par d�faut.
   */
	private static final SVector3d DEFAULT_UP = new SVector3d(0.0, 1.0, 0.0);
	
	/**
   * La constante <b>DEFAULT_VIEW_ANGLE</b> correspond � l'angle d'ouverture vertical de la cam�ra par d�faut �tant �gal � {@value} degr�s.
   */
	private static final double DEFAULT_VIEW_ANGLE = 60.0;
	
	/**
   * La constante <b>DEFAULT_ZNEAR</b> correspond � distance � partir de laquelle la cam�ra <b>peut voir</b> par d�faut �tant �gale � {@value}.
   */
	private static final double DEFAULT_ZNEAR = 1.0;
	
	/**
   * La constante <b>DEFAULT_ZFAR</b> correspond � distance � partir de laquelle la cam�ra <b>ne peut plus voir</b> par d�faut �tant �gale � {@value}.
   */
	private static final double DEFAULT_ZFAR = 100.0;

	/**
	 * La constante <b>MINIMUM_CAMERA_ANGLE</b> correspond � l'angle minimal d'ouverture d'une camera �tant �gal � {@value} degr�s.
	 */
	private static final double MINIMUM_CAMERA_ANGLE = 10;
	
	/**
   * La constante <b>MAXIMUM_CAMERA_ANGLE</b> correspond � l'angle maximal d'ouverture d'une camera �tant �gal � {@value} degr�s.
   */
  private static final double MAXIMUM_CAMERA_ANGLE = 170;
  
	//-------------
	// VARIABLES //
	//-------------
	
	/**
	 * La variable <b>position</b> repr�sente la position de la cam�ra.
	 */
	private SVector3d position;	
	
	/**
	 * La variable <b>look_at</b> repr�sente la position o� la cam�ra regarde.
	 * Cette information permet de d�terminer dans quelle direction la cam�ra pointe.
	 */
	private SVector3d look_at;	
	
	/**
	 * La variable <b>front</b> correspond � la direction vers o� la cam�ra est orient�e.
	 */
	private SVector3d front;	
	
	/**
	 * La variable <b>up</b> correspond � la d�finition du haut pour la cam�ra.
	 */
	private SVector3d up;		

	/**
	 * La variable <b>view_angle</b> correspond � l'angle d'ouverture de la cam�ra dans la direction verticale (up).
	 * Cet �l�ment se retrouve entre autre dans la fonction gluPerspective (param�tre <i>fovy</i>)de la librairie OpenGl pour faire la construction de la pyramide de vue.
	 * Cette valeur doit �tre en <b>degr�</b>.
	 */
	private double view_angle;	
	
	/**
	 * La variable <b>z_near</b> correspond � la distance <b>la plus pr�s</b> pouvant �tre observ�e par la cam�ra.
	 * Cette valeur doit toujours �tre <b>positive</b>. Elle correspond �galement la distance entre la cam�ra et le <i>near clipping plane</i> de la pyramide de vue.
	 */
	private double z_near;			
	
	/**
	 * Specifies the distance from the viewer to the far clipping plane (always positive). 
	 */
	private double z_far;
		
	//----------------
	// CONSTRUCTEUR //
	//----------------
	
	/**
	 * Constructeur vide de la cam�ra. Par d�faut, la camera sera situ�e � l'origine pointant selon l'axe x avec le haut orient� selon l'axe z.
	 */
	public SCamera()
	{
		this(DEFAULT_POSITION, DEFAULT_LOOK_AT, DEFAULT_UP);
	}
	
	/**
	 * Constructeur de la cam�ra avec param�tre de positionnement. 
	 * @param position - La position de la cam�ra.
	 * @param look_at - L'endroit o� regarde la cam�ra. La distance entre position et look_at n'a pas besoin d'�tre unitaire.
	 * @param up - L'orientation du haut de la cam�ra
	 * @throws SConstructorException Si les param�tres de la cam�ra ne permettent une construction compl�te de celle-ci. 
	 */
	public SCamera(SVector3d position, SVector3d look_at, SVector3d up) throws SConstructorException
	{
		if(position.equals(look_at))
		  throw new SConstructorException("Erreur SCamera 001 : Le vecteur position = " + position + " et le vecteur look_at = " + look_at + " sont identique ce qui ne permet pas de d�finir l'orientation de la cam�ra.");
		
	  if(up.equals(SVector3d.ORIGIN))
		  throw new SConstructorException("Erreur SCamera 002 : Le vecteur up = " + up + " ne peut pas �tre �gal � l'origine.");
		
	  this.position = position;
		this.look_at = look_at;
		this.up = up;
		
		view_angle = DEFAULT_VIEW_ANGLE;
		z_near = DEFAULT_ZNEAR;
		z_far = DEFAULT_ZFAR;
		
		try{
		  initialize();
		}catch(SInitializationException e){
		  throw new SConstructorException("Erreur SCamera 003 : La construction de la cam�ra n'est pas possible." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
		}
	}
	
	/**
	 * Constructeur de la cam�ra � partir d'information lue dans un fichier de format .txt.
	 * @param br Le BufferedReader cherchant l'information de le fichier .txt.
	 * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
	 * @throws SConstructorException Si le vecteur position de la cam�ra et position de regard ne pas pas compatible.
	 * @throws SConstructorException Si l'�cran de fond est plus pr�s de la cam�ra que l'�cran de face.
	 * @see SBufferedReader
	 */
	public SCamera(SBufferedReader br)throws IOException, SConstructorException
	{
		this();		//configuration de base s'il y a des param�tres non d�fini.
		
		try{
		  read(br);
		}catch(SInitializationException e){
		  //Si le vecteur position et look_at sont incompatible ensemble
		  throw new SConstructorException("Erreur SCamera 004 : La position de la cam�ra " + position + " et la position de regard " + look_at + " ne permet pas de d�finir un vecteur haut." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
		}
	}
	
	/**
	 * M�thode pour obtenir la position de la camera.
	 * @return La position de la cam�ra.
	 */
	public SVector3d getPosition()
	{
		return position;
	}
	
	/**
	 * M�thode pour obtenir l'orientation du devant de la cam�ra. 
	 * En d'autres mots, la camera point dans la direction de ce vecteur.
	 * @return L'orientation du devant de la cam�ra.
	 */
	public SVector3d getFront()
	{ 
		return front;
	}
	
	/**
	 * M�thode pour obtenir l'orientation du haut de la cam�ra.
	 * @return L'orientation du haut de la cam�ra.
	 */
	public SVector3d getUp()
	{ 
		return up;
	}
	
	/**
	 * M�thode pour obtenir l'angle d'ouverture de vue de la cam�ra dans la direction verticale.
	 * @return L'angle d'ouverture de la cam�ra (direction verticale).
	 */
	public double getViewAngle()
	{
		return view_angle;
	}
	
	/**
	 * M�thode pour obtenir la distance entre la cam�ra et la position du devant de la pyramide de vue (near clipping plane). 
	 * @return La distance entre la cam�ra et le devant de la pyramide de vue.
	 */
	public double getZNear()
	{
		return z_near;
	}
	
	/**
	 * M�thode pour obtenir la distance entre la cam�ra et la position de l'arri�re de la pyramide de vue (far clipping plane). 
	 * @return La distance entre la cam�ra et l'arri�re de la pyramide de vue.
	 */
	public double getZFar()
	{
		return z_far;
	}
	
	/**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
		try{
		  
		  // Construction du vecteur "front = devant" qui doivent �tre normalis�s
		  front = (look_at.substract(position)).normalize();
		  
		  // Construction du vecteur "up = haut" � partir des informations fournies
		  // afin de le rendre purement perpendiculaire au vecteur front (tout en �tant le le plan initial et gardant le m�me sens)
		  up = front.cross(up).cross(front).normalize();
		  
		}catch(SImpossibleNormalizationException e){
		  throw new SInitializationException("Erreur SCamera 005 : Un vecteur n'a pas pu �tre normalis�." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
		}
		
		//V�rifier que l'�cran de fond est plus loin que l'�cran de face
    if(z_far < z_near)
      throw new SInitializationException("Erreur SCamera 006 : La distance � l'�cran de fond '" + z_far + "' ne peut pas �tre plus pr�s que la distance � l'�cran de face '" + z_near + "'.");  
	}
	
	@Override
	public void write(BufferedWriter bw)throws IOException
	{
		bw.write(SKeyWordDecoder.KW_CAMERA);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		//�crire les param�tres de la classe SViewFrustum
		writeSCameraParameter(bw);
				
		bw.write(SKeyWordDecoder.KW_END);
		bw.write(SStringUtil.END_LINE_CARACTER);
		bw.write(SStringUtil.END_LINE_CARACTER);
	}
	
	/**
	 * M�thode pour �crire les param�tres associ�s � la classe SViewFrustum.
	 * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
	 * @throws IOException S'il y a une erreur d'�criture.
	 * @see IOException
	 */
	protected void writeSCameraParameter(BufferedWriter bw)throws IOException
	{
		bw.write(SKeyWordDecoder.KW_POSITION);
		bw.write("\t");
		position.write(bw);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_LOOK_AT);
		bw.write("\t\t");
		look_at.write(bw);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_UP);
		bw.write("\t\t");
		up.write(bw);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_ANGLE);
		bw.write("\t\t\t");
		bw.write(Double.toString(view_angle));
		bw.write(" degrees");
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_NEAR_CLIPPING_PLANE);
		bw.write("\t");
		bw.write(Double.toString(z_near));
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_FAR_CLIPPING_PLANE);
		bw.write("\t");
		bw.write(Double.toString(z_far));
		bw.write(SStringUtil.END_LINE_CARACTER);
	}
	
	@Override
	protected boolean read(SBufferedReader br, int code, String remaining_line)throws SReadingException
	{
		switch(code)
		{
			case SKeyWordDecoder.CODE_POSITION : position = new SVector3d(remaining_line); return true; 
			
			case SKeyWordDecoder.CODE_UP : 			 up = new SVector3d(remaining_line); return true;
			
			case SKeyWordDecoder.CODE_LOOK_AT :	 look_at = new SVector3d(remaining_line); return true;
			
			case SKeyWordDecoder.CODE_ANGLE : view_angle = this.readDoubleEqualOrGreaterThanValueAndEqualOrSmallerThanValue(remaining_line, MINIMUM_CAMERA_ANGLE, MAXIMUM_CAMERA_ANGLE, SKeyWordDecoder.KW_ANGLE); return true;
			
			case SKeyWordDecoder.CODE_NEAR_CLIPPING_PLANE :	z_near = readDoubleGreaterThanZero(remaining_line, SKeyWordDecoder.KW_NEAR_CLIPPING_PLANE); return true;
			
			case SKeyWordDecoder.CODE_FAR_CLIPPING_PLANE : z_far = readDoubleGreaterThanZero(remaining_line, SKeyWordDecoder.KW_FAR_CLIPPING_PLANE); return true;
			
			default : return false;
		}	
	}
			
	@Override
	protected void readingInitialization() throws SInitializationException
	{
	  initialize();
	}
	 
	@Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_CAMERA;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
}//fin classe SCamera
