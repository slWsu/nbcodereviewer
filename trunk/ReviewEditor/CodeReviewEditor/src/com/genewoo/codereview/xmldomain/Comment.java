package com.genewoo.codereview.xmldomain;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
/*
 * Comment.java
 *
 * Created on May 15, 2007, 9:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author genewu
 */
public class Comment extends Converable {
    
    public static final String OWNER = "owner";
    
    public static final String CONTENT = "content";
    
    private String owner;
    
    private String content;
    
    /** Creates a new instance of Comment */
    public Comment() {
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public static Comment parseCommentNode(Node commentNode) {
        Comment comment = new Comment();
        
        Node[] nodes = ReviewComments.getNotes(commentNode.getChildNodes());
        for (Node node : nodes) {
            if(node.getNodeName().equals(OWNER)) {
                comment.owner= node.getTextContent();
            } else{
                comment.content= node.getTextContent();
            }
        }
        return comment;
    }

    public String toString() {
        String retValue;
        retValue = String.format("[%s] %s", owner, content);
        return retValue;
    }

    public Node toNode(Document doc) {
        Node comment = doc.createElement(Comments.COMMENT);
        Node own = doc.createElement(Comment.OWNER);
        own.setTextContent(this.owner);
        Node con = doc.createElement(CONTENT);
        con.setTextContent(this.content);
        comment.appendChild(own);
        comment.appendChild(con);
        return comment;
    }

    public String getNodeText() {
        addValuedNode(OWNER, this.owner);
        addValuedNode(CONTENT, this.content);
        return sb.toString();
    }
    
    
}
