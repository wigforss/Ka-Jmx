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

org.kasource.jmx.dashboard.widgets = [];
org.kasource.jmx.dashboard.graphs = [];
org.kasource.jmx.dashboard.widgetFactory = {};
org.kasource.jmx.dashboard.currentDasboard={};

org.kasource.jmx.dashboard.gaugeFactory = {};


org.kasource.jmx.dashboard.Gauge = 
	function (dashboardId, id, options) {
		this.id = id;
		this.options = options;
		this.gauge;
		this.gaugeOptions;
		this.listeners={};
		this.dashboardId = dashboardId;
		
		this.initializeAttribute = function(attribute) {
			if(!attribute.jsFunction && attribute.value) {
				return this.roundValue(parseFloat(attribute.value));
			} else if(attribute.jsFunction) {
				attribute.transform = eval('('+attribute.jsFunction+')');
				if(attribute.value) {
					 return this.roundValue(parseFloat(attribute.transform(attribute.value)));
				}
			}
			return 0;
		} 
		
		this.roundValue = function(value) {
			var multiplier = Math.pow(10, this.options.decimals);
			return Math.round(value * multiplier) / multiplier;
		}
		
		this.initialize = function() {
			var minValue = this.initializeAttribute(this.options.min);
			var maxValue = this.initializeAttribute(this.options.max);
			var currentValue = this.initializeAttribute(this.options.value);
			
			var title = '';
			if(this.options.title) {
				title = this.options.title;
			}
			
			
			
			this.gaugeOptions = {
					id : this.id,
					value : currentValue,
					min : minValue,
					max : maxValue,
					label : this.options.value.label,
					title: title,
					valueFontColor : '#0F4A00',
					labelFontColor : '#0F4A00'
				};
			
			
		}
		
		this.subscribe = function() {
			
			if(this.options.min.subscribe && this.options.min.attribute) {	
				this.listeners[this.options.min.attribute.objectName+"."+this.options.min.attribute.attribute]=['min'];	
				org.kasource.Websocket.subscribe(this.options.min.attribute.objectName, this.options.min.attribute.attribute, id+"-min", this.refreshValue, this, this.options.min.type);
			}
			if(this.options.max.subscribe && this.options.max.attribute) {
				var types = this.listeners[this.options.max.attribute.objectName+"."+this.options.max.attribute.attribute];
				if(!types) {
					this.listeners[this.options.max.attribute.objectName+"."+this.options.max.attribute.attribute]=['max'];
				} else {
					types.push('max')
				}		
				org.kasource.Websocket.subscribe(this.options.max.attribute.objectName, this.options.max.attribute.attribute, id+"-max", this.refreshValue, this, this.options.max.type);
			}
			if(this.options.value.subscribe && this.options.value.attribute) {
				var types = this.listeners[this.options.value.attribute.objectName+"."+this.options.value.attribute.attribute];
				if(!types) {
					this.listeners[this.options.value.attribute.objectName+"."+this.options.value.attribute.attribute]=['value'];
				} else {
					types.push('value');
				}		
				org.kasource.Websocket.subscribe(this.options.value.attribute.objectName, this.options.value.attribute.attribute, id+"-value", this.refreshValue, this, this.options.value.type);
			}
			
		}
		
		
		this.render = function() {
			this.gauge = new JustGage(this.gaugeOptions);
		}
		
		this.getAttributeValue = function(attribute, value){
			
			if(attribute.transform) {
				return this.roundValue(parseFloat(attribute.transform(value)));
			} else {
				return this.roundValue(parseFloat(value));
			}
		}
		
		this.setOptionAndRender = function(widget, optionKey, value) {
			widget.gaugeOptions[optionKey] = value;
			if (org.kasource.jmx.dashboard.currentDasboard == widget.dashboardId) {
				$('#' + widget.id).empty();
				widget.gauge = new JustGage(widget.gaugeOptions);
			}
		}
		
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var types = widget.listeners[objectName+"."+attribute];
			var gauge = widget.gauge;
			if(jmxValue.value != undefined && jmxValue.value != null) {
				if(types) {
					for (var i = 0; i < types.length; i++) {
						var type = types[i];
					
						switch(type) {
						case 'value':
							var currentValue = widget.getAttributeValue(widget.options.value, jmxValue.value);
							
							widget.gaugeOptions['value']=currentValue;
							if (org.kasource.jmx.dashboard.currentDasboard == widget.dashboardId) {
								gauge.refresh(currentValue);
							}
							break;
						case 'min':
							var minValue = widget.getAttributeValue(widget.options.min, jmxValue.value);
							widget.setOptionAndRender(widget, 'min', minValue);
							break;
						case 'max':
							var maxValue = widget.getAttributeValue(widget.options.max, jmxValue.value);
							widget.setOptionAndRender(widget, 'max', maxValue);
							break;
					
						}
					}
				}
			}
		}
		
		this.close = function() {
			if(this.options.min.subscribe && this.options.min.attribute) {
				org.kasource.Websocket.unsubscribe(this.options.min.attribute.objectName, this.options.min.attribute.attribute, this.id+"-min");	
			}
			if(this.options.max.subscribe && this.options.max.attribute) {
				org.kasource.Websocket.unsubscribe(this.options.max.attribute.objectName, this.options.max.attribute.attribute, this.id+"-max");
				
			}
			if(this.options.value.subscribe && this.options.value.attribute) {
				org.kasource.Websocket.unsubscribe(this.options.value.attribute.objectName, this.options.value.attribute.attribute, this.id+"-value");
			}
		}
		
	}


org.kasource.jmx.dashboard.gaugeFactory.get = function(dashboardId, widgetId, options) {
	org.kasource.jmx.dashboard.widgets[widgetId] = new org.kasource.jmx.dashboard.Gauge(dashboardId, widgetId, options);
	org.kasource.jmx.dashboard.widgets[widgetId].initialize();
	org.kasource.jmx.dashboard.widgets[widgetId].render();
	org.kasource.jmx.dashboard.widgets[widgetId].subscribe();
	return org.kasource.jmx.dashboard.widgets[widgetId];
}

org.kasource.jmx.dashboard.Graph = 
	function (dashboardId, id, options) {
		this.id = id;
		this.options = options;
		this.chart = {};
		this.chartOptions={};
		this.currentValues=[];
		this.dataSeriesIndex={};
		this.dashboardId = dashboardId;
		
		this.initialize = function() {
			var series = [];
			var samples = this.options.samples;
			var title = ''; 
			if(this.options.title) {
				title = this.options.title;
			}
			var yAxisLabel = this.options.yAxisLabel;
			var useLegend = (this.options.dataSeries.length > 1);
			for (var i = 0; i < this.options.dataSeries.length; i++) {
				
				var yValue = 0;
				if (this.options.dataSeries[i].jsFunction) {
					this.options.dataSeries[i].transform = eval('('+this.options.dataSeries[i].jsFunction+')');
					if(this.options.dataSeries[i].value) {
						yValue = parseFloat(this.options.dataSeries[i].transform(this.options.dataSeries[i].value));
					}
				} else if (this.options.dataSeries[i].value){
					yValue = parseFloat(this.options.dataSeries[i].value);
				}
				
				
				
				series.push({
		              name: this.options.dataSeries[i].label,
			          visible: this.options.dataSeries[i].visible,
		              data: (function() {
		                
		                  var data = [];
		                  var time = (new Date()).getTime();
		                 
		              
		                      data.push({
		                    	  x: time,
		                          y: yValue,
		                          marker: {enabled: false}
		                      });
		                
		                  return data;
		            	 
		              })()
		          });
				this.currentValues.push(yValue);
			}
			
			
			Highcharts.setOptions({
			    global: {
			        useUTC: false
			    }
			});
			var values = this.currentValues;
			var decimals = this.options.decimals;
			this.chartOptions = {
			         chart: {
			          	  renderTo: this.id,
			              type: 'line',
			              animation: Highcharts.svg, // don't animate in old IE
			              marginRight: 10,
			              marginTop: 10,
			              marginBottom:20,
			              zoomType: 'x',
			              shadow: true,
			              showAxes: true, 
			          },
			          credits: {
			        	enabled: false  
			          },
			          exporting: {
			              enabled: true
			          },
			          title: {
			              text: title
			          },
			          xAxis: {
			              type: 'datetime',
			              tickPixelInterval: 150
			          },
			          yAxis: {
			              title: {
			                  text: yAxisLabel
			              },
			              plotLines: [{
			                  value: 0,
			                  width: 1,
			                  color: '#808080'
			              }]
			          },
			          tooltip: {
			              formatter: function() {
			                      return '<b>'+ this.series.name +'</b><br/>'+
			                      Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'+
			                      Highcharts.numberFormat(this.y, decimals);
			              }
			          },
			          legend: {
			              enabled: false
			              
			          },
			          exporting: {
			              enabled: false,
			              contextButton : {enabled: true}
			          },
			          series: series
			      };
			
				if(useLegend) {
					this.chartOptions.legend = {
								enabled: true, 
								 align: 'right',
								 layout: 'vertical',
						            verticalAlign: 'top',
						            x: 0,
						            y: 0,
						            backgroundColor: 'rgba(0,0,0,0.1)',
						            itemMarginTop: 0,
						            itemMarginBottom: 0,
						            borderColor: 'rgba(0,0,0,0.1)',
						            itemStyle: {
						               cursor: 'pointer',
						                   color: 'rgba(0,0,0,0.6)',
						                fontSize: '8px'}
						            };
				}
			
			this.chart = new Highcharts.Chart(this.chartOptions);
			if(!org.kasource.jmx.dashboard.graphPainter.timer) {
				org.kasource.jmx.dashboard.graphPainter.timer = window.setInterval(function(){org.kasource.jmx.dashboard.graphPainter.draw()},1000);
			}
		
		}
		
		this.render = function() {
			this.chart = new Highcharts.Chart(this.chartOptions);
		}
		
		this.subscribe = function() {
			for (var i = 0; i < this.options.dataSeries.length; i++) {
				var objectName = this.options.dataSeries[i].attribute.objectName;
				var attribute = this.options.dataSeries[i].attribute.attribute;
				var index = this.dataSeriesIndex[objectName+"."+attribute];
				if(!index) {
					this.dataSeriesIndex[objectName+"."+attribute]=[i];
				} else {
					index.push(i);
				}
				org.kasource.Websocket.subscribe(objectName, attribute, this.id+"-"+i, this.refreshValue, this, this.options.dataSeries[i].type);
			}
			
		}
		
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var indices = widget.dataSeriesIndex[objectName+"."+attribute];
			if(indices && jmxValue.value != undefined && jmxValue.value != null) {
				for (var i = 0; i < indices.length; i++) {
					var index = indices[i];
				
					if(widget.options.dataSeries[i].transform) {
						widget.currentValues[index]=parseFloat(widget.options.dataSeries[i].transform(jmxValue.value));
					} else {
						widget.currentValues[index]=parseFloat(jmxValue.value);
					}
					
				}
			}
			
			
		}
		
		this.close = function() {
			for (var i = 0; i < this.options.dataSeries.length; i++) {
				var objectName = this.options.dataSeries[i].attribute.objectName;
				var attribute = this.options.dataSeries[i].attribute.attribute;
				org.kasource.Websocket.unsubscribe(objectName, attribute, this.id+"-"+i);
				graphPainter.remove(this);
			}
			
		}
		
		
}



org.kasource.jmx.dashboard.graphPainter = {}


org.kasource.jmx.dashboard.graphPainter.addPoint = function(graph) {
		var time = (new Date()).getTime();
		var shift = true;
		var paint = false;
		
		if(graph.dashboardId == org.kasource.jmx.dashboard.currentDasboard) {
			paint = true;
		}
		if(graph.options.samples <= 0 || graph.chart.series[0].data.length <= graph.options.samples ) {
				shift = false;
		}
	  	for (var i = 0; i < graph.currentValues.length; i++) {		
	  				graph.chart.series[i].addPoint({
	  									x: time,
	  									y: parseFloat(graph.currentValues[i]),
	  									marker: {enabled: false}
	  									}, paint, shift);
	  			
	  	}
}

org.kasource.jmx.dashboard.graphPainter.draw = function() {
	for (var i = 0; i < org.kasource.jmx.dashboard.graphs.length; i++) {
		org.kasource.jmx.dashboard.graphPainter.addPoint(org.kasource.jmx.dashboard.graphs[i]);
	}	
}

org.kasource.jmx.dashboard.graphPainter.remove = function(graph) {
	for (var i = 0; i < org.kasource.jmx.dashboard.graphs.length; i++) {
		if(org.kasource.jmx.dashboard.graphs[i].id == graph.id) {
			org.kasource.jmx.dashboard.graphs.splice(i,1);
			break;
		}
	}
}

org.kasource.jmx.dashboard.graphFactory = {};

org.kasource.jmx.dashboard.graphFactory.get = function(dashboardId, widgetId, options) {
	org.kasource.jmx.dashboard.widgets[widgetId] = new org.kasource.jmx.dashboard.Graph(dashboardId, widgetId, options);
	org.kasource.jmx.dashboard.widgets[widgetId].initialize();
	org.kasource.jmx.dashboard.widgets[widgetId].subscribe();
	
	org.kasource.jmx.dashboard.graphs.push(org.kasource.jmx.dashboard.widgets[widgetId]);
	
	return org.kasource.jmx.dashboard.widgets[widgetId];
}

org.kasource.jmx.dashboard.Pie = 
	function (dashboardId, id, options) {
		this.id = id;
		this.options = options;
		this.chart = {};
		this.dataSeriesIndex={};
		this.dashboardId = dashboardId;
		this.chartOptions = {};
		this.initialize = function() {
			
			
			
			var seriesTitle = this.options.title;
			if(!seriesTitle) {
				seriesTitle = '';
			}
			var data = [];
			for (var i = 0; i < this.options.dataSeries.length; i++) {	
				var yValue = 0;
				if(this.options.dataSeries[i].jsFunction) {
					this.options.dataSeries[i].transform = eval('('+this.options.dataSeries[i].jsFunction+')');
					if(this.options.dataSeries[i].value) {
						yValue = parseFloat(this.options.dataSeries[i].transform(this.options.dataSeries[i].value));
					}
				} else if (this.options.dataSeries[i].value){
					yValue = parseFloat(this.options.dataSeries[i].value);
				}
						
				data.push({name: this.options.dataSeries[i].label, y: yValue});
			}
			var series = [{
	            type: 'pie',
	            name: seriesTitle,
	            data: data
				}]
			
			
			var title = '';
			this.chartOptions = {
			        chart: {
			        	renderTo: this.id,
			            plotBackgroundColor: null,
			            plotBorderWidth: null,
			            plotShadow: true
			        },
			        title: {
			              text: title
			        },
			        credits: {
			        	enabled: false  
			          },
			        tooltip: {
			    	    pointFormat: '{series.name}: <b>{point.y}</b>'
			        },
			        plotOptions: {
			            pie: {
			                allowPointSelect: true,
			                cursor: 'pointer',
			                dataLabels: {
			                    enabled: true,
			                    color: '#000000',
			                    connectorColor: '#000000',
			                   
			                }
			            }
			        },
			        series: series
			    };
			this.chart = new Highcharts.Chart(this.chartOptions);
		}
		
		this.subscribe = function() {
			for (var i = 0; i < this.options.dataSeries.length; i++) {
				if(this.options.dataSeries[i].attribute && this.options.dataSeries[i].subscribe) {
					var objectName = this.options.dataSeries[i].attribute.objectName;
					var attribute = this.options.dataSeries[i].attribute.attribute;
					var index = this.dataSeriesIndex[objectName+"."+attribute];
					if(!index) {
						this.dataSeriesIndex[objectName+"."+attribute]=[i];
					} else {
						index.push(i);
					}
					org.kasource.Websocket.subscribe(objectName, attribute, this.id+"-"+i, this.refreshValue, this, this.options.dataSeries[i].type);
				}
			}
		}
		
		this.render = function() {
			this.chart = new Highcharts.Chart(this.chartOptions);
		}
		
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var indices = widget.dataSeriesIndex[objectName+"."+attribute];
			var yValue =  parseFloat(jmxValue.value);
				
			if(indices && jmxValue.value != undefined && jmxValue.value != null) {
				for (var i = 0; i < indices.length; i++) {
					var index = indices[i];
					if(widget.options.dataSeries[index].transform) {
						yValue =  parseFloat(widget.options.dataSeries[index].transform(jmxValue.value));
					}
					
					
					if (org.kasource.jmx.dashboard.currentDasboard == widget.dashboardId) {
						widget.chart.series[0].data[index].update(yValue, true);
					} else {
						widget.chart.series[0].data[index].update(yValue, false);
					}
				}
			}
		}
		
		this.close = function() {
			for (var i = 0; i < this.options.dataSeries.length; i++) {
				if(this.options.dataSeries[i].attribute && this.options.dataSeries[i].subscribe) {
					var objectName = this.options.dataSeries[i].attribute.objectName;
					var attribute = this.options.dataSeries[i].attribute.attribute;
					org.kasource.Websocket.unsubscribe(objectName, attribute, this.id+"-"+i);
				}
			}
		}
}

org.kasource.jmx.dashboard.pieFactory = {};

org.kasource.jmx.dashboard.pieFactory.get = function(dashboardId, widgetId, options) {
	org.kasource.jmx.dashboard.widgets[widgetId] = new org.kasource.jmx.dashboard.Pie(dashboardId, widgetId, options);
	org.kasource.jmx.dashboard.widgets[widgetId].initialize();
	org.kasource.jmx.dashboard.widgets[widgetId].subscribe();
	
	return org.kasource.jmx.dashboard.widgets[widgetId];
}


org.kasource.jmx.dashboard.TextGroup = 
	function (dashboardId, id, data) {
		this.id = id;
		this.data = data;
		this.title = data.title;
		this.listeners = {};
		this.dashboardId = dashboardId;
		
		this.initialize = function() {
			for(var i = 0; i < this.data.value.length; i++) {
				if(this.data.value[i].jsFunction) {
					this.data.value[i].transform = eval('('+this.data.value[i].jsFunction+')');
				}
			}
		}
		
		this.render = function() {
			if(this.title) {
				$('#'+id).append('<span class="text-title">'+this.title+'</span>');
			}
			
		   var table = '<table>';
			for(var i = 0; i < this.data.value.length; i++) {
				table += '<tr><td class="text-label">';
				table += this.data.value[i].label;
				table += '</td><td class="text-value">';
				if(this.data.value[i].value) {
					if(this.data.value[i].transform) {
						table += this.data.value[i].transform(this.data.value[i].value);
					} else {
						table += this.data.value[i].value;
					}
				}
				table +='</td></tr>';
			}
			table += '</table>';
			$('#'+id).append(table);
			
		}
		
		this.subscribe = function() {
			for(var i = 0; i < this.data.value.length; i++) {
				var item = this.data.value[i];
				if(item.subscribe && item.attribute) {
					var index = this.listeners[item.attribute.objectName+"."+item.attribute.attribute];
					if(!index) {
						this.listeners[item.attribute.objectName+"."+item.attribute.attribute]=[i];
					} else {
						index.push(i);
					}
					org.kasource.Websocket.subscribe(item.attribute.objectName, item.attribute.attribute, id+"-"+i, this.refreshValue, this, item.type);
					
				}
			}
		}
		
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var rows = widget.listeners[objectName+"."+attribute];
			if(rows) {
				for(var i = 0; i < rows.length; i++) {
					var row = rows[i];
					var item = widget.data.value[row];
					var value = jmxValue.value
					item.value = value;
					if(value && item.transform) {
						value = item.transform(value);
					}
					if(!value) {
						value='';
					}
					$('#'+id).find('tr:nth-child('+(row+1)+')').find('td:nth-child(2)').text(value);
				}
			}
		}
		
		this.close = function() {
			for (var key in this.listeners) {
			    if (this.listeners.hasOwnProperty(key)) {
			    	var index = this.listeners[key] - 1;
			    	var item = this.data.value[index];		    	
			    	org.kasource.Websocket.unsubscribe(item.attribute.objectName, item.attribute.attribute, this.id+index);
			    }
			}
		}
		
	}

org.kasource.jmx.dashboard.textGroupFactory = {};

org.kasource.jmx.dashboard.textGroupFactory.get = function(dashboardId, widgetId, data) {
	org.kasource.jmx.dashboard.widgets[widgetId] = new org.kasource.jmx.dashboard.TextGroup(dashboardId, widgetId, data);
	org.kasource.jmx.dashboard.widgets[widgetId].initialize();
	org.kasource.jmx.dashboard.widgets[widgetId].render();
	org.kasource.jmx.dashboard.widgets[widgetId].subscribe();
	return org.kasource.jmx.dashboard.widgets[widgetId];
}



org.kasource.jmx.dashboard.LedPanelWidget = 
	function (dashboardId, id, options) {
		this.id = id;
		this.options = options;
		this.dashboardId = dashboardId;
		this.dataIndex = {};
		this.ledPanel = {};
		this.ledPanelOptions={};
		
		this.initialize = function() {
			var leds = [];
			for (var i = 0; i < this.options.data.length; i++) {
				var enabledValue = false;
				if(this.options.data[i].jsFunction) {
					this.options.data[i].transform = eval('('+this.options.data[i].jsFunction+')');
					if(this.options.data[i].value) {
						enabledValue = this.toBoolean(this.options.data[i].transform(this.options.data[i].value));
					}
				} else if(this.options.data[i].value){
					enabledValue = this.toBoolean(this.options.data[i].value);
				}
				leds.push({title: this.options.data[i].label, enabled: enabledValue});
			}
			this.ledPanelOptions = {layout: this.options.layout, color: this.options.color, showLabels: this.options.showLabels, data: leds};
			
		}
			
		
		this.render = function() {
			this.ledPanel = new org.kasource.jmx.widget.LedPanel(this.id, this.ledPanelOptions);
	    }
	
		this.subscribe = function() {
			for (var i = 0; i < this.options.data.length; i++) {
				if(this.options.data[i].attribute && this.options.data[i].subscribe) {
					var objectName = this.options.data[i].attribute.objectName;
					var attribute = this.options.data[i].attribute.attribute;
					var index = this.dataIndex[objectName+"."+attribute];
					if(!index) {
						this.dataIndex[objectName+"."+attribute]=[i];
					} else {
						index.push(i);
					}
					org.kasource.Websocket.subscribe(objectName, attribute, this.id+"-"+i, this.refreshValue, this, this.options.data[i].type);
				}
			}
		}
	
		this.toBoolean = function(value) {
			if(value && value === true) {
				return true;
			}
			if(value && value.toLowerCase() === 'true') {
				return true;
			}
			return false;
		}
		
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var indices = widget.dataIndex[objectName+"."+attribute];
			
			if(indices && jmxValue.value != undefined && jmxValue.value != null) {
				
				for (var i = 0; i < indices.length; i++) {
					var index = indices[i];
					var value;
					if(widget.options.data[index].transform) {
						value = widget.toBoolean(widget.options.data[index].transform(jmxValue.value));
					} else {
						value = widget.toBoolean(jmxValue.value);
					}
					widget.ledPanelOptions.data[index].enabled = value;
					if (org.kasource.jmx.dashboard.currentDasboard == widget.dashboardId) {
						widget.ledPanel.enableLed(index, value);
					}
				}
			}
		}
	
		this.close = function() {
			for (var i = 0; i < this.options.data.length; i++) {
				if(this.options.data[i].attribute && this.options.data[i].subscribe) {
					var objectName = this.options.data[i].attribute.objectName;
					var attribute = this.options.data[i].attribute.attribute;
					org.kasource.Websocket.unsubscribe(objectName, attribute, this.id+"-"+i);
				}
			}
		}
}

org.kasource.jmx.dashboard.ledPanelFactory = {};

org.kasource.jmx.dashboard.ledPanelFactory.get = function(dashboardId, widgetId, options) {
	org.kasource.jmx.dashboard.widgets[widgetId] = new org.kasource.jmx.dashboard.LedPanelWidget(dashboardId, widgetId, options);
	org.kasource.jmx.dashboard.widgets[widgetId].initialize();
	org.kasource.jmx.dashboard.widgets[widgetId].render();
	org.kasource.jmx.dashboard.widgets[widgetId].subscribe();
	return org.kasource.jmx.dashboard.widgets[widgetId];
}

org.kasource.jmx.dashboard.TrafficLightWidget = 
	function (dashboardId, id, options) {
		this.id = id;
		this.options = options;
		this.dashboardId = dashboardId;
		this.trafficLightOptions={};
		this.trafficLight = {};
		this.stateAttribute= {};
		this.listeners = {};
		this.redValue=0;
		this.yellowValue=0;
		this.greenValue=0;
		this.currentValue=0;
		
		this.parseValue = function(valueToParse) {
			if(this.options.attributeType === "numeric"){
				return parseFloat(valueToParse);
			} else if(this.options.attributeType.toLowerCase() === "text"){
				return valueToParse;
			} else {
				return valueToParse && (valueToParse === true || valueToParse.toLowerCase() === "true");
			}
		} 
		
		this.initializeAttribute = function(attribute) {
			if(!attribute.jsFunction && attribute.value) {
				return this.parseValue(attribute.value);
			} else if(attribute.jsFunction) {
				attribute.transform = eval('('+attribute.jsFunction+')');
				if(attribute.value) {
					this.parseValue(attribute.transform(attribute.transform.value));
				}
			}
			return '';
		}
		
		this.initialize = function() {
			this.options.attributeType = this.options.attributeType.toLowerCase();
			this.redValue = this.initializeAttribute(this.options.red);
			this.stateAttribute['red'] = this.options.red;
			this.yellowValue = this.initializeAttribute(this.options.yellow);
			this.stateAttribute['yellow'] = this.options.yellow;
			this.greenValue = this.initializeAttribute(this.options.green);
			this.stateAttribute['green'] = this.options.green;
			this.currentValue = this.initializeAttribute(this.options.value);
			this.stateAttribute['value'] = this.options.value;
			
			this.trafficLightOptions = {state: this.options.state, value: this.currentValue};
			if (this.currentValue != undefined && this.currentValue != null) {
				this.trafficLightOptions.state = this.resolveState();
			}
				
			
			
			if(this.options.title) {
				this.trafficLightOptions['title']=this.options.title;
			}
			if(this.options.prefix) {
				this.trafficLightOptions['prefix']=this.options.prefix;
			}
			if(this.options.suffix) {
				this.trafficLightOptions['suffix']=this.options.suffix;
			}
		}
	
		this.render = function() {
			this.trafficLight = new org.kasource.jmx.widget.TrafficLight(this.id, this.trafficLightOptions);
			this.trafficLight.setValue(this.currentValue);
	    }
	
		this.subscribe = function() {
			for (var key in this.stateAttribute) {
    		    var state = this.stateAttribute[key];
    		    if(state.attribute && state.subscribe) {
    		    	var types = this.listeners[state.attribute.objectName+"."+state.attribute.attribute];
    				if(!types) {
    					this.listeners[state.attribute.objectName+"."+state.attribute.attribute]=[key];
    				} else {
    					types.push(key)
    				}		
    				org.kasource.Websocket.subscribe(state.attribute.objectName, state.attribute.attribute, this.id+"-"+key, this.refreshValue, this, state.type);
    		    }
    		}
		}
	
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var types = widget.listeners[objectName+"."+attribute];
			var gauge = widget.gauge;
			if(types && jmxValue.value != undefined && jmxValue.value != null) {
				for (var i = 0; i < types.length; i++) {
					var type = types[i];
					switch(type) {
					case 'value':
						widget.currentValue = widget.getNewValue(widget.stateAttribute[type], jmxValue);
						widget.trafficLightOptions.state = widget.resolveState();
						if (org.kasource.jmx.dashboard.currentDasboard == widget.dashboardId) {
							widget.trafficLight.setState(widget.trafficLightOptions.state);
							widget.trafficLight.setValue(widget.currentValue);
						}
						break;
					case 'red':
						this.redValue = widget.getNewValue(widget.stateAttribute[type], jmxValue);
						break;
					case 'yelow':
						this.yellowValue = widget.getNewValue(widget.stateAttribute[type], jmxValue);
						break;
					case 'green':
						this.greenValue = widget.getNewValue(widget.stateAttribute[type], jmxValue);
						break;
					
					}
					
				}
			}
		}
		
		this.getNewValue = function(attribute, jmxValue) {
			if(attribute.transform) {
				return this.parseValue(attribute.transform(jmxValue.value));
			} else {
				return this.parseValue(jmxValue.value);
			}
		}
		
		this.resolveState = function() {
			if (this.options.attributeType === 'numeric') {
				return this.resolveStateForNumericalValue();
			} else {
				return this.resolveStateForTextValue();
			}
		}
		
		this.resolveStateForNumericalValue = function() {
			if(this.options.ascending && this.options.ascending === true) {
				if(this.currentValue < this.yellowValue) {
					return  'green';
				} else if(this.currentValue < this.redValue) {
					return 'yellow';
				} else {
					return 'red';
				}
			} else {
				if(this.currentValue <= this.redValue) {
					return 'red';
				} else if(this.currentValue <= this.green) {
					return 'yellow';
				} else {
					return 'green';
				}
			}
			
		}
		
		this.resolveStateForTextValue = function() {
			if(this.options.ascending && this.options.ascending === true) {
				if(this.greenValue.indexOf(this.currentValue) > -1) {
					return 'green';
				} else if(this.yellowValue.indexOf(this.currentValue) > -1) {
					return 'yellow';
				} else {
					return 'red';
				}
			} else {
				if(this.redValue.indexOf(this.currentValue) > -1) {
					return 'red';
				} else if(this.yellowValue.indexOf(this.currentValue) > -1) {
					return 'yellow';
				} else {
					return 'green';
				}
			}
		}
		
	
		this.close = function() {
			for (var key in this.stateAttribute) {
    		    var state = this.stateAttribute[key];
    		    if(state.attribute && state.subscribe) {
    		    	org.kasource.Websocket.unsubscribe(state.attribute.objectName, state.attribute.attribute, this.id+"-"+key);	
    		    }
			}
		}
}

org.kasource.jmx.dashboard.trafficLightFactory = {};

org.kasource.jmx.dashboard.trafficLightFactory.get = function(dashboardId, widgetId, options) {
	org.kasource.jmx.dashboard.widgets[widgetId] = new org.kasource.jmx.dashboard.TrafficLightWidget(dashboardId, widgetId, options);
	org.kasource.jmx.dashboard.widgets[widgetId].initialize();
	org.kasource.jmx.dashboard.widgets[widgetId].render();
	org.kasource.jmx.dashboard.widgets[widgetId].subscribe();
	return org.kasource.jmx.dashboard.widgets[widgetId];
}

org.kasource.jmx.dashboard.HeatMap = 
	function (dashboardId, id, options) {
		this.id = id;
		this.dashboardId = dashboardId;
		this.options = options;
		this.defaultHeat = 0;
		this.defaultValue = 'NULL';
		this.heatMapOptions = {};
		this.heatMap = {};
		this.listenerCellIndices = {};
		
		this.initialize = function() {
			
			this.options.colorSchema = this.options.colorSchema.toLowerCase();
			this.options.legendLayout = this.options.legendLayout.toLowerCase();
			
			
			var nodes = [];
			for (var y = 0; y < this.options.heatRow.length; y++) {
				var cells = [];
				var row = this.options.heatRow[y];
				for (var x = 0; x < row.heatCell.length; x++) {
					var heatCell = row.heatCell[x];
					if(heatCell.normalizationFunction) {
						heatCell.normalize = eval('('+heatCell.normalizationFunction+')');
					}
					if(heatCell.data.jsFunction){
						heatCell.data.transform = eval('('+heatCell.data.jsFunction+')');
					}
					var currentValue = this.defaultValue;
					var heat = this.defaultHeat;
					if(heatCell.data.value != undefined && heatCell.data.value != null) {
						 currentValue = this.getValue(heatCell, heatCell.data.value);
						 heat = this.getHeat(heatCell, heatCell.data.value);
					}
					var cellInfo = this.getCellInfo(heatCell, currentValue);
					
					
					cells.push({heat: heat, value: cellInfo, title: heatCell.data.label});
				}
				nodes.push(cells);
			}
			
			this.heatMapOptions = {colorSchema: this.options.colorSchema,showLegend: this.options.showLegend, legendLayout: this.options.legendLayout, data: nodes};
			
			
		}
		
		this.getCellInfo = function(heatCell, value) {
			if(heatCell.data.attribute) {
				return '<b>Value:</b> ' + value + '<br><b>Attribute:</b> ' + heatCell.data.attribute.attribute + '<br><b>ObjectName:</b> <a href="'+window.location+'?objectName='+encodeURI(heatCell.data.attribute.objectName)+'" target="_blank">' + heatCell.data.attribute.objectName + '</a>';
			} else {
				return value;
			}
		}
		
		this.getValue = function(heatCell, value) {
			if(heatCell.data.transform) {
				return heatCell.data.transform(value);
			} else {
				return value;
			}
		}
		
		this.getHeat = function(heatCell, value) {
			if(heatCell.normalize) {
				return heatCell.normalize(value);
			} else {
				return value;
			}
			
		}
		
		this.render = function() {
			this.heatMap = new org.kasource.jmx.widget.HeatMap(this.id, this.heatMapOptions);
		}
		
		this.subscribe = function() {
			for (var y = 0; y < this.options.heatRow.length; y++) {
				for (var x = 0; x < this.options.heatRow[y].heatCell.length; x++) {
					var heatCell = this.options.heatRow[y].heatCell[x];
					var attributeValue = heatCell.data;
					if(attributeValue.attribute && attributeValue.subscribe) {
						var objectName = attributeValue.attribute.objectName;
						var attribute = attributeValue.attribute.attribute;
						var index = this.listenerCellIndices[objectName+"."+attribute];
						if(!index) {
							this.listenerCellIndices[objectName+"."+attribute]=[{x: x, y: y}];
						} else {
							index.push({x: x, y: y});
						}
						org.kasource.Websocket.subscribe(objectName, attribute, this.id+"-"+x+"-"+y, this.refreshValue, this, attributeValue.type);
					}
				}
			}
		}
		
		this.close = function() {
			for (var y = 0; y < this.options.heatRow.length; y++) {
				for (var x = 0; x < this.options.heatRow[y].heatCell.length; x++) {
					var heatCell = this.options.heatRow[y].heatCell[x];
					var attributeValue = heatCell.data;
					if(attributeValue.attribute && attributeValue.subscribe) {
						org.kasource.Websocket.unsubscribe(attributeValue.attribute.objectName, attributeValue.attribute.attribute, this.id+"-"+x+"-"+y);	
					}
				}
			}
		}
		
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var positions = widget.listenerCellIndices[objectName+"."+attribute];
		
			if(positions && jmxValue.value != undefined && jmxValue != null) {
				for (var i = 0; i < positions.length; i++) {
					var pos = positions[i];
					var heatCell = widget.options.heatRow[pos.y].heatCell[pos.x];
					var value = widget.getValue(heatCell, jmxValue.value);
					var heat = widget.getHeat(heatCell, jmxValue.value);
					heatCell.value = value;
					heatCell.heat = heat;
					widget.heatMap.setHeat(pos.y, pos.x, heat, widget.getCellInfo(heatCell, value));
				}
			}
		}
}

org.kasource.jmx.dashboard.heatMapFactory = {};

org.kasource.jmx.dashboard.heatMapFactory.get = function(dashboardId, widgetId, options) {
	org.kasource.jmx.dashboard.widgets[widgetId] = new org.kasource.jmx.dashboard.HeatMap(dashboardId, widgetId, options);
	org.kasource.jmx.dashboard.widgets[widgetId].initialize();
	org.kasource.jmx.dashboard.widgets[widgetId].render();
	org.kasource.jmx.dashboard.widgets[widgetId].subscribe();
	return org.kasource.jmx.dashboard.widgets[widgetId];
}

org.kasource.jmx.dashboard.widgetFactory ['textGroup']=org.kasource.jmx.dashboard.textGroupFactory;
org.kasource.jmx.dashboard.widgetFactory ['gauge'] = org.kasource.jmx.dashboard.gaugeFactory;
org.kasource.jmx.dashboard.widgetFactory ['graph'] = org.kasource.jmx.dashboard.graphFactory;
org.kasource.jmx.dashboard.widgetFactory ['pie'] = org.kasource.jmx.dashboard.pieFactory;
org.kasource.jmx.dashboard.widgetFactory ['ledPanel'] = org.kasource.jmx.dashboard.ledPanelFactory;
org.kasource.jmx.dashboard.widgetFactory ['trafficLight'] = org.kasource.jmx.dashboard.trafficLightFactory;
org.kasource.jmx.dashboard.widgetFactory ['heatMap'] = org.kasource.jmx.dashboard.heatMapFactory;