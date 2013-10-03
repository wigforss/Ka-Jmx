<?xml version="1.0" encoding="UTF-8"?>
<jsp:root version="1.2" xmlns="http://www.w3c.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:tiles="http://tiles.apache.org/tags-tiles">



	<div xmlns:jsp="http://java.sun.com/JSP/Page" >
    <jsp:output omit-xml-declaration="yes"/>
	<jsp:directive.page contentType="text/html" />
	<jsp:directive.page isELIgnored="false" />
   
	
	
	<div id="beanView">
		
		<div id="jmx_bean">
			<tiles:insertAttribute name="jmx_bean"/>
		</div>	
		<div id="jmx_attributes">
			<tiles:insertAttribute name="jmx_attributes"/>
		</div>	
		<div id="jmx_operations">
			<tiles:insertAttribute name="jmx_operations"/>
		</div>
		<div id="jmx_notifications">
			<tiles:insertAttribute name="jmx_notifications"/>
		</div>
		
		<p/>
		
		<form id="refreshBeanForm" action="${pageContext.request.contextPath}/html/bean" method="post">
			<input type="hidden" name="objectName" id="refresh-bean-name" value="${fn:escapeXml(mBean.name)}"/>	
			<input id="refreshBeanButton" name="refresh" title="Refresh" value="Refresh JMX Bean" type="button" onclick="refreshBean()"/>
		</form>
	</div>
	<div id="dashboardView">
		<c:forEach var="dashboard" items="${dashboards}">
			<div id="${dashboard.id}" class="gridster hidden" >
				<ul class="dashboard">
				<c:forEach var="panel" items="${dashboard.panel}">
					
    					<li id="${panel.id}" data-row="${panel.row}" data-col="${panel.column}" data-sizex="${panel.width}" data-sizey="${panel.height}" class="panel" ondblclick="org.kasource.jmx.dashboard.togglePanelSize('${dashboard.id}',this.id,'${panel.widget.id}')">
    				 		<div class="title-bar"><span class="title-bar-text" title="${panel.title}"><c:out value="${panel.title}"/></span><span class="title-bar-icon"><img src="style/images/dialog-no-3.png" width="16" height="16" onclick="org.kasource.jmx.dashboard.removePanel('${dashboard.id}','${panel.id}','${panel.widget.id}')"/></span></div>
    						<div id="${panel.widget.id}" class="${panel.widget.type}"></div>
    					</li>
    					
    			</c:forEach>
    			</ul>
			</div>
 		</c:forEach>
	
	</div>	
	<script type="text/javascript">
 	<![CDATA[
		function loadBean(objectName) {
			currentDasboard = 'none';
			$("#dashboardView").addClass("hidden");
			$("#beanView").removeClass("hidden");
			var currentBean = document.getElementById('refresh-bean-name');
			if(currentBean && currentBean.value && currentBean.value==objectName) {
				focusOnElement('bean-title');
			} else {
				document.getElementById('refresh-bean-name').value=objectName;
				Spring.remoting.submitForm("refreshBeanButton", "refreshBeanForm", { fragments: "jmx_bean, jmx_attributes, jmx_operations, jmx_notifications"});
				setTimeout(function(){focusOnElement('bean-title')}, 500);
			}
	
		}

		function refreshBean() {
			Spring.remoting.submitForm("refreshBeanButton", "refreshBeanForm", { fragments: "jmx_bean, jmx_attributes, jmx_operations, jmx_notifications"});
		}
		
		
		
		
	]]>         
 	</script>
 	</div>
</jsp:root>

    