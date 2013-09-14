package org.kasource.jmx.core.dashboard.tomcat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.ObjectName;

import org.kasource.jmx.core.dashboard.DashboardFactory;
import org.kasource.jmx.core.dashboard.builder.AttributeBuilder;
import org.kasource.jmx.core.dashboard.builder.DashboardBuilder;
import org.kasource.jmx.core.dashboard.builder.GaugeBuilder;
import org.kasource.jmx.core.dashboard.builder.GraphBuilder;
import org.kasource.jmx.core.dashboard.builder.TextGroupBuilder;
import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.kasource.jmx.core.model.dashboard.Gauge;
import org.kasource.jmx.core.model.dashboard.Graph;
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
        return dashboards;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    
    private void addConnectorPanels(DashboardBuilder dashboardBuilder, int row) {
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

}
