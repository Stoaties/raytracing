/**
 * 
 */
package sim.graphics.material;

import sim.graphics.SColor;
import sim.math.SVectorUV;
import sim.readwrite.SWriteable;

/**
 * L'interface SMaterial repr�sente un mat�riel avec diff�rentes propri�t�s. 
 * 
 * @author Simon V�zina
 * @since 2015-01-13
 * @version 2015-10-31
 */
public interface SMaterial extends SWriteable {

	/**
	 * M�thode pour obtenir le nom du mat�riel.
	 * 
	 * @return Le nom du mat�riel.
	 */
	public String getName();
	
	/**
	 * M�thode pour obtenir la <b>couleur r�fl�chie</b> de fa�on <u>ambiante</u> par la surface du mat�riel.
	 * Cette couleur peut �tre calcul�e � partir du produit de la couleur de la surface <b><i>S</i></b> avec la constant
	 * de r�flexion ambiant <b><i>k</i>a</b> tel que <ul><b><i>S</i>a = <i>k</i>a*<i>S</i></b>.</ul>
	 * 
	 * @return La couleur de la lumi�re <b><i>S</i>a</b> r�fl�chie de fa�on ambiante par la surface.
	 */
	public SColor ambientColor();
	
	/**
   * M�thode pour obtenir la <b>couleur r�fl�chie</b> de fa�on <u>ambiante</u> par la surface du mat�riel
   * � partir d'une <b>coordonn�e <i>uv</i></b> d'une <b>texture</b>. S'il n'y a <b>pas de texture en m�moire</b> ou 
   * que cette <b>m�thode est non d�finie</b> par le mat�riel, la couleur retourn�e sera celle de la m�thode <b>ambiantColor()</b>.
   * 
   * @param uv - La coordonn�e <i>uv</i> de la texture. 
   * @return La couleur de r�flexion ambiante de la texture (si elle est disponible).
   */
  public SColor ambientColor(SVectorUV uv);
    
	/**
	 * M�thode pour obtenir la <b>couleur r�fl�chie</b> de fa�on <u>diffuse</u> par la surface du mat�riel.
	 * Cette couleur peut �tre calcul�e � partir du produit de la couleur de la surface <b><i>S</i></b> avec la constant
	 * de r�flexion diffuse <b><i>k</i>b</b> tel que <ul><b><i>S</i>d = <i>k</i>d*<i>S</i></b>.</ul>
	 * 
	 * @return La couleur de la lumi�re <b><i>S</i>d</b> r�fl�chie de fa�on diffuse par la surface.
	 */
	public SColor diffuseColor();
	
	/**
   * M�thode pour obtenir la <b>couleur r�fl�chie</b> de fa�on <u>diffuse</u> par la surface du mat�riel
   * � partir d'une <b>coordonn�e <i>uv</i></b> d'une <b>texture</b>. S'il n'y a <b>pas de texture en m�moire</b> ou 
   * que cette <b>m�thode est non d�finie</b> par le mat�riel, la couleur retourn�e sera celle de la m�thode <b>diffuseColor()</b>.
   * 
   * @param uv - La coordonn�e <i>uv</i> de la texture. 
   * @return La couleur de r�flexion diffuse de la texture (si elle est disponible).
   */
  public SColor diffuseColor(SVectorUV uv);
  
	/**
	 * M�thode pour obtenir la <b>couleur r�fl�chie</b> de fa�on <u>sp�culaire</u> par la surface du mat�riel.
	 * Cette couleur peut �tre calcul�e � partir du produit de la couleur de la surface <b><i>S</i></b> avec la constant
	 * de r�flexion sp�culaire <b><i>k</i>s</b> tel que <ul><b><i>S</i>s = <i>k</i>s*<i>S</i></b>.</ul>
	 * 
	 * @return La couleur de la lumi�re <b><i>S</i>s</b> r�fl�chie de fa�on sp�culaire par la surface.
	 */
	public SColor specularColor();
	
	/**
   * M�thode pour obtenir la <b>couleur r�fl�chie</b> de fa�on <u>sp�culaire</u> par la surface du mat�riel
   * � partir d'une <b>coordonn�e <i>uv</i></b> d'une <b>texture</b>. S'il n'y a <b>pas de texture en m�moire</b> ou 
   * que cette <b>m�thode est non d�finie</b> par le mat�riel, la couleur retourn�e sera celle de la m�thode <b>specularColor()</b>.
   * 
   * @param uv - La coordonn�e <i>uv</i> de la texture. 
   * @return La couleur de r�flexion speculaire de la texture (si elle est disponible).
   */
  public SColor specularColor(SVectorUV uv);
  
	/**
	 * <p>M�thode pour obtenir la <b>couleur transmise</b> (filtr�e) par la surface du mat�riel. Elle permet de faire des calculs
	 * de filtrage de la lumi�re.</p> <p>Par exemple, une couleur de transparence (1,1,1) fait tout traverser, une couleur de 
	 * transparence (0.8, 0.8, 0.8) fait tout traverser avec att�nuation et une couleur de transparence (1,0,0)
	 * fait uniquement traverser la couleur rouge.</p> Cette couleur est calcul�e � partir du produit de la couleur de la surface <b><i>S</i></b> avec la constant
	 * de transmission <b><i>k</i>t</b> tel que <ul><b><i>S</i>t = <i>k</i>t*<i>S</i></b>.</ul>
	 * 
	 * @return La couleur de la lumi�re <b><i>S</i>t</b> transmise par la surface.
	 */
	public SColor transparencyColor();
	
	/**
	 * M�thode pour obtenir le niveau de brillance de la surface du mat�riel.
	 * @return le niveau de brillance du mat�riel.
	 */
	public double getShininess();
	
	/**
	 * M�thode qui d�termine si le mat�riel r�fl�chi la lumi�re comme un miroir.
	 * @return <b>true</b> si le mat�riel est r�fl�chissant et <b>false</b> sinon.
	 */
	public boolean isReflective();
	
	/**
	 * M�thode pour obtenir le niveau de r�flexion d'une source de lumi�re par la surface du mat�riel.
	 * @return le niveau de r�flexion du mat�riel (0 = 0% r�fl�chie, 1 = 100% r�fl�chie).
	 */
	public double reflectivity();
	
	/**
	 * M�thode qui d�termine si le mat�riel est transparent.
	 * @return <b>true</b> si le mat�riel est transparent et <b>false</b> sinon.
	 */
	public boolean isTransparent();
	
	/**
	 * M�thode pour obtenir le niveau de transparence de la surface du mat�riel.
	 * @return le niveau de transparence du mat�riel (0 = 0% transparent, 1 = 100% transparent).
	 */
	public double transparency();
	
	/**
	 * M�thode pour obtenir le niveau d'opacit� de la surface du mat�riel. Cette valeur correspond � l'inverse de la transparence.
	 * La somme de la transparence <b><i>k</i>t</b> avec l'opacit� <b><i>k</i>o</b> donne 1 (kt + ko = 1).
	 * @return le niveau d'opacit� du mat�riel (0 = 0% opaque, 1 = 100% opaque).
	 */
	public double opacity();
	
	/**
	 * M�thode pour obtenir l'indice de r�fraction du mat�riel.
	 * @return L'indice de r�fraction du mat�riel. Cependant, si le mat�riel est <b>opaque</b>, l'indice de r�fraction sera automatiquement �gale � <b>1.0</b>.
	 * @throws SRuntimeException Si le mat�riel n'est pas transparent.
	 */
	public double refractiveIndex();
	
	/**
	 * M�thode permettant de pr�ciser si le mat�tiel contient des textures dans sa d�finition.
	 * 
	 * @return <b>true</b> si le mat�riel est d�fini avec des textures et <b>false</b> sinon.
	 */
	public boolean asTexture();
	
}//fin interface SMaterial
