<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
            <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    

<!DOCTYPE html>
<html>
<head>
<!--<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>


 <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">

<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.min.js"></script>
 -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<title>Insert title here</title>
</head>
<style>
#image {
	position: relative;
	left: 850px;
}

#formUpdate {
	position: absolute;
	left: 60px;
	top: 220px;
	background-color: #ffffff;
	width: 500px;
}
.alert {
	position: absolute;
	top: 81px;
	width: 100%;
}
.multiselect {
	width: 200px;
}
.input-invalid{
	color:red;
		text-align: center;
	
}

</style>
<body>
	<%@ include file="navBarTeacher.jsp"%>
	<c:if test="${not empty sessionScope.invalidFields}">
		<c:if test="${not sessionScope.invalidFields}">
			<div class="alert alert-success">
				<strong>Success!</strong> Indicates a successful or positive action.
			</div>
		</c:if>
	</c:if>
	<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	<div style = " position:absolute;top:260px;left:230px;z-index:1;">
	<form action="./ReadHomeworkServlet" method="GET" style = "">
			<input type='hidden' value='${sessionScope.currHomework.tasksFile}'
				name='fileName'>
			<button class='btn btn-link btn-xs'
				type='submit'>
				<b><i><c:out value="${sessionScope.currHomework.heading }.pdf" /></i></b>
			</button>
		</form>
		</div>
	<div id="formUpdate" align="right">
		<form action="./RemoveHomeworkDetails" method="POST">
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-2" style="left: 290px">
					<button type="submit"
										class="glyphicon glyphicon-remove btn btn-default btn-xs" onclick="javascript:return confirm('Are you sure you want to remove this homework permanently?')"></button>
				</div>
			</div>
		</form>
		
		<form action="./UpdateHomeworkServlet" method="POST"
			enctype="multipart/form-data" id="updateHomeworkForm">
			<label
				style="position: absolute; left: 290px; text-decoration: underline;">Update
				homework</label> <br> <br> <br>
			<c:if test="${not empty sessionScope.invalidFields}">
				<c:if test="${sessionScope.invalidFields}">
					<p style="text-align: center" class="input-invalid">Invalid
						fields</p>
				</c:if>

			</c:if>
			<c:if test="${sessionScope.emptyFields}">
				<p class="input-invalid" style="width: 250px; text-align: left">You
					cannot have empty fields</p>
			</c:if>
			<div class="form-group">
				<label class="control-label col-sm-6">Heading</label>
				<div class="col-sm-6">
					<input type="text" class="form-control" name="name"
						value='${sessionScope.currHomework.heading}' maxlength="40"
						data-toggle="popover" data-placement="bottom" data-trigger="focus"
						data-content="Size of heading - 5 to 40 symbols. Valid inputs are numbers and letters (large and small)"
						required />
					<c:if test="${not empty sessionScope.validHeading}">
						<c:if test="${not sessionScope.validHeading}">
							<p id="nameMsg" class="input-invalid">Invalid heading</p>
						</c:if>
						<c:if test="${not empty sessionScope.uniqueHeading}">
							<c:if test="${sessionScope.validHeading}">
								<c:if test="${not sessionScope.uniqueHeading}">
									<p id="nameMsg" class="input-invalid">Heading already
										exists</p>
								</c:if>
							</c:if>
						</c:if>
					</c:if>
					<p id="nameMsg" class="input-invalid"></p>

				</div>
			</div>
			<div class="form-group">
				<br> <label class="control-label col-sm-6">Opening time</label>
				<!--  <input type='date' class="form-control" name="opensDate"
						data-val="true" id="opens" />-->
				<div class='col-sm-6'>
					<div class='input-group date' id='datetimepicker6'>
						<input type='text'
							value="${sessionScope.currHomework.openingTime}"
							class="form-control" id="opens" name="opens"
							placeholder="Enter opening time" data-toggle="popover"
							data-placement="bottom" data-trigger="focus"
							data-content="From today max 6 months from now" required /> <span
							class="input-group-addon"> <span
							class="glyphicon glyphicon-calendar"></span>
						</span>

					</div>
					<c:if test="${not empty sessionScope.validOpeningTime}">

						<c:if test="${not sessionScope.validOpeningTime}">
							<p id="opensMsg" class="input-invalid">Invalid opening time</p>
						</c:if>
					</c:if>
					<p id="opensMsg" class="input-invalid"></p>


				</div>


			</div>
			<div class="form-group">
				<br> <label class="control-label col-sm-6">Closing time</label>
				<div class='col-sm-6'>
					<div class='input-group date' id='datetimepicker7'>
						<input type='text'
							value="${sessionScope.currHomework.closingTime}"
							class="form-control" id="closes" name="closes"
							placeholder="Enter closing time" data-toggle="popover"
							data-placement="bottom" data-trigger="focus"
							data-content="Max 6 months after opening time" required /> <span
							class="input-group-addon"> <span
							class="glyphicon glyphicon-calendar"></span>
						</span>


					</div>
					<c:if test="${not empty sessionScope.validClosingTime}">

						<c:if test="${not sessionScope.validClosingTime}">
							<p id="closesMsg" class="input-invalid">Invalid closing time</p>
						</c:if>
					</c:if>
					<p id="closesMsg" class="input-invalid"></p>

				</div>

			</div>
			<br>
			<div class="form-group">
				<label class="control-label col-sm-6">Number of tasks</label>
				<div class="col-sm-6">
					<input type="number" min="0" class="form-control"
						name="numberOfTasks"
						value='${sessionScope.currHomework.numberOfTasks}' maxlength="2"
						data-toggle="popover" data-placement="bottom" data-trigger="focus"
						data-content="From 1 to 40" required />
					<c:if test="${not empty sessionScope.validTasks}">

						<c:if test="${not sessionScope.validTasks}">
							<p id="numberOfTasksMsg" class="input-invalid">Invalid tasks</p>
						</c:if>
					</c:if>
					<p id="numberOfTasksMsg" class="input-invalid"></p>

				</div>
			</div>
			<br>
			<div class="form-group">
				<label class="control-label col-sm-6">Tasks</label>
				<div class="col-sm-6">
					<input type="file" accept="application/pdf" name="file">
					
					<c:if test="${not empty sessionScope.validFile}">

						<c:if test="${not sessionScope.validFile}">
							<p id="fileMsg" class="input-invalid">Invalid file</p>
						</c:if>
					</c:if>
					<p id="fileMsg" class="input-invalid"></p>
				</div>
			</div>
			<br>
					
			<div class="form-group">
				<label class="control-label col-sm-6">Groups</label>
				<div class="col-sm-6">
					<select class="selectpicker" multiple name="groups" id="groups"
						required>
						<c:forEach items="${applicationScope.allGroups}" var="group">
							<c:set var="isHwInGroup" value="false"></c:set>
							<c:forEach items="${group.homeworks}" var="homework">
								<c:if test="${homework.id==sessionScope.currHomework.id}">
									<c:set var="isHwInGroup" value="true"></c:set>
								</c:if>
							</c:forEach>
							<c:if test="${isHwInGroup}">
								<option value="${group.id}" selected>
									<c:out value="${group.name}"></c:out></option>
							</c:if>
							<c:if test="${not isHwInGroup}">
								<option value="${group.id}">
									<c:out value="${group.name}"></c:out></option>
							</c:if>
						</c:forEach>
					</select>
					<c:if test="${not empty sessionScope.validGroups}">

						<c:if test="${not sessionScope.validGroups}">
							<p id="groupsMsg" class="input-invalid">Invalid groups</p>
						</c:if>
					</c:if>
					<p id="groupsMsg" class="input-invalid"></p>

				</div>

			</div>

			<br>
			<br>
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-2" style="left: 290px">
					<input style="align: right" type="submit" class="btn btn-default"
						value="Save">
				</div>
			</div>

		</form>
	</div>

	<c:if test="${not empty sessionScope.invalidFields}">
		<c:remove var="invalidFields" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.emptyFields}">
		<c:remove var="emptyFields" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.validHeading}">
		<c:remove var="validHeading" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.uniqueHeading}">
		<c:remove var="uniqueHeading" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.validOpeningTime}">
		<c:remove var="validOpeningTime" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.validClosingTime}">
		<c:remove var="validClosingTime" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.validTasks}">
		<c:remove var="validTasks" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.validGroups}">
		<c:remove var="validGroups" scope="session" />
	</c:if>

	<script>
	$
	(document).ready(function() {
		$(function() {
			$('#datetimepicker6').datetimepicker({
				format : 'YYYY/MM/DD HH:mm'

			});
			$('#datetimepicker7').datetimepicker({
				format : 'YYYY/MM/DD HH:mm'

			});

		});
	});
		
	    function isFileValidCheck() {
			var file = document.forms["updateHomeworkForm"]["file"].value;
			var val = file.toLowerCase();
			var regex = new RegExp("(.*?)\.(pdf)$");
			if (!(regex.test(val))) {
				return false;
			}
			
			 var size = (document.forms["updateHomeworkForm"]["file"].files[0].size/1024/1024).toFixed(2);
			console.log(size)
			if(size > 20){
				console.log(false)
				return false;
			}
			return true;
		}
			$('#updateHomeworkForm').submit(function(e) {
				e.preventDefault();
				var name = document.forms["updateHomeworkForm"]["name"].value;
				var opens = document.forms["updateHomeworkForm"]["opens"].value;
				var closes = document.forms["updateHomeworkForm"]["closes"].value;
				var numberOfTasks = document.forms["updateHomeworkForm"]["numberOfTasks"].value;
				var file = document.forms["updateHomeworkForm"]["file"].value;
				var groups = document.forms["updateHomeworkForm"]["groups"].value;
				
				var isNameValid = true;
				var isNameUnique = true;
				var isOpensValid = true;
				var isClosesValid = true;
				var isNumberOfTasksValid = true;
				var isFileValid = true;
				var isGroupsValid = true;
				if (!$('#nameMsg').is(':empty')) {
					$("#nameMsg").empty();
				}if (!$('#opensMsg').is(':empty')) {
					$("#opensMsg").empty();
				}if (!$('#closesMsg').is(':empty')) {
					$("#closesMsg").empty();
				}if (!$('#numberOfTasksMsg').is(':empty')) {
					$("#numberOfTasksMsg").empty();
				}
				if (!$('#groupsMsg').is(':empty')) {
					$("#groupsMsg").empty();
				}
				
				
				if (name == "") {
					document.getElementById("nameMsg").append(
					"Fill heading");
					isNameValid = false;
				}
				if (opens == "") {
					document.getElementById("opensMsg").append(
					"Fill opening time");
					isOpensValid = false;
				}
				if (closes == "") {
					document.getElementById("closesMsg").append(
					"Fill closing time");
					isClosesValid = false;
				}
				if (numberOfTasks == "") {
					document.getElementById("numberOfTasksMsg").append(
					"Fill number of tasks");
					isNumberOfTasksValid = false;
				}else{
					if((numberOfTasks < 1) || (numberOfTasks > 40)){
						console.log(12)
						document.getElementById("numberOfTasksMsg").append(
						"Min = 1 max = 40");
						isNumberOfTasksValid = false;
					}
				}
				
				if(groups.length == 0){
					document.getElementById("groupsMsg").append(
					"Fill groups");
					isGroupsValid = false;
				}

			
				if(!(isNameValid === true && isOpensValid === true && isClosesValid === true && isNumberOfTasksValid === true && isFileValid === true && isGroupsValid === true)){
					return false;
				}
				
				$.ajax({
					url : './IsHomeworkUpdateHeadingValid',
					type : 'GET',
					data : {
						"heading" : name
					},
					success : function(response) {
						console.log(99)
						if (!$('#nameMsg').is(':empty')) {
							$("#nameMsg").empty();
							isNameValid = true;
						}
						$.ajax({
							url : './IsHomeworkUpdateHeadingIsRepeated',
							type : 'GET',
							data : {
								"heading" : name
							},
							success : function(response) {
								console.log(99)
								if (!$('#nameMsg').is(':empty')) {
									$("#nameMsg").empty();
									isNameUnique = true;
								}
							},
							error : function(data) {				

								if (!$('#nameMsg').is(':empty')) {
									$("#nameMsg").empty();
								}
								isNameUnique = false;
								document.getElementById("nameMsg").append(
										"Homework with this heading already exist");
								console.log("invalid heading")
							}
						});
					},
					error : function(data) {				
						if (!$('#nameMsg').is(':empty')) {
							$("#nameMsg").empty();
						}
						isNameValid = false;
						document.getElementById("nameMsg").append(
								"Heading is not valid");
						console.log("invalid heading")
					}
				});
				
				$.ajax({
					url : './IsHomeworkUpdateOpeningTimeValid',
					type : 'GET',
					data : {
						"opens" : opens
					},
					success : function(response) {
						if (!$('#opensMsg').is(':empty')) {
							$("#opensMsg").empty();
							isOpensValid = true;
						}
					},
					error : function(data) {
						if (!$('#opensMsg').is(':empty')) {
							$("#opensMsg").empty();
						}
						isOpensValid = false;
						document.getElementById("opensMsg").append(
								"Opening time is not valid - the earliest point can be a day ago");
						console.log("invalid opens")
					}
				});
				$.ajax({
					url : './IsHomeworkUpdateClosingTimeValid',
					type : 'GET',
					data : {
						"opens" : opens,
						"closes" : closes
					},
					success : function(response) {
						if (!$('#closesMsg').is(':empty')) {
							$("#closesMsg").empty();
							isClosesValid = true;
						}
					},
					error : function(data) {
						if (!$('#closesMsg').is(':empty')) {
							$("#closesMsg").empty();
						}
						isClosesValid = false;
						document.getElementById("closesMsg").append(
								"Closing time is not valid - closing time must be after opening time (max 6 months after)");
						console.log("invalid opens")
					}
				});
				
				if(numberOfTasks >=1 && numberOfTasks <= 40){
					if (!$('#numberOfTasksMsg').is(':empty')) {
						$("#numberOfTasksMsg").empty();
						isNumberOfTasksValid = true;
					}
				}else{
					if (!$('#numberOfTasksMsg').is(':empty')) {
						$("#numberOfTasksMsg").empty();
					}
					isNumberOfTasksValid = false;
					document.getElementById("numberOfTasksMsg").append(
							"Number of tasks- between 1 and 40");
					console.log("invalid numberOfTasks")
				}
				if(file != ""){
				isFileValid = false;
			
				isFileValid = isFileValidCheck();
				if(!isFileValid){
					if (!$('#fileMsg').is(':empty')) {
						$("#fileMsg").empty();
					}
					document.getElementById("fileMsg").append(
					"File format-pdf, maxSize - 20MB");
					console.log("invalid file")
				}
				}
				$( document ).ajaxStop(function() {
				if((isNameUnique === true && isNameValid === true && isOpensValid === true && isClosesValid === true && isNumberOfTasksValid === true && isFileValid === true)){
					
					document.getElementById("updateHomeworkForm").submit();
				}});
			});
	   
			$(document).ready(function() {

				$('[data-toggle="popover"]').popover();

				
			});
	</script>
</body>
</html>