package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Street is required")
    @Size(max = 255, message = "Street cannot be longer than 255 characters")
    private String street;

    @NotBlank(message = "House number is required")
    @Pattern(regexp = "^[0-9]+[A-Za-z]*$", message = "House number must be a number followed by an optional letter")
    private String houseNumber;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot be longer than 100 characters")
    private String city;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^(?:[0-9]{4}[A-Za-z]{2}|[0-9]{4})$", message = "Postal code must follow the pattern '1234AB' (NL) or '1234' (BE)")
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot be longer than 100 characters")
    private String country;

    private Boolean isShippingAddress;

    private Boolean isBillingAddress;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "employee_id", unique = true)
    private Employee employee;

    @OneToMany(mappedBy = "shippingAddress")
    private List<Order> shippingOrders = new ArrayList<>();

    @OneToMany(mappedBy = "billingAddress")
    private List<Order> billingOrders = new ArrayList<>();


    public boolean isOrderAddress() {
        return !shippingOrders.isEmpty() || !billingOrders.isEmpty();
    }

    public boolean isStandAlone() {
        return customer == null && employee == null && !isOrderAddress();
    }
}
