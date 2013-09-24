widgets = [];
graphs = [];
widgetFactory = [];
currentDasboard={};
gaugeFactory = {};


Gauge = 
	function (dashboardId, id, options) {
		this.id = id;
		this.options = options;
		this.gauge;
		this.gaugeOptions;
		this.listeners={};
		this.dashboardId = dashboardId;
		
		this.initialize = function() {
			var minValue = 0;
			if(!this.options.min.jsFunction && this.options.min.value) {
				minValue = parseFloat(this.options.min.value);
			} else if(this.options.min.jsFunction) {
				this.options.min.transform = eval('('+this.options.min.jsFunction+')');
				if(this.options.min.value) {
					 minValue = this.options.min.transform(this.options.min.value);
				}
			}
			
			var maxValue = 0;
			if(!this.options.max.jsFunction && this.options.max.value) {
				maxValue = parseFloat(this.options.max.value);
			} else if(this.options.max.jsFunction) {
				this.options.max.transform = eval('('+this.options.max.jsFunction+')');
				if(this.options.max.value) {
					 maxValue = this.options.max.transform(this.options.max.value);
				}
			}
			
			var currentValue = 0;
			if(!this.options.value.jsFunction && this.options.value.value) {
				currentValue = parseFloat(this.options.value.value);
			} else if(this.options.value.jsFunction) {
				this.options.value.transform = eval('('+this.options.value.jsFunction+')');
				if(this.options.value.value) {
					currentValue = this.options.value.transform(this.options.value.value);
				}
			}
			
			
			
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
				org.kasource.Websocket.subscribe(this.options.min.attribute.objectName, this.options.min.attribute.attribute, id+"-min", this.refreshValue, this);
			}
			if(this.options.max.subscribe && this.options.max.attribute) {
				var types = this.listeners[this.options.max.attribute.objectName+"."+this.options.max.attribute.attribute];
				if(!types) {
					this.listeners[this.options.max.attribute.objectName+"."+this.options.max.attribute.attribute]=['max'];
				} else {
					types.push('max')
				}		
				org.kasource.Websocket.subscribe(this.options.max.attribute.objectName, this.options.max.attribute.attribute, id+"-max", this.refreshValue, this);
			}
			if(this.options.value.subscribe && this.options.value.attribute) {
				var types = this.listeners[this.options.value.attribute.objectName+"."+this.options.value.attribute.attribute];
				if(!types) {
					this.listeners[this.options.value.attribute.objectName+"."+this.options.value.attribute.attribute]=['value'];
				} else {
					types.push('value');
				}		
				org.kasource.Websocket.subscribe(this.options.value.attribute.objectName, this.options.value.attribute.attribute, id+"-value", this.refreshValue, this);
			}
			
		}
		
		
		this.render = function() {
			this.gauge = new JustGage(this.gaugeOptions);
		}
		
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var types = widget.listeners[objectName+"."+attribute];
			var gauge = widget.gauge;
			if(types) {
				for (var i = 0; i < types.length; i++) {
					var type = types[i];
					switch(type) {
					case 'min':
						var minValue = 0;
						if (jmxValue.value) {
							if(widget.options.min.transform) {
								var minValue = parseFloat(widget.options.min.transform(jmxValue.value));
							} else {
								minValue = parseFloat(jmxValue.value);
							}
						}
						widget.gaugeOptions['min'] = minValue;
						if (currentDasboard == widget.dashboardId) {
							$('#' + widget.id).empty();
							widget.gauge = new JustGage(this.gaugeOptions);
						}
						break;
					case 'max':
						var maxValue = 0;
						if(jmxValue.value) {
							if(widget.options.max.transform) {
								maxValue = parseFloat(widget.options.max.transform(jmxValue.value));
							} else {
								maxValue = parseFloat(jmxValue.value);
							}
						}
						widget.gaugeOptions['max'] = maxValue;
						if (currentDasboard == widget.dashboardId) {
							$('#' + widget.id).empty();
							widget.gauge = new JustGage(widget.gaugeOptions);
						}
						break;
					case 'value':
						var currentValue = 0;
						if(jmxValue.value) {
							if(widget.options.value.transform) {
								currentValue = parseFloat(widget.options.value.transform(jmxValue.value));
							} else {
								currentValue = parseFloat(jmxValue.value);
							}
						}
						
						widget.gaugeOptions['value']=currentValue;
						if (currentDasboard == widget.dashboardId) {
							gauge.refresh(currentValue);
						}
						break;
					}
				}
			}
		}
		
		this.close = function() {
			if(this.options.min.subscribe && this.options.min.attribute) {
				org.kasource.Websocket.unsubscribe(this.options.min.attribute.objectName, this.options.min.attribute.attribute, this.id+"-min", this.options.min.type);	
			}
			if(this.options.max.subscribe && this.options.max.attribute) {
				org.kasource.Websocket.unsubscribe(this.options.max.attribute.objectName, this.options.max.attribute.attribute, this.id+"-max", this.options.max.type);
				
			}
			if(this.options.value.subscribe && this.options.value.attribute) {
				org.kasource.Websocket.unsubscribe(this.options.value.attribute.objectName, this.options.value.attribute.attribute, this.id+"-value", this.options.type);
			}
		}
		
	}


gaugeFactory.get = function(dashboardId, widgetId, options) {
	widgets[widgetId] = new Gauge(dashboardId, widgetId, options);
	widgets[widgetId].initialize();
	widgets[widgetId].render();
	widgets[widgetId].subscribe();
	return widgets[widgetId];
}

Graph = 
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
			                      Highcharts.numberFormat(this.y, 2);
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
			if(!graphPainter.timer) {
				graphPainter.timer = window.setInterval(function(){graphPainter.draw()},1000);
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
			if(indices && jmxValue.value) {
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



graphPainter = {}


graphPainter.addPoint = function(graph) {
		var time = (new Date()).getTime();
		var shift = true;
		var paint = false;
		
		if(graph.dashboardId == currentDasboard) {
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

graphPainter.draw = function() {
	for (var i = 0; i < graphs.length; i++) {
		graphPainter.addPoint(graphs[i]);
	}	
}

graphPainter.remove = function(graph) {
	for (var i = 0; i < graphs.length; i++) {
		if(graphs[i].id == graph.id) {
			graphs.splice(i,1);
			break;
		}
	}
}

graphFactory = {};

graphFactory.get = function(dashboardId, widgetId, options) {
	widgets[widgetId] = new Graph(dashboardId, widgetId, options);
	widgets[widgetId].initialize();
	widgets[widgetId].subscribe();
	
	graphs.push(widgets[widgetId]);
	
	return widgets[widgetId];
}

Pie = 
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
		
		this.render = function() {
			this.chart = new Highcharts.Chart(this.chartOptions);
		}
		
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var indices = widget.dataSeriesIndex[objectName+"."+attribute];
			var yValue =  parseFloat(jmxValue.value);
				
			if(indices && jmxValue.value) {
				for (var i = 0; i < indices.length; i++) {
					var index = indices[i];
					if(widget.options.dataSeries[index].transform) {
						yValue =  parseFloat(widget.options.dataSeries[index].transform(jmxValue.value));
					}
					
					
					if (currentDasboard == widget.dashboardId) {
						widget.chart.series[0].data[index].update(yValue, true);
					} else {
						widget.chart.series[0].data[index].update(yValue, false);
					}
				}
			}
		}
		
		this.close = function() {
			for (var i = 0; i < this.options.dataSeries.length; i++) {
				var objectName = this.options.dataSeries[i].attribute.objectName;
				var attribute = this.options.dataSeries[i].attribute.attribute;
				org.kasource.Websocket.unsubscribe(objectName, attribute, this.id+"-"+i);
			}
		}
}

pieFactory = {};

pieFactory.get = function(dashboardId, widgetId, options) {
	widgets[widgetId] = new Pie(dashboardId, widgetId, options);
	widgets[widgetId].initialize();
	widgets[widgetId].subscribe();
	
	return widgets[widgetId];
}


TextGroup = 
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

textGroupFactory = {};

textGroupFactory.get = function(dashboardId, widgetId, data) {
	widgets[widgetId] = new TextGroup(dashboardId, widgetId, data);
	widgets[widgetId].initialize();
	widgets[widgetId].render();
	widgets[widgetId].subscribe();
	return widgets[widgetId];
}

widgetFactory['textGroup']=textGroupFactory;
widgetFactory['gauge'] = gaugeFactory;
widgetFactory['graph'] = graphFactory;
widgetFactory['pie'] = pieFactory;