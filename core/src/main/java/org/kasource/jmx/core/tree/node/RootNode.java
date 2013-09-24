package org.kasource.jmx.core.tree.node;

import java.util.Set;

/**
 * Root node of the JMX Tree.
 *  
 * @author rikardwi
 **/
public class RootNode extends JmxTreeNode {

    public RootNode(String label, Set<DomainNode> children) {
        super(label, JmxTreeNodeType.ROOT, children);
    }

   
    
}
