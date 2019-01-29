package sim.graphics.light;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.graphics.SColor;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SStringUtil;

/**
 * La classe <b>SAmbientLight</b> repr�sente une source de lumi�re ambiante. 
 * Une source ambiante simule les multilples r�flexions interne entre les primitives.
 * Elle est traditionnellement tr�s faible dans une sc�ne. 
 * Dans cette classe, la position de la source aura un effet uniquement si l'on utilise les facteurs d'att�nuations.
 * 
 * @author Simon V�zina
 * @since 2015-01-09
 * @version 2016-02-17
 */
public class SAmbientLight extends SAbstractLight {

	/**
	 * Constructeur d'une source de lumi�re ambiante blanche � faible intensit� situ�e � l'origine sans facteur d'att�nuation. 
	 */
	public SAmbientLight()
	{
		this(DEFAULT_LIGHT_COLOR);
	}

	/**
	 * Constructeur d'une source de lumi�re ambiante sans facteur d'att�nuation.
	 * 
	 * @param color - La couleur de la source de lumi�re.
	 * @param position - La position de la source de lumi�re.
	 * @throws SConstructorException Si une erreur est survenue lors de la construction.
	 */
	public SAmbientLight(SColor color) throws SConstructorException
	{
		super(color);
		
		try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SAmbientLight 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
	}
	
	/**
	 * Constructeur d'une source de lumi�re ambiante � partir d'information lue dans un fichier de format txt.
	 * @param sbr - Le BufferedReader cherchant l'information dans le fichier txt.
	 * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
	 * @throws SConstructorException Si une erreur est survenue � la construction. 
	 * @see SBufferedReader
	 */
	public SAmbientLight(SBufferedReader sbr) throws IOException, SConstructorException
	{
		this();		
		
		try{
		  read(sbr);
		}catch(SInitializationException e){
      throw new SConstructorException("Erreur SAmbientLight 002 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
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
	public void write(BufferedWriter bw)throws IOException
	{
		bw.write(SKeyWordDecoder.KW_AMBIENT_LIGHT);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		//�crire les param�tres de la classe SAmbientLight et ceux qu'il h�rite
		writeAmbientLightParameter(bw);
		
		bw.write(SKeyWordDecoder.KW_END);
		bw.write(SStringUtil.END_LINE_CARACTER);
		bw.write(SStringUtil.END_LINE_CARACTER);
	}
	
	/**
	 * M�thode pour �crire les param�tres associ�s � la classe SAmbientLight et ceux qu'il a h�rit�.
	 * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
	 * @throws IOException S'il y a une erreur d'�criture.
	 * @see IOException
	 */
	protected void writeAmbientLightParameter(BufferedWriter bw)throws IOException 
	{
		//�crire les param�tres h�rit�s de la classe SAbstractLight
		writeSAbstractLightParameter(bw);
		
		// Puisque cette classe n'a pas de param�tre, il n'y a rien a ajouter ...
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
    return SKeyWordDecoder.KW_AMBIENT_LIGHT;
  }

  @Override
  public int getCodeName()
  {
    return AMBIENT_LIGHT_CODE;
  }
	
}//fin SAmbientLight
