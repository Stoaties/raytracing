package sim.util;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SRuntimeException;
import sim.readwrite.SAbstractReadable;
import sim.readwrite.SKeyWordDecoder;

/**
 * La classe <b>SLog</b> permet d'afficher des messages vers la console ou vers un fichier .txt afin de garder une trace de l'ex�cution du programme.
 * 
 * @author Simon Vezina
 * @since 2014-12-18
 * @version 2016-01-14
 */
public class SLog {
	
  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>LOG_EXTENSION</b> correspond � l'extension du fichier de log par d�faut �tant {@value}.
   */
  private static final String LOG_EXTENSION = "txt";             
  
  /**
	 * La constante <b>DEFAULT_LOG_FILE_NAME</b> correspond au nom du fichier de log par d�faut �tant {@value}.
	 */
  private static final String DEFAULT_LOG_FILE_NAME = "log" + "." + LOG_EXTENSION; 
	
	//-------------
	// VARIABLES //
	//-------------
	
	/**
	 * La variable <b>log_file_name</b> correspond au nom du fichier avec extension utilis� pour �crire le log.
	 */
	private static String log_file_name = DEFAULT_LOG_FILE_NAME + "." + LOG_EXTENSION;   
	
	/**
	 * La variable <b>isConsoleLogOn</b> d�termine si les messages de type log seront redirig�s vers la console.
	 */
	private static boolean isConsoleLogOn = true;		
	
	/**
	 * La variable <b>isFileLogOn<b> d�termine si si les messages de type log seront redirig�s vers un fichier texte en �criture.
	 */
	private static boolean isFileLogOn = true;						        
	
	/**
	 * La variable <b>fw</b> correspond � une r�f�rence vers un fichier texte en �criture.
	 */
	private static FileWriter fw;									                 
	
	/**
	 * La variable <b>bw</b> correspond � une r�f�rence vers un buffer en �criture dans un fichier texte.
	 */
	private static BufferedWriter bw;		
	
	/**
	 * La variable <b>isLogFileReady</b> d�terminer s'il y a eu cr�ation d'un fichier log pour l'�criture.
	 */
	private static boolean isLogFileReady = false;					       
	
	//------------
	// M�THODES //
	//------------
	
	/**
	 * M�thode pour obtenir le nom du fichier d'�criture du log.
	 * 
	 * @return Le nom du fichier log.
	 */
	public static String getLogFileName()
	{ 
	  return log_file_name;
	}
	
	/**
	 * M�thode pour d�terminer si la console sera utilis�e comme m�dia d'�criture du log.
	 * 
	 * @param value D�termine si la console sera utilis�e comme log.
	 */
	public static void setConsoleLog(boolean value)
	{ 
	  isConsoleLogOn = value;
	}
		
	/**
	 * M�thode pour d�terminer si un fichier de type .txt sera utilis� comme m�dia d'�criture du log.
	 * 
	 * @param value D�termine si un fichier sera utilis� comme log.
	 */
	public static void setFileLog(boolean value)
	{ 
	  isFileLogOn = value;
	}
	
	/**
	 * M�thode pour initialiser le nom du fichier log.
	 * 
	 * @param file_name Nom du fichier de sortir en format texte (de type .txt).
	 * @throws SRuntimeException Si le nom de l'extension n'est pas de type appropri�.
	 * @throws IOException Si une erreur I/O s'est produit.
	 */
	public static void setLogFileName(String file_name) throws SRuntimeException, IOException
	{
		
	  
	  //------------------------------------------------------------------------------------------------------------
	  //IL Y A UN PETIT PROBL�ME ICI
	  
	  //Si je fais �a de cette fa�on, le choix de ne pas imprimer dans le fichier ne marchera pas
	  //car je vais quand m�me en cr�er un ce que je ne veux pas si on choisit l'option de ne pas en �crire !!!!
	  //------------------------------------------------------------------------------------------------------------
	  
	  //TODO
	  
	  
	  //V�rification que le fichier porte un extension admissible (.txt)		
		String extension = SStringUtil.extensionFileLowerCase(file_name);
		    
		//V�rification si extension != LOG_EXTENSION 
		if(!extension.equals(LOG_EXTENSION))
			throw new SRuntimeException("Erreur SLog 001 : L'extension '" + extension + "' pour le fichier de log n'est pas de type '" + LOG_EXTENSION + "'.");
		
		//V�rifier s'il y au pr�sentement un fichier log en �criture. Si oui, le fermer pour ouvrier l'autre.
		if(isLogFileReady)
			closeLogFile();
			
		//Cr�ation du nouveau fichier d'�criture
		log_file_name = file_name;
		
		try{
			fw = new FileWriter(log_file_name);
			bw = new BufferedWriter(fw);
			isLogFileReady = true;
		}catch(IOException ioe){ 
			ioe.printStackTrace();
		}
	}
	
	/**
	 * M�thode pour fermer le fichier de log. Le fichier log sera vide si l'appel n'est pas effectu� avant la fermeture de l'application.
	 * 
	 * @throws IOException S'il y a une erreur lors de la fermeture du fichier.
	 */
	public static void closeLogFile() throws IOException
	{
		bw.close();     //  fermer celui-ci en premier, sinon, ERROR !!!
		fw.close();
		
		isLogFileReady = false;
	}
	
	/**
   * M�thode pour �crire dans le log un saut de ligne.
   */
	public static void logWriteLine()
	{
	  logWriteLine("");
	}
	
	/**
	 * M�thode pour �crire un String dans le log en incluant un saut de ligne.
	 * 
	 * @param str Le String a �crire dans le log.
	 */
	public static void logWriteLine(String str)
	{
	  logWrite(str + SStringUtil.END_LINE_CARACTER);
	}
	
	/**
   * M�thode pour �crire un String dans le log.
   * 
   * @param str Le String a �crire dans le log.
   */
  public static void logWrite(String str)
  {
    if(isConsoleLogOn)
      System.out.print(str);
    
    if(isFileLogOn)
    { 
      //V�rification qu'il y a eu initialisation du FileWriter, sinon cr�ation avec le nom par d�faut
      if(!isLogFileReady)
        try{
          setLogFileName(DEFAULT_LOG_FILE_NAME);
        }catch(IOException ioe){
          ioe.printStackTrace();
        }
      
      try{
        bw.write(str);  //�crire dans le buffer
        bw.flush();     //transf�rer le buffer dans le fichier (ne pas attendre la fermeture du fichier pour d�buter l'�criture)
      }catch(IOException ioe){ 
        ioe.printStackTrace();
      }
    }
  }
  	  
	/**
	 * M�thode pour �crire le contenu static de la classe SLog dans un fichier txt.
	 * @param bw - Le buffer pour l'�criture.
	 * @throws IOException Si une erreur de l'objet BufferedWriter est lanc�e.
	 * @see BufferedWriter
	 */
	public static void staticWrite(BufferedWriter bw)throws IOException
	{
		bw.write(SKeyWordDecoder.KW_LOG_FILE_NAME);
		bw.write("\t");
		bw.write(log_file_name);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_LOG_CONSOLE);
		bw.write("\t");
		if(isConsoleLogOn == true)
			bw.write(SAbstractReadable.TRUE_EXPRESSION[0]);
		else
			bw.write(SAbstractReadable.FALSE_EXPRESSION[0]);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_LOG_FILE);
		bw.write("\t\t");
		if(isFileLogOn == true)
			bw.write(SAbstractReadable.TRUE_EXPRESSION[0]);
		else
			bw.write(SAbstractReadable.FALSE_EXPRESSION[0]);
		bw.write(SStringUtil.END_LINE_CARACTER);
	}
	
	/**
	 * M�thode pour effectuer l'�criture d'une exception en log.
	 * 
	 * @param e L'exception � �crire.
	 */
	public static void logException(Exception e)
	{
	  logWriteLine(SStringUtil.exceptionMessage(e));
	}
	
	@Override
	public String toString()
	{
		return "log_file_name = " + log_file_name;
	}
	
}//end SLog
