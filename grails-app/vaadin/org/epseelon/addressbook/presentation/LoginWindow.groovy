package org.epseelon.addressbook.presentation

import com.vaadin.ui.Window

import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Form
import com.vaadin.ui.TextField
import com.vaadin.ui.Button
import com.vaadin.ui.Button.ClickEvent

import com.vaadin.terminal.Sizeable

/**
 *
 * @author sarbogast
 * @version 20/02/11, 00:32
 */
class LoginWindow extends Window implements Button.ClickListener{
    private Button login = new Button("Login", (Button.ClickListener)this)
    private Button cancel = new Button("Cancel", (Button.ClickListener)this)
    private Form loginForm = new Form()
    private AddressBookApplication app

    LoginWindow(AddressBookApplication app) {
        super("AddressBook Login")
        this.app = app
        setModal(true);

        TextField loginField = new TextField("User Name: ")
        loginField.setWidth("100%")
        loginField.setRequired(true)
        loginForm.addField("login", loginField)

        TextField passwordField = new TextField("Password: ")
        passwordField.setSecret(true)
        passwordField.setWidth("100%")
        passwordField.setRequired(true)
        loginForm.addField("password", passwordField)

        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addComponent(login);

        footer.addComponent(cancel);
        loginForm.setFooter(footer)
        loginForm.setWidth("100%")

        addComponent(loginForm);

        setWidth(300, Sizeable.UNITS_PIXELS)
        center();
    }

    void buttonClick(ClickEvent clickEvent) {
        switch(clickEvent.source){
            case login:
                if(loginForm.isValid()){
                    if(app.login((String)(loginForm.getField("login").getValue()), (String)(loginForm.getField("password").getValue()))){
                        this.close()
                    }
                }
            break;
            case cancel:
                this.close()
            break;
        }
    }
}
