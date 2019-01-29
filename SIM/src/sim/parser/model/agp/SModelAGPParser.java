/**
 * 
 */
package sim.parser.model.agp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import sim.parser.model.obj.SModelOBJParserException;
import sim.util.SBufferedReader;
import sim.util.SFileSearch;
import sim.util.SStringUtil;


/**
 * Classe repr�sentant un lecteur de fichier .agp correspondant � un mod�le 3d dans le format du <i>projet Anaglyphe</i> r�alis� par Anik Souli�re.
 * 
 * @author Simon V�zina
 * @since 2015-08-01
 * @version 2015-08-02
 */
public class SModelAGPParser {

  private static final String MODEL_EXTENSION = "agp";
  
  private final String file_name;                     //nom du fichier .agp
  private final List<SPointSequence> sequence_list;   //liste des s�quences de points
  
  /**
   * Constructeur d'un parser pour objet 3d de de format agp.
   * @param file_name - Le nom du fichier comportant le mod�le 3d de format obj.
   * @throws SModelOBJParserException Si le fichier n'est pas ad�quat. 
   */
  public SModelAGPParser(String file_name)throws SModelAGPParserException
  {
    this.file_name = file_name;
    sequence_list = new ArrayList<SPointSequence>();
    
    parseAGP(file_name);
  }
  
  /**
   * M�thode pour obtenir le nom du fichier du mod�le 3d de format AGP.
   * @return Le nom du fichier.
   */
  public String getFileName()
  {
    return file_name;
  }
  
  /**
   * M�thode pour obtenir la liste des s�quences de points du mod�le 3d de format AGP.
   * @return La liste des s�quences.
   */
  public List<SPointSequence> getListSequence()
  {
    return sequence_list;
  }
  
  /**
   * M�hode pour faire la lecture d'un fichier agp � partir du nom du fichier.
   * @param file_name - Le nom du fichier agp � lire.
   * @throws SModelAGPParserException Si le fichier n'est pas dans le bon format.
   * @throws SModelAGPParserException Si le fichier n'est pas trouv�.
   * @throws SModelAGPParserException Si le fichier existe sous plusieurs versions dans les diff�rents sous-r�pertoires.
   */
  private void parseAGP(String file_name)throws SModelAGPParserException
  {
    //V�rification du format du fichier
    if(!SStringUtil.extensionFileLowerCase(file_name).equals(MODEL_EXTENSION))
      throw new SModelAGPParserException("Erreur SModelAGPParser 001 : Le fichier '" + file_name + "' n'est pas dans le format '" + MODEL_EXTENSION + "'.");
    
    //Trouver le fichier � partir du r�pertoire o� l'ex�cution de l'application est r�alis�e
    SFileSearch search = new SFileSearch("", file_name);
    
    if(!search.isFileFound())
      throw new SModelAGPParserException("Erreur SModelAGPParser 002 : Le fichier '" + file_name + "' n'est pas trouv�.");
    
    if(search.isManyFileFound())
      throw new SModelAGPParserException("Erreur SModelAGPParser 003 : Le fichier '" + file_name + "' a �t� trouv� plus d'une fois dans les diff�rents sous-r�pertoires. Veuillez en garder qu'une seule version.");
    
    File file = new File(search.getFileFoundList().get(0)); //prendre la 1ier trouv� dans la liste
    
    try
    {
      FileReader fr = new FileReader(file);
      SBufferedReader sbr = new SBufferedReader(fr);
      parseAGP(sbr);
      
    }catch(FileNotFoundException e){
      throw new SModelAGPParserException("Erreur SModelAGPParser 004 : IMPOSSIBLE! La localisation du fichier a �t� d�j� test�.");     
    }catch(IOException ioe){
      throw new SModelAGPParserException("Erreur SModelAGPParser 005 : Une erreur de type I/O est survenue.");      
    }
  }
  
  /**
   * M�thode pour lire le fichier de format agp � l'aide d'un SBufferedReader.
   * 
   * @param sbr - Le buffer de lecture.
   * @throws IOException S'il y a eu une erreur de type I/O lors de la lecture du fichier.
   * @throws SModelAGPParserException S'il y a eu une erreur menant � une incapacit� de d�finir le mod�le 3d.
   */
  private void parseAGP(SBufferedReader sbr)throws SModelAGPParserException, IOException
  { 
    boolean end_reading = false;        //condition de fin de lecture
    
    while(!end_reading)
    {
      String line = sbr.readLine();    //ligne courante en lecture
      
      //Si la ligne lu est "null", la fin du fichier est atteinte
      if(line == null)
        end_reading = true;
      else
      {
        StringTokenizer tokens = new StringTokenizer(line);  //fragmenter la ligne lue en �liminant les caract�res d'espacement
        
        //S'il y a un token � analyser
        if(tokens.hasMoreTokens())
        {
          String first_token = tokens.nextToken();        //obtenir le 1ier token � analyser
          first_token = first_token.replaceAll(",",".");  //remplacer les ',' par des '.' car c'est le format employ� par java
          
          //Si le 1ier token peut �tre converti en double, c'est que l'on commence la d�finition d'une s�quence.
          //Si la conversion n'est pas possible, c'est que nous avons une exception correspondant � une ligne de commentaire.
          //Nous allons alors analyser :
          //1i�re ligne - Coordonn�e x de la s�quence de points
          //2i�me ligne - Coordonn�e y de la s�quence de points
          //3i�me ligne - Coordonn�e z de la s�quence de points
          try{
            
            Double.parseDouble(first_token);  //v�rifier si le 1ier token est un double, une exception sera lanc�e si ce n'est pas le cas
             
            String line_x = line;                 //ligne d�j� lue
            line_x = line_x.replaceAll(",",".");  //remplacement des ',' par des '.' pour les nombres d�cimaux
                        
            String line_y = sbr.readLine();       //ligne suivante lue
            line_y = line_y.replaceAll(",",".");  //remplacement des ',' par des '.' pour les nombres d�cimaux
                                    
            String line_z = sbr.readLine();       //ligne suivante lue
            line_z = line_z.replaceAll(",",".");  //remplacement des ',' par des '.' pour les nombres d�cimaux
                        
            parsePointSequence(line_x, line_y, line_z, sbr.atLine() - 3);
            
          }catch(NumberFormatException e){
            //rien faire, car c'est une ligne de commentaire ...
          }
        }
      }  
    }//fin while
  }
  
  /**
   * M�thode pour analyser trois string d�finissant les coordonn�es x,y et z d'une s�quence de points.
   * @param line_x - La ligne d�finissant les coordonn�es x.
   * @param line_y - La ligne d�finissant les coordonn�es y.
   * @param line_z - La ligne d�finissant les coordonn�es z.
   * @throws SModelAGPParserException Si les trois string n'ont pas le m�me nombre d'�l�ment en d�finition.
   * @throws SModelAGPParserException Si une ligne n'est pas bien d�fini.
   */
  public void parsePointSequence(String line_x, String line_y, String line_z, long reading_line)throws SModelAGPParserException
  {
    //V�rification si une ligne est 'null'. Dans ce cas, il y a une derni�re s�quence de point qui est alors mal d�fini
    if(line_x == null || line_y == null || line_z == null)
      throw new SModelAGPParserException("Erreur SModelAGPParseur 006 : � la s�quence de ligne " + reading_line + ", une ligne est manquante ce qui rend la s�quence de points mal d�finie.");
    
    //Fragmenter les lignes
    StringTokenizer tokens_x = new StringTokenizer(line_x);  //fragmenter la ligne d�finissant x
    StringTokenizer tokens_y = new StringTokenizer(line_y);  //fragmenter la ligne d�finissant y
    StringTokenizer tokens_z = new StringTokenizer(line_z);  //fragmenter la ligne d�finissant z
    
    //V�rification sur le nombre d'�l�ment par ligne
    if(tokens_x.countTokens() != tokens_y.countTokens() || tokens_x.countTokens() != tokens_z.countTokens())
      throw new SModelAGPParserException("Erreur SModelAGPParseur 007 : � la s�quence de ligne " + reading_line + ", il y a " +  tokens_x.countTokens() + " coordonn�es x, " + tokens_y.countTokens() + " coordonn�es y et " + tokens_z.countTokens() + " coordonn�es z. La s�quence est mal d�finie.");
  
    try{
      
      //Construire la s�quence de point avec le nombre de point correspondant au nombre de tokens
      SPointSequence sequence = new SPointSequence(tokens_x.countTokens());
      
      int index;  //l'indexage des points
      
      //Remplir la s�quence de coordonn�e x
      index = 0;
      
      while(tokens_x.hasMoreTokens())
      {
        sequence.setX(index, Double.parseDouble(tokens_x.nextToken()));
        index++;
      }
      
      //Remplir la s�quence de coordonn�e y
      index = 0;
      
      while(tokens_y.hasMoreTokens())
      {
        sequence.setY(index, Double.parseDouble(tokens_y.nextToken()));
        index++;
      }
      
      //Remplir la s�quence de coordonn�e z
      index = 0;
      
      while(tokens_z.hasMoreTokens())
      {
        sequence.setZ(index, Double.parseDouble(tokens_z.nextToken()));
        index++;
      }
      
      //La s�quence �tant termin�e, nous pouvons l'ajouter � la liste des s�quences
      sequence_list.add(sequence);
      
    }catch(NumberFormatException e){
      throw new SModelAGPParserException("Erreur SModelAGPParseur 008 : � la s�quence de ligne " + reading_line + ", un �l�ment lu n'a pas �t� convertir en valeur num�rique. La s�quence est ainsi invalide.");
    }
  }
  
  /**
   * Test de la classe SModelAGPParser
   * @param arg
   */
  public static void main(String[] arg)
  {
    test("shuriken.agp");
    test("pyramide.agp");
  }
  
  private static void test(String file_name)
  {
    try{
      
      System.out.println("Fichier en test : '" + file_name + "'.");
      
      SModelAGPParser parser = new SModelAGPParser(file_name);
    
      //Faire l'affichage du contenu
      List<SPointSequence> sequences = parser.getListSequence();
      
      for(SPointSequence s : sequences)
      { 
        for(int i=0; i<s.size(); i++)
          System.out.print(s.getPoint(i).getX() + "\t");
        System.out.println();
        
        for(int i=0; i<s.size(); i++)
          System.out.print(s.getPoint(i).getY() + "\t");
        System.out.println();
        
        for(int i=0; i<s.size(); i++)
          System.out.print(s.getPoint(i).getZ() + "\t");
        System.out.println();
        
        System.out.println();
      }
    }catch(SModelAGPParserException e){
      e.printStackTrace();
      System.out.println("test en �chec !!!");
    }
  }
}//fin de la classe SModelAGPParser
