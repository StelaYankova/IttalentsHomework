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
import com.IttalentsHomeworks.DAO.ValidationsDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.Exceptions.ValidationException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.Teacher;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class UpdateGroupServlet
 */
@WebServlet("/UpdateGroupServlet")
public class UpdateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  @Override
protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
	//TODO throw exception
			User user = (User) request.getSession().getAttribute("user");
			if(user.isTeacher()){
				if(request.getParameter("groupId") != null){//TODO check url (all updates too)
	  int groupId = Integer.parseInt(request.getParameter("groupId"));
	 Group group;
	try {
		group = GroupDAO.getInstance().getGroupById(groupId);
		 request.getSession().setAttribute("currGroup", group);
	} catch (GroupException | UserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
				}
	request.getRequestDispatcher("updateGroup.jsp").forward(request, resp);
			}
}
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO throw exception
				User user = (User) request.getSession().getAttribute("user");
				if(user.isTeacher()){
		Group currGroup = (Group) request.getSession().getAttribute("currGroup");
		int groupId = currGroup.getId();
		String newGroupName = request.getParameter("groupName").trim();
		String[] selectedTeachersUsername = request.getParameterValues("teachers");

		ArrayList<Integer> allSelectedTeachers = new ArrayList<>();
		if (selectedTeachersUsername != null) {
			for (int i = 0; i < selectedTeachersUsername.length; i++) {
				Teacher t = null;
				try {
					t = (Teacher) UserDAO.getInstance().getUserByUsername(selectedTeachersUsername[i]);
					allSelectedTeachers.add(t.getId());
				} catch (UserException | GroupException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// empty fields
		if (isThereEmptyField(newGroupName)) {
			request.getSession().setAttribute("emptyFields", true);
		} else {
			// invalid name
			boolean isNameUnique = false;
			if (isGroupNameUnique(groupId, newGroupName)) {
				isNameUnique = true;
			}
			request.getSession().setAttribute("uniqueName", isNameUnique);

			boolean isNameValid = false;
			if (isGroupNameValid(newGroupName)) {
				isNameValid = true;
			}
			request.getSession().setAttribute("validName", isNameValid); // success
			ArrayList<Teacher> allTeachers = (ArrayList<Teacher>) request.getServletContext().getAttribute("allTeachers");
			ArrayList<String> allTeacherUsernames = new ArrayList<>();
			boolean allTeachersExist = true;

			if (selectedTeachersUsername != null) {
				for (Teacher teacher : allTeachers) {
					allTeacherUsernames.add(teacher.getUsername());
				}
				if (!doAllTeachersExist(selectedTeachersUsername, allTeacherUsernames)) {
					allTeachersExist = false;
				}
				request.getSession().setAttribute("allTeachersExist", allTeachersExist);
			}
			if (isNameUnique == true && isNameValid == true && allTeachersExist == true) {

				try {
					currGroup.setName(newGroupName);
					GroupDAO.getInstance().updateGroup(currGroup, allSelectedTeachers);
					
					ArrayList<Group> allGroups;
					try {
						allGroups = GroupDAO.getInstance().getAllGroups();
						getServletContext().setAttribute("allGroups", allGroups);
						ArrayList<Teacher> allTeachersUpdated = UserDAO.getInstance().getAllTeachers();
						for(Teacher t : allTeachersUpdated){
							t.setGroups(UserDAO.getInstance().getGroupsOfUser(t.getId()));
						}
						getServletContext().setAttribute("allTeachers", allTeachersUpdated);
						
					} catch (UserException | GroupException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					request.getSession().setAttribute("invalidFields", false);

				} catch (GroupException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ValidationException e) {
					request.getSession().setAttribute("invalidFields", true);
				}
			}
		}
	//	request.getRequestDispatcher("updateGroup.jsp").forward(request, response);
		response.sendRedirect("./UpdateGroupServlet");
				}
	}

	private boolean isGroupNameUnique(int groupId, String groupName) {
		try {
			int wantedGroupNameId = GroupDAO.getInstance().getGroupIdByGroupName(groupName);

			if (ValidationsDAO.getInstance().isGroupNameUnique(groupName)) {
				return true;
			} else {
				if (wantedGroupNameId == groupId) {
					return true;
				}
			}

		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private boolean isGroupNameValid(String groupName){
		if(isGroupNameLengthValid(groupName) && areGroupNameCharactersValid(groupName)){
			return true;
		}
		return false;
	}
	private boolean isGroupNameLengthValid(String groupName) {
		if (groupName.length() >= 5 && groupName.length() <= 20) {
			return true;
		}
		return false;
	}

	private boolean areGroupNameCharactersValid(String groupName) {
		for(int i = 0; i < groupName.length(); i++){
			if(!(((int)groupName.charAt(i) >= 32 && (int)groupName.charAt(i) <= 126))){
				return false;
			}
		}
		return true;
	}
	private boolean isThereEmptyField(String groupName) {
		if (groupName == null || groupName.equals("")) {
			return true;
		}
		return false;
	}
	
	private boolean doAllTeachersExist(String[] selectedTeachersUsername, ArrayList<String> allTeachersUsernames){
		boolean doAllExist = true;
		for(int i = 0; i < selectedTeachersUsername.length;){
			if(!(allTeachersUsernames.contains(selectedTeachersUsername[i++]))){
				doAllExist = false;
				break;
			}
		}
		return doAllExist;
	}
}
