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
 * La classe <b>SRendererInfo</b> représente un générateur d'information décrivant un mode d'usage de l'application SIMRenderer.
 * Cette classe génère un text permettant d'éclaicir le mode de fonctionnement des fichiers suivants :
 * <ul>- Description générale de l'application</ul>
 * <ul>- Objectif de l'application</ul>
 * <ul>- Le fichier configuration.cfg</ul>
 * <ul>- Le fichier log.txt</ul>
 * <ul>- La structure d'une fichier de scene (ex : scene.txt)</ul>
 * 
 * @author Simon Vézina
 * @since 2016-02-28
 * @version 2016-05-20
 */
public class SRendererInfo {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>NB_KEY_WORD_PER_LINE</b> correspond au nombre de mots clés qui seront écrit par ligne de texte étant égal à {@value}.
   */
  private static int NB_KEY_WORD_PER_LINE = 5;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'un générateur d'information sur l'application SIMRenderer. 
   */
  public SRendererInfo()
  {
    
  }

  //------------
  // MÉTHODES //
  //------------
  
  /**
   * Méthode pour faire l'écriture de la scène par défaut.
   */
  public void writeDefaultScene()
  {
    try{   
      // Chargement de la scène par défaut
      SScene scene = new SScene(SScene.DEFAULT_SCENE_FILE_NAME); 
      
      SLog.logWriteLine("Message SRendererInfo : Écriture du fichier de scène '" + SScene.DEFAULT_SCENE_FILE_NAME + "'.");
      
      // Écrire un fichier de scène avec le nom par défaut
      scene.write(SScene.DEFAULT_SCENE_FILE_NAME);                
     
    }catch(IOException e){
      // rien à faire ...
    }
  }
  
  /**
   * Méthode pour faire l'affichage d'un message d'information en lien avec l'application SIMRenderer.
   */
  public void writeInfo()
  {
    SLog.logWriteLine("Bienvenu dans l'application 'info' de SIMRenderer.");
    SLog.logWriteLine();
    
    SLog.logWriteLine("Description de l'application SIMRenderer :");
    SLog.logWriteLine("Cette application est un générateur d'image de synthèse à partir d'une scène décrite en 3D à l'aide d'un algorithme de ray tracing.");
    SLog.logWriteLine("Un ray tracer permet de lancer des rayons depuis la position d'une caméra dans l'ensemble des pixels d'un écran situé dans la scène.");
    SLog.logWriteLine("À partir de calcul d'intersection entre le rayon et les géométries de la scène, on peut évaluer la visibilité de ceux-ci.");
    SLog.logWriteLine("La couleur attribuée à tous les pixels dépend d'un algorithme d'illumination pseudo-physique.");
    SLog.logWriteLine("En attribuant des matériaux aux géométries et en spécifiant l'éclairage de la scène, on peut générer une image 2D de grande qualité.");
    SLog.logWriteLine();
    
    SLog.logWriteLine("Objectif de l'application SIMRenderer :");
    SLog.logWriteLine("L'application est avant tout une plate-forme d'apprentissage en lien avec les mathématiques, la physique et l'informatique.");
    SLog.logWriteLine("Ce programme n'est pas d'intérêt commercial ni industriel.");
    SLog.logWriteLine("Pour toute question à ce sujet, veuillez contacter Simon Vézina (svezina@cmaisonneuve.qc.ca), l'auteur de ce programme et de ce projet éducatif."); 
    SLog.logWriteLine();
    
    SLog.logWriteLine();
    SLog.logWriteLine();
    
    SLog.logWriteLine("Information sur le fichier 'configuration.cfg' :");
    SLog.logWriteLine("Dans la variable 'application', vous pouvez choisir parmi les applications suivantes : " + SStringUtil.join(SConfiguration.APPLICATION_TYPE, ", "));
    SLog.logWriteLine("Dans la variable 'read_data', vous devez préciser le nom du fichier de scène que vous voulez charger. (ex : scene.txt)");
    SLog.logWriteLine();
    
    SLog.logWriteLine("Information sur le fichier 'log.txt' :");
    SLog.logWriteLine("Ce fichier contient des informations décrivant le fonctionnement de la dernière exécution du programme.");
    SLog.logWriteLine("Ce fichier doit être consulté si une erreur est survenue et que l'on cherche de l'information à ce sujet.");
    SLog.logWriteLine();
    
    SLog.logWriteLine("Information sur les fichiers de scène :");
    SLog.logWriteLine("Ce type de fichier contient la description complète de la scène 3D.");
    SLog.logWriteLine("Il reconnait un ensemble de " + SKeyWordDecoder.getNbKeyWord() + " mots clés pouvant être utilisés pour construire ou définir des objets.");
    SLog.logWriteLine("Certains de ces mots clés se retrouvent en version 'française' et 'anglaise'.");
    SLog.logWriteLine("Pour modifier un fichier de scène, il suffit d'écrire un 'mot clé' pour construire un objet et de définir ses attribues avec d'autres mots clé.");
    SLog.logWriteLine("Une erreur de lecture engendra un message d'erreur précisant la nature de l'erreur et proposera des mots clés valides.");
    SLog.logWriteLine();
    
    SLog.logWriteLine("Liste des mots clés reconnus par le système :");
    writeKeyWord();
    
    SLog.logWriteLine("Fin de l'application info.");
    SLog.logWriteLine();
  }
  
  /**
   * Méthode pour faire l'écriture des mots clés reconnus par la classe SKeyWordDecoder.
   */
  private static void writeKeyWord()
  {
    String[] tab = SKeyWordDecoder.getKeyWord();
    
    int j = 0;
    
    for(int i = 0; i < tab.length; i++)
    {
      SLog.logWrite(tab[i] + ", ");
      j++;
      
      // Écrire 
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
