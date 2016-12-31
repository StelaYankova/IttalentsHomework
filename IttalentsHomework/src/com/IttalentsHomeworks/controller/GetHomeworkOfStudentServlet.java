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

/**
 * Servlet implementation class GetHomeworkOfStudentServlet
 */
@WebServlet("/GetHomeworkOfStudentServlet")
public class GetHomeworkOfStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Student user = (Student) request.getSession().getAttribute("user");
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		int homeworkId = Integer.parseInt(request.getParameter("id"));
		request.getSession().setAttribute("studentId", studentId);
		Homework homework = null;
		//System.out.println(user.get);
		ArrayList<Homework> homeworks;
		try {
			homeworks = UserDAO.getInstance().getHomeworksOfStudent(studentId);
			for(Homework h: homeworks){
				if(h.getHomeworkDetails().getId() == homeworkId){
					System.out.println("VATRE SME");
					homework = new Homework(h.getTeacherGrade(), h.getTeacherComment(), h.getTasks(), h.getHomeworkDetails());
					break;
				}
			}
			request.getSession().setAttribute("currHomework", homework);
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("homeworkOfStudent.jsp");
	}
}
