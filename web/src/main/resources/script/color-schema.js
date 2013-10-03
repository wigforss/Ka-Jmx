if(typeof org=='undefined') {
	org = {};
}
if(!org.hasOwnProperty("kasource")) {
	org.kasource = {};
}
if(!(org.kasource.hasOwnProperty("color"))) {
	org.kasource.color = {};
}

org.kasource.color.ColorGenerator = function() {
	this.generateColors = function (fromColor, toColor, samples) {
		var colors = [];
		for(var i=0; i < samples; i++) {
			colors.push(this.generateBlend(fromColor, toColor, (i / samples)));
		}
		return colors;
	}
	
	this.generateBlend =  function(fromColor, toColor, blendPercentage) {
		fromColor = fromColor.trim();
		toColor = toColor.trim();
		if(fromColor.charAt(0) == '#'){
			fromColor = fromColor.substring(1);
		}
		if(toColor.charAt(0) == '#'){
			toColor = toColor.substring(1);
		}
		
		var from = {red: parseInt(fromColor.substring(0,2), 16), green: parseInt(fromColor.substring(2,4), 16), blue: parseInt(fromColor.substring(4,6), 16)};
		var to = {red: parseInt(toColor.substring(0,2), 16), green: parseInt(toColor.substring(2,4), 16), blue: parseInt(toColor.substring(4,6), 16)};
		var result = {red : Math.round(from.red * (1 - blendPercentage) + to.red * blendPercentage), 
					 green: Math.round(from.green * (1 - blendPercentage)  + to.green * blendPercentage),
					 blue: Math.round(from.blue * (1 - blendPercentage) + to.blue * blendPercentage)};
		var red =   result.red.toString(16);
		if(red.length == 1) {
			red = "0"+red;
		}
		var green = result.green.toString(16);
		if(green.length == 1) {
			green = "0"+green;
		}
		var blue =  result.blue.toString(16);
		if(blue.length == 1) {
			blue = "0"+blue;
		}
		var result = "#"+red+green+blue; 
		
		return result.toUpperCase();
		
		
	}
	
}


	
org.kasource.color.HeatMapColors = function() {
		var colorGenerator = new org.kasource.color.ColorGenerator();
		this.transitions = {
				cold: {from: 0, to :25 , fromColor: "#0000FF", toColor: "#00FFFF", colors: colorGenerator.generateColors("#0000FF", "#00FFFF", 25)},
				mild: {from: 25, to : 50, fromColor: "#00FFFF", toColor: "#00FF00", colors: colorGenerator.generateColors("#00FFFF", "#00FF00", 25)},
				warm: {from: 50, to : 75, fromColor: "#00FF00", toColor: "#FFFF00", colors: colorGenerator.generateColors("#00FF00", "#FFFF00", 25)},
				hot:  {from: 75, to : 100, fromColor: "#FFFF00", toColor: "#FF0000", colors: colorGenerator.generateColors("#FFFF00", "#FF0000", 26)}
		};
		this.getColor = function (heat) {
					
			if(heat < 25) { // (blue -> cyan)
				return this.transitions.cold.colors[heat];
			} else if(heat  < 50) { // (cyan -> green) 
				return this.transitions.mild.colors[heat-25]; 
			} else if(heat  < 75) { // (green -> yellow)
				return this.transitions.warm.colors[heat-50];
			} else { // (yellow -> red)
				return this.transitions.hot.colors[heat-75];
			}
			
		}
		
		this.getTransitions = function() {
			return [this.transitions.cold, this.transitions.mild, this.transitions.warm, this.transitions.hot];
		}
	}
	
org.kasource.color.GreenToRedColors = function() {
	var colorGenerator = new org.kasource.color.ColorGenerator();
	this.colors = colorGenerator.generateColors("#00FF00", "#FF0000", 101);
			
	this.getColor = function (heat) {
		return this.colors[heat];
	}
			
	this.getTransitions = function() {
		return [{from: 0, to: 100, fromColor: "#00FF00", toColor: "#FF0000", colors: this.colors}];
	}
}
	
org.kasource.color.GrayScaleColors = function() {
	var colorGenerator = new org.kasource.color.ColorGenerator();
	this.colors= colorGenerator.generateColors("#FFFFFF", "#000000", 101);
		
	this.getColor = function (heat) {
		return this.colors[heat];
	}
		
	this.getTransitions = function() {
		return [{from: 0, to: 100, fromColor: "#FFFFFF", toColor: "#000000", colors: this.colors}];
	}
}
	
org.kasource.color.BoliviaColors = function() {
		var colorGenerator = new org.kasource.color.ColorGenerator();
		this.transitions = { cold: {from: 0, to: 50, fromColor:"#00FF00",toColor: "#FFFF00",colors: colorGenerator.generateColors("#00FF00", "#FFFF00", 50)},
								   warm: {from: 50, to: 100, fromColor: "#FFFF00", toColor:"#FF0000", colors: colorGenerator.generateColors("#FFFF00", "#FF0000", 51)}};
		this.getColor = function (heat) {
			if(heat  < 50) { // (green -> yellow)
				return this.transitions.cold.colors[heat];
			} else { // (yellow -> red)
				return this.transitions.warm.colors[heat-50];
			}
		}
		
		this.getTransitions = function() {
			return [this.transitions.cold, this.transitions.warm];
		}
	}
	
org.kasource.color.MexicoColors = function() {
		var colorGenerator = new org.kasource.color.ColorGenerator();
		this.transitions = { cold: {from: 0, to: 50, fromColor: "#00FF00", toColor: "#FFFFFF", colors: colorGenerator.generateColors("#00FF00", "#FFFFFF", 50)},
								   warm: {from: 50, to: 100, fromColor: "#FFFFFF", toColor: "#FF0000",colors: colorGenerator.generateColors("#FFFFFF", "#FF0000", 51)}};
		this.getColor = function (heat) {
			if(heat  < 50) { // (green -> white)
				return this.transitions.cold.colors[heat];
			} else { // (white -> red)
				return this.transitions.warm.colors[heat-50];
			}
		}
		
		this.getTransitions = function() {
			return [this.transitions.cold, this.transitions.warm];
		}
}


				
			
org.kasource.color.colorSchemas={heat_map: new org.kasource.color.HeatMapColors(), 
			  green_to_red:  new org.kasource.color.GreenToRedColors(), 
			  bolivia: new org.kasource.color.BoliviaColors(), 
			  mexico: new org.kasource.color.MexicoColors(), 
			  gray: new org.kasource.color.GrayScaleColors()};
			
			
			