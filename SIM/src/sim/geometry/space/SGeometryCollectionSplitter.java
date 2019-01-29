/**
 * 
 */
package sim.geometry.space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sim.exception.SConstructorException;
import sim.geometry.SGeometry;
import sim.math.SStatistic;

/**
 * La classe <b>SGeometryCollectionSplitter</b> permet de séparer en sous-groupe une collection de géométrie.
 * Ces géométries sont regroupé en fonction de la taille des <b>boîtes englobantes</b> les recouvrant.
 * 
 * @author Simon Vézina
 * @since 2015-12-15
 * @version 2016-02-14
 */
public class SGeometryCollectionSplitter {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>DEFAULT_RECURSIVITY_LEVEL</b> représente le niveau de récursivité de certains algorithmes par défaut. 
   */
  private static final int DEFAULT_RECURSIVITY_LEVEL = 1;
  
  /**
   * La constante <b>MINIMUM_GEOMETRY_PER_LIST</b> représente le nombre minimal de géométrie pouvant être située dans une liste.
   * Ainsi, une liste ayant moins de géométrie que la valeur {@value} ne devrait pas être scindée.
   */
  private static final int MINIMUM_GEOMETRY_PER_LIST = 100;
  
  //Les codes de référence des différents algorithmes disponibles
  public static final int SPLIT_BOX_AND_NO_BOX  = 0;
  public static final int SPLIT_HALF_AND_HALF = 1;
  public static final int SPLIT_AT_AVERAGE_SIZE = 2;
  public static final int SPLIT_AT_HALF_SIGMA = 3;
    
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>box_builder</b> correspond au constructeur de boîte englobante des géométries.
   */
  private final SBoundingBoxBuilder box_builder;
  
  /**
   * La variable <b>bounding_box_split_list</b> correspond à la liste de listes de boîtes englobante ayant été séparées.
   * Ces boîtes englobantes contiennent les géométries qui doivent être organisées spatialement. 
   */
  private final List<List<SBoundingBox>> bounding_box_split_list;
  
  /**
   * La variable <b>geometry_no_box_list</b> correspond à la liste de géométries n'ayant pas de boîte englobante.
   * Ces géométries ne se retrouveront donc pas dans la liste <b>geometry_split_list</b>.
   */
  private final List<SGeometry> geometry_no_box_list;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * Constructeur d'un séparateur de collection de géométries à niveau de récursivité par défaut.
   * 
   * @param list - La liste de géométrie à séparer.
   * @param split_code - Le code de l'algorithme de séparation utilisé par le séparateur.
   * @throws SConstructorException Si le code de séparation (split_code) n'est pas valide.
   */
  public SGeometryCollectionSplitter(List<SGeometry> list, int split_code) throws SConstructorException
  {
    this(list, split_code, DEFAULT_RECURSIVITY_LEVEL);
  }

  /**
   * Constructeur d'un séparateur de collection de géométries.
   * 
    *@param list - La liste de géométrie à séparer.
   * @param split_code - Le code de l'algorithme de séparation utilisé par le séparateur.
   * @param recursivity_level
   * @throws SConstructorException Si le code de séparation (split_code) n'est pas valide.
   */
  public SGeometryCollectionSplitter(List<SGeometry> list, int split_code, int recursivity_level) throws SConstructorException
  {
    box_builder = new SBoundingBoxBuilder();
    bounding_box_split_list = new ArrayList<List<SBoundingBox>>();
    
    geometry_no_box_list = new ArrayList<SGeometry>();
    
    // Appliquer l'algorithme de séparation demandé
    switch(split_code)
    {
      case SPLIT_BOX_AND_NO_BOX :       splitBoxAndNoBox(list); break;
      
      case SPLIT_HALF_AND_HALF :        splitHalfAndHalf(list); break;
      
      case SPLIT_AT_AVERAGE_SIZE :      splitAtAverageSize(list, recursivity_level); break;
      
      case SPLIT_AT_HALF_SIGMA :     splitAtHalfSigma(list, recursivity_level); break;
      
      default : throw new SConstructorException("Erreur SGeometryCollectionSplitter 001 : Le code de référence d'algorithme " + split_code + " n'est pas reconnu.");
    }
  }
  
  //------------
  // MÉTHODES //
  //------------
  
  /**
   * Méthode pour obtenir la liste des listes de géométries après le fractionnement de l'ensemble.
   * 
   * @return La liste des listes de géométries.
   */
  public List<List<SGeometry>> getSplitList()
  {
    List<List<SGeometry>> list_of_list = new ArrayList<List<SGeometry>>();
    
    // Convertir une liste de liste de boîtes englobante en liste de liste de géométries 
    for(List<SBoundingBox> l : bounding_box_split_list)
      list_of_list.add(fillGeometryList(l));
    
    return list_of_list;
  }
  
  /**
   * Méthode pour otenir la liste des listes de boîtes englobantes après le frationnement de l'ensemble.
   * 
   * @return La liste des listes de boîtes englobantes.
   */
  public List<List<SBoundingBox>> getBoundingBoxSplitList()
  {
    return bounding_box_split_list;
  }
  
  /**
   * Méthode pour obtenir la liste des géométries n'étant pas fractionnées dans les ensembles
   * puisque ces géométries ne possède pas de boîte englobante.
   * 
   * @return La liste des géométries sans boîte englobante.
   */
  public List<SGeometry> getNoBoxList()
  {
    return geometry_no_box_list;
  }
  
  /**
   * Méthode qui effectue le filtrage des géométries ne pouvant obtenir de boîte englobante avec celle pouvant obtenir les boîtes englobantes.
   * Cette méthode va intégrer les géométries <b>sans boîtes</b> à la liste <b>geometry_no_box_list</b>. 
   * Les autres géométries seront retournées sous le format d'une une liste de boîte englobante où un triage particulier subséquent sera effectué au besoin.  
   * 
   * @param list La liste des géométries à filtrer.
   * @return Une liste de boîtes englobantes.
   */
  private List<SBoundingBox> filterBoxAndNoBox(List<SGeometry> list)
  {
    List<SBoundingBox> list_box = new ArrayList<SBoundingBox>();
    
    // Remplir la liste de boîtes englobantes et la liste des géométries sans boîtes englobantes
    for(SGeometry g : list)
    {
      SBoundingBox box = box_builder.buildBoundingBox(g);
      
      if(box != null)
        list_box.add(box);
      else
        geometry_no_box_list.add(g);
    }
    
    return list_box;
  }
  
  /**
   * Méthode qui effectue la séparation d'une liste de géométries en deux catégories : 
   * <ul>- avec boîte englobante</ul>
   * <ul>- sans boîte englobante</ul>
   * 
   * @param list - La liste des géométries à séparer.
   */
  private void splitBoxAndNoBox(List<SGeometry> list)
  {
    List<SBoundingBox> list_box = filterBoxAndNoBox(list);    // obtenir la liste des boîtes
    
    bounding_box_split_list.add(list_box);
  }
  
  /**
   * Méthode qui effectue la séparation d'une liste de géométries en deux ensembles égaux tel que
   * les plus petites boîtes englobantes seront dans la 1ière liste et les plus grandes boîtes
   * englobantes seront dans la 2ième liste.
   * 
   * @param list - La liste des géométries à séparer.
   */
  private void splitHalfAndHalf(List<SGeometry> list)
  {
    List<SBoundingBox> list_box = filterBoxAndNoBox(list);    // obtenir la liste des boîtes
    
    // Trier la liste des boîtes englobantes en fonction de leur taille (selon l'implémentation de comparable) 
    Collections.sort(list_box);
    
    List<SBoundingBox> list_1 = new ArrayList<SBoundingBox>();
    List<SBoundingBox> list_2 = new ArrayList<SBoundingBox>();
    
    int middle = list_box.size() / 2;
    
    // Remplir la 1ière liste
    for(int i = 0; i < middle; i++)
      list_1.add(list_box.get(i));
    
    // Remplir la 2ième liste
    for(int i = middle; i < list_box.size(); i++)
      list_2.add(list_box.get(i));
        
    bounding_box_split_list.add(list_1);
    bounding_box_split_list.add(list_2);
  }
    
  /**
   * Méthode pour séparer une liste de géométries en deux sous-liste à la valeur moyenne de la taille maximale des boîtes englobante.
   * Si le niveau de récursivité est supérieur à 2, les deux sous-liste seront à nouveau séparées par le même procédé.
   * 
   * @param list - La liste des géométries à séparer.
   * @param level - Le niveau de récursivité que la liste doit être séparé en deux.
   */
  private void splitAtAverageSize(List<SGeometry> list, int level)
  {
    // Obtenir la liste de boîte englobante
    List<SBoundingBox> list_box = filterBoxAndNoBox(list);
      
    // Séparer la liste des géométries situées dans la liste des boîtes
    if(!list_box.isEmpty())
      splitBoxListAtAverageSize(list_box, level);      
  }
  
  /**
   * Méthode pour séparer une liste de géométries en deux sous-liste à la valeur moyenne de la taille maximale des boîtes englobante.
   * Si le niveau de récursivité est supérieur à 2, les deux sous-liste seront à nouveau séparées par le même procédé.
   * 
   * @param list_box - La liste des boîtes contenant des géométries à séparer.
   * @param level - Le niveau de récursivité que la liste doit être séparé en deux.
   */
  private void splitBoxListAtAverageSize(List<SBoundingBox> list_box, int level)
  {
    // Séparer la liste si :
    // - le niveau de récursivité est supérieur à 1 
    // - Si le nombre de boîtes dans la liste est supérieur à un seuil minimal
    if(level > 1 && list_box.size() >  MINIMUM_GEOMETRY_PER_LIST)
    {
      // Évaluer la taille moyenne des boîtes
      double average = SStatistic.average(list_box);
      
      // Séparer en deux listes : Inférieur à la moyenne et supérieur à la moyenne
      List<SBoundingBox> list_down = new ArrayList<SBoundingBox>();
      List<SBoundingBox> list_up = new ArrayList<SBoundingBox>();
      
      for(SBoundingBox b : list_box)
        if(b.getStatisticalValue() < average)
          list_down.add(b);
        else
          list_up.add(b);
      
      // Faire à nouveau une séparation avec les deux nouvelles listes
      splitBoxListAtAverageSize(list_down, level-1);
      splitBoxListAtAverageSize(list_up, level-1);
    }
    else
    {
      // Mettre les boîtes contenues dans la liste dans la liste des listes de boîtes
      bounding_box_split_list.add(list_box);
    }
  }
    
  /**
   * Méthode pour séparer une liste de géométries en trois sous-liste en utilisant la <b>moyenne</b> et <b>l'écart-type</b>.
   * Si le niveau de récursivité est supérieur à 2, les sous-liste seront à nouveau séparées par le même procédé.
   * 
   * @param list - La liste des géométries à séparer.
   * @param level - Le niveau de récursivité que la liste doit être séparé en deux.
   */
  private void splitAtHalfSigma(List<SGeometry> list, int level)
  {
    // Obtenir la liste de boîte englobante
    List<SBoundingBox> list_box = filterBoxAndNoBox(list);
      
    // Séparer la liste des géométries situées dans la liste des boîtes
    if(!list_box.isEmpty())
      splitBoxListAtHalfSigma(list_box, level);      
  }
  
  /**
   * Méthode pour séparer une liste de géométries en trois sous-liste en utilisant la <b>moyenne</b> et <b>l'écart-type</b>.
   * Si le niveau de récursivité est supérieur à 2, les sous-liste seront à nouveau séparées par le même procédé.
   * 
   * @param list_box - La liste des boîtes contenant les géométries à séparer.
   * @param level - Le niveau de récursivité que la liste doit être séparé en deux.
   */
  private void splitBoxListAtHalfSigma(List<SBoundingBox> list_box, int level)
  {
    // Séparer la liste si :
    // - le niveau de récursivité est supérieur à 1 
    // - Si le nombre de boîtes dans la liste est supérieur à un seuil minimal
    if(level > 1 && list_box.size() >  MINIMUM_GEOMETRY_PER_LIST)
    {
      // Évaluer les paramètres statistiques
      double average = SStatistic.average(list_box);
      double sigma = SStatistic.standardDeviation(list_box);
      
      double min_value = average - sigma/2.0;
      double max_value = average + sigma/2.0;
      
      // Séparer en trois listes : 
      List<SBoundingBox> list_down = new ArrayList<SBoundingBox>();
      List<SBoundingBox> list_center = new ArrayList<SBoundingBox>();
      List<SBoundingBox> list_up = new ArrayList<SBoundingBox>();
      
      for(SBoundingBox b : list_box)
        if(b.getStatisticalValue() < min_value)
          list_down.add(b);
        else
          if(b.getStatisticalValue() > max_value)
            list_up.add(b);
          else
            list_center.add(b);
      
      // Faire à nouveau une séparation avec les deux listes down et up
      splitBoxListAtHalfSigma(list_down, level-1);
      splitBoxListAtHalfSigma(list_up, level-1);
      
      // Mettre les boîtes contenues dans la liste du centre dans la liste des listes de boîtes
      bounding_box_split_list.add(list_center);
    }
    else
    {
      // Mettre les boîtes contenues dans la liste dans la liste des listes de boîtes
      bounding_box_split_list.add(list_box);
    }
  }
  
  /**
   * Méthode qui fait la construction d'une liste de géométries à partir d'une liste de boîtes englobantes
   * où une référence à une géométrie s'y retrouve.
   * 
   * @param list - La liste des boîtes englobantes.
   * @return La liste des géométries.
   */
  private List<SGeometry> fillGeometryList(List<SBoundingBox> list)
  {
    List<SGeometry> geometry_list = new ArrayList<SGeometry>();
    
    for(SBoundingBox b : list)
      geometry_list.add(b.getGeometry());
    
    return geometry_list;
  }
  
}//fin de la classe SGeometryCollectionSplitter
