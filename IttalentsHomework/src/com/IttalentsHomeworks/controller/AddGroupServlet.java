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
		for (int i = 0; i < selectedTeachersUsername.length; i++) {
			Teacher t = null;
			try {
				System.out.println("Izbran daskal: " + selectedTeachersUsername[i].toString());
			//	int currTeacherId = UserDAO.getInstance().getUserIdByUsername(username)
				t = (Teacher) UserDAO.getInstance().getUserByUsername(selectedTeachersUsername[i]);
				allSelectedTeachers.add(t);
				System.out.println(t.getId());
			} catch (UserException | GroupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Izbrana grupa: " + groupName);
		Group newGroup = new Group(groupName,allSelectedTeachers);
		
		try {
			GroupDAO.getInstance().createNewGroup(newGroup);
			ArrayList<Group> allGroupsUpdated = GroupDAO.getInstance().getAllGroups();
			request.getServletContext().setAttribute("allGroups", allGroupsUpdated);
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
