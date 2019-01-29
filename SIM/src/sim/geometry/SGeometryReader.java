/**
 * 
 */
package sim.geometry;

import java.io.IOException;

import sim.exception.SConstructorException;
import sim.exception.SNoImplementationException;
import sim.exception.SRuntimeException;
import sim.graphics.SPrimitive;
import sim.readwrite.SAbstractReadable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SGeometryReader</b> repr�sente un lecteur de g�om�trie. 
 * � partir d'un SBufferedReader et d'un code de r�f�rence,
 * ce lecteur va construire la bonne g�om�trie en fonction des param�tres lus dans le fichier txt. 
 * Si le code de r�f�rence n'est pas reconnu, rien de plus sera lu dans le fichier txt. 
 * 
 * @author Simon V�zina
 * @since 2015-07-18
 * @version 2017-02-10
 */ 
public class SGeometryReader extends SAbstractReadable {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  public static final String[] KEYWORD_PARAMETER = { 
    SKeyWordDecoder.KW_SPHERE, SKeyWordDecoder.KW_PLANE, SKeyWordDecoder.KW_DISK, SKeyWordDecoder.KW_TRIANGLE, 
    SKeyWordDecoder.KW_BTRIANGLE, SKeyWordDecoder.KW_TUBE, SKeyWordDecoder.KW_CYLINDER, SKeyWordDecoder.KW_CONE,
    SKeyWordDecoder.KW_CUBE, SKeyWordDecoder.KW_SPHERICAL_CAP, SKeyWordDecoder.KW_LENS, SKeyWordDecoder.KW_TORUS, 
    SKeyWordDecoder.KW_GEOMETRY 
  };
  
  private static final SGeometry DEFAULT_GEOMETRY = null; //g�om�trie par d�faut �tant non charg�
  
  //-------------
  // VARIABLES //
  //-------------
  
  SGeometry geometry;           //g�om�trie lue
  boolean is_read;              //si la g�om�trie a �t� lue (n'est plus 'null')
  
  SPrimitive primitive_parent;  //parent primitive de la g�om�trie
  
  /**
   * Constructeur d'un constructeur de g�om�trie � partir d'information lue dans un fichier de format txt.
   * @param sbr - Le BufferedReader cherchant l'information dans le fichier txt.
   * @param code - Le code de lecture correspondant � la g�om�trie � lire dans le fichier txt.
   * @param parent - La primitive parent � la g�om�trie qui sera construite en lecture.
   * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
   * @throws SReadingException Si une erreur de lecture a �t� trouv�e.
   */
  public SGeometryReader(SBufferedReader sbr, int code, SPrimitive parent)throws IOException, SReadingException
  {
    geometry = DEFAULT_GEOMETRY;
    is_read = false;
    primitive_parent = parent;
    
    read(sbr, code, "");
  }

  /**
   * M�thode pour obtenir la g�om�trie lue par le lecteur.
   * @return La g�om�trie lue par le lecteur.
   * @throw SRuntimeException S'il n'y a pas eu de g�om�trie lue par le lecteur.
   */
  public SGeometry getGeometry()throws SRuntimeException
  {
    if(is_read)
      return geometry;
    else
      throw new SRuntimeException("Erreur SGeometryReader 001 : Il n'y a pas eu de g�om�trie lu par ce lecteur.");
  }
  
  /**
   * M�thode pour qui d�termine si le lecteur de g�om�trie a lu une g�om�trie.
   * @return <b>true</b> si une g�om�trie a �t� lue et <b>false</b> sinon.
   */
  public boolean isRead()
  {
    return is_read;
  }
  
  /**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    
  }

  /* (non-Javadoc)
   * @see sim.util.SAbstractReadableWriteable#read(sim.util.SBufferedReader, int, java.lang.String)
   */
  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException, IOException
  {
    if(is_read)
      throw new SReadingException("Erreur SGeometryReader 002 : Une g�om�trie a d�j� �t� lue par ce lecteur.");
    else
    { 
      
      try{
        
        //Lecture des diff�rentes g�om�trie reconnues par le lecteur
        switch(code)
        {
          case SKeyWordDecoder.CODE_SPHERE : geometry = new SSphereGeometry(sbr, primitive_parent); break; 
                                             
          case SKeyWordDecoder.CODE_PLANE : geometry = new SPlaneGeometry(sbr, primitive_parent); break; 
                                             
          case SKeyWordDecoder.CODE_DISK : geometry = new SDiskGeometry(sbr, primitive_parent); break;
               
          case SKeyWordDecoder.CODE_TRIANGLE : geometry = new STriangleGeometry(sbr, primitive_parent); break;
            
          case SKeyWordDecoder.CODE_BTRIANGLE : geometry = new SBTriangleGeometry(sbr, primitive_parent); break; 
          
          case SKeyWordDecoder.CODE_TUBE : geometry = new STubeGeometry(sbr, primitive_parent); break;
          
          case SKeyWordDecoder.CODE_CYLINDER : geometry = new SCylinderGeometry(sbr, primitive_parent); break; 
              
          case SKeyWordDecoder.CODE_CONE : geometry = new SConeGeometry(sbr, primitive_parent); break; 
          
          case SKeyWordDecoder.CODE_CUBE : geometry = new SCubeGeometry(sbr, primitive_parent); break; 
          
          case SKeyWordDecoder.CODE_SPHERICAL_CAP : geometry = new SSphericalCapGeometry(sbr, primitive_parent); break;
          
          case SKeyWordDecoder.CODE_LENS : geometry = new SLens(sbr, primitive_parent); break;
          
          case SKeyWordDecoder.CODE_TORUS : geometry = new STorusGeometry(sbr, primitive_parent); break;
          
          // La g�om�trie transformable
          case SKeyWordDecoder.CODE_GEOMETRY : geometry = new STransformableGeometry(sbr, primitive_parent); break;
          
          default : return false;
        }
        
        is_read = true;
        return true;
        
      }catch(SConstructorException e){
        // Une erreur est survenue lors de la construction d'une g�om�trie.
        throw new SReadingException("Erreur SGeometryReader 003 : Une g�om�trie n'a pas �t� construite avec succ�s." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
      }
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
    throw new SNoImplementationException("Erreur SGeometryReader 004 : Cette m�thode ne retourne pas de mot cl�, car cet objet ne peut pas �tre construit par l'appel d'un mot cl�.");
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    return KEYWORD_PARAMETER;
  }
  
}//fin de la classe SGeometryReader
