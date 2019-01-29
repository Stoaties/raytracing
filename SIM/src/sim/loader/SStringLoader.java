/**
 * 
 */
package sim.loader;

import sim.loader.SLoaderException;

/**
 * Interface repr�sentant un constructeur d'objet (<i>loader</i>) prenant en param�tre un String.
 * Le String peut avoir plusieurs interpr�tation comme le nom d'un fichier, un mot cl� ou une ligne dans un fichier.
 * L'objet qui impl�mentera cette classe devra sp�cifier l'interpr�tation du String afin d'utiliser cette information judicieusement.
 * @author Simon V�zina
 * @since 2015-03-16
 * @version 2015-04-03
 */
public interface SStringLoader {

  /**
   * M�thode qui fait la construction d'un objet dont le type d�pendera de la classe impl�mentant l'interface SStringLoader.
   * L'utilisateur de cette fonction devra conna�tre le "type" de loader o� 
   * @param string - Les informations sous forme String permettant au loader de fonctionner.
   * @return L'objet construit par le loader. Son type devra �tre caster par l'utilisateur.
   * @throws SLoaderException S'il y a eu une erreur durant le chargement du fichier.
   */
  public Object load(String string) throws SLoaderException;
  
}//fin Interface SStringLoader
