<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>
</head>
<style>
#image {
	position: relative;
	left: 850px;
}

#formRegister {
	position: absolute;
	left: 60px;
	top: 220px;
	background-color: #ffffff;
	width: 500px;
}
.input-invalid{
	color:red;
}
/*#invalidData{
	position:absolute;
	top: 160px;
	left:20px;
	color:red;
}*/
</style>
<body>
	<%@ include file="navBarHomeRegisterPage.jsp"%>
	<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	<!-- <p id = "invalidData"><c:if test="${requestScope.invalidData == 1}">Invalid data input</c:if></p> -->
	<div id="formRegister" align="center">
		<label class="control-label col-sm-16"
			style="position: absolute; left: 290px; text-decoration: underline;">Registration</label>
		<br>
		<br>
		<br>
		<form class="form-horizontal" name = "registerForm" id = "registerForm" action="./RegisterServlet" method="POST">
			<div class="form-group">
				<label for="username" class="control-label col-sm-6">Username:</label>
				<div class="col-sm-6">
					<input type="text" class="form-control"
						placeholder="Enter username" name="username"  value data-toggle = "popover" data-placement = "bottom" data-trigger = "focus" data-content = "Size of username - 6 to 15 symbols. Valid inputs are numbers and letters (large and small)" required/>
					<p id = "usernameMsg" class = "input-invalid"></p>
				</div>
			</div>
			<div class="form-group">
				<label for="password" class="control-label col-sm-6">Password:</label>
				<div class="col-sm-6">
					<input type="password" class="form-control" name="password"
						placeholder="Enter password"  value data-toggle = "popover" data-placement = "bottom" data-trigger = "focus" data-content = "Size of password - 6 to 15 symbols. Valid inputs are numbers and letters (large and small)" required/>
					<p id = "passwordMsg" class = "input-invalid"></p>
				</div>
			</div>
			<div class="form-group">
				<label for="repeatedPassword" class="control-label col-sm-6">Password:</label>
				<div class="col-sm-6">
					<input type="password" class="form-control" name="repeatedPassword"
						placeholder="Repeat password" required>
					<p id = "repeatedPasswordMsg" class = "input-invalid"></p>
				</div>
			</div>
			<div class="form-group">
				<label for="email" class="control-label col-sm-6">Email:</label>
				<div class="col-sm-6">
					<input type="email" class="form-control" name = "email" id="email"
						placeholder="Enter email" required>
						<p id = "emailMsg" class = "input-invalid"></p>
				</div>	
			</div>
			<br>
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-2" style="left: 290px">

					<input  style="align: right" type="submit" class="btn btn-default" value = "Register">
				</div>
			</div>
		</form>
	</div>
</body>
<script>

function checkIsEmailValid() {
	var email = document.forms["registerForm"]["email"].value;

	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

	if (!filter.test(email)) {
		return false;
	}
	return true;

}
$('#registerForm')
.submit(
		function(e) {
			e.preventDefault();
		console.log(23)
		var username = document.forms["registerForm"]["username"].value;
		var password = document.forms["registerForm"]["password"].value;
		var repeatedPassword = document.forms["registerForm"]["repeatedPassword"].value;
		var email = document.forms["registerForm"]["email"].value;

		if (!$('#usernameMsg').is(':empty')) {
			$("#usernameMsg").empty();
		}
		if (!$('#passwordMsg').is(':empty')) {
			$("#passwordMsg").empty();
		}
		if (!$('#emailMsg').is(':empty')) {
			$("#emailMsg").empty();
		}

		if (!$('#repeatedPasswordMsg').is(':empty')) {
			$("#repeatedPasswordMsg").empty();
		}
		var isUsernameUnique = true;
		var isUsernameValid = true;
		var isPasswordValid = true;
		var isRepeatedPasswordValid = true;
		var isEmailValid = true;
		
		if (username == "") {
		//	document.getElementById("usernameMsg").append("Fill username");
			isUsernameValid = false;

		}

		if (password == "") {
			
		//	document.getElementById("passwordMsg").append("Fill password");
			isPasswordValid = false;
		}
		if (email == "") {
		//	document.getElementById("emailMsg").append("Fill email");
			isEmailValid = false;
		}
		
		if(!(isUsernameValid === true && isPasswordValid === true && isRepeatedPasswordValid === true && isEmailValid === true)){
			return false;
		}
		if(password !== repeatedPassword){
			document.getElementById("repeatedPasswordMsg").append(
					"Repeated is not valid");
			isRepeatedPasswordValid = false;

		}else{
			isRepeatedPasswordValid = true;
			
		}
		
		
		
		

		$.ajax({
			url : './IsUsernameUniqueServlet',
			type : 'GET',
			data : {
				"username" : username
			},
			success : function(response) {
				if (!$('#usernameMsg').is(':empty')) {
					$("#usernameMsg").empty();
					isUsernameUnique = true;
				}
				$.ajax({
					url : './IsUsernameValid',
					type : 'GET',
					data : {
						"username" : username
					},
					success : function(response) {
						if (!$('#usernameMsg').is(':empty')) {
							$("#usernameMsg").empty();
							isUsernameValid = true;
						}
					},
					error : function(data) {
						if (!$('#usernameMsg').is(':empty')) {
							$("#usernameMsg").empty();
						}
						isUsernameValid = false;
						document.getElementById("usernameMsg").append(
								"Username is not valid");
						console.log("invalid username")

					}
				});
			},
			error : function(data) {
				if (!$('#usernameMsg').is(':empty')) {
					$("#usernameMsg").empty();
				}
				isUsernameUnique = false;

				document.getElementById("usernameMsg").append(
						"Username is already taken");
			}
		});

		$.ajax({
			url : './IsPasswordValid',
			type : 'GET',
			data : {
				"password" : password
			},
			success : function(response) {
				if (!$('#passwordMsg').is(':empty')) {
					$("#passwordMsg").empty();
					isPasswordValid = true;
				}
			},
			error : function(data) {
				if (!$('#passwordMsg').is(':empty')) {
					$("#passwordMsg").empty();
				}
				isPasswordValid = false;
				document.getElementById("passwordMsg").append(
						"Password is not valid");
				console.log("invalid pass")
			}
		});
		
		isEmailValid = checkIsEmailValid();
		if (!isEmailValid) {
			if (!$('#emailMsg').is(':empty')) {
				$("#emailMsg").empty();
			}
			document.getElementById("emailMsg").append("Email is not valid");

		}else{
			if (!$('#emailMsg').is(':empty')) {
				$("#emailMsg").empty();
			}
		}
		
		
		if((isUsernameValid === true && isUsernameUnique === true && isPasswordValid === true && isRepeatedPasswordValid === true && isEmailValid === true)){
			 document.getElementById("registerForm").submit();
		}
	});
	
	$(document).ready(function(){
	    $('[data-toggle="popover"]').popover();   
	});
</script>
</html>