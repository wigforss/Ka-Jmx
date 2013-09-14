


dashboards={};


function loadDashboard(dashboardId) {
	currentDasboard = dashboardId;
	$("#content-layout").removeClass("roundbox");
	$("#content-layout").removeClass("boxshadow");
	$("#dashboardView").removeClass("hidden");
	$("#beanView").addClass("hidden");
	// Hide all dashboards
	$("#dashboardView").children("div").addClass("hidden");
	if($("#"+dashboardId).find('div:nth-child(2)').is(':empty')) {
		// load dashboard
		$.get(encodeURI('json/dashboard'), {dashboardId: dashboardId}, this.renderDashboard);
	} else {
		// unhide render
		$("#"+dashboardId).removeClass("hidden");
		
		for (var key in widgets) {
		    if (widgets[key] && widgets[key].dashboardId == currentDasboard && (widgets[key].chart || widgets[key].gauge)) {
		    	$('#' + key).empty();
				widgets[key].render();	
		    }
		}
		graphPainter.draw();
		
	}
	
}

function renderDashboard(json) {
	var dashboard = json.data;
	   
	
		$("#"+dashboard.id).find("ul").gridster({
		widget_margins: [5, 5],
		widget_base_dimensions: [dashboard.baseWidth, dashboard.baseHeight],
		extra_cols: 10,
		draggable: {
			handle: '.title-bar'
		}
		});
		
		dashboards[dashboard.id] = $("#"+dashboard.id).find("ul").data('gridster');
		
		setTimeout(function() {
	
	for(var i = 0; i < dashboard.panel.length; i++) {
		var panel = dashboard.panel[i];
		if(panel.graph) {
			widgetFactory['graph'].get(dashboard.id, panel.graph.id, panel.graph);
		} else if(panel.gauge) {
			widgetFactory['gauge'].get(dashboard.id, panel.gauge.id, panel.gauge);
		} else {
			widgetFactory['textGroup'].get(dashboard.id, panel.textGroup.id, panel.textGroup);
		}
	}
		}, 500);
	
	$("#"+dashboard.id).removeClass("hidden");
	
}



function mergeOptions(options, overrideOptions) {
	if (overrideOptions != null) {
		for ( var key in overrideOptions) {
			if (overrideOptions.hasOwnProperty(key)) {
				options[key] = overrideOptions[key];
			}
		}
	}
	return options;
}



function removePanel(dashboardId, panelId, widgetId) {
	dashboards[dashboardId].remove_widget($("#" + panelId), function(){
		if(widgetId) {
			var widget = widgets[widgetId];
			if(widget && widget.close) {
				widget.close();
			}
		}
	});
	
}

function togglePanelSize(dashboardId, panelId, widgetId) {
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
	dashboards[dashboardId].resize_widget($("#" + panelId), x, y);
	
	
	
	if (widgetId) {
		var widgetClass = $('#'+widgetId).attr("class");
		var widget = widgets[widgetId];
		
		if(widget.render) {
			setTimeout(function() {
				$('#' + widgetId).empty();
				widget.render();
			}, 300);
		}
	}

}

