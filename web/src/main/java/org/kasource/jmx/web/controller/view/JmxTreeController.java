package org.kasource.jmx.web.controller.view;

import javax.annotation.Resource;
import javax.management.MalformedObjectNameException;
import javax.servlet.http.HttpServletRequest;

import org.kasource.jmx.core.service.DashboardService;
import org.kasource.jmx.core.service.JmxService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JmxTreeController {

    private static final String VIEW = "jmx_console";
    private static final String DEFAULT_BEAN = "JMImplementation:type=MBeanServerDelegate";
    
    @Resource
    private JmxService jmxService;
    
    @Resource
    private DashboardService dashboardService;
    
    
    @RequestMapping(value="/tree", method =  RequestMethod.GET)
    public ModelAndView getTree(HttpServletRequest request) throws MalformedObjectNameException, NullPointerException {
        ModelAndView mav = new ModelAndView(VIEW);
        String objectName =  DEFAULT_BEAN;
        if(request.getParameter("objectName") != null) {
            objectName = request.getParameter("objectName");
        }
        mav.addObject("dashboards", dashboardService.getDashboards());
        mav.addObject("tree", jmxService.getJmxTree());
        mav.addObject("mBean", jmxService.getBeanInfo(objectName));
        mav.addObject("attributeValues", jmxService.getAttributeValues(objectName));
        return mav;
    }
    
    
    @RequestMapping(value="/tree/refresh", method =  RequestMethod.POST)
    public ModelAndView refreshTree(HttpServletRequest request) throws MalformedObjectNameException, NullPointerException {
        jmxService.refreshTree();
        return getTree(request);
    }
    
}
