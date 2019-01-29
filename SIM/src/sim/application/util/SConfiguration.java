/**
 * 
 */
package sim.application.util;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import sim.exception.SConstructorException;
import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.graphics.SScene;
import sim.graphics.SViewport;
import sim.readwrite.SAbstractReadable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SFileSearch;
import sim.util.SInitializationException;
import sim.util.SLog;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SConfiguration</b> repr�sente la configuration d'un programme. Cette classe fait la lecture d'un fichier config.cgp et initialise des param�tres du programme.
 * 
 * @author Simon V�zina
 * @since 2015-01-04
 * @version 2016-02-29
 */
public class SConfiguration extends SAbstractReadable {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { 
    SKeyWordDecoder.KW_READ_DATA, SKeyWordDecoder.KW_WRITE_DATA, SKeyWordDecoder.KW_LOG_FILE_NAME,
    SKeyWordDecoder.KW_LOG_CONSOLE, SKeyWordDecoder.KW_LOG_FILE, SKeyWordDecoder.KW_VIEWPORT_IMAGE_COUNT, 
    SKeyWordDecoder.KW_APPLICATION 
  };
  
  /**
   * La constante <b>APPLICATION_TYPE</b> correspond � un tablea de mots cl� repr�sentant le nom des applications reconnues par la classe <b>SConfiguration</b>.
   */
  public static final String[] APPLICATION_TYPE = { "none", "info", "console", "frame", "comparator"};
  
  /**
   * La constante <b>DEFAULT_CONFIG_FILE_NAME</b> correspond au nom du fichier de configuration par d�faut �tant �gal � {@value}.
   */
	private static final String DEFAULT_CONFIG_FILE_NAME = "configuration.cfg";	
		
	/**
   * La constante <b>DEFAULT_WRITE_SCENE</b> correspond au nom du fichier de sc�ne qui sera en �criture par d�faut �tant �gal � {@value}.
   */
  private static final String DEFAULT_WRITE_SCENE = "out.txt";
  
	/**
	 * La constante <b>DEFAULT_READ_DATA_FILE_NAME</b> correspond au nom du fichier en lecture pour les donn�es de l'application par la configuration par d�faut �tant �gal � {@value}.
	 */
	private static final String DEFAULT_READ_DATA_FILE_NAME = "read_data_file_name.txt";	  
	
	/**
	 * La constante <b>DEFAULT_WRITE_DATA_FILE_NAME</b> correspond au nom du ficher en �criture des donn�es de l'application par la configuration par d�faut �tant �gal � {@value}.
	 */
	private static final String DEFAULT_WRITE_DATA_FILE_NAME = "write_data_file_name.txt";	
	
	//-------------
	// VARIABLES //
	//-------------
	
	/**
	 * La variable <b>config_file_name</b> correspond au nom du fichier de configuration.
	 */
	private final String config_file_name;		
	
	/**
	 * La variable <b>read_data_file_name<b> correspond au nom du fichier qui sera lu par la configuration pour d�finir les donn�es de l'application.
	 */
	private String read_data_file_name;			
	
	/**
	 * La varialbe <b>write_data_file_name</b> correspond au nom du fichier qui sera �crit par la configuration pour sauvegarder les donn�es de l'application en format texte.
	 */
	private String write_data_file_name;		
	
	/**
	 * La variable <b>application_type</b> correspond � un code de r�f�rence d�finissant l'application qui sera lanc�e par la configuration.
	 */
	private int application_type;	         
	
	//----------------
	// CONSTRUCTEUR //
	//----------------
	
	/**
	 * Constructeur de la configuration du programme par d�faut.
	 * Le nom du fichier sera <b>"configuration.cfg"</b>.
	 * Ce fichier ne sera pas lu, car le constructeur par d�faut propose ses propres valeurs. 
   */
	public SConfiguration()
	{
	  config_file_name = DEFAULT_CONFIG_FILE_NAME;
	  read_data_file_name =  SScene.DEFAULT_SCENE_FILE_NAME;
    write_data_file_name = DEFAULT_WRITE_SCENE;
    application_type = 1;   // application de type "info"
	}
	
	/**
	 * Constructeur de la configuration du programme avec le nom de configuration.
	 * 
	 * @param file_name - Le nom du fichier de configuration.
	 * @throws FileNotFoundException Si le fichier de lecture n'a pas �t� trouv�.
   * @throws IOException Si une erreur de type I/O est survenue.
   * @throws SConstructorException Si le nom du ficher de sc�ne a �t� trouv� sous plusieurs versions.
   */
	public SConfiguration(String file_name) throws FileNotFoundException, SConstructorException, IOException
	{
		config_file_name = file_name;
		
		read_data_file_name =  DEFAULT_READ_DATA_FILE_NAME;
		write_data_file_name = DEFAULT_WRITE_DATA_FILE_NAME;
		
		application_type = 0;   //application de type "none"
		
		//Trouver le fichier � partir du r�pertoire o� l'ex�cution de l'application est r�alis�e
    SFileSearch search = new SFileSearch("", file_name);
    
    if(!search.isFileFound())
      throw new FileNotFoundException("Erreur SConfiguration 001 : Le fichier '" + file_name + "' n'est pas trouv�.");
    
    if(search.isManyFileFound())
      throw new SConstructorException("Erreur SConfiguration 002 : Le fichier '" + file_name + "' a �t� trouv� plus d'une fois dans les diff�rents sous-r�pertoires. Veuillez en garder qu'une seule version.");
    
    //Lecture de la sc�ne � partir d'un fichier
    FileReader fr = new FileReader(search.getFileFoundList().get(0));
    SBufferedReader sbr = new SBufferedReader(fr);
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      //Les trois points sont colin�aire ce qui ne permet pas de d�finir une normale � la surface au triangle. 
      throw new SConstructorException("Erreur SConfiguration 003 : Une erreur est survenue lors de l'initialisation." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);  
    }
    
    sbr.close();
    fr.close();
	}
	
	//------------
	// M�THODES //
	//------------
	
	/**
	 * M�thode pour obtenir le nom du fichier en lecture du data.
	 * 
	 * @return Le nom du fichier en lecture du data.
	 */
	public String getReadDataFileName()
	{ 
	  return read_data_file_name;
	}
		
	/**
	 * M�thode pour obtenir le nom du fichier en �criture du data.
	 * 
	 * @return Le nom du fichier.
	 */
	public String getWriteDataFileName()
	{ 
	  return write_data_file_name;
	}
	
	/**
	 * M�thode pour obtenir le type d'application devant �tre construite selon la configuration.
	 * 
	 * @return Le code associ� au type de configuration.
	 */
	public int getApplicationType()
	{
	  return application_type;
	}
	
	@Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException, IOException
  {
    switch(code)
    {
      case SKeyWordDecoder.CODE_READ_SCENE : 
      case SKeyWordDecoder.CODE_READ_DATA :  read_data_file_name = readStringNotEmpty(remaining_line, SKeyWordDecoder.KW_READ_DATA); return true;           
                              
      case SKeyWordDecoder.CODE_WRITE_SCENE : 
      case SKeyWordDecoder.CODE_WRITE_DATA : write_data_file_name = readStringNotEmpty(remaining_line, SKeyWordDecoder.KW_WRITE_DATA); return true;        
      
      case SKeyWordDecoder.CODE_LOG_FILE_NAME : try{
                                                  SLog.setLogFileName(readStringNotEmpty(remaining_line, SKeyWordDecoder.KW_LOG_FILE_NAME)); 
                                                }catch(SRuntimeException e){
                                                  throw new SReadingException("Erreur SConfiguration 004 : Le nom du fichier de log n'est pas ad�quat." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
                                                }
                                                return true;   
        
      case SKeyWordDecoder.CODE_LOG_CONSOLE : SLog.setConsoleLog(readTrueFalseExpressionOrInt(remaining_line, SKeyWordDecoder.KW_LOG_CONSOLE)); return true;
        
      case SKeyWordDecoder.CODE_LOG_FILE : SLog.setFileLog(readTrueFalseExpressionOrInt(remaining_line, SKeyWordDecoder.KW_LOG_FILE)); return true; 
        
      case SKeyWordDecoder.CODE_VIEWPORT_IMAGE_COUNT :  try{
                                                          SViewport.setImageCounter(readIntEqualOrGreaterThanZero(remaining_line, SKeyWordDecoder.KW_VIEWPORT_IMAGE_COUNT));
                                                        }catch(SRuntimeException e){
                                                          throw new SReadingException("Erreur SConfiguration 005 : Le nombre en affectation � la num�rotation des images du viewport n'est pas ad�quat." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);   
                                                        }
                                                        return true;
      
      case SKeyWordDecoder.CODE_APPLICATION : application_type = readIntOrExpression(remaining_line, SKeyWordDecoder.KW_APPLICATION, APPLICATION_TYPE); return true;
      
      default : return false;
    }
  }
  
	/**
	 * M�thode pour �crire le fichier de configuration.
	 */
	public void writeFile()
	{
		try{
		  
			FileWriter fw = new FileWriter(config_file_name);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(SKeyWordDecoder.KW_APPLICATION);
			bw.write("\t");
			bw.write(APPLICATION_TYPE[application_type]);
			bw.write(SStringUtil.END_LINE_CARACTER);
			
			bw.write(SKeyWordDecoder.KW_READ_DATA);
			bw.write("\t\t");
			bw.write(read_data_file_name);
			bw.write(SStringUtil.END_LINE_CARACTER);
			
			bw.write(SKeyWordDecoder.KW_WRITE_DATA);
			bw.write("\t\t");
			bw.write(write_data_file_name);
			bw.write(SStringUtil.END_LINE_CARACTER);
			
			//�crire les propri�t�s static de la classe SLog
			SLog.staticWrite(bw);
			
			//�crire les propri�t�s static de la classe SViewport
			SViewport.staticWrite(bw);
			
			bw.close();     //  fermer celui-ci en premier, sinon, ERROR !!!
			fw.close();
			
		}catch(IOException ioe){
			ioe.printStackTrace();
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
  protected void readingInitialization() throws SInitializationException
  {
    initialize();
  }
	
	@Override
  public String getReadableName()
  {
	  throw new SNoImplementationException("Erreur SConfiguration 006 : Cette m�thode ne retourne pas de mot cl�, car cet objet ne peut pas �tre construit par l'appel d'un mot cl�.");
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
}//fin de la classe SConfiguration
