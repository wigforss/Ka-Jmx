package org.kasource.jmx.core.bean;

import javax.management.MBeanParameterInfo;

import org.kasource.jmx.core.util.JavadocResolver;

public class ManagedOperationParameter extends ManagedEntity {
    
    
    public ManagedOperationParameter(MBeanParameterInfo info, JavadocResolver javadocResolver) {
        super(info.getName(), info.getDescription(), info.getDescriptor(), javadocResolver);
        setTypeInfo(info.getType());
    }
    
    

}
