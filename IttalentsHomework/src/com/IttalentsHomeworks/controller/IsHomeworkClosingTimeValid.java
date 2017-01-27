package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IsHomeworkClosingTimeValid
 */
@WebServlet("/IsHomeworkClosingTimeValid")
public class IsHomeworkClosingTimeValid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String opens = request.getParameter("opens");
		String closes = request.getParameter("closes");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime openingDateTime = LocalDateTime.parse(opens, formatter);
		LocalDateTime closingDateTime = LocalDateTime.parse(closes, formatter);
	   // LocalDate openingDate = openingDateTime.toLocalDate();
	    //LocalDate closingDate = closingDateTime.toLocalDate();

//		Period betweenDates = Period.between(openingDate, closingDate);
		long diffInMonths = ChronoUnit.MONTHS.between(openingDateTime,
				closingDateTime);
	   // int diffInMonths = betweenDates.getMonths();
		System.out.println("sled now li e " + closingDateTime.isAfter(LocalDateTime.now()) + " now is " + LocalDateTime.now().compareTo(closingDateTime));
		System.out.println("sled opening time li e: " + closingDateTime.isAfter(openingDateTime));
		System.out.println();
		if(closingDateTime.isAfter(LocalDateTime.now()) && closingDateTime.isAfter(openingDateTime) && diffInMonths <= 6){
			response.setStatus(200); 
		}else{
			response.setStatus(400);
		}
	}

}
