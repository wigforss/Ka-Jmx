package org.kasource.jmx.core.dashboard.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.kasource.jmx.core.model.dashboard.Attribute;
import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.Graph;

public class GraphBuilder {
    private static final Pattern ID_REG_EXP = Pattern.compile("\\s|:|@|\\$|%|&|/|\\+|,|\\(|\\)|\\{|\\}|\\[|\\]");
    
    private List<AttributeValue> dataSeries = new ArrayList<AttributeValue>();
    private String title;
    private String id;
    
    
    public GraphBuilder(String id) {
        this.id = id;
    }
    
    public GraphBuilder addData( AttributeValue data ) {
        dataSeries.add(data);
        return this;
    }
    
   
    
    public GraphBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    
    private void validateId(String id) {
        if(ID_REG_EXP.matcher(id).find()) {
            throw new IllegalStateException("id may not contain space, :, @, +, $, %, /, + or any brackets");
        }
    }
    
    public Graph build() {
        Graph graph = new Graph();
        if(id == null || id.trim().isEmpty()) {
            throw new IllegalStateException("A non empty id must be set");
        }
        validateId(id);
        graph.setId(id);
        graph.setTitle(title);
        graph.setDataSeries(dataSeries);
        return graph;
    }
}
