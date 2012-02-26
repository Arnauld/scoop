package org.technbolts.sog.dom;

import java.util.Date;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.codec.binary.Base64;
import org.technbolts.sog.DataNode;
import org.technbolts.sog.DataType;
import org.technbolts.sog.exception.InvalidNodeException;
import org.technbolts.sog.exception.InvalidPathExpression;
import org.technbolts.sog.exception.MissingNodeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DataNodeDocument implements DataNode {

    private static final String TYPE = "type";
    private static final int NONE = -1;
    private static final String[] EMPTY = new String[0];
    private Root root;
    private Element element;
    
    public DataNodeDocument(Root root, Element element) {
        this.root = root;
        this.element = element;
    }

    public boolean isSet(String path) {
        return lookupNodes(path).getLength() > 0;
    }
    
    public boolean isSet(String path, int index) {
        return index < lookupNodes(path).getLength();
    }

    public void unset(String path) {
        NodeList nodes = lookupNodes(path);
        for(int i=0;i<nodes.getLength();i++) {
            Node item = nodes.item(i);
            item.getParentNode().removeChild(item);
        }
    }
    
    public void unset(String path, int index) {
        Element element = elementAtOrNone(path, index, null);
        element.getParentNode().removeChild(element);
    }
    
    public int getCount(String path) {
        return lookupNodes(path).getLength();
    }
    
    public DataNode createDataNode(String localName) {
        return new DataNodeDocument(root, root.createElement(localName));
    }
    
    public boolean getBoolean(String path) {
        String value = contentAt(path, DataType.Boolean);
        return Boolean.valueOf(value);
    }
    
    public boolean getBoolean(String path, int index) {
        String value = contentAt(path, index, DataType.Boolean);
        return Boolean.valueOf(value);
    }
    
    public Date getDate(String path) {
        String value = contentAt(path, DataType.Date);
        return new Date(Long.parseLong(value));
    }
    
    public Date getDate(String path, int index) {
        String value = contentAt(path, index, DataType.Date);
        return new Date(Long.parseLong(value));
    }
    
    public byte[] getBytes(String path) {
        String value = contentAt(path, DataType.Bytes);
        return Base64.decodeBase64(value);
    }
    
    public byte[] getBytes(String path, int index) {
        String value = contentAt(path, index, DataType.Bytes);
        return Base64.decodeBase64(value);
    }
    
    public double getDouble(String path) {
        String value = contentAt(path, DataType.Double);
        return Double.parseDouble(value);
    }
    
    public double getDouble(String path, int index) {
        String value = contentAt(path, index, DataType.Double);
        return Double.parseDouble(value);
    }
    
    public float getFloat(String path) {
        String value = contentAt(path, DataType.Float);
        return Float.parseFloat(value);
    }
    
    public float getFloat(String path, int index) {
        String value = contentAt(path, index, DataType.Float);
        return Float.parseFloat(value);
    }
    
    public int getInt(String path) {
        String value = contentAt(path, DataType.Int);
        return Integer.parseInt(value);
    }
    
    public int getInt(String path, int index) {
        String value = contentAt(path, index, DataType.Int);
        return Integer.parseInt(value);
    }
    
    public long getLong(String path) {
        String value = contentAt(path, DataType.Long);
        return Long.parseLong(value);
    }
    
    public long getLong(String path, int index) {
        String value = contentAt(path, index, DataType.Long);
        return Long.parseLong(value);
    }
    
    public String getString(String path) {
        String value = contentAt(path, DataType.String);
        return value;
    }
    
    public String getString(String path, int index) {
        String value = contentAt(path, index, DataType.String);
        return value;
    }
    
    public DataNode getSubNode(String path) {
        Element subNode = uniqueElementOrNone(path, null);
        return new DataNodeDocument(root, subNode);
    }
    
    public DataNode getSubNode(String path, int index) {
        Element subNode = elementAtOrNone(path, index, null);
        return new DataNodeDocument(root, subNode);
    }
    
    public void setBoolean(String path, boolean value) {
        setOrReplaceContent(path, DataType.Boolean, NONE, String.valueOf(value));
    }
    
    public void setBoolean(String path, int index, boolean value) {
        setOrReplaceContent(path, DataType.Boolean, index, String.valueOf(value));
    }
    
    public void setBytes(String path, byte[] value) {
        setOrReplaceContent(path, DataType.Bytes, NONE, Base64.encodeBase64String(value));
    }
    
    public void setBytes(String path, int index, byte[] value) {
        setOrReplaceContent(path, DataType.Bytes, index, Base64.encodeBase64String(value));
    }
    
    public void setDate(String path, Date value) {
        setOrReplaceContent(path, DataType.Date, NONE, String.valueOf(value.getTime()));
    }
    
    public void setDate(String path, int index, Date value) {
        setOrReplaceContent(path, DataType.Date, index, String.valueOf(value.getTime()));
    }
    
    public void setDouble(String path, double value) {
        setOrReplaceContent(path, DataType.Double, NONE, String.valueOf(value));
    }
    
    public void setDouble(String path, int index, double value) {
        setOrReplaceContent(path, DataType.Double, index, String.valueOf(value));
    }
    
    public void setFloat(String path, float value) {
        setOrReplaceContent(path, DataType.Float, NONE, String.valueOf(value));
        
    }
    
    public void setFloat(String path, int index, float value) {
        setOrReplaceContent(path, DataType.Float, index, String.valueOf(value));
    }
    
    public void setInt(String path, int value) {
        setOrReplaceContent(path, DataType.Int, NONE, String.valueOf(value));
    }
    
    public void setInt(String path, int index, int value) {
        setOrReplaceContent(path, DataType.Int, index, String.valueOf(value));
    }
    
    public void setLong(String path, int index, long value) {
        setOrReplaceContent(path, DataType.Long, index, String.valueOf(value));
    }
    
    public void setLong(String path, long value) {
        setOrReplaceContent(path, DataType.Long, NONE, String.valueOf(value));
    }
    
    public void setString(String path, int index, String value) {
        setOrReplaceContent(path, DataType.String, index, value);
    }
    
    public void setString(String path, String value) {
        setOrReplaceContent(path, DataType.String, NONE, value);
    }
    
    public void setSubNode(String path, DataNode value) {
        if(!(value instanceof DataNodeDocument))
            throw new IllegalArgumentException("Unsupported type");
        Element n = getOrCreateNodeAt(path, NONE);
        
        Node nodeToUse = ((DataNodeDocument)value).element;
        if(root.shouldCloneDataNode())
            nodeToUse = nodeToUse.cloneNode(true);
        n.appendChild(nodeToUse);
    }
    
    public void setSubNode(String path, int index, DataNode value) {
        if(!(value instanceof DataNodeDocument))
            throw new IllegalArgumentException("Unsupported type");
        Element n = getOrCreateNodeAt(path, NONE);
        Node clonedNode = ((DataNodeDocument)value).element.cloneNode(true);
        n.getParentNode().replaceChild(n, clonedNode);
    }
    
    // ~~~
    
    private void setOrReplaceContent(String path, DataType b, int index, String value) {
        Element n = getOrCreateNodeAt(path, index);
        n.setAttribute(TYPE, b.name());
        n.setTextContent(value);
    }

    protected Element getOrCreateNodeAt(String path, int index) {
        String[] fragments = (path==null)?EMPTY:path.split(Root.PathSeparator);
        
        Element n = (Element)this.element;
        Document document = n.getOwnerDocument();
        boolean checkExists = true;
        
        StringBuilder builder = new StringBuilder();
        for(String fragment : fragments) {
            builder.append(fragment).append(Root.PathSeparator);
            Element nodeToUse = null;
            if(checkExists) {
                NodeList nodes = lookupNodes(n, fragment);
                int length = nodes.getLength();
                if(index==NONE) {
                    if(length==1)
                        nodeToUse = (Element)nodes.item(0);
                    else if(length>1)
                        throw new MissingNodeException("Non unique node defined at <" + builder + ">, got: " + length);
                }
                else if(index<length) {
                    nodeToUse = (Element)nodes.item(index);
                }
            }
            if(nodeToUse==null) {
                checkExists = false;
                nodeToUse = document.createElement(fragment);
                n.appendChild(nodeToUse);
            }
            n = nodeToUse;
        }
        return n;
    }

    private String contentAt(String path, DataType dataType) {
        return uniqueElementOrNone(path, dataType).getTextContent();
    }
    
    private String contentAt(String path, int index, DataType dataType) {
        return elementAtOrNone(path, index, dataType).getTextContent();
    }
   
    protected Element elementAtOrNone(String path, int index, DataType dataType) {
        ensurePathIsValid(path);
        NodeList nodes = lookupNodes(path);
        int length = nodes.getLength();
        if(length==0)
            throw new MissingNodeException("No node defined at <" + path + ">");
        else if(index<length)
            throw new MissingNodeException("Not enough node defined at <" + path + ">, got: " + length + " expected index: " + index);
        Node node = nodes.item(index);
        Element element = checkDataType(dataType, node);
        return element;
    }
    
    protected Element uniqueElementOrNone(String path, DataType dataType) {
        ensurePathIsValid(path);
        NodeList nodes = lookupNodes(path);
        int length = nodes.getLength();
        if(length==0)
            throw new MissingNodeException("No node defined at <" + path + ">");
        else if(length>1)
            throw new MissingNodeException("Non unique node defined at <" + path + ">, got: " + length);
        Node node = nodes.item(0);
        Element element = checkDataType(dataType, node);
        return element;
    }
    
    protected Element checkDataType(DataType dataType, Node node) {
        if(!(node instanceof Element))
            throw new InvalidNodeException("Node is not an element: no attribute defined (" + node.getClass() + ")");
        Element element = (Element)node;
        if(dataType!=null) {
            String type = element.getAttribute(TYPE);
            if(!dataType.matches(type))
                throw new InvalidNodeException("Invalid node type expected: " + dataType + ", got: " + type);
        }
        return element;
    }
    
    protected NodeList lookupNodes(String path) {
        return lookupNodes(element, path);
    }
    
    protected NodeList lookupNodes(Node node, String path) {
        ensurePathIsValid(path);
        try {
            XPath xpath = root.getXmlHelper().newXPath();
            XPathExpression expression = xpath.compile(path);
            return (NodeList)expression.evaluate(node, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new InvalidPathExpression("Failed to lookup node at <" + path + ">", e);
        }
    }

    protected void ensurePathIsValid(String path) {
        root.ensurePathIsValid(path);
    }
}
