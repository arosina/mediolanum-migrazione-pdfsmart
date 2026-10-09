var isSomeDialogOpen = false;
(function($) {
    $.fn.openCustomModal = function() {
    	isSomeDialogOpen = true;
        return this.each(function() {
            var $modal = $(this);

            $("body").addClass("modal-open");
            $("body").append("<div class='modal-custom-backdrop'></div>");
            $modal.show();
            $modal.trigger('open-custom-modal');

            $modal.find('[data-dismiss-custom-modal]').on('click', function() {
            	isSomeDialogOpen = false;
            	$("body").removeClass("modal-open");
                $(".modal-custom-backdrop").remove();
                $modal.hide();
                $modal.trigger('close-custom-modal');
            });
            
        });
    };
}(jQuery));