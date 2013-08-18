package org.kasource.jmx.core.scheduling;

public class Subscription {
    private boolean subscribe;
    private AttributeKey key;
    
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
}
