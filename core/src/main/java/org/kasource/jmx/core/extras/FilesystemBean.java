package org.kasource.jmx.core.extras;

import java.io.File;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * Managed Bean for exposing file system space.
 * 
 * @author rikardwi
 **/
@Component
@ManagedResource(objectName="KaJMX:name=File System,type=Working Directory", description="Disk space of the working directory volume")
public class FilesystemBean {

    private static final File WORKING_DIR = new File(System.getProperty("user.dir"));
    private static final double MB = 1000.0 * 1000.0;
    
    /**
     * Returns Returns the number of unallocated MB.
     * 
     * @return Returns the number of unallocated MB
     **/
    @ManagedAttribute(description="Returns the number of unallocated MB")
    public double getFreeSpace() {
      
        return Math.round(100.0 * (WORKING_DIR.getFreeSpace() / MB)) / 100.0;
    }
    
    /**
     * Returns Returns the total number of MB
     * 
     * @return Returns the total number of MB
     **/
    @ManagedAttribute(description="Returns the total number of MB")
    public double getTotalSpace() {
        return Math.round(100.0 * (WORKING_DIR.getTotalSpace() / MB)) / 100.0;
        
    }
    
    /**
     * Returns Returns the percentage of used disk space
     * 
     * @return Returns the percentage of used disk space
     **/
    @ManagedAttribute(description = "Returns the percentage of used disk space")
    public double getDiskUsagePercent() {
        return Math.round(100.0 * 100.0 *(WORKING_DIR.getUsableSpace() / (double) WORKING_DIR.getTotalSpace())) / 100.0;
    }
    
    @ManagedAttribute(description = "Returns the MB of used disk space")
    public double getDiskUsage() {
        return Math.round(100.0 * ((WORKING_DIR.getTotalSpace() - WORKING_DIR.getUsableSpace()) / MB)) / 100.0;
    }
    
    /**
     * Returns the number of MB available to this virtual machine
     * 
     * @return Returns the number of MB available to this virtual machine
     **/
    @ManagedAttribute(description="Returns the number of MB available to this virtual machine")
    public double getUsableSpace() {
        return Math.round(100.0 * (WORKING_DIR.getUsableSpace() / MB)) / 100.0;
    }
}
