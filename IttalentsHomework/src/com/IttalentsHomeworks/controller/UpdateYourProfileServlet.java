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
import com.IttalentsHomeworks.Exceptions.ValidationException;
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
		User userTry = new Student(username, password, repeatedPassword, email);// by default
		request.setAttribute("userTry", userTry);
		User newUser = null;

		//TODO add validations
		/*if(password.equals(repeatedPassword) && isEmailValid(email) && isLengthValidPass(password) && areCharactersValidPass(password)){
			User newUser = null;
			if(user.isTeacher()){
				newUser = new Teacher(username, password, repeatedPassword, email);
			}else{
				newUser = new Student(username, password, repeatedPassword, email);
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
		}*/
		//empty fields
		if(isThereEmptyField(password, repeatedPassword, email)){
			request.setAttribute("emptyFields", true);
		}else{
		//password valid
			boolean isPassValid = false;
			if(isPasswordValid(password)){
				isPassValid = true;
			}
			request.setAttribute("validPass", isPassValid);
		//repeatedPass
			boolean isRepeatedPassValid = false;
			if(arePassAndRepeatedPassEqual(password, repeatedPassword)){
				isRepeatedPassValid = true;
			}
			request.setAttribute("validRepeatedPass", isRepeatedPassValid);
		//email
			boolean isEmailValid = false;
			if(isEmailValid(email)){
				isEmailValid = true;
			}
			request.setAttribute("validEmail", isEmailValid);
			
			if(isPassValid==true && isRepeatedPassValid==true && isEmailValid==true){
				if(user.isTeacher()){
					newUser = new Teacher(username, password, repeatedPassword, email);
				}else{
					newUser = new Student(username, password, repeatedPassword, email);
				}
				newUser.setId(userId);

				try {
					UserDAO.getInstance().updateUser(newUser);
					request.setAttribute("invalidFields", false);
					request.getSession().setAttribute("user", newUser);
				} catch (UserException e) {
					//TODO
				} catch (ValidationException e) {
					request.setAttribute("invalidFields", true);

				}
			}
		}
	
		request.getRequestDispatcher("updateProfile.jsp").forward(request, response);
	}
		
	private boolean isPasswordValid(String password){
		if(isLengthValidPass(password) && areCharactersValidPass(password)){
			return true;
		}
		return false;
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
		private boolean isThereEmptyField(String password, String repeatedPassword, String email){
			if(password == null || password == "" ||repeatedPassword == null || repeatedPassword == "" ||email == null || email == ""){
				return true;
			}
			return false;
		}
		private boolean arePassAndRepeatedPassEqual(String pass, String repeatedPass){
			if(pass.equals(repeatedPass)){
				return true;
			}
			return false;
		}
	}
	


