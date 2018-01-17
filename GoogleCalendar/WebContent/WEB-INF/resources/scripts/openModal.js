var modal = function(index){
	 document.getElementById('modal-wrapper'+index).style.display='block';
	 var endModal = 6;	
		 
	 for(i=1;i<endModal;i++){
		 if(i!=index)
		 document.getElementById('modal-wrapper'+i).style.display='none';
		 }	 
}

