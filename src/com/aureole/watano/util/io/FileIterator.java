package com.aureole.watano.util.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class FileIterator implements Iterator<File> {
	private List<File> childs = null;
	private boolean recursiveSubDir = true;

	public FileIterator(File file){
		if(file != null && file.exists()){
			if(file.isDirectory()){
				childs = new ArrayList<File>();
				for(File f2:file.listFiles()){
					childs.add(f2);
				}
			}
		}
	}

	public FileIterator(File file, boolean recursiveSubDir){
		this(file);
		this.recursiveSubDir = recursiveSubDir;
	}

	@Override
	public boolean hasNext() {
		if(childs != null && childs.size() > 0){
			return true;
		}
		return false;
	}

	@Override
	public File next() {
		if(hasNext()){
			File f = childs.get(0);
			if(recursiveSubDir && f.isDirectory()){
				for(File f2:f.listFiles()){
					childs.add(f2);
				}
			}
			return childs.remove(0);
		}
		return null;
	}

	@Override
	public void remove() {
	}


	@Test
	public void test() {
	}
}
