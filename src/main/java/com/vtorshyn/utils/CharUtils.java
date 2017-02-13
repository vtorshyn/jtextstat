package com.vtorshyn.utils;
public class CharUtils {
    public static boolean isazAZ09(char _ch) {
		if (
			( _ch >= 'a' && _ch <= 'z') ||
			( _ch >= 'A' && _ch <= 'Z') ||
			( _ch >= '0' && _ch <= '9')
			) {
			return true;
		}
		return false;
    }
}
