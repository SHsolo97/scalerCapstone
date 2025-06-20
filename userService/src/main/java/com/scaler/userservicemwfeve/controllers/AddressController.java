package com.scaler.userservicemwfeve.controllers;

import com.scaler.userservicemwfeve.dtos.AddressDto;
import com.scaler.userservicemwfeve.exceptions.NotFoundException;
import com.scaler.userservicemwfeve.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * POST /api/v1/users/{userId}/addresses : Create a new address for the specified user.
     *
     * @param userId The ID of the user.
     * @param addressDto The address data transfer object.
     * @return ResponseEntity with the created AddressDto and HTTP status 201 (Created).
     * @throws NotFoundException if the user is not found.
     */
    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@PathVariable Long userId, @Valid @RequestBody AddressDto addressDto) throws NotFoundException {
        // Ensure the userId in DTO matches path, or rely on service to use path userId
        // For simplicity, we'll let the service handle setting the user from path userId
        AddressDto createdAddress = addressService.createAddress(userId, addressDto);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/users/{userId}/addresses : Get all addresses for the specified user.
     *
     * @param userId The ID of the user.
     * @return ResponseEntity with a list of AddressDto and HTTP status 200 (OK).
     * @throws NotFoundException if the user is not found.
     */
    @GetMapping
    public ResponseEntity<List<AddressDto>> getUserAddresses(@PathVariable Long userId) throws NotFoundException {
        List<AddressDto> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(addresses);
    }

    /**
     * GET /api/v1/users/{userId}/addresses/{addressId} : Get a specific address for the user.
     *
     * @param userId The ID of the user.
     * @param addressId The ID of the address.
     * @return ResponseEntity with the AddressDto and HTTP status 200 (OK).
     * @throws NotFoundException if the user or address is not found.
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDto> getUserAddressById(@PathVariable Long userId, @PathVariable Long addressId) throws NotFoundException {
        AddressDto address = addressService.getUserAddressById(userId, addressId);
        return ResponseEntity.ok(address);
    }

    /**
     * PUT /api/v1/users/{userId}/addresses/{addressId} : Update an existing address for the user.
     *
     * @param userId The ID of the user.
     * @param addressId The ID of the address to update.
     * @param addressDto The address data transfer object with updated information.
     * @return ResponseEntity with the updated AddressDto and HTTP status 200 (OK).
     * @throws NotFoundException if the user or address is not found.
     */
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long userId, 
                                                  @PathVariable Long addressId, 
                                                  @Valid @RequestBody AddressDto addressDto) throws NotFoundException {
        AddressDto updatedAddress = addressService.updateAddress(userId, addressId, addressDto);
        return ResponseEntity.ok(updatedAddress);
    }

    /**
     * DELETE /api/v1/users/{userId}/addresses/{addressId} : Delete an address for the user.
     *
     * @param userId The ID of the user.
     * @param addressId The ID of the address to delete.
     * @return ResponseEntity with HTTP status 204 (No Content).
     * @throws NotFoundException if the user or address is not found.
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long userId, @PathVariable Long addressId) throws NotFoundException {
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }
}
