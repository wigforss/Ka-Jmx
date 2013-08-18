package org.kasource.jmx.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.kasource.jmx.core.bean.ManagedAttribute;
import org.kasource.jmx.core.bean.ManagedBean;
import org.kasource.jmx.core.tree.JmxTree;
import org.kasource.jmx.core.tree.JmxTreeBuilder;
import org.kasource.jmx.core.util.JavadocResolver;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
@ManagedResource(objectName="KaJMX:name=JmxService", description="JMX Service used by the JMX Console")
@Service
public class JmxServiceImpl implements JmxService, NotificationListener {

    @Resource
    private MBeanServer server;

    @Resource
    private JmxTreeBuilder treeBuilder;

    @Resource
    private JavadocResolver javadocResolver;
    
    private JmxTree jmxTree;

    private Set<NotificationListener> listeners = new HashSet<NotificationListener>();

    @SuppressWarnings("unused")
    @PostConstruct
    private void initialize() {
      
        registerAsNotificationListener();
    }

    private void registerAsNotificationListener() {
        Set<ObjectName> objectNames = getAllNames();
        for (ObjectName objectName : objectNames) {
            try {
                MBeanInfo beanInfo = server.getMBeanInfo(objectName);
                if (beanInfo.getNotifications().length > 0) {
                    server.addNotificationListener(objectName, this, null, null);
                }
            } catch (Exception e) {

            }

        }

    }

    private Set<ObjectName> getAllNames() {
        try {
            return server.queryNames(ObjectName.getInstance("*:*"), null);
        } catch (Exception e) {
            return new HashSet<ObjectName>();
        }
    }

    @Override
    public void setAttributes(String objectName, Map<String, Object> attributeValues) {
        AttributeList attributes = new AttributeList();
        for (Map.Entry<String, Object> entry : attributeValues.entrySet()) {
            attributes.add(new Attribute(entry.getKey(), entry.getValue()));
        }
        try {
            server.setAttributes(getObjectName(objectName), attributes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not set attribute value for: " + objectName, e);
        }

    }
    
    
 

    @Override
    public MBeanInfo getBeanInfo(String domain, String name) {
        try {
            return server.getMBeanInfo(getObjectName(domain, name));
        } catch (Exception e) {
            throw new IllegalStateException("Could not access bean " + domain + ":" + name, e);
        }
    }
    
    @Override
    public ManagedBean getBeanInfo(String name) {
        try {
            return new ManagedBean(name, server.getMBeanInfo(ObjectName.getInstance(name)), javadocResolver);
        } catch (Exception e) {
            throw new IllegalStateException("Could not access bean " + name, e);
        }
    }

    
    

   
   

   

    private ObjectName getObjectName(String domain, String name) {
        try {
            return ObjectName.getInstance(domain + ":" + name);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not create object name for: " + domain + ":" + name, e);
        }
    }
    
    private ObjectName getObjectName(String name) {
        try {
            return ObjectName.getInstance(name);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not create object name for: " + name, e);
        }
    }


    
    @Override
    public Object invokeOperation(String objectName, String operationName, Object[] params) {
        List<String> signatureList = new ArrayList<String>();
        for (Object param : params) {
            signatureList.add(param.getClass().getName());
        }
        String[] signature = new String[signatureList.size()];
        signatureList.toArray(signature);
        try {
            Object result = server.invoke(getObjectName(objectName), operationName, params, signature);
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke operation ", e);
        }
    }
    
    /**
     * @return the jmxTree
     */
    public JmxTree getJmxTree() {
        if(jmxTree == null) {
            refreshTree();
        }
        return jmxTree;
    }

    public void addNotificationListener(NotificationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        for (NotificationListener listener : listeners) {
            listener.handleNotification(notification, handback);
        }

    }

   
    
    @Override
    public Map<String, Object> getAttributeValues(String name) {
        return getAttributeValues(getObjectName(name));
    }
    
    private Map<String, Object> getAttributeValues(ObjectName name) {
        Map<String, Object> attributeValues = new HashMap<String, Object>();
        ManagedBean beanInfo = getBeanInfo(name.toString());
       
        for (ManagedAttribute attribute : beanInfo.getAttributes()) {
            try {
                Object value = server.getAttribute(name, attribute.getName());
                if(value.getClass().isArray()) {
                    value = asList(value, value.getClass());
                }
                attributeValues.put(attribute.getName(),
                            value);
            } catch (Exception e) {
            }
        }
        return attributeValues;
    }

    private <T> List<T> asList(Object value, Class<T> ofClass) {
        @SuppressWarnings("unchecked")
        T[] values = (T[]) value;
        return Arrays.asList(values);
    }
    
    @ManagedOperation(description="Refreshes the cached JMX Tree")
    @Override
    public void refreshTree() {
        jmxTree = treeBuilder.buildTree();
        
    }

}
