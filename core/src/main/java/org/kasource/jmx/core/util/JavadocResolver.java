package org.kasource.jmx.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

/**
 * Resolves JavaDoc URL for a class.
 * 
 * @author rikardwi
 **/
@Component
public class JavadocResolver {

    private  Map<String, String> cache = new HashMap<String, String>();
    
    @Resource (name = "javadocProperties")
    private Properties doc;
    
    /**
     * Returns a HTTP URL for a class.
     * 
     * @param className Name of a class to find URL for.
     * 
     * @return HTTP URL for a class.
     **/
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
    
    /**
     * Return JavaDoc URL for class.
     * 
     * @param className Class name to find JavaDoc for.
     * 
     * @return JavaDoc URL for class.
     **/
    private String resolveDocByClass(String className) {
        String urlBase = doc.getProperty(className);
        if(urlBase != null) {
            return urlBase + className.replace(".", "/")+".html";
        }
        return null;
    }
    
    /**
     * Return JavaDoc URL for package. Check package names recursively per dot, starting 
     * with the most specific package.
     * 
     * @param className Package Name to find JavaDoc for.
     * 
     * @return JavaDoc URL for package
     **/
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
