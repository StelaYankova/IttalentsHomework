package com.IttalentsHomeworks.DAO;

import java.time.LocalDateTime;
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

public interface IUserDAO {

	DBManager getManager();

	void setManager(DBManager manager);

	boolean isUserATeacher(int userId) throws UserException;

	int getUserIdByUsername(String username) throws UserException;

	ArrayList<Group> getGroupsOfUser(int userId) throws UserException, GroupException;

	ArrayList<Homework> getHomeworksOfStudentByGroup(int sId, Group g) throws UserException;

	ArrayList<Task> getTasksOfHomeworkOfStudent(int userId, HomeworkDetails hd) throws UserException;

	User getUserByUsername(String username) throws UserException, GroupException;

	//all homeworks
	ArrayList<Homework> getHomeworksOfStudent(int id) throws UserException;

	boolean isUsernameUnique(String username) throws UserException;

	boolean isPasswordValid(String pass);

	void createNewUser(User u) throws UserException;

	void removeUserProfile(User u) throws UserException;

	void setTeacherGrade(HomeworkDetails hd, int studentId, int teacherGrade) throws UserException;

	void setTeacherComment(HomeworkDetails hd, int studentId, String teacherComment) throws UserException;

	void setSolutionOfTask(HomeworkDetails hd, Student st, int taskNum, String solution, LocalDateTime timeOfUpload)
			throws UserException;

	void setTimeOfUploadOfTask(HomeworkDetails hd, Student st, int taskNum, LocalDateTime timeOfUpload) throws UserException;

	void updateUser(User u) throws UserException;

	Student getStudentsByUsername(String string) throws UserException;

	boolean isTaskNumberValid(int studentId, int homeworkId, int taskNum) throws UserException;

	boolean doesUserExistInDB(String username, String password) throws UserException;

	void addHomeworkToStudent(User user, HomeworkDetails hd) throws UserException;

	boolean doesTaskAlreadyExist(HomeworkDetails homeworkDetails, Student student, int taskNum) throws UserException;

	ArrayList<Teacher> getAllTeachers() throws UserException;

	ArrayList<Student> getAllStudents() throws UserException;

	User getUserById(int userId) throws UserException, GroupException;

}