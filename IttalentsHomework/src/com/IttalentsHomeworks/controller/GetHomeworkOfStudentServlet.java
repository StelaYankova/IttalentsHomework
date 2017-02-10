package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class GetHomeworkOfStudentServlet
 */
@WebServlet("/GetHomeworkOfStudentServlet")
public class GetHomeworkOfStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if(user.isTeacher()){
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		int homeworkId = Integer.parseInt(request.getParameter("id"));
		request.getSession().setAttribute("studentId", studentId);
		Homework homework = null;
		ArrayList<Homework> homeworks;
		try {
			homeworks = UserDAO.getInstance().getHomeworksOfStudent(studentId);
			for(Homework h: homeworks){
				if(h.getHomeworkDetails().getId() == homeworkId){
					homework = new Homework(h.getTeacherGrade(), h.getTeacherComment(), h.getTasks(), h.getHomeworkDetails());
					break;
				}
			}
			//request.getSession().setAttribute("currHomework", homework);
			request.getSession().setAttribute("currHomework", homework);
			response.sendRedirect("./GetCurrHomeworkOfStudent");
			//request.getRequestDispatcher("homeworkOfStudent.jsp").forward(request, response);
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//response.sendRedirect("homeworkOfStudent.jsp");
	}
	}
}
