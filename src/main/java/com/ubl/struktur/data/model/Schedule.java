package com.ubl.struktur.data.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Schedule {

	private Integer id;
	
	private Course course;
	
	private Date date;
	
	public Schedule() {
		// TODO Auto-generated constructor stub
	}

	public Schedule(Integer id, Course course, Date date) {
		super();
		this.id = id;
		this.course = course;
		this.date = date;
	}
}
