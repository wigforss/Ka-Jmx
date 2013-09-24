package org.kasource.jmx.core.util;

import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.CompositeData;

import org.w3c.dom.Text;
import org.w3c.dom.Element;

/**
 * Default implementation of JmxValueConverter, converts an Object to a representation more suitable for JSON serialization.
 * 
 * @author rikardwi
 **/
public class JmxValueConverterImpl implements JmxValueConverter {

    /**
     * Returns the value or the converted value if a conversion method is implemented.
     *  
     * @param value Value to convert.
     * 
     * @return the value or a converted value.
     **/
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
    
    /**
     * Returns the CompositeData value as a Map.
     * 
     * @param value CompositeData value to format.
     * 
     * @return the CompositeData value as a Map.
     */
    private Map<String, Object> toMap(CompositeData value) {
        Map<String, Object> data = new HashMap<String, Object>();
        for(String key : value.getCompositeType().keySet()) {
            data.put(key, value.get(key));
        }
        return data;
    }
    
    /**
     * Returns the XML Element as a String if it contains a Text as the first child, else the 
     * element itself is returned.
     * 
     * @param element XML Element to convert.
     * 
     * @return he XML Element as a String if it contains a Text as the first child.
     **/
    private Object parseXmlElement(Element element) {
        if(element.getFirstChild() instanceof Text) {
            Text text = (Text) element.getFirstChild();
            return text.getData();
        }
        return element;
    }

}
