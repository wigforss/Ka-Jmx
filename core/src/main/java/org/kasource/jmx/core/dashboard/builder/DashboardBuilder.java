package org.kasource.jmx.core.dashboard.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.model.dashboard.Panel;

public class DashboardBuilder {
    private static final Pattern ID_REG_EXP = Pattern.compile("\\s|:|@|\\$|%|&|/|\\+|,|\\(|\\)|\\{|\\}|\\[|\\]");
    
    private String id;
    private String name;
    private int baseWidth;
    private int baseHeight;
    private List<Panel> panels = new ArrayList<Panel>();
    
    
    public DashboardBuilder(String id,String name, int baseWidth, int baseHeight) {
        this.id = id;
        this.name = name;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
    }
    
    public DashboardBuilder add(Panel panel) {
        panels.add(panel);
        return this;
    }
    
    private void validateId(String id) {
        if(ID_REG_EXP.matcher(id).find()) {
            throw new IllegalStateException("id: " + id + " may not contain space, :, @, +, $, %, /, + or any brackets");
        }
    }
    
    public Dashboard build() {
        if(id == null || id.trim().isEmpty()) {
            throw new IllegalStateException("A non empty id must be set");
        }
        validateId(id);
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("A non empty name must be set");
        }
        if(baseWidth < 1) {
            throw new IllegalStateException("Base Width (in pixels) must be a posive value");
        }
        if(baseHeight < 1) {
            throw new IllegalStateException("Base Height (in pixels) must be a posive value");
        }
        
        Dashboard dashboard = new Dashboard();
        dashboard.setId(id);
        dashboard.setName(name);
        dashboard.setBaseWidth(baseWidth);
        dashboard.setBaseHeight(baseHeight);
        dashboard.setPanel(panels);
        return dashboard;
    }
}
