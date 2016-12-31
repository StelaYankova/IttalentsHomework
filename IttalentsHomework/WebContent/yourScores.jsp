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
Choose a group:
	<select id = "selectGroup">
		<option value="null">group</option>
		<option value="allGroups">All groups</option>
		<c:forEach items="${sessionScope.user.groups}" var="group">
			<option value="${group.id}">"${group.name}"</option>
		</c:forEach>
	</select>
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
		$('#selectGroup').on('change', function() {
			$('#resultTable tbody').html('');

			$.ajax({
				url : './SeeYourHomeworksByGroup',
				data : {
					"selectedGroupId" : $(this).find(":selected").val()
				},

				type : 'GET',
				dataType : 'json',
				success : function(response) {
					console.log(response)
					for ( var i in response) {
						console.log(response[i].heading)
						var row = $("<tr>");

						  row.append($("<td><form action = './GetHomeworkServlet' method = 'GET'><input type = 'hidden' name = 'id' value = " + response[i].id+ "><button type = 'submit'>" + response[i].heading +"</button></form></td>"))
						     .append($("<td>"+ response[i].opens+"</td>"))
						     .append($("<td>" +response[i].closes+"</td>"))
						     .append($("<td>"+ response[i].teacherScore+"/100</td>"))
						     .append($("<td>" +response[i].teacherComment+"</td>"));
						     
						 
						  $("#resultTable").append(row);
						
					}
				}
			});
		});
	</script>
</body>
</html>