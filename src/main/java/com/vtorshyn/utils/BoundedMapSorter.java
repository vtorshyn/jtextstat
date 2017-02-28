package com.vtorshyn.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class BoundedMapSorter {

	private Map<String, Integer> map_;
	public BoundedMapSorter(Map<String, Integer> map) {
		this.map_ = map;
	}
	
	public Map<String, Integer> sort(int limit, int minimumValue) {
		Map<String, Integer> m = map_.entrySet().stream()
				.filter(s -> s.getValue() >= minimumValue)
				.sorted(Map.Entry.<String, Integer> comparingByValue(Comparator.reverseOrder()) 
						.thenComparing(Map.Entry.comparingByKey()) // and the by
																	// key
				).limit(limit).collect(
						Collectors.toMap(
								Entry::getKey, Entry::getValue, 
								(e1, e2) -> e1, LinkedHashMap::new)
						);
		return m;
	}

}
