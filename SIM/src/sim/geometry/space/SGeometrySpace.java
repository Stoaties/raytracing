/**
 * 
 */
package sim.geometry.space;

import java.util.List;

import sim.exception.SRuntimeException;
import sim.geometry.SGeometry;
import sim.geometry.SRay;
import sim.math.SVector3d;

/**
 * L'interface <b>SGeometrySpace</b> repr�sentant un espace de rengement des g�om�tries. 
 * Cet espace permet d'organiser les g�om�tries afin d'effectuer le calculs de l'intersection avec les g�om�tries plus efficacement.
 * 
 * @author Simon V�zina
 * @since 2015-01-10
 * @version 2015-12-29
 */
public interface SGeometrySpace {

	/**
	 * M�thode qui ajoute une g�om�trie � l'espace.
	 * @param geometry - La g�om�trie � ajouter � l'espace.
	 */
	public void addGeometry(SGeometry geometry);
	
	/**
	 * M�thode qui ajoute une liste de g�om�tries � l'espace.
	 * @param list - La liste de g�om�tries � ajouter � l'espace.
	 */
	public void addGeometry(List<SGeometry> list);
	
	/**
	 * M�thode qui �value l'intersection la plus pr�s entre un rayon et les diff�rentes g�om�tries de l'espace ne d�passant pas un certain temps maximal.
	 * @param ray - Le rayon � intersecter avec les g�om�tries de l'espace.
	 * @param t_max - Le temps maximal.
	 * @return Un nouveau rayon avec les propri�t�s de l'intersection la plus pr�s ne d�passant pas le temps maximal. Si <b>aucune intersection</b> respectant ces contraintes n'a peut �tre r�alis�e, le <b>rayon d'origine sera retourn�</b> �tant un rayon sans intersection.
	 * @throws SRuntimeException Si le rayon a d�j� intersect� une autre g�om�trie.
	 * @throws SRuntimeException Si la valeur de t_max est n�gative.
	 * @throws SRuntimeException Si l'espace des g�om�tries n'a pas �t� pr�alablement initialis�.
	 */
	public SRay nearestIntersection(SRay ray, double t_max) throws SRuntimeException;
		
	/**
	 * M�thode qui �value l'intersection la plus pr�s entre un rayon et les diff�rentes g�om�tries <b>opaque</b> de l'espace ne d�passant pas un certain temps maximal.
	 * Une liste des g�om�tries transparentes sera ordonn�e en <b>ordre inverse d'apparition</b> (ordre d�croissant) et la <b>liste d�butera par une g�om�trie opaque</b> s'il y a eu intersection avec une g�om�trie opaque.
	 * @param ray - Le rayon � intersecter avec les g�om�tries de l'espace.
	 * @param t_max - Le temps maximal.
	 * @return Une liste des g�om�tries transparentes intersect�es en <b>ordre d�croissant</b> d�butant par une g�om�trie opaque s'il y a eu lieu. La liste sera <b>vide</b> s'il n'y a <b>aucune intersection</b> sur le temps maximal.
	 * @throws SRuntimeException Si le rayon a d�j� intersect� une autre g�om�trie.
	 * @throws SRuntimeException Si la valeur de t_max est n�gative.
	 * @throws SRuntimeException Si l'espace des g�om�tries n'a pas �t� pr�alablement initialis�.
	 */
	public List<SRay> nearestOpaqueIntersection(SRay ray, double t_max) throws SRuntimeException;
	
	/**
	 * M�thode pour obtenir la liste des g�om�tries o� la position du vecteur <i>v</i> se retrouve � l'int�rieur.
	 * Si la liste est vide, c'est que le vecteur <i>v</i> se retrouve � l'int�rieur d'aucune g�om�trie.
	 * @param v - La position d'un point de l'espace.
	 * @return La liste des g�om�tries o� le vecteur <i>v</i> se retrouve � l'int�rieur.
	 * @throws SRuntimeException Si l'espace des g�om�tries n'a pas �t� pr�alablement initialis�.
	 */
	public List<SGeometry> listInsideGeometry(SVector3d v) throws SRuntimeException;
	
	/**
	 * M�thode pour initialiser l'espace des g�om�tries. L'espace ne sera pas op�rationnel 
	 * pour effectuer des tests d'intersection si cette m�thode n'est pas activ�e avant.
	 */
	public void initialize();
	
}//fin interface SGeometrySpace
