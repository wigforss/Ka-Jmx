package org.kasource.jmx.core.dao;

import java.util.List;

import org.kasource.jmx.core.model.dashboard.Dashboard;

/**
 * DAO for Dashboards.
 * 
 * @author rikardwi
 **/
public interface DashboardDao {

     List<Dashboard> getDashboards();
}
