package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;

/**
 * Servlet implementation class IsHomeworkHeadingUnique
 */
@WebServlet("/IsHomeworkHeadingUnique")
public class IsHomeworkHeadingUnique extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String heading = request.getParameter("heading");
		if(GroupDAO.getInstance().isHomeworkHeadingUnique(heading)){
			response.setStatus(200);
		}else{
			response.setStatus(400);
		}
	}


}
