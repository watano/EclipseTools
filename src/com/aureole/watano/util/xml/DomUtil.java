package com.aureole.watano.util.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DomUtil {
	public static String getAttribute(Node node, String name){
		NamedNodeMap map = node.getAttributes();
		if(map != null && map.getNamedItem(name) != null){
			return map.getNamedItem(name).getNodeValue();
		}
		return null;
	}
}
