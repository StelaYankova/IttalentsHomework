package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.HomeworkDetails;

/**
 * Servlet implementation class AddHomework
 */
@WebServlet("/AddHomework")
public class AddHomework extends HttpServlet {
	/*private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("addHomework.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] selectedGroups = request.getParameterValues("group");
		String name = request.getParameter("name");
		
		String opens = request.getParameter("opens");
		System.out.println("IUGYUFTFXGF " + opens);
		String closes = request.getParameter("closes");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime openingTime = LocalDateTime.parse(opens, formatter);
		LocalDateTime closingTime = LocalDateTime.parse(closes, formatter);
		String tasks = request.getParameter("tasks");
		int numberOfTasks = Integer.parseInt(request.getParameter("numberOfTasks"));
		ArrayList<Group> groupsForHw = new ArrayList<>();
		HomeworkDetails homeworkDetails = new HomeworkDetails(name, openingTime, closingTime, numberOfTasks, tasks);

		try {
			GroupDAO.getInstance().createHomeworkDetails(homeworkDetails, groupsForHw);
			for (int i = 0; i < selectedGroups.length; i++) {
				int id = Integer.parseInt(selectedGroups[i]);
				Group g = GroupDAO.getInstance().getGroupById(id);
				groupsForHw.add(g);
			}
		} catch (GroupException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
