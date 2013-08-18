package org.kasource.jmx.core.tree.node;

import javax.management.MBeanNotificationInfo;

public class NotificationNode extends JmxTreeNode {

    private MBeanNotificationInfo info;
    
    public NotificationNode(String label, MBeanNotificationInfo info) {
        super(label, JmxTreeNodeType.NOTIFICATION);
        this.info = info;
    }

    /**
     * @return the info
     */
    public MBeanNotificationInfo getInfo() {
        return info;
    }

}
