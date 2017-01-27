package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IsGroupNameValid
 */
@WebServlet("/IsGroupNameValid")
public class IsGroupNameValid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("name");
		if(isLengthValid(groupName) && areCharactersValid(groupName)){
			response.setStatus(200);
		}else{
			response.setStatus(400);
		}
	}
	private boolean isLengthValid(String groupName) {
		if (groupName.length() >= 5 && groupName.length() <= 20) {
			return true;
		}
		return false;
	}

	private boolean areCharactersValid(String groupName) {
		for(int i = 0; i < groupName.length(); i++){
			if(!(((int)groupName.charAt(i) >= 32 && (int)groupName.charAt(i) <= 126))){
				return false;
			}
		}
		return true;
	}
	
}
