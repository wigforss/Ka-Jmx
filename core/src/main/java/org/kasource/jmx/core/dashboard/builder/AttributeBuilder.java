package org.kasource.jmx.core.dashboard.builder;


import org.kasource.jmx.core.model.dashboard.Attribute;
import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.ValueType;

public class AttributeBuilder {

   
    private Attribute attribute;
    private String label; 
    private String value;
    private boolean subscribe = true;
    private ValueType type = ValueType.TEXT;
    private String jsFunction;
    
    public AttributeBuilder() {
      
    }
    
    public AttributeBuilder attribute(String objectName, String attributeName) {
        Attribute attribute = new Attribute();
        attribute.setObjectName(objectName);
        attribute.setAttribute(attributeName);
        return this;
    }
    
    public AttributeBuilder value(String value) {
        this.value = value;
        return this;
    }
    
    public AttributeBuilder label(String label) {
        this.label = label;
        return this;
    }
    
    public AttributeBuilder subscribe(boolean subscribeToChanges) {
        this.subscribe = subscribeToChanges;
        return this;
    }
    
    public AttributeBuilder type(ValueType ofType) {
        this.type = ofType;
        return this;
    }
    
    public AttributeBuilder jsFunction(String javaScriptValueTranformFunction) {
        this.jsFunction = javaScriptValueTranformFunction;
        return this;
    }
    
    public AttributeValue build() {
        AttributeValue attributeValue = new AttributeValue();
        
        attributeValue.setAttribute(attribute);
        attributeValue.setLabel(label);
        attributeValue.setValue(value);
        attributeValue.setSubscribe(subscribe);
        attributeValue.setType(type);
        attributeValue.setJsFunction(jsFunction);
        return attributeValue;
    }
    
}
