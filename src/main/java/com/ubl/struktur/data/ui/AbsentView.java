package com.ubl.struktur.data.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.ubl.struktur.data.model.Absent;
import com.ubl.struktur.data.service.DataProvider;
import com.ubl.struktur.data.util.CommonUtil;
import com.ubl.struktur.data.util.SearchUtil;
import com.ubl.struktur.data.util.SortUtil;
import com.ubl.struktur.data.util.SortUtil.SortType;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
	private ComboBox<String> searchBy, sortBy;
	private Button searchBt;
	private RadioButtonGroup<String> rbg;
	
	private static final List<String> FIELDS = Arrays.asList("NIM", "Nama Mahasiswa","Matakuliah","Jadwal","Keterangan");
	
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
		search.setWidthFull();
		searchBy = new ComboBox<String>("Search By", FIELDS);
		searchBt = new Button(new Icon(VaadinIcon.SEARCH), e -> {
			search();
		});
		
		sortBy = new ComboBox<String>("Sort By", FIELDS);
		sortBy.addValueChangeListener(e -> {
			sort();
			this.search.clear();
			this.searchBy.clear();
		});
		
		rbg = new RadioButtonGroup<String>();
		rbg.setLabel("Sort Type");
		rbg.setItems(Arrays.asList("Ascending", "Descending"));
		rbg.addValueChangeListener(e -> {
			sort();
			this.search.clear();
			this.searchBy.clear();
		});
		
		HorizontalLayout h1 = new HorizontalLayout(searchBy,search,searchBt);
		h1.setWidthFull();
		h1.setVerticalComponentAlignment(Alignment.STRETCH, search);
		h1.setVerticalComponentAlignment(Alignment.END, searchBt);
		HorizontalLayout h2 = new HorizontalLayout(sortBy,rbg);
		h2.setWidthFull();
		hl.add(h1,h2);
		hl.setVerticalComponentAlignment(Alignment.STRETCH, h1);
		layout.add(hl,grid);
		setWidthFull();
		add(layout);
	}
	
	private void search() {
		String value = search.getOptionalValue().orElse(null);
		if(value != null) {
			Absent[] sortedArray = sort();
			Absent found = SearchUtil.binarySearchByStringField(sortedArray, value, getExtractors());
			if(found != null) {
				loadData(found);
			} else {
				loadData(new ArrayList<Absent>().stream().toArray(Absent[]::new));
			}
		}
	}
	
	private Absent[] sort() {
		Absent[] array = dataProvider.getAbsents().stream().toArray(Absent[]::new);
		Absent[] sortedArray = SortUtil.sorts(array, getComparator(), getSortType());
		loadData(sortedArray);
		return sortedArray;
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
		} else {
			this.sortBy.setValue("NIM");
			return getComparator();
		}
		return c;
	}
	
	private Function<Absent, String> getExtractors(){
		String searchBy = this.searchBy.getOptionalValue().orElse(null);
		Function<Absent, String> f = null;
		if(searchBy != null) {
			switch (searchBy) {
				case "NIM":
					f = (t) -> t.getStudent().getNim();
					break;
				case "Nama Mahasiswa":
					f = (t) -> t.getStudent().getName();
					break;
				case "Matakuliah":
					f = (t) -> t.getSchedule().getCourse().getCode() + " - " + t.getSchedule().getCourse().getName();
					break;
				case "Jadwal":
					f = (t) -> CommonUtil.DATE_FORMAT.format(t.getSchedule().getDate());
					break;
				case "Keterangan":
					f = (t) -> t.getDescription();
					break;
				default:
					break;
			}
		} else {
			this.searchBy.setValue("NIM");
			return getExtractors();
		}
		return f;
	}
	
	private SortType getSortType() {
		String sort = rbg.getOptionalValue().orElse(null);
		if(sort == null) {
			rbg.setValue("Ascending");
			return getSortType();
		} 
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
