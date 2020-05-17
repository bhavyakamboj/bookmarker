package com.sivalabs.bookmarker.domain.service;

import com.sivalabs.bookmarker.config.security.SecurityUser;
import com.sivalabs.bookmarker.config.security.oauth.AuthenticatedPrincipal;
import com.sivalabs.bookmarker.domain.entity.User;
import com.sivalabs.bookmarker.domain.model.BookmarkDTO;
import com.sivalabs.bookmarker.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.sivalabs.bookmarker.domain.utils.Constants.ROLE_ADMIN;
import static com.sivalabs.bookmarker.domain.utils.Constants.ROLE_MODERATOR;

@Service
@Transactional
@RequiredArgsConstructor
public class SecurityService {
    private final UserRepository userRepository;

    public User loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            return securityUser.getUser();
        } else if(authentication instanceof OAuth2AuthenticationToken) {
            Object principal = authentication.getPrincipal();
            if(principal instanceof AuthenticatedPrincipal) {
                String email = ((AuthenticatedPrincipal) principal).getEmail();
                return userRepository.findByEmail(email).orElse(null);
            }
        }
        return null;
    }

    public boolean canCurrentUserEditBookmark(BookmarkDTO bookmarkDTO) {
        User loginUser = loginUser();
        return loginUser != null && bookmarkDTO != null &&
                (Objects.equals(bookmarkDTO.getCreatedUserId(), loginUser.getId()) ||
                isCurrentUserAdminOrModerator(loginUser));
    }

    public Long loginUserId() {
        User loginUser = loginUser();
        if(loginUser != null) {
            return loginUser.getId();
        }
        return null;
    }

    public boolean isCurrentUserAdmin() {
        return isUserHasAnyRole(loginUser(), ROLE_ADMIN);
    }

    private boolean isCurrentUserAdminOrModerator() {
        return isCurrentUserAdminOrModerator(loginUser());
    }

    private boolean isCurrentUserAdminOrModerator(User loginUser) {
        return isUserHasAnyRole(loginUser, ROLE_ADMIN, ROLE_MODERATOR);
    }

    private boolean isUserHasAnyRole(User loginUser, String... roles) {
        List<String> roleList = Arrays.asList(roles);
        if(loginUser != null && loginUser.getRoles() != null) {
            return loginUser.getRoles().stream()
                    .anyMatch(role -> roleList.contains(role.getName()));
        }
        return false;
    }
}
