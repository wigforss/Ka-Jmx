package org.kasource.jmx.core.dashboard.tomcat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.ObjectName;

import org.kasource.jmx.core.dashboard.DashboardFactory;
import org.kasource.jmx.core.dashboard.JavaScriptFunction;

import static org.kasource.jmx.core.dashboard.JavaScriptFunction.*;
import org.kasource.jmx.core.dashboard.builder.AttributeBuilder;
import org.kasource.jmx.core.dashboard.builder.DashboardBuilder;
import org.kasource.jmx.core.dashboard.builder.GaugeBuilder;
import org.kasource.jmx.core.dashboard.builder.GraphBuilder;
import org.kasource.jmx.core.dashboard.builder.LedPanelBuilder;
import org.kasource.jmx.core.dashboard.builder.PanelBuilder;
import org.kasource.jmx.core.dashboard.builder.TextGroupBuilder;
import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.model.dashboard.Gauge;
import org.kasource.jmx.core.model.dashboard.Graph;
import org.kasource.jmx.core.model.dashboard.LayoutType;
import org.kasource.jmx.core.model.dashboard.LedPanel;
import org.kasource.jmx.core.model.dashboard.TextGroup;
import org.kasource.jmx.core.service.JmxService;
import org.springframework.stereotype.Component;

@Component
public class ConnectorDashboardFactory implements DashboardFactory {
   
    @Resource
    private JmxService jmxService;
    
    private String domain = "Tomcat";
    
    @Override
    public List<Dashboard> getDashboards() {
        List<Dashboard> dashboards = new ArrayList<Dashboard>();
        Map<String, Set<ObjectName>> beans = new HashMap<String, Set<ObjectName>>();
        // Connector etc per port
        List<String> ports = new ArrayList<String>();
       
        
        beans.put("connectors", jmxService.getNamesMatching(domain + ":type=Connector,*"));
        beans.put("protocolHandlers", jmxService.getNamesMatching(domain + ":type=ProtocolHandler,*"));
        beans.put("requestProcessors", jmxService.getNamesMatching(domain + ":type=GlobalRequestProcessor,*"));
        beans.put("threadPools", jmxService.getNamesMatching(domain + ":type=ThreadPool,*"));
        
        for(ObjectName connectorBean : beans.get("connectors")) {
            ports.add(connectorBean.getKeyProperty("port"));
        }
        
        for(String port : ports) {
            dashboards.add(getConnectorDashboard(port, beans));
        }
        return dashboards;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    
   
     
    private Dashboard getConnectorDashboard(String port, Map<String, Set<ObjectName>> beans) {
       
        ObjectName connector = getObjectByPort(beans, "connectors", port);
        
        String protocol = jmxService.getAttributeValue(connector.getCanonicalName(), "protocol").toString();
        
        ObjectName protocolHandler = getObjectByPort(beans, "protocolHandlers", port);
        ObjectName requestProcessor =  getObjectKeyMatches(beans, "requestProcessors", "name", ".*"+port+".*");
        ObjectName threadPool = getObjectKeyMatches(beans, "threadPools", "name", ".*"+port+".*");
        DashboardBuilder dashboardBuilder = new DashboardBuilder("connector-"+port, protocol+" "+port, 120, 120);
        dashboardBuilder.add(new PanelBuilder("connector-panel-" + port, "Connector " + port, 1, 1)
                                                            .width(2)
                                                            .height(3)
                                                            .textGroup(getConnectorTextGroup(connector, port)).build());
        dashboardBuilder.add(new PanelBuilder("protocol-panel-"+port, protocol + " "+ port, 1, 3)
                                                            .width(2)
                                                            .height(3)
                                                            .textGroup(getProtocolTextGroup(protocolHandler, port)).build());
        dashboardBuilder.add(new PanelBuilder("processing-panel-"+port, "Processing " + protocol + " "+port, 4, 1)
                                                             .width(2)
                                                             .height(2)
                                                             .textGroup(getProcessingAndThreadTextGroup(requestProcessor, threadPool, port))
                                                             .build());
        dashboardBuilder.add(new PanelBuilder("threadpool-panel"+port, "Thread pool " + protocol + " " + port, 4, 3)
                                                             .width(4)
                                                             .height(2)
                                                             .graph(getThreadGraph(threadPool, port))
                                                             .build());
        dashboardBuilder.add(new PanelBuilder("led-panel-panel"+port, "Status", 1, 5).width(2).height(3).ledPanel(getLedPanel(requestProcessor, threadPool, protocolHandler, port))
                                                             .build());
        
        dashboardBuilder.add(new PanelBuilder("processed-panel"+port, "Processed bytes", 6, 1)
        .width(4)
        .height(2)
        .graph(getProcessingGraph(requestProcessor, port))
        .build());
        return dashboardBuilder.build();
    }
    
    
    private TextGroup getConnectorTextGroup(ObjectName connector, String port) {
        String name = connector.getCanonicalName();
       
        TextGroup connectorInfo = new TextGroupBuilder("connector-text-"+port)
                                                        .text(new AttributeBuilder().attribute(name, "port").label("Port:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "protocol").label("Protocol:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "stateName").label("State:").build())
                                                        .text(new AttributeBuilder().attribute(name, "scheme").label("Scheme:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "localPort").label("Local Port:").subscribe(false).build()) 
                                                        .text(new AttributeBuilder().attribute(name, "maxKeepAliveRequests").label("Max # Keep Alive Requests:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "maxParameterCount").label("Max # Parameters:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "maxPostSize").label("Max POST size:").jsFunction(BYTES_TO_KB.getScript()).subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "maxParameterCount").label("Max # Parameters:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "address").label("Bind IP address:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "URIEncoding").label("URI Encoding:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "protocolHandlerClassName").label("Protocol Handler:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "redirectPort").label("Redirect Port:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "sslProtocols").label("SSL Protocols:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "proxyName").label("Proxy Server:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "proxyPort").label("Proxy Port:").subscribe(false).build())
                                                        .build();
       return connectorInfo;
        
    }
    
    private TextGroup getProtocolTextGroup(ObjectName protocolHandler, String port) {
        String name = protocolHandler.getCanonicalName();
        TextGroup protocolInfo = new TextGroupBuilder("protocol-"+port)
                                                        .text(new AttributeBuilder().attribute(name, "name").label("Name:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "maxConnections").label("Max # Connections:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "threadPriority").label("Thread Priority:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "connectionTimeout").jsFunction(MILLISECONDS_TO_SECONDS.getScript()).label("Connection Timeout:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "keepAliveTimeout").jsFunction(MILLISECONDS_TO_SECONDS.getScript()).label("Keep Alive Timeout:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "sessionTimeout").jsFunction(SECONDS_TO_MINUTES.getScript()).label("SSL Session Timeout").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "connectionUploadTimeout").jsFunction(MILLISECONDS_TO_SECONDS.getScript()).label("Upload Timeout:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "disableUploadTimeout").label("Upload Timeout Disbabled:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "maxHttpHeaderSize").jsFunction(BYTES_TO_BYTES.getScript()).label("Max HTTP Header Size:").subscribe(false).build())                                             
                                                        .text(new AttributeBuilder().attribute(name, "maxHeaderCount").label("Max # Headers:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "maxSavePostSize").jsFunction(BYTES_TO_BYTES.getScript()).label("Max authentication POST size:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "compression").label("Compression:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "compressionMinSize").jsFunction(BYTES_TO_BYTES.getScript()).label("Compression threshold:").subscribe(false).build())
                                                        .text(new AttributeBuilder().attribute(name, "compressableMimeTypes").label("MIME types to compress:").subscribe(false).build())
                                                        .build();
        return protocolInfo;
    }
  

    private TextGroup getProcessingAndThreadTextGroup(ObjectName requestProcessor, ObjectName threadPool, String port) {
        String processor = requestProcessor.getCanonicalName();
        String threads = threadPool.getCanonicalName();
        TextGroup processorInfo = new TextGroupBuilder("threads-"+port)
                                                        .text(new AttributeBuilder().attribute(processor, "processingTime").jsFunction(MILLISECONDS_TO_DURATION.getScript()).label("Processing Time:").build())
                                                        .text(new AttributeBuilder().attribute(processor, "maxTime").jsFunction(MILLISECONDS_TO_MILLISECONDS.getScript()).label("Max time for a request:").build())
                                                        .text(new AttributeBuilder().attribute(threads, "connectionCount").label("Connection Count:").build())
                                                        .text(new AttributeBuilder().attribute(processor, "requestCount").label("# Requests:").build())
                                                        .text(new AttributeBuilder().attribute(processor, "errorCount").label("# Errors:").build())
                                                       .build();
        return processorInfo;
                                                       
    }
    
    private LedPanel getLedPanel(ObjectName requestProcessor, ObjectName threadPool, ObjectName protocolHandler, String port) {
        String threads = threadPool.getCanonicalName();
        String processor = requestProcessor.getCanonicalName();
      
       return new LedPanelBuilder("led-panel-"+port).layout(LayoutType.VERTICAL).color("#FFFF00")
       .addData(new AttributeBuilder().attribute(processor, "errorCount").label("Error").jsFunction(JavaScriptFunction.NUMBER_TO_BOOLEAN.getScript()).build())
       .addData(new AttributeBuilder().attribute(protocolHandler.getCanonicalName(), "compression").label("Compression").subscribe(false).jsFunction(JavaScriptFunction.ONOFF_TO_BOOLEAN.getScript()).build())
       .addData(new AttributeBuilder().attribute(threads, "daemon").label("Daemon").subscribe(false).build())
       .addData(new AttributeBuilder().attribute(threads, "deferAccept").label("TCP Defer Accept").subscribe(false).build())
       .addData(new AttributeBuilder().attribute(threads, "useComet").label("Use Comet").subscribe(false).build())
       .addData(new AttributeBuilder().attribute(threads, "useCometTimeout").label("Use Comet Timeout").subscribe(false).build())
       .addData(new AttributeBuilder().attribute(threads, "usePolling").label("Use Polling").subscribe(false).build())
       .addData(new AttributeBuilder().attribute(threads, "useSendfile").label("Use Send File").subscribe(false).build())
       .build();
    }
    
   
    private Graph getProcessingGraph(ObjectName requestProcessor, String port) {
        Graph processingGraph = new GraphBuilder("processingGraph-"+port).samples(600).decimals(0)
                                                  .addData(new AttributeBuilder().attribute(requestProcessor.getCanonicalName(), "bytesSent").label("Sent").build())
                                                  .addData(new AttributeBuilder().attribute(requestProcessor.getCanonicalName(), "bytesReceived").label("Received").build())
                                                  .build();
        return processingGraph;
        
    }
    
    

    private Graph getThreadGraph(ObjectName threadPool, String port) {
        Graph threadPoolGraph = new GraphBuilder("threadPoolGraph-"+port).samples(600).decimals(0)
        .addData(new AttributeBuilder().attribute(threadPool.getCanonicalName(), "currentThreadCount").label("Count").visible(false).build())
        .addData(new AttributeBuilder().attribute(threadPool.getCanonicalName(), "currentThreadsBusy").label("Busy").build())
        .addData(new AttributeBuilder().attribute(threadPool.getCanonicalName(), "maxThreads").label("Max").visible(false).build())
        .addData(new AttributeBuilder().attribute(threadPool.getCanonicalName(), "minSpareThreads").label("Min").visible(false).build())
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
    
    @Override
    public void setJmxService(JmxService jmxService) {    
    }

}
