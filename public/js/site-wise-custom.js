var $ = jQuery.noConflict();


// Feedback highlighting
if($("body").hasClass("page-feedbacks")) {
	var idToSelect = $(location).attr('hash');
	$(idToSelect).parent().addClass("shake animated").attr("data-animate", "shake");
	
}

var video_wrapper = $('.youtube-video-place');
//  Check to see if youtube wrapper exists
if(video_wrapper.length){
// If user clicks on the video wrapper load the video.
$('.play-youtube-video').on('click', function(){
/* Dynamically inject the iframe on demand of the user.
 Pull the youtube url from the data attribute on the wrapper element. */
video_wrapper.addClass("embed-responsive-16by9");
video_wrapper.html('<iframe style="border:none;"  allowfullscreen class="embed-responsive-item" src="' + video_wrapper.data('yt-url') + '"></iframe>');
});
}

async function supportsWebp() {
  if (!self.createImageBitmap) return false;
  
  const webpData = 'data:image/webp;base64,UklGRh4AAABXRUJQVlA4TBEAAAAvAAAAAAfQ//73v/+BiOh/AAA=';
  const blob = await fetch(webpData).then(r => r.blob());
  return createImageBitmap(blob).then(() => true, () => false);
}

(async () => {
  if(await supportsWebp()) {
    console.log('webp supported');
  }
  else {
    $('.webp-alt-video > video').each(function() {
    	var replacePoster = $(this).attr("data-alt-poster");
    	$(this).attr("poster", replacePoster);
    });
    
    $('.webp-alt-style').each(function() {
    	var replaceStyle = $(this).attr("data-alt-style");
    	$(this).attr("style", replaceStyle);
    });
  }
})();