package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class GetHomeworksOfGroupsServlet
 */
@WebServlet("/GetHomeworksOfGroupsServlet")
public class GetHomeworksOfGroupsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User userTry = (User) request.getSession().getAttribute("user");
		if(!userTry.isTeacher()){
		User user = (User) request.getSession().getAttribute("user");
		int groupId = Integer.parseInt(request.getParameter("groupId"));
		Group group = null;
		for(Group g: user.getGroups()){
			if(g.getId() == groupId){
				group = g;
				break;
			}
		}
		ArrayList<HomeworkDetails> homeworks = new ArrayList<>();
		for(HomeworkDetails h: group.getHomeworks()){
			long days = LocalDateTime.now().until(h.getClosingTime(), ChronoUnit.DAYS);
			HomeworkDetails currHd = new HomeworkDetails(h.getHeading(), h.getOpeningTime(), h.getClosingTime(), h.getNumberOfTasks(), h.getTasksFile());
			currHd.setDaysLeft((int) days);
			try {
				currHd.setId(GroupDAO.getInstance().getHomeworkDetailsId(currHd));
			} catch (GroupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			homeworks.add(currHd);
		}
		request.getSession().setAttribute("currHomeworksOfGroup", homeworks);
		response.sendRedirect("seeYourHomeworks.jsp");
	}
	}
	

}
