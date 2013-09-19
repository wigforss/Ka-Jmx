<jsp:root version="1.2" xmlns="http://www.w3c.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:tiles="http://tiles.apache.org/tags-tiles">

	<jsp:directive.page contentType="text/html" />
	<jsp:directive.page isELIgnored="false" />
	<c:choose>
	<c:when test="${not empty dashboard}"> 
		<div id="dashboard" class="gridster">	
    	<ul class="dashboard">
    		<c:forEach var="panel" items="${dashboard.panel}">
    			<c:set var="widgetClass" value="${not empty panel.graph ? 'graph' : not empty panel.gauge ? 'gauge' : 'textGroup'}"/>
    			<c:set var="widgetId" value="${not empty panel.graph ? panel.graph.id : not empty panel.gauge ? panel.gauge.id : panel.textGroup.id}"/>
    			<li id="${panel.id}" data-row="${panel.row}" data-col="${panel.column}" data-sizex="${panel.width}" data-sizey="${panel.height}" class="panel" ondblclick="togglePanelSize(this.id,'${widgetId}')">
    				 <div class="title-bar"><span title="${panel.title}" style="white-space: nowrap">${panel.title}</span><div style="float:right"><img src="style/images/dialog-no-3.png" width="16" height="16" onclick="removePanel('${panel.id}','${widgetId}')"/></div></div>
    				
    				<div id="${widgetId}" class="${widgetClass}"></div>
    			</li>
    		</c:forEach>
    	</ul>
        

		
		</div>
		
		
 		<script type="text/javascript"><![CDATA[
			$(".gridster ul").gridster({
    			widget_margins: [5, 5],
    			widget_base_dimensions: [100, 100],
    			extra_cols: 5,
				draggable: {
					handle: '.title-bar'
				}
			});

			var gridster = $(".gridster").gridster().data('gridster');
    		]]></script>
 			 
		<c:forEach var="panel" items="${dashboard.panel}">
			<c:set var="widgetClass" value="${not empty panel.graph ? 'graph' : not empty panel.gauge ? 'gauge' : 'textGroup'}"/>
			<c:set var="widgetId" value="${not empty panel.graph ? panel.graph.id : not empty panel.gauge ? panel.gauge.id : panel.textGroup.id}"/>
			<c:choose>
				<c:when test="${widgetClass == 'textGroup'}">
					<script type="text/javascript"><![CDATA[
						widgetFactory['${widgetClass}'].get('${widgetId}', JSON.parse('${panel.textGroup.json}'));
					]]></script>
				</c:when>
				<c:when test="${widgetClass == 'gauge'}">
					<script type="text/javascript"><![CDATA[
						widgetFactory['${widgetClass}'].get('${widgetId}', JSON.parse('${panel.gauge.json}'));
					]]></script>
				</c:when>
				<c:otherwise>
					<script type="text/javascript"><![CDATA[
						widgetFactory['${widgetClass}'].get('${widgetId}', JSON.parse('${panel.graph.json}'));
					]]></script>
				</c:otherwise>
			</c:choose>
				
				
			
 		</c:forEach>
 		
	</c:when>
	<c:otherwise>
		<div id="dashboard"><c:out value=""/></div>
	</c:otherwise>
	</c:choose>

</jsp:root>
<!-- 
<script type="text/javascript">
 		<![CDATA[
			<c:forEach var="panel" items="${dashboard.panel}">
			 $( "#dialog-${panel.title}" ).dialog({
				 autoOpen: false,
				 show: {
				 effect: "blind",
				 duration: 1000
				 },
				 hide: {
				 effect: "explode",
				 duration: 1000
				 }
				 });
			 $( "#dialog" ).dialog( "open" );
			</c:forEach>
		]]>         
 		</script>
  -->