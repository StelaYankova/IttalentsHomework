package com.IttalentsHomeworks.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class ReadHomeworkServlet
 */
@WebServlet("/ReadHomeworkServlet")
public class ReadHomeworkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "/Users/Stela/Desktop/imagesIttalentsHomework";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		String fileName = request.getParameter("fileName").trim();
		String homeworkName = fileName.substring(6, fileName.length() - 4);
		boolean canUserAccessHomeworkTasks = false;
		if (!user.isTeacher()) {
			for (Group g : user.getGroups()) {
				for (HomeworkDetails hd : g.getHomeworks()) {
					if (hd.getHeading().equals(homeworkName)) {
						canUserAccessHomeworkTasks = true;
						break;
					}
				}
			}
		} else {
			canUserAccessHomeworkTasks = true;
		}
		if (canUserAccessHomeworkTasks) {
			File file = new File(SAVE_DIR + File.separator + fileName);
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			response.setContentLength((int) file.length());

			FileInputStream fileInputStream = new FileInputStream(file);
			OutputStream responseOutputStream = response.getOutputStream();
			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}
			request.getRequestDispatcher("currHomeworkPageStudent.jsp");
			//response.sendRedirect("./GetHomeworkPageServlet");
		}
	}

}
