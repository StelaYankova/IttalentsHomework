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
	<br>
	<button onclick="seeGroups()">Your groups:</button>
	<div id="groups">
	</div>
	<div id = "homeworks">
	</div>
	<script>
		function seeGroups() {
			if(!$('#groups').is(':empty') ) {
				$( "#groups" ).empty();
			}
			$.ajax({
				url : './GetGroupsOfUserServlet',
				type : 'GET',
				dataType : 'json',
				success : function(response) {
					for ( var i in response) {
						$('#groups').append(
								"<button id = 'response[i].id' onclick = 'seeHomeworks("
										+ response[i].id + ")'>"
										+ response[i].name + "</button>");
					}
				}
			});
		}

		function seeHomeworks(groupId) {
			if(!$('#homeworks').is(':empty') ) {
				$( "#homeworks" ).empty();
			}
			console.log(groupId);
			$.ajax({
				url : './GetHomeworksOfGroupsServlet',
				type : 'GET',
				dataType : 'json',
				data : {
					groupId : groupId
				},
				success : function(response) {
					for ( var h in response) {
						var id = response[h].id;console.log(id)
						$('#homeworks').append('<br><form action = "./GetHomeworkServlet" method = "GET"><input type = "hidden" name = "id" value = ' +id + '><button type = "submit">' + response[h].heading + '</form>');
						homeworkId = id;
						$('#homeworks').append(
								'  ' + response[h].timeLeft + ' days left');

					}

				}
			});
		}
	</script>
</body>
</html>