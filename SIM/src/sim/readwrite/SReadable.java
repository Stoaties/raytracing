/**
 * 
 */
package sim.readwrite;

/**
 * L'interface <b>SReadable</b> permet � l'objet de se configurer � l'aide d'une lecture dans un fichier txt.
 * 
 * @author Simon V�zina
 * @since 2015-01-09
 * @version 2017-06-01
 */
public interface SReadable {

	/**
	 * M�thode qui permet la lecture d'un fichier de format txt pour faire l'initialisation des param�tres de l'objet SReadable.
	 * 
	 * @param sbr Le buffer cherchant l'information dans le fichier txt.
	 * @throws IOException Si une erreur de type I/O est survenue lors de la lecture.
	 * @throws SInitializationException Si une erreur est survenue lors de l'initialisation de l'objet.
	 * @see SBufferedReader
	 */
	//public void read(SBufferedReader sbr) throws IOException, SInitializationException;
	
	/**
   * M�thode pour obtenir le nom de l'objet impl�mentant l'interface <b>SReadable</b>.
   * Ce nom correspond �galement au <b>mot cl�</b> � rechercher lors d'une lecture avec un <b>SBufferedReader</b> pour en faire la construction de l'objet en question.
   *    
   * @return Le nom de l'objet lisible.
   */
  public String getReadableName();
  
  /**
   * M�thode pour obtenir un tableau des <b>mots cl�s</b> permettant de d�finir les param�tres de l'objet impl�mentant l'interface <b>SReadable</b> lors de sa lecture.
   * 
   * @return Un tableau des <b>mots cl�s</b> des param�tres.
   */
  public String[] getReadableParameterName();
  
}//fin interface SReadable
