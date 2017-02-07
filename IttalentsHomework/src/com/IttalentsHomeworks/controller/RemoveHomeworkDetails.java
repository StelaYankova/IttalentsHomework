package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.HomeworkDetails;

/**
 * Servlet implementation class RemoveHomeworkDetails
 */
@WebServlet("/RemoveHomeworkDetails")
public class RemoveHomeworkDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HomeworkDetails hd = (HomeworkDetails) request.getSession().getAttribute("currHomework");
		try {
			GroupDAO.getInstance().removeHomeworkDetails(hd);
		} catch (GroupException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("invalidFields", false);
		request.getRequestDispatcher("seeHomeworks.jsp").forward(request, response);
	}

}
