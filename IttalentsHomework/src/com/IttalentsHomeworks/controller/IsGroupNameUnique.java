package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;

/**
 * Servlet implementation class IsGroupNameUnique
 */
@WebServlet("/IsGroupNameUnique")
public class IsGroupNameUnique extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("name");
		try {
			if(GroupDAO.getInstance().isGroupNameUnique(groupName)){
				response.setStatus(200);
			}else{
				response.setStatus(400);
			}
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
