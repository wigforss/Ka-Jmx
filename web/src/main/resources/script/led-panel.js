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


org.kasource.jmx.widget.LedPanel = 
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
		
		this.options.layout = this.options.layout.toLowerCase();
		
		var container = document.getElementById(this.id);
	    var paper = new Raphael(this.id); 
	    var set = paper.set();
	    var panelWidth = 0;
		var panelHeight = 0;
		if(this.options.layout == 'vertical') {
			panelHeight = 10 + this.options.data.length*50; 	
			panelWidth = 60;
		} else {
			panelHeight = 60;
		  	panelWidth = 10 + this.options.data.length*50; 	
		}
		var offsetX = 5;
		var offsetY = 5;
		var panel = paper.rect(offsetX, offsetY, panelWidth, panelHeight); 
	    panel.glow({
	        color: '#444',
	        offsety: 3,
	        offsetx: 3
	    });
	    panel.attr({stroke: '#000', 'stroke-width': 4, gradient: '90-#292929-#4D4D4D'});
		set.push(panel);
		for(var i=0; i < this.options.data.length; i++) {
	    	var ledGlow = paper.set();
	    	
	    	var led = {};
	    	var label;
	    	if(this.options.layout == 'vertical') {
	    		led = paper.circle(30 + offsetX, 30+(i*50) + offsetY, 20);
	    		set.push(led);
	    		if(this.options.showLabels) {
	    			label = paper.text(75 + offsetX, 30 + (i*50) + offsetY, options.data[i].title);
	    			
	    		}
	    	} else {
	    		led = paper.circle(30+(i*50)+offsetX, 30+offsetY, 20);
	    		set.push(led);
	    		if(this.options.showLabels) {
	    			label = paper.text(30 + (i*50) + offsetX, 75 + offsetY, options.data[i].title);
	    			
	    		}
	    		
	    	}
	    	if(this.options.showLabels) {
	    		label.attr({"font-size":16, "font-family": "Segoe UI, Arial, Helvetica, sans-serif", "font-weight": "bold", "text-anchor": "start"});
	    		var bbox = label.getBBox();
	    		var labelBox = paper.rect(bbox.x, bbox.y+bbox.height/2-1,bbox.width,2).attr('stroke','none');
	    		
    			label.rotate(this.options.labelRotation,bbox.x,bbox.y);
    			labelBox.rotate(this.options.labelRotation,bbox.x,bbox.y);
    			set.push(label);
    			set.push(labelBox);
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
		
		 var viewBox = set.getBBox();
		 paper.setViewBox(0, 0, viewBox.width + offsetX, viewBox.height + offsetY, true);
		// paper.canvas.setAttribute('preserveAspectRatio', 'none'); 
		
		this.enableLed = function (index, enabled) {
			var led = this.leds[index];
			if(led) {
				if(enabled && !led.enabled) {
					led.graphic.animate({opacity: 1}, 800, 'bounce');
					
					var glow = led.glow;
					setTimeout(function(){glow.show()},800);
					led.enabled = true;
				} else if(!enabled && led.enabled) {
					led.glow.hide();
					led.graphic.animate({opacity: 0.2}, 800, 'bounce');
							
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