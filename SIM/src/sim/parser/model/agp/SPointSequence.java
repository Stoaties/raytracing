/**
 * 
 */
package sim.parser.model.agp;

import java.util.ArrayList;

/**
 * Classe qui repr�sente une s�quence de point.
 * 
 * @author Simon V�zina
 * @since 2015-08-01
 * @version 2015-08-01
 */
public class SPointSequence {

  private final ArrayList<SPoint> sequence; //la s�quence de point
  
  /**
   * Constructeur d'une s�quence de point avec un nombre d'�l�ments � l'int�rieur qui devront �tre d�finis. 
   * @param nb - Le nombre de points � l'int�rieur de la s�quence.
   */
  public SPointSequence(int nb)
  {
    sequence = new ArrayList<SPoint>();
    
    //Ajouter des points non d�fini � l'int�rieur de la s�quence
    for(int i=0; i<nb; i++)
      sequence.add(new SPoint());
  }

  /**
   * M�thode pour obtenir un point � un index sp�cifique de la s�quence de point. L'index 0 correspond
   * au premier point.
   * @param index - L'index du point.
   * @return Le point � l'index sp�cifique
   * @throws IndexOutOfBoundsException Si l'index est � l'ext�rieur des bornes suivantes (index < 0 || index >= size()) 
   */
  public SPoint getPoint(int index)throws IndexOutOfBoundsException 
  {
    return sequence.get(index);
  }
 
  /**
   * M�thode pour obtenir la taille de la s�quence.
   * @return La taille de la s�quence.
   */
  public int size()
  {
    return sequence.size();
  }
  
  /**
   * M�thode pour d�finir la coordonn�e x d'un point � un index particulier.
   * @param index - L'index du point dont la coordonn�e x sera modifi�e.
   * @throws IndexOutOfBoundsException Si l'index est � l'ext�rieur des bornes suivantes (index < 0 || index >= size()) 
   */
  public void setX(int index, double x)throws IndexOutOfBoundsException 
  {
    sequence.get(index).setX(x);
  }
  
  /**
   * M�thode pour d�finir la coordonn�e y d'un point � un index particulier.
   * @param index - L'index du point dont la coordonn�e y sera modifi�e.
   * @throws IndexOutOfBoundsException Si l'index est � l'ext�rieur des bornes suivantes (index < 0 || index >= size()) 
   */
  public void setY(int index, double y)throws IndexOutOfBoundsException 
  {
    sequence.get(index).setY(y);
  }
  
  /**
   * M�thode pour d�finir la coordonn�e z d'un point � un index particulier.
   * @param index - L'index du point dont la coordonn�e z sera modifi�e.
   * @throws IndexOutOfBoundsException Si l'index est � l'ext�rieur des bornes suivantes (index < 0 || index >= size()) 
   */
  public void setZ(int index, double z)throws IndexOutOfBoundsException 
  {
    sequence.get(index).setZ(z);
  }
  
}//fin de la classe SPointSequence
