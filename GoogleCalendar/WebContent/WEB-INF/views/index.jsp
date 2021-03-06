<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<html data-ng-app="mwl.calendar.docs">
<head>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<script type="text/javascript" src="resources/opensource/angular.min.js"></script>
<script src="https://unpkg.com/moment@2.17.1"></script>
<script src="https://unpkg.com/interactjs@1"></script>
<script src="https://unpkg.com/angular@1.6.6/angular.js"></script>
<script src="https://unpkg.com/angular-animate@1.6.6/angular-animate.js"></script>
<script
	src="https://unpkg.com/angular-ui-bootstrap@2/dist/ui-bootstrap-tpls.js"></script>
<script src="https://unpkg.com/rrule@2"></script>
<script src="https://unpkg.com/angular-bootstrap-colorpicker@3"></script>
<script src="https://unpkg.com/angular-bootstrap-calendar"></script>



<link href="https://unpkg.com/bootstrap@3/dist/css/bootstrap.css"
	rel="stylesheet">
<link
	href="https://unpkg.com/angular-bootstrap-colorpicker@3/css/colorpicker.min.css"
	rel="stylesheet">
<link
	href="https://unpkg.com/angular-bootstrap-calendar/dist/css/angular-bootstrap-calendar.min.css"
	rel="stylesheet">


<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/css/bootstrap-select.min.css">

<script src="resources/scripts/example.js"></script>
<script src="resources/scripts/openModal.js"></script>
<script src="resources/scripts/dropDownMenu.js"></script>



<style type="text/css">
.clicked-cell {
	background-color: #d9edf7 !important;
}
</style>

<style type="text/css">
.clear-cell {
	background-color: white !important;
}
</style>

<!-- Bootstrap CSS CDN -->

<link rel="stylesheet" type="text/css"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="resources/css/sidebar.css">
<link rel="stylesheet" type="text/css" href="resources/css/popup.css">





<link
	href="https://cdn.rawgit.com/harvesthq/chosen/gh-pages/chosen.min.css"
	rel="stylesheet" />
</head>
<body data-ng-controller="KitchenSinkCtrl as vm">
	<div class="wrapper">
		<!-- Sidebar Holder -->
		<nav id="sidebar">
			<div class="sidebar-header">
				<h3>ASDE Calendar</h3>
			</div>

			<div class="col-sm-2" id="myCalendarsBox"></div>
			<ul class="list-unstyled components">
				<li class="active"><a href="#homeSubmenu"
					data-toggle="collapse" aria-expanded="false"> <i
						class="glyphicon glyphicon-calendar"></i> My Calendar
				</a>
					<ul class="collapse list-unstyled" id="homeSubmenu">
						<form action="">
							<fieldset id="calendarsList"></fieldset>
						</form>
					</ul></li>

				<li><a onclick="modal('1')"> <i
						class="glyphicon glyphicon-plus"></i> Add New Calendar
				</a></li>

				<li><a onclick="openModalType2()"> <i
						class="glyphicon glyphicon-user"></i> Update profile
				</a></li>
				<li><a onclick="modal('3')"> <i
						class="glyphicon glyphicon-briefcase"></i> About us
				</a></li>
				<li></li>
			</ul>

		</nav>

		<!-- Page Content Holder -->
		<div id="content" style="width: 100%;">

			<nav class="navbar navbar-default">
				<div class="container-fluid">

					<div class="navbar-header"></div>

					<div class="navbar-header">
						<button type="button" id="sidebarCollapse" class="navbar-btn">
							<span></span> <span></span> <span></span>
						</button>
					</div>

					<div class="collapse navbar-collapse"
						id="bs-example-navbar-collapse-1">
						<ul class="nav navbar-nav navbar-right">
							<li><a id="usernameHome" href="#"
								style="color: #337ab7; font-size: 20px;">${username}</a></li>
							<li><a id="notificationDropDown" href="#"
								ng-click="vm.updateNotificationsView()" data-toggle="null"
								aria-haspopup="true"> <i id="notif"
									class="glyphicon glyphicon-bell"
									style="font-size: 20px; color: '#9E9E9E';"></i> <span
									class="caret"></span>
							</a>
								<div class="dropdown-menu scrollable-menu" id="ulNotifications"></div></li>
							<li>
								<form action="logout">
									<div class="navbar-header">
										<button type="submit" id="logout-btn"
											class="btn btn-info navbar-btn" style="background: #337ab7;">
											<i class="glyphicon glyphicon-log-out"></i>
										</button>
									</div>
								</form>
							</li>
							<!-- <li><a href="#">Page</a></li>-->
						</ul>
					</div>
				</div>
			</nav>

			<!-- CALENDAR -->
			<div class="container col-sm-12">
				<div class="row">
					<div>
						<div>
							<h3 class="text-center">{{ vm.calendarTitle }}</h3>

							<div class="row">
								<div class="col-md-4 text-center">
									<div class="btn-group">

										<button class="btn btn-primary" mwl-date-modifier
											date="vm.viewDate" decrement="vm.calendarView"
											ng-click="vm.viewModifierBehavior()">Previous</button>
										<button class="btn btn-default" mwl-date-modifier
											date="vm.viewDate" set-to-today
											ng-click="vm.viewModifierBehavior()">Today</button>
										<button class="btn btn-primary" mwl-date-modifier
											date="vm.viewDate" increment="vm.calendarView"
											ng-click="vm.viewModifierBehavior()">Next</button>
 										<!-- 
 										DEBUG BUTTON
 										<button class="btn btn-primary" ng-click="vm.fn()">DEBUG</button>
									  -->
 
									</div>
								</div>
								<br class="visible-xs visible-sm">
								<div class="col-md-4 text-center">
									<div class="btn-group">
										<label class="btn btn-primary" ng-model="vm.calendarView"
											uib-btn-radio="'year'" ng-click="vm.viewModifierBehavior()">Year</label>
										<label class="btn btn-primary" ng-model="vm.calendarView"
											uib-btn-radio="'month'" ng-click="vm.viewModifierBehavior()">Month</label>
										<label class="btn btn-primary" ng-model="vm.calendarView"
											uib-btn-radio="'week'" ng-click="vm.viewModifierBehavior()">Week</label>
										<label class="btn btn-primary" ng-model="vm.calendarView"
											uib-btn-radio="'day'" ng-click="vm.viewModifierBehavior()">Day</label>
									</div>
								</div>
								<br class="visible-xs visible-sm">
								<div class="col-md-4 text-center">
									<div class="btn-group">


										<button class="btn btn-primary pull-right" id="buttonShowMemo"
											ng-click="vm.toggleMemos()" style="margin-left: 3px;">Show
											Memo</button>
										<!-- <i style="font-size: 16px; color: white;"class="glyphicon glyphicon-save-file"></i> Add event -->
										<button id="btn-add" class="btn btn-primary pull-right"
											ng-click="vm.openEventModal()" disabled="disabled">
											<i style="font-size: 16px; color: white;"
												class="glyphicon glyphicon-save-file"></i> Add event
										</button>
										<button class="btn btn-primary pull-right"
											ng-click="vm.openMemoModal()">
											<i style="font-size: 16px; color: white;"
												class="glyphicon glyphicon-pushpin"></i> Add Memo
										</button>
									</div>
								</div>

							</div>

							<br>

							<!-- <div class="alert alert-info">
     			Select range on a day on the view.
    			 <strong ng-show="vm.lastDateClicked" >You selected on this day: from {{ vm.firstDateClicked | date:'medium' }} to {{ vm.lastDateClicked | date:'medium' }}</strong>
 			</div> -->
							<mwl-calendar events="vm.events" view="vm.calendarView"
								view-title="vm.calendarTitle" view-date="vm.viewDate"
								day-view-start="00:00" day-view-end="24:00"
								on-event-click="vm.eventClicked(calendarEvent)"
								on-event-times-changed="vm.eventTimesChanged(calendarEvent); calendarEvent.startsAt = calendarNewEventStart; calendarEvent.endsAt = calendarNewEventEnd"
								cell-is-open="vm.cellIsOpen" day-view-split="30"
								cell-auto-open-disabled="true"
								on-timespan-click="vm.timespanClicked(calendarDate, calendarCell)"
								on-date-range-select="vm.rangeSelected(calendarRangeStartDate, calendarRangeEndDate)">
							</mwl-calendar>

						</div>
					</div>
				</div>
			</div>

			<!--  <button
            class="btn btn-danger"
            ng-click="vm.events.splice($index, 1)">
            Delete
          </button -->

		</div>
	</div>


	<!-- MODAL PAGE  -->
	<!-- ADD NEW CALENDAR-->
	<div id="modal-wrapper1" class="modal">
		<div>
			<form class="modal-content animate" action="/action_page.php">
				<div class="imgcontainer">
					<span
						onclick="document.getElementById('modal-wrapper1').style.display='none'"
						class="close" title="Close PopUp">&times;</span>
					<h1 style="text-align: center; color: white; padding-top: 10px;">New
						calendar</h1>
					<a href="#" class="avatar"><i
						style="font-size: 45px; color: white;"
						class="glyphicon glyphicon-calendar"></i></a>
				</div>

				<div class="modal-content">
					<input type="text" placeholder="Name" name="uname" id="nameCal">

					<textarea rows="4" placeholder="DescrCalendar" id="descrCalendar"></textarea>
					<div style="text-align: center;">
						<button type="button" id="newCalendar"
							class="btn btn-info navbar-btn"
							ng-click="vm.insertNewCalendarView()" style="background: #42A5F5">
							<span><strong>Create New Calendar</strong></span>
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<!-- UPDATE PROFILE-->
	<div id="modal-wrapper2" class="modal">
		<div>
			<form class="modal-content animate" action="/action_page.php">
				<div class="imgcontainer">
					<span
						onclick="document.getElementById('modal-wrapper2').style.display='none'"
						class="close" title="Close PopUp">&times;</span>
					<h1 style="text-align: center; color: white; padding-top: 10px;">Update
						profile</h1>
					<a href="#" class="avatar"><i
						style="font-size: 45px; color: white;"
						class="glyphicon glyphicon-calendar"></i></a>
				</div>

				<div class="modal-content">
					<input type="text" placeholder="Name" name="uname" id="nameUser">
					<input type="password" placeholder="Old Password" id="oldP">
					<input type="password" placeholder="New Password" id="newP">
					<div style="text-align: center;">
						<button type="button" id="newCalendar1"
							class="btn btn-info navbar-btn"
							ng-click="vm.updateUserInformation()"
							style="background: #42A5F5;">
							<span><strong>Update Your Profile</strong></span>
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>


	<!-- ABOUT US-->
	<div id="modal-wrapper3" class="modal">
		<div>
			<form class="modal-content animate" action="/action_page.php">
				<div class="imgcontainer">
					<span
						onclick="document.getElementById('modal-wrapper3').style.display='none'"
						class="close" title="Close PopUp">&times;</span>
					<h1 style="text-align: center; color: white; padding-top: 10px;">About
						us</h1>
					<a href="#" class="avatar"><i
						style="font-size: 45px; color: white;"
						class="glyphicon glyphicon-calendar"></i></a>
				</div>

				<div class="modal-content">
					<p style="color: white; text-align: center; font-size: 16px;">
						<strong>We are five Unical's students and we have
							developed this Web Application for ASDE exam.</strong>
					</p>
					<p></p>
					<p style="color: white; text-align: center;">Marco Amato -
						187734</p>
					<p style="color: white; text-align: center;">Giuseppe Benvenuto
						- 189248</p>
					<p style="color: white; text-align: center;">Mario Carricato -
						187799</p>
					<p style="color: white; text-align: center;">Fabio Fabiano</p>
					<p style="color: white; text-align: center;">Caterina Versace -
						187471</p>
				</div>
			</form>
		</div>
	</div>


	<!-- Info Calendar-->
	<div id="modal-wrapper4" class="modal">
		<div>
			<form class="modal-content animate" action="/action_page.php">
				<div class="imgcontainer">
					<span
						onclick="document.getElementById('modal-wrapper4').style.display='none'"
						class="close" title="Close PopUp">&times;</span>
					<h1 id="calendarNm"
						style="text-align: center; color: white; padding-top: 10px;"></h1>
					<a href="#" class="avatar"><i
						style="font-size: 45px; color: white;"
						class="glyphicon glyphicon-calendar"></i></a>
				</div>

				<div class="modal-content">
					<input type="text" placeholder="Change Calendar Name" name="uname"
						ng-model="vm.calendarToUpd.title" id="newNameCalndar">

					<textarea rows="4" placeholder="DescrCalendar"
						id="descrCalendarUpd" ng-model="vm.calendarToUpd.description"></textarea>

					<div class="btn-group" style="float: right;">
						<button type="button" class="btn btn-primary dropdown-toggle"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i style="font-size: 25px; color: white;"
								class="glyphicon glyphicon-equalizer"></i> <span class="caret"></span>
						</button>
						<input type="text"
							style="font-size: 15px; width: 150px; height: 40px;"
							id="privilages" value="none" class="form-control"></input>
						<div class="dropdown-menu scrollable-menu" id="ulGenres">
							<li><a href="javascript:void(0)"
								onclick="addPermission('ADMIN')" class="clocks" data-id="Admin">Admin</a></li>
							<li><a href="javascript:void(0)"
								onclick="addPermission('RW')" class="clocks"
								data-id="reader&writer">Reader&Writer</a></li>
							<li><a href="javascript:void(0)"
								onclick="addPermission('R')" class="clocks"
								data-id="reader">Reader</a></li>
						</div>
					</div>


					<div class="row-fluid">
						<input ng-keyup="onKeyUP($event)" type="text"
							style="width: 40%; height: 90%;" id="userChoice"
							placeholder="search user">
						<fieldset id="userListModal"></fieldset>
					</div>



					<div style="text-align: center;">
						<button type="button" id="btn-dlCalendar"
							class="btn btn-info navbar-btn" onclick="closeMadal4()"
							ng-click="vm.deleteCalendarView()" style="background: #C62828;">
							<span><strong>Delete Calendar</strong></span>
						</button>
						<button type="button" id="btn-upCalendar"
							class="btn btn-info navbar-btn"
							ng-click="vm.updateCalendarView()" style="background: #42A5F5;">
							<span><strong>Update Calendar</strong></span>
						</button>
						<button type="button" id="btn-shareCalendar"
							class="btn btn-info navbar-btn"
							ng-click=" vm.shareCalendarView()" style="background: #66BB6A;">
							<i style="font-size: 15px; color: white;"
								class="glyphicon glyphicon-send"></i> <span><strong>Share
									Calendar</strong></span>
						</button>
					</div>
				</div>

			</form>
		</div>
	</div>



	<!-- ADD EVENT-->
	<div id="modal-wrapper5" class="modal" style="width: 100%; left: 0%;">
		<div>
			<form class="modal-content animate" action="/action_page.php">
				<div class="imgcontainer">
					<span
						onclick="document.getElementById('modal-wrapper5').style.display='none'"
						class="close" title="Close PopUp">&times;</span>
					<h1 style="text-align: center; color: white; padding-top: 10px;">Add
						Event</h1>
					<a href="#" class="avatar"><i
						style="font-size: 45px; color: white;"
						class="glyphicon glyphicon-calendar"></i></a>
				</div>

				<div class="modal-content" style="width: 90%;">


					<div class="btn-group" style="float: right;">
						<button type="button" class="btn btn-primary dropdown-toggle"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i style="font-size: 25px; color: white;"
								class="glyphicon glyphicon-time"></i> <span class="caret"></span>
						</button>
						<input type="text"
							style="font-size: 15px; width: 80px; height: 40px;" id="TourId"
							value="none" class="form-control"></input>
						<div class="dropdown-menu scrollable-menu" id="ulGenres">
							<li><a href="javascript:void(0)" onclick="addClock(0)"
								class="clocks" data-id="0">At event time</a></li>
							<li><a href="javascript:void(0)" onclick="addClock(10)"
								class="clocks" data-id="10">10 min first</a></li>
							<li><a href="javascript:void(0)" onclick="addClock(20)"
								class="clocks" data-id="20">20 min first</a></li>
							<li><a href="javascript:void(0)" onclick="addClock(30)"
								class="clocks" data-id="30">30 min first</a></li>
							<li><a href="javascript:void(0)" onclick="addClock(-1)"
								class="clocks" data-id="none">none</a></li>
						</div>
					</div>


					<div>
						<form>
							<fieldset id="calendarsListModal""></fieldset>
							<label id="choiceId" style="visibility: hidden;"></label>
							<div id="event_tmp">
								<table class="table table-bordered">
									<thead>
										<tr>
											<th>Title</th>
											<th>Primary color</th>
											<th>Secondary color</th>
											<th>Starts at</th>
											<th>Ends at</th>
										</tr>
									</thead>

									<tbody>
										<td><input type="text" class="form-control"
											ng-model="vm.temp.title"></td>
										<td><input class="form-control" colorpicker type="text"
											ng-model="vm.temp.color.primary"></td>
										<td><input class="form-control" colorpicker type="text"
											ng-model="vm.temp.color.secondary"></td>
										<td>
											<p class="input-group">
												<input type="text" class="form-control" readonly
													uib-datepicker-popup="dd MMMM yyyy"
													ng-model="vm.temp.startsAt" is-open="vm.temp.startOpen"
													close-text="Close"> <span class="input-group-btn">
													<button type="button" class="btn btn-default"
														ng-click="vm.toggle($event, 'startOpen', vm.temp)">
														<i class="glyphicon glyphicon-calendar"></i>
													</button>
												</span>
											</p>
											<div uib-timepicker ng-model="vm.temp.startsAt" hour-step="1"
												minute-step="15" show-meridian="false"></div>
										</td>
										<td>
											<p class="input-group">
												<input type="text" class="form-control" readonly
													uib-datepicker-popup="dd MMMM yyyy"
													ng-model="vm.temp.endsAt" is-open="vm.temp.endOpen"
													close-text="Close"> <span class="input-group-btn">
													<button type="button" class="btn btn-default"
														ng-click="vm.toggle($event, 'endOpen', vm.temp)">
														<i class="glyphicon glyphicon-calendar"></i>
													</button>
												</span>
											</p>
											<div uib-timepicker ng-model="vm.temp.endsAt" hour-step="1"
												minute-step="15" show-meridian="false"></div>
										</td>
									</tbody>
								</table>
							</div>
							<div style="text-align: center;">



								<label style="float: right;"> <input type="checkbox"
									id="repetition" data-toggle="collapse" data-target="#repPanel">
									<label for="repetition"><span></span>set repetition</label>
								</label>

								<div id="repPanel" class="collapse">
									<!-- START HIDDEN PANNEL -->

									<div class="btn-group" style="float: left;">
										<button type="button" class="btn btn-primary dropdown-toggle"
											data-toggle="dropdown" aria-haspopup="true"
											aria-expanded="false">
											<i style="font-size: 25px; color: white;"
												class="glyphicon glyphicon-repeat"></i> <span class="caret"></span>
										</button>
										<input type="text"
											style="font-size: 15px; width: 150px; height: 40px;"
											id="repChoice" value="none" class="form-control"></input>
										<div class="dropdown-menu scrollable-menu" id="ulGenres">
											<li><a href="javascript:void(0)"
												onclick="addFreq('HOUR')" data-id="0">Hourly</a></li>
											<li><a href="javascript:void(0)"
												onclick="addFreq('DAY')" data-id="10">Daily</a></li>
											<li><a href="javascript:void(0)"
												onclick="addFreq('WEEK')" data-id="20">Weekly</a></li>
											<li><a href="javascript:void(0)"
												onclick="addFreq('MONTH')" data-id="30">Monthly</a></li>
											<li><a href="javascript:void(0)"
												onclick="addFreq('YEAR')" data-id="none">Yearly</a></li>
										</div>
									</div>

									<!-- div class="btn-group" >
						<button type="button" class="btn btn-primary dropdown-toggle"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i style="font-size: 25px; color: white;"
								class="glyphicon glyphicon-time"></i> <span class="caret"></span>
						</button>
						<input type="text"
							style="font-size: 15px; width: 150px; height: 40px;" id="TourId"
							value="none" class="form-control"></input>
						<div class="dropdown-menu scrollable-menu" id="ulGenres">
							<li><a href="javascript:void(0)" onclick="addClock(0)"
								class="clocks" data-id="0">At event time</a></li>
							<li><a href="javascript:void(0)" onclick="addClock(10)"
								class="clocks" data-id="10">10 min first</a></li>
							<li><a href="javascript:void(0)" onclick="addClock(20)"
								class="clocks" data-id="20">20 min first</a></li>
							<li><a href="javascript:void(0)" onclick="addClock(30)"
								class="clocks" data-id="30">30 min first</a></li>
							<li><a href="javascript:void(0)" onclick="addClock(-1)"
								class="clocks" data-id="none">none</a></li>
						</div>
					</div>   -->

									<!-- <i style="color: white; margin-left: 40px; ">Interval</i>
  					<input type="number" style="width: 100px;" name="quantity" min="1" max="5">  -->


									<table class="table table-bordered">
										<thead>
											<tr>
												<th>Starts at</th>
												<th>Ends at</th>
											</tr>
										</thead>

										<tbody>
											<td>
												<p class="input-group">
													<input type="text" class="form-control" readonly
														uib-datepicker-popup="dd MMMM yyyy"
														ng-model="vm.temp.dtstart" is-open="vm.temp.startOpen2"
														close-text="Close"> <span class="input-group-btn">
														<button type="button" class="btn btn-default"
															ng-click="vm.toggle($event, 'startOpen2', vm.temp)">
															<i class="glyphicon glyphicon-calendar"></i>
														</button>
													</span>
												</p>
												<div uib-timepicker ng-model="vm.temp.dtstart" hour-step="1"
													minute-step="15" show-meridian="false"></div>
											</td>
											<td>
												<p class="input-group">
													<input type="text" class="form-control" readonly
														uib-datepicker-popup="dd MMMM yyyy"
														ng-model="vm.temp.until" is-open="vm.temp.endOpen2"
														close-text="Close"> <span class="input-group-btn">
														<button type="button" class="btn btn-default"
															ng-click="vm.toggle($event, 'endOpen2', vm.temp)">
															<i class="glyphicon glyphicon-calendar"></i>
														</button>
													</span>
												</p>
												<div uib-timepicker ng-model="vm.temp.until" hour-step="1"
													minute-step="15" show-meridian="false"></div>
											</td>
										</tbody>
									</table>




								</div>
								<!-- END HIDDEN PANNEL -->




								<textarea rows="2" placeholder="Description" id="descEvent"
									ng-model="vm.temp.description"></textarea>

								<div>
									<button type="button" id="addEvnt" ng-click="vm.addEventView()"
										class="btn btn-info navbar-btn" style="background: #42A5F5">
										<span><strong>Add </strong></span>
									</button>
								</div>
							</div>
					</div>

					<!-- DELETEEEEE GRAPHIC EVENT
      				<button class="btn btn-danger" ng-click="vm.events.splice($index, 1)">Delete</button>
					 <td>
					</td> -->
				</div>
			</form>
		</div>
	</div>


	<div id="modal-wrapper6" class="modal" style="width: 100%; left: 0%;">
		<div>
			<form class="modal-content animate" action="/action_page.php">
				<div class="imgcontainer">
					<span
						onclick="document.getElementById('modal-wrapper6').style.display='none'"
						class="close" title="Close PopUp">&times;</span>


					<h3 style="text-align: center; color: white; padding-top: 30px;">Update
						Event</h3>
					<a href="#" class="avatar"><i
						style="text-align: center; font-size: 45px; color: white;"
						class="glyphicon glyphicon-pencil"></i></a>
				</div>

				<div class="modal-content" style="width: 90%;">

					<div>

						<div class="btn-group" style="float: right;">
							<button type="button" class="btn btn-primary dropdown-toggle"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false">
								<i style="font-size: 25px; color: white;"
									class="glyphicon glyphicon-time"></i> <span class="caret"></span>
							</button>
							<input type="text"
								style="font-size: 15px; width: 80px; height: 40px;" id="TourId2"
								class="form-control"></input>
							<div class="dropdown-menu scrollable-menu" id="ulGenres">
								<li><a href="javascript:void(0)" onclick="updateClock(0)"
									class="clocks" data-id="0">At event time</a></li>
								<li><a href="javascript:void(0)" onclick="updateClock(10)"
									class="clocks" data-id="10">10 min first</a></li>
								<li><a href="javascript:void(0)" onclick="updateClock(20)"
									class="clocks" data-id="20">20 min first</a></li>
								<li><a href="javascript:void(0)" onclick="updateClock(30)"
									class="clocks" data-id="30">30 min first</a></li>
								<li><a href="javascript:void(0)" onclick="updateClock(-1)"
									class="clocks" data-id="none">none</a></li>
							</div>
						</div>

						<div id="event_tmp">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th>Title</th>
										<th>Primary color</th>
										<th>Secondary color</th>
										<th>Starts at</th>
										<th>Ends at</th>
									</tr>
								</thead>

								<tbody>
									<td><input type="text" class="form-control"
										ng-model="vm.temp.title"></td>
									<td><input class="form-control" colorpicker type="text"
										ng-model="vm.temp.color.primary"></td>
									<td><input class="form-control" colorpicker type="text"
										ng-model="vm.temp.color.secondary"></td>
									<td>
										<p class="input-group">
											<input type="text" class="form-control" readonly
												uib-datepicker-popup="dd MMMM yyyy"
												ng-model="vm.temp.startsAt" is-open="vm.temp.startOpen"
												close-text="Close"> <span class="input-group-btn">
												<button type="button" class="btn btn-default"
													ng-click="vm.toggle($event, 'startOpen', vm.temp)">
													<i class="glyphicon glyphicon-calendar"></i>
												</button>
											</span>
										</p>
										<div uib-timepicker ng-model="vm.temp.startsAt" hour-step="1"
											minute-step="15" show-meridian="false"></div>
									</td>
									<td>
										<p class="input-group">
											<input type="text" class="form-control" readonly
												uib-datepicker-popup="dd MMMM yyyy"
												ng-model="vm.temp.endsAt" is-open="vm.temp.endOpen"
												close-text="Close"> <span class="input-group-btn">
												<button type="button" class="btn btn-default"
													ng-click="vm.toggle($event, 'endOpen', vm.temp)">
													<i class="glyphicon glyphicon-calendar"></i>
												</button>
											</span>
										</p>
										<div uib-timepicker ng-model="vm.temp.endsAt" hour-step="1"
											minute-step="15" show-meridian="false"></div>
									</td>
								</tbody>
							</table>
						</div>
						<div style="text-align: center;">
							<textarea rows="2" placeholder="Description" id="descEvent"
								ng-model="vm.temp.description"></textarea>




							<!-- TASTO CHE STA NEL MODAL -->
							<button type="button" id="upEvent"
								ng-click="vm.updateEventView()" class="btn btn-info navbar-btn"
								style="background: #42A5F5">
								<span><strong>Update Event</strong></span>

							</button>
						</div>
					</div>

					<!-- DELETEEEEE GRAPHIC EVENT
      				<button class="btn btn-danger" ng-click="vm.events.splice($index, 1)">Delete</button>
					 <td>
					</td> -->

				</div>
			</form>
		</div>
	</div>

	<!-- MEMO -->
	<div id="modal-wrapper7" class="modal">
		<div>
			<form class="modal-content animate" action="/action_page.php">
				<div class="imgcontainer">
					<span
						onclick="document.getElementById('modal-wrapper7').style.display='none'"
						class="close" title="Close PopUp">&times;</span>
					<h1 style="text-align: center; color: white; padding-top: 10px;">Add
						Meno</h1>
					<a href="#" class="avatar"><i
						style="font-size: 45px; color: white;"
						class="glyphicon glyphicon-save-file"></i></a>
				</div>

				<div class="modal-content">
					<div style="text-align: center;">


						<div id="event_tmp">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th>Title</th>
										<th>Primary color</th>
									</tr>
								</thead>

								<tbody>
									<div class="row">
										<td><input type="text" class="form-control"
											ng-model="vm.memo.title"></td>
										<td><input class="form-control" colorpicker type="text"
											ng-model="vm.memo.color.primary"></td>
									</div>
								</tbody>
							</table>
							<textarea rows="2" placeholder="Description" id="descMemo"
								ng-model="vm.memo.description"></textarea>
						</div>
						<button type="button" id="newCalendar"
							class="btn btn-info navbar-btn" ng-click="vm.addMemoView()"
							style="background: #42A5F5">
							<span><strong>Add</strong></span>
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- UPDATE MEMO -->
	<div id="modal-wrapper8" class="modal">
		<div>
			<form class="modal-content animate" action="/action_page.php">
				<div class="imgcontainer">
					<span
						onclick="document.getElementById('modal-wrapper8').style.display='none'"
						class="close" title="Close PopUp">&times;</span>
					<h1 style="text-align: center; color: white; padding-top: 10px;">Update
						Meno</h1>
					<a href="#" class="avatar"><i
						style="font-size: 45px; color: white;"
						class="glyphicon glyphicon-save-file"></i></a>
				</div>

				<div class="modal-content">

					<div style="text-align: center;">


						<div id="event_tmp">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th>Title</th>
										<th>Primary color</th>
									</tr>
								</thead>

								<tbody>
									<div class="row">
										<td><input type="text" class="form-control"
											ng-model="vm.memo.title"></td>
										<td><input class="form-control" colorpicker type="text"
											ng-model="vm.memo.color.primary"></td>
									</div>
								</tbody>
							</table>
							<textarea rows="2" placeholder="Description" id="descMemo"
								ng-model="vm.memo.description"></textarea>
						</div>


						<button type="button" id="newCalendar"
							class="btn btn-info navbar-btn" ng-click="vm.updateMemoView()"
							style="background: #42A5F5">
							<span><strong>Add</strong></span>
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>



	<!-- Invitations -->
	<div id="modal-wrapper9" class="modal">
		<div>
			<form class="modal-content animate" action="/action_page.php">
				<div class="imgcontainer">
					<span
						onclick="document.getElementById('modal-wrapper9').style.display='none'"
						class="close" title="Close PopUp">&times;</span>
					<h1 style="text-align: center; color: white; padding-top: 10px;">Invitation</h1>
					<a href="#" class="avatar"><i
						style="font-size: 45px; color: white;"
						class="glyphicon glyphicon-envelope"></i></a>
				</div>

				<div class="modal-content">
					<div style="text-align: center;">


						<p id="invitationModal" style="color: white;">Do you want to
							accept?</p>



						<div style="text-align: center;">
							<button type="button" id="btn-dlCalendar"
								class="btn btn-info navbar-btn" onclick=""
								ng-click="vm.refuseInvitation()" style="background: #C62828;">
								<span><strong>Decline</strong></span>
							</button>
							<button type="button" id="btn-upCalendar"
								class="btn btn-info navbar-btn" ng-click="vm.acceptInvitation()"
								style="background: #66BB6A;">
								<span><strong>Accept</strong></span>
							</button>




						</div>
					</div>
				</div>
			</form>
		</div>
	</div>


	<script type="text/javascript">
		var openModalType2 = function() {
			var name = document.getElementById("nameUser").value = '';
			var oldP = document.getElementById("oldP").value = '';
			var newP = document.getElementById("newP").value = '';
			modal(2);
		}
	</script>


	<!-- CALENDAR SETTINGS Madal(4)-->
	<script type="text/javascript">
		var manageCalendar = function(a, b) {
			//alert("chiamata funzione impostazion calendario");
			modal(4);
			document.getElementById("calendarNm").innerHTML = a + " settings";

		}
	</script>

	<script type="text/javascript">
		var closeMadal4 = function() {
			//	alert("chiamata funzione impostazion calendario");
			var a = document.getElementById("newNameCalndar").value;

			document.getElementById('modal-wrapper4').style.display = 'none';

		}
	</script>


	<!-- jQuery CDN -->
	<script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
	<!-- Bootstrap Js CDN -->
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

	<!--   ***************************************************************************************************************************** -->

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/js/bootstrap-select.min.js"></script>
	<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/js/i18n/defaults-*.min.js"></script> -->



	<script type="text/javascript">
		$(document).ready(function() {
			$('#sidebarCollapse').on('click', function() {
				$('#sidebar').toggleClass('active');
				$(this).toggleClass('active');
			});
		});
	</script>
</body>
</html>
