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

/**
 * Servlet implementation class UpdateGroupServlet
 */
@WebServlet("/UpdateGroupServlet")
public class UpdateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int groupId = Integer.parseInt(request.getParameter("groupId"));
		String newGroupName = request.getParameter("groupName");
		Group currGroup;
		try {
			currGroup = GroupDAO.getInstance().getGroupById(groupId);
			currGroup.setName(newGroupName);
			GroupDAO.getInstance().changeGroupName(currGroup);
			ArrayList<Group> allGroupsUpdated = GroupDAO.getInstance().getAllGroups();
			request.getServletContext().setAttribute("allGroups", allGroupsUpdated);
		} catch (GroupException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
