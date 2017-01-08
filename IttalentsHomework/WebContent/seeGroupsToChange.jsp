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
#pageContent{
	position:absolute;
	left: 20px;
	top:100px;
}
#createButtonPosition{
	position:absolute;
	left: 0px;
	top:7px;
}
</style>
<body>
<%@ include file="navBarTeacher.jsp"%>
<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	<div id = "pageContent">
	<label
				style="position: absolute; left: 290px; text-decoration: underline;">All groups</label> <br><br>
<form action = "./AddGroupServlet" method = "GET" ><button id = "createButtonPosition" class = "glyphicon glyphicon-plus btn-primary btn btn-xs" type = "submit">Create group</button></form>
	<table id = "resultTable" border = "1" class="table table-striped table-bordered table-hover" style = "width:60%">
		<thead>
		<tr>
			<th>Name</th>
			<th></th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="group" items="${applicationScope.allGroups}">
			<tr>
				<td><form action = "./UpdateGroupServlet" method = "POST"><input type = "hidden" name = "groupId" value = "${group.id}"><input type = "text" value = "${group.name}" name = "groupName"><button class="glyphicon glyphicon-check" type = "submit"></button></form>
				<td><form action = "./RemoveGroupServlet" method = "POST"><input type = "hidden" name = "groupId" value = "${group.id}"><button type = "submit" class = "glyphicon glyphicon-remove btn btn-default btn-sm"></button></form></td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
</body>
<script>
$( document ).ready(function() {
	var table = $('#resultTable').DataTable({
		"aoColumnDefs" : [ {
			'bSortable' : false,
			'aTargets' : [0,1],
			'className': "wrapword", "targets": [0]
			
		} ],
	      "dom":'<"top"l>rt<"bottom"ip><"clear">',
		 "aoColumns": [
		               { sWidth: '14%' },
		               { sWidth: '12%' }],
		"lengthMenu" : [5],
	});
});
</script>
</html>