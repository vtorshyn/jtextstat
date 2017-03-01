package com.vtorshyn.mocks;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class MockReader extends Reader {

	private char[] buffer;
	private int idx = 0;
	
	public void setBuffer(char[ ] buffer) {
		this.buffer = buffer;
		this.idx = 0;
	}
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int count = 0;
		for (; count < len; count++) {
			cbuf[count] = buffer[off+count];
		}
		idx += off;
		idx += count;
		System.out.println("MockReader::read(" + String.valueOf(cbuf) + ")=" + idx);
		return count;
	}

	@Override 
	public int read() {
		return buffer[idx++];
	}
	@Override
	public void close() throws IOException {
	}
	
}