package org.kasource.jmx.core.dashboard.builder;

import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.Gauge;

public class GaugeBuilder extends AbstractWidgetBuilder {
   
    
    private AttributeValue min;
    private AttributeValue max;
    private AttributeValue value;
    private String title;
  
    
    public GaugeBuilder(String id) {
       super(id);
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
    
    
    
    
    public Gauge build() {
        
       
        validateId();
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
        gauge.setId(getId());
        gauge.setMax(max);
        gauge.setMin(min);
        gauge.setValue(value);
        gauge.setTitle(title);
        return gauge;
    }
}
