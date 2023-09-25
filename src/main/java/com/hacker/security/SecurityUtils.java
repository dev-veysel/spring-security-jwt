package com.hacker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {

    // !!! Controller veya Servis Katmanında anlık olarak login olan kullanıcıya
    // ulaşmak için bu classı yazdık

    //Email almak
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication =securityContext.getAuthentication();

        // wenn authentication nichts inne hat, dann schützt ofNullable mich durch Optional darin
        // NullPointException zu erhalten und sendet eine leere Optional vor!
        return Optional.ofNullable(extractPrincipal(authentication));

    }

    private static String extractPrincipal(Authentication authentication){
        if(authentication==null) {
            return null;
        } else if(authentication.getPrincipal() instanceof UserDetails) { //instanceof  -> ist links gleich wie rechts?
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } else if(authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }

        return null;
    }
}
