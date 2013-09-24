package org.kasource.jmx.core.scheduling;


import javax.management.ObjectName;

public class AttributeKey {
    private  String name;
    private  String attributeName;
    private ObjectName objectName;
    
    public AttributeKey() {
    }
    
    public AttributeKey(String name, String attributeName) {
        setName(name);
        setAttributeName(attributeName);
       
    }

    /**
     * @return the objectName
     */
    public String getName() {
        return name;
    }

    /**
     * @return the attributeName
     */
    public String getAttributeName() {
        return attributeName;
    }
    
    public String getKey() {
        return name + "." + attributeName;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof AttributeKey) {
            return getKey().equals(((AttributeKey)obj).getKey());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    /**
     * @return the objectName
     */
    public ObjectName getObjectName() {
        return objectName;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
        try {
            this.objectName = ObjectName.getInstance(name);
        } catch (Exception e) {
           throw new IllegalArgumentException("Could not create JMX objectName", e);
        } 
    }

    /**
     * @param attributeName the attributeName to set
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    @Override
    public String toString() {
        return "AttributeKey[ObjectName: " + name + " Attribute: " + attributeName+"]";
    }
}
