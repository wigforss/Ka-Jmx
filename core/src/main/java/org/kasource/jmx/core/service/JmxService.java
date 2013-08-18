package org.kasource.jmx.core.service;

import java.util.Map;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.NotificationListener;

import org.kasource.jmx.core.bean.ManagedBean;
import org.kasource.jmx.core.tree.JmxTree;

public interface JmxService {

    ManagedBean getBeanInfo(String objectName);
    
    Map<String, Object> getAttributeValues(String name);
    
    MBeanInfo getBeanInfo(String domain, String objectName);
    
    Object invokeOperation(String objectName, String operationName, Object[] params);
    
    void setAttributes(String objectName, Map<String, Object> attributeValues);
    
    
    JmxTree getJmxTree();
    
    void refreshTree();
    
    void addNotificationListener(NotificationListener listener); 
}
