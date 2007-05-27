package com.genewoo.codereview.xmldomain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.AbstractListModel;

public class CommentsListModel extends AbstractListModel {
    
    private Comments comments;
    
    
    public int getSize() {
        return comments.getCommentList().size();
    }
    
    public Object getElementAt(int index) {
        return comments.getCommentList().get(index);
    }
    
    public void add(Object element) {
        if (comments.commentList.add((Comment)element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }
    
    public void addAll(Object elements[]) {
        for (Object elem : elements) {
            add(elem);            
        }
        fireContentsChanged(this, 0, getSize());
    }
    
    public void clear() {
        comments.commentList.clear();
        fireContentsChanged(this, 0, getSize());
    }
    
    public boolean contains(Object element) {
        return comments.commentList.contains(element);
    }
    
    public Object firstElement() {
        return comments.commentList.get(0);
    }
    
    public Iterator iterator() {
        return comments.commentList.iterator();
    }
    
    public Object lastElement() {
        if (getSize() >  0) {
            return getElementAt(getSize()-1);
        }        
        else
            return null;
    }
    
    public boolean removeElement(Object element) {
        boolean removed = comments.commentList.remove(element);
        if (removed) {
            fireContentsChanged(this, 0, getSize());
        }
        return removed;
    }
    
    public boolean remove(int index) {
        return removeElement(getElementAt(index));
    }
    
    public void setComments(Comments comments) {
        this.comments = comments;
        fireContentsChanged(this, 0, this.getSize());
    }

    public CommentsListModel(Comments comments) {
        this.comments = comments;
    }
}
