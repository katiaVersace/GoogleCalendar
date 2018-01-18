angular.module('mwl.calendar.docs', ['mwl.calendar', 'ngAnimate', 'ui.bootstrap', 'colorpicker.module']);
angular
  .module('mwl.calendar.docs')
  .controller('KitchenSinkCtrl', function($compile, $scope, moment) {     
      
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
    
    vm.cellIsOpen = true;
    
    // List of glyphs shown after entries in a day's event list,
    // with behavior
    var actions = [{
        label: '<i class=\'glyphicon glyphicon-remove\'></i>',
        onClick: function(args) {
            vm.deleteOccurrence(args.calendarEvent.id);
      },
    }];
    
    // --------------- //
    // -- UTILITIES -- //
    // --------------- //
    
    // Event Constructor
    vm.Event = function (id, calendar, title, description, 
    		startsAt, endsAt, primaryColor, secondaryColor) {
    	this.id = id;
    	this.calendar = calendar;
    	this.title = title;
    	this.description = description;
    	this.startsAt = startsAt;
    	this.endsAt = endsAt;
    	this.color = {
			primary: primaryColor,
			secondary: secondaryColor,
    	};
    	this.draggable = false;
    	this.resizable = false;
    	this.actions = actions;
    }
    
    vm.getViewDateBoundaries = function () {
    	var leftBoundary = moment(vm.viewDate).startOf(vm.calendarView);
    	var rightBoundary = moment(vm.viewDate).endOf(vm.calendarView);
    	
    	if (vm.calendarView == "month") {
    		leftBoundary.subtract(1, "week");
    		rightBoundary.add(1, "week");
    	}
    	
    	return {
            start: leftBoundary.toDate(),
            end: rightBoundary.toDate(),
        };
    };
    
    // ------------------- //
    // -- VIEW HANDLING -- //
    // ------------------- //
    
    // Repopulate vm.events accordingly to the data fetched from the DB
    vm.updateEventList = function () {        
        vm.events = [];

        var boundaries = vm.getViewDateBoundaries();

    	vm.shownCalendars.forEach(function (calendar_id) {
    		vm.JSON_getMyEventsInPeriod(calendar_id, boundaries.start, boundaries.end, function (events) {
       			JSON.parse(events).forEach(function (blueprint) {
    				vm.events.push(new vm.Event(
    					blueprint.id,
    					blueprint.calendar.id,
    					blueprint.title,
    					blueprint.description,
    					new Date(blueprint.startTime),
    					new Date(blueprint.endTime),
    					"#555555", // FIXME: substitute with blueprint.primaryColor,
    					"#aaaaaa" // FIXME: substitute with blueprint.secondaryColor,
    				));
    			});
    		});
    	});
    };
    
    // Update the list of calendars displayed within the sidebar
    vm.updateCalendarList = function () {
    	var viewList = $("#calendarsList");
    	vm.JSON_getAllMyCalendars(function (calendars) {
    		viewList.empty();
    		JSON.parse(calendars).forEach(function (calendar) {
    			viewList.append(
    			    $compile(
        				"<li id=\"cal_entry_" + calendar.id + "\">\n"
        			  + "  <label>\n"
                      + "    <input\n"
                      + "      type=\"checkbox\"\n"
                      + "      name=\"" + calendar.id + "\"\n"
                      + "      value=\"" + calendar.title + "\"\n"
                      + "      ng-model=\"vm.checkedCalendars['" + calendar.id + "']\"\n"
                      + "      ng-change=\"vm.toggleCalendar('" + calendar.id + "')\"/>\n"
                      + "     " + calendar.title + "\n"
                      + "  </label>\n"
                      + "</li>\n"
                    )($scope)
    			);
    		});
    	});
    };
    
    // Hide/Show a calendar's events
    vm.toggleCalendar = function (id) {
    	if (vm.checkedCalendars[id]) {
    		vm.shownCalendars.push(id);
    	} else {
    		vm.shownCalendars = vm.shownCalendars.filter(function (element) {
    			return element != id;
    		});
    	}
    	vm.updateEventList();
    };
    
    // -------------------------- //
    // -- DATABASE INTERACTION -- //
    // -------------------------- //
    
    /*
     * JSON_getMyEventsInPeriod
     */
    vm.JSON_getMyEventsInPeriod = function (calendar_id, start, end, callback) {
    	$.ajax({
    		type: "POST",
    		url: "JSON_getMyEventsInPeriod/" + calendar_id,
    		data: {
    			start: moment(start).format("YYYY-MM-DD HH:mm:ss"),
    			end: moment(end).format("YYYY-MM-DD HH:mm:ss"),
    		},
    		success: function (response) {
    			callback(response);
    		},
    	});
    };
    
    /*
     * JSON_getAllMyCalendars
     */
    vm.JSON_getAllMyCalendars = function (callback) {
    	$.ajax({
    		type: "POST",
    		url: "JSON_getAllMyCalendars",
    		success: function (response) {
    			callback(response);
    		},
    	});
    };
    
    /*
     * insertNewEvent
     */
    vm.insertNewEvent = function (calendar_id, title, description, 
    		startsAt, endsAt, primaryColor, secondaryColor) {
    	$.ajax({
    		type: "POST",
    		url: "insertNewEvent/" + calendar_id,
    		data: {
    			title: title,
    			description: description,
    			startTime: startsAt,
    			endTime: endsAt,
    			c1: primaryColor,
    			c2: secondaryColor,
    		},
    		success: function (response) {
    			if (response != -1) {
    				vm.updateEventList();
    			}
    		},
    	});
    };
    
    /*
     * updateEvent
     */
    vm.updateEvent = function (id, title, description, 
    		startsAt, endsAt, primaryColor, secondaryColor) {
    	$.ajax({
    		type: "POST",
    		url: "updateEvent/" + id,
    		data: {
    			title: title,
    			description: description,
    			startTime: startsAt,
    			endTime: endsAt,
    			c1: primaryColor,
    			c2: secondaryColor,
    		},
    		success: function (response) {
    			if (response == "YES") {
    				vm.updateEventList();
    			}
    		},
    	});
    };
    
    /*
     * deleteOccurenceId
     */
    vm.deleteOccurrence = function (id) {

        // TODO: rewrite the interface on IndexController so that
        // only the occurence's id is needed
        
    };
    
    /*
     * insertNewCalendar
     */
    vm.insertNewCalendar = function (title, description) {
    	$.ajax({
    	   type: "POST",
    	   url: "insertNewCalendar",
    	   data: {
    	       title: title,
    	       description, description,
    	   },
    	   success: function (response) {
    	       if (response != -1) {
    	           vm.updateCalendarList();
    	       }
    	   },
    	});
    };
    
    /*
     * deleteCalendarId
     */
    vm.deleteCalendar = function (id) {
        $.ajax({
            type: "POST",
            url: "delete/" + id,
            success: function (response) {
                if (response == "YES") {
                    vm.updateCalendarList();
                    vm.updateEventList();
                }
            },
        });
    };
    
    /*
     * updateCalendar
     */
    vm.updateCalendar = function (calendar_id, title, description) {
        $.ajax({
            type: "POST",
            url: "update/" + calendar_id,
            data: {
                title: title,
                description: description,
            },
            success: function (response) {
                if (response == "YES") {
                    vm.updateCalendarList();
                }
            },
        });
    };
    
    /*
     * disconnectFromCalendar
     */
    vm.disconnectFromCalendar = function (id) {
        // TODO
    }
    
    /*
     * insertNewMemo
     */
    vm.insertNewMemo = function ( /* ... */ ) {
        // TODO
    }
    
    /*
     * updateMemo
     */
    vm.updateMemo = function ( /* ... */ ) {
        // TODO
    }
    
    /*
     * updateUser
     */
    vm.updateUser = function ( /* ... */ ) {
        // TODO
    }
    
    /*
     * sendInvitation
     */
    vm.sendInvitation = function (/* ... */) {
        // TODO
    }
    
    // ---------- //
    // -- INIT -- //
    // ---------- //
    
    vm.updateCalendarList();
    
    // ---------------------------- //
    // --       WASTELAND        -- //
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

    vm.timespanClicked = function(date, cell) { 
        if (vm.calendarView === 'month') {
            if ((vm.cellIsOpen && moment(date).startOf('day').isSame(moment(vm.viewDate).startOf('day'))) || cell.events.length === 0 || !cell.inMonth) {
                vm.cellIsOpen = false;
            } else {
                vm.cellIsOpen = true;
                vm.viewDate = date;
            }
        } else if (vm.calendarView === 'year') {
            if ((vm.cellIsOpen && moment(date).startOf('month').isSame(moment(vm.viewDate).startOf('month'))) || cell.events.length === 0) {
                vm.cellIsOpen = false;
            } else {
                vm.cellIsOpen = true;
                vm.viewDate = date;
            }
        }    
    };
    
    // ----------- //
    // -- DEBUG -- //
    // ----------- //

    vm.fn = function () {
    };
});