/**
 * 
 */
package sim.geometry.space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sim.geometry.SGeometry;
import sim.geometry.SRay;
import sim.math.SVector3d;

/**
 * La classe <b>SAbstractGeometrySpace</b> repr�sente un espace � g�om�trie abstrait.
 * 
 * @author Simon V�zina
 * @since 2015-01-11
 * @version 2016-01-01
 */
public abstract class SAbstractGeometrySpace implements SGeometrySpace{

  //Mode de s�lection du type d'espace des g�om�tries
  public static final String[] TYPE_OF_SPACE = {"linear", "voxel", "multi_voxel"};
  public static final int LINEAR = 0;
  public static final int VOXEL = 1;
  public static final int MULTI_VOXEL = 2;
  
  /**
   * La variable <b>intersection_test_count</b> correspond au nombre de tests d'intersection qui ont �t� r�alis�s
   * depuis la cr�ation d'un espace des g�om�tries.
   */
  private static int intersection_test_count = 0;
  
  /**
   * La variable <b>geometry_list</b> correspond � la liste compl�te des g�om�tries dans l'espace des g�om�tries.
   */
  protected final List<SGeometry> geometry_list;
  
  /**
   * La variable <b>space_initialized</b> permet de d�finir si l'espace des voxels a �t� initialis�.
   */
  protected boolean space_initialized;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur par d�faut d'un espace de g�om�trie abstraite.
   */
  public SAbstractGeometrySpace()
  {
    geometry_list = new ArrayList<SGeometry>();
    
    space_initialized = false;
  }
  
  //------------
  // M�THODES //
  //------------
  
  @Override
  public void addGeometry(SGeometry geometry)
  {
    geometry_list.add(geometry);
    
    space_initialized = false;
  }
  
	@Override
	public void addGeometry(List<SGeometry> list)
	{
		for(SGeometry g : list)
			addGeometry(g);
	}

	/**
   * M�thode qui �value toutes les intersections entre un rayon et les diff�rentes g�om�tries d'une liste ne d�passant pas une valeur de temps maximal.
   * Les rayons ayant intersect�s seront retourn�s dans une <b>liste trier en ordre croissant de temps</b> (du plus pr�s au plus �loig�).
   *
   * @param list - La liste des g�om�tries.
   * @param ray - Le rayon � intersecter avec les g�om�tries de l'espace.
   * @param t_max - Le temps maximal.
   * @return Une liste des diff�rentes intersections avec les primitives en ordre croissant de temps. Si la <b>liste est vide</b>, il n'y a <b>pas eu d'intersection</b>.
   */
  protected List<SRay> intersections(List<SGeometry> list, SRay ray, double t_max)
  {
    // Compter le nombre de tests d'intersection qui seront effectu�s lors de l'appel de cette m�thode
    increaseIntersectionCount(list.size());
    
    // Liste des rayons intersect�s � g�n�rer
    List<SRay> return_list = new ArrayList<SRay>();  
    
    // It�ration sur l'ensemble des g�om�tries de la liste
    for(SGeometry g : list)
    {
      // �valuer l'intersection entre le rayon et la g�om�trie
      SRay result_ray = g.intersection(ray);  
          
      if(result_ray.asIntersected())    //V�rification de l'intersection
        if(result_ray.getT() < t_max)   //V�rification de ne pas d�passer t_max
          return_list.add(result_ray);
    }
        
    //Trier la list en ordre croissant de valeur t (ce que l'impl�mentation de Comparable fait pour SRay)
    Collections.sort(return_list); 
        
    return return_list;  
  }
  
  /**
   * <p>
   * M�thode pour fusionner deux listes d'intersections en une nouvelle liste.
   * La nouvelle liste des intersections sera <b>tri�e en ordre croissant de temps</b>.
   * </p>
   * 
   * <p>
   * Il est important de pr�ciser que cette m�thode ne v�rifie pas que les rayons � 
   * l'int�rieur des deux listes sont des rayons ayant r�alis�s des intersection.
   * </p>
   * 
   * @param list1 - La premi�re liste � fusionner.
   * @param list2 - La deuxi�me liste � fusionner.
   * @return La fusion des deux listes en <b>tri� en ordre croissant de temps</b>.
   */
  protected List<SRay> mergeIntersections(List<SRay> list1, List<SRay> list2)
  {
    List<SRay> return_list = new ArrayList<SRay>();  
    
    return_list.addAll(list1);
    return_list.addAll(list2);
    
    Collections.sort(return_list); 
    
    return return_list;
  }
  
  /**
   * <p>
   * M�thode qui �value l'intersection la plus pr�s entre un rayon et les diff�rentes g�om�tries <b>opaque</b> de l'espace ne d�passant pas un certain temps maximal.
   * Une liste des g�om�tries transparentes sera ordonn�e en <b>ordre inverse d'apparition</b> (ordre d�croissant du temps).
   * La <b>liste d�butera par une g�om�trie opaque</b> s'il y a eu une intersection avec une g�om�trie opaque.
   * S'il n'y a pas eu d'intersection avec une g�om�trie opaque, elle contiendra uniquement des g�om�tries transparentes.
   * </p>
   * 
   * <p>
   * Cette m�thode sera utile lors du lanc� d'un rayon d'ombre o� l'intersection avec une g�om�trie transparente
   * ne bloquera pas enti�rement la lumi�re (ombre partielle).
   * </p>
   *
   * @param list - La liste des g�om�tries.
   * @param ray - Le rayon � intersecter avec les g�om�tries de l'espace.
   * @param t_max - Le temps maximal.
   * @return Une liste des g�om�tries transparentes intersect�es en <b>ordre d�croissant</b> d�butant par une g�om�trie opaque s'il y a eu lieu. La liste sera <b>vide</b> s'il n'y a <b>aucune intersection</b> sur le temps maximal.
   */
  protected List<SRay> nearestOpaqueIntersection(List<SGeometry> list, SRay ray, final double t_max)
  {
    // Obtenir la liste compl�te des intersections tri�es en ordre croissant
    List<SRay> list_intersection = intersections(list, ray, t_max);
    
    // Iterer sur l'ensemble des intersections et conserver celle 
    // o� il y a des g�om�tries transparentes et une seule opaque s'il y en a une
    List<SRay> return_list = new ArrayList<SRay>();
    
    for(SRay r : list_intersection)
    {
      return_list.add(r);   // ajouter cette intersection
      
      // Si la g�om�trie intersect� n'est pas transparente,
      // ce sera la derni�re intersection ajout�e � la liste
      // car elle sera la seule intersection opaque de la liste.
      if(!r.getGeometry().isTransparent())
      {
        Collections.reverse(return_list);       // Inverser l'ordre en ordre d�croissant
        return return_list;                     // retourner une liste avec g�om�trie opaque
      }
    }
    
    Collections.reverse(return_list);           // Inverser l'ordre en ordre d�croissant
    return return_list;                         // retourner une liste sans g�om�trie opaque
    
    // ANCIENNE VERSION ... BEAUCOUP PLUS COMPLIQU�E !!!
    
    /*
    SRay nearest_opaque_intersection = ray;                             //l'intersection opaque la plus pr�s ... initialement, il n'y en a pas !
    
    List<SRay> list_transparent_intersection = new LinkedList<SRay>();  //liste des intersections avec g�om�trie transparente
    
    //- Conserver les intersections transparentes 
    //- Garder en gardant en m�moire l'intersection avec la g�om�trie opaque la plus pr�s
    for(SRay r : list_intersection)
      if(r.getGeometry().isTransparent())                  //Si la g�om�trie est transparente
        list_transparent_intersection.add(r);
      else                                                 //Si la g�om�trie est opaque
        if(r.getT() < nearest_opaque_intersection.getT())     //Si l'on trouve une g�om�trie opaque plus pr�s que celle trouv�e avant
          nearest_opaque_intersection = r;
    
    //Construire la liste officielle des intersections transparentes en gardant  
    //uniquement les intersections transparentes avant celle qui est opaque et  
    //mettre l'intersection opaque dans la liste s'il y en a eu une.
    //IMPORTANT : Il faut trier la liste en ordre d�croissant par la suite (la g�om�trie opaque sera la premi�re de la liste, si elle est pr�sente).
    if(nearest_opaque_intersection.asIntersected())       //S'il y a pas eu d'intersection opaque
    { 
      List<SRay> list_return = new LinkedList<SRay>();    //Nouvelle liste � retourner
      
      for(SRay r : list_transparent_intersection)
        if(r.getT() < nearest_opaque_intersection.getT()) //Ajouter les g�om�tries transparente plus pr�s que l'intersection opaque
          list_return.add(r);
      
      list_return.add(nearest_opaque_intersection);     //Ajouter l'intersection opaque
      
      Collections.sort(list_return);              //Trier dans l'ordre croissant
      Collections.reverse(list_return);           //Inverser l'ordre en ordre d�croissant
      return list_return;
    }
    else
    {
      Collections.sort(list_transparent_intersection);      //Trier dans l'ordre croissant
      Collections.reverse(list_transparent_intersection);   //Inverser l'ordre en ordre d�croissant
      return list_transparent_intersection;
    }   
    */
  }
  
  /**
   * M�thode qui effectue la fusion de deux listes de rayon ayant r�alis� le test de l'intersection
   * avec la g�om�trie opaque la plus pr�s. Ces deux listes seront fusionn�es, les premi�res intersections
   * avec une g�om�trie transparente seront conserv�e jusqu'� la 1i�re g�om�trie opaque et cette s�quence
   * sera finalement invers�e.
   * 
   * @param list1 - La 1i�re liste � fusionner.
   * @param list2 - La 2i�me liste � fusionner.
   * @return Une nouvelle liste d�terminant la 1i�re g�om�trie opaque intersect�e.
   */
  protected List<SRay> mergeNearestOpaqueIntersection(List<SRay> list1, List<SRay> list2)
  {
    // Fusionner les deux listes d'intersection en liste tri�e
    List<SRay> merge_list = mergeIntersections(list1, list2);
    
    // Iterer sur la liste fusionner et ajouter les intersection transparente
    // et garder seulement une seule intersection avec g�om�trie opaque
    List<SRay> return_list = new ArrayList<SRay>();
    
    for(SRay r : merge_list)
    {
      return_list.add(r);
      
      if(!r.getGeometry().isTransparent())
      {
        Collections.reverse(return_list);       // Inverser l'ordre en ordre d�croissant
        return return_list;                     // retourner une liste avec g�om�trie opaque
      }
    }
    
    Collections.reverse(return_list);           // Inverser l'ordre en ordre d�croissant
    return return_list;                         // retourner une liste sans g�om�trie opaque
  }
  
  /**
   * M�thode qui �value l'ensemble des g�om�tries d'une liste o� un vecteur v se retrouve � l'int�rieur.
   * 
   * @param list - La liste des g�om�tries.
   * @param v - La position du vecteur.
   * @return La liste des g�om�tries o� le vecteur v se retrouve � l'int�rieur.
   */
  protected List<SGeometry> listInsideGeometry(List<SGeometry> list, SVector3d v)
  {
    // Liste des g�om�tries o� le vecteur v se retrouvera � l'int�rieur
    List<SGeometry> return_list = new ArrayList<SGeometry>();  
    
    //It�ration sur l'ensemble des g�om�tries 
    if(list != null)
      for(SGeometry g : list)
        if(g.isClosedGeometry())     // la g�om�trie doit �tre ferm�e  
          if(g.isInside(v))          // le vecteur v doit se retrouver � l'int�rieur de la g�om�trie
            return_list.add(g);
          
    return return_list;
  }
  
  @Override
  public void initialize()
  {
    space_initialized = true;
  }
  
  @Override
  public String toString()
  {
    return new String("Nb geometry : " + geometry_list.size());
  }
  
  /**
   * M�thode pour obtenir le nombre de tests d'intersection qui ont �t� r�alis�s.
   * 
   * @return Le nombre de tests d'intersection calcul�s.
   */
  public static int getIntersectionTestCount()
  {
    return intersection_test_count;
  }
  
  /**
   * M�thode pour r�initialiser le comptage des tests d'intersection.
   */
  public static synchronized void resetIntersectinonTestCount()
  {
    intersection_test_count = 0;
  }
  
  /**
   * M�thode pour augmenter le nombre de tests d'intersection r�alis�s.
   * Cette m�thode sous cette forme est n�cessaire afin d'�viter des probl�mes lors d'un usage de cette classe en mode <i>multiprocessing</i>.
   * 
   * @param value - Le nombre de tests d'intersection � ajouter.
   */
  private static synchronized void increaseIntersectionCount(int value)
  {
    intersection_test_count += value;
  }
  
}//fin classe abstraite SAbstractGeometrySpace
