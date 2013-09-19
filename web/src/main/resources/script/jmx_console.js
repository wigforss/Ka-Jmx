function getCurrentDate() {
	return new Date().toJSON().replace("T", " ").replace("Z","");
}

function appendToArea(data, areaId) {
	$('#'+areaId).val(getCurrentDate() + ':\n' + data + '\n\n' + $('#'+areaId).val())
}

function toggleHidden(elementId) {
	$('#'+elementId).toggleClass('hidden');
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

var jmx = {};
jmx.Charts = {};
jmx.Timer = {};

function toggleSubscription(objectName, attribute) {
	var buttonId ="refresh-"+attribute;
	var refreshValueId = "refresh-value-"+attribute;
	var valueId="value-"+attribute;
	var isStopState = $('#'+buttonId).hasClass('stopRefresh');
	if(isStopState) {
		org.kasource.Websocket.unsubscribe(objectName, attribute, buttonId);
		if($('#'+refreshValueId).attr('graphable') == 'true') {
			delete jmx.Charts[attribute];
		}
	} else {
		org.kasource.Websocket.subscribe(objectName, attribute, buttonId, onJmxAttributeValue);
		if($('#'+refreshValueId).attr('graphable') == 'true') {
			 
			var decimals = 2;
			if($('#'+refreshValueId).attr('integerValue') == 'true') {
				decimals=0;
			}
			var chart = new SmoothieChart({maxValueScale:1.01,interpolation:'linear',grid:{verticalSections:5},labels:{precision: decimals},timestampFormatter:SmoothieChart.timeFormatter});
			jmx.Charts[attribute] = new TimeSeries();
			chart.addTimeSeries(jmx.Charts[attribute], {
														lineWidth:3,
														strokeStyle:'rgb(0, 255, 0)',
														fillStyle:'rgba(0, 255, 0, 0.4)'
													   });
			chart.streamTo(document.getElementById('chart-'+attribute));
		}
	}
	toggleClass(buttonId, 'stopRefresh', 500);
	toggleClass(refreshValueId, 'hidden', 500);
	toggleClass(valueId, 'hidden', 500)
}

function onJmxAttributeValue(jmxValue) {
	var attribute = jmxValue.key.attributeName;
	var chartData = jmx.Charts[attribute];
	
	if(chartData) {
		var timerTask = jmx.Timer[attribute];
		if(timerTask) {
			window.clearInterval(timerTask);
		}
		chartData.append(new Date().getTime(), jmxValue.value);
		jmx.Timer[attribute] = setInterval(function(){chartData.append(new Date().getTime(), jmxValue.value)},500);
	} else {	
		$('#refresh-value-'+attribute).text(jmxValue.value);	  	
	}
}