<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<!-- <link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.min.js"></script> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>
</head>
<style>
#image {
	position: absolute;
	left: 850px;
}

#listOfStudentsOfGroup{
	position:absolute;
	top:300px;
	right:40px;
	
}
#divTable{
	position:absolute;
	top:150px;
	left:0px;
	width:80%
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
</style>
<body>
<%@ include file="navBarTeacher.jsp"%>
<div id="image">
		<img src="logo-black.png" class="img-rounded" width="380" height="236">
	</div>
	Choose group: <select id =  "chosenGroup" class="selectpicker">
<option value="null">-</option>
 <c:if test="${sessionScope.isTeacher == false}">
<c:forEach var="group" items="${user.groups}" >
	 <option value="${group.id}"><c:out value="${group.name}"></c:out></option>
 </c:forEach>
 </c:if>
  <c:if test="${sessionScope.isTeacher == true}">
<c:forEach var="group" items="${applicationScope.allGroups}" >
	 <option value="${group.id}"><c:out value="${group.name}"></c:out></option>
 </c:forEach>
 </c:if>
</select>
<div id="divTable">
		<table id="resultTable" border="1"
			class="table table-striped table-bordered table-hover dataTables_wrapper form-inline dt-bootstrap"
			style="width: 60%">
			<thead class="wrapword">
				<tr>
					<td>Heading</td>
					<td>Opens</td>
					<td>Closes</td>
					<td>Teacher score</td>
					<td>Teacher comment</td>
				</tr>
			</thead>
			<tbody class="wrapword">
			
			</tbody>
		</table>
	</div>
	<ul id = "listOfStudentsOfGroup" class="editable wrapwordLink" style = "visibility:hidden;z-index :1;height:300px; 
	width:18%;
	overflow:hidden; 
	overflow-y:scroll;overflow-x:scroll;"></ul>
	<script>
var table = $('#resultTable').DataTable({
	"aoColumnDefs" : [ {
		'bSortable' : true,
		'aTargets' : [ 0, 1, 2 ],
		'className': "wrapword", "targets": [ 0,1,2,3]
		
	} ],
    "dom":'<"top"l>rt<"bottom"ip><"clear">',

		"aoColumns": [
	               { sWidth: '14%' },
	              { sWidth: '14%' },
	               { sWidth: '14%' },
	               { sWidth: '18%' },
	               { sWidth: '22%' }],
	"lengthMenu" : [5,10],
	"bDestroy" : true
});	
function seeHomeworks(e,e1){
			if(!$('#resultTable tbody').is(':empty') ) {
				$( "#resultTable tbody" ).empty();
			}
		var groupId = e;
		var studentId = e1;
		$.ajax({
			url:'./SeeAllHomeworksOfStudentByGroupServlet',
			type: 'GET',
			data:{
				"groupId": groupId,
				"studentId": studentId
			},
			dataType:'json',
			success: function(response){
				if ($(table).find("#tbody").html() !== 0) {
					$('#resultTable').DataTable().clear().draw();
				}
				for(var i in response){
					if(response === 'null'){
						
						$('#resultTable tbody').html('no data available in table');

					}else{
						var opens = response[i].opens;
						var opensRep = opens.replace("T", " ");
						var closes = response[i].closes;
						var closesRep = closes.replace("T", " ");
						var hasStudentGivenMinOneTask = response[i].hasStudentGivenMinOneTask;
						console.log(hasStudentGivenMinOneTask)
					
							if(hasStudentGivenMinOneTask===true){
								var rowNode = table.row
								.add(	
							
							["<form action = './GetHomeworkOfStudentServlet' method = 'GET'><input type = 'hidden' name = 'id' value = " + response[i].id+ "><input type = 'hidden' name = 'studentId' value = "+studentId+"><button type = 'submit' class = 'btn btn-link'>" + response[i].heading +"</button></form>",
							 opensRep,closesRep,response[i].teacherScore+"/100",response[i].teacherComment]).draw().node();
					}else{
						var rowNode = table.row
						.add(
						["<form action = './GetHomeworkOfStudentServlet' method = 'GET'><input type = 'hidden' name = 'id' value = " + response[i].id+ "><input type = 'hidden' name = 'studentId' value = "+studentId+"><button style= 'color:#620062' type = 'button' class = 'btn btn-link'>" + response[i].heading +"</button></form>",
						 opensRep,closesRep,response[i].teacherScore+"/100",response[i].teacherComment]).draw().node();
						
					}}
					}
				}
			
		});
			
		}
	$('#chosenGroup').change(function(event) {
		if(!$('#resultTable tbody').is(':empty') ) {
			$( "#resultTable tbody" ).html('<tr><td colspan="5" style = "padding-left:16em ">No data available in table</td></tr>');
		}			
		//<tr><td colspan="4" style = "padding-left:12em ">No data available in table</td></tr>

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
				// var div = document.getElementById("listOfStudentsOfGroup");
				for(var i in response){
					$('#listOfStudentsOfGroup').append("<li><button class = 'btn btn-link' onclick = 'seeHomeworks(" + groupId+ ","+ response[i].id+ ")'>" + response[i].username + "</button></li>");
					document.getElementById('listOfStudentsOfGroup').style.visibility = 'visible';
				}
			}
		});
	});
	

/*var row = $("<tr>");
row.append($("<td><form action = './GetHomeworkOfStudentServlet' method = 'GET'><input type = 'hidden' name = 'id' value = " + response[i].id+ "><input type = 'hidden' name = 'studentId' value = "+studentId+"><button type = 'submit'>" + response[i].heading +"</button></form></td>"))
 .append($("<td>"+ response[i].opens+"</td>"))
 .append($("<td>" +response[i].closes+"</td>"))
 .append($("<td>"+ response[i].teacherScore+"/100</td>"))
 .append($("<td>" +response[i].teacherComment+"</td>"));
 

$("#resultTable").append(row);*/
</script>
</body>
</html>