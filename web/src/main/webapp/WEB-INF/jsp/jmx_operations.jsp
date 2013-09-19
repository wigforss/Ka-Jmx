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
				 <c:when test="${fn:length(mBean.operations) > 0}"> 
				 	<div id="operations">
						<p/>
						<h2>Operations</h2>
					<!--  	<c:forEach var="operation" items="${mBean.operations}">
							<a id="a-operation-${operation.name}" href="#invokeForm-${operation.name}"><c:out value="${operation.name}"/></a><br/> 
						</c:forEach> -->		
					 	<c:forEach var="operation" items="${mBean.operations}" varStatus="operationStatus">
					 		<a id="a-operation-${operation.name}" href="javascript:toggleHidden('operation-${operationStatus.index}')"><c:out value="${operation.name}"/></a><br/>
					 		<div id="operation-${operationStatus.index}" class="hidden">
							<form id="invokeForm-${operation.name}" action="${pageContext.request.contextPath}/json/bean" method="post">
								<input type="hidden" name="objectName" value="${fn:escapeXml(mBean.name)}"/>
								<input type="hidden" name="operationName" value="${operation.name}"/>
								<h3 id="operation-${operation.name}">
									<i>
										<c:choose>
											<c:when test="${not empty operation.typeJavaDocUrl}">
												<a href="${operation.typeJavaDocUrl}" target="javadoc"><c:out value="${operation.returnType}"/></a>
											</c:when>
											<c:otherwise>
												<c:out value="${operation.returnType}"/>
											</c:otherwise>
										</c:choose>
										
									</i>
									<c:choose>
										<c:when test="${not empty mBean.typeJavaDocUrl}">
												<a href="${mBean.typeJavaDocUrl}#${operation.name}" target="javadoc"><c:out value=" ${operation.name}"/></a>
											</c:when>
											<c:otherwise>
												<c:out value=" ${operation.name}"/>
										</c:otherwise>
									</c:choose>
									
								</h3>
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
													<td>
														<c:choose>
															<c:when test="${not empty parameter.typeJavaDocUrl}">
																<a href="${parameter.typeJavaDocUrl}" target="_blank"><c:out value="${parameter.type}"/></a>
															</c:when>
															<c:otherwise>
																<c:out value="${parameter.type}"/>
															</c:otherwise>
														</c:choose>
														
													</td>
													<td>
														<c:choose>
															<c:when test="${parameter.enumeration}">
																<select name="${parameter.name}">
																	<c:forEach var="enumeratedValue" items="${parameter.enumeratedValues}">
																		<option value="${enumeratedValue}">
																			<c:out value="${enumeratedValue}" />
																		</option>
																	</c:forEach>
																</select>
															</c:when>
														<c:when test="${fn:toLowerCase(parameter.type) == 'boolean'}">
															<input type="checkbox" name="${parameter.name}" required="true"/>
														</c:when>
														<c:when test="${fn:toLowerCase(parameter.type) == 'long'}">
															<input type="text" name="${parameter.name}" size="20" 
																   integer="true" min="-9.22337203685478E18"
																   max="9.22337203685478E18" required="true"/>
														</c:when>
														<c:when test="${parameter.type == 'int' or parameter.type == 'Integer'}">
															<input type="text" name="${parameter.name}" size="11" 
																   integer="true" min="-2147483648" max="2147483647" required="true"/>
														</c:when>
														<c:when test="${fn:toLowerCase(parameter.type) == 'short'}">
															<input type="text" name="${parameter.name}" size="5" 
																   integer="true" min="-32768" max="32767" required="true" />
														</c:when>
														<c:when test="${fn:toLowerCase(parameter.type) == 'byte'}">
															<input type="text" name="${parameter.name}" size="3" 
																   integer="true" min="-128" max="127" required="true"/>
														</c:when>
														<c:when test="${fn:toLowerCase(parameter.type) == 'char'}">
															<input type="text" name="${parameter.name}" size="1" 
																   minLength="1" maxLength="1" required="true"/>
														</c:when>
														<c:when test="${fn:toLowerCase(parameter.type) == 'float'}">
															<input type="text" name="${parameter.name}" size="34" 
												    			   number="true" min="1.4E-45" max="3.4028235E38" required="true"/>
														</c:when>
														<c:when test="${fn:toLowerCase(parameter.type) == 'double'}">
															<input type="text" name="${parameter.name}" size="66" 
														           number="true" min="4.9E-324" max="1.7976931348623157E308" required="true"/>
														</c:when>
														<c:otherwise>
															<input type="text" name="${parameter.name}" style="width: 100%" required="true" />
														</c:otherwise>
													</c:choose>
														
													</td>
													
												</tr>
											</c:forEach>
									</tbody>
								</table>
								</c:if>
							<p/>
							<input type="button" value="Invoke ${operation.name}" onclick="validateAndPostOperationForm('invokeForm-${operation.name}','result-${operation.name}')"/>
							<img alt="Show/Hide Result" title="Show/Hide Result" src="style/images/run-build-file-3.png" onclick="$('#result-${operation.name}').toggleClass('hidden')"/>
							<br/>
							<textarea class="hidden" rows="20" cols="80" id="result-${operation.name}" readonly="readonly"><c:out value=""/></textarea>
						</form>
						</div>
						</c:forEach> 
						
						</div>
				  	</c:when>
				  	<c:otherwise>
				  		<div id="operations"><c:out value=""/></div>
				  	</c:otherwise>   
			</c:choose>
			
</jsp:root>