package com.vtorshyn.word.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.vtorshyn.WordMapBuilder;
import com.vtorshyn.utils.CharUtils;
import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.LogLevels;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.StringCommandLineOption;
import com.vtorshyn.exceptions.WordMapException;

public class WordsRangeReader {
	
	@StringCommandLineOption(defaultValue = "", mandatory=true, help="Path to file with text. Supported encoding: ASCII.")
	public String file = null;
	
	@IntegerCommandLineOption(defaultValue=8192, help="Use custom size for file io buffer. [!]")
	public int readBufferSize = 8192;
	
	@IntegerCommandLineOption(defaultValue=100, help="Reserve addition bytes in readers queue for read-a-head smart logic.\nIf current chunk is not complete, e.g. word is fragmented, reader will read addition number of bytes until delimiter is found or on overflow. [!]")
	public int bufferReserve = 100;
	
	@StringCommandLineOption(defaultValue="off", mandatory=false, help="Skip input buffer validation.")
	public String skipReadBufferChecks;
	
	protected boolean skipChecks = false;
	
	private Path path = null;
	private BufferedReader reader = null;
	
	private int lastItemIndex = readBufferSize - 1;
	
	private Logger logger;
	private boolean hasData_;
	
	public WordsRangeReader(Logger logger) throws Exception {
		this.logger = logger;
	}
	
	public int bufferSize() {
		return this.readBufferSize;
	}
	
	public int bufferReserve() {
		return this.bufferReserve;
	}
	
	public void init() throws Exception {
		try {
			path = Paths.get(file);
			reader = Files.newBufferedReader(path, Charset.forName("ASCII"));
			lastItemIndex = bufferSize() - 1;
			hasData_ = true;
		} catch (Exception e) {
			logger.log("Error: " + e.getMessage());
			throw e;
		}
	}
	
	public boolean hasData() {
		return hasData_;
	}
	
	public int nextChunk(char[] buffer, int offset, int count, int maxItemsScan) throws  WordMapException, IOException{
		if (buffer.length == 0) {
			throw new WordMapException(getClass().getName()+": zero buffer.");
		}
		if (count > buffer.length) {
			throw new WordMapException(getClass().getName()+": requested read count larger then buffer size.");
		}
		Arrays.fill(buffer, offset, count + maxItemsScan, '\0');
		int rd = reader.read(buffer, offset, count);
		if (rd <= 0) {
			hasData_ = false;
			return rd;
		}
		rd += alignBuffer(buffer, maxItemsScan);
		return rd;
	}
	
	public int nextChunk(CharBuffer buffer, int count, int maxItemsScan) throws IOException, WordMapException {
		return nextChunk(buffer.array(), buffer.position(), count, maxItemsScan);
	}
	
	private int alignBuffer(char[] buffer, int maxItemsScan)  throws IOException {
		int iteration = 0;
		if (CharUtils.isazAZ09(buffer[lastItemIndex])) {
			// Checking next char
			char n;
			
			while (((n = (char) reader.read()) > 0) &&
					(++iteration > 0) &&
					CharUtils.isazAZ09(n) &&
					(iteration <= maxItemsScan)) {
				//++iteration;
				buffer[lastItemIndex + iteration] = n;
				logger.log(LogLevels.LOG_DEBUG2, "bufferRead+" + iteration);
			}
		}
		return iteration;
	}
	
	public void destroy() throws IOException {
		reader.close();
	}
}