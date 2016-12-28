package com.IttalentsHomeworks.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class UploadSolutionServlet
 */
@WebServlet("/UploadSolutionServlet")
/*@MultipartConfig
public class UploadSolutionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String saveFile = "/Users/Stela/Desktop/imagesIttalentsHomework";
	
   
	@Override

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	
			throws ServletException, IOException {
		
		// process only if its multipart content

		if (ServletFileUpload.isMultipartContent(request)) {
			System.out.println("YAYAYAYAYYA");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
				// File uploaded successfully
			List items = null;
			try {
				items = upload.parseRequest((RequestContext) request);
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Iterator itr = items.iterator();
			while(itr.hasNext()){
				FileItem item = (FileItem) itr.next();
				if(!item.isFormField()){
					String itemname = item.getName();
					if(!(itemname == null || itemname.equals(""))){
						String filename = FilenameUtils.getName(itemname);
						File f = checkExist(filename);
						try {
							item.write(f);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
				request.setAttribute("message", "File Uploaded Successfully");

		}
		request.getRequestDispatcher("/result.jsp").forward(request, response);

	}


	private File checkExist(String filename) {
		File f = new File(saveFile + "/" + filename);
		if(f.exists()){
			StringBuffer sb = new StringBuffer(filename);
			sb.insert(sb.lastIndexOf("."), "-" + new Date().getTime());
			f = new File(saveFile + "/" + sb.toString());
		}
		return f;
	}

}*/
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
		// gets absolute path of the web application
		//String appPath = request.getServletContext().getRealPath("");
		// constructs path of the directory to save uploaded file
		int taskNum = Integer.parseInt(request.getParameter("taskNum"))-1;
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
		for (Part part : request.getParts()) {
			 fileName = extractFileName(part);
			// refines the fileName in case it is an absolute path
			//fileName = new File(fileName).getName();
			 fileName = "hwId"+homeworkDetails.getId() +"userId" +user.getId() +"taskNum"+ taskNum + ".pdf";
			part.write(savePath + File.separator+ fileName);
		}
		try {
			UserDAO.getInstance().setSolutionOfTask(homeworkDetails, (Student) user, taskNum, fileName, LocalDateTime.now());
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Extracts file name from HTTP header content-disposition
	 */
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}
}