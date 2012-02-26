package org.technbolts.sog.dom;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.technbolts.sog.exception.InvalidPathExpression;
import org.technbolts.util.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Root {
    public static final String PathSeparator = "/";
    //
    private static final String identifier = "[a-zA-Z0-9_][a-zA-Z0-9\\-_]*";
    private static final Pattern identifierValidator = Pattern.compile(identifier);
    //
    private static final String pathValidatorRegex = identifier+"("+PathSeparator+identifier+")*";
    private static final Pattern pathValidator = Pattern.compile(pathValidatorRegex);
    
    private XmlHelper xmlHelper;
    private Document document;
    private boolean typeReadAndChecked = true;
    private boolean typeWritten = true;
    private boolean shouldCloneDataNode = false;
    
    public Root(String rootTagName) throws ParserConfigurationException, IOException, SAXException {
        if(!identifierValidator.matcher(rootTagName).matches())
            throw new InvalidPathExpression("<"+rootTagName+"> does not fullfills regex <" + identifierValidator +">");
        
        this.xmlHelper = new XmlHelper();
        this.document = xmlHelper.createDocument();
        
        document.setXmlStandalone(true);
        // make sure root node is set
        document.appendChild(createElement(rootTagName));
    }

    protected Element createElement(String tagName) {
        return document.createElement(tagName);
    }
    
    public Root(Document document) {
        this.xmlHelper = new XmlHelper();
        this.document = document;
    }
    
    public void setTypeSetOnWrite(boolean typeWritten) {
        this.typeWritten = typeWritten;
    }
    
    public boolean isTypeSetOnWrite() {
        return typeWritten;
    }
    
    public void setTypeReadAndChecked(boolean typeReadAndChecked) {
        this.typeReadAndChecked = typeReadAndChecked;
    }
    
    public boolean isTypeReadAndChecked() {
        return typeReadAndChecked;
    }
    
    public XmlHelper getXmlHelper() {
        return xmlHelper;
    }

    public DataNodeDocument getRootNode() {
        return new DataNodeDocument(this, document.getDocumentElement());
    }
    
    public void ensurePathIsValid(String path) {
        if(!pathValidator.matcher(path).matches())
            throw new InvalidPathExpression("<"+path+"> does not fullfills regex <" + pathValidatorRegex +">");
    }

    public void toXML(PrintStream out) throws TransformerException {
        xmlHelper.serializeUTF8(document, out);
    }

    public void toXML(OutputStream out) throws TransformerException {
        xmlHelper.serializeUTF8(document, out);
    }

    public boolean shouldCloneDataNode() {
        return shouldCloneDataNode;
    }
}