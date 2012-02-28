package com.aureole.watano.util.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.iterators.ArrayIterator;
import org.apache.commons.lang.StringUtils;

public class AutoIterator<V>{
	private Map<V,?> map = null;
	private Iterator<V> iterator = null;
	private V value;
	private int index = -1;

	public AutoIterator(Iterator<V> iter){
		iterator = iter;
	}

	public AutoIterator(Collection<V> list){
		if(list != null && list.size() > 0){
			iterator = list.iterator();
		}
	}

	@SuppressWarnings("unchecked")
	public AutoIterator(V[] array){
		if(array != null && array.length > 0){
			iterator = new ArrayIterator(array);
		}
	}

	public AutoIterator(Map<V,?> map){
		if(map != null && map.size() > 0){
			iterator = map.keySet().iterator();
			this.map = map;
		}
	}

	@SuppressWarnings("unchecked")
	public AutoIterator(String text, String split){
		this((V[])StringUtils.split(text, split));
	}

	private boolean hasNext() {
		return iterator!= null && iterator.hasNext();
	}

	private V next() {
		if(hasNext()){
			V v = iterator.next();
			value = v;
			index++;
			return v;
		}
		return null;
	}

	public boolean move(){
		return next() != null;
	}

	public V getValue(){
		return value;
	}

	public Object getMapValue(){
		if(map != null){
			return map.get(value);
		}else{
			return null;
		}
	}

	public int getIndex(){
		return index;
	}
}
