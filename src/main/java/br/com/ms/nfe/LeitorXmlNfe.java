package br.com.ms.nfe;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LeitorXmlNfe {
	
	public String localizaTg(String tag, File xml) throws Exception {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
			NodeList infNFe = doc.getElementsByTagName(tag);
			Node no = infNFe.item(0);			
			return no.getTextContent();
		}catch(Exception e) {
			throw e;
		}
	}
	
}
