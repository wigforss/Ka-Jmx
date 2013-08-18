function getCurrentDate() {
	return new Date().toJSON().replace("T", " ").replace("Z","");
}

function appendToArea(data, areaId) {
	$('#'+areaId).val(getCurrentDate() + ':\n' + data + '\n\n' + $('#'+areaId).val())
}

function validateAndPostOperationForm(formId, areaId) {
	var formValidate = $("#"+formId).validate(true);
 	formValidate.form();
 	if(formValidate.numberOfInvalids() == 0) {
		$('#'+areaId).removeClass('hidden');
		$.post('json/bean/operation/invoke', $('#'+formId).serialize(), function(data) {appendToArea(data, areaId)});
	}
}

function toggleClass(id, cssClass, time) {
	if(time && time > 0) {
		setTimeout(function(){$('#'+id).toggleClass(cssClass)}, time);
	} else {
		$('#refresh-${attribute.name}').toggleClass(cssClass);	
	}
}

function unsubscribeAutoRefresh() {
	var currentBean = document.getElementById('refresh-bean-name');
	for (var key in org.kasource.Websocket.listeners) {
	    if (org.kasource.Websocket.listeners.hasOwnProperty(key)) {
	    	if(key.indexOf(currentBean) > -1) {
	    		
	    	}
	    }
	}
}

function toggleSubscription(objectName, attribute) {
	var buttonId ="refresh-"+attribute;
	var refreshValueId = "refresh-value-"+attribute;
	var valueId="value-"+attribute;
	var isStopState = $('#'+buttonId).hasClass('stopRefresh');
	if(isStopState) {
		org.kasource.Websocket.unsubscribe(objectName, attribute, buttonId);
	} else {
		org.kasource.Websocket.subscribe(objectName, attribute, buttonId, function(jmxValue){$('#'+refreshValueId).text(jmxValue.value);});
	}
	toggleClass(buttonId, 'stopRefresh', 500);
	toggleClass(refreshValueId, 'hidden', 500);
	toggleClass(valueId, 'hidden', 500)
}