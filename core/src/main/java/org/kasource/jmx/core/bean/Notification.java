package org.kasource.jmx.core.bean;

import javax.management.MBeanNotificationInfo;

import org.kasource.jmx.core.util.JavadocResolver;

public class Notification extends ManagedEntity {
    
    private final String[] notifyTypes;
    
    public Notification(MBeanNotificationInfo info, JavadocResolver javadocResolver) {
        super(info.getName(), info.getDescription(), info.getDescriptor(), javadocResolver);
        this.notifyTypes = info.getNotifTypes();
    }

    /**
     * @return the notifyTypes
     */
    public String[] getNotifyTypes() {
        return notifyTypes;
    }

}
