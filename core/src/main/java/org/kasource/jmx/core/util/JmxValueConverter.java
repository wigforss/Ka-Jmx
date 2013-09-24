package org.kasource.jmx.core.util;

/**
 * Converts an Object to a representation more suitable for JSON conversion.
 * 
 * @author rikardwi
 **/
public interface JmxValueConverter {
   
    /**
     * Returns the value or the converted value if a conversion method is implemented.
     *  
     * @param value Value to convert.
     * 
     * @return the value or a converted value.
     **/
    Object convert(Object value);
}
