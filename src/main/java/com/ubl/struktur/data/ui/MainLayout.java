package com.ubl.struktur.data.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.applayout.AppLayout.Section;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLink;

@CssImport("./styles/shared-styles.css")
@Push
public class MainLayout extends AppLayout implements AfterNavigationObserver {


	private final H1 pageTitle;
	private final RouterLink home;
	private final RouterLink masterStudent;
	private final RouterLink masterCourse;
	private final RouterLink masterSchedule;
	private final RouterLink absent;
	
	public MainLayout() {
		// TODO Auto-generated constructor stub
		home = new RouterLink("Home", HomeView.class);
		masterStudent = new RouterLink("Master Mahasiswa", MasterStudentView.class);
		masterCourse = new RouterLink("Master Course", MasterCourseView.class);
		masterSchedule = new RouterLink("Master Jadwal", MasterScheduleView.class);
		absent = new RouterLink("Absensi", AbsentView.class);
		
		final UnorderedList list = new UnorderedList(new ListItem(home), new ListItem(masterStudent),
				new ListItem(masterCourse), new ListItem(masterSchedule), new ListItem(absent));
		final Nav navigation = new Nav(list);
		addToDrawer(navigation);
		setPrimarySection(Section.DRAWER);
		setDrawerOpened(false);

		// Header
		pageTitle = new H1("Home");
		final Header header = new Header(new DrawerToggle(), pageTitle);
		addToNavbar(header);
	}
	
	private RouterLink[] getRouterLinks() {
		return new RouterLink[] { home, masterCourse, masterStudent, masterSchedule, absent };
	}
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		// TODO Auto-generated method stub
		for (final RouterLink routerLink : getRouterLinks()) {
			if (routerLink.getHighlightCondition().shouldHighlight(routerLink, event)) {
				pageTitle.setText(routerLink.getText());
			}
		}
	}

}
