package org.kasource.jmx.core.dashboard.tomcat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.ObjectName;
import static org.kasource.jmx.core.dashboard.JavaScriptFunction.*;
import org.kasource.jmx.core.dashboard.DashboardFactory;
import org.kasource.jmx.core.dashboard.builder.AttributeBuilder;
import org.kasource.jmx.core.dashboard.builder.DashboardBuilder;
import org.kasource.jmx.core.dashboard.builder.GaugeBuilder;
import org.kasource.jmx.core.dashboard.builder.GraphBuilder;
import org.kasource.jmx.core.dashboard.builder.PanelBuilder;
import org.kasource.jmx.core.dashboard.builder.TextGroupBuilder;
import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.model.dashboard.Gauge;
import org.kasource.jmx.core.model.dashboard.Graph;
import org.kasource.jmx.core.model.dashboard.TextGroup;
import org.kasource.jmx.core.service.JmxService;
import org.springframework.stereotype.Component;

@Component
public class ContextDashboardFactory implements DashboardFactory {

  
    @Resource
    private JmxService jmxService;
    
    private String domain = "Tomcat";
    
    
    
    @Override
    public List<Dashboard> getDashboards() {
        List<Dashboard> dashboards = new ArrayList<Dashboard>();
        Set<ObjectName> contexts = jmxService.getNamesMatching(domain + ":j2eeType=WebModule,*");
        for(ObjectName context : contexts) {
            dashboards.add(getDashboard(context));
        }
        return dashboards;
    }
    
    
  
    
    
    
    private Dashboard getDashboard(ObjectName context) {
        
        String contextName = jmxService.getAttributeValue(context.getCanonicalName(), "name").toString();
        DashboardBuilder dashboardBuilder = new DashboardBuilder("context-" + contextName.replace('/', '-'), contextName, 120, 120);
      
    
            
            String name = context.getCanonicalName();
            TextGroup textGroup = new TextGroupBuilder("text-"+ contextName.replace('/', '-')).staticText(contextName, "Context:")       
            .text(new AttributeBuilder().attribute(name, "displayName").label("Display Name:").subscribe(false).build())
            .text(new AttributeBuilder().attribute(name, "configFile").label("Configuration File:").subscribe(false).build())
            .text(new AttributeBuilder().attribute(name, "docBase").label("Document Base:").subscribe(false).build())
            .text(new AttributeBuilder().attribute(name, "welcomeFiles").label("Welcome Files:").subscribe(false).build())
            .text(new AttributeBuilder().attribute(name, "workDir").label("Working Directory:").subscribe(false).build())
            .text(new AttributeBuilder().attribute(name, "processingTime").label("Processing time:").jsFunction(MILLISECONDS_TO_SECONDS.getScript()).build())
            .text(new AttributeBuilder().attribute(name, "tldScanTime").label("TLD scan time:").jsFunction(MILLISECONDS_TO_SECONDS.getScript()).build()).build();
            
        dashboardBuilder.add(new PanelBuilder("panel-"+ contextName.replace('/', '-'), "Context " + contextName, 1, 1).width(3).height(2).textGroup(textGroup).build());
        addCachePanel(dashboardBuilder, contextName);
        addSessionPanels(dashboardBuilder, contextName, 1, context);
        
        int row = 3;
        String[] servletObjectNames =  (String[]) jmxService.getAttributeValue(context.getCanonicalName(), "servlets");
       for(String servletObjectName : servletObjectNames) {
            addServletPanel(dashboardBuilder, contextName, servletObjectName, row);
            row += 2;
       }
        
        return dashboardBuilder.build();
    }
    
    private void addSessionPanels(DashboardBuilder dashboardBuilder, String contextName, int row, ObjectName context) {
        Set<ObjectName> managerBeans = jmxService.getNamesMatching(domain + ":type=Manager,context="+contextName+",*");
        if(managerBeans.iterator().hasNext()) {
            ObjectName managerBean = managerBeans.iterator().next();
            String name = managerBean.getCanonicalName();
            TextGroup sessionInfo = new TextGroupBuilder("sessionInfo-"+contextName.replace('/', '-'))
                                                          .text(new AttributeBuilder().attribute(context.getCanonicalName(), "sessionTimeout").jsFunction("function(value){return value + \" minutes\";}")
                                                                          .label("Session timeout:")
                                                                          .subscribe(false).build())
                                                          .text(new AttributeBuilder().attribute(name, "sessionCounter").label("Total # created Sessions:").build())
                                                          .text(new AttributeBuilder().attribute(name, "rejectedSessions").label("Total # rejected Sessions:").build())
                                                          .text(new AttributeBuilder().attribute(name, "sessionMaxAliveTime").jsFunction(MILLISECONDS_TO_MINUTES.getScript()).label("Longest session time:").build())
                                                          .text(new AttributeBuilder().attribute(name, "sessionAverageAliveTime").jsFunction(MILLISECONDS_TO_MINUTES.getScript()).label("Average Session Time:").build())
                                                          .build();
            dashboardBuilder.add(new PanelBuilder("sessionInfoPanel","Session Info", row, 4).width(2).textGroup(sessionInfo).build());
            int maxActiveSessions = (Integer) jmxService.getAttributeValue(name, "maxActiveSessions");
            String maxAttribute = "maxActiveSessions";
            boolean subscribeToMax = false;
            String suffix = "";
            if(maxActiveSessions < 0) {
                maxAttribute = "maxActive";
                suffix=" (No limit)";
                subscribeToMax = true;
            }    
            Gauge sessionGauge = new GaugeBuilder("sessionGauge-"+contextName.replace('/', '-')).title("Sessions" + suffix).min("0")
                                                                                             .max(new AttributeBuilder().attribute(name, maxAttribute).subscribe(subscribeToMax).build())
                                                                                            .value(new AttributeBuilder().attribute(name, "activeSessions").label("Active").build()).build();
            dashboardBuilder.add(new PanelBuilder("sessionGaugePanel","Active Sessions", row+1, 5).gauge(sessionGauge).build());
            
            
            Graph createAndExpiryRate = new GraphBuilder("createAndExpiryRate-"+contextName.replace('/', '-'))
                                                          .addData(new AttributeBuilder().attribute(name, "sessionExpireRate").label("Expired").build())
                                                          .addData(new AttributeBuilder().attribute(name, "sessionCreateRate").label("Created").build()).build();
            dashboardBuilder.add(new PanelBuilder("createAndExpiryRatePanel","Sessions per minuete", row, 4).height(2).width(4).graph(createAndExpiryRate).build());
            
            
        }
    }
    
    private void addCachePanel(DashboardBuilder dashboardBuilder, String contextName) {
        Set<ObjectName> caheBeans = jmxService.getNamesMatching(domain+":type=Cache,context="+contextName+",*");
        if(caheBeans.iterator().hasNext()) {
            ObjectName cacheBean =caheBeans.iterator().next();
            String name = cacheBean.getCanonicalName();
            Gauge cacheUseGauge = new GaugeBuilder("cacheUseGauge-resource").title("Resorce Cache")
                                                    .min("0")
                                                    .max(new AttributeBuilder().attribute(name, "accessCount").build())
                                                    .value(new AttributeBuilder().attribute(name, "hitsCount").label("Hits").build())  
                                                    .build();
            dashboardBuilder.add(new PanelBuilder("cacheUseGaugePanel","Cache", 2, 4).gauge(cacheUseGauge).build());
        }
    }
    
    
    private void addServletPanel(DashboardBuilder dashboardBuilder, String contextName, String servletObjectName, int row) {
        String name = servletObjectName;
        String servletName = null;
        try {
            servletName = ObjectName.getInstance(servletObjectName).getKeyProperty("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String idSuffix = (servletName+"-"+contextName.replace('/', '-')).replace(" ", "");
        
        TextGroup servletInfo = new TextGroupBuilder("servletInfoText-"+idSuffix)
                                                      .staticText(servletName, "Name: ")
                                                      .text(new AttributeBuilder().attribute(name, "servletClass").label("Servlet Class:").subscribe(false).build())
                                                      .text(new AttributeBuilder().attribute(name, "stateName").label("State: ").build())
                                                      .text(new AttributeBuilder().attribute(name, "loadOnStartup").label("Startup order:").build())
                                                      .text(new AttributeBuilder().attribute(name, "classLoadTime").jsFunction(MILLISECONDS_TO_SECONDS.getScript()).label("Load Time:").build())
                                                      .text(new AttributeBuilder().attribute(name, "processingTime").jsFunction(MILLISECONDS_TO_SECONDS.getScript()).label("Processing Time:").build())
                                                      .text(new AttributeBuilder().attribute(name, "errorCount").label("# Errors:").build())
                                                      .text(new AttributeBuilder().attribute(name, "requestCount").label("# Requests:").build())                                                   
                                                      .build();
        dashboardBuilder.add(new PanelBuilder("servletInfoPanel-"+idSuffix,"Servlet " + servletName, row, 1).width(2).height(2).textGroup(servletInfo).build());
        Graph processingGraph = new GraphBuilder("processingGraph-"+idSuffix).yAxisLabel("Time (ms)")
                                                   .addData(new AttributeBuilder().attribute(name, "minTime").label("Minimum").build())
                                                   .addData(new AttributeBuilder().attribute(name, "maxTime").label("Maximum").build())
                                                   .build();
        dashboardBuilder.add(new PanelBuilder("processingGraphPanel-"+idSuffix,"Servlet " + servletName + " processing Time", row, 3).width(4).height(2).graph(processingGraph).build());
        
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }


    
    
}
