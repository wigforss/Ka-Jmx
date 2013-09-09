package org.kasource.jmx.core.dashboard.builder;

import java.util.regex.Pattern;

import org.kasource.jmx.core.model.dashboard.Attribute;
import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.Gauge;

public class GaugeBuilder {
    private static final Pattern ID_REG_EXP = Pattern.compile("\\s|:|@|\\$|%|&|/|\\+|,|\\(|\\)|\\{|\\}|\\[|\\]");
    
    private AttributeValue min;
    private AttributeValue max;
    private AttributeValue value;
    private String title;
    private String id;
    
    public GaugeBuilder(String id) {
        this.id = id;
    }
    
    public GaugeBuilder max(String maxValue) {
        max = new AttributeValue();
        max.setValue(maxValue);
        max.setSubscribe(false);
        return this;
    }
    
    public GaugeBuilder max(AttributeValue attributeValue) {
        max = attributeValue;    
        return this;
    }
    
 
    
    public GaugeBuilder min(String minValue) {
        min = new AttributeValue();
        min.setValue(minValue);
        min.setSubscribe(false);
        return this;
    }
    
    
    public GaugeBuilder min(AttributeValue attributeValue) {
        min = attributeValue;
        return this;
    }
    
    public GaugeBuilder value(String staticValue) {
        value = new AttributeValue();
        value.setValue(staticValue);
        value.setSubscribe(false);
        return this;
    }
    
    public GaugeBuilder value(AttributeValue attributeValue) {
        value = attributeValue; 
        return this;
    }
    
    
   
    
    public GaugeBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    
    private void validateId(String id) {
        if(ID_REG_EXP.matcher(id).find()) {
            throw new IllegalStateException("id may not contain space, :, @, +, $, %, /, + or any brackets");
        }
    }
    
    public Gauge build() {
        if(id == null || id.trim().isEmpty()) {
            throw new IllegalStateException("A non empty id must be set");
        }
       
        validateId(id);
        if(max == null) {
            throw new IllegalStateException("Max value must be set");
        }
        if(min == null) {
            throw new IllegalStateException("Min value must be set");
        }
        if(min == null) {
            throw new IllegalStateException("Value must be set");
        }
        Gauge gauge = new Gauge();
        gauge.setId(id);
        gauge.setMax(max);
        gauge.setMin(min);
        gauge.setValue(value);
        gauge.setTitle(title);
        return gauge;
    }
}
