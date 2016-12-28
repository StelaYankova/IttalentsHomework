<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="//cdn.datatables.net/1.10.12/css/jquery.dataTables.css">
<script type="text/javascript" charset="utf8"
	src="//cdn.datatables.net/1.10.12/js/jquery.dataTables.js"></script>
</head>
<style>
.navbar {
	background-color: #2E71AC;
	border-color: #2e6da4;
}

#signInField {
	top: 100px;
	right: 0;
	width: 700px;
}
</style>
<body>
	<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="#"> ITTalents Homework System</a>
		</div>
		<ul class="nav navbar-nav navbar-right">
			<li>
				<div class="container" id="signInField">
					<form class="form-inline" action="./LoginServlet" method="POST">
						<div class="form-group">
							<label>Username:</label> <input type="text" class=" form-control"
								name="username">
						</div>
						<div class="form-group">
							<label>Password:</label> <input type="password"
								class="form-control " name="password">
						</div>
						<button type="submit" class=" btn-m btn-default">Sign in</button>
					</form>
				</div>
			</li>
			<li><a href="registerPage.jsp"><span class="glyphicon glyphicon-log-in"></span>Register</a></li>
		</ul>
	</div>
	</nav>

</body>
</html>