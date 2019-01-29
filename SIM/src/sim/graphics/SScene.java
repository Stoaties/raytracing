/**
 * 
 */
package sim.graphics;

import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.geometry.SGeometry;
import sim.geometry.SPlaneGeometry;
import sim.geometry.SSphereGeometry;
import sim.graphics.light.SAmbientLight;
import sim.graphics.light.SApertureMaskLight;
import sim.graphics.light.SDirectionalLight;
import sim.graphics.light.SEllipticalApertureLight;
import sim.graphics.light.SLight;
import sim.graphics.light.SLinearApertureLight;
import sim.graphics.light.SPointLight;
import sim.graphics.light.SRectangularApertureLight;
import sim.graphics.material.SBlinnMaterial;
import sim.graphics.material.SBlinnTextureMaterial;
import sim.graphics.material.SMaterial;
import sim.graphics.SModel;
import sim.math.SVector3d;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SLog;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SScene</b> repr�sente une sc�ne en trois dimensions. 
 * Une sc�ne comprend des primitives, des mat�riaux, des lumi�res, ainsi que des param�tres de visionnement comme une cam�ra, un viewport et le ray tracer.
 *  
 * @author Simon V�zina
 * @since 2014-12-26
 * @version 2016-05-20
 */
public class SScene extends SAbstractReadableWriteable {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = {
    SKeyWordDecoder.KW_CAMERA, SKeyWordDecoder.KW_VIEWPORT, SKeyWordDecoder.KW_RAYTRACER,
    SKeyWordDecoder.KW_PRIMITIVE, SKeyWordDecoder.KW_BLINN_MATERIAL, SKeyWordDecoder.KW_TEXTURE_MATERIAL, 
    SKeyWordDecoder.KW_AMBIENT_LIGHT, SKeyWordDecoder.KW_DIRECTIONAL_LIGHT, SKeyWordDecoder.KW_POINT_LIGHT,
    SKeyWordDecoder.KW_LINEAR_APERTURE_LIGHT, SKeyWordDecoder.KW_RECTANGULAR_APERTURE_LIGHT,
    SKeyWordDecoder.KW_ELLIPTICAL_APERTURE_LIGHT,SKeyWordDecoder.KW_APERTURE_MASK_LIGHT,
    SKeyWordDecoder.KW_MODEL     
  };
  
  /**
   * La constante <b>DEFAULT_SCENE_FILE_NAME</b> correspond au nom du fichier de sc�ne par d�faut �tant �gal � {@value}. 
   */
  public static final String DEFAULT_SCENE_FILE_NAME = "default_scene.txt";
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable 'camera' correspond � la cam�ra qui sera utilis�e pour visualiser la sc�ne.
   */
	private SCamera camera;					                 
	
	/**
	 * La variable 'viewport' correspond � l'�cran de vue o� seront �crit les diff�rents pixels de couleur calcul�s par le ray tracer.
	 */
	private SViewport viewport;		
	
	/**
	 * La variable 'raytracer_builder' correspond � un constructeur du raytracer. Selon ses param�tres, un ray tracer particulier sera �ventuellement construit.
	 */
	private SRaytracerBuilder raytracer_builder;     
			
	/**
	 * La variable 'primitive_list' correspond � la liste des primitives situ�es dans la sc�ne. Une primitive est une g�om�trie affect�e � un mat�riel.
	 */
	private final List<SPrimitive> primitive_list;   
	
	/**
	 * La variable 'material_list' correspond � la liste des diff�rents mat�riaux disponibles lors du calcul du rendu.
	 */
	private final List<SMaterial> material_list;		 
	
	/**
	 * La variable 'light_list' correspond � la liste des sources de lumi�res disponibles pour faire les calculs d'illumination de la sc�ne lors du ray tracing.
	 */
	private final List<SLight> light_list;				   
	
	/**
	 * La variable 'model_list' correspond � la liste des diff�rents mod�les situ�s dans la sc�ne. Un mod�le comprend plusieurs primitives (g�om�trie + mat�riel).
	 */
	private final List<SModel> model_list;           
		
	/**
	 * La variable 'geometry_list' correspond � la liste des diff�rentes g�om�tries situ�s dans la sc�ne.
	 */
	private final List<SGeometry> geometry_list;     
	
	//----------------
	// CONSTRUCTEUR //
	//----------------
	
	/**
	 * Constructeur avec lecture d'un fichier txt.
	 * 
	 * @param file_name - Le nom du fichier en lecture.
	 * @throws FileNotFoundException Si le fichier de lecture n'a pas �t� trouv�.
	 * @throws IOException Si une erreur de type I/O est survenue.
	 * @throws SConstructorException Si une erreur est survenue lors de la construction.
	 */
	public SScene(String file_name) throws FileNotFoundException, IOException, SConstructorException
	{
	  camera = new SCamera();                             
    viewport = new SViewport();                         
    raytracer_builder = new SRaytracerBuilder();        
        
    primitive_list = new ArrayList<SPrimitive>();  
    material_list = new ArrayList<SMaterial>();     
    light_list = new ArrayList<SLight>();              
    model_list = new ArrayList<SModel>();              
      
    geometry_list = new ArrayList<SGeometry>();   
		
    // Choix de la cr�ation de la sc�ne
    if(file_name.equals(DEFAULT_SCENE_FILE_NAME))
      buildDefaultScene();                            // sc�ne par d�faut
    else
      read(file_name);                                // sc�ne en lecture  
	}
		
	//------------
	// M�THODES //
	//------------
	
	/**
	 * M�thode pour obtenir la cam�ra de la scene.
	 * 
	 * @return La cam�ra de la scene.
	 */
	public SCamera getCamera()
	{ 
	  return camera;
	}
	
	/**
	 * M�thode pour obtenir le viewport de la scene.
	 * 
	 * @return Le viewport de la scene.
	 */
	public SViewport getViewport()
	{ 
	  return viewport;
	}
	
	/**
	 * M�thode pour obtenir la liste des primitives de la sc�ne.
	 * 
	 * @return La liste des primitives.
	 */
	public List<SPrimitive> getPrimitiveList(){ return primitive_list; }
	
	/**
	 * M�thode pour obtenir la liste des sources de lumi�re de la sc�ne.
	 * 
	 * @return La liste des sources de lumi�re.
	 */
	public List<SLight> getLightList(){ return light_list; }
	
	/**
	 * M�thode pour faire la construction du raytracer associ� � la sc�ne en fonction de ses param�tres comme le viewport, la cam�ra, les g�om�tries et les lumi�res.
	 * 
	 * @return Le ray tracer de la sc�ne.
	 */
	public SRaytracer buildRaytracer()
	{
	  return raytracer_builder.buildRaytracer(viewport, camera, geometry_list, light_list);
	}

	@Override
	public void write(BufferedWriter bw) throws IOException
	{
		writeComment(bw, "Param�tres du rendu");
		
	  camera.write(bw);
		viewport.write(bw);
		raytracer_builder.write(bw);
			
		writeComment(bw, "Mod�les de la sc�ne");
		    
		//�crire les mod�le
    for(SModel m : model_list)
      m.write(bw);
    
    writeComment(bw, "Primitives de la sc�ne");
       
    //�crire les primitives
    for(SPrimitive p : primitive_list)
      p.write(bw);
    
    writeComment(bw, "Mat�riaux de la sc�ne");
    
   	//�crire les mat�riaux
		for(SMaterial m : material_list)
			m.write(bw);
		
		writeComment(bw, "Sources de lumi�re de la sc�ne");
				
		//�crire les lumi�res
		for(SLight l : light_list)
			l.write(bw);		
	}
	
	/**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
	{
		int primitive_count = 0;
		
		//Remplir l'espace de g�om�trie avec les g�om�tries des primitives disponibles.
		//Les primitives sans g�om�trie (qui sont � null) ne seront pas ajout�es � l'espace des g�om�tries.
		for(SPrimitive p : primitive_list)
			if(p.getGeometry() != null)							         //V�rification de la pr�sence d'une g�om�trie
			{
			  geometry_list.add(p.getGeometry());	//Ajout de la g�om�trie
			  primitive_count++;
			}
	
		//Mettre l'ensemble des g�om�tries des mod�le dans l'espace des g�om�tries
		for(SModel m : model_list)
		  for(SPrimitive p : m.getPrimitiveList())
		    if(p.getGeometry() != null)                     //V�rification de la pr�sence d'une g�om�trie
		    {
		      geometry_list.add(p.getGeometry());  //Ajout de la g�om�trie
		      primitive_count++;
		    }
		
		//Faire l'assignation des mat�riaux aux primitives
		SMaterialAffectation m_affectation = new SMaterialAffectation(primitive_list, material_list);
		m_affectation.affectation();
		
		//Message du comptage des primitives
    SLog.logWriteLine("Message SScene : La sc�ne comprend " + primitive_count + " primitives.");
	}
	
	@Override
	protected boolean read(SBufferedReader sbr, int code, String remaining_line)throws SReadingException, IOException
	{
		try{
  	  switch(code)
  		{
  			case SKeyWordDecoder.CODE_CAMERA :				        camera = new SCamera(sbr); return true;
  															
  			case SKeyWordDecoder.CODE_VIEWPORT :			        viewport = new SViewport(sbr); return true;
  											
  			case SKeyWordDecoder.CODE_RAYTRACER :             raytracer_builder = new SRaytracerBuilder(sbr); return true;
  			
  			case SKeyWordDecoder.CODE_PRIMITIVE :			        primitive_list.add(new SPrimitive(sbr)); return true;
  															
  			case SKeyWordDecoder.CODE_BLINN_MATERIAL:		      material_list.add(new SBlinnMaterial(sbr)); return true;
  			
  			case SKeyWordDecoder.CODE_TEXTURE_MATERIAL :      material_list.add(new SBlinnTextureMaterial(sbr)); return true;
  			
  			case SKeyWordDecoder.CODE_AMBIENT_LIGHT :		      light_list.add(new SAmbientLight(sbr));	return true;
  															
  			case SKeyWordDecoder.CODE_DIRECTIONAL_LIGHT :	    light_list.add(new SDirectionalLight(sbr));	return true;
  															
  			case SKeyWordDecoder.CODE_POINT_LIGHT :			      light_list.add(new SPointLight(sbr));	return true;
  			
  			case SKeyWordDecoder.CODE_LINEAR_APERTURE_LIGHT : light_list.add(new SLinearApertureLight(sbr)); return true;
  			
  			case SKeyWordDecoder.CODE_RECTANGULAR_APERTURE_LIGHT : light_list.add(new SRectangularApertureLight(sbr)); return true;
        
  			case SKeyWordDecoder.CODE_ELLIPTICAL_APERTURE_LIGHT : light_list.add(new SEllipticalApertureLight(sbr)); return true;
  			
  			case SKeyWordDecoder.CODE_APERTURE_MASK_LIGHT : light_list.add(new SApertureMaskLight(sbr)); return true;
        
  			
  			case SKeyWordDecoder.CODE_MODEL :   SModelReader m_reader = new SModelReader(sbr);
  			
  			                                    if(m_reader.asRead())
  			                                      model_list.add((SModel)m_reader.getValue());
  			                                    
  			                                    return true;
  			
  			default : return false;
  		}
		}catch(SConstructorException e){
		  throw new SReadingException("Erreur SScene 004 : La construction d'un �l�ment de la sc�ne est impossible." + SStringUtil.END_LINE_CARACTER + "\n" + e.getMessage());
		}
	}
			
	@Override
  protected void readingInitialization() throws SInitializationException
  {
    initialize();
  }
	
	@Override
  public String getReadableName()
  {
	  return SKeyWordDecoder.KW_SCENE;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
  /**
   * M�thode pour faire le chargement de la sc�ne par la lecture d'un fichier txt.
   * 
   * @param file_name Le nom du fichier txt.
   * @throws FileNotFoundException Si le ficher de lecture n'est pas trouv�.
   * @throws SConstructorException S'il y a eu une erreur lors de la construction de l'objet.
   * @throws IOException Si une erreur de type I/O est survenue.
   */
  /*
  private void readScene(String file_name) throws FileNotFoundException, SConstructorException, IOException
  {
    // Trouver le fichier � partir du r�pertoire o� l'ex�cution de l'application est r�alis�e
    SFileSearch search = new SFileSearch("", file_name);
    
    if(!search.isFileFound())
      throw new FileNotFoundException("Erreur SScene 001 : Le fichier '" + file_name + "' n'est pas trouv�.");
    
    if(search.isManyFileFound())
      throw new SConstructorException("Erreur SScene 002 : Le fichier '" + file_name + "' a �t� trouv� plus d'une fois dans les diff�rents sous-r�pertoires. Veuillez en garder qu'une seule version.");
    
    // Lecture de la sc�ne � partir d'un fichier
    FileReader fr = new FileReader(search.getFileFoundList().get(0));
    SBufferedReader sbr = new SBufferedReader(fr);
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SScene 003 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
      
    sbr.close();
    fr.close();
  }
  */
  
  /**
   * M�thode pour faire la construction d'une sc�ne par d�faut.
   */
  private void buildDefaultScene()
  {
    SPlaneGeometry plan = new SPlaneGeometry();
    SSphereGeometry sphere = new SSphereGeometry(new SVector3d(0.0, 0.0, 1.0), 0.5);
    
    SPrimitive p1 = new SPrimitive(plan);
    SPrimitive p2 = new SPrimitive(sphere);
    
    SDirectionalLight l1 = new SDirectionalLight();
    
    primitive_list.add(p1);
    primitive_list.add(p2);
    light_list.add(l1);
    
    camera = new SCamera(new SVector3d(-2.0, 0.0, 2.0), new SVector3d(0.0, 0.0, 0.0), new SVector3d(0.0, 0.0, 1.0));
    
    try{
      initialize();
    }catch(SInitializationException e){
      throw new SRuntimeException("Erreur SScene 005 : Une erreur d'initialisation est survenue durant la construction de la sc�ne par d�faut." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }
  
}//fin de la classe SScene
