package crac.admin;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import crac.admin.daos.CustomerRepository;
import crac.admin.daos.ProjectRepository;
import crac.admin.daos.TaskRepository;
import crac.admin.views.ProjectView;
import crac.admin.views.StartView;
import crac.admin.views.TaskView;
import crac.admin.views.UserView;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

	Navigator navigator;
    protected static final String MAINVIEW = "main";
    
	@Autowired
	private CustomerRepository UserRepo;
	
	@Autowired
	private TaskRepository taskRepo;

	@Autowired
	private ProjectRepository projectRepo;

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Navigation Example");

        // Create a navigator to control the views
        navigator = new Navigator(this, this);

        // Create and register the views
        navigator.addView("", new StartView(navigator));
        navigator.addView("users", new UserView(navigator, UserRepo));
        navigator.addView("tasks", new TaskView(navigator, taskRepo));
        navigator.addView("projects", new ProjectView(navigator, projectRepo));
    }

}
