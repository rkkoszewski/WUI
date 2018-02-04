/**
 * WUI Core Engine
 */

// WUI Core Self Contained Environment
(function(){
	
	$.ajax({
		url: window.location.href,
		headers: {'x-wui-content-request': 'true'}
	}).done(function(data){
		console.debug("GOT DATA: ", data);
		$(document.body).html(data);
	});
	
})();