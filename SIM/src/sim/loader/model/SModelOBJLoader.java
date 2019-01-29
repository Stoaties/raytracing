/**
 * 
 */
package sim.loader.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sim.loader.SLoaderException;
import sim.loader.SStringLoader;
import sim.math.SVector3d;
import sim.math.SVectorUV;
import sim.parser.model.obj.SMaterialOBJ;
import sim.parser.model.obj.SModelOBJParser;
import sim.parser.model.obj.SModelOBJParserException;
import sim.parser.model.obj.SPolygonOBJ;
import sim.parser.model.obj.SVertex;
import sim.util.SLog;
import sim.util.SStringUtil;
import sim.exception.SConstructorException;
import sim.graphics.material.SMaterial;
import sim.graphics.material.SOpenGLMaterial;
import sim.graphics.material.STextureOpenGLMaterial;
import sim.graphics.SColor;
import sim.graphics.SPrimitive;
import sim.graphics.SModel;
import sim.geometry.SGeometry;
import sim.geometry.STriangleGeometry;
import sim.geometry.SBTriangleGeometry;

/**
 * Classe qui repr�sente un interpr�teur de lecteur de mod�le 3D de format OBJ (WaveFront) g�n�rant un objet SMod�le pouvant �tre int�gr� au syst�me.
 *
 * @author Simon V�zina
 * @since 2015-03-28
 * @version 2015-09-27
 */
public class SModelOBJLoader implements SStringLoader {

  /**
   * La constante 'FILE_EXTENSION' correspond � l'extension du fichier en lecture {@value} admissible par ce lecteur de mod�le 3d.
   */
  public static final String FILE_EXTENSION = "obj";  //extension des fichiers lue par ce loader
  
  /**
   * La variable 'nb_triangle' correspond au nombre de triangles compris dans le mod�le.
   */
  private int nb_triangle;
  
  /**
   * La variable 'nb_square' correspond au nombre de carr�s compris dans le mod�le.
   */
  private int nb_square;
  
  /**
   * La variable 'nb_polygon' correspond au nombre de polygone (plus de 2 triangles requis) compris dans le mod�le.
   */
  private int nb_polygon;
  
  /**
   * La variable 'polygon_error' correspond au nombre de polygons lus par le parser qui sont en erreur de lecture.
   */
  private int polygon_error;              
  
  /**
   * La variable 'triangle_error' correspond au nombre de triangles qui n'ont pas �t� construit en raison d'une mauvaise d�finition des vertexs.
   */
  private int triangle_error;             
  
 
  
  /**
   * La variable 'triangle_repair' correspond au nombre de triangles mal d�fini, mais r�par� lors de la construction. Ces triangles n'auront cependant pas une d�finition compl�te.
   */
  private int triangle_repair;
  
	/**
	 * Constructeur d'un chargeur de mod�le 3D de format OBJ.
	 */
	public SModelOBJLoader() 
	{
	  nb_triangle = 0;
	  nb_square = 0;
	  nb_polygon = 0;
	  
	  polygon_error = 0;
	  triangle_error = 0;
	  triangle_repair = 0;
	}

	/* (non-Javadoc)
	 * @see sim.loader.SStringLoader#load(java.lang.String)
	 */
	@Override
	public Object load(String string) throws SLoaderException
	{
		try{
				
	  SModelOBJParser parser = new SModelOBJParser(string);		//lecture par le parser du fichier contenant la description du mod�le 3d en format OBJ
		
		SModel model = new SModel(string);							                           //mod�le 3d � remplir
		Map<String, SMaterial> material_map = new HashMap<String, SMaterial>();    //carte des mat�riaux de format SIM
		
		List<SPolygonOBJ> polygon_list = parser.getListPolygon();	   //liste des polygones � construire
		List<SMaterialOBJ> material_list = parser.getListMaterial(); //liste des mat�riels � construire
		
		//Iterer sur l'ensemble des mat�riaux pour les construire en format SIM
		for(SMaterialOBJ m : material_list)
		  buildMaterial(m, material_map);
				
		//Iterer sur l'ensemble des polygones pour construire toutes les primitives
		for(SPolygonOBJ p : polygon_list)
		{
		  //Obtenir le mat�riel du poylygone pr�alablement construit
		  SMaterial m = material_map.get(p.getMaterialName());
		    
		  //Construire un mat�riel par d�faut si aucun mat�riel n'a �t� trouv� (null) dans la carte des mat�riaux
		  if(m == null)
		  {
		    SLog.logWriteLine("Message SModelOBJLoader : Le mat�riel '" + p.getMaterialName() + "' n'a pas �t� trouv�. Un mat�riel par d�faut sera utilis�.");
		     
		    m = new SOpenGLMaterial(p.getMaterialName());  //construire le mat�riel par d�faut
		    material_map.put(m.getName(), m);              //ajouter ce mat�riel � la carte au cas o� il y aurait d'autres mat�riaux de ce nom demand� par d'autres polygone 
		  }
			
		  //--------------------------
		  //Construction du polygone
		  //--------------------------
		  if(!p.asVertex())                 //Polygone sans vertex --> erreur
		    polygon_error++;
		  else
		    if(p.vertexNumber() < 3)        //Polygone avec moins de 3 vertex --> erreur
		      polygon_error++;
		    else
		    {
		      if(p.asError())               //Polygone avec erreur lors de la lecture --> informer l'usager qu'il y a une erreur sur le polygone, mais pas sur ses vertexs. Il sera alors dessin�, mais pas ad�quatement.
		        polygon_error++;
		      
		      //Construction selon le nombre de vertex par polygone
		      switch(p.vertexNumber())
		      {
		        //Construction d'un seul triangle
		        case 3 : nb_triangle++;
		                 buildTriangle(p, m, model, 0, 1, 2);
		                 break;
		        
		        //Construction de 2 triangles
		        case 4 : nb_square++;
		                 buildTrianglesFanPolygon(p, m, model);         //selon mes lectures, c'est la "FAN" qui est officiel !
		                 //buildTrianglesStripPolygon(p, m, model);
		                 break; 
		                 
		        //Construction de plusieurs triangles � l'aide d'un polygone � plusieurs points
		        default : nb_polygon++;
		                  buildTrianglesFanPolygon(p, m, model); 
		      }   
		    }				
		}//fin for
				
		//Message en information
		SLog.logWriteLine("Message SModelOBJLoader : Le mod�le '" + string + "' poss�de : " + nb_triangle + " triangles, " + nb_square + " carr�s et " + nb_polygon + " polygones."); 
		
		//Message sur le nombre de polygones avec des erreurs
    if(polygon_error != 0)
      SLog.logWriteLine("Message SModelOBJLoader : Le mod�le '" + string + "' poss�de " + polygon_error + " polygones avec des erreurs de lecture. Ces triangles seront peut-�tre visible, mais avec des erreurs.");
    
		//Message sur le nombre de triangle r�par� ...
		if(triangle_repair != 0)
		  SLog.logWriteLine("Message SModelOBJLoader : Le mod�le '" + string + "' s'est fait r�parer " + triangle_repair + " triangles lors du chargement. Ces triangles seront sans normale ni texture.");
		 		
		//Message sur le nombre de triangles dont la construction a �chou� en raison d'erreur sur la d�finition des vertex
    if(triangle_error != 0)
      SLog.logWriteLine("Message SModelOBJLoader : Le mod�le '" + string + "' poss�de " + triangle_error + " triangles avec des erreurs de vertexs (ex: triangle avec trois points colin�aires).");
       
		return model;
		
		}catch(SModelOBJParserException e){
		  throw new SLoaderException("Erreur SModelOBJLoader 003 : Une erreur lors de la lecture est survenue ce qui emp�che le chargement du mod�le 3d. " + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());
		}
	}

	/**
	 * M�thode pour construire et ajouter un nouveau mat�riel � la carte des mat�riaux.
	 * @param material - Le mat�riel en format OBJ � traduire en format SIM.
	 * @param material_map - La carte des mat�riaux.
	 */
	private void buildMaterial(SMaterialOBJ material, Map<String, SMaterial> material_map)
	{
	  SColor ka = vertexToSColor(material.getKa());
	  SColor kd = vertexToSColor(material.getKd());
	  SColor ks = vertexToSColor(material.getKs());
	  double shininess = (float)material.getNs();
	  
	  //Construction du mat�riel
	  SMaterial m;
	   
	  if(!material.asTexture())                                                        //mat�riel sans texture
	    m = new SOpenGLMaterial(material.getMaterialName(), ka, kd, ks, shininess);    
	  else                                                                             //mat�riel avec texture
	  {  
	    String texture_ka = material.getTextureKaFileName();
	    String texture_kd = material.getTextureKdFileName();
	    String texture_ks = material.getTextureKsFileName();
	    	    
	    m = new STextureOpenGLMaterial(material.getMaterialName(), ka, kd, ks, shininess, texture_ka, texture_kd, texture_ks);
	  }
	  
	  //Ajouter le mat�riel � la carte si son nom ne se retrouve pas d�j� comme cl� d'acc�s � l'int�rieur
	  if(!material_map.containsKey(m.getName()))
	    material_map.put(m.getName(), m);
	}
	
	/**
	 * M�thode pour construire un STriangle ou un SBTriangle selon les caract�ristiques du polygone � partir
	 * de trois index correspondant � des points du polygone.
	 * 
	 * @param polygon - Les caract�ristiques du polygone.
   * @param material_map - La carte des mat�riels disponibles pour la construction de la primitive.
   * @param model - Le mod�le qui accueillera le nouveau polygone dans la structure.
	 * @param index_p0 - L'index du point P0 dans le tableau de points du polygone.
	 * @param index_p1 - L'index du point P1 dans le tableau de points du polygone.
	 * @param index_p2 - L'index du point P2 dans le tableau de points du polygone.
	 * @throws SConstructorException S'il y a une erreur dans la construction du triangle (ex : 3 points colin�aires, coordonn�e uv erron�e).
	 */
	private void buildTriangle(SPolygonOBJ polygon, SMaterial material, SModel model, int index_p0, int index_p1, int index_p2) throws SConstructorException
  {
	  try{
	      
	    SGeometry g; //la g�om�rie � d�finir
      
      //--------------------------------------------------------------------------
      //Construire la g�om�trie selon le type de triangle d�crit par le polygonOBJ
      //--------------------------------------------------------------------------
      
      //Triangle sans normale ni coordonn�e de texture (triangle de base)
      if(!polygon.asVertexNormal() && !polygon.asVertexTexture())  
        g = new STriangleGeometry(vertexToSVector3d(polygon.getVertex(index_p0)), vertexToSVector3d(polygon.getVertex(index_p1)), vertexToSVector3d(polygon.getVertex(index_p2)));
      else
        //Triangle avec normale et coordonn�e de texture (triangle barycentrique)
        if(polygon.asVertexNormal() && polygon.asVertexTexture())
          g = new SBTriangleGeometry(vertexToSVector3d(polygon.getVertex(index_p0)),        vertexToSVector3d(polygon.getVertex(index_p1)),        vertexToSVector3d(polygon.getVertex(index_p2)),
                                     vertexToSVector3d(polygon.getVertexNormal(index_p0)),  vertexToSVector3d(polygon.getVertexNormal(index_p1)),  vertexToSVector3d(polygon.getVertexNormal(index_p2)),
                                     vertexToSVectorUV(polygon.getVertexTexture(index_p0)), vertexToSVectorUV(polygon.getVertexTexture(index_p1)), vertexToSVectorUV(polygon.getVertexTexture(index_p2)));
        else
          //Triangle avec normale, mais sans coordonn�e de texture (triangle barycentrique)
          if(polygon.asVertexNormal())
            g = new SBTriangleGeometry(vertexToSVector3d(polygon.getVertex(index_p0)),       vertexToSVector3d(polygon.getVertex(index_p1)),       vertexToSVector3d(polygon.getVertex(index_p2)),
                                       vertexToSVector3d(polygon.getVertexNormal(index_p0)), vertexToSVector3d(polygon.getVertexNormal(index_p1)), vertexToSVector3d(polygon.getVertexNormal(index_p2)));
          else
            //Triangle sans normale, mais avec coordonn�e de texture (triangle barycentrique)
            g = new SBTriangleGeometry(vertexToSVector3d(polygon.getVertex(index_p0)),        vertexToSVector3d(polygon.getVertex(index_p1)),        vertexToSVector3d(polygon.getVertex(index_p2)),
                                       vertexToSVectorUV(polygon.getVertexTexture(index_p0)), vertexToSVectorUV(polygon.getVertexTexture(index_p1)), vertexToSVectorUV(polygon.getVertexTexture(index_p2)));
      
      //ajouter la primitive au model
      model.addPrimitive(new SPrimitive(g, material));  
	    
	  }catch(SConstructorException e1){
	      
	    //Ce sc�nario se produit si un triangle est mal d�fini (3 points colin�aires, coordonn�e uv mal d�fini).
	    //Nous allons tent� de r�parer ce triangle partiellement, mais aviser le syst�me de cette correction.
	    try{
	        
	      SGeometry g = new STriangleGeometry(vertexToSVector3d(polygon.getVertex(index_p0)), vertexToSVector3d(polygon.getVertex(index_p1)), vertexToSVector3d(polygon.getVertex(index_p2)));
	      model.addPrimitive(new SPrimitive(g, material));   //ajouter la primitive
	      
	      triangle_repair++;
	      
	      if(triangle_repair < 4)
	        SLog.logWriteLine("Message SModelOBJLoader : Un triangle a �t� r�par� en raison de ce message d'erreur." + SStringUtil.END_LINE_CARACTER + "\t" + e1.getMessage());
	      else
	        if(triangle_repair < 5)
	          SLog.logWriteLine("Message SModelOBJLoader : Un triangle a �t� r�par� en raison de ce message d'erreur ...........");
	      
	    }catch(SConstructorException e2){
	      
	      //Ce sc�nario se produit lorsque la tentative de r�paration de triangle a �chou�e
	      triangle_error++;
	      
	      if(triangle_error < 4)
          SLog.logWriteLine("Message SModelOBJLoader : Un triangle ne peut pas �tre construit en raison de ce message d'erreur." + SStringUtil.END_LINE_CARACTER + "\t" + e1.getMessage());
        else
          if(triangle_error < 5)
            SLog.logWriteLine("Message SModelOBJLoader :  Un triangle ne peut pas �tre construit en raison de ce message d'erreur ...........");  
	    }}
	      
  }
	
	/**
   * M�thode pour construire plusieurs triangles en format <i>triangle strip</i> � partir des points du polygones s'ils sont <b>coplanaire</b> et si le polygone est <b>convexe</b>.
   * Cette d�finition de triangularisation du polygone respecte le standard du format OBJ (WaveFront).
   * <ul>Un <i>triangle strip</i> � N points construit N-2 triangles avec la combinaison suivante de point :
   * point[i], point[i+1] et point[i+2] 
   * </ul>
   * @param polygon - Les caract�ristiques du polygone.
   * @param material_map - La carte des mat�riels disponibles pour la construction de la primitive.
   * @param model - Le mod�le qui accueillera le nouveau polygone dans la structure.
   */
	/*
  private void buildTrianglesStripPolygon(SPolygonOBJ polygon, SMaterial material, SModel model)
  {
    for(int i = 0; i < polygon.vertexNumber()-2; i++)
      buildTriangle(polygon, material, model, i, i+1, i+2);
  }
  */
	
	/**
	 * M�thode pour construire plusieurs triangles en format <i>triangle fan</i> � partir des points du polygones s'ils sont <b>coplanaire</b> et si le polygone est <b>convexe</b>.
	 * Cette d�finition de triangularisation du polygone respecte le standard du format OBJ (WaveFront).
	 * <ul>Un <i>triangle fan</i> � N points construit N-2 triangles avec la combinaison suivante de point :
	 * point[0], point[i] et point[i+1] 
	 * </ul>
	 * @param polygon - Les caract�ristiques du polygone.
   * @param material_map - La carte des mat�riels disponibles pour la construction de la primitive.
   * @param model - Le mod�le qui accueillera le nouveau polygone dans la structure.
   */
	private void buildTrianglesFanPolygon(SPolygonOBJ polygon, SMaterial material, SModel model)
	{
	  for(int i = 1; i < polygon.vertexNumber()-1; i++)
	    buildTriangle(polygon, material, model, 0, i, i+1);
	}
	
	/*
	private void buildTrianglesWithConcavePolygon(SPolygonOBJ polygon, SMaterial material, SModel model)
	{
	  //voir http://en.wikipedia.org/wiki/Polygon_triangulation#Ear_clipping_method
	  //r�f�rence : http://stackoverflow.com/questions/9147197/in-a-wavefront-object-file-obj-how-am-i-supposed-to-render-faces-with-more-th
	}
	*/
	
	/**
	 * M�thode pour convertir un objet de classe SVertex en classe SColor.
	 * 
	 * @param v - Le vertex.
	 * @return La couleur associ� au vertex.
	 */
	private SColor vertexToSColor(SVertex v)
	{
	  return new SColor((double)v.getX(), (double)v.getY(), (double)v.getZ());
	}
	  
	/**
	 * M�thode pour convertir un objet de classe SVertex en classe SVector3d.
	 * 
	 * @param v - Le vertex.
	 * @return Le vecteur 3d associ� au vertex.
	 */
	private SVector3d vertexToSVector3d(SVertex v)
	{
	  return new SVector3d((double)v.getX(), (double)v.getY(), (double)v.getZ());
	}
	  
	/**
	 * M�thode pour convertir un objet de classe SVertex en classe SVectorUV.
	 * 
	 * @param v - Le vertex.
	 * @return Le vecteur uv associ� au vertex.
	 */
	private SVectorUV vertexToSVectorUV(SVertex v)
	{
	  return new SVectorUV((double)v.getU(), (double) v.getV());
	}
	
}//fin classe SModelOBJLoader
