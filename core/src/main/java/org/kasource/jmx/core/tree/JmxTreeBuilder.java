package org.kasource.jmx.core.tree;

import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.kasource.jmx.core.tree.node.DomainNode;
import org.kasource.jmx.core.tree.node.ObjectNode;
import org.kasource.jmx.core.tree.node.RootNode;
import org.kasource.jmx.core.util.JavadocResolver;
import org.springframework.stereotype.Component;

@Component
public class JmxTreeBuilder {

    private static final String LABEL_DOMAINS = "Domains";

    @Resource
    private MBeanServer server;
    
 

    public JmxTree buildTree() {
        Set<DomainNode> domainNodes = new TreeSet<DomainNode>();
        String[] domains = server.getDomains();
        for (String domain : domains) {
            try {
                DomainNode domainNode = buildDomain(domain);
                domainNodes.add(domainNode);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        RootNode root = new RootNode(LABEL_DOMAINS, domainNodes);
        return new JmxTree(root);
    }

    private DomainNode buildDomain(String domain) throws MalformedObjectNameException, NullPointerException {

        Set<ObjectNode> objectNodes = new TreeSet<ObjectNode>();

        Set<ObjectName> objectNames = server.queryNames(ObjectName.getInstance(domain + ":*"), null);

        for (ObjectName objectInDomain : objectNames) {
            try {
                ObjectNode objectNode = buildObject(objectInDomain);
                objectNodes.add(objectNode);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return new DomainNode(domain, objectNodes);
    }

    private ObjectNode buildObject(ObjectName objectName) throws IntrospectionException, InstanceNotFoundException, ReflectionException {
        MBeanInfo beanInfo = server.getMBeanInfo(objectName);
        String label = objectName.getKeyProperty("name");
       
        if(label == null) {
            label = objectName.getCanonicalKeyPropertyListString();
        } else {
           
            if(objectName.getKeyPropertyList().size() > 1) {
                label += " ("+ objectName.getCanonicalKeyPropertyListString().replace(",name="+label, "").replace("name=" + label + ",", "") + ")";
            }
        }
    
        return new ObjectNode(label, beanInfo, objectName.toString());

    }
}
