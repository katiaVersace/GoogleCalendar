// --------------------------- //
// -- MERGE MARIO MARCO [0] -- //
// --------------------------- //

var imported = document.createElement('script');
imported.src = 'resources/scripts/openModal.js';
document.head.appendChild(imported);
var imported2 = document.createElement('script');
imported2.src = 'resources/scripts/dropDownMenu.js';
document.head.appendChild(imported2);

hoverCell = function(date, date1, cell) {
    if (cell.date >= date && cell.date <= date1) {
        cell.cssClass = 'clicked-cell';
    }
};

// --------------------------//
// -- ANGULARJS CONTROLLER --//
// --------------------------//

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
    
    vm.calendarState = [];

    // Events to be displayed
    vm.events = [];
    
    // Memo storage, shown when memosToggled is true
    vm.memoList = [];
    
    // Calendars currently associated with the user
    vm.calendarsArray = [];

    // Calendars whose events are currently shown, by id
    vm.shownCalendars = [];
    
    // Calendars currently checked on the sidebar, by id
    // Only used in conjunction with shownCalendars
    vm.checkedCalendars = [];
    
    // When false, memos are not shown
    vm.memosToggled = false;
    
    // Received notifications buffer
    vm.notifications = [];
    
    // Received invitations buffer
    vm.invitations = [];
    
    // Cell state, used by the view
    vm.cellIsOpen = true;
    
    // calendar for modal
    vm.calendarToUpd = undefined;
    
    var actions = [];
        
    // --------------------------- //
    // -- MERGE MARIO MARCO [1] -- //
    // --------------------------- //
    
    vm.vtrCell = [];
    // event for modal
    vm.temp = undefined;
    
    // clicked cell
    vm.lastCellClicked = undefined;
   
    // temp event to update
    vm.tmpMemo = undefined;
    vm.memo = undefined;
    
    // By Marco, used for message rendering purposes
    vm.notificationsView = [];
    
    vm.tempSenderId = undefined;
    
    // --------------- //
    // -- UTILITIES -- //
    // --------------- //
    
    
    // Event Constructor
    vm.Event = function (id, calendar, title, description, 
            startsAt, endsAt, primaryColor, secondaryColor,actionsType) {
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
        this.memo = false;
        this.actions = vm.getActions(actionsType);
    };
  
    // Memo Constructor (deceives vm.events into thinking it's a regular event)
    vm.Memo = function (id, title, description, color, dateAdded,actionsType) {
        var now = new Date();
        
        this.id = id;
        this.calendar = undefined; // FIXME: insert memo calendar (or whatever)
        this.title = title;
        this.description = description;
        this.startsAt = now; // graphical purposes only (renders it on the
								// current day)
        this.endsAt = now; // graphical purposes only (renders it on the
							// current day)
        this.dateAdded = now; // date this memo was added/saved on db
        this.color = {
            primary: color,
            secondary: color,
        };
        this.draggable = false;
        this.resizable = false;
        this.memo = true;
        this.actions = vm.getActions(actionsType);
    };
    
    

    // List of glyphs shown after entries in a day's event list,
    // with behavior
   
    vm.getActions = function(type){
    	
    	var result =[];
    	
    	switch (type) {
        case "REPETITION":
        	result = [ {
	                    label : '<i class=\'glyphicon glyphicon-remove\'></i>',
		                    onClick : function(args) {
		                       if (args.calendarEvent.memo) {
		                    	       vm.deleteMemoById(args.calendarEvent.id)
		                       } else {
		                           vm.deleteOccurrence(args.calendarEvent.id);
		                       }
		                    }
                		}, {
                			label : '<i class=\'glyphicon glyphicon-ban-circle\'></i>',
                			
                			 onClick : function(args) {
                				 vm.insertNewException(args.calendarEvent.repetition.id,args.calendarEvent.startsAt);
  		                    }
                			}
                	  ];
            return result;
        case "ADMIN":
        	result = [
                {
                    label : '<i class=\'glyphicon glyphicon-pencil\'></i>',
                    onClick : function(args) {
                        vm.clickUpdateEvent(args.calendarEvent);
                    }
                }, 
                {
                    label : '<i class=\'glyphicon glyphicon-remove\'></i>',
                    onClick : function(args) {
                       if (args.calendarEvent.memo) {
                    	       vm.deleteMemoById(args.calendarEvent.id)
                       } else {
                           vm.deleteOccurrence(args.calendarEvent.id);
                       }
                    }
                }, 
            ];
            return result;
            
        case "RW":
        	result = [
                {
                    label : '<i class=\'glyphicon glyphicon-pencil\'></i>',
                    onClick : function(args) {
                        vm.clickUpdateEvent(args.calendarEvent);
                    }
                }, 
                {
                    label : '<i class=\'glyphicon glyphicon-remove\'></i>',
                    onClick : function(args) {
                       if (args.calendarEvent.memo) {
                    	       vm.deleteMemoById(args.calendarEvent.id)
                       } else {
                           vm.deleteOccurrence(args.calendarEvent.id);
                       }
                    }
                }, 
            ];
            return result;
            
        case "READER":
            return result;
    	}
    
    return undefined;
    	
    }
    
    vm.getRRuleFreqType = function (type) {
        switch (type) {
       
            case "YEAR":
                return RRule.YEARLY;
            case "MONTH":
                return RRule.MONTHLY;
            case "WEEK":
                return RRule.WEEKLY;
            case "DAY":
                return RRule.DAILY;
            case "HOUR":
                return RRule.HOURLY;
        }
        
        return undefined;
    }
    
    vm.getViewDateBoundaries = function () {        
        return {
            start: moment(vm.viewDate).startOf(vm.calendarView),
            end: moment(vm.viewDate).endOf(vm.calendarView),
        };
    };
    
    // To be called before rendering received messages
    vm.arrangeMessages = function () {
        // BEWARE: shallow copies, remember in case of troubles
        var messages = vm.notifications.concat(vm.invitations);
        
        messages.sort(function (first, second) {
            var ts_first = moment(first.timestamp);
            var ts_second = moment(second.timestamp);

            if (ts_first < ts_second) {
                return -1;
            }
            if (ts_first > ts_second) {
                return 1;
            }            
            return 0;
        });
        return messages;
    };
    
    // Delete an answered invitation from table
    vm.discardInvitation = function (id) {
        vm.invitations = vm.invitations.filter(function (item) {
            return item.id != id;
        });
    };
    
    // ------------------- //
    // -- VIEW HANDLING -- //
    // ------------------- //
    
    // Repopulate vm.events accordingly to the data fetched from the DB
    vm.updateEventList = function () {
        vm.events = [];
        
        var boundaries = vm.getViewDateBoundaries();
        
        if(vm.memosToggled) {
            vm.events = vm.events.concat(vm.memoList);
            if (!vm.shownCalendars.length) { 
                $scope.$digest(); 
            }
        }
                
        vm.shownCalendars.forEach(function (calendar_id) {
        	 console.log("ENTRO ");
        	var privileges = vm.getPrivilegesByID(calendar_id);
           
            vm.JSON_getMyEventsInPeriod(calendar_id, boundaries.start.toDate(), boundaries.end.toDate(), function (events) {
                JSON.parse(events).forEach(function (blueprint) {
                    if (typeof blueprint.repetition !== "undefined") {
                        var rruleset = new RRuleSet();
                        var boundaries = vm.getViewDateBoundaries();
                        
                        var ruleStart = undefined;
                        var ruleEnd = undefined;
                        
                        if(moment(blueprint.startTime)>= boundaries.start && moment(blueprint.startTime)<=boundaries.end){
                    	      ruleStart = (moment.max(boundaries.start, moment(blueprint.startTime))).toDate();
                        	  ruleEnd = (moment.min(boundaries.end, moment(blueprint.repetition.endTime))).toDate();
                        } else {
                        	  ruleStart = moment(blueprint.startTime).toDate();
                        	  ruleEnd = moment(blueprint.repetition.endTime).toDate();
                        }
                        
                        
                        rruleset.rrule(new RRule({
                            freq: vm.getRRuleFreqType(blueprint.repetition.repetitionType),
                            dtstart: ruleStart,
                            until: ruleEnd
                        }));
                                                
                        if (typeof blueprint.repetition.exceptions !== "undefined") {
                            blueprint.repetition.exceptions.forEach(function (item) {
                                rruleset.exdate(new Date(item.startTime));
                            });
                        }
                        
                        rruleset.all().forEach(function (rule) {
                            var event = new vm.Event(
                                blueprint.id,
                                blueprint.calendar.id,
                                blueprint.title,
                                blueprint.description,
                                new Date(rule),
                                new Date(rule),
                                blueprint.primaryColor,
                                blueprint.secondaryColor,
                                "REPETITION"					// type for
																// actions
                            );
                            event.repetition = blueprint.repetition;
                            vm.events.push(event);
                        });
                        $scope.$digest();
                    } else {
                    	
	                    	 var event = new vm.Event(
	                	         blueprint.id,
	                         blueprint.calendar.id,
	                         blueprint.title,
	                         blueprint.description,
	                         new Date(blueprint.startTime),
	                         new Date(blueprint.endTime),
	                         blueprint.primaryColor,
	                         blueprint.secondaryColor,
	                         privileges 
	                     );
	                             
	                     vm.JSON_getAlarmForAnOccurrence(event.id, function (response) {
	                         if (response != "null") {
	                             var received = JSON.parse(response);                                     
	                             event.alarm = {
	                                 id: received.id,
	                                 time: received.alarm,
	                                 minutes : received.minutes
	                             }
	                         }
	                     	
	                         vm.events.push(event);
	                     	$scope.$digest();
	                     });
                    }
                });
                // Needed for asynchronous update of vm.events
                // $scope.$digest();
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
            	
           
            	// //////// GET PRIVILEGES
            	vm.JSON_getPrivileges(calendar.id, function (response) {
                    if (response != "null") {
// var received = response;
// cosnole.log(received);
                    	
                        calendar.privileges = response;
                        console.log(calendar);
                    }
            	});
 
            	
                vm.calendarsArray.push(calendar);
                var x = calendar.title;
                var title = x.replace(/'/g,"\\'");
                
                var y = calendar.description;
                var description = y.replace(/'/g,"\\'");
                
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
                        + "  </label>\n"
                        + "  <label>\n" 
                        + "    <i\n"
                        + "      class=\"glyphicon glyphicon-cog\"\n"
                        + "      ng-click=\"vm.openCalendarView('" + title + "','" + calendar.id + "','" + description + "')\"\n"
                        + "      style=\"margin-left: 80%;\">\n"
                        + "    </i>\n"
                        + "  </label>\n"
                        + "</li>\n"
                     )($scope)
                );
            });
            vm.updateCalendarListModal();   
        });
     };
    
    vm.updateMemoList = function () {
        vm.memoList = [];
        var str = '<i class="glyphicon glyphicon-tag" style=" color: #42A5F5; font-size: 20px; margin-right: 10px; "></i>';
        
        vm.JSON_getMyMemos(function (memos) {
            JSON.parse(memos).forEach(function (blueprint) {          
                vm.memoList.push(new vm.Memo(
                    blueprint.id,
                    str+blueprint.title,
                    blueprint.description,
                    blueprint.primaryColor,
                    blueprint.creationDate
                ));
            });
            vm.updateEventList();
        });
    };
    
    vm.purgeMemos = function () {
        vm.events = vm.events.filter(function (item) {
            return item.memo == false;
        });
    };
    
    // Update the list of calendars displayed within the Modal
    vm.updateCalendarListModal = function () {
        var viewList = $("#calendarsListModal");
        
        viewList.empty();
        var string =''; 
            string = "<div  class=\"btn-group\">\n"
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
            
            string+="<li><a href=\"javascript:void(0)\" onclick=\"setCalendar('"+title+"','"+id+"')\"" +
                " class = \"calendars\" data-id=\"" + id+ "\">" +vm.calendarsArray[i].title+"</a></li>";
            }
        viewList.append(string);        
    };
    
    // add notifications to dropdown menu
    vm.updateNotificationsView = function () { 
        var viewList = $("#ulNotifications");
        document.getElementById('notif').style.color = '#9E9E9E';
        
        // fill graphic vector with notifications and invitations
        vm.notificationsView = vm.arrangeMessages();
            
        if(vm.notificationsView.length){
            document.getElementById("notificationDropDown").setAttribute("data-toggle", "dropdown"); 
        } else {
            document.getElementById("notificationDropDown").setAttribute("data-toggle", "null"); 
        }
                        
        viewList.empty();
        var string =''; 

        for(i = vm.notificationsView.length-1; i>=0;i--) {
            var id = vm.notificationsView[i].id;
            var timestamp = vm.notificationsView[i].timestamp;
                
                
            if(vm.notificationsView[i].hasOwnProperty('description')) {
                // this is a simple notification
            	var description = vm.notificationsView[i].description;
            	var x = description;
                description = x.replace(/'/g,"\\'");
            	
                string+="<li><a href=\"javascript:void(0)\" class = \"calendars\" data-id=\"" + id+ "\">" +description+"</a></li>";
            } else {    
            	
            	var calendarName = vm.notificationsView[i].calendar.title;
            	var text = "you have been invited to calendar : "+calendarName;
                // this is a invitation TODO
            	string+="<li><a href=\"#\" ng-click=\"vm.openAnswerModal('"+id+"')\" class = \"calendars\" data-id=\"" + id+ "\">" +text+"</a></li>";
            }															
        }
            
        viewList.append( $compile(string)($scope));        
        vm.deleteNotifications();
      };

    
    vm.insertNewCalendarView = function () {
    	
    	
       var title = document.getElementById("nameCal").value;
       var description = document.getElementById("descrCalendar").value;
              
       vm.insertNewCalendar(title, description);
       document.getElementById('modal-wrapper1').style.display = 'none';
       document.getElementById("nameCal").value = '';
       document.getElementById("descrCalendar").value = '';
    };

    
    vm.openCalendarView = function (title,id,description) {
        // TO DO open modal update Calendar
    	document.getElementById("calendarNm").innerHTML = title;
    	
    	vm.calendarToUpd = {
    		id : id,
    		title: title,
    		description : description
    	}
    	
    	modal(4);
    };
    
    
    vm.shareCalendarView = function(){
    	
    	var privilages = document.getElementById("privilages").value;    	
    	
    	var email = document.getElementById('userChoice').value
    	
    	
    	if(privilages == 'none' ||  email==''){
    		alert("Please insert correct value");
    	}
    	else{
    		
    		if(vm.getPrivilegesByID(vm.calendarToUpd.id) != "ADMIN"){
    			alert("You cannot share this calendar \n" +
    					"missing required privilege (Admin)");
    		}
    		else{
	    		vm.sendInvitation(vm.calendarToUpd.id,email,privilages);
	    		document.getElementById('modal-wrapper4').style.display = 'none';
	         	resetShareCalendarValue();
    		}
         	
    	}
     }
    
    
    vm.updateCalendarView = function(title,description){
    	     	
    	vm.updateCalendar(vm.calendarToUpd.id, vm.calendarToUpd.title, vm.calendarToUpd.description);
    	
    	document.getElementById('modal-wrapper4').style.display = 'none';
    }
    
  	vm.deleteCalendarView = function(){
    	
    	vm.disconnectFromCalendar(vm.calendarToUpd.id);
    }
    
    
  	vm.updateUserInformation = function() {
        var name = document.getElementById("nameUser").value;
        var oldP = document.getElementById("oldP").value;
        var newP = document.getElementById("newP").value;
    
        vm.updateUser(name,oldP,newP);
        document.getElementById('modal-wrapper2').style.display = 'none';
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
    
    // Hide/Show memos
    vm.toggleMemos = function () {
        if (vm.memosToggled) {
            vm.purgeMemos();
            document.getElementById('buttonShowMemo').innerHTML = 'Show Memo'; 
            document.getElementById('buttonShowMemo').style.backgroundColor = "#337ab7";
        } 
        else 
        {
            document.getElementById('buttonShowMemo').innerHTML = 'Hide Memo';            
            document.getElementById('buttonShowMemo').style.backgroundColor = "#039BE5";
            vm.updateMemoList();
            
        }
        
        vm.memosToggled = !vm.memosToggled;
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
	 * JSON_getMyMemos
	 */
    vm.JSON_getMyMemos = function (callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getMyMemos",
            success: function (response) {
                callback(response);
            },
        });
    };
    
    /*
	 * JSON_getMyAlarms TODO: test me
	 */
    vm.JSON_getMyAlarms = function (callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getMyAlarms",
            success: function (response) {
                callback(response);
            },
        });  
    };
    
    /*
	 * JSON_getAlarmForAnOccurrence TODO: test me
	 */
    vm.JSON_getAlarmForAnOccurrence = function (occurrence_id, callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getAlarmForAnOccurrence/" + occurrence_id,
            success: function (response) {
                callback(response);
            },
        });
    };
    
    /*
	 * JSON_getMyNotifications TODO: test me
	 */
    vm.JSON_getMyNotifications = function (callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getMyNotifications",
            success: function (response) {
                callback(response);
            },
        });
    };
    
    
    /*
	 * JSON_getPrivileges
	 */
   
    vm.JSON_getPrivileges = function (calendar_id,callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getPrivileges/" + calendar_id,
            success: function (response) {
                // console.log("PRIVILEGGI PER CALENDARIO "+calendar_id);
                console.log(response);
                console.log("*********************");
                
                callback(response);
            },
        });
    };
    
    
    /*
	 * JSON_getMyInvitations
	 */
    vm.JSON_getMyInvitations = function (callback) {
        $.ajax({
            type: "POST",
            url: "JSON_getMyInvitations",
            success: function (response) {
                callback(response);
            },
        });
    };
    
    /*
	 * insertNewEvent
	 */
    vm.insertNewEvent = function (calendar_id, title, description, 
            startsAt, endsAt, primaryColor, secondaryColor, callback) {
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
                    if (typeof callback !== "undefined") {
                        callback(response);
                    } else {
                        vm.updateEventList();
                    }
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
	 * insertNewRepetition TODO aggiungere intervallo
	 */
    vm.insertNewRepetition = function (occurrence_id, repetitionType,
            startTime, endTime) {
        $.ajax({
            type: "POST",
            url: "insertNewRepetition/" + occurrence_id,
            data: {
                rType: repetitionType,
                sT: startTime,
                eT: endTime,
            },
            success: function (response) {
                if (response != -1) { 
                    vm.updateEventList();
                }
            },
        });
    };
    
    /*
	 * updateRepetition
	 */
    vm.updateRepetition = function (repetition_id, repetitionType,
            startTime, endTime) {
        $.ajax({
            type: "POST",
            url: "updateRepetition/" + repetition_id,
            data: {
                rType: repetitionType,
                sT: startTime,
                eT: endTime,
            },
            success: function (response) {
                if (response == "YES") {
                    vm.updateEventList();
                }
            },
        });
    };
    
    /*
	 * deleteRepetition
	 */
    vm.deleteRepetition = function (repetition_id) {
        $.ajax({
            type: "POST",
            url: "deleteRepetition" + repetition_id,
            success: function (response) {
                if (response == "YES") {
                    vm.updateEventList();
                }
            },
        });
    };
    
    /*
	 * insertNewException
	 */
    vm.insertNewException = function (repetition_id, date_exception) {
        $.ajax({
            type: "POST",
            url: "insertNewException/" + repetition_id,
            data:{
            	date_exception : date_exception,
            },
            success: function (response) {
                if (response != -1) {
                    vm.updateEventList();
                }
            },
        });
    };
    
    /*
	 * insertNewMemo
	 */
    vm.insertNewMemo = function (title, description, date, color) {
        $.ajax({
            type: "POST",
            url: "insertNewMemo",
            data: {
                title: title,
                description: description,
                data: date,
                c1: color,
            },
            success: function (response) {
                vm.updateMemoList();
            },
        });
    };
    
    /*
	 * updateMemo
	 */
    vm.updateMemo = function (memo_id, title, description, date, color) {
        $.ajax({
            type: "POST",
            url: "updateMemo/" + memo_id,
            data: {
                title: title,
                description: description,
                data: date,
                c1: color,
            },
            success: function (response) {
                vm.updateMemoList();
            },
        });
    };
    
    /*
	 * deleteMemoById
	 */
    vm.deleteMemoById = function (memo_id) {
        $.ajax({
            type: "POST",
            url: "deleteMemo/" + memo_id,
            success: function (response) {
                vm.updateMemoList();
            },
        });
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
	 * addAlarm
	 */
    vm.addAlarm = function (occurrence_id, minutes) {
        $.ajax({
            type: "POST",
            url: "addAlarm/" + occurrence_id,
            data: {
                minutes: minutes,
            },
            success: function (response) {
                vm.updateEventList();
            },
        });
    };
    
    /*
	 * updateAlarm
	 */
    vm.updateAlarm = function (alarm_id,minutes) {
        $.ajax({
            type: "POST",
            url: "updateAlarm/" + alarm_id,
            data : {
            	minutes:minutes
            },
            success: function (response) {
                // TODO
            	if(response == "YES")
            		console.log("alarm aggiornato correttamente");
            	else
            		console.log("alarm non aggiornato corretamente");
            },
        });
    };
    
    /*
	 * deleteAlarm FIXME: al momento Alarm nel modello ha una lista di allarmi
	 */
    vm.deleteAlarm = function (alarm_id) {
        $.ajax({
            type: "POST",
            url: "deleteAlarm/" + alarm_id,
            success: function (response) {
                // TODO
            },
        });
    };
    
    /*
	 * updateUser
	 */
    vm.updateUser = function (username, oldPassword , password) {
        $.ajax({
            type: "POST",
            url: "updateUser",
            data: {
                username: username,
                oldPassword : oldPassword,
                password: password,
            },
            success: function (response) {
            		if(response == "User update successfully" || response == "Username changed successfully")
                        document.getElementById('usernameHome').innerHTML = username;
            		
            	
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
                    if (response == "YES") {
                        // TODO
                    	console.log(response);
                    }else{
                    	
                    	console.log("else ===> "+response);
                    }
                    
                },
            });
        };
    
    /*
	 * deleteNotifications
	 */
    vm.deleteNotifications = function () {
        $.ajax({
            type: "POST",
            url: "deleteNotifications",
            success: function (response) {
                if (response == "YES") {
                    vm.notifications = [];
                }
            },
        });
    };
    
    /*
	 * answerInvitation MARCO
	 */
    vm.answerInvitation = function (invitation_id, answer) {
        $.ajax({
            type: "POST",
            url: "answerInvitation/" + invitation_id,
            data: {
                answer: answer ? "accept" : "decline",
            },
            success: function (response) {
                if (response == "accepted") {
                    vm.discardInvitation(invitation_id);
                    vm.updateCalendarList();
                } else if (response != "declined") {
                    console.log("vm.answerInvitation unsuccessful");
                    console.log(JSON.stringify(response, null, 4));
                }
            },
        });
    };
    
    
    /*
	 * resetSentStateOnMessages
	 */
    vm.resetSentStateOnMessages = function () {
        $.ajax({
            type: "POST",
            url: "resetSentStateOnMessages",
        });
    };
    
    // --------- //
    // -- SSE -- //
    // --------- //
    
    vm.SSENotificationSubscription = function () {
        var eventSource = new EventSource("notifications");
        
        eventSource.onmessage = function (event) {
            var received = JSON.parse(event.data);
            
            if (received.length) {
                vm.notifications = vm.notifications.concat(received.slice());
                document.getElementById('notif').style.color = '#F44336';
            }
        };
    };
    
    vm.SSEInvitationSubscription = function () {
        var eventSource = new EventSource("invitations");
        
        eventSource.onmessage = function (event) {
            var received = JSON.parse(event.data);
            
            if (received.length) {
            	
            	
            	console.log(JSON.stringify(received));
                vm.invitations = vm.invitations.concat(received.slice());
                document.getElementById('notif').style.color = '#F44336';
            }
        };
    };
    
    vm.SSEAlarmSubscription = function () {
        var eventSource = new EventSource("alarms");
        
        eventSource.onmessage = function (event) {
            JSON.parse(event.data).forEach(function (item) {
                alert(
                    "Event \"" + item.occurrence.title + "\" starting at " +
                    item.occurrence.startTime + "!\n"
                );
            });
        };
    };
    
    // test me extensively one of these days
    vm.SSECalendarStateChangeSubscription = function () {
        var eventSource = new EventSource("calendarStateChange");
        
        eventSource.onmessage = function (event) {
            var received = JSON.parse(event.data);
            for (var calendar_id in received) {
                if (received.hasOwnProperty(calendar_id)) {
                    var filtered = vm.calendarState.filter(function (e) { return e.id === calendar_id; });
                    if (!filtered.length) {
                        var newEntry = {
                            id: calendar_id,
                            version: received[calendar_id],
                        };
                        vm.calendarState.push(newEntry);
                    } else {
                        if (new Date(received[calendar_id]) > new Date(filtered[0].version)) {
                            filtered[0].version = received[calendar_id];
                            vm.updateEventList();
                        }
                    }
                }
            }
        };
    };
    
    // ---------- //
    // -- INIT -- //
    // ---------- //
    
    (function () {
        vm.updateCalendarList();
        vm.resetSentStateOnMessages();
        vm.SSENotificationSubscription();
        vm.SSEInvitationSubscription();
        vm.SSEAlarmSubscription();
        vm.SSECalendarStateChangeSubscription();
    })();
    
    // --------------------------- //
    // -- MERGE MARIO MARCO [2] -- //
    // --------------------------- //

    vm.clickUpdateEvent = function(event) {
        if (!event.memo) {
        	
        	var alarmID = undefined;
        	var minutes = undefined;
        	if(event.hasOwnProperty('alarm')){
        		alarmID = event.alarm.id;
        		minutes = event.alarm.minutes;
        	}
        	else{
        		alarmID = undefined;
        		minutes = undefined;		
        	}
        	
        	
            vm.temp = {
                title : event.title,
                id : event.id,
                memo : false,
                description : event.description,
                minutes : minutes, // TODO: fix clock on DB
                startsAt : event.startsAt,
                endsAt : event.endsAt,
                color : {
                    primary : event.color.primary,
                    secondary : event.color.secondary,
                },
                draggable : false,
                resizable : false,
                alarmID : alarmID,
                actions : vm.getActions("ADMIN")
            };

            updateClock(vm.temp.minutes);
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
                actions : vm.getActions("ADMIN")
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
            minutes : 'none',
            startsAt : startDate,
            endsAt : endDate,
            color : {
                primary : "#123456",
                secondary : "#123458"
            },
            draggable : false,
            resizable : false,
            actions : vm.getActions("ADMIN"),
            // ///////////////// REPETITON DATA ******************
            freq: "none",
            dtstart: startDate,
            until: endDate
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
            minutes : 'none',
            startsAt : vm.firstDateClicked,
            endsAt : vm.lastDateClicked,
            color : {
                primary : "#123456",
                secondary : "#123456",
            },
            draggable : false,
            resizable : false,
            actions : vm.getActions("ADMIN"),
            // ///////////////// REPETITON DATA ******************
            freq: "none",
            dtstart: vm.firstDateClicked,
            until: vm.lastDateClicked
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
    };

    // MANAGE EVENT
    vm.openEventModal = function() {
        
        modal(5);
    };
 


    // add event press button action
    vm.addEventView = function() {

    
    	var idCalendar = document.getElementById("choiceId").value;

 
        vm.temp.minutes = document.getElementById("TourId").value;

          
      
       

        if (idCalendar != undefined) {
        	
        	
        	if(vm.getPrivilegesByID(idCalendar) =="R"){
        		alert("this calendar is shared with you only as reader\n you cannot add new events");
        	}else{
        	
			        	  // for repetition events
			            if(document.getElementById("repetition").checked==true){
			            	
			            	 vm.temp.endsAt = vm.temp.startsAt; 
			            	 // value of repetition
			                 vm.temp.freq = document.getElementById("repChoice").value;
			                 
			
			             	if(vm.temp.freq != "none"){
			                 
					                 vm.insertNewEvent(idCalendar, vm.temp.title, vm.temp.description, vm.temp.startsAt, vm.temp.endsAt, vm.temp.color.primary,
					                         vm.temp.color.secondary, function (response){
					
					     				 resetFreqChoice();
				     				 
				     				 
				     				 document.getElementById("repetition").checked = false;
				                 	
				                 	vm.insertNewRepetition(response, vm.temp.freq, vm.temp.dtstart, vm.temp.until);
				                 });
				                 
				                 
				                 document.getElementById('btn-add').disabled = true;
				               	 
				                 
				                 document.getElementById('modal-wrapper5').style.display = 'none';
				                 resetClock(); 			
				      
		                 } else{
		                	 alert("please insert a valid frequence");
		                 }
		             }else{		
		            	 
		            	 
		            	 if(vm.temp.minutes != "none"){// alarm setted
		
		            		 
		            		 // event without repetition with ALARM
		                	 vm.insertNewEvent(idCalendar, vm.temp.title, vm.temp.description, vm.temp.startsAt, vm.temp.endsAt, vm.temp.color.primary,
		                             vm.temp.color.secondary,function(response){
		                		 vm.addAlarm(response, vm.temp.minutes);
		                	 });
		                	 
		                	 resetClock(); 
		            		 
		            	 }else{
		            	 
		            	 // event without repetition without ALARM
		            	 vm.insertNewEvent(idCalendar, vm.temp.title, vm.temp.description, vm.temp.startsAt, vm.temp.endsAt, vm.temp.color.primary,
		                         vm.temp.color.secondary);
		                 
		                
		                	
		            	 }
		            	 
		            	 document.getElementById('btn-add').disabled = true;
		               	 
		                 
		                 document.getElementById('modal-wrapper5').style.display = 'none';
		             }
        	}
  
        } else{ 
        	   alert("please choose a calendar");
        }
  
    	}

 // update event press button action
    vm.updateEventView = function() {

        var idCalendar = document.getElementById("choiceId").value;
        vm.temp.minutes = document.getElementById("TourId2").value;
        
        vm.updateEvent(vm.temp.id, vm.temp.title,
                vm.temp.description, vm.temp.startsAt,
                vm.temp.endsAt, vm.temp.color.primary,
                vm.temp.color.secondary);
        
        
        if(vm.temp.minutes != 'none'){
        		if(vm.temp.alarmID != undefined){
        				vm.updateAlarm(vm.temp.alarmID,vm.temp.minutes);
        			}
        			else{
        				vm.addAlarm(vm.temp.id, vm.temp.minutes);
        				}
        		
        }
        else{ // delete only if this is an event with alarm associated
        	
        	if(vm.temp.alarmID != undefined )
        		vm.deleteAlarm(vm.temp.alarmID);
        	
        }
        
        
        resetClock();
        document.getElementById('modal-wrapper6').style.display = 'none';

    }

    // MANAGE MEMO
    vm.openMemoModal = function() {
        modal(7);

        vm.memo = {
            title : 'New Memo',
            startsAt : moment(),
            color : {
                primary : "#123456",
            },
            description : '',
            draggable : false,
            resizable : false,
            memo : true,
            actions : vm.getActions("ADMIN")
        };

    };

    // add memo press button action
    vm.addMemoView = function() {

        // use tmp.memo variables
    	
    	var now = new Date();
    	
        vm.insertNewMemo(vm.memo.title, vm.memo.description, now , vm.memo.color.primary);
        
        document.getElementById('modal-wrapper7').style.display = 'none';

    }

    // update memo press button action
    vm.updateMemoView = function() {
       	// use tmp.memo variables
    	
    	var now = new Date();
 
        vm.updateMemo(vm.memo.id, vm.memo.title, vm.memo.description, now, vm.memo.color.primary);
        
        document.getElementById('modal-wrapper8').style.display = 'none';
    }
    
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
        
  // ------------------------------ //
  // -- PROVA PER RICERCA UTENTI -- //
  // -------------------------------//
    
  $scope.onKeyUP = function ($event) {
  	vm.manageInputString();
   };
     
  vm.manageInputString = function(){
  	
  	var strCurrent = document.getElementById('userChoice').value; 
    	
  vm.searchEmailInDb(strCurrent, function (x) {
  		var resp = JSON.parse(x);
  		vm.populateListOfUsername(resp);
 	});
  };


  vm.populateListOfUsername = function (resp){
  	    	
  	 var viewList = $("#userListModal");
       var contRespUser = 0;
            
       viewList.empty();
       var string =''; 
           string = "<div  class=\"btn-group\" >\n"
           +"<button type=\"button\" class=\"btn btn-primary dropdown-toggle\"" 
           +  "data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"true\" style=\"margin-top:3px;\">"
           +"<i style=\"font-size: 25px; color: white;\"" 
           +  "class=\"glyphicon glyphicon-search\"></i> <span class=\"caret\"></span>"
           +"</button>"
           +"<ul class=\"dropdown-menu\">";
       
      
           resp.forEach(function(element) {
          	if(contRespUser<=10    ){ 
   			string+="<li><a href=\"javascript:void(0)\" onclick=\"setUser('"+element+"')\"" +
              " class = \"calendars\" data-id=\" \">" +element+"</a></li>";                       
          	}
          	contRespUser++;
           	});

        viewList.append(string);  

  }

  vm.searchEmailInDb = function (email,callback) {
      $.ajax({
          type: "POST",
          url: "JSON_searchEmailInDb",
          data: {
          	emailToSearch:email,
          },
          success: function (response) {
          	callback(response);
          },
      });
  };
         
  
  vm.openAnswerModal = function (id){
	  
	      vm.tempSenderId = id;
		  modal(9);

	};
	
	vm.acceptInvitation = function(){
		vm.answerInvitation(vm.tempSenderId, true);
		document.getElementById('modal-wrapper9').style.display = 'none';
	}
	
	vm.refuseInvitation = function(){
		
		vm.answerInvitation(vm.tempSenderId, false);
		document.getElementById('modal-wrapper9').style.display = 'none';
	}
  
	
	vm.getPrivilegesByID = function(id_calendar){
    	 
		        for(i = 0 ;i< vm.calendarsArray.length;i++){
		        	if(vm.calendarsArray[i].id == id_calendar) 
		        		return vm.calendarsArray[i].privileges;
		        	
		        }
	 
	};

 
});


// =======
// // ----------- //
// // -- DEBUG -- //
// // ----------- //
//  
// vm.fn = function () {
// var time = new Date(); console.log(time);
// var delay = 10;
// time.setMinutes(time.getMinutes() + 1)
// time.setSeconds(time.getSeconds() + delay);
//      
// console.log(time);
//      
// vm.insertNewEvent(
// "2",
// delay + " seconds",
// delay + " seconds before alarm triggers",
// time, time,
// "#555555", "#aaaaaa", function (response) {
// if (response != -1) {
// vm.addAlarm(response, 1);
// }
// });
// };
// });
// >>>>>>> refs/remotes/origin/_giuseppe_
