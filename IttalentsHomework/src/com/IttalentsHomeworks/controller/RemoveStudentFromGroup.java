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
 * Servlet implementation class RemoveStudentFromGroup
 */
@WebServlet("/RemoveStudentFromGroup")
public class RemoveStudentFromGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println(request.getParameter("chosenGroupId"));
		if (!(request.getParameter("chosenGroupId").equals("null"))) {
			try {
				int chosenGroupId = Integer.parseInt(request.getParameter("chosenGroupId"));
				Group chosenGroup = GroupDAO.getInstance().getGroupById(chosenGroupId);
				String studentUsername = request.getParameter("studentId");
				Student chosenStudent = (Student) UserDAO.getInstance().getUserByUsername(studentUsername);

				GroupDAO.getInstance().removeUserFromGroup(chosenGroup, chosenStudent);
			} catch (GroupException | UserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
