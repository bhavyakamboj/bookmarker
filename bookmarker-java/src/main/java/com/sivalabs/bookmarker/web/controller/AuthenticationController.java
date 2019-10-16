package com.sivalabs.bookmarker.web.controller;

import com.sivalabs.bookmarker.config.BookmarkerProperties;
import com.sivalabs.bookmarker.config.security.CustomUserDetailsService;
import com.sivalabs.bookmarker.config.security.SecurityUser;
import com.sivalabs.bookmarker.config.security.TokenHelper;
import com.sivalabs.bookmarker.domain.entity.User;
import com.sivalabs.bookmarker.domain.model.AuthenticationRequest;
import com.sivalabs.bookmarker.domain.model.AuthenticationResponse;
import com.sivalabs.bookmarker.domain.model.UserDTO;
import com.sivalabs.bookmarker.web.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final TokenHelper tokenHelper;
    private final BookmarkerProperties bookmarkerProperties;

    public AuthenticationController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, TokenHelper tokenHelper, BookmarkerProperties bookmarkerProperties) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenHelper = tokenHelper;
        this.bookmarkerProperties = bookmarkerProperties;
    }

    @PostMapping(value = "/auth/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        String jws = tokenHelper.generateToken(user.getUsername());
        return new AuthenticationResponse(jws, bookmarkerProperties.getJwt().getExpiresIn());
    }

    @PostMapping(value = "/auth/refresh")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<AuthenticationResponse> refreshAuthenticationToken(HttpServletRequest request) {
        String authToken = tokenHelper.getToken(request);
        if (authToken != null) {
            String email = tokenHelper.getUsernameFromToken(authToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Boolean validToken = tokenHelper.validateToken(authToken, userDetails);
            if (validToken) {
                String refreshedToken = tokenHelper.refreshToken(authToken);
                return ResponseEntity.ok(
                        new AuthenticationResponse(
                                refreshedToken,
                                bookmarkerProperties.getJwt().getExpiresIn()
                        )
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDTO> me() {
        User loginUser = SecurityUtils.loginUser();
        if(loginUser != null) {
            return ResponseEntity.ok(UserDTO.fromEntity(loginUser));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
