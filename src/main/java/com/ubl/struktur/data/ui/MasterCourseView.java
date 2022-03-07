package com.ubl.struktur.data.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.ubl.struktur.data.model.Course;
import com.ubl.struktur.data.service.DataProvider;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Master Course")
@Route(value = "master-course", layout = MainLayout.class)
public class MasterCourseView extends AbstractMasterView<Course> {

	private TextField code;
	private TextField name;
	
	@Autowired
	private DataProvider dataProvider;
	
	public MasterCourseView() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	@Override
	public Grid<Course> settingGrid() {
		// TODO Auto-generated method stub
		Grid<Course> grid = new Grid<Course>();
		grid.removeAllColumns();
		grid.addColumn(e -> e.getCode()).setHeader("Kode MK");
		grid.addColumn(e -> e.getName()).setHeader("Nama Matakuliah");
		return grid;
	}

	@Override
	public Binder<Course> getBinder() {
		// TODO Auto-generated method stub
		Binder<Course> binder = new Binder<Course>(Course.class);
		binder.bindInstanceFields(this);
		return binder;
	}

	@Override
	public Set<Course> getInitialData() {
		// TODO Auto-generated method stub
		return dataProvider.getCourses();
	}

	@Override
	public FormLayout getFormLayout() {
		// TODO Auto-generated method stub
		FormLayout fl = new FormLayout();
		code = new TextField("Kode MK");
		code.setWidthFull();
		name = new TextField("Nama Matakuliah");
		name.setWidthFull();
		fl.add(code,name);
		return fl;
	}

	@Override
	public Course newData() {
		// TODO Auto-generated method stub
		return new Course();
	}

}
