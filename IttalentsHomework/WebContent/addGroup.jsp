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
New group
<form action = "./AddGroupServlet" method = "POST">
<input type = "text" name = "groupName">
<div class="multiselect">
			<div class="selectBox" onclick="showCheckboxes()">
				<select required>
					<option>Select an option</option>
				</select>
				<div class="overSelect"></div>
			</div>
			<div id="checkboxes" >
				<c:forEach items="${applicationScope.allTeachers}" var="teacher">
					<input type="checkbox" name = "teachers" value="${teacher.username}" />
					<c:out value="${teacher.username}"></c:out>
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