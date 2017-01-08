<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<title>Insert title here</title>
</head>
<style>
#image {
	position: relative;
	left: 850px;
}
</style>
<body>
	<!--<%@ include file="navBarTeacher.jsp"%>-->

	<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	Choose group:

	<div class="ui-widget">
		<form action="./AddStudentToGroupServlet" method="POST">
			<select id="chosenGroup" name="chosenGroup">
				<option value="null">-</option>
				<c:forEach var="group" items="${applicationScope.allGroups}">
					<option value="${group.id}"><c:out value="${group.name}"></c:out></option>
				</c:forEach>
			</select> <label for="tags">Tags: </label> <input id="tags"
				name="selectedStudent">
			<button type="submit">Add</button>
		</form>
	</div>
	<div id="listOfStudentsOfGroup"></div>
</body>
<script>
$(document).ready(function() {
	
	$('#chosenGroup').change(function(event) {
		
		if(!$('#listOfStudentsOfGroup').is(':empty') ) {
			$( "#listOfStudentsOfGroup" ).empty();
		}
		var groupId = $(this).find(":selected").val();
		$.ajax({
			url : './getAllStudentsOfGroupServlet',
			type : 'GET',
			data : {
				"chosenGroupId" : groupId
			},
			dataType : 'json',
			success : function(response) {
				 var div = document.getElementById("listOfStudentsOfGroup");
				for(var i in response){
					$('#listOfStudentsOfGroup').append("<br><form action = './RemoveStudentFromGroup' method = 'POST'>"+response[i].username+"<input type = 'hidden' name = 'studentId' value = "+response[i].username+"><input type = 'hidden' name = 'chosenGroupId' value = "+groupId+"><button type = 'submit'>Remove</button></form>");
				}
			}
		});
	});
});
$( function() {
	var availableTags = new Array();
	<c:forEach items="${applicationScope.allStudents}" var="student"> 
		availableTags.push('${student.username}');
	 </c:forEach> 
$( "#tags" ).autocomplete({
  source: availableTags
});
} );
</script>
</html>