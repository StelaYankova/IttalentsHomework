<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
            <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>


<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<title>Insert title here</title>
</head>
<style>
#image {
	position: relative;
	left: 850px;
}

#formUpdate {
	position: absolute;
	left: 60px;
	top: 220px;
	background-color: #ffffff;
	width: 500px;
}

.multiselect {
	width: 200px;
}
/*.selectBox {
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
	<div id="formUpdate" align="right">

		<form action="./UpdateHomeworkServlet" method="POST"
			enctype="multipart/form-data">
			<label
				style="position: absolute; left: 290px; text-decoration: underline;">Update
				homework</label> <br> <br> <br>
			<div class="form-group">
				 <label class="control-label col-sm-6">Heading</label>
				<div class="col-sm-6">
					<input type="text" class="form-control" name="name"
						value='${sessionScope.currHomework.heading}' />
				</div>
			</div>
			<div class="form-group">
				<br> <label class="control-label col-sm-6">Opening time</label>
				<div class="col-sm-6">
					<input type='datetime-local' class="form-control" name="opens"
						data-val="true" value='${sessionScope.currHomework.openingTime}'>
				</div>
			</div>
			<div class="form-group">
				<br> <label class="control-label col-sm-6">Closing time</label>
				<div class="col-sm-6">
					<input type='datetime-local' class="form-control" name="closes"
						data-val="true" value='${sessionScope.currHomework.closingTime}'>
				</div>
			</div>
			<br>
			<div class="form-group">
				<label class="control-label col-sm-6">Number of tasks</label>
				<div class="col-sm-6">
					<input type="number" min="0" class="form-control"
						name="numberOfTasks"
						value='${sessionScope.currHomework.numberOfTasks}' />
				</div>
			</div>
			<br>
			<div class="form-group">
				<label class="control-label col-sm-6">Tasks</label>
				<div class="col-sm-6">
					<input type="file" accept="application/pdf" name="file">
				</div>
			</div>
			<br>

			<div class="form-group">
					<label class="control-label col-sm-6">Groups</label>
					<div class="col-sm-6">

						<select class="selectpicker" multiple name="groups">
							<c:forEach items="${applicationScope.allGroups}" var="group">
								<c:set var="isHwInGroup" value="false"></c:set>
								<c:forEach items="${group.homeworks}" var="homework">
									<c:if test="${homework.id==sessionScope.currHomework.id}">
										<c:set var="isHwInGroup" value="true"></c:set>
									</c:if>
								</c:forEach>
								<c:if test="${isHwInGroup}">
									<option value="${group.id}" selected>
										<c:out value="${group.name}"></c:out></option>
								</c:if>
								<c:if test="${not isHwInGroup}">
									<option value="${group.id}">
										<c:out value="${group.name}"></c:out></option>
								</c:if>
							</c:forEach>
						</select>
					</div>
				</div>

			<br><br>
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-2" style="left: 290px">

					<button style="align: right" type="button" class="btn btn-default">Save</button>
				</div>
			</div>
		</form>
	</div>
	<!--  <div class="multiselect">
				<div class="selectBox" onclick="showCheckboxes()">
					<select>
						<option>Select an option</option>
					</select>
					<div class="overSelect"></div>
				</div>
				<div id="checkboxes">
					<c:forEach items="${applicationScope.allGroups}" var="group">
						<c:set var="isHwInGroup" value="false"></c:set>
						<c:forEach items="${group.homeworks}" var="homework">
							<c:if test="${homework.id==sessionScope.currHomework.id}">
								<c:set var="isHwInGroup" value="true"></c:set>
							</c:if>
						</c:forEach>
						<c:if test="${isHwInGroup}">
							<input type="checkbox" name="groups" value="${group.id}" checked>
							<c:out value="${group.name}"></c:out>
						</c:if>
						<c:if test="${not isHwInGroup}">
							<input type="checkbox" name="groups" value="${group.id}">
							<c:out value="${group.name}"></c:out>
						</c:if>

					</c:forEach>
				</div>
			</div>-->
	<script>
	
		var expanded = false;
		function showCheckboxes() {
			var checkboxes = document.getElementById("checkboxes");
			if (!expanded) {
				checkboxes.style.display = "block";
				expanded = true;
			} else {
				checkboxes.style.display = "none";
				expanded = false;
			}
		}
	</script>
</body>
</html>