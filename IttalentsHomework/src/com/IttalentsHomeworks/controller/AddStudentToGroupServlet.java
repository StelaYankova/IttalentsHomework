package com.IttalentsHomeworks.controller;

import java.io.IOException;
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
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class AddStudentToGroupServlet
 */
@WebServlet("/AddStudentToGroupServlet")
public class AddStudentToGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO throw exception
				User user = (User) request.getSession().getAttribute("user");
				if(user.isTeacher()){
		request.getRequestDispatcher("addStudentToGroup.jsp").forward(request, response);
				}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO throw exception
				User user = (User) request.getSession().getAttribute("user");
				if(user.isTeacher()){
		String chosenGroupIdString = request.getParameter("chosenGroup").trim();
		String chosenStudentUsername = request.getParameter("selectedStudent").trim();

		// empty fields
		request.setAttribute("chosenUsernameTry", chosenStudentUsername);
		if (isThereEmptyField(chosenGroupIdString, chosenStudentUsername)) {
			request.setAttribute("emptyFields", true);
		} else {
			// does student exist
			boolean doesStudentExist = false;
			boolean isStudentInGroup = false;
			int chosenGroupId = Integer.parseInt(chosenGroupIdString);
			if (doesStudentExist(chosenStudentUsername)) {
				doesStudentExist = true;
			}
			request.setAttribute("doesStudentExist", doesStudentExist);

			if (doesStudentExist == true) {
				// is student already in group
				if (isStudentAlreadyInGroup(chosenGroupId, chosenStudentUsername)) {
					isStudentInGroup = true;
				}
			}
			request.setAttribute("isStudentInGroup", isStudentInGroup);

			boolean isGroupValid = false;
			if(doesGroupExist(chosenGroupId)){
				isGroupValid = true;
			}
			request.setAttribute("validGroups", isGroupValid);
			
			if (doesStudentExist == true && isStudentInGroup == false && isGroupValid == true) {

				try {
					Group group = GroupDAO.getInstance().getGroupById(chosenGroupId);
					Student student = UserDAO.getInstance().getStudentsByUsername(chosenStudentUsername);
					GroupDAO.getInstance().addUserToGroup(group, student);
					request.setAttribute("invalidFields", false);
				} catch (UserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GroupException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ValidationException e) {
					request.setAttribute("invalidFields", true);
				}

			}
		}
		request.getRequestDispatcher("addStudentToGroup.jsp").forward(request, response);
				}
	}

	private boolean doesStudentExist(String username) {
		try {
			if (ValidationsDAO.getInstance().isUsernameUnique(username)) {
				return false;
			}
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean isStudentAlreadyInGroup(int groupId, String username) {
		Group chosenGroup;
		try {
			chosenGroup = GroupDAO.getInstance().getGroupById(groupId);
			com.IttalentsHomeworks.model.User chosenStudent = UserDAO.getInstance().getUserByUsername(username);
			if (GroupDAO.getInstance().isUserAlreadyInGroup(chosenGroup, chosenStudent)) {
				return true;
			}
		} catch (GroupException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	private boolean isThereEmptyField(String groupIdString, String username) {
		if (groupIdString.equals("null") || (groupIdString.equals("")) || (username.equals("")) || username == null) {
			return true;
		}
		return false;
	}
	
	private boolean doesGroupExist(int groupId){
			try {
				Group currGroup = GroupDAO.getInstance().getGroupById(groupId);
				if(currGroup == null){
					return false;
				}
			} catch (GroupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return true;
	}
}
