package org.kasource.jmx.core.dashboard;

/**
 * A Dashboard widget.
 * 
 * @author rikardwi
 **/
public interface DashboardWidget {
     String getId();
    
     String getType();
     
     void populateValues(AttributeValuePopulator populator);
}
