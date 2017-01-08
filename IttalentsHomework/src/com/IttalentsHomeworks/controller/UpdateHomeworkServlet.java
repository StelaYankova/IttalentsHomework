package com.IttalentsHomeworks.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
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
import com.IttalentsHomeworks.model.HomeworkDetails;

/**
 * Servlet implementation class UpdateHomeworkServlet
 */
@WebServlet("/UpdateHomeworkServlet")
@MultipartConfig

public class UpdateHomeworkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "/Users/Stela/Desktop/imagesIttalentsHomework";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int hwId = Integer.parseInt(request.getParameter("chosenHomework"));
		try {
			HomeworkDetails hd = GroupDAO.getInstance().getHomeworkDetailsById(hwId);
			request.getSession().setAttribute("currHomework", hd);
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.getRequestDispatcher("updateHomework.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO add file
		int homeworkDetailsId = ((HomeworkDetails)request.getSession().getAttribute("currHomework")).getId();
		
		String name = request.getParameter("name");
		String fileName = " ";
		/*String oldNameOfFile;
		try {
			oldNameOfFile = ((HomeworkDetails) GroupDAO.getInstance().getHomeworkDetailsById(homeworkDetailsId)).getHeading();
			File file1 = new File(oldNameOfFile);
			
			// File (or directory) with new name
			File file2 = new File(fileName);

			
			// Rename file (or directory)
			file1.renameTo(file2);
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		if (request.getPart("file") != null) {
			final Part filePart = request.getPart("file");
			String savePath = SAVE_DIR;
			File fileSaveDir = new File(savePath);
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdir();
				System.out.println("doesnt exists");
			}

			// final String fileName = extractFileName(filePart);
			fileName = "hwName" + name + ".pdf";

			OutputStream out = null;
			InputStream filecontent = null;
			final PrintWriter writer = response.getWriter();
			//
			
			

			File file = new File(SAVE_DIR + File.separator + fileName);
			if (!file.exists()) {
				String oldNameOfFile;
				try {
					oldNameOfFile = ((HomeworkDetails) GroupDAO.getInstance().getHomeworkDetailsById(homeworkDetailsId)).getHeading();
					File file1 = new File(SAVE_DIR + File.separator + "hwName"+oldNameOfFile + ".pdf");
					
					// File (or directory) with new name
					File file2 = new File(SAVE_DIR + File.separator + fileName);

					
					// Rename file (or directory)
					//File newFile = new File(file1.getParent(), SAVE_DIR + File.separator +"hwName"+ fileName + ".pdf");
					Files.move(file1.toPath(), file2.toPath());	
				} catch (GroupException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			out = new FileOutputStream(file, true);
			filecontent = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		} else {
			fileName = "hwName" + name + ".pdf";
		}
		try {

			// User user = (User) request.getSession().getAttribute("user");

			String[] selectedGroups = request.getParameterValues("groups");

			System.out.println(name);
			String opens = request.getParameter("opens");
			String closes = request.getParameter("closes");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
			LocalDateTime openingTime = LocalDateTime.parse(opens, formatter);
			LocalDateTime closingTime = LocalDateTime.parse(closes, formatter);
			int numberOfTasks = Integer.parseInt(request.getParameter("numberOfTasks"));
			ArrayList<Group> groupsForHw = new ArrayList<>();

			HomeworkDetails homeworkDetails = new HomeworkDetails(homeworkDetailsId,name, openingTime, closingTime, numberOfTasks,
					fileName);
			if (request.getParameterValues("groups") != null) {
				for (int i = 0; i < selectedGroups.length; i++) {
					int id = Integer.parseInt(selectedGroups[i]);
					Group g = GroupDAO.getInstance().getGroupById(id);
					groupsForHw.add(g);

				}
			}
			
			
			GroupDAO.getInstance().updateHomeworkDetails(homeworkDetails, groupsForHw);
			
			HomeworkDetails hd = GroupDAO.getInstance().getHomeworkDetailsById(homeworkDetailsId);
			request.getSession().setAttribute("currHomework", hd);
			request.getServletContext().removeAttribute("allGroups");
			ArrayList<Group> allGroups = GroupDAO.getInstance().getAllGroups();
			request.getServletContext().setAttribute("allGroups", allGroups);
		} catch (GroupException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
