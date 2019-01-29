/**
 * 
 */
package sim.graphics.light;

import java.io.BufferedWriter;
import java.io.IOException;

import sim.exception.SConstructorException;
import sim.exception.SRuntimeException;
import sim.graphics.SColor;
import sim.math.SImpossibleNormalizationException;
import sim.math.SMath;
import sim.math.SVector3d;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe abstraite <b>SAbstractAttenuatedLight</b> repr�sente une source de lumi�re avec attribue de base comme la couleur et les facteurs d'att�nuation.
 * 
 * @author Simon V�zina
 * @since 2015-01-09
 * @version 2016-02-28
 */
public abstract class SAbstractAttenuatedLight extends SAbstractLight implements SAttenuatedLight{

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = {
    SKeyWordDecoder.KW_POSITION, SKeyWordDecoder.KW_AMPLIFICATION, SKeyWordDecoder.KW_CONSTANT_ATTENUATOR,
    SKeyWordDecoder.KW_LINEAR_ATTENUATOR, SKeyWordDecoder.KW_QUADRATIC_ATTENUATOR
  };
  
  /**
   * La constante <b>DEFAULT_POSITION</b> correspond � la position de la source de lumi�re par d�faut.
   */
	protected static final SVector3d DEFAULT_POSITION = new SVector3d(0.0, 0.0, 0.0);	
	
	/**
	 * La constante <b>DEFAULT_AMPLIFICATION</b> correspond au facteur d'amplification de la srouce de lumi�re par d�faut �tant �gal � {@value}.
	 */
	protected static final double DEFAULT_AMPLIFICATION = 1.0;
	
	//Par d�faut, il n'y a pas d'att�nuation
	protected static final double DEFAULT_CONSTANT_ATTENUATOR = 1.0;						
	protected static final double DEFAULT_LINEAR_ATTENUATOR = 0.0;
	protected static final double DEFAULT_QUADRATIC_ATTENUATOR = 0.0;
	
	/**
	 * La variable <b>position</b> correspond � la position de la source de lumi�re.
	 */
	protected SVector3d position;			
	
	/**
	 * La variable <b>amplification</b> correspond au facteur d'amplification de la source de lumi�re. 
	 */
	protected double amplification;
	
	protected double constant_attenuator;	//constante d'att�nuation constant
	protected double linear_attenuator;		//constante d'att�nuation lin�aire
	protected double quadratic_attenuator;	//constante d'att�nuation quadratique
	
	//-----------------
	// CONSTRUCTEURS //
	//-----------------
	
	/**
	 * Constructeur d'une source de lumi�re blanche sans facteur d'att�nuation positionn� � l'origine.
	 */
	public SAbstractAttenuatedLight()
	{
		this(DEFAULT_LIGHT_COLOR, DEFAULT_POSITION);
	}
	
	/**
	 * Constucteur d'une source de lumi�re avec une couleur particuli�re sans facteur d'att�nuation.
	 * 
	 * @param color La couleur de la source de lumi�re.
	 */
	public SAbstractAttenuatedLight(SColor color, SVector3d position)
	{
		this(color, position, DEFAULT_AMPLIFICATION, DEFAULT_CONSTANT_ATTENUATOR, DEFAULT_LINEAR_ATTENUATOR, DEFAULT_QUADRATIC_ATTENUATOR);
	}
	
	/**
	 * Constructeur d'une source de lumi�re.
	 * 
	 * @param color La couleur de la source de lumi�re.
	 * @param position La positon de la source de lumi�re.
	 * @param amp Le facteur d'amplification de la source de lumi�re.
	 * @param cst_att La constante d'att�nuation � taux constant.
	 * @param lin_att La constante d'att�nuation � taux lin�aire.
	 * @param quad_att La constante d'att�nuation � taux quadratique.
	 * @throws SConstructorException Si des facteurs d'att�nuation sont initialis�s avec des valeurs erron�es.
	 */
	public SAbstractAttenuatedLight(SColor color, SVector3d position, double amp, double cst_att, double lin_att, double quad_att) throws SConstructorException
	{
		super(color);
		
		if(amp < 0)
		  throw new SConstructorException("Erreur SAbstractAttenuatedLight 001 : Le facteur d'amplification '" + amp + "' + doit �tre positive.");
    
		// Validation des coefficients d'att�nuation
		if(cst_att < 0)
			throw new SConstructorException("Erreur SAbstractAttenuatedLight 002 : La constante d'att�nuation constante doit �tre positive (c_cst > 0).");
				
		if(lin_att < 0)
			throw new SConstructorException("Erreur SAbstractAttenuatedLight 003 : La constante d'att�nuation lin�aire doit �tre positive (C_lin�aire > 0).");
		
		if(quad_att < 0)
			throw new SConstructorException("Erreur SAbstractAttenuatedLight 004 : La constante d'att�nuation quadratique doit �tre positive (C_quad > 0).");
				
		this.position = position;
		
		amplification = amp;
		constant_attenuator = cst_att;
		linear_attenuator = lin_att;
		quadratic_attenuator = quad_att;
		
		try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SAbstractAttenuatedLight 005 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
	}
	
	//------------
	// M�THODES //
	//------------
	
	@Override
	public SVector3d getPosition()
	{ 
	  return position;
	}
	
	@Override
  public double attenuation(SVector3d position_to_illuminate) throws SRuntimeException
  {
    // �valuer la distance entre la source et le point � illuminer.
	  double distance = (position.substract(position_to_illuminate)).modulus();
    
	  // �valuer le facteur d'att�nuation (qui doit �tre sup�rieur � z�ro).
    double factor = constant_attenuator + linear_attenuator*distance + quadratic_attenuator*distance*distance;
    
    // V�rifier que le facteur d'att�nuation n'est pas �gal � z�ro.
    if(factor < SMath.EPSILON)
      throw new SRuntimeException("Erreur SAbstractAttenuatedLight 006 : Le facteur d'att�nuation '" + factor + "' est trop petit.");
    
    return 1.0 / factor;
  }
	
	@Override
	public double amplification()
	{
	  return amplification;
	}
	
	@Override
  public SVector3d getOrientation(SVector3d position_to_illuminate) throws SRuntimeException
  { 
    SVector3d orientation = position_to_illuminate.substract(position);
    
    try{
      return orientation.normalize(); 
    }catch(SImpossibleNormalizationException e){
      // ce cas sera observ� si le point � illuminer est situ� sur la source de lumi�re
      throw new SRuntimeException("Erreur SAbstractAttenuatedLightt 007 : La cible � illuminer est situ�e sur la source de lumi�re ponctuelle." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
  }
	
	@Override
	protected boolean read(SBufferedReader sbr, int code, String remaining_line) throws SReadingException 
	{
	  switch(code)
		{
		  case SKeyWordDecoder.CODE_POSITION :              position = new SVector3d(remaining_line); return true;
											
		  case SKeyWordDecoder.CODE_AMPLIFICATION :         amplification = readDoubleEqualOrGreaterThanValue(remaining_line, 0.0, SKeyWordDecoder.KW_AMPLIFICATION); return true;
		  
			case SKeyWordDecoder.CODE_CONSTANT_ATTENUATOR : 	constant_attenuator = readDoubleEqualOrGreaterThanValue(remaining_line, 0.0, SKeyWordDecoder.KW_CONSTANT_ATTENUATOR); return true;
																	
			case SKeyWordDecoder.CODE_LINEAR_ATTENUATOR :		  linear_attenuator = readDoubleEqualOrGreaterThanValue(remaining_line, 0.0, SKeyWordDecoder.KW_LINEAR_ATTENUATOR); return true;
																	
			case SKeyWordDecoder.CODE_QUADRATIC_ATTENUATOR :	quadratic_attenuator = readDoubleEqualOrGreaterThanValue(remaining_line, 0.0, SKeyWordDecoder.KW_QUADRATIC_ATTENUATOR); return true;
				
			// Lecture des param�tres de la classe h�rit� SAbstractLight 
			default : return super.read(sbr, code, remaining_line); 
		}	
	}
	
	/**
	 * M�thode pour �crire les param�tres associ�s � la classe SAbstractAttenuatedLight et ceux qu'il h�rite.
	 * @param bw Le BufferedWriter �crivant l'information dans un fichier txt.
	 * @throws IOException S'il y a une erreur d'�criture.
	 * @see IOException
	 */
	protected void writeSAbstractAttenuatedLightParameter(BufferedWriter bw)throws IOException
	{
		//�crire les param�tres h�rit�s de la classe SAbstractLight
		writeSAbstractLightParameter(bw);
		
		bw.write(SKeyWordDecoder.KW_POSITION);
		bw.write("\t\t");
		position.write(bw);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_AMPLIFICATION);
    bw.write("\t");
    bw.write(Double.toString(amplification));
    bw.write(SStringUtil.END_LINE_CARACTER);
    
		bw.write(SKeyWordDecoder.KW_CONSTANT_ATTENUATOR);
		bw.write("\t");
		bw.write(Double.toString(constant_attenuator));
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_LINEAR_ATTENUATOR);
		bw.write("\t");
		bw.write(Double.toString(linear_attenuator));
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_QUADRATIC_ATTENUATOR);
		bw.write("\t");
		bw.write(Double.toString(quadratic_attenuator));
		bw.write(SStringUtil.END_LINE_CARACTER);
	}
	
	/**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
  {
    if(constant_attenuator == 0.0 && linear_attenuator == 0.0 && quadratic_attenuator == 0.0)
      throw new SInitializationException("Erreur SAbstractAttenuatedLight 008 : Tous les coefficients d'att�nuation sont �gals � z�ro ce qui rend le calcul de l'att�nuation invalide.");
  }
  
	@Override
  protected void readingInitialization() throws SInitializationException
  {
    super.readingInitialization();
    
    initialize();
  }
	
	@Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
	
}//fin classe abstraite SAbstractLight
