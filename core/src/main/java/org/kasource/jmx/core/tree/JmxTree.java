package org.kasource.jmx.core.tree;

import org.kasource.jmx.core.tree.node.RootNode;

/**
 * A ManagedBeans tree.
 * 
 * @author rikardwi
 **/
public class JmxTree {
    
    private RootNode root;
    
    /**
     * Constructor.
     * 
     * @param root Root node of the tree.
     **/
    public JmxTree(RootNode root) {
        this.root = root;
    }
    
    /**
     * Returns the root node of the tree.
     * 
     * @return the root node of the tree.
     **/
    public RootNode getRoot() {
        return root;
    }
}
