package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;

/**
 * Servlet implementation class IsHomeworkHeadingValid
 */
@WebServlet("/IsHomeworkHeadingValid")
public class IsHomeworkHeadingValid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String heading = request.getParameter("heading").trim();
		if(isLengthValid(heading) && areCharactersValid(heading)){
			response.setStatus(200);
		}else{
			response.setStatus(400);
		}
	}
	private boolean isLengthValid(String heading) {
		if (heading.length() >= 5 && heading.length() <= 40) {
			return true;
		}
		return false;
	}

	private boolean areCharactersValid(String heading) {
		for(int i = 0; i < heading.length(); i++){
			if(!(((int)heading.charAt(i) >= 32 && (int)heading.charAt(i) <= 126))){
				return false;
			}
		}
		return true;
	}
}


