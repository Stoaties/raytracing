/**
 * 
 */
package sim.util;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import sim.exception.SConstructorException;

/**
 * Classe qui permet la recherche d'un nom de fichier à partir d'un emplacement dans un ensemble de répertoire 
 * et de sous-répertoire de façon récursif.  
 * 
 * @author Simon Vézina
 * @since 2015-04-02
 * @version 2016-05-20
 */
public class SFileSearch {

  //-------------
  // CONSANTES //
  //-------------
  
  /**
   * La constante <b>DEFAULT_MAX_DEPTH<b> correspond au niveau de profondeur maximal de recherche par récursivité étant égal à {@value}.
   */
  private final static int DEFAULT_MAX_DEPTH = 8;
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>max_depth<b> correspond au niveau de profondeur de recherche par récursivité.
   */
  private final int max_depth;
  
  /**
   * La variable 'file_name_to_search' correspond au fichier à recherche. Ce nom peut comprendre "répertoire de localisation"/"nom du fichier".
   * S'il y a un nom de répertoire d'inclu au nom du fichier, cette information sera ajoutée à la variablle 'starting_subdirectory' lors de la recherche.
   */
  private final String file_name_to_search;     
  
  /**
   * La variable 'starting_subdirectory' correspond au nom de répertoire à partir du quel nous allons débuter la recherche du fichier.
   * Il est important de préciser que la recherche commencera <b>toujours</b> à partir du répertoire <b>"user.dir"</b>.
   */
  private final String starting_subdirectory;  
  
  /**
   * La variable 'file_found_list' correspond à la liste des noms de fichier (répertoire/nom_fichier) où le fichier en recherche a été trouvé.
   */
  private final List<String> file_found_list;    
 
  /**
   * Constructeur d'un chercheur de fichier à partir du répertoire "user.dir" où l'application est lancée.
   *
   * @param file_name Le nom du fichier à chercher.
   * @throws SConstructorException Si le sous-répertoire ne permet pas d'identifier un répertoire valide.
   * @see System.getProperty("user.dir")
   */
  public SFileSearch(String file_name) throws SConstructorException
  {
    this("",file_name);
  }
  
  /**
   * Constructeur d'un chercheur de fichier à partir du répertoire "user.dir" où l'application est lancée
   * et d'un sous-répertoire. Le recherche par récursivité aura une profondeur maximal par défaut.
   * 
   * @param subdirectory Le nom du sous-répertoire où débutera la recherche à partir du répertoire "user.dir". 
   * @param file_name Le nom du fichier à chercher.
   * @throws SConstructorException Si le sous-répertoire ne permet pas d'identifier un répertoire valide.
   * @see System.getProperty("user.dir")
   */
  public SFileSearch(String subdirectory, String file_name) throws SConstructorException
  {
    this(subdirectory, file_name, DEFAULT_MAX_DEPTH);
  }
  
  /**
   * Constructeur d'un chercheur de fichier à partir du répertoire "user.dir" où l'application est lancée
   * et d'un sous-répertoire.
   * 
   * @param subdirectory Le nom du sous-répertoire où débutera la recherche à partir du répertoire "user.dir". 
   * @param file_name Le nom du fichier à chercher.
   * @throws SConstructorException Si le sous-répertoire ne permet pas d'identifier un répertoire valide.
   * @see System.getProperty("user.dir")
   */
  public SFileSearch(String subdirectory, String file_name, int depth) throws SConstructorException
  {
    // Vérification du niveau de profondeur.
    if(depth < 1)
      throw new SConstructorException("Erreur SFileSearch 001 : Le niveau de profondeur '" + depth + "' doit être supérieur à 0.");
    
    max_depth = depth;
    
    // Mettre le sous-répertoire en minuscule
    subdirectory = subdirectory.toLowerCase();
    
    // Obtenir le nom du fichier sans les informations de répertoire de localisation du fichier.
    file_name_to_search = SStringUtil.getFileNameWithoutDirectory(file_name.toLowerCase());  
        
    // Établir le nom du répertoire initiale de recherche débutant par le répertoire "user.dir" (là où l'application est lancée)
    if(subdirectory.equals(""))
      this.starting_subdirectory = System.getProperty("user.dir");
    else
      this.starting_subdirectory = System.getProperty("user.dir") + SStringUtil.END_LINE_CARACTER + subdirectory;
    
    // Liste des fichiers trouvés, car il peut y avoir plusieurs fichiers avec le même nom
    file_found_list = new ArrayList<String>();        
    
    // Création du répertoire où début la recherche
    File directory = new File(starting_subdirectory); 
    
    // Vérification de la validité du nom du répertoire
    if(directory.isDirectory())
      search(directory, 0);        //début de la recherche
    else
      throw new SConstructorException("Erreur SFileSearch 001 : Le sous-répertoire '" + subdirectory + "' n'est pas un répertoire.");
  }
  
  /**
   * Méthode pour obtenir le nom du fichier à rechercher.
   * @return Le nom du fichier recherché.
   */
  public String getFileNameToSearch()
  {
    return file_name_to_search;
  }
 
  /**
   * Méthode pour déterminer si le fichier a été trouvé. Cependant, il est possible que le nom du fichier ait été trouvé plus d'une fois.
   * @return <b>true</b> si le fichier a été trouvé et <b>false</b> sinon.
   */
  public boolean isFileFound()
  {
    if(file_found_list.isEmpty())
      return false;
    else
      return true;
  }
  
  /**
   * Méthode pour déterminer si le fichier a été trouvé <b>plus d'une fois</b>.
   * @return <b>true</b> si le fichier a été trouvé plus d'une fois et <b>false</b> sinon. 
   */
  public boolean isManyFileFound()
  {
    return file_found_list.size() > 1;
  }
  
  /**
   * Méthode pour obtenir la liste des adresses où l'on a trouvé le nom du fichier. La liste sera vide si le fichier n'a pas été trouvé.
   * @return La liste des adresses où le fichier a été trouvé.
   */
  public List<String> getFileFoundList()
  {
    return file_found_list;
  }
 
  /**
   * Méthode pour faire la recherche du fichier désiré de façon récursive. À chaque fois que le fichier sera trouvé,
   * l'adresse du fichier sera sauvegardé dans la liste.
   * 
   * @param file Le répertoire où la recherche est rendue.
   * @param depth Le niveau de profondeur de la récursivité.
   */
  private void search(File file, int depth)
  {
    // Vérification du niveau de profondeur maximal du fichier en recherche
    if(depth < max_depth)
    {
      if(file.isDirectory())                  // Vérification si l'analyse se fait sur un répertoire
        if(file.canRead())                    // Vérification si l'accès au répertoire est possible
          for(File temp : file.listFiles())   // Itérer sur l'ensemble du contenu du répertoire
            if(temp.isDirectory())            // Vérifier si le fichier itéré est un répertoire
              search(temp, depth + 1);        // Lecture récursive du répertoire de niveau supérieur
            else 
              if(getFileNameToSearch().equals(temp.getName().toLowerCase()))  // Si le fichier est trouvé ! 
                file_found_list.add(temp.getAbsoluteFile().toString());    
    }
  }
  
}//fin classe SFileSearch
