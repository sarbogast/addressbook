package org.epseelon.addressbook.presentation

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Component
import org.epseelon.addressbook.presentation.data.PersonContainer
import com.vaadin.data.Item
import com.vaadin.event.ItemClickEvent.ItemClickListener
import com.vaadin.event.ItemClickEvent
import org.epseelon.addressbook.presentation.data.SearchFilter
import com.vaadin.ui.Window.Notification
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Embedded
import com.vaadin.ui.Alignment
import org.epseelon.addressbook.business.PersonService
import org.epseelon.addressbook.business.SecurityService
import org.epseelon.addressbook.exceptions.SecurityServiceException

/**
 *
 * @author sarbogast
 * @version 18/02/11, 20:57
 */
class AddressBookApplication extends Application implements Button.ClickListener, Property.ValueChangeListener, ItemClickListener {
    private static AddressBookApplication application
    public static AddressBookApplication getApplication(){
        return application
    }

    private Button newContact = new Button("Add contact");
    private Button search = new Button("Search");
    private Button share = new Button("Share");
    private Button help = new Button("Help");
    private Button login = new Button("Login")
    private Button logout = new Button("Logout")
    private SplitPanel horizontalSplit = new SplitPanel(SplitPanel.ORIENTATION_HORIZONTAL);
    private NavigationTree tree = new NavigationTree(this);
    private ListView listView = null;
    private PersonList personList = null;
    private PersonForm personForm = null;
    private HelpWindow helpWindow = null
    private SharingOptions sharingOptions
    private PersonContainer dataSource = new PersonContainer((PersonService)getBean(PersonService));
    private SearchView searchView = null;
    private SecurityService security = (SecurityService)getBean(SecurityService)

    private SearchView getSearchView() {
        if (searchView == null) {
            searchView = new SearchView(this);
        }
        return searchView;
    }

    private ListView getListView() {
        if (listView == null) {
            personList = new PersonList(this);
            personForm = new PersonForm(this);
            listView = new ListView(personList, personForm);
        }
        return listView;
    }

    HelpWindow getHelpWindow() {
        if (helpWindow == null) {
            helpWindow = new HelpWindow()
        }
        return helpWindow
    }

    SharingOptions getSharingOptions() {
        if (!sharingOptions) {
            sharingOptions = new SharingOptions()
        }
        sharingOptions
    }

    PersonContainer getDataSource() {
        return dataSource;
    }

    @Override
    public void init() {
        AddressBookApplication.application = this
        setTheme("contacts")
        buildMainLayout()
    }

    private void buildMainLayout() {
        setMainWindow(new Window("Address Book Demo application"));

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        layout.addComponent(createToolbar());
        layout.addComponent(horizontalSplit);
        horizontalSplit.setFirstComponent(tree);
        setMainComponent(getListView());
        /* Allocate all available extra space to the horizontal split panel */
        layout.setExpandRatio(horizontalSplit, 1);
        /* Set the initial split position so we can have a 200 pixel menu to the left */
        horizontalSplit.setSplitPosition(200, SplitPanel.UNITS_PIXELS);

        getMainWindow().setContent(layout);
    }

    private void setMainComponent(Component c) {
        horizontalSplit.setSecondComponent(c);
    }

    HorizontalLayout createToolbar() {
        HorizontalLayout lo = new HorizontalLayout();

        newContact.addListener((Button.ClickListener) this)
        newContact.setIcon(new ThemeResource("icons/32/document-add.png"));
        lo.addComponent(newContact);

        search.addListener((Button.ClickListener) this);
        search.setIcon(new ThemeResource("icons/32/folder-add.png"));
        lo.addComponent(search);

        share.addListener((Button.ClickListener) this)
        share.setIcon(new ThemeResource("icons/32/users.png"));
        lo.addComponent(share);

        help.addListener((Button.ClickListener) this);
        help.setIcon(new ThemeResource("icons/32/help.png"));
        lo.addComponent(help);

        login.addListener((Button.ClickListener)this)
        login.setIcon(new ThemeResource("icons/32/lock.png"))
        lo.addComponent(login)
        login.setVisible(!security.isSignedIn())

        logout.addListener((Button.ClickListener)this)
        logout.setIcon(new ThemeResource("icons/32/lock.png"))
        lo.addComponent(logout)
        logout.setVisible(security.isSignedIn())

        lo.setWidth("100%");
        Embedded em = new Embedded("", new ThemeResource("images/logo.png"));
        lo.addComponent(em);
        lo.setComponentAlignment(em, Alignment.MIDDLE_RIGHT);
        lo.setExpandRatio(em, 1);

        lo.setStyleName("toolbar");
        lo.setMargin(true);
        lo.setSpacing(true);
        return lo;
    }

    void buttonClick(ClickEvent clickEvent) {
        switch (clickEvent.getButton()) {
            case search: setMainComponent(getSearchView()); break;
            case newContact:
                setMainComponent(getListView());
                personForm.addContact();
                break;
            case share: getMainWindow().addWindow(getSharingOptions()); break;
            case help: getMainWindow().addWindow(getHelpWindow()); break;
            case login: getMainWindow().addWindow(new LoginWindow(this)); break;
            case logout:
                security.signOut()
                refreshToolbar()
            break;
        }
    }

    void valueChange(ValueChangeEvent valueChangeEvent) {
        Property property = valueChangeEvent.getProperty();
        if (property == personList) {
            Item item = personList.getItem(personList.getValue());
            if (item != personForm.getItemDataSource()) {
                personForm.setItemDataSource(item);
            }
        }
    }

    void itemClick(ItemClickEvent itemClickEvent) {
        switch (itemClickEvent.getSource()) {
            case tree:
                Object itemId = itemClickEvent.getItemId();
                if (itemId != null) {
                    if (NavigationTree.SHOW_ALL.equals(itemId)) {
                        // clear previous filters
                        getDataSource().removeAllContainerFilters();
                        setMainComponent(getListView())
                    } else if (NavigationTree.SEARCH.equals(itemId)) {
                        setMainComponent(getSearchView());
                    } else if (itemId instanceof SearchFilter) {
                        search((SearchFilter) itemId);
                    }
                }
                break;
        }
    }

    public void search(SearchFilter searchFilter) {
        // clear previous filters
        getDataSource().removeAllContainerFilters();
        // filter contacts with given filter
        getDataSource().addContainerFilter(searchFilter.getPropertyId(), searchFilter.getTerm(), true, false);
        setMainComponent(getListView());

        getMainWindow().showNotification(
                "Searched for " + searchFilter.getPropertyId() + "=*"
                        + searchFilter.getTerm() + "*, found "
                        + getDataSource().size() + " item(s).",
                Notification.TYPE_HUMANIZED_MESSAGE);
    }

    public void saveSearch(SearchFilter searchFilter) {
        tree.addItem(searchFilter);
        tree.setParent(searchFilter, NavigationTree.SEARCH);
        // mark the saved search as a leaf (cannot have children)
        tree.setChildrenAllowed(searchFilter, false);
        // make sure "Search" is expanded
        tree.expandItem(NavigationTree.SEARCH);
        // select the saved search
        tree.setValue(searchFilter);
    }

    boolean login(String username, String password) {
        try {
            security.signIn(username, password)
            refreshToolbar()
            return true
        } catch (SecurityServiceException e) {
            getMainWindow().showNotification(e.message, Notification.TYPE_ERROR_MESSAGE);
            return false
        }
    }

    private void refreshToolbar(){
        login.setVisible(!security.isSignedIn())
        logout.setVisible(security.isSignedIn())
    }
}
