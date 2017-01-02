package com.IttalentsHomeworks.controller;

import java.io.IOException;
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
import com.IttalentsHomeworks.model.Student;

/**
 * Servlet implementation class AddStudentToGroupServlet
 */
@WebServlet("/AddStudentToGroupServlet")
public class AddStudentToGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("addStudentToGroup.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!request.getParameter("chosenGroup").equals("null")){
			//addnahme mu si4ki doma6ni ne samo 4, ne e v taskovete
		try {
			String studentUsername = request.getParameter("selectedStudent");
				int chosenGroupId = Integer.parseInt(request.getParameter("chosenGroup"));
				Group group;
					group = GroupDAO.getInstance().getGroupById(chosenGroupId);
			Student student = UserDAO.getInstance().getStudentsByUsername(studentUsername);
			GroupDAO.getInstance().addUserToGroup(group, student);
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}}

}
