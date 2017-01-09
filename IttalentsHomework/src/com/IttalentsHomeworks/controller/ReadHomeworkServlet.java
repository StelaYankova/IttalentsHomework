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

/**
 * Servlet implementation class ReadHomeworkServlet
 */
@WebServlet("/ReadHomeworkServlet")
public class ReadHomeworkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "/Users/Stela/Desktop/imagesIttalentsHomework";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReadHomeworkServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("fileName");
		File file = new File(SAVE_DIR + File.separator + fileName);
		response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
		response.setContentLength((int) file.length());

		FileInputStream fileInputStream = new FileInputStream(file);
		OutputStream responseOutputStream = response.getOutputStream();
		int bytes;
		while ((bytes = fileInputStream.read()) != -1) {
			responseOutputStream.write(bytes);
		}
		/*System.out.println(file);
		FileInputStream fis = null;
		String text = null;
		try {
			fis = new FileInputStream(file);

			int bytes;
			while ((bytes = fis.read()) != -1) {
				// convert to char and display it
				//System.out.print((char) bytes);
				text+=(char) bytes;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		response.setContentType("application/pdf");
		response.getWriter().write(text);*/
		request.getRequestDispatcher("currHomeworkPageStudent.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
