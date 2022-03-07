package com.ubl.struktur.data.ui;

import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

public abstract class AbstractMasterView<T> extends Main {

	private Grid<T> grid;
	private VerticalLayout editor;
	protected Button saveBtn;
	private Button resetBtn;
	private Button addBtn;
	protected Binder<T> binder;
	
	public abstract Grid<T> settingGrid();
	public abstract Binder<T> getBinder();
	public abstract Set<T> getInitialData();
	public abstract FormLayout getFormLayout();
	public abstract T newData();
	
	protected Set<T> data;
	
	public AbstractMasterView() {
		// TODO Auto-generated constructor stub
		initUI();
		binder = getBinder();
	}
	
	@PostConstruct
	private void init() {
		data = getInitialData();
		loadData();
	}
	
	private void initUI() {
		HorizontalLayout layout = new HorizontalLayout();
		
		grid = settingGrid();
		grid.addSelectionListener(e -> {
			Optional<T> selected = e.getFirstSelectedItem();
			editor.setVisible(selected.isPresent());
			binder.setBean(selected.orElse(null));
		});
		
		addBtn = new Button("Tambah Data");
		addBtn.addClickListener(e -> {
			grid.deselectAll();
			binder.setBean(newData());
			editor.setVisible(true);
		});
		
		VerticalLayout vl = new VerticalLayout();
		vl.setWidthFull();
		vl.add(addBtn,grid);
		vl.setHorizontalComponentAlignment(Alignment.BASELINE, addBtn);
		
		editor = new VerticalLayout();
		editor.setWidth("300px");
		
		saveBtn = new Button("Save");
		saveBtn.setWidthFull();
		saveBtn.addClickListener(e -> {
			if(binder.isValid()) {
				T bean = binder.getBean();
				if(!data.contains(bean))
					data.add(bean);
				loadData();
				reset();
			}
		});
		
		resetBtn = new Button("Reset");
		resetBtn.setWidthFull();
		resetBtn.addClickListener(e -> {
			binder.setBean(newData());
		});
		
		HorizontalLayout btnLy = new HorizontalLayout(resetBtn,saveBtn);
		btnLy.setWidthFull();
		
		editor.add(getFormLayout(),btnLy);
		editor.setVisible(false);
		
		layout.add(vl,editor);
		layout.setMargin(false);
		add(layout);
	}
		
	protected void loadData() {
		grid.deselectAll();
		grid.setItems(data);
	}
	
	protected void reset() {
		binder.setBean(newData());
		editor.setVisible(false);
	}
}
