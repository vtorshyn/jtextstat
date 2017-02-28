package com.vtorshyn.utils;

public enum LogLevels {
	LOG_ERROR(0),
	LOG_MESSAGE(1),
	LOG_INFO(2),
	LOG_DEBUG(3),
	LOG_DEBUG1(4),
	LOG_DEBUG2(5);
	
	LogLevels(int level) {
		this.level = level;
	}
	
	public int intValue() {
		return level;
	}
	
	private int level;
}
