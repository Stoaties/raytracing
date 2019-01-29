/**
 * 
 */
package sim.graphics;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import sim.exception.SConstructorException;
import sim.exception.SCoordinateOutOfBoundException;
import sim.exception.SRuntimeException;
import sim.math.SVectorPixel;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SViewport</b> repr�sente une grille de couleur en deux dimension en espace pixels o� sera dessin� la sc�ne du point de vue de le pyramide de vue (ViewFrustum).
 * Les coordonn�es sont index�s avec [x][y] o� x correspond au num�ro de ligne et y correspond au num�ro de la colonne du tableau.
 * <ul>- La coordonn�e [0][0] correspond au coin sup�rieur gauche de l'image.</ul>
 * <ul>- La coordonn�e [width-1][0] correspond au coin sup�rieur droit de l'image.</ul>
 * <ul>- La coordonn�e [0][height-1] correspond au coin inf�rieur gauche de l'image.</ul>
 * <ul>- La coordonn�e [width-1][height-1] correspond au coint inf�rieur droit de l'image.</ul>
 *  
 * @author Simon V�zina
 * @since 2014-12-27
 * @version 2017-12-08
 */
public class SViewport extends SAbstractReadableWriteable {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = {
    SKeyWordDecoder.KW_WIDTH, SKeyWordDecoder.KW_HEIGHT, SKeyWordDecoder.KW_IMAGE_FILE_NAME
  };
  
  /**
   * La constante <b>ORIGIN_PIXEL</b> correspond � l'origine en coordonn�e pixel du viewport.
   */
  private static final SVectorPixel ORIGIN_PIXEL = new SVectorPixel(0,0);
  
	private final static int DEFAULT_IMAGE_COUNTER = 0;						        //num�ro de l'image initiale par d�faut
	private final static int MAX_IMAGE_COUNTER = 999;						          //num�ro maximal de l'image
		
	public final SColor BACKGROUND_SCOLOR = new SColor(0.0, 0.0, 0.0);		//Couleur du background (en SColor)
	
	private static final int DEFAULT_WIDTH = 500;							            //largeur du viewport par d�faut
	private static final int DEFAULT_HEIGHT = 500;						            //hauteur du viewport par d�faut
	
	private static final String DEFAULT_IMAGE_FILE_NAME = "image";			  //nom du fichier de l'�criture du viewport par d�faut
	private static final String DEFAULT_IMAGE_EXTENSION = "png";			    //extension du fichier d'�criture du viewport par d�faut
	
	private static int image_counter = DEFAULT_IMAGE_COUNTER;				      //num�ro de l'image qui sera g�n�r� lors de l'�criture du viewport dans un fichier image
	
	/**
	 * La variable <b>image_file_name</b> repr�sente le nom du fichier lors de l'�criture du viewport en fichier image.
	 */
	private String image_file_name;				
		
	/**
	 * La variable <b>width</b> repr�sente le nombre de pixels en largeur (selon l'axe x, de 0 � width-1).
	 */
	private int width;	
	
	/**
	 * La variable <b>height</b> repr�sente le nombre de pixels en hauteur (selon l'axe y, de 0 � height-1).
	 */ 
	private int height;							      
	
	/**
	 * La variable <b>image</b> correspond au tableau � deux dimensions contenant les couleurs.
	 */
	private SColor[][] image;             
	
	/**
	 * La variable <b>image_buffer</b> correspond � l'image du viewport.
	 */
	private BufferedImage image_buffer;
	
	/**
	 * La variable <b>current_pixel</b> repr�sente le pixel qui est pr�sentement en analyse dans le <i>viewport</i>.
	 */
	private SVectorPixel current_pixel;		
		
	//----------------
	// CONSTRUCTEUR //
	//----------------
	
	/**
	 * Constructeur d'un viewport par d�faut.
	 */
	public SViewport()
	{
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	/**
	 * Constructeur avec d�finition des param�tres de largeur (width) et de hauteur (height).
	 * 
	 * @param width - La largeur du viewport.
	 * @param height - La hauteur du viewport.
	 */
	public SViewport(int width, int height)
	{
		this(width, height, DEFAULT_IMAGE_FILE_NAME);
	}
	
	/**
	 * Constructeur d'un viewport.
	 * 
	 * @param width - La largeur du viewport.
	 * @param height - La hauteur du viewport.
	 * @param file_name - Le nom du fichier image qui sera g�n�r�.
	 * @throws SConstructorException Si une erreur est survenue � la construction.
	 */
	public SViewport(int width, int height, String file_name) throws SConstructorException
	{
		if(width < 1)
		  throw new SConstructorException("Erreur SViewport 001 : La largeur du viewport (width) �tant �gal � " + width + "est inf�rieur � 1.");
		
		if(height < 1)
      throw new SConstructorException("Erreur SViewport 002 : La hauteur du viewport (height) �tant �gal � " + height + "est inf�rieur � 1.");
    
	  this.width = width;
		this.height = height;
		image_file_name = file_name;
				
		try{
		  initialize();
		}catch(SInitializationException e){
      throw new SConstructorException("Erreur SViewport 003 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
	}
	
	/**
	 * Constructeur du viewport � partir d'information lue dans un fichier de format txt.
	 * 
	 * @param br Le BufferedReader cherchant l'information de le fichier .txt.
	 * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
	 * @throws SConstructorException Si une erreur est survenue � la construction.
	 * @see SBufferedReader
	 */
	public SViewport(SBufferedReader br) throws IOException, SConstructorException
	{
		this();		//configuration de base s'il y a des param�tres non d�fini.
		
		try{
		  read(br);
		}catch(SInitializationException e){
      throw new SConstructorException("Erreur SViewport 004 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
	}
	
	/**
	 * M�thode pour obtenir le nombre de pixels en largeur (width) du Viewport.
	 * 
	 * @return Le nombre de pixels en largeur.
	 */
	public int getWidth()
	{ 
	  return width;
	}
	
	/**
	 * M�thode pour obtenir le nombre de pixels en hauteur (height) du Viewport.
	 * 
	 * @return Le nombre de pixels en hauteur.
	 */
	public int getHeight()
	{ 
	  return height;
	}
  
	/**
	 * M�thode pour avoir acc�s au contenu du viewport sous forme d'un BufferedImage.
	 * Il est important de ne pas modifier cet objet sous le risque de faire de la corruption dans le bufferimage du viewport. 
	 * 
	 * @return Le buffer associ� � l'image du viewport.
	 * @see BufferedImage
	 */
	public BufferedImage getBufferedImage()
	{ 
	  return image_buffer;
	  
	  /*
	  BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    //BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    
    for(int i=0; i<width; i++)
      for(int j=0; j<height; j++)
        buffer.setRGB(i, j, image[i][j].normalizeColor().getRGB());
    
    return buffer;
    */
	}
	
	/**
	 * M�thode pour obtenir une copie de l'image enregistr� dans le viewport.
	 * Cette information peut �tre modifi�e sans affecter le viewport.
	 * 
	 * @return Une copie de l'image du viewport.
	 */
	public BufferedImage copyBufferedImage()
	{
	  BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    //BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    
    for(int i=0; i<width; i++)
      for(int j=0; j<height; j++)
        buffer.setRGB(i, j, image[i][j].normalizeColor().getRGB());
    
    return buffer;
	}
	
	/**
	 * M�thode pour d�finir le num�ro de la prochaine image g�n�r� par les viewports.
	 * 
	 * @param nb - Le num�ro de l'image.
	 * @throws SRuntimeException Si le num�ro n'est pas entre 0 et 999.
	 */
	public static void setImageCounter(int nb) throws SRuntimeException
	{
		if(nb < DEFAULT_IMAGE_COUNTER || nb > MAX_IMAGE_COUNTER)
			throw new SRuntimeException("Erreur SViewport 005 : Le num�ro d'image '" + nb +"' n'est pas compris entre '" + DEFAULT_IMAGE_COUNTER + "' et '" + MAX_IMAGE_COUNTER +"'.");
		
		image_counter = nb;
	}
	
	/**
	 * M�thode pour attribuer une couleur � une coordonn�e xy d'un pixel du viewport.
	 * Il est important de rappeler que la coordonn�e (0,0) correspond au coin sup�rieur gauche du viewport.
	 * 
	 * @param x - La coordonn�e x en largeur (width) du viewport.
	 * @param y - La coordonn�e y en hauteur (height) du viewport.
	 * @param color - La couleur � affecter au pixel.
	 * @throws SCoordinateOutOfBoundException Si la coordonn�e (x,y) du pixel n'est pas admissible.
	 */
	public void setColor(int x, int y, SColor color) throws SCoordinateOutOfBoundException
	{
		if(x<0 || x>= width)
			throw new SCoordinateOutOfBoundException("Erreur SViewport 006 : La coordonn�e x = " + x + " n'est pas comprise entre 0 et " + (width-1) + "(width-1).");
		
		if(y<0 || y>= height)
			throw new SCoordinateOutOfBoundException("Erreur SViewport 007 : La coordonn�e y = " + y + " n'est pas comprise entre 0 et " + (height-1) + "(height-1).");
		
		// Mettre la couleur dans le tableau � 2 dimension.
		image[x][y] = color;
		
		// Mettre la couleur dans le buffered image.
		image_buffer.setRGB(x, y, image[x][y].normalizeColor().getRGB());
	}
	
	/**
	 * M�thode pour attribuer une couleur � une coordonn�e xy d'un pixel du viewport.
	 * Il est important de rappeler que la coordonn�e (0,0) correspond au coin sup�rieur gauche du viewport.
	 * 
	 * @param p - La coordonn�e du pixel du viewport.
	 * @param color - La couleur � affecter au pixel.
	 * @throws SCoordinateOutOfBoundException Si la coordonn�e xy du pixel n'est pas admissible.
	 */
	public void setColor(SVectorPixel p, SColor color) throws SCoordinateOutOfBoundException
	{ 
		setColor(p.getX(), p.getY(), color);
	}  
	
	/**
	 * M�thode pour effacer le viewport en r�initialisant l'ensemble des pixels � la couleur par d�faut BACKGROUND_COLOR.
	 * Il y a �galement r�initilisation de l'it�ration des pixels du viewport avec la m�thode restartPixelIteration().
	 * 
	 * @see restartPixelIteration
	 * @see BACKGROUND_COLOR
	 */
	public synchronized void clear()
	{
		for(int i=0; i<width; i++)
      for(int j=0; j<height; j++)
        image[i][j] = BACKGROUND_SCOLOR;
		
		
		restartPixelIteration();
	}
	
	/**
	 * M�thode qui r�initialise l'it�ration des pixels du viewport � l'origine (de coordonn�e (0,0)).
	 */
	public void restartPixelIteration()
	{
		current_pixel = null;
	}
	
	/**
	 * M�thode pour d�terminer s'il y a encore des pixels � analyser dans le viewport.
	 * 
	 * @return <b>true</b> s'il y a encore des pixels � analyser et <b>false</b> sinon.
	 */
	public boolean hasNextPixel()
	{
		if(current_pixel == null)
			return true;
		
		//Condition d'arr�t : Si x == width-1 et y == height-1
		if(current_pixel.getX() >= (width-1) && current_pixel.getY() >= (height-1))
			return false;
		else
			return true;
	}
	/**
	 * M�thode qui retourne le pixel suivant � �tre analys�. Cette m�thode a �t� d�velopp�e afin de pouvoir it�rer sur l'ensemble des pixels du viewport.
	 * L'it�ration va s'effectuer ligne par ligne ce qui correspond � x=0 jusqu'� x=width-1 et l'on recommence en augmentant y afin qu'il passe de y=0 � y=height-1.
	 * 
	 * @return la coordonn�e du prochain pixel � analyser et <b> null </b> s'il n'y a plus de pixel � it�rer.
	 */
	public synchronized SVectorPixel nextPixel()
	{
		if(current_pixel == null)
			current_pixel = ORIGIN_PIXEL;	// 1ier pixel �tant l'origine (0,0)
		else
		{	
			int x = current_pixel.getX();
			int y = current_pixel.getY();
		
			x++;	// incr�menter la colonne
		
			// V�rifier s'il faut changer le ligne
			if(x >= width)
			{
				x = 0;  // retourner � la colonne #0
				y++;	  // incr�menter la ligne
			
				// V�rifier si les lignes sont compl�t�es
				if(y >= height)
					return null;	// ne pas mettre � jour le current_pixel, car nous avons atteint le maximum
			}
			
			current_pixel = new SVectorPixel(x,y);	// Nouveau pixel courant
		}
		
		return current_pixel;
	}
	
	/**
	 * M�thode qui retourne le ratio width/height sans distorsion. 
	 * Ce calcul est n�cessaire pour la formation de la pyramide de vue (ViewFrustum) afin de d�terminer une hauteur de pyramide en fonction de la largueur (d�termin�e par un angle d'ouverture).
	 *  
	 * @return Le ratio width/height.
	 */
	public double normalAspect()
	{ 
	  return (double)width / (double)height; 
	}
	
	/**
	 * M�thode pour �crire un objet SReadableWriteable dans un fichier txt.
	 * 
	 * @param bw - Le buffer pour l'�criture.
	 * @throws IOException Si une erreur de l'objet BufferedWriter est lanc�e.
	 * @see BufferedWriter
	 */
	public void write(BufferedWriter bw)throws IOException
	{
		bw.write(SKeyWordDecoder.KW_VIEWPORT);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		//�crire les propri�t�s de la classe SViewport et ses param�tres h�rit�s
		writeSViewportParameter(bw);
				
		bw.write(SKeyWordDecoder.KW_END);
		bw.write(SStringUtil.END_LINE_CARACTER);
		bw.write(SStringUtil.END_LINE_CARACTER);
	}
	
	/**
	 * M�thode pour �crire les param�tres associ�s � la classe SViewport et ses param�tres h�rit�s.
	 * 
	 * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
	 * @throws IOException Si une erreur I/O s'est produite.
	 */
	protected void writeSViewportParameter(BufferedWriter bw)throws IOException
	{	
		bw.write(SKeyWordDecoder.KW_WIDTH);
		bw.write("\t\t");
		bw.write(Integer.toString(width));
		bw.write(" pixels");
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_HEIGHT);
		bw.write("\t\t");
		bw.write(Integer.toString(height));
		bw.write(" pixels");
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_IMAGE_FILE_NAME);
		bw.write("\t");
		bw.write(image_file_name);
		bw.write(SStringUtil.END_LINE_CARACTER);
	}
	
	/**
	 * M�thode pour �crire le contenu static de la classe SViewport dans un fichier txt.
	 * 
	 * @param bw - Le buffer pour l'�criture.
	 * @throws IOException Si une erreur de l'objet BufferedWriter est lanc�e.
	 * @see BufferedWriter
	 */
	public static void staticWrite(BufferedWriter bw)throws IOException
	{
		bw.write(SKeyWordDecoder.KW_VIEWPORT_IMAGE_COUNT);
	  bw.write("\t");
	  bw.write(Integer.toString(image_counter));
		bw.write(SStringUtil.END_LINE_CARACTER);
	}
		
	/**
	 * M�thode pour dessiner le viewport dans un fichier image.
	 * 
	 * @throws IOException S'il y a une erreur lors de l'�criture du fichier image. 
	 */
	public void writeImage() throws IOException
	{
		String name = image_file_name + "_";
		
		//Ajouter le num�ro de l'image g�n�r� par ce viewport
		String number = Integer.toString(image_counter);
		
		//Afficher le bon nombre de '0' au nombre entre 0 et 1000
		if(image_counter < 10)
			name = name.concat("00");
		else
			if(image_counter < 100)
				name = name.concat("0");
		
		name = name.concat(number);
		
		//G�n�rer le fichier image
		File file = new File(name + "." + DEFAULT_IMAGE_EXTENSION);
		BufferedImage buffer = getBufferedImage();
		ImageIO.write(buffer, DEFAULT_IMAGE_EXTENSION, file);
		
		image_counter++;	//augmenter le compteur
		
		//R�initialiser le compteur si la valeur maximale est atteinte
		if(image_counter > MAX_IMAGE_COUNTER)
			image_counter = DEFAULT_IMAGE_COUNTER;
	}
	
	
	/**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
	{
		image = new SColor[width][height];
		
		image_buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		clear();
	}
	
  @Override
	protected boolean read(SBufferedReader sbr, int code, String remaining_line)throws SReadingException
	{
		switch(code)
		{
			case SKeyWordDecoder.CODE_WIDTH :  width = readIntGreaterThanZero(remaining_line, SKeyWordDecoder.KW_WIDTH); return true; 
														
			case SKeyWordDecoder.CODE_HEIGHT : height = readIntGreaterThanZero(remaining_line, SKeyWordDecoder.KW_HEIGHT); return true;
														
			case SKeyWordDecoder.CODE_IMAGE_FILE_NAME : image_file_name = readStringNotEmpty(remaining_line, SKeyWordDecoder.KW_IMAGE_FILE_NAME); return true;
				
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
    return SKeyWordDecoder.KW_SPHERE;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    
    if (obj == null)
      return false;
    
    if (getClass() != obj.getClass())
      return false;
    
    SViewport other = (SViewport) obj;
    
    if (height != other.height)
      return false;
    
    if (width != other.width)
      return false;
    
    // V�rification de l'ensemble des couleurs du viewport
    if (!Arrays.deepEquals(image, other.image))
      return false;
    
    return true;
  }

}//fin classe SViewport
