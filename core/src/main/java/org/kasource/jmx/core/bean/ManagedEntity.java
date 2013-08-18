package org.kasource.jmx.core.bean;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.management.Descriptor;

import org.kasource.jmx.core.util.JavadocResolver;


public class ManagedEntity implements Comparable<ManagedEntity> {
    private static final Map<String, Class<?>> primitiveClasses = new HashMap<String, Class<?>>();
    
    private final String name;
    private final String description;
    private final Descriptor descriptor;
    private String type;
    private Class<?> targetClass;
    private boolean enumeration;
    private Object[] enumeratedValues;
    private JavadocResolver javadocResolver;
    private String typeJavaDocUrl;
    
    static {
        primitiveClasses.put("byte", byte.class);
        primitiveClasses.put("short", short.class);
        primitiveClasses.put("char", char.class);
        primitiveClasses.put("int", int.class);
        primitiveClasses.put("long", long.class);
        primitiveClasses.put("float", float.class);
        primitiveClasses.put("double", double.class);
        primitiveClasses.put("boolean", boolean.class);
    }
    
    
    public ManagedEntity(String name, String description, Descriptor descriptor, JavadocResolver javadocResolver) {
        this.name = name;
        this.description = description;
        this.descriptor = descriptor;
        this.javadocResolver = javadocResolver;
    }
    
    
    protected void setTypeInfo(String typeString) {
        if("void".equals(typeString)) {
            this.type = typeString;
            this.targetClass = Void.TYPE;
            return;
        }
        this.targetClass = primitiveClasses.get(typeString);
        if(this.targetClass == null) {
            try {
                this.targetClass = Class.forName(typeString);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.type = typeString;
        if(typeString.startsWith("[L")) {
            this.type = typeString.substring(2).replace(";", "[]");
        } 
        
        typeJavaDocUrl = javadocResolver.getDocUrl(type.replace("[]", ""));
        
        if(type.startsWith("java.lang")) {
            this.type = this.type.substring("java.lang".length() + 1);
        } else if(type.startsWith("java.util")) {
            this.type = this.type.substring("java.util".length() + 1);
        } else {
            if(this.targetClass != null) {
                this.enumeration = this.targetClass.isEnum();
                if (this.enumeration) {
                  this.enumeratedValues = this.targetClass.getEnumConstants();
                }
            }
        }
    }
    
    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    

    /**
     * @return the descriptor
     */
    protected Descriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public int compareTo(ManagedEntity other) {
        return name.compareTo(other.getName());
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * @return the enumeration
     */
    public boolean isEnumeration() {
        return enumeration;
    }

    /**
     * @return the enumeratedValues
     */
    public Object[] getEnumeratedValues() {
        return enumeratedValues;
    }

    /**
     * @return the targetClass
     */
    public Class<?> getTargetClass() {
        return targetClass;
    }


    /**
     * @return the typeJavaDocUrl
     */
    public String getTypeJavaDocUrl() {
        return typeJavaDocUrl;
    }
}
