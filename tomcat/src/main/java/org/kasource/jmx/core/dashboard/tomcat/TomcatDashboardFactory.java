package org.kasource.jmx.core.dashboard.tomcat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.management.ObjectName;


import org.kasource.jmx.core.dashboard.DashboardPlugin;
import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.service.JmxService;
import org.kasource.kaplugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Plugin
public class TomcatDashboardFactory implements  DashboardPlugin {
    private static Logger LOG = LoggerFactory.getLogger(TomcatDashboardFactory.class);
    @Resource
    private JmxService jmxService;
    
    private String domain;
    
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
        if(domain != null) {
            dashboards.addAll(serverDashboardFactory.getDashboards());
            dashboards.addAll(contextDashboardFactory.getDashboards());    
            dashboards.addAll(connectorDashboardFactory.getDashboards());
        } else {
            LOG.warn("Could not resolve JMX Domain for Tomcat managed beans, no dashboards created for Tomcat.");
        }
        return dashboards;
    }
    
    private void resolveDomain() {
        domain = "Tomcat";
        Set<ObjectName> server = jmxService.getNamesMatching("Tomcat:type=Server");
        if(server.isEmpty()) {
            server = jmxService.getNamesMatching("Catalina:type=Server");
            if(!server.isEmpty()) {
                domain = "Catalina";
            } 
        } else {
            domain = "Tomcat";
        }
    }


    


    @Override
    public void registerDashboard(List<Dashboard> dashboards, JmxService jmxService) {
        dashboards.addAll(getDashboards());
        
    }
    
}
