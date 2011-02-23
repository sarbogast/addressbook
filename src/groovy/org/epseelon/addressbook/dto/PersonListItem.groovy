package org.epseelon.addressbook.dto

/**
 *
 * @author sarbogast
 * @version 19/02/11, 11:10
 */
class PersonListItem implements Serializable{
    Long id
    String firstName = ""
    String lastName = ""
    String email = ""
    String phoneNumber = ""
    String streetAddress = ""
    Integer postalCode = null
    String city = ""
}
