package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.User;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.Student;

/**
 * Servlet implementation class IsChosenStudentAlreadyInGroup
 */
@WebServlet("/IsChosenStudentAlreadyInGroup")
public class IsChosenStudentAlreadyInGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int chosenGroupId = Integer.parseInt(request.getParameter("chosenGroupId"));
		String chosenStudentUsername = request.getParameter("chosenStudentUsername");
		try {
			Group chosenGroup = GroupDAO.getInstance().getGroupById(chosenGroupId);
			com.IttalentsHomeworks.model.User chosenStudent = UserDAO.getInstance().getUserByUsername(chosenStudentUsername);
			if(GroupDAO.getInstance().isUserAlreadyInGroup(chosenGroup, chosenStudent)){
				System.out.println("USER IS IN");
				response.setStatus(400);
			}else{
				System.out.println("USER IS NOT");

				response.setStatus(200);
			}
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
