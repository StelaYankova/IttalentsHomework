package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class getAllStudentsOfGroupServlet
 */
@WebServlet("/getAllStudentsOfGroupServlet")
public class getAllStudentsOfGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String groupIdStr = request.getParameter("chosenGroupId");
		
		if (groupIdStr.equals("allGroups")) {
			response.sendRedirect("chooseGroupForHomework.jsp");
		} else if (!(groupIdStr.equals("null"))) {
			int groupId = Integer.parseInt((String) request.getParameter("chosenGroupId"));
			try {
				Group selectedGroup = GroupDAO.getInstance().getGroupById(groupId);
				ArrayList<Student> allStudentsOfGroup = GroupDAO.getInstance().getStudentsOfGroup(selectedGroup);
				JsonArray array = new JsonArray();
				for (Student student : allStudentsOfGroup) {
					boolean hasStudentGivenMinOneTask = false;
					System.out.println(student.getUsername());
					System.out.println(student.getId());
					JsonObject obj = new JsonObject();
					obj.addProperty("id", student.getId());
					obj.addProperty("username", student.getUsername());
					if (request.getParameter("homeworkId") != null) {
						int chosenHomeworkId = Integer.parseInt(request.getParameter("homeworkId"));
						HomeworkDetails chosenHomework = null;
						try {
							chosenHomework = GroupDAO.getInstance().getHomeworkDetailsById(chosenHomeworkId);
						} catch (GroupException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						for (Task t : UserDAO.getInstance().getTasksOfHomeworkOfStudent(student.getId(),
								chosenHomework)) {
							hasStudentGivenMinOneTask = false;
							String x = t.getSolution();
							if (x != null) {
								System.out.println("TRUE " + student.getUsername() );
								hasStudentGivenMinOneTask = true;
								obj.addProperty("hasStudentGivenMinOneTask", hasStudentGivenMinOneTask);
								break;
							}
							System.out.println(
									"Student " + student.getUsername() + " has done it: " + hasStudentGivenMinOneTask + " sol " + t.getSolution());
							obj.addProperty("hasStudentGivenMinOneTask", hasStudentGivenMinOneTask);

						}
					}
					array.add(obj);
				}
				response.getWriter().write(array.toString());

			} catch (GroupException | UserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}