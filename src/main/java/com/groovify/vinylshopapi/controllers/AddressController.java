package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.services.AddressService;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

}
