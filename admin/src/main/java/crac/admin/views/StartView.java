package crac.admin.views;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

public class StartView extends VerticalLayout implements View {
    public StartView(Navigator navigator) {
        //setSizeFull();

        Button userButton = new Button("Go to users",
                new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigator.navigateTo("users");
            }
        });
        
        Button taskButton = new Button("Go to projects",
                new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigator.navigateTo("projects");
            }
        });
        
        Button projectButton = new Button("Go to tasks",
                new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigator.navigateTo("tasks");
            }
        });
        
        VerticalLayout mainLayout = new VerticalLayout(userButton, taskButton, projectButton);   
        addComponent(mainLayout);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

    }

    @Override
    public void enter(ViewChangeEvent event) {
        Notification.show("Welcome to the CrAc-Admin-Interface!");
    }

}