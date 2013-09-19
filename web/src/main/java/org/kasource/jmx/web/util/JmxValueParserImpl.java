package org.kasource.jmx.web.util;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;

public class JmxValueParserImpl implements JmxValueParser {

    @Value("${null.placeholder}")
    private String nullStringValue;
    
    @Value("${date.pattern}")
    private String datePattern;
    
    @Value("${date.time.pattern}")
    private String dateTimePattern;
    
    private WebDataBinder dataBinder;
    
    @Override
    public Object parse(String value, Class<?> clazz) {
       
        if(clazz.isArray()) {
            return parseArray(value, clazz);
        } else {
            return parseElement(value, clazz);
        }
        
    }
    
    private Object parseArray(String value, Class<?> clazz) {
        Class<?> componentClass = clazz.getComponentType();
        if(componentClass.isPrimitive()) {
            if(componentClass.equals(int.class)) {
                return parseIntArray(value);
            } else if (componentClass.equals(long.class)) {
                return parseLongArray(value);
            } else if (componentClass.equals(boolean.class)) {
                return parseBooleanArray(value);
            } else if (componentClass.equals(short.class)) {
                return parseShortArray(value);
            } else if (componentClass.equals(byte.class)) {
                return parseByteArray(value);
            } else {
                return parseCharArray(value);
            }
        } else {
            return parseObjectArray(value, componentClass);
        }
    }
    
    private Object parseElement(String value, Class<?> clazz) {
        if(value == null) {
            return null;
        } else if(value.equals(nullStringValue)) {
            return null;
        } else if(Date.class.isAssignableFrom(clazz)) {
           return parseDate(value, clazz);
        } 
        
        return dataBinder.convertIfNecessary(value.trim(), clazz);
    }
    
    private Object parseDate(String value, Class<?> clazz) {
        Date date = null;
        String pattern = dateTimePattern;
        if(value.length() == datePattern.length()) {
            pattern = datePattern;
        } 
        try {
            date =  new SimpleDateFormat(pattern).parse(value);
        } catch(ParseException pe) {
            throw new IllegalArgumentException("Could not parse " + value + " as date with pattern " + pattern);
        }
        if(!clazz.equals(Date.class)) {
            Constructor<?> cons;
            try {
                cons = clazz.getConstructor(long.class);
                return cons.newInstance(date.getTime());
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not create instance of with long." + clazz, e);
            } 
        }
        return date;
    }
    
    private String[] asArray(String value) {
        String array = value.trim();
        if(array.startsWith("[") && array.endsWith("]")) {
            array = array.substring(1, array.length() - 1);
        }
        array = array.trim();
        return array.split(",");
    }
    
    private Object parseObjectArray(String value, Class<?> clazz) {
       String[] values = asArray(value);
       Object[] array = new Object[values.length];
       for (int i = 0; i <  values.length; ++i) {
           String arrayValue = values[i];
           array[i] = parseElement(arrayValue, clazz);
       }
       return array;
    }

    private Object parseIntArray(String value) {
        String[] values = asArray(value);
        int[] array = new int[values.length];
        for (int i = 0; i <  values.length; ++i) {
            String arrayValue = values[i];
            array[i] = Integer.valueOf(arrayValue.trim());
        }
        return array;
    }
    
    private Object parseLongArray(String value) {
        String[] values = asArray(value);
        long[] array = new long[values.length];
        for (int i = 0; i <  values.length; ++i) {
            String arrayValue = values[i];
            array[i] = Long.valueOf(arrayValue.trim());
        }
        return array;
    }
    
    private Object parseBooleanArray(String value) {
        String[] values = asArray(value);
        boolean[] array = new boolean[values.length];
        for (int i = 0; i <  values.length; ++i) {
            String arrayValue = values[i];
            array[i] = Boolean.valueOf(arrayValue.trim());
        }
        return array;
    }
    
    private Object parseShortArray(String value) {
        String[] values = asArray(value);
        short[] array = new short[values.length];
        for (int i = 0; i <  values.length; ++i) {
            String arrayValue = values[i];
            array[i] = Short.valueOf(arrayValue.trim());
        }
        return array;
    }
    
    private Object parseByteArray(String value) {
        String[] values = asArray(value);
        byte[] array = new byte[values.length];
        for (int i = 0; i <  values.length; ++i) {
            String arrayValue = values[i];
            array[i] = Byte.valueOf(arrayValue.trim());
        }
        return array;
    }
    
    private Object parseCharArray(String value) {
        String[] values = asArray(value);
        char[] array = new char[values.length];
        for (int i = 0; i <  values.length; ++i) {
            String arrayValue = values[i];
            array[i] = arrayValue.trim().charAt(0);
        }
        return array;
    }
    
    /**
     * @param dataBinder the dataBinder to set
     */
    @Override
    public void setDataBinder(WebDataBinder dataBinder) {
        this.dataBinder = dataBinder;
    }

}
