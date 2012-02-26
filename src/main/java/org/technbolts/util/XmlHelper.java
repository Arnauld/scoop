package org.technbolts.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlHelper {
    private TransformerFactory transformerFactory;
    private XPathFactory xPathFactory;

    public XPathFactory getXPathFactory() {
        if (xPathFactory == null)
            xPathFactory = XPathFactory.newInstance();
        return xPathFactory;
    }

    public TransformerFactory getTransformerFactory() {
        if (transformerFactory == null) {
            transformerFactory = TransformerFactory.newInstance();
        }
        return transformerFactory;
    }

    public XPath newXPath() {
        XPathFactory xPathFactory = getXPathFactory();
        synchronized (xPathFactory) {
            return xPathFactory.newXPath();
        }
    }

    public Transformer getEmptyUTF8Transformer() throws TransformerConfigurationException {
        Transformer transformer = createTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        return transformer;
    }

    public Document createDocument() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    public Transformer createTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = getTransformerFactory();
        synchronized (transformerFactory) {
            return transformerFactory.newTransformer();
        }
    }

    public Templates createTemplates(Source source) throws TransformerConfigurationException {
        TransformerFactory transformerFactory = getTransformerFactory();
        synchronized (transformerFactory) {
            return transformerFactory.newTemplates(source);
        }
    }

    public void serializeUTF8(Document doc, Writer writer) throws TransformerException {
        serialize(doc, writer, getEmptyUTF8Transformer());
    }

    public void serializeUTF8(Document doc, OutputStream os) throws TransformerException {
        serialize(doc, os, getEmptyUTF8Transformer());
    }

    public void serialize(Document doc, Writer writer, Transformer transformer) throws TransformerException {
        Source source = new DOMSource(doc);
        Result result = new StreamResult(writer);

        transformer.transform(source, result);
    }

    public static void serialize(Document doc, OutputStream out, Transformer transformer) throws TransformerException {
        Source source = new DOMSource(doc);
        Result result = new StreamResult(out);

        transformer.transform(source, result);
    }

    public Document toDocument(InputStream xmlStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(xmlStream);
    }

    /**
     * Removes all illegal XML characters from specified text
     * 
     * @see <a href="http://www.w3.org/TR/REC-xml/#charsets">XML characters</a>
     * @param value
     *            value to correct
     * @return value where all invalid XML characters are removed
     */
    public static String cleanupContent(String value) {
        int length = value.length();
        StringBuilder ret = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c == 0x09 || c == 0x0A || c == 0x0D //
                    || (c >= 0x20 && c <= 0xD7FF) //
                    || (c >= 0xE000 && c <= 0xFFFD) //
                    || (c >= 0x10000 && c <= 0x10FFFF)) {
                ret.append(c);
            }
        }
        return ret.toString();
    }
}
