<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action = "./RegisterServlet" method = "POST">
	Username<input type = "text" name = "username"/>
	Password<input type = "password" name = "password">
	Repeat password<input type = "password" name = "repeatedPassword">
	Email<input type = "text" name = "email">
	<button type = "submit">Submit</button>
</form>
</body>
</html>