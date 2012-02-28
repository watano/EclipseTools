package com.aureole.watano.util.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;

public class ReadLineIterator implements Iterator<String>{
	private LineNumberReader lnReader = null;
	private String currLine = null;
	private int no = 0;

	public ReadLineIterator(InputStreamReader in){
		lnReader = new LineNumberReader(in);
	}

	public ReadLineIterator(InputStream in){
		this(new InputStreamReader(in));
	}
	public ReadLineIterator(InputStream in, Charset cs){
		this(new InputStreamReader(in, cs));
	}
	public ReadLineIterator(InputStream in, CharsetDecoder dec){
		this(new InputStreamReader(in, dec));
	}
	public ReadLineIterator(InputStream in, String charsetName) throws UnsupportedEncodingException{
		this(new InputStreamReader(in, charsetName));
	}

	@Override
	public boolean hasNext() {
		if(no == 0 || currLine == null){
			try {
				currLine = lnReader.readLine();
				if(currLine != null){
					no++;
					return true;
				}
			} catch (IOException e) {
			}
		}
		return false;
	}

	public int getCurrLineNumber(){
		return no;
	}

	@Override
	public String next() {
		String tmp = currLine;
		currLine = null;
		return tmp;
	}

	@Override
	public void remove() {
	}

	public void close(){
		if(lnReader != null){
			try {
				lnReader.close();
			} catch (IOException e) {
			}
		}
	}

	public static void main(String[] args) {
		ReadLineIterator rli = null;
		try {
			rli = new ReadLineIterator(new FileInputStream("E:\\documents\\workspace\\EclipseTools\\src\\log4j.xml"));
			while(rli.hasNext()){
				System.err.println(rli.next());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if(rli != null) {
				rli.close();
			}
		}
	}
}
