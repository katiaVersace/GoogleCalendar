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

function resetClock() {
	document.getElementById('TourId').value = "none";

}

function setCalendar(calendar,id){
	document.getElementById('ChoiceCalendar').innerHTML = calendar;
	document.getElementById('choiceId').value = id;
}