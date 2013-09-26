TrafficLight = 
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
	    
	    var container = document.getElementById(this.id);
		var paper = new Raphael(container, container.offsetWidth, container.offsetHeight); 
	    var redGlow = paper.set();
	    var yellowGlow = paper.set();
	    var greenGlow = paper.set();
	    var rectangle = paper.rect(0, 0, 60, 160); 
	    rectangle.glow({
	        color: '#444',
	        offsety: 3,
	        offsetx: 3
	    });
	    
	    this.lights={};
	    
	    var tooltip = this.getTooltip();
	    
	    rectangle.attr({stroke: '#000', 'stroke-width': 4, gradient: '90-#292929-#4D4D4D', title: tooltip});
	    var circleRed = paper.circle(30, 30, 20);
	    circleRed.attr({fill: this.options.red, opacity:0.2, stroke: '#292828', 'stroke-width': 2, title: tooltip});
	    redGlow.push(circleRed.glow({opacity: 0.7, color: this.options.red, width: 20}));
	    redGlow.hide();
	    
	    this.lights['red'] = {graphics: circleRed, glow: redGlow};
	    
	    var circleYellow = paper.circle(30, 80, 20); 
	    circleYellow.attr({fill: this.options.yellow, opacity:0.2, stroke: '#292828', 'stroke-width': 2, title: tooltip});
	    yellowGlow.push(circleYellow.glow({opacity: 0.7, color: this.options.yellow, width: 20}));
	    yellowGlow.hide();
	    
	    this.lights['yellow'] = {graphics: circleYellow, glow: yellowGlow};
	    
	    var circleGreen = paper.circle(30, 130, 20);
	    circleGreen.attr({fill: this.options.green, opacity: 0.2, stroke: '#292828', 'stroke-width': 2, title: tooltip});
	    greenGlow.push(circleGreen.glow({opacity: 0.7, color: this.options.green, width: 20}));
	    greenGlow.hide();
	    
	    this.lights['green'] = {graphics: circleGreen, glow: greenGlow};
	   
	   
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
	    			for(var i=0; i < 8; i++) {
	    				var alpha = 1.0-(i*0.1);
	    				this.lights[fromLight].graphics.animate({opacity: alpha}, 100*i, 'bounce');
			    	
	    			}
	    		}
	    		if(this.lights[toLight]) {
	    			for(var i=0; i < 8; i++) {
	    				var alpha = 0.2+(i*0.1);
	    				this.lights[toLight].graphics.animate({opacity: alpha}, 100*i, 'bounce');
	    			}
	    			var glow = this.lights[toLight].glow;
	    			setTimeout(function(){glow.show()},800);
	    		}
	    	}
	    }
	    
	  
		
		this.setValue = function(newValue) {
			this.options.value = newValue;
			var newTooltip = this.getTooltip();
		
			for (var key in this.lights) {
				this.lights[key].graphics.attr({title: newTooltip});
			}
		}
		
		this.setState = function(newState) {
			this.transition(this.state, newState);
			this.state = newState;
			this.options.state = newState;
		}
		
		this.setState(this.options.state);
}