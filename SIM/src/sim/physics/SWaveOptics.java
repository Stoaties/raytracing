/**
 * 
 */
package sim.physics;

import java.util.List;
import java.util.function.DoubleUnaryOperator;

import sim.exception.SNoImplementationException;
import sim.graphics.SColor;
import sim.math.SMath;
import sim.math.SVector3d;

/**
 * La classe <b>SWaveOptics</b> représente une classe permettant d'effectuer des calculs en lien avec <b>l'optique ondulatoire</b>.
 * 
 * @author Simon Vézina
 * @since 2016-02-14
 * @version 2017-02-02
 */
public class SWaveOptics {

  //--------------
  // CONSTANTES //
  //--------------
  
  /**
   * La constante <b>MINIMUM_VISIBLE_LIGHT_WAVELENGHT</b> représente la borne inférieure du domaine des longueurs d'onde de la lumière du visible.
   * Cette longueur d'onde égale à <b>{@value} nm</b> correspond au <b>mauve</b>.
   */
  public static final double MINIMUM_VISIBLE_LIGHT_WAVELENGHT = 380.0;
  
  /**
   * La constante <b>MAXIMUM_VISIBLE_LIGHT_WAVELENGHT</b> représente la borne supérieur du domaine des longueurs d'onde de la lumière du visible.
   * Cette longueur d'onde égale à <b>{@value} nm</b> correspond au <b>rouge</b>.
   */
  public static final double MAXIMUM_VISIBLE_LIGHT_WAVELENGHT = 780.0;
  
  /**
   * La constante <b>POSITIVE_2PI</b> correspond à la constante 2*Pi.
   */
  private static final double POSITIVE_2PI = 2*Math.PI;
  
  /**
   * La constante <b>NEGATIVE_2PI</b> correspond à la constante -2*Pi.
   */
  private static final double NEGATIVE_2PI = -1*POSITIVE_2PI;
  
  //------------
  // MÉTHODES //
  //------------
  
  /**
   * Méthode pour obtenir une phase en radian entre zéro et 2*Pi équivalente à celle passée en paramètre.
   * 
   * @param phase La phase (en radian).
   * @return Une phase entre zéro et 2*Pi
   */
  public static double phaseBetweenZeroAnd2Pi(double phase)
  {
    if(phase > POSITIVE_2PI)
    {
      // Compter le nombre de 2*Pi inclus dans la phase
      int nb = (int)(phase / (2*Math.PI));
      
      // Retirer une nombre entier de 2*Pi
      phase = phase - 2*Math.PI*nb;
    }
    else
      if(phase < NEGATIVE_2PI)
      {
        throw new SNoImplementationException("Erreur SWaveOptics 001 : La méthode n'est pas complètement implémentée.");
      }
     
    // Le case phase entre 0 et 2*Pi ne nécessite aucun changement
    return phase;
  }
  
  /**
   * <p>
   * Méthode pour convertir la longueur d'onde d'une onde en fréquence.
   * </p>
   * <p>
   * Cette convertion utilise la relation 
   * <ul>lamda = v / f</ul>
   * <p>
   * 
   * @param wave_lenght La longueur d'onde (en m).
   * @param wave_speed La vitesse de l'onde (en m/s).
   * @return La fréquence (en Hz).
   */
  public static double waveLenghtToFrequency(double wave_lenght, double wave_speed)
  {
    return wave_speed/wave_lenght;
  }
  
  /**
   * <p>
   * Méthode pour convertir la fréquence d'une onde en longueur d'onde.
   * </p>
   * <p>
   * Cette convertion utilise la relation 
   * <ul>lamda = v / f</ul>
   * <p>
   * 
   * @param wave_lenght La longueur d'onde (en m).
   * @param wave_speed La vitesse de l'onde (en m/s).
   * @return La fréquence (en Hz).
   */
  public static double frequencyToWaveLenght(double frequency, double wave_speed)
  {
    return wave_speed/frequency;
  }
  
  /**
   * Méthode pour faire le calcul de l'interférence de plusieurs ondes <b>de même fréquence</b> générées par plusieurs générateurs
   * en un point P de l'espace. 
   * 
   * @param oscillator_list La liste des oscillateurs en interférence.
   * @param position La position où est calculé l'interférence.
   * @param period La periode sur laquelle la moyenne sera effectuée.
   * @param step Le nombre d'itérations effectuées sur une période de calcul pour évaluer la moyenne
   * @return La valeur moyenne de l'interférence au point P.
   */
  public static double interferenceAverageWaveValue(List<SWave> wave_list, SVector3d position, double period, int step)
  {
    // Définir la position du point P pour l'ensemble des ondes
    for(SWave w : wave_list)
      w.setPosition(position);
    
    // Évaluer le temps de chaque itération d'une période
    double dt = period / step;
    
    // Tableau des valeurs de chaque itération
    double value[] = new double[step];
    
    // Temps associé à l'itération. Cette valeur va changer à chaque changement de step.
    double t = 0.0;
    
    // Faire la somme des ondes sur une période complète de 'step' itération
    for(int i = 0; i < step; i++)
    {
      // Tableau des valeurs des de chaque onde
      double[] tab = new double[wave_list.size()];
  
      for(int index = 0; index < tab.length; index++)
        tab[index] = wave_list.get(index).getValue(t);
  
      double superposition = SMath.strategicArraySum(tab);
      
      // Additionner le produit de la superposition au carré avec un écoulement de temps dt.
      value[i] = superposition*superposition * dt;
      
      // Avancer dans le cycle de l'intégrale pour compléter la période.
      t += dt;
    }
    
    // Retourner la sommes des valeur de chaque cycle de période et obtenir la moyenne en divisant par la période.
    return SMath.strategicArraySum(value) / period;
  }
  
  // ANCIENNE FAÇON DE FAIRE !!!
  
  /*
  public static double interferenceAverageWaveValue(List<SWave> wave_list, SVector3d position, double period, int step)
  {
    // Définir la position du point P pour l'ensemble des ondes
    for(SWave w : wave_list)
      w.setPosition(position);
    
    double dt = period / step;
    
    double t = 0.0;
    double value[] = new double[step];
    
    // Faire la somme des ondes sur une période complète de 'step' itération
    for(int i = 0; i < step; i++)
    {
      // Effectuer la superposition des ondes à un temps t
      double superposition = 0.0;
      
      à revoir ...
      
      // Methode #1 : Tout additionner simplement
//      for(SWave w : wave_list)
//        superposition += w.getValue(t);
      
      final double tf = t;
//      superposition = wave_list.stream() //
//          .map(w -> w.getValue(tf) ) //
//          .map(BigDecimal::new) //
//          .sorted((b1, b2) -> b1.abs().compareTo(b2.abs()))
//          .reduce(BigDecimal.ZERO, BigDecimal::add) //
//          .doubleValue();
      superposition = wave_list.stream().mapToDouble(w -> w.getValue(tf)).sorted().sum();
//      System.out.println("sorted="+wave_list.stream().mapToDouble(w -> w.getValue(tf)).sorted().sum()+" original="+superposition);
//      
      // Méthode #2 : Addition stratégique
//      double[] tab = new double[wave_list.size()];
//      
//      for(int index = 0; index < tab.length; index++)
//        tab[index] = wave_list.get(index).getValue(t);
//      
//      superposition = SMath.strategicArraySum(tab);
//      
      
      // Additionner le produit de la superposition au carré avec un écoulement de temps dt
      // pour approximer le calcul de l'intégrale
      value[i] = superposition*superposition * dt;
      
      t += dt;
    }    
       
    // Faire la moyenne en divisant le tout par le nombre d'itération
    double value2 =  Arrays.stream(value).sorted().sum();
    
//    double value2 = 0;
//    for (double v:value)
//      value2 += v;
    
    return value2 / period;
  }
  */
  
  
  /**
   * Méthode pour convertir une couleur en longueur d'onde dans le domaine du visible (en nm) en couleur de type SColor de format rgb.
   * 
   * @param wavelength La longueur d'onde (en nm).
   * @return La couleur en format rgb.
   * @throws SNotVisibleLightException Si la longueur d'onde n'est pas dans le domaine du visible étant de 380 nm à 780 nm.
   */
  public static SColor wavelengthToSColor(double wavelength) throws SNotVisibleLightException
  {
    if(wavelength < MINIMUM_VISIBLE_LIGHT_WAVELENGHT)
      throw new SNotVisibleLightException("Erreur SWaveOptics 001 : La longueur d'onde de la lumière dans le visible '" + wavelength + "' nm est inférieure à " + MINIMUM_VISIBLE_LIGHT_WAVELENGHT + " nm.");
    
    if(wavelength > MAXIMUM_VISIBLE_LIGHT_WAVELENGHT)
      throw new SNotVisibleLightException("Erreur SWaveOptics 002 : La longueur d'onde de la lumière dans le visible '" + wavelength + "' nm est supérieure à " + MAXIMUM_VISIBLE_LIGHT_WAVELENGHT + " nm.");
    
    double[] xyz = cie1931WavelengthToXYZFit(wavelength);
    double[] rgb = srgbXYZ2RGB(xyz);
    
    return new SColor(rgb[0], rgb[1], rgb[2]);
  }
  
  /**
   * <p>
   * Convert a wavelength in the visible light spectrum to a RGB color value that is suitable to be displayed on a
   * monitor.
   * </p>
   * 
   * @param wavelength wavelength in nm
   * @return RGB color encoded in int. each color is represented with 8 bits and has a layout of
   * 00000000RRRRRRRRGGGGGGGGBBBBBBBB where MSB is at the leftmost
   * @author http://stackoverflow.com/questions/1472514/convert-light-frequency-to-rgb
   */
  public static int wavelengthToRGB(double wavelength)
  {
    double[] xyz = cie1931WavelengthToXYZFit(wavelength);
    double[] rgb = srgbXYZ2RGB(xyz);

    int c = 0;
    c |= (((int) (rgb[0] * 0xFF)) & 0xFF) << 16;
    c |= (((int) (rgb[1] * 0xFF)) & 0xFF) << 8;
    c |= (((int) (rgb[2] * 0xFF)) & 0xFF) << 0;

    return c;
  }

  /**
   * Convert XYZ to RGB in the sRGB color space
   * <p>
   * The conversion matrix and color component transfer function is taken from http://www.color.org/srgb.pdf, which
   * follows the International Electrotechnical Commission standard IEC 61966-2-1 "Multimedia systems and equipment -
   * Colour measurement and management - Part 2-1: Colour management - Default RGB colour space - sRGB"
   * </p>
   * 
   * @param xyz XYZ values in a double array in the order of X, Y, Z. each value in the range of [0.0, 1.0]
   * @return RGB values in a double array, in the order of R, G, B. each value in the range of [0.0, 1.0]
   * @author http://stackoverflow.com/questions/1472514/convert-light-frequency-to-rgb
   */
  private static double[] srgbXYZ2RGB(double[] xyz)
  {
    double x = xyz[0];
    double y = xyz[1];
    double z = xyz[2];

    double rl =  3.2406255 * x + -1.537208  * y + -0.4986286 * z;
    double gl = -0.9689307 * x +  1.8757561 * y +  0.0415175 * z;
    double bl =  0.0557101 * x + -0.2040211 * y +  1.0569959 * z;
    
    
    // Version Lambda expression :
    DoubleUnaryOperator transfer = c -> c <= 0.0031308 ? c * 12.92 : 1.055 * Math.pow(c, 1. / 2.4) - 0.055;
    transfer = transfer.compose(c -> c > 1 ? 1 : (c < 0 ? 0 : c));
    
    return new double[] {
           transfer.applyAsDouble(rl),
            transfer.applyAsDouble(gl),
            transfer.applyAsDouble(bl)
    };
    
    /*
    // Version traditionnelle : (PROBABLEMEMENT MARCHE PAS !!!!!)
    return new double[] {
        srgbXYZ2RGB_2(srgbXYZ2RGB_1(rl)),
        srgbXYZ2RGB_2(srgbXYZ2RGB_1(gl)),
            srgbXYZ2RGB_2(srgbXYZ2RGB_1(bl))
    };
    */
  }
  
  /**
   * ...
   * 
   * @param c
   * @return
   */
  /*
  private static double srgbXYZ2RGB_1(double c){
    return c > 1 ? 1 : c < 0 ? 0 : c;
  }
  */
  
  /**
   * ...
   * 
   * @param c
   * @return
   */
  /*
  private static double srgbXYZ2RGB_2(double c){
    return c <= 0.0031308 ? c * 12.92 : 1.055 * Math.pow(c, 1. / 2.4) - 0.055;
  }
  */
  
  /**
   * A multi-lobe, piecewise Gaussian fit of CIE 1931 XYZ Color Matching Functions by Wyman el al. from Nvidia. The
   * code here is adopted from the Listing 1 of the paper authored by Wyman et al.
   * <p>
   * Reference: Chris Wyman, Peter-Pike Sloan, and Peter Shirley, Simple Analytic Approximations to the CIE XYZ Color
   * Matching Functions, Journal of Computer Graphics Techniques (JCGT), vol. 2, no. 2, 1-11, 2013.
   *</p>
   * 
   * @param wavelength wavelength in nm
   * @return XYZ in a double array in the order of X, Y, Z. each value in the range of [0.0, 1.0]
   * @author http://stackoverflow.com/questions/1472514/convert-light-frequency-to-rgb
   */
  private static double[] cie1931WavelengthToXYZFit(double wavelength)
  {
    double wave = wavelength;

    double x;
    {
        double t1 = (wave - 442.0) * ((wave < 442.0) ? 0.0624 : 0.0374);
        double t2 = (wave - 599.8) * ((wave < 599.8) ? 0.0264 : 0.0323);
        double t3 = (wave - 501.1) * ((wave < 501.1) ? 0.0490 : 0.0382);

        x =   0.362 * Math.exp(-0.5 * t1 * t1)
            + 1.056 * Math.exp(-0.5 * t2 * t2)
            - 0.065 * Math.exp(-0.5 * t3 * t3);
    }

    double y;
    {
        double t1 = (wave - 568.8) * ((wave < 568.8) ? 0.0213 : 0.0247);
        double t2 = (wave - 530.9) * ((wave < 530.9) ? 0.0613 : 0.0322);

        y =   0.821 * Math.exp(-0.5 * t1 * t1)
            + 0.286 * Math.exp(-0.5 * t2 * t2);
    }

    double z;
    {
        double t1 = (wave - 437.0) * ((wave < 437.0) ? 0.0845 : 0.0278);
        double t2 = (wave - 459.0) * ((wave < 459.0) ? 0.0385 : 0.0725);

        z =   1.217 * Math.exp(-0.5 * t1 * t1)
            + 0.681 * Math.exp(-0.5 * t2 * t2);
    }

    return new double[] { x, y, z };
  }
  
}//fin de la classe SWaveOptics
