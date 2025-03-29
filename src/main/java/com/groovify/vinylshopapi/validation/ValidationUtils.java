package com.groovify.vinylshopapi.validation;

import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.DeactivatedException;
import com.groovify.vinylshopapi.exceptions.InsufficientStockException;
import com.groovify.vinylshopapi.exceptions.InvalidFileTypeException;
import com.groovify.vinylshopapi.models.User;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
                throw new DeactivatedException("This username is linked to a deleted account.");
            } else {
                throw new ConflictException("Username '" + username + "' is already taken by another user. Please choose another username.");
            }
        }
    }

    public void validateUniqueEmail(String email, Long currentUserId) {
        Optional<User> existingUser = userRepository.findByEmail(email.toLowerCase());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(currentUserId)) {
            if (existingUser.get().getIsDeleted()) {
                throw new DeactivatedException("This email is linked to a deleted account.", email);
            } else {
                throw new ConflictException("Email: '" + email + "' is already in use by another user. Please choose another email.");
            }
        }
    }

    public static void validateVinylRecordStock(VinylRecord vinylRecord, Integer quantity) {
        Integer amountInStock = vinylRecord.getStock().getAmountInStock();

        if (amountInStock == 0) {
            throw new InsufficientStockException("Vinyl record '" + vinylRecord.getTitle() + "' is sold out");
        }

        if (amountInStock < quantity) {
            throw new InsufficientStockException("Only " + amountInStock + " items left of '" + vinylRecord.getTitle() + "'");
        }
    }

    public static void validateFile(MultipartFile file, List<String> allowedFileTypes) {
        if (file.isEmpty()) {
            throw new InvalidFileTypeException("Uploaded file is empty");
        }

        if (!allowedFileTypes.contains(file.getContentType())) {
            throw new InvalidFileTypeException("File type is invalid. Only JPEG and PNG files are allowed.");
        }
    }


    public static LocalDate parseValidationDate(String dateString) {
        if (dateString.matches("now[+\\-]\\d+[YM]")) {
            int amountToAddOrSubtract = Integer.parseInt(dateString.substring(4, dateString.length() - 1));
            char unit = dateString.charAt(dateString.length() - 1);

            if (dateString.charAt(3) == '-') {
                amountToAddOrSubtract *= -1;
            }

            if (unit == 'Y') {
                return LocalDate.now().plusYears(amountToAddOrSubtract);
            } else if (unit == 'M') {
                return LocalDate.now().plusMonths(amountToAddOrSubtract);
            }
        }

        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format in @Valid date. Valid format is 'now+X[M/Y]' or 'DD-MM-YYYY'.");
        }
    }
}
