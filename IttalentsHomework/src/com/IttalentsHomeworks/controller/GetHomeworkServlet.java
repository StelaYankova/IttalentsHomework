package com.IttalentsHomeworks.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class GetHomeworkServlet
 */
@WebServlet("/GetHomeworkServlet")
public class GetHomeworkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Student user = (Student) request.getSession().getAttribute("user");
		int homeworkId = Integer.parseInt(request.getParameter("id"));
		System.out.println("GVHBJNKML " + homeworkId);
		Homework homework = null;
		// System.out.println(user.get);
		for (Homework h : user.getHomeworks()) {
			if (h.getHomeworkDetails().getId() == homeworkId) {
				System.out.println("VATRE SME");
				homework = new Homework(h.getTeacherGrade(), h.getTeacherComment(), h.getTasks(),
						h.getHomeworkDetails());
				break;
			}
		}
		request.getSession().setAttribute("currHomework", homework);
		boolean hasUploadTimePassed = true;
		if (homework.getHomeworkDetails().getClosingTime().isAfter(LocalDateTime.now())) {
			hasUploadTimePassed = false;
		}
		boolean hasUploadTimeCome = false;
		if (LocalDateTime.now().isAfter(homework.getHomeworkDetails().getOpeningTime())) {
			hasUploadTimeCome = true;
		}
		request.getSession().setAttribute("hasUploadTimePassed", hasUploadTimePassed);
		request.getSession().setAttribute("hasUploadTimeCome", hasUploadTimeCome);

		System.out.println(hasUploadTimePassed);
		System.out.println(hasUploadTimeCome);
		response.sendRedirect("currHomeworkPageStudent.jsp");
	}

}
