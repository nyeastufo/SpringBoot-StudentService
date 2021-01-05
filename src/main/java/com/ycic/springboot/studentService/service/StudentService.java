package com.ycic.springboot.studentService.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycic.springboot.studentService.model.Course;
import com.ycic.springboot.studentService.model.Student;

@Component
public class StudentService {
	private static List<Student> students = new ArrayList<>();
	
	static {
		// initialize data
		Course course1 = new Course("Course1", "Spring", "10Steps",
				Arrays.asList("Learn Maven", "Import Project", "First Project"));
		Course course2 = new Course("Course2", "Spring MVC", "10 Examples",
				Arrays.asList("Learn Maven", "Import Project", "First Example",
						"Second Example"));
		Course course3 = new Course("Course3", "Spring Boot", "6K Students",
				Arrays.asList("Learn Maven", "Learn Spring",
						"Learn Spring MVC", "First Example", "Second Example"));
		Course course4 = new Course("Course4", "Maven",
				"Most popular maven course on internet!", Arrays.asList(
						"Pom.xml", "Build Life Cycle", "Parent POM",
						"Importing into Eclipse"));
		
		Student david = new Student("Student1", "David Peng", "IT director",
				Arrays.asList(course1, course2, course3, course4));
		Student satish = new Student("Student2", "Satish T",
				"Hiker, Programmer and Architect", new ArrayList<>(Arrays
						.asList(course1, course2, course3, course4)));
		
		students.add(david);
		students.add(satish);
	}
	
	public List<Student> retrieveAllStudents() {
		return students;
	}
	
	public Student retrieveStudent(String studentId) {
		for (Student student : students) {
			if (student.getId().equals(studentId)) {
				return student;
			}
		}
		return null;
	}
	
	public List<Course> retrieveCourses(String studentId) {
		Student student = retrieveStudent(studentId);
		
		if (studentId == null) {
			return null;
		}
		
		if (studentId.equalsIgnoreCase("Student1")) {
			throw new RuntimeException("Something went wrong");
		}
		
		return student.getCourses();
	}
	
	public Course retrieveCourse(String studentId, String courseId) {
		List<Course> courseList = retrieveCourses(studentId);
		
		if (courseList == null) {
			return null;
		}
		
		for (Course course : courseList) {
			if (course.getId().equals(courseId)) {
				return course;
			}
		}
		
		return null;
	}
	
	public Course addCourse(String studentId, Course course) {
		Student student = retrieveStudent(studentId);
		
		if (student == null) {
			return null;
		} 
		
		String randomId = new BigInteger(130, random).toString(32);
		course.setId(randomId);
		
		student.getCourses().add(course);
		
		return course;
	}
	
	private SecureRandom random = new SecureRandom();
}
