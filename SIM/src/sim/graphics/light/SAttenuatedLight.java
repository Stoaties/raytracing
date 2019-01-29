/**
 * 
 */
package sim.graphics.light;

import sim.exception.SRuntimeException;
import sim.math.SVector3d;

/**
 * L'interface <b>SAttenuatedLight</b> repr�sentant une source de lumi�re qui peut s'att�nuer en fonction de la distance entre la source de lumi�re et la position �clair�e.
 * 
 * @author Simon V�zina
 * @since 2015-02-06
 * @version 2016-02-28
 */
public interface SAttenuatedLight extends SLight {

	/**
	 * M�thode pour obtenir la position de la source de lumi�re.
	 * 
	 * @return La position de la source de lumi�re.
	 */
	public SVector3d getPosition();
	
	/**
	 * M�thode pour obtenir l'orientation de la source de lumi�re en fonction de la position du point � illuminer.
	 * 
	 * @param position_to_illuminate - La position � illuminer
	 * @return L'orientation de la source de lumi�re (unitaire).
	 * @throws SRuntimeException Si la position � illuminer est situ�e sur la source de lumi�re.
	 */
	public SVector3d getOrientation(SVector3d position_to_illuminate) throws SRuntimeException;
	
	/**
	 * <p>
	 * M�thode pour obtenir le facteur d'att�nuation A qui d�pend de la distance d 
	 * entre le point � �clairer et la source de lumi�re. La formule utilis�e est 
	 * <ul> A = 1 / ( Ccst + Clin*d + Cquad*d*d ).</ul>
	 * Si d > 1, alors A < 1.
	 * </p>
	 * <p>
	 * Cependant, si la <b>distance d est inf�rieure � 1</b>, le facteur d'att�nuation A peut �tre
	 * sup�rieur � 1.0 (A > 1) ce qui donnera une intensit� accrue � la couleur de la source de lumi�re.
	 * Le choix de normalisation de la couleur devra �tre judicieusement afin de cr�er l'effet visuel d�sir�.
	 * </p>
	 * 
	 * @param position_to_illuminate - La position � �clairer.
	 * @return Le facteur d'att�nuation A � la position � �clairer.  
	 * @throws SRuntimeException Si le facteur d'att�nuation A tend vers l'infini (d�nominateur �tant �gal � z�ro).
	 */
	public double attenuation(SVector3d position_to_illuminate) throws SRuntimeException;
	
	/**
	 * <p>
	 * M�thode pour obtenir le facteur d'amplification de la source de lumi�re.
	 * Cependant, une couleur amplifi�e peut faire exc�der un canal de couleur au-del� de 1.0.
	 * Dans cette situation, il sera important de bien choisir l'algorithme de normalisation des couleurs
	 * afin d'obtenir le r�sultat d�sir� .
	 * </p>
	 * 
	 * <p>
	 * En utilisant cette option dans un <i>shader</i>, cela permet d'attribuer plus d'importance � une source de lumi�re.
	 * </p>
	 * 
	 * @return Le facteur d'amplification de la source de lumi�re.
	 * @see SColor
	 */
	public double amplification();
	
}//fin interface SAttenuatedLight
