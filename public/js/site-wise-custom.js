var $ = jQuery.noConflict();


// Feedback highlighting
if($("body").hasClass("page-feedbacks")) {
	var idToSelect = $(location).attr('hash');
	$(idToSelect).parent().addClass("shake animated").attr("data-animate", "shake");
	
}