package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
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
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		request.getSession().setMaxInactiveInterval(1000);

		try {
			if(UserDAO.getInstance().doesUserExistInDB(username, password)){
				user = UserDAO.getInstance().getUserByUsername(username);
				request.getSession().setAttribute("user", user);
				ArrayList<Group> allGroups = GroupDAO.getInstance().getAllGroups();
				request.getServletContext().setAttribute("allGroups", allGroups);
				if(user.isTeacher()){
					response.sendRedirect("mainPageTeacher.jsp");
				}else{
					response.sendRedirect("mainPageStudent.jsp");
				}
			}else{
				request.getSession().setAttribute("invalidField", "true");
				response.sendRedirect("homePage.jsp");
			}
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
