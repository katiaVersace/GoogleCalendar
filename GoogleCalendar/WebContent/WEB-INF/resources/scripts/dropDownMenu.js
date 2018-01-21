function addClock(x) {
	if (x < 0) {
		document.getElementById('TourId').value = "none";
	} else {
		document.getElementById('TourId').value = x;
	}
}

function updateClock(x) {
	if (x < 0) {
		document.getElementById('TourId2').value = "none";
	} else {
		document.getElementById('TourId2').value = x;
	}
}

function addPermission(x) {
	
		document.getElementById('TourId3').value = x;
}



function resetClock() {
	document.getElementById('TourId').value = "none";

}

function setCalendar(calendar,id){
	document.getElementById('ChoiceCalendar').innerHTML = calendar;
	document.getElementById('choiceId').value = id;
}

function setUser(user){
	alert(user);
	document.getElementById('userChoice').value = user;
}


