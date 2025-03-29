package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.exceptions.InvalidTokenException;
import com.groovify.vinylshopapi.exceptions.MissingTokenException;
import com.groovify.vinylshopapi.payload.AuthRequestDTO;
import com.groovify.vinylshopapi.payload.AuthResponseDTO;
import com.groovify.vinylshopapi.security.CustomUserDetailsService;
import com.groovify.vinylshopapi.security.JwtProvider;
import com.groovify.vinylshopapi.security.SecurityUser;
import com.groovify.vinylshopapi.security.UserDetailsResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;

    public AuthController(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtProvider jwtProvider
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponseDTO> getCurrentUser(
        Authentication authentication
    ) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        UserDetailsResponseDTO userDetails = new UserDetailsResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
        );
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody AuthRequestDTO authRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getUsername(),
                        authRequestDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(userDetails.getUsername());
        final String token = jwtProvider.generateToken(userDetails, user.getUserId());

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }


    @GetMapping("/validate-token")
    public ResponseEntity<AuthResponseDTO> validateToken(
            @RequestHeader("Authorization") String authorizationHeader,
            Authentication authentication
    ) {
        String token = extractTokenFromHeader(authorizationHeader);
        if (token == null) {
            throw new MissingTokenException("Authorization header is missing a bearer token");
        }

        if (!jwtProvider.validateToken(token, (UserDetails) authentication.getPrincipal())) {
            throw new InvalidTokenException("Token is invalid");
        }

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshToken(
            @RequestHeader("Authorization") String authorizationHeader,
            Authentication authentication
    ) {
        String token = extractTokenFromHeader(authorizationHeader);
        if (token == null) {
            throw new MissingTokenException("Authorization header is missing a bearer token");
        }

        String refreshedToken = jwtProvider.refreshToken(token, (UserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(new AuthResponseDTO(refreshedToken));
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
