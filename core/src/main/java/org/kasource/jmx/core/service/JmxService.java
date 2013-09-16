package org.kasource.jmx.core.service;

import java.util.Map;
import java.util.Set;

import javax.management.MBeanInfo;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.kasource.jmx.core.bean.ManagedBean;
import org.kasource.jmx.core.tree.JmxTree;

public interface JmxService {

    ManagedBean getBeanInfo(String objectName);
    
    Map<String, Object> getAttributeValues(String name);
    
    Object getAttributeValue(String name, String attribute);
    
    MBeanInfo getBeanInfo(String domain, String objectName);
    
    Object invokeOperation(String objectName, String operationName, Object[] params);
    
    void setAttributes(String objectName, Map<String, Object> attributeValues);
    
    Set<ObjectName> getNamesMatching(String pattern);
    
    JmxTree getJmxTree();
    
    JmxTree filterTree(JmxTree tree, String objectNameFilter, boolean includeChildren);
    
    void refreshTree();
    
    void addNotificationListener(NotificationListener listener); 
}
