function addClock(x) {

	if (x < 0) {
		document.getElementById('TourId').value = "none";
	} else {
		document.getElementById('TourId').value = x;
	}

	// alert("dentro prova TOUR ID VALUE ");
	// alert(document.getElementById('TourId').value)

}
function updateClock(x) {

	if (x < 0) {
		document.getElementById('TourId2').value = "none";
	} else {
		document.getElementById('TourId2').value = x;
	}

	// alert("dentro prova TOUR ID 2 VALUE ");
	// alert(document.getElementById('TourId2').value)

}
function resetClock() {

	document.getElementById('TourId').value = "none";

}

function setCalendar(calendar,id){
	
	alert(id);
	
	document.getElementById('ChoiceCalendar').innerHTML = calendar;
	document.getElementById('choiceId').value = id;
	
	
	
}