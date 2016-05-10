package crac.admin.views;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;

import crac.admin.daos.CustomerRepository;
import crac.admin.daos.ProjectRepository;
import crac.admin.editors.CustomerEditor;
import crac.admin.editors.ProjectEditor;
import crac.admin.models.CracUser;
import crac.admin.models.JsonConnector;
import crac.admin.models.Project;

import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ProjectView extends VerticalLayout implements View {
	
    public ProjectView(Navigator navigator, ProjectRepository repo) {
    	ProjectEditor editor = new ProjectEditor(repo);
    	Grid grid = new Grid();
    	TextField filter = new TextField();
		Button addNewBtn = new Button("New Project", FontAwesome.PLUS);
		JsonConnector jsonConn = new JsonConnector("frontend", "frontendKey", "http://localhost:8080", Project.class, Project[].class);
		
		
		repo.deleteAll();
		Project[] taskList = (Project[]) jsonConn.index("/project");
		for(Project task : taskList){
			repo.save(task);
		}
		
		Button menuButton = new Button("Back",
                new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigator.navigateTo("/");
            }
        });
		
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, menuButton);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		addComponent(mainLayout);

		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
  	
    	grid.setHeight(90, Unit.PERCENTAGE);
		grid.setWidth(100, Unit.PERCENTAGE);
		grid.addColumn("id");
		grid.addColumn("name");
		grid.addColumn("description");
		grid.addColumn("location");
		grid.addColumn("startTime");
		grid.addColumn("endTime");
		
		filter.setInputPrompt("Filter by name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listProjects(e.getText(), grid, repo));

		
		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			}
			else {
				editor.editCustomer((Project) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> {
			editor.editCustomer(new Project("", "", "", new Date(), new Date()));
			//newUser.setId(this.repo.getMaxId() + 1);
			//System.out.println(this.repo.getMaxId() + 1);
			//editor.editCustomer(newUser);
			});

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listProjects(filter.getValue(), grid, repo);
		});

		// Initialize listing
		listProjects(null, grid, repo);
		

		
		//addComponent(grid);
		
    }
    
    private void listProjects(String text, Grid grid, ProjectRepository repo) {
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(
					new BeanItemContainer(Project.class, repo.findAll()));
		}
		else {
			grid.setContainerDataSource(new BeanItemContainer(Project.class,
					repo.findByNameStartsWithIgnoreCase(text)));
		}
	}

    @Override
    public void enter(ViewChangeEvent event) {
        Notification.show("Manage and change tasks here!");
    }
}