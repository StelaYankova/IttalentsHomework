<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
All groups<!-- name , teachers -->
<form action = "./AddGroupServlet" method = "GET"><button type = "submit">Add group</button></form>
	<table>
		<tr>
			<th>Name</th>
		</tr>
		<c:forEach var="group" items="${applicationScope.allGroups}">
			<tr>
				<td><form action = "./UpdateGroupServlet" method = "POST"><input type = "hidden" name = "groupId" value = "${group.id}"><input type = "text" value = "${group.name}" name = "groupName"><button type = "submit">Save new name</button></form>
				<td><form action = "./RemoveGroupServlet" method = "POST"><input type = "hidden" name = "groupId" value = "${group.id}"><button type = "submit">Remove group</button></form></td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>