/**
 * 
 */
package sim.application.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.graphics.STextureComparator;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SFileSearch;
import sim.util.SInitializationException;
import sim.util.SLog;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * <p>
 * La classe <b>SConsoleComparator</b> repr�sente un comparateur d'image. � partir d'une liste � comparer
 * obtenu apr�s la lecture d'un fichier, cette classe va comparer des fichiers pouvant �tre interpr�t�
 * comme une image et d�terminer si deux images sont identiques. 
 * </p>
 * 
 * <p>
 * Une image peut venir d'un fichier image (comme un jpg, png) ou d'un fichier txt d�crivant
 * une sc�ne 3d. Dans ce cas, un calcul de <i>ray tracing</i> sera effectu� avant d'obtenir l'image.
 * </p>
 * 
 * @author Simon V�zina
 * @since 2016-01-14
 * @version 2016-01-18
 */
public class SConsoleComparator extends SAbstractReadableWriteable {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_TEXTURE_COMPARATOR };
 
  //-------------
  // VARIALBES //
  //-------------
  
  /**
   * La variable <b>texture_comparator_list</b> correspond � la liste des comparateurs de textures qui seront r�alis�es par l'application.
   */
  private static final List<STextureComparator> texture_comparator_list = new ArrayList<STextureComparator>();
 
  /**
   * Constructeur d'un comparateur d'image � partir du nom d'un fichier texte o� se retrouve la liste des �l�ments � comparer.
   * 
   * @param file_name - Le nom du ficher d�finissant la liste des �l�ments � comparer.
   * @throws FileNotFoundException Si le fichier de lecture n'est pas trouv�.
   * @throws IOException Si une erreur de type I/O est survenue.
   * @throws SConstructorException Si une erreur est survenue lors de l'initialisation de l'objet.
   */
  public SConsoleComparator(String file_name) throws FileNotFoundException, IOException, SConstructorException
  {
    //Trouver le fichier � partir du r�pertoire o� l'ex�cution de l'application est r�alis�e
    SFileSearch search = new SFileSearch("", file_name);
    
    if(!search.isFileFound())
      throw new FileNotFoundException("Erreur SConsoleComparator 001 : Le fichier '" + file_name + "' n'est pas trouv�.");
    
    if(search.isManyFileFound())
      throw new SConstructorException("Erreur SConsoleComparator : Le fichier '" + file_name + "' a �t� trouv� plus d'une fois dans les diff�rents sous-r�pertoires. Veuillez en garder qu'une seule version.");
    
    //Lecture de la sc�ne � partir d'un fichier
    FileReader fr = new FileReader(search.getFileFoundList().get(0));
    SBufferedReader sbr = new SBufferedReader(fr);
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SConsoleComparator 003 : Une erreur � l'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
      
    sbr.close();
    fr.close();
  }

  /**
   * M�thode pour effectuer la comparaison de l'ensemble des �l�ments de la classe.
   */
  public void compareImage()
  {
    // Tableau des comparaisons
    int[] tab_result = new int[texture_comparator_list.size()];
    
    // Effectuer les comparaisons
    SLog.logWriteLine("Message SIMImageComparator --- Calcul des comparaisons :");
    SLog.logWriteLine();
    
    for(int i = 0; i < texture_comparator_list.size(); i++)
    {
      try{
        
        boolean result = texture_comparator_list.get(i).isTextureEquals();
        
        if(result)
          tab_result[i] = 1;    //code vrai
        else
          tab_result[i] = 0;    //code faux
        
      }catch(SRuntimeException e){
        SLog.logWriteLine("Message SIMImageComparator : Une comparaison n'est pas possible, car certain �l�ment n'ont pas �t� d�fini.");
        
        tab_result[i] = 2;      //code ind�termin�
      }
    }
    
    //Affichage des r�sultats
    SLog.logWriteLine();
    SLog.logWriteLine("Message SIMImageComparator --- R�sultat des comparaisons :");
    SLog.logWriteLine();
    
    for(int i = 0; i < texture_comparator_list.size(); i++)
    {
      SLog.logWriteLine("Comparaison #" + (i+1) + " :");
      SLog.logWriteLine("Fichier 1 - " + texture_comparator_list.get(i).getFileName1());
      SLog.logWriteLine("Fichier 2 - " + texture_comparator_list.get(i).getFileName2());
      
      // L'affichage d'un r�sultat
      if(tab_result[i] == 1)
        SLog.logWriteLine("R�sultat --> �GALES");
      else
        if(tab_result[i] == 0)  
          SLog.logWriteLine("R�sultat --> DIFF�RENTES");
        else
          SLog.logWriteLine("R�sultat --> IND�TERMIN� !");  
      
      SLog.logWriteLine();
    }
    
  }

  @Override
  public void write(BufferedWriter bw) throws IOException
  {
    this.writeComment(bw, "Liste des comparateurs de texture (image et/ou sc�ne).");
    
    for(STextureComparator comp : texture_comparator_list)
      comp.write(bw);
  }

  @Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_CONSOLE_COMPARATOR;
  }
  
  @Override
  protected void readingInitialization() throws SInitializationException
  {
    
  }

  @Override
  protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException, IOException
  {
    try{
      
      switch(code)
      {
        case SKeyWordDecoder.CODE_TEXTURE_COMPARATOR : texture_comparator_list.add(new STextureComparator(sbr)); return true;
        
        default : return false;
      }
      
    }catch(SConstructorException e){
      throw new SReadingException("Erreur SConsoleComparator 004 : Un comparateur de texture n'a pas bien �t� d�fini." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
    
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
}//fin de la classe SConsoleComparator
