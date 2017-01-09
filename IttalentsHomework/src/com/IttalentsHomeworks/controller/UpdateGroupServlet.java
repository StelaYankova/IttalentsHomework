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
 * Servlet implementation class UpdateGroupServlet
 */
@WebServlet("/UpdateGroupServlet")
public class UpdateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	 int groupId = Integer.parseInt(req.getParameter("groupId"));
	 Group group;
	try {
		group = GroupDAO.getInstance().getGroupById(groupId);
		 req.getSession().setAttribute("currGroup", group);
	} catch (GroupException | UserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	req.getRequestDispatcher("updateGroup.jsp").forward(req, resp);
}
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Group currGroup = (Group) request.getSession().getAttribute("currGroup");
		//int groupId = currGroup.getId();
		String newGroupName = request.getParameter("groupName");
		String[] selectedTeachersUsername = request.getParameterValues("teachers");
		ArrayList<Integer> allSelectedTeachers = new ArrayList<>();
		if(selectedTeachersUsername != null && selectedTeachersUsername.length > 0){
		for (int i = 0; i < selectedTeachersUsername.length; i++) {
			Teacher t = null;
			try {
				System.out.println("Chosen teacher: " + selectedTeachersUsername[i].toString());
			//	int currTeacherId = UserDAO.getInstance().getUserIdByUsername(username)
				t = (Teacher) UserDAO.getInstance().getUserByUsername(selectedTeachersUsername[i]);
				allSelectedTeachers.add(t.getId());
				System.out.println(t.getId());
			} catch (UserException | GroupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	//	Group currGroup;
		try {
			//currGroup = GroupDAO.getInstance().getGroupById(groupId);
			currGroup.setName(newGroupName);
			GroupDAO.getInstance().updateGroup(currGroup, allSelectedTeachers);
			ArrayList<Group> allGroupsUpdated = GroupDAO.getInstance().getAllGroups();
			request.getServletContext().setAttribute("allGroups", allGroupsUpdated);
			ArrayList<Teacher> allTeachers = UserDAO.getInstance().getAllTeachers();
			getServletContext().setAttribute("allTeachers", allTeachers);
			for(Teacher t : allTeachers){
				t.setGroups(UserDAO.getInstance().getGroupsOfUser(t.getId()));
			}
		} catch (GroupException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
