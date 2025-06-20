package com.ecommerce.orderservice.services.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

@Service
public class CartServiceClient {
    private final WebClient webClient;

    public CartServiceClient(WebClient.Builder webClientBuilder,
                           @Value("${external.services.cart-service.base-url:http://localhost:8082/api/v1/cart}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<CartDetailsDto> getCartDetails(Long userId) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("/user/{userId}").build(userId))
            .retrieve()
            .bodyToMono(CartDetailsDto.class)
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
            .onErrorMap(WebClientResponseException.class, ex -> new RuntimeException("Error fetching cart for user " + userId, ex));
    }

    public Mono<Void> clearCart(Long userId) {
        return webClient.delete()
            .uri(uriBuilder -> uriBuilder.path("/user/{userId}").build(userId))
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
            .onErrorMap(WebClientResponseException.class, ex -> new RuntimeException("Error clearing cart for user " + userId, ex));
    }
}
