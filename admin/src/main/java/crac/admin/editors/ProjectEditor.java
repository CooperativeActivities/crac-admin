package crac.admin.editors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import crac.admin.daos.CustomerRepository;
import crac.admin.daos.ProjectRepository;
import crac.admin.models.CracUser;
import crac.admin.models.JsonConnector;
import crac.admin.models.Role;
import crac.admin.models.Project;

/**
 * A simple example to introduce building forms. As your real application is
 * probably much more complicated than this example, you could re-use this form in
 * multiple places. This example component is only used in VaadinUI.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX. See e.g. AbstractForm in Virin
 * (https://vaadin.com/addon/viritin).
 */
@SpringComponent
@UIScope
public class ProjectEditor extends VerticalLayout {
	
	private final ProjectRepository repository;

	/**
	 * The currently edited customer
	 */
	private Project myProject;
	private JsonConnector jsonConn = new JsonConnector("frontend", "frontendKey", "http://localhost:8080", Project.class, Project[].class);

	/* Fields to edit properties in Customer entity */
	TextField name = new TextField("name");
	TextField description = new TextField("description");
	TextField location = new TextField("location");
	DateField startTime = new DateField("Start-time");
	DateField endTime = new DateField("End-time");


	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button update = new Button("Update", FontAwesome.ADJUST);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	CssLayout actions = new CssLayout(save, update, cancel, delete);

	@Autowired
	public ProjectEditor(ProjectRepository repository, Navigator navigator) {
		this.repository = repository;
		
		Button tasks = new Button("Tasks",
	            new Button.ClickListener() {
	        @Override
	        public void buttonClick(ClickEvent event) {
	            navigator.navigateTo("tasks/project/"+myProject.getId());
	        }
	    });

		addComponents(name, description, location, startTime, endTime, tasks, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> jsonConn.post("/project", myProject));
		save.addClickListener(e -> repoReload());
		update.addClickListener(e -> jsonConn.put("/project/"+myProject.getId(), myProject));
		update.addClickListener(e -> repoReload());
		delete.addClickListener(e -> jsonConn.delete("/project/"+myProject.getId(), myProject));
		delete.addClickListener(e -> repoReload());
		cancel.addClickListener(e -> editCustomer(myProject));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}
	
	public void repoReload(){
		repository.deleteAll();
		Project[] taskList = (Project[]) this.jsonConn.index("/project");
		for(Project task : taskList){
			this.repository.save(task);
		}
	}

	public final void editCustomer(Project c) {
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			myProject = repository.findOne(c.getId());
		}
		else {
			myProject = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(myProject, this);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		name.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		update.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
		cancel.addClickListener(e -> h.onChange());
	}

}
