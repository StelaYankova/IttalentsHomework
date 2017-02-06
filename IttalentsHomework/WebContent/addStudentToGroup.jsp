<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!--  <link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.min.js"></script>-->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>
.ui-helper-hidden-accessible {
	display: none;
}
.alert {
	position: absolute;
	top: 81px;
	width: 100%;
}
.input-invalid{
	color:red;
}
ul.ui-autocomplete {
	list-style-type: none;
	text-decoration: none;
}

#image {
	position: relative;
	left: 850px;
}

#pageContent {
	position: absolute;
	top: 150px;
	left: 30px;
	width: 60%;
}

#addStudentButton{
position: relative;
	top: 10px;
}
.form-group {
	width: 30%
}
</style>
<body>
	<%@ include file="navBarTeacher.jsp"%>
<c:if test="${not empty invalidFields}">
		<c:if test="${not invalidFields}">
			<div class="alert alert-success"  id = "alert">
				<strong>Success!</strong> Indicates a successful or positive action.
			</div>
		</c:if>
	</c:if>
	<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	<div id="pageContent">
												

		<div class="ui-widget">
			<form action="./AddStudentToGroupServlet" method="POST"
				class="form-inline" id = "addStudentToGroupForm">
				<c:if test="${not empty invalidFields}">
			<c:if test="${invalidFields}">
			<p style = "text-align:left" class="input-invalid">Invalid fields</p>
			</c:if>
		</c:if>
			<c:if test="${not empty emptyFields}">
				<c:if test="${emptyFields}">
					<p style="text-align: left" class="input-invalid">You cannot
						have empty fields</p>
				</c:if>
			</c:if>
			<c:if test="${not empty doesStudentExist}">
						<c:if test="${not doesStudentExist}">
							<p id="studentMsg" class="input-invalid">Student does not exist</p>
						</c:if>
						<c:if test="${not empty isStudentInGroup}">
							<c:if test="${doesStudentExist}">
								<c:if test="${isStudentInGroup}">
									<p id="studentMsg" class="input-invalid">Student is already in group</p>
								</c:if>
							</c:if>
						</c:if>
					</c:if>
															<p id = "groupMsg" class = "input-invalid"></p>
																					<p id = "studentMsg" class = "input-invalid"></p>
															
					
				<div class="form-group">
				
					<label class="control-label col-sm-8">Choose group:</label> <select
						id="chosenGroup" name="chosenGroup" class="selectpicker">
						<option value="null">-</option>
						<c:forEach var="group" items="${applicationScope.allGroups}">
							<option value="${group.id}"><c:out value="${group.name}"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group" id = "studentSearch">
					<label class="control-label col-sm-8">Choose student:</label> <input
						id="searchStudents" name="selectedStudent" class="form-control" value = "${chosenUsernameTry}" required/>
										</div>
						
					<button type="submit" id = "addStudentButton" class="btn btn-default btn-md">
						<span class="glyphicon glyphicon-plus">Add</span>
					</button>
			</form>
		</div>
		<br>
		<br>
		<ul id="listOfStudentsOfGroup" class="editable list-group"
			style="visibility: hidden; z-index: 1; height: 300px; width: 35%; overflow: hidden; overflow-y: scroll; overflow-x: scroll;"></ul>
	</div>
</body>
<script>
$('#addStudentToGroupForm').submit(function(e) {
	e.preventDefault();
	var chosenGroupId = document.forms["addStudentToGroupForm"]["chosenGroup"].value;
	var chosenStudentUsername = document.forms["addStudentToGroupForm"]["selectedStudent"].value;
		
	var doesUserExist = true;
	var chosenStudentUsernameAlreadyInGroup = true;
	var chosenStudentUsernameEmpty = false;
	var chosenGroupEmpty = false;

	if(chosenGroupId == 'null'){	
		chosenGroupEmpty = true;
	}

	if (!$('#alert').is(':empty')) {
		$("#alert").remove();
	}
	if (!$('#groupMsg').is(':empty')) {
		$("#groupMsg").empty();
	}
	if (!$('#studentMsg').is(':empty')) {
		$("#studentMsg").empty();
	}
	if(chosenGroupEmpty === true){
		document.getElementById("groupMsg").append(
				"Choose group first");
		return false;
	}
	if(!chosenStudentUsername){
		chosenStudentUsernameEmpty = true;
	}
	
	if(chosenStudentUsernameEmpty === true){
		document.getElementById("studentMsg").append(
				"Student is empty");
		return false;
	}
	$.ajax({
		url : './DoesUserExist',
		type : 'GET',
		data : {
			"chosenStudentUsername":chosenStudentUsername
		},
		success : function(response) {
			if (!$('#studentMsg').is(':empty')) {
				$("#studentMsg").empty();
				doesUserExist = true;
			}
			$.ajax({
				url : './IsChosenStudentAlreadyInGroup',
				type : 'GET',
				data : {
					"chosenGroupId" : chosenGroupId,
					"chosenStudentUsername":chosenStudentUsername
				},
				success : function(response) {
					
					if (!$('#studentMsg').is(':empty')) {
						$("#studentMsg").empty();
					}
						chosenStudentUsernameAlreadyInGroup = false;
				},
				error : function(data) {
					if (!$('#studentMsg').is(':empty')) {
						$("#studentMsg").empty();
					}
					chosenStudentUsernameAlreadyInGroup = true;
					document.getElementById("studentMsg").append(
							"Student is already in group");
				}
			});
		},
		error : function(data) {				
			if (!$('#studentMsg').is(':empty')) {
				$("#studentMsg").empty();
			}
	doesUserExist = false;
			document.getElementById("studentMsg").append(
					"Student does not exist");
		}
	});

	$( document ).ajaxStop(function() {
if(chosenStudentUsernameAlreadyInGroup === false && doesUserExist === true){
	
	document.getElementById("addStudentToGroupForm").submit();
}});
});
function areYouSureRemove(e){		 
	if(confirm("Do you really want to do this?") ){
		document.getElementById("removeStudent").submit();
	}else{
return false;
	}
}

$(document).ready(function() {
	
	$('#chosenGroup').change(function(event) {
		
		if(!$('#listOfStudentsOfGroup').is(':empty') ) {
			$( "#listOfStudentsOfGroup" ).empty();
		}
		var groupId = $(this).find(":selected").val();
		$.ajax({
			url : './getAllStudentsOfGroupServlet',
			type : 'GET',
			data : {
				"chosenGroupId" : groupId
			},
			dataType : 'json',
			success : function(response) {
				 var div = document.getElementById("listOfStudentsOfGroup");
					var areYouSureMsg = "Are you sure, that you want to remove the student from this group?";

				for(var i in response){
					$('#listOfStudentsOfGroup').append("<li class='list-group-item'><form action = './RemoveStudentFromGroup' id = 'removeStudent'  method = 'POST'>"+response[i].username+"<input type = 'hidden' name = 'studentId' value = "+response[i].username+"><input type = 'hidden' name = 'chosenGroupId' value = "+groupId+"><button style = 'background:transparent; borders:none; position:relative;float:right' type = 'button' class = 'btn btn-xs'  onclick = 'areYouSureRemove()' ><span  class='badge glyphicon glyphicon-remove'>"+" "+"</span></button></form></li>");
					document.getElementById('listOfStudentsOfGroup').style.visibility = 'visible';
				}
			}
		});
	});
$( function() {
	var availableTags = new Array();
	<c:forEach items="${applicationScope.allStudents}" var="student"> 
		availableTags.push('${student.username}');
	 </c:forEach> 

$( "#searchStudents" ).autocomplete({
  source: availableTags,
  messages: {
      noResults: '',
      results: function() {}
  }
});});
} );

	
</script>
</html>