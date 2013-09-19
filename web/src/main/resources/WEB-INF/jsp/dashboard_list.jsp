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
	
	<c:when test="${not empty dashboards}"> 
		<div id="dashboards" class="list-menu">
		<h2>Dashboards</h2>
			<ul>
			<c:forEach var="dashboard" items="${dashboards}">
				<li>
					<a href="javascript:loadDashboard('${dashboard.id}')"><c:out value="${dashboard.name}"/></a>
				</li>
			</c:forEach>
			</ul>
		</div>
	</c:when>
	<c:otherwise>
		<div id="dashboards"><c:out value=""/></div>
	</c:otherwise>
	</c:choose>

</jsp:root>