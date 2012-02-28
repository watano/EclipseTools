package com.aureole.watano.util.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MultiValueMap<K, V> extends HashMap<K, ArrayList<V>>{
	private static final long serialVersionUID = -3526459351681619989L;

	public ArrayList<V> putValue(K key, V value){
		ArrayList<V> values = get(key);
		if(values == null){
			values = new ArrayList<V>();
		}
		values.add(value);
		put(key, values);
		return put(key, values);
	}

	public V getValue(K key, int index){
		ArrayList<V> values = get(key);
		if(values != null && values.size() > index){
			return values.get(index);
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends ArrayList<V>> m) {
		if(m != null && m.size() > 0) {
			for(K key:m.keySet()){
				ArrayList<V> values = m.get(key);
				if(values != null && values.size() > 0){
					for(V v:values){
						putValue(key, v);
					}
				}
			}
		}
	}

	public void sortValues(Comparator<V> comparator, boolean order){
		for(K key:keySet()){
			ArrayList<V> values = get(key);
			Collections.sort(values, comparator);
			if(!order){
				Collections.reverse(values);
			}
		}
	}
}
