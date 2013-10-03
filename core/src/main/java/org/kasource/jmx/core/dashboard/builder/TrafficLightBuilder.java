package org.kasource.jmx.core.dashboard.builder;

import org.kasource.jmx.core.model.dashboard.AttributeType;
import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.TrafficLight;
import org.kasource.jmx.core.model.dashboard.TrafficLightState;

public class TrafficLightBuilder extends AbstractWidgetBuilder {

    private AttributeValue red;
    private AttributeValue yellow;
    private AttributeValue green;
    private AttributeValue value;
    private String suffix;
    private String prefix;
    private String title;
    private TrafficLightState state;
    private boolean ascending = true;
    private AttributeType attributeType = AttributeType.NUMERIC;
    
    public TrafficLightBuilder(String id) {
        super(id);
    }
    
    public TrafficLightBuilder red(AttributeValue redValue) {
        this.red = redValue;
        return this;
    }
    
    public TrafficLightBuilder yellow(AttributeValue yellowValue) {
        this.yellow = yellowValue;
        return this;
    }
    
    public TrafficLightBuilder green(AttributeValue greenValue) {
        this.green = greenValue;
        return this;
    }
    
    public TrafficLightBuilder value(AttributeValue currentValue) {
        this.value = currentValue;
        return this;
    }
    
    public TrafficLightBuilder  suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }
    
    public TrafficLightBuilder  prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }
    
    public TrafficLightBuilder  title(String title){
        this.title = title;
        return this;
    }
    
    public TrafficLightBuilder  state(TrafficLightState state) {
        this.state = state;
        return this;
    }
    
    public TrafficLightBuilder  ascending(boolean ascending) {
        this.ascending = ascending;
        return this;
    }
    
    public TrafficLightBuilder  attributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
        return this;
    }
    
    
    public TrafficLight build() {
        validateId();
        if(red == null) {
            throw new IllegalStateException("Red value must be set.");
        }
        if(yellow == null) {
            throw new IllegalStateException("Yellow value must be set.");
        }
        if(green == null) {
            throw new IllegalStateException("Green value must be set.");
        }
        if(value == null) {
            throw new IllegalStateException("Value must be set.");
        }
        TrafficLight trafficLight = new TrafficLight();
        trafficLight.setId(getId());
        trafficLight.setRed(red);
        trafficLight.setYellow(yellow);
        trafficLight.setGreen(green);
        trafficLight.setValue(value);
        trafficLight.setSuffix(suffix);
        trafficLight.setPrefix(prefix);
        trafficLight.setTitle(title);
        trafficLight.setState(state);
        trafficLight.setAscending(ascending);
        trafficLight.setAttributeType(attributeType);
        return trafficLight;
    }

}
