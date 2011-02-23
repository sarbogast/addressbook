package org.epseelon.addressbook.presentation.data

/**
 *
 * @author sarbogast
 * @version 19/02/11, 14:37
 */
class SearchFilter implements Serializable {
    final String term;
    final Object propertyId;
    String searchName;

    public SearchFilter(Object propertyId, String searchTerm, String name) {
        this.propertyId = propertyId;
        this.term = searchTerm;
        this.searchName = name;
    }

    @Override
    public String toString() {
        return searchName;
    }
}
