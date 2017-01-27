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
import com.IttalentsHomeworks.model.Teacher;

/**
 * Servlet implementation class AddGroupServlet
 */
@WebServlet("/AddGroupServlet")
public class AddGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("addGroup.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("groupName");
		String[] selectedTeachersUsername = request.getParameterValues("teachers");
		ArrayList<Teacher> allSelectedTeachers = new ArrayList<>();
		if(selectedTeachersUsername != null){
		for (int i = 0; i < selectedTeachersUsername.length; i++) {
			Teacher t = null;
			try {
				t = (Teacher) UserDAO.getInstance().getUserByUsername(selectedTeachersUsername[i]);
				allSelectedTeachers.add(t);
				System.out.println(t.getId());
			} catch (UserException | GroupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		Group newGroup = new Group(groupName,allSelectedTeachers);
		
		try {
			GroupDAO.getInstance().createNewGroup(newGroup);
			ArrayList<Group> allGroupsUpdated = GroupDAO.getInstance().getAllGroups();
			request.getServletContext().setAttribute("allGroups", allGroupsUpdated);
			ArrayList<Teacher> allTeachers = UserDAO.getInstance().getAllTeachers();
			getServletContext().setAttribute("allTeachers", allTeachers);
			for(Teacher t : allTeachers){
				t.setGroups(UserDAO.getInstance().getGroupsOfUser(t.getId()));
			}
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("seeGroupsToChange.jsp");
	}

}
