package com.vtorshyn;

import java.util.HashMap;
import java.util.Map;

import com.vtorshyn.utils.CharUtils;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.StringCommandLineOption;

public class WordMapBuilder {
	@StringCommandLineOption(defaultValue="", mandatory=false, help="Ignore case, e.g. \"text\" and \"tExt\" will be counted separtely")
	public String ignoreCase;
	
	Logger logger;

	public WordMapBuilder(Logger logger) {
		this.logger = logger;
	}
	
	public Map<String, Integer> buildFromCharArray(char[] buffer) {
		Map<String, Integer> wordsMap = new HashMap<>(512);
		return buildFromCharArray(wordsMap, buffer);
	}
	
	public Map<String, Integer> buildFromCharArray(Map<String, Integer> wordsMap, char[] buffer) {
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
		// Edge case when last char in buffer is alphanum. Captured by UT :)
		if (word.length() > 0) {
			wordsMap.merge(word, 1, Integer::sum);
		}
		return wordsMap;
	}
}