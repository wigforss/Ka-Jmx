package org.kasource.jmx.core.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.management.openmbean.CompositeData;

import org.kasource.jmx.core.bean.ManagedAttributeValue;
import org.kasource.jmx.core.scheduling.AttributeKey;
import org.kasource.jmx.core.scheduling.AttributeValueListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Default implementation of SubscriptionService.
 * 
 * @author rikardwi
 **/
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    private Map<AttributeKey, Set<AttributeValueListener>> listeners = new ConcurrentHashMap<AttributeKey, Set<AttributeValueListener>>();
    private Map<AttributeKey, Object> values = new ConcurrentHashMap<AttributeKey, Object>();
    
    
    @Resource
    private JmxService jmxService; 
    
    @Override
    public void addListener(AttributeKey key, AttributeValueListener listener){
        Object value = jmxService.getAttributeValue(key.getName(), key.getAttributeName());
        listener.onValueChange(new ManagedAttributeValue(key, value));
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
                   
                        Object value = jmxService.getAttributeValue(key.getName(), key.getAttributeName());
                       
                        if(isNewValue(key, value)) {
                            notifyListeners(attributeListeners, key, value);
                            if(value != null) {
                                values.put(key, value);
                            }
                        } 
                    
                }
            }
        }
    }
    
    
    private boolean isNewValue(AttributeKey key, Object value) {
        try {
            if (!values.containsKey(key)) {
                return true;
            }
        
            if(value.getClass().isArray()) {
                return !Arrays.deepEquals((Object[])values.get(key), (Object[])value);
            } else if(value instanceof CompositeData) {
                Object[] valueArray = ((CompositeData) value).values().toArray();
                Object[] previousArray =((CompositeData) values.get(key)).values().toArray();
                return !Arrays.deepEquals(valueArray, previousArray);
            } else if(value instanceof Collection) {
                Object[] valueArray = ((Collection) value).toArray();
                Object[] previousArray = ((Collection) values.get(key)).toArray();
                return !Arrays.deepEquals(valueArray, previousArray);
            } else {
                return !values.get(key).equals(value);
            }
        } catch(Exception e) {
            LOG.error("Could not verify value change for attribute "+ key, e);
            return false;
        }
    }
    
    private void notifyListeners(Set<AttributeValueListener> attributeListeners, AttributeKey key, Object value) {
        ManagedAttributeValue attributeValue = new ManagedAttributeValue(key, value);
        for(AttributeValueListener listener : attributeListeners) {
            listener.onValueChange(attributeValue);
        }
    }
}
