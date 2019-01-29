/**
 * 
 */
package sim.parser.model.obj;

import java.util.ArrayList;

import sim.exception.SRuntimeException;

/**
 * Classe qui repr�sente un polygone selon le format OBJ.
 * @author Simon V�zina
 * @since 2015-03-25
 * @version 2015-06-15
 */
public class SPolygonOBJ {

  private int index_error;  //le nombre d'erreur d'indexage entre les indexe demand� par la Face et la disponibilit� des listes.
  
  private final String material_name;                    //nom du materiel
  
  private final ArrayList<SVertex> vertex_list;          //liste des Vertex du polygone
  private final ArrayList<SVertex> vertex_texture_list;  //liste des Vertex texture du polygone
  private final ArrayList<SVertex> vertex_normal_list;   //liste des Vertex normal du polygone
  
  /**
   * Constructeur d'un polygone selon le format OBJ � partir de la d�finition d'une face et de l'ensemble des vertexs disponibles.
   * 
   * @param face - La face du polygone comprenant les indexes des diff�rents Vertex du polygone.
   * @param vertex_list - La liste des Vertex disponibles (ne seront pas tous utilis�s).
   * @param texture_list - La liste des Vertex texture disponibles (ne seront pas tous utilis�s).
   * @param normal_list - La liste des Vertex normales disponibles (ne seront pas tous utilis�s). 
   */
  public SPolygonOBJ(SFace face, ArrayList<SVertex> all_vertex_list,  ArrayList<SVertex> all_texture_list,  ArrayList<SVertex> all_normal_list)
  {
    material_name = face.getMaterialName();
    
    index_error = 0;
    
    vertex_list = buildVertexList(face, all_vertex_list); 
    vertex_texture_list = buildVertexTextureList(face, all_texture_list);
    vertex_normal_list = buildVertexNormalList(face, all_normal_list);
  }

  /**
   * Constructeur d'un polygone selon le format OBJ particulier permettant de copier des informations <b>sans</b> les vertexs de normale ni texture.
   * Cette version peut �tre pertinante si la d�finition de ces derniers �tait erron�e dans la d�finition officiel du SPolygonOBJ.
   * 
   * @param material_name - Le nom du mat�riel associ� au polygone.
   * @param vertex_list - La liste des vertex d�finissant le polygone.
   */
  private SPolygonOBJ(String material_name, ArrayList<SVertex> vertex_list)
  {
    this.material_name = material_name;
    
    index_error = 0;
    
    this.vertex_list = vertex_list;
    this.vertex_texture_list = new ArrayList<SVertex>();   //liste vide
    
    this.vertex_normal_list = new ArrayList<SVertex>();    //liste vide
  }
  
  /**
   * M�thode pour obtenir une copie d'un polygone en ayant retir� les r�f�rence aux normales et aux coordonn�es uv.
   * Ce nouveau polygone sera automatiquement d�finit avec une normale g�om�trique.
   * 
   * @return Un nouveau polygone moins d�finit (sans normale ni coordonn�e de texture en chaque point).
   */
  public SPolygonOBJ generateBasicSPolygonOBJ()
  {
    return new SPolygonOBJ(material_name, vertex_list);
  }
  
  /**
   * M�thode pour obtenir le nom du mat�riel associ� au polygone.
   * @return Le nom du mat�riel.
   */
  public String getMaterialName()
  {
    return material_name;
  }
  
  /**
   * M�thode pour obtenir le nombre de Vertex dans le polygone.
   * @return Le nombre de Vertex dans le polygone.
   */
  public int vertexNumber()
  {
    return vertex_list.size();
  }
  
  /**
   * M�thode pour d�terminer s'il y a des Vertex dans le polygone.
   * @return <b>true</b> s'il y a des Vertex et <b>false</b> sinon.
   */
  public boolean asVertex()
  {
    if(vertex_list.isEmpty())
      return false;
    else
      return true;
  }
  
  /**
   * M�thode pour d�terminer s'il y a des Vertex textures dans le polygone.
   * @return <b>true</b> s'il y a des Vertex textures et <b>false</b> sinon.
   */
  public boolean asVertexTexture()
  {
    if(vertex_texture_list.isEmpty())
      return false;
    else
      return true;
  }
  
  /**
   * M�thode pour d�terminer s'il y a des Vertex noramles dans le polygone.
   * @return <b>true</b> s'il y a des Vertex noramles et <b>false</b> sinon.
   */
  public boolean asVertexNormal()
  {
    if(vertex_normal_list.isEmpty())
      return false;
    else
      return true;
  }
  
  /**
   * M�thode pour d�terminer s'il y a eu des erreur d'indexage durant l'affectation des Vertex au polygone � partir de la table d'indexage.
   * Si tel est le cas, il sera pr�f�rable de ne pas g�n�rer de polygone avec ces informations.
   * @return <b>true</b> s'il y a eu des erreur � l'indexage et <b>false</b> sinon.
   */
  public boolean asError()
  {
    if(index_error != 0)
      return true;
    else
      return false;
  }
  
  /**
   * M�thode pour obtenir le Vertex d'indice i dans le polygone.
   * @param i - L'indice du Vertex dans le polygone.
   * @return Le Vertex d'indice i.
   * @throws SRuntimeException Si le polygone ne poss�de pas de Vertex.
   */
  public SVertex getVertex(int i)throws SRuntimeException
  {
    if(asVertex())
      return vertex_list.get(i);
    else
      throw new SRuntimeException("Erreur SPolygonOBJ 001 : Le polygone ne poss�de pas de Vertex comme param�tre. Une erreur lors de la lecture a du �tre rencontr�e.");
  }
  
  /**
   * M�thode pour obtenir le Vertex texture d'indice i dans le polygone.
   * @param i - L'indice du Vertex dans le polygone. 
   * @return Le Vertex texture d'indice i.
   * @throws SRuntimeException Si le polygone ne poss�de pas de Vertex texture.
   */
  public SVertex getVertexTexture(int i)throws SRuntimeException
  {
    if(asVertexTexture())
      return vertex_texture_list.get(i);
    else
      throw new SRuntimeException("Erreur SPolygonOBJ 002 : Le polygone ne poss�de pas de Vertex texture comme param�tre.");
  }
  
  /**
   * M�thode pour obtenir le Vertex normale d'indice i dans le polygone.
   * @param i - L'indice du Vertex dans le polygone. 
   * @return Le Vertex normale d'indice i.
   * @throws SRuntimeException Si le polygone ne poss�de pas de Vertex normale.
   */
  public SVertex getVertexNormal(int i)throws SRuntimeException
  {
    if(asVertexNormal())
      return vertex_normal_list.get(i);
    else
      throw new SRuntimeException("Erreur SPolygonOBJ 003 : Le polygone ne poss�de pas de Vertex normale comme param�tre.");
  }
  
  /**
   * M�thode pour construire la liste des Vertex du polygone � partir d'une liste d'index et l'ensemble des Vertex disponibles tri�s par num�ro d'index.
   * @param face - La Face du polygone.
   * @param vertex_list - La liste des Vertex disponibles.
   * @return La liste des Vertex du polygone.
   */
  private ArrayList<SVertex> buildVertexList(SFace face, ArrayList<SVertex> vertex_list)
  {
    ArrayList<SVertex> list = new ArrayList<SVertex>();
    
    if(face.asVertex())
    {
      int[] index_tab = face.getVertexIndex();
      int size_tab = face.getVertexNumber();
      
      //It�rer sur l'ensemble des index du tableau et ajouter le Vertex correspondant � la liste des Vertex
      for(int i=0; i<size_tab; i++)
      {
        int index = index_tab[i];
        
        if(index < vertex_list.size())		//si l'index demand� est disponible
          list.add(vertex_list.get(index));
        else								//si l'index demand� n'est pas disponible
        {
          index_error++;	//identification d'une erreur		
          list.clear();		//vider la liste et rendre cette liste inacessible
          return list;
        }
      }   
    }
    return list;
  }
  
  /**
   * M�thode pour construire la liste des Vertex textures du polygone � partir d'une liste d'index et l'ensemble des Vertex textures disponibles tri�s par num�ro d'index.
   * @param face - La Face du polygone.
   * @param vertex_list - La liste des Vertex textures disponibles.
   * @return La liste des Vertex textures du polygone.
   */
  private ArrayList<SVertex> buildVertexTextureList(SFace face, ArrayList<SVertex> vertex_list)
  {
    ArrayList<SVertex> list = new ArrayList<SVertex>();
    
    if(face.asVertexTexture())
    {
      int[] index_tab = face.getVertexTextureIndex();
      int size_tab = face.getVertexNumber();
      
      //It�rer sur l'ensemble des index du tableau et ajouter le Vertex correspondant � la liste des Vertex
      for(int i=0; i<size_tab; i++)
      {
        int index = index_tab[i];
        
        if(index < vertex_list.size())			//si l'index demand� est disponible
          list.add(vertex_list.get(index));
        else									//si l'index demand� n'est pas disponible
        {
          index_error++;	//identification d'une erreur		
          list.clear();		//vider la liste et rendre cette liste inacessible
          return list;
        }
      }   
    }
    return list;
  }
  
  /**
   * M�thode pour construire la liste des Vertex normales du polygone � partir d'une liste d'index et l'ensemble des Vertex normales disponibles tri�s par num�ro d'index.
   * @param face - La Face du polygone.
   * @param vertex_list - La liste des Vertex normales disponibles.
   * @return La liste des Vertex normales du polygone.
   */
  private ArrayList<SVertex> buildVertexNormalList(SFace face, ArrayList<SVertex> vertex_list)
  {
    ArrayList<SVertex> list = new ArrayList<SVertex>();
    
    if(face.asVertexNormal())
    {
      int[] index_tab = face.getVertexNormalIndex();
      int size_tab = face.getVertexNumber();
      
      //It�rer sur l'ensemble des index du tableau et ajouter le Vertex correspondant � la liste des Vertex
      for(int i=0; i<size_tab; i++)
      {
        int index = index_tab[i];
        
        if(index < vertex_list.size())			//si l'index demand� est disponible
          list.add(vertex_list.get(index));		
        else									//si l'index demand� n'est pas disponible
        {
          index_error++;	//identification d'une erreur		
          list.clear();		//vider la liste et rendre cette liste inacessible
          return list;
        }
      }   
    }
    return list;
  }
  
}//fin classe SPolygonOBJ
