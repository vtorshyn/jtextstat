package com.vtorshyn.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplicationOptions extends HashMap<String,String>{

	private static final long serialVersionUID = 1L;

	ApplicationOptions(String[] mandatoryKeys, String[] stringList) {
		super();
		loadFromStringArray(stringList, "-");
		validateKeys(mandatoryKeys);
	}
	public void loadFromStringArray(String[] stringList, String delim) {
		String option = null;
		int len = stringList.length,
				idx = 0;
		for (; idx < len; ++idx) {
			String v = stringList[idx];
			if (!v.startsWith(delim)) {
				if (null != option) {
					this.put(option, v);
					option = null;
					continue;
				}
				System.out.println("Unknown option: " + v);
			} else if (null != option) {
				this.put(option, "");
				option = v.replaceFirst("-", "");
			} else {
				option = v.replaceFirst("-", "");
			}
		}
		System.out.println(this.toString());
	}
	
	public String value(String key) {
		if (this.containsKey(key)) {
			return get(key);
		}
		return null;
	}
	
	public int intValue(String key) {
		String v = value(key);
		if (null == v || v.length() == 0) {
			return 0;
		}
		return Integer.parseInt(v);
	}
	
	public List<String> validateKeys(String[] keys) {
		List<String> missedKeys = null;
		
		for (String k : keys) {
			if (!containsKey(k)) {
				if (null == missedKeys) {
					missedKeys = new ArrayList<String>();
				}
				missedKeys.add(k);
			}
		}
		
		if (null != missedKeys) {
			System.out.println("Missed: " + missedKeys.toString());
		}
		return missedKeys;
	}
}
