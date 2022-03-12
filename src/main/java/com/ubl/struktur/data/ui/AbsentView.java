package com.ubl.struktur.data.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.ubl.struktur.data.model.Absent;
import com.ubl.struktur.data.model.Course;
import com.ubl.struktur.data.model.Schedule;
import com.ubl.struktur.data.model.Student;
import com.ubl.struktur.data.service.DataProvider;
import com.ubl.struktur.data.util.CommonUtil;
import com.ubl.struktur.data.util.SearchUtil;
import com.ubl.struktur.data.util.SortUtil;
import com.ubl.struktur.data.util.SortUtil.SortType;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Absensi")
@Route(value = "absent", layout = MainLayout.class)
public class AbsentView extends Main {

	private Grid<Absent> grid;
	private TextField search;
	private ComboBox<String> searchBy, sortBy;
	private Button searchBt, addBt;
	private RadioButtonGroup<String> rbg;
	
	private static final List<String> FIELDS = Arrays.asList("NIM", "Nama Mahasiswa","Matakuliah","Jadwal","Keterangan");
	
	private Dialog formModal;
	
	private Binder<Absent> binder;
	
	@Autowired
	private DataProvider dataProvider;
	
	public AbsentView() {
		// TODO Auto-generated constructor stub
		initUI();
	}
	
	@PostConstruct
	private void init() {
		loadData();
		formModal = new Dialog(getAddAbsentFormLayout());
		formModal.setModal(false);
		formModal.setDraggable(true);
		formModal.setOpened(false);
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
		
		addBt = new Button("Tambah Data");
		addBt.setThemeName("friendly");
		addBt.addClickListener(e -> {
			binder.setBean(new Absent());
			formModal.open();
		});
		
		HorizontalLayout h1 = new HorizontalLayout(searchBy,search,searchBt);
		h1.setWidthFull();
		h1.setVerticalComponentAlignment(Alignment.STRETCH, search);
		h1.setVerticalComponentAlignment(Alignment.END, searchBt);
		HorizontalLayout h2 = new HorizontalLayout(sortBy,rbg,addBt);
		h2.setVerticalComponentAlignment(Alignment.END, addBt);
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
	
	private VerticalLayout getAddAbsentFormLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("500px");
		
		H2 headline = new H2("Tambah Absensi");
		headline.getStyle().set("margin", "0").set("font-size", "1.5em").set("font-weight", "bold");
		HorizontalLayout header = new HorizontalLayout(headline);
		header.getElement().getClassList().add("draggable");
		header.setSpacing(false);
		header.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)").set("cursor", "move");
		// Use negative margins to make draggable header stretch over full width,
		// covering the padding of the dialog
		header.getStyle().set("padding", "var(--lumo-space-m) var(--lumo-space-l)").set("margin",
				"calc(var(--lumo-space-s) * -1) calc(var(--lumo-space-l) * -1) 0");

		
		
		FormLayout formLayout = new FormLayout();
		formLayout.setWidthFull();
		ComboBox<Schedule> scheculeCb = new ComboBox<Schedule>("");
		scheculeCb.setItemLabelGenerator(s -> CommonUtil.DATE_FORMAT.format(s.getDate()));
		scheculeCb.setWidthFull();
		ComboBox<Student> studentCb = new ComboBox<Student>("", dataProvider.getStudents());
		studentCb.setItemLabelGenerator(s -> s.getNim() +" - "+s.getName());
		studentCb.setWidthFull();
		ComboBox<Course> courseCb = new ComboBox<Course>("", dataProvider.getCourses());
		courseCb.addValueChangeListener(e -> {
			if (!e.getSource().isEmpty()) {
				scheculeCb.setItems(dataProvider.getSchedules().stream()
						.filter(p -> p.getCourse().getCode().equals(e.getValue().getCode()))
						.collect(Collectors.toList()));
			}
		});
		courseCb.setItemLabelGenerator(s -> s.getCode() +" - "+s.getName());
		courseCb.setWidthFull();
		ComboBox<String> keteranganCb = new ComboBox<String>("", Arrays.asList("Hadir", "Tidak Hadir"));
		keteranganCb.setWidthFull();
		
		binder = new Binder<Absent>(Absent.class);
		binder.forField(scheculeCb).asRequired().bind("schedule");
		binder.forField(studentCb).asRequired().bind("student");
		binder.forField(keteranganCb).asRequired().bind("description");
		binder.setBean(new Absent());
		
		formLayout.addFormItem(courseCb, "Matakuliah");
		formLayout.addFormItem(scheculeCb, "Pilih Jadwal");
		formLayout.addFormItem(studentCb, "Mahasiswa");
		formLayout.addFormItem(keteranganCb, "Keterangan");
		
		Button saveBt = new Button("Simpan");
		Button cancelBt = new Button("Batal");
		saveBt.addClickListener(e -> {
			if(binder.validate().isOk()) {
				Absent bean = new Absent();
				bean.setId((int)(Math.random() * 50 + 1));
				binder.writeBeanIfValid(bean);
				if(!dataProvider.getAbsents().contains(bean)) {
					dataProvider.getAbsents().add(bean);
				}
				this.loadData();
				this.formModal.close();
				binder.setBean(null);
			}
		});
		cancelBt.addClickListener(e -> {
			binder.removeBean();
			this.formModal.close();
		});
		saveBt.addThemeName("primary");
		HorizontalLayout lyBt = new HorizontalLayout(cancelBt,saveBt);
		layout.add(header,formLayout,lyBt);
		return layout;
	}
}
