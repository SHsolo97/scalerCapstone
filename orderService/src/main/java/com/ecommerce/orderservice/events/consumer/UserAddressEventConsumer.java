package com.ecommerce.orderservice.events.consumer;

import com.ecommerce.orderservice.models.User;
import com.ecommerce.orderservice.models.Address;
import com.ecommerce.orderservice.repositories.UserRepository;
import com.ecommerce.orderservice.repositories.AddressRepository;
import com.ecommerce.orderservice.events.model.UserEventPayload;
import com.ecommerce.orderservice.events.model.AddressEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAddressEventConsumer {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "UserCreatedEvent", groupId = "order-service")
    @Transactional
    public void handleUserCreated(String userJson) {
        try {
            UserEventPayload payload = objectMapper.readValue(userJson, UserEventPayload.class);
            User user = new User();
            user.setId(payload.getId());
            user.setEmail(payload.getEmail());
            user.setName(payload.getName());
            user.setPhone(payload.getPhone());
            userRepository.save(user);
            log.info("User created/replicated locally: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to process userCreated event", e);
        }
    }

    @KafkaListener(topics = "UserUpdatedEvent", groupId = "order-service")
    @Transactional
    public void handleUserUpdated(String userJson) {
        try {
            UserEventPayload payload = objectMapper.readValue(userJson, UserEventPayload.class);
            User user = userRepository.findById(payload.getId()).orElse(new User());
            user.setId(payload.getId());
            user.setEmail(payload.getEmail());
            user.setName(payload.getName());
            user.setPhone(payload.getPhone());
            userRepository.save(user);
            log.info("User updated/replicated locally: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to process userUpdated event", e);
        }
    }

    @KafkaListener(topics = "AddressCreatedEvent", groupId = "order-service")
    @Transactional
    public void handleAddressAdded(String addressJson) {
        try {
            AddressEventPayload payload = objectMapper.readValue(addressJson, AddressEventPayload.class);
            Address address = new Address();
            address.setId(payload.getId());
            address.setStreet(payload.getStreet());
            address.setApartment(payload.getApartment());
            address.setCity(payload.getCity());
            address.setState(payload.getState());
            address.setPostalCode(payload.getPostalCode());
            address.setCountry(payload.getCountry());
            address.setAddressType(payload.getAddressType());
            address.setUser(userRepository.findById(payload.getUserId()).orElse(null));
            addressRepository.save(address);
            log.info("Address added/replicated locally: {}", address.getId());
        } catch (Exception e) {
            log.error("Failed to process AddressAdded event", e);
        }
    }

    @KafkaListener(topics = "AddressUpdatedEvent", groupId = "order-service")
    @Transactional
    public void handleAddressUpdated(String addressJson) {
        try {
            AddressEventPayload payload = objectMapper.readValue(addressJson, AddressEventPayload.class);
            Address address = addressRepository.findById(payload.getId()).orElse(new Address());
            address.setId(payload.getId());
            address.setStreet(payload.getStreet());
            address.setApartment(payload.getApartment());
            address.setCity(payload.getCity());
            address.setState(payload.getState());
            address.setPostalCode(payload.getPostalCode());
            address.setCountry(payload.getCountry());
            address.setAddressType(payload.getAddressType());
            address.setUser(userRepository.findById(payload.getUserId()).orElse(null));
            addressRepository.save(address);
            log.info("Address updated/replicated locally: {}", address.getId());
        } catch (Exception e) {
            log.error("Failed to process AddressUpdated event", e);
        }
    }
}
