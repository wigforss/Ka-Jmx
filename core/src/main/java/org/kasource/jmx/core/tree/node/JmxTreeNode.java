package org.kasource.jmx.core.tree.node;



import java.util.Set;
import java.util.TreeSet;


/**
 * Abstract base class for JMX Tree Nodes.
 * 
 * @author rikardwi
 **/
public abstract class JmxTreeNode implements Comparable<JmxTreeNode>{
    private String label;
    private Set<? extends JmxTreeNode> children = new TreeSet<JmxTreeNode>();
    private JmxTreeNodeType type;
   
    public JmxTreeNode(String label, JmxTreeNodeType type) {
        this.label = label;
        this.type = type;
    }
    
    public JmxTreeNode(String label, JmxTreeNodeType type, Set<? extends JmxTreeNode> children) {
        this.label = label;
        this.type = type;
        this.children = children;
    }
    
    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }
   
    /**
     * @return the children
     */
    public Set<? extends JmxTreeNode> getChildren() {
        return children;
    }

    /**
     * @return the type
     */
    public JmxTreeNodeType getType() {
        return type;
    }

   
    
    /**
     * @param children the children to set
     */
    protected void setChildren(Set<? extends JmxTreeNode> children) {
        this.children = children;
    }
    
   
    
    @Override
    public int compareTo(JmxTreeNode node) {
        return label.compareTo(node.getLabel());
    }
    
    
}
