package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.Teacher;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class UpdateYourProfileServlet
 */
@WebServlet("/UpdateYourProfileServlet")
public class UpdateYourProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("updateProfile.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getId();
		String username = user.getUsername();
		String password = request.getParameter("password");
		String repeatedPassword = request.getParameter("repeatedPassword");
		String email = request.getParameter("email");
		//TODO add validations
		if(password.equals(repeatedPassword)){
			User newUser = null;
			if(user.isTeacher()){
				newUser = new Teacher(username, password,email);
			}else{
				newUser = new Student(username, password,email);
			}
			newUser.setId(userId);
			try {
				UserDAO.getInstance().updateUser(newUser);
			} catch (UserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	

}
