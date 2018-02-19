/**
 * WUI Core Engine
 */

// WUI Core Self Contained Environment
(function(){
	
	// TODO: Define this externally
	var elements = {
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
	}
	
	function getElement(element_name, callback_done){
		// Get Element Definition
	}
	
	function generateElement(data){
		var element = elements[data.element];
		var $html = $(element.html);
		element.setData($html[0], data.data, data);
		return $html[0];
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