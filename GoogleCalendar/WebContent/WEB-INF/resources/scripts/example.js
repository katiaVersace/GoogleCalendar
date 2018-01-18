angular.module('mwl.calendar.docs', ['mwl.calendar', 'ngAnimate', 'ui.bootstrap', 'colorpicker.module']);
angular
  .module('mwl.calendar.docs')
  .controller('KitchenSinkCtrl', function(moment, calendarConfig) {     
      
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
    
    // Calendars currently shown, by id
    vm.shownCalendars = [];
        
    // Calendars currently checked on the sidebar, by id
    // FIXME: only used in conjunction with shownCalendars, 
    //        there may be a better way to do this
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
    
    // --------------- //
    // -- UTILITIES -- //
    // --------------- //
    
    // Looks for an element in vm.events by id,
    // returning the event or null if not found.
    vm.getEventViewByID = function (id) {
        var event = vm.events.filter(function (item) {
            return item.id = id;
        })
        return event ? event[0] : null;
    };
    
    vm.getEventDataByID = function (id) {
        // FIXME: uses view for data, can be done better
        var event = vm.getEventViewByID(id);
        
        if (event != null) {
            for (var i = 0; i < edb[event.calendar][events].length; i++) {
                var current = edb[event.calendar][events][i];
                if (current.id == id) {
                    event = current;
                }
            }
        }
        
        return event;
    };
    
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
    
    // Add an event to a calendar
    // TODO: rewrite entirely
    vm.addEvent = function() {
        vm.events.push({
            title: 'New event',
            startsAt: moment().startOf('day').toDate(),
            endsAt: moment().endOf('day').toDate(),
            color: calendarConfig.colorTypes.important,
            draggable: false,
            resizable: false,
            actions: actions
        });
    };
    
    // Makes a calendar's events visible
    vm.showCalendar = function (id) {        
        // TODO
    };
    
    vm.hideCalendar = function (id) {
        // TODO
    };
    
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
     * deleteCalendarId
     */
    vm.deleteCalendar = function (id) {
        // Delete calendar in DB
        $.ajax({
            type: "POST",
            url: "delete/" + id,
            success: function (response) {
                // Client-side deletion (vm.events is updated accordingly)
                vm.hideCalendar(id);
                delete edb[id];
                
                // Calendar's entry in sidebar is deleted
                $("#cal_entry_" + id).remove();
            },
            error: function (response) {
                console.log("ERROR ERROR ERROR ERROR ERROR");
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
     * insertNewCalendar
     */
    vm.createCalendar = function (title, description) {
        $.ajax({
            type: "POST",
            url: "insertNewCalendar",
            data: {
                title: title,
                description: description,
            },
            success: function (response) {
                if (response != -1) {
                    // add new calendar to edb
                    edb[response] = {
                        title: title,
                        description: description,
                        events: [],
                    };
                    
                    // create new entry and add it to the sidebar menu
                    var entry = 
                        "<li>\n"
                      + "  <label id=\"cal_entry_" + response + "\">\n"
                      + "    <input\n"
                      + "      type=\"checkbox\"\n"
                      + "      name=\"" + response + "\"\n"
                      + "      value=\"" + edb[response].title + "\"\n"
                      + "      ng-model=\"vm.checkedCalendars['" + response + "']\"\n"
                      + "      ng-change=\"vm.toggleCalendar('" + response + "')\"\n/>"
                      + "     " + edb[response].title + "\n"
                      + "  </label>\n"
                      + "</li>\n";
                    
                    // FIXME: clicking on this entry doesn't work without a page refresh!
                    $("#calendarsList").append(entry);
                    
                } else {
                    console.log(
                        "[UNSUCCESSFUL] vm.createCalendar:\n"
                      + "{\n"
                      + "    title: " + title + "\n"
                      + "    description: " + description + "\n"
                      + "}\n"
                    );
                }
            },
            error: function (response) {
                console.log(
                    "[ERROR] vm.createCalendar:\n"
                  + "{\n"
                  + "    title: " + title + "\n"
                  + "    description: " + description + "\n"
                  + "}\n"
                );
            },
        });
    };
    
    /*
     * updateCalendar
     */
    vm.updateCalendar = function (calendarId, title, description) {
        $.ajax({
            type: "POST",
            url: "update/" + calendarId,
            data: {
                title: title,
                description: description,
            },
            success: function (response) {
                if (response == "YES") {
                    // update edb
                    edb[calendarId].title = title;
                    edb[calendarId].description = description;
                    // TODO: update date (?)
                    
                    // update calendar list entry
                    $("#cal_entry_" + calendarId + " > label > input").attr("value", title);
                    $("#cal_entry_" + calendarId + " > label").contents().last().replaceWith(title);
                } else {
                    console.log(
                        "[UNSUCCESSFUL] vm.updateCalendar:\n"
                      + "{\n"
                      + "    calendarId: " + calendarId + "\n"
                      + "    title: " + title + "\n"
                      + "    description: " + description + "\n"
                      + "}\n"
                    );
                }
            },
            error: function (response) {
                console.log(
                    "[ERROR] vm.updateCalendar:\n"
                  + "{\n"
                  + "    calendarId: " + calendarId + "\n"
                  + "    title: " + title + "\n"
                  + "    description: " + description + "\n"
                  + "}\n"
                );
            },
        });
    }
    
    /*
     * insertNewEvent
     */
    vm.insertNewEvent = function ( /* ... */ ) {
        // TODO
    }
    
    /*
     * insertNewMemo
     */
    vm.insertNewMemo = function ( /* ... */ ) {
        // TODO
    }
    
    /*
     * deleteOccurenceId
     */
    vm.deleteOccurrence = function (id) {
        var event = vm.getEventViewByID(id);
        if (event != null) {
            $.ajax({
                type: "POST",
                url: "deleteOccurrence/" + id,
                success: function (response) {
                    if (response == "YES") {
                        // view is updated
                        vm.events = vm.events.filter(function (item) {
                            return item.id != id;
                        });
                        
                        // edb is updated
                        edb[event.calendar].events = edb[event.calendar].events.filter(function (item) {
                            return item.id != id;
                        });
                        
                        // DEBUG
                        console.log("[SUCCESS] vm.deleteOccurrence | response: " + response);
                        // END DEBUG
                    } else {
                        console.log(
                            "[UNSUCCESSFUL] vm.deleteOccurrence:\n"
                          + "{\n"
                          + "    eventId: " + id + "\n"
                          + "}\n"
                        );
                    }
                },
                error: function (response) {
                    console.log(
                        "[ERROR] vm.deleteOccurrence:\n"
                      + "{\n"
                      + "    eventId: " + id + "\n"
                      + "}\n"
                    );
                },
            });
        }
    };
    
    /*
     * updateEvent
     */
    vm.updateEvent = function (id, title, date, description) {
        $.ajax({
            type: "POST",
            url: "updateMemo/" + id,
            data: {
                title: title,
                data: date,
                description: description,
            },
            success: function (response) {
                if (response == "YES") {
                    var eventView = vm.getEventViewByID(id);
                    var eventData = vm.getEventDataByID(id);
                    var now = new Date();
                    
                    // FIXME: la storia delle date
                    // FIXME: fatta così sta cosa fa schifo al cazzo
                    if (eventView != null) {
                        eventView.title = title;
                        eventView.startsAt = now;
                        eventView.endsAt = now;
                        eventView.description = description;
                    }
                    
                    if (eventData != null) {
                        eventData.title = title;
                        eventData.startsAt = now;
                        eventData.endsAt = now;
                        eventData.description = description;
                    }
                } else {
                    console.log(
                        "[UNSUCCESSFUL] vm.updateEvent:\n"
                      + "{\n"
                      + "    eventId: " + id + "\n"
                      + "    title: " + title + "\n"
                      + "    date: " + date + "\n"
                      + "    description: " + description + "\n"
                      + "    response: " + JSON.stringify(response, null, 4) + "\n"
                      + "}\n"
                    );
                }
            },
            error: function (response) {
                console.log(
                    "[ERROR] vm.updateEvent:\n"
                  + "{\n"
                  + "    eventId: " + id + "\n"
                  + "    title: " + title + "\n"
                  + "    date: " + date + "\n"
                  + "    description: " + description + "\n"
                  + "}\n"
                );
            },
        })
    };
    
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
    
    /*
     * JSON_getAllMyCalendars
     */
    vm.JSON_getAllMyCalendars = function () {
        $.ajax({
            type: "POST",
            url: "JSON_getAllMyCalendars",
            success: function (response) {
                // TODO
            },
            error: function (response) {
                // TODO
            },
        });
    };
    
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
                console.log(JSON.stringify(vm.events, null, 4));
            },
            error: function (response) {
                // TODO
            },
        });
    }

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

});