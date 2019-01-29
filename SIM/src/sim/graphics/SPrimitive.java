/**
 * 
 */
package sim.graphics;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.geometry.SGeometry;
import sim.geometry.SGeometryReader;
import sim.graphics.material.SDefaultMaterial;
import sim.graphics.material.SMaterial;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SLog;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SPrimitive</b> repr�sente un objet comprenant une g�om�trie et un mat�riel.
 * 
 * @author Simon V�zina
 * @since 2015-01-06
 * @version 2016-05-20
 */
public class SPrimitive extends SAbstractReadableWriteable{

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_MATERIAL_NAME };
  
  //-------------
  // VARIABLES //
  //-------------
  
	private SGeometry geometry;				    //la g�om�trie de la primitive
	private SMaterial material;				    //le mat�riel de la primitive
	
	private String material_name;			    //nom du mat�riel si affectation � l'ext�rieur 
	private boolean isGeometry_selected;	//d�termine si la g�om�trie a �t� s�lectionn� lors de la lecture
	
	/**
	 * Constructeur d'une primitive lors d'une lecture d'un fichier de chargement.
	 */
	private SPrimitive()
	{
	  this(null, null);
	}
	
	/**
	 * Constructeur d'une primitive sans mat�riel.
	 * 
	 * @param geometry La g�om�trie affect�e � la primitive.
	 */
	public SPrimitive(SGeometry geometry)
	{
	  this(geometry, null);
	}
	
	/**
	 * Constructeur avec une g�om�trie et un mat�riel sans relation pr�alable.
	 * @param geometry - La g�om�trie de la primitive.
	 * @param material - Le mat�riel de la primitive.
	 * @throws SConstructorException Si une erreur est survenue lors de la construction.
	 */
	public SPrimitive(SGeometry geometry, SMaterial material) throws SConstructorException
	{
	  // Affectation de la g�om�trie
	  this.geometry = geometry;
	  
	  if(geometry == null)
	    isGeometry_selected = false;
	  else
	  {
	    isGeometry_selected = true;
	    geometry.setPrimitiveParent(this);
	  }
	  
	  // Affectation du mat�riel
	  this.material = material;
	  
	  if(material == null)
	    material_name = SDefaultMaterial.DEFAULT_MATERIAL_NAME;
	  else
	    material_name = material.getName();
	  
	  try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SPrimitive 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
	}
	
	/**
	 * Constructeur d'une primitive � partir d'information lue dans un fichier de format txt.
	 * @param sbr - Le BufferedReader cherchant l'information dans le fichier txt.
	 * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
	 * @throws SConstructorException Si une erreur est survenue � la construction.
	 * @see SBufferedReader
	 */
	public SPrimitive(SBufferedReader sbr)throws IOException, SConstructorException
	{
		this();		
		
		try{
		  read(sbr);
		}catch(SInitializationException e){
      throw new SConstructorException("Erreur SPrimitive 002 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }  
	}
	
	/**
	 * M�thode pour obtenir le nom du mat�riel � attribuer � cette primitive.
	 * @return Le nom de du mat�riel.
	 */
	public String getMaterialName()
	{ 
	  return material_name;
	}
	
	/**
	 * M�thode pour obtenir la g�om�trie de la primitive. 
	 * Il est important de pr�ciser qu'une primitive <b>sans g�om�trie</b> retourne <b>null</b> comme r�f�rence.
	 * 
	 * @return La g�om�trie de la primitive ou <b>null</b> si elle n'en a pas.
	 */
	public SGeometry getGeometry()
	{ 
	  return geometry;
	}
	
	/**
	 * M�thode pour obtenir le mat�riel affect� � la primitive. 
	 * @return Le mat�riel de la primitive.
	 * @throws SRuntimeException Si l'affectation du mat�riel � la primitive n'a pas �t� encore effectu�e. 
	 */
	public SMaterial getMaterial()throws SRuntimeException
	{ 
		if(material == null)
			throw new SRuntimeException("Erreur SPrimitive 004 : L'affectation du mat�riel � cette primitive n'a pas encore �t� r�alis�e.");
		else
			return material;
	}
	
	/**
	 * M�thode pour affecter un mat�riel � une primitive. L'affectation sera r�alis�e si le nom du mat�riel est celui d�fini dans la primitive.
	 * @param new_material - Le mat�riel � affecter � la primitive.
	 * @throws SRuntimeException Si le nom du mat�riel n'est pas celui accept� par la primitive.
	 */
	public void setMaterial(SMaterial new_material)throws SRuntimeException
	{
		//V�rification que le nom du mat�riel accept� par cette primitive est bien celui du nouveau mat�riel en param�tre
		if(new_material.getName().equals(material_name))
			material = new_material;
		else
			throw new SRuntimeException("Erreur SPrimitive 005 : L'affectation de ce mat�riel n'est pas possible puisque son nom n'est pas celui accept� par cette primitive.");
	}
	
	@Override
	public void write(BufferedWriter bw) throws IOException 
	{
		bw.write(SKeyWordDecoder.KW_PRIMITIVE);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		//�crire les param�tres de la classe SPrimitive
		writeSPrimitiveParameter(bw);
		
		bw.write(SKeyWordDecoder.KW_END);
		bw.write("\t#end primitive" + SStringUtil.END_LINE_CARACTER);
		bw.write(SStringUtil.END_LINE_CARACTER);
	}

	/**
	 * M�thode pour �crire les param�tres associ�s � la classe SPrimitive.
	 * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
	 * @throws IOException S'il y a une erreur d'�criture.
	 * @see IOException
	 */
	protected void writeSPrimitiveParameter(BufferedWriter bw)throws IOException 
	{
		bw.write(SKeyWordDecoder.KW_MATERIAL_NAME);
		bw.write("\t");
		bw.write(material_name);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		if(geometry != null)
			geometry.write(bw);
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
	 * @see simTools.SReadableWriteable#read(SBufferedReader sbr, int code, String remaining_line) throws Exception
	 */
	@Override
	protected boolean read(SBufferedReader sbr, int code, String remaining_line)throws SReadingException, IOException
	{
		//Analyser le code sur la lecture d'une g�om�trie � l'aide du lecteur de g�om�trie
	  SGeometryReader reader = new SGeometryReader(sbr, code, this);
    
	  //S'il y a eu lecture d'une g�om�trie
    if(reader.isRead())
      if(!isGeometry_selected)
      {
        geometry = reader.getGeometry();
        isGeometry_selected = true;
        return true;
      }
      else
        throw new SReadingException("Erreur SPrimitive 006 : Une g�om�trie a d�j� �t� s�lectionn�e pour cette primitive.");   
      
	  //Analyser le code sur d'autres param�tres
	  switch(code)
		{
			case SKeyWordDecoder.CODE_MATERIAL_NAME : material_name = readStringNotEmpty(remaining_line, "nom du mat�riel"); return true;
			
			default : return false;
		}
	}
	
	@Override
	protected void readingInitialization() throws SInitializationException
	{
	  initialize();
	  
	  //Affichage de message si la lecture est incompl�te
    if(!isGeometry_selected)
      SLog.logWriteLine("Message SPrimitive : Une primitive a �t� initialis�e sans g�om�trie apr�s une lecture.");
    
    if(material_name.equals(SDefaultMaterial.DEFAULT_MATERIAL_NAME))
      SLog.logWriteLine("Message SPrimitive : Une primitive a �t� initialis�e sans nom de mat�riel apr�s une lecture.");
	}
	 
	@Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_PRIMITIVE;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    // Mettre les mots cl� du lecteur de g�om�trie (SGeometryReader)
    other_parameters = SStringUtil.merge(other_parameters, SGeometryReader.KEYWORD_PARAMETER);
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
}//fin classe SPrimitive
