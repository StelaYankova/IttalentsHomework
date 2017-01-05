package com.IttalentsHomeworks.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class AddHomework
 */
@WebServlet("/AddHomework")
@MultipartConfig
public class AddHomework extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "/Users/Stela/Desktop/imagesIttalentsHomework";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("addHomework.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO add file
		String name = request.getParameter("name");
		String savePath = SAVE_DIR;
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
			System.out.println("doesnt exists");
		}
		String fileName = " ";
		final Part filePart = request.getPart("file");
		// final String fileName = extractFileName(filePart);
		fileName = "hwName" + name + ".pdf";

		OutputStream out = null;
		InputStream filecontent = null;
		final PrintWriter writer = response.getWriter();

		try {
			File file = new File(SAVE_DIR + File.separator + fileName);
			if (!file.exists()) {
				// file.getParentFile().mkdirs();
				file.createNewFile();
			}

			out = new FileOutputStream(file, true);
			filecontent = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			// User user = (User) request.getSession().getAttribute("user");

			String[] selectedGroups = request.getParameterValues("groups");
			System.out.println("!!!");

			System.out.println(name);
			String opens = request.getParameter("opens");
			String closes = request.getParameter("closes");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
			LocalDateTime openingTime = LocalDateTime.parse(opens, formatter);
			LocalDateTime closingTime = LocalDateTime.parse(closes, formatter);
			int numberOfTasks = Integer.parseInt(request.getParameter("numberOfTasks"));
			ArrayList<Group> groupsForHw = new ArrayList<>();

			HomeworkDetails homeworkDetails = new HomeworkDetails(name, openingTime, closingTime, numberOfTasks,
					fileName);

			for (int i = 0; i < selectedGroups.length; i++) {
				int id = Integer.parseInt(selectedGroups[i]);
				Group g = GroupDAO.getInstance().getGroupById(id);
				groupsForHw.add(g);

			}
			GroupDAO.getInstance().createHomeworkDetails(homeworkDetails, groupsForHw);

		} catch (GroupException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
