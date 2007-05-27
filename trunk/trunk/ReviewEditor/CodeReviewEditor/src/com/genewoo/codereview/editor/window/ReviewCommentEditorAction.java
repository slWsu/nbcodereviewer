package com.genewoo.codereview.editor.window;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows ReviewCommentEditor component.
 */
public class ReviewCommentEditorAction extends AbstractAction {
    
    public ReviewCommentEditorAction() {
        super(NbBundle.getMessage(ReviewCommentEditorAction.class, "CTL_ReviewCommentEditorAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(ReviewCommentEditorTopComponent.ICON_PATH, true)));
    }
    
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = ReviewCommentEditorTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
    
}
