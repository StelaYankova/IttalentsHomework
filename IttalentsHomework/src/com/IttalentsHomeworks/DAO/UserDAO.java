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

public class UserDAO implements IUserDAO {
	private static IUserDAO instance;
	private DBManager manager;
	
	private UserDAO() {
		setManager(DBManager.getInstance());
	}

	public static IUserDAO getInstance() {
		if (instance == null)
			instance = new UserDAO();
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#getManager()
	 */
	@Override
	public DBManager getManager() {
		return manager;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#setManager(com.IttalentsHomeworks.DB.DBManager)
	 */
	@Override
	public void setManager(DBManager manager) {
		this.manager = manager;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#isUserATeacher(int)
	 */
	@Override
	public boolean isUserATeacher(int userId) throws UserException{
		Connection con = manager.getConnection();
		boolean isTeacher = false;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT isTeacher FROM IttalentsHomeworks.Users WHERE id = ?;");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){//TODO
				isTeacher = rs.getBoolean(1);
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking if user is a teacher..");
		}
		
		return isTeacher;
	}
	
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#getUserIdByUsername(java.lang.String)
	 */
	@Override
	public int getUserIdByUsername(String username) throws UserException{
		Connection con = manager.getConnection();
		int userId = 0;
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("SELECT id FROM IttalentsHomeworks.Users WHERE username = ?;");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				userId = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with getting user's id by username..");
		}
		return userId;
	}
	
	
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#getGroupsOfUser(int)
	 */
	@Override
	public ArrayList<Group> getGroupsOfUser(int userId) throws UserException, GroupException{
		Connection con = manager.getConnection();
		ArrayList<Group> groupsOfUser = new ArrayList<>();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(
					"SELECT CONCAT(G.id) AS 'group_id', G.group_name FROM IttalentsHomeworks.User_has_Group UG JOIN IttalentsHomeworks.Groups G ON (G.id = UG.group_id) WHERE UG.user_id = ?");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
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
	
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#getHomeworksOfStudentByGroup(com.IttalentsHomeworks.model.Student, com.IttalentsHomeworks.model.Group)
	 */
	@Override
	public ArrayList<Homework> getHomeworksOfStudentByGroup(int sId, Group g) throws UserException{
		ArrayList<Homework> homeworksOfStudentByGroup = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT H.id, H.heading, H.num_of_tasks, H.tasks_pdf, H.opens, H.closes, UH.teacher_grade, UH.teacher_comment FROM IttalentsHomeworks.User_has_homework UH JOIN IttalentsHomeworks.Homework H ON (H.id = UH.homework_id) JOIN IttalentsHomeworks.Group_has_Homework GH ON (H.id = GH.homework_id) WHERE UH.user_id = ? AND GH.group_id = ?;");
			ps.setInt(1, sId);
			ps.setInt(2, g.getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				String openingTimeString = rs.getString(5); 
				String closingTimeString = rs.getString(6); 
				LocalDateTime openingTime = LocalDateTime.parse(openingTimeString, formatter);
				LocalDateTime closingTime = LocalDateTime.parse(closingTimeString, formatter);
				HomeworkDetails hd = new HomeworkDetails(rs.getInt(1), rs.getString(2), openingTime, closingTime, rs.getInt(3), rs.getString(4));
				ArrayList<Task> tasksOfHomeworkOfStudent = UserDAO.getInstance().getTasksOfHomeworkOfStudent(sId, hd);
				homeworksOfStudentByGroup.add(new Homework(rs.getInt(7), rs.getString(8), tasksOfHomeworkOfStudent, hd));
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking the homeworks of a student by group..");
		}
		
		return homeworksOfStudentByGroup;
	}
	
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#getTasksOfHomeworkOfStudent(com.IttalentsHomeworks.model.Student, com.IttalentsHomeworks.model.HomeworkDetails)
	 */
	@Override
	public ArrayList<Task> getTasksOfHomeworkOfStudent(int sId, HomeworkDetails hd) throws UserException{
		ArrayList<Task> tasksOfHomeworkOfStudent = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT CONCAT(H.id) AS 'homework_id', HTS.task_number, HTS.solution_java, HTS.uploaded_on FROM IttalentsHomeworks.User_has_homework UH JOIN IttalentsHomeworks.Homework H ON (H.id = UH.homework_id) JOIN IttalentsHomeworks.Homework_task_solution HTS ON (H.id = HTS.homework_id) WHERE UH.user_id = ? AND H.id = ?;");
			ps.setInt(1, sId);
			ps.setInt(2, hd.getId());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				String uploadedOnString = null;
				if(rs.getString(4) == null){
					uploadedOnString = "";
					tasksOfHomeworkOfStudent.add(new Task(rs.getInt(2), rs.getString(3), null));
				}else{
					uploadedOnString = rs.getString(4); 
					LocalDateTime uploadedOn = LocalDateTime.parse(uploadedOnString, formatter);
					tasksOfHomeworkOfStudent.add(new Task(rs.getInt(2), rs.getString(3), uploadedOn));
				}
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking the tasks of the homework of a student..");
		}
		return tasksOfHomeworkOfStudent;
	}
	
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#getUserByUsername(java.lang.String)
	 */
	@Override
	public User getUserByUsername(String username) throws UserException, GroupException{
		User u = null;
		Connection con = manager.getConnection();
		int userId = UserDAO.getInstance().getUserIdByUsername(username);
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM IttalentsHomeworks.Users WHERE id = ?;");
			ps.setInt(1, userId);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				ArrayList<Group> groupsOfUser = UserDAO.getInstance().getGroupsOfUser(UserDAO.getInstance().getUserIdByUsername(username));
				if(UserDAO.getInstance().isUserATeacher(userId)){
					u = new Teacher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), groupsOfUser);
				}else{
					int id = UserDAO.getInstance().getUserIdByUsername(username);
					ArrayList<Homework> homeworksOfStudent = UserDAO.getInstance().getHomeworksOfStudent(id);
					u = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), groupsOfUser, homeworksOfStudent);
				}
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with getting user by username..");
		}
		
		return u;
	}
	
	//all homeworks
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#getHomeworksOfStudent(com.IttalentsHomeworks.model.Student)
	 */
	@Override
	public ArrayList<Homework> getHomeworksOfStudent(int userId) throws UserException {
		ArrayList<Homework> homeworksOfStudent = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT H.id, H.heading, H.num_of_tasks, H.tasks_pdf, H.opens, H.closes, UH.teacher_grade, UH.teacher_comment FROM IttalentsHomeworks.User_has_homework UH JOIN IttalentsHomeworks.Homework H ON (H.id = UH.homework_id) WHERE UH.user_id = ?;");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				String openingTimeString = rs.getString(5); 
				String closingTimeString = rs.getString(6); 
				LocalDateTime openingTime = LocalDateTime.parse(openingTimeString, formatter);
				LocalDateTime closingTime = LocalDateTime.parse(closingTimeString, formatter);
				HomeworkDetails hd = new HomeworkDetails(rs.getInt(1), rs.getString(2), openingTime, closingTime, rs.getInt(3), rs.getString(4));
				ArrayList<Task> tasksOfHomeworkOfStudent = UserDAO.getInstance().getTasksOfHomeworkOfStudent(userId, hd);
				int teacherScore = 0;
				if(rs.getInt(7) != 0){
					teacherScore = rs.getInt(7);
				}
				String teacherComment = " ";
				if(rs.getString(8) != null){
					teacherComment = rs.getString(8);
				}
				
				homeworksOfStudent.add(new Homework(teacherScore, teacherComment, tasksOfHomeworkOfStudent, hd));
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking the homeworks of a student by group..");
		}

		return homeworksOfStudent;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#isUsernameUnique(java.lang.String)
	 */
	@Override
	public boolean isUsernameUnique(String username) throws UserException{
		boolean isUsernameUnique = true;
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM IttalentsHomeworks.Users WHERE username = ?;");
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
	
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#isPasswordValid(java.lang.String)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#createNewUser(com.IttalentsHomeworks.model.User)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#removeUserProfile(com.IttalentsHomeworks.model.User)
	 */
	@Override
	public void removeUserProfile(User u) throws UserException{
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM IttalentsHomeworks.Users WHERE id = ?;");
			ps.setInt(1, u.getId());
			ps.execute();
		} catch (SQLException e) {
			throw new UserException("Something went wrong with removing the profile of a user.."  + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#setTeacherGrade(com.IttalentsHomeworks.model.HomeworkDetails, com.IttalentsHomeworks.model.Student, int)
	 */
	@Override
	public void setTeacherGrade(HomeworkDetails hd, Student st, int teacherGrade) throws UserException{
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE IttalentsHomeworks.User_has_homework SET teacher_grade = ? WHERE user_id = ? AND homework_id = ?;");
			ps.setInt(1, teacherGrade);
			ps.setInt(2, st.getId());
			ps.setInt(3, hd.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new UserException("Something went wrong with setting the teacher's grade of homework..");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#setTeacherComment(com.IttalentsHomeworks.model.HomeworkDetails, com.IttalentsHomeworks.model.Student, java.lang.String)
	 */
	@Override
	public void setTeacherComment(HomeworkDetails hd, Student st, String teacherComment) throws UserException{
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE IttalentsHomeworks.User_has_homework SET teacher_comment = ? WHERE user_id = ? AND homework_id = ?;");
			ps.setString(1, teacherComment);
			ps.setInt(2, st.getId());
			ps.setInt(3, hd.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new UserException("Something went wrong with setting the teacher's comment of homework..");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#setSolutionOfTask(com.
	 * IttalentsHomeworks.model.HomeworkDetails,
	 * com.IttalentsHomeworks.model.Student, com.IttalentsHomeworks.model.Task,
	 * java.lang.String, java.time.LocalDateTime)
	 */
	@Override
	public void setSolutionOfTask(HomeworkDetails hd, Student st, int taskNum, String solution,
			LocalDateTime timeOfUpload) throws UserException {
		Connection con = manager.getConnection();
		if (UserDAO.getInstance().isTaskNumberValid(st.getId(),hd.getId(),taskNum)) {
			try {
				con.setAutoCommit(false);
				try {
					PreparedStatement ps = con.prepareStatement(
							"UPDATE IttalentsHomeworks.Homework_task_solution SET solution_java = ? WHERE student_id = ? AND homework_id = ? AND task_number = ?;");
					ps.setString(1, solution);
					ps.setInt(2, st.getId());
					ps.setInt(3, hd.getId());
					ps.setInt(4, taskNum);
					ps.executeUpdate();

					UserDAO.getInstance().setTimeOfUploadOfTask(hd, st, taskNum, timeOfUpload);

					con.commit();
				} catch (SQLException e) {
					con.rollback();
					throw new UserException("Something went wrong with setting student's solution of task..");
				} finally {
					con.setAutoCommit(true);
				}
			} catch (SQLException e1) {
				throw new UserException("Something went wrong with setting student's solution of task..");
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#setTimeOfUploadOfTask(com.IttalentsHomeworks.model.HomeworkDetails, com.IttalentsHomeworks.model.Student, com.IttalentsHomeworks.model.Task, java.time.LocalDateTime)
	 */
	@Override
	public void setTimeOfUploadOfTask(HomeworkDetails hd, Student st, int taskNum, LocalDateTime timeOfUpload)
			throws UserException {
		Connection con = manager.getConnection();
		if (UserDAO.getInstance().isTaskNumberValid(st.getId(), hd.getId(), taskNum)) {
			try {
				PreparedStatement ps = con.prepareStatement(
						"UPDATE IttalentsHomeworks.Homework_task_solution SET uploaded_on = ? WHERE student_id = ? AND homework_id = ? AND task_number = ?;");
				ps.setString(1, timeOfUpload.toString());
				ps.setInt(2, st.getId());
				ps.setInt(3, hd.getId());
				ps.setInt(4, taskNum);
				ps.executeUpdate();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new UserException(
						"Something went wrong with setting the upload time of student's solution of task..");
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#doesTaskAlreadyExist(com.IttalentsHomeworks.model.HomeworkDetails, com.IttalentsHomeworks.model.Student, com.IttalentsHomeworks.model.Task)
	 */
	@Override
	public boolean doesTaskAlreadyExist(HomeworkDetails hd, Student st, Task t) throws UserException{
		boolean doesExist = false;
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM IttalentsHomeworks.Homework_task_solution WHERE student_id = ? AND homework_id = ? AND task_number = ?;");
			ps.setInt(1, st.getId());
			ps.setInt(2, hd.getId());
			ps.setInt(3, t.getTaskNumber());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				doesExist = true;
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking if task in table already exists..");
		}
		return doesExist;
	}
	
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IUserDAO#updateUser(com.IttalentsHomeworks.model.User)
	 */
	@Override
	public void updateUser(User u) throws UserException{
		int id = UserDAO.getInstance().getUserIdByUsername(u.getUsername());
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE IttalentsHomeworks.Users SET pass = ?, email = ? WHERE id = ?");
			ps.setString(1, u.getPassword());
			ps.setString(2, u.getEmail());
			ps.setInt(3, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new UserException("Something went wrong with updating user..");
		}
	}
//we dont have to know his groups
	@Override
	public Student getStudentsByUsername(String username) throws UserException {
			User u = null;
			Connection con = manager.getConnection();
			int userId = UserDAO.getInstance().getUserIdByUsername(username);
			try {
				PreparedStatement ps = con.prepareStatement("SELECT * FROM IttalentsHomeworks.Users WHERE id = ?;");
				ps.setInt(1, userId);
				
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					//ArrayList<Group> groupsOfUser = UserDAO.getInstance().getGroupsOfUser(UserDAO.getInstance().getUserIdByUsername(username));
					//if(UserDAO.getInstance().isUserATeacher(userId)){
						//u = new Teacher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), null);
				//	}else{
						int id = UserDAO.getInstance().getUserIdByUsername(username);
						ArrayList<Homework> homeworksOfStudent = UserDAO.getInstance().getHomeworksOfStudent(id);
						u = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), null, homeworksOfStudent);
				//	}
				}
			} catch (SQLException e) {
				throw new UserException("Something went wrong with getting user by username..");
			}
			
			return (Student) u;
		
	}

	@Override
	public boolean isTaskNumberValid(int studentId, int homeworkId, int taskNum) throws UserException {
		boolean isValid = false;
		Connection con = manager.getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("SELECT COUNT(*) FROM IttalentsHomeworks.Homework_task_solution WHERE student_id = ? AND homework_id = ?;");
			ps.setInt(1, studentId);
			ps.setInt(2, homeworkId);
			System.out.println("zad: " + taskNum);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				if(taskNum < rs.getInt(1)){
					isValid = true;
				}
			}
		} catch (SQLException e) {
System.out.println(e.getMessage());			throw new UserException("Something went wrong with checking if the task number is valid..");
		}
		
		return isValid;
	}
}
