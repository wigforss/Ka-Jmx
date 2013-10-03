package org.kasource.jmx.core.dashboard.builder;

import java.util.ArrayList;
import java.util.List;




import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.LayoutType;
import org.kasource.jmx.core.model.dashboard.LedPanel;

public class LedPanelBuilder extends AbstractWidgetBuilder {

 
    private List<AttributeValue> data = new ArrayList<AttributeValue>();
    private LayoutType layout = LayoutType.HORIZONTAL;
    private boolean showLabels = true;
    private String color = "#00FF00";
    
    public LedPanelBuilder(String id) {
        super(id);
    }
    
    public LedPanelBuilder addData(AttributeValue value) {
        data.add(value);
        return this;
    }
    
    public LedPanelBuilder layout(LayoutType layout) {
        this.layout = layout;
        return this;
    }
    
    public LedPanelBuilder showLabels(boolean showLabels) {
        this.showLabels = showLabels;
        return this;
    }
    
    public LedPanelBuilder color(String color) {
        this.color = color;
        return this;
    }
    
    public LedPanel build() {
        validateId();
        if(data.isEmpty()) {
            throw new IllegalStateException("At least one data item needs to be set");
        }
        validateColor(color);
       
        LedPanel ledPanel = new LedPanel();
        ledPanel.setId(getId());
        ledPanel.setLayout(layout);
        ledPanel.setShowLabels(showLabels);
        ledPanel.setData(data);
        ledPanel.setColor(color);
        return ledPanel;
    }

}
