package org.kasource.jmx.core.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.ObjectName;

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
import static java.sql.Connection.*;


public class TomcatDashboardFactory implements DashboardFactory {

    @Resource
    private JmxService jmxService;
    
    public List<Dashboard> getDashboards() {
        List<Dashboard> dashboards = new ArrayList<Dashboard>();
        DashboardBuilder dashboardBuilder = new DashboardBuilder("Tomcat", 100, 100);
     /*   addServerInfo(dashboardBuilder, 1);
        addSessionPanels(dashboardBuilder, 3);
        int nextRow = addDatasourcePanels(dashboardBuilder, 4);
        nextRow = addServletPanels(dashboardBuilder, nextRow);*/
        dashboards.add(dashboardBuilder.build());
        return dashboards;
    }
    
   private void addConnectorPanels(DashboardBuilder dashboardBuilder, int row) {
       Map<String, Set<ObjectName>> beans = new HashMap<String, Set<ObjectName>>();
       // Connector etc per port
       List<String> ports = new ArrayList<String>();
       
       
       beans.put("connectors", jmxService.getNamesMatching("Tomcat:*type=Connector*"));
       beans.put("protocolHandlers", jmxService.getNamesMatching("Tomcat:*type=ProtocolHandler*"));
       beans.put("requestProcessors", jmxService.getNamesMatching("Tomcat:*type=GlobalRequestProcessor*"));
       beans.put("threadPools", jmxService.getNamesMatching("Tomcat:*type=ThreadPool*"));
       
       for(ObjectName connectorBean : beans.get("connectors")) {
           ports.add(connectorBean.getKeyProperty("port"));
       }
       
       for(String port : ports) {
           addConnectorPanels(dashboardBuilder,  row,  port, beans);
       }
       
      
      
   }
    
   private void addConnectorPanels(DashboardBuilder dashboardBuilder, int row, String port, Map<String, Set<ObjectName>> beans) {
       ObjectName connector = getObjectByPort(beans, "connectors", port);
       ObjectName protocolHandler = getObjectByPort(beans, "protocolHandlers", port);
       ObjectName requestProcessor =  getObjectKeyMatches(beans, "requestProcessors", "name", ".*"+port+".*");
       ObjectName threadPool = getObjectKeyMatches(beans, "threadPools", "name", ".*"+port+".*");
   }
   
   
   private TextGroup getConnectorTextGroup(ObjectName connector, String port) {
       String name = connector.getCanonicalName();
      
       TextGroup connectorInfo = new TextGroupBuilder("Connector-"+port).title("Connector " + port)
                                                       .text(new AttributeBuilder().attribute(name, "localPort").label("Port:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "stateName").label("State:").build())
                                                       .text(new AttributeBuilder().attribute(name, "scheme").label("Scheme:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "protocol").label("Protocol:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "maxThreads").label("Max Number of Threads:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "threadPriority").label("Thread Priority:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "maxKeepAliveRequests").label("Max Number of Keep Alive:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "maxParameterCount").label("Max Number of Parameters:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "maxPostSize").label("Max POST size in bytes:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "address").label("Bind IP address:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "URIEncoding").label("URI Encoding:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "maxParameterCount").label("Max Number of Parameters:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "maxPostSize").label("Max POST size in bytes:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "protocolHandlerClassName").label("Protocol Handler Class:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "redirectPort").label("Redirect Port:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "sslProtocols").label("SSL Protocols:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "proxyName").label("Proxy Server:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "proxyPort").label("Proxy Port:").subscribe(false).build())
                                                       .build();
      return connectorInfo;
       
   }
   
   private TextGroup getProtocolTextGroup(ObjectName protocolHandler, String port) {
       String name = protocolHandler.getCanonicalName();
       TextGroup protocolInfo = new TextGroupBuilder("protocol-"+port).title("Protocol " + port)
                                                       .text(new AttributeBuilder().attribute(name, "name").label("Name:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "maxConnections").label("Max Number of Connections:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "connectionTimeout").label("Connection Timeout (ms):").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "keepAliveTimeout").label("Keep Alive Timeout (ms):").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "connectionUploadTimeout").label("Upload Timeout (ms):").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "disableUploadTimeout").label("Upload Timeout Disbabled:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "maxHttpHeaderSize").label("Max HTTP Header Size in bytes:").subscribe(false).build())                                             
                                                       .text(new AttributeBuilder().attribute(name, "maxHeaderCount").label("Max Number of Headers:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "maxSavePostSize").label("Max authentication POST size in bytes:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "compression").label("Compression:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "compressionMinSize").label("Compression threshold in bytes:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(name, "compressableMimeTypes").label("MIME types to compress:").subscribe(false).build())
                                                       .build();
       return protocolInfo;
   }
 

   private TextGroup getProcessingAndThreadTextGroup(ObjectName requestProcessor, ObjectName threadPool, String port) {
       String processor = requestProcessor.getCanonicalName();
       String threads = threadPool.getCanonicalName();
       TextGroup processorInfo = new TextGroupBuilder("threads-"+port).title("Processor/Threads " + port)
                                                       .text(new AttributeBuilder().attribute(processor, "processingTime").label("Processing Time (ms):").build())
                                                       .text(new AttributeBuilder().attribute(processor, "maxTime").label("Max time for a request (ms):").build())
                                                       .text(new AttributeBuilder().attribute(threads, "connectionCount").label("Connection Count:").build())
                                                       .text(new AttributeBuilder().attribute(threads, "daemon").label("Daemon:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(threads, "deferAccept").label("TCP Defer Accept:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(threads, "useComet").label("Use Comet:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(threads, "useCometTimeout").label("Use Comet Timeout:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(threads, "usePolling").label("Use Polling:").subscribe(false).build())
                                                       .text(new AttributeBuilder().attribute(threads, "useSendfile").label("Use Send File:").subscribe(false).build())
                                                      .build();
       return processorInfo;
                                                      
   }
   
  
   private Graph getProcessingGraph(ObjectName requestProcessor, String port) {
       Graph processingGraph = new GraphBuilder("processingGraph-"+port).title("Processed Bytes "+port)
                                                 .addData(new AttributeBuilder().attribute(requestProcessor.getCanonicalName(), "bytesSent").label("Sent").build())
                                                 .addData(new AttributeBuilder().attribute(requestProcessor.getCanonicalName(), "bytesReceived").label("Received").build())
                                                 .build();
       return processingGraph;
       
   }
   
   private Gauge getProcessingErrorGauge(ObjectName requestProcessor, String port) {
       Gauge errorGauge = new GaugeBuilder("errorGauge-"+port).title("Error Rate " + port)
                                            .min("0")
                                            .max(new AttributeBuilder().attribute(requestProcessor.getCanonicalName(), "requestCount").build())
                                            .value(new AttributeBuilder().attribute(requestProcessor.getCanonicalName(), "errorCount").label("Errors").build())
                                            .build();
       return errorGauge;
   }

   
   private Gauge getThreadUsageGauge(ObjectName threadPool, String port) {
       Gauge threadGauge = new GaugeBuilder("threadGauge-"+port).title("Threads " + port)
                                             .min("0")
                                             .max(new AttributeBuilder().attribute(threadPool.getCanonicalName(), "maxThreads").build())
                                             .value(new AttributeBuilder().attribute(threadPool.getCanonicalName(), "currentThreadsBusy").label("Busy Threads").build())
                                             .build();
       return threadGauge;
   }

   private Graph getThreadGraph(ObjectName threadPool, String port) {
       Graph threadPoolGraph = new GraphBuilder("threadPoolGraph-"+port).title("Thread pool " + port)
       .addData(new AttributeBuilder().attribute(threadPool.getCanonicalName(), "currentThreadCount").label("Count").build())
       .addData(new AttributeBuilder().attribute(threadPool.getCanonicalName(), "currentThreadsBusy").label("Busy").build())
       .addData(new AttributeBuilder().attribute(threadPool.getCanonicalName(), "maxThreads").label("Max").build())
       .addData(new AttributeBuilder().attribute(threadPool.getCanonicalName(), "minSpareThreads").label("Min").build())
       .build();
       return threadPoolGraph;
   }
   
   
   private ObjectName getObjectByPort(Map<String, Set<ObjectName>> beans, String type, String port) {
       Set<ObjectName> objects = beans.get(type);
       for(ObjectName objectName : objects) {
           if(objectName.getKeyProperty("port").equals(port)) {
               return objectName;
           }
       }
       return null;
   }
   
   private ObjectName getObjectKeyMatches(Map<String, Set<ObjectName>> beans, String type, String key, String pattern) {
       Set<ObjectName> objects = beans.get(type);
       for(ObjectName objectName : objects) {
           if(objectName.getKeyProperty(key).matches(pattern)) {
               return objectName;
           }
       }
       return null;
   }
   
    
   private void addServerInfo(DashboardBuilder dashboardBuilder, int row) {
       TextGroupBuilder serverInfoBuilder= new TextGroupBuilder("serverInfo").title("Server Info").text((new AttributeBuilder().attribute("Tomcat:type=Server", "serverInfo").label("Server:").build()));
       
       Set<ObjectName> webModules = jmxService.getNamesMatching("Tomcat:*j2eeType=WebModule*");
       if(webModules.iterator().hasNext()) {
           ObjectName webModule = webModules.iterator().next();
           String name = webModule.getCanonicalName();
           serverInfoBuilder.staticText("Context: ", webModule.getKeyProperty("name"))
           .text(new AttributeBuilder().attribute(name, "name").label("Name:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "sessionTimeout").label("Session timeout (min):").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "baseName").label("Base Name:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "displayName").label("Display Name:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "path").label("Path:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "configFile").label("Configuration File:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "docBase").label("Document Base:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "welcomeFiles").label("Welcome Files:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "workDir").label("Working Directory:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "processingTime").label("Processing time (ms):").build())
           .text(new AttributeBuilder().attribute(name, "tldScanTime").label("Time spent scanning JARs for TLDs (ms):").build());
           
           
       }
       Set<ObjectName> hosts = jmxService.getNamesMatching("Tomcat:*type=Host*");
       if(hosts.iterator().hasNext()) {
           ObjectName hostBean = hosts.iterator().next();
           serverInfoBuilder.text(new AttributeBuilder().attribute(hostBean.getCanonicalName(), "appBase").label("Application Base:").build());
           serverInfoBuilder.text(new AttributeBuilder().attribute(hostBean.getCanonicalName(), "autoDeploy").label("Auto Deploy:").build());
       }
       TextGroup serverInfo = serverInfoBuilder.build();
       dashboardBuilder.add(new PanelBuilder("serverInfoPanel","Server Info", row, 1).width(2).height(2).textGroup(serverInfo).build());
       
       Set<ObjectName> caheBeans = jmxService.getNamesMatching("Tomcat:*type=Cache*");
       if(caheBeans.iterator().hasNext()) {
           ObjectName cacheBean =caheBeans.iterator().next();
           String name = cacheBean.getCanonicalName();
           Gauge cacheUseGauge = new GaugeBuilder("cacheUseGauge-resource").title("Resorce Cache Usage")
                                                   .min("0")
                                                   .max(new AttributeBuilder().attribute(name, "accessCount").build())
                                                   .value(new AttributeBuilder().attribute(name, "hitsCount").label("Hits").build())  
                                                   .build();
           dashboardBuilder.add(new PanelBuilder("cacheUseGaugePanel","Cache", row, 3).gauge(cacheUseGauge).build());
       }
       
   }
    
   private void addSessionPanels(DashboardBuilder dashboardBuilder, int row) {
       Set<ObjectName> managerBeans = jmxService.getNamesMatching("Tomcat:*type=Manager*");
       if(managerBeans.iterator().hasNext()) {
           ObjectName managerBean = managerBeans.iterator().next();
           String name = managerBean.getCanonicalName();
           TextGroup sessionInfo = new TextGroupBuilder("sessionInfo").title("Sessions")
                                                         .text(new AttributeBuilder().attribute(name, "sessionCounter").label("Total # created Sessions:").build())
                                                         .text(new AttributeBuilder().attribute(name, "rejectedSessions").label("Total # rejected Sessions:").build())
                                                         .text(new AttributeBuilder().attribute(name, "sessionMaxAliveTime").label("Longest session time (ms):").build())
                                                         .text(new AttributeBuilder().attribute(name, "sessionAverageAliveTime").label("Average Session Time (ms):").build())
                                                         .build();
           dashboardBuilder.add(new PanelBuilder("sessionInfoPanel","Session Info", row, 1).width(2).textGroup(sessionInfo).build());
           int maxActiveSessions = (Integer) jmxService.getAttributeValue(name, "maxActiveSessions");
           String maxAttribute = "maxActiveSessions";
           boolean subscribeToMax = false;
           String suffix = "";
           if(maxActiveSessions < 0) {
               maxAttribute = "maxActive";
               suffix=" (No limit)";
               subscribeToMax = true;
           }    
           Gauge sessionGauge = new GaugeBuilder("sessionGauge").title("Sessions" + suffix).min("0")
                                                                                            .max(new AttributeBuilder().attribute(name, maxAttribute).subscribe(subscribeToMax).build())
                                                                                           .value(new AttributeBuilder().attribute(name, "activeSessions").label("Active").build()).build();
           dashboardBuilder.add(new PanelBuilder("sessionGaugePanel","Active Sessions", row, 3).gauge(sessionGauge).build());
           
           
           Graph createAndExpiryRate = new GraphBuilder("createAndExpiryRate").title("Sessions per minuete")
                                                         .addData(new AttributeBuilder().attribute(name, "sessionExpireRate").label("Expired").build())
                                                         .addData(new AttributeBuilder().attribute(name, "sessionCreateRate").label("Created").build()).build();
           dashboardBuilder.add(new PanelBuilder("createAndExpiryRatePanel","Session Rate", row, 4).graph(createAndExpiryRate).build());
           
           
       }
   }
    
   private int addDatasourcePanels(DashboardBuilder dashboardBuilder, int row) {
       Set<ObjectName> datasourceBeans = jmxService.getNamesMatching("Tomcat:*type=DataSource*");
       for(ObjectName datasourceBean : datasourceBeans) {
           String name = datasourceBean.getCanonicalName();
           TextGroup dataSourceText = new TextGroupBuilder("dataSourceText-"+row).title("DataSource " + datasourceBean.getKeyProperty("name"))
           .staticText("JNDI Name", datasourceBean.getKeyProperty("name"))
           .text(new AttributeBuilder().attribute(name, "modelerType").label("Datasource class:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "username").label("Username:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "url").label("URL:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "driverClassName").label("Driver Class:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "defaultAutoCommit").label("Auto commit:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "defaultCatalog").label("Catalog:").subscribe(false).build())
           .text(new AttributeBuilder().attribute(name, "defaultReadOnly").label("Read only:").subscribe(false).build())
           .staticText("Transaction Isolation", getTranscationIsolation(datasourceBean))
           .build();
           
           dashboardBuilder.add(new PanelBuilder("dataSourceTextPanel-"+row,"DataSource " + datasourceBean.getKeyProperty("name") + " Info", row, 1).width(2).height(2).textGroup(dataSourceText).build());
           
           Gauge usedGauge = new GaugeBuilder("datasourceUsageGauge-"+row)
                                               .max(new AttributeBuilder().attribute(name, "maxActive").subscribe(false).build())
                                               .min("0")
                                               .value(new AttributeBuilder().attribute(name, "numActive").label("Count").build())
                                               .title("Used Connections")
                                               .build();
           dashboardBuilder.add(new PanelBuilder("datasourceUsagePanel-"+row,"Active " + datasourceBean.getKeyProperty("name"), row, 2).gauge(usedGauge).build());
           Gauge idleGauge = new GaugeBuilder("datasourceIdleGauge"+row)
                                               .max(new AttributeBuilder().attribute(name, "maxIdle").subscribe(false).build())
                                               .min(new AttributeBuilder().attribute(name, "minIdle").subscribe(false).build())
                                               .value(new AttributeBuilder().attribute(name, "numIdle").label("Count").build())
                                               .title("Idle Connections")
                                               .build();
           
           dashboardBuilder.add(new PanelBuilder("datasourceIdlePanel"+row,"Idle " + datasourceBean.getKeyProperty("name"), row+1, 2).gauge(idleGauge).build());
          row +=2;
       }
       return row;
   }
   
   private int addServletPanels(DashboardBuilder dashboardBuilder, int row) {
       Set<ObjectName> servletBeans = jmxService.getNamesMatching("Tomcat:*j2eeType=Servlet*");
       for(ObjectName servletBean : servletBeans) {
           String name = servletBean.getCanonicalName();
           TextGroup servletInfo = new TextGroupBuilder("servletInfoText-"+row).title("Servlet " + servletBean.getKeyProperty("WebModule"))
                                                         .text(new AttributeBuilder().attribute(name, "servletClass").label("Servlet Class:").subscribe(false).build())
                                                         .text(new AttributeBuilder().attribute(name, "stateName").label("State: ").build())
                                                         .text(new AttributeBuilder().attribute(name, "loadOnStartup").label("Load On Startup (order):").build())
                                                         .text(new AttributeBuilder().attribute(name, "classLoadTime").label("Load Time (ms):").build())
                                                         .text(new AttributeBuilder().attribute(name, "processingTime").label("Processing Time (ms):").build())
                                                         .text(new AttributeBuilder().attribute(name, "errorCount").label("Error count:").build())
                                                         .text(new AttributeBuilder().attribute(name, "requestCount").label("Number of requests processed").build())                                                   
                                                         .build();
           dashboardBuilder.add(new PanelBuilder("servletInfoPanel-"+row,"Servlet " + servletBean.getKeyProperty("WebModule"), row, 1).width(2).height(2).textGroup(servletInfo).build());
           Graph processingGraph = new GraphBuilder("processingGraph-"+row).title("Processing time of a request")
                                                      .addData(new AttributeBuilder().attribute(name, "minTime").label("Minimum").build())
                                                      .addData(new AttributeBuilder().attribute(name, "maxTime").label("Maximum").build())
                                                      .build();
           dashboardBuilder.add(new PanelBuilder("processingGraphPanel-"+row,"Processing Time", row, 3).width(3).height(2).graph(processingGraph).build());
           Gauge errorRate = new GaugeBuilder("servletErrorRate-"+row).min("0")
                                               .max(new AttributeBuilder().attribute(name, "requestCount").build())
                                               .value(new AttributeBuilder().attribute(name, "errorCount").label("# Errors").build()).title("Error rate").build();
           dashboardBuilder.add(new PanelBuilder("servletErrorRatePanel-"+row,"Errors", row, 7).gauge(errorRate).build());
           row += 2;
       }
       
       return row;
   }
   
   private String getTranscationIsolation(ObjectName objectName) {
       Integer value = (Integer) jmxService.getAttributeValue(objectName.getCanonicalName(), "defaultTransactionIsolation");
       return transactionIsolationAsString(value);
   }
    
   private String transactionIsolationAsString(int transactionIsolation) {
       switch(transactionIsolation) {
       case TRANSACTION_NONE:
           return "TRANSACTION_NONE";
       case TRANSACTION_READ_COMMITTED:
           return "TRANSACTION_READ_COMMITTED";
       case TRANSACTION_READ_UNCOMMITTED:
           return "TRANSACTION_READ_UNCOMMITTED";
       case TRANSACTION_REPEATABLE_READ:
           return "TRANSACTION_REPEATABLE_READ";
       case TRANSACTION_SERIALIZABLE:
           return "TRANSACTION_SERIALIZABLE";
       }
       return "Unknown Isolation Level";
   }
    
}
