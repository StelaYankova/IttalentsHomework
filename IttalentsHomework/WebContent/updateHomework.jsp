<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
            <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
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
<form action="./UpdateHomeworkServlet" method="POST"
		enctype="multipart/form-data">
		<br>Heading<input type="text" name="name" value = '${sessionScope.currHomework.heading}'/> 
		<br>Opening time<input type='datetime-local' name="opens" data-val="true" value = '${sessionScope.currHomework.openingTime}'> <br>Closing time<input
			type='datetime-local' name="closes" data-val="true" value = '${sessionScope.currHomework.closingTime}'> <br>Number of tasks<input
			type="number" min="0" name="numberOfTasks" value = '${sessionScope.currHomework.numberOfTasks}'/> 
			<br>Tasks<input type="file"
				accept="application/pdf" name="file">
		<div class="multiselect">
			<div class="selectBox" onclick="showCheckboxes()">
				<select>
					<option>Select an option</option>
				</select>
				<div class="overSelect"></div>
			</div>
			<div id="checkboxes" >
				<c:forEach items="${applicationScope.allGroups}" var="group">
					<c:set var="isHwInGroup"
						value="false"></c:set>
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