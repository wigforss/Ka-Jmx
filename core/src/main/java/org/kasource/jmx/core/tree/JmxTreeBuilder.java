package org.kasource.jmx.core.tree;


import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.kasource.jmx.core.tree.node.AttributeNode;
import org.kasource.jmx.core.tree.node.AttributesNode;
import org.kasource.jmx.core.tree.node.DomainNode;
import org.kasource.jmx.core.tree.node.JmxTreeNode;
import org.kasource.jmx.core.tree.node.NotificationNode;
import org.kasource.jmx.core.tree.node.NotificationsNode;
import org.kasource.jmx.core.tree.node.ObjectNode;
import org.kasource.jmx.core.tree.node.OperationNode;
import org.kasource.jmx.core.tree.node.OperationsNode;
import org.kasource.jmx.core.tree.node.RootNode;
import org.springframework.stereotype.Component;

/***
 * Builds Tree of Managed Beans.
 * 
 * @author rikardwi
 **/
@Component
public class JmxTreeBuilder {

    private static final String LABEL_DOMAINS = "Domains";

    @Resource
    private MBeanServer server;
    
    /**
     * Returns a filtered tree from the supplied tree.
     * 
     * @param tree              Tree to filter.
     * @param filter            Name to filter on.
     * @param includeChildren   true to include attributes, operations and notifications, false to only filter on ObjectName.
     * 
     * @return a new Tree with the filter result.
     **/
    public JmxTree filterTree(JmxTree tree, String filter, boolean includeChildren) {
        String nameFilter = filter.trim().toLowerCase();
        Set<DomainNode> domainNodes = new TreeSet<DomainNode>();
        
        for(JmxTreeNode domain : tree.getRoot().getChildren()) {
            Set<ObjectNode> objectNodes = new TreeSet<ObjectNode>();
            for(JmxTreeNode object : domain.getChildren()) {
                ObjectNode objectNode = (ObjectNode) object;
                if(objectNode.getObjectName().getCanonicalName().toLowerCase().contains(nameFilter)) {
                    if(includeChildren) {
                        Set<JmxTreeNode> objectChildren = getMatchingChildren(nameFilter, objectNode);
                        ObjectNode newNode = new ObjectNode(objectNode, replaceMatch(objectNode.getLabel(), nameFilter), objectChildren);
                        newNode.setExpand(true);
                        objectNodes.add(newNode);
                    } else {
                        objectNodes.add(new ObjectNode(objectNode, replaceMatch(objectNode.getLabel(), nameFilter)));
                    }
                   
                } else if(includeChildren){
                    addObjectNodeChildMatch(nameFilter, objectNode, objectNodes);
                }
            }
            if(!objectNodes.isEmpty()) {
                DomainNode domainNode = new DomainNode(domain.getLabel(), objectNodes);
                domainNode.setExpand(true);
                domainNodes.add(domainNode);
            }
        }
        
        RootNode root = new RootNode(LABEL_DOMAINS, domainNodes);
        JmxTree jmxTree = new JmxTree(root);
        return jmxTree;
    }
    
    /**
     * Returns the matching child nodes of a ManagedObject.
     * 
     * @param nameFilter    Name to filter on
     * @param objectNode    The object to inspect.
     * 
     * @return the matching child nodes of objectNode.
     **/
    private Set<JmxTreeNode> getMatchingChildren(String nameFilter, ObjectNode objectNode) {
        AttributesNode attributesNode = null;
        OperationsNode operationsNode = null;
        NotificationsNode notificationsNode = null;
        Set<JmxTreeNode> objectChildren = new TreeSet<JmxTreeNode>();
        for(JmxTreeNode child : objectNode.getChildren()) {

           
                if(child instanceof AttributesNode) {
                    Set<AttributeNode> attributes = getMatchingAttributes((AttributesNode) child, nameFilter);
                    if(!attributes.isEmpty()) {
                         attributesNode = new AttributesNode(ObjectNode.LABEL_ATTRIBUTES, attributes);
                    }
                } else if (child instanceof OperationsNode) {
                    Set<OperationNode> operations = getMatchingOperations((OperationsNode) child, nameFilter);
                    if(!operations.isEmpty()) {
                        operationsNode = new OperationsNode(ObjectNode.LABEL_OPERATIONS, operations);
                    }
                } else if (child instanceof NotificationsNode) {
                    Set<NotificationNode> notifications = getMatchingNotifications((NotificationsNode) child, nameFilter);
                    if(!notifications.isEmpty()) {
                        notificationsNode = new NotificationsNode(ObjectNode.LABEL_NOTIFICATIONS, notifications);
                    }
                }
                
            
        }
        
        if(attributesNode != null) {
            objectChildren.add(attributesNode);
        }
        if(operationsNode != null) {
            objectChildren.add(operationsNode);
        }
        if(notificationsNode != null) {
            objectChildren.add(notificationsNode);
        }
        return objectChildren;
    }
    
    private void addObjectNodeChildMatch(String nameFilter, ObjectNode objectNode, Set<ObjectNode> objectNodes) {
        Set<JmxTreeNode> objectChildren = getMatchingChildren(nameFilter, objectNode);
        
        if(!objectChildren.isEmpty()) {
            ObjectNode newNode = new ObjectNode(objectNode, objectChildren);
            newNode.setExpand(true);
            objectNodes.add(newNode);
        }
      
    }
    
    private Set<AttributeNode> getMatchingAttributes(AttributesNode node, String nameFilter) {
        Set<AttributeNode> matches = new TreeSet<AttributeNode>();
        for(JmxTreeNode child : node.getChildren()) {
            AttributeNode attribute = (AttributeNode) child;
            if(attribute.getLabel().toLowerCase().contains(nameFilter)) {
                AttributeNode newNode = new AttributeNode(replaceMatch(attribute.getLabel(), nameFilter), attribute.getInfo());
                matches.add(newNode);
            }
        }
        return matches;
    }
    
   
 
    private Set<OperationNode> getMatchingOperations(OperationsNode node, String nameFilter) {
        Set<OperationNode> matches = new TreeSet<OperationNode>();
        for(JmxTreeNode child : node.getChildren()) {
            OperationNode operation = (OperationNode) child;
            if(operation.getLabel().toLowerCase().contains(nameFilter)) {
                OperationNode newNode = new OperationNode(replaceMatch(operation.getLabel(), nameFilter), operation.getInfo());
                matches.add(newNode);
            }
        }
        return matches;
    }
    
    private Set<NotificationNode> getMatchingNotifications(NotificationsNode node, String nameFilter) {
        Set<NotificationNode> matches = new TreeSet<NotificationNode>();
        for(JmxTreeNode child : node.getChildren()) {
            NotificationNode notification = (NotificationNode) child;
            if(notification.getLabel().toLowerCase().contains(nameFilter)) {
                NotificationNode newNode = new NotificationNode(replaceMatch(notification.getLabel(), nameFilter), notification.getInfo());
                matches.add(newNode);
            }
        }
        return matches;
    }
    
  
    
    private String replaceMatch(String label, String nameFilter) {
        Pattern pattern = Pattern.compile(Pattern.quote(nameFilter), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(label);
        Set<String> matches = new HashSet<String>();
        while(matcher.find()) {
            matches.add(matcher.group());
        }
        if (!matches.isEmpty()) {
            
            for(String nameMatch : matches) {
                label = label.replaceAll(Pattern.quote(nameMatch), "<span class=\"filterHit\">"+nameMatch+"</span>");
            }
        }
        return label;
    }
   

    public JmxTree buildTree() {
        Set<DomainNode> domainNodes = new TreeSet<DomainNode>();
        String[] domains = server.getDomains();
        for (String domain : domains) {
            try {
                DomainNode domainNode = buildDomain(domain);
                domainNodes.add(domainNode);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        RootNode root = new RootNode(LABEL_DOMAINS, domainNodes);
        return new JmxTree(root);
    }

    private DomainNode buildDomain(String domain) throws MalformedObjectNameException, NullPointerException {

        Set<ObjectNode> objectNodes = new TreeSet<ObjectNode>();

        Set<ObjectName> objectNames = server.queryNames(ObjectName.getInstance(domain + ":*"), null);

        for (ObjectName objectInDomain : objectNames) {
            try {
                ObjectNode objectNode = buildObject(objectInDomain);
                objectNodes.add(objectNode);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return new DomainNode(domain, objectNodes);
    }

    private ObjectNode buildObject(ObjectName objectName) throws IntrospectionException, InstanceNotFoundException, ReflectionException {
        MBeanInfo beanInfo = server.getMBeanInfo(objectName);
        String label = objectName.getKeyProperty("name");
       
        if(label == null) {
            label = objectName.getCanonicalKeyPropertyListString();
        } else {
           
            if(objectName.getKeyPropertyList().size() > 1) {
                label += " ("+ objectName.getCanonicalKeyPropertyListString().replace(",name="+label, "").replace("name=" + label + ",", "") + ")";
            }
        }
    
        return new ObjectNode(label, beanInfo, objectName.toString(), objectName);

    }
}
