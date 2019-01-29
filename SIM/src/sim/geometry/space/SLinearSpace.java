/**
 * 
 */
package sim.geometry.space;

import java.util.List;

import sim.exception.SRuntimeException;
import sim.geometry.SGeometry;
import sim.geometry.SRay;
import sim.math.SVector3d;
import sim.util.SLog;

/**
 * Classe qui repr�sente un espace � g�om�trie lin�aire. Cela signifie que les g�om�tries ne sont pas organis�es
 * dans une structure complexe. Il sera test� en s�quence lin�aire � chaque tentative d'intersection avec un rayon.
 * Cette version tr�s na�ve est efficace seulement pour des sc�nes comportant un nombre tr�s tr�s faible de g�om�tries.
 * 
 * @author Simon V�zina
 * @since 2015-01-10
 * @version 2015-12-27
 */
public class SLinearSpace extends SAbstractGeometrySpace {

	/**
	 * Constructeur d'un espace � g�om�trie lin�aire.
	 */
	public SLinearSpace()
	{
	  super();
	}
	
	@Override
	public SRay nearestIntersection(SRay ray, double t_max)throws SRuntimeException
	{
		//V�rifier si le rayon a d�j� intersect� une g�om�trie
		if(ray.asIntersected())
			throw new SRuntimeException("Erreur SLinearSpace 001 : Le rayon en param�tre a d�j� intersect� une g�om�trie.");
				
		//V�rifier la valeur de t_max est ad�quate
		if(t_max < 0.0)
			throw new SRuntimeException("Erreur SLinearSpace 002 : Le temps/distance maximale ne peut pas �tre n�gative.");
				
		// V�rifier si l'espace a �t� initialis�
    if(!space_initialized)
      throw new SRuntimeException("Erreur SLinearSpace 003 : L'espace de g�om�trie n'a pas �t� initialis�.");
    
		//Obtenir la liste des intersections
		List<SRay> list_intersection = intersections(geometry_list, ray, t_max);
		
		//Si la liste n'est pas vide, la premi�re intersection sera l'�l�ment [0] de la liste, 
		//car elle doit �tre tri�e en ordre croissant de temps/distante
		if(!list_intersection.isEmpty())
			return list_intersection.get(0);
		else
			return ray;	//sans intersection, on retourne le rayon d'origine
	}
	
	@Override
	public List<SRay> nearestOpaqueIntersection(SRay ray, final double t_max)throws SRuntimeException
	{
	  //V�rifier si le rayon a d�j� intersect� une g�om�trie
    if(ray.asIntersected())
      throw new SRuntimeException("Erreur SLinearSpace 004 : Le rayon en param�tre a d�j� intersect� une g�om�trie.");
        
    //V�rifier la valeur de t_max est ad�quate
    if(t_max < 0.0)
      throw new SRuntimeException("Erreur SLinearSpace 005 : Le temps/distance maximale ne peut pas �tre n�gative.");
        
    // V�rifier si l'espace a �t� initialis�
    if(!space_initialized)
      throw new SRuntimeException("Erreur SLinearSpace 006 : L'espace de g�om�trie n'a pas �t� initialis�.");
    
	  return nearestOpaqueIntersection(geometry_list, ray, t_max);
	}
	
  @Override
  public List<SGeometry> listInsideGeometry(SVector3d v) throws SRuntimeException
  {
    // V�rifier si l'espace a �t� initialis�
    if(!space_initialized)
      throw new SRuntimeException("Erreur SLinearSpace 007 : L'espace de g�om�trie n'a pas �t� initialis�.");
    
    return listInsideGeometry(geometry_list, v);
  }
 
  @Override
  public void initialize()
  {
    SLog.logWriteLine("Message SLinearSpace : Construction de l'espace lin�aire des g�om�tries.");
    
    SLog.logWriteLine("Message SLinearSpace : Fin de la construction de l'espace lin�aire des g�om�tries.");
    SLog.logWriteLine();
    
    space_initialized = true;
  }
  
}//fin classe SLinearSpace
