if(typeof org=='undefined') {
	org = {};
}
if(!org.hasOwnProperty("kasource")) {
	org.kasource = {};
}
if(!(org.kasource.hasOwnProperty("jmx"))) {
	org.kasource.jmx = {};
} 
if(!(org.kasource.jmx.hasOwnProperty("widget"))) {
	org.kasource.jmx.widget = {};
} 


org.kasource.jmx.widget.TrafficLight = 
	function (containerId, options) {
		this.options = {title: '', state: 'none', value: '', prefix: '', suffix: '', red:'#FF0000', yellow: '#FFFF00', green: '#00FF00'};
		this.id = containerId;
		this.state = 'none';
		this.mergeOptions = function(options, overrideOptions) {
			if (overrideOptions != null) {
				for ( var key in overrideOptions) {
					if (overrideOptions.hasOwnProperty(key)) {
					options[key] = overrideOptions[key];
					}
				}
			}
			return options;
		}
	
		if(options) {
			this.options = this.mergeOptions(this.options, options);
		}
		
		
		
	   
	    
	    this.getTooltip = function() {
	    
		    if(this.options.value) {
		    	if(this.options.prefix) {
		    		return this.options.title + ' ' + this.options.prefix + ' ' + this.options.value;
		    	} else if(this.options.suffix) {
		    		return this.options.title + ' ' + this.options.value + ' ' + this.options.suffix; 
		    	} else {
		    		return this.options.title + ' ' + this.options.value; 
		    	}
		    	
		    }
		    return this.options.title;
	    }
	    
	   
		var paper = new Raphael(this.id); 
		var set = paper.set();
	    var redGlow = paper.set();
	    var yellowGlow = paper.set();
	    var greenGlow = paper.set();
	    
	    var offsetX=5;
	    var offsetY=5;
	    
	    var rectangle = paper.rect(offsetX, offsetY, 60, 160); 
	    rectangle.glow({
	        color: '#444',
	        offsety: 3,
	        offsetx: 3
	    });
	    set.push(rectangle);
	    this.lights={};
	    
	    var tooltip = this.getTooltip();
	    
	    rectangle.attr({stroke: '#000', 'stroke-width': 4, gradient: '90-#292929-#4D4D4D'});
	    var circleRed = paper.circle(30 + offsetX, 30 + offsetY, 20);
	    circleRed.attr({fill: this.options.red, opacity:0.2, stroke: '#292828', 'stroke-width': 2});
	    $(circleRed.node).qtip({ content: { text: tooltip},
            style: {
                background: 'rgba(255,255,255,0.7)',
                color: '#000000',
                border: { width: 2, radius: 3, color: '#000000'},
                'font-size': 12
            },
            position: {
                corner: {
                    target: 'topRight',
                    tooltip: 'bottomLeft'
                }
            }
        });
	
	    
	    redGlow.push(circleRed.glow({opacity: 0.7, color: this.options.red, width: 20}));
	    redGlow.hide();
	    set.push(circleRed);
	    
	    
	    this.lights['red'] = {graphics: circleRed, glow: redGlow};
	    
	    var circleYellow = paper.circle(30 + offsetX, 80 + offsetY, 20); 
	    circleYellow.attr({fill: this.options.yellow, opacity:0.2, stroke: '#292828', 'stroke-width': 2});
	    $(circleYellow.node).qtip({ content: { text: tooltip },
            style: {
                background: 'rgba(255,255,255,0.7)',
                color: '#000000',
                border: { width: 2, radius: 3, color: '#000000'},
                'font-size': 12
            },
            position: {
                corner: {
                    target: 'topRight',
                    tooltip: 'bottomLeft'
                }
            }
        });
	    yellowGlow.push(circleYellow.glow({opacity: 0.7, color: this.options.yellow, width: 20}));
	    yellowGlow.hide();
	    set.push(circleYellow);
	    this.lights['yellow'] = {graphics: circleYellow, glow: yellowGlow};
	    
	    var circleGreen = paper.circle(30 + offsetX, 130 + offsetY, 20);
	    circleGreen.attr({fill: this.options.green, opacity: 0.2, stroke: '#292828', 'stroke-width': 2});
	    $(circleGreen.node).qtip({ content: { text: tooltip },
            style: {
                background: 'rgba(255,255,255,0.7)',
                color: '#000000',
                border: { width: 2, radius: 3, color: '#000000'},
                'font-size': 12
            },
            position: {
                corner: {
                    target: 'topRight',
                    tooltip: 'bottomLeft'
                }
            }
        });
	    greenGlow.push(circleGreen.glow({opacity: 0.7, color: this.options.green, width: 20}));
	    greenGlow.hide();
	    set.push(circleGreen);
	    this.lights['green'] = {graphics: circleGreen, glow: greenGlow};
	   
	    var viewBox = set.getBBox();
		paper.setViewBox(0, 0, viewBox.width + offsetX, viewBox.height + offsetY, true);
	    
	    
	    this.transition = function(fromLight, toLight) {
	    	
	    	if(fromLight != toLight) {
	    		var otherLight;
	    		for (var key in this.lights) {
	    		    if(key != fromLight && key != toLight) {
	    		    	otherLight = key;
	    		    }
	    		}
	    		
	    		
	    		if(this.lights[otherLight]) {
	    			this.lights[otherLight].graphics.attr({opacity: 0.2});
	    			this.lights[otherLight].glow.hide();
	    		}
	    
	    		if(this.lights[fromLight]) {
	    			this.lights[fromLight].glow.hide();
	    			this.lights[fromLight].graphics.animate({opacity: 0.2}, 800, 'bounce');
	    		
	    		}
	    		if(this.lights[toLight]) {
	    			this.lights[toLight].graphics.animate({opacity: 1}, 800, 'bounce');
	    			var glow = this.lights[toLight].glow;
	    			setTimeout(function(){glow.show()},800);
	    		}
	    	}
	    }
	    
	  
		
		this.setValue = function(newValue) {
			this.options.value = newValue;
			var newTooltip = this.getTooltip();
			var color = '#000000';
			switch(this.state) {
			case 'red':
				color = this.options.red;
				break;
			case 'yellow':
				color = this.options.yellow;
				break;
			case 'green':
				color = this.options.green;
				break;
			}
			
			for (var key in this.lights) {
				var circle = this.lights[key].graphics;
				$(circle.node).qtip('destroy');
				$(circle.node).qtip({ content: { text: newTooltip},
		            style: {
		                background: 'rgba(255,255,255,0.65)',
		                color: '#000000',
		                border: { width: 2, radius: 3, color: color},
		                'font-size': 12
		            },
		            position: {
		                corner: {
		                    target: 'topRight',
		                    tooltip: 'bottomLeft'
		                }
		            }
		        });
				//$(circle.node).qtip('option', 'content.text', newTooltip);
				//$(circle.node).qtip('option', 'style.border.color', color);
				
			}
		}
		
		this.setState = function(newState) {
			if(newState) {
				newState = newState.toLowerCase();
				this.transition(this.state, newState);
				this.state = newState;
				this.options.state = newState;
			}
		}
		
		this.setState(this.options.state);
		this.setValue(this.options.value);
}