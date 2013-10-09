package org.kasource.jmx.core.dashboard.tomcat;

import static java.sql.Connection.TRANSACTION_NONE;
import static java.sql.Connection.TRANSACTION_READ_COMMITTED;
import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;
import static java.sql.Connection.TRANSACTION_REPEATABLE_READ;
import static java.sql.Connection.TRANSACTION_SERIALIZABLE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.ObjectName;


import org.kasource.jmx.core.dashboard.JavaScriptFunction;
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
public class ServerDashboardFactory {
    
    @Resource
    private JmxService jmxService;
    
    private String domain = "Tomcat";
    
    
   

    public List<Dashboard> getDashboards() {
        List<Dashboard> dashboards = new ArrayList<Dashboard>();
        dashboards.add(getServerDashboard());
        return dashboards;
    }
    
    private Dashboard getServerDashboard() {
        DashboardBuilder dashboardBuilder = new DashboardBuilder("tomcat","Tomcat", 120, 120);
        addServerInfo(dashboardBuilder, 1);
        return dashboardBuilder.build();
    }
    
    private void addServerInfo(DashboardBuilder dashboardBuilder, int row) {
        TextGroupBuilder serverInfoBuilder = new TextGroupBuilder("serverInfo")
                                    .text(new AttributeBuilder().attribute(domain + ":type=Server", "serverInfo").label("Server:").build());
        
        Set<ObjectName> hosts = jmxService.getNamesMatching(domain + ":type=Host,*");
        ObjectName hostBean = null;
        if(hosts.iterator().hasNext()) {
            hostBean = hosts.iterator().next();
            serverInfoBuilder.text(new AttributeBuilder().attribute(hostBean.getCanonicalName(), "appBase").label("Application Base:").build());
            serverInfoBuilder.text(new AttributeBuilder().attribute(hostBean.getCanonicalName(), "autoDeploy").label("Auto Deploy:").build());
        }
        TextGroup serverInfo = serverInfoBuilder.build();
        dashboardBuilder.add(new PanelBuilder("serverInfoPanel","Server Info", row, 1).width(2).height(2).textGroup(serverInfo).build());
        if(hostBean != null) {
            LedPanel ledPanel = getServerLedPanlel(hostBean);
            dashboardBuilder.add(new PanelBuilder("serverInfo-led-panel","Server State", row, 3).width(3).height(2).ledPanel(ledPanel).build());
        }
        addDatasourcePanels(dashboardBuilder, row + 2);
       
        
        
    }
    
    private LedPanel getServerLedPanlel(ObjectName hostBean) {
        
        String name = hostBean.getCanonicalName();
        LedPanelBuilder builder = new LedPanelBuilder("serverInfo-leds").layout(LayoutType.HORIZONTAL)
                                                    .addData(new AttributeBuilder().attribute(name, "stateName").label("Started").subscribe(false).jsFunction("function(value){return value === \"STARTED\";}").build())
                                                    .addData(new AttributeBuilder().attribute(name, "deployOnStartup").label("Deploy on Startup").subscribe(false).build())
                                                    .addData(new AttributeBuilder().attribute(name, "autoDeploy").label("Auto Deploy").subscribe(false).build())
                                                    .addData(new AttributeBuilder().attribute(name, "unpackWARs").label("Unpack WARs").subscribe(false).build())
                                                    .addData(new AttributeBuilder().attribute(name, "createDirs").label("Create Directories").subscribe(false).build())
                                                    .addData(new AttributeBuilder().attribute(name, "deployXML").label("Deploy Context XML").subscribe(false).build())
                                                    .addData(new AttributeBuilder().attribute(name, "startChildren").label("Start Child Containers").subscribe(false).build());
        Set<ObjectName> datasourceBeans = jmxService.getNamesMatching(domain + ":type=DataSource,*");
        for(ObjectName datasourceBean : datasourceBeans) {
            builder.addData(new AttributeBuilder().attribute(datasourceBean.getCanonicalName(), "numActive")
                                                                .label(datasourceBean.getKeyProperty("name"))
                                                                .jsFunction(JavaScriptFunction.NUMBER_TO_BOOLEAN.getScript()).build());
        }
        return builder.build();
                                                    
    }
    
    
    private int addDatasourcePanels(DashboardBuilder dashboardBuilder, int row) {
        Set<ObjectName> datasourceBeans = jmxService.getNamesMatching(domain + ":type=DataSource,*");
        for(ObjectName datasourceBean : datasourceBeans) {
            String name = datasourceBean.getCanonicalName();
            TextGroup dataSourceText = new TextGroupBuilder("dataSourceText-"+row)
            .staticText(datasourceBean.getKeyProperty("name"), "JNDI Name:")
            .text(new AttributeBuilder().attribute(name, "username").label("Username:").subscribe(false).build())
            .text(new AttributeBuilder().attribute(name, "url").label("URL:").subscribe(false).build())
            .text(new AttributeBuilder().attribute(name, "driverClassName").label("Driver Class:").subscribe(false).build())
            .text(new AttributeBuilder().attribute(name, "defaultAutoCommit").label("Auto commit:").subscribe(false).build())
            .text(new AttributeBuilder().attribute(name, "defaultCatalog").label("Catalog:").subscribe(false).build())
            .text(new AttributeBuilder().attribute(name, "defaultReadOnly").label("Read only:").subscribe(false).build())
            .staticText(getTranscationIsolation(datasourceBean), "Transaction Isolation:")
            .build();
            
            dashboardBuilder.add(new PanelBuilder("dataSourceTextPanel-"+row,"DataSource " + datasourceBean.getKeyProperty("name"), row, 1).height(2).width(2).textGroup(dataSourceText).build());
            
            Gauge usedGauge = new GaugeBuilder("datasourceUsageGauge-"+row)
                                                .max(new AttributeBuilder().attribute(name, "maxActive").subscribe(false).build())
                                                .min("0")
                                                .value(new AttributeBuilder().attribute(name, "numActive").label("Count").build())
                                                .title("Used Connections")
                                                .build();
            dashboardBuilder.add(new PanelBuilder("datasourceUsagePanel-"+row,"Active" + datasourceBean.getKeyProperty("name"), row, 3).gauge(usedGauge).build());
            Gauge idleGauge = new GaugeBuilder("datasourceIdleGauge-"+row)
                                                .max(new AttributeBuilder().attribute(name, "maxIdle").subscribe(false).build())
                                                .min(new AttributeBuilder().attribute(name, "minIdle").subscribe(false).build())
                                                .value(new AttributeBuilder().attribute(name, "numIdle").label("Count").build())
                                                .title("Idle Connections")
                                                .build();
            
            dashboardBuilder.add(new PanelBuilder("datasourceIdlePanel-"+row,"Idle" + datasourceBean.getKeyProperty("name"), row+1, 3).gauge(idleGauge).build());
            
            Graph connectionUsage = new GraphBuilder("connectionUsage-"+row).yAxisLabel("# Connections").samples(600).decimals(0)
                                                    .addData(new AttributeBuilder().attribute(name, "numActive").label("Active").build())
                                                    .addData(new AttributeBuilder().attribute(name, "numIdle").label("Idle").build())
                                                    .addData(new AttributeBuilder().attribute(name, "maxActive").label("Max").subscribe(false).build())
                                                    .build();
            dashboardBuilder.add(new PanelBuilder("connectionUsagePanel-"+row, "Connections "+ datasourceBean.getKeyProperty("name") + " last 10 min", row, 4).height(2).width(4).graph(connectionUsage).build());
            
           row +=2;
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
        return Integer.valueOf(transactionIsolation).toString();
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    
}
