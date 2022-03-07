package com.ubl.struktur.data.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Course {

	private String code;
	
	private String name;
	
	public Course() {
		// TODO Auto-generated constructor stub
	}

	public Course(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	
	
}
