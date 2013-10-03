package org.kasource.jmx.core.dashboard;

import org.kasource.jmx.core.model.dashboard.AttributeValue;

/**
 * Populate values on AttributeValue instances from JMX.
 * 
 * @author rikardwi
 **/
public interface AttributeValuePopulator {
    /**
     * Populates the value of the supplied attributeValue.
     * 
     * @param attributeValue to set value on.
     **/
     void populateValue(AttributeValue attributeValue);
}
