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
<select id =  "chosenGroup">
 <option value="null">-</option>
 <!--  <option value="allGroups">All Groups</option>-->

<c:forEach var="group" items="${applicationScope.allGroups}" >
	<option value = "${group.id}"><c:out value="${group.name}"></c:out></option>
</c:forEach>
</select>

	<table id="resultTable" border = "1">
		<thead>
			<tr>
				<td>Heading</td>
				<td>Opens</td>
				<td>Closes</td>
			</tr>
		</thead>
		<tbody>

		</tbody>
	</table>
	<div id = "listOfStudentsOfGroup"></div>
	<script>
//$(document).ready(function() {
	$('#chosenGroup').change(function(event) {
		if(!$('#resultTable tbody').is(':empty') ) {
			$( "#resultTable tbody" ).empty();
		}
		
		var groupId = $(this).find(":selected").val();
		$.ajax({
			url:'./seeHomeworksOfGroupServlet',
			type:'GET',
			data:{
				"chosenGroup": groupId
			},
		dataType:'json',
		success: function(response){
			for(var i in response){
				
			var row = $("<tr>");
				//row.append($("<td><form action = './GetAllStudentsOfGroupServlet' method = 'GET'><input type = 'hidden' name = 'hdId' value = " + response[i].id+ "><input type = 'hidden' name = 'chosenGroupId' value = " +groupId+ "><button type = 'submit'>" + response[i].heading +"</button></form></td>"))
			    row.append($("<td><button type = 'submit' onclick = 'chooseStudent(" + response[i].id +"," + groupId +")'>" +response[i].heading + "</button></td>")) 
				.append($("<td>"+ response[i].opens+"</td>"))
			     .append($("<td>" +response[i].closes+"</td>"))
			  $("#resultTable").append(row);
			}
		}
	});
});
	function chooseStudent(homeworkId, groupId){
		if(!$('#listOfStudentsOfGroup').is(':empty') ) {
			$( "#listOfStudentsOfGroup").empty();
		}
		$.ajax({
			url:'./getAllStudentsOfGroupServlet',
			type:'GET',
			data:{
				"chosenGroupId": groupId
			},
		dataType:'json',
		success: function(response){
			 var div = document.getElementById("listOfStudentsOfGroup");
				for(var i in response){
					$('#listOfStudentsOfGroup').append("<form action = './GetHomeworkOfStudentServlet'><input type = 'hidden' name = 'id' value = "+homeworkId +"><input type = 'hidden' name = 'studentId' value = "+response[i].id+"><button type = 'submit'>"+response[i].username + "</button></form>");
				}
			}
			});
	};
//});
</script>
</body>
</html>