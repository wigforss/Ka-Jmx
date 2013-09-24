package org.kasource.jmx.core.tree.node;

import java.util.Set;

/**
 * Holds a set of JMX Tree Operation Nodes.
 *  
 * @author rikardwi
 **/
public class OperationsNode extends JmxTreeNode {

    public OperationsNode(String label, Set<OperationNode> operations) {
        super(label, JmxTreeNodeType.OPERATIONS, operations);
    }

}
