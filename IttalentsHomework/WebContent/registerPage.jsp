<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>

#image{
	position:relative;
	   left: 850px;
	
}
#formRegister{
	position:absolute;
	 left: 60px;
	 top: 220px;
	background-color:#ffffff;
	width:500px;
}
</style>
<body>
	<%@ include file="navBarHomeRegisterPage.jsp"%>
	<div id = "image">
     <img src="logo-black.png" class="img-rounded" width="380" height="236"> 
	</div>
	<div id = "formRegister" align="center">
		<label class="control-label col-sm-16" style = "position:absolute;left:290px;text-decoration: underline;">Registration</label>
		<br><br><br>
		<form class="form-horizontal" action="./RegisterServlet" method="POST">
		<div class="form-group">
				<label class="control-label col-sm-6">Username:</label>
				<div class="col-sm-6">
					<input type="text" class="form-control" 
						placeholder="Enter username" name = "username">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-6">Password:</label>
				<div class="col-sm-6">
					<input type="password" class="form-control"  name = "password"
						placeholder="Enter password">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-6">Password:</label>
				<div class="col-sm-6">
					<input type="password" class="form-control"  name = "repeatedPassword"
						placeholder="Repeat password">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-6">Email:</label>
				<div class="col-sm-6">
					<input type="email" class="form-control" id="email"
						placeholder="Enter email">
				</div>
			</div>
			<!--Username<input type="text" name="username" /> Password<input
				type="password" name="password"> Repeat password<input
				type="password" name="repeatedPassword"> Email<input
				type="text" name="email">  -->
						<br>	<div class="form-group">
				      <div class="col-sm-offset-3 col-sm-2"  style="left:290px">
				
			<button style = "align:right" type="button" class="btn btn-default">Register</button>
			</div>
			</div>
		</form>
	</div>
</body>
</html>