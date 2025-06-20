package com.ecommerce.orderservice.services.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

@Service
public class ProductCatalogServiceClient {
    private final WebClient webClient;

    public ProductCatalogServiceClient(WebClient.Builder webClientBuilder,
                                     @Value("${external.services.product-catalog-service.base-url:http://localhost:8081/api/v1/products}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<ProductDetailsDto> getProductDetails(Long productId) {
        return webClient.get()
            .uri("/{productId}", productId)
            .retrieve()
            .bodyToMono(ProductDetailsDto.class)
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
            .onErrorMap(WebClientResponseException.class, ex -> new RuntimeException("Error fetching product " + productId, ex));
    }
}
