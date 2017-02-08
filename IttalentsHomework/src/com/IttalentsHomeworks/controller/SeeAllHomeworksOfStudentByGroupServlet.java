package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.time.LocalDateTime;
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
import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.Task;
import com.IttalentsHomeworks.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SeeAllHomeworksOfStudentByGroupServlet
 */
@WebServlet("/SeeAllHomeworksOfStudentByGroupServlet")
public class SeeAllHomeworksOfStudentByGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if(user.isTeacher()){	
			int groupId = Integer.parseInt(request.getParameter("groupId"));
		
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		Group selectedGroup = null;
		JsonArray array = new JsonArray();
		ArrayList<HomeworkDetails> homeworkDetailsByGroup = new ArrayList<>();
		try {
			selectedGroup = GroupDAO.getInstance().getGroupById(groupId);
			homeworkDetailsByGroup.addAll(GroupDAO.getInstance().getHomeworkDetailsOfGroup(selectedGroup));
		} catch (GroupException | UserException e) {
			e.printStackTrace();
		}
		boolean hasStudentGivenMinOneTask = false;
		
		for (HomeworkDetails hd : homeworkDetailsByGroup) {
			JsonObject obj = new JsonObject();
			obj.addProperty("heading", hd.getHeading());
			obj.addProperty("id", hd.getId());
			obj.addProperty("opens", hd.getOpeningTime().toString());
			obj.addProperty("closes", hd.getClosingTime().toString());
			try {
				for (Homework h : UserDAO.getInstance().getHomeworksOfStudentByGroup(studentId,
						selectedGroup)) {
					if (hd.getId() == h.getHomeworkDetails().getId()) {
						int grade = h.getTeacherGrade();
						String comment = h.getTeacherComment();
						for(Task t: h.getTasks()){
							String x = t.getSolution();
							if(x != null){
								hasStudentGivenMinOneTask = true;
								break;
							}
						}
						
						obj.addProperty("hasStudentGivenMinOneTask", hasStudentGivenMinOneTask);
						obj.addProperty("teacherScore", grade);
						obj.addProperty("teacherComment", comment);
						hasStudentGivenMinOneTask = false;

						break;
					}
				}
			} catch (UserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			array.add(obj);
		}
		response.getWriter().write(array.toString());
	}
	
	}
	

}
