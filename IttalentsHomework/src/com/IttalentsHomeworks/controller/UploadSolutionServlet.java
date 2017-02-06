package com.IttalentsHomeworks.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.Task;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class UploadSolutionServlet
 */
@WebServlet("/UploadSolutionServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50) // 50MB
public class UploadSolutionServlet extends HttpServlet {
	/**
	 * Name of the directory where uploaded files will be saved, relative to the
	 * web application directory.
	 */
	private static final String SAVE_DIR = "/Users/Stela/Desktop/imagesIttalentsHomework";

	/**
	 * handles file upload
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int taskNum = Integer.parseInt(request.getParameter("taskNum")) - 1;
		Homework homework = (Homework) request.getSession().getAttribute("currHomework");
		HomeworkDetails homeworkDetails = homework.getHomeworkDetails();
		User user = (User) request.getSession().getAttribute("user");

		String savePath = SAVE_DIR;

		// creates the save directory if it does not exists
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}
		String fileName = " ";
		Part file = request.getPart("file");
		if (!isFileEmpty(file)) {
			//boolean isContentTypeValid = true;

			if (!isSizeValid(file)) {
				System.out.println("EMPTY");

				request.setAttribute("wrongSize", true);
			} else {

				if (!isContentTypeValid(file)) {
					//isContentTypeValid = false;
					System.out.println("INVALID");
					request.setAttribute("wrongContentType", true);
				}else{
					fileName = "hwId" + homeworkDetails.getId() + "userId" + user.getId() + "taskNum" + taskNum
							+ ".java";
					file.write(savePath + File.separator + fileName);
					try {
						UserDAO.getInstance().setSolutionOfTask(homeworkDetails, (Student) user, taskNum, fileName,
								LocalDateTime.now());
						homework.getTasks().get(taskNum).setSolution(fileName);
						homework.getTasks().get(taskNum).setUploadedOn(LocalDateTime.now());
						request.setAttribute("invalidFields", true);
					} catch (UserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		request.setAttribute("currTaskUpload", taskNum);
		request.getRequestDispatcher("currHomeworkPageStudent.jsp").forward(request, response);
	}

	/**
	 * Extracts file name from HTTP header content-disposition
	 */
	/*private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring((part.getName()).lastIndexOf('/') + 1);
			}
		}
		return "";
	}*/
	private boolean isContentTypeValid(Part file) {
		String contentType = file.getSubmittedFileName().substring(file.getSubmittedFileName().indexOf("."));
		if (!(contentType.equals(".java"))) {
			return false;
		}
		return true;
	}

	private boolean isFileEmpty(Part file) {
		if (file.getSize() != 0) {
			System.out.println("SEZE : " + file.getSize());

			return false;
		}
		return true;
	}

	private boolean isSizeValid(Part file) {
		long sizeInMb = file.getSize() / (1024 * 1024);
		if (sizeInMb >= 1) {
			return false;
		}
		return true;
	}
}