if(typeof org=='undefined') {
	org = {};
}
if(!org.hasOwnProperty("kasource")) {
	org.kasource = {};
}
if(!(org.kasource.hasOwnProperty("jmx"))) {
	org.kasource.jmx = {};
} 
if(!(org.kasource.jmx.hasOwnProperty("console"))) {
	org.kasource.jmx.console = {};
} 

org.kasource.jmx.console.getCurrentDate = function() {
	return new Date().toJSON().replace("T", " ").replace("Z","");
}

org.kasource.jmx.console.appendToArea = function(data, areaId) {
	$('#'+areaId).val(org.kasource.jmx.console.getCurrentDate() + ':\n' + data + '\n\n' + $('#'+areaId).val())
}

org.kasource.jmx.console.toggleHidden = function(elementId) {
	$('#'+elementId).toggleClass('hidden');
}

org.kasource.jmx.console.validateAndPostOperationForm = function(formId, areaId) {
	var formValidate = $("#"+formId).validate(true);
 	formValidate.form();
 	if(formValidate.numberOfInvalids() == 0) {
		$('#'+areaId).removeClass('hidden');
		$.post('json/bean/operation/invoke', $('#'+formId).serialize(), function(data) {org.kasource.jmx.console.appendToArea(data, areaId)});
	}
}

org.kasource.jmx.console.toggleClass = function(id, cssClass, time) {
	if(time && time > 0) {
		setTimeout(function(){$('#'+id).toggleClass(cssClass)}, time);
	} else {
		$('#refresh-${attribute.name}').toggleClass(cssClass);	
	}
}

org.kasource.jmx.console.unsubscribeAutoRefresh = function() {
	var currentBean = document.getElementById('refresh-bean-name');
	for (var key in org.kasource.Websocket.listeners) {
	    if (org.kasource.Websocket.listeners.hasOwnProperty(key)) {
	    	if(key.indexOf(currentBean) > -1) {
	    		
	    	}
	    }
	}
}

org.kasource.jmx.console.Charts = {};
org.kasource.jmx.console.Timer = {};


org.kasource.jmx.console.toggleSubscription = function(objectName, attribute) {
	var buttonId ="refresh-"+attribute;
	var refreshValueId = "refresh-value-"+attribute;
	var valueId="value-"+attribute;
	var isStopState = $('#'+buttonId).hasClass('stopRefresh');
	if(isStopState) {
		org.kasource.Websocket.unsubscribe(objectName, attribute, buttonId);
		if($('#'+refreshValueId).attr('graphable') == 'true') {
			delete org.kasource.jmx.console.Charts[attribute];
		}
	} else {
		org.kasource.Websocket.subscribe(objectName, attribute, buttonId, org.kasource.jmx.console.onJmxAttributeValue);
		if($('#'+refreshValueId).attr('graphable') == 'true') {
			 
			var decimals = 2;
			if($('#'+refreshValueId).attr('integerValue') == 'true') {
				decimals=0;
			}
			var chart = new SmoothieChart({maxValueScale:1.01,interpolation:'linear',grid:{verticalSections:5},labels:{precision: decimals},timestampFormatter:SmoothieChart.timeFormatter});
			org.kasource.jmx.console.Charts[attribute] = new TimeSeries();
			chart.addTimeSeries(org.kasource.jmx.console.Charts[attribute], {
														lineWidth:3,
														strokeStyle:'rgb(0, 255, 0)',
														fillStyle:'rgba(0, 255, 0, 0.4)'
													   });
			chart.streamTo(document.getElementById('chart-'+attribute));
		}
	}
	org.kasource.jmx.console.toggleClass(buttonId, 'stopRefresh', 500);
	org.kasource.jmx.console.toggleClass(refreshValueId, 'hidden', 500);
	org.kasource.jmx.console.toggleClass(valueId, 'hidden', 500)
}

org.kasource.jmx.console.onJmxAttributeValue = function(jmxValue) {
	var attribute = jmxValue.key.attributeName;
	var chartData = org.kasource.jmx.console.Charts[attribute];
	
	if(chartData) {
		var timerTask = org.kasource.jmx.console.Timer[attribute];
		if(timerTask) {
			window.clearInterval(timerTask);
		}
		chartData.append(new Date().getTime(), parseFloat(jmxValue.value));
		org.kasource.jmx.console.Timer[attribute] = setInterval(function(){chartData.append(new Date().getTime(), parseFloat(jmxValue.value))},500);
	} else {	
		$('#refresh-value-'+attribute).text(jmxValue.value);	  	
	}
}