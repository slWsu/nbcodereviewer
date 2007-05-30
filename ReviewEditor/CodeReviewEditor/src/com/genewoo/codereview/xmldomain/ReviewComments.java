package com.genewoo.codereview.xmldomain;
import com.sun.org.apache.xpath.internal.XPathAPI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * ReviewComments.java
 *
 * Created on May 15, 2007, 9:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author genewu
 */
public class ReviewComments extends Converable {
    
    public static final String VERSION = "version";
    
    public static final String COMMENTS = "comments";
    
    public static final String REVIEWCOMMENTS = "/reviewcomments";
    public static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    private String version;
    
    private Map<Integer,Comments> commentsMap = new HashMap<Integer, Comments>();
    
    private String path;
    
    /** Creates a new instance of ReviewComments */
    public ReviewComments(String path) {
        this.path = path;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Comments getComments(Integer lineNumber) {
        return this.commentsMap.get(lineNumber);
    }
    
    public void addComments(Comments comments) {
        this.commentsMap.put(comments.getPosition(), comments);
    }
    
    /**
     * Get comments size.
     * @return
     */
    public int size() {
        return this.commentsMap.size();
    }
    
    /**
     * Validate the comments list. When the internal comment count equals to zero. Remove this comments instance.
     */
    public void validateComments() {
        List<Integer> delList = new ArrayList<Integer>();
        for(Comments comments: this.commentsMap.values()){
            if(comments.getCommentList().size() == 0) {
                delList.add(comments.getPosition());
            }
        }
        for (Integer del : delList) {
            this.commentsMap.remove(del);
        }
    }
    
    
    public static ReviewComments parseReviewComments(String filename) {
        ReviewComments reviewCmts = null;
        
        File file = new File(filename);
        reviewCmts = new ReviewComments(filename);
        if(file.exists()){
            try {
                Document doc = parseXmlFile(filename);
                
                Node reviewComments = XPathAPI.selectSingleNode(doc, REVIEWCOMMENTS);
                NodeList list = reviewComments.getChildNodes();
                for(int i = 0; i < list.getLength(); i++){
                    Node node = list.item(i);
                    if(node.getNodeName().equals(VERSION)) {
                        reviewCmts.version = node.getTextContent();
                    } else if (node.getNodeName().equals(COMMENTS)) {
                        Comments comments = Comments.parseComments(node);
                        reviewCmts.addComments(comments);
                    }
                }
            } catch (Exception ex) {
                reviewCmts = null;
                ex.printStackTrace();
            }
        }
        
        
        return reviewCmts;
    }
    
    
    
    public static Node[] getNotes(NodeList nodeList) {
        List<Node> nodes = new ArrayList<Node>();
        for(int i = 0; i < nodeList.getLength(); i++){
            nodes.add(nodeList.item(i));
        }
        return nodes.toArray(new Node[] {});
    }
    
    // Parses an XML file and returns a DOM document.
    // If validating is true, the contents is validated against the DTD
    // specified in the file.
    public static Document parseXmlFile(String filename) throws Exception {
        // Create a builder factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        
        // Create the builder and parse the file
        Document doc = factory.newDocumentBuilder().parse(new File(filename));
        return doc;
        
    }
    
    public String getPath() {
        return path;
    }
    
    public Node toNode(Document doc) {
        cleanNodeText();
        Node root = doc.appendChild(doc.createElement(REVIEWCOMMENTS.replace("/", "")));
        Node ver = doc.createTextNode(VERSION);
        ver.setTextContent(this.getVersion());
        root.appendChild(ver);
        for (Comments comments : this.commentsMap.values()) {
            root.appendChild(comments.toNode(doc));
        }
        return root;
    }
    
    public synchronized  String getNodeText() {
        validateComments();
        sb.append(HEADER);
        startNode(REVIEWCOMMENTS.replace("/", ""));
        addValuedNode(VERSION, this.version);
        for (Comments comments : this.commentsMap.values()) {
            startNode(COMMENTS);
            sb.append(comments.getNodeText());
            endNode(COMMENTS);
        }
        endNode(REVIEWCOMMENTS.replace("/", ""));
        return sb.toString();
    }
    
    public synchronized void writeFile() {
        try {
            File file = new File(this.path);
            if(file.exists()) {
                file.delete();
            }
            this.validateComments();
            Writer out = new OutputStreamWriter(new FileOutputStream(this.path, false), "UTF8");
            out.write(this.getNodeText());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
