var edb = {};
var imported = document.createElement('script');
imported.src = 'resources/scripts/openModal.js';
document.head.appendChild(imported);

angular.module('mwl.calendar.docs', [ 'mwl.calendar', 'ngAnimate',
		'ui.bootstrap', 'colorpicker.module' ]);
angular
		.module('mwl.calendar.docs')
		.controller(
				'KitchenSinkCtrl',
				function(moment, calendarConfig) {
					var vm = this;
					vm.calendarView = 'month';
					vm.viewDate = new Date();

					var actions = [ {
						label : '<i class=\'glyphicon glyphicon-pencil\'></i>',
						onClick : function(args) {
							vm.clickUpdateEvent(args.calendarEvent);
						}
					}, {
						label : '<i class=\'glyphicon glyphicon-remove\'></i>',
						onClick : function(args) {
							vm.deleteEvent(args.calendarEvent);
						}
					}, ];

					vm.events = [];

					vm.vtrCell = [];
					// event for modal
					vm.temp = undefined;
					// clicked cell
					vm.lastCellClicked = undefined;
					vm.contId = 0;
					// temp event to update
					vm.tmpEvt = undefined;
					vm.tmpMemo = undefined;
					
					vm.memo= undefined;
					
					 vm.clickUpdateEvent = function(event) {
						if(!event.memo){ 
						vm.temp = {
							title : event.title,
							id : event.id,
							memo : false,
							descr : event.descr,
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
						modal(6);
					 }
					else{
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
						
					
					vm.deleteEvent = function(event) {
						vm.events = vm.events.filter(function(item) {
							return item.id != event.id;
						});

						edb[event.calendar].events = edb[event.calendar].events
								.filter(function(item) {
									return item.id != event.id;
								});
					};

					vm.shownCalendars = [];
					vm.checkedCalendars = [];

					vm.showCalendar = function(id) {
						if (edb.hasOwnProperty(id)
								&& !vm.shownCalendars.includes(id)) {
							for (var i = 0; i < edb[id]["events"].length; i++) {
								var event;
								event = edb[id]["events"][i];
								event.calendar = id;
								event.startsAt = new Date(event.startsAt);
								event.endsAt = new Date(event.endsAt);
								event.actions = actions;
								event.draggable = false;
								event.resizable = false;
								vm.events.push(event);
							}
							vm.shownCalendars.push(id);
						}
					};

					vm.hideCalendar = function(id) {
						if (vm.shownCalendars.includes(id)) {
							var i = vm.events.length;
							while (i--) {
								if (vm.events[i].calendar == id) {
									vm.events.splice(i, 1);
								}
							}

							var index = vm.shownCalendars.indexOf(id);
							if (index !== -1) {
								vm.shownCalendars.splice(index, 1);
							}
						}
					};

					vm.toggleCalendar = function(id) {
						if (vm.checkedCalendars[id]) {
							vm.showCalendar(id);
						} else {
							vm.hideCalendar(id);
						}
					};

					// --------
					// - TEST -
					// --------

					vm.deleteCalendar = function() {
						var id = "2";

						$.ajax({
							type : "POST",
							url : "delete/" + id,
							success : function(result) {
								console.log("vm.deleteCalendar: " + result);
							},
							error : function(result) {
								console.log("ERROR ERROR ERROR ERROR ERROR");
							},
						});
					};

					vm.cellIsOpen = true;

					vm.eventClicked = function(event) {

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

						//var value = document.getElementById("descEvent").value;
						//vm.temp.descr = value;
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
						//alert(vm.temp.id);
										
						document.getElementById('modal-wrapper5').style.display = 'none';
     		}
			
			vm.updateEvents = function() {
						var index = vm.events.indexOf(vm.tmpEvt);
						if(index > -1){
						       vm.events.splice(index,1);	
						}
						
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
						
						vm.events.push(vm.tmpEvt);
						document.getElementById('modal-wrapper6').style.display = 'none';

			}
					
					
					
			// MANAGE MEMO		
			vm.openMemoModal = function() {
			modal(7);
						
			vm.memo = {
					    title: 'New Memo',
				        id : vm.contId,
			            startsAt: moment(),
			            color: {
					           	primary: "#123456",
					           },
					    draggable: false,
					    resizable: false,
					    memo : true,
					    actions: actions
			   		};
					
			};
			
		  vm.addMemo = function() {
			  
			    vm.memo.title = '<i class="glyphicon glyphicon-tag" style=" color: #42A5F5; font-size: 20px; margin-right: 10px; "></i>'+vm.memo.title;
	    		vm.events.push(vm.memo);
	    		vm.contId++;
	    		document.getElementById('modal-wrapper7').style.display='none';
				
			}

		  
		vm.updateMemo = function() {
				var index = vm.events.indexOf(vm.tmpMemo);
				if(index > -1){
				       vm.events.splice(index,1);	
				}
				
				vm.memo.title = '<i class="glyphicon glyphicon-tag" style=" color: #42A5F5; font-size: 20px; margin-right: 10px; "></i>'+vm.memo.title;
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
				
				vm.events.push(vm.tmpMemo);
				document.getElementById('modal-wrapper8').style.display = 'none';

	}

		  
		  
});

hoverCell = function(date, date1, cell) {
	if (cell.date >= date && cell.date <= date1) {
		cell.cssClass = 'clicked-cell';
	}
};
