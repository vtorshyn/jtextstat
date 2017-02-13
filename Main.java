import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
public class Main {
	private static boolean isazAZ09(char _ch) {
		if (
			( _ch >= 'a' && _ch <= 'z') ||
			( _ch >= 'A' && _ch <= 'Z') ||
			( _ch >= '0' && _ch <= '9')
			) {
			return true;
		}
		return false;
	}
	public static void main(String[] args) throws Exception {
		Path f = Paths.get("input.txt");
		BufferedReader rd = Files.newBufferedReader(f,Charset.forName("UTF-8"));
		int size = 8192,
			lastBufferPos = size - 1,
			bufferReserve = 100;
		char [] buffer = new char[size+bufferReserve];
		Map<String, Integer> wordsMap = new HashMap<String, Integer>(1024);

		while(rd.read(buffer, 0, size) > 0) {
			int pos = 0;
			String word = "";
			int iteration = 0;
			// Checking boundaries
			if (isazAZ09(buffer[lastBufferPos])) {
				// Checking next char
				char n;
				while ( (n = (char)rd.read()) > 0 && isazAZ09(n) ) {
					buffer[lastBufferPos + iteration + 1] = n;
					++iteration;
				}
			}
			for (; pos < buffer.length; ++pos) {
				char _ch = buffer[pos];
				if (isazAZ09(_ch)) {
					word += _ch;
				} else {
					if (word.length() > 0) {
						wordsMap.merge(word, 1, Integer::sum);
					}
					word = "";
				}
			}
			Arrays.fill(buffer, ' '); // Yes, let's reuse mem
		}
		Map<String, Integer> sortedMap = 
			wordsMap.entrySet().stream()
			.sorted(Entry.comparingByValue(Comparator.reverseOrder()))
			.limit(5)
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                              (e1, e2) -> e1, LinkedHashMap::new));
		System.out.println("Raw: " + wordsMap);
		System.out.println("Result: " + sortedMap);
		
	}
}
