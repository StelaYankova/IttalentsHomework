package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IsUsernameValid
 */
@WebServlet("/IsUsernameValid")
public class IsUsernameValid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username").trim();
		if(isLengthValid(username) && areCharactersValid(username)){
			response.setStatus(200);
		}else{
			response.setStatus(400);
		}
	}

	private boolean isLengthValid(String username) {
		if (username.length() >= 6 && username.length() <= 15) {
			return true;
		}
		return false;
	}

	private boolean areCharactersValid(String username) {
		for(int i = 0; i < username.length(); i++){
			if(!(((int)username.charAt(i) >= 48 && (int)username.charAt(i) <= 57) || ((int)username.charAt(i) >= 65 && (int)username.charAt(i) <= 90) || ((int)username.charAt(i) >= 97 && (int)username.charAt(i) <= 122))){
				return false;
			}
		}
		return true;
	}
}
