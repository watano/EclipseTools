package com.aureole.watano.util.xml;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.ArrayUtils;

public class XMLIterator implements Iterator<XMLEvent> {
	private XMLEventReader xmlEventReader;
	private XMLEvent currEvent = null;
	private int[] ignoreTypes = new int[]{};

	public XMLIterator(InputStream input) throws XMLStreamException{
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		xmlEventReader = inputFactory.createXMLEventReader(input);
	}

	@Override
	public boolean hasNext() {
		while(xmlEventReader.hasNext()){
			try {
				XMLEvent event = xmlEventReader.nextEvent();
				if(event != null && !ArrayUtils.contains(ignoreTypes, event.getEventType())){
					currEvent = event;
					return true;
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public XMLEvent next() {
		return currEvent;
	}

	@Override
	public void remove() {
	}
}
