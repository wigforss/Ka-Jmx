package org.kasource.jmx.core.dashboard.builder;

import java.util.ArrayList;
import java.util.List;



import org.kasource.jmx.core.model.dashboard.ColorSchemaType;
import org.kasource.jmx.core.model.dashboard.HeatMap;
import org.kasource.jmx.core.model.dashboard.HeatRow;
import org.kasource.jmx.core.model.dashboard.LayoutType;

public class HeatMapBuilder extends AbstractWidgetBuilder {
    
    private List<HeatRow> heatRows = new ArrayList<HeatRow>();
    private boolean showLegend = true;
    private LayoutType legendLayout = LayoutType.VERTICAL;
    private ColorSchemaType colorSchema = ColorSchemaType.HEAT_MAP;
    
    public HeatMapBuilder(String id) {
        super(id);
    }

    
    public HeatMapBuilder addRow(HeatRow row) {
        heatRows.add(row);
        return this;
    }
    
    public HeatMapBuilder showLegend(boolean showLegend) {
        this.showLegend = showLegend;
        return this;
    }
    
    public HeatMapBuilder legendLayout(LayoutType legendLayout) {
        this.legendLayout = legendLayout;
        return this;
    }
    
    public HeatMapBuilder colorSchema(ColorSchemaType colorSchema) {
        this.colorSchema = colorSchema;
        return this;
    }
    
    public HeatMap build() {
        validateId();
        HeatMap heatMap = new HeatMap();
        heatMap.setId(getId());
        heatMap.setHeatRow(heatRows);
        heatMap.setShowLegend(showLegend);
        heatMap.setLegendLayout(legendLayout);
        heatMap.setColorSchema(colorSchema);
        return heatMap;
    }
}
