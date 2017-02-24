package com.vtorshyn.word.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.vtorshyn.utils.OptionsMap;
import com.vtorshyn.utils.CharUtils;
import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.StringCommandLineOption;

public class WordsRangeReader {
	
	@IntegerCommandLineOption(defaultValue=8192, help="Use custom size for file io buffer. [!]")
	public int readBufferSize = 8192;
	
	@IntegerCommandLineOption(defaultValue=100, help="Reserve addition bytes in readers queue for read-a-head smart logic.\nIf current chunk is not complete, e.g. word is fragmented, reader will read addition number of bytes until delimiter is found or on overflow. [!]")
	public int readBufferDeltaSize = 100;
	
	@StringCommandLineOption(defaultValue = "", mandatory=true, help="Path to file with text. Assumed ASCII.")
	public String file = null;
	
	private Path path = null;
	private BufferedReader reader = null;
	private char[] buffer = null;
	
	private int lastItemIndex = readBufferSize - 1;
	
	private Logger logger;
	
	public WordsRangeReader() throws Exception {
		init();
	}
	
	private void init() throws Exception {

		logger = Logger.get();
		try {
			OptionsMap o = OptionsMap.get();
			o.bind(this.getClass(), this);
			path = Paths.get(file);
			reader = Files.newBufferedReader(path, Charset.forName("UTF-8"));
			buffer = new char[readBufferSize + readBufferDeltaSize];
			lastItemIndex = readBufferSize - 1;
			logger.log(this.getClass().getName()+".readBufferSize="+readBufferSize);
			logger.log(this.getClass().getName()+".readBufferDeltaSize="+readBufferDeltaSize);
			logger.log(this.getClass().getName()+".file="+file);
		} catch (Exception e) {
			logger.log("Error: " + e.getMessage());
			throw e;
		}
	}
	
	public char[] nextChunk() throws IOException {
		buffer = new char[readBufferSize + readBufferDeltaSize];
		if (reader.read(buffer, 0, readBufferSize) <= 0) {
			return null;
		}
		if (CharUtils.isazAZ09(buffer[lastItemIndex])) {
			// Checking next char
			char n;
			int iteration = 0;
			while ((n = (char) reader.read()) > 0 && CharUtils.isazAZ09(n)) {
				buffer[lastItemIndex + iteration + 1] = n;
				++iteration;
			}
		}
		return buffer;
	}
	
	public void destroy() throws IOException {
		buffer = null;
		reader.close();
	}
}