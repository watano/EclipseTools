package com.aureole.watano.util.xml;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListIterator implements Iterator<Node>{
	private NodeList nodes = null;
	private int i = 0;

	public NodeListIterator(NodeList nodes) {
		super();
		this.nodes = nodes;
	}

	public NodeListIterator(Object nodes) {
		super();
		this.nodes = (NodeList) nodes;
	}

	@Override
	public boolean hasNext() {
		return nodes!= null && i<nodes.getLength();
	}

	@Override
	public Node next() {
		return nodes.item(i++);
	}

	@Override
	public void remove() {
	}

}
