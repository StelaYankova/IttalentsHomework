<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.14.1/moment.min.js"></script>

<script type="text/javascript" src="http://www.datejs.com/build/date.js"></script>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>
#image {
	position: relative;
	left: 850px;
}
.input-invalid{
	color:red;
}
#formAddHomework {
	position: absolute;
	left: 60px;
	top: 220px;
	background-color: #ffffff;
	width: 500px;
}
/*.multiselect {
	width: 200px;
}

.selectBox {
	position: relative;
}

.selectBox select {
	width: 100%;
	font-weight: bold;
}

.overSelect {
	position: absolute;
	left: 0;
	right: 0;
	top: 0;
	bottom: 0;
}

#checkboxes {
	display: none;
	border: 1px #dadada solid;
}

#checkboxes label {
	display: block;
}

#checkboxes label:hover {
	background-color: #1e90ff;
}*/
</style>
<body>
	<%@ include file="navBarTeacher.jsp"%>
	<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	<div id="formAddHomework" align="right">
		<form action="./AddHomework" method="POST"
			enctype="multipart/form-data" id = "addHomeworkForm">
			<label
				style="position: absolute; left: 290px; text-decoration: underline;">Add
				homework</label> <br> <br> <br>
			<div class="form-group">
			<label class="control-label col-sm-6">Name</label>
				<div class="col-sm-6">
					<input type="text" class="form-control" name="name" placeholder="Enter heading" value data-toggle = "popover" data-placement = "bottom" data-trigger = "focus" data-content = "Size of heading - 5 to 40 symbols. Valid inputs are numbers and letters (large and small)" required/>
					<p id = "nameMsg" class = "input-invalid"></p>
				</div>
			</div>
			<div class="form-group">
				<br>
				<label class="control-label col-sm-6">Opening time</label>
				<div class="col-sm-6">
					<input type='datetime' class="form-control" name="opens"
						data-val="true" id = "opens" required/>
					<p id = "opensMsg" class = "input-invalid"></p>
				</div>

			</div>
			<div class="form-group">
				<br>
				<label class="control-label col-sm-6">Closing time</label>
				<div class="col-sm-6">
					<input type='datetime' class="form-control" name="closes" id = "closes"
						data-val="true" required/>
					<p id = "closesMsg" class = "input-invalid"></p>		
				</div>
			</div>
			<div class="form-group">
				<br>
				<label class="control-label col-sm-6">Number of tasks</label>
				<div class="col-sm-6">
					<input type="number" min="0" max = "40" class="form-control"
						name="numberOfTasks" placeholder="Enter number of tasks" value data-toggle = "popover" data-placement = "bottom" data-trigger = "focus" required/>
					<p id = "numberOfTasksMsg" class = "input-invalid"></p>
				</div>

			</div>
			<div class="form-group">

				<br>
				<label class="control-label col-sm-6">Tasks</label>
				<div class="col-sm-6">
					<input type="file" accept="application/pdf" name="file" required/>
					<p id = "fileMsg" class = "input-invalid"></p>
				</div>
			</div>
			<!--  <div class="multiselect"><div class="selectBox" onclick="showCheckboxes()">-->
			<br>
			<div class="form-group">
				<label class="control-label col-sm-6">Groups</label>
				<div class="col-sm-6">
					<select class="selectpicker" multiple name="groups">

						<c:forEach items="${applicationScope.allGroups}" var="group">
							<option value="${group.id}">
								<c:out value="${group.name}"></c:out></option>
						</c:forEach>
						<!-- <select>
					<option>Select an option</option>
				</select>
				<div class="overSelect"></div>
			</div>
			<div id="checkboxes" >
				<c:forEach items="${applicationScope.allGroups}" var="group">
					<input type="checkbox" name = "groups" value="${group.id}" >
					<c:out value="${group.name}"></c:out>
				</c:forEach>
			</div> -->

					</select>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-2" style="left: 290px">

					<!-- <button style="align: right" type="button" class="btn btn-default">Save</button> -->
					<input  style="align: right" type="submit" class="btn btn-default" value = "Save">
				</div>
			</div>
		</form>
	</div>
	<script>
    /*var expanded = false;
    function showCheckboxes() {
        var checkboxes = document.getElementById("checkboxes");
        if (!expanded) {
            checkboxes.style.display = "block";
            expanded = true;
        } else {
            checkboxes.style.display = "none";
            expanded = false;
        }
    }*/
    var m = moment().format("YYYY-MM-DD hh:mm a");

    document.getElementById("opens").value = m;
    document.getElementById("closes").value = m;
    
    function isFileValidCheck() {
		var file = document.forms["addHomeworkForm"]["file"].value;
		var val = file.toLowerCase();
		var regex = new RegExp("(.*?)\.(pdf)$");
		if (!(regex.test(val))) {
			return false;
		}
		
		 var size = (document.forms["addHomeworkForm"]["file"].files[0].size/1024/1024).toFixed(2);
		console.log(size)
		if(size > 20){
			console.log(false)
			return false;
		}
		//console.log('This file size is: ' + (document.forms["addHomeworkForm"].files[0].size/1024/1024).toFixed(2) + " MB");
		return true;
	}
		$('#addHomeworkForm').submit(function(e) {
			e.preventDefault();
			var name = document.forms["addHomeworkForm"]["name"].value;
			var opens = document.forms["addHomeworkForm"]["opens"].value;
			var closes = document.forms["addHomeworkForm"]["closes"].value;
			var numberOfTasks = document.forms["addHomeworkForm"]["numberOfTasks"].value;
			var file = document.forms["addHomeworkForm"]["file"].value;

			/*var input = document.getElementById('input-file');
	        file = input.files[0];
	        var data = new FormData();
	        data.append('file', file);*/
			/*var input = document.forms["addHomeworkForm"]["file"].value;
	        var file = new FormData();
	        file.append("file", input);*/


			var isNameValid = true;
			var isOpensValid = true;
			var isClosesValid = true;
			var isNumberOfTasksValid = true;
			var isFileValid = true;
			
			if(name == ""){
				isNameValid = false;
			}
			if(opens == ""){
				isOpensValid = false;
			}
			if(closes == ""){
				isClosesValid = false;
			}
			if(numberOfTasks == ""){
				isNumberOfTasksValid = false;
			}
			if(file == ""){
				isFileValid = false;
			}
			
			if(!(isNameValid === true && isOpensValid === true && isClosesValid === true && isNumberOfTasksValid === true && isFileValid === true)){
				return false;
			}
			
			$.ajax({
				url : './IsHomeworkHeadingValid',
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
				url : './IsHomeworkOpeningTimeValid',
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
				url : './IsHomeworkClosingTimeValid',
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
							"Opening time is not valid - closing time must be after opening time (max 6 months after)");
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
			
			/*$.ajax({
				url : './IsHomeworkFileValid',
				type : 'GET',
				data : {
					"file" : file
				},
				cache: false,
			    contentType:false,
			    processData: false,
				success : function(response) {
					if (!$('#fileMsg').is(':empty')) {
						$("#fileMsg").empty();
						isFileValid = true;
					}
				},
				error : function(data) {
					if (!$('#fileMsg').is(':empty')) {
						$("#fileMsg").empty();
					}
					isFileValid = false;
					document.getElementById("fileMsg").append(
							"File format-pdf, maxSize - 1MB");
					console.log("invalid file")
				}
			});*/
			isFileValid = isFileValidCheck();
			if(!isFileValid){
				if (!$('#fileMsg').is(':empty')) {
					$("#fileMsg").empty();
				}
				document.getElementById("fileMsg").append(
				"File format-pdf, maxSize - 20MB");
				console.log("invalid file")
			}
			$( document ).ajaxStop(function() {
					console.log(isClosesValid);
					console.log(isOpensValid);
					console.log(isNameValid);
					console.log(isNumberOfTasksValid);
					console.log(isFileValid);
			if((isNameValid === true && isOpensValid === true && isClosesValid === true && isNumberOfTasksValid === true && isFileValid === true)){
				
				document.getElementById("addHomeworkForm").submit();
			}});
		});
   
		
	</script>
</body>
</html>
