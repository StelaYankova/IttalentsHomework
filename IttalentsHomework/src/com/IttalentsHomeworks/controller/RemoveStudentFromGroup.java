package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class RemoveStudentFromGroup
 */
@WebServlet("/RemoveStudentFromGroup")
public class RemoveStudentFromGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO throw exception
		System.out.println("11111");
				User user = (User) request.getSession().getAttribute("user");
				if(user.isTeacher()){
		if (!(request.getParameter("chosenGroupId").equals("null"))) {
			System.out.println("22222");

			try {
				int chosenGroupId = Integer.parseInt(request.getParameter("chosenGroupId"));
				Group chosenGroup = GroupDAO.getInstance().getGroupById(chosenGroupId);
				String studentUsername = request.getParameter("chosenStudentUsername").trim();
				System.out.println("We will remove student: " + studentUsername +" from group: " + chosenGroupId);
				Student chosenStudent = (Student) UserDAO.getInstance().getUserByUsername(studentUsername);
				System.out.println("333333");

				GroupDAO.getInstance().removeUserFromGroup(chosenGroup, chosenStudent.getId());
				response.setStatus(200);
				response.getWriter().write("");
				//request.getSession().setAttribute("invalidFields", false);
				System.out.println("44444");

			} catch (GroupException | UserException e) {
				
				System.out.println(e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//response.sendRedirect("./AddStudentToGroupServlet");
		//request.getRequestDispatcher("addStudentToGroup.jsp").forward(request, response);
	}}

}
