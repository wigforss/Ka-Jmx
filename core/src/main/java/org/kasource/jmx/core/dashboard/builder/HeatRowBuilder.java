package org.kasource.jmx.core.dashboard.builder;

import java.util.ArrayList;
import java.util.List;

import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.HeatCell;
import org.kasource.jmx.core.model.dashboard.HeatRow;

public class HeatRowBuilder {

    private List<HeatCell> heatCells = new ArrayList<HeatCell>();
    
    public HeatRowBuilder add(AttributeValue value, String normalizationFunction) {
        HeatCell cell = new HeatCell();
        cell.setData(value);
        cell.setNormalizationFunction(normalizationFunction);
        heatCells.add(cell);
        return this;
    }
    
    public HeatRowBuilder add(AttributeValue value) {
        HeatCell cell = new HeatCell();
        cell.setData(value);
        heatCells.add(cell);
        return this;
    }
    
    
    public HeatRow build() {
        HeatRow row = new HeatRow();
        row.setHeatCell(heatCells);
        return row;
    }
}
