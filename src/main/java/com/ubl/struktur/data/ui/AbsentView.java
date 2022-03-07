package com.ubl.struktur.data.ui;

import java.util.Arrays;
import java.util.Comparator;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.ubl.struktur.data.model.Absent;
import com.ubl.struktur.data.service.DataProvider;
import com.ubl.struktur.data.util.CommonUtil;
import com.ubl.struktur.data.util.SortUtil;
import com.ubl.struktur.data.util.SortUtil.SortType;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Absensi")
@Route(value = "absent", layout = MainLayout.class)
public class AbsentView extends Main {

	private Grid<Absent> grid;
	private TextField search;
	private ComboBox<String> sortBy;
	private RadioButtonGroup<String> rbg;
	
	@Autowired
	private DataProvider dataProvider;
	
	public AbsentView() {
		// TODO Auto-generated constructor stub
		initUI();
	}
	
	@PostConstruct
	private void init() {
		loadData();
	}
	
	private void initUI() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidthFull();
		layout.setMargin(false);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setWidthFull();
		
		grid = new Grid<Absent>();
		grid.removeAllColumns();
		grid.addColumn(e -> e.getStudent().getNim()).setHeader("NIM");
		grid.addColumn(e -> e.getStudent().getName()).setHeader("Nama Mahasiswa");
		grid.addColumn(e -> e.getSchedule().getCourse().getCode() + " - "+e.getSchedule().getCourse().getName()).setHeader("Matakuliah");
		grid.addColumn(e -> CommonUtil.DATE_FORMAT.format(e.getSchedule().getDate())).setHeader("Jadwal Absen");
		grid.addColumn(e -> e.getDescription()).setHeader("Keterangan");
		
		search = new TextField("Search");
		search.setWidth("50%");
		sortBy = new ComboBox<String>("Sort By", Arrays.asList("NIM", "Nama Mahasiswa","Matakuliah","Jadwal","Keterangan"));
		sortBy.addValueChangeListener(e -> {
			sort();
		});
		
		rbg = new RadioButtonGroup<String>();
		rbg.setLabel("Sort Type");
		rbg.setItems(Arrays.asList("Ascending", "Descending"));
		rbg.addValueChangeListener(e -> {
			sort();
		});
		
		hl.add(search,sortBy,rbg);
		hl.setVerticalComponentAlignment(Alignment.STRETCH, search);
		hl.setVerticalComponentAlignment(Alignment.END, sortBy);
		
		layout.add(hl,grid);
		setWidthFull();
		add(layout);
	}
	
	private void sort() {
		Absent[] list = SortUtil.sorts(dataProvider.getAbsents().stream().toArray(Absent[]::new), getComparator(), getSortType());
		loadData(list);
	}
	
	private Comparator<Absent> getComparator(){
		String sortBy = this.sortBy.getOptionalValue().orElse(null);
		Comparator<Absent> c = null;
		if(sortBy != null) {
			switch (sortBy) {
				case "NIM":
					c = Comparator.comparing(s -> s.getStudent().getNim());
					break;
				case "Nama Mahasiswa":
					c = Comparator.comparing(s -> s.getStudent().getName());
					break;
				case "Matakuliah":
					Comparator<Absent> first = Comparator.comparing(s -> s.getSchedule().getCourse().getCode());
					c = first.thenComparing(Comparator.comparing(s -> s.getSchedule().getCourse().getName()));
					break;
				case "Jadwal":
					c = Comparator.comparing(s -> s.getSchedule().getDate());
					break;
				case "Keterangan":
					c = Comparator.comparing(s -> s.getDescription());
					break;
				default:
					break;
			}
		}
		return c;
	}
	
	private SortType getSortType() {
		String sort = rbg.getOptionalValue().orElse(null);
		if(sort == null)
			return SortType.ASC;
		return "Ascending".equals(sort) ? SortType.ASC : SortType.DESC;
	}
	
	private void loadData() {
		grid.deselectAll();
		grid.setItems(dataProvider.getAbsents());
	}
	
	private void loadData(Absent...items) {
		if(items == null)
			loadData();
		grid.deselectAll();
		grid.setItems(items);
	}
}
