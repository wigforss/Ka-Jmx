package org.kasource.jmx.core.dashboard.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;

import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.Graph;

public class GraphBuilder {
    private static final Pattern ID_REG_EXP = Pattern.compile("\\s|:|@|\\$|%|&|/|\\+|,|\\(|\\)|\\{|\\}|\\[|\\]");
    
    private List<AttributeValue> dataSeries = new ArrayList<AttributeValue>();
    private String title;
    private String id;
    private String yAxisLabel;
    private int samples = 30;
    
    public GraphBuilder(String id) {
        this.id = id;
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
    
    
    private void validateId(String id) {
        if(ID_REG_EXP.matcher(id).find()) {
            throw new IllegalStateException("id " + id + " may not contain space, :, @, +, $, %, /, + or any brackets");
        }
    }
    
    public Graph build() {
        Graph graph = new Graph();
        if(id == null || id.trim().isEmpty()) {
            throw new IllegalStateException("A non empty id must be set");
        }
        validateId(id);
        graph.setId(id);
        graph.setSamples(samples);
        graph.setyAxisLabel(yAxisLabel);
        graph.setTitle(title);
        graph.setDataSeries(dataSeries);
        return graph;
    }
}
