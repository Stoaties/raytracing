/**
 * 
 */
package sim.readwrite;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import sim.util.SLog;
import sim.util.SStringUtil;

/**
 * La classe abstraite <b>SAbstractReadableWriteable</b> repr�sente un objet pouvant 
 * <ul>- �tre initialis� par une lecture dans un fichier txt.</ul>
 * <ul>- �crire ses propri�t�s dans le format de la lecture dans un fichier txt.</ul>
 * 
 * @author Simon V�zina
 * @since 2015-01-10
 * @version 2017-05-31
 * @see SReadable
 * @see SWriteable
 */
public abstract class SAbstractReadableWriteable extends SAbstractReadable implements SWriteable {

  /**
   * M�thode pour �crire l'objet <b>SAbstractReadableWriteable</b> dans un fichier txt.
   * Le d�tail de ce qui sera �crit est d�fini par l'impl�mentation de la m�thode write(BufferedWriter bw)
   * de l'interface <b>SWriteable</b>.
   * 
   * @param file_name - Le nom du fichier o� sera �crit l'objet.
   */
  public void write(String file_name)
  {
    try{
      FileWriter fw = new FileWriter(file_name);
      BufferedWriter bw = new BufferedWriter(fw);
    
      write(bw);
    
      bw.close(); //  fermer celui-ci en premier, sinon, ERROR !!!
      fw.close();   
    }catch(IOException ioe){
      SLog.logWriteLine("Message SAbstractReadableWriteable - Une erreur de type I/O est survenue.");
      ioe.printStackTrace();
    }
  }
  
  /**
   * M�thode pour �crire une ligne de commentaire dans le fichier.
   * 
   * @param bw - Le buffer d'�criture dans le fichier.
   * @param comment - Le commentaire � �crire.
   * @throws IOException S'il y a une erreur de type I/O qui est survenue.
   */
  protected void writeComment(BufferedWriter bw, String comment) throws IOException
  {
    bw.write("/*" + SStringUtil.END_LINE_CARACTER);
    bw.write("   " + comment + SStringUtil.END_LINE_CARACTER);
    bw.write("*/" + SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
}//fin classe abstraite SAbstractReadableWriteable
