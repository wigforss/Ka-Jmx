package org.kasource.jmx.core.tree.node;


import java.util.Set;

public class AttributesNode extends JmxTreeNode {

    public AttributesNode(String label, Set<AttributeNode> attributes) {
        super(label, JmxTreeNodeType.ATTRIBUTES, attributes);
    }

}
