package com.ubl.struktur.data.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ubl.struktur.data.model.Absent;
import com.ubl.struktur.data.model.Course;
import com.ubl.struktur.data.model.Schedule;
import com.ubl.struktur.data.model.Student;

import lombok.Getter;

@Component
public class DataProvider {

	@Getter
	private Set<Student> students;
	
	@Getter
	private Set<Course> courses;
	
	@Getter
	private Set<Schedule> schedules;
	
	@Getter
	private Set<Absent> absents;
	
	public DataProvider() {
		// TODO Auto-generated constructor stub
		students = new HashSet<Student>(Arrays.asList(new Student("2111600819","Alfi Ramdhani"), 
				new Student("2111600785", "Jenar Suseno"), new Student("2111600843", "M. Afrizal Munawas")));
		courses = new HashSet<Course>(Arrays.asList(new Course("MT006", "Struktur Data")));
		schedules = new HashSet<Schedule>();
		courses.forEach(c -> {
			Schedule schedule = new Schedule();
			schedule.setId((int) Math.random());
			schedule.setCourse(c);
			schedule.setDate(new Date());
			schedules.add(schedule);
		});
		absents = new HashSet<Absent>();
		students.forEach(s -> {
			schedules.forEach(c -> {
				Absent absent = new Absent();
				absent.setId((int) Math.random());
				absent.setSchedule(c);
				absent.setStudent(s);
				absent.setDescription("Hadir");
				absents.add(absent);
			});
		});
	}
	
}
