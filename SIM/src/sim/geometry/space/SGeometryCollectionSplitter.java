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
 * La classe <b>SGeometryCollectionSplitter</b> permet de s�parer en sous-groupe une collection de g�om�trie.
 * Ces g�om�tries sont regroup� en fonction de la taille des <b>bo�tes englobantes</b> les recouvrant.
 * 
 * @author Simon V�zina
 * @since 2015-12-15
 * @version 2016-02-14
 */
public class SGeometryCollectionSplitter {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>DEFAULT_RECURSIVITY_LEVEL</b> repr�sente le niveau de r�cursivit� de certains algorithmes par d�faut. 
   */
  private static final int DEFAULT_RECURSIVITY_LEVEL = 1;
  
  /**
   * La constante <b>MINIMUM_GEOMETRY_PER_LIST</b> repr�sente le nombre minimal de g�om�trie pouvant �tre situ�e dans une liste.
   * Ainsi, une liste ayant moins de g�om�trie que la valeur {@value} ne devrait pas �tre scind�e.
   */
  private static final int MINIMUM_GEOMETRY_PER_LIST = 100;
  
  //Les codes de r�f�rence des diff�rents algorithmes disponibles
  public static final int SPLIT_BOX_AND_NO_BOX  = 0;
  public static final int SPLIT_HALF_AND_HALF = 1;
  public static final int SPLIT_AT_AVERAGE_SIZE = 2;
  public static final int SPLIT_AT_HALF_SIGMA = 3;
    
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>box_builder</b> correspond au constructeur de bo�te englobante des g�om�tries.
   */
  private final SBoundingBoxBuilder box_builder;
  
  /**
   * La variable <b>bounding_box_split_list</b> correspond � la liste de listes de bo�tes englobante ayant �t� s�par�es.
   * Ces bo�tes englobantes contiennent les g�om�tries qui doivent �tre organis�es spatialement. 
   */
  private final List<List<SBoundingBox>> bounding_box_split_list;
  
  /**
   * La variable <b>geometry_no_box_list</b> correspond � la liste de g�om�tries n'ayant pas de bo�te englobante.
   * Ces g�om�tries ne se retrouveront donc pas dans la liste <b>geometry_split_list</b>.
   */
  private final List<SGeometry> geometry_no_box_list;
  
  //-----------------
  // CONSTRUCTEURS //
  //-----------------
  
  /**
   * Constructeur d'un s�parateur de collection de g�om�tries � niveau de r�cursivit� par d�faut.
   * 
   * @param list - La liste de g�om�trie � s�parer.
   * @param split_code - Le code de l'algorithme de s�paration utilis� par le s�parateur.
   * @throws SConstructorException Si le code de s�paration (split_code) n'est pas valide.
   */
  public SGeometryCollectionSplitter(List<SGeometry> list, int split_code) throws SConstructorException
  {
    this(list, split_code, DEFAULT_RECURSIVITY_LEVEL);
  }

  /**
   * Constructeur d'un s�parateur de collection de g�om�tries.
   * 
    *@param list - La liste de g�om�trie � s�parer.
   * @param split_code - Le code de l'algorithme de s�paration utilis� par le s�parateur.
   * @param recursivity_level
   * @throws SConstructorException Si le code de s�paration (split_code) n'est pas valide.
   */
  public SGeometryCollectionSplitter(List<SGeometry> list, int split_code, int recursivity_level) throws SConstructorException
  {
    box_builder = new SBoundingBoxBuilder();
    bounding_box_split_list = new ArrayList<List<SBoundingBox>>();
    
    geometry_no_box_list = new ArrayList<SGeometry>();
    
    // Appliquer l'algorithme de s�paration demand�
    switch(split_code)
    {
      case SPLIT_BOX_AND_NO_BOX :       splitBoxAndNoBox(list); break;
      
      case SPLIT_HALF_AND_HALF :        splitHalfAndHalf(list); break;
      
      case SPLIT_AT_AVERAGE_SIZE :      splitAtAverageSize(list, recursivity_level); break;
      
      case SPLIT_AT_HALF_SIGMA :     splitAtHalfSigma(list, recursivity_level); break;
      
      default : throw new SConstructorException("Erreur SGeometryCollectionSplitter 001 : Le code de r�f�rence d'algorithme " + split_code + " n'est pas reconnu.");
    }
  }
  
  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir la liste des listes de g�om�tries apr�s le fractionnement de l'ensemble.
   * 
   * @return La liste des listes de g�om�tries.
   */
  public List<List<SGeometry>> getSplitList()
  {
    List<List<SGeometry>> list_of_list = new ArrayList<List<SGeometry>>();
    
    // Convertir une liste de liste de bo�tes englobante en liste de liste de g�om�tries 
    for(List<SBoundingBox> l : bounding_box_split_list)
      list_of_list.add(fillGeometryList(l));
    
    return list_of_list;
  }
  
  /**
   * M�thode pour otenir la liste des listes de bo�tes englobantes apr�s le frationnement de l'ensemble.
   * 
   * @return La liste des listes de bo�tes englobantes.
   */
  public List<List<SBoundingBox>> getBoundingBoxSplitList()
  {
    return bounding_box_split_list;
  }
  
  /**
   * M�thode pour obtenir la liste des g�om�tries n'�tant pas fractionn�es dans les ensembles
   * puisque ces g�om�tries ne poss�de pas de bo�te englobante.
   * 
   * @return La liste des g�om�tries sans bo�te englobante.
   */
  public List<SGeometry> getNoBoxList()
  {
    return geometry_no_box_list;
  }
  
  /**
   * M�thode qui effectue le filtrage des g�om�tries ne pouvant obtenir de bo�te englobante avec celle pouvant obtenir les bo�tes englobantes.
   * Cette m�thode va int�grer les g�om�tries <b>sans bo�tes</b> � la liste <b>geometry_no_box_list</b>. 
   * Les autres g�om�tries seront retourn�es sous le format d'une une liste de bo�te englobante o� un triage particulier subs�quent sera effectu� au besoin.  
   * 
   * @param list La liste des g�om�tries � filtrer.
   * @return Une liste de bo�tes englobantes.
   */
  private List<SBoundingBox> filterBoxAndNoBox(List<SGeometry> list)
  {
    List<SBoundingBox> list_box = new ArrayList<SBoundingBox>();
    
    // Remplir la liste de bo�tes englobantes et la liste des g�om�tries sans bo�tes englobantes
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
   * M�thode qui effectue la s�paration d'une liste de g�om�tries en deux cat�gories : 
   * <ul>- avec bo�te englobante</ul>
   * <ul>- sans bo�te englobante</ul>
   * 
   * @param list - La liste des g�om�tries � s�parer.
   */
  private void splitBoxAndNoBox(List<SGeometry> list)
  {
    List<SBoundingBox> list_box = filterBoxAndNoBox(list);    // obtenir la liste des bo�tes
    
    bounding_box_split_list.add(list_box);
  }
  
  /**
   * M�thode qui effectue la s�paration d'une liste de g�om�tries en deux ensembles �gaux tel que
   * les plus petites bo�tes englobantes seront dans la 1i�re liste et les plus grandes bo�tes
   * englobantes seront dans la 2i�me liste.
   * 
   * @param list - La liste des g�om�tries � s�parer.
   */
  private void splitHalfAndHalf(List<SGeometry> list)
  {
    List<SBoundingBox> list_box = filterBoxAndNoBox(list);    // obtenir la liste des bo�tes
    
    // Trier la liste des bo�tes englobantes en fonction de leur taille (selon l'impl�mentation de comparable) 
    Collections.sort(list_box);
    
    List<SBoundingBox> list_1 = new ArrayList<SBoundingBox>();
    List<SBoundingBox> list_2 = new ArrayList<SBoundingBox>();
    
    int middle = list_box.size() / 2;
    
    // Remplir la 1i�re liste
    for(int i = 0; i < middle; i++)
      list_1.add(list_box.get(i));
    
    // Remplir la 2i�me liste
    for(int i = middle; i < list_box.size(); i++)
      list_2.add(list_box.get(i));
        
    bounding_box_split_list.add(list_1);
    bounding_box_split_list.add(list_2);
  }
    
  /**
   * M�thode pour s�parer une liste de g�om�tries en deux sous-liste � la valeur moyenne de la taille maximale des bo�tes englobante.
   * Si le niveau de r�cursivit� est sup�rieur � 2, les deux sous-liste seront � nouveau s�par�es par le m�me proc�d�.
   * 
   * @param list - La liste des g�om�tries � s�parer.
   * @param level - Le niveau de r�cursivit� que la liste doit �tre s�par� en deux.
   */
  private void splitAtAverageSize(List<SGeometry> list, int level)
  {
    // Obtenir la liste de bo�te englobante
    List<SBoundingBox> list_box = filterBoxAndNoBox(list);
      
    // S�parer la liste des g�om�tries situ�es dans la liste des bo�tes
    if(!list_box.isEmpty())
      splitBoxListAtAverageSize(list_box, level);      
  }
  
  /**
   * M�thode pour s�parer une liste de g�om�tries en deux sous-liste � la valeur moyenne de la taille maximale des bo�tes englobante.
   * Si le niveau de r�cursivit� est sup�rieur � 2, les deux sous-liste seront � nouveau s�par�es par le m�me proc�d�.
   * 
   * @param list_box - La liste des bo�tes contenant des g�om�tries � s�parer.
   * @param level - Le niveau de r�cursivit� que la liste doit �tre s�par� en deux.
   */
  private void splitBoxListAtAverageSize(List<SBoundingBox> list_box, int level)
  {
    // S�parer la liste si :
    // - le niveau de r�cursivit� est sup�rieur � 1 
    // - Si le nombre de bo�tes dans la liste est sup�rieur � un seuil minimal
    if(level > 1 && list_box.size() >  MINIMUM_GEOMETRY_PER_LIST)
    {
      // �valuer la taille moyenne des bo�tes
      double average = SStatistic.average(list_box);
      
      // S�parer en deux listes : Inf�rieur � la moyenne et sup�rieur � la moyenne
      List<SBoundingBox> list_down = new ArrayList<SBoundingBox>();
      List<SBoundingBox> list_up = new ArrayList<SBoundingBox>();
      
      for(SBoundingBox b : list_box)
        if(b.getStatisticalValue() < average)
          list_down.add(b);
        else
          list_up.add(b);
      
      // Faire � nouveau une s�paration avec les deux nouvelles listes
      splitBoxListAtAverageSize(list_down, level-1);
      splitBoxListAtAverageSize(list_up, level-1);
    }
    else
    {
      // Mettre les bo�tes contenues dans la liste dans la liste des listes de bo�tes
      bounding_box_split_list.add(list_box);
    }
  }
    
  /**
   * M�thode pour s�parer une liste de g�om�tries en trois sous-liste en utilisant la <b>moyenne</b> et <b>l'�cart-type</b>.
   * Si le niveau de r�cursivit� est sup�rieur � 2, les sous-liste seront � nouveau s�par�es par le m�me proc�d�.
   * 
   * @param list - La liste des g�om�tries � s�parer.
   * @param level - Le niveau de r�cursivit� que la liste doit �tre s�par� en deux.
   */
  private void splitAtHalfSigma(List<SGeometry> list, int level)
  {
    // Obtenir la liste de bo�te englobante
    List<SBoundingBox> list_box = filterBoxAndNoBox(list);
      
    // S�parer la liste des g�om�tries situ�es dans la liste des bo�tes
    if(!list_box.isEmpty())
      splitBoxListAtHalfSigma(list_box, level);      
  }
  
  /**
   * M�thode pour s�parer une liste de g�om�tries en trois sous-liste en utilisant la <b>moyenne</b> et <b>l'�cart-type</b>.
   * Si le niveau de r�cursivit� est sup�rieur � 2, les sous-liste seront � nouveau s�par�es par le m�me proc�d�.
   * 
   * @param list_box - La liste des bo�tes contenant les g�om�tries � s�parer.
   * @param level - Le niveau de r�cursivit� que la liste doit �tre s�par� en deux.
   */
  private void splitBoxListAtHalfSigma(List<SBoundingBox> list_box, int level)
  {
    // S�parer la liste si :
    // - le niveau de r�cursivit� est sup�rieur � 1 
    // - Si le nombre de bo�tes dans la liste est sup�rieur � un seuil minimal
    if(level > 1 && list_box.size() >  MINIMUM_GEOMETRY_PER_LIST)
    {
      // �valuer les param�tres statistiques
      double average = SStatistic.average(list_box);
      double sigma = SStatistic.standardDeviation(list_box);
      
      double min_value = average - sigma/2.0;
      double max_value = average + sigma/2.0;
      
      // S�parer en trois listes : 
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
      
      // Faire � nouveau une s�paration avec les deux listes down et up
      splitBoxListAtHalfSigma(list_down, level-1);
      splitBoxListAtHalfSigma(list_up, level-1);
      
      // Mettre les bo�tes contenues dans la liste du centre dans la liste des listes de bo�tes
      bounding_box_split_list.add(list_center);
    }
    else
    {
      // Mettre les bo�tes contenues dans la liste dans la liste des listes de bo�tes
      bounding_box_split_list.add(list_box);
    }
  }
  
  /**
   * M�thode qui fait la construction d'une liste de g�om�tries � partir d'une liste de bo�tes englobantes
   * o� une r�f�rence � une g�om�trie s'y retrouve.
   * 
   * @param list - La liste des bo�tes englobantes.
   * @return La liste des g�om�tries.
   */
  private List<SGeometry> fillGeometryList(List<SBoundingBox> list)
  {
    List<SGeometry> geometry_list = new ArrayList<SGeometry>();
    
    for(SBoundingBox b : list)
      geometry_list.add(b.getGeometry());
    
    return geometry_list;
  }
  
}//fin de la classe SGeometryCollectionSplitter
