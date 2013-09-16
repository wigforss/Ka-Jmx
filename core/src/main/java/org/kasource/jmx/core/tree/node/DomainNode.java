package org.kasource.jmx.core.tree.node;


import java.util.Set;

public class DomainNode extends JmxTreeNode {

    private boolean expand;
    
    public DomainNode(String label, Set<ObjectNode> children) {
        super(label, JmxTreeNodeType.DOMAIN, children);
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the expand
     */
    public boolean isExpand() {
        return expand;
    }

    /**
     * @param expand the expand to set
     */
    public void setExpand(boolean expand) {
        this.expand = expand;
    }

}
