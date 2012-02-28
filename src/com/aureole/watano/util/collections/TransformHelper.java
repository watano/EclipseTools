package com.aureole.watano.util.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransformHelper {
	public static <Input, Output> List<Output> transform(Iterator<Input> iterator, Transformer<Input, Output> transformer){
		List<Output> list = new ArrayList<Output>();
		if(iterator != null){
			while(iterator.hasNext()){
				list.add(transformer.transform(iterator.next()));
			}
		}
		return list;
	}
}
