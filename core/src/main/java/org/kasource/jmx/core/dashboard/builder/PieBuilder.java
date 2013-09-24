package org.kasource.jmx.core.dashboard.builder;



import java.util.ArrayList;
import java.util.List;

import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.Pie;

/**
 * Builder for Pie.
 * 
 * @author rikardwi
 **/
public class PieBuilder extends AbstractWidgetBuilder {
   
    private List<AttributeValue> dataSeries = new ArrayList<AttributeValue>();
    private String title;
    
    public PieBuilder(String id) {
        super(id);
    }
    
    
    public PieBuilder addData(AttributeValue data) {
        dataSeries.add(data);
        return this;
    }
    
    public PieBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    public Pie build() {
        validateId();
        Pie pie = new Pie();
        pie.setId(getId());
        pie.setTitle(title);
        pie.setDataSeries(dataSeries);
        return pie;
    }
}
