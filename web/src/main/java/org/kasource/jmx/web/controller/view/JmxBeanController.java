package org.kasource.jmx.web.controller.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.MalformedObjectNameException;
import javax.servlet.http.HttpServletRequest;

import org.kasource.jmx.core.bean.ManagedAttribute;
import org.kasource.jmx.core.bean.ManagedBean;
import org.kasource.jmx.core.service.DashboardService;
import org.kasource.jmx.core.service.JmxService;
import org.kasource.jmx.core.util.JmxValueFormatter;
import org.kasource.jmx.web.util.JmxValueParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JmxBeanController {

    private static final String VIEW = "jmx_console";
    
    @Resource
    private JmxService jmxService;
    
    @Resource
    private DashboardService dashboardService;
    
    @Resource
    private JmxValueParser jmxValueParser;
    
    @Resource
    private JmxValueFormatter jmxValueFormatter;
   
    
    @SuppressWarnings("unused")
    @InitBinder  
    private void initBinder(WebDataBinder binder) {  
        jmxValueParser.setDataBinder(binder);
        
    }  
 
   
    
    @RequestMapping(value="/bean", method =  RequestMethod.POST)
    public ModelAndView refreshBean(@RequestParam("objectName") String objectName) throws MalformedObjectNameException, NullPointerException {
        ModelAndView mav = new ModelAndView(VIEW);
        mav.addObject("dashboards", dashboardService.getDashboards());
        mav.addObject("tree", jmxService.getJmxTree());
        mav.addObject("mBean", jmxService.getBeanInfo(objectName));
        mav.addObject("attributeValues", formatAttributeValues(jmxService.getAttributeValues(objectName)));
        return mav;
        
    }
    
    @RequestMapping(value="/bean/attributes", method =  RequestMethod.POST)
    public ModelAndView saveAttributes(@RequestParam("objectName") String objectName, HttpServletRequest request) throws MalformedObjectNameException, NullPointerException {
        ModelAndView mav = new ModelAndView(VIEW);
        objectName = removeDuplicates(objectName);
        ManagedBean bean = jmxService.getBeanInfo(objectName);
        Map<String, Object> attributeValues = parseAttributeValues(bean, request);
        
        if (!attributeValues.isEmpty()) {
            jmxService.setAttributes(objectName, attributeValues);
        }
        mav.addObject("dashboards", dashboardService.getDashboards());
        mav.addObject("tree", jmxService.getJmxTree());
        mav.addObject("mBean", bean);
        mav.addObject("attributeValues", formatAttributeValues(jmxService.getAttributeValues(objectName)));
        return mav;
        // Tomcat:j2eeType=Servlet,name=view,WebModule=//localhost/ka-jmx,J2EEApplication=none,J2EEServer=none
        // Tomcat:j2eeType=Servlet,name=view,WebModule=//localhost/ka-jmx,J2EEApplication=none,J2EEServer=none,Tomcat:j2eeType=Servlet,name=view,WebModule=//localhost/ka-jmx,J2EEApplication=none,J2EEServer=none
    }
    
    private Map<String, Object> formatAttributeValues(Map<String, Object> values) {
        for(String key : values.keySet()) {
            Object unformattedValue = values.get(key);
            values.put(key, jmxValueFormatter.format(unformattedValue));
        }
        return values;
    }
    
    private String removeDuplicates(String objectName) {
        int firstColonIndex = objectName.indexOf(":");
        if(!(objectName.lastIndexOf(":") == firstColonIndex)) {
            String domain = objectName.substring(0, firstColonIndex + 1);
            int domainIndex = objectName.lastIndexOf(domain);
            while(domainIndex > 0) {
                objectName = objectName.substring(0, domainIndex);
                domainIndex = objectName.lastIndexOf(domain);
            }
            if(objectName.endsWith(",")) {
                objectName = objectName.substring(0, objectName.length() - 1);
            }
        }
        return objectName;
    }
    
    
    private Map<String, Object> parseAttributeValues(ManagedBean bean, HttpServletRequest request) {
        Map<String, Object> attributeValues = new HashMap<String, Object>();
        Set<ManagedAttribute> attributes = bean.getAttributes();
        for(ManagedAttribute attribute : attributes) {
            String value = request.getParameter(attribute.getName());
            if(value != null) {
                attributeValues.put(attribute.getName(), jmxValueParser.parse(value, attribute.getTargetClass()));
            }
        }
        return attributeValues;
    }
}
