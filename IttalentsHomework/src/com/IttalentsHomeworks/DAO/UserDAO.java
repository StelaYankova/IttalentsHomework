package com.IttalentsHomeworks.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.IttalentsHomeworks.DB.DBManager;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.Homework;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.Task;
import com.IttalentsHomeworks.model.Teacher;
import com.IttalentsHomeworks.model.User;
import java.sql.Connection;

public class UserDAO {
	private static UserDAO instance;
	private DBManager manager;
	
	private UserDAO() {
		setManager(DBManager.getInstance());
	}

	public static UserDAO getInstance() {
		if (instance == null)
			instance = new UserDAO();
		return instance;
	}

	public DBManager getManager() {
		return manager;
	}

	public void setManager(DBManager manager) {
		this.manager = manager;
	}

	public boolean isUserATeacher(int userId) throws UserException{
		Connection con = manager.getConnection();
		boolean isTeacher = false;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT isTeacher FROM IttalentsHomeworks.Users WHERE id = ?;");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next() == true){
				isTeacher = true;
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking if user is a teacher..");
		}
		
		return isTeacher;
	}
	
	public int getUserIdByUsername(String username) throws UserException{
		Connection con = manager.getConnection();
		int userId = 0;
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("SELECT id FROM IttalentsHomeworks.Users WHERE username = ?;");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			userId = rs.getInt(1);
		} catch (SQLException e) {
			throw new UserException("Something went wrong with getting user's id by username..");
		}
		return userId;
	}
	
	
	public ArrayList<Group> getGroupsOfUser(int userId) throws UserException, GroupException{
		Connection con = manager.getConnection();
		ArrayList<Group> groupsOfUser = new ArrayList<>();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(
					"SELECT CONCAT(G.id) AS 'group_id', G.group_name FROM IttalentsHomeworks.User_has_Group UG JOIN IttalentsHomeworks.Groups G ON (G.id = UG.group_id) WHERE UG.user_id = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				Group currGroup = new Group(rs.getInt(1), rs.getString(2));
				ArrayList<Teacher> teachersOfGroup = GroupDAO.getInstance().getTeachersOfGroup(currGroup);
				ArrayList<Student> studentsOfGroup = GroupDAO.getInstance().getStudentsOfGroup(currGroup);
				ArrayList<HomeworkDetails> homeworkDetailsOfGroup = GroupDAO.getInstance().getHomeworksDetailsOfGroup(currGroup);
				groupsOfUser.add(new Group(rs.getInt(1), rs.getString(2), teachersOfGroup, studentsOfGroup, homeworkDetailsOfGroup));
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with getting user's groups..");
		}
		
		return groupsOfUser;
	}
	
	public ArrayList<Homework> getHomeworksOfStudentByGroup(Student s, Group g) throws UserException{
		ArrayList<Homework> homeworksOfStudentByGroup = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT H.id, H.heading, H.num_of_tasks, H.tasks_pdf, H.opens, H.closes, UH.teacher_grade, UH.comments FROM IttalentsHomeworks.User_has_homework UH JOIN IttalentsHomeworks.Homework H ON (H.id = UH.homework_id) JOIN IttalentsHomeworks.Group_has_Homework GH ON (H.id = GH.homework_id) WHERE UH.user_id = ? AND GH.group_id = ?;");
			ps.setInt(1, s.getId());
			ps.setInt(2, g.getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				String openingTimeString = rs.getString(5); 
				String closingTimeString = rs.getString(6); 
				LocalDateTime openingTime = LocalDateTime.parse(openingTimeString, formatter);
				LocalDateTime closingTime = LocalDateTime.parse(closingTimeString, formatter);
				HomeworkDetails hd = new HomeworkDetails(rs.getInt(1), rs.getString(2), openingTime, closingTime, rs.getInt(3), rs.getString(4));
				ArrayList<Task> tasksOfHomeworkOfStudent = UserDAO.getInstance().getTasksOfHomeworkOfStudent(s, hd);
				homeworksOfStudentByGroup.add(new Homework(rs.getInt(7), rs.getString(8), tasksOfHomeworkOfStudent, hd));
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking the homeworks of a student by group..");
		}
		
		return homeworksOfStudentByGroup;
	}
	
	public ArrayList<Task> getTasksOfHomeworkOfStudent(Student s, HomeworkDetails hd) throws UserException{
		ArrayList<Task> tasksOfHomeworkOfStudent = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT CONCAT(H.id) AS 'homework_id', HTS.task_number, HTS.solution_java, HTS.uploaded_on FROM IttalentsHomeworks.User_has_homework UH JOIN IttalentsHomeworks.Homework H ON (H.id = UH.homework_id) JOIN IttalentsHomeworks.Homework_task_solution HTS ON (H.id = HTS.homework_id) WHERE UH.user_id = ? AND H.id = ?;");
			ps.setInt(1, s.getId());
			ps.setInt(2, hd.getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				String uploadedOnString = rs.getString(4); 
				LocalDateTime uploadedOn = LocalDateTime.parse(uploadedOnString, formatter);
				tasksOfHomeworkOfStudent.add(new Task(rs.getInt(2), rs.getString(3), uploadedOn));
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking the tasks of the homework of a student..");
		}
		return tasksOfHomeworkOfStudent;
	}
	
	public User getUserByUsername(String username) throws UserException, GroupException{
		User u = null;
		Connection con = manager.getConnection();
		int userId = UserDAO.getInstance().getUserIdByUsername(username);
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM IttalentsHomeworks.Users WHERE id = ?;");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				ArrayList<Group> groupsOfUser = UserDAO.getInstance().getGroupsOfUser(UserDAO.getInstance().getUserIdByUsername(username));
				if(isUserATeacher(userId)){
					u = new Teacher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), groupsOfUser);
					
				}else{
					ArrayList<Homework> homeworksOfStudent = UserDAO.getInstance().getHomeworksOfStudent((Student) UserDAO.getInstance().getUserByUsername(username));
					u = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), groupsOfUser, homeworksOfStudent);
					
				}
				
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with getting user by username..");
		}
		
		return u;
	}
	
	//all homeworks
	public ArrayList<Homework> getHomeworksOfStudent(Student s) throws UserException {
		ArrayList<Homework> homeworksOfStudent = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT H.id, H.heading, H.num_of_tasks, H.tasks_pdf, H.opens, H.closes, UH.teacher_grade, UH.comments FROM IttalentsHomeworks.User_has_homework UH JOIN IttalentsHomeworks.Homework H ON (H.id = UH.homework_id) WHERE UH.user_id = 3;");
			ps.setInt(1, s.getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				String openingTimeString = rs.getString(5); 
				String closingTimeString = rs.getString(6); 
				LocalDateTime openingTime = LocalDateTime.parse(openingTimeString, formatter);
				LocalDateTime closingTime = LocalDateTime.parse(closingTimeString, formatter);
				HomeworkDetails hd = new HomeworkDetails(rs.getInt(1), rs.getString(2), openingTime, closingTime, rs.getInt(3), rs.getString(4));
				ArrayList<Task> tasksOfHomeworkOfStudent = UserDAO.getInstance().getTasksOfHomeworkOfStudent(s, hd);
				homeworksOfStudent.add(new Homework(rs.getInt(7), rs.getString(8), tasksOfHomeworkOfStudent, hd));
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking the homeworks of a student by group..");
		}
		return homeworksOfStudent;
	}

	public boolean isUsernameUnique(String username) throws UserException{
		boolean isUsernameUnique = true;
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id FROM IttalentsHomeworks.Users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				isUsernameUnique = false;
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking if username is unique");
		}
		return isUsernameUnique;
	}
	
	public boolean isPasswordValid(String pass){
		boolean isPasswordValid = true;
		if(pass.length() >= 6 && pass.length() <= 10){
			for (int i = 0; i < pass.length(); i++) {
				if (!(((int) pass.charAt(i) > 47 && (int) pass.charAt(i) < 58)
						|| ((int) pass.charAt(i) > 64 && (int) pass.charAt(i) < 91)
						|| ((int) pass.charAt(i) > 96 && (int) pass.charAt(i) < 123))) {
					isPasswordValid = false;
					break;
				}
			}
		}
		return isPasswordValid;
	}
	
	public void createNewUser(User u) throws UserException{
		Connection con = manager.getConnection();
		if (UserDAO.getInstance().isUsernameUnique(u.getUsername())
				&& UserDAO.getInstance().isPasswordValid(u.getPassword())) {
			try {
				PreparedStatement ps = con.prepareStatement(
						"INSERT INTO IttalentsHomeworks.Users (username, pass, email) VALUES (?,?,?);");
				ps.setString(1, u.getUsername());
				ps.setString(2, u.getPassword());
				ps.setString(3, u.getEmail());
				ps.execute();

			} catch (SQLException e) {
				throw new UserException("Something went wrong with adding new user to DB..");
			}
		}
	}
}
