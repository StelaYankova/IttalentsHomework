package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.UserDAO;
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
		User user = (User) request.getSession().getAttribute("user");
		int groupId = Integer.parseInt(request.getParameter("groupId"));
		Group group = null;
		for(Group g: user.getGroups()){
			if(g.getId() == groupId){
				group = g;
				break;
			}
		}
		JsonArray homeworks = new JsonArray();
		for(HomeworkDetails h: group.getHomeworks()){
			JsonObject obj = new JsonObject();
			obj.addProperty("heading", h.getHeading());
			long days = h.getClosingTime().until( h.getOpeningTime(), ChronoUnit.DAYS);
			obj.addProperty("timeLeft", days);
			//System.out.println(group.getName() + " " +  h.getHeading() + "  "  + days + "homeworks: " + group.getHomeworks().size());
			homeworks.add(obj);
		}
		//obj.add("homeworks", homeworks);
		response.setContentType("application/json");
		response.getWriter().write(homeworks.toString());
	}

	

}
