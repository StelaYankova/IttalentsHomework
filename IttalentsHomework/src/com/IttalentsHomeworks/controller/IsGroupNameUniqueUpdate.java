package com.IttalentsHomeworks.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.DAO.ValidationsDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.model.Group;

/**
 * Servlet implementation class IsGroupNameUniqueUpdate
 */
@WebServlet("/IsGroupNameUniqueUpdate")
public class IsGroupNameUniqueUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("name").trim();
		Group currGroup = (Group) request.getSession().getAttribute("currGroup");
		int currGroupId = currGroup.getId();
		try {
			int wantedGroupNameId = GroupDAO.getInstance().getGroupIdByGroupName(groupName);

			if(ValidationsDAO.getInstance().isGroupNameUnique(groupName)){
				response.setStatus(200);
			}else{
				if(wantedGroupNameId == currGroupId){
					response.setStatus(200);
				}else{
					response.setStatus(400);
				}
			}
		} catch (GroupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
