package org.kasource.jmx.web.controller.rest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.kasource.jmx.core.bean.ManagedBean;
import org.kasource.jmx.core.bean.ManagedOperation;
import org.kasource.jmx.core.bean.ManagedOperationParameter;
import org.kasource.jmx.core.service.JmxService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JmxController {
    
    @Resource
    private JmxService jmxService;
    
   
   
    private WebDataBinder dataBinder;
   
    @SuppressWarnings("unused")
    @InitBinder  
    private void initBinder(WebDataBinder binder) {  
       
        this.dataBinder = binder;
    }  
    
    @RequestMapping(value="/bean/operation/invoke", method = RequestMethod.POST)
    public @ResponseBody Object invokeOperation(@RequestParam String objectName, @RequestParam String operationName, HttpServletRequest request) {
        ManagedBean bean = jmxService.getBeanInfo(objectName);
        List<Object> parameterValues = new ArrayList<Object>();
        List<ManagedOperationParameter> parameters = null;
        for(ManagedOperation operation : bean.getOperations()) {
            if(operation.getName().equals(operationName)) {
                parameters = operation.getSignature();
            }
        }
        if(parameters != null) {
            
            for(ManagedOperationParameter param : parameters) {
               Object value = request.getParameter(param.getName());
               parameterValues.add(dataBinder.convertIfNecessary(value, param.getTargetClass()));
            }
        }
        try {
            Object returnValue = jmxService.invokeOperation(objectName, operationName, parameterValues.toArray());
            if(returnValue == null) {
                return "null";
            }
            return returnValue.toString();
        } catch(Exception e) {
            Throwable rootException = getRootCause(e);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream errorPrinter = new PrintStream(bos);
            rootException.printStackTrace(errorPrinter);
            return bos.toString();
          
           
        }
        
    }
    
    private Throwable getRootCause(Exception e) {
        Throwable root = e;
        while(root.getCause() != null) {
            root = root.getCause();
        }
        return root;
    }
    
    /*
    @RequestMapping(value="/{domain}/{name}/attribute/info/{attributeName}", method =  RequestMethod.GET)
    public @ResponseBody MBeanAttributeInfo getMBeanAttributeInfo(@PathVariable String domain, 
                                                    @PathVariable String name, 
                                                    @PathVariable String attributeName) {
        return jmxService.getAttributeInfo(domain, name, attributeName);
    }
    
    @RequestMapping(value="/{domain}/{name}/attribute/value/{attributeName}", method =  RequestMethod.GET)
    public @ResponseBody Object getMBeanAttributeValue(@PathVariable String domain, 
                                                    @PathVariable String name, 
                                                    @PathVariable String attributeName) {
        return jmxService.getAttributeValue(domain, name, attributeName);
    }
    
    
    @RequestMapping(value="/{domain}/{name}/attribute/value/{attributeName}", method =  RequestMethod.PUT)
    public void setMBeanAttributeValue(@PathVariable String domain, 
                                                    @PathVariable String name, 
                                                    @PathVariable String attributeName,
                                                    @RequestParam("value") String value) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
        MBeanAttributeInfo attributeInfo = jmxService.getAttributeInfo(domain, name, attributeName);
        Object attributeValue = objectMapper.readValue(value, Class.forName(attributeInfo.getType()));
        jmxService.setAttributeValue(domain, name, attributeName, attributeValue);
    }
    
    
    
    
    
    
    @RequestMapping(value="/{domain}/{name}/operation/info/{operationName}", method =  RequestMethod.GET)
    public @ResponseBody MBeanOperationInfo getMBeanOperationInfo(@PathVariable String domain, 
                                                    @PathVariable String name, 
                                                    @PathVariable String operationName) {
        return jmxService.getOperationInfo(domain, name, operationName);
    }
    
    
    @RequestMapping(value="/{domain}/{name}/operation/invoke/{operationName}", method =  RequestMethod.POST)
    public @ResponseBody Object invokeOperation(@PathVariable String domain, 
                                                    @PathVariable String name, 
                                                    @PathVariable String operationName,
                                                    HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
        MBeanOperationInfo operation = jmxService.getOperationInfo(domain, name, operationName);
        List<Object> params = new ArrayList<Object>();
        MBeanParameterInfo[] parameterInfos = operation.getSignature();
        for(int i = 0; i < parameterInfos.length; ++i) {
            MBeanParameterInfo paremeterInfo = parameterInfos[i];
            String parameterValue = request.getParameter("p"+i);
            params.add(objectMapper.readValue(parameterValue, Class.forName(paremeterInfo.getType())));
        }  
        return jmxService.invokeOperation(domain, name, operationName, params.toArray());
    }
    
    @RequestMapping(value="/{domain}/{name}/notification/info/{notificationName}", method =  RequestMethod.GET)
    public @ResponseBody MBeanNotificationInfo getMBeanNotificationInfo(@PathVariable String domain, 
                                                    @PathVariable String name, 
                                                    @PathVariable String notificationName) {
        return jmxService.getNotificationInfo(domain, name, notificationName);
    }
    
    @RequestMapping(value="/{domain}/{name}/values", method =  RequestMethod.GET)
    public @ResponseBody Map<String, Object> getMBeanAttributeValues(@PathVariable String domain, 
                                                    @PathVariable String name) {
        return jmxService.getAttributeValues(domain, name);
    }
    
    @RequestMapping(value="/{domain}/{name}/values", method =  RequestMethod.PUT)
    public void setAttributeValues(@PathVariable String domain, 
                                                  @PathVariable String name,
                                                  HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
        MBeanInfo beanInfo = jmxService.getBeanInfo(domain, name);
        MBeanAttributeInfo[] attributes = beanInfo.getAttributes();
        
        Map<String, Object> attributeValues = new HashMap<String, Object>();
        
        for(MBeanAttributeInfo attribute : attributes) {
            String attributeValue = request.getParameter(attribute.getName());
            if(attributeValue != null) {
                attributeValues.put(attribute.getName(), objectMapper.readValue(attributeValue, Class.forName(attribute.getType())));
            }
            
        }
        jmxService.setAttributes(domain, name, attributeValues);
    }
    
    @RequestMapping(value="/{domain}/{name}/info", method =  RequestMethod.GET)
    public @ResponseBody MBeanInfo getObjectInfo(@PathVariable String domain, 
                                   @PathVariable String name) {
        return jmxService.getBeanInfo(domain, name);
    }
    
    
    @RequestMapping(value="/bean/operation/invoke", method = RequestMethod.PUT)
    public @ResponseBody Object invokeOperation(@RequestParam String objectName, @RequestParam String operationName, HttpServletRequest request) {
        ManagedBean bean = jmxService.getBeanInfo(objectName);
        List<Object> parameterValues = new ArrayList<Object>();
        List<ManagedOperationParameter> parameters = null;
        for(ManagedOperation operation : bean.getOperations()) {
            if(operation.getName().equals(operationName)) {
                parameters = operation.getSignature();
            }
        }
        if(parameters != null) {
            
            for(ManagedOperationParameter param : parameters) {
               
            }
        }
        return jmxService.invokeOperation(objectName, operationName, params);
    }
    
    
    
    
    @RequestMapping(value="/tree", method =  RequestMethod.GET)
    public @ResponseBody JmxTree getObjectInfo() {
        return jmxService.getJmxTree();
    }
    */
}
