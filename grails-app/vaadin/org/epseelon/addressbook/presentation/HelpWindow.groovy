package org.epseelon.addressbook.presentation

import com.vaadin.ui.Window
import com.vaadin.ui.Label

/**
 *
 * @author sarbogast
 * @version 19/02/11, 00:58
 */
class HelpWindow extends Window {
    private static final String HELP_HTML_SNIPPET = "This is " +
            "an application built during <strong><a href=\"" +
            "http://dev.vaadin.com/\">Vaadin</a></strong> " +
            "tutorial. Hopefully it doesn't need any real help.";

    public HelpWindow() {
        setCaption("Address Book help");
        addComponent(new Label(HELP_HTML_SNIPPET, Label.CONTENT_XHTML));
    }
}
