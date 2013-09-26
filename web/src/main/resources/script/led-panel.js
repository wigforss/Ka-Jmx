LedPanel = 
	function (containerId, options) {
		this.options = {layout: 'vertical', showLabels: true, color: '#00FF00', labelRotation: 45, data:[]};
		this.id = containerId;
		this.leds = [];
		
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
		
		var container = document.getElementById(this.id);
	    var paper = new Raphael(container, container.offsetWidth, container.offsetHeight); 
	    
	    var panelWidth = 0;
		var panelHeight = 0;
		if(this.options.layout == 'horizontal') {
			panelHeight = 10 + this.options.data.length*50; 	
			panelWidth = 60;
		} else {
			panelHeight = 60;
		  	panelWidth = 10 + this.options.data.length*50; 	
		}
		
		var panel = paper.rect(0, 0, panelWidth, panelHeight); 
	    panel.glow({
	        color: '#444',
	        offsety: 3,
	        offsetx: 3
	    });
	    panel.attr({stroke: '#000', 'stroke-width': 4, gradient: '90-#292929-#4D4D4D'});
		
		for(var i=0; i < this.options.data.length; i++) {
	    	var ledGlow = paper.set();
	    	
	    	var led = {};
	    	var label;
	    	if(this.options.layout == 'horizontal') {
	    		led = paper.circle(30, 30+(i*50), 20);
	    		if(this.options.showLabels) {
	    			label = paper.text(80, 30+(i*50), options.data[i].title);
	    		}
	    	} else {
	    		led = paper.circle(30+(i*50), 30, 20);
	    		if(this.options.showLabels) {
	    			label = paper.text(30+(i*50), 80, options.data[i].title);
	    		}
	    		
	    	}
	    	if(this.options.showLabels) {
	    		label.attr({"font-size":16, "font-family": "Segoe UI, Arial, Helvetica, sans-serif", "font-weight": "bold"});
	    		var bbox = label.getBBox();
	    		var labelBox = paper.rect(bbox.x, bbox.y+bbox.height/2-1,bbox.width,2).attr('stroke','none');
	    		
    			label.rotate(this.options.labelRotation);
    			labelBox.rotate(this.options.labelRotation);
    			
    			ledGlow.push(labelBox.glow({color: this.options.color, opacity: 0.3, width: 10}));
	    	}
	    	ledGlow.push(led.glow({opacity: 0.7, color: this.options.color, width: 20}));
	    	
	    	
	    	if(this.options.data[i].enabled) {
	    		led.attr({fill: this.options.color, stroke: '#292828', 'stroke-width': 2, opacity: 1.0, title: this.options.data[i].title});
	    	} else {
	    		led.attr({fill: this.options.color, stroke: '#292828', 'stroke-width': 2, opacity: 0.2, title: this.options.data[i].title});
	    		ledGlow.hide();
	    	}
	    	
			this.leds.push({graphic: led, glow: ledGlow, enabled: this.options.data[i].enabled, title: this.options.data[i].title});
	    }
		
		this.enableLed = function (index, enabled) {
			var led = this.leds[index];
			if(led) {
				if(enabled && !led.enabled) {
					for(var i=0; i < 8; i++) {
						var alpha = 0.2+(i*0.1);
						led.graphic.animate({opacity: alpha}, 100*i, 'bounce');
					}
					var glow = led.glow;
					setTimeout(function(){glow.show()},800);
					led.enabled = true;
				} else if(!enabled && led.enabled) {
					led.glow.hide();
					for(var i=0; i < 8; i++) {
						var alpha = 1.0-(i*0.1);
						led.graphic.animate({opacity: alpha}, 100*i, 'bounce');
			    	
					}		
					led.enabled=false;
				}
			}
		}
		
		this.isEnabled = function(index) {
			var led = this.leds[index];
			if(led) {
				return led.endabled;
			}
			return false;
		}
		
}