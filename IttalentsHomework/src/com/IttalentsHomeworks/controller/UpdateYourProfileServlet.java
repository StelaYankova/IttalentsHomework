package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.Teacher;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class UpdateYourProfileServlet
 */
@WebServlet("/UpdateYourProfileServlet")
public class UpdateYourProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("updateProfile.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getId();
		String username = user.getUsername();
		String password = request.getParameter("password");
		String repeatedPassword = request.getParameter("repeatedPassword");
		String email = request.getParameter("email");
		//TODO add validations
		if(password.equals(repeatedPassword) && isEmailValid(email) && isLengthValidPass(password) && areCharactersValidPass(password)){
			User newUser = null;
			if(user.isTeacher()){
				newUser = new Teacher(username, password,email);
			}else{
				newUser = new Student(username, password,email);
			}
			newUser.setId(userId);
			try {
				UserDAO.getInstance().updateUser(newUser);
			} catch (UserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else{
			//TODO
		}
	}
		
		private boolean isLengthValidPass(String password) {
			if (password.length() >= 6 && password.length() <= 15) {
				return true;
			}
			return false;
		}

		private boolean areCharactersValidPass(String password) {
			for(int i = 0; i < password.length(); i++){
				if(!(((int)password.charAt(i) >= 48 && (int)password.charAt(i) <= 57) || ((int)password.charAt(i) >= 65 && (int)password.charAt(i) <= 90) || ((int)password.charAt(i) >= 97 && (int)password.charAt(i) <= 122))){
					return false;
				}
			}
			return true;
		}
		
		private boolean isEmailValid(String email){
			 String regex = "^(.+)@(.+)$";
		      Pattern pattern = Pattern.compile(regex);
		      Matcher matcher = pattern.matcher((CharSequence) email);
		         System.out.println(email + " : " + matcher.matches());
		         
				return matcher.matches();
		}
	}
	


