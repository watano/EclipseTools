package com.aureole.watano.util.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

public class XpathUtil {
	public static NodeListIterator items(XPath xpath, String expression, InputSource source) throws XPathExpressionException{
		return new NodeListIterator(xpath.evaluate(expression, source, XPathConstants.NODESET));
	}
	public static NodeListIterator items(XPath xpath, String expression, Object item) throws XPathExpressionException{
		return new NodeListIterator(xpath.evaluate(expression, item, XPathConstants.NODESET));
	}
	public static NodeListIterator items(String expression, InputSource source) throws XPathExpressionException{
		return new NodeListIterator(XPathFactory.newInstance().newXPath().evaluate(expression, source, XPathConstants.NODESET));
	}
	public static NodeListIterator items(String expression, Object item) throws XPathExpressionException{
		return new NodeListIterator(XPathFactory.newInstance().newXPath().evaluate(expression, item, XPathConstants.NODESET));
	}
}
