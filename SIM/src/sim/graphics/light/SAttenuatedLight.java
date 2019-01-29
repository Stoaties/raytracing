/**
 * 
 */
package sim.graphics.light;

import sim.exception.SRuntimeException;
import sim.math.SVector3d;

/**
 * L'interface <b>SAttenuatedLight</b> représentant une source de lumière qui peut s'atténuer en fonction de la distance entre la source de lumière et la position éclairée.
 * 
 * @author Simon Vézina
 * @since 2015-02-06
 * @version 2016-02-28
 */
public interface SAttenuatedLight extends SLight {

	/**
	 * Méthode pour obtenir la position de la source de lumière.
	 * 
	 * @return La position de la source de lumière.
	 */
	public SVector3d getPosition();
	
	/**
	 * Méthode pour obtenir l'orientation de la source de lumière en fonction de la position du point à illuminer.
	 * 
	 * @param position_to_illuminate - La position à illuminer
	 * @return L'orientation de la source de lumière (unitaire).
	 * @throws SRuntimeException Si la position à illuminer est située sur la source de lumière.
	 */
	public SVector3d getOrientation(SVector3d position_to_illuminate) throws SRuntimeException;
	
	/**
	 * <p>
	 * Méthode pour obtenir le facteur d'atténuation A qui dépend de la distance d 
	 * entre le point à éclairer et la source de lumière. La formule utilisée est 
	 * <ul> A = 1 / ( Ccst + Clin*d + Cquad*d*d ).</ul>
	 * Si d > 1, alors A < 1.
	 * </p>
	 * <p>
	 * Cependant, si la <b>distance d est inférieure à 1</b>, le facteur d'atténuation A peut être
	 * supérieur à 1.0 (A > 1) ce qui donnera une intensité accrue à la couleur de la source de lumière.
	 * Le choix de normalisation de la couleur devra être judicieusement afin de créer l'effet visuel désiré.
	 * </p>
	 * 
	 * @param position_to_illuminate - La position à éclairer.
	 * @return Le facteur d'atténuation A à la position à éclairer.  
	 * @throws SRuntimeException Si le facteur d'atténuation A tend vers l'infini (dénominateur étant égal à zéro).
	 */
	public double attenuation(SVector3d position_to_illuminate) throws SRuntimeException;
	
	/**
	 * <p>
	 * Méthode pour obtenir le facteur d'amplification de la source de lumière.
	 * Cependant, une couleur amplifiée peut faire excéder un canal de couleur au-delà de 1.0.
	 * Dans cette situation, il sera important de bien choisir l'algorithme de normalisation des couleurs
	 * afin d'obtenir le résultat désiré .
	 * </p>
	 * 
	 * <p>
	 * En utilisant cette option dans un <i>shader</i>, cela permet d'attribuer plus d'importance à une source de lumière.
	 * </p>
	 * 
	 * @return Le facteur d'amplification de la source de lumière.
	 * @see SColor
	 */
	public double amplification();
	
}//fin interface SAttenuatedLight
