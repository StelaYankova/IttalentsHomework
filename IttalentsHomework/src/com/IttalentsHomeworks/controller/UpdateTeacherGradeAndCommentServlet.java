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
import com.IttalentsHomeworks.model.HomeworkDetails;

/**
 * Servlet implementation class UpdateTeacherGradeAndCommentServlet
 */
@WebServlet("/UpdateTeacherGradeAndCommentServlet")
public class UpdateTeacherGradeAndCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Homework homework  = (Homework) request.getSession().getAttribute("currHomework");
		HomeworkDetails hdOfhomework = null;
		System.out.println("#####################");
		String teacherComment = request.getParameter("comment");
		System.out.println(teacherComment);
		int teacherGrade = Integer.parseInt(request.getParameter("grade"));
		System.out.println(teacherGrade);
		int studentId = (int) request.getSession().getAttribute("studentId");
		System.out.println(studentId);
		ArrayList<Homework> homeworksOfStudent;
		try {
			homeworksOfStudent = UserDAO.getInstance().getHomeworksOfStudent(studentId);
			for(Homework h: homeworksOfStudent){
				if(h.getHomeworkDetails().getId() == homework.getHomeworkDetails().getId()){
					hdOfhomework = h.getHomeworkDetails();
				}
			}
			
			UserDAO.getInstance().setTeacherComment(hdOfhomework, studentId, teacherComment);
			UserDAO.getInstance().setTeacherGrade(hdOfhomework, studentId, teacherGrade);
			homework.setTeacherComment(teacherComment);
			homework.setTeacherGrade(teacherGrade);
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
