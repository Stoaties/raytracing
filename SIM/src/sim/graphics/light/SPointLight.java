/**
 * 
 */
package sim.graphics.light;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.graphics.SColor;
import sim.math.SVector3d;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SStringUtil;

/**
 * La classe <b>SPointLight</b> repr�sente une source de lumi�re ponctuelle.
 * 
 * @author Simon V�zina
 * @since 2015-01-21
 * @version 2016-02-28
 */
public class SPointLight extends SAbstractAttenuatedLight {

  //----------------
  // CONSTRUCTEUR //
  //----------------
  
	/**
	 * Constructeur d'une source de lumi�re ponctuelle blanche situ�e � l'origine sans facteur d'att�nuation.
	 */
	public SPointLight()
	{
		this(DEFAULT_LIGHT_COLOR, DEFAULT_POSITION);
	}

	/**
	 * Constructeur d'une source ponctuelle de lumi�re sans att�nuation.
	 * 
	 * @param color La couleur de la source de lumi�re.
	 * @param position La position de la source de lumi�re.
	 */
	public SPointLight(SColor color, SVector3d position)
	{
		this(color, position, DEFAULT_AMPLIFICATION, DEFAULT_CONSTANT_ATTENUATOR, DEFAULT_LINEAR_ATTENUATOR, DEFAULT_QUADRATIC_ATTENUATOR);
	}

	/**
	 * Constructeur d'une source de lumi�re blanche avec facteur d'att�nuation.
	 * 
	 * @param color La couleur de la source de lumi�re.
	 * @param position La positon de la source de lumi�re.
	 * @param amp Le facteur d'amplification de la source de lumi�re.
	 * @param cst_att La constante d'att�nuation � taux constant.
	 * @param lin_att La constante d'att�nuation � taux lin�aire.
	 * @param quad_att La constante d'att�nuation � taux quadratique.
	 * @throws SConstructorException Si une erreur est survenue lors de la construction.
	 */
	public SPointLight(SColor color, SVector3d position, double amp, double cst_att, double lin_att, double quad_att) throws SConstructorException
	{
		super(color, position, amp, cst_att, lin_att, quad_att);
		
		try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SPointLight 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
	}

	/**
	 * Constructeur d'une source de lumi�re ponctuelle � partir d'information lue dans un fichier de format txt.
	 * @param sbr - Le BufferedReader cherchant l'information dans le fichier txt.
	 * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
	 * @throws SException Si des facteurs d'att�nuation sont initialis�s avec des valeurs erron�es.
	 * @throws SConstructorException Si une erreur est survenue � la construction. 
	 * @see SBufferedReader
	 */
	public SPointLight(SBufferedReader sbr)throws IOException, SConstructorException
	{
		this();		
		
		try{
		  read(sbr);
		}catch(SInitializationException e){
      throw new SConstructorException("Erreur SPointLight 002 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
	}
		
	@Override
	public void write(BufferedWriter bw) throws IOException
	{
		bw.write(SKeyWordDecoder.KW_POINT_LIGHT);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		writeSPointLightParameter(bw);
		
		bw.write(SKeyWordDecoder.KW_END);
		bw.write(SStringUtil.END_LINE_CARACTER);
		bw.write(SStringUtil.END_LINE_CARACTER);
	}

	/**
	 * M�thode pour �crire les param�tres associ�s � la classe SPointLight et ceux qu'il a h�rit�.
	 * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
	 * @throws IOException S'il y a une erreur d'�criture.
	 * @see IOException
	 */
	protected void writeSPointLightParameter(BufferedWriter bw)throws IOException
	{
		//�crire les param�tres h�rit�s de la classe SAbstractLight
	  writeSAbstractAttenuatedLightParameter(bw);
	}
	
	/**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
	{
	  
	}

	@Override
  protected void readingInitialization() throws SInitializationException
  {
    super.readingInitialization();
    
    initialize();
  }
	
	@Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_POINT_LIGHT;
  }

  @Override
  public int getCodeName()
  {
    return POINT_LIGHT_CODE;
  }
	
}//fin de la classe SPointLight
