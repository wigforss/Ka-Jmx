package org.kasource.jmx.core.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.kasource.jmx.core.dao.DashboardDao;
import org.kasource.jmx.core.dashboard.DashboardFactory;
import org.kasource.jmx.core.model.dashboard.AttributeValue;
import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.model.dashboard.Gauge;
import org.kasource.jmx.core.model.dashboard.Graph;
import org.kasource.jmx.core.model.dashboard.Panel;
import org.kasource.jmx.core.model.dashboard.TextGroup;
import org.kasource.jmx.core.util.JmxValueFormatter;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private DashboardDao dao;
    
    @Resource(name = "vendorDashboardFactory")
    private DashboardFactory vendorDashboardFactory;
    
    @Resource(name = "additionalDashboardFactory")
    private DashboardFactory additionalDashboardFactory;
    
    @Resource
    private JmxService jmxService;
    
    @Resource
    private JmxValueFormatter jmxValueFormatter;
    
    private List<Dashboard> dashboards;
    
    @Override
    public List<Dashboard> getDashboards() {
        if(dashboards == null) {
            dashboards = dao.getDashboards();
            dashboards.addAll(vendorDashboardFactory.getDashboards());
            dashboards.addAll(additionalDashboardFactory.getDashboards());
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
            if(panel.getGraph() != null) {
                populateValuesGraph(panel.getGraph());
            } else if (panel.getGauge() != null) {
                populateValuesGauge(panel.getGauge());
            } else {
                populateValuesTextGroup(panel.getTextGroup());
            }
        }
    }
    
  
    
    private void populateValuesGraph(Graph graph) {
        for(AttributeValue data : graph.getDataSeries()) {
            populateValue(data);
        }
    }
    
    private void populateValuesGauge(Gauge gauge) {
        populateValue(gauge.getMin());
        populateValue(gauge.getMax());
        populateValue(gauge.getValue());
    }
    
    private void populateValuesTextGroup(TextGroup textGroup) {
        for (AttributeValue value : textGroup.getValue()) {
            populateValue(value);
            
        }
    }
    
    private void populateValue(AttributeValue value) {
        if(value != null) {
            if(value.getLabel() == null && value.getAttribute() != null) {
                value.setLabel(value.getAttribute().getAttribute());
            } else if(value.getLabel() == null){
                value.setLabel("");
            }
            if(value.getValue() == null && value.getAttribute() != null) {
                Object jmxAttributeValue = jmxService.getAttributeValue(value.getAttribute().getObjectName(), value.getAttribute().getAttribute());
       
                if(jmxAttributeValue != null) {
                    value.setValue(jmxValueFormatter.format(jmxAttributeValue).toString());
                }
            }
        }
    }
}
