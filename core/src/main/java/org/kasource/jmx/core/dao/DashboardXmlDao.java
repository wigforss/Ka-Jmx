package org.kasource.jmx.core.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;




import org.kasource.jmx.core.model.dashboard.Configuration;
import org.kasource.jmx.core.model.dashboard.Dashboard;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;



public class DashboardXmlDao implements DashboardDao {

    private Resource sourceXml;
    private Resource additionalSourceXml;
    
    @Override
    public List<Dashboard> getDashboards() {
        
       
            JAXBContext context;
            try {
                context = JAXBContext.newInstance(Dashboard.class.getPackage().getName());
            } catch (JAXBException e) {
                throw new IllegalArgumentException("Could not create JAXBContext", e);
            }
            List<Dashboard> dashboards = new ArrayList<Dashboard>();
            try {
                Configuration config = (Configuration) context.createUnmarshaller().unmarshal(sourceXml.getInputStream());
                dashboards.addAll(config.getDashboard());
            } catch (Exception e) {
                throw new IllegalStateException("Could not load dashboards configuration from " + sourceXml.toString(), e);
            }
            if(additionalSourceXml != null) {
                if (additionalSourceXml.isReadable()) {
            
                    try {
                        Configuration config = (Configuration) context.createUnmarshaller().unmarshal(additionalSourceXml.getInputStream());
                        dashboards.addAll(config.getDashboard());
                    } catch (Exception e) {
                    throw new IllegalStateException("Could not load dashboards configuration from " + additionalSourceXml.toString(), e);
                    }
                } else {
                    throw new IllegalStateException("Could not read configuration from " + additionalSourceXml.toString());
                }
            }
            Collections.sort(dashboards);
            return dashboards;
      
       
    }

    /**
     * @param sourceXml the sourceXml to set
     */
    @Required
    public void setSourceXml(Resource sourceXml) {
        this.sourceXml = sourceXml;
    }

    /**
     * @param additionalSourceXml the additionalSourceXml to set
     */
    @Required
    public void setAdditionalSourceXml(Resource additionalSourceXml) {
        this.additionalSourceXml = additionalSourceXml;
    }

}
