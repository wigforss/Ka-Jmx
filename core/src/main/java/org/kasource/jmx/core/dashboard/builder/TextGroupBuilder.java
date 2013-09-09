package org.kasource.jmx.core.dashboard.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.kasource.jmx.core.model.dashboard.Attribute;
import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.TextGroup;

public class TextGroupBuilder {
    private static final Pattern ID_REG_EXP = Pattern.compile("\\s|:|@|\\$|%|&|/|\\+|,|\\(|\\)|\\{|\\}|\\[|\\]");

    private List<AttributeValue> values = new ArrayList<AttributeValue>();

    private String id;
    private String title;
    
    public TextGroupBuilder(String id) {
        this.id = id;
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
    
    private void validateId(String id) {
        if(ID_REG_EXP.matcher(id).find()) {
            throw new IllegalStateException("id may not contain space, :, @, +, $, %, /, + or any brackets");
        }
    }
   
    public TextGroup build() {
        if(id == null || id.trim().isEmpty()) {
            throw new IllegalStateException("A non empty id must be set");
        }
        validateId(id);
        TextGroup textGroup = new TextGroup();
        textGroup.setId(id);
        textGroup.setValue(values);
        textGroup.setTitle(title);
        return textGroup;
    }
}
