package com.genewoo.codereview.editor.action;

import com.genewoo.codereview.editor.window.ReviewCommentEditorTopComponent;
import com.genewoo.codereview.xmldomain.Comments;
import com.genewoo.codereview.xmldomain.ReviewComments;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.EventObject;
import javax.swing.JEditorPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.Registry;
import org.netbeans.editor.Utilities;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.modules.editor.java.JavaKit;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.text.Annotation;
import org.openide.text.NbDocument;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class ShowCommentsAction extends CookieAction implements CaretListener, PropertyChangeListener{
    
    
    class CommentAnnotation extends Annotation {
        
        static final String COMMENT_ANNOTATION_TYPE = "code-review"; // NOI18N
        
        public String getAnnotationType() {
            return COMMENT_ANNOTATION_TYPE;
        }
        
        public String getShortDescription() {
            return "Comments";
        }
        
        public CommentAnnotation(int pos) {
            this.pos = pos;
        }
        
        private int pos;
        
    }
    
    private ReviewComments rc;
    
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getOldValue() instanceof JavaKit && null == evt.getNewValue()) {
            //Close Event
            closeAll(true);
        }
    }
    /**
     * Close and clean up everything.
     *
     */
    public void closeAll(boolean isEditorClose) {
//            c.getOpenedPanes()[0].removePropertyChangeListener(this);
//            c.getOpenedPanes()[0].removeCaretListener(this);
        // Close Review Comments Editor
//        if(null !=  getMostActiveComponent()) {
        synchronized (status) {
            if(status == 0)
                return;
            
            if(! isEditorClose && null != c && null != c.getOpenedPanes()[0]) {
                Utilities.setStatusBoldText(c.getOpenedPanes()[0], NbBundle.getMessage(ShowCommentsAction.class, "CTL_CloseCommentsAction"));
                c.getOpenedPanes()[0].removeCaretListener(this);
                c.getOpenedPanes()[0].removePropertyChangeListener("editorKit", this);
            }
            //TODO rc.toNodeText() can't be invoked twice.'
            rc.writeFile();
            
            TopComponent win = WindowManager.getDefault().findTopComponent("ReviewCommentEditorTopComponent");
            status = 0;
            win.close();
            rc = null;
            
        }
    }    
    
    EditorCookie c;
    
    private Integer status = 0;
    
    protected void performAction(Node[] activatedNodes) {
        if(status == 1) {
            closeAll(false);
            
            return;
        }
        status = 1;
        c = (EditorCookie) activatedNodes[0].getCookie(EditorCookie.class);
        
        c.getOpenedPanes()[0].addCaretListener(this);
        c.getOpenedPanes()[0].addPropertyChangeListener("editorKit", this);
        
        StyledDocument sd = (StyledDocument)c.getOpenedPanes()[0].getDocument();
        rc = ReviewComments.parseReviewComments(getFileName());
        Utilities.setStatusBoldText(c.getOpenedPanes()[0], NbBundle.getMessage(ShowCommentsAction.class, "CTL_OpenCommentsAction"));
        TopComponent win = WindowManager.getDefault().findTopComponent("ReviewCommentEditorTopComponent");
        
        win.open();
        win.requestActive();
        System.out.println(getFileName());
//        try {
//            Utilities.getRowStart(getMostActiveComponent(), 0);
//        } catch (BadLocationException ex) {
//            ex.printStackTrace();
//        }
        //((ReviewCommentEditorTopComponent)win).setComments();
    }
    
    private static JTextComponent getTextComponent(EventObject e) {
        if (e != null) {
            Object o = e.getSource();
            if (o instanceof JTextComponent) {
                return (JTextComponent) o;
            }
        }
        return null;
        
    }
    
    private int linenumber = -1;
    
    public void caretUpdate(CaretEvent e) {
        if(getLineNumber(getTextComponent(e)) == linenumber)
            return;
        
        
        
//        System.out.println(getLineNumber(getTextComponent(e)));
        ReviewCommentEditorTopComponent win = (ReviewCommentEditorTopComponent) WindowManager.getDefault().findTopComponent("ReviewCommentEditorTopComponent");
        win.open();
        win.requestActive();
        win.setReviewCommentAction(this);
        Comments comments = rc.getComments(getLineNumber(getTextComponent(e)));
        if(null == comments) {
            comments = new Comments();
            comments.setPosition(getLineNumber(getTextComponent(e)));
            rc.addComments(comments);
            Utilities.setStatusText(getTextComponent(e), "Comments Created");
        } else {
            if(comments.getCommentList().size() > 0)
                Utilities.setStatusBoldText(getTextComponent(e), "Comments Loaded");
            else
                Utilities.setStatusText(getTextComponent(e), "Comments Created");
        }
        win.setComments(comments);
        win.updateUI();
        Document doc = getTextComponent(e).getDocument();
        if (doc instanceof StyledDocument) {
            Position pos;
            try {
                
                pos = doc.createPosition(e.getDot());
                
                Node[] n = TopComponent.getRegistry().getActivatedNodes();
                if (n.length == 1) {
                    EditorCookie ec = (EditorCookie) n[0].getCookie(EditorCookie.class);
                    if (ec != null) {
                        
                    }
                }
//                NbDocument.addAnnotation((StyledDocument)doc, pos, -1, new CommentAnnotation(pos));
//                System.out.println("Added");
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            
        } else {
            System.out.println(doc);
        }
    }
    
    private static void attachComment(int linenumber) {
//        FileObject fileWithError = fs.findResource("pkg/Name.java");
//        DataObject objWithError = DataObject.find(fileWithError);
//        LineCookie cookie = (LineCookie)objWithError.getCookie(LineCookie.class);
//        Line.Set lineSet = cookie.getLineSet();
//        final Line line = lineSet.getOriginal(linenumber);
//        final Annotation ann = new CommentAnnotation();
//        ann.attach(line);
    }
    
    /**
     * Try to retrieve the java code line number.
     *
     * @param component The text component looked into
     * @return Line number start from 0. (e.g. No.1 = 0)
     */
    private static int getLineNumber(JTextComponent component) {
        
        int offset = component.getCaretPosition();
        Document doc = component.getDocument();
        int line = 0;
        
        if (doc instanceof StyledDocument) {
            
            line = NbDocument.findLineNumber((StyledDocument)doc, offset);
        }
        return line;
    }
    
    public static void addStatusBarItem(JTextComponent jText) {
        
        //System.out.println("S:" + Utilities.getStatusText(jText));
    }
    
    /**
     * Find most active JTextComponent component in window.
     * <tt>Warning:</tt> if it be could't work then other functionals also get errors.
     *
     * @return  JTextComponent, return <tt>null</tt> if most active JTextComponent not found.
     */
    public static JTextComponent getMostActiveComponent() {
        TopComponent tc = TopComponent.getRegistry().getActivated();
        if (tc != null) {
            Node[] nodes = tc.getActivatedNodes();
            if (nodes == null)
                return null;
            for (int i = 0; i < nodes.length; i++) {
                EditorCookie ec = (EditorCookie)nodes[i].getCookie(EditorCookie.class);  //not enough, EditorCookie
                if (ec != null) {
                    JEditorPane[] panes = ec.getOpenedPanes();
                    if (panes != null) {
                        return panes[0];
                    }
                }
            }
        }
        return null;
    }
    
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }
    
    public String getName() {
        
        return NbBundle.getMessage(ShowCommentsAction.class, "CTL_ShowCommentsAction");
        
    }
    
    protected Class[] cookieClasses() {
        return new Class[] {
            EditorCookie.class
        };
    }
    
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
    /**
     * Get the code review file name.
     * The naming conversion is "*.java" + ".cr"
     */
    public String getFileName() {
        BaseDocument nue = Registry.getMostActiveDocument();
        JTextComponent jtc = Registry.getMostActiveComponent();
        
        FileObject fob = NbEditorUtilities.getFileObject(nue);
//        DataObject dob = NbEditorUtilities.getDataObject(nue); //setModified(false);
//        //NbEditorUtilities.getTopComponent(Registry.getMostActiveComponent());//close
//
//
//
//        System.out.println("making a copy of " + dob.getName());
//        System.out.println("folder " + dob.getFolder().getName());
//        System.out.println("file" + ":" + fob.getPath());
        return fob.getPath() + ".cr";
        
        
//      if (returnVal == JFileChooser.APPROVE_OPTION) {
//         FileObject newfile = null;
//         SaveCookie sc = (SaveCookie) dob.getCookie(SaveCookie.class);
//         File toDir = chooser.getSelectedFile().getParentFile();
//         String filename = chooser.getSelectedFile().getName();
//
//         if (sc == null) {
//            //copy fo
//            FileObject fo = dob.getPrimaryFile();
//
//            try {
//               newfile = fo.copy(FileUtil.toFileObject(toDir), filename, "");
//            } catch (final IOException ex) {
//               ex.printStackTrace();
//            }
//         } else {
//            //getText
//            String text = jtc.getText();
//            try {
//               newfile = FileUtil.createData(FileUtil.toFileObject(toDir),
//                     filename);
//
//               FileLock lock = newfile.lock();
//               OutputStream os = newfile.getOutputStream(lock);
//               PrintWriter pw = new PrintWriter(os);
//               pw.print(text);
//               pw.close();
//               os.close();
//               lock.releaseLock();
//            } catch (final IOException ex) {
//               ex.printStackTrace();
//            }
//         }
//
//         OpenCookie oc = null;
//
//         try {
//            oc = (OpenCookie) DataObject.find(newfile)
//                                        .getCookie(OpenCookie.class);
//         } catch (final DataObjectNotFoundException ex) {
//            ex.printStackTrace();
//         }
//
//         oc.open();
//      }
        
        //file chooser
        //copy or new with buffer text if modified dob
        //open copied file # open cookie
        
        //not now cause making save a copy funtionality
        //ggf setmodified false on original and close buffer (TC)
        //http://jroller.com/page/ramlog/20060715
    }
    
    
    
    
}

