package com.aureole.watano.util.xml;

import com.aureole.watano.util.functional.Obj1;

public class SimpleXMLReader1<A> extends SimpleXMLReader<Obj1<A>> {
	public SimpleXMLReader1(A a) {
		super(new Obj1<A>(a));
	}
}
