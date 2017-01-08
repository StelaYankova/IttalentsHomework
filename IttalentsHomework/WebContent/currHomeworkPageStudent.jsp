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
<style>
#textareaComment {
    max-width: 30%;
}
#currTaskSolution {
    max-width: 50%;
}

#image {
	position: absolute;
	left: 850px;
}
#pageContent{
position: absolute;
	left: 50px;
	padding:10px;
   padding-bottom:60px; 

}

</style>
<body>
	
	<%@ include file="navBarStudent.jsp"%>
<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	<div id = "pageContent">
	<br><form style="display:inline" action="./ReadHomeworkServlet" method="GET">
		<input type='hidden'
			value='${sessionScope.currHomework.homeworkDetails.tasksFile}'
			name='fileName'>
		<button class='btn btn-link' style='text-decoration:none' type='submit'><b><c:out value="${sessionScope.currHomework.homeworkDetails.heading }" /></b></button>
	</form><b><u>  - until  <c:out
		value="${sessionScope.currHomework.homeworkDetails.closingTime }" /></u></b>
	<div class="form-group">	
	<br><b>Teacher grade:</b>
	<c:out value="${sessionScope.currHomework.teacherGrade }" />
	<br><br><b>Teacher comment:</b>
	<textarea style="display:inline" disabled="disabled" class="form-control field span12" id = "textareaComment" rows="3"><c:out value="${sessionScope.currHomework.teacherComment }" /></textarea>
	<br><br><br>
	<c:forEach var="i" begin="1"
		end="${sessionScope.currHomework.homeworkDetails.numberOfTasks}">
		<c:if test="${i == 5}"><br><br><br><br><br></c:if>
		<div style = 'float:left' >
		<button class="btn btn-primary btn-sm" style = "color:#fff; background-color:#0086b3" type="submit" onclick="seeTaskSolution('${i}')">
			<c:out value="Task ${i}" />
		</button>
		<form action="./UploadSolutionServlet" method="POST"
			enctype="multipart/form-data">
			<input type="hidden" value="${i}" name = "taskNum">
			<input type="file"
				accept="application/java" size="50" name="file">
			<button class="btn btn-default btn-xs" type="Submit">Upload solution</button>
		</form>
		</div>
	</c:forEach>
		
	
	<div id = "taskUpload" style = "visibility:hidden"></div>
	<textarea id = "currTaskSolution"  disabled="disabled"  style = "visibility:hidden;" class="form-control" cols="150" rows="25">
	</textarea>
	</div>
	</div>
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
				$("#taskUpload").html("<br><br><br><br>Task " + taskNum + " uploaded on: " + response.uploadedOn);
				$("#currTaskSolution").html(response.solution);
				document.getElementById("taskUpload").style.visibility = "visible";
				document.getElementById("currTaskSolution").style.visibility = "visible";
			}
		});
	}
	

	</script>
</body>
</html>