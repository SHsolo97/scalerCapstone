package com.scaler.userservicemwfeve.services;

import com.scaler.userservicemwfeve.dtos.AddressDto;
import com.scaler.userservicemwfeve.exceptions.NotFoundException;

import java.util.List;

public interface AddressService {
    /**
     * Creates a new address for a specified user.
     *
     * @param userId The ID of the user for whom the address is being created.
     * @param addressDto The DTO containing the details of the address to create.
     * @return The DTO of the newly created address.
     * @throws NotFoundException if the user with the given ID is not found.
     */
    AddressDto createAddress(Long userId, AddressDto addressDto) throws NotFoundException;

    /**
     * Retrieves all addresses associated with a specific user.
     *
     * @param userId The ID of the user whose addresses are to be retrieved.
     * @return A list of AddressDto objects.
     * @throws NotFoundException if the user with the given ID is not found.
     */
    List<AddressDto> getUserAddresses(Long userId) throws NotFoundException;

    /**
     * Retrieves a specific address by its ID, ensuring it belongs to the specified user.
     *
     * @param userId The ID of the user.
     * @param addressId The ID of the address to retrieve.
     * @return The AddressDto of the found address.
     * @throws NotFoundException if the user or the address is not found, or if the address does not belong to the user.
     */
    AddressDto getUserAddressById(Long userId, Long addressId) throws NotFoundException;

    /**
     * Updates an existing address for a specified user.
     *
     * @param userId The ID of the user whose address is being updated.
     * @param addressId The ID of the address to update.
     * @param addressDto The DTO containing the updated address details.
     * @return The DTO of the updated address.
     * @throws NotFoundException if the user or the address is not found, or if the address does not belong to the user.
     */
    AddressDto updateAddress(Long userId, Long addressId, AddressDto addressDto) throws NotFoundException;

    /**
     * Deletes an address for a specified user.
     *
     * @param userId The ID of the user whose address is being deleted.
     * @param addressId The ID of the address to delete.
     * @throws NotFoundException if the user or the address is not found, or if the address does not belong to the user.
     */
    void deleteAddress(Long userId, Long addressId) throws NotFoundException;
}
