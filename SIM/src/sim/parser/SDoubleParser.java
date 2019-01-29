/**
 * 
 */
package sim.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import sim.util.SBufferedReader;
import sim.util.SFileSearch;
import sim.util.SStringUtil;

/**
 * La classe <b>SDoubleParser</b> repr�sente une classe pouvant lire un fichier texte et obtenir une s�rie de nombre de type double.
 * 
 * @author Simon V�zina
 * @since 2017-05-26
 * @version 2017-05-29
 */
public class SDoubleParser {

  /**
   * La constante <b>EXTENSION</b> correspond � l'extension d'un fichier pouvant �tre lu par ce parser.
   */
  private static final String EXTENSION = "txt";
  
  /**
   * La variable <b>file_name</b> repr�sente le nom du fichier en lecture.
   */
  private final String file_name;
  
  /**
   * La variable <b>list</b> repr�sente la liste des valeurs lues.
   */
  private final List<Double> list;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un lecteur de type double.
   * 
   * @param file_name Le nom du fichier en lecture.
   * @throws SParserException S'il y a eu une erreur de lecture lors de la construction.
   */
  public SDoubleParser(String file_name) throws SParserException
  {
    this.file_name = file_name;
    list = new ArrayList<Double>();
    
    parse(file_name);
  }

  //--------------------
  // M�THODE PUBLIQUE //
  //--------------------
  
  /**
   * M�thode pour obtenir le nom du fichier en lecture.
   * 
   * @return Le nom du fichier en lecture.
   */
  public String getFileName()
  {
    return file_name;
  }
  
  /**
   * M�thode pour obtenir les valeurs lues par le lecteur.
   * 
   * @return Un tableau des valeurs lues.
   */
  public double[] getValues()
  {
    double[] values = new double[list.size()];
    
    for(int i = 0; i < list.size(); i++)
      values[i] = list.get(i).doubleValue();
    
    return values;
  }
  
  //----------------------
  // M�THODE UTILITAIRE //
  //----------------------
  
  /**
   * M�hode pour faire la lecture d'un fichier txt � partir du nom du fichier.
   * 
   * @param file_name Le nom du fichier txt � lire.
   * @throws SParserException Si le fichier n'est pas dans le bon format.
   * @throws SParserException Si le fichier n'est pas trouv�.
   * @throws SParserException Si le fichier existe sous plusieurs versions dans les diff�rents sous-r�pertoires.
   */
  private void parse(String file_name) throws SParserException
  {
    //V�rification du format du fichier
    if(!SStringUtil.extensionFileLowerCase(file_name).equals(EXTENSION))
      throw new SParserException("Erreur SRealFunctionParser 001 : Le fichier '" + file_name + "' n'est pas dans le format '" + EXTENSION + "'.");
    
    //Trouver le fichier � partir du r�pertoire o� l'ex�cution de l'application est r�alis�e
    SFileSearch search = new SFileSearch("", file_name);
    
    if(!search.isFileFound())
      throw new SParserException("Erreur SRealFunctionParser 002 : Le fichier '" + file_name + "' n'est pas trouv�.");
    
    if(search.isManyFileFound())
      throw new SParserException("Erreur SRealFunctionParser 003 : Le fichier '" + file_name + "' a �t� trouv� plus d'une fois dans les diff�rents sous-r�pertoires. Veuillez en garder qu'une seule version.");
    
    File file = new File(search.getFileFoundList().get(0)); //prendre la 1ier trouv� dans la liste
    
    try
    {
      FileReader fr = new FileReader(file);
      SBufferedReader sbr = new SBufferedReader(fr);
      parse(sbr);
      
    }catch(FileNotFoundException e){
      throw new SParserException("Erreur SModelAGPParser 004 : IMPOSSIBLE! La localisation du fichier a �t� d�j� test�.", e);     
    }catch(IOException ioe){
      throw new SParserException("Erreur SModelAGPParser 005 : Une erreur de type I/O est survenue.", ioe);      
    }
  }
  
  /**
   * M�thode pour lire le fichier � l'aide d'un SBufferedReader.
   * 
   * @param sbr Le buffer de lecture.
   * @throws IOException S'il y a eu une erreur de type I/O lors de la lecture du fichier.
   * @throws SParserException S'il y a eu une erreur menant � une incapacit� de d�finir la fonction.
   */
  private void parse(SBufferedReader sbr) throws SParserException, IOException
  { 
    boolean end_reading = false;        //condition de fin de lecture
    
    while(!end_reading)
    {   
      // Lecture de la ligne
      String line = sbr.readLine();    
      
      //Si la ligne lu est "null", la fin du fichier est atteinte
      if(line == null)
        end_reading = true;
      else
      {
        // Fragmenter la ligne lue en �liminant les caract�res d'espacement.
        StringTokenizer tokens = new StringTokenizer(line);  
        
        if(tokens.hasMoreTokens())
        {
          // Obtenir le 1ier token � analyser.
          String first_token = tokens.nextToken();      
          
          // Remplacer les ',' par des '.' car c'est le format employ� par java
          first_token = first_token.replaceAll(",",".");  
          
          try{
            
          // La valeur � ajouter � la liste.  
          Double d = new Double( Double.parseDouble(first_token) );
          list.add(d);
          
          }catch(NumberFormatException e){
            //rien faire, car c'est une ligne de commentaire ...
          }
        }//fin if
      }//fin else
    }//fin du while
  }
  
    
}// fin de la classe SRealFunctionParser 
