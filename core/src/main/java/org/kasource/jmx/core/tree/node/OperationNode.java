package org.kasource.jmx.core.tree.node;

import javax.management.MBeanOperationInfo;

/**
 * JMX Operations node of the JMX Tree.
 *  
 * @author rikardwi
 **/
public class OperationNode extends JmxTreeNode {

    private MBeanOperationInfo info;
    
    public OperationNode(String label, MBeanOperationInfo info) {
        super(label, JmxTreeNodeType.OPERATION);
       this.info = info;
    }

    /**
     * @return the info
     */
    public MBeanOperationInfo getInfo() {
        return info;
    }

}
