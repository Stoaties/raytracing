/**
 * 
 */
package sim.parser.model.obj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import sim.exception.SConstructorException;
import sim.util.SBufferedReader;
import sim.util.SFileSearch;
import sim.util.SLog;
import sim.util.SStringUtil;

/**
 * Classe qui permet la lecture d'une librairie de mat�riel pour un format de mod�le 3d de type OBJ (Wavefront).
 * Le parser produira toujours au moins un mat�riel �tant le mat�riel par d�faut portant le nom de "default".
 * Ainsi, un fichier vide permettra d'avoir au moins un mat�riel pouvant ne pas �tre affect� si le mod�le 3d
 * est bien d�fini dans son ensemble.
 * @author Simon V�zina
 * @since 2015-03-28
 * @version 2015-09-14
 */
public class SMaterialOBJParser {

  private static final String MATERIAL_LIBRAIRY_OBJ_EXTENSION = "mtl";
  
  private static final int KA_CODE = 1;
  private static final int KD_CODE = 2;
  private static final int KS_CODE = 3;
  private static final int NS_CODE = 4;
  private static final int NEWMTL_CODE = 5;
  private static final int TEXTURE_KA_CODE = 6;
  private static final int TEXTURE_KD_CODE = 7;
  private static final int TEXTURE_KS_CODE = 8;
  
  private static final Hashtable<String,Integer> keyword_table = initialiseKeywordTable();
  
  private final String file_name; //nom du fichier .mtl
  
  private String current_material_name;
  private SVertex current_Ka;           //valeur Ka � affecter lors de la fin de la lecture du mat�riel pr�sentement en lecture
  private SVertex current_Kd;           //valeur Kd � affecter lors de la fin de la lecture du mat�riel pr�sentement en lecture
  private SVertex current_Ks;           //valeur Ks � affecter lors de la fin de la lecture du mat�riel pr�sentement en lecture
  private float current_Ns;             //valeur Ns � affecter lors de la fin de la lecture du mat�riel pr�sentement en lecture
  
  //Nom des textures s'il y a usage
  private String current_texture_Ka;    //propri�t� de r�flexion ambiante de la texture
  private String current_texture_Kd;    //propri�t� de r�flexion diffuse de la texture
  private String current_texture_Ks;    //propri�t� de r�flexion sp�culaire de la texture
  
  private List<SMaterialOBJ> material_list; //liste des mat�riels construit par la parser
  
  /**
   * Constructeur pour parser de librairie de mat�riel pour format de mod�le OBJ (Wavefront).
   * @param file_name - Le nom du fichier en lecture.
   */
  public SMaterialOBJParser(String file_name) throws SMaterialOBJParserException
  {
    this.file_name = file_name; //nom du fichier MTL en lecture
    
    material_list = new LinkedList<SMaterialOBJ>(); //construction de la liste contenant les futurs mat�riaux
    
    initializeCurrent();  //initialisation par d�faut des coefficients si un mat�riel est cr�� sans pr�ciser l'ensemble de ces valeurs

    parseMTL(file_name);
  }

  /**
   * M�thode pour obtenir le nom du fichier en lecture.
   * @return Le nom du fichier en lecture.
   */
  public String getFileName()
  {
    return file_name;
  }
  
  /**
   * M�thode pour obtenir la liste des mat�riels associ�s � la lecture du fichier MTL.
   * Un mat�riel par d�faut (portant le nom "default) sera automatiquement g�n�r� lors de la lecture.
   * Si le fichier est sans erreur, ce mat�riel ne devrait par �tre utilis�.
   * @return La liste des mat�riels du fichier MTL.
   */
  public List<SMaterialOBJ> getMaterialList()
  {
    return material_list;
  }
  
  /**
   * M�thode pour faire la table de correspondance entre un mot cl� un un code d'action.
   * @return La table de mot cl�.
   */
  private static Hashtable<String,Integer> initialiseKeywordTable()
  {
    Hashtable<String,Integer> table = new Hashtable<String,Integer>();
    
    table.put("Ka", new Integer(KA_CODE));
    table.put("Kd", new Integer(KD_CODE));
    table.put("Ks", new Integer(KS_CODE));
    table.put("Ns", new Integer(NS_CODE));
    
    table.put("newmtl", new Integer(NEWMTL_CODE));
    
    table.put("map_Ka", new Integer(TEXTURE_KA_CODE));
    table.put("map_Kd", new Integer(TEXTURE_KD_CODE));
    table.put("map_Ks", new Integer(TEXTURE_KS_CODE));
    
    //Autres termes � inclure �ventuellement :
    // d : "dissolve" (probablement l'opacit�) o� 1.0 est compl�tement opaque
    // Tr : l'inverse de d
    // map_bump : bump mapping
    // Ni : optical_density (range from 0.001 to 10)
    // Tf : transmission filter using RGB values
    // illum : ???
    
    return table;
  }
  
  /**
   * M�hode pour faire la lecture d'un fichier mtl � partir du nom du fichier.
   * @param file_name - Le nom du fichier obj � lire.
   */
  private void parseMTL(String file_name) throws SMaterialOBJParserException
  {
    //V�rification du format du fichier
    if(!SStringUtil.extensionFileLowerCase(file_name).equals(MATERIAL_LIBRAIRY_OBJ_EXTENSION))
      throw new SMaterialOBJParserException("Erreur SMaterialOBJParser 001 : Le fichier '" + file_name + "' n'est pas dans le format '" + MATERIAL_LIBRAIRY_OBJ_EXTENSION + "'.");
    
    //Trouver le fichier � partir du r�pertoire o� l'ex�cution de l'application est r�alis�e
    SFileSearch search = new SFileSearch("", file_name);
    
    if(!search.isFileFound())
      throw new SMaterialOBJParserException("Erreur SMaterialOBJParser 002 : Le fichier '" + file_name + "' n'est pas trouv�.");
    
    if(search.isManyFileFound())
      throw new SMaterialOBJParserException("Erreur SMaterialOBJParser 003 : Le fichier '" + file_name + "' a �t� trouv� plus d'une fois dans les diff�rents sous-r�pertoires. Veuillez en garder qu'une seule version.");
    
    File file = new File(search.getFileFoundList().get(0)); //prendre la 1ier trouv� dans la liste
    
    try
    {
      FileReader fr = new FileReader(file);
      SBufferedReader sbr = new SBufferedReader(fr);
      parseMTL(sbr);
      
    }catch(FileNotFoundException e){
      throw new SMaterialOBJParserException("Erreur SMaterialOBJParser 004 : IMPOSSIBLE! La localisation du fichier a �t� d�j� test�.");     
    }catch(IOException ioe){
      throw new SMaterialOBJParserException("Erreur SMaterialOBJParser 005 : Une erreur de type I/O est survenue.");      
    }
  }
  
  /**
   * M�thode pour lire le fichier de format mtl � l'aide d'un SBufferedReader.
   * @param sbr - Le buffer de lecture.
   */
  private void parseMTL(SBufferedReader sbr)throws IOException
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
          String keyword = tokens.nextToken();     //obtenir le mot cl� �tant le 1ier mot de la ligne           
                
          //Si le mot cl� doit �tre analys�
          if(keyword_table.containsKey(keyword))
          {
            int code = (keyword_table.get(keyword)).intValue();       //code de lecture
            
            //Obtenir la ligne restante (sans 1ier caract�re d'espacement + mot cl� + autres caract�res d'espacement)
            String remaining_line = SStringUtil.removeAllFirstSpacerCaracter(line);
            remaining_line = remaining_line.substring(keyword.length());  
            remaining_line = SStringUtil.removeAllFirstSpacerCaracter(remaining_line);
          
            try{
            parseMTLLine(code, remaining_line);                       //analyse de la ligne restante
            }catch(SConstructorException e){
              SLog.logWriteLine("Ligne " + (sbr.atLine()-1) + " - " + e.getMessage());
            }catch(NumberFormatException e){
              SLog.logWriteLine("Ligne " + (sbr.atLine()-1) + " - " + e.getMessage());
            }
          }
        }  
      }
    }//fin while end_reading
    
    //Puisque le format du fichier .mtl ne poss�de pas un mot cl� de fin,
    //lorsque la fin du fichier est atteint, il faut construire le dernier
    //mat�riel en d�finition. S'il n'y pas de nom affect�, il sera "default".
    buildMaterial();
  }
  
  /**
   * M�thode pour faire l'analyse de l'expression d'un restant de ligne en fonction du code de lecture lu 
   * au d�but de la ligne.
   * @param code - Le code repr�sentant la d�finition de l'expression restant � analyser.
   * @param remaining_line - L'expression � analyser sera le code de lecture.
   * @throws SConstructorException Si un vertex est mal lu.
   * @throws NumberFormatException Si un nombre float est mal lu.
   */
  private void parseMTLLine(int code, String remaining_line)throws SConstructorException, NumberFormatException
  {
    switch(code)
    {
      case KA_CODE : current_Ka = new SVertex(remaining_line); break;
      
      case KD_CODE : current_Kd = new SVertex(remaining_line); break;
      
      case KS_CODE : current_Ks = new SVertex(remaining_line); break;
      
      case NS_CODE : current_Ns = Float.valueOf(remaining_line); break;
      
      case TEXTURE_KA_CODE : remaining_line = SStringUtil.removeAllFirstSpacerCaracter(remaining_line);
                             
                             if(!remaining_line.equals(""))
                               current_texture_Ka = remaining_line;
                             break;
                             
      case TEXTURE_KD_CODE : remaining_line = SStringUtil.removeAllFirstSpacerCaracter(remaining_line);
                             
                             if(!remaining_line.equals(""))
                               current_texture_Kd = remaining_line;
                             
                             break;
      
      case TEXTURE_KS_CODE : remaining_line = SStringUtil.removeAllFirstSpacerCaracter(remaining_line);
                             
                             if(!remaining_line.equals(""))
                               current_texture_Ks = remaining_line;
                             
                             break;
      
      case NEWMTL_CODE :  buildMaterial();                                                            //construire un mat�riel avec les informations pr�c�dentes recueillies
                          initializeCurrent();                                                        //r�initialiser les coefficients pour la prochaines lecture
                          remaining_line = SStringUtil.removeAllFirstSpacerCaracter(remaining_line);
                          current_material_name = remaining_line;                                     //affectation du prochaine nom du mat�riel
                          break;
    }
  }
  
  /**
   * M�thode pour construire un nouveau mat�riel � partir des donn�es courantes lues pr�c�demment.
   * Ce mat�riel est par la suite ajout� � la liste.
   */
  private void buildMaterial()
  {
    material_list.add(new SMaterialOBJ(current_material_name, current_Ka, current_Kd, current_Ks, current_Ns, current_texture_Ka, current_texture_Kd, current_texture_Ks));
  }
  
  /**
   * M�thode pour faire l'initialisation des coefficients par d�faut d'un mat�riel (s'ils ne sont pas affect�s lors de la lecture d'un mat�riel). 
   */
  private void initializeCurrent()
  {
    current_material_name = SMaterialOBJ.DEFAULT_NAME;
    current_Ka = SMaterialOBJ.DEFAULT_AMBIENT_COEFFICIENT;
    current_Kd = SMaterialOBJ.DEFAULT_DIFFUSE_COEFFICIENT;
    current_Ks = SMaterialOBJ.DEFAULT_SPECULAR_COEFFICIENT;
    current_Ns = SMaterialOBJ.DEFAULT_SHININESS;
    
    current_texture_Ka = null;
    current_texture_Kd = null;
    current_texture_Ks = null;
  }
  
}//fin de la classe SMaterialOBJParser
