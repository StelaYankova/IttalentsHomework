package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IsPasswordValid
 */
@WebServlet("/IsPasswordValid")
public class IsPasswordValid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String password = request.getParameter("password").trim();
		if(isLengthValid(password) && areCharactersValid(password)){
			response.setStatus(200);
		}else{
			response.setStatus(400);
		}
	}

	private boolean isLengthValid(String password) {
		if (password.length() >= 6 && password.length() <= 15) {
			return true;
		}
		return false;
	}

	private boolean areCharactersValid(String password) {
		for(int i = 0; i < password.length(); i++){
			if(!(((int)password.charAt(i) >= 48 && (int)password.charAt(i) <= 57) || ((int)password.charAt(i) >= 65 && (int)password.charAt(i) <= 90) || ((int)password.charAt(i) >= 97 && (int)password.charAt(i) <= 122))){
				return false;
			}
		}
		return true;
	}
}
