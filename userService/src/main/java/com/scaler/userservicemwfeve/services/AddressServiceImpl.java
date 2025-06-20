package com.scaler.userservicemwfeve.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.userservicemwfeve.dtos.AddressDto;
import com.scaler.userservicemwfeve.events.AddressCreatedEvent;
import com.scaler.userservicemwfeve.events.AddressUpdatedEvent;
import com.scaler.userservicemwfeve.exceptions.NotFoundException;
import com.scaler.userservicemwfeve.models.Address;
import com.scaler.userservicemwfeve.models.User;
import com.scaler.userservicemwfeve.repositories.AddressRepository;
import com.scaler.userservicemwfeve.repositories.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository, KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public AddressDto createAddress(Long userId, AddressDto addressDto) throws NotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Address address = mapToEntity(addressDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);

        // Produce Kafka event
        AddressCreatedEvent event = new AddressCreatedEvent();
        event.setAddressId(savedAddress.getId());
        event.setUserId(userId);
        event.setStreet(savedAddress.getStreet());
        event.setApartment(savedAddress.getApartment());
        event.setCity(savedAddress.getCity());
        event.setState(savedAddress.getState());
        event.setPostalCode(savedAddress.getPostalCode());
        event.setCountry(savedAddress.getCountry());
        event.setAddressType(savedAddress.getAddressType());
        kafkaTemplate.send("address.events", event);

        return mapToDto(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDto> getUserAddresses(Long userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDto getUserAddressById(Long userId, Long addressId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new NotFoundException("Address not found with id: " + addressId + " for user: " + userId));
        return mapToDto(address);
    }

    @Override
    @Transactional
    public AddressDto updateAddress(Long userId, Long addressId, AddressDto addressDto) throws NotFoundException {
        Address existingAddress = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new NotFoundException("Address not found with id: " + addressId + " for user: " + userId));

        // Update fields
        existingAddress.setStreet(addressDto.getStreet());
        existingAddress.setApartment(addressDto.getApartment());
        existingAddress.setCity(addressDto.getCity());
        existingAddress.setState(addressDto.getState());
        existingAddress.setPostalCode(addressDto.getPostalCode());
        existingAddress.setCountry(addressDto.getCountry());
        existingAddress.setAddressType(addressDto.getAddressType());
        // User remains the same

        Address updatedAddress = addressRepository.save(existingAddress);

        // Produce Kafka event
        AddressUpdatedEvent event = new AddressUpdatedEvent();
        event.setAddressId(updatedAddress.getId());
        event.setUserId(userId);
        event.setStreet(updatedAddress.getStreet());
        event.setApartment(updatedAddress.getApartment());
        event.setCity(updatedAddress.getCity());
        event.setState(updatedAddress.getState());
        event.setPostalCode(updatedAddress.getPostalCode());
        event.setCountry(updatedAddress.getCountry());
        event.setAddressType(updatedAddress.getAddressType());
        kafkaTemplate.send("address.events", event);

        return mapToDto(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new NotFoundException("Address not found with id: " + addressId + " for user: " + userId));
        
        // Soft delete by setting deleted flag (assuming BaseModel has 'deleted' field and @Where clause)
        // address.setDeleted(true); 
        // addressRepository.save(address);
        // For hard delete:
        addressRepository.delete(address);
    }

    private AddressDto mapToDto(Address address) {
        if (address == null) return null;
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setApartment(address.getApartment());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setAddressType(address.getAddressType());
        if (address.getUser() != null) {
            dto.setUserId(address.getUser().getId());
        }
        return dto;
    }

    private Address mapToEntity(AddressDto dto) {
        if (dto == null) return null;
        Address address = new Address();
        // ID is not set from DTO for new entities
        address.setStreet(dto.getStreet());
        address.setApartment(dto.getApartment());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setAddressType(dto.getAddressType());
        // User will be set separately
        return address;
    }
}
