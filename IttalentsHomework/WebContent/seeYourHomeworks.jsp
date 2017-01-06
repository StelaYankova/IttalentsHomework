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
.wrapword {
	white-space: -moz-pre-wrap !important; /* Mozilla, since 1999 */
	white-space: -webkit-pre-wrap; /*Chrome & Safari */
	white-space: -pre-wrap; /* Opera 4-6 */
	white-space: -o-pre-wrap; /* Opera 7 */
	white-space: pre-wrap; /* css-3 */
	word-wrap: break-word; /* Internet Explorer 5.5+ */
	word-break: break-all;
	white-space: normal;
}
#image{
	position:absolute;
	   left: 850px;
	
}
</style>
<body>
	<%@ include file="navBarStudent.jsp"%>
<div id = "image">
     <img src="logo-black.png" class="img-rounded" width="380" height="236"> 
	</div>
	<br>
	<table border = "1">
		<thead>
			<tr>
				<td class="wrapword">Heading</td>
				<td class="wrapword">Days left</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="homework" items = "${sessionScope.currHomeworksOfGroup}">
  				<tr>
  				<td class="wrapword"><form action = "./GetHomeworkServlet" method = "GET"><input type = "hidden" name = "id" value = '${homework.id}'><button type = "submit"><c:out value="${homework.heading}"/></button></form>
  					</td>
  					<td class="wrapword"><c:out value="${homework.daysLeft}"/></td>
  				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>