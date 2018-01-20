var imported = document.createElement('script');
imported.src = 'resources/scripts/openModal.js';
document.head.appendChild(imported);
var imported2 = document.createElement('script');
imported2.src = 'resources/scripts/clocks.js';
document.head.appendChild(imported2);

hoverCell = function(date, date1, cell) {
	if (cell.date >= date && cell.date <= date1) {
		cell.cssClass = 'clicked-cell';
	}
};

angular.module('mwl.calendar.docs', [ 'mwl.calendar', 'ngAnimate',
		'ui.bootstrap', 'colorpicker.module' ]);
angular
		.module('mwl.calendar.docs')
		.controller(
				'KitchenSinkCtrl',
				function($compile, $scope, moment) {

					// Controller
					var vm = this;

					// ---------- //
					// -- DATA -- //
					// ---------- //

					// Default view
					vm.calendarView = 'month';

					// Current date
					vm.viewDate = new Date();

					// Events to be displayed
					vm.events = [];

					// Calendars whose events are currently shown, by id
					vm.shownCalendars = [];

					// Calendars currently checked on the sidebar, by id
					// Only used in conjunction with shownCalendars
					vm.checkedCalendars = [];

					vm.calendarsArray = [];

					vm.cellIsOpen = true;

					// List of glyphs shown after entries in a day's event list,
					// with behavior
					var actions = [ {
						label : '<i class=\'glyphicon glyphicon-pencil\'></i>',
						onClick : function(args) {
							vm.clickUpdateEvent(args.calendarEvent);
						}
					}, {
						label : '<i class=\'glyphicon glyphicon-remove\'></i>',
						onClick : function(args) {
							alert("delete");
							alert(args.calendarEvent.id);
							vm.deleteOccurrence(args.calendarEvent.id);
						},
					} ];

					// ---- MERGE MARIO MARCO
					vm.vtrCell = [];
					// event for modal
					vm.temp = undefined;
					// clicked cell
					vm.lastCellClicked = undefined;
					// temp event to update
					vm.tmpMemo = undefined;
					vm.memo = undefined;
					//

					// --------------- //
					// -- UTILITIES -- //
					// --------------- //

					// Event Constructor
					vm.Event = function(id, calendar, title, description,
							startsAt, endsAt, primaryColor, secondaryColor) {
						this.id = id;
						this.calendar = calendar;
						this.title = title;
						this.description = description;
						this.startsAt = startsAt;
						this.endsAt = endsAt;
						this.color = {
							primary : primaryColor,
							secondary : secondaryColor,
						};
						this.draggable = false;
						this.resizable = false;
						this.actions = actions;
					}

					vm.getViewDateBoundaries = function() {
						return {
							start : moment(vm.viewDate)
									.startOf(vm.calendarView).toDate(),
							end : moment(vm.viewDate).endOf(vm.calendarView)
									.toDate(),
						};
					};

					// ------------------- //
					// -- VIEW HANDLING -- //
					// ------------------- //

					// Repopulate vm.events accordingly to the data fetched from
					// the DB
					vm.updateEventList = function() {
						vm.events = [];

						var boundaries = vm.getViewDateBoundaries();

						vm.shownCalendars
								.forEach(function(calendar_id) {
									vm
											.JSON_getMyEventsInPeriod(
													calendar_id,
													boundaries.start,
													boundaries.end,
													function(events) {
														JSON
																.parse(events)
																.forEach(
																		function(
																				blueprint) {
																			vm.events
																					.push(new vm.Event(
																							blueprint.id,
																							blueprint.calendar.id,
																							blueprint.title,
																							blueprint.description,
																							new Date(
																									blueprint.startTime),
																							new Date(
																									blueprint.endTime),
																							"#555555", // FIXME:
																							// substitute
																							// with
																							// blueprint.primaryColor,
																							"#aaaaaa" // FIXME:
																					// substitute
																					// with
																					// blueprint.secondaryColor,
																					));
																	
																		});
														// Needed for
														// asynchronous update
														// of vm.events
														$scope
														.$digest();

													});
								});
					};

					// Update the list of calendars displayed within the sidebar
					vm.updateCalendarList = function() {
						var viewList = $("#calendarsList");
						vm.calendarsArray = [];
						vm
								.JSON_getAllMyCalendars(function(calendars) {
									viewList.empty();
									JSON
											.parse(calendars)
											.forEach(
													function(calendar) {
														vm.calendarsArray
																.push(calendar);
														viewList
																.append($compile(
																		"<li id=\"cal_entry_"
																				+ calendar.id
																				+ "\">\n"
																				+ "  <label>\n"
																				+ "    <input\n"
																				+ "      type=\"checkbox\"\n"
																				+ "      id= \""
																				+ calendar.id
																				+ "\"\n"
																				+ "      name=\""
																				+ calendar.id
																				+ "\"\n"
																				+ "      value=\""
																				+ calendar.title
																				+ "\"\n"
																				+ "      ng-model=\"vm.checkedCalendars['"
																				+ calendar.id
																				+ "']\"\n"
																				+ "      ng-change=\"vm.toggleCalendar('"
																				+ calendar.id
																				+ "')\"/>\n"
																				+ "    <label for=\""
																				+ calendar.id
																				+ "\"><span></span>"
																				+ calendar.title
																				+ "</label>\n"
																				+ "</label>\n"
																				+ "<label>\n"
																				+ "      <i\n"
																				+ "        class=\"glyphicon glyphicon-cog\"\n"
																				+ "        onclick=\"manageCalendar('${cal.title}','${cal.id}')\"\n"
																				+ "        style=\"margin-left: 80%;\">\n"
																				+ "      </i>\n"
																				+ "    </label>\n"
																				+ "</li>\n")
																		($scope));

													});
									vm.updateCalendarListModal();
								});
					};

					// Update the list of calendars displayed within the Modal
					// Update the list of calendars displayed within the Modal
					vm.updateCalendarListModal = function() {
						var viewList = $("#calendarsListModal");

						var string = "<div  class=\"btn-group\">\n"
								+ "<button type=\"button\" class=\"btn btn-primary dropdown-toggle\""
								+ "data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">"
								+ "<i style=\"font-size: 25px; color: white;\""
								+ "class=\"glyphicon glyphicon-list-alt\"></i> <span class=\"caret\"></span>"
								+ "</button>"
								+ "<h4 id=\"ChoiceCalendar\" style=\"color: white; padding-left:120px;\"></h4>"
								+ "<ul class=\"dropdown-menu\">";

						for (i = 0; i < vm.calendarsArray.length; i++) {

							var title = vm.calendarsArray[i].title;
							var id = vm.calendarsArray[i].id;

							var x = title;
							title = x.replace(/'/g, "\\'");

							string += "<li><a href=\"javascript:void(0)\" onclick=\"setCalendar('"
									+ title
									+ "','"
									+ id
									+ "')\""
									+ " class = \"calendars\" data-id=\""
									+ id
									+ "\">"
									+ vm.calendarsArray[i].title
									+ "</a></li>";
						}

						viewList.append(string);
					};

					// Hide/Show a calendar's events
					vm.toggleCalendar = function(id) {
						if (vm.checkedCalendars[id]) {
							vm.shownCalendars.push(id);
						} else {
							vm.shownCalendars = vm.shownCalendars
									.filter(function(element) {
										return element != id;
									});
						}
						vm.updateEventList();
					};

					// Update event list when
					vm.viewModifierBehavior = function() {
						vm.cellIsOpen = false;
						vm.updateEventList();
					}

					// -------------------------- //
					// -- DATABASE INTERACTION -- //
					// -------------------------- //

					/*
					 * JSON_getMyEventsInPeriod done
					 */
					vm.JSON_getMyEventsInPeriod = function(calendar_id, start,
							end, callback) {
						$
								.ajax({
									type : "POST",
									url : "JSON_getMyEventsInPeriod/"
											+ calendar_id,
									data : {
										start : moment(start).format(
												"YYYY-MM-DD HH:mm:ss"),
										end : moment(end).format(
												"YYYY-MM-DD HH:mm:ss"),
									},
									success : function(response) {
										callback(response);
									},
								});
					};

					/*
					 * JSON_getAllMyCalendars done
					 */
					vm.JSON_getAllMyCalendars = function(callback) {
						$.ajax({
							type : "POST",
							url : "JSON_getAllMyCalendars",
							success : function(response) {
								callback(response);
							},
						});
					};

					/*
					 * insertNewEvent done
					 */
					vm.insertNewEvent = function(calendar_id, title,
							description, startsAt, endsAt, primaryColor,
							secondaryColor) {
						$.ajax({
							type : "POST",
							url : "insertNewEvent/" + calendar_id,
							data : {
								title : title,
								description : description,
								startTime : startsAt,
								endTime : endsAt,
								c1 : primaryColor,
								c2 : secondaryColor,
							},
							success : function(response) {
								if (response != -1) {
									vm.updateEventList();
								}
							},
						});
					};

					/*
					 * updateEvent done
					 */
					vm.updateEvent = function(id, title, description, startsAt,
							endsAt, primaryColor, secondaryColor) {
						$.ajax({
							type : "POST",
							url : "updateEvent/" + id,
							data : {
								title : title,
								description : description,
								startTime : startsAt,
								endTime : endsAt,
								c1 : primaryColor,
								c2 : secondaryColor,
							},
							success : function(response) {
								if (response == "YES") {
									vm.updateEventList();
								}
							},
						});
					};

					/*
					 * deleteOccurenceId done
					 */
					vm.deleteOccurrence = function(id) {
						$.ajax({
							type : "POST",
							url : "deleteOccurrence/" + id,
							success : function(response) {
								if (response == "YES") {
									vm.updateEventList();
								} else {
									console.log("ERROR: " + response);
								}
							},
						});
					};

					/*
					 * insertNewMemo ni
					 */
					vm.insertNewMemo = function( /* ... */) {
						$.ajax({

						});
					};

					/*
					 * updateMemo ni
					 */
					vm.updateMemo = function( /* ... */) {
						// TODO
					};

					/*
					 * insertNewCalendar
					 */
					vm.insertNewCalendar = function(title, description) {
						$.ajax({
							type : "POST",
							url : "insertNewCalendar",
							data : {
								title : title,
								description : description,
							},
							success : function(response) {
								if (response != -1) {
									vm.updateCalendarList();
								}
							},
						});
					};

					/*
					 * updateCalendar
					 */
					vm.updateCalendar = function(calendar_id, title,
							description) {
						$.ajax({
							type : "POST",
							url : "update/" + calendar_id,
							data : {
								title : title,
								description : description,
							},
							success : function(response) {
								if (response == "YES") {
									vm.updateCalendarList();
								}
							},
						});
					};

					/*
					 * disconnectFromCalendar
					 */
					vm.disconnectFromCalendar = function(id) {
						$.ajax({
							type : "POST",
							url : "disconnect/" + id,
							success : function(response) {
								if (response == "YES") {
									vm.updateCalendarList();
									vm.updateEventList();
								}
							},
						});
					};

					/*
					 * updateUser
					 */
					vm.updateUser = function(username, password) {
						$.ajax({
							type : "POST",
							url : "updateUser",
							data : {
								username : username,
								password : password,
							},
							success : function(response) {
								// FIXME: graphical representations of the
								// username
								// inside the page need to be updated.
								// IndexController
								// should expose a function for retrieving the
								// current
								// username, to be used at page initialization
								// and after
								// a successful call to vm.updateUser
							},
						});
					};

					/*
					 * sendInvitation
					 */
					vm.sendInvitation = function(calendar_id, email, privileges) {
						$.ajax({
							type : "POST",
							url : "sendInvitation/" + calendar_id,
							data : {
								receiver_email : email,
								privilege : privileges,
							},
							success : function(response) {
								if (reponse == "YES") {
									// TODO
								}
							},
						});
					};

					// ---------- //
					// -- INIT -- //
					// ---------- //

					(function() {
						vm.updateCalendarList();
					})();

					// ---------------------------- //
					// -- WASTELAND -- //
					// -- enter at your own risk -- //
					// ---------------------------- //

					vm.eventClicked = function(event) {
						// TODO
					};

					vm.eventEdited = function(event) {
						// TODO
					};

					vm.eventDeleted = function(event) {
						// TODO
					};

					vm.eventTimesChanged = function(event) {
						// TODO
					};

					vm.toggle = function($event, field, event) {
						$event.preventDefault();
						$event.stopPropagation();
						event[field] = !event[field];
					};

					vm.clickUpdateEvent = function(event) {
						if (!event.memo) {

							vm.temp = {
								title : event.title,
								id : event.id,
								memo : false,
								description : event.description,
								clock : event.clock, // TODO: fix clock on DB
								startsAt : event.startsAt,
								endsAt : event.endsAt,
								color : {
									primary : event.color.primary,
									secondary : event.color.secondary,
								},
								draggable : false,
								resizable : false,
								actions : actions
							};

							updateClock(vm.temp.clock);
							// document.getElementById("TourId").value =
							// vm.temp.clock;
							modal(6);
						} else {
							var str = event.title;
							var res = str.slice(102);
							vm.memo = {
								title : res,
								id : event.id,
								memo : true,
								description : event.description,
								startsAt : moment(),
								color : {
									primary : event.color.primary,
								},
								draggable : false,
								resizable : false,
								actions : actions
							};
							vm.tmpMemo = event;
							modal(8);
						}
					};

					vm.rangeSelected = function(startDate, endDate) {

						if (vm.lastCellClicked != undefined) {
							vm.lastCellClicked.cssClass = '.clear-cell';
						}

						vm.firstDateClicked = startDate;
						vm.lastDateClicked = endDate;
						vm.temp = {
							title : 'New event',
							id : '',
							memo : false,
							description : '',
							clock : 'none',
							startsAt : startDate,
							endsAt : endDate,
							color : {
								primary : "#123456",
								secondary : "#123458"
							},
							draggable : false,
							resizable : false,
							actions : actions
						};
						vm.openEventModal();

					};

					vm.timespanClicked = function(date, cell) {
						vm.firstDateClicked = date;
						vm.lastDateClicked = date;

						if (vm.lastCellClicked != undefined) {
							vm.lastCellClicked.cssClass = '.clear-cell';
						}

						hoverCell(vm.firstDateClicked, vm.lastDateClicked, cell);
						vm.lastCellClicked = cell;

						vm.temp = {
							id : '',
							title : 'New event',
							description : '',
							memo : false,
							clock : 'none',
							startsAt : vm.firstDateClicked,
							endsAt : vm.lastDateClicked,
							color : {
								primary : "#123456",
								secondary : "#123456",
							},
							draggable : false,
							resizable : false,
							actions : actions
						};
						document.getElementById('btn-add').disabled = false;

						if (vm.calendarView === 'month') {
							if ((vm.cellIsOpen && moment(date).startOf('day')
									.isSame(moment(vm.viewDate).startOf('day')))
									|| cell.events.length === 0
									|| !cell.inMonth) {
								vm.cellIsOpen = false;
							} else {
								vm.cellIsOpen = true;
								vm.viewDate = date;
							}
						} else if (vm.calendarView === 'year') {
							if ((vm.cellIsOpen && moment(date).startOf('month')
									.isSame(
											moment(vm.viewDate)
													.startOf('month')))
									|| cell.events.length === 0) {
								vm.cellIsOpen = false;
							} else {
								vm.cellIsOpen = true;
								vm.viewDate = date;
							}
						}
					};

					vm.cellModifier = function(cell) {
						vm.vtrCell.push(cell);
						console.log(vm.vtrCell.length);
						console.log(vm.vtrCell[length - 1]);

					};

					// MANAGE EVENT
					vm.openEventModal = function() {
						modal(5);
					};

					// add event press button action
					vm.addEventView = function() {

						var idCalendar = document.getElementById("choiceId").value;

						// ///////////// TO DO /////////////////// ADD ATTR in
						// INSERTNEWEVENT
						vm.temp.clock = document.getElementById("TourId").value;

						console.log("event inserted => " + idCalendar + " "
								+ vm.temp.title + " " + vm.temp.description
								+ " " + vm.temp.startsAt + " " + vm.temp.endsAt
								+ " " + vm.temp.color.primary + " "
								+ vm.temp.color.secondary);

						if (idCalendar != undefined) {
							vm.insertNewEvent(idCalendar, vm.temp.title,
									vm.temp.description, vm.temp.startsAt,
									vm.temp.endsAt, vm.temp.color.primary,
									vm.temp.color.secondary);

							document.getElementById('btn-add').disabled = true;

							resetClock();
							document.getElementById('modal-wrapper5').style.display = 'none';

						} else {
							alert("please choose a calendar");
						}
					}

					// update event press button action
					vm.updateEventView = function() {

						var idCalendar = document.getElementById("choiceId").value;

						vm.updateEvent(vm.temp.id, vm.temp.title,
								vm.temp.description, vm.temp.startsAt,
								vm.temp.endsAt, vm.temp.color.primary,
								vm.temp.color.secondary);

						resetClock();
						document.getElementById('modal-wrapper6').style.display = 'none';

					}

					// MANAGE MEMO
					vm.openMemoModal = function() {
						modal(7);

						vm.memo = {
							title : 'New Memo',
							id : '',
							startsAt : moment(),
							color : {
								primary : "#123456",
							},
							draggable : false,
							resizable : false,
							memo : true,
							actions : actions
						};

					};

					// add memo press button action
					vm.addMemoView = function() {

						// use tmp.memo variables

						vm.memo.title = '<i class="glyphicon glyphicon-tag" style=" color: #42A5F5; font-size: 20px; margin-right: 10px; "></i>'
								+ vm.memo.title;

						// TO DO
						// vm.insertNewMemo(....);

						document.getElementById('modal-wrapper7').style.display = 'none';

					}

					// update memo press button action
					vm.updateMemoView = function() {

						// use tmp.memo variables

						vm.memo.title = '<i class="glyphicon glyphicon-tag" style=" color: #42A5F5; font-size: 20px; margin-right: 10px; "></i>'
								+ vm.memo.title;
						// TO DO
						// vm.updateMemo(...);

						document.getElementById('modal-wrapper8').style.display = 'none';
					}
				});