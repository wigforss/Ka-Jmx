package org.kasource.jmx.core.tree.node;


import java.util.Set;

/**
 * Holds a set of JMX Tree Attribute Nodes.
 *  
 * @author rikardwi
 **/
public class AttributesNode extends JmxTreeNode {

    public AttributesNode(String label, Set<AttributeNode> attributes) {
        super(label, JmxTreeNodeType.ATTRIBUTES, attributes);
    }

}
