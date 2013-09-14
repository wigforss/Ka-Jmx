package org.kasource.jmx.core.dashboard.tomcat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.management.ObjectName;

import org.kasource.jmx.core.dashboard.DashboardFactory;
import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.service.JmxService;


public class TomcatDashboardFactory implements DashboardFactory {

    @Resource
    private JmxService jmxService;
    
    private String domain = "Tomcat";
    
    @Resource
    private ContextDashboardFactory contextDashboardFactory;
    
    @Resource
    private ServerDashboardFactory serverDashboardFactory;
    
    @Resource
    private ConnectorDashboardFactory connectorDashboardFactory;
    
    @SuppressWarnings("unused")
    @PostConstruct
    private void initialize() {
        resolveDomain();
        contextDashboardFactory.setDomain(domain);
        serverDashboardFactory.setDomain(domain);
        connectorDashboardFactory.setDomain(domain);
    }
    
    
    public List<Dashboard> getDashboards() {    
        List<Dashboard> dashboards = new ArrayList<Dashboard>();
        dashboards.addAll(serverDashboardFactory.getDashboards());
        dashboards.addAll(contextDashboardFactory.getDashboards());    
        dashboards.addAll(connectorDashboardFactory.getDashboards());
        return dashboards;
    }
    
    private void resolveDomain() {
        Set<ObjectName> server = jmxService.getNamesMatching("Tomcat:type=Server");
        if(server.isEmpty()) {
            server = jmxService.getNamesMatching("Catalina:type=Server");
            if(server.isEmpty()) {
                throw new IllegalStateException("Could not resolve Tomcat JMX Domain name");
            }
            domain = "Catalina";
        }
    }
    
    
   
    
    
   
   
    
    
    
    
  
   
    

    
  
    
   
   
  
   
  
    
}
