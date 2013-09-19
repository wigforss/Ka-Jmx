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
	
	<c:when test="${fn:length(mBean.notifications) > 0}"> 
		<div id="notifications">
		<p />
		<h2>Notifications</h2>
		<table class="bordered">
			<thead>
				<th>Name</th>
				<th class="tableDescColumn">Description</th>
				<th>Notify Types</th>
			</thead>
			<tbody>
				<c:forEach var="notification" items="${mBean.notifications}" varStatus="notificationStatus">
					<tr >
						<td><c:out value="${notification.name}" /></td>
						<td class="tableDescColumn"><c:out
							value="${notification.description}" /></td>
						<td id="notification-${notification.name}" tabindex="${notificationStatus.index + 1}"><c:forEach var="notifyType"
							items="${notification.notifyTypes}" varStatus="notifyTypeStatus">
							<c:if test="${notifyTypeStatus.index > 1}">
													,<br />
							</c:if>
							<c:out value="${notifyType}" />
						</c:forEach></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</c:when>
	<c:otherwise>
		<div id="notifications"><c:out value=""/></div>
	</c:otherwise>
	</c:choose>

</jsp:root>