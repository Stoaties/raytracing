/**
 * 
 */
package sim.graphics;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SLog;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>STextureComparator</b> repr�sente un comparateur de texture.
 * 
 * @author Simon V�zina
 * @since 2016-01-11
 * @version 2016-01-12
 */
public class STextureComparator extends SAbstractReadableWriteable {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_FILE };
  
  //-------------
  // VARIABLES //
  //-------------
  
  /**
   * La variable <b>file_name1</b> repr�sente le nom du fichier #1 qui sera interpr�t� comme une texture et qui sera compar�.
   */
  private String file_name1;
  
  /**
   * La variable <b>file_name2</b> repr�sente le nom du fichier #2 qui sera interpr�t� comme une texture et qui sera compar�.
   */
  private String file_name2;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * 
   */
  public STextureComparator()
  {
    this(null, null);
  }
  
  /**
   * ...
   * 
   * @param file_name1
   * @param file_name2
   */
  public STextureComparator(String file_name1, String file_name2)
  {
    this.file_name1 = file_name1;
    this.file_name2 = file_name2;
  }

 
  /**
   * Constructeur d'un comparateur de texture � partir d'information lue dans un fichier de format .txt.
   * 
   * @param sbr - Le BufferedReader cherchant l'information de le fichier .txt.
   * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
   * @throws SConstructorException Si une erreur est survenue � la construction.
   * @see SBufferedReader
   */
  public STextureComparator(SBufferedReader sbr)throws IOException, SConstructorException
  {
    this();   //configuration de base s'il y a des param�tres non d�fini.
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur STextureComparator 001 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());
    }  
  }
  
  //------------
  // M�THODES //
  //------------
  
  /**
   * ...
   * 
   * @return
   */
  public String getFileName1()
  {
    return file_name1;
  }
  
  /**
   * ...
   * 
   * @return
   */
  public String getFileName2()
  {
    return file_name2;
  }
  
  /**
   * M�thode pour identifier si deux fichiers peuvent �tre interpr�t�s comme �tant deux textures identiques.
   * 
   * @return <b>true</b> si les deux fichiers g�n�rent deux textures identiques et <b>false</b> sinon. Deux textures <b>null</b> seront consid�r�s automatiquement non identique.
   * @throws SRuntimeException Si les noms de fichier ne peuvent pas g�n�rer de texture.
   */
  public boolean isTextureEquals() throws SRuntimeException
  {
    if(file_name1 == null)
      throw new SRuntimeException("Erreur STextureComparator 002 : Le premier fichier � comparer est 'null'.");
    
    if(file_name2 == null)
      throw new SRuntimeException("Erreur STextureComparator 003 : Le premier fichier � comparer est 'null'.");
    
    STexture t1 = loadTexture(file_name1);
    STexture t2 = loadTexture(file_name2);
    
    // Comparer les deux textures si elles ne sonts
    if(t1 == null || t2 == null)
      throw new SRuntimeException("Erreur STextureComparator 004 : Une ou plusieurs textures sont 'null'.");
    else
      return t1.equals(t2);
    
  }
  
  /**
   * M�thode pour faire la construction d'une texture � partir d'un nom de fichier.
   * S'il n'y a pas d'interpr�tation valide de texture � partir du fichier, une valeur <b>null</b> sera retourn�e.
   * 
   * @param file_name - Le nom du fichier qui sera utilis� pour g�n�rer la texture.
   * @return La texture g�n�r� ou <b>null</b> s'il n'y a pas de texture g�n�r�e.
   */
  private STexture loadTexture(String file_name)
  {
    // Interpr�ter le fichier comme une sc�ne o� un calcul de ray tracing sera requis.
    if(SStringUtil.extensionFileLowerCase(file_name).equals("txt"))
      return loadTextureFromRaytracing(file_name);
    else
    {
      // Interpr�ter le fichier comme un fichier image
      STextureReader reader = new STextureReader(file_name);
      
      if(reader.asRead())
        return reader.getValue();
      else
        return null;
    }
  }
  
  /**
   * M�thode pour faire la construction d'une texture � partir du rendu d'une sc�ne par ray tracing.
   * S'il y a une erreur lors du rendu de la sc�ne, une texture <b>null</b> sera retourn�e.
   * 
   * @param file_name - Le nom du fichier de la sc�ne.
   * @return Une texture qui sera le r�sultat d'un calcul de ray tracing d'un sc�ne ou <b>null</b> s'il n'y a pas de texture g�n�r�e.
   */
  private STexture loadTextureFromRaytracing(String file_name)
  {
    try{
      
      SScene scene = new SScene(file_name);
      
      SRaytracer raytracer = scene.buildRaytracer();
      
      raytracer.raytrace();
      
      return new STexture(file_name, scene.getViewport().getBufferedImage());
      
    }catch(FileNotFoundException e){
      SLog.logWriteLine("Message STextureComparator : Le fichier de sc�ne '" + file_name + "' n'a pas �t� trouv�." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());
    }catch(SConstructorException e){
      SLog.logWriteLine("Message STextureComparator : La sc�ne de fichier '" + file_name + "' n'a pas �t� construite en raison d'une erreur de construction." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage());  
    }catch(IOException e){
      SLog.logWriteLine("Message STextureComparator : Une erreur de type I/O est survenue lors de la lecture du fichier de sc�ne '" + file_name + "'.");
    }
    
    // La texture issue de la sc�ne n'a pas �t� construite
    return null;
  }
  
  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_TEXTURE_COMPARATOR);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //�crire les param�tres de la classe SPrimitive
    writeSTextureComparatorParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }

  /**
   * M�thode pour �crire les param�tres associ�s � la classe STextureComparator et ses param�tres h�rit�s.
   * 
   * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException Si une erreur I/O s'est produite.
   * @see IOException
   */
  protected void writeSTextureComparatorParameter(BufferedWriter bw)throws IOException
  {
    bw.write(SKeyWordDecoder.KW_FILE);
    bw.write("\t\t");
    bw.write(file_name1);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_FILE);
    bw.write("\t\t");
    bw.write(file_name2);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }  
    
  @Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_TEXTURE_COMPARATOR;
  }

  @Override
  protected void readingInitialization() throws SInitializationException
  {
    
  }

  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException, IOException
  {
    switch(code)
    {
      case SKeyWordDecoder.CODE_FILE :  String file_name = this.readStringNotEmpty(remaining_line, SKeyWordDecoder.KW_FILE);
      
                                        if(file_name1 == null)
                                          file_name1 = file_name;
                                        else
                                          if(file_name2 == null)
                                            file_name2 = file_name;
                                          else
                                            throw new SReadingException("Erreur STextureComparator 004 : Il y a d�j� deux fichiers en comparaison.");
                                        
                                        return true;
      default : return false;
    }
  }

  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
}//fin de la classe STextureComparator
