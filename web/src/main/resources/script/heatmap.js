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


org.kasource.jmx.widget.HeatMap = function(containerId, options) {
	 
	  this.drawLegend = function(width, height, paper, set) {
			
	    	var legendWidth;
	    	var legendHeight;
	    	var legendLength = this.colorSchema.getTransitions().length * 50;
	    
	    	this.options.legendLayout = this.options.legendLayout.toLowerCase();
	    	var legendX;
	    	var legendY;
	    
	    	if(this.options.legendLayout === "horizontal") {
	    		legendWidth = legendLength;
	    		legendHeight = 50;
	    		legendX = 5;
	    		legendY = height+20;
	    		if(width > legendLength) {
	    			legendWidth = width;
	    			legendWidth -= 5;
	    		}
	    		
	    	} else {
	    		legendHeight = legendLength;
	    		legendWidth = 50;
	    		legendX = width+20;
	    		legendY = 10;
	    		if(height > legendLength) {
	    			legendHeight = height;
	    		} 		
	    	}
	    	    
	    	var legendBox =  paper.rect(legendX, legendY, legendWidth, legendHeight);
	    	legendBox.attr({stroke: '#000', 'stroke-width': 2});
	    	legendBox.glow({
	        	color: '#444',
	        	offsety: 3,
	        	offsetx: 3
	    	});
	    	set.push(legendBox);
			// Draw color transitions	  
	    	var transitions = this.colorSchema.getTransitions();    
	    	if(this.options.legendLayout === "horizontal") {
	    		this.drawHorizontalLegend(legendX, legendY, legendWidth, legendHeight, transitions, paper, set);
	    	} else {
	    		this.drawVerticalLegend(legendX, legendY, legendWidth, legendHeight, transitions, paper, set);
	    	}
		
	}
	
	 this.drawHorizontalLegend = function(legendX, legendY, legendWidth, legendHeight, transitions, paper, set) {
		for (var i = 0; i < transitions.length; i++) {
 		 var transistion = transitions[i];
 		 var transistionBox = paper.rect(legendX+(legendWidth / transitions.length)*i, legendY, legendWidth / transitions.length, legendHeight);
 		 var gradient = '0-'+transistion.fromColor+'-'+transistion.toColor;
		     transistionBox.attr({stroke: 'none', gradient: gradient, title: 'Heat: ' + transistion.from +' - ' + transistion.to});
		    
		     var transistionLabelFrom = paper.text(legendX+(legendWidth / transitions.length)*i, legendY+70, transistion.from);
		     transistionLabelFrom.attr({"font-size":16, "font-family": "Segoe UI, Arial, Helvetica, sans-serif", "font-weight": "bold"});
		     set.push(transistionLabelFrom);
		     var fromLine = paper.path('M '+(legendX+(legendWidth / transitions.length)*i)+' '+(legendY + 50)+' l 0 8');
		     fromLine.attr({'stroke-width': 2});
		     
		     var transistionLabelTo = paper.text(legendX+(legendWidth / transitions.length)*i+(legendWidth / transitions.length), legendY+70, transistion.to);
		     transistionLabelTo.attr({"font-size":16, "font-family": "Segoe UI, Arial, Helvetica, sans-serif", "font-weight": "bold" });
		     set.push(transistionLabelTo);
		     var toLine = paper.path('M '+(legendX+(legendWidth / transitions.length)*i+(legendWidth / transitions.length))+' '+(legendY + 50)+' l 0 8');
		     toLine.attr({'stroke-width': 2});
		}
	}
	
	
	 this.drawVerticalLegend = function(legendX, legendY, legendWidth, legendHeight, transitions, paper, set) {
		
		 var count=0;
		 for (var i = transitions.length-1; i >=0; i--) {
			 var transistion = transitions[i];
			 var transistionBox = paper.rect(legendX, legendY + (legendHeight / transitions.length)*count, legendWidth, legendHeight / transitions.length);
			 var gradient = '90-'+transistion.fromColor+'-'+transistion.toColor;
		     transistionBox.attr({stroke: 'none', gradient: gradient, title: 'Heat: ' + transistion.from +' - ' + transistion.to});
		     var transistionLabelFrom = paper.text(legendX+60, legendY + (legendHeight / transitions.length)*count+(legendHeight / transitions.length), transistion.from);
		     transistionLabelFrom.attr({"font-size":16, "font-family": "Segoe UI, Arial, Helvetica, sans-serif", "font-weight": "bold", "text-anchor": "start"});
		     set.push(transistionLabelFrom);
		     var fromLine = paper.path('M '+(legendX+50)+' '+(legendY + (legendHeight / transitions.length)*count+(legendHeight / transitions.length))+' l 8 0');
		     fromLine.attr({'stroke-width': 2});
		     var transistionLabelTo = paper.text(legendX+60, legendY + (legendHeight / transitions.length)*count, transistion.to);
		     transistionLabelTo.attr({"font-size":16, "font-family": "Segoe UI, Arial, Helvetica, sans-serif", "font-weight": "bold", "text-anchor": "start"});
		     set.push(transistionLabelTo);
		   
		     var toLine = paper.path('M '+(legendX+50)+' '+(legendY + (legendHeight / transitions.length)*count)+' l 8 0');
		     toLine.attr({'stroke-width': 2});
		     count++;
		 }
	}
	
	this.setHeat = function(row, column, heat, value) {
		heat = Math.round(parseFloat(heat));
		nodeData = this.options.data[row][column];
		if (nodeData) {
			if(heat > 100) {
				heat = 100;
			} else if(heat < 0) {
				heat = 0;
			}
			nodeData.heat = heat;
			nodeData.value = value;
			node = this.nodes[row][column];
			if (node && node.hasOwnProperty("nodeX")){
				var color = this.colorSchema.getColor(heat);
				var title = 'Heat ('+heat+')';
    			if(nodeData.title) {
    				title = nodeData.title + ' (' + heat + ')';
    			}
				$(node.node).qtip('destroy');
				 $(node.node).qtip({ content: { text: nodeData.value, title: title },
 		            style: {
 		                background: 'rgba(255,255,255,0.7)',
 		                color: '#000000',
 		                border: { width: 2, radius: 3, color: color },
 		                'font-size': 12
 		            },
 		            position: {
 		                corner: {
 		                    target: 'topRight',
 		                    tooltip: 'bottomLeft'
 		                }
 		            }
 		        });
				
				
				
				node.toFront();
				node.animate({fill: color, 'stroke-width': 2, stroke: '#FFFFFF'}, 500, 'bounce', function(){this.animate({'stroke-width': 1, stroke: '#000'}, 500)});
				
				
				
			}
		}
		
	}
	
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
    
    var defaultOptions = {shape: 'square', colorSchema:"heat_map", showLegend: false, legendLayout: "vertical",  "background-color-from": '#292929',  "background-color-to": '#4D4D4D', gradientAngle: 90}
	this.containerId = containerId;
    this.options = this.mergeOptions(defaultOptions, options);
	this.options.colorSchema = this.options.colorSchema.toLowerCase();
	this.colorSchema = org.kasource.color.colorSchemas[this.options.colorSchema];
	this.nodes = [];
				
	var width = 0;
	var height = this.options.data.length * 50;
	
	for (var i=0; i < this.options.data.length; i++) {
		if(this.options.data[i].length * 50 > width) {
			width = this.options.data[i].length * 50;
		}
	}
    var paper = new Raphael(this.containerId); 
    var set = paper.set();
    
    if(this.options.showLegend === true){
    	this.drawLegend(width, height, paper, set);
    }
    
    var mapBackground = paper.rect(5, 8, width, height); 
    
    mapBackground.glow({
        color: '#444',
        offsety: 3,
        offsetx: 3
    });
    
    var gradient = this.options.gradientAngle+'-'+this.options["background-color-from"]+'-'+this.options["background-color-to"];
    
    mapBackground.attr({stroke: '#000', 'stroke-width': 3, gradient: gradient});
    set.push(mapBackground);
   
    
    for (var y = 0; y < this.options.data.length; y++) {
    	var columns = new Array();
    	for (var x=0; x < this.options.data[y].length; x++) {
    		if(this.options.data[y][x] && this.options.data[y][x].heat != undefined && this.options.data[y][x].heat != null) {
    			var node = this.options.data[y][x];
    			var shape = paper.rect(x*50+5, y*50+8, 50, 50);
    			
    			shape.nodeX = x;
    			shape.nodeY = y;
    			
    			shape.mouseover(function(){
    				this.toFront();
    				
    				var translateX = 28 - this.nodeX * 5 + this.nodeY * 27;
    				this.animate({transform: 't 0,0 m 1.1,0,-0.6,1,0,0 t ' + translateX + ',0'}, 500, 'bounce');
    				
    			});
    			
    			shape.mouseout(function(){ 
    				this.animate({transform: 'r0'}, 500, 'bounce');
    					
    			});
    			var heat = Math.round(parseFloat(node.heat));
    			var color = this.colorSchema.getColor(heat);
    			
    			shape.attr({stroke: '#000', 'stroke-width': 1, fill: color});
    			var title = 'Heat ('+heat+')';
    			if(node.title) {
    				title = node.title + '(' + heat + ')';
    			}
    			
    			 $(shape.node).qtip({ content: { text: node.value, title: title },
    		            style: {
    		                background: 'rgba(255,255,255,0.7)',
    		                color: '#000000',
    		                border: { width: 2, radius: 3, color: color },
    		                'font-size': 12
    		            },
    		            position: {
    		                corner: {
    		                    target: 'topRight',
    		                    tooltip: 'bottomLeft'
    		                }
    		            }
    		        });
    			
    			
    			columns.push(shape);
    		} else {
    			columns.push({});
    		}
	    }
    	this.nodes.push(columns);
    }
    
    var viewBox = set.getBBox();
   
    paper.setViewBox(0, 0, viewBox.width+8+30, viewBox.height+15, true);	  
	
 }