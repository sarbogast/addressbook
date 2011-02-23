package org.epseelon.addressbook.presentation.data

import com.vaadin.data.util.BeanItemContainer
import org.epseelon.addressbook.dto.PersonListItem
import org.epseelon.addressbook.business.PersonService
import com.vaadin.data.util.BeanItem
import com.vaadin.ui.Window.Notification
import org.epseelon.addressbook.presentation.AddressBookApplication

/**
 *
 * @author sarbogast
 * @version 19/02/11, 11:12
 */
class PersonContainer extends BeanItemContainer<PersonListItem> implements Serializable {
    /**
     * Natural property order for PersonListItem bean. Used in tables and forms.
     */
    public static final Object[] NATURAL_COL_ORDER = [
            "firstName", "lastName", "email", "phoneNumber", "streetAddress",
            "postalCode", "city"
    ];

    private PersonService personService

    /**
     * "Human readable" captions for properties in same order as in
     * NATURAL_COL_ORDER.
     */
    public static final String[] COL_HEADERS_ENGLISH = [
            "First name", "Last name", "Email", "Phone number",
            "Street Address", "Postal Code", "City"];

    public PersonContainer(PersonService personService) throws InstantiationException, IllegalAccessException {
        super(personService.getAllPersons());
        this.personService = personService
    }

    @Override
    BeanItem<PersonListItem> addItem(Object itemId) {
        try {
            PersonListItem person = personService.createPerson((PersonListItem) itemId)
            return super.addItem(person)
        } catch (Exception e) {
            AddressBookApplication.application.getMainWindow().showNotification(
                    e.message,
                    Notification.TYPE_ERROR_MESSAGE
            );
            return null
        }
    }

    boolean updateItem(Object itemId) {
        try {
            personService.updatePerson((PersonListItem) itemId)
            return true
        } catch (Exception e) {
            AddressBookApplication.application.getMainWindow().showNotification(
                    e.message,
                    Notification.TYPE_ERROR_MESSAGE
            );
            return false
        }
    }
}
