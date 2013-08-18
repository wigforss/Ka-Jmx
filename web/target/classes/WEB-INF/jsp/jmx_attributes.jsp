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
	<c:when test="${fn:length(mBean.attributes) > 0}">
		<div id="attributes">
		<p />
		<h2>Attributes</h2>
		<form id="setAttributesForm"
			action="${pageContext.request.contextPath}/html/bean/attributes"
			method="post">
			<input type="hidden" name="objectName" value="${fn:escapeXml(mBean.name)}"/> 
		<table  class="bordered">
			<thead>
				<tr>
					<th>Name</th>
					<th class="tableDescColumn">Description</th>
					<th>Type</th>
					<th>Value</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="attribute" items="${mBean.attributes}" varStatus="attributeStatus">
					<c:if test="${attribute.readable}">
						<tr >
							<td><c:out value="${attribute.name}" /></td>
							<td class="tableDescColumn"><c:out
								value="${attribute.description}" /></td>
							<td>
								<c:choose>
									<c:when test="${not empty attribute.typeJavaDocUrl}">
										<a href="${attribute.typeJavaDocUrl}" target="javadoc"><c:out value="${attribute.type}" /></a>
									</c:when>
									<c:otherwise>
										<c:out value="${attribute.type}" />
									</c:otherwise>
								</c:choose>
								
							</td>
							
							<td id="attribute-${attribute.name}" tabindex="${attributeStatus.index + 1}">
								<input id="refresh-${attribute.name}" type="button" title="Auto refresh value" value="" class="refreshButton" onclick="toggleSubscription('${fn:escapeXml(mBean.name)}','${attribute.name}')"/>
								<span id="refresh-value-${attribute.name}" class="hidden"><c:out value="${attributeValues[attribute.name]}"/></span>
								<span id="value-${attribute.name}">
								<c:choose>
									<c:when test="${attribute.writable}">
										<c:choose>
											<c:when test="${attribute.enumeration}">
												<select name="${attribute.name}">
												<c:forEach var="enumeratedValue"
													items="${attribute.enumeratedValues}">
													<option value="${enumeratedValue}"
														selected="${enumeratedValue == attributeValues[attribute.name] ? 'true' : 'false'}"><c:out
														value="${enumeratedValue}" /></option>
												</c:forEach>
											</select>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'boolean'}">
											<input type="checkbox" name="${attribute.name}"
												checked="${attributeValues[attribute.name]}" />
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'long'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="20" 
												integer="true" min="-9.22337203685478E18"
												max="9.22337203685478E18" required="true"/>
										</c:when>
										<c:when
											test="${attribute.type == 'int' or attribute.type == 'Integer'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="11" 
												integer="true" min="-2147483648" max="2147483647" required="true"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'short'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="5" 
												integer="true" min="-32768" max="32767" required="true"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'byte'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="3" 
												integer="true" min="-128" max="127" required="true"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'char'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="1" 
												minLength="1" maxLength="1" required="true"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'float'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="34" 
												number="true" min="1.4E-45" max="3.4028235E38" required="true"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'double'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="66" 
												number="true" min="4.9E-324" max="1.7976931348623157E308" required="true"/>
										</c:when>
										<c:otherwise>
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" 
												style="width: 100%" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									
											<c:out value="${attributeValues[attribute.name]}"/>
										
								</c:otherwise>
								
							</c:choose>
								</span>
							</td>
								

						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
		<p />
		<input id="saveAttributesButton" type="button" value="Save" onclick="saveValues()"/>
		</form>

		<script type="text/javascript"><![CDATA[
			 $("#setAttributesForm").validate(true);
			 
			 function saveValues() {
			 	var validator = $("#setAttributesForm").validate(true);
			 	validator.form();
			 	if(validator.numberOfInvalids() == 0) {
			 		Spring.remoting.submitForm("saveAttributesButton", "setAttributesForm", { fragments: "jmx_attributes"});
			 	} else {
			 		$("#setAttributesForm").toggleClass('error');
			 		setTimeout(function(){$("#setAttributesForm").toggleClass('error')}, 500);
			 	}
			 }
		]]></script>
		</div>
	</c:when>
	<c:otherwise>
		<div id="attributes"><c:out value=""/></div>
	</c:otherwise>
	</c:choose>
</jsp:root>