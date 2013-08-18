package org.kasource.jmx.core.tree.node;

import java.util.Set;

public class RootNode extends JmxTreeNode {

    public RootNode(String label, Set<DomainNode> children) {
        super(label, JmxTreeNodeType.ROOT, children);
    }

   
    
}
