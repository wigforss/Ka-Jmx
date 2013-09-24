package org.kasource.jmx.core.dashboard;

import java.util.List;

import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.service.JmxService;

/**
 * Created Dashboards.
 * 
 * @author rikardwi
 **/
public interface DashboardFactory {
     List<Dashboard> getDashboards();
     
     void setJmxService(JmxService jmxService);
}
