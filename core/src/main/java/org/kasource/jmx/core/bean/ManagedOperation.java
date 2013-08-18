package org.kasource.jmx.core.bean;

import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.kasource.jmx.core.util.JavadocResolver;

public class ManagedOperation extends ManagedEntity {
    
    
    
    private final List<ManagedOperationParameter> signature = new ArrayList<ManagedOperationParameter>();
    
    public ManagedOperation(MBeanOperationInfo info, JavadocResolver javadocResolver) {
        super(info.getName(), info.getDescription(), info.getDescriptor(), javadocResolver);
        
        setTypeInfo(info.getReturnType());
        
        for(MBeanParameterInfo parameter : info.getSignature()) {
            signature.add(new ManagedOperationParameter(parameter, javadocResolver));
        }
    }

    /**
     * @return the returnType
     */
    public String getReturnType() {
        return getType();
    }

    /**
     * @return the signature
     */
    public List<ManagedOperationParameter> getSignature() {
        return signature;
    }
}
