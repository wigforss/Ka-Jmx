package org.kasource.jmx.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;


import org.kasource.jmx.core.dashboard.AttributeValuePopulator;
import org.kasource.jmx.core.dashboard.DashboardPlugin;
import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.model.dashboard.Panel;
import org.kasource.jmx.core.model.dashboard.ValueType;
import org.kasource.jmx.core.util.JmxValueConverter;
import org.kasource.jmx.core.util.JmxValueFormatter;
import org.kasource.kaplugin.PluginManager;
import org.springframework.stereotype.Service;

/**
 * Default implementation of DashboardService.
 * 
 * @author rikardwi
 **/
@Service
public class DashboardServiceImpl implements DashboardService, AttributeValuePopulator {
   
    @Resource
    private PluginManager pluginManager;
    
    @Resource
    private JmxService jmxService;
    
    @Resource
    private JmxValueFormatter jmxValueFormatter;
    
    @Resource
    private JmxValueConverter jmxValueConverter;
    
    private List<Dashboard> dashboards;
    
    @Override
    public List<Dashboard> getDashboards() {
        if(dashboards == null) {
            dashboards = new ArrayList<Dashboard>();
            DashboardPlugin plugins = pluginManager.getExtensionPointFor(DashboardPlugin.class);
            plugins.registerDashboard(dashboards, jmxService);
            Collections.sort(dashboards);
            populateValuesDashboards(dashboards);
        }
        return dashboards;
    }

    
    private void populateValuesDashboards(List<Dashboard> dashboards) {
        for(Dashboard dashboard : dashboards) {
            populateValuesPanels(dashboard.getPanel());
        }
    }
    
    private void populateValuesPanels(List<Panel> panels) {
        for (Panel panel : panels) {
            panel.getWidget().populateValues(this);
        }
    }
    
   
    
    public void populateValue(AttributeValue value) {
       if(value != null) {
            if(value.getLabel() == null && value.getAttribute() != null) {
                value.setLabel(value.getAttribute().getAttribute());
            } else if(value.getLabel() == null){
                value.setLabel("");
            }
            Object data = value.getValue();
            if(data == null && value.getAttribute() != null) {
                Object jmxAttributeValue = jmxService.getAttributeValue(value.getAttribute().getObjectName(), value.getAttribute().getAttribute());
                data = jmxAttributeValue;
            }
            if(data != null) {
                if (ValueType.TEXT.equals(value.getType())){
                    value.setValue(jmxValueFormatter.format(data).toString());
                } else {
                    value.setValue(jmxValueConverter.convert(data));
                }
                 
            }
            
        }
        
    }
}
