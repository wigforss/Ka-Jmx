TrafficLight = 
	function (containerId, title, state, value, prefix, suffix) {
	
		this.id = containerId;
		this.state = 'red';
		if(state) {
			if(state.toLowerCase() == 'yellow') {
				this.state = 'yellow';
			} else if(state.toLowerCase() == 'green') {
				this.state = 'green';
			}
		}
		
	    this.title = '';
	    if(title) {
	    	this.title=title;
	    }
	    
	    this.value = '';
	    if(value) {
	    	this.value = value;
	    }
	    
	    this.prefix = '';
	    if(prefix) {
	    	this.prefix = prefix;
	    }
	    
	    this.suffix = '';
	    if(suffix) {
	    	this.suffix = suffix;
	    }
	    
	    this.getTooltip = function() {
	    	var tooltip = this.title;
		    if(this.value) {
		    	if(this.prefix) {
		    		tooltip = tooltip + ' ' + this.prefix + ' ' + this.value;
		    	} else if(this.suffix) {
		    		tooltip = tooltip + ' ' + this.value + ' ' + suffix; 
		    	} else {
		    		tooltip = tooltip + ' ' + this.value; 
		    	}
		    	
		    }
		    return tooltip;
	    }
	    
	    var container = document.getElementById(this.id);
		var paper = new Raphael(container, container.offsetWidth, container.offsetHeight); 
	    this.redGlow = paper.set();
	    this.yellowGlow = paper.set();
	    this.greenGlow = paper.set();
	    this.rectangle = paper.rect(0, 0, 60, 160); 
	    this.rectangle.glow({
	        color: '#444',
	        offsety: 3,
	        offsetx: 3
	    });
	    
	    
	    var tooltip = this.getTooltip();
	    
	    this.rectangle.attr({stroke: '#000', 'stroke-width': 4, gradient: '90-#292929-#4D4D4D', title: tooltip});
	    this.circleRed = paper.circle(30, 30, 20);
	    this.circleRed.attr({fill: 'rgba(255,0,0,0.2)', stroke: '#292828', 'stroke-width': 2, title: tooltip});
	    this.redGlow.push(this.circleRed.glow({opacity: 0.7, color: "#FF0000", width: 20}));
	    this.redGlow.hide();
	    
	    this.circleYellow = paper.circle(30, 80, 20); 
	    this.circleYellow.attr({fill: 'rgba(255,255,0,0.2)', stroke: '#292828', 'stroke-width': 2, title: tooltip});
	    this.yellowGlow.push(this.circleYellow.glow({opacity: 0.7, color: "#FFFF00", width: 20}));
	    this.yellowGlow.hide();
	    
	    
	    this.circleGreen = paper.circle(30, 130, 20);
	    this.circleGreen.attr({fill: 'rgba(0,255,0,0.2)', stroke: '#292828', 'stroke-width': 2, title: tooltip});
	    this.greenGlow.push(this.circleGreen.glow({opacity: 0.7, color: "#00FF00", width: 20}));
	    this.greenGlow.hide();
	    
	   
	    
	    this.redToYellow = function () {
			this.fromRed();
			this.toYellow();
		}
	    
	    this.fromRed = function() {
	    	this.circleGreen.attr({fill: 'rgba(0,255,0,0.2)'});
			this.greenGlow.hide();
			this.redGlow.hide();
			for(var i=0; i < 8; i++) {
		    	var alpha = 1.0-(i*0.1);
		    	this.circleRed.animate({fill: 'rgba(255,0,0,'+alpha+')'}, 100*i, 'bounce');
		    	
		    }
	    }
	   
		
		this.yellowToRed = function () {
			this.fromYellow();
			this.toRed();
		}
		
		this.toRed = function() {
			for(var i=0; i < 8; i++) {
		    	var alpha = 0.2+(i*0.1);
		    	this.circleRed.animate({fill: 'rgba(255,0,0,'+alpha+')'}, 100*i, 'bounce');
		    }
			var glow = this.redGlow;
			 setTimeout(function(){glow.show()},800);
		}
		
		this.yellowToGreen = function () {
			this.fromYellow();
			this.toGreen();
			
		}
		
		this.toGreen = function() {
			for(var i=0; i < 8; i++) {
		    	var alpha = 0.2+(i*0.1);
		    	this.circleGreen.animate({fill: 'rgba(0,255,0,'+alpha+')'}, 100*i, 'bounce');
		    }
			var glow = this.greenGlow;
			 setTimeout(function(){glow.show()},800);
		}
		
		this.fromYellow = function() {
			this.circleRed.attr({fill: 'rgba(255,0,0,0.2)'});
			this.redGlow.hide();
			this.yellowGlow.hide();
			for(var i=0; i < 8; i++) {
		    	var alpha = 1.0-(i*0.1);
		    	this.circleYellow.animate({fill: 'rgba(255,255,0,'+alpha+')'}, 100*i, 'bounce');
		    }
		}
		
		
		this.toYellow = function() {
			for(var i=0; i < 8; i++) {
		    	var alpha = 0.2+(i*0.1);
		    	this.circleYellow.animate({fill: 'rgba(255,255,0,'+alpha+')'}, 100*i, 'bounce');
		    }
			var glow = this.yellowGlow;
			setTimeout(function(){glow.show()},800);
		}
		
		this.fromGreen = function() {
			this.circleRed.attr({fill: 'rgba(255,0,0,0.2)'});
			this.redGlow.hide();
			this.greenGlow.hide();
			for(var i=0; i < 8; i++) {
		    	var alpha = 1.0-(i*0.1);
		    	this.circleGreen.animate({fill: 'rgba(0,255,0,'+alpha+')'}, 100*i, 'bounce');
		    }
		}
		
		this.greenToYellow = function () {
			this.fromGreen();
			this.toYellow();
			
		}
		
		this.setValue = function(newValue) {
			this.value = newValue;
			var newTooltip = this.getTooltip();
			this.rectangle.attr({title: newTooltip});
			this.circleRed.attr({title: newTooltip});
			this.circleYellow.attr({title: newTooltip});
			this.circleGreen.attr({title: newTooltip});
		}
		
		this.setState = function(newState) {
			switch(newState) {
		    case 'red':
		    	switch(this.state) {
		    	case 'red':
		    		this.toRed();
		    		break;
		    	case 'yellow':
		    		this.yellowToRed();
		    		break;
		    	case 'green':
		    		this.fromGreen();
		    		this.toRed();
		    		break;
		    	}
				this.state = 'red';
		    	break;
		    case 'yellow':
		    	switch(this.state) {
		    	case 'red':
		    		this.redToYellow();
		    		break;
		    	case 'yellow':
		    		this.toYellow();
		    		break;
		    	case 'green':
		    		this.greenToYellow();
		    		break;
		    	}
		  
				this.state = 'yellow';
		    	break;
		    case 'green':
		    	switch(this.state) {
		    	case 'red':
		    		this.fromRed();
		    		this.toGreen();
		    		break;
		    	case 'yellow':
		    		this.yellowToGreen();
		    		break;
		    	case 'green':
		    		this.toGreen();
		    		break;
		    	}
				this.state = 'green';
		    	break;
		    }
			
		}
		
		 this.setState(this.state);
}