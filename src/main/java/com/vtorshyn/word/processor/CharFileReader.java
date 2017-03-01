package com.vtorshyn.word.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.vtorshyn.utils.CharUtils;
import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.LogLevels;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.StringCommandLineOption;
import com.vtorshyn.exceptions.WordMapException;

public class CharFileReader {
	
	@StringCommandLineOption(defaultValue = "", mandatory=true, help="Path to file with text. Supported encoding: ASCII.")
	public String file = null;
	
	@IntegerCommandLineOption(defaultValue=2*8192, help="Use custom size for file io buffer. [!]")
	public int blockSize = 2*8192;
	
	@IntegerCommandLineOption(defaultValue=10, help="Reserve addition bytes in readers queue for read-a-head smart logic.\nIf current chunk is not complete, e.g. word is fragmented, reader will read addition number of bytes until delimiter is found or on overflow. [!]")
	public int delta = 10;
	
	@StringCommandLineOption(defaultValue="false", mandatory=false, help="When \"true\", input buffer validation will be skipped.")
	public String skipReadBufferChecks;
	
	protected boolean skipChecks = false;
	
	private Path path = null;
	private BufferedReader reader = null;
	private Logger logger;
	
	/**!
	 * Replaces current BufferedReader by new one.
	 * Old reader is returned on success.
	 * 
	 * @param other
	 * @return
	 */
	public BufferedReader reader(BufferedReader other) {
		BufferedReader old = this.reader;
		this.reader = other;
		return old;
	}
	
	public BufferedReader reader() {
		return this.reader;
	}
	
	public CharFileReader(Logger logger) throws Exception {
		this.logger = logger;
	}
	
	public int bufferSize() {
		return this.blockSize;
	}
	
	public int bufferReserve() {
		return this.delta;
	}
	
	public void init() throws Exception {
		try {
			path = Paths.get(file);
			reader = Files.newBufferedReader(path, Charset.forName("ASCII"));
			if ("true".equals(skipReadBufferChecks)) {
				skipChecks = true;
			}
		} catch (InvalidPathException e) {
			logger.log(LogLevels.LOG_DEBUG1, "Invalid path \""+file+"\"");
			throw new Exception("Invalid path \""+file+"\"");
		} catch(IOException e) {
			throw new Exception("Can not load file \""+file+"\"");
		}
		catch (Exception e) {
			logger.log(LogLevels.LOG_DEBUG1, "WordsRangeReader critical problem: " + e.getMessage());
			throw e;
		}
	}
	
	public int nextChunk(char[] buffer, int offset, int count, int maxItemsScan) throws  WordMapException, IOException{
		if (!skipChecks) { 
			if (buffer.length == 0) {
				throw new WordMapException(getClass().getName()+": zero buffer.");
			}
			if (count > buffer.length) {
				throw new WordMapException(getClass().getName()+": requested read count larger then buffer size.");
			}
		}
		
		Arrays.fill(buffer, offset, count + maxItemsScan, '\0');
		
		int rd = reader.read(buffer, offset, count);

		if (rd <= 0) {
			return rd;
		}
		
		int currentPos = rd - 1;
		
		if (CharUtils.isazAZ09(buffer[currentPos])) {
			char _ch; 
			int extraCount = 0;
			while ( ((_ch = (char)reader.read()) > 0) )
			{
				if (!CharUtils.isazAZ09(_ch)) {
					break;
				}
				if (extraCount >= maxItemsScan) {
					break;
				}
				++rd;
				++extraCount;
				buffer[currentPos + extraCount] = _ch;
			}
		}
		
		return rd;
	}
	
	public int nextChunk(CharBuffer buffer, int count, int maxItemsScan) throws IOException, WordMapException {
		return nextChunk(buffer.array(), buffer.position(), count, maxItemsScan);
	}
	
	public void destroy() throws IOException {
		reader.close();
	}
}