package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.DAO.ValidationsDAO;
import com.IttalentsHomeworks.Exceptions.UserException;

/**
 * Servlet implementation class DoesUserExist
 */
@WebServlet("/DoesUserExist")
public class DoesUserExist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String chosenStudentUsername = request.getParameter("chosenStudentUsername").trim();
		try {
			if(ValidationsDAO.getInstance().isUsernameUnique(chosenStudentUsername)){//if its unique id is not in DB
				response.setStatus(400);
			}else{
				response.setStatus(200);
			}
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
