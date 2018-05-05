/**************************************************************************\
 * Copyright (c) 2018 Robert Koszewski                                    *
 *                                                                        *
 * Permission is hereby granted, free of charge, to any person obtaining  *
 * a copy of this software and associated documentation files (the        *
 * "Software"), to deal in the Software without restriction, including    *
 * without limitation the rights to use, copy, modify, merge, publish,    *
 * distribute, sublicense, and/or sell copies of the Software, and to     *
 * permit persons to whom the Software is furnished to do so, subject to  *
 * the following conditions:                                              *
 *                                                                        *
 * The above copyright notice and this permission notice shall be         *
 * included in all copies or substantial portions of the Software.        *
 *                                                                        *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,        *
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF     *
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND                  *
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE *
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION *
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION  *
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.        *
\**************************************************************************/

// ################################################
// WUI Dependency Provider Implementation using WUI
// ################################################
// Dependencies: Requires LOADJS.js

(function(window){
	'use strict';

	// WUI Dependency Provider Engine
	function WUIDependencyProvider(configuration = {}){
		// Correct Instantiation Guard
		if (!(this instanceof WUIDependencyProvider)) 
			return new WUIDependencyProvider(configuration);
		
		// Variables
		this.dependencyCacheJS = {};
		this.dependencyCacheCSS = {};
	}

	// DependencyProvider Methods
	WUIDependencyProvider.prototype = {
		loadDependencies: function(dependencies){ // Load Dependencies
			console.log("LOADING DEPENDENCIES")

			// Build Dependency URLs
			var dependencyFilesJS = [];
			
			// Process JS Dependencies
			if(typeof dependencies.js !== 'undefined'){
				Object.keys(dependencies.js).forEach(function(depName) { // Iterate Data Target
					var depConfig = dependencies.js[depName];
					var depVersion = depConfig.version;
					console.log("REQUIRES JS DEPENDENCY: " + depName + " @ " + depVersion);
					dependencyFilesJS.push('/?requestType=jsDependency&name='+depName+'&version='+depVersion)
					console.log("DOWNLOADING FROM: " + '/?requestType=jsDependency&name='+depName+'&version='+depVersion);
				});
			}
			
			// Process CSS Dependencies
			if(typeof dependencies.css !== 'undefined'){
				Object.keys(dependencies.css).forEach(function(depName) { // Iterate Data Target
					var depConfig = dependencies.css[depName];
					var depVersion = depConfig.version;
					console.log("REQUIRES CSS DEPENDENCY: " + depName + " @ " + depVersion);
					loadCSS('/?requestType=cssDependency&name='+depName+'&version='+depVersion)
					console.log("DOWNLOADING FROM: " + '/?requestType=cssDependency&name='+depName+'&version='+depVersion);
				});
			}

			// Abort when no more new dependencies are needed
			if(dependencyFilesJS.length === 0){
				var ret = {
					success: function(callback){ callback(); return ret; },
					fail: function(){ return ret; }
				}
				return ret;
			}
			
			// Status Variables
			var status = 0; // 0 = In Process , 1 = Success, 2 = Fail
			var success_callback = [];
			var fail_callback = [];
						
			// Load JavaScripts
			loadjs(dependencyFilesJS, {
				async: false,
				success: function(){
					while(success_callback.length != 0){
						console.log("LENGT: " + success_callback.length)
						status = 1;
						success_callback.pop()(); // Run Callback
					};
				},
				error: function(){
					while(fail_callback.length != 0){
						console.log("LENGT: " + success_callback.length)
						status = 2;
						fail_callback.pop()(); // Run Callback
					};
				}
			});

			// Promise Like Object
			var promise = {
				success: function(callback){
					if(status === 1) callback();
					else if(status === 0) success_callback.push(callback);
					return promise;
				},
				fail: function(callback){
					if(status === 2) callback();
					else if(status === 0) fail_callback.push(callback);
					return promise;
				},
			}
			
			return promise;
		}
	}
	
	// Exports
	if(typeof window.WUIEngine === 'undefined'){
		console.error('ERROR: WUI Dependency Provider -> Could not find WUIEngine in global context. ' +
				'It is eider missing or you loaded this library before WUIEngine was available.');
	}else{
		// Register Dependency Provider
		window.WUIEngine.DependencyProvider = new WUIDependencyProvider();
	}
	
	// Loads the CSS File
	function loadCSS(url) {
		  return new Promise((resolve, reject) => {
		    var link = document.createElement('link');
		    link.type = 'text/css';
		    link.rel = 'stylesheet';
		    link.onload = function() { resolve(); };
		    link.href = url;
		    
		    // Append Link
		    document.head.appendChild(link);
		  });
		};
	
})(window);