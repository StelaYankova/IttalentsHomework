package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.model.Homework;

/**
 * Servlet implementation class GetCurrHomeworkOfStudent
 */
@WebServlet("/GetCurrHomeworkOfStudent")
public class GetCurrHomeworkOfStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Homework currHw = (Homework) request.getSession().getAttribute("currHomework");
		System.out.println(currHw.getHomeworkDetails().getId());
		System.out.println("OKIJUHYGTFGYHUJIKOLP:{");
		request.getRequestDispatcher("homeworkOfStudent.jsp").forward(request, response);
	}



}
