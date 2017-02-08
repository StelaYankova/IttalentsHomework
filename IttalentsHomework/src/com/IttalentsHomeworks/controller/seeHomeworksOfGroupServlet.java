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
import com.IttalentsHomeworks.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class seeHomeworksOfGroupServlet
 */
@WebServlet("/seeHomeworksOfGroupServlet")
public class seeHomeworksOfGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if(user.isTeacher()){	
			ArrayList<HomeworkDetails> homeworkDetailsByGroup = new ArrayList<>();
		JsonArray array = new JsonArray();
		if (!(request.getParameter("chosenGroup").equals("null"))) {
			if (!(request.getParameter("chosenGroup").equals("allGroups"))) {
			int groupId = Integer.parseInt(request.getParameter("chosenGroup"));
		
			Group chosenGroup = null;

			try {
				chosenGroup = GroupDAO.getInstance().getGroupById(groupId);
				homeworkDetailsByGroup.addAll(GroupDAO.getInstance().getHomeworkDetailsOfGroup(chosenGroup));
			} catch (GroupException | UserException e) {
				e.printStackTrace();
			}
		
		
			}else{
				try {
					homeworkDetailsByGroup = GroupDAO.getInstance().getAllHomeworksDetails();
				} catch (GroupException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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


}
