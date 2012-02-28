package com.aureole.watano.util.xml;

import com.aureole.watano.util.functional.Obj2;

public class SimpleXMLReader2<A,B> extends SimpleXMLReader<Obj2<A, B>> {
	public SimpleXMLReader2(A a, B b) {
		super(new Obj2<A, B>(a, b));
	}
}