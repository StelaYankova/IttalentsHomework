package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.GroupDAO;
import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.Student;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SeeYourHomeworksByGroup
 */
@WebServlet("/SeeYourHomeworksByGroup")
public class SeeYourHomeworksByGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<HomeworkDetails> homeworkDetailsByGroup = new ArrayList<>();
		Student user = (Student) request.getSession().getAttribute("user");
		Group selectedGroup = null;
		if (!request.getParameter("selectedGroupId").equals("null")) {
			JsonArray array = new JsonArray();
			if (request.getParameter("selectedGroupId").equals("allGroups")) {
				try {
					ArrayList<Integer> checkedIds = new ArrayList<>();
					for (Group g : user.getGroups()) {
						homeworkDetailsByGroup.addAll(GroupDAO.getInstance().getHomeworkDetailsOfGroup(g));
						for (HomeworkDetails hd : homeworkDetailsByGroup) {
							if (!(checkedIds.contains((Integer) hd.getId()))) {
								JsonObject obj = new JsonObject();
								obj.addProperty("heading", hd.getHeading());
								obj.addProperty("opens", hd.getOpeningTime().toString());
								obj.addProperty("closes", hd.getClosingTime().toString());
								try {
									for (Homework h : UserDAO.getInstance().getHomeworksOfStudent(user.getId())) {
										if (hd.getId() == h.getHomeworkDetails().getId()) {
											int grade = h.getTeacherGrade();
											String comment = h.getTeacherComment();
											obj.addProperty("teacherScore", grade);
											obj.addProperty("teacherComment", comment);
											break;
										}
									}
									checkedIds.add(hd.getId());
								} catch (UserException e) {
									e.printStackTrace();
								}
								array.add(obj);
							}

						}
					}
					response.getWriter().write(array.toString());
				} catch (GroupException e) {
					e.printStackTrace();
				}
			} else {
				int selectedGroupId = Integer.parseInt(request.getParameter("selectedGroupId"));

				try {
					selectedGroup = GroupDAO.getInstance().getGroupById(selectedGroupId);
					homeworkDetailsByGroup.addAll(GroupDAO.getInstance().getHomeworkDetailsOfGroup(selectedGroup));
				} catch (GroupException | UserException e) {
					e.printStackTrace();
				}
				for (HomeworkDetails hd : homeworkDetailsByGroup) {
					JsonObject obj = new JsonObject();
					obj.addProperty("heading", hd.getHeading());
					obj.addProperty("opens", hd.getOpeningTime().toString());
					obj.addProperty("closes", hd.getClosingTime().toString());
					try {
						for (Homework h : UserDAO.getInstance().getHomeworksOfStudentByGroup(user.getId(),
								selectedGroup)) {
							if (hd.getId() == h.getHomeworkDetails().getId()) {
								int grade = h.getTeacherGrade();
								String comment = h.getTeacherComment();
								
								obj.addProperty("teacherScore", grade);
								obj.addProperty("teacherComment", comment);
								break;
							}
						}
					} catch (UserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					array.add(obj);
				}
				response.getWriter().write(array.toString());
			}

		}
	}

}
