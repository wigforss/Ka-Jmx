package org.kasource.jmx.web.controller.websocket;

import org.kasource.jmx.core.bean.ManagedAttributeValue;
import org.kasource.jmx.core.scheduling.AttributeValueListener;
import org.kasource.web.websocket.RecipientType;
import org.kasource.web.websocket.channel.WebSocketChannel;

public class WebSocketAttributeListener implements AttributeValueListener {

    private WebSocketChannel channel;
    private String clientId;
    
    public WebSocketAttributeListener(WebSocketChannel channel, String clientId) {
        this.channel = channel;
        this.clientId = clientId;
    }
    
    @Override
    public void onValueChange(ManagedAttributeValue value) {
        try {
            channel.sendMessage(value, clientId, RecipientType.CLIENT_ID);
        } catch (Exception e) {
      
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
