package org.kasource.jmx.core.dashboard.builder;



import java.util.regex.Pattern;

import org.kasource.jmx.core.model.dashboard.Gauge;
import org.kasource.jmx.core.model.dashboard.Graph;
import org.kasource.jmx.core.model.dashboard.Panel;
import org.kasource.jmx.core.model.dashboard.TextGroup;

public class PanelBuilder {
    private static final Pattern ID_REG_EXP = Pattern.compile("\\s|:|@|\\$|%|&|/|\\+|,|\\(|\\)|\\{|\\}|\\[|\\]");
    private String id;
    private String title;
    private int row;
    private int column;
    private int width = 1;
    private int height = 1;
    private Gauge gauge;
    private Graph graph;
    private TextGroup textGroup;
    
    public PanelBuilder(String id, String title, int row, int column) {
        this.id = id;
        this.title = title;
        this.row = row;
        this.column = column;
        
    }
    
    /**
     * Set the width of the panel, number of baseWidth units. 
     * The baseWidth is defined by the Dashboard.
     * 
     * @param width Width is expressed in baseWidth units.
     * 
     * @return this builder to support method chaining.
     **/
    public PanelBuilder width(int width) {
        this.width = width;
        return this;
    }
    
    /**
     * Set the height of the panel, number of baseHeight units. 
     * The baseHeight is defined by the Dashboard.
     * 
     * @param height Height is expressed in baseHeight units.
     * 
     * @return this builder to support method chaining.
     **/
    public PanelBuilder height(int height) {
        this.height = height;
        return this;
    }
    
    public PanelBuilder gauge(Gauge gauge) {
        this.gauge = gauge;
        return this;
    }
    
    public PanelBuilder graph(Graph graph) {
        this.graph = graph;
        return this;
    }
    
    public PanelBuilder textGroup(TextGroup textGroup) {
        this.textGroup = textGroup;
        return this;
    }
    
    
    private void validateId(String id) {
        if(ID_REG_EXP.matcher(id).find()) {
            throw new IllegalStateException("id may not contain space, :, @, +, $, %, /, + or any brackets");
        }
    }
    
    public Panel build() {
        if(id == null || id.trim().isEmpty()) {
            throw new IllegalStateException("A non empty id must be set");
        }
        validateId(id);
        if(title == null || title.trim().isEmpty()) {
            throw new IllegalStateException("A non empty title must be set");
        }
        if(row < 1) {
            throw new IllegalStateException("Thr row number must be a posive value");
        }
        if(column < 1) {
            throw new IllegalStateException("The column number must be a posive value");
        }
        
        Panel panel = new Panel();
        panel.setId(id);
        panel.setColumn(column);
        panel.setRow(row);
        panel.setTitle(title);
        panel.setHeight(height);
        panel.setWidth(width);
        if(textGroup != null && gauge == null && graph == null) {
            panel.setTextGroup(textGroup);
        } else if(gauge != null && textGroup == null && graph == null) {
            panel.setGauge(gauge);
        } else if (graph != null && textGroup == null && gauge == null) {
            panel.setGraph(graph);
        } else {
            throw new IllegalStateException("Only one of Grah, Gauge or TextGroup may be set.");
        }
        return panel;
    }
}
