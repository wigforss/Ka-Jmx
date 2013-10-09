package org.kasource.jmx.core.dashboard;

import java.util.List;

import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.service.JmxService;
import org.kasource.kaplugin.ExtensionPoint;

/**
 * Interface that enables plugins to add their own Dashboards,
 * 
 * @author rikardwi
 **/
@ExtensionPoint
public interface DashboardPlugin {
    
    /**
     * Allows implementor classes to add their Dashboards to the dashboards list.
     * 
     * @param dashboards Dashboard list to add Dashboards to.
     * @param jmxService The JMX Service which can be used to inspect JMX beans and attribute values.
     **/
    void registerDashboard(List<Dashboard> dashboards, JmxService jmxService);
}
