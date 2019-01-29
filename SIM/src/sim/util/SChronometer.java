package sim.util;

import sim.exception.SRuntimeException;

/**
 * La classe SChronometer permet d'�valuer le temps �coul� avec une r�solution en milliseconde.
 * @author Simon Vezina
 * @version 2014-12-08
 * @version 2014-12-25
 */
public class SChronometer 
{
	private long start_time;	// temps �coul� en milliseconde depuis January 1, 1970, 00:00:00 GMT pour signaler le d�but du chronom�tre.
	private boolean isStarted;
	
	private long stop_time;		// temps �coul� en milliseconde depuis January 1, 1970, 00:00:00 GMT pour signaler l'arr�t du chronom�tre.
	private boolean isStoped;
	
	/**
	 * Constructeur du chronometre.
	 */
	public SChronometer()
	{
		reset();
	}
	
	/**
	 * M�thode pour initialiser le chronometre.
	 */
	public void start()
	{	
		start_time = System.currentTimeMillis();	// Temps � partir de January 1, 1970, 00:00:00 GMT
		isStarted = true;
	}
	
	/**
	 * M�thode pour arr�ter la mesure de l'�coulement du temps depuis l'appel de la m�thode start().
	 */
	public void stop()
	{
		stop_time = System.currentTimeMillis();		// Temps � partir de January 1, 1970, 00:00:00 GMT
		isStoped = true;
	}
	
	/**
	 * M�thode pour r�initialiser le chronom�tre.
	 */
	public void reset()
	{
		start_time = 0;
		isStarted = false;
		
		stop_time = 0;
		isStoped = false;
	}
	
	/**
	 * M�thode pour obtenir l'�coulement du temps entre l'appel de la fonction start() et stop().
	 * @return Temps �coul� en seconde.
	 * @throws SRuntimeException S'il l'appel de la m�thode start() n'a pas encore initialis�e le chronom�tre.
	 */
	public double getTime()throws SRuntimeException
	{
		return (double) getTimeMilliseconde() / 1000;
	}
	
	/**
	 * M�thode pour obtenir l'�coulement du temps entre l'appel de la fonction start() et stop().
	 * @return Temps �coul� en milliseconde.
	 * @throws SRuntimeException S'il l'appel de la m�thode start() n'a pas encore initialis�e le chronom�tre.
	 */
	public long getTimeMilliseconde()throws SRuntimeException
	{
		if(!isStarted)
			throw new SRuntimeException("Erreur SChronometer 001 : La m�thode start() du chronom�tre n'a pas encore initialis�e le chronom�tre.");
		
		if(isStoped)
			return stop_time - start_time;
		else
		{
			//Si le chronom�tre n'a pas �t� stopp�, retourner le temps �coul� entre le start() et l'appel de la fonction getTimeMilliseconde()
			long current_time = System.currentTimeMillis();
			return current_time - start_time;
		}
	}
	
	/**
	 * M�thode test pour v�rifier la classe SChronometer.
	 * @param args
	 */
	public static void main(String[] args)
	{
		Test1();
	}

	/**
	 * Schronometer Test #1 
	 */
	private static void Test1()
	{
		SChronometer c = new SChronometer();
		
		//�criture du temps �coul� (sera en exception).
		try{
		System.out.println(String.valueOf(c.getTime()) + " s");
		}catch(Exception e){System.out.println(e); }
		
		
		
		//�coulement du temps d�but� avec m�thode "boucle bidon"
		c.start();
		System.out.println("D�but de la boucle bidon.");
		for(int i=0; i<1000000; i++)
			for(int j=0; j<1000; j++)
				c.stop();
		System.out.println("Fin de la boucle bidon.");
		c.stop();
		
		//�criture du temps �coul�.
		try{
		System.out.println(String.valueOf(c.getTime()) + " s");
		}catch(Exception e){}
		
		try{
		System.out.println(String.valueOf(c.getTimeMilliseconde()) + " ms");
		}catch(Exception e){}
	}
}//end SChronometer
