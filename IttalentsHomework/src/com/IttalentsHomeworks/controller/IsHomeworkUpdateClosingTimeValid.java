package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.model.HomeworkDetails;

/**
 * Servlet implementation class IsHomeworkUpdateClosingTimeValid
 */
@WebServlet("/IsHomeworkUpdateClosingTimeValid")
public class IsHomeworkUpdateClosingTimeValid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String opens = request.getParameter("opens").trim().replace("/", "-");
		String closes = request.getParameter("closes").trim().replace("/", "-");
		
		HomeworkDetails currHd = (HomeworkDetails) request.getSession().getAttribute("currHomework");

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime openingDateTime = LocalDateTime.parse(opens, formatter);
			LocalDateTime closingDateTime = LocalDateTime.parse(closes, formatter);
			long diffInMonths = ChronoUnit.MONTHS.between(openingDateTime, closingDateTime);
			if (closingDateTime.equals(currHd.getClosingTime())) {
				response.setStatus(200);
			}else{
			if (closingDateTime.isAfter(LocalDateTime.now()) && closingDateTime.isAfter(openingDateTime)
					&& diffInMonths < 6) {
				response.setStatus(200);
			} else {
				response.setStatus(400);
			}
			}
		} catch (NumberFormatException e) {
			response.setStatus(400);

		}
	}

}
