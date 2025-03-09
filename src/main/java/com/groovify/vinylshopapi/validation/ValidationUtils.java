package com.groovify.vinylshopapi.validation;

import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.DeactivatedException;
import com.groovify.vinylshopapi.exceptions.InvalidFileTypeException;
import com.groovify.vinylshopapi.models.User;
import com.groovify.vinylshopapi.repositories.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
                throw new DeactivatedException("This username is linked to a deleted account. Do you want to reactivate it?");
            } else {
                throw new ConflictException("Username " + username + " is already taken by another user. Please choose another username.");
            }
        }
    }

    public void validateUniqueEmail(String email, Long currentUserId) {
        Optional<User> existingUser = userRepository.findByEmail(email.toLowerCase());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(currentUserId)) {
            if (existingUser.get().getIsDeleted()) {
                throw new DeactivatedException("This email is linked to a deleted account. Do you want to reactivate it?", email);
            } else {
                throw new ConflictException("Email " + email + " is already in use by another user. Please choose another email.");
            }
        }
    }

    public void validateFile(MultipartFile file, List<String> allowedFileTypes) {
        if (file.isEmpty()) {
            throw new InvalidFileTypeException("Uploaded file is empty");
        }

        if (!allowedFileTypes.contains(file.getContentType())) {
            throw new InvalidFileTypeException("File type is invalid. Only JPEG and PNG files are allowed.");
        }
    }
}
