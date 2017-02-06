package com.IttalentsHomeworks.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.DAO.ValidationsDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.Exceptions.ValidationException;
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
		String heading = request.getParameter("name").trim();
		String[] selectedGroups = request.getParameterValues("groups");
		String opens = request.getParameter("opens").replace("/", "-").trim();
		String closes = request.getParameter("closes").replace("/", "-").trim();
		final Part filePart = request.getPart("file");
		String numberOfTasksString = request.getParameter("numberOfTasks").trim();
//	int numberOfTasks = Integer.parseInt(request.getParameter("numberOfTasks"));

		
		request.setAttribute("nameTry", heading);
		request.setAttribute("opensTry", opens.replace("-", "/"));
		request.setAttribute("closesTry", closes.replace("-", "/"));
		if(isHomeworkNumberOfTasksLengthValid(numberOfTasksString)){
			request.setAttribute("numberOfTasksTry", Integer.parseInt(numberOfTasksString));
		}
		//request.setAttribute("numberOfTasksTry", numberOfTasks);
		//empty fields
		if(isThereEmptyField(heading, opens, closes, filePart,numberOfTasksString, selectedGroups)){
			request.setAttribute("emptyFields", true);

		}else{
		//heading valid
			int numberOfTasks = 0;
			boolean isHeadingValid = false;
			boolean isHeadingUnique = false;
			if(areCharactersHeadingValid(heading) && isLengthHeadingValid(heading)){
				isHeadingValid = true;
				if(isHomeworkHeadingUnique(heading)){
					isHeadingUnique = true;
				}
			}
			request.setAttribute("validHeading", isHeadingValid);
			request.setAttribute("uniqueHeading", isHeadingUnique);

		//heading unique
		//opening time
			boolean isOpeningTimeValid = false;
			if(isHomeworkOpeningTimeValid(opens)){
				isOpeningTimeValid = true;
			}
			request.setAttribute("validOpeningTime", isOpeningTimeValid);

		//closing time
			boolean isClosingTimeValid = false;
			if(isHomeworkClosingTimeValid(opens, closes)){
				isClosingTimeValid = true;
			}
			request.setAttribute("validClosingTime", isClosingTimeValid);

		//file
			boolean isFileValid = false;
			if(isHomeworkContentTypeValid(filePart) && isHomeworkSizeValid(filePart)){
				isFileValid = true;
			}
			request.setAttribute("validFile", isFileValid);

		//numTasks
			boolean areTasksValid = false;
			
			if(isHomeworkNumberOfTasksLengthValid(numberOfTasksString)){
				numberOfTasks = Integer.parseInt(request.getParameter("numberOfTasks"));
				if(isHomeworkNumberOfTasksValid(numberOfTasks)){
					areTasksValid = true;
				}
			}
			request.setAttribute("validTasks", areTasksValid);
			//groups
			boolean areGroupsValid = false;
			if(doAllGroupsExist(selectedGroups)){
				areGroupsValid = true;
			}
			request.setAttribute("validGroups", areGroupsValid);

			if(isHeadingValid == true && isHeadingUnique == true && isOpeningTimeValid == true && isClosingTimeValid == true && isFileValid == true && areTasksValid ==true && areGroupsValid == true){
		String savePath = SAVE_DIR;
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
			System.out.println("doesnt exists");
		}
		String fileName = " ";
		// final String fileName = extractFileName(filePart);
		fileName = "hwName" + heading + ".pdf";

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
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime openingTime = LocalDateTime.parse(opens, formatter);
			LocalDateTime closingTime = LocalDateTime.parse(closes, formatter);
			ArrayList<Group> groupsForHw = new ArrayList<>();
			HomeworkDetails homeworkDetails = new HomeworkDetails(heading, openingTime, closingTime, numberOfTasks,
					fileName);
			for (int i = 0; i < selectedGroups.length; i++) {
				int id = Integer.parseInt(selectedGroups[i]);
				Group g = GroupDAO.getInstance().getGroupById(id);
				groupsForHw.add(g);


			}
			GroupDAO.getInstance().createHomeworkDetails(homeworkDetails, groupsForHw);
			request.setAttribute("invalidFields", false);


		} catch (GroupException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			request.setAttribute("invalidFields", true);
		}
		}}
		request.getRequestDispatcher("addHomework.jsp").forward(request, response);
	}

	private boolean isThereEmptyField(String heading, String opens, String closes, Part file, String numberOfTasksString, String[] selectedGroups) {
		if (heading == null || heading.equals("") || opens == null || opens.equals("") || closes == null
				|| closes.equals("") || numberOfTasksString == null || numberOfTasksString.equals("") ||selectedGroups == null) {
			return true;
		}
		if (file.getSize() == 0) {
			return true;
		}
		return false;
	}
	private boolean isLengthHeadingValid(String heading) {
		if (heading.length() >= 5 && heading.length() <= 40) {
			return true;
		}
		return false;
	}

	private boolean areCharactersHeadingValid(String heading) {
		for(int i = 0; i < heading.length(); i++){
			if(!(((int)heading.charAt(i) >= 32 && (int)heading.charAt(i) <= 126))){
				return false;
			}
		}
		return true;
	}
	private boolean isHomeworkHeadingUnique(String heading){
		if(GroupDAO.getInstance().isHomeworkHeadingUnique(heading)){
			return true;
		}
		return false;
	}

	private boolean isHomeworkOpeningTimeValid(String opens) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

			LocalDateTime openingTime = LocalDateTime.parse(opens, formatter);
			LocalDate openingDate = openingTime.toLocalDate();
			if (openingDate.isAfter(LocalDate.now().minusDays(1))
					&& openingDate.isBefore(LocalDate.now().plusMonths(6).minusDays(1))) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isHomeworkClosingTimeValid(String opens, String closes){
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime openingDateTime = LocalDateTime.parse(opens, formatter);
			LocalDateTime closingDateTime = LocalDateTime.parse(closes, formatter);
			long diffInMonths = ChronoUnit.MONTHS.between(openingDateTime, closingDateTime);

			if (closingDateTime.isAfter(LocalDateTime.now()) && closingDateTime.isAfter(openingDateTime)
					&& diffInMonths < 6) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isHomeworkNumberOfTasksLengthValid(String numberOfTasks){
		if(numberOfTasks.length() >= 10){
			return false;
		}
		return true;
	}
	private boolean isHomeworkNumberOfTasksValid(int numberOfTasks){
		if(numberOfTasks >= 1 && numberOfTasks <= 40){
			return true;
		}
		return false;
	}
	private boolean isHomeworkContentTypeValid(Part file) {
		String contentType = file.getSubmittedFileName().substring(file.getSubmittedFileName().indexOf("."));
		if (!(contentType.equals(".pdf"))) {
			return false;
		}
		return true;
	}


	private boolean isHomeworkSizeValid(Part file) {
		long sizeInMb = file.getSize() / (1024 * 1024);
		if (sizeInMb > 20) {
			return false;
		}
		return true;
	}
	
	private boolean doAllGroupsExist(String[] selectedGroups){
		for(String groupId: selectedGroups){
			try {
				Group currGroup = GroupDAO.getInstance().getGroupById(Integer.parseInt(groupId));
				String groupName = currGroup.getName();
				if(ValidationsDAO.getInstance().isGroupNameUnique(groupName)){
					System.out.println("Unique is: " + groupName);
					return false;
				}
			} catch (GroupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
}
