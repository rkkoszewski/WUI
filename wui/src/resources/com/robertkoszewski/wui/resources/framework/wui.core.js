/**
 * WUI Core Engine
 */

// WUI Core Self Contained Environment

var useSockets = false; // Enable WebSockets

(function(){
	
	// Variables
	var elements = {};
	var element_cache = {};
	var timestamp = 0;
	var element_download_queue = {};
	var new_nodes = {}; // New Nodes
	var placeholder_html = "<div>Loading...</div>"; // Placeholder Element
	
	// Prepare Body Node
	document.body._wuidata = {
		container: {
			body: document.body
		}
	}

	// Process Child Nodes
	function processChildNodes(root_nodes, child_nodes){
		// root_nodes = Array of nodes = {body: BODYNODE, }
		// child_nodes = Array of Element UUIDS

		// Process Each Branch
		Object.keys(child_nodes).map(function(key, index) {
		    var branch = child_nodes[key];
		    var index = 0;
		    branch.forEach(function(node_uuid){
				console.debug(key + " @ " + node_uuid, root_nodes[key]);
				if(typeof root_nodes[key] !== 'undefined'){
					appendNode(node_uuid, root_nodes[key], index++); // Append Node
				}else{
					// Error: The Root is missing a branch that has been defined on the server side. Deviation. Reload page?
					console.error("ERROR: Expected Child Branch '" + key + "' in Root Node, but it desn't exist.");
				}
			})
		});
	}

	// Append Node At Position
	function appendNodeAt(root, index, node, uuid){
		if(root.childNodes.length >= index){
			// Inside Bounds
			if(root.childNodes[index] !== node){
				root.insertBefore(node, root.childNodes[index]);
			}
		}else{
			// Must Append
			root.appendChild(node);  
		}
	}
	
	// Append Node
	function appendNode(node_uuid, root, index){
		console.log("APPENDING NODE: " + node_uuid + " AT INDEX: " + index);
		// Check Active Nodes
		if(typeof element_cache[node_uuid] !== 'undefined'){
			console.debug("[UPDATE] FOUND ACTIVE NODE: ", element_cache[node_uuid]);
			appendNodeAt(root, index, element_cache[node_uuid]);
		} else
		// Check New Nodes
		if(typeof new_nodes[node_uuid] !== 'undefined'){
			console.debug("[NEW] FOUND <NEW> NODE: ", new_nodes[node_uuid]);

			if(typeof elements[new_nodes[node_uuid].element] !== 'undefined'){
				// Element Definition Exists
				console.log("ELEMENT DEFINITION EXISTS: "+ new_nodes[node_uuid].element);
				createNode(new_nodes[node_uuid], root, index);
			}else{
				// Element Definition Does not Exists
				var $placeholder = $(placeholder_html); // TODO: Inherit this from Template
				$(root).append($placeholder);
				getElementDefinition(new_nodes[node_uuid].element, function(){
					console.log("DOWNLOADED ELEMENT DEFINITION FOR: " + new_nodes[node_uuid].element)
					createNode(new_nodes[node_uuid], root, index, $placeholder[0]);
				});
			}
			return 
		} else
		// Exception
		console.error("ERROR: Node with UUID '" + node_uuid + "' is unknown. Something is wrong.")
	}
	
	// Create Node
	function createNode(data, root, index, replace_node = null){
		var element = elements[data.element];
		var $node = $(element.html);
		// $node.attr('uuid', data.uuid);
		element.initialize($node[0], data.data, data, performAction);
		var element_data = data.data;
		if(typeof element_data === 'undefined'){
			element_data = {};
		}
		element.setData($node[0], element_data, data, performAction);
		
		// Node Methods
		var node = $node[0];
		node._wuimethods = { // WUI Methods
			setData: function(data){
				var element_data = data.data;
				if(typeof element_data === 'undefined'){
					element_data = {};
				}
				element.setData(node, element_data, data, performAction);
			},
			setChildElements: function(data){
				processChildNodes(node._wuidata.container, data.children);
			}
		}
		
		//node._wuidata = {}; // WUI Data
		
		element_cache[data.uuid] = $node[0];
		
		// Append Element
		if(replace_node != null){
			console.debug("Replacing Element (PREVIOUS)", replace_node)
			console.debug("Replacing Element", node)
			$(replace_node).replaceWith(node);
		}else{
			console.debug("Appending Element", node)
			appendNodeAt(root, index, node, "uuid");
		}
		
		
		// Populate Children
		if(typeof data.children !== 'undefined'){
			// Element has children
			console.debug("Node Has Children: ", data.children)
			console.debug("Node Has Containers: ", node._wuidata)
			processChildNodes(node._wuidata.container, data.children)
		}
		
		// Remove Element from New Nodes List
		delete new_nodes[data.uuid];
		
		console.debug("## NEW NODES LEFT: ", new_nodes)
	}

	// Get Element Definition
	function getElementDefinition(element_name, callback_done){
		// Get Element Definition
		
		if(typeof element_download_queue[element_name] === 'undefined'){
			// Create Callback Queue
			element_download_queue[element_name] = [callback_done];
			
			// Start Element Definition Request
			$.ajax({
				url: window.location.href,
				headers: {'x-wui-request': 'element'},
				data: {element: element_name}
			
			}).done(function(data){
				// console.debug("GOT ELEMENT DATA: ", data);
				
				// Register Element
				elements[element_name] = {
						"html": data.html,
						"initialize": new Function("node, data, element, performAction", data['js-initialize']),
						"setData": new Function("node, data, element, performAction", data['js-set-data'])
				}
				
				// Run all Callbacks
				element_download_queue[element_name].forEach(function(callback){
					callback();
				});
			});
			
		}else{
			// Element is in process of downloading
			element_download_queue[element_name].push(callback_done);
		}

		// TODO: IF this fails. DO something.
	}
	
	// Get Page Content
	function getPageContent(){
		// Get Page Content
		
		doRequest({
			url: window.location.href,
			headers: {'x-wui-request': 'content'},
			data: {'t': timestamp}
			
		}, 'content')
		.onReceive(function(data){
			
			console.debug("GOT DATA: ", data);
			timestamp = data.timestamp; // Update Timestamp
			
			// $(document.body).empty(); // Reset Body
			
			// Template Child Nodes (TODO: To be inherited from the template)
			var template_child_nodes = {
					body: document.body
			}
			
			new_nodes = data.nodes; // Set New Nodes
			
			// TODO: Implement Root Data Updating
			if(typeof data.root != 'undefined'){
				// Root Updates
				processChildNodes(template_child_nodes, data.root); // Process Child Nodes
			}
			
			// Update Existing Nodes
			Object.keys(new_nodes).forEach(function(uuid) {
				
				// Check if Element Exists in Element Cache
				var element = element_cache[uuid];
				if(typeof element !== 'undefined'){
					
					if(typeof new_nodes[uuid] === 'undefined') return; // TODO: This is a workaround. WHy is this happening?
					
					// Update Data
					if(typeof new_nodes[uuid].data !== 'undefined'){
						element._wuimethods.setData(new_nodes[uuid]);
					}
					
					// Update Children
					if(typeof new_nodes[uuid].children !== 'undefined'){
						element._wuimethods.setChildElements(new_nodes[uuid]);
					}
					
					delete new_nodes[uuid]; // Delete New Node Entry
				}

				// console.debug("MISSING NODES: " + uuid);
    		});
			
			// At this point there should be no more New Nodes Left
			if(Object.keys(new_nodes).length != 0){
				console.error("ERROR: There are still New Nodes left. Wrong state.", new_nodes);
				console.debug(new_nodes)
			}

		}).onClose(function(){
			getPageContent(); // Get Page Update
			
		});

	}

	
	// Responses
	
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
				data: request.data
			});
			
			return new JQResponse(jqrequest);
		}
	}

	// Perform an Action
	function performAction(uuid){
		doRequest({
			url: window.location.href,
			headers: {'x-wui-request': 'action', 'x-wui-element': uuid}
		}, 'action', 2000);
	}

	getPageContent(); // Initial Run
})();