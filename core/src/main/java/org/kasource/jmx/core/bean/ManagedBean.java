package org.kasource.jmx.core.bean;

import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;

import org.kasource.jmx.core.util.JavadocResolver;

public class ManagedBean extends ManagedEntity {
   
    private final Set<ManagedAttribute> attributes = new TreeSet<ManagedAttribute>();
    private final Set<ManagedOperation> operations = new TreeSet<ManagedOperation>();
    private final Set<Notification> notifications = new TreeSet<Notification>();
    
   
    
    public ManagedBean(String name, MBeanInfo info, JavadocResolver javadocResolver) {
        super(name, info.getDescription(), info.getDescriptor(), javadocResolver);
        setTypeInfo(info.getClassName());
  
        for(MBeanAttributeInfo attribute : info.getAttributes()) {
            attributes.add(new ManagedAttribute(attribute, javadocResolver));
        }
        for(MBeanOperationInfo operation : info.getOperations()) {
            operations.add(new ManagedOperation(operation, javadocResolver));
        }
        for(MBeanNotificationInfo notification : info.getNotifications()) {
            notifications.add(new Notification(notification, javadocResolver));
        }
    }

    /**
     * @return the attributes
     */
    public Set<ManagedAttribute> getAttributes() {
        return attributes;
    }

   

    /**
     * @return the operations
     */
    public Set<ManagedOperation> getOperations() {
        return operations;
    }

    

    /**
     * @return the notifications
     */
    public Set<Notification> getNotifications() {
        return notifications;
    }

    
}
