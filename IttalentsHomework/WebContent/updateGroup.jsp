<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
		<form action="./UpdateGroupServlet" method="POST">
			<label
				style="position: absolute; left: 290px; text-decoration: underline;">Update
				group</label> <br> <br> <br>
			<div class="form-group">
				<label class="control-label col-sm-6">Name</label>
				<div class="col-sm-6">
					<input type="text" name="groupName" class="form-control" value = "${sessionScope.currGroup.name}">
				</div>
			</div>
			<br><div class="form-group">
					<label class="control-label col-sm-6">Teachers</label>
					<div class="col-sm-6">
						<select class="selectpicker" multiple name = "teachers">
						<!--<c:forEach items="${applicationScope.allTeachers}" var="teacher">
							<option value="${teacher.username}">
								<c:out value="${teacher.username}"></c:out></option>
						</c:forEach>-->
						<c:forEach items="${applicationScope.allTeachers}" var="teacher">
								<c:set var="isTeacherInGroup" value="false"></c:set>
								<c:forEach items="${teacher.groups}" var="group">
									<c:if test="${group.id==sessionScope.currGroup.id}">
										<c:set var="isTeacherInGroup" value="true"></c:set>
									</c:if>
								</c:forEach>
								<c:if test="${isTeacherInGroup}">
									<option value="${teacher.username}" selected>
										<c:out value="${teacher.username}"></c:out></option>
								</c:if>
								<c:if test="${not isTeacherInGroup}">
									<option value="${teacher.username}">
										<c:out value="${teacher.username}"></c:out></option>
								</c:if>
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

					<button style="align: right" type="submit" class="btn btn-default">Save</button>
				</div>
			</div>
		</form>
	</div>
	<script>
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