package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * Servlet implementation class GetGroupsOfUserServlet
 */
@WebServlet("/GetGroupsOfUserServlet")
public class GetGroupsOfUserServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		ArrayList<Group> groupsOfUser = user.getGroups();
		JsonArray jsonGroups = new JsonArray();
				for (Group g: groupsOfUser) {
					JsonObject obj = new JsonObject();
					obj.addProperty("name", g.getName());
					obj.addProperty("id", g.getId());
					obj.add("homeworks", null);
					JsonArray homeworks = new JsonArray();
					for(HomeworkDetails h: g.getHomeworks()){
						System.out.println("!! " + h.getId());
						JsonObject obj1 = new JsonObject();
						obj1.addProperty("heading", h.getHeading());
						obj1.addProperty("id", h.getId());
						
						long days = h.getClosingTime().until( h.getOpeningTime(), ChronoUnit.DAYS);
						obj1.addProperty("timeLeft", days);
						System.out.println(g.getName() + " " +  h.getHeading() + "  "  + days + "homeworks: " + g.getHomeworks().size());
						homeworks.add(obj1);
					}
					obj.add("homeworks", homeworks);
					jsonGroups.add(obj);
				}
				response.setContentType("application/json");
				response.getWriter().write(jsonGroups.toString());
			
		
	}
}
