package org.epseelon.addressbook.business

import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class SecurityService {

    static transactional = true

    def springSecurityService
    def authenticationManager

    void signIn(String username, String password) {
        try {
            def authentication = new UsernamePasswordAuthenticationToken(username, password)
            SCH.context.authentication = authenticationManager.authenticate(authentication)
        } catch (BadCredentialsException e) {
            throw new SecurityException("Invalid username/password")
        }
    }

    void signOut(){
        SCH.context.authentication = null
    }

    boolean isSignedIn(){
        return springSecurityService.isLoggedIn()
    }
}
