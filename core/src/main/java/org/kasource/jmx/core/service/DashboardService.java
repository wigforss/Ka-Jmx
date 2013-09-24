package org.kasource.jmx.core.service;

import java.util.List;

import org.kasource.jmx.core.model.dashboard.Dashboard;

/**
 * Service for managing Dashboards.
 * 
 * @author rikardwi
 **/
public interface DashboardService {
    List<Dashboard> getDashboards();
}
