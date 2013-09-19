<jsp:root version="1.2" xmlns="http://www.w3c.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:tiles="http://tiles.apache.org/tags-tiles">
	

    <jsp:output omit-xml-declaration="yes"/>
	<jsp:directive.page contentType="text/html" /> 
	<jsp:directive.page isELIgnored="false" />
   
	<c:choose>
	<c:when test="${not empty tree.root.children}">
		
			<div class="css-treeview" id="tree-view">
				<ul>
					<c:forEach var="domain" items="${tree.root.children}">
						<li>
							<c:choose>
								<c:when test="${domain.expand}">
									<input type="checkbox" id="domain-${fn:escapeXml(domain.label)}" checked="checked"/>
								</c:when>
								<c:otherwise>
									<input type="checkbox" id="domain-${fn:escapeXml(domain.label)}"/>
								</c:otherwise>
							</c:choose>
        					<label for="domain-${fn:escapeXml(domain.label)}" nodeType="folder"><c:out value="${domain.label}"/></label>
        					<ul>
        						<c:forEach var="bean" items="${domain.children}">
        							<li>
        								<c:choose>
        									<c:when test="${bean.expand}">
        										<input type="checkbox" id="tree-bean-${fn:escapeXml(bean.label)}" checked="checked"/>
        									</c:when>
        									<c:otherwise>
        										<input type="checkbox" id="tree-bean-${fn:escapeXml(bean.label)}" />
        									</c:otherwise>
        								</c:choose>
        								<label for="tree-bean-${fn:escapeXml(bean.label)}" nodeType="object"><a href="javascript:loadBean('${fn:escapeXml(bean.name)}')"><c:out value="${bean.label}" escapeXml="false"/></a></label>
        								<ul>
        									<c:forEach var="content" items="${bean.children}">
        										<li>
        											<input type="checkbox" id="content-${content.label}" checked="true"/><label for="content-${content.label}" nodeType="folder"><c:out value="${content.label}"/></label>
        											<c:if test="${content.label == 'Attributes'}">
        											<ul>
        												<c:forEach var="attribute" items="${content.children}">
        													<li>
        														<label id="tree-attribute-${fn:escapeXml(attribute.label)}" nodeType="attribute"><a href="javascript:focus('${fn:escapeXml(bean.name)}','attribute-${fn:escapeXml(attribute.label)}')"><c:out value="${attribute.label}" escapeXml="false"/></a></label>
        													</li>
        												</c:forEach>
        											</ul>
        										</c:if>
        										<c:if test="${content.label == 'Notifications'}">
        											<ul>
        												<c:forEach var="notification" items="${content.children}">
        													<li>
        														<label id="tree-notification-${fn:escapeXml(notification.label)}" nodeType="notification"><a href="javascript:focus('${fn:escapeXml(bean.name)}','notification-${notification.label}')"><c:out value="${notification.label}" escapeXml="false"/></a></label>
        													</li>
        												</c:forEach>
        											</ul>
        										</c:if>
        										<c:if test="${content.label == 'Operations'}">
        												<ul>
        												<c:forEach var="operation" items="${content.children}">
        													<li>
        														<label id="tree-operation-${fn:escapeXml(operation.label)}" nodeType="operation"><a href="javascript:focus('${fn:escapeXml(bean.name)}','a-operation-${fn:escapeXml(operation.label)}')"><c:out value="${operation.label}" escapeXml="false"/></a></label>
        													</li>
        												</c:forEach>
        											</ul>
        										</c:if>
        										</li>
        									</c:forEach>
        								</ul>
        							</li>
        						</c:forEach>
        					</ul>
        				</li>
					</c:forEach>
				</ul>
			</div>
	</c:when>
	<c:otherwise>
		<div id="tree-view"><c:out value=""/></div>
	</c:otherwise>
	</c:choose>

</jsp:root> 