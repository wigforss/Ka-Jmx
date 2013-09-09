widgets = [];

widgetFactory = [];

gaugeFactory = {};


Gauge = 
	function (id, options) {
		this.id = id;
		this.options = options;
		this.gauge;
		this.gaugeOptions;
		this.listeners={};
		
		this.initialize = function() {
			var minValue = this.options.min.value;
			var maxValue = this.options.max.value;
			var currentValue = this.options.value.value;
			if(this.options.min.jsFunction) {
				this.options.min.transform = eval('('+this.options.min.jsFunction+')');
				var minValue = this.options.min.transform(minValue);
			}
			if(this.options.max.jsFunction) {
				this.options.max.transform = eval('('+this.options.max.jsFunction+')');
				var maxValue = this.options.min.transform(maxValue);
			}
			if(this.options.value.jsFunction) {
				this.options.value.transform = eval('('+this.options.value.jsFunction+')');
				var currentValue = this.options.value.transform(currentValue);
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
					valueKinds.push('max')
				}		
				org.kasource.Websocket.subscribe(this.options.max.attribute.objectName, this.options.max.attribute.attribute, id+"-max", this.refreshValue, this);
			}
			if(this.options.value.subscribe && this.options.value.attribute) {
				var types = this.listeners[this.options.max.attribute.objectName+"."+this.options.max.attribute.attribute];
				if(!types) {
					this.listeners[this.options.value.attribute.objectName+"."+this.options.value.attribute.attribute]='value';
				} else {
					valueKinds.push('value');
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
						var minValue = jmxValue.value;
						if(widget.options.min.transform) {
							var minValue = widget.options.min.transform(minValue);
						}
						widget.gaugeOptions['min'] = minValue;
						widget.gauge = new JustGage(this.gaugeOptions);
						break;
					case 'max':
						var maxValue = jmxValue.value;
						if(widget.options.max.transform) {
							maxValue = widget.options.max.transform(maxValue);
						}
						this.gaugeOptions['max'] = maxValue;
						widget.gauge = new JustGage(this.gaugeOptions);
						break;
					case 'value':
						var currentValue = jmxValue.value;
						if(widget.options.value.transform) {
							currentValue = widget.options.value.transform(currentValue);
						}
						gauge.refresh(currentValue);
						break;
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


gaugeFactory.get = function(widgetId, options) {
	widgets[widgetId] = new Gauge(widgetId, options);
	widgets[widgetId].initialize();
	widgets[widgetId].render();
	widgets[widgetId].subscribe();
	return widgets[widgetId];
}





FlotGraph = 
	function (id, options) {
		this.id = id;
		this.options = options;
		this.plot={};
		this.plotOptions={};
		this.dataSet=[];
		this.dataSeriesIndex={};
		this.currentValues=[];
		this.color = ["#FF000","#0062E3","#00D604", "#FFF700", "#D600BD"];
		
		this.initialize = function() {
			
			for (var i = 0; i < this.options.dataSeries.length; i++) {
				
				this.dataSet.push({
			        label: this.options.dataSeries[i].label,
			        data: [],
			        color: this.color[i],
			        points: {
			            fillColor: this.color[i],
			            show: true
			        },
			        lines: {
			            show: true
			        }
			    });
				this.currentValues.push(0);
				
			}
			
			
			this.plotOptions = {
					 series: {
				            shadowSize: 5
				        },
				        xaxis: {
				            mode: "time",
				            timeformat: "%H:%M",
				            color: "black",
				            position: "bottom",				          
				            axisLabel: "Hour and Minutes",
				            axisLabelUseCanvas: true,
				            axisLabelFontSizePixels: 12,
				            axisLabelFontFamily: 'Verdana, Arial',
				            axisLabelPadding: 5
				        },
				        yaxis: {
				            color: "black",
				            tickDecimals: 2,
				            axisLabel: "Gold Price  in USD/oz",
				            axisLabelUseCanvas: true,
				            axisLabelFontSizePixels: 12,
				            axisLabelFontFamily: 'Verdana, Arial',
				            axisLabelPadding: 5
				        },
				        legend: {
				            noColumns: 1,
				            labelFormatter: function(label, series) {
				                return "<font color=\"white\">" + label + "</font>";
				            },
				            backgroundColor: "#000",
				            backgroundOpacity: 0.8,
				            labelBoxBorderColor: "#000000",
				            position: "nw"
				        },
				        grid: {
				            hoverable: true,
				            borderWidth: 3,
				            mouseActiveRadius: 50,
				            backgroundColor: {
				                colors: ["#ffffff", "#EDF5FF"]
				            },
				            axisMargin: 20
				        }	
			};
			
			
			
			this.plot = $.plot("#"+this.id, this.dataSet, this.plotOptions);
			
			
		}
		
		this.render = function() {
			this.plot = $.plot("#"+this.id, this.dataSet, this.plotOptions);
		}
		
		this.subscribe = function() {
			for (var i = 0; i < this.options.dataSeries.length; i++) {
				var objectName = this.options.dataSeries[i].attribute.objectName;
				var attribute = this.options.dataSeries[i].attribute.attribute;
				this.dataSeriesIndex[objectName+"."+attribute]=i;
				org.kasource.Websocket.subscribe(objectName, attribute, this.id+"-"+i, this.refreshValue, this);
			}
			var widget = this;
			this.timerTask = setInterval(function(){
				var time = new Date().getTime();
				for (var i = 0; i < widget.currentValues.length; i++) {
					widget.dataSet[i].data.push([time, widget.currentValues[i]]);
				}
				widget.plot = $.plot("#"+widget.id, widget.dataSet, widget.plotOptions);
			}, 1000);
		}
		
		
		
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var index = widget.dataSeriesIndex[objectName+"."+attribute];
			widget.currentValues[index]=jmxValue.value;
			
			
			
		}
		
		this.close = function() {
			for (var i = 0; i < this.options.dataSeries.length; i++) {
				var objectName = this.options.dataSeries[i].attribute.objectName;
				var attribute = this.options.dataSeries[i].attribute.attribute;
				org.kasource.Websocket.unsubscribe(objectName, attribute, this.id+"-"+i);
			}
			if (this.timerTask) {
				window.clearInterval(this.timerTask);
			}
		}
}

Graph = 
	function (id, options) {
		this.id = id;
		this.options = options;
		this.chart = {};
		this.chartOptions={};
		this.currentValues=[];
		this.dataSeriesIndex={};
		
		this.initialize = function() {
			var series = [];
			var samples = this.options.samples;
			var title = this.options.title;
			var yAxisLabel = this.options.yAxisLabel;
			var useLegend = (this.options.dataSeries.length > 1);
			for (var i = 0; i < this.options.dataSeries.length; i++) {
				
				var yValue = parseFloat(this.options.dataSeries[i].value);
				if(this.options.dataSeries[i].jsFunction) {
					this.options.dataSeries[i].transform = eval('('+this.options.dataSeries[i].jsFunction+')');
					yValue = parseFloat(this.options.dataSeries[i].transform(this.options.dataSeries[i].value));
				}
				
				
				
				series.push({
		              name: this.options.dataSeries[i].label,
			             
		              data: (function() {
		                  // generate an array of random data
		                  var data = [],
		                      time = (new Date()).getTime(),
		                      i;
		  
		                  for (i = 0; i < samples; i++) {
		                      data.push({
		                    	  x: time,
		                          y: yValue
		                      });
		                  }
		                  return data;
		            	 
		              })()
		          });
				this.currentValues.push(yValue);
			}
			var values = this.currentValues;
			
			
			Highcharts.setOptions({
			    global: {
			        useUTC: false
			    }
			});
			var values = this.currentValues;
			this.chartOptions = {
			         chart: {
			          	renderTo: this.id,
			              type: 'spline',
			              animation: Highcharts.svg, // don't animate in old IE
			              marginRight: 10,
			              events: {
			                  load: function() {
			  
			                      // set up the updating of the chart each second
			                      var series = this.series;
			                      setInterval(function() {
			                    	  var time = (new Date()).getTime();
			                    	  for (var i = 0; i < values.length; i++) {
			                    		  var x = time, // current time
			                              	y = values[i];
			                    		  series[i].addPoint([x, y], true, true);
			                    	  }
			                      }, 1000);
			                  }
			              }
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
			              enabled: useLegend
			          },
			          exporting: {
			              enabled: false
			          },
			          series: series
			      };
			this.chart = new Highcharts.Chart(this.chartOptions);
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
				org.kasource.Websocket.subscribe(objectName, attribute, this.id+"-"+i, this.refreshValue, this);
			}
			
		}
		
		
		
		this.refreshValue = function(jmxValue, widget) {
			var attribute = jmxValue.key.attributeName;
			var objectName = jmxValue.key.name;
			var indices = widget.dataSeriesIndex[objectName+"."+attribute];
			if(indices) {
				for (var i = 0; i < indices.length; i++) {
					var index = indices[i];
					if(widget.options.dataSeries[i].transform) {
						widget.currentValues[index]=widget.options.dataSeries[i].transform(jmxValue.value);
					} else {
						widget.currentValues[index]=jmxValue.value;
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

graphFactory = {};


graphFactory.get = function(widgetId, options) {
	widgets[widgetId] = new Graph(widgetId, options);
	widgets[widgetId].initialize();
	widgets[widgetId].subscribe();
	return widgets[widgetId];
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



function removePanel(panelId, widgetId) {
	
	$(".gridster ul").data('gridster').remove_widget($("#" + panelId), function(){
		if(widgetId) {
			var widget = widgets[widgetId];
			if(widget && widget.close) {
				widget.close();
			}
		}
	});
}

function togglePanelSize(panelId, widgetId) {
	var isMax = $("#" + panelId).attr("maximized");

	var x = $("#" + panelId).attr("data-sizex");
	var y = $("#" + panelId).attr("data-sizey");

	if (isMax && isMax == 'true') {
		x = x / 2;
		y = y / 2;
		$("#" + panelId).attr("maximized", "false");
		
	} else {
		x = x * 2;
		y = y * 2;
		$("#" + panelId).attr("maximized", "true");
		
	}

	
	 $(".gridster ul").data('gridster').resize_widget($("#" + panelId), x, y);
	
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

TextGroup = 
	function (id, data) {
		this.id = id;
		this.data = data;
		this.title = data.title;
		this.listeners = {};
		
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
				if(this.data.value[i].transform) {
					table += this.data.value[i].transform(this.data.value[i].value);
				} else {
					table += this.data.value[i].value;
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
					if(item.transform) {
						value = item.transform(value);
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

textGroupFactory.get = function(widgetId, data) {
	widgets[widgetId] = new TextGroup(widgetId, data);
	widgets[widgetId].initialize();
	widgets[widgetId].render();
	widgets[widgetId].subscribe();
	return widgets[widgetId];
}

widgetFactory['textGroup']=textGroupFactory;
widgetFactory['gauge'] = gaugeFactory;
widgetFactory['graph'] = graphFactory;