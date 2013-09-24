package org.kasource.jmx.core.tree.node;

import javax.management.MBeanAttributeInfo;


/**
 * JMX Attribute node of the JMX Tree.
 *  
 * @author rikardwi
 **/
public class AttributeNode extends JmxTreeNode {

    private MBeanAttributeInfo info;
    
    public AttributeNode(String label, MBeanAttributeInfo info) {
        super(label, JmxTreeNodeType.ATTRIBUTE);
        this.info = info;
    }
    
    

    /**
     * @return the info
     */
    public MBeanAttributeInfo getInfo() {
        return info;
    }

   

}
