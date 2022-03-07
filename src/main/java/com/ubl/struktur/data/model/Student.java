package com.ubl.struktur.data.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class Student {

	private String nim;
	
	private String name;
	
	public Student() {

	}

	public Student(String nim, String name) {
		super();
		this.nim = nim;
		this.name = name;
	}
	
}
