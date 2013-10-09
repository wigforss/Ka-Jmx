package org.kasource.jmx.core.dashboard.plugin;

import javax.annotation.PostConstruct;

import org.kasource.kaplugin.Plugin;
import org.springframework.beans.factory.annotation.Value;


@Plugin
public class AdditionalXmlDashboardsLoader extends AbstractXmlDahboardLoader  {

    @Value("${dashboard.location}")
    private String locations;
    
    @SuppressWarnings("unused")
    @PostConstruct
    private void initialize() {
        setLocations(locations);
    }
}
