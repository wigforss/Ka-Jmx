package org.kasource.jmx.core.dashboard.plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import org.kasource.jmx.core.dao.DashboardDao;
import org.kasource.jmx.core.dao.DashboardXmlDao;
import org.kasource.jmx.core.dashboard.DashboardPlugin;
import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.service.JmxService;
import org.springframework.core.io.DefaultResourceLoader;


/**
 * Loads and registers XML Dashboards from a locations or a comma 
 * separated list of locations.
 *  
 * @author rikardwi
 **/
public abstract class AbstractXmlDahboardLoader implements DashboardPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractXmlDahboardLoader.class);
    
    private DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
    
    private Set<DashboardDao> daos = new HashSet<DashboardDao>();

    
    protected void setLocation(Resource sourceXml) {
        if (sourceXml.isReadable()) {
            daos.add(new DashboardXmlDao(sourceXml));
        } else {
            LOG.warn("Could not read dashboards from " + sourceXml);
        }
    }
    
    protected void setLocations(String sourceXmlLocations) {
        String[] locations = sourceXmlLocations.split(",");
        for(String location : locations) {
            Resource sourceXml = defaultResourceLoader.getResource(location.trim());
            if (sourceXml.isReadable()) {
                daos.add(new DashboardXmlDao(sourceXml));
            } else {
                LOG.warn("Could not read dashboards from "+ location);
            }
        }
        
    }
    
  
    public void registerDashboard(List<Dashboard> dashboards, JmxService jmxService) {
        if (daos != null && !daos.isEmpty()) {
            for(DashboardDao dao : daos) {
                dashboards.addAll(dao.getDashboards());
            }
        }
        
    }
    
    
}
