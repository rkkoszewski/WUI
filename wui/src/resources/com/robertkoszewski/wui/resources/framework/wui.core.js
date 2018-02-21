/**
 * WUI Core Engine
 */

// WUI Core Self Contained Environment

var elementsArray;

(function(){
	
	// TODO: Define this externally
	var elements = {
			/*
			"Label" : {
				"html": "<div></div>",
				"setData": function(node, data){
					$(node).text(data);
				}
			},
			"Button" : {
				"html": "<button></button>",
				"setData": function(node, data, element){
					console.debug("DATA", data);
					$(node).text(data.value);
					$(node).click(function(){
						performAction(element.uuid);
					});
				}
			}
			*/
	}
	
	elementsArray = elements; // FOR DEBUG:
	
	function getElement(element_name, callback_done){
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
					"setData": new Function("node, data, element, performAction", data['js-set-data'])
			}
			
			callback_done();

		});
		// TODO: IF this fails. DO something.
	}
	
	function generateElement(data){

		var element = elements[data.element];
		
		if(typeof element === 'undefined'){
			// Download Node
			console.debug("NODE DEFINITITION NOT FOUND. Downloading!!!. NODE ID: " + data.element, elements)
			var $node = $('<div/>');
			
			getElement(data.element, function(){
				// Replace Node Callback
				console.debug("REPLACING PLACEHOLDER NODE WITH NEW NODE");
				element = elements[data.element];
				var $new_node = $(element.html);
				element.setData($new_node[0], data.data, data, performAction);
				$node.replaceWith($new_node);
			});
			
			return $node[0];
		}else{
			// Node exists in cache
			console.debug("NODE DEFINITITION FOUND. NODE ID: " + data.element)
			var $node = $(element.html);
			element.setData($node[0], data.data, data, performAction);
			return $node[0];
		}
	}
	
	function performAction(uuid){
		$.ajax({
			url: window.location.href,
			headers: {'x-wui-request': 'action', 'x-wui-element': uuid}
		});
	}
	
	function getPageContent(){
		// Get Page Content
		$.ajax({
			url: window.location.href,
			headers: {'x-wui-request': 'content'}
		
		}).done(function(data){
			console.debug("GOT DATA: ", data);
			
			$(document.body).empty();
			
			$.each(data, function(key, value) {
			  console.debug(key, value);
			  
			  var el = generateElement(value);

			  console.debug("Appending Element", el)

			  $(document.body).append(el);
			  
			});
		}).always(function(){
			// Get Page
			//setTimeout(getPageContent, 1000);
			getPageContent();
		});

	}
		
	getPageContent(); // Initial Run
		
	//setInterval(getPageContent, 1000); // Run Every Second

})();