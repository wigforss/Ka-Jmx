package org.kasource.jmx.web.controller.websocket;

import javax.annotation.Resource;


import org.kasource.jmx.core.scheduling.Subscription;
import org.kasource.jmx.core.service.SubscriptionService;
import org.kasource.web.websocket.annotations.OnWebSocketEvent;
import org.kasource.web.websocket.annotations.WebSocketListener;
import org.kasource.web.websocket.event.WebSocketTextObjectMessageEvent;
import org.springframework.stereotype.Controller;

@Controller
@WebSocketListener("/push")
public class PushController {

    @Resource
    private SubscriptionService subscriptionService;
    
    @OnWebSocketEvent
    public void subscribe(WebSocketTextObjectMessageEvent event) {
        Subscription subscription = event.getMessageAsObject(Subscription.class);
        if(subscription.isSubscribe()) {
            subscriptionService.addListener(subscription.getKey(), new WebSocketAttributeListener(event.getSource(), event.getClientId()));
        } else {
            subscriptionService.removeListener(subscription.getKey(), new WebSocketAttributeListener(event.getSource(), event.getClientId()));
        }
    }
}
