package crac.admin;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
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
import com.vaadin.ui.themes.ValoTheme;

import crac.admin.models.Role;

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
public class CustomerEditor extends VerticalLayout {

	private final CustomerRepository repository;

	/**
	 * The currently edited customer
	 */
	private CracUser customer;
	private JsonConnector jsonConn = new JsonConnector("frontend", "frontendKey", "http://localhost:8080", CracUser.class, CracUser[].class);

	/* Fields to edit properties in Customer entity */
	TextField name = new TextField("name");
	TextField email = new TextField("email");
	PasswordField password = new PasswordField("password");
	TextField lastName = new TextField("Last name");
	TextField firstName = new TextField("First name");
	DateField birthDate = new DateField("Birthdate");
	TextField status = new TextField("status");
	TextField phone = new TextField("phone");
	TextField address = new TextField("address");
	ComboBox role = new ComboBox("role");

	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button update = new Button("Update", FontAwesome.ADJUST);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	CssLayout actions = new CssLayout(save, update, cancel, delete);

	@Autowired
	public CustomerEditor(CustomerRepository repository) {
		this.repository = repository;
		
		role.addItem(Role.USER);
		role.addItem(Role.ADMIN);

		addComponents(name, email, password, firstName, lastName, birthDate, status, phone, address, role, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> jsonConn.post("/user", customer));
		save.addClickListener(e -> repoReload());
		update.addClickListener(e -> jsonConn.put("/user/"+customer.getId(), customer));
		update.addClickListener(e -> repoReload());
		delete.addClickListener(e -> jsonConn.delete("/user/"+customer.getId(), customer));
		delete.addClickListener(e -> repoReload());
		cancel.addClickListener(e -> editCustomer(customer));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}
	
	public void repoReload(){
		repository.deleteAll();
		CracUser[] userList = (CracUser[]) this.jsonConn.index("/user");
		for(CracUser user : userList){
			this.repository.save(user);
		}
	}

	public final void editCustomer(CracUser c) {
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			customer = repository.findOne(c.getId());
		}
		else {
			customer = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(customer, this);

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
