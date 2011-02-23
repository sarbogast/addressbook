package org.epseelon.addressbook.presentation

import com.vaadin.ui.Window
import com.vaadin.ui.Button
import com.vaadin.ui.CheckBox
import com.vaadin.ui.Label

/**
 * 
 * @author sarbogast
 * @version 19/02/11, 00:56
 */
class SharingOptions extends Window {
    public SharingOptions() {
         /*
          * Make the window modal, which will disable all other components while
          * it is visible
          */
         setModal(true);
        /* Make the sub window 50% the size of the browser window */
         setWidth("50%");
         /*
          * Center the window both horizontally and vertically in the browser
          * window
          */
         center();

         setCaption("Sharing options");
         addComponent(new Label(
                 "With these setting you can modify contact sharing "
                         + "options. (non-functional, example of modal dialog)"));
         addComponent(new CheckBox("Gmail"));
         addComponent(new CheckBox(".Mac"));
         Button close = new Button("OK");
         addComponent(close);
     }
}
