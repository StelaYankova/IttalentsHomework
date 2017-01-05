<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
Choose group:
<select id = "chosenGroup">
 <option value="null">-</option>
 <c:if test="${sessionScope.isTeacher == false}">
<c:forEach var="group" items="${user.groups}" >
	 <option value="${group.id}"><c:out value="${group.name}"></c:out></option>
 </c:forEach>
 </c:if>
  <c:if test="${sessionScope.isTeacher == true}">
<c:forEach var="group" items="${applicationScope.allGroups}" >
	 <option value="${group.id}"><c:out value="${group.name}"></c:out></option>
 </c:forEach>
 </c:if>
</select>
<div id = "listOfStudentsOfGroup">
</div>
<div >
		<table id="resultTable" border="1">
		<thead>
			<tr>
				<td>Heading</td>
				<td>Opens</td>
				<td>Closes</td>
				<td>Teacher score</td>
				<td>Teacher comment</td>
			</tr>
			</thead>
			<tbody >
			</tbody>
		</table>
	</div>
<script>
$(document).ready(function() {
	
	$('#chosenGroup').change(function(event) {
		if(!$('#resultTable tbody').is(':empty') ) {
			$( "#resultTable tbody" ).empty();
		}
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
					$('#listOfStudentsOfGroup').append("<button onclick = 'seeHomeworks(" + groupId+ ","+ response[i].id+ ")'>" + response[i].username + "</button>");
				}
			}
		});
	});
});
function seeHomeworks(e,e1){
	if(!$('#resultTable tbody').is(':empty') ) {
		$( "#resultTable tbody" ).empty();
	}
var groupId = e;
var studentId = e1;
$.ajax({
	url:'./SeeAllHomeworksOfStudentByGroupServlet',
	type: 'GET',
	data:{
		"groupId": groupId,
		"studentId": studentId
	},
	dataType:'json',
	success: function(response){
		for(var i in response){
		var row = $("<tr>");
			row.append($("<td><form action = './GetHomeworkOfStudentServlet' method = 'GET'><input type = 'hidden' name = 'id' value = " + response[i].id+ "><input type = 'hidden' name = 'studentId' value = "+studentId+"><button type = 'submit'>" + response[i].heading +"</button></form></td>"))
		     .append($("<td>"+ response[i].opens+"</td>"))
		     .append($("<td>" +response[i].closes+"</td>"))
		     .append($("<td>"+ response[i].teacherScore+"/100</td>"))
		     .append($("<td>" +response[i].teacherComment+"</td>"));
		     
		 
		  $("#resultTable").append(row);
		}
	}
	
});
	
}

</script>
</body>
</html>