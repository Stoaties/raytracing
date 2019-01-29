/**
 * 
 */
package sim.graphics.shader;

import sim.exception.SRuntimeException;
import sim.geometry.SRay;
import sim.graphics.SColor;
import sim.math.SVector3d;

/**
 * Interface permet de d�terminer la couleur associ�e � l'intersection d'un rayon avec une g�om�trie en fonction du mat�riel appliqu� sur la g�om�trie, de l'�clairage de la sc�ne et des primitives environnant (ex : effet d'ombrage).
 * Il existe une tr�s grande vari�t� de "shader" dont leur complexit� am�liore le r�alisme de la couleur calcul�e.
 * 
 * @author Simon V�zina
 * @since 2015-01-09
 * @version 2015-08-14
 */
public interface SShader {

	/**
	 * M�thode qui d�termine la couleur associ�e � un rayon ayant effectu� une intersection avec une g�om�trie de la sc�ne.
	 * Si le <b>rayon a touch� une g�om�trie</b>, un calcul sera r�alis� pour <b>d�terminer la couleur</b> en lien avec le <b>point d'intersection</b>.
	 * Si le <b>rayon n'a pas touch� de g�om�trie</b>, un <b>test d'intersection sera r�alis�</b> en premier temps pour d�terminer la g�om�trie intersect� et un calcul de couleur sera r�alis� par la suite.
	 * Une couleur </b>noire</b> sera retourn�e s'il n'y a <b>pas d'intersection</b> entre le rayon et une g�om�trie de la sc�ne.
	 * Il est important de rappeler que chaque g�om�trie poss�de un lien vers sa primitive parent qui d�tient l'information du mat�riel (ex: couleur de la g�om�trie).
	 * @param ray - Le rayon ayant r�alis� une intersection.
	 * @return La couleur associ�e � l'intersection. S'il n'y a <b>pas d'intersection</b>, la couleur retourn�e sera <b>noire</b>.
	 * @throws SRuntimeException Si le rayon a d�j� �t� intersect� p�alablement.
	 */
	public SColor shade(SRay ray)throws SRuntimeException;
	
	/**
   * M�thode pour �valuer l'indice de r�fraction associ� � un point de l'espace.
   * Nous avons trois sc�narios possibles :
   * <p>1) Si la position est situ�e � la <b>fronti�re d'une g�om�trie</b> (sur la surface), elle sera consid�r�e comme � l'ext�rieure de la g�om�trie et l'indice n = 1.0 sera affect�.</p>
   * <p>2) Si la position est situ�e dans <b>une g�om�trie</b>, il faudra obtenir son indice de r�fraction n s'il poss�de une primitive comme parent, sinon n = 1.0 sera affect�.</p>
   * <p>3) Si la position est situ�e dans <b>plusieurs g�om�trie</b>, NOUS AVONS PR�SENTEMENT UN PROBL�ME !!! qui devra �tre r�solu dans le futur.</p>
   * @param position - La position dans l'espace des g�om�tries.
   * @return L'indice de r�fraction associ� � la position.
   */
  public double evaluateRefractiveIndex(SVector3d position);
  
}//fin interface SShader
