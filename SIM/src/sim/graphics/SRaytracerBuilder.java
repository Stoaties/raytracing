/**
 * 
 */
package sim.graphics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.geometry.SGeometry;
import sim.geometry.space.SAbstractGeometrySpace;
import sim.geometry.space.SGeometrySpace;
import sim.geometry.space.SLinearSpace;
import sim.geometry.space.SMultiVoxelSpace;
import sim.geometry.space.SVoxelSpace;
import sim.graphics.light.SLight;
import sim.graphics.shader.SAbstractShader;
import sim.graphics.shader.SPhongReflexionShader;
import sim.graphics.shader.SRecursiveShader;
import sim.graphics.shader.SShader;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SRaytracerBuilder</b> repr�sente un <b>constructeur de raytracer</b> � partir d'information lue dans un fichier txt.
 * La lecture permettra de d�finir les propri�t�s du <b>shader</b> ainsi que du <b>raytracer</b>. 
 * 
 * @author Simon V�zina
 * @since 2015-07-08
 * @version 2016-05-06
 */
public class SRaytracerBuilder extends SAbstractReadableWriteable {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = {
    SKeyWordDecoder.KW_TASK, SKeyWordDecoder.KW_SAMPLING, SKeyWordDecoder.KW_RECURSIVE_LEVEL,
    SKeyWordDecoder.KW_REFLEXION_ALGORITHM, SKeyWordDecoder.KW_PIXEL_COORDINATE,
    SKeyWordDecoder.KW_SPACE, SKeyWordDecoder.KW_COLOR_NORMALIZATION
  };
  
  private static int DEFAULT_NB_TASK = 1;       //nombre de t�ches en multiprocesseur
  private static int DEFAULT_NB_SAMPLING = 1;   //nombre de rayon par pixel
  
  //Param�tres pour la d�finition du shader
  private int reflexion_algorithm;   
  private int recursive_level;
  
  //Param�tre pour la d�finition du view frustum
  private int pixel_internal_coordinate;
  
  //Param�tre pour la d�finition du ray tracer
  private int nb_task;                          //le nombre de t�ches simultan�es qui seront effectu�es durant le calcul de l'image (multi-processeur)
  private int nb_sampling;                      //le nombre de rayon dans le calcul de la couleur d'un pixel
  
  //Param�tre pour la d�finition de l'espace des g�om�trie
  private int type_of_space;
  
  /**
   * Constructeur d'un constructeur � raytracer par d�faut. 
   */
  public SRaytracerBuilder()
  {
    reflexion_algorithm = SAbstractShader.BLINN_REFLEXION;
    recursive_level = 1;
    
    pixel_internal_coordinate = SViewFrustum.TOP_LEFT_PIXEL;
    
    nb_task = DEFAULT_NB_TASK;
    nb_sampling = DEFAULT_NB_SAMPLING;
    
    type_of_space = SAbstractGeometrySpace.LINEAR;
    
    try{
      initialize();
    }catch(SInitializationException e){
      //throw new SConstructorException("Erreur SRaytracerBuilder 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
      throw new SRuntimeException("Erreur SRaytracerBuilder 001 : Une erreur dans un constructeur vide ne doit jamais se produire !!!" + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }

  /**
   * Constructeur d'un constructeur � raytracer � partir d'information lue dans un fichier de format .txt.
   * @param sbr - Le BufferedReader cherchant l'information de le fichier .txt.
   * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
   * @throws SConstructorException Si une erreur est survenue � la construction.
   * @see SBufferedReader
   */
  public SRaytracerBuilder(SBufferedReader sbr)throws IOException, SConstructorException
  {
    this();   //configuration de base s'il y a des param�tres non d�fini.
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SRaytracerBuilder 002 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());
    }  
  }
  
  /**
   * M�thode pour faire la construction d'un raytracer � partir de plusieurs param�tres.
   * @param viewport - Le viewport.
   * @param camera - La cam�ra.
   * @param geometry_space - L'espace des g�om�tries de la sc�ne. 
   * @param light_list - La liste des lumi�res de la sc�ne.
   * @return Le raytracer pouvant faire le rendu de la sc�ne.
   * @throws SRuntimeException Si le code associ� � l'espace des g�om�tries n'est pas reconnu par le syst�me.
   */
  public SRaytracer buildRaytracer(SViewport viewport, SCamera camera, List<SGeometry> geometry_list, List<SLight> light_list) throws SRuntimeException
  {
    //Construction de l'espace des g�om�tries
    SGeometrySpace geometry_space;
    
    switch(type_of_space)
    {
      case SAbstractGeometrySpace.LINEAR : geometry_space = new SLinearSpace(); break;
      
      case SAbstractGeometrySpace.VOXEL : geometry_space = new SVoxelSpace(); break;
         
      case SAbstractGeometrySpace.MULTI_VOXEL : geometry_space = new SMultiVoxelSpace(); break;
      
      default : throw new SRuntimeException("Erreur SRaytracerBuilder 003 : Le type d'espace de code '" + type_of_space + "' n'est pas reconnu par le syst�me.");
    }
    
    geometry_space.addGeometry(geometry_list);  //ajouter les g�om�tries � l'espace des g�om�tries
    geometry_space.initialize();                //faire l'initialisation de l'espace (pr�calcul pour acc�l�rer les calculs d'intersection)
    
    //Construction du shader
    SShader shader;
    
    if(recursive_level == 1)
      shader = new SPhongReflexionShader(geometry_space, camera.getZFar(), light_list, reflexion_algorithm);
    else
      shader = new SRecursiveShader(geometry_space, camera.getZFar(), light_list, reflexion_algorithm, recursive_level);
    
    //Construction du view frustum
    SViewFrustum view_frustum = new SViewFrustum(camera, viewport, pixel_internal_coordinate);
    
    //Construction du raytracer ad�quat
    if(nb_sampling == 1 && nb_task == 1)
      return new SSingleCastRaytracer(view_frustum, shader, viewport);
    else
      return new SThreadPoolCastRaytracer(view_frustum, shader, viewport, nb_sampling, nb_task);
//      return new SMultiCastRaytracer(view_frustum, shader, viewport, nb_sampling, nb_task);
  }
  
  /* (non-Javadoc)
   * @see sim.util.SWriteable#write(java.io.BufferedWriter)
   */
  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_RAYTRACER);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //�crire les param�tres de la classe SPrimitive
    writeSRaytracerBuilderParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }

  /**
   * M�thode pour �crire les param�tres associ�s � la classe SRaytracerBuilder et ses param�tres h�rit�s.
   * 
   * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException Si une erreur I/O s'est produite.
   * @see IOException
   */
  protected void writeSRaytracerBuilderParameter(BufferedWriter bw)throws IOException
  {
    bw.write(SKeyWordDecoder.KW_TASK);
    bw.write("\t\t\t");
    bw.write(Integer.toString(nb_task));
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_SAMPLING);
    bw.write("\t\t");
    bw.write(Integer.toString(nb_sampling));
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_RECURSIVE_LEVEL);
    bw.write("\t\t");
    bw.write(Integer.toString(recursive_level));
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_REFLEXION_ALGORITHM);
    bw.write("\t");
    bw.write(SAbstractShader.REFLEXION_ALGORITHM[reflexion_algorithm]);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_PIXEL_COORDINATE);
    bw.write("\t");
    bw.write(SViewFrustum.PIXEL_COORDINATE[pixel_internal_coordinate]);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_SPACE);
    bw.write("\t\t\t");
    bw.write(SAbstractGeometrySpace.TYPE_OF_SPACE[type_of_space]);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_COLOR_NORMALIZATION);
    bw.write("\t");
    bw.write(SColor.COLOR_NORMALIZATION[SColor.getColorNormalization()]);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
  /**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    
  }

  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException, IOException
  {
    switch(code)
    {
      case SKeyWordDecoder.CODE_TASK :                nb_task = readIntGreaterThanZero(remaining_line, SKeyWordDecoder.KW_TASK); return true;
      
      case SKeyWordDecoder.CODE_SAMPLING :            nb_sampling = readIntGreaterThanZero(remaining_line, SKeyWordDecoder.KW_SAMPLING); return true;
                         
      case SKeyWordDecoder.CODE_RECURSIVE_LEVEL :     recursive_level = readIntGreaterThanZero(remaining_line, SKeyWordDecoder.KW_RECURSIVE_LEVEL); return true;
      
      case SKeyWordDecoder.CODE_REFLEXION_ALGORITHM : reflexion_algorithm = readIntOrExpression(remaining_line, SKeyWordDecoder.KW_REFLEXION_ALGORITHM, SAbstractShader.REFLEXION_ALGORITHM); return true;
      
      case SKeyWordDecoder.CODE_PIXEL_COORDINATE :    pixel_internal_coordinate = readIntOrExpression(remaining_line, SKeyWordDecoder.KW_PIXEL_COORDINATE, SViewFrustum.PIXEL_COORDINATE); return true;
      
      case SKeyWordDecoder.CODE_SPACE :               type_of_space = readIntOrExpression(remaining_line, SKeyWordDecoder.KW_SPACE, SAbstractGeometrySpace.TYPE_OF_SPACE); return true;
        
      case SKeyWordDecoder.CODE_COLOR_NORMALIZATION : SColor.setColorNormalization(readIntOrExpression(remaining_line, SKeyWordDecoder.KW_COLOR_NORMALIZATION, SColor.COLOR_NORMALIZATION)); return true;
      
      default : return false;
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
    return SKeyWordDecoder.KW_RAYTRACER;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
}//fin classe SRaytracerBuilder
