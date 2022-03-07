package com.ubl.struktur.data.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Absent {

	private Integer id;
	
	private Student student;
	
	private Schedule schedule;
	
	private String description;
}
