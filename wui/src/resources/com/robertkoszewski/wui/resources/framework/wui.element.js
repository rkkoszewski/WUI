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

// #####################################
// WUIElement: WUI Element Object
// #####################################
// Dependencies: Requires LESS.js and WUI.ENGINE.js

(function(window, less){
	'use strict';

	// Element Object
	function WUIElement(definition, configuration = {}){
		// Correct Instantiation Guard
		if (!(this instanceof WUIElement)) 
			return new WUIElement(definition, configuration);
		
		// Element Data and Metadata
		this.definition = (definition !== null ? definition : {});
		this.isWrapped = false;
		this._isDOMReady = false;
		this._configuration = configuration;
		this.uuid = configuration.uuid;
		
		// WUI Element Debug: Enables some additional audit data
		this._debug = (typeof configuration.debug !== 'undefined' ? configuration.debug : false);
		if(this._debug){
			this._debugAccessedData = {};
			this._debugCSSError;
		}
		
		// Node Definition
		this.node = null;
		
		// Auto Data
		this.autoDataRefrences = {}; // Auto Data References

		// Process Element
		if(definition !== null){
			this._processElement(true);
		}
	}
	
	// WUIElement Prototypes
	WUIElement.prototype = {
		// Process the Element	
		_processElement: function(cleanData = false){
			this.node = this.createNode(); // Create Node (Do not access WUIElement.node directly when getting the node to be added to the DOM, use getNode() instead)
			this._renderCSS(); // Render CSS
			
			// Auto Data
			this._parseAutoData(this._configuration.data); // Parse Auto Data
			if(cleanData){
				delete this._configuration.data; // Clean data that won't be required anymore
			}

			// Child Node Containers
			this.childNodeContainers = {};
			this._parseChildNodeContainers();
			// console.debug("POST CHILD NODE CONTAINER", this.childNodeContainers)
		},
		// Render CSS
		_renderCSS: function(force = false){
			var self = this;
			// Make Sure WUIEngine is Available
			if(typeof document.body.WUIEngine === 'undefined'){
				document.body.WUIEngine = {};
			}
			// Make Sure CSSCache is available in WUIEngine
			if(typeof document.body.WUIEngine.CSSCache === 'undefined'){
				document.body.WUIEngine.CSSCache = {};
			}
			// Fore Re-Render of CSS
			if(force){
				if(typeof document.body.WUIEngine.CSSCache[this.definition.name] !== 'undefined'){
					// console.log("REMOVING CSS NODE FOR RERENDERING");
					if(typeof document.body.WUIEngine.CSSCache[this.definition.name].styleNode !== 'undefined'){
						var styleNode = document.body.WUIEngine.CSSCache[this.definition.name].styleNode;
						styleNode.parentNode.removeChild(styleNode); // Remove Style DOM Node
					}
					delete document.body.WUIEngine.CSSCache[this.definition.name]; // Delete Cache Object
				}
			}
			
			// Create Style Element
			if(typeof document.body.WUIEngine.CSSCache[this.definition.name] === 'undefined'){
				if(typeof this.definition.css !== 'undefined' && this.definition.css.style !== ''){ 
					// Create Style node
					var style = document.createElement('style'); 
					var elementUID = this.definition.name;
					
					// Render Style
					var format = (typeof this.definition.css.format !== 'undefined' ?  this.definition.css.format.toLowerCase() : '???');
					switch(format){
					case 'lesscss':
						// Create Style node
						// console.log("CSS: RENDERING LESSCSS");
						less.render('[data-wuielement="' + elementUID + '"]' + (this.isWrapped ? '' : '>') + '{' + this.definition.css.style + '}')
						.then(function(output) {
							// console.debug(output.css);
							style.innerHTML = output.css;
							if(self._debug && typeof self._configuration.debugCompileCSSSuccess === 'function'){
								self._configuration.debugCompileCSSSuccess(output.css, output.map, output.imports);
							}
							// output.css = string of css
							// output.map = string of sourcemap
							// output.imports = array of string filenames of the imports referenced
						},
						function(error) {
							console.error("ERROR: " + error);
							if(self._debug && typeof self._configuration.debugCompileCSSFail === 'function'){
								self._configuration.debugCompileCSSFail(error);
							}
						});
						break;
						
					default: // Defaults to regular CSS
						// console.log("CSS: RENDERING REGULAR CSS");
						style.innerHTML = this.definition.css.style;
					}
					
					// Cache Style
					document.body.WUIEngine.CSSCache[this.definition.name] = { 
						styleNode: style,
						uid: elementUID
					}; 
					
					document.head.appendChild(style); // Append style to DOM
					this.node.dataset.wuielement = document.body.WUIEngine.CSSCache[this.definition.name].uid;
					// this.node.classList.add(document.body.WUIEngine.CSSCache[this.definition.name].uid);
				}else{
					// Non existent CSS
					document.body.WUIEngine.CSSCache[this.definition.name] = {};
					// console.log("CSS: CSS NON EXISTENT");
				}
			}else{
				// Found already processed CSS
				if(typeof document.body.WUIEngine.CSSCache[this.definition.name].uid !== 'undefined'){
					this.node.dataset.wuielement = document.body.WUIEngine.CSSCache[this.definition.name].uid;
					// this.node.classList.add(document.body.WUIEngine.CSSCache[this.definition.name].uid);
				}
			}
		},
		// Set Delayed Element Definition
		setElementDefinition: function(definition, cleanData = false){
			this.definition = definition; // Set Element Definition
			this._processElement(cleanData); // Process Element
		},
		// Create DOM Node
		createNode: function(){
			var wrapper = document.createElement('div');
			wrapper.innerHTML = this.definition.html;
			if(wrapper.childNodes.length === 1){
				var firstChild = wrapper.firstChild;
				if(firstChild.nodeType === Node.ELEMENT_NODE){
					// console.debug('Returning Non-Wrapped Node', firstChild);
					this.isWrapped = false;
					// Export WUIElement instance in DOM Node
					firstChild.WUIElement = this;
					return firstChild; // Return non-wrapped node
				}
			}
			// console.debug('Returning Wrapped Node', wrapper);
			this.isWrapped = true;
			// Export WUIElement instance in DOM Node
			wrapper.WUIElement = this;
			return wrapper;
		},
		// Call Element Definition Method Helper with AutoCompile
		_callMethod: function(definitionBranch, definitionLeafID, parameterDefinition, parameterData){
			if(typeof definitionBranch === 'undefined') return;
			var callback = definitionBranch[definitionLeafID];
			switch(typeof callback){
				case 'string': // Uncompiled State (Do compilation)
					callback = new Function(parameterDefinition, callback);
					definitionBranch[definitionLeafID] = callback; // Cache it for next time
					// 'break' left out intentionally to run the compiled function afterwards
				case 'function': // Compiled Function
					callback.apply(this, parameterData);
					break;
				default: // Handles 'undefined', null and unknown values
				// Do Nothing
			}	
		},
		// Data Access Debug
		_dataDebug: function(data){
			var self = this;
			return new Proxy(data, {
				get: function(obj, prop) {
					// console.debug("REQUESTING " + prop, obj)
					self._debugAccessedData[prop] = true; // Mark data as accessed
					return obj[prop];
			    }
			});
		},
		// Shared Data Parameters
		_dataParameters: function(){
			var self = this;
			return {
				definition: 'node, element, setContainer, performAction',
				parameters: [ // Default Elements: 'node, element, setContainer, performAction'
					this.node, // Element DOM Node
					this,  // Element Object
					function(id, node){ 
						id = id.trim();
						if(id === '') throw 'Container ID Empty';
						if(typeof node === 'undefined' || node == null) throw 'Selected invalid Node';
						self._addChildNodeContainer(self, id, node); 
					}, // Set Container Node
					function(){ if(typeof self._configuration.actionPerformed === 'function') 
						self._configuration.actionPerformed(); } // Action Performed Callback
				]
			};
		},
		// Initialize Element Before node is added to DOM
		initializeBefore: function(){
			var dataParameters = this._dataParameters();
			var ret = this._callMethod(this.definition.js, 'initialize-before', dataParameters.definition, dataParameters.parameters);
			this._isDOMReady = true; // Node is read to be added to DOM
			if(typeof this.nodePlaceholder !== 'undefined'){
				this.nodePlaceholder.parentNode.replaceChild(this.node, this.nodePlaceholder); // Replace Placeholder node with element node
				delete this.nodePlaceholder;
			}
			return ret;
		},
		// Initialize Element After node is added to DOM
		initializeAfter: function(){
			var dataParameters = this._dataParameters();
			return this._callMethod(this.definition.js, 'initialize-after', dataParameters.definition, dataParameters.parameters);
		},
		// Set Data (Call order: 2)
		setData: function(data){
			var dataParameters = this._dataParameters();
			dataParameters.definition += ', data';
			dataParameters.parameters.push(this._debug ? this._dataDebug(data) :data);
			console.debug(this);
			return this._callMethod(this.definition.js, 'set-data', dataParameters.definition, dataParameters.parameters);
		},
		// Parse Auto Data Node
		_parseAutoDataNode: function(self, dnode, data){
			var data_content = dnode.getAttribute('wui-data'); // Get Data from wui-data attribute
			dnode.removeAttribute('wui-data'); // Remove the wui-data attribute from node
			var hasData = (typeof data !== 'undefined' ? true : false);
			// console.log("DATA CONTENT: "+data_content);
	
			data_content.split(',').forEach(function(data_value){
				var data_split_index = data_value.indexOf(':');
				if(data_split_index !== -1){
					var data_target = data_value.substring(0, data_split_index).trim();
					var data_id = data_value.substring(data_split_index + 1, data_value.length).trim();
					// console.debug("DATA TARGET: " +data_target + " / DATA ID: "+data_id, self);
	
						// Node References
						var ref = self.autoDataRefrences[data_id];
						if(typeof ref === 'undefined'){
							ref = self.autoDataRefrences[data_id] = {};
						}
						
						// Data Targets
						if(typeof ref[data_target] === 'undefined'){
							ref[data_target] = [];
						}
						
						ref[data_target].push(dnode);
	
						// Set Data
						if(hasData) self._setNodeAutoData(dnode, data, data_id, data_target);
				}else{
					console.error("INVALID ELEMENT DATA TYPE: " + data_value);
				}
			});
		},
		// Parse Auto Data
		_parseAutoData: function(data){
			var self = this;
			// Find all Child nodes with wui-data attribute
			var wui_data_nodes = this.node.querySelectorAll('[wui-data]');
			wui_data_nodes.forEach(function(node){
				self._parseAutoDataNode(self, node, data);
			});
			// Check root node for wui-data attribute
			if(this.node.hasAttribute('wui-data')){
				this._parseAutoDataNode(this, this.node, data);
			}
		},
		// Set Node Auto Data
		_setNodeAutoData: function(tnode, data, data_id, data_target){
			data = this._debug ? this._dataDebug(data) : data;
			if(typeof data[data_id] !== 'undefined'){
				if(data_target.startsWith('@')){ // Pseudo Element
					// console.debug('Debug: Using Pseudo Element ' + data_target.substring(1));
					switch(data_target.substring(1)){
						case 'text': 
							tnode.innerText = data[data_id]; 
						break; // Set Text
						default: console.error('ERROR: Invalid Pseudo Element ' + data[data_id].substring(1));
					}
				}else{ // Raw Attribute
					// console.debug('Debug: Using Raw Element ' + data_target);
					tnode[data_target] = data[data_id];
				}
			}
		},
		// Set Auto Data (Call order: 1)
		setAutoData: function(data){
			var ref = this.autoDataRefrences;
			var self = this;
			Object.keys(ref).forEach(function(data_id) { // Iterate Data ID
				Object.keys(ref[data_id]).forEach(function(data_target) { // Iterate Data Target
					ref[data_id][data_target].forEach(function(dnode){
						self._setNodeAutoData(dnode, data, data_id, data_target);
					});
					// console.debug(data_target, ref[data_id][data_target]);
				});
			});
		},
		// Parse Child Node Containers
		_parseChildNodeContainers: function(){
			var self = this;
			// Find all Child nodes with wui-data attribute
			var wui_container_nodes = this.node.querySelectorAll('[wui-container]');
			wui_container_nodes.forEach(function(node){
				var containerID = node.getAttribute('wui-container');
				node.removeAttribute('wui-container');
				self._addChildNodeContainer(self, containerID, node);
			});
			// Check root node for wui-data attribute
			if(this.node.hasAttribute('wui-container')){
				var containerID = this.node.getAttribute('wui-container');
				this.node.removeAttribute('wui-container');
				this._addChildNodeContainer(this, containerID, this.node);
			}
		},
		// Add Child Node Container
		_addChildNodeContainer: function(self, id, node){
			// console.debug("ADDING CONTAINER WITH ID: " + id, node)
			self.childNodeContainers[id] = node;
		},
		// Load Dependencies
		loadDependencies: function(){
			if(typeof this.definition.dependencies === 'undefined'){  // Abort if no dependencies available
				var dummy_ret = {
					success: function(callback) { callback(); return dummy_ret},
					fail: function(){ return dummy_ret }
				}
				return dummy_ret;
			}
			return window.WUIEngine.DependencyProvider.loadDependencies(this.definition.dependencies);
		},
		// Get Element DOM Node
		getNode: function(){
			// Check if Element node is DOM read (Element-Initialize-Before was called)
			if(this._isDOMReady === false){
				// Element Node is not DOM Ready. Use placeholder element instead.
				this.nodePlaceholder = document.createElement('div');
				var placeholderType = typeof this._configuration.placeholder;
				if(placeholderType !== 'undefined'){
					switch(placeholderType){
					case 'string': // HTML Definition
						this.nodePlaceholder.innerHTML = this._configuration.placeholder;
						break;
					case 'object': // Probably Node Object
						if(this._configuration.placeholder.nodeType === Node.ELEMENT_NODE){ // Node Object
							nodePlaceholder.appendChild(this._configuration.placeholder);
						}
						break;
					}
				}
				return this.nodePlaceholder;
				
			}else{
				// Element Node is DOM Ready
				return this.node;
			}
		}
	};
	
	// Exports
	window.WUIElement = WUIElement;

})(window, less);