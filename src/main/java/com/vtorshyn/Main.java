package com.vtorshyn;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

import com.vtorshyn.utils.ApplicationOptions;
import com.vtorshyn.utils.CharUtils;
import com.vtorshyn.utils.Logger;
/**!
 * 
 * @author vtorshyn
 * Main class of application.
 */
public class Main {
	static String[] requiredCommandLineOptions = { "input" };

	public static void main(String[] args) throws Exception {
		ApplicationOptions options = new ApplicationOptions(requiredCommandLineOptions, args);
		Logger logger = Logger.initLogger(options);

		Path f = Paths.get("input.txt");
		BufferedReader rd = Files.newBufferedReader(f, Charset.forName("UTF-8"));
		int readBufferSize = 8192, lastItemIndex = readBufferSize - 1, bufferReserve = 100;
		char[] buffer = new char[readBufferSize + bufferReserve];
		boolean ignoreCase = true;
		int outputLimit = 5;
		int occuranceCountFilterValue = 10;
		Map<String, Integer> wordsMap = new HashMap<String, Integer>(512);

		while (rd.read(buffer, 0, readBufferSize) > 0) {
			int pos = 0;
			String word = "";
			int iteration = 0;
			// Checking boundaries
			if (CharUtils.isazAZ09(buffer[lastItemIndex])) {
				// Checking next char
				char n;
				while ((n = (char) rd.read()) > 0 && CharUtils.isazAZ09(n)) {
					buffer[lastItemIndex + iteration + 1] = n;
					++iteration;
				}
			}
			for (; pos < buffer.length; ++pos) {
				char _ch = buffer[pos];
				if (CharUtils.isazAZ09(_ch)) {
					word += _ch;
				} else {
					if (word.length() > 0) {
						if (ignoreCase)
							word = word.toLowerCase(); // will slightly slowdown
						wordsMap.merge(word, 1, Integer::sum);
					}
					word = "";
				}
			}
			Arrays.fill(buffer, ' '); // Yes, let's reuse mem
		}
		Map<String, Integer> sortedMap = wordsMap.entrySet().stream()
				.filter(s -> s.getValue() >= occuranceCountFilterValue)
				.sorted(Map.Entry.<String, Integer> comparingByValue(Comparator.reverseOrder()) // First
																								// by
																								// value
						.thenComparing(Map.Entry.comparingByKey()) // and the by
																	// key
				).limit(outputLimit).collect(
						Collectors.toMap(
								Entry::getKey, Entry::getValue, 
								(e1, e2) -> e1, LinkedHashMap::new)
						);

		logger.log("Raw: " + wordsMap);
		logger.log("Result: " + sortedMap);

	}
}
