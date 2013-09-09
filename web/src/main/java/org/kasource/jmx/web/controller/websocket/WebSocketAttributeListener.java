package org.kasource.jmx.web.controller.websocket;

import org.kasource.jmx.core.bean.ManagedAttributeValue;
import org.kasource.jmx.core.model.dashboard.ValueType;
import org.kasource.jmx.core.scheduling.AttributeValueListener;
import org.kasource.jmx.core.scheduling.Subscription;
import org.kasource.jmx.core.service.SubscriptionService;
import org.kasource.jmx.core.util.JmxValueFormatter;
import org.kasource.web.websocket.RecipientType;
import org.kasource.web.websocket.channel.NoSuchWebSocketClient;
import org.kasource.web.websocket.channel.WebSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketAttributeListener implements AttributeValueListener {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketAttributeListener.class);
    private static final String TEXT_TYPE = ValueType.TEXT.name();
    
    private WebSocketChannel channel;
    private String clientId;
    private JmxValueFormatter jmxValueFormatter;
    private SubscriptionService subscriptionService;
    private Subscription subscription;
    
    public WebSocketAttributeListener(WebSocketChannel channel, String clientId, Subscription subscription, JmxValueFormatter jmxValueFormatter, SubscriptionService subscriptionService) {
        this.channel = channel;
        this.clientId = clientId;
        this.jmxValueFormatter = jmxValueFormatter;
        this.subscriptionService=subscriptionService;
        this.subscription = subscription;
    }
    
    @Override
    public void onValueChange(ManagedAttributeValue value) {
        try {
            
            value.setValue(jmxValueFormatter.format(value.getValue()));
            if(TEXT_TYPE.equals(subscription.getType())) {
                if(!value.getValue().getClass().isPrimitive()) {
                    value.setValue(value.getValue().toString());
                }
            }
            channel.sendMessage(value, clientId, RecipientType.CLIENT_ID);
        } catch(NoSuchWebSocketClient nswc) {
            subscriptionService.removeListener(this);
        }
        catch (Exception e) {
            LOG.debug("Could not set value for client: " + clientId, e);
        } 
        
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof WebSocketAttributeListener) {
            return this.clientId.equals(((WebSocketAttributeListener) obj).clientId);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return clientId.hashCode();
    }
    
    @Override
    public String toString() {
        return WebSocketAttributeListener.class.getName() + "[" + clientId + "]";
    }

}
