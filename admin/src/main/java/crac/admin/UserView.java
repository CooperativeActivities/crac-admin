package crac.admin;

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
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class UserView extends VerticalLayout implements View {
	
    public UserView(Navigator navigator, CustomerRepository repo) {
    	CustomerEditor editor = new CustomerEditor(repo);
    	Grid grid = new Grid();
    	TextField filter = new TextField();
		Button addNewBtn = new Button("New User", FontAwesome.PLUS);
		JsonConnector jsonConn = new JsonConnector("frontend", "frontendKey", "http://localhost:8080", CracUser.class, CracUser[].class);
		
		
		repo.deleteAll();
		CracUser[] userList = (CracUser[]) jsonConn.index("/user");
		for(CracUser user : userList){
			repo.save(user);
		}
		
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		addComponent(mainLayout);

		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
  	
    	grid.setHeight(90, Unit.PERCENTAGE);
		grid.setWidth(100, Unit.PERCENTAGE);
		grid.addColumn("id");
		grid.addColumn("name");
		grid.addColumn("email");
		grid.addColumn("lastName");
		grid.addColumn("firstName");
		grid.addColumn("birthDate");
		grid.addColumn("status");
		grid.addColumn("phone");
		grid.addColumn("address");
		grid.addColumn("role");
		
		filter.setInputPrompt("Filter by last name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listCustomers(e.getText(), grid, repo));

		
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
		addNewBtn.addClickListener(e -> {
			editor.editCustomer(new CracUser("", "", "", "", "", new Date(), "", "+43", ""));
			//newUser.setId(this.repo.getMaxId() + 1);
			//System.out.println(this.repo.getMaxId() + 1);
			//editor.editCustomer(newUser);
			});

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCustomers(filter.getValue(), grid, repo);
		});

		// Initialize listing
		listCustomers(null, grid, repo);
		

		
		//addComponent(grid);
		
    }
    
    private void listCustomers(String text, Grid grid, CustomerRepository repo) {
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(
					new BeanItemContainer(CracUser.class, repo.findAll()));
		}
		else {
			grid.setContainerDataSource(new BeanItemContainer(CracUser.class,
					repo.findByLastNameStartsWithIgnoreCase(text)));
		}
	}

    @Override
    public void enter(ViewChangeEvent event) {
        Notification.show("Welcome to the Animal Farm");
    }
}