package com.groovify.vinylshopapi.validation;

import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.DeactivedException;
import com.groovify.vinylshopapi.models.User;
import com.groovify.vinylshopapi.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidationUtils {

    private final UserRepository userRepository;

    public ValidationUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUniqueUsername(String username, Long currentUserId) {
        Optional<User> existingUser = userRepository.findByUsername(username.toLowerCase());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(currentUserId)) {
            if (existingUser.get().getIsDeleted()) {
                throw new DeactivedException("This username is linked to a deleted account. Do you want to reactivate it?");
            } else {
                throw new ConflictException("Username " + username + " is already taken by another user. Please choose another username.");
            }
        }
    }

    public void validateUniqueEmail(String email, Long currentUserId) {
        Optional<User> existingUser = userRepository.findByEmail(email.toLowerCase());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(currentUserId)) {
            if (existingUser.get().getIsDeleted()) {
                throw new DeactivedException("This email is linked to a deleted account. Do you want to reactivate it?", email);
            } else {
                throw new ConflictException("Email " + email + " is already in use by another user. Please choose another email.");
            }
        }
    }
}
