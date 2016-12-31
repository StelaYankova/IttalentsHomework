package com.IttalentsHomeworks.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.Task;
import com.IttalentsHomeworks.model.User;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class ReadJavaFileServlet
 */
@WebServlet("/ReadJavaFileServlet")
public class ReadJavaFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SAVE_DIR = "/Users/Stela/Desktop/imagesIttalentsHomework";

   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int taskNum = Integer.parseInt(request.getParameter("taskNum"))-1;
		User user = (User) request.getSession().getAttribute("user");
		Homework homework = (Homework) request.getSession().getAttribute("currHomework");
		HomeworkDetails homeworkDetails = homework.getHomeworkDetails();
		String fileName = null;
		if(!user.isTeacher()){
		 fileName = SAVE_DIR + File.separator + "hwId"+homeworkDetails.getId() +"userId" +user.getId() +"taskNum"+ taskNum + ".java";
		}else{
			int studentId = (int) request.getSession().getAttribute("studentId");
			 fileName = SAVE_DIR + File.separator + "hwId"+homeworkDetails.getId() +"userId" +studentId +"taskNum"+ taskNum + ".java";

		}
		File f = new File(fileName);
		System.out.println("AUGSS" + fileName);
		String strLine = "";
		if (f.exists()) {
			FileInputStream fstream = new FileInputStream(fileName);

			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			while ((br.readLine()) != null) {
				strLine = strLine.concat(br.readLine() + "\n");
			}

			br.close();
		}else{
			strLine = "Solution is not uploaded yet.";
		}
		JsonObject obj = new JsonObject();
		Task task = homework.getTasks().get(taskNum);
		System.out.println(homework.getTasks().size());

		System.out.println(task);
		obj.addProperty("uploadedOn", task.getUploadedOn().toString());
		obj.addProperty("solution", strLine);
		response.getWriter().write(obj.toString());
	}


}
