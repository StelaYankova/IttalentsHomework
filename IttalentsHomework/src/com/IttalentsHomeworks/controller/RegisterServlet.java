package com.IttalentsHomeworks.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.IttalentsHomeworks.DAO.UserDAO;
import com.IttalentsHomeworks.DAO.ValidationsDAO;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.Exceptions.ValidationException;
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("registerPage.jsp").forward(req, resp);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		String repeatedPassword = request.getParameter("repeatedPassword").trim();
		String email = request.getParameter("email").trim();
		User userTry = new Student(username, password, repeatedPassword, email);// by default
		request.setAttribute("userTry", userTry);
		//not null
		if (isThereEmptyField(username, password, repeatedPassword, email)) {
			request.setAttribute("emptyFields", true);
		} else {
			// uniqueUsername
			boolean isUsernameUnique = false;
			if (isUsernameUnique(username)) {
				isUsernameUnique = true;
			}
			request.setAttribute("uniqueUsername", isUsernameUnique);

			// validUsername
			boolean isUsernameValid = false;
			if (isLengthValidUsername(username) && areCharactersValidUsername(username)) {
				isUsernameValid = true;
			}
			request.setAttribute("validUsername", isUsernameValid);

			// validPass
			boolean isPassValid = false;
			if (isLengthValidPass(password) && areCharactersValidPass(password)) {
				isPassValid = true;
			}
			request.setAttribute("validPass", isPassValid);

			// validRepeatedPass
			boolean isRepeatedPassValid = false;
			if (arePassAndRepeatedPassEqual(password, repeatedPassword)) {
				isRepeatedPassValid = true;
			}
			request.setAttribute("validRepeatedPass", isRepeatedPassValid);

			// validEmail
			boolean isEmailValid = false;
			if (isEmailValid(email)) {
				isEmailValid = true;
			}
			request.setAttribute("validEmail", isEmailValid);

			if (isUsernameUnique == true && isUsernameValid == true && isPassValid == true
					&& isRepeatedPassValid == true && isEmailValid == true) {
				// we create user
				User user = new Student(username, password, repeatedPassword, email);// by default
				try {
					UserDAO.getInstance().createNewUser(user);
					request.setAttribute("invalidFields", false);
				} catch (UserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ValidationException e) {
					request.setAttribute("invalidFields", true);
				}

			}
		}
		request.getRequestDispatcher("registerPage.jsp").forward(request, response);
	}
	private boolean isLengthValidUsername(String username) {
		if (username.length() >= 6 && username.length() <= 15) {
			return true;
		}
		return false;
	}

	private boolean isUsernameUnique(String username){
		boolean isUnique = false;
		try {
			isUnique = ValidationsDAO.getInstance().isUsernameUnique(username);
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isUnique;
	}
	private boolean areCharactersValidUsername(String username) {
		for(int i = 0; i < username.length(); i++){
			if(!(((int)username.charAt(i) >= 48 && (int)username.charAt(i) <= 57) || ((int)username.charAt(i) >= 65 && (int)username.charAt(i) <= 90) || ((int)username.charAt(i) >= 97 && (int)username.charAt(i) <= 122))){
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
			return matcher.matches();
	}
	
	private boolean arePassAndRepeatedPassEqual(String pass, String repeatedPass){
		if(pass.equals(repeatedPass)){
			return true;
		}
		return false;
	}
	private boolean isThereEmptyField(String username, String password, String repeatedPassword, String email){
		if(username == null || username.equals("") ||password == null || password.equals("") ||repeatedPassword == null || repeatedPassword.equals("") ||email == null || email.equals("")){
			return true;
		}
		return false;
	}
}
