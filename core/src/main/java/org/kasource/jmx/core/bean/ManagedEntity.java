package org.kasource.jmx.core.bean;

import java.util.HashMap;
import java.util.Map;

import javax.management.Descriptor;

import org.kasource.jmx.core.util.JavadocResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ManagedEntity implements Comparable<ManagedEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(ManagedEntity.class);
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
        primitiveClasses.put("[B", byte[].class);
        primitiveClasses.put("[Lbyte", byte[].class);
        primitiveClasses.put("[S", short[].class);
        primitiveClasses.put("[Lshort", short[].class);
        primitiveClasses.put("[C", char[].class);
        primitiveClasses.put("[Lchar", char[].class);
        primitiveClasses.put("[I", int[].class);
        primitiveClasses.put("[Lint", int[].class);
        primitiveClasses.put("[L", long[].class);
        primitiveClasses.put("[Llong", long[].class);
        primitiveClasses.put("[F", float[].class);
        primitiveClasses.put("[Lfloat", float[].class);
        primitiveClasses.put("[D", double[].class);
        primitiveClasses.put("[Ldouble", double[].class);
        primitiveClasses.put("[Z", boolean[].class);
        primitiveClasses.put("[Lboolean", boolean[].class);
        
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
                LOG.warn("Could not load class '" + typeString + "'", e);
               
            }
        }
        this.type = typeString;
        if(typeString.startsWith("[L")) {
            this.type = typeString.substring(2).replace(";", "[]");
        } else if(typeString.startsWith("[")) {
            this.type = targetClass.getComponentType().getSimpleName() + "[]";
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
