package org.kasource.jmx.core.scheduling;

import org.kasource.jmx.core.model.dashboard.ValueType;

public class Subscription {
    private boolean subscribe;
    private AttributeKey key;
    private String type = ValueType.TEXT.name();
    
    /**
     * @return the subscribe
     */
    public boolean isSubscribe() {
        return subscribe;
    }
    /**
     * @param subscribe the subscribe to set
     */
    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
    /**
     * @return the key
     */
    public AttributeKey getKey() {
        return key;
    }
    /**
     * @param key the key to set
     */
    public void setKey(AttributeKey key) {
        this.key = key;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
