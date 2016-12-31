<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./UpdateYourProfileServlet" method="POST">
		Username
		<c:out value="${sessionScope.user.username}"></c:out>
		Password <input type="password" value="${sessionScope.user.password}"
			name="password" /> Repeat new password<input type="password"
			value="${sessionScope.user.password}" name="repeatedPassword">
		Email<input type="text" value="${sessionScope.user.email}"
			name="email">
		<button type="submit" value="Save"></button>
	</form>
</body>
</html>