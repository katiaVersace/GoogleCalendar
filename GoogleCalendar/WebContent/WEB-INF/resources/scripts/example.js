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
    
    vm.calendarsArray = [];
    
    vm.cellIsOpen = true;
    
    // List of glyphs shown after entries in a day's event list,
    // with behavior
    var actions = [{
        label : '<i class=\'glyphicon glyphicon-pencil\'></i>',
        onClick : function(args) {
            vm.clickUpdateEvent(args.calendarEvent);
        }
      }, {
        label: '<i class=\'glyphicon glyphicon-remove\'></i>',
        onClick: function (args) {
            // TODO: delete occurrence
      },
    }];
    
    // ---- MERGE MARIO MARCO
    vm.vtrCell = [];
    // event for modal
    vm.temp = undefined;
    // clicked cell
    vm.lastCellClicked = undefined;
    vm.contId = 0;
    // temp event to update
    vm.tmpEvt = undefined;
    vm.tmpMemo = undefined;
    vm.memo = undefined;
    //
    
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
        return {
            start: moment(vm.viewDate).startOf(vm.calendarView).toDate(),
            end: moment(vm.viewDate).endOf(vm.calendarView).toDate(),
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
       			// Needed for asynchronous update of vm.events
       			$scope.$digest();
        		});
        	});
    };
    
    // Update the list of calendars displayed within the sidebar
    vm.updateCalendarList = function () {
        	var viewList = $("#calendarsList");
        	vm.calendarsArray = [];
        	vm.JSON_getAllMyCalendars(function (calendars) {
        		viewList.empty();
        		JSON.parse(calendars).forEach(function (calendar) {
        			vm.calendarsArray.push(calendar);
        			viewList.append(
                     $compile(
                          "<li id=\"cal_entry_" + calendar.id + "\">\n"
                        + "  <label>\n"
                        + "    <input\n"
                        + "      type=\"checkbox\"\n"
                        + "      id= \"" + calendar.id + "\"\n"
                        + "      name=\"" + calendar.id + "\"\n"
                        + "      value=\"" + calendar.title + "\"\n"
                        + "      ng-model=\"vm.checkedCalendars['" + calendar.id + "']\"\n"
                        + "      ng-change=\"vm.toggleCalendar('" + calendar.id + "')\"/>\n"
                        + "    <label for=\"" + calendar.id + "\"><span></span>" + calendar.title + "</label>\n"
                        + "</label>\n"
                        + "<label>\n" 
                        + "      <i\n"
                        + "        class=\"glyphicon glyphicon-cog\"\n"
                        + "        onclick=\"manageCalendar('${cal.title}','${cal.id}')\"\n"
                        + "        style=\"margin-left: 80%;\">\n"
                        + "      </i>\n"
                        + "    </label>\n"
                        + "</li>\n"
                     )($scope)
        			);
        			
        		});
        		vm.updateCalendarListModal();	
        	});
     };
    
    
    
 // Update the list of calendars displayed within the Modal
 // Update the list of calendars displayed within the Modal
    vm.updateCalendarListModal = function () {
        	var viewList = $("#calendarsListModal");
        	
        	var string = "<div  class=\"btn-group\">\n"
                +"<button type=\"button\" class=\"btn btn-primary dropdown-toggle\"" 
                +  "data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">"
                +"<i style=\"font-size: 25px; color: white;\"" 
                +  "class=\"glyphicon glyphicon-list-alt\"></i> <span class=\"caret\"></span>"
                +"</button>"
                +"<h4 id=\"ChoiceCalendar\" style=\"color: white; padding-left:120px;\"></h4>"
                +"<ul class=\"dropdown-menu\">";
        	
        	for(i = 0;i< vm.calendarsArray.length;i++) {
        		
        		var title = vm.calendarsArray[i].title;
        		var id = vm.calendarsArray[i].id;
        		
        		var x = title;
        		title = x.replace(/'/g,"\\'");
        		alert(title);
        	 
        		
        		string+="<li><a href=\"javascript:void(0)\" onclick=\"setCalendar('"+title+"','"+id+"')\"" +
        			" class = \"calendars\" data-id=\"" + id+ "\">" +vm.calendarsArray[i].title+"</a></li>";
        		}
           	viewList.append(string);      	
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
    
    // Update event list when
    vm.viewModifierBehavior = function () {
        vm.cellIsOpen = false;
        vm.updateEventList();
    }
    
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
        $.ajax({
            type: "POST",
            url: "deleteOccurrence/" + id,
            success: function (response) {
                if (response == "YES") {
                    vm.updateEventList();
                }
            },
        });
    };
    
    /*
     * insertNewMemo
     */
    vm.insertNewMemo = function ( /* ... */ ) {
        $.ajax({
            
        });
    };
    
    /*
     * updateMemo
     */
    vm.updateMemo = function ( /* ... */ ) {
        // TODO
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
        $.ajax({
            type: "POST",
            url: "disconnect/" + id,
            success: function (response) {
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
    vm.updateUser = function (username, password) {
        $.ajax({
            type: "POST",
            url: "updateUser",
            data: {
                username: username,
                password: password,
            },
            success: function (response) {
                // FIXME: graphical representations of the username
                //        inside the page need to be updated. IndexController
                //        should expose a function for retrieving the current
                //        username, to be used at page initialization and after
                //        a successful call to vm.updateUser
            },
        });
    };
    
    /*
     * sendInvitation
     */
    vm.sendInvitation = function (calendar_id, email, privileges) {
        $.ajax({
            type: "POST",
            url: "sendInvitation/" + calendar_id,
            data: {
                receiver_email: email,
                privilege: privileges,
            },
            success: function (response) {
                if (reponse == "YES") {
                    // TODO
                }
            },
        });
    };
    
    // ---------- //
    // -- INIT -- //
    // ---------- //
    
    (function () {
        vm.updateCalendarList();        
    })();
    
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


    vm.clickUpdateEvent = function(event) {
        	if (!event.memo) {
        		vm.temp = {
        			title : event.title,
        			id : event.id,
        			memo : false,
        			descr : event.descr,
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
        
        		vm.tmpEvt = event;
        		
        		updateClock(vm.temp.clock);
        		// document.getElementById("TourId").value = vm.temp.clock;
            modal(6);
        } else {
            var str = event.title;
            var res = str.slice(102);
            vm.memo = {
                title : res,
                id : event.id,
                memo : true,
                descr : event.descr,
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
            id : vm.contId,
            memo : false,
            descr : '',
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
            id : vm.contId,
            title : 'New event',
            descr : '',
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

	vm.addEvent = function() {

		// var value =
		// document.getElementById("descEvent").value;
		// vm.temp.descr = value;

		vm.temp.clock = document.getElementById("TourId").value;
		
		// TODO: remove
		vm.events.push(vm.temp);
		vm.contId++;

		document.getElementById('btn-add').disabled = true;

		// contenuto del modale evento
		// var id = document.querySelector('input[name =
		// "rr"]:checked').value;
		// var title = document.getElementById('titl').value;
		// var colP = document.getElementById('colP').value;
		// var colS = document.getElementById('colS').value;
		// var dataStart =
		// document.getElementById('dataStart').value;
		// var timeStart =
		// document.getElementById('timeStart').value ;
		// var dataEnd =
		// document.getElementById('dataEnd').value;
		// var timeEnd =
		// document.getElementById('timeEnd').value;
		// alert(vm.temp.id);
		resetClock();
		document.getElementById('modal-wrapper5').style.display = 'none';
	}

	vm.updateEvents = function() {
		
		var index = vm.events.indexOf(vm.tmpEvt);
		if (index > -1) {
		    // TODO: remove
			vm.events.splice(index, 1);
		}

		//vm.events[index].title = vm.temp.title
		vm.tmpEvt = {
			title : vm.temp.title,
			id : vm.temp.id,
			memo : false,
			descr : vm.temp.descr,
			startsAt : vm.temp.startsAt,
			endsAt : vm.temp.endsAt,
			color : {
				primary : vm.temp.color.primary,
				secondary : vm.temp.color.secondary,
			},
			draggable : false,
			resizable : false,
			actions : actions
		};

		
		vm.tmpEvt.clock = document.getElementById('TourId2').value;
		// TODO: remove
		vm.events.push(vm.tmpEvt);
		
		resetClock();
		document.getElementById('modal-wrapper6').style.display = 'none';

	}

	// MANAGE MEMO
	vm.openMemoModal = function() {
		modal(7);

		vm.memo = {
			title : 'New Memo',
			id : vm.contId,
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

	// questa funzione non servira' piu' perche' facciamo l'update direttamente dal database
	vm.addMemo = function() {

		vm.memo.title = '<i class="glyphicon glyphicon-tag" style=" color: #42A5F5; font-size: 20px; margin-right: 10px; "></i>'
				+ vm.memo.title;
		// TODO: remove
		vm.events.push(vm.memo);
		vm.contId++;
		document.getElementById('modal-wrapper7').style.display = 'none';

	}

	vm.updateMemo = function() {
		var index = vm.events.indexOf(vm.tmpMemo);
		if (index > -1) {
		    // TODO: remove
			vm.events.splice(index, 1);
		}

		vm.memo.title = '<i class="glyphicon glyphicon-tag" style=" color: #42A5F5; font-size: 20px; margin-right: 10px; "></i>'
				+ vm.memo.title;
		vm.tmpMemo = {
			title : vm.memo.title,
			id : vm.memo.id,
			descr : vm.memo.descr,
			startsAt : moment(),
			color : {
				primary : vm.memo.color.primary,
			},
			draggable : false,
			resizable : false,
			memo : true,
			actions : actions
		};

		// TODO: remove
		vm.events.push(vm.tmpMemo);
		document.getElementById('modal-wrapper8').style.display = 'none';
	}
});