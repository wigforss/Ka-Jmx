package org.kasource.jmx.core.tree.node;

import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ObjectName;

/**
 * JMX Object node of the JMX Tree.
 *  
 * @author rikardwi
 **/
public class ObjectNode extends JmxTreeNode {

    public static final String LABEL_ATTRIBUTES = "Attributes";
    public static final String LABEL_OPERATIONS = "Operations";
    public static final String LABEL_NOTIFICATIONS = "Notifications";
    
    private MBeanInfo info;
    private String name;
    private ObjectName objectName;
    private boolean expand;
    
    public ObjectNode(String label, MBeanInfo info, String name, ObjectName objectName) {
        super(label, JmxTreeNodeType.OBJECT);
        this.info = info;
        this.objectName = objectName;
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

    public ObjectNode(ObjectNode node, String label) {
        super(label, JmxTreeNodeType.OBJECT);
        this.info = node.getInfo();
        this.objectName = node.getObjectName();
        this.name = node.getName();
        this.setChildren(node.getChildren());
    }
    
    public ObjectNode(ObjectNode node, Set<JmxTreeNode> children) {
        super(node.getLabel(), JmxTreeNodeType.OBJECT);
        this.info = node.getInfo();
        this.objectName = node.getObjectName();
        this.name = node.getName();
        this.setChildren(children);
    }
    
    public ObjectNode(ObjectNode node, String label, Set<JmxTreeNode> children) {
        super(label, JmxTreeNodeType.OBJECT);
        this.info = node.getInfo();
        this.objectName = node.getObjectName();
        this.name = node.getName();
        this.setChildren(children);
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


    /**
     * @return the objectName
     */
    public ObjectName getObjectName() {
        return objectName;
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
