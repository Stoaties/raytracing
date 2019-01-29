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
 * La classe abstraite <b>SAbstractGeometry</b> représentant une géométrie générale sans définition spatiale. 
 *  
 * @author Simon Vézina
 * @since 2014-12-30
 * @version 2017-08-15
 */
public abstract class SAbstractGeometry extends SAbstractReadableWriteable implements SGeometry {

  //--------------
  // CONSTANTES //
  //--------------
  
  //Numéro d'identification des types de géométrie 
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
	 * La variable <b>atomic_counteur_ID</b> correspond à un compteur THREAD PROOF à numéro d'identification des géométries. 
	 */
	private static final AtomicLong atomic_counter_ID = new AtomicLong();
  
	/**
   * La variable <b>id</b> correspond au numéro d'identification unique de la géométrie.
   */
	private long id;						           
	
	/**
	 * La variable <b>primitive_parent</b> correspond à la primitive qui est le propriétaire de la géométrie (son parent).
	 */
	private SPrimitive primitive_parent;	 
	
	//----------------
	// CONSTRUCTEUR //
	//----------------
	
	/**
	 * Constructeur d'une géométrie sans parent primitive.
	 */
	public SAbstractGeometry()
	{
		this(null);
	}
	
	/**
	 * Constructeur par défaut d'une géométrie ayant une primitive comme parent.
	 * 
	 * @param parent La primitive parent de la géométrie.
	 */
	public SAbstractGeometry(SPrimitive parent)
	{
		// Mettre à jour le compter et obtenir le nouveau ID (THREAD PROOF)
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
      throw new SRuntimeException("Erreur SAbstractGeometry 001 : La géométrie ne possède pas de primitive comme parent.");
    
  }
  
  @Override
  public void setPrimitiveParent(SPrimitive parent)throws SRuntimeException
  {
    if(primitive_parent != null)
      throw new SRuntimeException("Erreur SAbstractGeometry 002 : La géométrie ne peut pas se faire affecter un nouveau parent, car cette valeur a été préalablement déterminée.");
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
	 * Méthode pour déterminer la normale à la surface de la géométrie intersectée par le rayon.
	 * Cette méthode doit déterminer <u>l'orientation extérieure</u> à la géométrie.
	 * 
	 * @param ray Le rayon réalisant l'intersection avec la géométrie.
	 * @param intersection_t Le temps requis au rayon pour réaliser l'intersection. 
	 * @return La normale à la surface <b>normalisée</b> où est réalisée l'intersection.
	 */
	abstract protected SVector3d evaluateIntersectionNormal(SRay ray, double intersection_t);
	
	/**
	 * Méthode pour déterminer les coordonnées uv de la géométrie à l'endroit où il y a intersectin avec un rayon.
	 * Avant d'utiliser cette méthode, il faut préalablement évaluer le temps pour réaliser l'intersection.
   * Cette méthode prendra pour acquis que ces tests ont été réalisés adéquatement.
   * 
	 * @param ray Le rayon réalisant l'intersection avec la géométrie.
	 * @param intersection_t Le temps requis au rayon pour réaliser l'intersection. 
	 * @return La coordonnée uv de la surface où est réalisée l'intersection.
	 */
	abstract protected SVectorUV evaluateIntersectionUV(SRay ray, double intersection_t);
	
	@Override
	public boolean equals(Object other)
	{
		if (other == null) 					//Test du null
		  return false;
	    
	  if (other == this)					//Test de la même référence 
	    return true;
	    
	  if (!(other instanceof SGeometry))	//Test d'un type différent
	    return false;
	    
	  //Vérification du numéro d'identification, car il doit être unique pour chaque géométrie construite
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
