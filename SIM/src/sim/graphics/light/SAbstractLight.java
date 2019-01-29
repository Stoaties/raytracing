/**
 * 
 */
package sim.graphics.light;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.graphics.SColor;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * Classe abstraite <b>SAbstractLight</b> représentante une source de lumière avec attribue de base comme la couleur et les facteurs d'atténuation.
 * 
 * @author Simon Vézina
 * @since 2015-01-09
 * @version 2016-03-03
 */
public abstract class SAbstractLight extends SAbstractReadableWriteable implements SLight {

  //--------------
  // CONSTANTES //
  //--------------
  
  //Numéro d'identification des types de source de lumière 
  public static final int AMBIENT_LIGHT_CODE = 1;
  public static final int POINT_LIGHT_CODE = 2;
  public static final int DIRECTIONAL_LIGHT_CODE = 3;
  public static final int LINEAR_APERTURE_LIGHT_CODE = 4;
  public static final int RECTANGULAR_APERTURE_LIGHT_CODE = 5;
  public static final int ELLIPTICAL_APERTURE_LIGHT_CODE = 6;
  public static final int APERTURE_MASK_LIGHT_CODE = 7;
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond à un tableau contenant l'ensemble des mots clés 
   * à utiliser reconnus lors de la définition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_COLOR };
    
  /**
   * La constante <b>DEFAULT_LIGHT_COLOR</b> représente la couleur de la source de lumière par défaut.
   */
	protected static final SColor DEFAULT_LIGHT_COLOR = new SColor(1.0, 1.0, 1.0);		
	
	//-------------
	// VARIABLES //
	//-------------
	
	/**
	 * La variable <b>color</b> représente la couleur de la source de lumière. 
	 */
	protected SColor color;					
		
	//----------------
	// CONSTRUCTEUR //
	//----------------
	
	/**
	 * Constructeur d'une source de lumière blanche abstraite.
	 */
	public SAbstractLight()
	{
		this(DEFAULT_LIGHT_COLOR);
	}
	
	/**
	 * Constructeur d'une source de lumière abstraite.
	 * @param color - La couleur de la source de lumière.
	 * @throws SConstructorException Si une erreur est survenue lors de la construction. 
	 */
	public SAbstractLight(SColor color) throws SConstructorException
	{
		this.color = color;
		
		try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SAbstractLight 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
	}
	
	@Override
	public SColor getColor()
	{ 
	  return color;
	}
	
	@Override
	protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException 
	{
		switch(code)
		{
			case SKeyWordDecoder.CODE_COLOR : color = new SColor(remaining_line); return true; 
						
			default : return false;
		}	
	}
	
	/**
	 * Méthode pour écrire les paramètres associés à la classe SAbstractLight.
	 * @param bw - Le BufferedWriter écrivant l'information dans un fichier txt.
	 * @throws IOException S'il y a une erreur d'écriture.
	 * @see IOException
	 */
	protected void writeSAbstractLightParameter(BufferedWriter bw) throws IOException
	{
		bw.write(SKeyWordDecoder.KW_COLOR);
		bw.write("\t\t\t");
		color.write(bw);
		bw.write(SStringUtil.END_LINE_CARACTER);
	}
	
	/**
   * Méthode pour faire l'initialisation de l'objet après sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    
  }
  
	@Override
  protected void readingInitialization() throws SInitializationException
  {
    initialize();
  }
	
	@Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
}//fin classe abstraite SAbstractLight
