package org.kasource.jmx.core.tree.node;


import java.util.Set;

public class DomainNode extends JmxTreeNode {

    public DomainNode(String label, Set<ObjectNode> children) {
        super(label, JmxTreeNodeType.DOMAIN, children);
        // TODO Auto-generated constructor stub
    }

}
