package org.kasource.jmx.core.tree.node;


import java.util.Set;

/**
 * Holds a set of JMX Tree Notification Nodes.
 *  
 * @author rikardwi
 **/
public class NotificationsNode extends JmxTreeNode {

    public NotificationsNode(String label, Set<NotificationNode> notifications) {
        super(label, JmxTreeNodeType.NOTIFICATIONS, notifications);
    }

}
