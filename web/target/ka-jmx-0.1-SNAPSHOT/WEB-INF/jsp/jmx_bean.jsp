<jsp:root version="1.2" xmlns="http://www.w3c.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:tiles="http://tiles.apache.org/tags-tiles">   
	
	
	

	<jsp:directive.page contentType="text/html" />
	<jsp:directive.page isELIgnored="false" />
   
		<!--   <c:if test="${not empty mBean}"> --> 	
			<h1 id="bean-title"><c:out value="${mBean.name}"/></h1>
		  	<h2 id="bean-class">
		  		<c:choose>
		  			<c:when test="${not empty mBean.typeJavaDocUrl}">
		  				Class: <a href="${mBean.typeJavaDocUrl}" target="javadoc"><c:out value="${mBean.type}"/></a>
		  			</c:when>
		  			<c:otherwise>
		  				Class: <c:out value="${mBean.type}"/>
		  			</c:otherwise>
		  		</c:choose>
		  		
		  	</h2>
			<span id="bean-description"><c:out value="${mBean.description}"/></span> 
		<!--< /c:if>-->
		
</jsp:root>