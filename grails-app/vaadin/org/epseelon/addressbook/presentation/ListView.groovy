package org.epseelon.addressbook.presentation

import com.vaadin.ui.SplitPanel

/**
 *
 * @author sarbogast
 * @version 19/02/11, 00:44
 */
class ListView extends SplitPanel {
    public ListView(PersonList personList, PersonForm personForm) {
        setFirstComponent(personList);
        setSecondComponent(personForm);
        setSplitPosition(40);
        addStyleName("view");
    }
}
