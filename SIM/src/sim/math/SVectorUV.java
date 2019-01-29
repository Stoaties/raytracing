package sim.math;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe SVectorUV repr�sentante une coordonn�e uv associ�e habituellement � une texture. 
 * Ce vecteur poss�de une composante u et v et sont <b>habituellement</b> situ� dans l'intervalle [0.0 , 1.0].
 * Si ce n'est pas le cas, il est important de les recadrer les coordonn�es uv � une valeur dans l'intervalle [0,1].
 * <p>Pour ce faire, il y a deux algorithmes possibles :
 * <ul>Repeat : Effectue une r�p�tition de la coordonn�e de droite � gauche et de bas � haut (si d�passement positif).</ul>
 * <ul> Clamp : Coupe la coordonn�e apr�s d�passement (u > 1 ==> u = 1, u < 0 ==> u = 0).</ul></p> 
 *  
 * @author Simon Vezina
 * @since 2015-09-15
 * @version 2016-03-27
 */
public class SVectorUV implements SVector {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante 'DEFAULT_COMPONENT' correspond � la coordonn�e par d�faut d'une composante u et v qui est �gale � {@value}.
   */
  private static final double DEFAULT_COMPONENT = 0.0; 
  
  /**
   * La constante 'MIN_VALUE' correspond � la coordonn�e minimale d'une composante u et v qui est �gale � {@value}.
   * <p>Cependant, une coordonn�e uv peut �tre quand m�me inf�rieure � {@value}, mais devra �tre reconvertie � une valeur sup�rieure � {@value}
   * avant d'�tre utilis�e pour chercher une information dans une texture. Si tel est le cas, le SVectorUV sera consid�r� <b>out_of_bound</b>.</p>
   */
  private static final double MIN_VALUE = 0.0; // coordonn�e minimale
  
  /**
   * La constante 'MAX_VALUE' correspond � la coordonn�e maximale d'une composante u et v qui est �gale � {@value}.
   * <p>Cependant, une coordonn�e uv peut �tre quand m�me sup�rieure � {@value}, mais devra �tre reconvertie � une valeur inf�rieure � {@value}
   * avant d'�tre utilis�e pour chercher une information dans une texture. Si tel est le cas, le SVectorUV sera consid�r� <b>out_of_bound</b>.</p>
   */
  private static final double MAX_VALUE = 1.0; // coordonn�e maximale
  
  /**
   * La constante 'REPEAT' d�finit le choix de recadrer une coordonn�e uv � l'ext�rieur de l'intervalle [0,1] � l'aide d'une r�p�tition des coordonn�es uv.
   * <p>Par exemple : 
   * <ul>si u = 1.23, alors la coordonn�e recadr�e par r�p�tition donnera u = 0.23.</ul>
   * <ul>si u = -2.8, alors la coordonn�e recadr�e par r�p�tition donnera u = 0.2.</ul></p>
   */
  public static final int REPEAT = 0;
  
  /**
   * La constante 'CLAMP' d�finit le choix de recadrer une coordonn�e uv � l'ext�rieur de l'intervalle [0,1] � l'aide d'un arr�t des coordonn�es uv.
   * <p>Par exemple : 
   * <ul>si u = 1.23, alors la coordonn�e recadr�e par r�p�tition donnera u = 1.0</ul>
   * <ul>si u = -2.8, alors la coordonn�e recadr�e par r�p�tition donnera u = 0.0.</ul></p>
   */
  public static final int CLAMP = 1;
  
  //--------------
  // VARIABLES  //
  //--------------
  
  /**
   * La variable 'u' correspond � la composante u du vecteur uv. Cette coordonn�e est selon l'axe x (largeur, width) d'une texture o� le (0,0) est situ� dans le coin sup�rieur gauche.
   */
  private final double u;
  
  /**
   * La variable 'v' correspond � la composante v du vecteur uv. Cette coordonn�e est selon l'axe y (hauteur, height) d'une texture o� le (0,0) est situ� dans le coin sup�rieur gauche.
   */
  private final double v; 

  /**
   * La variable 'is_out_of_bound' d�termine si l'une des composantes uv du vecteur est � l'ext�rieur de l'intervalle [0,1]. 
   * Si tel est le cas, ces coordonn�es devront �tre recadr�es.  
   */
  private final boolean is_out_of_bound;
  
  //-----------------------------
  // CONSTRUCTEURS ET M�THODES //
  //-----------------------------
  
  /**
   * Constructeur repr�sentant une coordonn�e uv � l'origine.
   */
  public SVectorUV()
  {
    this(DEFAULT_COMPONENT, DEFAULT_COMPONENT);
  }

  /**
   * Constructeur d'une coordonn�e uv quelconque.
   * 
   * @param u - La composante u de la coordonn�e uv.
   * @param v - La composante v de la coordonn�e uv.
   */
  public SVectorUV(double u, double v) throws SConstructorException
  {
    if (u < MIN_VALUE || u > MAX_VALUE || v < MIN_VALUE || v > MAX_VALUE)
      is_out_of_bound = true;
    else
      is_out_of_bound = false;
    
    this.u = u;
    this.v = v;
  }

  /**
   * Constructeur d'un vecteur uv en utilisant un string d�crivant les param�tres u et v du vecteur uv. 
   * Une lecture autoris�e peut prendre la forme suivante : "double u" "double v" (incluant la notation avec , ( ) [ ] comme d�limieur dans l'expression du string comme par exemple (2.3, 4.3) ).
   * 
   * @param string - Le string de l'expression du vecteur en param�tres u et v.
   * @throws SReadingException Si le format de la lecture n'est pas ad�quat.
   */
  public SVectorUV(String string)throws SReadingException
  {
    double[] tab = read(string);
    
    //V�rification du respect de la contrainte 0 <= u,v <= 1
    if(tab[0] < MIN_VALUE || tab[0] > MAX_VALUE || tab[1] < MIN_VALUE || tab[1] > MAX_VALUE)
      is_out_of_bound = true;
    else
      is_out_of_bound = false;
    
    u = tab[0];
    v = tab[1];
  }
  
  /**
   * M�thode pour d�terminer si la coordonn�e uv poss�de une composante dont la valeur est � l'ext�rieur de l'intervalle [0,1].
   * 
   * @return <b>true</b> s'il y a une composante � l'ext�rieur de l'intervalle et <b>false</b> sinon.
   */
  public boolean isOutOfBound()
  {
    return is_out_of_bound;
  }
  
  /**
   * M�thode pour obtenir la composante u de la coordonn�e uv.
   * 
   * @return La composante u de la coordonn�e uv.
   */
  public double getU()
  {
    return u;
  }

  /**
   * M�thode pour obtenir la composante v de la coordonn�e uv.
   * 
   * @return La composante v de la coordonn�e uv.
   */
  public double getV()
  {
    return v;
  }

  /**
   * M�thode qui permet d'obtenir un vecteur uv cadr� dans l'intervalle [0,1] � l'aide de l'algoritme de r�p�tition (REPEAT).
   *
   * @return Le vecteur uv recadr� (si n�cessaire) par l'algorithme de r�p�tition.
   */
  public SVectorUV getInBound()
  {
    return getInBound(REPEAT);
  }
  
  
  /**
   * M�thode qui permet d'obtenir un vecteur uv cadr� dans l'intervalle [0,1] � l'aide d'un algoritme particulier.
   * 
   * @param crop_code - Le code de l'algorithme
   * @return Le vecteur uv recadr� (si n�cessaire). 
   * @throws SRuntimeException Si le code de recadrement n'est pas reconnu par le syst�me.
   */
  public SVectorUV getInBound(int crop_code) throws SRuntimeException
  {
    if(!is_out_of_bound)
      return this;
    else
      switch(crop_code)
      {
        case REPEAT : return cropRepeat(); 
        
        case CLAMP : return cropClamp();
        
        default : throw new SRuntimeException("Erreur SVectorUV 001 : Le code de recadrement '" + crop_code + "' n'est pas reconnu par le syst�me.");
      }
  }
  
  /**
   * M�thode pour recadrer une coordonn�e uv par l'algoritme de r�p�tition (REPEAT).
   * 
   * @return Le vecteur uv apr�s recadrement par coupure (REPEAT).
   */
  private SVectorUV cropRepeat()
  {
    //la coordonn�e uv sorte de l'intervale [0,1] positivement ou n�gativement. 
    //Nous allons rectifier la situation ... par r�p�tition
    //Une coordonn�e sup�rieur � 1 revient dans la texture par r�p�tition par 0
    //Une coordonn�e int�rieur � 0 revient dans la texture par r�p�tition par 1 
    double repeat_u = u;
    if(repeat_u > 0)
      while(repeat_u > 1.0)
        repeat_u -= 1.0;
    else
      while(repeat_u < 0)
        repeat_u += 1.0;
    
    double repeat_v = v;
    if(repeat_v > 0)
      while(repeat_v > 1.0)
        repeat_v -= 1.0;
    else
      while(repeat_v < 0)
        repeat_v += 1.0;
    
    return new SVectorUV(repeat_u, repeat_v);
  }
  
  /**
   * M�thode pour recadrer une coordonn�e uv par l'algoritme de coupure (CLAMP).
   * 
   * @return Le vecteur uv apr�s recadrement par coupure (CLAMP).
   */
  private SVectorUV cropClamp()
  {
    double clamp_u = u;
    
    if(clamp_u < 0)
      clamp_u = 0.0;
    else
      if(clamp_u > 1)
        clamp_u = 1.0;
    
    double clamp_v = v;
    
    if(clamp_v < 0)
      clamp_v = 0.0;
    else
      if(clamp_v > 1)
        clamp_v = 1.0;
    
    return new SVectorUV(clamp_u, clamp_v);
  }
  
  @Override
  public SVector add(SVector v)
  {
    return add((SVectorUV) v);
  }
  
  /**
   * M�thode pour effectuer l'addition avec une coordonn�e uv.
   * 
   * @param uv - La coordonn�e uv � additionner.
   * @return La coordonn�e uv r�sultant de l'addition.
   */
  public SVectorUV add(SVectorUV uv)
  {
    return new SVectorUV(u + uv.u, v + uv.v);
  }
  
  /**
   * M�thode pour effectuer la multiplication par un scalaire d'une constante avec la coordonn�e uv lan�ant l'appel de la m�thode.
   * 
   * @param cst La constante � multiplier.
   * @return La coordonn�e uv r�sultant de la multiplication par un scalaire.
   */
  public SVectorUV multiply(double cst)
  {
    return new SVectorUV(u*cst, v*cst);
  }

  @Override
  public double dot(SVector vector)
  {
    return dot((SVectorUV)vector);
  }
  
  /**
   * M�thode pour effectuer le <b>produit scalaire</b> entre deux vecteurs.
   * 
   * @param vector Le vecteur � mettre en produit scalaire.
   * @return Le produit scalaire entre les deux vecteurs.
   */
  public double dot(SVectorUV vector)
  {
    return (u*vector.u + v*vector.v);
  }
  
  @Override
  public String toString()
  {
    return "[" + u + ", " + v + "]";    
  }

  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(toString());
  }
  
  /**
   * M�thode utilisant un string comme param�tre pour d�finir les composantes u et v vecteur uv. 
   * Une lecture autoris�e peut prendre la forme suivante : "double u" "double v" 
   *  (en incluant la notation avec , ( ) [ ] < > comme d�limieur dans l'expression du string comme par exemple (2.3, 4.3) ).
   *  
   * @param string - Le string de l'expression du vecteur en param�tres u et v.
   * @return un tableau de deux �l�ments tel que u = [0] et v = [1]. 
   * @throws SReadingException Si le format de lecture n'est pas ad�quat.
   */
  private double[] read(String string)throws SReadingException
  {
    StringTokenizer tokens = new StringTokenizer(string, SStringUtil.REMOVE_CARACTER_TOKENIZER);  
    
    if(tokens.countTokens() != 2)
      throw new SReadingException("Erreur SVectorUV 003 : L'expression '" + string + "' ne contient pas exactement 2 param�tres pour les composantes uv du vecteur uv.");
    else
    {
      String s = "";          //String � convertir en double pour les composantes u et v.
      String comp = "";       //Nom de la composante en lecture utilis�e pour l'envoie d'une Exception s'il y a lieu.
      
      try
      {
        double[] tab = new double[2]; //Tableau des 2 composantes
        
        comp = "u";
        s = tokens.nextToken();
        tab[0] = Double.valueOf(s);
        
        comp = "v";
        s = tokens.nextToken();
        tab[1] = Double.valueOf(s);
        
        return tab;
        
      }catch(NumberFormatException e){ 
        throw new SReadingException("Erreur SVectorUV 004 : L'expression '" + s +"' n'est pas valide pour d�finir la composante '" + comp + "' du vecteur en cours.");
      }
    } 
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (is_out_of_bound ? 1231 : 1237);
    long temp;
    temp = Double.doubleToLongBits(u);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(v);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof SVectorUV)) {
      return false;
    }
    SVectorUV other = (SVectorUV) obj;
    
    if(!SMath.nearlyEquals(u, other.u))
      return false;

    if(!SMath.nearlyEquals(v, other.v))
      return false;
    
    return true;
  }

}// fin de la classe SVectorUV
