package com.groovify.vinylshopapi.config;

import com.groovify.vinylshopapi.security.CustomAuthenticationEntryPoint;
import com.groovify.vinylshopapi.security.CustomUserDetailsService;
import com.groovify.vinylshopapi.security.JwtAuthenticationFilter;
import com.groovify.vinylshopapi.services.AuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthorizationService authorizationService;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            AuthorizationService authorizationService,
            CustomUserDetailsService customUserDetailsService,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.authorizationService = authorizationService;
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //Swagger
                        .requestMatchers("/api-docs/**", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()

                        //Auth endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/customers").permitAll()

                        // Vinyl Record endpoints
                        .requestMatchers(HttpMethod.GET, "/api/vinyl-records", "/api/vinyl-records/{id}", "/api/vinyl-records/title/{title}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/vinyl-records", "/api/vinyl-records/{id}/artist/{artistId}").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/vinyl-records/{id}").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, "/api/vinyl-records/{id}").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/api/vinyl-records/{id}", "/api/vinyl-records/{id}/**").hasRole("ADMIN")

                        // Vinyl Record Stock endpoints
                        .requestMatchers(HttpMethod.GET, "/api/vinyl-records/{id}/stock").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/vinyl-records/{id}/stock").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/vinyl-records/{id}/stock").hasRole("EMPLOYEE")

                        // Vinyl Record Cover endpoints
                        .requestMatchers(HttpMethod.GET, "/api/vinyl-records/{id}/cover").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/vinyl-records/{id}/cover").hasRole("EMPLOYEE")

                        // Artist endpoints
                        .requestMatchers(HttpMethod.GET, "/api/artists", "/api/artists/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/artists").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/artists/{id}").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, "/api/artists/{id}").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/api/artists/{id}").hasRole("EMPLOYEE")

                        // Role endpoints
                        .requestMatchers("/api/roles", "/api/users/{id}/roles").hasRole("ADMIN")

                        // User endpoints
                        .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/{id}/deactivate", "/api/users/{id}/reactivate")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, "ADMIN", true))
                        .requestMatchers(HttpMethod.PATCH, "/api/users/{id}/change-password")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, null, true))

                        // Customer endpoints
                        .requestMatchers(HttpMethod.GET, "/api/customers").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/customers/{id}")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, "EMPLOYEE", true))
                        .requestMatchers(HttpMethod.GET, "/api/customers/username/{username}")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, "EMPLOYEE", false))
                        .requestMatchers(HttpMethod.GET, "/api/customers/{id}/**")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, "ADMIN", true))
                        .requestMatchers(HttpMethod.POST, "/api/customers/{id}/**")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, null, true))
                        .requestMatchers(HttpMethod.PUT, "/api/customers/{id}", "/api/customers/{id}/**")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, null, true))
                        .requestMatchers(HttpMethod.PATCH, "/api/customers/{id}/**")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, null, true))
                        .requestMatchers(HttpMethod.DELETE, "/api/customers/{id}/**")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, null, true))

                        // Employee endpoints
                        .requestMatchers("/api/employees", "/api/employees/{id}/admin").hasRole("ADMIN")
                        .requestMatchers("/api/employees/{id}", "/api/employees/{id}/address")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, "ADMIN", true))
                        .requestMatchers(HttpMethod.GET, "/api/employees/username/{username}")
                            .access((authentication, context) ->
                                    canAccessUser(authentication.get(), context, "ADMIN", false))

                        // Address endpoints
                        .requestMatchers(HttpMethod.GET, "/api/addresses").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/addresses/customers").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/addresses").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/addresses/{id}").hasRole("USER")

                        // Cart endpoints
                        .requestMatchers("/api/carts", "/api/carts/{id}").hasRole("ADMIN")

                        // Order endpoints
                        .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/{id}").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/orders/{id}/invoice")
                            .access((authentication, context) ->
                                    canAccessOrder(authentication.get(), context, "EMPLOYEE"))
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/{id}")
                            .access((authentication, context) ->
                                    canAccessOrder(authentication.get(), context, null))
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/{id}/status")
                            .access((authentication, context) ->
                                    canAccessOrder(authentication.get(), context, "EMPLOYEE"))
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/{id}")
                            .access((authentication, context) ->
                                    canAccessOrder(authentication.get(), context, null))
                        .requestMatchers("/api/orders/{id}/deactivate", "/api/orders/{id}/reactivate").hasRole("ADMIN")

                        // Dashboard endpoints
                        .requestMatchers(HttpMethod.GET, "/api/dashboard").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/dashboard/bestsellers").hasRole("USER")

                        .anyRequest().denyAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAuthenticationEntryPoint)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
    throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthorizationDecision canAccessUser(Authentication authentication, RequestAuthorizationContext context, String role, Boolean pathVariableIsId) {
        String path = context.getRequest().getRequestURI();
        Long userId = null;
        String username = null;

        if (pathVariableIsId) {
            userId = extractIdFromPath(path);
        } else {
            username = extractUsernameFromPath(path);
        }

        return new AuthorizationDecision(authorizationService.canAccessUser(userId, username, role, authentication));
    }

    private AuthorizationDecision canAccessOrder(Authentication authentication, RequestAuthorizationContext context, String role) {
        String path = context.getRequest().getRequestURI();
        Long orderId = extractIdFromPath(path);

        return new AuthorizationDecision(authorizationService.canAccessOrder(orderId, role, authentication));
    }

    private Long extractIdFromPath(String path) {
        String[] pathParts = path.split("/");
        if (pathParts.length >= 4) {
            try {
                return Long.parseLong(pathParts[3]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid ID format in URL path.");
            }
        } else {
            throw new IllegalArgumentException("Invalid URL structure.");
        }
    }

    private String extractUsernameFromPath(String path) {
        String[] pathParts = path.split("/");
        if (pathParts.length >= 4) {
            return pathParts[4];
        } else {
            throw new IllegalArgumentException("Invalid URL structure.");
        }
    }
}
