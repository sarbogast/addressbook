package org.epseelon.addressbook.presentation

import com.vaadin.ui.Panel
import com.vaadin.ui.TextField
import com.vaadin.ui.NativeSelect
import com.vaadin.ui.CheckBox
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Button
import org.epseelon.addressbook.presentation.data.PersonContainer
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import org.epseelon.addressbook.presentation.data.SearchFilter

/**
 *
 * @author sarbogast
 * @version 19/02/11, 11:32
 */
class SearchView extends Panel {
    private TextField tf;
    private NativeSelect fieldToSearch;
    private CheckBox saveSearch;
    private TextField searchName;
    private AddressBookApplication app;

    public SearchView(final AddressBookApplication app) {
        this.app = app;

        setCaption("Search contacts");
        addStyleName("view");
        setSizeFull();
        /* Use a FormLayout as main layout for this Panel */
        FormLayout formLayout = new FormLayout();
        setContent(formLayout);
        /* Create UI components */
        tf = new TextField("Search term");
        fieldToSearch = new NativeSelect("Field to search");
        saveSearch = new CheckBox("Save search");
        saveSearch.setImmediate(true)
        saveSearch.addListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                searchName.setVisible(event.getButton().booleanValue());
            }
        });
        searchName = new TextField("Search name");
        Button search = new Button("Search");
        search.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                performSearch();
            }
        });
        /* Initialize fieldToSearch */
        for (int i = 0; i < PersonContainer.NATURAL_COL_ORDER.length; i++) {
            fieldToSearch.addItem(PersonContainer.NATURAL_COL_ORDER[i]);
            fieldToSearch.setItemCaption(PersonContainer.NATURAL_COL_ORDER[i],
                    PersonContainer.COL_HEADERS_ENGLISH[i]);
        }
        fieldToSearch.setValue("lastName");
        fieldToSearch.setNullSelectionAllowed(false);
        /* Initialize save checkbox */
        saveSearch.setValue(true);
        /* Add all the created components to the form */
        addComponent(tf);
        addComponent(fieldToSearch);
        addComponent(saveSearch);
        addComponent(searchName);
        addComponent(search);
    }

    private void performSearch() {
        String searchTerm = (String) tf.getValue();
        SearchFilter searchFilter = new SearchFilter(fieldToSearch.getValue(),
                searchTerm, (String) searchName.getValue());
        if (saveSearch.booleanValue()) {
            app.saveSearch(searchFilter);
        }
        app.search(searchFilter);
    }
}
