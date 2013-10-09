package org.kasource.jmx.core.dashboard.plugin;

import javax.annotation.PostConstruct;

import org.kasource.kaplugin.Plugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

@Plugin
public class DefaultDashboardLoader extends AbstractXmlDahboardLoader {

    @Value("${default.dashboard.location}")
    private Resource location;
    
    @SuppressWarnings("unused")
    @PostConstruct
    private void initialize() {
        setLocation(location);
    }
}
