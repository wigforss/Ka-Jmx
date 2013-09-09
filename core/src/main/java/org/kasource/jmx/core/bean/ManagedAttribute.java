package org.kasource.jmx.core.bean;



import javax.management.MBeanAttributeInfo;

import org.kasource.jmx.core.util.JavadocResolver;

public class ManagedAttribute extends ManagedEntity {
    
   
    private final boolean writable;
    private final boolean readable;
  
    
    public ManagedAttribute(MBeanAttributeInfo info, JavadocResolver javadocResolver) {
        super(info.getName(), info.getDescription(), info.getDescriptor(), javadocResolver);
        setTypeInfo(info.getType());
       
        this.writable = info.isWritable();
        this.readable = info.isReadable();
    }
    
    

   

    /**
     * @return the writable
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * @return the readable
     */
    public boolean isReadable() {
        return readable;
    }

   

    
    
}
