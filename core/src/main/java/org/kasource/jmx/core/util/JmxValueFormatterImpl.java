package org.kasource.jmx.core.util;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.management.openmbean.CompositeData;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Default implementation of JmxValueFormatter that formats a value to a presentation friendly representation.
 * 
 * @author rikardwi
 **/
public class JmxValueFormatterImpl implements JmxValueFormatter {
    private static final long ONE_DAY_IN_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
    
    /**
     * Looks up the null place holder from properties
     **/
    @Value("${null.placeholder}")
    private String nullStringValue;
    
    /**
     * Looks up the date pattern from properties
     **/
    @Value("${date.pattern}")
    private String datePattern;
    
    /**
     * Looks up the date time pattern from properties
     **/
    @Value("${date.time.pattern}")
    private String dateTimePattern;
    
    /**
     * Returns a formatted representation for the value.
     * 
     * @param value Value to format.
     * 
     * @return a formatted representation for the value.
     **/
    @Override
    public Object format(Object value) {
        if (value == null) {
            return nullStringValue;
        }
        if(value.getClass().isArray()) {
            return asList(value);
        } else if (value instanceof Date) {
           Date date = (Date) value;
           if (date.getTime() % ONE_DAY_IN_MILLIS == 0) {
               return new SimpleDateFormat(datePattern).format(date);
           } else {
               return new SimpleDateFormat(dateTimePattern).format(date);
           }
        } else if(value instanceof CompositeData) {
            return toMap((CompositeData)value);
        } else if(value instanceof Element) {
            return parseXmlElement((Element) value);
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
     * Returns the XML Element as a String.
     * 
     * @param element XML Element to format.
     * 
     * @return XML Element as a String.
     **/
    private String parseXmlElement(Element element) {
        if(element.getFirstChild() instanceof Text) {
            Text text = (Text) element.getFirstChild();
            return text.getData();
        }
       
        return toXml(element);
    }
    
    /**
     * Returns the XML node as String of XML.
     * 
     * @param node Node to convert to String of XML
     * 
     * @return the XML node as String of XML.
     **/
    private String toXml(Node node) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(node);
            transformer.transform(source, result);
            return result.getWriter().toString();
          } catch(TransformerException ex) {
              throw new IllegalArgumentException("Could not transform "  +node + " to XML", ex);
          }
    }
    
    /**
     * Returns the array value as a List.
     * 
     * @param value Array to convert into a list.
     * 
     * @return the array value as a List.
     **/
    private Object asList(Object value) {
        Class<?> componentClass = value.getClass().getComponentType();
        if (componentClass.isPrimitive()) {
            if (componentClass.equals(int.class)) {
                return asIntList(value);
            } else if (componentClass.equals(long.class)) {
                return asLongList(value);
            } else if (componentClass.equals(boolean.class)) {
                return asBooleanList(value);
            } else if (componentClass.equals(short.class)) {
                return asShortList(value);
            } else if (componentClass.equals(byte.class)) {
                return asByteList(value);
            } else {
                return asCharList(value);
            }
        } else {
            return asList(value, componentClass);
        }
    }

    /**
     * Returns value as an Integer List.
     * 
     * @param value array to convert.
     * 
     * @return value as an Integer List.
     **/
    private List<Integer> asIntList(Object value) {
        int[] values = (int[]) value;
        List<Integer> list = new ArrayList<Integer>(values.length);
        for (int integer : values) {
            list.add(integer);
        }
        return list;
    }

    /**
     * Returns value as a Long List.
     * 
     * @param value array to convert.
     * 
     * @return value as a Long List.
     **/
    private List<Long> asLongList(Object value) {
        long[] values = (long[]) value;
        List<Long> list = new ArrayList<Long>(values.length);
        for (long longValue : values) {
            list.add(longValue);
        }
        return list;
    }

    /**
     * Returns value as a Short List.
     * 
     * @param value array to convert.
     * 
     * @return value as a Short List.
     **/
    private List<Short> asShortList(Object value) {
        short[] values = (short[]) value;
        List<Short> list = new ArrayList<Short>(values.length);
        for (short shortValue : values) {
            list.add(shortValue);
        }
        return list;
    }

    /**
     * Returns value as a Byte List.
     * 
     * @param value array to convert.
     * 
     * @return value as a Byte List.
     **/
    private List<Byte> asByteList(Object value) {
        byte[] values = (byte[]) value;
        List<Byte> list = new ArrayList<Byte>(values.length);
        for (byte byteValue : values) {
            list.add(byteValue);
        }
        return list;
    }

    /**
     * Returns value as a Character List.
     * 
     * @param value array to convert.
     * 
     * @return value as a Character List.
     **/
    private List<Character> asCharList(Object value) {
        char[] values = (char[]) value;
        List<Character> list = new ArrayList<Character>(values.length);
        for (char charValue : values) {
            list.add(charValue);
        }
        return list;
    }

    /**
     * Returns value as a Boolean List.
     * 
     * @param value array to convert.
     * 
     * @return value as a Boolean List.
     **/
    private List<Boolean> asBooleanList(Object value) {
        boolean[] values = (boolean[]) value;
        List<Boolean> list = new ArrayList<Boolean>(values.length);
        for (boolean booleanValue : values) {
            list.add(booleanValue);
        }
        return list;
    }

    /**
     * Returns value as a T List.
     * 
     * @param value array to convert.
     * 
     * @return value as a T List.
     **/
    private <T> List<T> asList(Object value, Class<T> ofClass) {
        @SuppressWarnings("unchecked")
        T[] values = (T[]) value;
        return Arrays.asList(values);
    }

}
