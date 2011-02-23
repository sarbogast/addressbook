package org.epseelon.addressbook.presentation

import com.vaadin.ui.Tree
import com.vaadin.event.ItemClickEvent.ItemClickListener

/**
 *
 * @author sarbogast
 * @version 19/02/11, 00:36
 */
class NavigationTree extends Tree {
    public static final Object SHOW_ALL = "Show all";
    public static final Object SEARCH = "Search";

    public NavigationTree(AddressBookApplication app) {
        addItem(SHOW_ALL);
        addItem(SEARCH);

        setSelectable(true);
        setNullSelectionAllowed(false);
        // Make application handle item click events
        addListener((ItemClickListener) app);
    }
}
