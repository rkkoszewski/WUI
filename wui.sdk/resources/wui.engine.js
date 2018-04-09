// WUIEngine
(function(window){
	'use strict';

	// WUI Core Engine
	function WUIEngine(configuration = {}){
		// Correct Instantiation Guard
		if (!(this instanceof WUIEngine)) 
			return new WUIEngine(configuration);
		
		// Variables
		this.DependencyProvider;
	}
	
	// WUIEngine Methods
	WUIEngine.prototype = {
		
	}
	
	// Exports
	window.WUIEngine = WUIEngine;
	
})(window);