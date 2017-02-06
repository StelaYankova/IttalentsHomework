package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.time.LocalDate;
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
 * Servlet implementation class IsHomeworkUpdateOpeningTimeValid
 */
@WebServlet("/IsHomeworkUpdateOpeningTimeValid")
public class IsHomeworkUpdateOpeningTimeValid extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String opens = request.getParameter("opens").replace("/", "-");
		HomeworkDetails currHd = (HomeworkDetails) request.getSession().getAttribute("currHomework");
	
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

			LocalDateTime openingTime = LocalDateTime.parse(opens, formatter);
			LocalDate openingDate = openingTime.toLocalDate();
			if(openingTime.equals(currHd.getOpeningTime())){
				response.setStatus(200);
			}else{
			if (openingDate.isAfter(LocalDate.now().minusDays(1))
					&& openingDate.isBefore(LocalDate.now().plusMonths(6).minusDays(1))) {
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
