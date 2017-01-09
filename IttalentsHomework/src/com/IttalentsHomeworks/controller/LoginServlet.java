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
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.Teacher;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		ArrayList<Group> allGroups;
		try {
			allGroups = GroupDAO.getInstance().getAllGroups();
			getServletContext().setAttribute("allGroups", allGroups);
			ArrayList<Teacher> allTeachers = UserDAO.getInstance().getAllTeachers();
			for(Teacher t : allTeachers){
				t.setGroups(UserDAO.getInstance().getGroupsOfUser(t.getId()));
			}
			getServletContext().setAttribute("allTeachers", allTeachers);
			
		} catch (UserException | GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		request.getSession().setMaxInactiveInterval(100000);

		try {
			if(UserDAO.getInstance().doesUserExistInDB(username, password)){
				user = UserDAO.getInstance().getUserByUsername(username);
				request.getSession().setAttribute("user", user);
				
				if(user.isTeacher()){
					request.getSession().setAttribute("isTeacher", true);

					ArrayList<Student> allStudents = UserDAO.getInstance().getAllStudents();
					request.getServletContext().setAttribute("allStudents", allStudents);
					response.sendRedirect("mainPageTeacher.jsp");

				}else{
					request.getSession().setAttribute("isTeacher", false);

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
