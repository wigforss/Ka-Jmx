package org.kasource.jmx.core.dashboard;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.service.JmxService;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

@Component("additionalDashboardFactory")
public class AdditionalDashboardFactory implements DashboardFactory {

    @Resource
    private JmxService jmxService;
    
    @Value("${additional.dashboard.factory}")
    private String dashboardClass;
    
    @Override
    public List<Dashboard> getDashboards() {
        try {
            return loadDashboards();
        } catch(Exception e) {
            throw new IllegalStateException("Could not load dashboards from " + dashboardClass, e);
        }
    }
    
    private List<Dashboard> loadDashboards() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<Dashboard> dashboards = new ArrayList<Dashboard>();
        if(dashboardClass != null && !dashboardClass.trim().isEmpty()) {
            Class<?> clazz = Class.forName(dashboardClass);
            if(DashboardFactory.class.isAssignableFrom(clazz)) {
                DashboardFactory factory = (DashboardFactory) clazz.newInstance();
                factory.setJmxService(jmxService);
                dashboards.addAll(factory.getDashboards());
            }
        }
        return dashboards;
    }

    @Override
    public void setJmxService(JmxService jmxService) {    
    }

}
