package sim.util;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.IOException;

import sim.exception.SNoImplementationException;

/**
 * La classe <b>SBufferedReader</b> repr�sente un objet h�ritant de la classe BufferedReader avec l'option de conna�tre le num�ro de la ligne d�j� lu par le buffer.
 *
 * @author Simon V�zina
 * @see BufferedReader
 * @since 2014-12-21
 * @version 2015-12-05
 */
public class SBufferedReader extends BufferedReader {

  /**
   * La variable <b>At_line</b> correspond au num�ro de la ligne en pr�sente lecture.
   */
	private long at_line;
	
	/**
	 * La variable <b>mark_line</b> correspond � ??????????.
	 */
	private long mark_line;

	/**
	 * Creates a buffering character-input stream that uses a default-sized input buffer.
	 * @param in - A Reader
	 */
	public SBufferedReader(Reader in)
	{
		super(in);
		at_line = 1;
		mark_line = 0;
	}
	
	/**
	 * Creates a buffering character-input stream that uses an input buffer of the specified size. 
	 * @param in - A Reader
	 * @param sz - Input-buffer size
	 */
	public SBufferedReader(Reader in, int sz)
	{
		super(in, sz);
		at_line = 1;
		mark_line = 0;
	}
	
	/**
	 * M�thode pour obtenir le num�ro de la ligne en pr�sente lecture par le buffer.
	 * Si un appel de la m�thode readLine() est effectu�e, le buffer se positionnera sur la ligne suivante et augmentera le comptage de la ligne de 1.
	 * Ainsi, le num�ro de la ligne associ� au String retourn� par la m�thode readLine() sera getAtLine() - 1.  
	 * @return Le num�ro de la ligne o� le buffer est rendu dans sa lecture.
	 */
	public long atLine(){ return at_line;	}
	
	@Override
	public String readLine()throws IOException
	{
		at_line++;
		return super.readLine();
	}
	
	@Override
	public void mark(int readAheadLimit)throws IOException
	{
		super.mark(readAheadLimit);
		mark_line = at_line;
	}
	
	@Override
	public void reset()throws IOException
	{
		super.reset();
		at_line = mark_line;
	}

	@Override
	public long skip(long n)throws IOException
	{
		throw new SNoImplementationException("public long skip(long n)throws IOException - M�thode non impl�ment�e.");
	}
    
	@Override
	public int read(char[] cbuf, int off, int len)throws IOException
	{
		throw new SNoImplementationException("public int read(char[] cbuf, int off, int len)throws IOException - M�thode non impl�ment�e.");
	}
	
	@Override
	public int read()throws IOException
	{
		throw new SNoImplementationException("public int read()throws IOException - M�thode non impl�ment�e.");
	}
	
}//fin classe SBufferedReader
