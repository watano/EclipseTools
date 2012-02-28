package com.aureole.watano.util.io;

import java.io.File;
import java.util.Iterator;

public class PackageIterator<T> implements Iterator<T>{
	private File root;
	private String[] childs = null;
	private File current = null;

	public PackageIterator(File file){
		root = file;
	}

	@Override
	public boolean hasNext() {
		if(current == null){
			current = root;
		}
		if(current.isDirectory()){
			childs = current.list();
			if(childs != null && childs.length > 0){
				return true;
			}
		}
		return false;
	}

	@Override
	public T next() {
		if(hasNext()){



		}
		return null;
	}

	@Override
	public void remove() {
	}
}
