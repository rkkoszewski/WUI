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

// ###########################################
// WUIEngine: The WUI Java2Web Binding Engine
// ###########################################
// Dependencies: Requires JQUERY3.js

var useSockets = false; // Enable WebSockets (DEBUG -> TO BE REMOVED)

(function(window){
	'use strict';

	// WUI Core Engine
	function WUIEngine(rootContainers, configuration = {}){
		// Correct Instantiation Guard
		if (!(this instanceof WUIEngine)) 
			return new WUIEngine(rootContainers, configuration);
		
		// Configuration
		this.configuration = configuration;

		// Variables
		this.elements = {};
		this.element_cache = {};
		this.timestamp = 0;
		this.element_download_queue = {};
		this.new_nodes = {}; // New Nodes
		this.placeholder_html = "<div>Loading...</div>"; // Placeholder Element
		this.rootContainers = rootContainers;
		
		// Prepare Body Node
		/*
		document.body._wuidata = {
			container: {
				body: document.body
			}
		}
		*/
		
		// Variables
		this.DependencyProvider;
	}
	
	// WUIEngine Methods
	WUIEngine.prototype = {
		loadPage: function(url){
			// alert("LOADING PAGE "+url)
			this.getPageContent();
		},
		// Get Page Content
		getPageContent: function(){
			var self = this;
			// Request Page Content
			doRequest({
				url: window.location.href,
				headers: {'x-wui-request': 'content'},
				data: {'t': this.timestamp}
			}, 'content')
			
			// When Content is Received
			.onReceive(function(data){
				
				console.debug("GOT DATA: ", data);
				self.timestamp = data.timestamp; // Update Timestamp

				// Template Child Nodes (TODO: To be inherited from the template)
				/*
				var template_child_nodes = {
					body: document.body
				}
				*/
				
				self.new_nodes = data.nodes; // Set New Nodes
				
				// TODO: Implement Root Data Updating
				if(typeof data.root != 'undefined'){
					// Root Updates
					// alert(self.childNodes);
					console.debug("CHILD NODES:", self.childNodes)
					// console.debug("getPageContent: PROCESS BODY CHILD NODES!!!", template_child_nodes);
					self.processChildNodes(self.rootContainers, data.root); // Process Child Nodes
				}
				
				// Update Existing Nodes
				Object.keys(self.new_nodes).forEach(function(uuid) {
					
					// Check if Element Exists in Element Cache
					var element = self.element_cache[uuid];
					if(typeof element !== 'undefined'){
						
						// console.debug("FOUND EXISTING ELEMENT;;; ", element)
						
						if(typeof self.new_nodes[uuid] === 'undefined') return; // TODO: This is a workaround. Why is this happening? Find out and fix
						
						// Update Data
						if(typeof self.new_nodes[uuid].data !== 'undefined'){
							element.setAutoData(self.new_nodes[uuid].data);
							element.setData(self.new_nodes[uuid].data);
						}
						
						// Update Children
						if(typeof self.new_nodes[uuid].children !== 'undefined' && typeof element.childNodeContainers !== 'undefined'){
							// console.debug("PROCESSING CHILD NODES: ", self.new_nodes[uuid].children)
							self.processChildNodes(element.childNodeContainers, self.new_nodes[uuid].children);
						}
						
						delete self.new_nodes[uuid]; // Delete New Node Entry
					}

					// console.debug("MISSING NODES: " + uuid);
	    		});
				
				// At this point there should be no more New Nodes Left (NOT VALID ANYMORE AS THE WHOLE PROCESS IS ASYNCHRONOUS, BUT STILL WOULD BE GOOD TO HAVE THIS WORKING)
				/*
				if(Object.keys(self.new_nodes).length != 0){
					console.error("ERROR: There are still New Nodes left. Wrong state.", self.new_nodes);
					console.debug(self.new_nodes)
				}
				*/

			}).onClose(function(){
				self.getPageContent(); // Get Page Update
				
			});
		},
		// Process Child Nodes from a Parent Element
		processChildNodes: function(root_nodes, child_nodes){
			// root_nodes = Array of nodes = {body: BODYNODE, }
			// child_nodes = Array of Element UUIDS
			var self = this;

			// Process Each Branch
			Object.keys(child_nodes).map(function(key, index) {
			    var branch = child_nodes[key];
			    var index = 0;
			    branch.forEach(function(node_uuid){
					// console.debug("processChildNodes: " + key + " @ " + node_uuid, root_nodes[key]);
					if(typeof root_nodes[key] !== 'undefined'){
						// console.debug("######### NEW NODE: " + node_uuid)
						self.appendNode(node_uuid, root_nodes[key], index++); // Append Node
					}else{
						// Error: The Root is missing a branch that has been defined on the server side. Deviation. Reload page?
						console.error("ERROR: Expected Child Branch '" + key + "' in Root Node, but it desn't exist.");
					}
				});
			    // Clear all remaining nodes
			    while(branch.length < root_nodes[key].childElementCount) root_nodes[key].removeChild(root_nodes[key].lastChild);
			});
		},
		// Append Node to DOM
		appendNode: function(node_uuid, root, index){
			// console.log("appendNode: APPENDING NODE: " + node_uuid + " AT INDEX: " + index);
			// Check Active Nodes
			if(typeof this.element_cache[node_uuid] !== 'undefined'){
				// console.debug("[UPDATE] FOUND ACTIVE NODE: ", this.element_cache[node_uuid]);
				appendNodeAt(root, index, this.element_cache[node_uuid]);
			} else
			// Check New Nodes
			if(typeof this.new_nodes[node_uuid] !== 'undefined'){
				// console.debug("appendNode: [NEW] FOUND <NEW> NODE: ", this.new_nodes[node_uuid]);

				if(typeof this.elements[this.new_nodes[node_uuid].element] !== 'undefined'){
					// Element Definition Exists
					// console.log("appendNode: ELEMENT DEFINITION EXISTS: "+ this.new_nodes[node_uuid].element);
					//createNode(this.new_nodes[node_uuid], root, index);
					
					//console.debug("DATA ELEMENT", elements[data.element]);

					var element = new WUIElement(this.elements[this.new_nodes[node_uuid].element], { // NULL definition
						uuid: node_uuid,
						data: this.new_nodes[node_uuid].data,
						placeholder: this.placeholder_html,
						eventTrigger: function(eventID, data){ triggerEvent(node_uuid, eventID, data); }
					});
					this.element_cache[node_uuid] = element; // Cache Element
					root.appendChild(element.getNode()); // Get Placeholder Node
					
					this.initializeElement(element, this.new_nodes[node_uuid].data); // Initialize Element
					delete this.new_nodes[node_uuid]; // Remove New Element
					
				}else{
					// console.log("appendNode: ELEMENT DEFINITION DOES NOT EXISTS: "+ this.new_nodes[node_uuid].element);
					// Element Definition Does not Exists
					var self = this;
					
					var element = new WUIElement(null, { // NULL definition
						uuid: node_uuid,
						data: this.new_nodes[node_uuid].data,
						placeholder: this.placeholder_html,
						eventTrigger: function(eventID, data){ triggerEvent(node_uuid, eventID, data); }
					});
					this.element_cache[node_uuid] = element; // Cache Element
					
					root.appendChild(element.getNode()); // Get Placeholder Node
					var new_node = this.new_nodes[node_uuid];
					delete this.new_nodes[node_uuid]; // Remove New Element
					
					this.getElementDefinition(new_node.element, function(){
						// console.log("appendNode: DOWNLOADED ELEMENT DEFINITION FOR: " + new_node.element)
						element.setElementDefinition(self.elements[new_node.element]); // Set Element Definition
						
						self.initializeElement(element, new_node.data, new_node.children); // Initialize Element
					});
				}
				return 
			} else
			// Exception
			console.error("ERROR: Node with UUID '" + node_uuid + "' is unknown. Something is wrong.")
		},
		// Initialize Element
		initializeElement: function(element, data, children){
			var self = this;
			element.loadDependencies()
				.success(function(){
					element.initializeBefore(); // Initialize Before
					element.initializeAfter(); // Initialize After
					element.setData(data);
	
					// Set Child Nodes
					//console.debug("CHILD NODE CONTAINERS", element.childNodeContainers);
					// console.debug("FOR DATA CHILDREN: ", children)

					// Process Child Elements
					if(typeof children !== 'undefined' && typeof element.childNodeContainers !== 'undefined'){
						self.processChildNodes(element.childNodeContainers, children);
					}
					// console.debug(element)
				})
				.fail(function(){
					// TODO: Replace Element with failed to load node message node
					alert("FAIL to build element");
				});
		},
		// Get Element Definition
		getElementDefinition: function(element_name, callback_done){
			// Get Element Definition
			var self = this;
			
			if(typeof this.element_download_queue[element_name] === 'undefined'){
				// Create Callback Queue
				this.element_download_queue[element_name] = [callback_done];
				
				// Start Element Definition Request
				$.ajax({
					url: window.location.href,
					headers: {'x-wui-request': 'element'},
					data: {element: element_name}
				
				}).done(function(data){
					// console.debug("GOT ELEMENT DATA: ", data);
					
					// Register Element
					/*
					self.elements[element_name] = {
							"html": data.html,
							"initialize": new Function("node, data, element, eventTriggered", data['js-initialize']),
							"setData": new Function("node, data, element, eventTriggered", data['js-set-data'])
					}
					*/
					// Get Element Definition
					self.elements[element_name] = data;
					console.debug("getElementDefinition: GOT ELEMENT DEFINITION FOR '"+element_name+"': ", data);
					
					// Run all Callbacks
					self.element_download_queue[element_name].forEach(function(callback){
						callback();
					});
					
				}).fail(function(){
					// TODO: Create dummy error definition
					alert("FAILED TO DOWNLOAD ELEMENT DEFINITION")
				});
				
			}else{
				// Element is in process of downloading
				self.element_download_queue[element_name].push(callback_done);
			}

			// TODO: IF this fails. DO something.
		}
	}
	
	// Exports
	window.WUIEngine = WUIEngine;
	
	// *************************************
	// Additional Helper Classes and Methods
	// *************************************

	// JQuery Response
	var JQResponse = function(jqrequest){
		this.jqrequest = jqrequest;
	};

	JQResponse.prototype = {
		onReceive: function(callback){
			this.jqrequest.done(callback);
			return this;
		},
		onClose: function(callback){
			this.jqrequest.always(callback);
			return this;
		}
		// TODO: Implement on fail
	}
	
	// WebSocket Response
	var WSResponse = function(callbacks){
		this.callbacks = callbacks;
	}
	
	WSResponse.prototype = {
		onReceive: function(callback){
			this.callbacks.onReceive.push(callback);
			return this;
		},
		onClose: function(callback){
			this.callbacks.onClose.push(callback);
			return this;
		}
		// TODO: Implement on fail
	}
	
	// Append Node At Position
	function appendNodeAt(root, index, node, uuid){
		if(root.childNodes.length >= index){
			// Inside Bounds
			if(root.childNodes[index] !== node){
				root.insertBefore(node.getNode(), root.childNodes[index]);
			}
		}else{
			// Must Append
			root.appendChild(node);  
		}
	}
	
	// Format Parameters
	function formatParameters(data){
    	var parameters; // Undefined
    	if(data){
    		parameters = {};
    		Object.keys(data).forEach(function(key) {
    			parameters[key] = [data[key]];
    		});
    	}
    	return parameters;
	}
	
	
	var wsConnectionPool = {};

	function doRequest(request, pool, ping_timeout = 0){
		// request.url
		// request.data
		// request.headers

		if (useSockets && window.WebSocket){ // Browser with WebSocket Support
			console.log("BROWSER SUPPORTED");
			
			// Reuse Open Connection
			var connection = wsConnectionPool[pool];
			if(typeof connection != 'undefined' && /*typeof connection.readyState != 'undefined' &&*/ connection.readyState == 1){
				console.debug("REUSING WS CONNECTION", connection);

				// Send Data
				connection.send(JSON.stringify({
					url: window.location.pathname,
					headers: request.headers,
					parameters: formatParameters(request.data)
				}));

				// TODO: Implement Callbacks (And how and when to flush them)
				
				return;
			}

			// Build WebSocket Connection URL
			var loc = window.location;
			var ws_uri =
				(loc.protocol === 'https:' ? 'wss:' : 'ws:') + // Protocol
				'//' + loc.host + // Host
				loc.pathname // Path Name
	
			// Initialize WebSocket Connection
			connection = new WebSocket(ws_uri);
				
			// Callbacks
		    var callbacks = {
	    		onReceive: [],
	    		onClose: []
		    }

		    var ping;
		    
			// Send Data
		    connection.addEventListener('open', function (event) {

		    	connection.send(JSON.stringify({
					url: loc.pathname,
					headers: request.headers,
					parameters: formatParameters(request.data)
				}));
				
		    	if(ping_timeout != 0)
		    		ping = setInterval(function(){ connection.send(""); }, ping_timeout); // Keep Alive Ping (TODO: May not be required by all servers)
			});
			
			// Receive Data	
		    connection.addEventListener('message', function (event) {
				console.log('Message from server', event.data);
				callbacks.onReceive.forEach(function(callback){
					callback(JSON.parse(event.data));
				});
			});
			
		    // Connection Error
		    connection.addEventListener('error', function (event) {
				console.debug("SERVER ERROR OCCURED", event);
			});
		    
			// Connection Closed
		    connection.addEventListener('close', function (event) {
				console.debug("SERVER CLOSED");
				clearInterval(ping);
				callbacks.onClose.forEach(function(callback){
					callback();
				});
			});
		    
		    // Add Connection to Pool
		    wsConnectionPool[pool] = connection;
			
			return new WSResponse(callbacks);

		} else { // Legacy Fallback
		     console.log("BROWSER NOT SUPPORTED");
		     
		     // Do HTTP Request
			var jqrequest = $.ajax({
				url: request.url,
				headers: request.headers,
				data: request.data,
				method: request.method,
				contentType: request.contentType,
				processData: request.processData
			});
			
			return new JQResponse(jqrequest);
		}
	}

	// Trigger Event
	function triggerEvent(uuid, eventID, data = undefined){
		console.debug("EVENT TRIGGERED: " + eventID, data)
		if(typeof data !== 'undefined'){
			data = JSON.stringify(data); // Encode Object with JSON
		}
		
		doRequest({
			url: window.location.href,
			headers: {
				'x-wui-request': 'event', 
				'x-wui-element': uuid,
				'x-wui-event': eventID},
			method: 'POST',
			processData: false,
			contentType: 'application/json',
			data: data
		}, 'action', 2000);
	}
	
	
	
})(window);