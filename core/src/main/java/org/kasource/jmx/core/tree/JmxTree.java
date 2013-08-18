package org.kasource.jmx.core.tree;

import org.kasource.jmx.core.tree.node.RootNode;

public class JmxTree {
    
    private RootNode root;
    
    public JmxTree(RootNode root) {
        this.root = root;
    }
    
    public RootNode getRoot() {
        return root;
    }
}
