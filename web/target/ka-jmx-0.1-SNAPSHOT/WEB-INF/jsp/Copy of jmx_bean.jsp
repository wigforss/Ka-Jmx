<jsp:root version="1.2" xmlns="http://www.w3c.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:tiles="http://tiles.apache.org/tags-tiles">   
	
	
	

	<jsp:directive.page contentType="text/html" />
	<jsp:directive.page isELIgnored="false" />
     
	

	 
		
   	
		<!--  	<c:if test="${not empty mBean}"> -->		
				<h1 id="bean-title"><c:out value="${mBean.name}"/></h1>
				<h2>Class: <c:out value="${mBean.className}"/></h2>
				<c:out value="${mBean.description}"/>
			<!-- 	<c:if test="${fn:length(mBean.attributes) > 0}"> -->
					<p/>
					<h2>Attributes</h2>
						<form id="setAttributesForm" action="${pageContext.request.contextPath}/json/attributes" method="post" >
							<table class="bordered">
								<thead>
									<tr>
										<th>Name</th>
										<th class="tableDescColumn">Description</th>
										<th>Type</th>
										<th>Value</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="attribute" items="${mBean.attributes}">
										<c:if test="${attribute.readable}">
											<tr id="attribute-${attribute.name}">
												<td><c:out value="${attribute.name}"/></td>
												<td class="tableDescColumn"><c:out value="${attribute.description}"/></td>
												<td><c:out value="${attribute.type}"/></td>
												<c:choose>
													<c:when test="${attribute.writable}">
														<td>
															
															<c:choose>
																<c:when test="${attribute.enumeration}">
																	<select name="${attribute.name}">
																		<c:forEach var="enumeratedValue" items="${attribute.enumeratedValues}">
																			<option value="${enumeratedValue}" selected="${enumeratedValue == attributeValues[attribute.name] ? 'true' : 'false'}"><c:out value="${enumeratedValue}"/></option>
																		</c:forEach>
																	</select>
																</c:when>
																<c:when test="${fn:toLowerCase(attribute.type) == 'boolean'}">
																	<input type="checkbox" name="${attribute.name}" checked="${attributeValues[attribute.name]}"/>
																</c:when>
																<c:when test="${fn:toLowerCase(attribute.type) == 'long'}">
																	<input type="text" name="${attribute.name}" value="${attributeValues[attribute.name]}" size="20" integer="true" min="-9.22337203685478E18" max="9.22337203685478E18"/>
																</c:when>
																<c:when test="${attribute.type == 'int' or attributeType == 'Integer'}">
																	<input type="text" name="${attribute.name}" value="${attributeValues[attribute.name]}" size="11" integer="true" min="-2147483648" max="2147483647"/>
																</c:when>
																<c:when test="${fn:toLowerCase(attribute.type) == 'short'}">
																	<input type="text" name="${attribute.name}" value="${attributeValues[attribute.name]}" size="5"  integer="true" min="-32768" max="32767"/>
																</c:when>
																<c:when test="${fn:toLowerCase(attribute.type) == 'byte'}">
																	<input type="text" name="${attribute.name}" value="${attributeValues[attribute.name]}" size="3" integer="true" min="-128" max="127"/>
																</c:when>
																<c:when test="${fn:toLowerCase(attribute.type) == 'char'}">
																	<input type="text" name="${attribute.name}" value="${attributeValues[attribute.name]}" size="1" minLength="1" maxLength="1"/>
																</c:when>
																<c:when test="${fn:toLowerCase(attribute.type) == 'float'}">
																	<input type="text" name="${attribute.name}" value="${attributeValues[attribute.name]}" size="34" number="true" min="1.4E-45" max="3.4028235E38"/>
																</c:when>
																<c:when test="${fn:toLowerCase(attribute.type) == 'double'}">
																	<input type="text" name="${attribute.name}" value="${attributeValues[attribute.name]}" size="66" number="true" min="4.9E-324" max="1.7976931348623157E308"/>
																</c:when> 
																<c:otherwise>
																	<input type="text" name="${attribute.name}" value="${attributeValues[attribute.name]}" style="width:100%"/>
																</c:otherwise>
															</c:choose>
															
														</td>
													</c:when>
													<c:otherwise>
														<td><c:out value="${attributeValues[attribute.name]}"/></td>
													</c:otherwise>
												</c:choose>
												
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
							<p/>
							<input type="button" value="Save"/>
						</form>
				<!-- </c:if> -->
				<!-- <c:if test="${fn:length(mBean.operations) > 0}"> -->
						<p/>
						<h2>Operations</h2>
						<c:forEach var="operation" items="${mBean.operations}">
							<c:out value="${operation.name}"/>
						</c:forEach>
					<!--  	<c:forEach var="operation" items="${mBean.operations}">
							<form id="invokeOperationForm" action="${pageContext.request.contextPath}/json/bean" method="post">
								<input type="hidden" name="objectName" value="${fn:escapeXml(mBean.name)}"/>
								<input type="hidden" name="operationName" value="${operation.name}"/>
								<h3 id="operation-${operation.name}"><c:out value="${operation.returnType}"/>&#160;<c:out value="${operation.name}"/></h3>
								<c:if test="${fn:length(operation.signature) > 0}">
								<h3>Parameters</h3>
									<table class="bordered">
										<thead>
											<tr>
												<th>Name</th>
												<th class="tableDescColumn">Description</th>
												<th>Type</th>
												<th>Value</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="parameter" items="${operation.signature}">
												<tr>
													<td><c:out value="${parameter.name}"/></td>
													<td class="tableDescColumn"><c:out value="${parameter.description}"/></td>
													<td><c:out value="${parameter.type}"/></td>
													<td><input type="text" name="${parameter.name}"/></td>
												</tr>
											</c:forEach>
									</tbody>
								</table>
								</c:if>
							<p/>
							<input type="button" value="Invoke ${operation.name}"/>
							<textarea rows="20" cols="80" id="result-${operation.name}"></textarea>
						</form>
						</c:forEach> -->
						
				<!--  	</c:if> -->  
				<!--  	<c:if test="${fn:length(mBean.notifications) > 0}"> -->
						<p/>
						<h3>Notifications</h3>
						<table class="bordered">
							<thead>
								<th>Name</th>
								<th class="tableDescColumn">Description</th>
								<th>Notify Types</th>
							</thead>
							<tbody>
								<c:forEach var="notification" items="${mBean.notifications}">
									<tr id="notification-${notification.name}">
										<td><c:out value="${notification.name}"/></td>
										<td class="tableDescColumn"><c:out value="${notification.description}"/></td>
										<td>
											<c:forEach var="notifyType" items="${notification.notifyTypes}" varStatus="notifyTypeStatus">
												<c:if test="${notifyTypeStatus.index > 1}">
													,<br/>
												</c:if>
												<c:out value="${notifyType}"/>
											</c:forEach>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
				<!--  	</c:if> -->	
		<!--  	</c:if> -->
		<script type="text/javascript">
 		<![CDATA[
 		         var validator = $("#setAttributesForm").validate(true);
		]]>         
 		</script>
</jsp:root>