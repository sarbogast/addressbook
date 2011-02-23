package org.epseelon.addressbook.business

import org.epseelon.addressbook.dto.PersonListItem
import org.epseelon.addressbook.domain.Person
import grails.plugins.springsecurity.Secured

class PersonService {

    static transactional = true

    List<PersonListItem> getAllPersons() {
        return Person.findAll().collect {
            new PersonListItem(
                    id: it.id,
                    firstName: it.firstName,
                    lastName: it.lastName,
                    email: it.email,
                    phoneNumber: it.phoneNumber,
                    streetAddress: it.streetAddress,
                    postalCode: it.postalCode,
                    city: it.city
            )
        }
    }

    @Secured(["ROLE_USER"])
    PersonListItem createPerson(PersonListItem item) {
        Person p = new Person(
                firstName: item.firstName,
                lastName: item.lastName,
                email: item.email,
                phoneNumber: item.phoneNumber,
                streetAddress: item.streetAddress,
                postalCode: item.postalCode,
                city: item.city
        ).save()

        return new PersonListItem(
                firstName: p.firstName,
                lastName: p.lastName,
                email: p.email,
                phoneNumber: p.phoneNumber,
                streetAddress: p.streetAddress,
                postalCode: p.postalCode,
                city: p.city
        )
    }

    @Secured(["ROLE_USER"])
    PersonListItem updatePerson(PersonListItem item) {
        Person p = Person.get(item.id)
        if(p){
            p.firstName = item.firstName
            p.lastName = item.lastName
            p.email = item.email
            p.phoneNumber = item.phoneNumber
            p.streetAddress = item.streetAddress
            p.postalCode = item.postalCode
            p.city = item.city
            p.save()

            return new PersonListItem(
                firstName: p.firstName,
                lastName: p.lastName,
                email: p.email,
                phoneNumber: p.phoneNumber,
                streetAddress: p.streetAddress,
                postalCode: p.postalCode,
                city: p.city
            )
        }
        return null
    }
}
