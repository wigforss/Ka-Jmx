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
			action="json/bean/attributes"
			method="post">
			<input type="hidden" name="objectName" value="${fn:escapeXml(mBean.name)}"/> 
		<table  class="bordered">
			<thead>
				<tr>
					<th>Name</th>
					<th class="tableDescColumn">Description</th>
					<th>Type</th>
					<th ><span title="Auto refresh">A</span></th>
					<th>Value</th>
					<th><span title="Save value">Save</span></th>
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
							<td>
							<input id="refresh-${attribute.name}" type="button" title="Auto refresh value" value="" class="refreshButton" onclick="toggleSubscription('${fn:escapeXml(mBean.name)}','${attribute.name}')"/>
								
							</td>
							
							<td id="attribute-${attribute.name}" tabindex="${attributeStatus.index + 1}">
							
								<c:set var="graphable" value="${fn:toLowerCase(attribute.type) == 'long' or attribute.type == 'int' or attribute.type == 'Integer' or fn:toLowerCase(attribute.type) == 'short' or fn:toLowerCase(attribute.type) == 'byte' or fn:toLowerCase(attribute.type) == 'float' or fn:toLowerCase(attribute.type) == 'double' ? 'true' : 'false'}"/>
								<c:set var="integerValue" value="${fn:toLowerCase(attribute.type) == 'long' or attribute.type == 'int' or attribute.type == 'Integer' or fn:toLowerCase(attribute.type) == 'short' or fn:toLowerCase(attribute.type) == 'byte' ? 'true' : 'false'}"/>
							   <span id="refresh-value-${attribute.name}" class="hidden" graphable="${graphable}" integerValue="${integerValue}">
									<c:choose>
										<c:when test="${graphable}">
											<canvas id="chart-${attribute.name}" width="500" height="100" class="boxshadow"/>
										</c:when>
										<c:otherwise>
											<c:out value="${attributeValues[attribute.name]}"/>
										</c:otherwise>
									</c:choose>
									
								</span>	
								<span id="value-${attribute.name}">
								<c:choose>
									<c:when test="${attribute.writable}">
										<c:choose>
											<c:when test="${attribute.enumeration}">
												<select name="${attribute.name}" onchange="$('#changedId-${attribute.name}').prop('checked',true)">
													<option value=""></option>
												<c:forEach var="enumeratedValue"
													items="${attribute.enumeratedValues}">
													
													<c:choose>
														<c:when test="${enumeratedValue == attributeValues[attribute.name]}">
															<option value="${enumeratedValue}" selected="selected"><c:out value="${enumeratedValue}"/></option>
														</c:when>
														<c:otherwise>
															<option value="${enumeratedValue}"><c:out value="${enumeratedValue}"/></option>
														</c:otherwise>
														
													</c:choose>
														
												</c:forEach>
											</select>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'boolean'}">
											<c:choose>
											<c:when test="${attributeValues[attribute.name]}">
												<input type="checkbox" name="${attribute.name}" checked="checked"
												onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
											</c:when>
											<c:otherwise>
												<input type="checkbox" name="${attribute.name}"
												onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
											</c:otherwise>
											</c:choose>
										
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'long'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="20" 
												integer="true" min="-9.22337203685478E18"
												max="9.22337203685478E18" required="true"
												onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
										</c:when>
										<c:when
											test="${attribute.type == 'int' or attribute.type == 'Integer'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="11" 
												integer="true" min="-2147483648" max="2147483647" required="true"
												onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'short'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="5" 
												integer="true" min="-32768" max="32767" required="true"
												onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'byte'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="3" 
												integer="true" min="-128" max="127" required="true"
												onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'char'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="1" 
												minLength="1" maxLength="1" required="true"
												onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'float'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="34" 
												number="true" min="1.4E-45" max="3.4028235E38" required="true"
												onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'double'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="66" 
												number="true" min="4.9E-324" max="1.7976931348623157E308" required="true"
												onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
										</c:when>
										<c:when test="${fn:toLowerCase(attribute.type) == 'date' or fn:toLowerCase(attribute.type) == 'java.sql.Date' or fn:toLowerCase(attribute.type) == 'java.sql.Timestamp'}">
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" size="13" 
												required="true" onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
										</c:when>
										<c:otherwise>
											<input type="text" name="${attribute.name}"
												value="${attributeValues[attribute.name]}" 
												style="width: 100%" onchange="$('#changedId-${attribute.name}').prop('checked',true)"/>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									
											<c:out value="${attributeValues[attribute.name]}"/>
										
								</c:otherwise>
								
							</c:choose>
								</span>
								
							</td>
							<td>
								<c:choose>
									<c:when test="${attribute.writable}">
										<input type="checkbox" name="changed-${attribute.name}" id="changedId-${attribute.name}"/>
									</c:when>
									<c:otherwise>
										<input type="checkbox" disabled="disabled"/>
									</c:otherwise>
								</c:choose>
								
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
			 var attributeValidator = $("#setAttributesForm").validate(true);
			 
			 function saveValues() {
			 	var validator = $("#setAttributesForm").validate(true);
			 	validator.form();
			 	if(validator.numberOfInvalids() == 0) {
			 	    
			 		$.post('json/bean/attributes', $('#setAttributesForm').serialize(), handleSaveResponse);
			 		//Spring.remoting.submitForm("saveAttributesButton", "setAttributesForm", { fragments: "jmx_attributes"});
			 	} else {
			 		$("#setAttributesForm").toggleClass('error');
			 		setTimeout(function(){$("#setAttributesForm").toggleClass('error')}, 500);
			 	}
			 }
			 
			 function handleSaveResponse(data) {
			 	 if(data.saved) {
			 	 	for(var i = 0; i < data.saved.length; i++) {
			 	 		var attributeName = data.saved[i];
			 	 		$('#changedId-'+attributeName).prop('checked', false);
			 	 	}
			 	 }
			 	 if(data.errors) {
			 	 	for (var key in data.errors) {
	    				if (data.errors.hasOwnProperty(key)) {
	    					var error = {};
	    					error[key] = data.errors[key];
	    					attributeValidator.showErrors(error);
	    				}
	    			}
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