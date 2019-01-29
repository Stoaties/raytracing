/**
 * 
 */
package sim.math;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;

/**
 * La classe <b>SVectorPixel</b> repr�sente un vecteur pour positionner un pixel dans un <b>viewport</b>.
 * 
 * @author Simon V�zina
 * @since 2014-12-27
 * @version 2017-12-14
 */
public class SVectorPixel implements SVector {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante 'DEFAULT_COMPONENT' correspond � la coordonn�e par d�faut des composantes x et y du pixel qui est �gale � {@value}. 
   */
  private final static int DEFAULT_COMPONENT = 0;
  
  //--------------
  // VARIABLES  //
  //--------------
  
	/**
	 * La variable 'x' correspond � la coordonn�e x du pixel.
	 */
  private final int x;		
  
  /**
   * La variable 'y' correspond � la coordonn�e y du pixel.
   */
	private final int y;		
	
	//-----------------------------
	// CONSTRUCTEURS ET M�THODES //
	//-----------------------------
	
	/**
	 * Constructeur d'un pixel par d�faut. Ce vecteur correspond � l'origine d'un viewport �tant � la coordonn�e (0,0).
	 * Dans la grande majorit� des viewports, cette coordonn�e correspond g�om�triquement au coint sup�rieur gauche. 
	 */
	public SVectorPixel()
	{
		this(DEFAULT_COMPONENT, DEFAULT_COMPONENT);
	}
	
	/**
	 * Constructeur d'un vecteur de pixel.
	 * 
	 * @param x - La coordonn�e x du pixel.
	 * @param y - La coordonn�e y du pixel.
	 * @throws SConstructorException Si le pixel poss�de une coordonn�e n�gative.
	 */
	public SVectorPixel(int x, int y) throws SConstructorException
	{
	  //V�rification des coordonn�es afin qu'elles soient positives
	  if(x < 0)
	    throw new SConstructorException("Erreur SVectorPixel 001 : La coordonn�e x = " + x + " ne peut pas �tre n�gative.");
		
	  if(y < 0)
	    throw new SConstructorException("Erreur SVectorPixel 002 : La coordonn�e y = " + y + " ne peut pas �tre n�gative.");
			
	  this.x = x;
	  this.y = y;
	}

	/**
	 * M�thode pour obtenir la coordonn�e x du pixel.
	 * 
	 * @return La coordonn�e x du pixel.
	 */
	public int getX()
	{ 
	  return x;
	}
	
	/**
	 * M�thode pour obtenir la coordonn�e y du pixel.
	 * 
	 * @return La coordonn�e y du pixel.
	 */
	public int getY()
	{ 
	  return y;
	}
	
	@Override
	public SVector add(SVector v) 
  {
	  return add((SVectorPixel)v);
  }
  
	/**
	 * M�thode pour effectuer l'addition d'une coordonn�e de pixel avec une autre.
	 * 
	 * @param p - La coordonn�e de pixel � additionner.
	 * @return La coordonn�e de pixel r�sultant de l'addition d'une autre coordonn�e de pixel.
	 */
	public SVectorPixel add(SVectorPixel p)
	{
	  return new SVectorPixel(x + p.x, y + p.y);
	}
	
	/**
	 * M�thode pour effectuer la multiplication par un scalaire avec une coordonn�e de pixel. Si la constante est n�gative,
	 * une exception de type <b>SConstrutorException</b> sera lanc�e puisqu'une coordonn�e de pixel ne peut pas contenir de composante n�gative.
	 * 
	 * @param cst - La constante � multiplier avec la coordonn�e de pixel.
	 * @return La coordonn�e de pixel r�sultant de la multiplication par un scalaire.
	 * @throws SConstructorException Si la constante � multiplier est n�gative ne permettant pas de construire une coordonn�e de pixel avec des composantes n�gatives.
	 */
	public SVectorPixel multiply(double cst) throws SConstructorException
	{
	  return new SVectorPixel((int)(x*cst), (int)(y*cst));
	}

	@Override
  public double dot(SVector v)
  {
    return dot((SVectorPixel)v);
  }
  
  /**
   * M�thode pour effectuer le <b>produit scalaire</b> entre deux vecteurs.
   * 
   * @param v Le vecteur � mettre en produit scalaire.
   * @return Le produit scalaire entre les deux vecteurs.
   */
  public double dot(SVectorPixel v)
  {
    return (x*v.x + y*v.y);
  }
  
	@Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    
    if (obj == null) {
      return false;
    }
    
    if (!(obj instanceof SVectorPixel)) {
      return false;
    }
    
    SVectorPixel other = (SVectorPixel) obj;
    
    if (x != other.x) 
      return false;
    
    if (y != other.y) 
      return false;
    
    return true;
  }

  @Override
  public String toString()
  { 
    return new String("[" + x + ", " + y + "]");
  }
	
  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(toString());
  }
	
}//fin de la classe SVectorPixel
