package org.kasource.jmx.core.dashboard.tomcat;

import static org.kasource.jmx.core.dashboard.JavaScriptFunction.MILLISECONDS_TO_MINUTES;
import static org.kasource.jmx.core.dashboard.JavaScriptFunction.MILLISECONDS_TO_SECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.ObjectName;


import org.kasource.jmx.core.dashboard.builder.AttributeBuilder;
import org.kasource.jmx.core.dashboard.builder.DashboardBuilder;
import org.kasource.jmx.core.dashboard.builder.GaugeBuilder;
import org.kasource.jmx.core.dashboard.builder.GraphBuilder;
import org.kasource.jmx.core.dashboard.builder.PanelBuilder;
import org.kasource.jmx.core.dashboard.builder.PieBuilder;
import org.kasource.jmx.core.dashboard.builder.TextGroupBuilder;
import org.kasource.jmx.core.dashboard.builder.TrafficLightBuilder;
import org.kasource.jmx.core.model.dashboard.AttributeType;
import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.model.dashboard.Gauge;
import org.kasource.jmx.core.model.dashboard.Graph;
import org.kasource.jmx.core.model.dashboard.TextGroup;
import org.kasource.jmx.core.model.dashboard.TrafficLight;
import org.kasource.jmx.core.service.JmxService;
import org.springframework.stereotype.Component;

@Component
public class ContextDashboardFactory  {

  
    @Resource
    private JmxService jmxService;
    
    private String domain = "Tomcat";
    
    
    
  
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
        if(contextName == null || contextName.trim().isEmpty()){
            contextName = "/";
        }
        DashboardBuilder dashboardBuilder = new DashboardBuilder("context-" + contextName.replace('/', '-'),  contextName, 120, 120);
      
    
            
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
        ObjectName host = jmxService.getNamesMatching(domain+":type=Host,*").iterator().next();
        TextGroup filters = getFilters(contextName, host.getKeyProperty("host"));
        if(filters != null) {
            dashboardBuilder.add(new PanelBuilder("filter-panel-"+contextName.replace('/', '-'), "Filters", 3, 7).width(2).height(3).textGroup(filters).build());
        }
        
        
        
        int row = 3;
        String[] servletObjectNames =  (String[]) jmxService.getAttributeValue(context.getCanonicalName(), "servlets");
        
        if(servletObjectNames.length > 1) {
        
            PieBuilder requestPieChartBuilder = new PieBuilder("request-pie-"+contextName.replace('/', '-')).title("# Requests"); 
            PieBuilder processingPieChartBuilder = new PieBuilder("process-pie-"+contextName.replace('/', '-')).title("Processing Time"); 
            PieBuilder errorPieChartBuilder = new PieBuilder("error-pie-"+contextName.replace('/', '-')).title("# Errors"); 
            for(String servletObjectName : servletObjectNames) {
                try {
                    String servletName = ObjectName.getInstance(servletObjectName).getKeyProperty("name");
                    requestPieChartBuilder.addData(new AttributeBuilder().attribute(servletObjectName, "requestCount").label(servletName).build());
                    errorPieChartBuilder.addData(new AttributeBuilder().attribute(servletObjectName, "errorCount").label(servletName).build());
                    processingPieChartBuilder.addData(new AttributeBuilder().attribute(servletObjectName, "processingTime").label(servletName).build());
                }  catch (Exception e) {}
            }
            TrafficLight state = getStateTrafficLight(contextName);
            int column = 1;
            if(state != null) {
                dashboardBuilder.add(new PanelBuilder("state-panel-"+contextName.replace('/', '-'), "Context State", row, column).width(1).height(2).trafficLight(state).build());
                column++;
            }
            dashboardBuilder.add(new PanelBuilder("request-pie-panel"+contextName.replace('/', '-'),"Number of Requests", row, column).width(2).height(2).pie(requestPieChartBuilder.build()).build());
            dashboardBuilder.add(new PanelBuilder("process-pie-panel"+contextName.replace('/', '-'),"Processing Time (ms)", row, column + 2).width(2).height(2).pie(processingPieChartBuilder.build()).build());
            dashboardBuilder.add(new PanelBuilder("error-pie-panel"+contextName.replace('/', '-'),"Number of Errors", row, column + 4).width(2).height(2).pie(errorPieChartBuilder.build()).build());
        }
        row += 2;
        
       for(String servletObjectName : servletObjectNames) {
            addServletPanel(dashboardBuilder, contextName, servletObjectName, row);
            row += 2;
       }
        
        return dashboardBuilder.build();
    }
    
    private TrafficLight getStateTrafficLight(String contextName) {
       
        Set<ObjectName> contextValves = jmxService.getNamesMatching(domain + ":name=StandardContextValve,context="+contextName+",*");
        if(!contextValves.isEmpty()) {
            ObjectName contextValve = contextValves.iterator().next();
            return new TrafficLightBuilder("state-traffic-light-"+contextName.replace('/', '-')).attributeType(AttributeType.TEXT).title("State")
                    .green(new AttributeBuilder().value("STARTED").build())
                    .yellow(new AttributeBuilder().value("NEW, INITIALIZING, INITIALIZED, STARTING_PREP, STARTING, MUST_STOP, STOPPING_PREP").build())     
                    .red(new AttributeBuilder().build())
                    .value(new AttributeBuilder().attribute(contextValve.getCanonicalName(), "stateName").build())
                    .build();
        }
        return null;
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
        Graph processingGraph = new GraphBuilder("processingGraph-"+idSuffix).yAxisLabel("Time (ms)").decimals(0)
                                                   .addData(new AttributeBuilder().attribute(name, "minTime").label("Minimum").build())
                                                   .addData(new AttributeBuilder().attribute(name, "maxTime").label("Maximum").build())
                                                   .build();
        dashboardBuilder.add(new PanelBuilder("processingGraphPanel-"+idSuffix,"Servlet " + servletName + " processing Time", row, 3).width(4).height(2).graph(processingGraph).build());
        
    }

    
    private TextGroup getFilters(String contextName, String host){
      
        Set<ObjectName> filters = jmxService.getNamesMatching(domain+":j2eeType=Filter,WebModule=//"+host+contextName+",*");
        if(!filters.isEmpty()) {
            TextGroupBuilder filterInfo = new TextGroupBuilder("filterText-"+contextName.replace('/', '-'));
            for(ObjectName filter : filters) {
             
                filterInfo.text(new AttributeBuilder().attribute(filter.getCanonicalName(), "filterName").label("Name:").subscribe(false).build())
                .text(new AttributeBuilder().attribute(filter.getCanonicalName(), "filterClass").label("Class:").subscribe(false).build())
                .text(new AttributeBuilder().attribute(filter.getCanonicalName(), "filterInitParameterMap").label("Params:").subscribe(false).build());
            }
            return filterInfo.build();
        }
        return null;
    }
    
    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }


  
    
}
