<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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

#formUpdateProfile {
	position: absolute;
	left: 60px;
	top: 220px;
	background-color: #ffffff;
	width: 500px;
}
</style>
<body>
<c:if test="${sessionScope.isTeacher == false}"><%@ include file="navBarStudent.jsp"%></c:if>
<c:if test="${sessionScope.isTeacher == true}"><%@ include file="navBarTeacher.jsp"%></c:if>
	
	
	<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	<div id="formUpdateProfile" align="center">
		<label
			style="position: absolute; left: 290px; text-decoration: underline;">Update
			profile</label> <br> <br> <br>
		<form action="./UpdateYourProfileServlet" method="POST"
			class="form-horizontal">
			<div class="form-group">
				<label class="control-label col-sm-6">Username</label>
				<div class="col-sm-6">
					<c:out value="${sessionScope.user.username}"></c:out>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-6">Password</label>
				<div class="col-sm-6">
					<input type="password" class="form-control"
						value="${sessionScope.user.password}" name="password" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-6">Repeat new password</label>
				<div class="col-sm-6">
					<input type="password" class="form-control"
						value="${sessionScope.user.password}" name="repeatedPassword">
				</div>
			</div>
			
			<div class="form-group">
				<label class="control-label col-sm-6">Email</label>
				<div class="col-sm-6">
					<input type="text" class="form-control" value="${sessionScope.user.email}" name="email">
				</div>
			</div>
			<br>
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-2" style="left: 290px">

					<button style="align: right" type="button" class="btn btn-default">Save</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>