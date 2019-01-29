/**
 * 
 */
package sim.graphics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sim.graphics.material.SDefaultMaterial;
import sim.graphics.material.SMaterial;
import sim.util.SLog;

/**
 * Classe qui permet d'affecter le bon mat�riel � la bonne primitive en fonction du nom du mat�riel demand� par la primitive.
 *
 * @author Simon V�zina
 * @since 2015-07-31
 * @version 2015-07-31
 */
public class SMaterialAffectation {

  private final List<SPrimitive> primitive_list;  //liste des primitives que l'on doit affecter un mat�riel
  private final List<SMaterial> material_list;    //liste des mat�riaux disponibles pour l'affectation
  
  /**
   * Constructeur de l'assignateur de mat�riel aux primitives.
   * @param primitive_list - La liste des primitives qui doivent subir une affectation d'un mat�riel.
   * @param material_list - La liste des mat�riaux disponibles pour l'affectation.
   */
  public SMaterialAffectation(List<SPrimitive> primitive_list, List<SMaterial> material_list)
  {
    this.primitive_list = primitive_list;
    this.material_list = material_list;
  }
  
  /**
   * M�thode pour r�aliser l'affectation des mat�riaux aux primitives. Si un mat�riau est demand� (par son nom �tant la cl� d'identification)
   * et qu'il n'est pas pr�sent dans la liste, il sera cr�� avec des param�tres par d�faut. Un message sera affich� pour signaler cette situation.
   */
  public void affectation()
  {
    //Remplir une carte de mat�riel avec le nom du mat�riel en cl� d'acc�s.
    //Un message sera envoy� si deux mat�riaux ont le m�me nom.
    Map<String, SMaterial> material_map = new HashMap<String, SMaterial>();
    
    for(SMaterial m : material_list)
      if(material_map.containsKey(m.getName()))
        SLog.logWriteLine("Message SMaterialAffectation : Le nom de mat�riel '" + m.getName() + "' se retrouve deux fois au plus dans la sc�ne. Seule la premi�re d�finition sera affect�e aux primitives.");
      else
        material_map.put(m.getName(), m);
      
    
    //Affecter les mat�riels aux primitives qui n'ont pas de mat�riel (ceux provenant d'un mod�le devrait avoir un mat�riel pr�alablement affect�)
    //Affecter un mat�riel � la primitive si le nom du mat�riel demand� par la primitive se retrouve dans la map.
    //Cr�ation d'un mat�riel par d�faut avec le nom demand� par la primitive si une primitive n'a pas de mat�riel (pour qu'elle soit visible puisqu'elle a une g�om�trie).
    for(SPrimitive p : primitive_list)
      if(material_map.containsKey(p.getMaterialName()))        //v�rifier si le nom du mat�riel d�sir� est pr�alablement disponible
        p.setMaterial(material_map.get(p.getMaterialName()));
      else                                                     
      {
        //Construire un nouveau mat�riel, car celui recherch� n'est pas disponible !
        SLog.logWriteLine("Message SMaterialAffectation : Le mat�riel '" + p.getMaterialName() + "' demand� par une primitive ne se retrouve pas dans la collection des mat�riaux. Un nouveau mat�riel sera cr�� par d�faut.");
          
        //Cr�ation d'un nouveau mat�riel de couleur blanche avec le nom d�sir� par la primitive
        SMaterial new_material = new SDefaultMaterial(p.getMaterialName()); 
          
        //Ajout � la liste des mat�riels (n�cessaire pour la sauvegarde de la sc�ne)
        material_list.add(new_material);
          
        //Ajout de ce mat�riel � la carte temporaire (au cas o� il y aurait d'autres primitives qui voudraient utiliser ce mat�riel).
        material_map.put(new_material.getName(), new_material);
          
        p.setMaterial(new_material);  //Affectation du nouveau mat�riel � la primitive
      }
  }
  
}//fin de la classe SMaterialAffectation
