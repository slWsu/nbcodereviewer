package com.genewoo.codereview.xmldomain;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
/*
 * Converable.java
 *
 * Created on May 21, 2007, 9:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author genewu
 */
public abstract class Converable {
    protected StringBuilder sb = new StringBuilder();
    
    protected void startNode(String node){
        sb.append(String.format("<%s>", node));
    }
    
    protected void endNode(String node){
        sb.append(String.format("</%s>", node));
    }
    protected void addValuedNode(String node, String value){
        if(null == value || value.trim().equals("")) {
            sb.append(String.format("<%s />", node));
        }
        else {
            sb.append(String.format("<%s>%s</%s>", node, value, node));
        }
    };
    
    public abstract String getNodeText();
    
    protected void cleanNodeText() {
        if(sb.length() != 0)
        sb.delete(0, sb.length() - 1);
    }
    
    public abstract Node toNode(Document doc);
    
}
