package com.genewoo.codereview.xmldomain;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
/*
 * Main.java
 *
 * Created on May 21, 2007, 10:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author genewu
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    public static void main(String[] args) throws Exception {
        ReviewComments instance = ReviewComments.parseReviewComments("test/ExampleXml.xml");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("build/out.xml"));
            out.write(instance.getNodeText());
            out.close();
        } catch (IOException e) {
        }
    }
    
}
