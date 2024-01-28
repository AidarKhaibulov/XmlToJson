package com.xmltojson.xmltojson.utils;

import com.xmltojson.xmltojson.xmlmodel.XmlNode;
import com.xmltojson.xmltojson.xmlmodel.XmlTree;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlParser {

    public static XmlTree parseXml(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            Element rootElement = document.getDocumentElement();
            XmlNode rootXmlNode = parseXmlElement(rootElement);

            return new XmlTree(rootXmlNode);
        } catch (ParserConfigurationException | org.xml.sax.SAXException e) {
            throw new IOException("Error parsing XML", e);
        }
    }
    private static XmlNode parseXmlElement(Element element) {
        String nodeName = element.getNodeName();
        String nodeValue = getNodeValue(element);

        XmlNode xmlNode = new XmlNode(nodeName, nodeValue);

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                xmlNode.addChild(parseXmlElement((Element) childNode));
            }
        }

        return xmlNode;
    }
    private static String getNodeValue(Element element) {
        Node firstChild = element.getFirstChild();
        if (firstChild != null) {
            return firstChild.getNodeValue();
        } else {
            return "";
        }
    }
}
