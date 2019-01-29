/**
 * 
 */
package sim.graphics.light;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.graphics.SColor;
import sim.math.SImpossibleNormalizationException;
import sim.math.SVector3d;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SDirectionalLight</b> repr�sente une source de lumi�re directionnel. 
 * Contrairement � une source de lumi�re ambiante, cette source de lumi�re a une orientation ce qui a un impact sur la r�flexion diffuse.
 * Ce type de lumi�re correspond tr�s bien au <b>comportement du Soleil</b> dont l'orientation d�pend de la position du Soleil dans le ciel.
 * Dans cette classe, la position de la source aura un effet uniquement si l'on utilise les facteurs d'att�nuations.
 * 
 * @author Simon V�zina
 * @since 2015-01-15
 * @version 2016-02-17
 */
public class SDirectionalLight extends SAbstractLight{

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_ORIENTATION };
  
	private static final SVector3d DEFAULT_DIRECTIONAL_LIGHT_ORIENTATION = new SVector3d(0.0, 0.0, -1.0);
	
	/**
	 * La variable <b>orientation</b> correspond � l'orientation de la source de lumi�re.
	 */
	private SVector3d orientation;		
	
	/**
	 * Constructeur d'une source de lumi�re directionnelle blanche positionn�e � l'origine et orient�e selon l'axe -z.
	 * Si dans une sc�ne, l'axe z repr�sente le haut, cette source de lumi�re va �clairer vers le bas comme le ferait le Soleil � midi.
	 */
	public SDirectionalLight()
	{
		this(DEFAULT_LIGHT_COLOR, DEFAULT_DIRECTIONAL_LIGHT_ORIENTATION);
	}
	
	/**
	 * Constructeur d'une source de lumi�re directionnelle. 
	 * 
	 * @param color - La couleur de la source de lumi�re.
	 * @param orientation - L'orientation de la source de lumi�re.
	 * @throws SConstructorException Si une erreur est survenue � la construction. 
	 */
	public SDirectionalLight(SColor color, SVector3d orientation) throws SConstructorException
	{
		super(color);
		
		this.orientation = orientation;
		
		try{
		  initialize();
		}catch(SInitializationException e){
      throw new SConstructorException("Erreur SDirectionalLight 001 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    } 
	}

	/**
	 * Constructeur d'une source de lumi�re directionnelle � partir d'information lue dans un fichier de format txt.
	 * 
	 * @param sbr - Le BufferedReader cherchant l'information dans le fichier txt.
	 * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
	 * @throws SConstructorException Si une erreur est survenue � la construction. 
	 * @see SBufferedReader
	 */
	public SDirectionalLight(SBufferedReader sbr) throws IOException, SConstructorException
	{
		this();		
		
		try{
		  read(sbr);
		}catch(SInitializationException e){
      throw new SConstructorException("Erreur SDirectionalLight 002 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
	}
	
	/**
	 * M�thode pour obtenir l'orientation de la source de lumi�re directionnelle.
	 * 
	 * @return L'orientation de la source de lumi�re.
	 */
	public SVector3d getOrientation()
	{ 
	  return orientation;
	}
	
	@Override
	protected boolean read(SBufferedReader sbr, int code, String remaining_line)throws SReadingException 
	{
		//Analyse du code du mot cl� dans la classe h�rit� SAbstractLight
		if(super.read(sbr, code, remaining_line))
			return true;
		else
			switch(code)
			{
				case SKeyWordDecoder.CODE_ORIENTATION :	orientation = new SVector3d(remaining_line); return true;
				
				default : return false;
			}
	}
	
	@Override
	public void write(BufferedWriter bw) throws IOException 
	{
		bw.write(SKeyWordDecoder.KW_DIRECTIONAL_LIGHT);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		//�crire les param�tres de la classe abstraite SAbstractLight
		writeSDirectionalLightParameter(bw);
		
		bw.write(SKeyWordDecoder.KW_END);
		bw.write(SStringUtil.END_LINE_CARACTER);
		bw.write(SStringUtil.END_LINE_CARACTER);	
	}
	
	/**
	 * M�thode pour �crire les param�tres associ�s � la classe SDirectionalLight et ceux qu'il a h�rit�.
	 * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
	 * @throws IOException S'il y a une erreur d'�criture.
	 * @see IOException
	 */
	protected void writeSDirectionalLightParameter(BufferedWriter bw)throws IOException 
	{
		//�crire les param�tres h�rit�s de la classe SAbstractLight
		writeSAbstractLightParameter(bw);
		
		//�crire les param�tres de la classe SDirectionalLight
		bw.write(SKeyWordDecoder.KW_ORIENTATION);
		bw.write("\t\t");
		orientation.write(bw);
		bw.write(SStringUtil.END_LINE_CARACTER);
	}
	
	/**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
	{
	  try{
	    orientation = orientation.normalize();
	  }catch(SImpossibleNormalizationException e){
	    throw new SInitializationException("Erreur SDirectionalLight 003 : Une erreur est survenue lors de l'initialisation." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());
	  }
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
    return SKeyWordDecoder.KW_DIRECTIONAL_LIGHT;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }

  @Override
  public int getCodeName()
  {
    return DIRECTIONAL_LIGHT_CODE;
  }
  
}//fin classe SDirectionalLight
