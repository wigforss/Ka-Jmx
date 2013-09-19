package org.kasource.jmx.core.dashboard.builder;

import java.util.ArrayList;
import java.util.List;

import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.Graph;

public class GraphBuilder extends AbstractWidgetBuilder {
 
    
    private List<AttributeValue> dataSeries = new ArrayList<AttributeValue>();
    private String title;
   
    private String yAxisLabel;
    private int samples = 30;
    
    public GraphBuilder(String id) {
        super(id);
    }
    
    public GraphBuilder addData( AttributeValue data ) {
        dataSeries.add(data);
        return this;
    }
    
    public GraphBuilder yAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
        return this;
    }
    
    public GraphBuilder samples(int numberOfSamples) {
        this.samples = numberOfSamples;
        return this;
    }
    
   
    
    public GraphBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    
    
    
    public Graph build() {
        Graph graph = new Graph();
        
        validateId();
        graph.setId(getId());
        graph.setSamples(samples);
        graph.setyAxisLabel(yAxisLabel);
        graph.setTitle(title);
        graph.setDataSeries(dataSeries);
        return graph;
    }
}
