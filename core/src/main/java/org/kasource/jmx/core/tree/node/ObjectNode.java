package org.kasource.jmx.core.tree.node;

import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;


public class ObjectNode extends JmxTreeNode {

    private static final String LABEL_ATTRIBUTES = "Attributes";
    private static final String LABEL_OPERATIONS = "Operations";
    private static final String LABEL_NOTIFICATIONS = "Notifications";
    
    private MBeanInfo info;
    private String name;
    
    public ObjectNode(String label, MBeanInfo info, String name) {
        super(label, JmxTreeNodeType.OBJECT);
        this.info = info;

        this.name = name;
        Set<JmxTreeNode> nodes = new TreeSet<JmxTreeNode>();
        
        AttributesNode attributes = getAttributes();
        if(!attributes.getChildren().isEmpty()) {
            nodes.add(attributes);
        }
        
        OperationsNode operations = getOperations(); 
        if(!operations.getChildren().isEmpty()) {
            nodes.add(operations);
        }
        
        NotificationsNode notifications = getNotifications();
        if(!notifications.getChildren().isEmpty()) {
            nodes.add(notifications);
        }
        setChildren(nodes);
    }

    
    private AttributesNode getAttributes() {
        MBeanAttributeInfo[] attributes = info.getAttributes();
        Set<AttributeNode> attributeNodes = new TreeSet<AttributeNode>();
        for (MBeanAttributeInfo attribute : attributes) {
            attributeNodes.add(new AttributeNode(attribute.getName(), attribute));
        }
        return new AttributesNode(LABEL_ATTRIBUTES, attributeNodes);
    }
    
    private OperationsNode getOperations() {
        MBeanOperationInfo[] operations = info.getOperations();
        Set<OperationNode> operationNodes = new TreeSet<OperationNode>();
        for (MBeanOperationInfo operation : operations) {
            operationNodes.add(new OperationNode(operation.getName(), operation));
        }
        return new OperationsNode(LABEL_OPERATIONS, operationNodes);
    }
    
    private NotificationsNode getNotifications() {
        MBeanNotificationInfo[] notifications = info.getNotifications();
        Set<NotificationNode> notificationNodes = new TreeSet<NotificationNode>();
        for (MBeanNotificationInfo notification : notifications) {
            notificationNodes.add(new NotificationNode(notification.getName(), notification));
        }
        return new NotificationsNode(LABEL_NOTIFICATIONS, notificationNodes);
    }


    /**
     * @return the info
     */
    public MBeanInfo getInfo() {
        return info;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
}
