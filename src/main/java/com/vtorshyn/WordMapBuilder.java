package com.vtorshyn;

import java.util.HashMap;
import java.util.Map;

import com.vtorshyn.utils.CharUtils;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.StringCommandLineOption;

public class WordMapBuilder {
	@StringCommandLineOption(defaultValue="false", mandatory=false, help="If \"true\" transformation to lower case will be done. Will slow down processing.")
	public String ignoreCase;
	
	Logger logger;

	public WordMapBuilder(Logger logger) {
		this.logger = logger;
	}
	
	public Map<String, Integer> buildFromCharArray(char[] buffer) {
		Map<String, Integer> wordsMap = new HashMap<>(128);
		return buildFromCharArray(wordsMap, buffer);
	}
	
	public Map<String, Integer> buildFromCharArray(Map<String, Integer> wordsMap, char[] buffer) {
		int pos = 0, offset = 0, count = 0;
		if ("true".equals(ignoreCase)) {
			for(; pos < buffer.length; ++pos) {
				char c = buffer[pos];
				buffer[pos] = Character.toLowerCase(c);
			}
			pos = 0;
		}
		for (; pos < buffer.length; ++pos) {
			char _ch = buffer[pos];
			if (CharUtils.isazAZ09(_ch)) {
				++count;
			} else {
				if (count > 0) {
					String value = String.copyValueOf(buffer, offset, count);
					wordsMap.merge(value, 1, Integer::sum);
					count = 0;
				}
				offset = pos + 1;
			}
		}
		return wordsMap;
	}
}