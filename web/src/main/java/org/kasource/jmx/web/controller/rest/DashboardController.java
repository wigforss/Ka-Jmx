package org.kasource.jmx.web.controller.rest;

import java.util.List;

import javax.annotation.Resource;
import javax.management.MalformedObjectNameException;

import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DashboardController {
    @Resource
    private DashboardService dashboardService;
    
    @RequestMapping(value="/dashboard", method =  RequestMethod.GET)
    public @ResponseBody JsonWrapper<Dashboard> getDashboard(@RequestParam("dashboardId") String dashboardId) throws MalformedObjectNameException, NullPointerException {
     
        List<Dashboard> dashboards = dashboardService.getDashboards();
        for(Dashboard dashboard : dashboards) {
            if(dashboard.getId().equals(dashboardId)) {
               return new JsonWrapper<Dashboard>(dashboard);
            }
        }
        throw new IllegalArgumentException("No Dashboard with id: "+dashboardId+ "found!");
       
        
    }
}
