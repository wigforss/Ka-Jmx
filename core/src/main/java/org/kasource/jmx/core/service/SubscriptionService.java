package org.kasource.jmx.core.service;

import org.kasource.jmx.core.scheduling.AttributeKey;
import org.kasource.jmx.core.scheduling.AttributeValueListener;

public interface SubscriptionService {

    public void addListener(AttributeKey key, AttributeValueListener listener);

    public void removeListener(AttributeKey key, AttributeValueListener listener);
    
    public void removeListener(AttributeValueListener listener);

}
