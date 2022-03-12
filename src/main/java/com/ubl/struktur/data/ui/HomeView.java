package com.ubl.struktur.data.ui;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Stuktur Data Project - XB")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

	public HomeView() {
		// TODO Auto-generated constructor stub
		add(new Section(new Paragraph(
				"This example app demonstrates Structure Data Algorithm with example real data and Vaadin Framework."),
				new Span("The sources for this application can be found "),
				new Anchor("https://github.com/alfiskensa/java-struktur-data-project", "here.")));

		add(new Section(
				new H2(new RouterLink("Master Mahasiswa", MasterStudentView.class)),
				new Paragraph(
						"Menu untuk menambahkan Listing Data Mahasiswa.")));

		add(new Section(new H2(new RouterLink("Master Course", MasterCourseView.class)),
				new Paragraph(
						"Menu untuk menambahankan Listing Data Course.")));

		add(new Section(new H2(new RouterLink("Master Jadwal", MasterScheduleView.class)),
				new Paragraph(
						"Menu untuk menambahkan Listing Data jadwal tiap Course.")));

		add(new Section(
				new H2(new RouterLink("Absensi",
						AbsentView.class)),
				new Paragraph(
						"Menu untuk menampilkan data Absensi secara defaullt, menambah data absensi dan implementasi fitur Sorting and Search di dalamnya.")));

	}
}
