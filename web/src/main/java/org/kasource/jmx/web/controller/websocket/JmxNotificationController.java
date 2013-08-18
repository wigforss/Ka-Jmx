package org.kasource.jmx.web.controller.websocket;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.management.Notification;
import javax.management.NotificationListener;

import org.kasource.jmx.core.service.JmxService;
import org.kasource.web.websocket.annotations.WebSocketListener;
import org.kasource.web.websocket.channel.WebSocketChannel;

import org.springframework.stereotype.Controller;

@Controller
@WebSocketListener("/notifications")
public class JmxNotificationController implements NotificationListener {

    @Resource
    private JmxService jmxService;
    
    @Resource(name = "notificationChannel")
    private WebSocketChannel notificationChannel;
    
    @SuppressWarnings("unused")
    @PostConstruct
    private void initialize() {
        jmxService.addNotificationListener(this);
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        notificationChannel.broadcast(notification);
        
    }
    
}
