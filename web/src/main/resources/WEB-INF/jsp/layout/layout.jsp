<?xml version="1.0" encoding="UTF-8"?>
<jsp:root version="1.2" xmlns="http://www.w3c.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:tiles="http://tiles.apache.org/tags-tiles">


<!--  
	<jsp:output doctype-root-element="html"
				doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
				doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />
-->
	<jsp:output doctype-root-element="html" doctype-public="html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"/>
	<jsp:directive.page contentType="text/html" />
	<jsp:directive.page isELIgnored="false" />
    
  	<html xmlns="http://www.w3.org/1999/xhtml">
		<head>
			<title><tiles:insertAttribute name="title" ignore="true" /></title>
			<![CDATA[
			<link rel="stylesheet" type="text/css" href="style/treeview.css"/>
			<link rel="stylesheet" type="text/css" href="style/layout.css"/>
			<link rel="stylesheet" type="text/css" href="style/table.css"/>
			<link rel="stylesheet" type="text/css" href="style/dashboard.css"/>
			<link rel="stylesheet" type="text/css" href="style/list_menu.css"/>
			<link rel="stylesheet" type="text/css" href="style/jquery.gridster.css"/>
			<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
			<script type="text/javascript"  src="http://code.jquery.com/jquery-1.9.1.js"></script>
			<script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>  	
			<script type="text/javascript" src="script/jquery.gridster.with-extras.js"> </script>
			<script type="text/javascript" src="script/raphael.2.1.0.min.js"></script>
    		<script type="text/javascript" src="script/justgage.1.0.1.min.js"></script>
    		<script type="text/javascript" src="script/dashboard.js"></script>
    		<script type="text/javascript" src="script/widget.js"></script>
      		<script type="text/javascript" src="http://code.highcharts.com/highcharts.js"></script>
			<script type="text/javascript" src="http://code.highcharts.com/modules/exporting.js"></script> 
			<script type="text/javascript" src="resources/dojo/dojo.js"> </script>
			<script type="text/javascript" src="resources/spring/Spring.js"> </script>
			<script type="text/javascript" src="resources/spring/Spring-Dojo.js"> </script>		
		    <script type="text/javascript" src="script/smoothie.js"></script>  
		    <script type="text/javascript" src="script/jmx_console.js"></script>  
		    <script type="text/javascript" src="script/websocket.js"></script>  
		    <script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script> 
		    <script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/additional-methods.min.js"></script> 	
			]]>
		</head>
		<body> 
			<div id="main-layout">
				<div id="header-layout">
					<tiles:insertAttribute name="header"/>
				</div>
				<div id="body-layout">
					
						<div id="naviagation-layout" class="roundbox boxshadow">
							<tiles:insertAttribute name="navigation" />
						</div>
					
					
					<div id="content-layout" class="roundbox boxshadow">
						<tiles:insertAttribute name="body" />
					</div>
					
				</div>
			<!--  	<div id="footer-layout">
					<tiles:insertAttribute name="footer" />
				</div> -->
			</div>
		
		<!-- 
			<table style="border-collapse: collapse;" align="center" border="0" cellpadding="2" cellspacing="2" width="100%" height="100%">    
				<tbody>
					<tr>
	        			<td colspan="2" height="5%">
	        				<tiles:insertAttribute name="header"/>
	        			</td>
	    			</tr>
	    			<tr>
	        			<td height="90%" valign="top" width="50%">
	         				<tiles:insertAttribute name="navigation" />
	        			</td>
	        			<td valign="top" width="50%">
	         				<tiles:insertAttribute name="body" />
	        			</td>
	    			</tr>
	    			<tr>
	        			<td colspan="2" height="5%">
	         				<tiles:insertAttribute name="footer" />
	        			</td>
	    			</tr>
				</tbody>
			</table>
			 -->
		</body>
	</html>
</jsp:root>