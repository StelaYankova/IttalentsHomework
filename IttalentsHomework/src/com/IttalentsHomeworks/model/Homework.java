package com.IttalentsHomeworks.model;

import java.util.ArrayList;

public class Homework{
	
	private int teacherGrade;
	private String comment;
	private ArrayList<Task> tasks;
	private HomeworkDetails homeworkDetails;
	
	public Homework(int teacherGrade, String comment, ArrayList<Task> tasks, HomeworkDetails homeworkDetails) {
		this.teacherGrade = teacherGrade;
		this.comment = comment;
		this.tasks = tasks;
		this.homeworkDetails = homeworkDetails;
	}
	public int getTeacherGrade() {
		return teacherGrade;
	}
	public void setTeacherGrade(int teacherGrade) {
		this.teacherGrade = teacherGrade;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public ArrayList<Task> getTasks() {
		return tasks;
	}
	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}
	public HomeworkDetails getHomeworkDetails() {
		return homeworkDetails;
	}
	public void setHomeworkDetails(HomeworkDetails homeworkDetails) {
		this.homeworkDetails = homeworkDetails;
	}
	
	
	
}
