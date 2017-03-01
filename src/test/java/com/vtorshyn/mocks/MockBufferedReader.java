package com.vtorshyn.mocks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MockBufferedReader extends BufferedReader {

	private Reader rd;
	public MockBufferedReader(Reader in) {
		super(in);
		rd = in;
	}
	
	@Override
	public
	int read(char[] buffer, int offset, int count) throws IOException {
		int c = rd.read(buffer, offset, count);
		System.out.println("[["+String.copyValueOf(buffer) + "]]");
		return c;
	}
	
	@Override
	public int read() throws IOException {
		return rd.read();
	}
}