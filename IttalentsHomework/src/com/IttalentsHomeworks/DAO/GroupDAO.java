package com.IttalentsHomeworks.DAO;

import java.sql.Connection;
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
import com.IttalentsHomeworks.model.Teacher;

public class GroupDAO {
	
	private static GroupDAO instance;
	private DBManager manager;
	
	private GroupDAO() {
		setManager(DBManager.getInstance());
	}

	public static GroupDAO getInstance() {
		if (instance == null)
			instance = new GroupDAO();
		return instance;
	}

	public DBManager getManager() {
		return manager;
	}

	public void setManager(DBManager manager) {
		this.manager = manager;
	}
	public ArrayList<Teacher> getTeachersOfGroup(Group g) throws GroupException{
		ArrayList<Teacher> teachersOfGroup = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT U.id, U.username, U.email, U.isTeacher FROM IttalentsHomeworks.User_has_Group G JOIN IttalentsHomeworks.Users U ON (G.user_id = U.id) WHERE G.group_id = ? AND U.isTeacher = 1;");
			ps.setInt(1, g.getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				teachersOfGroup.add(new Teacher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4)));
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking the teaches of a group");
		}
		return teachersOfGroup;
	}
	public ArrayList<Student> getStudentsOfGroup(Group g) throws GroupException, UserException{
		ArrayList<Student> studentsOfGroup = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT U.id, U.username, U.email, U.isTeacher FROM IttalentsHomeworks.User_has_Group G JOIN IttalentsHomeworks.Users U ON (G.user_id = U.id) WHERE G.group_id = ? AND U.isTeacher = 0;");
			ps.setInt(1, g.getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				//all homeworks of student, not by group
				Student currStudent = (Student) UserDAO.getInstance().getUserByUsername(rs.getString(2));
				ArrayList<Homework> homeworksOfStudent = UserDAO.getInstance().getHomeworksOfStudent(currStudent);
				studentsOfGroup.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4), homeworksOfStudent));
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking the teaches of a group..");
		}
		return studentsOfGroup;
	}
	
	public ArrayList<HomeworkDetails> getHomeworksDetailsOfGroup(Group g) throws GroupException{
		ArrayList<HomeworkDetails> homeworksOfGroup = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT H.id, H.heading, H.opens, H.closes, H.num_of_tasks, H.tasks_pdf FROM IttalentsHomeworks.Homework H JOIN IttalentsHomeworks.Group_has_Homework GH ON (GH. homework_id = H.id) WHERE GH.group_id = ?;");
			ps.setInt(1, g.getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				String openingTimeString = rs.getString(3); 
				String closingTimeString = rs.getString(4); 
				LocalDateTime openingTime = LocalDateTime.parse(openingTimeString, formatter);
				LocalDateTime closingTime = LocalDateTime.parse(closingTimeString, formatter);

				homeworksOfGroup.add(new HomeworkDetails(rs.getInt(1), rs.getString(2), openingTime, closingTime, rs.getInt(5), rs.getString(6)));
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking the homeworks of a group..");
		}
		
		return homeworksOfGroup;
	}
	
	public boolean isStudentAlreadyInGroup(Group g, Student s) throws GroupException{
		Connection con = manager.getConnection();
		boolean isStudentAlreadyInGroup = false;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM IttalentsHomeworks.User_has_Group WHERE user_id = ? AND group_id = ?;");
			ps.setInt(1, s.getId());
			ps.setInt(2, g.getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				isStudentAlreadyInGroup = true;
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking if student is already in a group..");
		}
		return isStudentAlreadyInGroup;
	}

	public void addStudentToGroup(Group g, Student s) throws GroupException {
		Connection con = manager.getConnection();
		if (!GroupDAO.getInstance().isStudentAlreadyInGroup(g, s)) {
			try {
				PreparedStatement ps = con
						.prepareStatement("INSERT INTO IttalentsHomeworks.User_has_Group VALUES (?,?);");
				ps.setInt(1, s.getId());
				ps.setInt(2, g.getId());
				ps.execute();
			} catch (SQLException e) {
				throw new GroupException("Something went wrong with adding a student to a group..");
			}
		}
	}

	//constructor with teachers
	public void createNewGroup(Group g) throws GroupException {
		Connection con = manager.getConnection();
		try {
			con.setAutoCommit(false);
			if (isGroupNameUnique(g.getName())) {
				try {
					PreparedStatement ps = con
							.prepareStatement("INSERT INTO IttalentsHomeworks.Groups (group_name) VALUE (?);");
					ps.setString(1, g.getName());
					ps.execute();

					for(int i = 0; i < g.getTeachers().size(); i++){
						ps = con.prepareStatement("INSERT INTO IttalentsHomeworks.User_has_Group VALUES (?,?);");
						ps.setInt(1, g.getId());
						ps.setInt(2, g.getTeachers().get(i).getId());
						ps.execute();
					}
					con.commit();
				} catch (SQLException e) {
					con.rollback();
					throw new GroupException("Something went wrong with creating new group..");
				}finally{
					con.setAutoCommit(true);
				}
			}
		} catch (SQLException e1) {
			throw new GroupException("Something went wrong with creating new group..");
		}
		
	}

	public boolean isGroupNameUnique(String groupName) throws GroupException{
		Connection con = manager.getConnection();
		boolean isGroupNameUnique = true;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id FROM IttalentsHomeworks.Groups WHERE group_name = ?");
			ps.setString(1, groupName);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				isGroupNameUnique = false;
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking if group's name is unique..");
		}
		return isGroupNameUnique;
	}
}
