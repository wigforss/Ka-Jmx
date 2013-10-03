if(typeof org=='undefined') {
	org = {};
}
if(!org.hasOwnProperty("kasource")) {
	org.kasource = {};
}
if(!(org.kasource.hasOwnProperty("jmx"))) {
	org.kasource.jmx = {};
} 
if(!(org.kasource.jmx.hasOwnProperty("dashboard"))) {
	org.kasource.jmx.dashboard = {};
} 


org.kasource.jmx.dashboard.dashboards={};


org.kasource.jmx.dashboard.loadDashboard = function(dashboardId) {
	org.kasource.jmx.dashboard.currentDasboard = dashboardId;
	
	$("#dashboardView").removeClass("hidden");
	$("#beanView").addClass("hidden");
	// Hide all dashboards
	$("#dashboardView").children("div").addClass("hidden");
	if($("#"+dashboardId).find('div:nth-child(2)').is(':empty')) {
		// load dashboard
		$.get(encodeURI('json/dashboard'), {dashboardId: dashboardId}, org.kasource.jmx.dashboard.renderDashboard);
	} else {
		// unhide render
		$("#"+dashboardId).removeClass("hidden");
		
		for (var key in org.kasource.jmx.dashboard.widgets) {
		    if (org.kasource.jmx.dashboard.widgets[key] 
		    	&& org.kasource.jmx.dashboard.widgets[key].dashboardId == org.kasource.jmx.dashboard.currentDasboard 
		    	/*&& (org.kasource.jmx.dashboard.widgets[key].chart || org.kasource.jmx.dashboard.widgets[key].gauge)*/) {
		    	$('#' + key).empty();
		    	org.kasource.jmx.dashboard.widgets[key].render();	
		    }
		}
		org.kasource.jmx.dashboard.graphPainter.draw();
		
	}
	
}

org.kasource.jmx.dashboard.renderDashboard = function(json) {
	var dashboard = json.data;
	   
	
		$("#"+dashboard.id).find("ul").gridster({
		widget_margins: [5, 5],
		widget_base_dimensions: [dashboard.baseWidth, dashboard.baseHeight],
		extra_cols: 10,
		draggable: {
			handle: '.title-bar-text'
		}
		});
		
		org.kasource.jmx.dashboard.dashboards[dashboard.id] = $("#"+dashboard.id).find("ul").data('gridster');
		
		setTimeout(function() {
	
	for(var i = 0; i < dashboard.panel.length; i++) {
		var panel = dashboard.panel[i];
		org.kasource.jmx.dashboard.widgetFactory[panel.widget.type].get(dashboard.id, panel.widget.id, panel.widget);
		
	}
		}, 500);
	
	$("#"+dashboard.id).removeClass("hidden");
	
}



org.kasource.jmx.dashboard.removePanel = function(dashboardId, panelId, widgetId) {
	org.kasource.jmx.dashboard.dashboards[dashboardId].remove_widget($("#" + panelId), function(){
		if(widgetId) {
			var widget = org.kasource.jmx.dashboard.widgets[widgetId];
			if(widget && widget.close) {
				widget.close();
			}
		}
	});
	
}

org.kasource.jmx.dashboard.togglePanelSize = function(dashboardId, panelId, widgetId) {
	var isMax = $("#" + panelId).attr("maximized");

	var x = parseInt($("#" + panelId).attr("data-sizex"));
	var y = parseInt($("#" + panelId).attr("data-sizey"));

	if (isMax && isMax == 'true') {
		x = x - 1;
		y = y - 1;
		$("#" + panelId).attr("maximized", "false");
		
	} else {
		x = x + 1;
		y = y + 1;
		$("#" + panelId).attr("maximized", "true");
		
	}
	org.kasource.jmx.dashboard.dashboards[dashboardId].resize_widget($("#" + panelId), x, y);
	
	
	
	if (widgetId) {
		var widgetClass = $('#'+widgetId).attr("class");
		var widget = org.kasource.jmx.dashboard.widgets[widgetId];
		
		if(widget.render) {
			setTimeout(function() {
				$('#' + widgetId).empty();
				widget.render();
			}, 300);
		}
	}

}

