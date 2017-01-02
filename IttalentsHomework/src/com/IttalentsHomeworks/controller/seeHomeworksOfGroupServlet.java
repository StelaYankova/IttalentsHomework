package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class seeHomeworksOfGroupServlet
 */
@WebServlet("/seeHomeworksOfGroupServlet")
public class seeHomeworksOfGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<HomeworkDetails> homeworkDetailsByGroup = new ArrayList<>();
		JsonArray array = new JsonArray();
		String group = request.getParameter("chosenGroup");
		System.out.println(group);
	if (!(request.getParameter("chosenGroup").equals("null"))) {
			int groupId = Integer.parseInt(request.getParameter("chosenGroup"));
		
			Group chosenGroup = null;

			try {
				chosenGroup = GroupDAO.getInstance().getGroupById(groupId);
				homeworkDetailsByGroup.addAll(GroupDAO.getInstance().getHomeworkDetailsOfGroup(chosenGroup));
			} catch (GroupException | UserException e) {
				e.printStackTrace();
			}
		
		for (HomeworkDetails hd : homeworkDetailsByGroup) {
			JsonObject obj = new JsonObject();
			obj.addProperty("heading", hd.getHeading());
			obj.addProperty("id", hd.getId());
			obj.addProperty("opens", hd.getOpeningTime().toString());
			obj.addProperty("closes", hd.getClosingTime().toString());

			array.add(obj);
		}
	}
		response.getWriter().write(array.toString());
	}
	


}
