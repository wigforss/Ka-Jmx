package org.kasource.jmx.core.util;

import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.CompositeData;

import org.w3c.dom.Text;
import org.w3c.dom.Element;

public class JmxValueConverterImpl implements JmxValueConverter {

    @Override
    public Object convert(Object value) {
       if(value instanceof Element) {
           return parseXmlElement((Element) value);
       }
       if(value instanceof CompositeData) {
           return toMap((CompositeData)value);
       }
       return value;
    }
    
    private Map<String, Object> toMap(CompositeData value) {
        Map<String, Object> data = new HashMap<String, Object>();
        for(String key : value.getCompositeType().keySet()) {
            data.put(key, value.get(key));
        }
        return data;
    }
    
    private Object parseXmlElement(Element element) {
        if(element.getFirstChild() instanceof Text) {
            Text text = (Text) element.getFirstChild();
            return text.getData();
        }
        return element;
    }

}
