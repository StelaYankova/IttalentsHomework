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
 * Servlet implementation class AddGroupServlet
 */
@WebServlet("/AddGroupServlet")
public class AddGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO throw exception
		User user = (User) request.getSession().getAttribute("user");
		if(user.isTeacher()){
			request.getRequestDispatcher("addGroup.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if(user.isTeacher()){
		String groupName = request.getParameter("groupName").trim();
		// empty fields
		request.setAttribute("nameTry", groupName);
		String[] selectedTeachersUsername = request.getParameterValues("teachers");
		
		request.setAttribute("selectedTeachersUsernameTry", selectedTeachersUsername);
	
		boolean isNameUnique = false;
		if (isThereEmptyField(groupName)) {
			request.setAttribute("emptyFields", true);
		} else {
			// unique name
			if (isGroupNameUnique(groupName)) {
				isNameUnique = true;
			}
			request.setAttribute("uniqueName", isNameUnique);
			// valid name
			boolean isNameValid = false;
			if (isGroupNameValid(groupName)) {
				isNameValid = true;
			}
			request.setAttribute("validName", isNameValid);
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
				request.setAttribute("allTeachersExist", allTeachersExist);
			}
			if (isNameUnique == true && isNameValid == true && allTeachersExist == true) {
				ArrayList<Teacher> allSelectedTeachers = new ArrayList<>();
				if (selectedTeachersUsername != null) {
					
					for (int i = 0; i < selectedTeachersUsername.length; i++) {
						Teacher t = null;
						try {
							t = (Teacher) UserDAO.getInstance().getUserByUsername(selectedTeachersUsername[i]);
							allSelectedTeachers.add(t);
						} catch (UserException | GroupException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				Group newGroup = new Group(groupName, allSelectedTeachers);

				try {
					try {
						GroupDAO.getInstance().createNewGroup(newGroup);
						request.setAttribute("invalidFields", false);

					} catch (ValidationException e) {
						request.setAttribute("invalidFields", true);
						request.getRequestDispatcher("addGroup.jsp").forward(request, response);
						return;
					}
					ArrayList<Group> allGroupsUpdated = GroupDAO.getInstance().getAllGroups();
					request.getServletContext().setAttribute("allGroups", allGroupsUpdated);
						ArrayList<Teacher> allTeachersUpdated = UserDAO.getInstance().getAllTeachers();
						for(Teacher t : allTeachersUpdated){
							t.setGroups(UserDAO.getInstance().getGroupsOfUser(t.getId()));
						}
						getServletContext().setAttribute("allTeachers", allTeachersUpdated);
				} catch (GroupException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		request.getRequestDispatcher("addGroup.jsp").forward(request, response);
		}
	}

	private boolean isGroupNameUnique(String groupName) {
		try {
			if (ValidationsDAO.getInstance().isGroupNameUnique(groupName)) {
				return true;
			}
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private boolean isGroupNameValid(String groupName) {
		if (isLengthValid(groupName) && areCharactersValid(groupName)) {
			return true;
		}
		return false;
	}

	private boolean isLengthValid(String groupName) {
		if (groupName.length() >= 5 && groupName.length() <= 20) {
			return true;
		}
		return false;
	}

	private boolean areCharactersValid(String groupName) {
		for (int i = 0; i < groupName.length(); i++) {
			if (!(((int) groupName.charAt(i) >= 32 && (int) groupName.charAt(i) <= 126))) {
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
