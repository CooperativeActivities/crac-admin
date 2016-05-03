package crac.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

	private final CustomerRepository repo;

	private final CustomerEditor editor;

	private final Grid grid;

	private final TextField filter;

	private final Button addNewBtn;
	
	private JsonConnector jsonConn;

	@Autowired
	public VaadinUI(CustomerRepository repo, CustomerEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid();
		this.filter = new TextField();
		this.addNewBtn = new Button("New User", FontAwesome.PLUS);
		//TODO
		this.jsonConn = new JsonConnector();
	}

	@Override
	protected void init(VaadinRequest request) {
		//TODO
		this.jsonConn.index();
		CracUser myUser = new CracUser("asd", "asd", "asd", "asd", "asd", new Date(), "asd", "+43", "asd");
		this.repo.save(myUser);
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		setContent(mainLayout);

		// Configure layouts and components
		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		grid.setHeight(300, Unit.PIXELS);
		grid.setWidth(900, Unit.PIXELS);
		grid.addColumn("id");
		grid.addColumn("name");
		grid.addColumn("email");
		grid.addColumn("password");
		grid.addColumn("lastName");
		grid.addColumn("firstName");
		grid.addColumn("birthDate");
		grid.addColumn("status");
		grid.addColumn("phone");
		grid.addColumn("address");

		filter.setInputPrompt("Filter by last name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listCustomers(e.getText()));

		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			}
			else {
				editor.editCustomer((CracUser) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editCustomer(new CracUser("", "", "", "", "", new Date(), "", "+43", "")));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCustomers(filter.getValue());
		});

		// Initialize listing
		listCustomers(null);
	}

	// tag::listCustomers[]
	private void listCustomers(String text) {
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(
					new BeanItemContainer(CracUser.class, repo.findAll()));
		}
		else {
			grid.setContainerDataSource(new BeanItemContainer(CracUser.class,
					repo.findByLastNameStartsWithIgnoreCase(text)));
		}
	}
	// end::listCustomers[]

}