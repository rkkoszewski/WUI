// WUIEngine
(function(window){
	'use strict';

	// WUI Dependency Provider Engine
	function AsyncDependencyProvider(configuration = {}){
		// Correct Instantiation Guard
		if (!(this instanceof AsyncDependencyProvider)) 
			return new AsyncDependencyProvider(configuration);
		
		// Variables
	}
	
	// DependencyProvider Methods
	AsyncDependencyProvider.prototype = {
		
	}
	
	// Exports
	if(typeof window.WUIEngine === 'undefined'){
		console.error('ERROR: WUI Dependency Provider -> Could not find WUIEngine in global context. ' +
				'It is eider missing or you loaded this library before WUIEngine was available.');
	}else{
		// Register Dependency Provider
		window.WUIEngine.DependencyProvider = new AsyncDependencyProvider();
	}
	
})(window);