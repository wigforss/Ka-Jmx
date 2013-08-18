package org.kasource.jmx.core.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ReflectionException;

import org.kasource.jmx.core.bean.ManagedAttributeValue;
import org.kasource.jmx.core.scheduling.AttributeKey;
import org.kasource.jmx.core.scheduling.AttributeValueListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    
    private Map<AttributeKey, Set<AttributeValueListener>> listeners = new ConcurrentHashMap<AttributeKey, Set<AttributeValueListener>>();
    private Map<AttributeKey, Object> values = new ConcurrentHashMap<AttributeKey, Object>();
    
    @Resource
    private MBeanServer server;
    
    @Override
    public void addListener(AttributeKey key, AttributeValueListener listener){
        
        Set<AttributeValueListener> listenerSet = listeners.get(key);
        if(listenerSet == null) {
            listenerSet = new HashSet<AttributeValueListener>();
            listeners.put(key, listenerSet);
           
        }
        listenerSet.add(listener);
    }
    
    @Override
    public void removeListener(AttributeKey key, AttributeValueListener listener) {
        Set<AttributeValueListener> listenerSet = listeners.get(key);
        if(listenerSet != null) {
            listenerSet.remove(listener);
            if(listenerSet.isEmpty()) {
                listeners.remove(key);
            }
        }
    }
    
    public void removeListener(AttributeValueListener listener) {
        for(Map.Entry<AttributeKey, Set<AttributeValueListener>> entry : listeners.entrySet()) {
            Set<AttributeValueListener> listenerSet = entry.getValue();
            if(listenerSet.contains(listener)) {
                listenerSet.remove(listener);
                if(listenerSet.isEmpty()) {
                    listeners.remove(entry.getKey());
                }
            }
        }
    }
    
    public Set<AttributeValueListener> getListeners(AttributeKey key){
        return listeners.get(key);
    }
    
   
    @Scheduled(fixedDelay = 1000)
    public void collectSamples() {
        if(!listeners.isEmpty()) {
            for(Map.Entry<AttributeKey, Set<AttributeValueListener>> entry : listeners.entrySet()) {
                AttributeKey key = entry.getKey();
                Set<AttributeValueListener> attributeListeners = entry.getValue();
                if(listeners != null && !listeners.isEmpty()) {
                    try {
                        Object value = server.getAttribute(key.getObjectName(), key.getAttributeName());
                        if(!values.containsKey(key) || !values.get(key).equals(value)) {
                            notifyListeners(attributeListeners, key, value);
                            if(value != null) {
                                values.put(key, value);
                            }
                        } 
                    } catch (Exception e) {
                        e.printStackTrace();
                    } 
                }
            }
        }
    }
    
    private void notifyListeners(Set<AttributeValueListener> attributeListeners, AttributeKey key, Object value) {
        ManagedAttributeValue attributeValue = new ManagedAttributeValue(key, value);
        for(AttributeValueListener listener : attributeListeners) {
            listener.onValueChange(attributeValue);
        }
    }
}
