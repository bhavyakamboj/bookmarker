package com.sivalabs.bookmarker.web.utils;

import com.sivalabs.bookmarker.config.security.SecurityUser;
import com.sivalabs.bookmarker.domain.entity.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {

    public static User loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            return securityUser.getUser();
        }
        return null;
    }

    public static Long loginUserId() {
        User loginUser = loginUser();
        if(loginUser != null) {
            return loginUser.getId();
        }
        return null;
    }

    public static boolean isCurrentUserAdmin() {
        User loginUser = loginUser();
        if(loginUser != null) {
            return loginUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        }
        return false;
    }

}
