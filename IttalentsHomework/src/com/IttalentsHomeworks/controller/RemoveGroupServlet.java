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
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class RemoveGroupServlet
 */
@WebServlet("/RemoveGroupServlet")
public class RemoveGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//TODO throw exception
				User user = (User) request.getSession().getAttribute("user");
				if(user.isTeacher()){
		int groupId = Integer.parseInt(request.getParameter("groupId"));
		Group selectedGroup;
		try {
			selectedGroup = GroupDAO.getInstance().getGroupById(groupId);
			GroupDAO.getInstance().removeGroup(selectedGroup);
			request.getServletContext().removeAttribute("allGroups");
			ArrayList<Group> allGroupsUpdated = GroupDAO.getInstance().getAllGroups();
			request.getServletContext().setAttribute("allGroups", allGroupsUpdated);
		} catch (GroupException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("seeGroupsToChange.jsp");
				}
	}

}
