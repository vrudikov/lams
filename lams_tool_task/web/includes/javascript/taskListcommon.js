	function showBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			$("#"+targetDiv+"_Busy").show();
		}
	}
	function hideBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			$("#"+targetDiv+"_Busy").hide();
		}				
	}
	function refreshCKEditors() {
		// make sure all the ckeditors are refreshed, not just the validated ones
		for (var i in CKEDITOR.instances) {
       		CKEDITOR.instances[i].updateElement();
   		}
	}