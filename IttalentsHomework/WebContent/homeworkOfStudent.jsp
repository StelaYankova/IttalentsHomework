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
    max-width: 40%;
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
   padding-bottom:75px; 

}
label {
  display:inline-block;
    *display: inline;     /* for IE7*/
    zoom:1;              /* for IE7*/
    float: left;
    padding-top: 5px;
    text-align: left;
    width: 140px;
}​
</style>
<body>
	<%@ include file="navBarTeacher.jsp"%>
	<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	<div id="pageContent">

		<br>
		<form style="display: inline" action="./ReadHomeworkServlet"
			method="GET">
			<input type='hidden'
				value='${sessionScope.currHomework.homeworkDetails.tasksFile}'
				name='fileName'>
			<button class='btn btn-link' style='text-decoration: none'
				type='submit'>
				<b><c:out
						value="${sessionScope.currHomework.homeworkDetails.heading }" /></b>
			</button>
		</form>
		<b><u> - until <c:out
					value="${sessionScope.currHomework.homeworkDetails.closingTime }" /></u></b>


		<br><br><br>
		<form action="./UpdateTeacherGradeAndCommentServlet" method="POST">
			<div class="block">
				<label><b>Teacher grade:</b></label>
				<div class="col-xs-2">
					<input type="number" class="form-control" min=0
						value="${sessionScope.currHomework.teacherGrade }" name="grade" />
				</div>
			</div>
			<br> <br>
			<div class="block">
				<br><label><b>Teacher comment:</b></label>&nbsp;
				<textarea class="form-control" id="textareaComment" rows="3"
					name="comment" value="${sessionScope.currHomework.teacherComment}"><c:out
						value="${sessionScope.currHomework.teacherComment}"></c:out></textarea>
			</div>

			<div class="col-sm-offset-3 col-sm-2" style="left: 200px">
				<button style="align: right" type="submit" class="btn btn-default">Save</button>
			</div> 
		</form>
		<br><br><br>
			<c:forEach var="i" begin="1"
				end="${sessionScope.currHomework.homeworkDetails.numberOfTasks}">
				<c:if test="${i == 5}">
					<br>
					<br>
					
				</c:if>
				<div style='float: left'>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
					<button type="submit" onclick="seeTaskSolution('${i}')"
						class="btn btn-primary btn-sm"
						style="color: #fff; background-color: #0086b3">
						<c:out value="Task ${i}" ></c:out>
					</button>
				</div>
			</c:forEach>
	<br>
			<br>
			<br>
			<div id="taskUpload" style = "visibility:hidden"></div>
			<br>
			<textarea id="currTaskSolution" disabled="disabled" style = "visibility:hidden"  class="form-control" cols="150" rows="25">
	</textarea>
		</div>
	<!--  <form action="./ReadHomeworkServlet" method="GET">
		<input type='hidden'
			value='${sessionScope.currHomework.homeworkDetails.tasksFile}'
			name='fileName'>
		<button type='submit'>download homework here</button>
	</form>
	<c:out value="${sessionScope.currHomework.homeworkDetails.heading }" />
	- until
	<c:out
		value="${sessionScope.currHomework.homeworkDetails.closingTime }" />-->
		
		
		<!--  <br>Teacher grade:
	<c:out value="${sessionScope.currHomework.teacherGrade }" />
	<br>Teacher comment:
	<c:out value="${sessionScope.currHomework.teacherComment }" />-->
	<script>
	
	function seeTaskSolution(taskNum){
		$.ajax({
			url : './ReadJavaFileServlet',
			data : {
				"taskNum" : taskNum,
			},
			type : 'GET',
			dataType: 'json',
			success : function(response) {
				console.log(response)
				$("#taskUpload").html("Task " + taskNum + " uploaded on: " + response.uploadedOn);
				$("#currTaskSolution").html(response.solution);
				document.getElementById("taskUpload").style.visibility = "visible";
				document.getElementById("currTaskSolution").style.visibility = "visible";
			}
		});
	}
	

	</script>
</body>
</html>