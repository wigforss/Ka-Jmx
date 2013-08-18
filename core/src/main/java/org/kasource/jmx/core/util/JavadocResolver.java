package org.kasource.jmx.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class JavadocResolver {

    private  Map<String, String> cache = new HashMap<String, String>();
    
    @Resource (name = "javadocProperties")
    private Properties doc;
    
    public String getDocUrl(String className) {
        
        if(!cache.containsKey(className)) {
            String url = resolveDocByClass(className);
            if(url == null) {
                url = resolveDocByPackage(className);
            }
            cache.put(className, url);
        }
        return cache.get(className);
        
        
       
    }
    
    private String resolveDocByClass(String className) {
        String urlBase = doc.getProperty(className);
        if(urlBase != null) {
            return urlBase + className.replace(".", "/")+".html";
        }
        return null;
    }
    
    private String resolveDocByPackage(String className) {
        int index = className.lastIndexOf(".");
        String packageName = className;
        while(index > 0) {
            packageName = packageName.substring(0, index);
            String urlBase = doc.getProperty(packageName);
            if(urlBase != null) {
                return urlBase + className.replace(".", "/")+".html";
            }
            index = packageName.lastIndexOf(".");
        }
        return null;
    }
}
