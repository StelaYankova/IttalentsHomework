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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class GetAllGroupsServlet
 */
/*@WebServlet("/GetAllGroupsServlet")
public class GetAllGroupsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ArrayList<Group> allGroups = GroupDAO.getInstance().getAllGroups();
			JsonArray array = new JsonArray();
			for(Group g: allGroups){
				JsonObject obj = new JsonObject();
				obj.addProperty("id", g.getId());
				obj.addProperty("name", g.getName());
				array.add(obj);
			}
			response.getWriter().write(array.toString());
		} catch (UserException | GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}*/
