package com.IttalentsHomeworks.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class GroupDAO implements IGroupDAO {

	private static IGroupDAO instance;
	private DBManager manager;

	private GroupDAO() {
		setManager(DBManager.getInstance());
	}

	public static IGroupDAO getInstance() {
		if (instance == null)
			instance = new GroupDAO();
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#getManager()
	 */
	@Override
	public DBManager getManager() {
		return manager;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#setManager(com.IttalentsHomeworks.DB.DBManager)
	 */
	@Override
	public void setManager(DBManager manager) {
		this.manager = manager;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#getTeachersOfGroup(com.IttalentsHomeworks.model.Group)
	 */
	@Override
	public ArrayList<Teacher> getTeachersOfGroup(Group g) throws GroupException {
		ArrayList<Teacher> teachersOfGroup = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT U.id, U.username, U.email, U.isTeacher FROM IttalentsHomeworks.User_has_Group G JOIN IttalentsHomeworks.Users U ON (G.user_id = U.id) WHERE G.group_id = ? AND U.isTeacher = 1;");
			ps.setInt(1, g.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				teachersOfGroup.add(new Teacher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4)));
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking the teaches of a group..");
		}
		return teachersOfGroup;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#getStudentsOfGroup(com.IttalentsHomeworks.model.Group)
	 */
	@Override
	public ArrayList<Student> getStudentsOfGroup(Group g) throws GroupException, UserException {
		ArrayList<Student> studentsOfGroup = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT U.id, U.username, U.email, U.isTeacher FROM IttalentsHomeworks.User_has_Group G JOIN IttalentsHomeworks.Users U ON (G.user_id = U.id) WHERE G.group_id = ? AND U.isTeacher = 0;");
			ps.setInt(1, g.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// all homeworks of student, not by group
				// we dont have to know their groups
				Student currStudent = (Student) UserDAO.getInstance().getStudentsByUsername(rs.getString(2));
				ArrayList<Homework> homeworksOfStudent = UserDAO.getInstance().getHomeworksOfStudent(currStudent.getId());
				
				studentsOfGroup.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
						homeworksOfStudent));
				
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking the students of a group..");
		}
		return studentsOfGroup;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#getHomeworksDetailsOfGroup(com.IttalentsHomeworks.model.Group)
	 */
	@Override
	public ArrayList<HomeworkDetails> getHomeworksDetailsOfGroup(Group g) throws GroupException {
		ArrayList<HomeworkDetails> homeworksOfGroup = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT H.id, H.heading, H.opens, H.closes, H.num_of_tasks, H.tasks_pdf FROM IttalentsHomeworks.Homework H JOIN IttalentsHomeworks.Group_has_Homework GH ON (GH. homework_id = H.id) WHERE GH.group_id = ?;");
			ps.setInt(1, g.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String openingTimeString = rs.getString(3);
				String closingTimeString = rs.getString(4);
				LocalDateTime openingTime = LocalDateTime.parse(openingTimeString, formatter);
				LocalDateTime closingTime = LocalDateTime.parse(closingTimeString, formatter);

				homeworksOfGroup.add(new HomeworkDetails(rs.getInt(1), rs.getString(2), openingTime, closingTime,
						rs.getInt(5), rs.getString(6)));
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking the homeworks of a group..");
		}

		return homeworksOfGroup;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#isUserAlreadyInGroup(com.IttalentsHomeworks.model.Group, com.IttalentsHomeworks.model.User)
	 */
	@Override
	public boolean isUserAlreadyInGroup(Group g, User u) throws GroupException {
		Connection con = manager.getConnection();
		boolean isUserAlreadyInGroup = false;
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM IttalentsHomeworks.User_has_Group WHERE user_id = ? AND group_id = ?;");
			ps.setInt(1, u.getId());
			ps.setInt(2, g.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				isUserAlreadyInGroup = true;
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking if user is already in a group..");
		}
		return isUserAlreadyInGroup;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#addUserToGroup(com.IttalentsHomeworks.model.Group, com.IttalentsHomeworks.model.User)
	 */
	@Override
	public void addUserToGroup(Group g, User u) throws GroupException {
		Connection con = manager.getConnection();
		if (!GroupDAO.getInstance().isUserAlreadyInGroup(g, u)) {
			try {
				PreparedStatement ps = con
						.prepareStatement("INSERT INTO IttalentsHomeworks.User_has_Group VALUES (?,?);");
				ps.setInt(1, u.getId());
				ps.setInt(2, g.getId());
				ps.execute();
			} catch (SQLException e) {
				throw new GroupException("Something went wrong with adding a user to a group..");
			}
		}
	}

	// constructor with teachers
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#createNewGroup(com.IttalentsHomeworks.model.Group)
	 */
	@Override
	public void createNewGroup(Group g) throws GroupException {
		Connection con = manager.getConnection();
		try {
			con.setAutoCommit(false);
			if (GroupDAO.getInstance().isGroupNameUnique(g.getName())) {
				PreparedStatement ps = con
						.prepareStatement("INSERT INTO IttalentsHomeworks.Groups (group_name) VALUE (?);");
				ps.setString(1, g.getName());
				ps.executeUpdate();
				g.setId(GroupDAO.getInstance().getGroupIdByGroupName(g));
				for (int i = 0; i < g.getTeachers().size(); i++) {
					ps = con.prepareStatement("INSERT INTO IttalentsHomeworks.User_has_Group VALUES (?,?);");
					ps.setInt(1, g.getTeachers().get(i).getId());
					ps.setInt(2, g.getId());
					ps.executeUpdate();
				}
				con.commit();	

			}
		} catch (SQLException e1) {
			if (con != null) {
	            try {
	                con.rollback();
	            } catch(SQLException excep) {
	    			throw new GroupException("Something went wrong with creating new group..");
	            }
	        }
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				throw new GroupException("Something went wrong with creating new group..");
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#isGroupNameUnique(java.lang.String)
	 */
	@Override
	public boolean isGroupNameUnique(String groupName) throws GroupException {
		Connection con = manager.getConnection();
		boolean isGroupNameUnique = true;
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT id FROM IttalentsHomeworks.Groups WHERE group_name = ?");
			ps.setString(1, groupName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				isGroupNameUnique = false;
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking if group's name is unique..");
		}
		return isGroupNameUnique;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#getAllHomeworksDetails()
	 */
	@Override
	public ArrayList<HomeworkDetails> getAllHomeworksDetails() throws GroupException {
		ArrayList<HomeworkDetails> homeworksOfGroup = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT H.id, H.heading, H.opens, H.closes, H.num_of_tasks, H.tasks_pdf FROM IttalentsHomeworks.Homework H;");
			while (rs.next()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String openingTimeString = rs.getString(3);
				String closingTimeString = rs.getString(4);
				LocalDateTime openingTime = LocalDateTime.parse(openingTimeString, formatter);
				LocalDateTime closingTime = LocalDateTime.parse(closingTimeString, formatter);

				homeworksOfGroup.add(new HomeworkDetails(rs.getInt(1), rs.getString(2), openingTime, closingTime,
						rs.getInt(5), rs.getString(6)));
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking all homework details..");
		}

		return homeworksOfGroup;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#getAllGroups()
	 */
	@Override
	public ArrayList<Group> getAllGroups() throws UserException, GroupException {
		Connection con = manager.getConnection();
		ArrayList<Group> groups = new ArrayList<>();
		Statement st;
		try {
			st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT CONCAT(G.id) AS 'group_id', G.group_name FROM IttalentsHomeworks.Groups G;");
			while (rs.next()) {
				Group currGroup = new Group(rs.getInt(1), rs.getString(2));
				ArrayList<Teacher> teachersOfGroup = GroupDAO.getInstance().getTeachersOfGroup(currGroup);
				ArrayList<Student> studentsOfGroup = GroupDAO.getInstance().getStudentsOfGroup(currGroup);
				ArrayList<HomeworkDetails> homeworkDetailsOfGroup = GroupDAO.getInstance()
						.getHomeworksDetailsOfGroup(currGroup);
				groups.add(new Group(rs.getInt(1), rs.getString(2), teachersOfGroup, studentsOfGroup,
						homeworkDetailsOfGroup));
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with getting groups.." + e.getMessage());
		}

		return groups;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#removeUserFromGroup(com.IttalentsHomeworks.model.Group, com.IttalentsHomeworks.model.Student)
	 */
	@Override
	public void removeUserFromGroup(Group group, User u) throws GroupException {
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"DELETE FROM IttalentsHomeworks.User_has_Group WHERE user_id = ? AND group_id = ?;");
			ps.setInt(1, u.getId());
			ps.setInt(2, group.getId());
			ps.execute();
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with removing a student from a group..");
		}

	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#removeGroup(com.IttalentsHomeworks.model.Group)
	 */
	@Override
	public void removeGroup(Group g) throws GroupException {
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM IttalentsHomeworks.Groups WHERE id = ?;");
			ps.setInt(1, g.getId());
			ps.execute();
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with removing a group.." + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#createHomeworkForGroup(com.IttalentsHomeworks.model.HomeworkDetails, java.util.ArrayList)
	 */
	@Override
	public void createHomeworkForGroup(HomeworkDetails hd, ArrayList<Group> groupsForHw)
			throws GroupException, UserException {
		Connection con = manager.getConnection();
		try {
			con.setAutoCommit(false);
			try {
				//con.setAutoCommit(false);

				PreparedStatement ps = con
						.prepareStatement("INSERT INTO IttalentsHomeworks.Homework VALUES (NULL, ?, ?, ?, ?, ?);");
				ps.setString(1, hd.getHeading());
				ps.setString(2, hd.getOpeningTime().toString());
				ps.setString(3, hd.getClosingTime().toString());
				ps.setInt(4, hd.getNumberOfTasks());
				ps.setString(5, hd.getTasksFile());
				ps.execute();
				hd.setId(GroupDAO.getInstance().getHomeworkDetailsId(hd));
				//con.setAutoCommit(false);

				for (Group g : groupsForHw) {
					//con.setAutoCommit(false);

					GroupDAO.getInstance().addHomeworkToGroup(hd, g);
				}


				con.commit();
			} catch (SQLException e) {
				//con.setAutoCommit(false);
			     con.rollback();
				throw new GroupException("Something went wrong with creating a homework..");
			} finally {
				//con.setAutoCommit(true);
			}
		} catch (SQLException e) {
			
			throw new GroupException("Something went wrong with creating a homework..");
		}
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#getHomeworkDetailsId(com.IttalentsHomeworks.model.HomeworkDetails)
	 */
	@Override
	public int getHomeworkDetailsId(HomeworkDetails hd) throws GroupException {
		Connection con = manager.getConnection();
		int hwId = 0;
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT id FROM IttalentsHomeworks.Homework WHERE heading = ?;");
			ps.setString(1, hd.getHeading());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				hwId = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with checking the id of a homework..");
		}
		return hwId;
	}

	//updateHomework
	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#updateHomeworkDetails(com.IttalentsHomeworks.model.HomeworkDetails, java.util.ArrayList)
	 */
	@Override
	public void updateHomeworkDetails(HomeworkDetails hd, ArrayList<Group> groupsforHw)
			throws GroupException, UserException {
		Connection con = manager.getConnection();// get id
		try {
			con.setAutoCommit(false);
			try {
				PreparedStatement ps = con.prepareStatement(
						"UPDATE IttalentsHomeworks.Homework SET heading = ?, opens = ?, closes = ?, num_of_tasks = ?, tasks_pdf = ? WHERE id = ?;");
				ps.setString(1, hd.getHeading());
				ps.setString(2, hd.getOpeningTime().toString());
				ps.setString(3, hd.getClosingTime().toString());
				ps.setInt(4, hd.getNumberOfTasks());
				ps.setString(5, hd.getTasksFile());
				ps.setInt(6, hd.getId());
				ps.executeUpdate();

				ArrayList<Integer> currGroupIds = GroupDAO.getInstance().getIdsOfGroupsForWhichIsHw(hd);
				ArrayList<Integer> wishedGroupIds = new ArrayList<>();
				for (Group g : groupsforHw) {
					wishedGroupIds.add(g.getId());
				}
				for (Integer id : currGroupIds) {
					if (!wishedGroupIds.contains(id)) {
						GroupDAO.getInstance().removeHomeworkFromGroup(hd, GroupDAO.getInstance().getGroupById(id));
					}
				}
				for (Integer id : wishedGroupIds) {
					if (!currGroupIds.contains(id)) {
						GroupDAO.getInstance().addHomeworkToGroup(hd, GroupDAO.getInstance().getGroupById(id));
					}
				}
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				throw new GroupException("Something went wrong with updating homework details..");
			} finally {
				con.setAutoCommit(true);
			}
		} catch (SQLException e1) {
			throw new GroupException("Something went wrong with updating homework details..");
		}
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#getIdsOfGroupsForWhichIsHw(com.IttalentsHomeworks.model.HomeworkDetails)
	 */
	@Override
	public ArrayList<Integer> getIdsOfGroupsForWhichIsHw(HomeworkDetails hd) throws GroupException {
		ArrayList<Integer> groupIds = new ArrayList<>();
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT group_id FROM IttalentsHomeworks.Group_has_Homework WHERE homework_id = ?;");
			ps.setInt(1, hd.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				groupIds.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			throw new GroupException(
					"Something went wrong with getting the ids of groups, for which is the homework..");
		}
		return groupIds;
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#removeHomeworkFromGroup(com.IttalentsHomeworks.model.HomeworkDetails, com.IttalentsHomeworks.model.Group)
	 */
	@Override//TODO add transaction
	public void removeHomeworkFromGroup(HomeworkDetails hd, Group g) throws GroupException, UserException {
		Connection con = manager.getConnection();
		//try {
			//con.setAutoCommit(false);
			try {
				PreparedStatement ps = con.prepareStatement(
						"DELETE FROM IttalentsHomeworks.Group_has_Homework WHERE group_id = ? AND homework_id = ?;");
				ps.setInt(1, g.getId());
				ps.setInt(2, hd.getId());
				ps.execute();
				for (Student s : GroupDAO.getInstance().getStudentsOfGroup(g)) {
					ps = con.prepareStatement("DELETE FROM IttalentsHomeworks.User_has_homework WHERE user_id = ? AND homework_id = ?;");
					ps.setInt(1, s.getId());
					ps.setInt(2, hd.getId());
					ps.execute();
					
					for (int i = 0; i < hd.getNumberOfTasks(); i++) {
						Task t = new Task(i, null, null);
						if (UserDAO.getInstance().doesTaskAlreadyExist(hd, s, t)) {
							ps = con.prepareStatement(
									"DELETE FROM IttalentsHomeworks.Homework_task_solution WHERE student_id = ? AND homework_id = ?;");
							ps.setInt(1, s.getId());
							ps.setInt(2, hd.getId());
							ps.execute();
						}
					}
				}
				//con.commit();
			} catch (SQLException e) {
			//	con.rollback();
				throw new GroupException("Something went wrong with removing a homework from group..");
			}/* finally {
				con.setAutoCommit(true);
			}
		} catch (SQLException e1) {
			throw new GroupException("Something went wrong with removing a homework from group..");
		}*/
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#addHomeworkToGroup(com.IttalentsHomeworks.model.HomeworkDetails, com.IttalentsHomeworks.model.Group)
	 */
	@Override//TODO add transaction
	public void addHomeworkToGroup(HomeworkDetails hd, Group g) throws GroupException, UserException {
		Connection con = manager.getConnection();
		try {
			//con.setAutoCommit(false);
			//try {
				PreparedStatement ps = con
						.prepareStatement("INSERT INTO IttalentsHomeworks.Group_has_Homework VALUES (?,?);");
				ps.setInt(1, g.getId());
				ps.setInt(2, hd.getId());
				ps.execute();
				for (Student s : GroupDAO.getInstance().getStudentsOfGroup(g)) {
					ps = con.prepareStatement("INSERT INTO IttalentsHomeworks.User_has_homework VALUES (?,?,null, null);");
					ps.setInt(1, s.getId());
					ps.setInt(2, hd.getId());
					ps.execute();
					for (int i = 0; i < hd.getNumberOfTasks(); i++) {
						Task t = new Task(i, null, null);
						if (!UserDAO.getInstance().doesTaskAlreadyExist(hd, s, t)) {
							ps = con.prepareStatement(
									"INSERT INTO IttalentsHomeworks.Homework_task_solution VALUES(?,?,?,null,null);");
							ps.setInt(1, s.getId());
							ps.setInt(2, hd.getId());
							ps.setInt(3, t.getTaskNumber());
							ps.execute();
						}
					}
				}
				//con.commit();
			} catch (SQLException e) {
			//	con.rollback();
				throw new GroupException("Something went wrong with adding a homework to group..");
			} finally {
				//con.setAutoCommit(true);
			}
		/*} catch (SQLException e1) {
			System.out.println(e1.getMessage());
			throw new GroupException("Something went wrong with adding a homework to group..");
		}*/
	}

	/* (non-Javadoc)
	 * @see com.IttalentsHomeworks.DAO.IGroupDAO#getGroupById(int)
	 */
	@Override
	public Group getGroupById(int id) throws GroupException, UserException {
		Group group = null;
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT id, group_name FROM IttalentsHomeworks.Groups WHERE id = ?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ArrayList<Teacher> teachers = GroupDAO.getInstance().getTeachersOfGroup(new Group(id, rs.getString(2)));
				ArrayList<Student> students = GroupDAO.getInstance().getStudentsOfGroup(new Group(id, rs.getString(2)));
				ArrayList<HomeworkDetails> homeworkDetails = GroupDAO.getInstance()
						.getHomeworksDetailsOfGroup(new Group(id, rs.getString(2)));

				group = new Group(rs.getInt(1), rs.getString(2), teachers, students, homeworkDetails);
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with getting the group by id..");
		}
		return group;
	}

	public void removeHomework(HomeworkDetails hd) throws GroupException, UserException {
		Connection con = manager.getConnection();
		try {
			con.setAutoCommit(false);
			try {
				PreparedStatement ps = con.prepareStatement("DELETE FROM IttalentsHomeworks.Homework WHERE id = ?;");
				ps.setInt(1, hd.getId());
				ps.execute();

				for (Group g : GroupDAO.getInstance().getAllGroups()) {
					GroupDAO.getInstance().removeHomeworkFromGroup(hd, g);
				}
			} catch (SQLException e) {
				con.rollback();
				throw new GroupException("Something went wrong with removing homework..");
			} finally {
				con.setAutoCommit(true);
			}
		} catch (SQLException e1) {
			throw new GroupException("Something went wrong with removing homework..");
		}
	}

	@Override
	public int getGroupIdByGroupName(Group g) throws GroupException {
		int idGroup = 0;
		Connection con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id FROM IttalentsHomeworks.Groups WHERE group_name = ?;");
			ps.setString(1, g.getName());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				idGroup = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new GroupException("Something went wrong with getting id of group..");
		}
		
		return idGroup;
	}

	@Override
	public void removeHomeworkDetails(HomeworkDetails hd) throws GroupException, UserException {
		Connection con = manager.getConnection();
		try {
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement("DELETE FROM IttalentsHomeworks.Homework WHERE id = ?");
			ps.setInt(1, hd.getId());
			ps.execute();
			ArrayList<Integer> currGroupIds = GroupDAO.getInstance().getIdsOfGroupsForWhichIsHw(hd);
			for (Integer id : currGroupIds) {
				GroupDAO.getInstance().removeHomeworkFromGroup(hd, GroupDAO.getInstance().getGroupById(id));
			}
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				throw new GroupException("Something went wrong with removing homework details rollback..");

			}
			throw new GroupException("Something went wrong with removing homework details..");
		}finally{
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				throw new GroupException("Something went wrong with removing homework details rollback..");
			}
		}
		
		
	}
}
