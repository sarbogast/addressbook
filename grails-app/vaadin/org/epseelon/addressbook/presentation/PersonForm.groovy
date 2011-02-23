package org.epseelon.addressbook.presentation

import com.vaadin.ui.Form
import com.vaadin.ui.Button
import com.vaadin.ui.TextField
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.data.Item
import org.epseelon.addressbook.presentation.data.PersonContainer
import org.epseelon.addressbook.dto.PersonListItem
import com.vaadin.data.util.BeanItem
import com.vaadin.ui.ComboBox
import com.vaadin.ui.DefaultFieldFactory
import com.vaadin.ui.Field
import com.vaadin.ui.Component
import com.vaadin.data.validator.RegexpValidator
import com.vaadin.data.validator.EmailValidator

/**
 *
 * @author sarbogast
 * @version 19/02/11, 00:51
 */
class PersonForm extends Form implements ClickListener {
    private Button save = new Button("Save", (ClickListener) this);
    private Button cancel = new Button("Cancel", (ClickListener) this);
    private Button edit = new Button("Edit", (ClickListener) this);
    private final ComboBox cities = new ComboBox("City");
    private AddressBookApplication app;

    private boolean newContactMode = false;
    private PersonListItem newPerson = null;

    public PersonForm(AddressBookApplication app) {
        this.app = app
        setWriteThrough(false)

        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addComponent(save);
        footer.addComponent(cancel);
        footer.addComponent(edit);
        footer.setVisible(false)
        setFooter(footer);

        /* Allow the user to enter new cities */
        cities.setNewItemsAllowed(true);
        /* We do not want to use null values */
        cities.setNullSelectionAllowed(false);
        /* Add an empty city used for selecting no city */
        cities.addItem("");
        /* Populate cities select using the cities in the data container */
        PersonContainer ds = app.getDataSource();
        for (Iterator<PersonListItem> it = ds.getItemIds().iterator(); it.hasNext();) {
            String city = (it.next()).getCity();
            cities.addItem(city);
        }

        /*
        * Field factory for overriding how the component for city selection is
        * created
        */
        setFormFieldFactory(new DefaultFieldFactory() {
            @Override
            public Field createField(Item item, Object propertyId,
                                     Component uiContext) {
                if (propertyId.equals("city")) {
                    return cities;
                }
                Field field = super.createField(item, propertyId, uiContext);
                if (propertyId.equals("postalCode")) {
                    TextField tf = (TextField) field;
                    /*
                     * We do not want to display "null" to the user when the
                     * field is empty
                     */
                    tf.setNullRepresentation("");
                    /* Add a validator for postalCode and make it required */
                    tf.addValidator(new RegexpValidator("[1-9][0-9]{4}",
                            "Postal code must be a five digit number and cannot start with a zero."));
                    tf.setRequired(true);
                } else if(propertyId.equals("email")){
                    TextField tf = (TextField) field;
                    tf.addValidator(new EmailValidator("Email must be a valid email address"))
                }
                return field;
            }
        });
    }

    void buttonClick(ClickEvent clickEvent) {
        switch (clickEvent.button) {
            case save:
                if (isValid()) {
                    commit()
                    if (newContactMode) {
                        /* We need to add the new person to the container */
                        Item addedItem = app.getDataSource().addItem(newPerson);
                        /*
                        * We must update the form to use the Item from our datasource
                        * as we are now in edit mode
                        */
                        if(addedItem){
                            setItemDataSource(addedItem);
                            newContactMode = false;
                            setReadOnly(true)
                        }
                    } else {
                        if(app.getDataSource().updateItem(((BeanItem)getItemDataSource()).getBean())){
                            setReadOnly(true)
                        }
                    }
                }
                break;
            case cancel:
                if (newContactMode) {
                    newContactMode = false;
                    setItemDataSource(null);
                } else {
                    discard();
                }
                setReadOnly(true);
                break;
            case edit:
                setReadOnly(false);
                break;
        }
    }

    @Override
    void setItemDataSource(Item newDataSource) {
        newContactMode = false;
        if (newDataSource != null) {
            List<Object> orderedProperties = Arrays.asList(PersonContainer.NATURAL_COL_ORDER);
            super.setItemDataSource(newDataSource, orderedProperties);
            setReadOnly(true);
            getFooter().setVisible(true);
        } else {
            super.setItemDataSource(null);
            getFooter().setVisible(false);
        }
    }

    @Override
    void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        save.setVisible(!readOnly);
        cancel.setVisible(!readOnly);
        edit.setVisible(readOnly);
    }

    void addContact() {
        // Create a temporary item for the form
        newPerson = new PersonListItem();
        setItemDataSource(new BeanItem(newPerson));
        newContactMode = true;
        setReadOnly(false);
    }
}
