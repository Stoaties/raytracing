/**
 * 
 */
package sim.math;

import sim.exception.SNoImplementationException;

/**
 * La classe <b>SAffineTransformation</b> représente une classe pouvant réaliser des transformations affines sur des vecteurs.
 * 
 * @author Simon Vezina
 * @since 2017-08-21
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public class SAffineTransformation {

  /**
   * Méthode qui effectue la transformation d'un <i>origine</i> (vecteur position désignant l'origine d'une système d'axe) en 3D
   * à l'aide d'une transformation linéaire définit à l'aide d'une matrice 4x4. 
   * Un vecteur 4d représentant un origine 3d correspond au vecteur (0,0,0,1). 
   * 
   * @param transformation La matrice de transformation.
   * @return Un vecteur origine transformé à l'aide d'une matrice de transformation.
   */
  public static SVector3d transformOrigin(SMatrix4x4 transformation)
  {
    throw new SNoImplementationException("Erreur SAffineTransformation : La méthode n'est pas implémentée.");
  }
  
  /**
   * Méthode qui effectue la transformation d'un <i>vecteur position</i> en 3D (converti en vecteur 4d où la 4ième composante est 1),
   * à l'aide d'une transformation linéaire définit à l'aide d'une matrice 4x4.  
   * 
   * @param transformation La matrice de transformation.
   * @param v Le vecteur position à transformer.
   * @return Un vecteur position transformé à l'aide d'une matrice de transformation.
   */
  public static SVector3d transformPosition(SMatrix4x4 transformation, SVector3d v)
  {
    throw new SNoImplementationException("Erreur SAffineTransformation : La méthode n'est pas implémentée.");
  }
  
  
  /**
   * Méthode qui effectue la transformation d'un <i>vecteur orientation</i> en 3D (converti en vecteur 4d où la 4ième composante est 1),
   * à l'aide d'une transformation linéaire définit à l'aide d'une matrice 4x4.  
   * 
   * @param transformation La matrice de transformation.
   * @param v Le vecteur orientation à transformer.
   * @return Un vecteur orientation transformé à l'aide d'une matrice de transformation.
   */
  public static SVector3d transformOrientation(SMatrix4x4 transformation, SVector3d v)
  {
    throw new SNoImplementationException("Erreur SAffineTransformation : La méthode n'est pas implémentée.");
  }
  
  /**
   * Méthode qui effectue la transformation d'un <i>vecteur orientation</i> en 3D (converti en vecteur 4d où la 4ième composante est 1),
   * à l'aide d'une transformation linéaire définit à l'aide d'une matrice 4x4. 
   * Puisque cette transformation nécessite la transformation d'un origine de référence au vecteur orientation,
   * cette version d'implémentation utilise une origine transformée calculée à l'extérieur de l'appel de cette méthode. 
   * Un usage répétitif de cette transformation avec le même origine transformée
   * permettra d'optenir un économie d'allocation de mémoire et une réduction de calcul répétitif.
   * 
   * @param transformation La matrice de transformation.
   * @param transformed_origin L'origine transformée du vecteur orientation.
   * @param v Le vecteur orientation à transformer.
   * @return Un vecteur orientation transformé à l'aide d'une matrice de transformation.
   */
  public static SVector3d transformOrientation(SMatrix4x4 transformation, SVector3d transformed_origin, SVector3d v)
  {
    throw new SNoImplementationException("Erreur SAffineTransformation : La méthode n'est pas implémentée.");
  }
  
  /**
   * Méthode qui effectue la transformation d'un <i>vecteur normale</i> à l'aide d'une transformation linéaire.  
   *  
   * @param transformation La matrice de transformation.
   * @param v Le vecteur normale à transformer.
   * @return Un vecteur normale transformé à l'aide d'une matrice de transformation.
   */
  public static SVector3d transformNormal(SMatrix4x4 transformation, SVector3d v)
  {
	throw new SNoImplementationException("Erreur SAffineTransformation : La méthode n'est pas implémentée.");
   
  }
  
  /**
   * Méthode qui effectue la transformation d'un <i>vecteur normale</i> à l'aide d'une transformation linéaire. 
   * Puisque cette transformation nécessite la transformation d'un origine de référence au vecteur orientation,
   * cette version d'implémentation utilise une origine transformée calculée à l'extérieur de l'appel de cette méthode. 
   * Un usage répétitif de cette transformation avec le même origine transformée
   * permettra d'optenir un économie d'allocation de mémoire et une réduction de calcul répétitif.
   * 
   * @param transformation La matrice de transformation.
   * @param transformed_origin L'origine transformée du vecteur orientation.
   * @param v Le vecteur normale à transformer.
   * @return Un vecteur normale transformé à l'aide d'une matrice de transformation.
   */
  public static SVector3d transformNormal(SMatrix4x4 transformation, SVector3d transformed_origin, SVector3d v) 
  {
    throw new SNoImplementationException("Erreur SAffineTransformation : La méthode n'est pas implémentée.");
  }
  
}// fin de la classe SLinearTransformation
