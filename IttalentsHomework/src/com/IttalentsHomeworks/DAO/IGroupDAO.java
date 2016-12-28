package com.IttalentsHomeworks.DAO;

import java.util.ArrayList;

import com.IttalentsHomeworks.DB.DBManager;
import com.IttalentsHomeworks.Exceptions.GroupException;
import com.IttalentsHomeworks.Exceptions.UserException;
import com.IttalentsHomeworks.model.Group;
import com.IttalentsHomeworks.model.HomeworkDetails;
import com.IttalentsHomeworks.model.Student;
import com.IttalentsHomeworks.model.Teacher;
import com.IttalentsHomeworks.model.User;

public interface IGroupDAO {

	DBManager getManager();

	void setManager(DBManager manager);

	ArrayList<Teacher> getTeachersOfGroup(Group g) throws GroupException;

	ArrayList<Student> getStudentsOfGroup(Group g) throws GroupException, UserException;

	ArrayList<HomeworkDetails> getHomeworkDetailsOfGroup(Group g) throws GroupException;

	boolean isUserAlreadyInGroup(Group g, User u) throws GroupException;

	void addUserToGroup(Group g, User u) throws GroupException, UserException;

	// constructor with teachers
	void createNewGroup(Group g) throws GroupException;

	boolean isGroupNameUnique(String groupName) throws GroupException;

	ArrayList<HomeworkDetails> getAllHomeworksDetails() throws GroupException;

	ArrayList<Group> getAllGroups() throws UserException, GroupException;

	void removeUserFromGroup(Group group, User u) throws GroupException;

	void removeGroup(Group g) throws GroupException;

	void createHomeworkDetails(HomeworkDetails hd, ArrayList<Group> groupsForHw) throws GroupException, UserException;

	int getHomeworkDetailsId(HomeworkDetails hd) throws GroupException;

	void updateHomeworkDetails(HomeworkDetails hd, ArrayList<Group> groupsforHw) throws GroupException, UserException;

	void removeHomeworkFromGroup(HomeworkDetails hd, Group g) throws GroupException, UserException;

	void addHomeworkToGroup(HomeworkDetails hd, Group g) throws GroupException, UserException;

	Group getGroupById(int id) throws GroupException, UserException;

	int getGroupIdByGroupName(Group g) throws GroupException;

	void removeHomeworkDetails(HomeworkDetails hd) throws GroupException, UserException;

	void changeGroupName(Group g) throws GroupException;

	ArrayList<Integer> getIdsOfGroupsForWhichIsHomework(HomeworkDetails homeworkDetails) throws GroupException;

}