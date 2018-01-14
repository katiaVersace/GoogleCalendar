var check = function() {
  
 var match = 0 ;
	
  if ( document.getElementById('password_1').value != "" && document.getElementById('password_1').value ==
      document.getElementById('confirm_password').value ) {
	  document.getElementById('message').style.color = 'green';
      document.getElementById('message').innerHTML = 'Passwords match';
      match = 1;
      }
  else {
	document.getElementById('message').style.color = 'red';
    document.getElementById('message').innerHTML = 'Passwords not match';
    document.getElementById("register_button").disabled=true; 
      }
  
  if (document.getElementById('username_1').value == "" ) {
		document.getElementById('message').style.color = 'red';
	    document.getElementById('message').innerHTML = 'Username empty';
	    document.getElementById("register_button").disabled=true; 
	  }
  
   if (document.getElementById('email_1').value == "" ) {
		document.getElementById('message').style.color = 'red';
	    document.getElementById('message').innerHTML = 'Email empty';
	    document.getElementById("register_button").disabled=true; 
	 }
   
   if (document.getElementById('username_1').value != "" && document.getElementById('email_1').value != "" && match==1){
	    document.getElementById('message').style.color = 'green';
        document.getElementById('message').innerHTML = ' ';
	    document.getElementById("register_button").disabled=false;
	    document.getElementById("register_button").style.color='white';
    } 
  
 
}