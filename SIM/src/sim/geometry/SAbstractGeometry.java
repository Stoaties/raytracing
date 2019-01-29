/**
 * 
 */
package sim.geometry;

import java.util.concurrent.atomic.AtomicLong;

import sim.exception.SRuntimeException;
import sim.graphics.SPrimitive;
import sim.math.SVector3d;
import sim.math.SVectorUV;
import sim.readwrite.SAbstractReadableWriteable;
import sim.util.SInitializationException;

/**
 * La classe abstraite <b>SAbstractGeometry</b> repr�sentant une g�om�trie g�n�rale sans d�finition spatiale. 
 *  
 * @author Simon V�zina
 * @since 2014-12-30
 * @version 2017-08-15
 */
public abstract class SAbstractGeometry extends SAbstractReadableWriteable implements SGeometry {

  //--------------
  // CONSTANTES //
  //--------------
  
  //Num�ro d'identification des types de g�om�trie 
  public static final int PLANE_CODE = 1;
  public static final int DISK_CODE = 2;
  public static final int SPHERE_CODE = 3;
  public static final int TUBE_CODE = 4;
	public static final int CYLINDER_CODE = 5;
	public static final int CONE_CODE = 6;
	public static final int TRIANGLE_CODE = 7;
	public static final int BTRIANGLE_CODE = 8;
	public static final int TRANSFORMABLE_CODE = 9;
	public static final int CUBE_CODE = 10;
	public static final int SPHERICAL_CAP_CODE = 11;
	public static final int LENS_CODE = 12;
	public static final int TORUS_CODE = 13;
	
	//-------------
	// VARIABLES //
	//-------------
	
	/**
	 * La variable <b>atomic_counteur_ID</b> correspond � un compteur THREAD PROOF � num�ro d'identification des g�om�tries. 
	 */
	private static final AtomicLong atomic_counter_ID = new AtomicLong();
  
	/**
   * La variable <b>id</b> correspond au num�ro d'identification unique de la g�om�trie.
   */
	private long id;						           
	
	/**
	 * La variable <b>primitive_parent</b> correspond � la primitive qui est le propri�taire de la g�om�trie (son parent).
	 */
	private SPrimitive primitive_parent;	 
	
	//----------------
	// CONSTRUCTEUR //
	//----------------
	
	/**
	 * Constructeur d'une g�om�trie sans parent primitive.
	 */
	public SAbstractGeometry()
	{
		this(null);
	}
	
	/**
	 * Constructeur par d�faut d'une g�om�trie ayant une primitive comme parent.
	 * 
	 * @param parent La primitive parent de la g�om�trie.
	 */
	public SAbstractGeometry(SPrimitive parent)
	{
		// Mettre � jour le compter et obtenir le nouveau ID (THREAD PROOF)
	  id = atomic_counter_ID.incrementAndGet();
	  
		primitive_parent = parent;
	}
	
	@Override
	public long getID()
	{ 
	  return id;
	}
	
	@Override
	public int hashCode()
	{
	  return (int) id;
	}
	
	@Override
  public SPrimitive getPrimitiveParent()throws SRuntimeException
  { 
    if(primitive_parent != null)
      return primitive_parent;
    else
      throw new SRuntimeException("Erreur SAbstractGeometry 001 : La g�om�trie ne poss�de pas de primitive comme parent.");
    
  }
  
  @Override
  public void setPrimitiveParent(SPrimitive parent)throws SRuntimeException
  {
    if(primitive_parent != null)
      throw new SRuntimeException("Erreur SAbstractGeometry 002 : La g�om�trie ne peut pas se faire affecter un nouveau parent, car cette valeur a �t� pr�alablement d�termin�e.");
    else
      primitive_parent = parent;
  }
  
	@Override
	public boolean isTransparent()
	{
	  if(primitive_parent != null)
	    if(primitive_parent.getMaterial() != null)
	      return primitive_parent.getMaterial().isTransparent();
	    else
	      return false;
	  else
	    return false;
  } 
		
	/**
	 * M�thode pour d�terminer la normale � la surface de la g�om�trie intersect�e par le rayon.
	 * Cette m�thode doit d�terminer <u>l'orientation ext�rieure</u> � la g�om�trie.
	 * 
	 * @param ray Le rayon r�alisant l'intersection avec la g�om�trie.
	 * @param intersection_t Le temps requis au rayon pour r�aliser l'intersection. 
	 * @return La normale � la surface <b>normalis�e</b> o� est r�alis�e l'intersection.
	 */
	abstract protected SVector3d evaluateIntersectionNormal(SRay ray, double intersection_t);
	
	/**
	 * M�thode pour d�terminer les coordonn�es uv de la g�om�trie � l'endroit o� il y a intersectin avec un rayon.
	 * Avant d'utiliser cette m�thode, il faut pr�alablement �valuer le temps pour r�aliser l'intersection.
   * Cette m�thode prendra pour acquis que ces tests ont �t� r�alis�s ad�quatement.
   * 
	 * @param ray Le rayon r�alisant l'intersection avec la g�om�trie.
	 * @param intersection_t Le temps requis au rayon pour r�aliser l'intersection. 
	 * @return La coordonn�e uv de la surface o� est r�alis�e l'intersection.
	 */
	abstract protected SVectorUV evaluateIntersectionUV(SRay ray, double intersection_t);
	
	@Override
	public boolean equals(Object other)
	{
		if (other == null) 					//Test du null
		  return false;
	    
	  if (other == this)					//Test de la m�me r�f�rence 
	    return true;
	    
	  if (!(other instanceof SGeometry))	//Test d'un type diff�rent
	    return false;
	    
	  //V�rification du num�ro d'identification, car il doit �tre unique pour chaque g�om�trie construite
	  SAbstractGeometry o = (SAbstractGeometry)other;
	    
	  if(id != o.getID())
	    return false;
	   
	  return true;
	}
			
	@Override
  protected void readingInitialization() throws SInitializationException
  {
   
  }
		
	@Override
  public String toString()
  {
    return "SGeometry [name=" + getReadableName() + ", id=" + id + "]";
  }
  
}//fin de la classe abstraite SAbstractGeometry
