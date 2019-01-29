/**
 * 
 */
package sim.geometry.space;

import java.util.List;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;

/**
 * Classe qui repr�sente un �valuateur de dimension de voxel. � partir d'une liste de bo�tes englobantes contenant une g�om�trie, cette
 * classe propose plusieurs algorithmes �valuant une dimension aux voxels afin de r�partir dans un grillage id�al l'ensemble des bo�tes englobante.
 * Une dimension optimale permettra � un espace de voxel de faire les tests d'intersections le plus rapidement possible.
 * 
 * @author Simon V�zina
 * @since 2015-08-07
 * @version 2015-08-08
 */
public class SVoxelDimensionEvaluator {

  // Les codes de r�f�rence des diff�rents algorithmes disponibles
  public static final int ONE_FOR_ONE_ALGORITHM = 0;
  public static final int BIGGEST_AVERAGE_LENGHT_ALGORITHM = 1;
  public static final int MID_AVERAGE_LENGHT_ALGORITHM = 2;
  public static final int SMALLEST_AVERAGE_LENGHT_ALGORITHM = 3;
  
  private final double dimension;     //la dimension sugg�r�e pour les voxels
  
  /**
   * Constructeur d'un �valuateur de dimension de voxel pour espace de voxels.
   * @param list - La liste des bo�tes englobantes.
   * @param algorithm_code - Le code de r�f�rence de l'algorithme utilis� pour �valuer la dimension.
   * @throws SConstructorException Si le code de r�f�rence de l'algorithme n'est pas reconnu.
   * @throws SConstructorException Si un algorithme particulier est lanc� avec une liste de bo�te vide. 
   */
  public SVoxelDimensionEvaluator(List<SBoundingBox> list, int algorithm_code) throws SConstructorException
  {
    try{
      
      switch(algorithm_code)
      {
        case ONE_FOR_ONE_ALGORITHM :              dimension = algorithOneForOne(); break;
        
        case BIGGEST_AVERAGE_LENGHT_ALGORITHM :   dimension = algorithBiggestAverageLenght(list); break;
        
        case MID_AVERAGE_LENGHT_ALGORITHM :       dimension = algorithMidAverageLenght(list); break;
        
        case SMALLEST_AVERAGE_LENGHT_ALGORITHM :  dimension = algorithSmallestAverageLenght(list); break;
        
        default : throw new SConstructorException("Erreur SVoxelDimensionEvaluator 001 : Le code de l'algorithme '" + algorithm_code + "' n'est pas reconnu.");
      }
    }catch(SRuntimeException e){
      //Si un algorithme "crash" en raison d'une liste de bo�te vide
      throw new SConstructorException("Erreur SVoxelDimensionEvaluator 002 : L'algorithme de code '" + algorithm_code + "' a �t� lanc� avec une liste de bo�te englobante vide.");
    }
  }

  /**
   * M�thode pour obtenir la dimension des voxels sugg�r�e par l'algorithme choisi.
   * @return La dimension du voxel.
   */
  public double getDimension()
  {
    return dimension;
  }
  
  /**
   * M�thode pour �valuer la dimension du voxel comme �tant <b>unitaire</b> (�gale � 1).
   * @return La dimension unitaire du voxel. 
   */
  private double algorithOneForOne()
  {
    return 1.0;   //1 unit� voxel = 1 unit� monde
  }
  
  /**
   * M�thode pour �valuer la dimension du voxel comme �tant de largeur �gale <b>� la largeur moyenne de la plus grande bo�te</b>.
   * @param list - La liste des bo�tes englobantes.
   * @return La dimension.
   * @throws SRuntimeException S'il n'y a pas de bo�te dans la liste.
   */
  private double algorithBiggestAverageLenght(List<SBoundingBox> list) throws SRuntimeException
  {
    if(list.isEmpty())
      throw new SRuntimeException("Erreur SVoxelDimensionEvaluator 003 : La list �tant vide, il est impossible d'appliquer cet algorithme.");
    
    SBoundingBox biggest = list.get(0);
    
    for(SBoundingBox b : list)
      if(b.getAverageLenght() > biggest.getAverageLenght())
        biggest = b;
     
    return biggest.getAverageLenght();  
  }
  
  /**
   * M�thode pour �valuer la dimension du voxel comme �tant de largeur �gale <b>racine cubique du volume de la bo�te ayant le volume moyen</b>.
   * @param list - La liste des bo�tes englobantes.
   * @return La dimension.
   * @throws SRuntimeException S'il n'y a pas de bo�te dans la liste.
   */
  private double algorithMidAverageLenght(List<SBoundingBox> list) throws SRuntimeException
  {
    if(list.isEmpty())
      throw new SRuntimeException("Erreur SVoxelDimensionEvaluator 004 : La list �tant vide, il est impossible d'appliquer cet algorithme.");
   
    SBoundingBox smallest = list.get(0);
    SBoundingBox biggest = list.get(0);
    
    for(SBoundingBox b : list)
      if(b.getAverageLenght() < smallest.getAverageLenght())
        smallest = b;
      else
        if(b.getAverageLenght() > biggest.getAverageLenght())
          biggest = b;
    
    return (smallest.getAverageLenght() + biggest.getAverageLenght())/2;  
  }
  
  /**
   * M�thode pour �valuer la dimension du voxel comme �tant de largeur �gale <b>� la largeur moyenne de la plus petite bo�te</b>.
   * @param list - La liste des bo�tes englobantes.
   * @return La dimension.
   * @throws SRuntimeException S'il n'y a pas de bo�te dans la liste.
   */
  private double algorithSmallestAverageLenght(List<SBoundingBox> list) throws SRuntimeException
  {
    if(list.isEmpty())
      throw new SRuntimeException("Erreur SVoxelDimensionEvaluator 005 : La list �tant vide, il est impossible d'appliquer cet algorithme.");
    
    SBoundingBox smallest = list.get(0);
    
    for(SBoundingBox b : list)
      if(b.getAverageLenght() < smallest.getAverageLenght())
        smallest = b;
     
    return smallest.getAverageLenght();  
  }
    
}//fin de la classe SVoxelDimensionEvaluator
