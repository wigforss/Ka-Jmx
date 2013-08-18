<?xml version="1.0" encoding="UTF-8"?>
<jsp:root version="1.2" xmlns="http://www.w3c.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:tiles="http://tiles.apache.org/tags-tiles">



	<div xmlns:jsp="http://java.sun.com/JSP/Page" >
    <jsp:output omit-xml-declaration="yes"/>
	<jsp:directive.page contentType="text/html" />
	<jsp:directive.page isELIgnored="false" />
 	<script type="text/javascript">
 	<![CDATA[
       org.kasource.Websocket.initialize();     
 	         
		function refreshTree() {
			Spring.remoting.submitForm("refreshTreeButton", "refreshTreeForm", { fragments: "jmx_tree"});
		}
		
		
		
		function focus(beanName, elementId) {
			var currentBean = document.getElementById('refresh-bean-name');
			if(currentBean && currentBean.value && currentBean.value==beanName) {
				focusOnElement(elementId)
			} else {
				loadBean(beanName);
				setTimeout(function(){focusOnElement(elementId)}, 500);
			}
		}
		
		function focusOnElement(elementId) {
			var element = document.getElementById(elementId);
			if(element) {
				element.focus();
			}
		}
	]]>         
 	</script>
 	
		
		<div id="jmx_tree">
			<tiles:insertAttribute name="jmx_tree"/>
		</div>	
			<p/>
		<form id="refreshTreeForm" action="${pageContext.request.contextPath}/html/tree/refresh" method="post">
			
			<input id="refreshTreeButton" name="refresh" title="Refresh" value="Refresh JMX Tree" type="button" onclick="refreshTree()"/>
		</form>
	
	
 	</div>
</jsp:root>

    