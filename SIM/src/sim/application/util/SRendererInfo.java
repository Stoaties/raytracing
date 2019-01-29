/**
 * 
 */
package sim.application.util;

import java.io.IOException;

import sim.graphics.SScene;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SLog;
import sim.util.SStringUtil;

/**
 * La classe <b>SRendererInfo</b> repr�sente un g�n�rateur d'information d�crivant un mode d'usage de l'application SIMRenderer.
 * Cette classe g�n�re un text permettant d'�claicir le mode de fonctionnement des fichiers suivants :
 * <ul>- Description g�n�rale de l'application</ul>
 * <ul>- Objectif de l'application</ul>
 * <ul>- Le fichier configuration.cfg</ul>
 * <ul>- Le fichier log.txt</ul>
 * <ul>- La structure d'une fichier de scene (ex : scene.txt)</ul>
 * 
 * @author Simon V�zina
 * @since 2016-02-28
 * @version 2016-05-20
 */
public class SRendererInfo {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>NB_KEY_WORD_PER_LINE</b> correspond au nombre de mots cl�s qui seront �crit par ligne de texte �tant �gal � {@value}.
   */
  private static int NB_KEY_WORD_PER_LINE = 5;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un g�n�rateur d'information sur l'application SIMRenderer. 
   */
  public SRendererInfo()
  {
    
  }

  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour faire l'�criture de la sc�ne par d�faut.
   */
  public void writeDefaultScene()
  {
    try{   
      // Chargement de la sc�ne par d�faut
      SScene scene = new SScene(SScene.DEFAULT_SCENE_FILE_NAME); 
      
      SLog.logWriteLine("Message SRendererInfo : �criture du fichier de sc�ne '" + SScene.DEFAULT_SCENE_FILE_NAME + "'.");
      
      // �crire un fichier de sc�ne avec le nom par d�faut
      scene.write(SScene.DEFAULT_SCENE_FILE_NAME);                
     
    }catch(IOException e){
      // rien � faire ...
    }
  }
  
  /**
   * M�thode pour faire l'affichage d'un message d'information en lien avec l'application SIMRenderer.
   */
  public void writeInfo()
  {
    SLog.logWriteLine("Bienvenu dans l'application 'info' de SIMRenderer.");
    SLog.logWriteLine();
    
    SLog.logWriteLine("Description de l'application SIMRenderer :");
    SLog.logWriteLine("Cette application est un g�n�rateur d'image de synth�se � partir d'une sc�ne d�crite en 3D � l'aide d'un algorithme de ray tracing.");
    SLog.logWriteLine("Un ray tracer permet de lancer des rayons depuis la position d'une cam�ra dans l'ensemble des pixels d'un �cran situ� dans la sc�ne.");
    SLog.logWriteLine("� partir de calcul d'intersection entre le rayon et les g�om�tries de la sc�ne, on peut �valuer la visibilit� de ceux-ci.");
    SLog.logWriteLine("La couleur attribu�e � tous les pixels d�pend d'un algorithme d'illumination pseudo-physique.");
    SLog.logWriteLine("En attribuant des mat�riaux aux g�om�tries et en sp�cifiant l'�clairage de la sc�ne, on peut g�n�rer une image 2D de grande qualit�.");
    SLog.logWriteLine();
    
    SLog.logWriteLine("Objectif de l'application SIMRenderer :");
    SLog.logWriteLine("L'application est avant tout une plate-forme d'apprentissage en lien avec les math�matiques, la physique et l'informatique.");
    SLog.logWriteLine("Ce programme n'est pas d'int�r�t commercial ni industriel.");
    SLog.logWriteLine("Pour toute question � ce sujet, veuillez contacter Simon V�zina (svezina@cmaisonneuve.qc.ca), l'auteur de ce programme et de ce projet �ducatif."); 
    SLog.logWriteLine();
    
    SLog.logWriteLine();
    SLog.logWriteLine();
    
    SLog.logWriteLine("Information sur le fichier 'configuration.cfg' :");
    SLog.logWriteLine("Dans la variable 'application', vous pouvez choisir parmi les applications suivantes : " + SStringUtil.join(SConfiguration.APPLICATION_TYPE, ", "));
    SLog.logWriteLine("Dans la variable 'read_data', vous devez pr�ciser le nom du fichier de sc�ne que vous voulez charger. (ex : scene.txt)");
    SLog.logWriteLine();
    
    SLog.logWriteLine("Information sur le fichier 'log.txt' :");
    SLog.logWriteLine("Ce fichier contient des informations d�crivant le fonctionnement de la derni�re ex�cution du programme.");
    SLog.logWriteLine("Ce fichier doit �tre consult� si une erreur est survenue et que l'on cherche de l'information � ce sujet.");
    SLog.logWriteLine();
    
    SLog.logWriteLine("Information sur les fichiers de sc�ne :");
    SLog.logWriteLine("Ce type de fichier contient la description compl�te de la sc�ne 3D.");
    SLog.logWriteLine("Il reconnait un ensemble de " + SKeyWordDecoder.getNbKeyWord() + " mots cl�s pouvant �tre utilis�s pour construire ou d�finir des objets.");
    SLog.logWriteLine("Certains de ces mots cl�s se retrouvent en version 'fran�aise' et 'anglaise'.");
    SLog.logWriteLine("Pour modifier un fichier de sc�ne, il suffit d'�crire un 'mot cl�' pour construire un objet et de d�finir ses attribues avec d'autres mots cl�.");
    SLog.logWriteLine("Une erreur de lecture engendra un message d'erreur pr�cisant la nature de l'erreur et proposera des mots cl�s valides.");
    SLog.logWriteLine();
    
    SLog.logWriteLine("Liste des mots cl�s reconnus par le syst�me :");
    writeKeyWord();
    
    SLog.logWriteLine("Fin de l'application info.");
    SLog.logWriteLine();
  }
  
  /**
   * M�thode pour faire l'�criture des mots cl�s reconnus par la classe SKeyWordDecoder.
   */
  private static void writeKeyWord()
  {
    String[] tab = SKeyWordDecoder.getKeyWord();
    
    int j = 0;
    
    for(int i = 0; i < tab.length; i++)
    {
      SLog.logWrite(tab[i] + ", ");
      j++;
      
      // �crire 
      if(j > NB_KEY_WORD_PER_LINE)
      {
        SLog.logWriteLine();
        j = 0;
      }
    }
    SLog.logWriteLine();
    SLog.logWriteLine();
  }
  
}//fin de la classe SRendererInfo
