<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>


<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>
#image {
	position: relative;
	left: 850px;
}

#formAddHomework {
	position: absolute;
	left: 60px;
	top: 220px;
	background-color: #ffffff;
	width: 500px;
}
/*.multiselect {
	width: 200px;
}

.selectBox {
	position: relative;
}

.selectBox select {
	width: 100%;
	font-weight: bold;
}

.overSelect {
	position: absolute;
	left: 0;
	right: 0;
	top: 0;
	bottom: 0;
}

#checkboxes {
	display: none;
	border: 1px #dadada solid;
}

#checkboxes label {
	display: block;
}

#checkboxes label:hover {
	background-color: #1e90ff;
}*/
</style>
<body>
	<%@ include file="navBarTeacher.jsp"%>
	<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	<div id="formAddHomework" align="right">
	<form action="./AddHomework" method="POST"
		enctype="multipart/form-data">
		<label
				style="position: absolute; left: 290px; text-decoration: underline;">Add
				homework</label> <br> <br> <br>
							<div class="form-group">
				
		<label class="control-label col-sm-6">Name</label><div class="col-sm-6"><input type="text" class="form-control" name="name" /> </div></div>
		<div class="form-group">
		<br><label class="control-label col-sm-6">Opening time</label><div class="col-sm-6"><input type='datetime-local' class="form-control" name="opens" data-val="true"></div>
		
		</div>
		<div class="form-group">
		 <br><label class="control-label col-sm-6">Closing time</label><div class="col-sm-6"><input
			type='datetime-local' class="form-control" name="closes" data-val="true"></div>
		</div>	
		<div class="form-group">
			<br><label class="control-label col-sm-6">Number of tasks</label><div class="col-sm-6"><input
			type="number" min="0" class="form-control" name="numberOfTasks" /> </div>
			
		</div>	
					<div class="form-group">
			
			<br><label class="control-label col-sm-6">Tasks</label>				<div class="col-sm-6">
			<input type="file"
				accept="application/pdf" name="file"></div>
		</div>
			<!--  <div class="multiselect"><div class="selectBox" onclick="showCheckboxes()">-->
			<br><div class="form-group">
					<label class="control-label col-sm-6">Groups</label>
					<div class="col-sm-6">
			<select class="selectpicker" multiple name = "groups">
			
			<c:forEach items="${applicationScope.allGroups}" var="group">
					<option value="${group.id}" >
					<c:out value="${group.name}"></c:out></option>
				</c:forEach>
				<!-- <select>
					<option>Select an option</option>
				</select>
				<div class="overSelect"></div>
			</div>
			<div id="checkboxes" >
				<c:forEach items="${applicationScope.allGroups}" var="group">
					<input type="checkbox" name = "groups" value="${group.id}" >
					<c:out value="${group.name}"></c:out>
				</c:forEach>
			</div> -->
			
		</select></div></div>
		
		<div class="form-group">
				<div class="col-sm-offset-3 col-sm-2" style="left: 290px">

					<button style="align: right" type="button" class="btn btn-default">Save</button>
				</div>
			</div>
	</form>
	</div>
	<script>
    /*var expanded = false;
    function showCheckboxes() {
        var checkboxes = document.getElementById("checkboxes");
        if (!expanded) {
            checkboxes.style.display = "block";
            expanded = true;
        } else {
            checkboxes.style.display = "none";
            expanded = false;
        }
    }*/
</script>
</body>
</html>
