package com.IttalentsHomeworks.controller;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IsHomeworkOpeningTimeValid
 */
@WebServlet("/IsHomeworkOpeningTimeValid")
public class IsHomeworkOpeningTimeValid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String opens = request.getParameter("opens");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime openingTime = LocalDateTime.parse(opens, formatter);
		if(openingTime.isAfter(LocalDateTime.now().minusDays(1))){
			response.setStatus(200); 
		}else{
			response.setStatus(400);
		}
	}


}
