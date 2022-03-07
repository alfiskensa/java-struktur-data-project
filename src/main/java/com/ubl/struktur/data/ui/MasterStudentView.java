package com.ubl.struktur.data.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.ubl.struktur.data.model.Student;
import com.ubl.struktur.data.service.DataProvider;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Master Mahasiswa")
@Route(value = "master-student", layout = MainLayout.class)
public class MasterStudentView extends AbstractMasterView<Student> {

	private TextField nim;
	private TextField name;
	
	@Autowired
	private DataProvider dataProvider;
		
	public MasterStudentView() {
		// TODO Auto-generated constructor stub
		super();
	}

	@Override
	public Grid<Student> settingGrid() {
		// TODO Auto-generated method stub
		Grid<Student> grid = new Grid<Student>();
		grid.removeAllColumns();
		grid.addColumn(s -> s.getNim()).setHeader("NIM");
		grid.addColumn(e -> e.getName()).setHeader("Nama Mahasiswa");
		return grid;
	}

	@Override
	public Binder<Student> getBinder() {
		// TODO Auto-generated method stub
		Binder<Student> binder = new Binder<Student>(Student.class);
		binder.bindInstanceFields(this);
		return binder;
	}

	@Override
	public Set<Student> getInitialData() {
		// TODO Auto-generated method stub
		return dataProvider.getStudents();
	}

	@Override
	public FormLayout getFormLayout() {
		// TODO Auto-generated method stub
		FormLayout fl = new FormLayout();
		fl.setWidthFull();
		nim = new TextField("NIM");
		nim.setWidthFull();
		name = new TextField("Nama Mahasiswa");
		name.setWidthFull();
		fl.add(nim,name);
		return fl;
	}

	@Override
	public Student newData() {
		// TODO Auto-generated method stub
		return new Student();
	}
	
	
}
