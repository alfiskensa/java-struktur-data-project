package com.ubl.struktur.data.ui;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ubl.struktur.data.model.Course;
import com.ubl.struktur.data.model.Schedule;
import com.ubl.struktur.data.model.Student;
import com.ubl.struktur.data.service.DataProvider;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Master Schedule")
@Route(value = "master-schedule", layout = MainLayout.class)
public class MasterScheduleView extends AbstractMasterView<Schedule> {
	
	private TextField id;
	private ComboBox<Course> course;
	private DatePicker date;
	
	@Autowired
	private DataProvider dataProvider;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public MasterScheduleView() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	@PostConstruct
	private void init() {
		course.setItems(dataProvider.getCourses());
	}

	@Override
	public Grid<Schedule> settingGrid() {
		// TODO Auto-generated method stub
		Grid<Schedule> grid = new Grid<Schedule>();
		grid.removeAllColumns();
		grid.addColumn(e -> e.getId()).setHeader("ID");
		grid.addColumn(e -> e.getCourse().getCode()).setHeader("Kode MK");
		grid.addColumn(e -> e.getCourse().getName()).setHeader("Nama MK");
		grid.addColumn(e -> sdf.format(e.getDate())).setHeader("Tanggal");
		return grid;
	}

	@Override
	public Binder<Schedule> getBinder() {
		// TODO Auto-generated method stub
		Binder<Schedule> binder = new Binder<Schedule>(Schedule.class);
		binder.forField(id).asRequired().withValidator((v,c) -> {
			if(!StringUtils.isNumeric(v)) {
				return ValidationResult.error("ID harus numerik");
			} else {
				Integer val = new Integer(v);
				boolean exist = dataProvider.getSchedules().stream().anyMatch(s -> s.getId().equals(val));
				if(exist)
					return ValidationResult.error("ID harus unik");
			}
			return ValidationResult.ok();
		}).withNullRepresentation("").withConverter(new StringToIntegerConverter("ID harus numerik")).bind("id");
		binder.forField(course).bind("course");
		binder.forField(date).withConverter(new LocalDateToDateConverter()).bind("date");
	
		return binder;
	}

	@Override
	public Set<Schedule> getInitialData() {
		// TODO Auto-generated method stub
		return dataProvider.getSchedules();
	}

	@Override
	public FormLayout getFormLayout() {
		// TODO Auto-generated method stub
		FormLayout fl = new FormLayout();
		fl.setWidthFull();
		id = new TextField("ID");
		id.setWidthFull();
		course = new ComboBox<Course>("Course");
		course.setItemLabelGenerator(Course::getName);
		course.setWidthFull();
		date = new DatePicker("Tanggal");
		date.setWidthFull();
		fl.add(id,course,date);
		return fl;
	}

	@Override
	public Schedule newData() {
		// TODO Auto-generated method stub
		return new Schedule();
	}
}
