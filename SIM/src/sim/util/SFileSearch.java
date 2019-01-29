/**
 * 
 */
package sim.util;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import sim.exception.SConstructorException;

/**
 * Classe qui permet la recherche d'un nom de fichier � partir d'un emplacement dans un ensemble de r�pertoire 
 * et de sous-r�pertoire de fa�on r�cursif.  
 * 
 * @author Simon V�zina
 * @since 2015-04-02
 * @version 2016-05-20
 */
public class SFileSearch {

  //-------------
  // CONSANTES //
  //-------------
  
  /**
   * La constante <b>DEFAULT_MAX_DEPTH<b> correspond au niveau de profondeur maximal de recherche par r�cursivit� �tant �gal � {@value}.
   */
  private final static int DEFAULT_MAX_DEPTH = 8;
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>max_depth<b> correspond au niveau de profondeur de recherche par r�cursivit�.
   */
  private final int max_depth;
  
  /**
   * La variable 'file_name_to_search' correspond au fichier � recherche. Ce nom peut comprendre "r�pertoire de localisation"/"nom du fichier".
   * S'il y a un nom de r�pertoire d'inclu au nom du fichier, cette information sera ajout�e � la variablle 'starting_subdirectory' lors de la recherche.
   */
  private final String file_name_to_search;     
  
  /**
   * La variable 'starting_subdirectory' correspond au nom de r�pertoire � partir du quel nous allons d�buter la recherche du fichier.
   * Il est important de pr�ciser que la recherche commencera <b>toujours</b> � partir du r�pertoire <b>"user.dir"</b>.
   */
  private final String starting_subdirectory;  
  
  /**
   * La variable 'file_found_list' correspond � la liste des noms de fichier (r�pertoire/nom_fichier) o� le fichier en recherche a �t� trouv�.
   */
  private final List<String> file_found_list;    
 
  /**
   * Constructeur d'un chercheur de fichier � partir du r�pertoire "user.dir" o� l'application est lanc�e.
   *
   * @param file_name Le nom du fichier � chercher.
   * @throws SConstructorException Si le sous-r�pertoire ne permet pas d'identifier un r�pertoire valide.
   * @see System.getProperty("user.dir")
   */
  public SFileSearch(String file_name) throws SConstructorException
  {
    this("",file_name);
  }
  
  /**
   * Constructeur d'un chercheur de fichier � partir du r�pertoire "user.dir" o� l'application est lanc�e
   * et d'un sous-r�pertoire. Le recherche par r�cursivit� aura une profondeur maximal par d�faut.
   * 
   * @param subdirectory Le nom du sous-r�pertoire o� d�butera la recherche � partir du r�pertoire "user.dir". 
   * @param file_name Le nom du fichier � chercher.
   * @throws SConstructorException Si le sous-r�pertoire ne permet pas d'identifier un r�pertoire valide.
   * @see System.getProperty("user.dir")
   */
  public SFileSearch(String subdirectory, String file_name) throws SConstructorException
  {
    this(subdirectory, file_name, DEFAULT_MAX_DEPTH);
  }
  
  /**
   * Constructeur d'un chercheur de fichier � partir du r�pertoire "user.dir" o� l'application est lanc�e
   * et d'un sous-r�pertoire.
   * 
   * @param subdirectory Le nom du sous-r�pertoire o� d�butera la recherche � partir du r�pertoire "user.dir". 
   * @param file_name Le nom du fichier � chercher.
   * @throws SConstructorException Si le sous-r�pertoire ne permet pas d'identifier un r�pertoire valide.
   * @see System.getProperty("user.dir")
   */
  public SFileSearch(String subdirectory, String file_name, int depth) throws SConstructorException
  {
    // V�rification du niveau de profondeur.
    if(depth < 1)
      throw new SConstructorException("Erreur SFileSearch 001 : Le niveau de profondeur '" + depth + "' doit �tre sup�rieur � 0.");
    
    max_depth = depth;
    
    // Mettre le sous-r�pertoire en minuscule
    subdirectory = subdirectory.toLowerCase();
    
    // Obtenir le nom du fichier sans les informations de r�pertoire de localisation du fichier.
    file_name_to_search = SStringUtil.getFileNameWithoutDirectory(file_name.toLowerCase());  
        
    // �tablir le nom du r�pertoire initiale de recherche d�butant par le r�pertoire "user.dir" (l� o� l'application est lanc�e)
    if(subdirectory.equals(""))
      this.starting_subdirectory = System.getProperty("user.dir");
    else
      this.starting_subdirectory = System.getProperty("user.dir") + SStringUtil.END_LINE_CARACTER + subdirectory;
    
    // Liste des fichiers trouv�s, car il peut y avoir plusieurs fichiers avec le m�me nom
    file_found_list = new ArrayList<String>();        
    
    // Cr�ation du r�pertoire o� d�but la recherche
    File directory = new File(starting_subdirectory); 
    
    // V�rification de la validit� du nom du r�pertoire
    if(directory.isDirectory())
      search(directory, 0);        //d�but de la recherche
    else
      throw new SConstructorException("Erreur SFileSearch 001 : Le sous-r�pertoire '" + subdirectory + "' n'est pas un r�pertoire.");
  }
  
  /**
   * M�thode pour obtenir le nom du fichier � rechercher.
   * @return Le nom du fichier recherch�.
   */
  public String getFileNameToSearch()
  {
    return file_name_to_search;
  }
 
  /**
   * M�thode pour d�terminer si le fichier a �t� trouv�. Cependant, il est possible que le nom du fichier ait �t� trouv� plus d'une fois.
   * @return <b>true</b> si le fichier a �t� trouv� et <b>false</b> sinon.
   */
  public boolean isFileFound()
  {
    if(file_found_list.isEmpty())
      return false;
    else
      return true;
  }
  
  /**
   * M�thode pour d�terminer si le fichier a �t� trouv� <b>plus d'une fois</b>.
   * @return <b>true</b> si le fichier a �t� trouv� plus d'une fois et <b>false</b> sinon. 
   */
  public boolean isManyFileFound()
  {
    return file_found_list.size() > 1;
  }
  
  /**
   * M�thode pour obtenir la liste des adresses o� l'on a trouv� le nom du fichier. La liste sera vide si le fichier n'a pas �t� trouv�.
   * @return La liste des adresses o� le fichier a �t� trouv�.
   */
  public List<String> getFileFoundList()
  {
    return file_found_list;
  }
 
  /**
   * M�thode pour faire la recherche du fichier d�sir� de fa�on r�cursive. � chaque fois que le fichier sera trouv�,
   * l'adresse du fichier sera sauvegard� dans la liste.
   * 
   * @param file Le r�pertoire o� la recherche est rendue.
   * @param depth Le niveau de profondeur de la r�cursivit�.
   */
  private void search(File file, int depth)
  {
    // V�rification du niveau de profondeur maximal du fichier en recherche
    if(depth < max_depth)
    {
      if(file.isDirectory())                  // V�rification si l'analyse se fait sur un r�pertoire
        if(file.canRead())                    // V�rification si l'acc�s au r�pertoire est possible
          for(File temp : file.listFiles())   // It�rer sur l'ensemble du contenu du r�pertoire
            if(temp.isDirectory())            // V�rifier si le fichier it�r� est un r�pertoire
              search(temp, depth + 1);        // Lecture r�cursive du r�pertoire de niveau sup�rieur
            else 
              if(getFileNameToSearch().equals(temp.getName().toLowerCase()))  // Si le fichier est trouv� ! 
                file_found_list.add(temp.getAbsoluteFile().toString());    
    }
  }
  
}//fin classe SFileSearch
