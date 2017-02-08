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
import com.IttalentsHomeworks.Exceptions.ValidationException;
import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class UpdateTeacherGradeAndCommentServlet
 */
@WebServlet("/UpdateTeacherGradeAndCommentServlet")
public class UpdateTeacherGradeAndCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO throw exception
				User user = (User) request.getSession().getAttribute("user");
				if(user.isTeacher()){
		Homework homework = (Homework) request.getSession().getAttribute("currHomework");
		HomeworkDetails hdOfhomework = null;
		String teacherComment = request.getParameter("comment").trim();
		String teacherGradeString = request.getParameter("grade").trim();
		int studentId = (int) request.getSession().getAttribute("studentId");
		int teacherGrade = 0;
		// grade not empty
		if (isGradeEmpty(teacherGradeString)) {
			request.setAttribute("emptyFields", true); // success

		} else if (isGradeTooLog(teacherGradeString)) {
			request.setAttribute("GradeTooLong", true); // success

		} else {
			teacherGrade = Integer.parseInt(teacherGradeString);
			// grade >=0 <=100
			boolean isGradeValueValid = false;
			if (isGradeValueValid(teacherGrade)) {
				isGradeValueValid = true;
			}
			request.setAttribute("validGrade", isGradeValueValid); // success

			// comment max length = 150
			boolean isCommentLengthValid = false;
			if (isCommentLengthValid(teacherComment)) {
				isCommentLengthValid = true;
			}
			request.setAttribute("validComment", isCommentLengthValid); // success
			if (isGradeValueValid == true && isCommentLengthValid == true) {
				ArrayList<Homework> homeworksOfStudent;
				try {
					homeworksOfStudent = UserDAO.getInstance().getHomeworksOfStudent(studentId);
					for (Homework h : homeworksOfStudent) {
						if (h.getHomeworkDetails().getId() == homework.getHomeworkDetails().getId()) {
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
				} catch (ValidationException e) {
					request.setAttribute("invalidFields", true);
				}
			}
		}
		request.getRequestDispatcher("homeworkOfStudent.jsp").forward(request, response);
				}
	}

	private boolean isGradeEmpty(String grade) {
		if (grade == null || grade.equals("")) {
			return true;
		}
		return false;
	}

	private boolean isGradeTooLog(String grade) {
		if (grade.length() > 3) {
			return true;
		}
		return false;
	}

	private boolean isGradeValueValid(int grade) {
		if (grade >= 0 && grade <= 100) {
			return true;
		}
		return false;
	}

	private boolean isCommentLengthValid(String comment) {
		if (comment.length() <= 150) {
			return true;
		}
		return false;
	}
}
