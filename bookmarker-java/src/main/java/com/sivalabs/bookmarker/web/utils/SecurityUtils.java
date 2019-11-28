package com.sivalabs.bookmarker.web.utils;

import com.sivalabs.bookmarker.config.security.SecurityUser;
import com.sivalabs.bookmarker.domain.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static User loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            return securityUser.getUser();
        }
        return null;
    }

    public static Boolean isCurrentUserAdmin() {
        User loginUser = loginUser();
        if(loginUser != null) {
            return loginUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        }
        return false;
    }

}
