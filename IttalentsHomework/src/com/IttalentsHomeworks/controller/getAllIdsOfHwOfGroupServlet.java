package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.google.gson.JsonArray;

/**
 * Servlet implementation class getAllIdsOfHwOfGroupServlet
 */
//@WebServlet("/getAllIdsOfHwOfGroupServlet")
public class getAllIdsOfHwOfGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int groupId = Integer.parseInt(request.getParameter("groupId"));
		//System.out.println("QQQQQQ "+groupId);
		JsonArray array = new JsonArray();
		ArrayList<HomeworkDetails> allHwDetailsByGroup;
		try {
			Group chosenGroup = GroupDAO.getInstance().getGroupById(groupId);
			allHwDetailsByGroup = GroupDAO.getInstance().getHomeworkDetailsOfGroup(chosenGroup);
			for(HomeworkDetails hd: allHwDetailsByGroup){
				array.add(hd.getId());
				System.out.println(hd.getId());
			}
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write(array.toString());
		
	}

	

}
