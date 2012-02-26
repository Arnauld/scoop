package org.technbolts.sog.dom;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.LocalDateTime;
import org.technbolts.sog.DataNode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DataNodeDocumentTest {

    private DataNodeDocument dataNode;
    private Root root;
    private ByteArrayOutputStream outStream;
    
    @BeforeMethod
    public void setUp () throws Exception {
        root = new Root ("event");
        dataNode = root.getRootNode();
        outStream = new ByteArrayOutputStream();
    }
    
    @Test
    public void usecase_writeValues_toXML_readAndCheck () throws Exception {
        long ts = new LocalDateTime(2011, 1, 19, 0, 7).toDate().getTime();
        
        dataNode.setBoolean("active", true);
        dataNode.setString("event-type", "StoryCreatedEvent");
        dataNode.setDate("when", new Date(ts));
        
        root.toXML(outStream);
        
        Document doc = readDocumentFromStream();
        root = new Root(doc);
        dataNode = root.getRootNode();
        assertThat(dataNode.getBoolean("active"), is(true));
        assertThat(dataNode.getString("event-type"), equalTo("StoryCreatedEvent"));
        assertThat(dataNode.getDate("when"), equalTo(new Date(ts)));
    }

    protected Document readDocumentFromStream() throws ParserConfigurationException, IOException, SAXException {
        return root.getXmlHelper().toDocument(new ByteArrayInputStream(outStream.toByteArray()));
    }
    
    @Test
    public void usecase_writeValuesInSubNodes_toXML_getSubNode_readAndCheck () throws Exception {
        dataNode.setString("headers/content-type", "text/html");
        dataNode.setInt("headers/timeout", 2000);
        
        root.toXML(outStream);
        
        Document doc = readDocumentFromStream();
        root = new Root(doc);
        DataNode dn = root.getRootNode().getSubNode("headers");
        assertThat(dn.getString("content-type"), equalTo("text/html"));
        assertThat(dn.getInt("timeout"), equalTo(2000));
    }
    
    @Test
    public void usecase_createAndAttachNode_writeValuesIn_toXML_readAndCheck () throws Exception {
        DataNode dn = dataNode.createDataNode("headers");
        dn.setString("content-type", "text/html");
        dn.setInt("timeout", 2000);
        
        dataNode.setSubNode(null, dn);
        
        root.toXML(System.out);
        root.toXML(outStream);
        
        Document doc = readDocumentFromStream();
        root = new Root(doc);
        dataNode = root.getRootNode();
        assertThat(dataNode.getString("headers/content-type"), equalTo("text/html"));
        assertThat(dataNode.getInt("headers/timeout"), equalTo(2000));
    }
}
