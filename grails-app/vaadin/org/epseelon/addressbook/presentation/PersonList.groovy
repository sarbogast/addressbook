package org.epseelon.addressbook.presentation

import com.vaadin.ui.Table
import org.epseelon.addressbook.presentation.data.PersonContainer
import com.vaadin.data.Property
import com.vaadin.terminal.ExternalResource
import com.vaadin.ui.Link
import org.epseelon.addressbook.dto.PersonListItem
import com.vaadin.ui.Component

/**
 *
 * @author sarbogast
 * @version 19/02/11, 00:48
 */
class PersonList extends Table {

    PersonList(AddressBookApplication app) {
        setSizeFull();
        // customize email column to have mailto: links using column generator
        addGeneratedColumn("email", new Table.ColumnGenerator() {
            public Component generateCell(Table source, Object itemId,
                                          Object columnId) {
                PersonListItem p = (PersonListItem) itemId;
                Link l = new Link();
                l.setResource(new ExternalResource("mailto:" + p.getEmail()));
                l.setCaption(p.getEmail());
                return l;
            }
        });
        setContainerDataSource(app.getDataSource());
        setVisibleColumns(PersonContainer.NATURAL_COL_ORDER);
        setColumnHeaders(PersonContainer.COL_HEADERS_ENGLISH);
        setSelectable(true);
        setImmediate(true);
        setColumnCollapsingAllowed(true);
        setColumnReorderingAllowed(true);
        addListener((Property.ValueChangeListener) app);
        /* We don't want to allow users to de-select a row */
        setNullSelectionAllowed(false);
    }
}
