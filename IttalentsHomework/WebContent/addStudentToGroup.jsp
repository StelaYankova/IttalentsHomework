<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>
.ui-helper-hidden-accessible { display:none; }
 ul.ui-autocomplete
    {
        list-style-type: none;
        text-decoration: none;
    }
#image {
	position: relative;
	left: 850px;
}
#pageContent{
	position:absolute;

		top: 120px;
		left: 30px;
		width:60%;
	
}

.form-group{
	width:40%
}
</style>
<body>
	<%@ include file="navBarTeacher.jsp"%>

	<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	<div id="pageContent">

		<div class="ui-widget">
			<form action="./AddStudentToGroupServlet" method="POST"
				class="form-inline">
				<div class="form-group">
					<label class="control-label col-sm-8">Choose group:</label>
						<select id="chosenGroup" name="chosenGroup" class="selectpicker">
							<option value="null">-</option>
							<c:forEach var="group" items="${applicationScope.allGroups}">
								<option value="${group.id}"><c:out
										value="${group.name}"></c:out></option>
							</c:forEach>
						</select>
				</div>
					<div class="form-group">
					<label class="control-label col-sm-8">Choose student:</label>
						<input id="searchStudents" 
							name="selectedStudent" class="form-control">
					&nbsp;&nbsp;<button type="submit" class="btn btn-default btn-lg"><span class = "glyphicon glyphicon-plus">Add</span></button>
				</div>
			</form>
		</div><br><br>
		<ul id="listOfStudentsOfGroup" class="editable list-group" style = "visibility:hidden;z-index :1;height:300px; 
	width:35%;
	overflow:hidden; 
	overflow-y:scroll;overflow-x:scroll;"></ul>
	</div>
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
					$('#listOfStudentsOfGroup').append("<li class='list-group-item'><form action = './RemoveStudentFromGroup' method = 'POST'>"+response[i].username+"<input type = 'hidden' name = 'studentId' value = "+response[i].username+"><input type = 'hidden' name = 'chosenGroupId' value = "+groupId+"><button style = 'background:transparent; borders:none; position:relative;float:right' class = 'btn btn-xs' type = 'submit'><span  class='badge glyphicon glyphicon-remove'>"+" "+"</span></button></form></li>");
					document.getElementById('listOfStudentsOfGroup').style.visibility = 'visible';
				}
			}
		});
	});
$( function() {
	var availableTags = new Array();
	<c:forEach items="${applicationScope.allStudents}" var="student"> 
		availableTags.push('${student.username}');
	 </c:forEach> 

$( "#searchStudents" ).autocomplete({
  source: availableTags,
  messages: {
      noResults: '',
      results: function() {}
  }
});});
} );

	
</script>
</html>