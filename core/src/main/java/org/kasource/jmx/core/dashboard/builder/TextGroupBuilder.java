package org.kasource.jmx.core.dashboard.builder;

import java.util.ArrayList;
import java.util.List;

import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.TextGroup;

/**
 * Builder for TextGroup.
 * 
 * @author rikardwi
 **/
public class TextGroupBuilder extends AbstractWidgetBuilder {
    private List<AttributeValue> values = new ArrayList<AttributeValue>();
    private String title;
    
    public TextGroupBuilder(String id) {
        super(id);
    }
    
    public TextGroupBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    public TextGroupBuilder staticText(String value, String label) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setLabel(label);
        attributeValue.setValue(value);
        values.add(attributeValue);
        return this;
    }
    
    public TextGroupBuilder text(AttributeValue attributeValue) {
        values.add(attributeValue);
        return this;
    }
    
   
    public TextGroup build() {
       
        validateId();
        TextGroup textGroup = new TextGroup();
        textGroup.setId(getId());
        textGroup.setValue(values);
        textGroup.setTitle(title);
        return textGroup;
    }
}
