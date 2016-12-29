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
	<br>
	<c:out value="${sessionScope.currHomework.homeworkDetails.heading }" />
	- until
	<c:out
		value="${sessionScope.currHomework.homeworkDetails.closingTime }" />
	<br>Teacher grade:
	<c:out value="${sessionScope.currHomework.teacherGrade }" />
	<br>Teacher comment:
	<c:out value="${sessionScope.currHomework.teacherComment }" />
	<c:forEach var="i" begin="1"
		end="${sessionScope.currHomework.homeworkDetails.numberOfTasks}">
		<br>
		<button type="submit" onclick="seeTaskSolution('${i}')">
			<c:out value="Task ${i}" />
		</button>
		<form action="./UploadSolutionServlet" method="POST"
			enctype="multipart/form-data">
			<input type="hidden" value="${i}" name = "taskNum">
			<input type="file"
				accept="application/java" size="50" name="file">
			<button type="Submit">Upload solution</button>
		</form>
	</c:forEach>
	
	<br><br>
	<div id = "taskUpload"></div>
	<br>
	<textarea id = "currTaskSolution" rows="30" cols="70">
	</textarea>
	<script>
	
	function seeTaskSolution(taskNum){
		$.ajax({
			url : './ReadJavaFileServlet',
			data : {
				"taskNum" : taskNum
			},
			type : 'GET',
			dataType: 'json',
			success : function(response) {
				console.log(response)
				$("#taskUpload").html("Task " + taskNum + " uploaded on: " + response.uploadedOn);
				$("#currTaskSolution").html(response.solution);
			}
		});
	}
	

	</script>
</body>
</html>