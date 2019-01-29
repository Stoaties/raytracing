package sim.math;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * <p>
 * La classe <b>SVector4d</b> repr�sente un vecteur en 4 dimension xyzt pouvant effectuer des op�rations math�matiques.
 * </p>
 * 
 * <p>
 * Les op�rations math�matiques support�es par ce vecteur sont les suivantes :
 * <ul> - L'addition.</ul>
 * <ul> - La soustraction.</ul>
 * <ul> - La multiplication par un scalaire.</ul>
 * <ul> - Le produit scalaire.</ul>
 * <ul> - La multiplication par une matrice 4x4 (de type <b>SMatrix4x4</b>).</ul>
 * </p>
 * 
 * @author Simon Vezina
 * @since 2015-02-14
 * @version 2017-06-20
 */
public class SVector4d implements SVector {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante 'DEFAULT_COMPONENT' correspond � la composante par d�faut des variables x,y et z �tant �gale � {@value}.
   */
	final private static double DEFAULT_COMPONENT = 0.0;
	
	/**
	 * La constante 'DEFAULT_T' correspond � la composante par d�faut de la variable t �tant �gale � {@value}.
	 */
	final private static double DEFAULT_T = 1.0;
	
	/**
   * La constante <b>ORIGIN</b> repr�sente le vecteur d�signant l'origine en 3D. Ce vecteur en 4D est situ� � la coordonn�e (0.0, 0.0, 0.0, 1.0).
   * Ce vecteur peut �galement repr�senter un vecteur orientation <b>sans</b> orientation.
   */
  public static final SVector4d ORIGIN = new SVector4d(0.0, 0.0, 0.0, 1.0);
  
	//-------------
	// VARIABLES //
	//-------------
	
	/**
	 * La variable 'x' correspond � la composante x du vecteur 4d.
	 */
	final private double x;	
	
	/**
   * La variable 'y' correspond � la composante y du vecteur 4d.
   */
	final private double y;	
	
	/**
   * La variable 'z' correspond � la composante z du vecteur 4d.
   */
	final private double z;	
	
	/**
   * La variable 't' correspond � la composante t du vecteur 4d.
   */
	final private double t;	
	
	//-----------------------------
	// CONSTRUCTEURS ET M�THODES //
	//-----------------------------
	
	/**
	 * Constructeur d'un vecteur 4d � l'origine d'un syst�me d'axe <i>xyz</i> dont <i>t</i> = 1.0.
	 */
	public SVector4d()
	{
		this(DEFAULT_COMPONENT, DEFAULT_COMPONENT, DEFAULT_COMPONENT);
	}
	
	/**
	 * Constructeur d'un vecteur 4d � partir d'un vecteur 3d dont la 4i�me dimension <i>t</i> sera �gale � 1.0.
	 * @param v - Le vecteur 3d.
	 */
	public SVector4d(SVector3d v)
	{
	  this(v.getX(), v.getY(), v.getZ());
	}
	
	/**
	 * Constructeur d'un vecteur 4d � la position xyz dont t = 1.0.
	 * @param x - La composante <i>x</i> du vecteur.
	 * @param y - La composante <i>y</i> du vecteur.
	 * @param z - La composante <i>z</i> du vecteur.
	 */
	public SVector4d(double x, double y, double z)
	{
		this(x,y,z,DEFAULT_T);
	}
	
	/**
	 * Constructeur avec composante x,y,z et t du vecteur 4d.
	 * @param x - La composante <i>x</i> du vecteur.
	 * @param y - La composante <i>y</i> du vecteur.
	 * @param z - La composante <i>z</i> du vecteur.
	 * @param t - La composante <i>t</i> du vecteur.
	 */
	public SVector4d(double x, double y, double z, double t)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.t = t;
	}
	
	/**
	 * Constructeur d'un vecteur 4d en utilisant un string d�crivant les param�tres x, y, z et t du vecteur. 
	 * Une lecture autoris�e peut prendre la forme suivante : "double x" "double y" "double z" "double t" (incluant la notation avec , ( ) [ ] comme d�limieur dans l'expression du string comme par exemple (2.3, 4.3, 5.7, 1.0) ).
	 * @param string - Le string de l'expression du vecteur en param�tres x, y, z et t.
	 * @throws SReadingException Si le format de la lecture n'est pas ad�quat.
	 */
	public SVector4d(String string)throws SReadingException
	{
		double[] tab = read(string);
		
		x = tab[0];
		y = tab[1];
		z = tab[2];
		t = tab[3];
	}
	
	/**
	 * M�thode qui donne acc�s � la coordonn�e x du vecteur.
	 * @return La coordonn�e x.
	 */
	public double getX()
	{ 
	  return x;
	}
	
	/**
	 * M�thode qui donne acc�s � la coordonn�e y du vecteur.
	 * @return La coordonn�e y.
	 */
	public double getY()
	{ 
	  return y;
	}
	
	/**
	 * M�thode qui donne acc�s � la coordonn�e z du vecteur.
	 * @return La coordonn�e z.
	 */
	public double getZ()
	{ 
	  return z;
	}
	
	/**
	 * M�thode qui donne acc�s � la coordonn�e t du vecteur.
	 * @return La coordonn�e t.
	 */
	public double getT()
	{ 
	  return t;
	}
	
	/**
	 * M�thode pour obtenir un vecteur 3d � partir des composantes <i>x</i>,<i>y</i> et <i>z</i> du vecteur 4d.
	 * La composante <i>t</i> est alors �cart�e.
	 * @return
	 */
	public SVector3d getSVector3d()
	{
	  return new SVector3d(x, y, z);
	}
	
	 @Override
	 public SVector add(SVector v)
	 {
	   return add((SVector4d) v);
	 }
	 
	/**
	 * M�thode qui retourne <b>l'addition</b> de deux vecteurs. 
	 * @param v - Le vecteur � ajouter au vecteur pr�sent.
	 * @return La somme des deux vecteurs.
	 */
	public SVector4d add(SVector4d v)
	{	
		return new SVector4d(x + v.x, y + v.y, z + v.z, t + v.t);
	}
	
	/**
	 * M�thode qui retourne la <b>soustraction</b> de deux vecteurs. 
	 * @param v - Le vecteur � soustraire au vecteur pr�sent.
	 * @return La soustraction des deux vecteurs.
	 */
	public SVector4d substract(SVector4d v)
	{
		return new SVector4d(x - v.x, y - v.y, z - v.z, t - v.t);
	}
	
	/**
	 * M�thode qui effectue la <b>multiplication par une scalaire</b> sur un vecteur.
	 * @param m - Le muliplicateur.
	 * @return Le r�sultat de la multiplication par un scalaire m sur le vecteur.
	 */
	public SVector4d multiply(double m)
	{
		return new SVector4d(m*x, m*y, m*z, m*t);
	}
		
	/**
	 * M�thode pour obtenir le <b>module</b> d'un vecteur.
	 * @return Le module du vecteur.
	 */
	public double modulus()
	{
		return Math.sqrt((x*x) + (y*y) + (z*z) + (t*t));
	}
	  
	/**
	 * M�thode pour <b>normaliser</b> un vecteur � quatre dimensions. 
	 * Un vecteur normalis� poss�de la m�me orientation, mais poss�de une <b>longeur unitaire</b>.
	 * @return Le vecteur normalis�.
	 * @throws SImpossibleNormalizationException Si le vecteur ne peut pas �tre normalis� (de module �gal � z�ro). 
	 */
	public SVector4d normalize() throws SImpossibleNormalizationException
	{
	  double mod = modulus();     //obtenir le module du vecteur
    
    //V�rification du module. S'il est trop petit, nous ne pouvons pas num�riquement normaliser ce vecteur
    if(mod < SMath.EPSILON)
      throw new SImpossibleNormalizationException("Erreur SVector4d 001 : Le vecteur " + this.toString() + " �tant nul ou pr�s que nul ne peut pas �tre num�riquement normalis�.");
    else
      return new SVector4d(x/mod, y/mod, z/mod, t/mod);
	}
	
	@Override
  public double dot(SVector v)
  {
    return dot((SVector4d)v);
  }
	
	/**
	 * M�thode pour effectuer le <b>produit scalaire</b> avec un autre vecteur v.
	 * @param v - L'autre vecteur en produit scalaire.
	 * @return Le produit scalaire entre les deux vecteurs.
	 */
	public double dot(SVector4d v)
	{
		return (x*v.getX() + y*v.getY() + z*v.getZ() + t*v.getT());
	}
		
	/**
	 * M�thode utilisant un string comme param�tre pour d�finir les composantes x, y, z et t du vecteur. 
	 * Une lecture autoris�e peut prendre la forme suivante : "double x" "double y" "double z" "double t"
	 *  (en incluant la notation avec , ( ) [ ] < > comme d�limieur dans l'expression du string comme par exemple (2.3, 4.3, 5.7, 1.0) ).
	 *  
	 * @param string - Le string de l'expression du vecteur en param�tres x, y, et z.
	 * @return un tableau de trois �l�ments tel que x = [0], y = [1], z = [2] et t = [3]. 
	 * @throws SReadingException Si le format de lecture n'est pas ad�quat.
	 */
	private double[] read(String string)throws SReadingException
	{
		StringTokenizer tokens = new StringTokenizer(string, SStringUtil.REMOVE_CARACTER_TOKENIZER);
		
		if(tokens.countTokens() != 4)
			throw new SReadingException("Erreur SVector4d 002 : L'expression '" + string + "' ne contient pas exactement 4 param�tres pour les composantes xyzt du vecteur 4d.");
		else
		{
			String s = "";					// String � convertir en double pour les composantes x, y et z.
			String comp = "";				// Nom de la composante en lecture utilis� pour l'envoie d'une Exception s'il y a lieu.
			
			try
			{
			  // Tableau des 4 composantes xyzt
			  double[] tab = new double[4];	
				
				comp = "x";
				s = tokens.nextToken();
				tab[0] = Double.valueOf(s);
				
				comp = "y";
				s = tokens.nextToken();
				tab[1] = Double.valueOf(s);
				
				comp = "z";
				s = tokens.nextToken();
				tab[2] = Double.valueOf(s);
				
				comp = "t";
				s = tokens.nextToken();
				tab[3] = Double.valueOf(s);
				
				return tab;
				
			}catch(NumberFormatException e){ 
				throw new SReadingException("Erreur SVector4d 003 : L'expression '" + s +"' n'est pas valide pour d�finir la composante '" + comp + "' du vecteur en cours.");
			}
		}	
	}
	
	@Override
	public void write(BufferedWriter bw) throws IOException
	{
		bw.write(toString());
	}
	
	@Override
	public String toString()
	{
		return "[" + x + ", " + y + ", " + z + ", " + t + "]";		
	}
	
	@Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(t);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(x);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(z);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if(this == obj)
      return true;
    
    if(obj == null)
      return false;
    
    if(!(obj instanceof SVector4d))
      return false;
    
    SVector4d other = (SVector4d) obj;
    
    //Comparaison des valeurs x,y,z et t en utilisant les m�thode de la classe SMath
    if(!SMath.nearlyEquals(x, other.x))
      return false;
    
    if(!SMath.nearlyEquals(y, other.y))
      return false;
    
    if(!SMath.nearlyEquals(z, other.z))
      return false;
    
    if(!SMath.nearlyEquals(t, other.t))
      return false;
          
    return true;
  }
  
}//fin de la classe SVector4d
