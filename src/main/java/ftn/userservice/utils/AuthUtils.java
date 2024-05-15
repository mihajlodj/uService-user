package ftn.userservice.utils;

import ftn.userservice.exception.exceptions.AuthorizationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.UUID;

public class AuthUtils {

    public static String getLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (String) authentication.getPrincipal();
        }

        throw new AuthorizationException("Not logged in");
    }

    public static UUID getLoggedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object details = authentication.getDetails();
            if (details instanceof Map<?,?> detailsMap) {
                Object userId = detailsMap.get("userId");
                if (userId instanceof UUID) {
                    return (UUID) userId;
                }
            }
        }

        throw new AuthorizationException("Not logged in");
    }

}
