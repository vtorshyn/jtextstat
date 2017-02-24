package com.vtorshyn;

import java.util.HashMap;
import java.util.Map;

import com.vtorshyn.utils.OptionsMap;
import com.vtorshyn.utils.CharUtils;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.StringCommandLineOption;

public class WordMapBuilder {
	@StringCommandLineOption(defaultValue="", mandatory=false, help="Ignore case, e.g. \"text\" and \"tExt\" will be counted separtely")
	public String ignoreCase;
	
	Logger logger;
	
	private void init() {
		logger = Logger.get();
		try {
			OptionsMap o = OptionsMap.get();
			o.bind(this.getClass(), this);
			logger.log(this.getClass().getName() + ".ignoreCase="+ignoreCase);
		} catch (Exception e) {
			logger.log("Error: " + e.getMessage());
		}
	}
	
	public WordMapBuilder() {
		init();
	}
	
	public Map<String, Integer> buildFromCharArray(char[] buffer) {
		synchronized(buffer) {
		Map<String, Integer> wordsMap = new HashMap<>(512);
		int pos = 0;
		String word = "";
		
		for (; pos < buffer.length; ++pos) {
			char _ch = buffer[pos];
			if (CharUtils.isazAZ09(_ch)) {
				word += _ch;
			} else {
				if (word.length() > 0) {
					if (ignoreCase.length() > 0)
						word = word.toLowerCase(); // will slightly slowdown
					wordsMap.merge(word, 1, Integer::sum);
				}
				word = "";
			}
		}
		return wordsMap;
		}
	}
}