package org.kasource.jmx.core.bean;



import org.kasource.jmx.core.scheduling.AttributeKey;

public class ManagedAttributeValue {
    private final AttributeKey key;
    private Object value;

    public ManagedAttributeValue(AttributeKey key, Object value) {
        this.key = key;
        this.value = value;
    }

    
    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }


    /**
     * @return the key
     */
    public AttributeKey getKey() {
        return key;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }

}
