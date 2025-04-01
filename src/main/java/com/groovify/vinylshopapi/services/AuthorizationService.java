package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.models.Order;
import com.groovify.vinylshopapi.repositories.OrderRepository;
import com.groovify.vinylshopapi.security.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final OrderRepository orderRepository;

    public AuthorizationService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean canAccessUser(Long userId, String username, String role, Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        return isMatchingUser(user, userId, username) || hasRoleWithAccess(user, getRoleWithAccess(role));
    }

    public boolean canAccessOrder(Long orderId, String role, Authentication authentication) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RecordNotFoundException("No order found with id: " + orderId));

        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        return isMatchingUser(user, order.getCustomer().getId(), null) || hasRoleWithAccess(user, getRoleWithAccess(role));
    }

    private String getRoleWithAccess(String role) {
        if (role != null) {
            return "ROLE_" + role;
        }
        return null;
    }

    private boolean hasRoleWithAccess(SecurityUser user, String role) {
        if (user != null && role != null) {
            return user.getAuthorities().contains(new SimpleGrantedAuthority(role));
        }
        return false;
    }

    private boolean isMatchingUser(SecurityUser user, Long userId, String username) {
        return username != null ? user.getUsername().equals(username) : user.getUserId().equals(userId);
    }
}
