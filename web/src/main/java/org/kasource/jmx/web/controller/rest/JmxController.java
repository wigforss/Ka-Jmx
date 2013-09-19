package org.kasource.jmx.web.controller.rest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.MalformedObjectNameException;
import javax.servlet.http.HttpServletRequest;

import org.kasource.jmx.core.bean.ManagedAttribute;
import org.kasource.jmx.core.bean.ManagedBean;
import org.kasource.jmx.core.bean.ManagedOperation;
import org.kasource.jmx.core.bean.ManagedOperationParameter;
import org.kasource.jmx.core.service.JmxService;
import org.kasource.jmx.web.util.JmxValueParser;
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
    
    @Resource
    private JmxValueParser jmxValueParser;
   
    
   
    @SuppressWarnings("unused")
    @InitBinder  
    private void initBinder(WebDataBinder binder) {  
        jmxValueParser.setDataBinder(binder);
       
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
               String value = request.getParameter(param.getName());
               parameterValues.add(jmxValueParser.parse(value, param.getTargetClass()));
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
    
    
    @RequestMapping(value="/bean/attributes", method =  RequestMethod.POST)
    public @ResponseBody SaveResult saveAttributes(@RequestParam("objectName") String objectName, HttpServletRequest request) throws MalformedObjectNameException, NullPointerException {
        
        SaveResult saveResult = new SaveResult();
        ManagedBean bean = jmxService.getBeanInfo(objectName);
        Map<ManagedAttribute, Object> attributeValues = parseAttributeValues(bean, request, saveResult);
        
        if (!attributeValues.isEmpty()) {
            for(Map.Entry<ManagedAttribute, Object> attributeValue : attributeValues.entrySet()) {
                ManagedAttribute attribute = attributeValue.getKey();
                try {
                    jmxService.setAttribute(objectName, attribute.getName(), attributeValue.getValue());
                    saveResult.getSaved().add(attribute.getName());
                } catch(Exception e) {
                    saveResult.getErrors().put(attribute.getName(), "Could not save " + attributeValue.getValue() + " as "+attribute.getType()+ ": " +getRootCause(e).getMessage());
                }
            }
           
        }
       return saveResult;
       
    }
    
    private Map<ManagedAttribute, Object> parseAttributeValues(ManagedBean bean, HttpServletRequest request, SaveResult saveResult) {
        Map<ManagedAttribute, Object> attributeValues = new HashMap<ManagedAttribute, Object>();
        Set<ManagedAttribute> attributes = bean.getAttributes();
        for(ManagedAttribute attribute : attributes) {
            String value = request.getParameter(attribute.getName());
            String changed = request.getParameter("changed-"+attribute.getName());
            if(value != null && changed != null && "on".equals(changed)) {
                try {
                    attributeValues.put(attribute, jmxValueParser.parse(value, attribute.getTargetClass()));
                } catch(Exception e) {
                    saveResult.getErrors().put(attribute.getName(), "Could not parse "+ value+" as " + attribute.getName() +" of type "+attribute.getType()+ ": " +getRootCause(e).getMessage());
                }
                
            }
        }
        return attributeValues;
    }
    
    private Throwable getRootCause(Exception e) {
        Throwable root = e;
        while(root.getCause() != null) {
            root = root.getCause();
        }
        return root;
    }
    
  
}
