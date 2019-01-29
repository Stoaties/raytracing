/**
 * 
 */
package sim.graphics;

import sim.geometry.SRay;
import sim.graphics.shader.SShader;
import sim.math.SVector3d;
import sim.math.SVectorPixel;

/**
 * Classe abstraite repr�sentant un raytracer.
 * @author Simon V�zina
 * @since 2015-04-11
 * @version 2015-07-16
 */
public abstract class SAbstractRaytracer implements SRaytracer {

	 protected final SViewFrustum view_frustum; // pyramide de vue du raytracer
	 protected final SShader shader; // shader du raytracer
	 protected final SViewport viewport; // viewport du raytracer
	
	 /**
	   * Constructeur d'un raytracer.
	   * 
	   * @param view_frustum - La pyramide de vue.
	   * @param shader - Le calculateur d'illumination (<i>shader</i>).
	   * @param viewport - La fen�tre de rendu (<i>viewport</i>).
	   */
	  public SAbstractRaytracer(SViewFrustum view_frustum, SShader shader, SViewport viewport)
	  {
	    this.view_frustum = view_frustum;
	    this.shader = shader;
	    this.viewport = viewport;
	  }
	  
	/* (non-Javadoc)
	 * @see sim.graphics.SRaytracer#raytrace()
	 */
	@Override
	public void raytrace()
	{
		raytrace(nbPixels());
	}

	/* (non-Javadoc)
	 * @see sim.graphics.SRaytracer#nbPixels()
	 */
	@Override
	public int nbPixels() 
	{
		return viewport.getHeight() * viewport.getHeight();
	}

	/**
	   * M�thode pour �valuer la couleur � attribuer � un pixel.
	   * 
	   * @param pixel - La coordonn�e du pixel.
	   * @return La couleur � attribuer au pixel.
	   */
	abstract protected SColor evaluatePixelColor(SVectorPixel pixel);
	
	/**
	   * M�thode pour calculer la couleur associ� � un rayon lanc� depuis la position de la cam�ra et traversant un pixel de la pyramide de vue. La coordonn�e du
	   * pixel sera calcul�e par la pyramide de vue (view frustum) et la couleur sera d�termin�e par le shader ayant acc�s � la g�om�trie de la sc�ne et des
	   * mat�riaux qui s'y trouvent.
	   * 
	   * @param pixel - La coordonn�e du pixel.
	   * @return La couleur calcul�e par <b>le lanc� d'un rayon</b> dans le pixel.
	   */
	  protected SColor raytracePixel(SVectorPixel pixel)
	  {
	    // La coordonn�e 3d du pixel dans les coordonn�es de la sc�ne
	    SVector3d position_pixel = view_frustum.viewportToViewFrustum(pixel);

	    // Direction du rayon normalis�
	    SVector3d direction = position_pixel.substract(view_frustum.getCameraPosition()).normalize();
	   
	    // �valuer ad�quatement l'indice de r�fraction et reconstruction du rayon ad�quatement
	    double refractive_index = shader.evaluateRefractiveIndex(position_pixel);
	    
	    // Construire le rayon avec le bon indice de r�fraction
	    SRay ray = new SRay(position_pixel, direction, refractive_index); 

	    // �valuer la couleur attribu�e � ce rayon partant de la cam�ra et traversant un pixel du viewport
	    return shader.shade(ray);
	  }
	  
}//fin de la classe SAbstractRaytracer
