package org.kasource.jmx.core.dashboard.builder;



import org.kasource.jmx.core.model.dashboard.Gauge;
import org.kasource.jmx.core.model.dashboard.Graph;
import org.kasource.jmx.core.model.dashboard.Panel;
import org.kasource.jmx.core.model.dashboard.Pie;
import org.kasource.jmx.core.model.dashboard.TextGroup;

/**
 * Builder for Panel.
 * 
 * @author rikardwi
 **/
public class PanelBuilder extends AbstractWidgetBuilder {
  
   
    private String title;
    private int row;
    private int column;
    private int width = 1;
    private int height = 1;
    private Gauge gauge;
    private Graph graph;
    private TextGroup textGroup;
    private Pie pie;
    
    public PanelBuilder(String id, String title, int row, int column) {
        super(id);
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
    
    public PanelBuilder pie(Pie pieChart) {
        this.pie = pieChart;
        return this;
    }
    
    
    
    
    public Panel build() {
        
        validateId();
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
        panel.setId(getId());
        panel.setColumn(column);
        panel.setRow(row);
        panel.setTitle(title);
        panel.setHeight(height);
        panel.setWidth(width);
        panel.setTextGroup(textGroup);
        panel.setGauge(gauge);
        panel.setGraph(graph);
        panel.setPie(pie);
        return panel;
    }
}
