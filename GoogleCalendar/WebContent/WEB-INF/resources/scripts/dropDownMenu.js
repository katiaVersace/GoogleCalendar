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
	
	document.getElementById('privilages').value = x;
}


function resetShareCalendarValue() {
	document.getElementById('privilages').value  = "none";
	document.getElementById('userChoice').value  = "";
}



function resetClock() {
	document.getElementById('TourId').value = "none";

}

function setCalendar(calendar,id){
	document.getElementById('ChoiceCalendar').innerHTML = calendar;
	document.getElementById('choiceId').value = id;
}

function setUser(user){
	document.getElementById('userChoice').value = user;
}

function addFreq(freq){
	document.getElementById('repChoice').value = freq;
	
}

function resetFreqChoice() {
	document.getElementById('repChoice').value = "none";
	document.getElementById('repPanel').className = "collapse out";
}
