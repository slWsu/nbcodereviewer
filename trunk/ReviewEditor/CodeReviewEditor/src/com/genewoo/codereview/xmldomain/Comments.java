package com.genewoo.codereview.xmldomain;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
/*
 * Comments.java
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
public class Comments extends Converable {
    
    public static final String POSITION = "position";
    public static final String COMMENT = "comment";
    
    private int position = -1;
    
    List<Comment> commentList = new ArrayList<Comment>();
    
    /** Creates a new instance of Comments */
    public Comments() {
    }
    
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    
    public List<Comment> getCommentList() {
        return commentList;
    }
    
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
    
    public static Comments parseComments(Node commentsNode) {
        Comments comments = new Comments();
        
        Node[] nodes = ReviewComments.getNotes(commentsNode.getChildNodes());
        for (Node node : nodes) {
            if(node.getNodeName().equals(POSITION)) {
                comments.position = Integer.parseInt(node.getTextContent());
            } else if (node.getNodeName().equals(COMMENT)) {
                comments.commentList.add(Comment.parseCommentNode(node));
            }
        }
        return comments;
    }
    
    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }
    
    public void removeComment(int index) {
        this.commentList.remove(index);
    }
//    public CommentsListModel getListModel() {
//        return new CommentsListModel(this);
//    }
//    
    public Node toNode(Document doc) {
        Node node = doc.createElement(ReviewComments.COMMENTS);
        Node pos = doc.createTextNode(POSITION);
        pos.setTextContent(Integer.toString(this.position));
        node.appendChild(pos);
        for (Comment comment : this.commentList) {
            node.appendChild(comment.toNode(doc));
        }
        return node;
    }
    
    public String getNodeText() {
        addValuedNode(POSITION, Integer.toString(this.position));
        for (Comment comment : this.commentList) {
            startNode(COMMENT);
            sb.append(comment.getNodeText());
            endNode(COMMENT);
        }
        return sb.toString();
    }
    
    
}
