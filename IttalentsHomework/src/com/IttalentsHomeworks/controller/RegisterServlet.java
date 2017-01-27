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
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//TODO validations
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		
		
		User user = new Student(username, password, email);//by default
		
		try {
			if(username != null && (!username.isEmpty())){
				boolean isUnique = UserDAO.getInstance().isUsernameUnique(username);
				if((isUnique == true) && isLengthValidUsername(username) && areCharactersValidUsername(username) && isLengthValidPass(username) && areCharactersValidPass(username) && isEmailValid(email)){
					UserDAO.getInstance().createNewUser(user);
					response.setStatus(200);
				}else{
					//request.setAttribute("invalidData", 1);
					//response.setStatus(400);

				}
			}
			response.sendRedirect("registerPage.jsp");

		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private boolean isLengthValidUsername(String password) {
		if (password.length() >= 6 && password.length() <= 15) {
			return true;
		}
		return false;
	}

	private boolean areCharactersValidUsername(String password) {
		for(int i = 0; i < password.length(); i++){
			if(!(((int)password.charAt(i) >= 48 && (int)password.charAt(i) <= 57) || ((int)password.charAt(i) >= 65 && (int)password.charAt(i) <= 90) || ((int)password.charAt(i) >= 97 && (int)password.charAt(i) <= 122))){
				return false;
			}
		}
		return true;
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
