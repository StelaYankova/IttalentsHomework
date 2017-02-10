package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class GetHomeworkPageServlet
 */
@WebServlet("/GetHomeworkPageServlet")
public class GetHomeworkPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if(!user.isTeacher()){
			request.getRequestDispatcher("currHomeworkPageStudent.jsp").forward(request, response);
		}
	}
	

}
