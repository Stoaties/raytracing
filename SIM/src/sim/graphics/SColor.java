package sim.graphics;

import java.awt.Color;
import java.util.Locale;
import java.util.StringTokenizer;
import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.math.SMath;
import sim.math.SVector;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe SColor repr�sente une couleur � 4 composantes (r,g,b,a) rouge, vert, bleu et alpha (transparence). 
 * Cette classe est compl�mentaire � la classe java.awt.Color puisqu'elle pourra supporter des op�rations d'addition et de multiplication par un scalaire. 
 * Cette classe permet l'initialisation de ses param�tres � partir d'un String.
 * Cette classe permet une �criture dans un fichier de format txt � l'aide de la classe BufferedWriter.
 * 
 * @see java.awt.Color
 * @see java.io.BufferedWriter
 * @author Simon Vezina
 * @since 2014-12-18
 * @version 2017-12-18
 */
public class SColor implements SVector {

	
  //--------------
  // CONSTANTES //
  //--------------
  
	/**
	 * La constant <b>NEGATIVE_COLOR_LIMITE</b> repr�sente une limite n�gative � laquelle une couleur sera consid�r�e comme �tant nulle.
	 */
	private static final double NEGATIVE_COLOR_LIMITE = -1e-8;
	
  /**
   * La constante <b>COLOR_NORMALIZATION</b> correspond � l'ensemble des mots cl�s qui sont reconnus pour identifier un code de r�f�rence pour d�terminer le choix de l'algorithme de normalisation des couleurs.
   */
  public static final String[] COLOR_NORMALIZATION = {"clamp_channel", "division_factor"};
  
  /**
   * La constante <b>CLAMP_CHANNEL_NORMALIZATION</b> correspond au code de r�f�rence pour utiliser l'algorithme de normalisation des couleurs par coupure de canal.
   * Cette approche r�duit � une valeur �gale � 1.0 tout canal sup�rieur � 1.0. 
   */
  public static final int CLAMP_CHANNEL_NORMALIZATION = 0;
  
  /**
   * La constante <b>DIVISION_FACTOR_NORMALIZATION</b> correspond au code de r�f�rence pour utiliser l'algorithme de normalisation des couleurs par facteur de division.
   * Cette approche r�duit tous les canaux d'un certain facteur selon l'amplitude maximale de l'un d'eux. 
   * Si un canal est sup�rieur � 1.0 et qu'il est le plus �lev� parmis les canaux rgb, il sera divis� par sa grandeur le r�duisant � 1.0 et tous les autres canaux rgb seront divis�s par ce m�me facteur les r�duisant � une valeur inf�rieure au canal de valeur maximal.
   */
  public static final int DIVISION_FACTOR_NORMALIZATION = 1;
	
  /**
   * La constante <b>CHANNEL_MAX_VALUE</b> correspond � la valeur maximale d'un canal d'une couleur �tant �gale � 1.0.
   */
  private static double CHANNEL_MAX_VALUE = 1.0;                
  
  /**
   * La constante <b>DEFAULT_CHANNEL</b> correspond � la valeur par d�faut d'un canal d'une couleur �tant �gale � 1.0.
   */
  private static double DEFAULT_CHANNEL = 1.0;            
  
  /**
   * La constante <b>BLACK</b> correspond � la couleur <u>noire</u>.
   */
  public static SColor BLACK = new SColor(0.0, 0.0, 0.0, 1.0);
  
  /**
   * La constante <b>WHITE</b> correspond � la couleur <u>blanche</u>.
   */
  public static SColor WHITE = new SColor(1.0, 1.0, 1.0, 1.0);
  
  //-----------------------
  // VARIABLES STATIQUES //
  //-----------------------
  
  /**
   * La variable <b>color_normalization</b> correspond au code de r�f�rence utilis� pour choisir l'algorithme de normalisation des couleurs.
   */
  private static int color_normalization = CLAMP_CHANNEL_NORMALIZATION;   
	
  //-------------
  // VARIABLES //
  //-------------
  
	/**
	 * La variable <b>r</b> correspond au canal de couleur rouge (<i>red</i>). Cette valeur doit �tre toujours positive.
	 */
  final private double r;	
  
  /**
   * La variable <b>g</b> correspond au canal de couleur verte (<i>green</i>). Cette valeur doit �tre toujours positive.
   */
	final private double g;	
	
	/**
   * La variable <b>r</b> correspond au canal de couleur bleu (<i>blue</i>). Cette valeur doit �tre toujours positive.
   */
	final private double b;	
	
	/**
	 * La variable <b>a</b> correspond au canal de couleur alpha �tant la transparence. Cette valeur doit �tre toujours positive.
	 * La valeur <b>0.0</b> correspond � une transparence � 100% et une valeur <b>1.0</b> correspond � une transparence � 0% (opacit� compl�te).
	 */
	final private double a;	
	
	//----------------
	// CONSTRUCTEUR //
	//----------------
	
	/**
	 * Constructeur vide correpondant � la couleur blanche et transparence nulle (alpha = 1).
	 */
	public SColor()
	{
		this(DEFAULT_CHANNEL, DEFAULT_CHANNEL, DEFAULT_CHANNEL);
	}
	
	/**
	 * Constructeur d'une couleur avec transparance nulle (compl�tement opaque). La valeur de alpha est de 1.
	 * 
	 * @param red - La couleur rouge.
	 * @param green - La couleur verte.
	 * @param blue - La couleur bleu.
	 * @throws SConstructorException Si la couleur est d�finie avec un param�tre n�gatif.
	 */
	public SColor(double red, double green, double blue) throws SConstructorException
	{ 
		this(red, green, blue, 1.0);
	}
	
	/**
	 * Constructeur d'une couleur avec transparence alpha.
	 * 
	 * @param red - La couleur rouge.
	 * @param green - La couleur verte.
	 * @param blue - La couleur bleu.
	 * @param alpha - La transparence.
	 * @throws SConstructorException Si la couleur est d�finie avec un param�tre n�gatif.
	 */
	public SColor(double red, double green, double blue, double alpha) throws SConstructorException
	{
		// V�rifier si la couleur poss�de uniquement des composantes positives.
	  // Pour des raisons num�riques, il est possible qu'un canal ait une valeur tr�s pr�s de z�ro, mais n�gativement (-0).
	  // Cette couleur sera alors interpr�t�e comme �tant pr�s de z�ro, mais transform�e positivement (-0 vers +0) par la suite.
	  if(red < NEGATIVE_COLOR_LIMITE || green < NEGATIVE_COLOR_LIMITE || blue < NEGATIVE_COLOR_LIMITE || alpha < NEGATIVE_COLOR_LIMITE)
		  throw new SConstructorException("Erreur SColor 001 : La couleur [" + red + "," + green + "," + blue + "," + alpha + "] poss�de un param�tre n�gatif et inf�rieur au seuil acceptable de " + NEGATIVE_COLOR_LIMITE + ".");
		
	  // Affecter la valeur absolue aux diff�rents canaux de couleur (pour g�rer le cas particulier pr�c�dent)
	  r = Math.abs(red);
		g = Math.abs(green);
		b = Math.abs(blue);
		a = Math.abs(alpha);
	}
	
	/**
	 * <p>
	 * Constructeur d'une couleur en utilisant un String comme param�tre fractionn� dans un StringTokenizer. Une lecture autoris�e peut prendre la forme suivante en fonction du nombre de tokens dans le StringTokenizer.
	 * </p>
	 * <p>Format (1) : "nom couleur"</p>
	 * <p>Format (2) : "nom couleur" "float alpha"</p>
	 * <p>Format (3) : "float r" "float g" "float b"</p>
	 * <p>Format (4) : "float r" "float g" "float b" "float a"</p>
	 * @param string - L'expression donnant acc�s aux param�tres r, g, b et a de la couleur.
	 * @throws SReadingException Si le format de la lecture n'est pas ad�quat.
	 * @throws SConstructorException Si la couleur est d�finie avec un param�tre n�gatif.
	 */
	public SColor(String string)throws SReadingException, SConstructorException
	{
		double[] tab = read(string);
		
		//V�rifier si la couleur poss�de uniquement des composantes positives
		if(tab[0] < 0.0 || tab[1] < 0.0 || tab[2] < 0.0 || tab[3] < 0.0)
      throw new SConstructorException("Erreur SColor 002 : L'expression de la La couleur �gale � [" + tab[0] + "," + tab[1] + "," + tab[2] + "," + tab[3] + "] poss�de un param�tre n�gatif.");
    
		r = tab[0];
		g = tab[1];
		b = tab[2];
		a = tab[3];
	}
	
	/**
	 * Constructeur d'une couleur � partir d'une couleur de type <b>Color</b> d�finit � l'aide de canaux entier compris dans l'intervalle [0, 255]. 
	 * @param color La couleur � convertir.
	 */
	public SColor(Color color)
	{
	  float[] compArray = new float[4];
	  compArray = color.getColorComponents(compArray);
	  
	  r = (double) compArray[0];
	  g = (double) compArray[1];
	  b = (double) compArray[2];
	  a = (double) compArray[3];
	}
	
	/**
	 * Constructeur d'une couleur � partir d'une couleur de type <b>Color</b> d�finit � l'aide de canaux entier compris dans l'intervalle [0, 255]. 
	 * Cependant, le canal alpha est red�fini � partir d'une nouvelle valeur pass�e en param�tre.
	 * @param color La couleur � convertir.
	 * @param alpha Le nouveau param�tre de transparence. 
	 */
	public SColor(Color color, double alpha)
	{
	  float[] compArray = new float[4];
    compArray = color.getColorComponents(compArray);
    
    r = (double) compArray[0];
    g = (double) compArray[1];
    b = (double) compArray[2];
    a = alpha;
	}
	
	//------------
	// M�THODES //
	//------------
	
	/**
	 * M�thode qui donne la couleur du canal rouge.
	 * @return La couleur rouge.
	 */
	public double getRed()
	{ 
	  return r;
	}
	
	/**
	 * M�thode qui donne la couleur du canal vert.
	 * @return La couleur verte.
	 */
	public double getGreen()
	{ 
	  return g;
	}
	
	/**
	 * M�thode qui donne la couleur du canal blue.
	 * @return La couleur bleu.
	 */
	public double getBlue()
	{ 
	  return b;
	}
	
	/**
	 * M�thode qui donne la transparence de la couleur.
	 * @return La transparence dans l'intervalle [0,1].
	 */
	public double getAlpha()
	{ 
	  return a;
	}
	
	/**
	 * M�thode pour obtenir le code correspondant au type de normalisation appliqu�e aux couleurs dont un canal ou plus d�passe la valeur de 1.0.
	 * @return Le code du type de normalisation.
	 */
	public static int getColorNormalization()
	{
	  return color_normalization;
	}
	
	/**
	 * M�thode pour d�finir le type de normalisation qui sera appliqu�e aux couleurs dont un canal au plus d�passe la valeur de 1.0.
	 * @param code - Le code associ� au type d'algorithme de normalisation de la couleur.
	 * @throws SRuntimeException Si le code de r�f�rence n'est pas reconnu par le syst�me.
	 */
	public static void setColorNormalization(int code)throws SRuntimeException
	{
	  switch(code)
    {
      case CLAMP_CHANNEL_NORMALIZATION : 
      
      case DIVISION_FACTOR_NORMALIZATION : color_normalization = code; break;
    
      default : throw new SRuntimeException("Erreur SColor 003 : Le type de normalisation '" + code + "' n'est pas reconnu par le syst�me.");
    }
	}
	
	@Override
  public SVector add(SVector v) 
	{
	  return add((SColor) v);
  }
	
	/**
	 * M�thode qui retourne l'addition de la couleur de l'objet avec une nouvelle couleur. 
	 * @param c - La couleur � ajouter.
	 * @return La somme des deux couleurs sans influancer la transparence.
	 */
	public SColor add(SColor c)
	{	
		return new SColor(r + c.r, g + c.g, b + c.b, a);
	}
	
	/**
	 * M�thode qui retourne le r�sultat de la multiplication par un scalaire de chaque canal de la couleur sauf le canal alpha restant inchang�. Ce calcul correcpond � la multiplication d'un vecteur par un scalaire sans affecter la 4i�me composante.
	 * @param m - Le muliplicateur de chaque canal (sauf le alpha).
	 * @return La couleur mulipli�e par un scalaire.
	 */
	public SColor multiply(double m)
	{
		return new SColor(m*r, m*g, m*b, a);
	}
	
	/**
	 * M�thode pour multiplier deux couleurs ensembles. Le calcul correspond � muliplier les canaux semblables ensemble (sauf le canal alpha).
	 * @param c La couleur � multiplier
	 * @return Un nouvelle couleur qui est le produit de deux couleurs pr�c�dentes.
	 */
	public SColor multiply(SColor c)
	{
		return new SColor(r * c.r, g * c.g, b * c.b, a);
	}
	
	@Override
  public double dot(SVector v)
  {
	  return dot((SColor)v);
  }
	
	/**
   * M�thode pour effectuer le <b>produit scalaire</b> entre deux vecteurs.
   * 
   * @param c Le vecteur � mettre en produit scalaire.
   * @return Le produit scalaire entre les deux vecteurs.
   */
	public double dot(SColor c)
  {
    return r*c.r + g*c.g + b*c.b + a*c.a;
  }
	
	/**
	 * M�thode pour normaliser une couleur. Le type de normalisation d�pend du choix de l'algorythme choisi. 
	 * @return La couleur apr�s normalisation.
	 * @throws SRuntimeException Si le type de normalisation n'est pas reconnu par le syst�me.
	 * @see normalizeClampChannel
	 * @see normalizeDivisionFactor
	 */
	public SColor normalize()throws SRuntimeException
	{
		switch(color_normalization)
		{
		  case CLAMP_CHANNEL_NORMALIZATION : return normalizeClampChannel();
		  
		  case DIVISION_FACTOR_NORMALIZATION : return normalizeDivisionFactor();
		
		  default : throw new SRuntimeException("Erreur SColor 005 : Le type de normalisation '" + color_normalization + "' n'est pas reconnu par le syst�me.");
		}
	}
	
	/**
	 * M�thode pour normaliser une couleur en appliquant un seuil minimal et maximal � chaque canal de la couleur sauf pour le canal alpha restant inchang�.
	 * Ainsi, un canal ayant une valeur inf�rieure ou sup�rieure aux valeurs de seuil sera r�duit � la valeur de seuil appropri�.
	 * Habituellement, le seuil est limit� entre 0.0 et 1.0 en raison des standards des librairies graphiques. 
	 * @return La couleur normalis�e selon les seuils (sauf pour le alpha).
	 */
	private SColor normalizeClampChannel()
	{
		double tmp_r = Math.min(r, CHANNEL_MAX_VALUE);
				
		double tmp_g = Math.min(g, CHANNEL_MAX_VALUE);
				
		double tmp_b = Math.min(b, CHANNEL_MAX_VALUE);
				
		double tmp_a = Math.min(a, CHANNEL_MAX_VALUE);
		
		return new SColor(tmp_r, tmp_g, tmp_b, tmp_a);
	}
	
	/**
	 * M�thode pour normaliser une couleur en divisant l'amplitude des canaux par l'amplitude du plus grand 
	 * seulement si un canal poss�de une amplitude sup�rieure � 1.0 .
	 * @return
	 */
	private SColor normalizeDivisionFactor()
	{
	  //Trouver le canal ayant la valeur la plus grande
	  double max = r;
	  
	  if(max < g)
	    max = g;
	  
	  if(max < b)
	    max = b;
	  
	  //S'assurer de ne pas augmenter les canaux de couleur
	  if(max < 1.0)
	    max = 1.0;
	  
	  //Diviser tous les canaux par la valeur maximale ce qui limite � 1.0 le plus grand canal et les autres seront �galement r�duit par le m�me facteur
	  return new SColor(r/max, g/max, b/max, a);
	}
	
	/**
	 * M�thode qui retourne un objet Color convertible en format RGB [0..255]. Si un canal rgb d�passe l'intervalle [0..1], il est alors maximis� � 1.0f (ou 255). Cette couleur est alors normalis�e.
	 * @return La couleur apr�s normalisation.
	 * @see Color
	 */
	public Color normalizeColor()
	{
		SColor nor_color = normalize();
		
		return new Color((float)nor_color.r, (float)nor_color.g, (float)nor_color.b, (float)nor_color.a);
	}
	
	@Override
	public String toString()
	{
		return "[ " + r + ", " + g + ", " + b + ", " + a + " ]";		
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other == null) 							//Test du null
			return false;
		    
		if (other == this)							//Test de la m�me r�f�rence 
			return true;
		    
		if (!(other instanceof SColor))	//Test d'un type diff�rent
		    	return false;
		    
		//V�rification de chaque canal de la couleur (si identique)
		SColor o = (SColor)other;
		
		if(!SMath.nearlyEquals(r, o.r))
		  return false;
		
		if(!SMath.nearlyEquals(g, o.g))
      return false;
		
		if(!SMath.nearlyEquals(b, o.b))
      return false;
		
		if(!SMath.nearlyEquals(a, o.a))
      return false;
		
		return true;    
	}
	
	/**
	 * M�thode pour lire une couleur en utilisant un String comme param�tre fractionn� dans un StringTokenizer. Une lecture autoris�e peut prendre la forme suivante en fonction du nombre de tokens dans le StringTokenizer.
	 *  Format (1) : "nom couleur"
	 *  Format (2) : "nom couleur" "double alpha"
	 *  Format (3) : "double r" "double g" "double b"
	 *  Format (4) : "double r" "double g" "double b" "double a"
	 *  Remarque : Les caract�ses , ( ) [ ] < > seront utilis�s comme d�limieur dans l'expression.
	 * @param string L'expression de la couleur en format texte.
	 * @return Un tableau donnant acc�s au couleur rgba tel que r = [0], g = [1], b = [2] et a = [3].
	 * @throws SReadingException Si le format de la lecture n'est pas ad�quat avec l'un des quatre modes de lecture.
	 */
	private double[] read(String string) throws SReadingException
	{
		double[] tab = new double[4];
		double[] tmp;
		
		StringTokenizer tokens = new StringTokenizer(string,SStringUtil.REMOVE_CARACTER_TOKENIZER);	
		
		switch(tokens.countTokens())
		{
			case 0 : 	throw new SReadingException("Erreur SColor 006 : Il n'y a pas d'�l�ment dans la d�finition de la couleur."); 
			
			case 1 : 	tmp = readColorName(tokens.nextToken()); 
    						tab[0] = tmp[0];
    						tab[1] = tmp[1];
    						tab[2] = tmp[2];
    						tab[3] = 1.0; 
    						break;
			
			case 2 :	tmp = readColorName(tokens.nextToken()); 
    						tab[0] = tmp[0];
    						tab[1] = tmp[1];
    						tab[2] = tmp[2];
    						tab[3] = readChannel(tokens.nextToken());
    						break;
			
			case 3 :	tab[0] = readChannel(tokens.nextToken());
    						tab[1] = readChannel(tokens.nextToken());
    						tab[2] = readChannel(tokens.nextToken());
    						tab[3] = 1.0;
    						break;
			
			case 4 :	tab[0] = readChannel(tokens.nextToken());
    						tab[1] = readChannel(tokens.nextToken());
    						tab[2] = readChannel(tokens.nextToken());
    						tab[3] = readChannel(tokens.nextToken());
    						break;
			
			default :	throw new SReadingException("Erreur SColor 007 : L'expression '" + string + "' contient plus de 4 �l�ments dans la lecture de la couleur ce qui est invalide."); 
		} 
		
		return tab;
	}
	
	/**
	 * M�thode pour faire l'analyser d'un String afin d'affecter les canaux rgb � une valeur correspondant au choix de la couleur nomm� par le String.
	 * @param s Le nom de la couleur.
	 * @return Un tableau donnant acc�s au couleur rgb tel que r = [0], g = [1] et b = [2].
	 * @throws SReadingException Si le nom de la couleur ne fait pas partie de la liste disponible dans la classe SColor. Les noms disponibles sont : blanc/white, noir/black.
	 */
	private double[] readColorName(String s) throws SReadingException
	{
		double[] tab = new double[3];
		
		String color_name = s.toLowerCase(Locale.ENGLISH);
		
		//Analyser le texte de la couleur
		if(color_name.equals("black") || color_name.equals("noir"))
		{	tab[0] = BLACK.r;	tab[1] = BLACK.g;	tab[2] = BLACK.b;	}
		else 
		if(color_name.equals("white") || color_name.equals("blanc"))
		{	tab[0] = WHITE.r;	tab[1] = WHITE.g;	tab[2] = WHITE.b;	}
		else 
		if(color_name.equals("red") || color_name.equals("rouge"))
		{	tab[0] = 1.0;	tab[1] = 0.0;	tab[2] = 0.0;	}
		else 
		if(color_name.equals("green") || color_name.equals("vert"))
		{	tab[0] = 0.0;	tab[1] = 1.0;	tab[2] = 0.0;	}
		else 
		if(color_name.equals("blue") || color_name.equals("bleu"))
		{	tab[0] = 0.0;	tab[1] = 0.0;	tab[2] = 1.0;	}
		else
			throw new SReadingException("Erreur SColor 008 : La couleur �" + color_name + "� n�est pas reconnue.");
	
		return tab;
	}
	
	/**
	 * M�thode pour faire l'analyse d'un string afin d'y retourner un type double pour affecter cette valeur � un canal d'une couleur.
	 * @param s Le String � analyser.
	 * @return La valeur du String en format double.
	 * @throws SReadingException Si l'expression du String ne peut pas �tre convertie en type double.
	 */
	private double readChannel(String s) throws SReadingException
	{
		try{
			return Double.valueOf(s);
		}catch(NumberFormatException e){ 
			throw new SReadingException("Erreur SColor 009 : L'expression '" + s + "' n'est pas un nombre de type float pouvant �tre affect� � un canal r, g, b ou a d'une couleur."); 
		}	
	}
	
	/**
	 * M�thode pour �crire les param�tres rgba de la couleur dans un fichier en format txt. Le format de l'�criture est "rouge"  "vert"  "bleu"  "alpha" comme l'exemple suivant : 0.6  0.2  0.5  1.0
	 * @param bw Le buffer d'�criture.
	 * @throws IOException S'il y a une erreur avec le buffer d'�criture.
	 * @see BufferedWriter
	 */
	public void write(BufferedWriter bw)throws IOException
	{
		bw.write(toString());
	}
	
}//fin classe SColor
