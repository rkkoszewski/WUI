/**
 * WUI Core Engine
 */

// WUI Core Self Contained Environment

(function(){
	
	// Variables
	var elements = {};
	var element_cache = {};
	var timestamp = 0;

	// Get Element Definition
	function getElementDefinition(element_name, callback_done){
		// Get Element Definition
		$.ajax({
			url: window.location.href,
			headers: {'x-wui-request': 'element'},
			data: {element: element_name}
		
		}).done(function(data){
			console.debug("GOT ELEMENT DATA: ", data);
			
			// Register Element
			elements[element_name] = {
					"html": data.html,
					"initialize": new Function("node, data, element, performAction", data['js-initialize']),
					"setData": new Function("node, data, element, performAction", data['js-set-data'])
			}
			
			callback_done();

		});
		// TODO: IF this fails. DO something.
	}
	
	// Generate Element Node
	function generateElement(data){

		var element = elements[data.element];
		
		if(typeof element === 'undefined'){
			// Download Node
			console.debug("NODE DEFINITITION NOT FOUND. Downloading!!!. NODE ID: " + data.element, elements)
			var $node = $('<div/>');
			
			getElementDefinition(data.element, function(){
				// Replace Node Callback
				console.debug("REPLACING PLACEHOLDER NODE WITH NEW NODE");
				element = elements[data.element];
				var $new_node = $(element.html);
				element.initialize($new_node[0], data.data, data, performAction);
				element.setData($new_node[0], data.data, data, performAction);
				$node.replaceWith($new_node);
				
				// Node Methods
				var node = $new_node[0];
				node._wuimethods = {
					setData: function(data){
						element.setData(node, data.data, data, performAction);
					}
				}
				
				element_cache[data.uuid] = node;
			});
			
			return $node[0];
		}else{
			// Node exists in cache
			console.debug("NODE DEFINITITION FOUND. NODE ID: " + data.element)
			var $node = $(element.html);
			element.initialize($node[0], data.data, data, performAction);
			element.setData($node[0], data.data, data, performAction);
			
			// Node Methods
			node._wuimethods = {
				setData: function(data){
					element.setData(node, data.data, data, performAction);
				}
			}
			
			element_cache[data.uuid] = $node[0];
			return $node[0];
		}
	}
	
	// Perform an Action
	function performAction(uuid){
		$.ajax({
			url: window.location.href,
			headers: {'x-wui-request': 'action', 'x-wui-element': uuid}
		});
	}
	
	// Get Page Content
	function getPageContent(){
		// Get Page Content
		$.ajax({
			url: window.location.href,
			headers: {'x-wui-request': 'content'},
			data: {'t': timestamp}
		
		}).done(function(data){
			console.debug("GOT DATA: ", data);
			timestamp = data.timestamp; // Update Timestamp
			
			switch(data.type.toUpperCase()){
			case 'FULL': // Full Page Update
				$(document.body).empty();
				$.each(data.nodes.body, function(key, value) {
				  console.debug(key, value);
				  
				  var el = generateElement(value);

				  console.debug("Appending Element", el)

				  $(document.body).append(el);
				  
				});
				break;
				
			case 'PARTIALDATA': // Partial Data Only Update
				$.each(data.updates, function(key, value) {
				  console.debug(key, value);
				  
				  // Get from cache
				  var element = element_cache[value.uuid]
				  if(typeof element === 'undefined'){
					  // ERROR: Unexpected state. Existing element is being updated but is not found in the DOM. Something went completely wrong.
					  console.error("ERROR: Element with UUID " + value.uuid + " does not exists. Inconsistent state. Requires full page update.");
					  
				  }else{
					  // Update Element
					  console.debug("UPDATING ELEMENT: ", element);
					  element._wuimethods.setData(value);

				  }
				  
				});

				break;
			
			}			

		}).always(function(){
			getPageContent(); // Get Page Update
			
		});

	}
		
	getPageContent(); // Initial Run

})();