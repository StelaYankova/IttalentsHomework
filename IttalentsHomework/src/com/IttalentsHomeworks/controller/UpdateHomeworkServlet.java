package com.IttalentsHomeworks.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
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
import com.IttalentsHomeworks.Exceptions.NotUniqueUsernameException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.Exceptions.ValidationException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.User;

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
		//TODO throw exception
				User user = (User) request.getSession().getAttribute("user");
				if(user.isTeacher()){
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
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO throw exception
				User user = (User) request.getSession().getAttribute("user");
				if(user.isTeacher()){
		int homeworkDetailsId = ((HomeworkDetails) request.getSession().getAttribute("currHomework")).getId();

		String heading = request.getParameter("name").trim();
		String fileName = " ";
		String[] selectedGroups = request.getParameterValues("groups");
		Part filePart = request.getPart("file");
		String opens = request.getParameter("opens").trim().replace("/", "-");
		String closes = request.getParameter("closes").trim().replace("/", "-");
		String numberOfTasksString = request.getParameter("numberOfTasks").trim();

		// empty fields (except file)
		if (isThereEmptyField(heading, opens, closes, numberOfTasksString, selectedGroups)) {
			request.setAttribute("emptyFields", true);
		} else {
			HomeworkDetails currHd = null;
			try {
				currHd = GroupDAO.getInstance().getHomeworkDetailsById(homeworkDetailsId);
			} catch (GroupException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// valid heading
			int numberOfTasks = 0;
			boolean isHeadingValid = false;
			boolean isHeadingUnique = false;
			if (areHomeworkUpdateCharactersValid(heading) && isHomeworkUpdateLengthValid(heading)) {
				isHeadingValid = true;
				if (isHomeworkUpdateHeadingUnique(heading, currHd)) {
					isHeadingUnique = true;
				}
			}
			request.setAttribute("validHeading", isHeadingValid);
			request.setAttribute("uniqueHeading", isHeadingUnique); // unique
																	// heading
			// opening time
			boolean isOpeningTimeValid = false;
			if (isHomeworkUpdateOpeningTimeValid(opens, currHd)) {
				isOpeningTimeValid = true;
			}
			request.setAttribute("validOpeningTime", isOpeningTimeValid);
			// closing time
			boolean isClosingTimeValid = false;
			if (isHomeworkUpdateClosingTimeValid(opens, closes, currHd)) {
				isClosingTimeValid = true;
			}
			request.setAttribute("validClosingTime", isClosingTimeValid);
			// numTasks
			boolean areTasksValid = false;

			if (isHomeworkUpdateNumberOfTasksLengthValid(numberOfTasksString)) {
				numberOfTasks = Integer.parseInt(request.getParameter("numberOfTasks"));
				if (isHomeworkUpdateNumberOfTasksValid(numberOfTasks)) {
					areTasksValid = true;
				}
			}
			request.setAttribute("validTasks", areTasksValid);
			// do all groups exist
			boolean areGroupsValid = false;
			if (doAllGroupsExistHomeworkUpdate(selectedGroups)) {
				areGroupsValid = true;
			}
			request.setAttribute("validGroups", areGroupsValid);
			boolean isFileValid = false;
			if (filePart.getSize() == 0) {
				isFileValid = true;
			} else {
				if (isFileUpdateHomeworkValid(filePart)) {
					isFileValid = true;
				}
			}
			request.setAttribute("validFile", isFileValid);
			
			if (isHeadingValid == true && isHeadingUnique == true && isOpeningTimeValid == true
					&& isClosingTimeValid == true && areTasksValid == true && areGroupsValid == true
					&& isFileValid == true) {
				fileName = "hwName" + heading + ".pdf";

				File file = new File(SAVE_DIR + File.separator + fileName);
				File file1 = null;
				if (!file.exists()) {
					String oldNameOfFile;
					try {
						oldNameOfFile = ((HomeworkDetails) GroupDAO.getInstance()
								.getHomeworkDetailsById(homeworkDetailsId)).getHeading();
						 file1 = new File(SAVE_DIR + File.separator + "hwName" + oldNameOfFile + ".pdf");
						Files.copy(file1.toPath(), file.toPath());//we copy, if it succeeds we remove old file, else we remove new file
					} catch (GroupException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (filePart.getSize() != 0) {
					isFileValid = true;
					String savePath = SAVE_DIR;
					File fileSaveDir = new File(savePath);
					if (!fileSaveDir.exists()) {
						fileSaveDir.mkdir();
					}
					// final String fileName = extractFileName(filePart);
					fileName = "hwName" + heading + ".pdf";
				} else {
					fileName = "hwName" + heading + ".pdf";
				}
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					LocalDateTime openingTime = LocalDateTime.parse(opens, formatter);
					LocalDateTime closingTime = LocalDateTime.parse(closes, formatter);
					ArrayList<Group> groupsForHw = new ArrayList<>();
					HomeworkDetails homeworkDetails = new HomeworkDetails(homeworkDetailsId, heading, openingTime,
							closingTime, numberOfTasks, fileName);
					if (request.getParameterValues("groups") != null) {
						for (int i = 0; i < selectedGroups.length; i++) {
							int id = Integer.parseInt(selectedGroups[i]);
							Group g = GroupDAO.getInstance().getGroupById(id);
							groupsForHw.add(g);

						}
					}

					GroupDAO.getInstance().updateHomeworkDetails(homeworkDetails, groupsForHw);
					//ako ne e gramnalo
					OutputStream out = null;
					InputStream filecontent = null;
					// final PrintWriter writer = response.getWriter();

					out = new FileOutputStream(file, true);
					filecontent = filePart.getInputStream();

					int read = 0;
					final byte[] bytes = new byte[1024];

					while ((read = filecontent.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}
					HomeworkDetails hd = GroupDAO.getInstance().getHomeworkDetailsById(homeworkDetailsId);
					request.getSession().setAttribute("currHomework", hd);
					request.getServletContext().removeAttribute("allGroups");
					ArrayList<Group> allGroups = GroupDAO.getInstance().getAllGroups();
					request.getServletContext().setAttribute("allGroups", allGroups);
					request.setAttribute("invalidFields", false);
					if(file1 != null){
						file1.delete();
					}
				} catch (GroupException | UserException e) {
					file.delete();
					e.printStackTrace();
				} catch (ValidationException e) {
					file.delete();
					request.setAttribute("invalidFields", true);

				} catch (NotUniqueUsernameException e) {
					request.setAttribute("invalidFields", true);
					e.printStackTrace();
				}
			}
		}

		request.getRequestDispatcher("updateHomework.jsp").forward(request, response);
				}
	}

	private boolean isFileUpdateHomeworkValid(Part file) {
		if (isHomeworkUpdateContentTypeValid(file) && isHomeworkUpdateSizeValid(file)) {
			return true;
		}
		return false;
	}

	private boolean isThereEmptyField(String heading, String opens, String closes, String numberOfTasksString,
			String[] selectedGroups) {
		if (heading == null || heading.equals("") || opens == null || opens.equals("") || closes == null
				|| closes.equals("") || numberOfTasksString == null || numberOfTasksString.equals("")
				|| selectedGroups == null) {

			return true;
		}
		return false;

	}

	private boolean isHomeworkUpdateLengthValid(String heading) {
		if (heading.length() >= 5 && heading.length() <= 40) {
			return true;
		}
		return false;
	}

	private boolean areHomeworkUpdateCharactersValid(String heading) {
		for (int i = 0; i < heading.length(); i++) {
			if (!(((int) heading.charAt(i) >= 32 && (int) heading.charAt(i) <= 126))) {
				return false;
			}
		}
		return true;
	}

	private boolean isHomeworkUpdateHeadingUnique(String heading, HomeworkDetails currHd) {
		if (currHd.getHeading().equals(heading) || GroupDAO.getInstance().isHomeworkHeadingUnique(heading)) {
			return true;
		}else{
			return false;
		}
	}
	private boolean isHomeworkUpdateOpeningTimeValid(String opens, HomeworkDetails currHd){
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

			LocalDateTime openingTime = LocalDateTime.parse(opens, formatter);
			LocalDate openingDate = openingTime.toLocalDate();
			if(openingTime.equals(currHd.getOpeningTime())){
				return true;
			}else{
			if (openingDate.isAfter(LocalDate.now().minusDays(1))
					&& openingDate.isBefore(LocalDate.now().plusMonths(6).minusDays(1))) {
				return true;
			} else {
				return false;
			}
			}
		} catch (NumberFormatException e) {
			return false;

		}
	}
	private boolean isHomeworkUpdateClosingTimeValid(String opens, String closes, HomeworkDetails currHd){
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime openingDateTime = LocalDateTime.parse(opens, formatter);
			LocalDateTime closingDateTime = LocalDateTime.parse(closes, formatter);
			long diffInMonths = ChronoUnit.MONTHS.between(openingDateTime, closingDateTime);
			if (closingDateTime.equals(currHd.getClosingTime())) {
				return true;
			}else{
			if (closingDateTime.isAfter(LocalDateTime.now()) && closingDateTime.isAfter(openingDateTime)
					&& diffInMonths < 6) {
				return true;
			} else {
				return false;
			}
			}
		} catch (NumberFormatException e) {
			return false;

		}
	}
	private boolean isHomeworkUpdateNumberOfTasksLengthValid(String numberOfTasks){
		if(numberOfTasks.length() >= 10){
			return false;
		}
		return true;
	}
	private boolean isHomeworkUpdateNumberOfTasksValid(int numberOfTasks){
		if(numberOfTasks >= 1 && numberOfTasks <= 40){
			return true;
		}
		return false;
	}
	private boolean isHomeworkUpdateContentTypeValid(Part file) {
		String contentType = file.getSubmittedFileName().substring(file.getSubmittedFileName().indexOf("."));
		if (!(contentType.equals(".pdf"))) {
			return false;
		}
		return true;
	}


	private boolean isHomeworkUpdateSizeValid(Part file) {
		long sizeInMb = file.getSize() / (1024 * 1024);
		if (sizeInMb > 20) {
			return false;
		}
		return true;
	}
	
	private boolean doAllGroupsExistHomeworkUpdate(String[] selectedGroups){
		for(String groupId: selectedGroups){
			try {
				Group currGroup = GroupDAO.getInstance().getGroupById(Integer.parseInt(groupId));
				String groupName = currGroup.getName();
				if(ValidationsDAO.getInstance().isGroupNameUnique(groupName)){
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
	}}
