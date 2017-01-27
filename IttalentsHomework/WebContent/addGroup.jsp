<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.min.js"></script>

<title>Insert title here</title>
</head>
<style>
#image {
	position: relative;
	left: 850px;
}

#formAddGroup {
	position: absolute;
	left: 60px;
	top: 220px;
	background-color: #ffffff;
	width: 500px;
}
.input-invalid{
	color:red;
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

	<div id="formAddGroup" align="right">
		<form action="./AddGroupServlet" method="POST" id = "addGroupForm">
			<label
				style="position: absolute; left: 290px; text-decoration: underline;">New
				group</label> <br> <br> <br>
			<div class="form-group">
				<label class="control-label col-sm-6">Name</label>
				<div class="col-sm-6">
					<input type="text" name="groupName" class="form-control" placeholder="Enter name" value data-toggle = "popover" data-placement = "bottom" data-trigger = "focus" data-content = "Size of name - 4 to 15 symbols. Valid inputs are numbers and letters (large and small)" required/>
					<p id = "nameMsg" class = "input-invalid"></p>
				</div>
			</div>
			<br><div class="form-group">
					<label class="control-label col-sm-6">Teachers</label>
					<div class="col-sm-6">
						<select class="selectpicker" multiple name = "teachers">
						<c:forEach items="${applicationScope.allTeachers}" var="teacher">
							<option value="${teacher.username}">
								<c:out value="${teacher.username}"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<!-- <div class="multiselect">
					<div class="selectBox" onclick="showCheckboxes()">
						<select required>
							<option>Select an option</option>
						</select>
						<div class="overSelect"></div>
					</div>
					<div id="checkboxes">
						<c:forEach items="${applicationScope.allTeachers}" var="teacher">
							<input type="checkbox" name="teachers"
								value="${teacher.username}" />
							<c:out value="${teacher.username}"></c:out>
						</c:forEach>
					</div>
				</div> -->
			</div>
			<br>
			<br>
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-2" style="left: 290px">

					<input  style="align: right" type="submit" class="btn btn-default" value = "Save">
				</div>
			</div>
		</form>
	</div>
	<script>
	$('#addGroupForm').submit(function(e) {
		e.preventDefault();
		var name = document.forms["addGroupForm"]["groupName"].value;

		var isNameValid = true;
		if(name == ""){
			console.log(1)
			isNameValid = false;
		}
		console.log(name)
		if((isNameValid === false) || (name.length < 4 && name.length > 15)){			console.log(2)

			if (!$('#nameMsg').is(':empty')) {
				$("#nameMsg").empty();
			}
			document.getElementById("nameMsg").append(
					"Invalid symbols or length");
			
			return false;
		}
		$.ajax({

			url : './IsGroupNameUnique',
			type : 'GET',
			data : {
				"name" : name
			},
			success : function(response) {
				if (!$('#nameMsg').is(':empty')) {
					$("#nameMsg").empty();
					isNameValid = true;
				}
				$.ajax({
					url : './IsGroupNameValid',
					type : 'GET',
					data : {
						"name" : name
					},
					success : function(response) {
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
								"name is not valid");
						console.log("invalid name")

					}
				});
				},
			error : function(data) {				
				console.log(5)

				if (!$('#nameMsg').is(':empty')) {
					$("#nameMsg").empty();
				}
				isNameValid = false;
				document.getElementById("nameMsg").append(
						"Group with this name already exists");
				console.log("invalid heading")
			}
		});
		$( document ).ajaxStop(function() {
			
	if((isNameValid === true)){
		console.log(6)
		document.getElementById("addGroupForm").submit();
	}else{
		return false;
	}
	});
	});
   /* var expanded = false;
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
</script>
</body>
</html>