package com.aureole.watano.util.xml;

import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class SimpleXMLReader<T> {
	protected T param = null;

	public SimpleXMLReader(T object) {
		super();
		this.param = object;
	}
	public T parse(InputStream input){
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(input);
			doParseStart();
			while (xmlEventReader.hasNext()) {
				XMLEvent event = xmlEventReader.nextEvent();
				doEventStart(event);
				switch(event.getEventType()){
				case XMLStreamConstants.START_ELEMENT:
					doStartElement(event.asStartElement(), event.asStartElement().getName().getLocalPart());
					break;
				case XMLStreamConstants.END_ELEMENT:
					doEndElement(event.asEndElement(), event.asEndElement().getName().getLocalPart());
					break;
				case XMLStreamConstants.CHARACTERS:
					doCharacters(event.asCharacters());
					break;
				case XMLStreamConstants.ATTRIBUTE:
					if(event instanceof Attribute){
						Attribute attr = (Attribute)event;
						doAttribute(attr, attr.getName().getLocalPart());
					}
					break;
				case XMLStreamConstants.NAMESPACE:
					if(event instanceof Namespace){
						doNamespace((Namespace)event);
					}
					break;
				case XMLStreamConstants.PROCESSING_INSTRUCTION:
					if(event instanceof ProcessingInstruction){
						doProcessingInstruction((ProcessingInstruction)event);
					}
					break;
				case XMLStreamConstants.COMMENT:
					if(event instanceof Comment){
						doComment((Comment)event);
					}
					break;
				case XMLStreamConstants.START_DOCUMENT:
					if(event instanceof StartDocument){
						doStartDocument((StartDocument)event);
					}
					break;
				case XMLStreamConstants.END_DOCUMENT:
					if(event instanceof EndDocument){
						doEndDocument((EndDocument)event);
					}
					break;
				case XMLStreamConstants.DTD:
					if(event instanceof DTD){
						doDTD((DTD)event);
					}
					break;
				default:
					break;
				}

				doEventEnd(event);
			}
			doParseEnd();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return param;
	}

	public static String attrByName(StartElement startElement, String name){
		return startElement.getAttributeByName(new QName(name)).getValue();
	}

	private void doNamespace(Namespace event) {}
	private void doProcessingInstruction(ProcessingInstruction event) {}
	private void doDTD(DTD event) {}
	private void doComment(Comment event) {}
	public void doStartDocument(StartDocument event){}
	public void doEndDocument(EndDocument event){}
	public void doCharacters(Characters text) {}
	public void doParseStart(){}
	public void doParseEnd() {}
	public void doEventStart(XMLEvent event){}
	public void doEventEnd(XMLEvent event) {}
	public void doStartElement(StartElement startElement, String tag){}
	public void doEndElement(EndElement endElement, String tag){}
	public void doAttribute(Attribute attr, String name) {}
}
