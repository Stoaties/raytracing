/**
 * 
 */
package sim.physics;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.math.SVector3d;
import sim.readwrite.SAbstractReadableWriteable;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SParticle</b> repr�sente une particule physique.
 * 
 * @author Simon V�zina
 * @since 2016-02-01
 * @version 2017-06-05
 */
public class SParticle extends SAbstractReadableWriteable {

  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { SKeyWordDecoder.KW_POSITION, SKeyWordDecoder.KW_ELECTRIC_CHARGE };
  
  /**
   * La constante <b>DEFAULT_POSITION</b> repr�sente la position par d�faut d'une particule �tant � l'origine.
   */
  private static final SVector3d DEFAULT_POSITION = SVector3d.ORIGIN;
  
  /**
   * La constante <b>DEFAULT_ELECTRIC_CHARGE</b> repr�sente la charge �lectrique par d�faut d'une particule �tant �gale � {@value}.
   */
  private static final double DEFAULT_ELECTRIC_CHARGE = 0.0;
  
  //------------
  // VARIABLE //
  //------------
  
  /**
   * La variable <b>position</b> repr�sente la position d'une particule.
   */
  private SVector3d position;
  
  /**
   * La variable <b>e_charge</b> repr�sente la charge �lectrique d'une particule.
   */
  private double electric_charge;
  
  //----------------
  // CONSTRUCTEUR //
  //----------------
  
  /**
   * Constructeur d'une particule.
   * 
   * @param q La charge de la particule.
   * @param position La position d'une particule.
   */
  public SParticle(double q, SVector3d position)
  {
    this.electric_charge = q;
    this.position = position;
  }

  /**
   * Constructeur d'une particule avec lecture.
   * 
   * @param sbr Le buffer de lecture.
   * @throws IOException Si une erreur de de type I/O est lanc�e.
   * @throws SConstructorException Si une ereur est survenue lors de la construction de la particule.
   */
  public SParticle(SBufferedReader sbr) throws IOException, SConstructorException
  {
    this(DEFAULT_ELECTRIC_CHARGE, DEFAULT_POSITION);
    
    try{
      read(sbr);
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SParticle 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }
  
  //------------
  // M�THODES //
  //------------
  
  /**
   * M�thode pour obtenir la position de la particule.
   * 
   * @return La position de la particule.
   */
  public SVector3d getPosition()
  {
    return position;
  }
  
  /**
   * M�thode pour obtenir la charge �lectrique d'une particule.
   * 
   * @return La charge �lectrique.
   */
  public double getElectricCharge()
  {
    return electric_charge;
  }

  @Override
  public void write(BufferedWriter bw) throws IOException 
  {
    bw.write(SKeyWordDecoder.KW_PARTICLE);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    //�crire les propri�t�s de la classe SSphereGeometry et ses param�tres h�rit�s
    writeSParticleParameter(bw);
    
    bw.write(SKeyWordDecoder.KW_END);
    bw.write(SStringUtil.END_LINE_CARACTER);
    bw.write(SStringUtil.END_LINE_CARACTER);
  }

  /**
   * M�thode pour �crire les param�tres associ�s � la classe SParticle et ses param�tres h�rit�s.
   * @param bw Le BufferedWriter �crivant l'information dans un fichier txt.
   * @throws IOException Si une erreur I/O s'est produite.
   * @see IOException
   */
  protected void writeSParticleParameter(BufferedWriter bw) throws IOException
  {
    bw.write(SKeyWordDecoder.KW_POSITION);
    bw.write("\t");
    position.write(bw);
    bw.write(SStringUtil.END_LINE_CARACTER);
    
    bw.write(SKeyWordDecoder.KW_ELECTRIC_CHARGE);
    bw.write("\t\t");
    bw.write(Double.toString(electric_charge));
    bw.write(SStringUtil.END_LINE_CARACTER);
  }
  
  @Override
  public String getReadableName() 
  {
    return SKeyWordDecoder.KW_PARTICLE;
  }

  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
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
      case SKeyWordDecoder.CODE_POSITION :        position = new SVector3d(remaining_line); return true;
                          
      case SKeyWordDecoder.CODE_ELECTRIC_CHARGE : electric_charge = readDouble(remaining_line, SKeyWordDecoder.KW_ELECTRIC_CHARGE); return true;
          
      default : return false;
    }
  }
  
  @Override
  public String toString() 
  {
    return "SParticle [position=" + position + ", electric_charge=" + electric_charge + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(electric_charge);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((position == null) ? 0 : position.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof SParticle)) {
      return false;
    }
    SParticle other = (SParticle) obj;
    if (Double.doubleToLongBits(electric_charge) != Double.doubleToLongBits(other.electric_charge)) {
      return false;
    }
    if (position == null) {
      if (other.position != null) {
        return false;
      }
    } else if (!position.equals(other.position)) {
      return false;
    }
    return true;
  }
  
}//fin de la classe SParticle
