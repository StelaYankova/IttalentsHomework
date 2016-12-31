<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--  <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>
.multiselect {
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
}
</style>
<body>
	New homework
	<form action="./AddHomework" method="POST"
		enctype="multipart/form-data">
		<br>Name<input type="text" name="name" /> 
		<br>Opening time<input type="datetime-local" name="opens" > <br>Closing time<input
			type="datetime-local" name="closes" > <br>Number of tasks<input
			type="number" min="0" name="numberOfTasks" /> <br>Tasks<input
			type="file" accept="application/pdf" name="tasks" size="50" />
		<div class="multiselect">
			<div class="selectBox" onclick="showCheckboxes()">
				<select>
					<option>Select an option</option>
				</select>
				<div class="overSelect"></div>
			</div>
			<div id="checkboxes" >
				<c:forEach items="${applicationScope.allGroups}" var="group">
					<input type="checkbox" name = "groups" id="${group.id}" />
					<c:out value="${group.name}"></c:out>
				</c:forEach>
			</div>
		</div>
		<button type = "submit">Save</button>
	</form>
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
-->