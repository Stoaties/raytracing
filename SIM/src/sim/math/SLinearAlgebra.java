/**
 * 
 */
package sim.math;

import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;

/**
 * La classe <b>SLinearAlgebra</b> représente une classe utilitaire pour effectuer du calcul en lien avec l'algèbre linéaire.
 * 
 * @author Simon Vézina
 * @since 2016-12-15
 * @version 2017-12-20 (version labo – Le ray tracer v2.1)
 */
public class SLinearAlgebra {

  /**
   * La constante <b>EPSILON</b> correspond à une valeur numérique de référence près de zéro.
   * Pour des raisons numériques, cette valeur doit être légèrement supérieure à celle proposée dans la classe <b>SMath</b>.
   * @see SMath
   */
  private static final double EPSILON = SMath.EPSILON*2.0;
  
  /**
   * <p>
   * Méthode permettant d'évaluer la normale à la surface d'un plan formé à l'aide de trois points. 
   * L'orientation de la normale est déterminé par la <u>règle de la main droite</u> en lien avec l'ordre des trois points du plan.
   * Le module de la normale dépend de la distance entre les points ainsi que de leur positionnement angulaire (en lien avec le sin(theta) dans le produit vectoriel).
   * </p>
   * <p>
   * Si les trois points sont dits <u>colinéaire</u>, la normale à la surface aura une orientation indéterminée de module égale à zéro.
   * </p>
   * 
   * @param r0 Le 1ier point du plan.
   * @param r1 Le 2ième point du plan.
   * @param r2 Le 3ième point du plan.
   * @return La normale à la surface du plan.
   */
  public static SVector3d planNormal(SVector3d r0, SVector3d r1, SVector3d r2) 
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
  /**
   * <p>
   * Méthode permettant d'évaluer la normale à la surface d'un plan formé à l'aide de trois points. 
   * L'orientation de la normale est déterminé par la <u>règle de la main droite</u> en lien avec l'ordre des trois points du plan.
   * </p>
   * <p>
   * La normale retournée sera <u>normalisée</u>.
   * </p>
   * 
   * @param r0 Le 1ier point du plan.
   * @param r1 Le 2ièem point du plan.
   * @param r2 Le 3ième point du plan.
   * @return La normale à la surface du plan <b>normalisé</b>.
   * @throws SColinearException Si les trois points sont colinéaires.
   */
  public static SVector3d normalizedPlanNormal(SVector3d r0, SVector3d r1, SVector3d r2) throws SColinearException
  {    
    // Évaluer la normale à la surface d'un plan et normaliser celle-ci (la rendre de taille égale à 1).
    try{
      return planNormal(r0, r1, r2).normalize();
    }catch(SImpossibleNormalizationException e){
      throw new SColinearException("Erreur SLinearAlgebra 001 : La normale d'un plan composé des points r0 = " + r0 + ", r1 = " + r1 + ", r2 = " + r2 + " est indéterminé, car ces points sont colinéaires.", e);   
    }
  }
  
  /**
   * Méthode permettant de déterminer si quatre points p0, p1, p2 et p3 sont coplanaires (ils sont tous situés dans le même plan).
   *
   * @param r0 Le vecteur position du point p0.
   * @param r1 Le vecteur position du point p1.
   * @param r2 Le vecteur position du point p2.
   * @param r3 Le vecteur position du point p3.
   * @return <b>true</b> si les quatre points sont coplanaires et <b>false</b> sinon.
   */
  public static boolean isCoplanar(SVector3d r0, SVector3d r1, SVector3d r2, SVector3d r3)
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }

  /**
   * Méthode permettant de déterminer si trois points p0, p1 et p2 sont colinéaire (ils sont tous alignés sur la même droite).
   * 
   * @param r0 Le vecteur position du point p0.
   * @param r1 Le vecteur position du point p1.
   * @param r2 Le vecteur position du point p2.
   * @return <b>true</b> si les trois points sont colinéaire et <b>false</b> sinon.
   */
  public static boolean isCollinear(SVector3d r0, SVector3d r1, SVector3d r2)
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
  /**
   * <p>
   * Méthode permettant d'obtenir les coordonnées barycentriques d'une vecteur r associé à une projection dans le plan d'un triangle de coordonnées P0, P1 et P2.
   * </p>
   * <p>
   * Les coordonnées (t1, t2) obtenues correspondent à la projection du vecteur r dans le plan du triangle :
   * <ul>- La coordonnée (0,0) est associée à une projection sur le point P0.</ul>
   * <ul>- La coordonnée (1,0) est associée à une projection sur le point P1.</ul>
   * <ul>- La coordonnée (0,1) est associée à une projection sur le point P2.</ul>
   * <ul>- La coordonnée (t1,t2) où t1 > 0, t2 > 0 et t1 + t2 < 1 est associée à une projection à <u>l'intérieur du triangle</u>.</ul>
   * <ul>- La coordonnée (t1,t2) où t1 > 0, t2 > 0 ou t1 + t2 < 1 n'est pas respectée est associée à une projection à <u>l'extérieur du triangle</u>.</ul>
   * </p>
   * @param P0 Le 1ier point du triangle.
   * @param P1 Le 2ième point du triangle.
   * @param P2 Le 3ième point du triangle.
   * @param r Le vecteur à projeter dans le triangle afin d'y obtenir les coordonnées barycentriques.
   * @return La coordonnée barycentrique (t1, t2) de la projection du vecteur r dans le triangle où tab[0] = t1 et tab[1] = t2.
   */
  public static double[] triangleBarycentricCoordinates(SVector3d P0, SVector3d P1, SVector3d P2, SVector3d r)
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
  /**
   * <p>
   * Méthode permettant d'obtenir les coordonnées barycentriques d'une vecteur r associé à une projection dans le plan d'un triangle de coordonnées P0, P1 et P2.
   * </p> 
   * <p>
   * Cette version de l'implémentation permet d'effectuer le calcul en réduisant l'allocation de mémoire.
   * </p>
   * @param P0 Le 1ier point du triangle.
   * @param s1 Le segment du triangle P0 vers P1.
   * @param s2 Le segment du triangle P0 vers P2.
   * @param s1Dots1 Le produit scalaire entre s1 et s1.
   * @param s2Dots2 Le produit sclaire entre s2 et s2.
   * @param s1Dots2 Le produit scalaire entre s1 et s1.
   * @param denominator Le dénominateur du calcul étant S1dotS1*S2dotS2 - S1dotS2*S1dotS2.
   * @param r Le vecteur à projeter dans le triangle afin d'y obtenir les coordonnées barycentriques. 
   * @return La coordonnée barycentrique (t1, t2) de la projection du vecteur r dans le triangle où tab[0] = t1 et tab[1] = t2.
   */
  public static double[] triangleBarycentricCoordinates(SVector3d P0, SVector3d S1, SVector3d S2, double S1dotS1, double S2dotS2, double S1dotS2, double denominator, SVector3d r)
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
  /**
   * Méthode pour déterminer si un couple de coordonnée baricentrique de triangle correspond à un point à l'intérieur du triangle.
   * 
   * @param b_coord Le tableau contenant les deux coordonnées barycentriques à analyser.
   * @return <b>true</b> si les coordonnées barycentriques correspondent à être à l'intérieur du triangle et <b>false</b> sinon.
   * @throws SRuntimeException Si le tableau ne contient pas exactement deux valeurs.
   */
  public static boolean isBarycentricCoordinatesInsideTriangle(double[] b_coord) throws SRuntimeException
  {
    // Vérification de la taille du tableau.
    if(b_coord.length != 2)
      throw new SRuntimeException("Erreur SLinearAlgebra 002 : Le tableau de coordonnée barycentrique d'un triangle contient " + b_coord.length + " élément ce qui n'est pas exactement égal à 2.");
    
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
  /**
   * Méthode pour évaluer la position de l'intersection de deux droites coplanaires.
   * Dans ce calcul, les deux droites sont considérées comme étant <i>infini</i>.
   * 
   * @param r0 Le vecteur position du point p0 associé à la première droite.
   * @param r1 Le vecteur position du point p1 associé à la première droite.
   * @param r2 Le vecteur position du point p2 associé à la deuxième droite.
   * @param r3 Le vecteur position du point p3 associé à la deuxième droite.
   * @throws SNoCoplanarException Si les deux droites ne sont pas coplanaires.
   * @return La position de l'intersection des deux droites coplanaires.
   */
  public static SVector3d coplanarEdgeEdgeIntersection(SVector3d r0, SVector3d r1, SVector3d r2, SVector3d r3) throws SNoCoplanarException
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
  /**
   * Méthode pour déterminer si deux droites sont en intersection. Cette condition sera satisfaite si
   * <ul>- Les deux droites sont coplanaires.</ul>
   * <ul>- Le point de l'intersection est situé entre les deux droites.</ul> 
   * 
   * @param r0 Le vecteur position du point p0 associé à la première droite.
   * @param r1 Le vecteur position du point p1 associé à la première droite.
   * @param r2 Le vecteur position du point p2 associé à la deuxième droite.
   * @param r3 Le vecteur position du point p3 associé à la deuxième droite.
   * @return <b>true</b> si les deux droites sont en intersection et <b>false</b> sinon.
   */
  public static boolean isEdgeEdgeIntersection(SVector3d r0, SVector3d r1, SVector3d r2, SVector3d r3)
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
  /**
   * Méthode pour déterminer si un point est en intersection avec un triangle. Cette condition sera satisfaite si
   * <ul>- Le point et le triangle sont coplanaires.</ul>
   * <ul>- Le point en intersection est situé dans le triangle.</ul>
   *  
   * @param r0 Le 1ier point du triangle.
   * @param r1 Le 2ième point du triangle.
   * @param r2 Le 3ième point du triangle.
   * @param r_p Le point à vérifier.
   * @return <b>true</b> si le point est en intersection avec le triangle et <b>false</b> sinon.
   */
  public static boolean isPointTriangleIntersection(SVector3d r0, SVector3d r1, SVector3d r2, SVector3d r_p)
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
  /**
   * Méthode pour obtenir le temps nécessaire afin que trois points en mouvement à vitesse constante puissent être colinéaire (être aligné sur une ligne droite).
   * S'il n'y a pas de temps admissible, la solution sera un tableau vide.
   * S'il y a une infinité de solution, une exception sera lancée.
   * 
   * @param r0 La position du 1ier point.
   * @param v0 La vitesse du 1ier point.
   * @param r1 La position de 2ième point.
   * @param v1 La vitesse du 2ième point.
   * @param r2 La position du 3ième point.
   * @param v2 La vitesse du 3ième point.
   * @return Un tableau des temps où les trois points seront colinéaire.
   * @throws SInfinityOfSolutionsException Si les trois points sont toujours colinéaire. Il y aura une infinité de temps admissible.
   */
  public static double[] timeToCollinear(SVector3d r0, SVector3d v0, SVector3d r1, SVector3d v1, SVector3d r2, SVector3d v2) throws SInfinityOfSolutionsException
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
  /**
   * Méthode permettant d'évaluer le temps requis afin que quatre points en mouvement à vitesse constante puissent être coplanaire (être situé dans le même plan).
   * Puisque la résolution de ce problème mathématique nécessite la résolution d'un polynôme du 3ième degré, il y aura toujours une solution réelle.
   * S'il y a une infinité de solution, une exception sera lancée.
   * 
   * @param r0 La position du 1ier point.
   * @param v0 La vitesse du 1ier point.
   * @param r1 La position de 2ième point.
   * @param v1 La vitesse du 2ième point.
   * @param r2 La position du 3ième point.
   * @param v2 La vitesse du 3ième point.
   * @param r3 La position du 4ième point.
   * @param v3 La vitesse du 4ième point.
   * @return Un tableau des temps où les trois points seront colinéaire.
   * @throws SInfinityOfSolutionsException Si les quatre points sont toujours coplanaires. Il y aura une infinité de temps admissible.
   */
  public static double[] timeToCoplanar(SVector3d r0, SVector3d v0, SVector3d r1, SVector3d v1, SVector3d r2, SVector3d v2, SVector3d r3, SVector3d v3) throws SInfinityOfSolutionsException
  {
    throw new SNoImplementationException("La méthode n'a pas été implémentée.");
  }
  
}//fin de la classe SLinearAlgebra
