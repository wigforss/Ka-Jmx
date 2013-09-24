package org.kasource.jmx.core.util;

/**
 * Formats a value to a presentation friendly representation.
 * 
 * The JmxValueParser should be able to parse what's formatted by the implementation class. 
 *  
 * @author rikardwi
 **/
public interface JmxValueFormatter {
    
    /**
     * Returns a formatted representation for the value.
     * 
     * @param value Value to format.
     * 
     * @return a formatted representation for the value.
     **/
    Object format(Object value);
}
