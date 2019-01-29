/**
 * 
 */
package sim.graphics.material;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import sim.exception.SConstructorException;
import sim.graphics.SColor;
import sim.readwrite.SKeyWordDecoder;
import sim.util.SBufferedReader;
import sim.util.SInitializationException;
import sim.util.SReadingException;
import sim.util.SStringUtil;

/**
 * La classe <b>SBlinnMaterial</b> repr�sente un mat�riel ayant une couleur de base et des coefficients de r�flexion unique pour l'ensemble des canaux rgb de couleur.
 * Ce mat�riel poss�de �galement des caract�ristiques comme le lustrage (<i>shininess</i>), la plasticit�, l'indice de r�fraction.
 * On y retrouve �galement un coefficient de r�flexion <i>kr</i> et de transmission <i>tr</i> lors de l'usage d'un shader r�cursif pour une illumination indirecte.
 * 
 * @author Simon V�zina
 * @since 2015-01-06
 * @version 2015-12-05
 */
public class SBlinnMaterial extends SDefaultMaterial{

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>KEYWORD_PARAMETER</b> correspond � un tableau contenant l'ensemble des mots cl�s 
   * � utiliser reconnus lors de la d�finition de l'objet par une lecture en fichier.
   */
  private static final String[] KEYWORD_PARAMETER = { 
    SKeyWordDecoder.KW_COLOR, SKeyWordDecoder.KW_KA, SKeyWordDecoder.KW_KD,
    SKeyWordDecoder.KW_KS, SKeyWordDecoder.KW_KR, SKeyWordDecoder.KW_KT,
    SKeyWordDecoder.KW_SHININESS, SKeyWordDecoder.KW_PLASTICITY, SKeyWordDecoder.KW_REFRACTIVE_INDEX
  };
  
  /**
   * La constante 'DEFAULT_COLOR' correspond � la couleur du mat�riel par d�faut �tant <b>blanche</b>.
   */
  protected static final SColor DEFAULT_COLOR = new SColor(1.0, 1.0, 1.0);               
  
  /**
   * La constante 'PLASTIC_SPECULAR_REFLEXION' correspond � la couleur d'une r�flexion sp�culaire plastique maximale �tant de couleur <b>blanche</b>.
   */
  private static final SColor PLASTIC_SPECULAR_REFLEXION = new SColor(1.0, 1.0, 1.0);		
	
	//Constante de r�flexion par d�faut
	private static final double DEFAULT_KA = 0.2;							
	private static final double DEFAULT_KD = 0.8;	
	private static final double DEFAULT_KS = 0.5;
	private static final double DEFAULT_KR = 0.0;
	private static final double DEFAULT_KT = 0.0;
	private static final double DEFAULT_SHININESS = 20.0;
	private static final double DEFAULT_PLASTICITY = 0.0;
	private static final double DEFAULT_REFRACTIVE_INDEX = 1.00;   //indice de r�fraction par d�faut (et utilis� pour les mat�riaux opaques)
	
	private SColor color;	//la couleur de base du mat�riel
	
	private double ka;		//constante de r�flexion ambiante
	private double kd;		//constante de r�flexion diffuse
	private double ks;		//constante de r�flexion sp�culaire
		
	private double kr;		//constante de r�flexion pour effet miroir (0 = 0% r�flexion, 1 = 100% r�flexion)
	private double kt;		//constante de transmission (0 = 0% transparent, 1 = 100% transparent)
	
	private double plasticity;	//constante qui correspond au niveau de r�flexion de type plastique(0 = 0% plastique, 1 = 100% plastique). Un plastique a tendance � r�fl�chir de fa�on sp�culaire la couleur de la lumi�re et non la couleur du mat�riel
	private double shininess;   //niveau de brillance du mat�riel (doit �tre positif)
	
	private double refractive_index;	//indice de r�fraction
	
	/**
	 * Constructeur d'une mat�riel avec couleur par d�faut.
	 */
	public SBlinnMaterial()
	{ 
		this(DEFAULT_COLOR);
	}
	
	/**
	 * Constructeur d'un mat�riel avec une couleur s�lectionn�e.
	 * 
	 * @param color - La couleur du mat�riel.
	 * @throws SConstructorException Si une erreur est survenue lors de la construction. 
	 */
	public SBlinnMaterial(SColor color) throws SConstructorException
	{ 
		super();
		
		this.color = color;
		
		ka = DEFAULT_KA;
		kd = DEFAULT_KD;
		ks = DEFAULT_KS;
		kr = DEFAULT_KR;
		kt = DEFAULT_KT;
		
		plasticity = DEFAULT_PLASTICITY;
		shininess = DEFAULT_SHININESS;
		
		refractive_index = DEFAULT_REFRACTIVE_INDEX;
		
		try{
      initialize();
    }catch(SInitializationException e){
      throw new SConstructorException("Erreur SBlinnMaterial 001 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
    }
	}
	
	/**
	 * Constructeur d'un mat�riel � partir d'information lue dans un fichier de format txt.
	 * 
	 * @param br - Le BufferedReader cherchant l'information dans le fichier txt.
	 * @throws IOException Si une erreur de l'objet SBufferedWriter est lanc�e.
	 * @throws SConstructorException Si une erreur est survenue lors de la construction.
	 * @see SBufferedReader
	 */
	public SBlinnMaterial(SBufferedReader br) throws IOException, SConstructorException
	{
		this();		
		
		try{
		  read(br);
		 }catch(SInitializationException e){
	      throw new SConstructorException("Erreur SBlinnMaterial 002 : Une erreur d'initialisation est survenue." + SStringUtil.END_LINE_CARACTER + "\t" + e.getMessage(), e);
	    }  
	}
	
	/* (non-Javadoc)
	 * @see simGraphic.SMaterial#ambientColor()
	 */
	@Override
	public SColor ambientColor() 
	{
		return color.multiply(ka);
	}

	/* (non-Javadoc)
	 * @see simGraphic.SMaterial#diffuseColor()
	 */
	@Override
	public SColor diffuseColor() 
	{
		return color.multiply(kd);
	}

	/* (non-Javadoc)
	 * @see simGraphic.SMaterial#specularColor()
	 */
	@Override
	public SColor specularColor() 
	{
		SColor spec_plastic = PLASTIC_SPECULAR_REFLEXION.multiply(plasticity);	//proportion de lumi�re r�fl�chie de type plastique (couleur lumi�re)
		SColor spec_metal = color.multiply(1.0-plasticity);						//proportion de lumi�re r�fl�chie de type m�tal (couleur mat�riel)
		
		return spec_plastic.add(spec_metal).multiply(ks);
	}
	
	/* (non-Javadoc)
   * @see simGraphic.SMaterial#shininess()
   */
  @Override
  public double getShininess()
  { 
    return shininess;
  }
  
	@Override
	public SColor transparencyColor()
	{ 
		//Couleur diffuse de l'objet pond�r�e par le facteur de transparence
		return color.multiply(kt);
	}
	
	/* (non-Javadoc)
	 * @see simGraphic.SMaterial#reflexivity()
	 */
	@Override
	public double reflectivity()
	{ 
	  return kr;
	}
	
	/* (non-Javadoc)
	 * @see simGraphic.SMaterial#getTransparency()
	 */
	@Override
	public double transparency()
	{ 
	  return kt;
	}
	
	/* (non-Javadoc)
	 * @see simGraphic.SMaterial#refractiveIndex()
	 */
	@Override
	public double refractiveIndex()
	{
		if(!isTransparent())
			return DEFAULT_REFRACTIVE_INDEX;
		else
			return refractive_index;
	}
	
	/* (non-Javadoc)
	 * @see simTools.SReadableWriteable#read(SBufferedReader sbr, int code, String remaining_line) throws Exception
	 */
	@Override
	protected boolean read(SBufferedReader sbr, int code, String remaining_line)throws SReadingException
	{
		//Faire la v�rification du code pour la classe parent
		if(super.read(sbr, code, remaining_line))
			return true;
		else
			switch(code)
			{
				case SKeyWordDecoder.CODE_COLOR :		color = new SColor(remaining_line); return true; 					
			
				case SKeyWordDecoder.CODE_KA :			ka = readCoefficient(remaining_line, "ka", 1.0); return true;
				
				case SKeyWordDecoder.CODE_KD :			kd = readCoefficient(remaining_line, "kd", 1.0); return true;
				
				case SKeyWordDecoder.CODE_KS :			ks = readCoefficient(remaining_line, "ks", 1.0); return true;									
				
				case SKeyWordDecoder.CODE_KR :			kr = readCoefficient(remaining_line, "kr", 1.0); return true;
				
				case SKeyWordDecoder.CODE_KT :			kt = readCoefficient(remaining_line, "kt", 1.0); return true;
				
				case SKeyWordDecoder.CODE_SHININESS :         shininess = readCoefficient(remaining_line, "shininess", 200.0); return true;
				
				case SKeyWordDecoder.CODE_PLASTICITY :	      plasticity = readCoefficient(remaining_line, "p", 1.0); return true;
											
				case SKeyWordDecoder.CODE_REFRACTIVE_INDEX :  refractive_index = readCoefficient(remaining_line, "n", 10.0); return true;
																
				default : return false;
			}
	}
	
	/**
	 * M�thode pour faire l'analyse d'un string afin d'y retourner un type double entre 0 et une valeur maximale pour l'affecter � un coefficient ka, kd, ks, kr, kt, etc.
	 * @param remaining_line - L'expression en String du rayon.
	 * @param coefficient_name - Le nom du coefficent en lecture (pour affichage de message d'erreur).
	 * @return Le coefficient entre 0 et une valeur maximale.
	 * @throws SReadingException S'il y a une erreur de lecture.
	 */
	private double readCoefficient(String remaining_line, String coefficient_name, double max_value) throws SReadingException
	{
		StringTokenizer tokens = new StringTokenizer(remaining_line,SStringUtil.REMOVE_CARACTER_TOKENIZER);
		
		if(tokens.countTokens() == 0)
			throw new SReadingException("Erreur SColorMaterial 003 : Il n'y a pas de valeur num�rique affect�e au coefficient '" + coefficient_name +"'.");
		
		String s = tokens.nextToken();
		double coefficient = 0.0;
	
		try{
			coefficient = Double.valueOf(s);
		}catch(NumberFormatException e){ 
			throw new SReadingException("Erreur SColorMaterial 004 : L'expression '" + s + "' n'est pas un nombre de type double pouvant �tre affect� au coefficient '" + coefficient_name + "'.");
		}	
	
		if(coefficient < 0.0 || coefficient > max_value)
			throw new SReadingException("Erreur SColorMaterial 005 : Le coefficient '" + coefficient_name + "' doit �tre entre 0 et " + max_value + ".");
		
		return coefficient;
	}
	
	@Override
	public void write(BufferedWriter bw) throws IOException 
	{
		bw.write(SKeyWordDecoder.KW_MATERIAL);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		//�crire les propri�t�s de la classe SBlinnMaterial et ses param�tres h�rit�s
		writeBlinnMaterialParameter(bw);
		
		bw.write(SKeyWordDecoder.KW_END);
		bw.write("\t#end material" + SStringUtil.END_LINE_CARACTER);
		bw.write(SStringUtil.END_LINE_CARACTER);	
	}

	/**
	 * M�thode pour �crire les param�tres associ�s � la classe SBlinnMaterial et ses param�tres h�rit�s.
	 * @param bw - Le BufferedWriter �crivant l'information dans un fichier txt.
	 * @throws IOException Si une erreur I/O s'est produite.
	 * @see IOException
	 */
	protected void writeBlinnMaterialParameter(BufferedWriter bw)throws IOException 
	{
		//�crire les propri�t�s de la classe SDefaultMaterial h�rit�es
		writeDefaultMaterialParameter(bw);
		
		bw.write(SKeyWordDecoder.KW_COLOR);
		bw.write("\t");
		color.write(bw);
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_KA);
		bw.write("\t");
		bw.write(Double.toString(ka));
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_KD);
		bw.write("\t");
		bw.write(Double.toString(kd));
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_KS);
		bw.write("\t");
		bw.write(Double.toString(ks));
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_P);
		bw.write("\t");
		bw.write(Double.toString(plasticity));
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_KR);
		bw.write("\t");
		bw.write(Double.toString(kr));
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_KT);
		bw.write("\t");
		bw.write(Double.toString(kt));
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_N);
		bw.write("\t");
		bw.write(Double.toString(refractive_index));
		bw.write(SStringUtil.END_LINE_CARACTER);
		
		bw.write(SKeyWordDecoder.KW_SHININESS);
    bw.write("\t");
    bw.write(Double.toString(shininess));
    bw.write(SStringUtil.END_LINE_CARACTER);
	}
	
	/**
   * M�thode pour faire l'initialisation de l'objet apr�s sa construction.
   * 
   * @throws SInitializationException Si une erreur est survenue lors de l'initialisation.
   */
  private void initialize() throws SInitializationException
	{
			
	}
	
	@Override
  protected void readingInitialization() throws SInitializationException
  {
    super.readingInitialization();
    
    initialize();
  }
	
	@Override
  public String getReadableName()
  {
    return SKeyWordDecoder.KW_MATERIAL;
  }
  
  @Override
  public String[] getReadableParameterName()
  {
    String[] other_parameters = super.getReadableParameterName();
    
    return SStringUtil.merge(other_parameters, KEYWORD_PARAMETER);
  }
  
}//fin class SMaterial
