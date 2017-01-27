package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class IsHomeworkFileValid
 */
@WebServlet("/IsHomeworkFileValid")
@MultipartConfig
public class IsHomeworkFileValid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IsHomeworkFileValid() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Part file = request.getPart("file");
		String contentType = file.getSubmittedFileName().substring(file.getSubmittedFileName().indexOf("."));
		long sizeInMb = file.getSize() / (1024 * 1024);
		if(contentType.equals("pdf") && sizeInMb <=1){
			response.setStatus(200);
		}else{
			response.setStatus(400);
		}
	}*/


}
