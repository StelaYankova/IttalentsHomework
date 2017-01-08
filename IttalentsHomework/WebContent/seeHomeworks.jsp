<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.min.js"></script>
	
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>
#divTable{
	position:absolute;
	top:150px;
	left:10px;
	width:60%;
}
#listOfStudentsOfGroup{
	position:absolute;
	top:300px;
	right:50px;
	
}

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

#image {
	position: absolute;
	left: 850px;
}


</style>
<body>
<%@ include file="navBarTeacher.jsp"%>
<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
Choose group: <select id =  "chosenGroup" class="selectpicker">
 <option value="null">-</option>
   <option value="allGroups">All Groups</option>

<c:forEach var="group" items="${applicationScope.allGroups}" >
	<option value = "${group.id}"><c:out value="${group.name}"></c:out></option>
</c:forEach>
</select>
<div id = "divTable">
	<table id="resultTable" border = "1" class="table table-striped table-bordered table-hover" style="width:60%">
		<thead class = wrapword>
			<tr>
				<td>Heading</td>
				<td>Opens</td>
				<td>Closes</td>
				<td></td>
			</tr>
		</thead>
		<tbody  class = wrapwordLink>
	
		</tbody>
	</table>
	</div>

	<ul id = "listOfStudentsOfGroup" class="editable wrapwordLink" style = "visibility:hidden;border-style: groove;z-index :1;height:300px; 
	width:18%;
	overflow:hidden; 
	overflow-y:scroll;overflow-x:scroll;"></ul>
	<script>
	$(document).ready(function() {
		var table = $('#resultTable').DataTable({
			"aoColumnDefs" : [ {
				'bSortable' : true,
				'aTargets' : [ 0, 1, 2 ],
				'className': "wrapword", "targets": [ 0,1,2,3]
				
			} ],
		      "dom":'<"top"l>rt<"bottom"ip><"clear">',
			 "aoColumns": [
			               { sWidth: '14%' },
			              { sWidth: '12%' },
			               { sWidth: '12%' },
			               { sWidth: '10%' }],
			"lengthMenu" : [5],
			"bDestroy" : true
		});
	$('#chosenGroup').change(function(event) {
		if(!$('#resultTable tbody').is(':empty') ) {
			$( "#resultTable tbody" ).empty();
		}
		if(!$('#listOfStudentsOfGroup').is(':empty') ) {
			$( "#listOfStudentsOfGroup" ).empty();
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
			if ($(table).find("#tbody").html() !== 0) {
			$('#resultTable').DataTable().clear().draw();
		}
			for(var i in response){
				if(response === 'null'){
				
				$('#resultTable tbody').html('no data available in table');

			}
				else if(groupId === 'allGroups'){
					var rowNode = table.row
					.add(
							["<button type = 'submit' class='btn btn-link' onclick = 'chooseGroupFirst()'>" +response[i].heading + "</button>",
							 response[i].opens,response[i].closes,"<form action = './UpdateHomeworkServlet' method = 'GET'><input type = 'hidden' name = 'chosenHomework' value = "+response[i].id +"><button type = 'submit' style = 'color:black' class='btn btn-link'>Change</button></form>"]).draw().node();
				}else{
					var rowNode = table.row
					.add(
					["<button type = 'submit' class='btn btn-link' onclick = 'chooseStudent(" + response[i].id +"," + groupId +")'>" +response[i].heading + "</button>",
					 response[i].opens,response[i].closes,"<form action = './UpdateHomeworkServlet' method = 'GET'><input type = 'hidden' name = 'chosenHomework' value = "+response[i].id +"><button type = 'submit' class='btn btn-link' style = 'color:black'>Change</button></form>"]).draw().node();
				}
				
			
				
			}
				
		}
	});
	});});
	function chooseGroupFirst(){
		alert("If you would like to see the homework of some of the students you should choose group first.")
	}
	
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
					$('#listOfStudentsOfGroup').append("<li><form action = './GetHomeworkOfStudentServlet'><input type = 'hidden' name = 'id' value = "+homeworkId +"><input type = 'hidden' name = 'studentId' value = "+response[i].id+"><button type = 'submit' class='btn btn-link'>"+response[i].username + "</button></form></li>");
					document.getElementById("listOfStudentsOfGroup").style.visibility = "visible";


				}
			}
			});
	};
	

	/*for(var i in response){
	
	var row = $("<tr>");
	
	if(groupId === 'allGroups'){
		row.append($("<td><button type = 'submit' onclick = 'chooseGroupFirst()'>" +response[i].heading + "</button></td>")) 
		.append($("<td>"+ response[i].opens+"</td>"))
	     .append($("<td>" +response[i].closes+"</td>"))
	     .append($("<form action = './UpdateHomeworkServlet' method = 'GET'><input type = 'hidden' name = 'chosenHomework' value = "+response[i].id +"><button type = 'submit'>Change</button></form>"))
	  $("#resultTable").append(row);
	
	}else{
	row.append($("<td><button type = 'submit' onclick = 'chooseStudent(" + response[i].id +"," + groupId +")'>" +response[i].heading + "</button></td>")) 
		.append($("<td>"+ response[i].opens+"</td>"))
	     .append($("<td>" +response[i].closes+"</td>"))
	     .append($("<form action = './UpdateHomeworkServlet' method = 'GET'><input type = 'hidden' name = 'chosenHomework' value = "+response[i].id +"><button type = 'submit'>Change</button></form>"))
	  $("#resultTable").append(row);
	}
	}*/
//});
</script>
</body>
</html>